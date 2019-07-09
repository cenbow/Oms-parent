package com.work.shop.oms.common.paraBean;
import java.io.Serializable;
import java.util.List;

/**
 * 退单请求参数信息
 * @author Administrator
 *
 */
public class ParaChargebackBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7399774195053800709L;
	
	/**
	 * 订单号
	 */
	private String order_sn;
	
	/**
	 * 退单原因
	 */
	private String back_msg;
	
	/**
	 * 门店编号信息
	 */
	private String shop_number;
	
	/**
	 * 操作人员
	 */
	private String operatorStr;
	
	/**
	 * 运费
	 */
	private double shipping=0.00d;
	
	/**
	 * 退单支付方式
	 */
	private String return_pay_type;
	
	/**
	 * 退单发货状态
	 */
	private String return_ship_status;	
	
	/**
	 * 退单支付状态
	 */
	private String return_pay_status;			
					
					
	/**
	 * 银行信息							
	 */
	private String bank_name;
	
	/**
	 * 支付卡号
	 */
	private String bank_card;
	
	
	
	/**
	 * 退单退款金额
	 */
	private double return_amount=0.00d;
	
	
	
	/**
	 * 微信商品订单号
	 */
	private String wx_orderNo;			

	

	private List<ParaBackGoodsBean> goodsList;
	
	
	public String getReturn_pay_type() {
		return return_pay_type;
	}

	public void setReturn_pay_type(String return_pay_type) {
		this.return_pay_type = return_pay_type;
	}

	public String getReturn_ship_status() {
		return return_ship_status;
	}

	public void setReturn_ship_status(String return_ship_status) {
		this.return_ship_status = return_ship_status;
	}

	public String getReturn_pay_status() {
		return return_pay_status;
	}

	public void setReturn_pay_status(String return_pay_status) {
		this.return_pay_status = return_pay_status;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_card() {
		return bank_card;
	}

	public void setBank_card(String bank_card) {
		this.bank_card = bank_card;
	}

	public double getReturn_amount() {
		return return_amount;
	}

	public void setReturn_amount(double return_amount) {
		this.return_amount = return_amount;
	}

	public String getWx_orderNo() {
		return wx_orderNo;
	}

	public void setWx_orderNo(String wx_orderNo) {
		this.wx_orderNo = wx_orderNo;
	}



	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	

	public String getBack_msg() {
		return back_msg;
	}

	public void setBack_msg(String back_msg) {
		this.back_msg = back_msg;
	}

	public String getShop_number() {
		return shop_number;
	}

	public void setShop_number(String shop_number) {
		this.shop_number = shop_number;
	}

	

	public double getShipping() {
		return shipping;
	}

	public void setShipping(double shipping) {
		this.shipping = shipping;
	}

	public List<ParaBackGoodsBean> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<ParaBackGoodsBean> goodsList) {
		this.goodsList = goodsList;
	}

	public String getOperatorStr() {
		return operatorStr;
	}

	public void setOperatorStr(String operatorStr) {
		this.operatorStr = operatorStr;
	}
}
