package com.work.shop.oms.order.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderInfoExample;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.MasterOrderActionMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.order.service.MasterOrderActionAPIService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
@Service
public class MasterOrderActionAPIServiceImpl implements MasterOrderActionAPIService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private MasterOrderActionMapper masterOrderActionMapper;
	
	@Resource
	private MasterOrderInfoMapper masterOrderInfoMapper;
	
	@Resource
	private MasterOrderActionService masterOrderActionService;
	
	@Override
	public ReturnInfo insertOrderActionByOutSn(String orderOutSn, String message,
			String actionUser) {
		ReturnInfo info = new ReturnInfo(Constant.OS_NO);
		if (StringUtil.isTrimEmpty(orderOutSn)) {
			info.setMessage("[orderOutSn] 不能为空");
			return info;
		}
		MasterOrderInfoExample example = new MasterOrderInfoExample();
		example.or().andOrderOutSnEqualTo(orderOutSn);
		List<MasterOrderInfo> infos = this.masterOrderInfoMapper.selectByExample(example);
		if (StringUtil.isListNull(infos)) {
			info.setMessage("外部交易号[" + orderOutSn + "] 查询订单信息为空");
			return info;
		}
		try {
			MasterOrderAction orderAction = masterOrderActionService.createOrderAction(infos.get(0));
			if (StringUtil.isNotEmpty(actionUser)) {
				orderAction.setActionUser(actionUser);
				orderAction.setActionNote(message);
			}
			masterOrderActionService.insertOrderActionByObj(orderAction);
			info.setIsOk(Constant.OS_YES);
			info.setMessage("保存日志成功");
		} catch (Exception e) {
			logger.error("外部交易号" + orderOutSn + "报错日志异常:" + e.getMessage(), e);
			info.setMessage("外部交易号" + orderOutSn + "报错日志异常:" + e.getMessage());
		}
		return info;
	}

}
