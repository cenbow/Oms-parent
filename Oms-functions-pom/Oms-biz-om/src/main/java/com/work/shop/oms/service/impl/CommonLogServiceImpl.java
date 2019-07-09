package com.work.shop.oms.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.DistributeAction;
import com.work.shop.oms.bean.DistributeActionExample;
import com.work.shop.oms.bean.DistributeActionHistory;
import com.work.shop.oms.bean.DistributeActionHistoryExample;
import com.work.shop.oms.bean.GoodsReturnChangeAction;
import com.work.shop.oms.bean.GoodsReturnChangeActionExample;
import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderActionExample;
import com.work.shop.oms.bean.MasterOrderActionHistory;
import com.work.shop.oms.bean.MasterOrderActionHistoryExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.dao.DistributeActionHistoryMapper;
import com.work.shop.oms.dao.DistributeActionMapper;
import com.work.shop.oms.dao.GoodsReturnChangeActionMapper;
import com.work.shop.oms.dao.MasterOrderActionHistoryMapper;
import com.work.shop.oms.dao.MasterOrderActionMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.orderReturn.service.OrderReturnStService;
import com.work.shop.oms.service.CommonLogService;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.vo.AdminUser;

@Service
public class CommonLogServiceImpl implements CommonLogService {
private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MasterOrderActionMapper masterOrderActionMapper;
	@Autowired
	private MasterOrderActionHistoryMapper masterOrderActionHistoryMapper;
	@Autowired
	private DistributeActionMapper distributeActionMapper;
	@Autowired
	private DistributeActionHistoryMapper distributeActionHistoryMapper;
	@Autowired
	private MasterOrderInfoMapper masterOrderInfoMapper;

	@Resource
	private OrderReturnStService orderReturnStService;

	@Resource
	private GoodsReturnChangeActionMapper goodsReturnChangeActionMapper;
	
	public Paging getMasterOrderAction(AdminUser adminUser, String masterOrderSn, Integer isHistory){
		Paging paging = new Paging();
		try{
			if(isHistory==0){
				MasterOrderActionExample example = new MasterOrderActionExample();
				example.or().andMasterOrderSnEqualTo(masterOrderSn);
				example.setOrderByClause("action_id desc");
				List<MasterOrderAction> list = masterOrderActionMapper.selectByExampleWithBLOBs(example);
				if(list!=null&&list.size()>0){
					paging.setRoot(list);
					paging.setTotalProperty(list.size());
					paging.setMessage("获取主单日志成功！");
				}else{
					paging.setRoot(null);
					paging.setTotalProperty(0);
					paging.setMessage("无主单日志数据！");
				}
			}else{//查历史表
				MasterOrderActionHistoryExample example = new MasterOrderActionHistoryExample();
				example.or().andMasterOrderSnEqualTo(masterOrderSn);
				example.setOrderByClause("action_id desc");
				List<MasterOrderActionHistory> list = masterOrderActionHistoryMapper.selectByExampleWithBLOBs(example);
				if(list!=null&&list.size()>0){
					paging.setRoot(list);
					paging.setTotalProperty(list.size());
					paging.setMessage("获取主单历史单日志成功！");
				}else{
					paging.setRoot(null);
					paging.setTotalProperty(0);
					paging.setMessage("无主单历史单日志数据！");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			paging.setRoot(null);
			paging.setTotalProperty(0);
			paging.setMessage("获取主单日志异常！");
		}
		return paging;
	}

	@Override
	public Paging getDistributeOrderAction(AdminUser adminUser, String orderSn,
			Integer isHistory) {
		// TODO Auto-generated method stub
		Paging paging = new Paging();
		try{
			if(isHistory==0){
				DistributeActionExample example = new DistributeActionExample();
				example.or().andOrderSnEqualTo(orderSn);
				example.setOrderByClause("action_id desc");
				List<DistributeAction> list = distributeActionMapper.selectByExampleWithBLOBs(example);
				if(list!=null&&list.size()>0){
					paging.setRoot(list);
					paging.setTotalProperty(list.size());
					paging.setMessage("获取交货单["+orderSn+"]日志成功！");
				}else{
					paging.setRoot(null);
					paging.setTotalProperty(0);
					paging.setMessage("无交货单["+orderSn+"]日志数据！");
				}
			}else{
				DistributeActionHistoryExample example = new DistributeActionHistoryExample();
				example.or().andOrderSnEqualTo(orderSn);
				example.setOrderByClause("action_id desc");
				List<DistributeActionHistory> list = distributeActionHistoryMapper.selectByExample(example);
				if(list!=null&&list.size()>0){
					paging.setRoot(list);
					paging.setTotalProperty(list.size());
					paging.setMessage("获取交货单["+orderSn+"]历史单日志成功！");
				}else{
					paging.setRoot(null);
					paging.setTotalProperty(0);
					paging.setMessage("无交货单["+orderSn+"]历史单日志数据！");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			paging.setRoot(null);
			paging.setTotalProperty(0);
			paging.setMessage("获取交货单["+orderSn+"]日志异常！");
		}
		return paging;
	}

	@Override
	public Map<String,String> decodeLinkMoble(AdminUser adminUser, String mobile, String tel,
			String masterOrderSn,String returnSn) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "0";//0:解密失败，1：解密成功
		String msg = "解密失败！";
		try{
			if(adminUser==null){
				msg = "登录失效！请重新登录！";
			}else{
				//解密返回数据
				if (StringUtil.isNotEmpty(mobile) || StringUtil.isNotEmpty(tel)) {
					if (StringUtil.isNotEmpty(mobile)) {
						map.put("mobile", mobile);
					}
					if (StringUtil.isNotEmpty(tel)) {
						map.put("tel", tel);
					}
					code = "1";
					msg = "解密联系方式成功！";
					//记录解密日志
					if (StringUtil.isNotEmpty(masterOrderSn)) {
						MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
						if(masterOrderInfo!=null){
							MasterOrderAction masterOrderAction = new MasterOrderAction();
							masterOrderAction.setMasterOrderSn(masterOrderSn);
							masterOrderAction.setActionUser(adminUser.getUserName());
							masterOrderAction.setOrderStatus(masterOrderInfo.getOrderStatus());
							masterOrderAction.setShippingStatus(masterOrderInfo.getShipStatus());
							masterOrderAction.setPayStatus(masterOrderInfo.getPayStatus());
							masterOrderAction.setActionNote("解密联系方式");
							masterOrderAction.setLogTime(new Date());
							masterOrderActionMapper.insert(masterOrderAction);
						}
					}
					if (StringUtil.isNotEmpty(returnSn)) {
					    orderReturnStService.addReturnOrderAction(returnSn, "解密联系方式", adminUser.getUserName());
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	@Override
	public Paging getGoodsReturnChangeAction(AdminUser adminUser, String orderSn,
			Integer isHistory) {
		// TODO Auto-generated method stub
		Paging paging = new Paging();
		try{
			GoodsReturnChangeActionExample example = new GoodsReturnChangeActionExample();
			example.setOrderByClause("action_id desc");
			example.or().andOrderSnEqualTo(orderSn);
			List<GoodsReturnChangeAction> list = goodsReturnChangeActionMapper.selectByExampleWithBLOBs(example);
			if(list!=null&&list.size()>0){
				paging.setRoot(list);
				paging.setTotalProperty(list.size());
				paging.setMessage("获取退换货申请["+orderSn+"]历史单日志成功！");
			}else{
				paging.setRoot(null);
				paging.setTotalProperty(0);
				paging.setMessage("无退换货申请["+orderSn+"]历史单日志数据！");
			}
		}catch(Exception e){
			e.printStackTrace();
			paging.setRoot(null);
			paging.setTotalProperty(0);
			paging.setMessage("获取交货单["+orderSn+"]日志异常！");
		}
		return paging;
	}

}
