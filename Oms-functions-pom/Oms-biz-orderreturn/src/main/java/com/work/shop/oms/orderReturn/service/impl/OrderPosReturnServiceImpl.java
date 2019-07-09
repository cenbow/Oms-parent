package com.work.shop.oms.orderReturn.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.bean.DistributeAction;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderGoodsChargeBack;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.common.paraBean.ParaBackGoodsBean;
import com.work.shop.oms.common.paraBean.ParaChargebackBean;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderGoodsChargeBackMapper;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.orderReturn.service.OrderPosReturnService;
import com.work.shop.oms.utils.StringUtil;


@Service
public class OrderPosReturnServiceImpl implements OrderPosReturnService {
	private static Logger logger = LoggerFactory.getLogger(OrderPosReturnServiceImpl.class);

	@Resource
	private OrderGoodsChargeBackMapper orderGoodsChargeBackMapper;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;
	@Resource
	private DistributeActionService distributeActionService;
	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Override
	public void saveOrderGoodsChargeBackData(OrderGoodsChargeBack paraBean) {
		logger.info("保存门店接口退单记录请求paraBean:" + JSON.toJSONString(paraBean));
		if(paraBean==null){
			logger.warn("function saveOrderGoodsChargeBackData para Bean is null");
			return;
		}
		if(paraBean.getAddTime()==null){
			paraBean.setAddTime(new Date());
		}
		orderGoodsChargeBackMapper.insertSelective(paraBean);
	}

	@Override
	public ServiceReturnInfo<OrderDistribute> createPosReturnApplication(ParaChargebackBean paraBean) {
		ServiceReturnInfo<OrderDistribute> result = new ServiceReturnInfo<OrderDistribute>(false, "门店退单处理失败", null);
		try {
			List<MasterOrderGoods> updateGoodsList = new ArrayList<MasterOrderGoods>();
			if (StringUtils.isBlank(paraBean.getOrder_sn()) || StringUtils.isBlank(paraBean.getShop_number())
					|| StringUtils.isBlank(paraBean.getOperatorStr()) || StringUtils.isBlank(paraBean.getReturn_pay_status())
					|| StringUtils.isBlank(paraBean.getReturn_pay_type()) || StringUtils.isBlank(paraBean.getReturn_ship_status())) {
				result.setMessage("必传参数为空,请核实参数!");
				return result;
			}
			OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(paraBean.getOrder_sn());
			if (distribute == null) {
				result.setMessage("订单[" + paraBean.getOrder_sn() + "]不存在,请核实参数!");
				return result;
			}
			List<ParaBackGoodsBean> paraGoodsList = paraBean.getGoodsList();
			for (ParaBackGoodsBean paraBackGoodsBean : paraGoodsList) {
				if (paraBackGoodsBean.getGoods_sum() <= 0) {
					result.setMessage("退单商品数量小于等于0,请核实参数!");
					return result;
				}
				MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
				List<MasterOrderGoods> list = masterOrderGoodsMapper.selectByExample(goodsExample);
				if (StringUtil.isListNull(list)) {
					result.setMessage("退单:" + paraBean.getOrder_sn() + "中对应sku商品" + paraBackGoodsBean.getSku_sn() + "信息不存在,请核实参数!");
					return result;
				}
				int returnCount = paraBackGoodsBean.getGoods_sum();
				for (MasterOrderGoods orderGoods : list) {
					int retreatCount = orderGoods.getGoodsNumber() - orderGoods.getChargeBackCount();
					if (retreatCount > 0 && returnCount > 0) {
						MasterOrderGoods updateGoods = new MasterOrderGoods();
						updateGoods.setId(orderGoods.getId());
						updateGoods.setCustomCode(updateGoods.getCustomCode());
						if (retreatCount >= returnCount) {
							updateGoods.setChargeBackCount(orderGoods.getChargeBackCount() + returnCount);
							returnCount = 0;
						} else {
							updateGoods.setChargeBackCount(orderGoods.getChargeBackCount() + retreatCount);
							returnCount -= retreatCount;
						}
						updateGoodsList.add(updateGoods);
					}
				}
				if (returnCount > 0) {
					result.setMessage("退单:" + paraBean.getOrder_sn() + "中对应sku商品" + paraBackGoodsBean.getSku_sn() + "退单商品数量大于购买数量,请核实参数!");
					return result;
				}
				if (StringUtil.isListNull(updateGoodsList)) {
					result.setMessage("退单:" + paraBean.getOrder_sn() + "中对应sku商品" + paraBackGoodsBean.getSku_sn() + "没有可退订单商品,请核实参数!");
					return result;
				}
				for (MasterOrderGoods updateGoods : updateGoodsList) {
					masterOrderGoodsMapper.updateByPrimaryKeySelective(updateGoods);
				}
			}
			saveOrderGoodsChargeBackData(paraBean);
			saveOrderActionFuncton(paraBean, distribute);
			//&&StringUtils.equals("微信精品商城", refererStr)
			result.setResult(distribute);
		} catch (Exception e) {
			logger.error("function shopChargebackFunction" + e.getMessage(), e);
			result.setMessage("门店退单接口异常,请重新请求!" + e.getMessage());
		}
		return result;
	}
	
	/**
	 * 保存门店接口退单记录请求
	 * 
	 * @param paraBean
	 */
	protected void saveOrderGoodsChargeBackData(ParaChargebackBean paraBean) {
		try {
			if (paraBean != null && paraBean.getGoodsList() != null) {
				for (ParaBackGoodsBean paraBackGoodsBean : paraBean.getGoodsList()) {
					OrderGoodsChargeBack orderGoodsBackbean = new OrderGoodsChargeBack();
					orderGoodsBackbean.setBackMsg(paraBean.getBack_msg() + " 明细:" + paraBackGoodsBean.getReturn_msg());
					BigDecimal bigBean = new BigDecimal(paraBackGoodsBean.getGoods_money());
					orderGoodsBackbean.setGoodsPrice(bigBean);
					orderGoodsBackbean.setGoodsSum(paraBackGoodsBean.getGoods_sum());
					orderGoodsBackbean.setOperator(paraBean.getOperatorStr());
					orderGoodsBackbean.setOrderSn(paraBean.getOrder_sn());
					BigDecimal bigshipBean = new BigDecimal(paraBean.getShipping());
					orderGoodsBackbean.setShipping(bigshipBean);
					orderGoodsBackbean.setShopNumber(paraBean.getShop_number());
					orderGoodsBackbean.setSkuSn(paraBackGoodsBean.getSku_sn());
					orderGoodsBackbean.setReturnPayStatus(paraBean.getReturn_pay_status());
					orderGoodsBackbean.setReturnPayType(paraBean.getReturn_pay_type());
					orderGoodsBackbean.setReturnShipStatus(paraBean.getReturn_ship_status());
					orderGoodsBackbean.setBankCard(paraBean.getBank_card());
					orderGoodsBackbean.setBankName(paraBean.getBank_name());
					orderGoodsBackbean.setWxOrderno(paraBean.getWx_orderNo());
					BigDecimal returnAmountaleBean = new BigDecimal(paraBean.getReturn_amount());
					orderGoodsBackbean.setReturnAmount(returnAmountaleBean);
					BigDecimal GoodsAmountaleBean = new BigDecimal(paraBackGoodsBean.getGoods_amount());
					orderGoodsBackbean.setGoodsAmount(GoodsAmountaleBean);
					saveOrderGoodsChargeBackData(orderGoodsBackbean);
				}
			}
		} catch (Exception e) {
			logger.error("function saveOrderGoodsChargeBackData", e);
		}
	}

	/**
	 * 保存订单操作日志表
	 * 
	 * @param paraBean
	 */
	protected void saveOrderActionFuncton(ParaChargebackBean paraBean, OrderDistribute distribute) {
		DistributeAction orderAction = new DistributeAction();
		orderAction.setOrderSn(paraBean.getOrder_sn());
		for (ParaBackGoodsBean paraBackGoodsBean : paraBean.getGoodsList()) {
			if (distribute != null) {
				orderAction.setActionUser(paraBean.getShop_number() + "->System");
				orderAction.setOrderStatus(distribute.getOrderStatus());
				orderAction.setPayStatus(distribute.getPayStatus());
				orderAction.setQuestionStatus(distribute.getQuestionStatus().byteValue());
				orderAction.setRangeStatus((byte) -1);
				orderAction.setShippingStatus(distribute.getShipStatus());
				orderAction.setActionNote("门店请求订单退单请求 订单号:" + paraBean.getOrder_sn() + " 商品sku:" + paraBackGoodsBean.getSku_sn() + " 退单数量:"
						+ paraBackGoodsBean.getGoods_sum());
				distributeActionService.saveOrderAction(orderAction);
			}
		}
	}
}
