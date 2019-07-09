package com.work.shop.oms.bean.bgapidb.defined;

public class RefundMQMessag extends BaseMQMessage{
	
	private String status;//处理类型

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
