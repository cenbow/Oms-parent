package com.work.shop.oms.dao;

import com.work.shop.oms.bean.PayPeriod;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账期支付信息
 * @author YeQingchao
 *
 */
@Repository("payPeriodMapper")
public interface PayPeriodMapper {
	
	/**
	 * 根据支付编码获取获取账期支付信息
	 * @param payPeriod
	 * @return
	 */
    public List<PayPeriod> getPayPeriodInfo(PayPeriod payPeriod);

}
