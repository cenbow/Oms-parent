package com.work.shop.oms.order.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.order.service.*;
import com.work.shop.oms.utils.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.common.bean.GoodsReturnChangeInfoVO;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.GoodsReturnChangeActionMapper;
import com.work.shop.oms.dao.GoodsReturnChangeMapper;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.define.GoodsReturnChangePageListMapper;
import com.work.shop.oms.dao.define.OrderMapper;
import com.work.shop.oms.order.request.OmsBaseRequest;
import com.work.shop.oms.order.response.OmsBaseResponse;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单售后申请服务
 * @author lemon
 */
@Service
public class ApplyManagementServiceImpl implements ApplyManagementService {
	
	private static final Logger logger = Logger.getLogger(ApplyManagementServiceImpl.class);

	@Resource
	private GoodsReturnChangePageListMapper goodsReturnChangePageListMapper;

	@Resource
	private GoodsReturnChangeActionMapper goodsReturnChangeActionMapper;

	@Resource
	private GoodsReturnChangeMapper goodsReturnChangeMapper;

	@Resource(name="goodsReturnChangeService")
	private GoodsReturnChangeService goodsReturnChangeService;

	@Resource
	private OrderMapper orderMapper;

	@Resource
	private MasterOrderGoodsMapper masterOrderGoodsMapper;

	@Resource
	private MasterOrderInfoService masterOrderInfoService;

	@Resource
	private MasterOrderPayService masterOrderPayService;

	@Resource
	private MasterOrderActionService masterOrderActionService;

	/**
	 * 根据退换货申请单号, 获取申请日志
	 * @param returnChangeSn 申请单号
	 * @return OmsBaseResponse<GoodsReturnChangeAction>
	 */
	@Override
	public OmsBaseResponse<GoodsReturnChangeAction> findActionByReturnChangeSn(String returnChangeSn) {
		logger.info("查询退单申请单日志returnChangeSn:" + returnChangeSn);
		OmsBaseResponse<GoodsReturnChangeAction> response = new OmsBaseResponse<GoodsReturnChangeAction>();
		response.setSuccess(false);
		response.setMessage("查询失败");
		GoodsReturnChangeActionExample example = new GoodsReturnChangeActionExample();
		GoodsReturnChangeActionExample.Criteria criteria = example.or();
		criteria.andReturnchangeSnEqualTo(returnChangeSn);
		List<GoodsReturnChangeAction> actions = goodsReturnChangeActionMapper.selectByExampleWithBLOBs(example);
		response.setList(actions);
		response.setSuccess(true);
		response.setMessage("查询成功");
		return response;
	}

	/**
	 * 根据申请单id,获取申请单详情
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChange>
	 */
	@Override
	public OmsBaseResponse<GoodsReturnChange> findGoodsReturnChangeById(OmsBaseRequest<ApplyItem> request) {
		logger.info("查询退单申请单日志request:" + JSON.toJSONString(request));
		OmsBaseResponse<GoodsReturnChange> response = new OmsBaseResponse<GoodsReturnChange>();
		response.setSuccess(false);
		response.setMessage("查询退单申请单日志失败");
		if (request == null) {
			return response;
		}
		ApplyItem item = request.getData();
		if (item == null || item.getApplyId() == null) {
			response.setMessage("查询条件为空");
			return response;
		}
		try {
			GoodsReturnChange change = goodsReturnChangeMapper.selectByPrimaryKey(item.getApplyId());
			response.setData(change);
			response.setSuccess(true);
			response.setMessage("查询退单申请单日志成功");
		} catch (Exception e) {
			logger.error("查询退单申请单日志失败" + e.getMessage(), e);
			response.setMessage("查询退单申请单日志失败" + e.getMessage());
		}
		return response;
	}

	/**
	 * 修改申请单状态
	 * @param request 请求参数
	 * @return OmsBaseResponse<String>
	 */
	@Override
	public OmsBaseResponse<String> updateStatusBatch(OmsBaseRequest<ApplyItem> request) {
		logger.info("退单申请单变更状态:request" + JSON.toJSONString(request));
		OmsBaseResponse<String> response = new OmsBaseResponse<String>();
		response.setSuccess(false);
		response.setMessage("退单申请单变更状态失败");
		if (request == null) {
			return response;
		}
		ApplyItem item = request.getData();
		if (item == null) {
			response.setMessage("查询条件为空");
			return response;
		}
		List<Integer> applyIds = item.getApplyIds();
		if (StringUtil.isListNull(applyIds)) {
			response.setMessage("申请单ID列表不能为空");
			return response;
		}
		Integer applyStatus = item.getApplyStatus();
		if (applyStatus == null) {
			response.setMessage("申请单状态不能为空");
			return response;
		}
		try {
			for (Integer id : applyIds) {
				GoodsReturnChange goodsReturnChange = goodsReturnChangeMapper.selectByPrimaryKey(id);
				ReturnInfo<String> returnInfo = goodsReturnChangeService.updateStatus(applyStatus, id, goodsReturnChange.getOrderSn(), request.getActionUser(), "更新");
				if (returnInfo.getIsOk() == Constant.OS_NO) {
					response.setMessage(id + "退单申请单变更状态失败" + returnInfo.getMessage());
					return response;
				}
			}
			response.setSuccess(true);
			response.setMessage("退单申请单变更状态成功");
		} catch (Exception e) {
			logger.error("退单申请单变更状态失败" + e.getMessage(), e);
			response.setMessage("退单申请单变更状态失败" + e.getMessage());
		}
		return response;
	}

	/**
	 * 根据订单号，获取退换货申请单详情
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChangeInfoVO>
	 */
	@Override
	public OmsBaseResponse<GoodsReturnChangeInfoVO> findGoodsReturnChangeBySn(OmsBaseRequest<ApplyItem> request) {
		logger.info("退单申请单查询:request" + JSON.toJSONString(request));
		OmsBaseResponse<GoodsReturnChangeInfoVO> response = new OmsBaseResponse<GoodsReturnChangeInfoVO>();
		response.setSuccess(false);
		response.setMessage("查询失败");
		if (request == null) {
			return response;
		}
		ApplyItem item = request.getData();
		if (item == null) {
			response.setMessage("查询条件为空");
			return response;
		}
		String orderSn = item.getOrderSn();
		if (StringUtil.isTrimEmpty(orderSn)) {
			response.setMessage("查询单号为空");
			return response;
		}
		try {
			List<GoodsReturnChangeInfoVO> list = orderMapper.selectGoodsReturnChangeBySn(orderSn);
			for (GoodsReturnChangeInfoVO goodsReturnChangeInfoVO : list) {
				MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
				goodsExample.or().andMasterOrderSnEqualTo(orderSn).andCustomCodeEqualTo(goodsReturnChangeInfoVO.getCustumCode());
				List<MasterOrderGoods> orderGoods = masterOrderGoodsMapper.selectByExample(goodsExample);
				if(CollectionUtils.isEmpty(orderGoods)){
					throw new RuntimeException("无法获取有效的商品基础数据");
				}
				MasterOrderGoods masterOrderGoods = orderGoods.get(0);
				//加载商品基础数据
				goodsReturnChangeInfoVO.setGoodsName(masterOrderGoods.getGoodsName());
				goodsReturnChangeInfoVO.setGoodsSn(masterOrderGoods.getGoodsSn());
				goodsReturnChangeInfoVO.setColorName(masterOrderGoods.getGoodsColorName());
				goodsReturnChangeInfoVO.setSizeName(masterOrderGoods.getGoodsSizeName());
				goodsReturnChangeInfoVO.setGoodsNumber(orderGoods.size());
			}
			response.setSuccess(true);
			response.setMessage("查询成功");
			response.setList(list);
		} catch (Exception e) {
			logger.error("退单申请单变更状态失败" + e.getMessage(), e);
			response.setMessage("退单申请单变更状态失败" + e.getMessage());
		}
		return response;
	}

	/**
	 * 根据订单号，查询退换货申请单
	 * @param request 请求参数
	 * @return OmsBaseResponse<GoodsReturnChange>
	 */
	@Override
	public OmsBaseResponse<GoodsReturnChange> findGoodsReturnChangeByOrderSn(OmsBaseRequest<ApplyItem> request) {
		OmsBaseResponse<GoodsReturnChange> response = new OmsBaseResponse<GoodsReturnChange>();
		response.setSuccess(false);
		response.setMessage("查询失败");
		if (request == null) {
			return response;
		}
		ApplyItem item = request.getData();
		if (item == null) {
			response.setMessage("查询条件为空");
			return response;
		}
		String orderSn = item.getOrderSn();
		if (StringUtil.isTrimEmpty(orderSn)) {
			response.setMessage("查询单号为空");
			return response;
		}
		try {
			GoodsReturnChangeExample example = new GoodsReturnChangeExample();
			example.or().andOrderSnEqualTo(orderSn);
			List<GoodsReturnChange> changes = goodsReturnChangeMapper.selectByExample(example);
			response.setSuccess(true);
			response.setMessage("查询成功");
			response.setList(changes);
		} catch (Exception e) {
			logger.error("查询失败" + e.getMessage(), e);
			response.setMessage("查询失败" + e.getMessage());
		}
		return response;
	}

    /**
     * 更新订单商品价格
     * @param applyItem
     * @return
     */
    @Override
    public OmsBaseResponse<Boolean> updateOrderPrice(ApplyItem applyItem) {
        OmsBaseResponse<Boolean> response = new OmsBaseResponse<>();
        response.setMessage("更新订单商品价格失败");
        response.setSuccess(false);

        try {
            //校验参数,订单
            boolean priceCheck = updateOrderPriceCheck(applyItem, response);
            if (!priceCheck) {
                return response;
            }
            //查询订单商品信息
            String masterOrderSn = applyItem.getMasterOrderSn();

            //校验订单
            MasterOrderInfo masterOrderInfo = masterOrderInfoService.getOrderInfoBySn(masterOrderSn);
            if (masterOrderInfo == null) {
                response.setMessage("订单信息异常");
                return response;
            }
            //已取消订单不修改
            if (Constant.OI_ORDER_STATUS_CANCLED == masterOrderInfo.getOrderStatus()) {
                response.setMessage("订单已取消，无法更改订单商品价格");
                return response;
            }
            //已发货订单不修改
            if (Constant.OS_SHIPPING_STATUS_UNSHIPPED != masterOrderInfo.getShipStatus()) {
                response.setMessage("订单已发货，无法更改订单商品价格");
                return response;
            }
            //校验店铺
            String shopCode = applyItem.getShopCode();
            if (StringUtils.isNotBlank(shopCode) && !shopCode.equals(masterOrderInfo.getOrderFrom())) {
                response.setMessage(shopCode + "店铺下无此订单");
                return response;
            }

            //获取订单商品信息
            List<UpdateGoodsItem> updateGoodsItems = applyItem.getUpdateGoodsItems();
            List<String> skuList = new ArrayList<>();
            Map<String, BigDecimal> priceMap = new HashMap<String, BigDecimal>(updateGoodsItems.size());
            for (UpdateGoodsItem item : updateGoodsItems) {
                skuList.add(item.getSku());
                priceMap.put(item.getSku(), item.getGoodsPrice());
            }
            MasterOrderGoodsExample example = new MasterOrderGoodsExample();
            example.or().andMasterOrderSnEqualTo(masterOrderSn).andCustomCodeIn(skuList).andExtensionCodeEqualTo("common");
            List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(example);
            if (goodsList == null || goodsList.size() < 1) {
                response.setMessage("未查询到到商品信息");
                return response;
            }

            //成交价总差额
            BigDecimal totalAmount = new BigDecimal(0);
            //折扣总差额
            BigDecimal totalDiscount = new BigDecimal(0);
            //重新计算优惠价，更新成交价
            MasterOrderGoods updateGoods = null;
            String note = "";
            String messageBefore = "更新失败";
            for (MasterOrderGoods masterOrderGoods : goodsList) {
                //sku
                String customCode = masterOrderGoods.getCustomCode();
                //商品售价
                BigDecimal goodsPrice = masterOrderGoods.getGoodsPrice();
                //商品成交价
                BigDecimal transactionPrice = masterOrderGoods.getTransactionPrice();
                //商品折扣
                BigDecimal discount = masterOrderGoods.getDiscount();
                //商品数量
                Short goodsNumber = masterOrderGoods.getGoodsNumber();

                //需改成修改价格
                BigDecimal updatePrice = priceMap.get(customCode);
                //累加差额
                totalAmount = totalAmount.add(transactionPrice.subtract(updatePrice).multiply(BigDecimal.valueOf(goodsNumber)));

                //计算修改后的折扣
                BigDecimal updateDiscount = goodsPrice.subtract(updatePrice);
                //累加差折扣
                totalDiscount = totalDiscount.add(discount.subtract(updateDiscount).multiply(BigDecimal.valueOf(goodsNumber)));

                //更新商品价格信息
                updateGoods = new MasterOrderGoods();
                updateGoods.setTransactionPrice(updatePrice);
                updateGoods.setSettlementPrice(updatePrice);
                updateGoods.setDiscount(updateDiscount);
                updateGoods.setId(masterOrderGoods.getId());
                int update = masterOrderGoodsMapper.updateByPrimaryKeySelective(updateGoods);
                if (update > 0) {
                    messageBefore = "更新成功";
                }

                //添加日志
                note += customCode + "商品成交价由" + transactionPrice + "改为" + updatePrice + messageBefore + ";";
            }

            //更新支付单应付金额
            updatePayPrice(masterOrderSn, totalAmount);

            //更新订单
            boolean orderPrice = updateOrderPrice(masterOrderInfo, totalAmount, totalDiscount);
            String orderMessage = "失败";
            if (orderPrice) {
                orderMessage = "成功";
            }

            note = masterOrderSn + "订单价格更新" + orderMessage + "：" + note;
            //加入订单日志
            masterOrderActionService.insertOrderActionBySn(masterOrderSn, note, applyItem.getUserId());

            response.setSuccess(true);
            response.setMessage("更新订单商品价格成功");

        } catch (Exception e) {
            logger.error("更新订单商品价格异常" + JSONObject.toJSONString(applyItem), e);
        }

        return response;
    }

    /**
     * 更新支付单支付总金额
     * @param masterOrderSn 订单号
     * @param totalAmount 成交价差额
     */
    private void updatePayPrice(String masterOrderSn, BigDecimal totalAmount) {
        List<MasterOrderPay> masterOrderPayList = masterOrderPayService.getMasterOrderPayList(masterOrderSn);
        if (masterOrderPayList != null && masterOrderPayList.size() > 0) {
            MasterOrderPay masterOrderPay = masterOrderPayList.get(0);
            MasterOrderPay updatePay = new MasterOrderPay();
            updatePay.setMasterPaySn(masterOrderPay.getMasterPaySn());

            BigDecimal payTotalfee = masterOrderPay.getPayTotalfee();
            //原价 - 差额
            updatePay.setPayTotalfee(payTotalfee.subtract(totalAmount));
            //更新支付单行记录
            masterOrderPayService.updateByPrimaryKeySelective(updatePay);
        }
    }

    /**
     * 更新主订单支付总金额
     * @param masterOrderInfo 订单信息
     * @param totalAmount 成交价差额
     * @param totalDiscount 折扣差额
     */
    private boolean updateOrderPrice(MasterOrderInfo masterOrderInfo, BigDecimal totalAmount, BigDecimal totalDiscount) {
        //支付总费用
        BigDecimal payTotalFee = masterOrderInfo.getPayTotalFee();
        //应付款总金额
        BigDecimal totalPayable = masterOrderInfo.getTotalPayable();
        //订单总金额
        BigDecimal totalFee = masterOrderInfo.getTotalFee();
        //已付款金额
        BigDecimal moneyPaid = masterOrderInfo.getMoneyPaid();
        //折扣
        BigDecimal discount = masterOrderInfo.getDiscount();

        //更新订单价格
        MasterOrderInfo updateOrder = new MasterOrderInfo();
        updateOrder.setMasterOrderSn(masterOrderInfo.getMasterOrderSn());
        updateOrder.setPayTotalFee(payTotalFee.subtract(totalAmount));
        updateOrder.setTotalFee(totalFee.subtract(totalAmount));
        updateOrder.setDiscount(discount.subtract(totalDiscount));
        updateOrder.setUpdateTime(new Date());

        //未支付，更新订单应付款金额；部分付款，若订单应付款金额 >= 差额，只更新订单应付款金额；若若订单应付款金额 < 差额,更新订单应付款金额和已付款金额；
        //已支付，更新已付款金额
        Byte payStatus = masterOrderInfo.getPayStatus();
        if (payStatus == 0) {
            updateOrder.setTotalPayable(totalPayable.subtract(totalAmount));
        } else if (payStatus == 1) {
            BigDecimal diffAmount = totalPayable.subtract(totalAmount);
            if (diffAmount.doubleValue() >= 0) {
                updateOrder.setTotalPayable(diffAmount);
            } else {
                updateOrder.setTotalPayable(BigDecimal.valueOf(0));
                updateOrder.setMoneyPaid(moneyPaid.add(diffAmount));
            }
        } else {
            updateOrder.setMoneyPaid(moneyPaid.subtract(totalAmount));
        }

        int update = masterOrderInfoService.updateByPrimaryKeySelective(updateOrder);
        if (update == 0) {
            return false;
        }
        return true;
    }

    /**
     * 更新订单商品价格校验
     * @param applyItem
     * @return
     */
    private boolean updateOrderPriceCheck(ApplyItem applyItem, OmsBaseResponse<Boolean> response) {
        if (applyItem == null) {
            response.setMessage("请求参数为空");
            return false;
        }

        String userId = applyItem.getUserId();
        if (StringUtils.isBlank(userId)) {
            response.setMessage("下单人为空");
            return false;
        }

        //订单号
        String masterOrderSn = applyItem.getMasterOrderSn();
        if (StringUtils.isBlank(masterOrderSn)) {
            response.setMessage("订单号为空");
            return false;
        }

        //校验商品信息
        List<UpdateGoodsItem> updateGoodsItems = applyItem.getUpdateGoodsItems();
        if (updateGoodsItems == null || updateGoodsItems.size() == 0) {
            response.setMessage("商品信息为空");
            return false;
        }
        for (UpdateGoodsItem item : updateGoodsItems) {
            String sku = item.getSku();
            if (StringUtils.isBlank(sku)) {
                response.setMessage("商品sku为空");
                return false;
            }

            BigDecimal goodsPrice = item.getGoodsPrice();
            if (goodsPrice == null) {
                response.setMessage(sku + "价格为空");
                return false;
            }
            if (goodsPrice.doubleValue() < 0) {
                response.setMessage(sku + "价格不合法");
                return false;
            }
        }

        return true;
    }

}
