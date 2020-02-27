package com.work.shop.oms.jms.listener;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.stock.center.bean.StockServiceBean;
import com.work.shop.stock.center.dto.WithoutStockSyncDepotBean;
import com.work.shop.stock.center.feign.DepotProcessService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.mq.listener.Consumer;
import com.work.shop.oms.stock.service.ChannelStockService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 订单库存释放消费
 * @author QuYachu
 */
@Service
public class OrderStockRealeseConsumer extends Consumer {

	private static Logger logger = Logger.getLogger(OrderStockRealeseConsumer.class);

	@Resource
	private ChannelStockService channelStockService;
	@Autowired
	private DepotProcessService depotProcessService;


	@Override
	public String getDATA(String text) {
		try {
			logger.info("order_stock_realese:" + text);
			if (StringUtil.isTrimEmpty(text)) {
				logger.error("订单库存释放操作:参数为空");
				return Constant.OS_STR_NO;
			}
			OrderStatus orderStatus = JSON.parseObject(text, OrderStatus.class);
			if (orderStatus == null) {
				logger.error("异步处理订单库存释放操作:参数为空");
				return Constant.OS_STR_NO;
			}
			// 创建退单
			ReturnInfo<List<MasterOrderGoods>> listReturnInfo = null;
			if (Constant.order_type_distribute.equals(orderStatus.getType())) {
				//orderStockService.realeseByOrderSn(orderStatus.getOrderSn(), orderStatus.getMasterOrderSn());
			} else {
				listReturnInfo = channelStockService.cancelRealese(orderStatus.getMasterOrderSn());
			}
			//取消订单成功后，归还无库存下单补上的库存
			if (null != listReturnInfo && Constant.OS_YES == listReturnInfo.getIsOk()) {
				logger.info("归还无库存下单补上的库存,MasterOrderSn=" + orderStatus.getMasterOrderSn());
				try {
					if (!CollectionUtils.isEmpty(listReturnInfo.getData())) {
						for (MasterOrderGoods masterOrderGoods : listReturnInfo.getData()) {
							if (0 < masterOrderGoods.getWithoutStockNumber()) {
								WithoutStockSyncDepotBean withoutStockSyncDepotBean = new WithoutStockSyncDepotBean();
								withoutStockSyncDepotBean.setSku(masterOrderGoods.getGoodsSn());
								withoutStockSyncDepotBean.setDepotNo(masterOrderGoods.getWithoutStockDepotNo());
								withoutStockSyncDepotBean.setOrderType(Constant.WKCCK);
								withoutStockSyncDepotBean.setOperUser("");
								//需要补充的库存即为走无库存下单的量
								withoutStockSyncDepotBean.setStock(masterOrderGoods.getWithoutStockNumber());
								withoutStockSyncDepotBean.setMemo("订单号:" + orderStatus.getMasterOrderSn());
								logger.info("无库存下单调减少库存withoutStockSyncDepotBean=" + JSONObject.toJSONString(withoutStockSyncDepotBean));
								StockServiceBean<String> stringStockServiceBean = depotProcessService.withoutStockProcessDepotStock(withoutStockSyncDepotBean);
								logger.info("无库存下单调减少库存response=" + JSONObject.toJSONString(stringStockServiceBean));
							}
						}
					}
				} catch (Exception e) {
					logger.error(text + "归还无库存下单补上的库存失败", e);
				}
			}
		} catch (Exception e) {
			logger.error(text + "订单库存释放操作失败", e);
		}
		return Constant.OS_STR_YES;
	}
}
