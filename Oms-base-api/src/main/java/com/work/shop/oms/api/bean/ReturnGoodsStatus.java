package com.work.shop.oms.api.bean;

public class ReturnGoodsStatus {
	
	private Integer payStatus;//'退款总状态（0，未结算；1，已结算；2，待结算）'
	private Integer processType;//处理状态 （0无操作，1退货，2修补，3销毁，4换货）
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public Integer getProcessType() {
		return processType;
	}
	public void setProcessType(Integer processType) {
		this.processType = processType;
	}
	//订单单个商品状态(1、退货中;2、换货中;3、退货完成;4、换货完成)
	public int getReturnGoodsStatus(){
		if(processType==1&&(payStatus==0||payStatus==2)){
			return 1;
		}
		if(processType==1&&payStatus==1){
			return 3;
		}
		if(processType==4&&(payStatus==0||payStatus==2)){
			return 2;
		}
		if(processType==4&&payStatus==1){
			return 4;
		}
		return 0;
	}

}
