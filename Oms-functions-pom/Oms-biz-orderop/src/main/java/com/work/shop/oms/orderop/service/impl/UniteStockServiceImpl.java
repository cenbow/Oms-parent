package com.work.shop.oms.orderop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.MasterOrderAddressInfoMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.stock.service.ChannelStockService;
import com.work.shop.oms.stock.service.UniteStockService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.stock.center.bean.StockServiceBean;
import com.work.shop.stock.center.dto.OrderDistributeBean;
import com.work.shop.stock.center.dto.OrderStockBean;
import com.work.shop.stock.center.dto.StockBean;
import com.work.shop.stock.center.feign.OrderStockService;
import com.work.shop.stockcenter.client.dto.PlatformSkuStock;
import com.work.shop.stockcenter.client.dto.SkuStock;
import com.work.shop.stockcenter.client.dto.StockOperatePO;
import com.work.shop.stockcenter.client.dto.StockUserInfo;

/**
 * 统一库存服务
 * @author QuYachu
 */
@Service
public class UniteStockServiceImpl implements UniteStockService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;
	@Resource
	private ChannelStockService channelStockService;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;

	@Resource
	private OrderStockService orderStockService;
	
	@Resource
	private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
	
	@Override
	public ReturnInfo occupy(String masterOrderSn) {
		return occupyByCommon(masterOrderSn, 1);
	}

	@Override
	public ReturnInfo occupyByOutChannel(StockOperatePO stockOperatePO) {
		logger.info("拉单订单占用库存开始 stockOperatePO：" + JSON.toJSONString(stockOperatePO));
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (stockOperatePO == null) {
			info.setMessage("[stockOperatePO]参数不能为空");
			return info;
		}
		if (StringUtil.isTrimEmpty(stockOperatePO.getBusinessId())) {
			info.setMessage("[stockOperatePO.businessId]参数不能为空");
			return info;
		}
		if (StringUtil.isListNull(stockOperatePO.getSkuStockList())) {
			info.setMessage("[stockOperatePO.occpBeans]参数不能为空");
			return info;
		}
		if (StringUtil.isTrimEmpty(stockOperatePO.getChannelCode())) {
			info.setMessage("[orderStockBean.channelCode]参数不能为空");
			return info;
		}
		info = occupyByUniteStock(stockOperatePO);
		logger.info("拉单订单占用库存结束 result：" + JSON.toJSONString(info));
		return info;
	}

	@SuppressWarnings("rawtypes")
	public ReturnInfo occupyByUniteStock(StockOperatePO stockOperatePO) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		logger.info("预占stock_center_occupy_param:" + JSON.toJSONString(stockOperatePO));
		boolean flag = false;
		for (int i = 0; i < 3; i++) {
			if (flag) {
				break;
			}
			try {
				/*PlatformStockOperateRst operateRst = orderStockService.occupy(stockOperatePO);
				logger.info("预占stock_center_occupy_result:" + JSON.toJSONString(operateRst));
				if (operateRst == null) {
					logger.error(stockOperatePO.getBusinessId() + "预占失败！");
					info.setMessage("预占失败：返回结果为空");
					return info;
				}
				if ("1".equals(operateRst.getReturnCode())) {
					flag = true;
					info.setIsOk(Constant.OS_YES);
					info.setMessage("占用成功！");
					logger.info(stockOperatePO.getBusinessId() + ": 占用成功！");
				} else {
					logger.error(stockOperatePO.getBusinessId() + "预占失败:" + operateRst.getReturnMsg());
					info.setMessage("占用失败：" + operateRst.getReturnMsg());
				}*/
			} catch (Exception e) {
				logger.error("订单[" + stockOperatePO.getBusinessId() + "]第[" + (i + 1) + "]次占用库存异常:", e);
				info.setMessage("订单[" + stockOperatePO.getBusinessId() + "]占用库存异常" + e.getMessage());
				info.setIsOk(Constant.OS_ERROR);
			}
		}
		return info;
	}

	/**
	 * 订单取消, 释放统一库存
	 * @param masterOrderSn 主单号
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> realese(String masterOrderSn) {
		logger.info("订单取消后释放库存masterOrderSn:" + masterOrderSn);
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			info.setMessage("[masterOrderSn]参数不能为空");
			return info;
		}
		try {
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
            MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			if (master == null || masterOrderInfoExtend == null) {
				info.setMessage("订单[" + masterOrderSn + "] 不存在！");
				return info;
			}
			if (master.getSplitStatus().intValue() != Constant.SPLIT_STATUS_SPLITED) {
				info.setMessage("订单[" + masterOrderSn + "] 未拆单！");
				return info;
			}

			// 订单类型 0正常订单、1联采订单
            Short orderType = masterOrderInfoExtend.getOrderType();
			if (orderType != null && orderType == 1) {
                info.setMessage("订单[" + masterOrderSn + "] 联采订单！");
                info.setIsOk(Constant.OS_YES);
                return info;
            }
			
			// 查询配送单,如果配送单未配送的状态，才能创建退单入库调整单
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			
			OrderDistributeExample.Criteria criteria = distributeExample.or();
			criteria.andMasterOrderSnEqualTo(masterOrderSn);
			criteria.andOrderTypeEqualTo(0);
			List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
			if (distributes == null || distributes.size() == 0) {
				info.setMessage("订单[" + masterOrderSn + "] 无有效交货单！");
				return info;
			}
			
			for (OrderDistribute distribute : distributes) {
				if (distribute.getShipStatus() != Constant.OI_SHIP_STATUS_UNSHIPPED) {
					info.setMessage("订单[" + masterOrderSn + "] 货物已处理！");
					return info;
				}
			}
			
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.or().andMasterOrderSnEqualTo(masterOrderSn).andIsDelEqualTo(0);
			List<MasterOrderGoods> list = masterOrderGoodsMapper.selectByExample(goodsExample);
			if (StringUtil.isListNull(list)) {
				info.setMessage("订单[" + masterOrderSn + "] 已经释放过库存");
				info.setIsOk(Constant.OS_YES);
				return info;
			}
			// 处理订单取消, 库存返还
			info = processOrderReturnStock(master, list);
		} catch (Exception e) {
			logger.error(masterOrderSn + "取消后释放失败:" + e.getMessage(), e);
			info.setMessage("取消后释放失败:" + e.getMessage());
		} finally {
			logger.info("订单取消后释放库存response:" + JSON.toJSONString(info));
		}
		return info;
	}

    /**
     * 获取交货单对应商品信息关系
     * @param list
     * @return Map<String, List<MasterOrderGoods>>
     */
	private Map<String, List<MasterOrderGoods>> processMasterOrderStock(List<MasterOrderGoods> list) {

        Map<String, List<MasterOrderGoods>> distItemMap = new HashMap<String, List<MasterOrderGoods>>(Constant.DEFAULT_MAP_SIZE);
        for (MasterOrderGoods orderGoods : list) {
            String orderSn = orderGoods.getOrderSn();
            List<MasterOrderGoods> items = distItemMap.get(orderSn);
            if (StringUtil.isListNull(items)) {
                items = new ArrayList<MasterOrderGoods>();
            }
            items.add(orderGoods);
            distItemMap.put(orderSn, items);
        }

        return distItemMap;
    }

    /**
     * 获取订单商品列表
     * @param masterOrderSn
     * @return List<MasterOrderGoods>
     */
	private List<MasterOrderGoods> getMasterOrderGoods(String masterOrderSn) {
        MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
        goodsExample.or().andMasterOrderSnEqualTo(masterOrderSn).andIsDelEqualTo(0);
        List<MasterOrderGoods> list = masterOrderGoodsMapper.selectByExample(goodsExample);
        return list;
    }

    /**
     * 获取交货单对应商品库存信息
     * @param master
     * @param distItemMap
     * @return List<OrderDistributeBean>
     */
    private List<OrderDistributeBean> getOrderDistributeInfo(MasterOrderInfo master, Map<String, List<MasterOrderGoods>> distItemMap) {
        // 订单来源
        Integer source = master.getSource();
        if (source == null) {
            source = 0;
        }
        List<OrderDistributeBean> distributeBeans = new ArrayList<OrderDistributeBean>();
        for (String orderSn : distItemMap.keySet()) {
            List<MasterOrderGoods> items = distItemMap.get(orderSn);
            // 合并相同sku
            Map<String, Integer> itemQtyMap = new HashMap<String, Integer>();
            for (MasterOrderGoods item : items) {
                String customCode = item.getCustomCode();
                Integer qty = itemQtyMap.get(customCode);
                // 库存量 无库存下单相关，这里的数据用于扣减库存，使用订单商品走库存的数量
                int stockNum = item.getWithStockNumber();
                if (source == 6) {
                	//目前业务没有这个箱规什么的，如果有的话这里要修改为无库存下单相关，创建订单那里也有这个判断
                    Short boxGauge = item.getBoxGauge();
                    if (boxGauge != null && boxGauge > 1) {
                        stockNum = stockNum * boxGauge;
                    }
                }
                if (qty == null) {
                    qty = stockNum;
                } else {
                    qty += stockNum;
                }
                itemQtyMap.put(customCode, qty);
            }

            List<StockBean> stockBeans = new ArrayList<StockBean>();
            for (String customCode : itemQtyMap.keySet()) {
                int qty = itemQtyMap.get(customCode);
                StockBean bean = new StockBean();
                bean.setSku(customCode);
                bean.setStock(qty);
                stockBeans.add(bean);
            }
            OrderDistributeBean distributeBean = new OrderDistributeBean();
            distributeBean.setOrderSn(orderSn);
            distributeBean.setStockList(stockBeans);
            distributeBeans.add(distributeBean);
        }

        return distributeBeans;
    }

    /**
     * 填充统一库存分配请求参数
     * @param master
     * @param masterOrderAddressInfo
     * @param distributeBeans
     * @return OrderStockBean
     */
    private OrderStockBean fillStockRequestBean(MasterOrderInfo master, MasterOrderAddressInfo masterOrderAddressInfo, List<OrderDistributeBean> distributeBeans) {
        OrderStockBean stockBean = new OrderStockBean();
        stockBean.setChannelCode(master.getOrderFrom());
        stockBean.setFlag(1);
        stockBean.setOrderDistributeList(distributeBeans);
        stockBean.setOrderNo(master.getMasterOrderSn());
        stockBean.setOutOrderNo(master.getOrderOutSn());
        stockBean.setType(1);

        // 设置用户地址信息
        String province = masterOrderAddressInfo.getProvince();
        String city = masterOrderAddressInfo.getCity();
        String district = masterOrderAddressInfo.getDistrict();

        Double longitude = masterOrderAddressInfo.getLongitude();
        Double latitude = masterOrderAddressInfo.getLatitude();

        StockUserInfo stockUserInfo = new StockUserInfo();
        if (StringUtils.isNotBlank(province)) {
            stockUserInfo.setProvince(province);
        }
        if (StringUtils.isNotBlank(city)) {
            stockUserInfo.setCity(city);
        }
        if (StringUtils.isNotBlank(district)) {
            stockUserInfo.setDistrict(district);
        }
        if (longitude != null && longitude != 0) {
            stockUserInfo.setLongitude(longitude);
        }
        if (latitude != null && latitude != 0) {
            stockUserInfo.setLatitude(latitude);
        }
        stockUserInfo.setAddress(masterOrderAddressInfo.getAddress());
        stockBean.setStockUserInfo(stockUserInfo);

        return stockBean;
    }

	/**
	 * 订单配送单分配后，统一库存实占
	 * @param masterOrderSn 主单号
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> distOccupy(String masterOrderSn) {
		logger.info("订单拆单后分配占用库存masterOrderSn:" + masterOrderSn);
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			info.setMessage("[masterOrderSn]参数不能为空");
			return info;
		}
		try {
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if (master == null) {
				info.setMessage("订单[" + masterOrderSn + "] 不存在！");
				return info;
			}
			if (master.getSplitStatus().intValue() != Constant.SPLIT_STATUS_SPLITED) {
				info.setMessage("订单[" + masterOrderSn + "] 未拆单！");
				return info;
			}
			MasterOrderInfoExtend masterExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			if (masterExtend == null) {
				info.setMessage("订单[" + masterOrderSn + "] 扩展信息不存在！");
				return info;
			}
			
			// 用户信息地址
			MasterOrderAddressInfo masterOrderAddressInfo = masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
			if (masterOrderAddressInfo == null) {
				info.setMessage("订单[" + masterOrderSn + "] 用户地址信息不存在！");
				return info;
			}

            List<MasterOrderGoods> list = getMasterOrderGoods(masterOrderSn);
			if (StringUtil.isListNull(list)) {
				info.setMessage("订单[" + masterOrderSn + "] 已经占用过库存");
				info.setIsOk(Constant.OS_YES);
				return info;
			}

			// 交货单与对应商品关系
			Map<String, List<MasterOrderGoods>> distItemMap = processMasterOrderStock(list);
			List<OrderDistributeBean> distributeBeans = getOrderDistributeInfo(master, distItemMap);
			if (StringUtil.isListNull(distributeBeans)) {
				info.setMessage("订单[" + masterOrderSn + "] 分配单列表为空");
				info.setIsOk(Constant.OS_YES);
				return info;
			}

			// 获取请求接口参数
            OrderStockBean stockBean = fillStockRequestBean(master, masterOrderAddressInfo, distributeBeans);
			
			// 调研失败后重试三次
			boolean flag = false;
			for (int i = 0; i < 3; i++) {
				if (flag) {
					break;
				}
				logger.info("拆单后实占通知统一库存 request:" + JSON.toJSONString(stockBean));
				StockServiceBean<Boolean> result = orderStockService.occupy(stockBean);
				logger.info("拆单后实占通知统一库存 response:" + JSON.toJSONString(result));
				if (result == null) {
					logger.error(masterOrderSn + "拆单后实占失败：返回为空");
					info.setMessage("拆单后实占失败：返回为空");
					return info;
				}
				if (Constant.OS_STR_YES.equals(result.getReturnCode())) {
					info.setIsOk(Constant.OS_YES);
					info.setMessage("占用成功！");
					logger.info("订单[" + masterOrderSn + "]平台库存占用结果：成功");
					try {
						// 更新商品占用库存数据
						for (MasterOrderGoods goods : list) {
							MasterOrderGoods updateGoods = new MasterOrderGoods();
							updateGoods.setId(goods.getId());
							updateGoods.setSendNumber(goods.getGoodsNumber());
							masterOrderGoodsMapper.updateByPrimaryKeySelective(updateGoods);
						}
					} catch (Exception e) {
						logger.error("订单[" + masterOrderSn + "]调用统一库存占用接口成功,OMS库存占用数据处理异常" + e.getMessage(), e);
						info.setIsOk(Constant.OS_ERROR);
						info.setMessage("订单[" + masterOrderSn + "]调用统一库存占用接口成功,OMS库存占用数据处理异常" + e.getMessage());
					}
					flag = true;
				} else {
					logger.error(masterOrderSn + "拆单后实占失败:" + result.getReturnMsg());
					info.setMessage("占用失败：" + result.getReturnMsg());
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "拆单后实占失败:" + e.getMessage(), e);
			info.setMessage("拆单后实占失败:" + e.getMessage());
		} finally {
			logger.info("订单拆单后分配占用库存response:" + JSON.toJSONString(info));
		}
		return info;
	}


	/**
	 * 
	 * @param masterOrderSn 订单号
	 * @param occupyType 库存占用类型   1 ：未拆单，0：已拆单
	 * @return
	 */
	private ReturnInfo<String> occupyByCommon(String masterOrderSn, int occupyType) {
		logger.info("订单[" + masterOrderSn + "] 占用库存开始; occupyType :" + occupyType);
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			info.setMessage("[masterOrderSn]参数不能为空");
			return info;
		}
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (master == null) {
			info.setMessage("订单[" + masterOrderSn + "] 不存在！");
			return info;
		}
		MasterOrderInfoExtend masterExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
		if (masterExtend == null) {
			info.setMessage("订单[" + masterOrderSn + "] 扩展信息不存在！");
			return info;
		}
		List<MasterOrderGoods> list = null;
		if (master.getSplitStatus().intValue() == Constant.SPLIT_STATUS_SPLITED) {
			// 主订单确认时，多个子订单根据供应商确认（第三方供应商批量确认）
			OrderDistributeExample distributeExample = new OrderDistributeExample();
			OrderDistributeExample.Criteria criteria = distributeExample.or();
			criteria.andMasterOrderSnEqualTo(masterOrderSn);
			criteria.andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
			List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
			if (StringUtil.isListNull(distributes)) {
				logger.info("订单[" + masterOrderSn + "]有效发货单为空！");
				info.setMessage("订单[" + masterOrderSn + "]有效发货单为空！");
				return info;
			}
			List<String> orderSns = new ArrayList<String>();
			for (OrderDistribute distribute : distributes) {
				orderSns.add(distribute.getOrderSn());
			}
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.or().andOrderSnIn(orderSns).andIsDelEqualTo(0);
			list = masterOrderGoodsMapper.selectByExample(goodsExample);
		} else {
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.or().andMasterOrderSnEqualTo(masterOrderSn).andIsDelEqualTo(0);
			list = masterOrderGoodsMapper.selectByExample(goodsExample);
		}
		if (StringUtil.isListNull(list)) {
			info.setMessage("订单[" + masterOrderSn + "] 已经占用过库存");
			info.setIsOk(Constant.OS_YES);
			return info;
		}
		String siteCode = master.getChannelCode();
		Map<String, PlatformSkuStock> bgStockMap = new HashMap<String, PlatformSkuStock>();
		for (MasterOrderGoods orderGoods : list) {
			String sku = orderGoods.getCustomCode();
			// 平台库存判断
			if (orderGoods.getGoodsNumber().intValue() > orderGoods.getSendNumber().intValue()) {
				PlatformSkuStock bgSkuBean = bgStockMap.get(sku);
				if (bgSkuBean == null) {
					bgSkuBean = new PlatformSkuStock();
				}
				bgSkuBean.setSku(sku);
				bgSkuBean.setStock(bgSkuBean.getStock() + (orderGoods.getGoodsNumber() - orderGoods.getSendNumber()));
				bgStockMap.put(sku, bgSkuBean);
			}
		}
		List<PlatformSkuStock> bgSkuStockList = new ArrayList<PlatformSkuStock>();
		for (String sku : bgStockMap.keySet()) {
			bgSkuStockList.add(bgStockMap.get(sku));
		}
		if (StringUtil.isListNotNull(bgSkuStockList)) {
			info.setMessage("订单[" + masterOrderSn + "] 库存占用明细为空");
			info.setIsOk(Constant.OS_YES);
			return info;
		}

		StockOperatePO bgpo = new StockOperatePO();
		bgpo.setBusinessId(master.getOrderOutSn());
		bgpo.setChannelCode(master.getReferer());
		bgpo.setSkuStockList(bgSkuStockList);
		bgpo.setSiteCode(siteCode);
		bgpo.setDepot(false);
		logger.info("实占stock_center_occupy_param:" + JSON.toJSONString(bgpo));
		ReturnInfo returnInfo = channelStockService.occupy(bgpo);
		logger.info("实占stock_center_occupy_result:" + JSON.toJSONString(returnInfo));
		if (returnInfo == null) {
			logger.error(masterOrderSn + "预占失败！");
			info.setMessage("预占失败：返回结果为空");
			return info;
		}
		if (returnInfo.getIsOk() == Constant.OS_YES) {
			info.setIsOk(Constant.OS_YES);
			info.setMessage("占用成功！");
			logger.info("订单[" + masterOrderSn + "]平台库存占用结果：成功");
			try {
				// 更新商品占用库存数据
				for (MasterOrderGoods goods : list) {
					MasterOrderGoods updateGoods = new MasterOrderGoods();
					updateGoods.setId(goods.getId());
					updateGoods.setSendNumber(goods.getGoodsNumber());
					masterOrderGoodsMapper.updateByPrimaryKeySelective(updateGoods);
				}
			} catch (Exception e) {
				logger.error("订单[" + masterOrderSn + "]调用平台库存占用接口成功,OMS库存占用数据处理异常" + e.getMessage(), e);
				info.setIsOk(Constant.OS_ERROR);
				info.setMessage("订单[" + masterOrderSn + "]调用平台库存占用接口成功,OMS库存占用数据处理异常" + e.getMessage());
			}
		} else {
			logger.error(masterOrderSn + "预占失败:" + returnInfo.getMessage());
			info.setMessage("占用失败：" + returnInfo.getMessage());
		}
		logger.debug("订单[" + masterOrderSn + "] 占用库存结束");
		return info;
	}

	@Override
	public List<SkuStock> queryStockBySkus(List<String> skus, String shopCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SkuStock queryStockBySku(String sku, String shopCode,
			String depotCode) {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * 处理订单取消, 库存返还
     * @param master 订单信息
     * @param list 订单商品列表
     * @return ReturnInfo<String>
     */
	private ReturnInfo<String> processOrderReturnStock(MasterOrderInfo master, List<MasterOrderGoods> list) {
        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);

        String masterOrderSn = master.getMasterOrderSn();
        List<OrderDistributeBean> distributeBeans = processOrderDistribute(master, list);
        if (StringUtil.isListNull(distributeBeans)) {
            info.setMessage("订单[" + masterOrderSn + "] 分配单列表为空");
            info.setIsOk(Constant.OS_YES);
            return info;
        }

        OrderStockBean stockBean = new OrderStockBean();
        stockBean.setChannelCode(master.getOrderFrom());
        stockBean.setOrderDistributeList(distributeBeans);
        stockBean.setOrderNo(masterOrderSn);
        // 调研失败后重试三次
        boolean flag = false;
        for (int i = 0; i < 3; i++) {
            if (flag) {
                break;
            }
            logger.info("取消后释放通知统一库存 request:" + JSON.toJSONString(stockBean));
            StockServiceBean<Boolean> result = orderStockService.orderReturn(stockBean);
            logger.info("取消后释放通知统一库存 response:" + JSON.toJSONString(result));
            if (result == null) {
                logger.error(masterOrderSn + "取消后释放失败：返回为空");
                info.setMessage("取消后释放失败：返回为空");
                return info;
            }
            if (Constant.OS_STR_YES.equals(result.getReturnCode())) {
                info.setIsOk(Constant.OS_YES);
                info.setMessage("释放成功！");
                logger.info("订单[" + masterOrderSn + "]平台库存释放结果：成功");
                try {
                    // 更新商品占用库存数据
                    for (MasterOrderGoods goods : list) {
                        MasterOrderGoods updateGoods = new MasterOrderGoods();
                        updateGoods.setId(goods.getId());
                        updateGoods.setSendNumber(0);
                        masterOrderGoodsMapper.updateByPrimaryKeySelective(updateGoods);
                    }
                } catch (Exception e) {
                    logger.error("订单[" + masterOrderSn + "]调用统一库存释放接口成功,OMS库存释放数据处理异常" + e.getMessage(), e);
                    info.setIsOk(Constant.OS_ERROR);
                    info.setMessage("订单[" + masterOrderSn + "]调用统一库存释放接口成功,OMS库存释放数据处理异常" + e.getMessage());
                }
                flag = true;
            } else {
                logger.error(masterOrderSn + "取消后释放失败:" + result.getReturnMsg());
                info.setMessage("释放失败：" + result.getReturnMsg());
                // 暂停0.2秒钟
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    logger.error("线程暂停异常" + e.getMessage(), e);
                }
            }
        }

        return info;
    }

    /**
     * 处理订单交货单列表
     * @param master 订单信息
     * @param list 订单商品列表
     * @return List<OrderDistributeBean>
     */
	private List<OrderDistributeBean> processOrderDistribute(MasterOrderInfo master, List<MasterOrderGoods> list) {
        List<OrderDistributeBean> distributeBeans = new ArrayList<OrderDistributeBean>();

        // 订单来源
        Integer source = master.getSource();
        if (source == null) {
            source = 0;
        }

        Map<String, List<MasterOrderGoods>> distItemMap = processMasterGoods(list);

        for (String orderSn : distItemMap.keySet()) {
            List<MasterOrderGoods> items = distItemMap.get(orderSn);
            // 根据仓合并相同sku
            Map<String, MasterOrderGoods> itemQtyMap = new HashMap<String, MasterOrderGoods>();
            for (MasterOrderGoods item : items) {
                String customCode = item.getCustomCode();
                String depotCode = item.getDepotCode();
                String key = depotCode + customCode;
                MasterOrderGoods goods = itemQtyMap.get(key);
                if (goods == null) {
                    goods = new MasterOrderGoods();
                    goods.setGoodsNumber(item.getGoodsNumber());
                    goods.setCustomCode(customCode);
                    goods.setDepotCode(depotCode);
                    goods.setBoxGauge(item.getBoxGauge());
                } else {
                    int qty = goods.getGoodsNumber().intValue() + item.getGoodsNumber();
                    goods.setGoodsNumber(qty);
                }
                itemQtyMap.put(key, goods);
            }
            List<StockBean> stockBeans = new ArrayList<StockBean>();
            for (String skuKey : itemQtyMap.keySet()) {
                MasterOrderGoods item = itemQtyMap.get(skuKey);
                StockBean bean = new StockBean();
                bean.setSku(item.getCustomCode());

                int stockNum = item.getGoodsNumber().intValue();
                if (source == 6) {
                    // 箱规
                    Short boxGauge = item.getBoxGauge();
                    if (boxGauge != null && boxGauge > 1) {
                        stockNum = stockNum * boxGauge;
                    }
                }

                bean.setStock(stockNum);
                bean.setDepotNo(item.getDepotCode().equals(Constant.DETAILS_DEPOT_CODE) ? "" : item.getDepotCode());
                stockBeans.add(bean);
            }

            OrderDistributeBean distributeBean = new OrderDistributeBean();
            distributeBean.setOrderSn(orderSn);
            distributeBean.setStockList(stockBeans);
            distributeBeans.add(distributeBean);
        }

        return distributeBeans;
    }

    /**
     * 处理订单商品分交货单处理
     * @param list 订单商品
     * @return Map<String, List<MasterOrderGoods>>
     */
	private Map<String, List<MasterOrderGoods>> processMasterGoods(List<MasterOrderGoods> list) {
        Map<String, List<MasterOrderGoods>> distItemMap = new HashMap<String, List<MasterOrderGoods>>(Constant.DEFAULT_MAP_SIZE);
        for (MasterOrderGoods orderGoods : list) {
            String orderSn = orderGoods.getOrderSn();
            List<MasterOrderGoods> items = distItemMap.get(orderSn);
            if (StringUtil.isListNull(items)) {
                items = new ArrayList<MasterOrderGoods>();
            }
            items.add(orderGoods);
            distItemMap.put(orderSn, items);
        }

        return distItemMap;
    }
	
}