package com.work.shop.oms.config.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.SystemPayment;
import com.work.shop.oms.bean.SystemPaymentExample;
import com.work.shop.oms.config.service.SystemPaymentService;
import com.work.shop.oms.dao.SystemPaymentMapper;
import com.work.shop.oms.utils.StringUtil;

@Service
public class SystemPaymentServiceImpl implements SystemPaymentService{
	@Resource
	private SystemPaymentMapper systemPaymentMapper;

	@Override
	public SystemPayment selectSystemPayByCode(String payCode) {
		if (StringUtil.isNotBlank(payCode)) {
			SystemPaymentExample systemPayExa = new SystemPaymentExample();
			systemPayExa.or().andPayCodeEqualTo(payCode);
			List<SystemPayment> resultList = systemPaymentMapper.selectByExample(systemPayExa);
			if (resultList != null && resultList.size() > 0) {
				return resultList.get(0);
			}
		}
		return null;
	}
	
	@Override
	public SystemPayment getSystemPaymentByPayId(Byte payId) {
	    return systemPaymentMapper.selectByPrimaryKey(payId);
	}
	
	@Override
    public List<SystemPayment> getSystemPaymentList() {
        SystemPaymentExample s=new SystemPaymentExample();
        s.or();
        return systemPaymentMapper.selectByExample(s);
    }

}
