package com.work.shop.oms.distribute.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.work.shop.oms.common.utils.CachedBeanCopier;
import com.work.shop.oms.common.utils.NumberUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.ship.bean.WkUdDistribute;
import com.work.shop.oms.bean.DistributeAction;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDepotShipExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderPickingList;
import com.work.shop.oms.bean.OrderPickingListDetails;
import com.work.shop.oms.bean.OrderPickingListDetailsExample;
import com.work.shop.oms.bean.OrderPickingListExample;
import com.work.shop.oms.bean.SystemRegionArea;
import com.work.shop.oms.bean.SystemRegionAreaExample;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.config.service.SystemShippingService;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderPickingListDetailsMapper;
import com.work.shop.oms.dao.OrderPickingListMapper;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.distribute.service.NoticeDistributeService;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.orderop.service.JmsSendQueueService;
import com.work.shop.oms.rider.service.RiderDistributeService;
import com.work.shop.oms.ship.service.WkUdDistributeService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.MqConfig;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单交货单通知服务
 * @author QuYachu
 */
@Service("noticeDistributeService")
public class NoticeDistributeServiceImpl implements NoticeDistributeService {

	Logger logger = Logger.getLogger(NoticeDistributeServiceImpl.class);

	@Resource
	WkUdDistributeService wkUdDistributeService;
	@Resource
	DistributeActionService distributeActionService;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private OrderDepotShipMapper orderDepotShipMapper;
	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
	@Resource
	private SystemShippingService systemShippingService;
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	@Resource
	private OrderPickingListMapper orderPickingListMapper;
	@Resource
	private OrderPickingListDetailsMapper orderPickingListDetailsMapper;
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	@Resource
	private SystemRegionAreaMapper systemRegionAreaMapper;
	@Resource
	private RiderDistributeService riderDistributeService;
	@Resource
	private JmsSendQueueService jmsSendQueueService;

	/**
	 * 处理订单分配结果
	 * @param orderSn
	 * @param wkUdDistributeList
	 * @param isSystem 是否系统分仓 true:是;false:否
	 */
	@Override
	public void processDistribute(String orderSn, final List<WkUdDistribute> wkUdDistributeList, boolean isSystem) {
		logger.info("订单分配回写：orderSn=" + orderSn + ";wkUdDistributeList=" + JSON.toJSONString(wkUdDistributeList));

		OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
		if (distribute == null) {
			logger.error("交货单[" + orderSn + "]不存在");
			return;
		}

		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
		if (master == null) {
			logger.error("订单[" + distribute.getMasterOrderSn() + "]不存在");
			return;
		}

		DistributeAction orderAction = createOrderAction(distribute);
		if (!isSystem) {
			orderAction.setActionNote("已分仓(手工同步)");
		}
		if (checkOrderStatus(distribute)) {
			// 添加分仓日志
			orderAction.setActionNote("订单失效无法分仓");
			distributeActionService.saveOrderAction(orderAction);
			return;
		}
		logger.debug("订单[" + orderSn + "]开始分仓,count:" + wkUdDistributeList.size());
		if (wkUdDistributeList.size() < 1) {
			logger.debug("订单[" + orderSn + "]ERP未分仓，OMS分仓结束");
			return;
		}
		// 设置订单发货状态为未发货
		updateOrderShippingStatus(orderSn);
		List<OrderDepotShip> nowOrderShipList = selectAllDepot(orderSn);
		logger.debug("订单[" + orderSn + "]开始分仓,原始数据:" + JSON.toJSONString(nowOrderShipList));
		// 已经发货的depot
		final List<String> depotCodes = findShippedShipSn(nowOrderShipList);
		logger.debug("订单[" + orderSn + "]已发货的shipSn" + depotCodes);

		// 创建填充orderDepot
		List<OrderDepotShip> orderDepotShips = processOrderDepotShip(wkUdDistributeList, orderSn, nowOrderShipList, depotCodes);
		logger.debug("订单[" + orderSn + "] orderDepotShips:" + orderDepotShips);

		// 处理OrderGoods
		List<MasterOrderGoods> orderGoodsList = processOrderGoods(orderSn, wkUdDistributeList, orderDepotShips, depotCodes);
		logger.debug("订单[" + orderSn + "]OrderGoods:" + orderGoodsList);
		
		// 分配成功后创建拣货单
		createPickingList(orderDepotShips, wkUdDistributeList, distribute.getMasterOrderSn(), master);

		// 处理骑手配送
        //processTaskOutDistribute(master);

		// 设置订单成已经分仓状态
		refreshOrderStatus(orderSn);
		// 添加分仓日志
		distributeActionService.saveOrderAction(orderAction);
	}

    /**
     * 处理骑手配送
     * @param master
     */
	private void processTaskOutDistribute(MasterOrderInfo master) {
	    Integer source = master.getSource();
		if (source == null) {
			source = 0;
		}

		// 如果不是B2B订单
		if (source != 6) {
			// 分配成功后通知骑手配送
			List<String> orderSnList = new ArrayList<String>();
			orderSnList.add(master.getMasterOrderSn());
			ServiceReturnInfo<Boolean> serviceReturnInfo = riderDistributeService.saveRiderDistributeInfoList(orderSnList, 1);
			// 通知起手配送成功后打印配送单
			if (serviceReturnInfo.isIsok()) {
				Map<String, Object> distMap = new HashMap<>();
				distMap.put("channelCode", master.getChannelCode());
				distMap.put("orderFrom", master.getOrderFrom());
				distMap.put("masterOrderSn", master.getMasterOrderSn());
				jmsSendQueueService.sendQueueMessage(MqConfig.cloud_center_orderDistribute, JSON.toJSONString(distMap));
			}
		}
    }
	
	/**
	 * 生成拣货单
	 * @param orderDepotShips
	 * @param wkUdDistributeList
	 * @param orderSn
	 * @param master
	 */
	private void createPickingList(List<OrderDepotShip> orderDepotShips, List<WkUdDistribute> wkUdDistributeList, String orderSn, MasterOrderInfo master) {
		MasterOrderAddressInfo address = masterOrderAddressInfoMapper.selectByPrimaryKey(orderSn);
		if (address == null) {
			return ;
		}
		Map<String, String> systemRegionMap = new HashMap<String, String>();
		Set<String> areaIdList = new HashSet<String>();
		areaIdList.add(address.getCountry());
		areaIdList.add(address.getProvince());
		areaIdList.add(address.getCity());
		areaIdList.add(address.getDistrict());
		SystemRegionAreaExample systemRegionAreaExample = new SystemRegionAreaExample();
		systemRegionAreaExample.or().andRegionIdIn(new ArrayList<String>(areaIdList));
		List<SystemRegionArea> systemRegionAreaList = systemRegionAreaMapper.selectByExample(systemRegionAreaExample);
		if(CollectionUtils.isEmpty(systemRegionAreaList)){
			logger.error("订单[" + orderSn + "]收货地址信息OMS地区信息表中不存在");
			return ;
		}
		for (SystemRegionArea systemRegionArea : systemRegionAreaList) {
			systemRegionMap.put(systemRegionArea.getRegionId(), systemRegionArea.getRegionName());
		}
		
		Map<String, OrderPickingList> orderPickingListMap = new HashMap<String, OrderPickingList>();
		if (StringUtil.isListNotNull(orderDepotShips)) {
			for (OrderDepotShip depotShip : orderDepotShips) {
				OrderPickingList pickingList = new OrderPickingList();
				pickingList.setAddress(address.getAddress());
				pickingList.setApprovalStatus((byte) 1);
				pickingList.setChannelCode(master.getOrderFrom());
				pickingList.setCountry(systemRegionMap.get(address.getCountry()));
				pickingList.setProvince(systemRegionMap.get(address.getProvince()));
				pickingList.setCity(systemRegionMap.get(address.getCity()));
				pickingList.setConsignee(address.getConsignee());
				pickingList.setDistrict(systemRegionMap.get(address.getDistrict()));
				pickingList.setCreateTime(new Date());
				pickingList.setCreateUser(Constant.OS_STRING_SYSTEM);
				pickingList.setDepotNo(depotShip.getDepotCode());
				pickingList.setEmail(address.getEmail());
				pickingList.setMobile(address.getMobile());
				pickingList.setOrderNo(orderSn);
				pickingList.setTel(address.getTel());
				pickingList.setUserName(master.getUserName());
				pickingList.setZipcode(address.getZipcode());
				orderPickingListMap.put(depotShip.getDepotCode(), pickingList);
			}
		}
		Map<String, List<OrderPickingListDetails>> detailsListMap = new HashMap<String, List<OrderPickingListDetails>>();
		if (StringUtil.isListNotNull(wkUdDistributeList)) {
			for (WkUdDistribute udDistribute : wkUdDistributeList) {
				String customCode = udDistribute.getProdId();
				String depotCode = udDistribute.getDistWarehCode();
				MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
				goodsExample.or().andMasterOrderSnEqualTo(orderSn).andCustomCodeEqualTo(customCode);
				List<MasterOrderGoods> list = masterOrderGoodsMapper.selectByExample(goodsExample);
				if (StringUtil.isListNull(list)) {
					continue;
				}
				MasterOrderGoods goods = list.get(0);
				OrderPickingListDetails details = new OrderPickingListDetails();
				details.setDepotNo(udDistribute.getDistWarehCode());
				details.setGoodsName(goods.getGoodsName());
				details.setGoodsColorName(goods.getGoodsColorName());
				details.setGoodsSizeName(goods.getGoodsSizeName());
				details.setGoodsNumber(Integer.valueOf(udDistribute.getGoodsNumber()));
				details.setOrderNo(orderSn);
				details.setSku(customCode);
//				details.setPickingListNo(pickingListNo);
				List<OrderPickingListDetails> listDetails = detailsListMap.get(depotCode);
				if (StringUtil.isListNull(listDetails)) {
					listDetails = new ArrayList<OrderPickingListDetails>();
				}
				listDetails.add(details);
				detailsListMap.put(depotCode, listDetails);
			}
		}
		if (!orderPickingListMap.isEmpty()) {
			for (String depotCode : orderPickingListMap.keySet()) {
				OrderPickingList pickingList = orderPickingListMap.get(depotCode);
				if (pickingList != null) {
					OrderPickingListExample pickingListExample = new OrderPickingListExample();
					pickingListExample.or().andOrderNoEqualTo(orderSn).andDepotNoEqualTo(depotCode);
					List<OrderPickingList> pickingLists = orderPickingListMapper.selectByExample(pickingListExample);
					if (StringUtil.isListNull(pickingLists)) {
						orderPickingListMapper.insertSelective(pickingList);
					}
				}
				OrderPickingListExample pickingListExample = new OrderPickingListExample();
				pickingListExample.or().andOrderNoEqualTo(orderSn).andDepotNoEqualTo(depotCode);
				List<OrderPickingList> pickingLists = orderPickingListMapper.selectByExample(pickingListExample);
				if (StringUtil.isListNotNull(pickingLists)) {
					List<OrderPickingListDetails> listDetails = detailsListMap.get(depotCode);
					if (StringUtil.isListNotNull(listDetails)) {
						for (OrderPickingListDetails details : listDetails) {
							String sku = details.getSku();
							OrderPickingListDetailsExample listDetailsExample = new OrderPickingListDetailsExample();
							listDetailsExample.or().andOrderNoEqualTo(orderSn).andDepotNoEqualTo(depotCode).andSkuEqualTo(sku);
							List<OrderPickingListDetails> detailsList = orderPickingListDetailsMapper.selectByExample(listDetailsExample);
							if (StringUtil.isListNull(detailsList)) {
								details.setPickingListNo(pickingLists.get(0).getId());
								orderPickingListDetailsMapper.insertSelective(details);
							}
						}
					}
				}
			}
		}
	}

    /**
     * 设置发货单、交货单发货状态为未发货
     * @param orderSn
     */
	private void updateOrderShippingStatus(String orderSn) {
		logger.debug("设置订单" + orderSn + "发货状态为未发货");
		OrderDepotShip depotShip = new OrderDepotShip();
		depotShip.setShippingStatus((byte) Constant.OI_SHIP_STATUS_UNSHIPPED);
		OrderDepotShipExample shipExample = new OrderDepotShipExample();
		shipExample.or().andOrderSnEqualTo(orderSn);
		orderDepotShipMapper.updateByExampleSelective(depotShip, shipExample);

		OrderDistribute od = new OrderDistribute();
		od.setShipStatus((byte) Constant.OI_SHIP_STATUS_UNSHIPPED);
		od.setOrderSn(orderSn);
		orderDistributeMapper.updateByPrimaryKeySelective(od);
	}

    /**
     * 获取交货单下发货单列表
     * @param orderSn
     * @return List<OrderDepotShip>
     */
	public List<OrderDepotShip> selectAllDepot(String orderSn) {
		OrderDepotShipExample example = new OrderDepotShipExample();
		example.or().andOrderSnEqualTo(orderSn);
		return orderDepotShipMapper.selectByExample(example);
	}

    /**
     * 更新交货单已分仓已通知状态
     * @param orderSn
     */
	public void refreshOrderStatus(String orderSn) {
		OrderDistribute distribute = new OrderDistribute();
		distribute.setOrderSn(orderSn);
		// depot_status 分仓发货状态（0，未分仓 1，已分仓未通知 2，已分仓已通知）
		distribute.setDepotStatus((byte)Constant.OI_DEPOT_STATUS_DEPOTED_NOTICED);
		orderDistributeMapper.updateByPrimaryKeySelective(distribute);
	}

	/**
	 * 处理发货商品
	 * 
	 * @param orderSn
	 * @param shipProviderList
	 * @param orderShipList
     * @param depotCodeList
	 */
	private List<MasterOrderGoods> processOrderGoods(String orderSn, List<WkUdDistribute> shipProviderList, List<OrderDepotShip> orderShipList, List<String> depotCodeList) {

	    // 获取交货单下的商品列表
		final List<MasterOrderGoods> baseOrderGoodsList = selectByExample(orderSn);

		// 设置交货单下不同发货仓的商品列表
		final List<MasterOrderGoods> oldOrderGoodsList = groupOrderGoodsBySkuExtCode(baseOrderGoodsList, depotCodeList);

		if (oldOrderGoodsList == null || oldOrderGoodsList.isEmpty()) {
			logger.error("删除接口数据(删除临时表数据)在OS中没有找到对应的OrderGoods" + orderSn);
			throw new RuntimeException("在OS中没有找到对应的OrderGoods");
		}
		// 构建shipSn---(sku,shipsn)的关系
        final Map<String, Map<ShipSku, Integer>> shipSkuMap = new HashMap<String, Map<ShipSku, Integer>>(Constant.DEFAULT_MAP_SIZE);
		fillShipSkuMap(shipProviderList, orderShipList, shipSkuMap);

		List<MasterOrderGoods> newOrderGoodsList = new ArrayList<MasterOrderGoods>(oldOrderGoodsList.size() + 10);
		for (int i = 0; i < oldOrderGoodsList.size(); i++) {
			MasterOrderGoods orderGoods = oldOrderGoodsList.get(i);
			// 2次分仓已发货的不能分仓
			if (depotCodeList.contains(orderGoods.getDepotCode()) && !Constant.DETAILS_DEPOT_CODE.equals(orderGoods.getDepotCode())) {
				newOrderGoodsList.add(cloneGoods(orderGoods));
				continue;
			}
			// 商品数量
			int goodsNum = orderGoods.getGoodsNumber();
			// 发货单
			Set<String> shipSns = shipSkuMap.keySet();
			for (Iterator<String> iterator = shipSns.iterator(); iterator.hasNext();) {
				String shipSn = iterator.next();
				// 2次分仓已发货的不能分仓
				if (depotCodeList.contains(shipSn)) {
					continue;
				}
				ShipSku shipsku = new ShipSku(shipSn, orderGoods.getCustomCode());
				Map<ShipSku, Integer> tempShipSkuMap = shipSkuMap.get(shipSn);
				if (!tempShipSkuMap.containsKey(shipsku)) {
					continue;
				}

				// 发货仓数量
				int tempCount = tempShipSkuMap.get(shipsku);
				MasterOrderGoods og = cloneGoods(orderGoods);
				og.setDepotCode(shipSn);
				newOrderGoodsList.add(og);
				if (tempCount > goodsNum) {
					tempShipSkuMap.put(shipsku, tempCount - goodsNum);
                    tempCount = goodsNum;
					og.setGoodsNumber(Short.parseShort("" + tempCount));
					fillGoodsDisSendNm(og);
					// 该商品已经处理完毕s
					break;
				}
				og.setGoodsNumber(Short.parseShort("" + tempCount));
				fillGoodsDisSendNm(og);

				tempShipSkuMap.remove(shipsku);
				if (tempCount == goodsNum) {
					break;
				}
				goodsNum -= tempCount;
			}
		}

		refreshGoods(newOrderGoodsList);

		// 部分分配交货单，新加默认仓
        processDefaultDepotGoods(orderSn);

		return newOrderGoodsList;
	}

    /**
     * 处理交货单下, 默认的发货仓
     * @param orderSn 交货单
     */
	private void processDefaultDepotGoods(String orderSn) {
        MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
        goodsExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(Constant.DETAILS_DEPOT_CODE).andIsDelEqualTo(0);
        List<MasterOrderGoods> orderGoods = this.masterOrderGoodsMapper.selectByExample(goodsExample);
        if (StringUtil.isListNotNull(orderGoods)) {
            OrderDepotShip insertDepotShip = new OrderDepotShip();
            insertDepotShip = new OrderDepotShip();
            insertDepotShip.setOrderSn(orderSn);
            insertDepotShip.setDepotCode(Constant.DETAILS_DEPOT_CODE);
            insertDepotShip.setCreatTime(new Date());
            insertDepotShip.setBak1("");
            // 发货单号 发货的时候再给 但是 不允许为空
            insertDepotShip.setInvoiceNo("");
            // 商品配送情况
            insertDepotShip.setShippingStatus((byte)Constant.OS_SHIPPING_STATUS_UNSHIPPED);
            // 发货单编号
            insertDepotShip.setOrderSn(orderSn);
            orderDepotShipMapper.insertSelective(insertDepotShip);
        }
    }

    /**
     * 查找已发货仓库列表
     * @param orderDepotShips
     * @return
     */
	private List<String> findShippedShipSn(List<OrderDepotShip> orderDepotShips) {
		List<String> item = new ArrayList<String>();
		for (int i = 0; i < orderDepotShips.size(); i++) {
			OrderDepotShip orderShip = orderDepotShips.get(i);
			if (orderShip == null || orderShip.getShippingStatus() == null) {
                continue;
            }
			if (orderShip.getShippingStatus() != 0) {
				item.add(orderShip.getDepotCode());
			}
		}
		return item;
	}

	/**
	 * 重新计算goods折扣和sendNumber
	 * 
	 * @param og
	 */
	private void fillGoodsDisSendNm(MasterOrderGoods og) {
		og.setSendNumber(og.getGoodsNumber());
//		double disCount = NumberUtil.sub(og.getGoodsPrice().doubleValue(), og.getTransactionPrice().doubleValue());
//		og.setDiscount(BigDecimal.valueOf(disCount * og.getGoodsNumber()));
	}

    /**
     * 不同发货仓下对于的商品列表
     * @param baseOrderGoodsList
     * @param depotCodeList 仓库列表
     * @return List<MasterOrderGoods>
     */
	private List<MasterOrderGoods> groupOrderGoodsBySkuExtCode(List<MasterOrderGoods> baseOrderGoodsList, List<String> depotCodeList) {
		List<MasterOrderGoods> item = new ArrayList<MasterOrderGoods>();
		for (int i = 0; i < baseOrderGoodsList.size(); i++) {
			MasterOrderGoods goods = baseOrderGoodsList.get(i);
			// 存在仓库的商品列表
			if (depotCodeList.contains(goods.getDepotCode())) {
				item.add(goods);
				continue;
			}
			boolean flag = false;
			for (int j = 0; j < item.size(); j++) {
				MasterOrderGoods orderGoods = item.get(j);
				if (depotCodeList.contains(orderGoods.getDepotCode())) {
                    continue;
                }
				if (orderGoods.getCustomCode().equals(goods.getCustomCode())
						&& orderGoods.getExtensionCode().equals(goods.getExtensionCode())
						&& orderGoods.getExtensionId().equals(goods.getExtensionId())) {
					orderGoods.setGoodsNumber((short) (orderGoods.getGoodsNumber() + goods.getGoodsNumber()));
					flag = true;
				}
			}
			// 没有发货仓的情况
			if (!flag || item.size() == 0) {
				item.add(goods);
			}
		}
		return item;
	}

	public void refreshGoods(List<MasterOrderGoods> goodsList) {
		logger.debug("orderToShipMapperDefined.refreshGoodsBAK(unshipped);");
		if (goodsList == null) {
			throw new RuntimeException("增加的商品为空！");
		}
		for (int i = 0; goodsList != null && i < goodsList.size(); i++) {
			masterOrderGoodsMapper.updateByPrimaryKeySelective(goodsList.get(i));
		}
	}

	private MasterOrderGoods cloneGoods(MasterOrderGoods orderGoods) {
		MasterOrderGoods og = new MasterOrderGoods();
		try {
			CachedBeanCopier.copy(orderGoods, og);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return og;
	}

	/**
	 * 构建ship-sku数量关系 填充Map
	 * 
	 * @param shipProviderList
	 * @param orderShipList
	 * @param shipSkuMap
	 */
	private void fillShipSkuMap(List<WkUdDistribute> shipProviderList, List<OrderDepotShip> orderShipList,
			final Map<String, Map<ShipSku, Integer>> shipSkuMap) {

	    // 多个发货单列表
		for (int i = 0; i < orderShipList.size(); i++) {
			final OrderDepotShip orderShip = orderShipList.get(i);
			// 发货单仓库
			final String depotCode = orderShip.getDepotCode();

			findShipProvider(shipProviderList, new Loop<WkUdDistribute>() {

			    @Override
				public boolean process(WkUdDistribute shipProviderBean) {
					return buildShipSkuCount(shipSkuMap, orderShip, depotCode, shipProviderBean);
				}
			});
		}
	}

	/**
	 * 构建ship-sku数量关系
	 * 
	 * @param shipSkuMap
	 * @param orderShip
	 * @param shipSn
	 * @param shipProviderBean
	 * @return boolean
	 */
	private boolean buildShipSkuCount(final Map<String, Map<ShipSku, Integer>> shipSkuMap, final OrderDepotShip orderShip,
			final String shipSn, WkUdDistribute shipProviderBean) {
		// 虚拟仓编号
		String vDepotCode = shipProviderBean.getDistWarehCode();
		// 实际出货仓库
		if (!vDepotCode.equals(orderShip.getDepotCode())) {
			return true;
		}

		// 仓库发货数量
		int goodsCount = (int) Math.round(Double.parseDouble(shipProviderBean.getGoodsNumber()));
		// 商品Sku
		String sku = shipProviderBean.getProdId();
		if (!shipSkuMap.containsKey(shipSn)) {
			shipSkuMap.put(shipSn, new HashMap<ShipSku, Integer>(0));
		}

		// 通过发货单和sku，获取对应的发货数量
		Map<ShipSku, Integer> tempMap = shipSkuMap.get(shipSn);
		ShipSku shipSku = new ShipSku(shipSn, sku);
		if (!tempMap.containsKey(shipSku)) {
			tempMap.put(shipSku, 0);
		}
		goodsCount = tempMap.get(shipSku) + goodsCount;
		tempMap.put(shipSku, goodsCount);
		return true;
	}

    /**
     * 处理发货单信息
     * @param shipProviderList
     * @param loop
     */
	private void findShipProvider(List<WkUdDistribute> shipProviderList, Loop<WkUdDistribute> loop) {
		for (int i = 0; i < shipProviderList.size(); i++) {
			loop.process(shipProviderList.get(i));
		}
	}

	/**
	 * 处理orderDepot,orderDepotShip
	 * 
	 * @param shipProviderList
	 * @param orderSn
	 * @param nowOrderShipList
	 * @param shippedShipSn
	 */
	private List<OrderDepotShip> processOrderDepotShip(final List<WkUdDistribute> shipProviderList, String orderSn,
			List<OrderDepotShip> nowOrderShipList, List<String> shippedShipSn) {
		OrderDepotShip orderDepotShipTemp = getOrderShipTemp(orderSn);
		if (orderDepotShipTemp == null) {
			orderDepotShipTemp = new OrderDepotShip();
			orderDepotShipTemp.setOrderSn(orderSn);
			orderDepotShipTemp.setDepotCode(Constant.DETAILS_DEPOT_CODE);
			orderDepotShipTemp.setCreatTime(new Date());
			orderDepotShipTemp.setBak1("");
		}

		// 根据分配结果，建立对应的仓库发货单列表
		List<OrderDepotShip> depotShips = groupByDistWarehCode(shipProviderList);
		for (int i = 0; i < depotShips.size(); i++) {
			OrderDepotShip depotShip = depotShips.get(i);
			fillOrderDepotShip(depotShip, orderDepotShipTemp);
			WkUdDistribute shipProviderBean = getShipProvider(shipProviderList, depotShip.getDepotCode());
			depotShip.setPdwarhCode(shipProviderBean.getPdwarhCode());
			depotShip.setPdwarhName(shipProviderBean.getPdwarhName());
			depotShip.setToUser(shipProviderBean.getToUser());
			depotShip.setToUserPhone(shipProviderBean.getToUserPhone());
			depotShip.setProvincecity(shipProviderBean.getProvincecity());
			depotShip.setOverTransCycle(shipProviderBean.getOverTransCycle());
            // 配送类型 1:门店发货单;2:工厂发货单;3:第三方仓库发货单
			depotShip.setDeliveryType(shipProviderBean.getShipType());
			depotShip = getShippedOrderShip(nowOrderShipList, depotShip);
			depotShips.set(i, depotShip);
		}
		refreshShip(orderSn, depotShips);
		return depotShips;
	}

    /**
     * 通过仓库编码，获取对应分配结果中的数据
     * @param shipProviderList
     * @param depotCode
     * @return
     */
	private WkUdDistribute getShipProvider(List<WkUdDistribute> shipProviderList, String depotCode) {
		for (int i = 0; i < shipProviderList.size(); i++) {
			WkUdDistribute bean = shipProviderList.get(i);
			if(StringUtil.equals(bean.getDistWarehCode(), depotCode)) {
				return bean;
			}
		}
		return null;
	}

	/**
	 * 获取已发货的ship原始信息
	 * 
	 * @param nowOrderShipList
	 * @param orderShip
	 * @return
	 */
	private OrderDepotShip getShippedOrderShip(List<OrderDepotShip> nowOrderShipList, OrderDepotShip orderShip) {
		if (orderShip == null) {
            return orderShip;
        }
		if (nowOrderShipList == null || nowOrderShipList.isEmpty()) {
            return orderShip;
        }
		for (int i = 0; i < nowOrderShipList.size(); i++) {
			OrderDepotShip depotShip = nowOrderShipList.get(i);
			// 如果商品已经发货并且2个发货单虚拟仓CODE一致 则该ship已经发货
			if (depotShip.getShippingStatus() != 0 && StringUtil.equals(orderShip.getDepotCode(), depotShip.getDepotCode())) {
				return depotShip;
			}
		}
		return orderShip;
	}

	public static void main(String[] args) {
		// Integer in =Integer.parseInt("5.0");
		double d = Double.parseDouble("5.0");
		System.out.println((int) Math.round(d));
	}

	/**
	 * 刷新orderDepot数据
	 * 
	 * @param orderSn
	 * @param depotShips
	 */
	public void refreshShip(String orderSn, List<OrderDepotShip> depotShips) {
        // 删除原有交货单发货单数据
		OrderDepotShipExample shipExample = new OrderDepotShipExample();
		shipExample.or().andOrderSnEqualTo(orderSn);
		orderDepotShipMapper.deleteByExample(shipExample);

		// 重新新建发货单
		for (int i = 0; depotShips != null && i < depotShips.size(); i++) {
			logger.debug("insert orderShip : " + depotShips.get(i));
			orderDepotShipMapper.insertSelective(depotShips.get(i));
		}
	}

	/**
	 * 将ERP的分仓信息按虚拟仓库号分组并且统计商品数量
	 * 
	 * @param shipProviderList
	 * @return
	 */
	private List<OrderDepotShip> groupByDistWarehCode(List<WkUdDistribute> shipProviderList) {
		List<OrderDepotShip> orderShipList = new ArrayList<OrderDepotShip>();
		for (int i = 0; i < shipProviderList.size(); i++) {
			WkUdDistribute shipProviderBean = shipProviderList.get(i);
			OrderDepotShip orderShip = new OrderDepotShip();
			orderShip.setDepotCode(shipProviderBean.getDistWarehCode());
			if (!containsDepotCode(orderShipList, orderShip.getDepotCode())) {
				fillShippingInfo(shipProviderBean, orderShip);
				orderShipList.add(orderShip);
			}
		}
		return orderShipList;
	}

	/**
	 * 填充配送信息到orderShip里
	 * 
	 * @param shipProviderBean
	 * @param depotShip
	 */
	private void fillShippingInfo(WkUdDistribute shipProviderBean, OrderDepotShip depotShip) {
		SystemShipping shipping = systemShippingService.getSystemShipByShipCode(shipProviderBean.getShipCode());
		if (shipping == null) {
			depotShip.setShippingId(new Byte("0"));
			depotShip.setShippingName("");
		} else {
            // 用户选择的配送方式id
			depotShip.setShippingId(shipping.getShippingId());
            // 用户选择的配送方式的名称
			depotShip.setShippingName(shipping.getShippingName());
		}
		// 最后分仓时间
		depotShip.setDepotTime(shipProviderBean.getCreateDate());
	}

    /**
     * 仓库发货单是否存在该仓库
     * @param orderShipList
     * @param depotCode
     * @return boolean
     */
	private boolean containsDepotCode(List<OrderDepotShip> orderShipList, String depotCode) {
		for (int i = 0; i < orderShipList.size(); i++) {
			OrderDepotShip orderShip = orderShipList.get(i);
			if (orderShip.getDepotCode().equals(depotCode)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取最后创建的orderShip
	 * 
	 * @param orderSn
	 * @return
	 */
	private OrderDepotShip getOrderShipTemp(String orderSn) {
		OrderDepotShipExample example = new OrderDepotShipExample();
		example.or().andOrderSnEqualTo(orderSn).limit(1);
		List<OrderDepotShip> depots = orderDepotShipMapper.selectByExample(example);
		if (StringUtil.isListNull(depots)) {
			return null;
		}
		return depots.get(0);
	}

	/**
	 * 判断订单状态时候可以进行分仓动作
	 * 
	 * @param orderInfo
	 * @return 订单处于取消状态或者订单处于无效状态的时候不需要再进行分仓处理
	 */
	private boolean checkOrderStatus(OrderDistribute orderInfo) {
        // 订单处于取消状态、// 订单处于无效状态;
		return orderInfo.getOrderStatus() == 2 || orderInfo.getOrderStatus() == 3;
	}

	/**
	 * 创建订单日志基础信息
	 * 
	 * @param orderInfo
	 * @return
	 */
	private DistributeAction createOrderAction(OrderDistribute orderInfo) {
		DistributeAction orderAction = new DistributeAction();
		// 设定要保存的订单操作日志信息
		orderAction.setOrderSn(orderInfo.getOrderSn());
		orderAction.setActionUser(Constant.OS_STRING_SYSTEM);
		orderAction.setOrderStatus(orderInfo.getOrderStatus());
		orderAction.setShippingStatus(orderInfo.getShipStatus());
		orderAction.setPayStatus(orderInfo.getPayStatus());
		orderAction.setQuestionStatus(Byte.parseByte("" + orderInfo.getQuestionStatus()));
		orderAction.setRangeStatus((byte) -1);
		orderAction.setActionNote("已分仓");
		orderAction.setLogTime(new Date());
		return orderAction;
	}

    /**
     * 根据交货单号获取订单商品列表
     * @param orderSn 交货单号
     * @return List<MasterOrderGoods>
     */
	public List<MasterOrderGoods> selectByExample(String orderSn) {
		MasterOrderGoodsExample example = new MasterOrderGoodsExample();
		example.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
		return masterOrderGoodsMapper.selectByExample(example);
	}

	/**
	 * 填充orderShip信息
	 * @param depotShip
	 * @param orderShipTemp
	 */
	private void fillOrderDepotShip(OrderDepotShip depotShip, OrderDepotShip orderShipTemp) {
        // 发货单号 发货的时候再给 但是 不允许为空
		depotShip.setInvoiceNo("");
        // 商品配送情况
		depotShip.setShippingStatus((byte)Constant.OS_SHIPPING_STATUS_UNSHIPPED);
        // 发货单编号
		depotShip.setOrderSn(orderShipTemp.getOrderSn());
        // 订单配送时间
		depotShip.setDepotTime(orderShipTemp.getDeliveryTime());
        // 出库号
		depotShip.setBak1(orderShipTemp.getBak1());
        // 发货单生成时间
		depotShip.setCreatTime(orderShipTemp.getCreatTime());
        // 最后更新时间
		depotShip.setUpdateTime(orderShipTemp.getUpdateTime());
        // 发货仓地区ID
		depotShip.setRegionId(orderShipTemp.getRegionId());
	}

	interface Loop<T> {
		boolean process(T t);
	}

	class ShipSku {
		private String shipSn;
		private String sku;
		private boolean flag = false;

		public ShipSku() {
		}

		public ShipSku(String shipSn, String sku) {
			super();
			this.shipSn = shipSn;
			this.sku = sku;
		}

		public String getShipSn() {
			return shipSn;
		}

		public void setShipSn(String shipSn) {
			this.shipSn = shipSn;
		}

		public String getSku() {
			return sku;
		}

		public void setSku(String sku) {
			this.sku = sku;
		}

		public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((shipSn == null) ? 0 : shipSn.hashCode());
			result = prime * result + ((sku == null) ? 0 : sku.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ShipSku other = (ShipSku) obj;
			if (shipSn == null) {
				if (other.shipSn != null)
					return false;
			} else if (!shipSn.equals(other.shipSn))
				return false;
			if (sku == null) {
				if (other.sku != null)
					return false;
			} else if (!sku.equals(other.sku))
				return false;
			return true;
		}
	}
}
