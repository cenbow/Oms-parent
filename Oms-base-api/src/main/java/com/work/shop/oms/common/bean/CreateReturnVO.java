package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.work.shop.oms.api.param.bean.CreateOrderReturnGoods;

/**
 * 退单接口创建参数
 * @author
 *
 */
public class CreateReturnVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主单号
	 */
	private String masterOrderSn;
	
	/**
	 * 退款金额
	 */
	private Double returnMoney;
	
	/**
	 * 退运费
	 */
	private Double returnShipping = 0d;
	
	/**
	 * 操作人
	 */
	private String actionUser;
	
	/**
	 * 	无货退款单 来源类型（ 删除商品,订单发货，订单取消）
	 */
	private String returnSource;
	
	/**
	 * bvValue
	 */
	private Integer bvValue;
	
	/**
	 * 点数
	 */
	private Double points;
	
	private Integer baseBvValue;
	
	private List<CreateOrderReturnGoods> orderReturnGoodsList = new ArrayList<CreateOrderReturnGoods>();//商品列表

	public String getMasterOrderSn() {
        return masterOrderSn;
    }
    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }
    public Double getReturnMoney() {
		return returnMoney;
	}
	public void setReturnMoney(Double returnMoney) {
		this.returnMoney = returnMoney;
	}
	public String getActionUser() {
		return actionUser;
	}
	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}
	public List<CreateOrderReturnGoods> getOrderReturnGoodsList() {
		return orderReturnGoodsList;
	}
	public void setOrderReturnGoodsList(
			List<CreateOrderReturnGoods> orderReturnGoodsList) {
		this.orderReturnGoodsList = orderReturnGoodsList;
	}
	public String getReturnSource() {
		return returnSource;
	}
	public void setReturnSource(String returnSource) {
		this.returnSource = returnSource;
	}
	public Integer getBvValue() {
		return bvValue;
	}
	public void setBvValue(Integer bvValue) {
		this.bvValue = bvValue;
	}
	public Double getPoints() {
		return points;
	}
	public void setPoints(Double points) {
		this.points = points;
	}
	public Integer getBaseBvValue() {
		return baseBvValue;
	}
	public void setBaseBvValue(Integer baseBvValue) {
		this.baseBvValue = baseBvValue;
	}
	public Double getReturnShipping() {
		return returnShipping;
	}
	public void setReturnShipping(Double returnShipping) {
		this.returnShipping = returnShipping;
	}
}
