package com.work.shop.oms.express.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.api.express.service.OrderExpressCompanyService;
import com.work.shop.oms.api.express.service.OrderExpressService;
import com.work.shop.oms.api.express.service.OrderExpressTracingService;
import com.work.shop.oms.bean.OrderDepotShip;
import com.work.shop.oms.bean.OrderDepotShipExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.bean.OrderExpressTracingExample;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderExpressTracingMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.dao.OrderReturnShipMapper;
import com.work.shop.oms.dao.SystemShippingMapper;
import com.work.shop.oms.express.bean.ExpressContent;
import com.work.shop.oms.express.bean.ExpressInfo;
import com.work.shop.oms.express.utils.HttpClientUtil;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;

@Service
public class OrderExpressServiceImpl implements OrderExpressService {

	private Logger logger = Logger.getLogger(OrderExpressServiceImpl.class);

	@Resource(name = "orderExpressPullProviderJmsTemplate")
	private JmsTemplate orderExpressJmsTemplate;
	@Resource(name = "returnExpressPullProviderJmsTemplate")
	private JmsTemplate returnExpressjmsTemplate;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private OrderDepotShipMapper orderDepotShipMapper;
	@Resource
	private OrderExpressTracingService orderExpressTracingService;
	@Resource
	private OrderExpressCompanyService orderExpressCompanyService;
	@Resource
	private SystemShippingMapper systemShippingMapper;
	@Resource
	private OrderExpressTracingMapper orderExpressTracingMapper;

	@Resource
	private OrderReturnMapper orderReturnMapper;

	@Resource
	private OrderReturnShipMapper orderReturnShipMapper;
	
	/**
	 * 订单物流写入
	 * @param orderSn 订单编码
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> orderExpress(String orderSn) {
		logger.info("订单物流写入 start: orderSn=" + orderSn);
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		try {
			final String msg = orderSn;
			orderExpressJmsTemplate.send(new MessageCreator() {

				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(msg);
				}
			});
			info.setIsOk(Constant.OS_YES);
			info.setMessage("success");
		} catch (Exception e) {
			logger.error("订单物流写入: orderSn=" + orderSn + e.getMessage(), e);
		}
		logger.info("订单物流写入 end: orderSn=" + orderSn);
		return info;
	}

	/**
	 * 退单物流写入
	 * 
	 * @param orderReturn
	 * @param orderReturnShip
	 * @return ReturnInfo<String>
	 */
	@Override
	public ReturnInfo<String> orderReturnExpress(String returnSn) {
		logger.info("退单物流写入 start: returnSn=" + returnSn);
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(returnSn)) {
			
		}
		OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
		if (orderReturn == null) {
			info.setMessage("退单为空");
			return info;
		}
		OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
		if (orderReturnShip == null) {
			info.setMessage("退单配货单为空");
			return info;
		}
		try {
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("orderReturn", orderReturn);
			map1.put("orderReturnShip", orderReturnShip);
			final Map<String, Object> map2 = map1;
			returnExpressjmsTemplate.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					String msg = JSON.toJSONString(map2);
					return session.createTextMessage(msg);
				}
			});
			info.setIsOk(Constant.OS_YES);
			info.setMessage("success");
		} catch (Exception e) {
			logger.error("退单物流写入 :returnSn=" + orderReturn.getReturnSn() + e.getMessage(), e);
		}
		logger.info("退单物流写入 end: returnSn" + orderReturn.getReturnSn());
		return info;
	}

    /**
     * 订单查询物流信息
     * @param tracing
     * @return ReturnInfo<List<ExpressInfo>>
     */
	@Override
	public ReturnInfo<List<ExpressInfo>> orderExpressQuery(OrderExpressTracing tracing) {
		ReturnInfo<List<ExpressInfo>> info = new ReturnInfo<>(Constant.OS_NO, "oms订单物流查询失败");
		try {
			logger.debug("oms订单物流查询.begin-tracing:" + JSON.toJSONString(tracing));
			String orderSn = tracing.getOrderSn();
			if (StringUtil.isTrimEmpty(orderSn)) {
				info.setMessage("单号为空");
				return info;
			}
			// 查询在途信息表
			OrderExpressTracingExample tracingExample = new OrderExpressTracingExample();
			OrderExpressTracingExample.Criteria criteria = tracingExample.or();
			criteria.andOrderSnEqualTo(orderSn);
			// 快递单号
			if (StringUtil.isNotEmpty(tracing.getTrackno())) {
				criteria.andTracknoEqualTo(tracing.getTrackno());
			}
			List<OrderExpressTracing> list = orderExpressTracingMapper.selectByExampleWithBLOBs(tracingExample);
			if (CollectionUtils.isEmpty(list)) {
				// 重新执行订单物流信息写入在途信息表
				orderExpress(orderSn);
				info.setMessage("订单在途信息不存在，确认后重试");
				return info;
			}
			info.setData(getExpressInfo(list, orderSn));
			info.setIsOk(Constant.OS_YES);
			info.setMessage("查询成功");
		} catch (Exception e) {
			logger.error("oms订单物流查询.tracing:" + JSON.toJSONString(tracing), e);
		}
		logger.debug("oms订单物流查询.end-tracing:" + JSON.toJSONString(tracing));
		return info;
	}

	@Override
	public ReturnInfo<List<ExpressInfo>> orderExpressOmsQuery(OrderExpressTracing tracing) {
		ReturnInfo<List<ExpressInfo>> info = new ReturnInfo<>(Constant.OS_NO, "oms订单物流查询失败");
		try {
			logger.debug("oms订单物流查询.begin-tracing:" + JSON.toJSONString(tracing));
			String orderSn = tracing.getOrderSn();
			String trackno = tracing.getTrackno();
			String depotCode = tracing.getDepotCode();
			ReturnInfo<String> checkInfo = checkQueryConditions(tracing);
			if (checkInfo.getIsOk() == Constant.OS_NO) {
				info.setMessage(checkInfo.getMessage());
				return info;
			}
			// 查询在途信息表
			OrderExpressTracingExample tracingExample = new OrderExpressTracingExample();
			tracingExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(depotCode).andTracknoEqualTo(trackno);
			List<OrderExpressTracing> list = orderExpressTracingMapper.selectByExampleWithBLOBs(tracingExample);
			if (CollectionUtils.isEmpty(list)) {
				// 重新执行订单物流信息写入在途信息表
				orderExpress(orderSn);
				info.setMessage("该订单下快递单号[" + trackno + "]不存在,确认查询条件后重试");
				return info;
			}
			logger.debug("oms订单物流查询.orderSn:" + orderSn + ",list:" + JSON.toJSONString(list));
			info.setData(getExpressInfo(list, orderSn));
			info.setIsOk(Constant.OS_YES);
			info.setMessage("查询成功");
		} catch (Exception e) {
			logger.error("oms订单物流查询.tracing:" + JSON.toJSONString(tracing), e);
		}
		logger.debug("oms订单物流查询.end-tracing:" + JSON.toJSONString(tracing));
		return info;
	}

	/**
	 * 通过快递100api获取物流信息
	 * @param orderSn 订单号
	 * @param companyCode
	 * @param trackNo 快递单号
	 * @return ExpressInfo
	 */
	public ExpressInfo getExpressMessage(String orderSn, String companyCode, String trackNo) {
		ExpressInfo express = null;
		express = HttpClientUtil.getKuaidi100NEW(trackNo, companyCode, null, null);
		if (express == null) {
			express = new ExpressInfo();
		}
		logger.info("trackNo =" + trackNo + " :" + JSON.toJSONString(express));
		if (express == null || StringUtil.isListNull(express.getData())) {
			return express;
		}
		List<OrderExpressTracing> expressTracings = orderExpressTracingService.selectExpressByOrderSnAndtrackno(orderSn, trackNo);
		if (StringUtil.isListNull(expressTracings)) {
			return express;
		}
		OrderExpressTracing oet = expressTracings.get(0);
		try {
			OrderExpressTracing newTracing = newOrderExpressTracing(oet, express, companyCode);
			int count = orderExpressTracingService.updateTracing(newTracing);
			logger.info("快递100 Udate Count" + "*******" + count + "=====");
		} catch (Exception e) {
			logger.error("订单[" + orderSn + "] trackNo" + trackNo + "更新快点信息异常：" + e.getMessage(), e);
		}
		return express;
	}

	/**
	 * 更新在途物流信息
	 * 
	 * @param old
	 * @param info
	 * @param expressName
	 * @return
	 * @throws ParseException
	 */
	public OrderExpressTracing newOrderExpressTracing(OrderExpressTracing old, ExpressInfo info, String expressName)
			throws ParseException {
		if (info == null || info.getData() == null || info.getData().size() < 1) {
			return old;
		}
		old.setNote(JSON.toJSONString(info));
		List<com.work.shop.oms.express.bean.ExpressContent> list = info.getData();
		// int site = 0;
		// 10，快递取件；11，运输中；11.1，运输中(上干线)；11.2，运输中(到达目的地)；12，派件中；13，客户签收；14，客户拒签；15，货物遗失；16，货物损毁）
		float expressStatus = 0.0f;
		try {
			expressStatus = info.getState();
			if (expressStatus == Constant.express_status_sign || expressStatus == Constant.express_status_returnSign) {
				old.setExpressTimeSign(TimeUtil.parseString2Date(list.get(0).getTime()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		old.setExpressStatus(expressStatus);
		old.setUpdTime(new Date());
		// old.setFetchFlag(new Byte("1"));
		return old;
	}

	/**
	 * 检查物流查询条件
	 * 
	 * @param tracing
	 * @return
	 */
	private ReturnInfo<String> checkQueryConditions(OrderExpressTracing tracing) {
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO, "查询条件不满足");
		if (StringUtil.isTrimEmpty(tracing.getOrderSn())) {
			info.setMessage("单号为空");
			return info;
		}
		if (StringUtil.isTrimEmpty(tracing.getTrackno())) {
			info.setMessage("快递单号为空");
			return info;
		}
		if (StringUtil.isTrimEmpty(tracing.getDepotCode())) {
			info.setMessage("仓编码为空");
			return info;
		}
		info.setIsOk(Constant.OS_YES);
		info.setMessage("查询条件满足");
		return info;
	}

	/**
	 * 组装查询在途物流信息
	 * 
	 * @param items 物流信息列表
	 * @param orderSn 订单号
	 * @return List<ExpressInfo>
	 */
	private List<ExpressInfo> getExpressInfo(List<OrderExpressTracing> items, String orderSn) {
		logger.debug("oms订单物流查询.orderSn:" + orderSn + ",list:" + JSON.toJSONString(items));
		List<ExpressInfo> infoList = new ArrayList<ExpressInfo>();

		if (items == null || items.size() == 0) {
		    return infoList;
        }

        for (OrderExpressTracing oet : items) {
            try {
                // 在途信息表数据不为空
                if (oet.getNote() != null && oet.getNote().trim().length() > 10) {
                    if (oet.getNote().trim().charAt(0) != '{') {
                        infoList.add(getExpressMessage(orderSn, oet.getCompanyCode(), oet.getTrackno()));
                    } else {
                        ExpressInfo in = JSON.parseObject(oet.getNote(), ExpressInfo.class);
                        List<ExpressContent> data = in.getData();
                        if (data.get(0).getTime().compareTo(data.get(data.size() - 1).getTime()) < 0) {
                            List<ExpressContent> temp = new ArrayList<ExpressContent>();
                            for (int i = data.size(); i > 0; i--) {
                                temp.add(data.get(i - 1));
                            }
                            in.setData(temp);
                        }
                        infoList.add(in);
                    }
                } else {
                    // 若物流信息为空时，直接查询快递100API获取快递信息
                    if (oet.getCompanyCode() != null && oet.getTrackno() != null) {
                        infoList.add(getExpressMessage(orderSn, oet.getCompanyCode(), oet.getTrackno()));
                    }
                }
            } catch (Exception e) {
                logger.error("组装查询在途物流信息" + oet.getOrderSn() + e.getMessage(), e);
            }
        }

		return infoList;
	}
}
