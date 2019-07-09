package com.work.shop.oms.order.response;

import java.io.Serializable;
import java.util.List;

import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.vo.ReturnGoodsVO;
import com.work.shop.oms.vo.ReturnOrderVO;
import com.work.shop.oms.vo.ReturnPaymentVO;

/**
 * 退单管理返回对象
 * @author lemon
 */
public class ReturnManagementResponse implements Serializable {

	private static final long serialVersionUID = -2448060626465981003L;

	private Boolean success;
	
	private String message;
	
	private ReturnOrderVO returnOrderVO;
	
	private List<ReturnPaymentVO> returnPars;
	
	private List<ReturnGoodsVO> goodsVOs;
	
	private OrderItemDetail itemDetail;
	
	private List<OrderItemPayDetail> payDetails;

	private List<GoodsReturnChangeDetailVo> returnChangeGoods;
	
	private ReturnItemStatusUtils statusUtils;
	
	private String returnSn;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ReturnOrderVO getReturnOrderVO() {
		return returnOrderVO;
	}

	public void setReturnOrderVO(ReturnOrderVO returnOrderVO) {
		this.returnOrderVO = returnOrderVO;
	}

	public OrderItemDetail getItemDetail() {
		return itemDetail;
	}

	public void setItemDetail(OrderItemDetail itemDetail) {
		this.itemDetail = itemDetail;
	}

	public List<OrderItemPayDetail> getPayDetails() {
		return payDetails;
	}

	public void setPayDetails(List<OrderItemPayDetail> payDetails) {
		this.payDetails = payDetails;
	}

	public ReturnItemStatusUtils getStatusUtils() {
		return statusUtils;
	}

	public void setStatusUtils(ReturnItemStatusUtils statusUtils) {
		this.statusUtils = statusUtils;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public List<ReturnPaymentVO> getReturnPars() {
		return returnPars;
	}

	public void setReturnPars(List<ReturnPaymentVO> returnPars) {
		this.returnPars = returnPars;
	}

	public List<ReturnGoodsVO> getGoodsVOs() {
		return goodsVOs;
	}

	public void setGoodsVOs(List<ReturnGoodsVO> goodsVOs) {
		this.goodsVOs = goodsVOs;
	}

    public List<GoodsReturnChangeDetailVo> getReturnChangeGoods() {
        return returnChangeGoods;
    }

    public void setReturnChangeGoods(List<GoodsReturnChangeDetailVo> returnChangeGoods) {
        this.returnChangeGoods = returnChangeGoods;
    }
}
