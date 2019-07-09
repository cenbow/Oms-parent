package com.work.shop.oms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.PaymentManagementVO;
import com.work.shop.oms.dao.PaymentManagementMapper;
import com.work.shop.oms.service.PaymentManagementService;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.StringUtil;

@Service("paymentManagementService")
public class PaymentManagementServiceImpl implements PaymentManagementService{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private PaymentManagementMapper paymentManagementMapper;
	@Override
	
	public Paging getPaymentQueryList(PaymentManagementVO vo, PageHelper helper) {
		// TODO Auto-generated method stub
		Paging paging = new Paging();
		try{
			//拼装查询入参
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("payCode", StringUtil.Null2Str(vo.getPayCode()));
			paramMap.put("payName", StringUtil.Null2Str(vo.getPayName()));
			paramMap.put("isOnline", StringUtil.Null2Str(vo.getIsOnline()));
			paramMap.put("enabled", StringUtil.Null2Str(vo.getEnabled()));
			paramMap.put("limitNum", helper.getLimit());
			paramMap.put("limitStart", helper.getStart());
			//查询
			int paymentListCount  = paymentManagementMapper.getPaymentQueryListCount(paramMap);
			List<PaymentManagementVO> paymentList = paymentManagementMapper.getPaymentQueryList(paramMap);
			//装载返回参数
			paging.setTotalProperty(paymentListCount);
			paging.setRoot(paymentList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return paging;
	}
	
	@Override
	public Map doAddPayment(PaymentManagementVO vo) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "2";//0：插入成功  1：因存同名/同code插入失败  2：其他原因插入失败
		String msg = "失败！";
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			String payCode = vo.getPayCode();
			String payName = vo.getPayName();
			//验证是否存在同code支付方式
			paramMap.put("payCode", payCode);
			paramMap.put("payName", "");
			int sameCodeCount = paymentManagementMapper.checkSameRecord(paramMap);
			//验证是否存在同名支付方式
			paramMap.put("payCode", "");
			paramMap.put("payName", payName);
			int sameNameCount = paymentManagementMapper.checkSameRecord(paramMap);
			if(sameCodeCount>0){
				code = "1";
				msg = "失败！存在相同的支付方式编码！";
			}else if(sameNameCount>0){
				code = "1";
				msg = "失败！存在相同的支付方式名称！";
			}else{//插入数据
				//支付费用拼装%(规则：大于0就拼装%，其他值不管)
				String payFee = vo.getPayFee();
				if(!"".equals(payFee)&&payFee!=null){
					if(!"0".equals(payFee)){
						vo.setPayFee(payFee+"%");
					}
				}
				vo.setEnabled("1");//默认新增的支付方式为启用状态
				vo.setPayConfig("");//支付方式的配置信息设置默认值
				paymentManagementMapper.doAddPayment(vo);
				code = "0";
				msg = "成功！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	@Override
	public Map changeStatus(String payId, String enabled) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "1";//0：状态切换成功  1：状态切换失败
		String msg = "失败！";
		try{
			if("".equals(payId)||payId==null||"".equals(enabled)||enabled==null){
				msg = "失败！获取支付方式数据失败！";
			}else{
				String toStatus = "";
				if("1".equals(enabled)){//原始状态为启用则改成禁用
					toStatus = "0";
				}else{//原始状态为禁用则改成启用
					toStatus = "1";
				}
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("payId", payId);
				paramMap.put("enabled", toStatus);
				paymentManagementMapper.changeStatus(paramMap);
				code = "0";
				msg = "成功！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	
	@Override
	public Map doSaveEdit(PaymentManagementVO vo) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "1";//0：保存支付方式成功  1：保存支付方式失败
		String msg = "失败！";
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			String payCode = vo.getPayCode();
			String payName = vo.getPayName();
			String payId = vo.getPayId();
			paramMap.put("payId", payId);
			//验证是否存在同code承运商
			paramMap.put("payCode", payCode);
			paramMap.put("payName", "");
			int sameCodeCount = paymentManagementMapper.checkSameRecord(paramMap);
			//验证是否存在同名承运商
			paramMap.put("payCode", "");
			paramMap.put("payName", payName);
			int sameNameCount = paymentManagementMapper.checkSameRecord(paramMap);
			if(sameCodeCount>0){
				code = "1";
				msg = "失败！存在相同的支付方式编码！";
			}else if(sameNameCount>0){
				code = "1";
				msg = "失败！存在相同的支付方式名称！";
			}else{//保存数据
				//支付费用拼装%(规则：大于0就拼装%，其他值不管)
				String payFee = vo.getPayFee();
				if(!"".equals(payFee)&&payFee!=null){
					if(!"0".equals(payFee)){
						vo.setPayFee(payFee+"%");
					}
				}
				paymentManagementMapper.doSaveEdit(vo);
				code = "0";
				msg = "成功！";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

}
