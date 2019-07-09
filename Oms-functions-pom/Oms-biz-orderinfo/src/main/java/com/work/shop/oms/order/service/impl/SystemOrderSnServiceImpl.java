package com.work.shop.oms.order.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderDistributeExample;
import com.work.shop.oms.bean.SystemMasterOrderSn;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.SystemMasterOrderSnDefineMapper;
import com.work.shop.oms.order.service.SystemOrderSnService;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;

/**
 * 系统订单号
 * @author QuYachu
 */
@Service
public class SystemOrderSnServiceImpl implements SystemOrderSnService{

	@Resource
	private SystemMasterOrderSnDefineMapper systemMasterOrderSnDefineMapper;
	@Resource
	private OrderDistributeMapper orderDistributeMapper;

	/**
	 * 创建订单号
	 * @return ServiceReturnInfo<String>
	 */
	@Override
	public ServiceReturnInfo<String> createMasterOrderSn() {
		SystemMasterOrderSn s = new SystemMasterOrderSn();
		systemMasterOrderSnDefineMapper.insertSelectAutoColum(s);
		if (null == s || s.getAutoId() == null || s.getAutoId() < 1) {
			return new ServiceReturnInfo<String>(false, "generate order error when get SystemMasterOrderSn", null);
		}
		Date systemDate = systemMasterOrderSnDefineMapper.selectSystemDate();
		if (null == systemDate) {
			systemDate = new Date();
		}
		String formatDate = TimeUtil.format(systemDate, "yyMMddHHmmss");
		String autoColum = s.getAutoId().toString();
		int index = 4;
		if (autoColum.length() > index) {
			autoColum = autoColum.substring(autoColum.length() - index);
		}
		//String masterOrderSn = "HL" + formatDate + autoColum;
		String masterOrderSn = formatDate + autoColum;
		return new ServiceReturnInfo<String>(true, "订单号创建成功", masterOrderSn);
	}

	@Override
	public ServiceReturnInfo<String> createOrderSn(String masterOrderSn) {
		OrderDistributeExample distributeExample = new OrderDistributeExample();
		distributeExample.setOrderByClause(" order_sn desc");
		distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn).limit(1);
		List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
		if (StringUtil.isListNull(distributes)) {
			distributes.get(0);
		}
		/*String formatDate = TimeUtil.format(systemDate, "yyMMddHHmmss");
		String autoColum = s.getAutoId().toString();
		int index = 5;
		if (autoColum.length() > index) {
			autoColum = autoColum.substring(autoColum.length() - index);
		}
		String masterOrderSn = formatDate + autoColum;
		return new ServiceReturnInfo<String>(true, "子订单号创建成功", masterOrderSn);*/
		return null;
	}
	
	
	public static void main(String[] args) {
		Integer aa = 100001;
		String autoColum = aa.toString();
		int index = 4;
		if (autoColum.length() > index) {
			autoColum = autoColum.substring(autoColum.length() - index);
		} else {
		}
		System.out.println(autoColum);
		System.out.println(aa.toString().substring(4));
	}
}
