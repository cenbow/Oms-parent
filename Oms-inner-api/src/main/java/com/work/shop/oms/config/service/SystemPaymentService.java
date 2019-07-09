package com.work.shop.oms.config.service;

import java.util.List;

import com.work.shop.oms.bean.SystemPayment;

public interface SystemPaymentService {

	
	public SystemPayment selectSystemPayByCode(String payCode);
	
	/**
     * @param payId
     * @return
     */
    SystemPayment getSystemPaymentByPayId(Byte payId);
    
    /**
     * 得到支付List
     * @return
     */
    List<SystemPayment> getSystemPaymentList();
}
