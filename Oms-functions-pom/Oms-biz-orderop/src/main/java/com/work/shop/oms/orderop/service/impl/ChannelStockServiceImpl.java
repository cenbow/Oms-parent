package com.work.shop.oms.orderop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.stock.center.bean.StockServiceBean;
import com.work.shop.stock.center.dto.WithoutStockSyncDepotBean;
import com.work.shop.stock.center.feign.DepotProcessService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.StockGoods;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.stock.service.ChannelStockService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.stockcenter.client.dto.PlatformSkuStock;
import com.work.shop.stockcenter.client.dto.PlatformStockOperateRst;
import com.work.shop.stockcenter.client.dto.StockOperatePO;
import com.work.shop.stockcenter.client.feign.StockServiceByShop;
import org.springframework.util.CollectionUtils;

/**
 * 商城库存服务
 * @author QuYachu
 */
@Service
public class ChannelStockServiceImpl implements ChannelStockService {
	
	public static Logger logger = Logger.getLogger(ChannelStockServiceImpl.class);

	@Resource
	private StockServiceByShop stockServiceByShop;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;

	@Autowired
	private DepotProcessService depotProcessService;
	
	@Override
	public ReturnInfo occupy(StockOperatePO bgpo, String isPayFirst) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (bgpo == null) {
			ri.setMessage("调用平台占用库存（支付扣减）: 传入参数为空");
			return null;
		}
		if (StringUtil.isEmpty(isPayFirst)) {
			isPayFirst = "0";
		}
		String orderSn = bgpo.getBusinessId();
		logger.info(" start 平台库存占用（支付扣减）：orderSn=" + orderSn + ";shopCode=" + bgpo.getChannelCode()
				+ ";isPayFirst=" + isPayFirst + ";skus=" + JSON.toJSONString(bgpo.getSkuStockList()));
		boolean flag = false;
		for (int i = 0; i < 3; i++) {
			if (flag) {
				break;
			}
			try {
				bgpo.setIsPayFirst(isPayFirst);
				logger.info("平台库存占用（支付扣减）：orderSn= " + orderSn + "json=" + JSON.toJSONString(bgpo));
				PlatformStockOperateRst result = stockServiceByShop.paySuccess(bgpo);
//				PlatformStockOperateRst result = stockNewService.paySuccess(bgpo);
//				PlatformStockOperateRst result = new PlatformStockOperateRst();
				logger.info("平台库存占用（支付扣减）：orderSn= " + orderSn + "result=" + JSON.toJSONString(result));
				flag = true;
				if (result != null && "1".equals(result.getReturnCode())) {
					ri.setIsOk(Constant.OS_YES);
					ri.setMessage(result.getReturnMsg());
				} else {
					ri.setMessage(result.getReturnMsg());
				}
			} catch (Exception e) {
				logger.error(orderSn + "调用平台占用库存异常：" + e.getMessage(), e);
				ri.setMessage("调用平台占用库存异常：" + e.getMessage());
			}
		}
		return ri;
	}

	@Override
	public ReturnInfo occupy(StockOperatePO po) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (po == null) {
			ri.setMessage("调用平台占用库存（支付扣减）: 传入参数为空");
			return null;
		}
		logger.info(" start 平台库存占用（支付扣减）：orderSn=" + po.getBusinessId()
				+ ";shopCode=" + po.getChannelCode() + ";isPayFirst=" + po.getIsPayFirst());
		if (StringUtil.isEmpty(po.getIsPayFirst())) {
			po.setIsPayFirst("0");
		}
		boolean flag = false;
		for (int i = 0; i < 3; i++) {
			if (flag) {
				break;
			}
			try {
				logger.info("平台库存占用（支付扣减）：orderSn= " + po.getBusinessId() + "json=" + JSON.toJSONString(po));
				PlatformStockOperateRst result = stockServiceByShop.paySuccess(po);
				logger.info("平台库存占用（支付扣减）：orderSn= " + po.getBusinessId() + "result=" + JSON.toJSONString(result));
				flag = true;
				if (result != null && "1".equals(result.getReturnCode())) {
					ri.setIsOk(Constant.OS_YES);
					ri.setMessage(result.getReturnMsg());
				} else {
					ri.setMessage(result.getReturnMsg());
				}
			} catch (Exception e) {
				logger.error(po.getBusinessId() + "调用平台占用库存异常：" + e.getMessage(), e);
				ri.setMessage("调用平台占用库存异常：" + e.getMessage());
			}
		}
		return ri;
	}

	/**
	 * 订单预占库存
	 * @param masterOrderSn 订单编码
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo preOccupy(String masterOrderSn) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			ri.setMessage("平台库存占用（预扣）: 传入订单号为空");
			return null;
		}
		logger.info("start 平台库存占用（预扣）：masterOrderSn=" + masterOrderSn);
		// 查询需占用库存商品
		try {
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if (null == master) {
				ri.setMessage("订单" + masterOrderSn + "不存在,不能占用库存");
				return ri;
			}
			
			// 交易类型
			Byte transType = master.getTransType();
			if (transType == Constant.OI_TRANS_TYPE_GUARANTEE) {
				// 平台担保订单,无需预占商城库存
				logger.info("订单" + masterOrderSn + "为平台担保订单,无需预占商城库存");
				ri.setMessage("订单" + masterOrderSn + "为平台担保订单");
				ri.setIsOk(Constant.OS_YES);
				return ri;
			}
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.or().andMasterOrderSnEqualTo(masterOrderSn).andSendNumberEqualTo((short)0);
			List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
			if (StringUtil.isListNull(goodsList)) {
				ri.setMessage("订单[" + masterOrderSn + "] 已经占用过库存");
				ri.setIsOk(Constant.OS_YES);
				return ri;
			}
			boolean isWaitStock = false;
			for (MasterOrderGoods masterOrderGoods : goodsList) {
				if (0 < masterOrderGoods.getWithoutStockNumber()) {
					//有商品需要无库存下单，已异步补过库存，需要重试等待
					isWaitStock = true;
					break;
				}
			}
			List<PlatformSkuStock> skuStockList = processGoodsStockList(master, goodsList);
			// 已经全部占用库存
			if (!StringUtil.isListNotNull(skuStockList)) {
				ri.setMessage(masterOrderSn + "已经占用过库存！");
				return ri;
			}

            boolean isDepot = false;
			StockOperatePO stockPO = new StockOperatePO();
            stockPO.setBusinessId(masterOrderSn);
            stockPO.setChannelCode(master.getOrderFrom());
            stockPO.setSkuStockList(skuStockList);
            stockPO.setSiteCode(master.getChannelCode());
            stockPO.setDepot(isDepot);
			try {
				//由于无库存下单补库存同步库存是异步的，可能还没同步过来，重试3次，最多等11s
				int tryCount = 1;
				if (isWaitStock) {
					tryCount = 3;
				}
				logger.info("平台库存占用（预扣）：masterOrderSn= " + masterOrderSn + ",json=" + JSON.toJSONString(stockPO));
				PlatformStockOperateRst result = null;
				for (int i = 0; i < tryCount; i++) {
					result = stockServiceByShop.occupy(stockPO);
					logger.info("第" + (i+1) + "次平台库存占用（预扣）：masterOrderSn= " + masterOrderSn + ",result=" + JSON.toJSONString(result));
					if (result == null || !Constant.OS_STR_YES.equals(result.getReturnCode())) {
						//第一次不行等2s再试。接下来分别是3s 和 6s
						Thread.sleep(1000 * (i*i + 2));
					} else {
						break;
					}
				}
				if (result != null && Constant.OS_STR_YES.equals(result.getReturnCode())) {
					ri.setIsOk(Constant.OS_YES);
					ri.setMessage("占用成功！");
					logger.info("订单[" + masterOrderSn + "]平台库存占用结果：成功");
					try {
						// 更新商品占用库存数据
                        processGoodsSendStock(goodsList);
					} catch (Exception e) {
						logger.error("订单[" + masterOrderSn + "]OMS库存占用数据处理异常" + e.getMessage(), e);
						ri.setIsOk(Constant.OS_ERROR);
						ri.setMessage("订单[" + masterOrderSn + "]OMS库存占用数据处理异常" + e.getMessage());
					}
					ri.setIsOk(Constant.OS_YES);
					ri.setMessage(result.getReturnMsg());
				} else {
					ri.setMessage(result.getReturnMsg());
				}
			} catch (Exception e) {
				logger.error(masterOrderSn + "平台库存占用（预扣）" + e.getMessage(), e);
				ri.setMessage("调用平台释放库存异常：" + e.getMessage());
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "平台库存占用（预扣）, orderSn:" + masterOrderSn, e);
			ri.setMessage("平台库存占用（预扣）, orderSn:" + masterOrderSn);
		}
		return ri;
	}

	/**
	 * 商城库存支付完成后实扣库存
	 * @param masterOrderSn 订单编码
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo payOccupy(String masterOrderSn) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			ri.setMessage("平台支付库存占用: 传入订单号为空");
			return null;
		}
		logger.info(" start 平台支付库存占用：masterOrderSn=" + masterOrderSn);
		// 查询需占用库存商品
		try {
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if (null == master) {
				ri.setMessage("订单" + masterOrderSn + "不存在,不能占用库存");
				return ri;
			}

            MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
			if (masterOrderInfoExtend == null) {
                ri.setMessage("订单" + masterOrderSn + "不存在,不能占用库存");
                return ri;
            }

            // 订单类型 0普通订单、1联采订单
            Short orderType = masterOrderInfoExtend.getOrderType();
			if (orderType != null && orderType == 1) {
                ri.setIsOk(Constant.OS_YES);
                ri.setMessage("联采订单, 不需占用库存");
                return ri;
            }

			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.or().andMasterOrderSnEqualTo(masterOrderSn);
			List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
			if (StringUtil.isListNull(goodsList)) {
				ri.setMessage("订单[" + masterOrderSn + "] 已经占用过库存");
				ri.setIsOk(Constant.OS_YES);
				return ri;
			}

			List<PlatformSkuStock> skuStockList = processGoodsStockList(master, goodsList);
			// 已经全部占用库存
			if (!StringUtil.isListNotNull(skuStockList)) {
				ri.setMessage(masterOrderSn + "已经占用过库存！");
				return ri;
			}

            boolean isDepot = false;
			StockOperatePO stockPO = new StockOperatePO();
            stockPO.setBusinessId(masterOrderSn);
            stockPO.setChannelCode(master.getOrderFrom());
            stockPO.setSkuStockList(skuStockList);
            stockPO.setSiteCode(master.getChannelCode());
            stockPO.setDepot(isDepot);
			boolean flag = false;
			for (int i = 0; i < 3; i++) {
				if (flag) {
					break;
				}
				try {
					logger.info("平台支付库存占用：masterOrderSn= " + masterOrderSn + "json=" + JSON.toJSONString(stockPO));
					PlatformStockOperateRst result = stockServiceByShop.paySuccess(stockPO);
					logger.info("平台支付库存占用：masterOrderSn= " + masterOrderSn + "result=" + JSON.toJSONString(result));
					flag = true;
					if (result != null && Constant.OS_STR_YES.equals(result.getReturnCode())) {
						ri.setIsOk(Constant.OS_YES);
						ri.setMessage(result.getReturnMsg());
					} else {
						ri.setMessage(result.getReturnMsg());
					}
				} catch (Exception e) {
					logger.error(masterOrderSn + "平台支付库存占用" + e.getMessage(), e);
					ri.setMessage("平台支付库存占用异常：" + e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "平台支付库存占用, orderSn:" + masterOrderSn, e);
			ri.setMessage("平台支付库存占用, orderSn:" + masterOrderSn);
		}
		logger.info("订单支付占用库存结束");
		return ri;
	}

    /**
     * 处理订单商品占用sku列表
     * @param master 订单信息
     * @param goodsList 订单商品列表
     * @return List<PlatformSkuStock>
     */
	private List<PlatformSkuStock> processGoodsStockList(MasterOrderInfo master, List<MasterOrderGoods> goodsList) {
        Map<String, PlatformSkuStock> stockMap = processGoodsStock(master, goodsList);
        List<PlatformSkuStock> skuStockList = new ArrayList<PlatformSkuStock>();
        for (String sku : stockMap.keySet()) {
            skuStockList.add(stockMap.get(sku));
        }
        return skuStockList;
    }

	/**
	 * 处理订单商品占用sku情况
	 * @param master 订单信息
	 * @param goodsList 订单商品列表
	 * @return Map<String, PlatformSkuStock>
	 */
	private Map<String, PlatformSkuStock> processGoodsStock(MasterOrderInfo master, List<MasterOrderGoods> goodsList) {
		// 订单来源 3B2C、6B2B
		Integer source = master.getSource();
		if (source == null) {
			source = 0;
		}

		Map<String, PlatformSkuStock> stockMap = new HashMap<String, PlatformSkuStock>(Constant.DEFAULT_MAP_SIZE);
		for (MasterOrderGoods orderGoods : goodsList) {
			String sku = orderGoods.getCustomCode();
			// 相同SKU合并提交占用库存
			PlatformSkuStock skuStock = stockMap.get(sku);
			if (skuStock == null) {
				skuStock = new PlatformSkuStock();
			}
			skuStock.setSku(sku);
			if (source == 6) {
				// 按箱规数量扣除
				int stockNum = orderGoods.getGoodsNumber();
				Short boxGauge = orderGoods.getBoxGauge();
				if (boxGauge != null && boxGauge > 1) {
					stockNum = stockNum * boxGauge;
				}
				skuStock.setStock(skuStock.getStock() + stockNum);
			} else {
				skuStock.setStock(skuStock.getStock() + orderGoods.getGoodsNumber());
			}
			stockMap.put(sku, skuStock);
		}
		return stockMap;
	}

    /**
     * 处理订单商品预占库存结果
     * @param goodsList
     */
	private void processGoodsSendStock(List<MasterOrderGoods> goodsList) {
        // 更新商品占用库存数据
        for (MasterOrderGoods goods : goodsList) {
            MasterOrderGoods updateGoods = new MasterOrderGoods();
            updateGoods.setId(goods.getId());
            updateGoods.setChannelSendNumber(goods.getGoodsNumber());
            masterOrderGoodsMapper.updateByPrimaryKeySelective(updateGoods);
        }
    }

	@Override
	public ReturnInfo preOccupyByGoodsList(String masterOrderSn, String shopCode, List<MasterOrderGoods> goodsList) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			ri.setMessage("平台库存占用（预扣）: 传入订单号为空");
			return null;
		}
		logger.info(" start 平台库存占用（预扣）：masterOrderSn=" + masterOrderSn);
		MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
		if (null == master) {
			ri.setMessage("订单" + masterOrderSn + "不存在,不能占用库存");
			return ri;
		}
		// 占用库存，且占用释放库存只针对未发货发货单商品。
		List<StockGoods> list = null;
		// 查询需占用库存商品
		try {
			Map<String, PlatformSkuStock> stockMap = new HashMap<String, PlatformSkuStock>();
			boolean isDepot = false;
			for (MasterOrderGoods orderGoods : goodsList) {
				String sku = orderGoods.getCustomCode();
				// 相同SKU合并提交占用库存
				PlatformSkuStock skuStock = stockMap.get(sku);
				if (skuStock == null) {
					skuStock = new PlatformSkuStock();
				}
				/*if (!orderGoods.getDepotCode().equals(Constant.DETAILS_DEPOT_CODE)) {
					isDepot = true;
					skuStock.setDepotNo(orderGoods.getDepotCode());
				}*/
				skuStock.setSku(sku);
				skuStock.setStock(skuStock.getStock() + orderGoods.getGoodsNumber());
				stockMap.put(sku, skuStock);
			}

			List<PlatformSkuStock> skuStockList = new ArrayList<PlatformSkuStock>();
			for (String sku : stockMap.keySet()) {
				skuStockList.add(stockMap.get(sku));
			}
			// 已经全部占用库存
			if (!StringUtil.isListNotNull(skuStockList)) {
				ri.setMessage(masterOrderSn + "已经占用过库存！");
				return ri;
			}
			StockOperatePO bgpo = new StockOperatePO();
			bgpo.setBusinessId(masterOrderSn);
			bgpo.setChannelCode(shopCode);
			bgpo.setSkuStockList(skuStockList);
			bgpo.setSiteCode(master.getChannelCode());
			bgpo.setDepot(isDepot);
			boolean flag = false;
			for (int i = 0; i < 3; i++) {
				if (flag) {
					break;
				}
				try {
					logger.info("平台库存占用（预扣）：masterOrderSn= " + masterOrderSn + "json=" + JSON.toJSONString(bgpo));
					PlatformStockOperateRst result = stockServiceByShop.occupy(bgpo);
//					PlatformStockOperateRst result = stockNewService.occupy(bgpo);
//					PlatformStockOperateRst result = new PlatformStockOperateRst();
					logger.info("平台库存占用（预扣）：masterOrderSn= " + masterOrderSn + "result=" + JSON.toJSONString(result));

					flag = true;
					if (result != null && "1".equals(result.getReturnCode())) {
						ri.setIsOk(Constant.OS_YES);
						ri.setMessage(result.getReturnMsg());
					} else {
						ri.setMessage(result.getReturnMsg());
					}
				} catch (Exception e) {
					logger.error(masterOrderSn + "平台库存占用（预扣）" + e.getMessage(), e);
					ri.setMessage("调用平台释放库存异常：" + e.getMessage());
				}
			}
		
		} catch (Exception e) {
			logger.error(masterOrderSn + "平台库存占用（预扣）, orderSn:" + masterOrderSn, e);
			ri.setMessage("平台库存占用（预扣）, orderSn:" + masterOrderSn);
		}
		return ri;
	}

	@Override
	public ReturnInfo realese(StockOperatePO bgop) {
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (bgop == null) {
			ri.setMessage("调用平台释放库存: 传入参数为空");
			return null;
		}
		logger.info("start 平台释放库存：orderSn=" + bgop.getBusinessId() + ";shopCode=" + bgop.getChannelCode());
		boolean flag = false;
		for (int i = 0; i < 3; i++) {
			if (flag) {
				break;
			}
			try {
				logger.info("平台释放库存：orderSn= " + bgop.getBusinessId() + "json=" + JSON.toJSONString(bgop));
				PlatformStockOperateRst result = stockServiceByShop.release(bgop);
				logger.info("平台释放库存：orderSn=" + bgop.getBusinessId() + "JSON=" + JSON.toJSONString(result));
				flag = true;
				if (result != null && "1".equals(result.getReturnCode())) {
					ri.setIsOk(Constant.OS_YES);
					ri.setMessage(result.getReturnMsg());
				} else {
					ri.setMessage(result.getReturnMsg());
				}
			} catch (Exception e) {
				logger.error(bgop.getBusinessId() + "调用平台释放库存异常：" + e.getMessage(), e);
				ri.setMessage("调用平台释放库存异常：" + e.getMessage());
			}
		}
		return ri;
	}

	@Override
	public ReturnInfo<Integer> queryStockBySku(String sku, String shopCode, String siteCode, String depotCode) {
		ReturnInfo<Integer> ri = new ReturnInfo<Integer>(Constant.OS_NO);
		logger.info("查询库存：sku=" + sku + ";shopCode=" + shopCode);
		try {
//			int stock = stockNewService.platformGetStock(sku, shopCode, siteCode, depotCode);
			int stock = stockServiceByShop.platformGetStock(sku, shopCode, siteCode, depotCode);
			ri.setIsOk(Constant.OS_YES);
			ri.setMessage("查询成功");
			ri.setData(stock);
		} catch (Exception e) {
			logger.error("查询库存：sku=" + sku + ";shopCode=" + shopCode + ";异常：" + e.getMessage(), e);
			ri.setMessage("调用库存查询异常：" + e.getMessage());
		}
		return ri;
	}

	@Override
	public void checkAndDeductWithoutStockInventory(String masterOrderSn, List<MasterOrderGoods> masterOrderGoodsList) {
		logger.info("归还无库存下单补上的库存,MasterOrderSn=" + masterOrderSn);
		try {
			if (!CollectionUtils.isEmpty(masterOrderGoodsList)) {
				for (MasterOrderGoods masterOrderGoods : masterOrderGoodsList) {
					if (0 < masterOrderGoods.getWithoutStockNumber()) {
						WithoutStockSyncDepotBean withoutStockSyncDepotBean = new WithoutStockSyncDepotBean();
						withoutStockSyncDepotBean.setSku(masterOrderGoods.getGoodsSn());
						withoutStockSyncDepotBean.setDepotNo(masterOrderGoods.getWithoutStockDepotNo());
						withoutStockSyncDepotBean.setOrderType(Constant.WKCCK);
						withoutStockSyncDepotBean.setOperUser(Constant.OS_STRING_SYSTEM);
						//需要补充的库存即为走无库存下单的量
						withoutStockSyncDepotBean.setStock(masterOrderGoods.getWithoutStockNumber());
						withoutStockSyncDepotBean.setMemo("订单号:" + masterOrderSn);
						logger.info("无库存下单调减少库存withoutStockSyncDepotBean=" + JSONObject.toJSONString(withoutStockSyncDepotBean));
						StockServiceBean<String> stringStockServiceBean = depotProcessService.withoutStockProcessDepotStock(withoutStockSyncDepotBean);
						logger.info("无库存下单调减少库存response=" + JSONObject.toJSONString(stringStockServiceBean));
					}
				}
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "归还无库存下单补上的库存失败", e);
		}
	}

	/**
	 * 订单取消释放库存
	 * @param masterOrderSn 订单编码
	 * @return ReturnInfo
	 */
	@Override
	public ReturnInfo cancelRealese(String masterOrderSn) {
		logger.info("订单[" + masterOrderSn + "]取消释放库存");
		ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			ri.setMessage("订单释放库存: 传入订单号为空");
			return null;
		}
		// 查询需占用库存商品
		try {
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if (null == master) {
				ri.setMessage("订单" + masterOrderSn + "不存在,不能占用库存");
				return ri;
			}
			// .andChannelSendNumberEqualTo((short)1);
			MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
			goodsExample.or().andMasterOrderSnEqualTo(masterOrderSn).andChannelSendNumberGreaterThan((short)0);
			List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
			boolean isPay = false;
			// 如果订单已支付,并且主订单已取消，配送单的状态不是未发货，不需要返回库存
			if (Constant.OI_PAY_STATUS_PAYED == master.getPayStatus()) {
				isPay = true;
			}

			if (isPay) {
				ri.setMessage("订单" + masterOrderSn + "已付款,不返回库存");
				//订单支付后如果被审核驳回，需要减去无库存下单补得库存
				checkAndDeductWithoutStockInventory(masterOrderSn, goodsList);
				return ri;
			}
			
			if (master.getShipStatus() > Constant.OI_SHIP_STATUS_UNSHIPPED) {
				if (isPay) {
					ri.setMessage("订单" + masterOrderSn + "发货状态不为未发货");
					return ri;
				}
			}
			if (StringUtil.isListNull(goodsList)) {
				ri.setMessage("订单[" + masterOrderSn + "] 已经占用过库存");
				ri.setIsOk(Constant.OS_YES);
				return ri;
			}

			Map<String, PlatformSkuStock> stockMap = processGoodsStock(master, goodsList);

			List<PlatformSkuStock> skuStockList = new ArrayList<PlatformSkuStock>();
			for (String sku : stockMap.keySet()) {
				PlatformSkuStock skuStock = stockMap.get(sku);
				if (isPay) {
					skuStock.setReturnOrder(true);
				} else {
					skuStock.setReturnOrder(false);
				}
				skuStockList.add(skuStock);
			}

			// 已经全部占用库存
			if (!StringUtil.isListNotNull(skuStockList)) {
				ri.setMessage(masterOrderSn + "已经释放过库存！");
				return ri;
			}

			boolean isDepot = false;
			StockOperatePO bgpo = new StockOperatePO();
			bgpo.setBusinessId(masterOrderSn);
			bgpo.setChannelCode(master.getOrderFrom());
			bgpo.setSkuStockList(skuStockList);
			bgpo.setSiteCode(master.getChannelCode());
			bgpo.setDepot(isDepot);
			boolean flag = false;
			for (int i = 0; i < 3; i++) {
				if (flag) {
					break;
				}
				try {
					logger.info("订单释放库存：masterOrderSn= " + masterOrderSn + "json=" + JSON.toJSONString(bgpo));
					PlatformStockOperateRst result = stockServiceByShop.release(bgpo);
					logger.info("订单释放库存：masterOrderSn= " + masterOrderSn + "result=" + JSON.toJSONString(result));
					flag = true;
					if (result != null && Constant.OS_STR_YES.equals(result.getReturnCode())) {
						ri.setIsOk(Constant.OS_YES);
						ri.setMessage("释放成功！");
						logger.info("订单[" + masterOrderSn + "]商城库存释放结果：成功");
						try {
							// 更新商品占用库存数据
							for (MasterOrderGoods goods : goodsList) {
								MasterOrderGoods updateGoods = new MasterOrderGoods();
								updateGoods.setId(goods.getId());
								updateGoods.setChannelSendNumber(0);
								masterOrderGoodsMapper.updateByPrimaryKeySelective(updateGoods);
							}
						} catch (Exception e) {
							logger.error("订单[" + masterOrderSn + "]OMS库存释放数据处理异常" + e.getMessage(), e);
							ri.setIsOk(Constant.OS_ERROR);
							ri.setMessage("订单[" + masterOrderSn + "]OMS库存释放数据处理异常" + e.getMessage());
						}
						ri.setIsOk(Constant.OS_YES);
						ri.setMessage(result.getReturnMsg());
						//订单取消后，需要减去无库存下单补得库存
						checkAndDeductWithoutStockInventory(masterOrderSn, goodsList);
					} else {
						ri.setMessage(result.getReturnMsg());
					}
				} catch (Exception e) {
					logger.error(masterOrderSn + "商城库存释放" + e.getMessage(), e);
					ri.setMessage("调用平台释放库存异常：" + e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "商城库存释放, orderSn:" + masterOrderSn + e.getMessage(), e);
			ri.setMessage("商城库存释放, orderSn:" + masterOrderSn + e.getMessage());
		}
		return ri;
	}
}