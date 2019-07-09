package com.work.shop.oms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.ShippingManagementVO;
import com.work.shop.oms.dao.ShippingManagementMapper;
import com.work.shop.oms.service.ShippingManagementService;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.StringUtil;

@Service("shippingManagementService")
public class ShippingManagementServiceImpl implements ShippingManagementService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private ShippingManagementMapper shippingManagementMapper;

	@Override
	public Paging getShippingQueryList(ShippingManagementVO vo, PageHelper helper) {
		// TODO Auto-generated method stub
		Paging paging = new Paging();
		try{
			//拼装查询入参
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("shippingCode", StringUtil.Null2Str(vo.getShippingCode()));
			paramMap.put("shippingName", StringUtil.Null2Str(vo.getShippingName()));
			paramMap.put("supportCod", StringUtil.Null2Str(vo.getSupportCod()));
			paramMap.put("enabled", StringUtil.Null2Str(vo.getEnabled()));
			paramMap.put("limitNum", helper.getLimit());
			paramMap.put("limitStart", helper.getStart());
			//查询
			int shippingListCount  = shippingManagementMapper.getShippingQueryListCount(paramMap);
			List<ShippingManagementVO> shippingList = shippingManagementMapper.getShippingQueryList(paramMap);
			//装载返回参数
			paging.setTotalProperty(shippingListCount);
			paging.setRoot(shippingList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return paging;
	}

	@Override
	public Map doAddShipping(ShippingManagementVO vo) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "2";//0：插入成功  1：因存同名/同code插入失败  2：其他原因插入失败
		String msg = "失败！";
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			String shippingCode = vo.getShippingCode();
			String shippingName = vo.getShippingName();
			//验证是否存在同code承运商
			paramMap.put("shippingCode", shippingCode);
			paramMap.put("shippingName", "");
			int sameCodeCount = shippingManagementMapper.checkSameRecord(paramMap);
			//验证是否存在同名承运商
			paramMap.put("shippingCode", "");
			paramMap.put("shippingName", shippingName);
			int sameNameCount = shippingManagementMapper.checkSameRecord(paramMap);
			if(sameCodeCount>0){
				code = "1";
				msg = "失败！存在相同的承运商编码！";
			}else if(sameNameCount>0){
				code = "1";
				msg = "失败！存在相同的承运商名称！";
			}else{//插入数据
				//保价费用拼装%(规则：大于0就拼装%，其他值不管)
				String insure = vo.getInsure();
				if(!"".equals(insure)&&insure!=null){
					if(!"0".equals(insure)){
						vo.setInsure(insure+"%");
					}
				}
				vo.setEnabled("1");//默认新增的承运商为启用状态
				vo.setShippingPrint("");//打印模板设置默认值
				shippingManagementMapper.doAddShipping(vo);
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
	public Map changeStatus(String shippingId, String enabled) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "1";//0：状态切换成功  1：状态切换失败
		String msg = "失败！";
		try{
			if("".equals(shippingId)||shippingId==null||"".equals(enabled)||enabled==null){
				msg = "失败！获取承运商数据失败！";
			}else{
				String toStatus = "";
				if("1".equals(enabled)){//原始状态为启用则改成禁用
					toStatus = "0";
				}else{//原始状态为禁用则改成启用
					toStatus = "1";
				}
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("shippingId", shippingId);
				paramMap.put("enabled", toStatus);
				shippingManagementMapper.changeStatus(paramMap);
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
	public Map doSaveEdit(ShippingManagementVO vo) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "1";//0：保存承运商成功  1：保存承运商失败
		String msg = "失败！";
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			String shippingCode = vo.getShippingCode();
			String shippingName = vo.getShippingName();
			String shippingId = vo.getShippingId();
			paramMap.put("shippingId", shippingId);
			//验证是否存在同code承运商
			paramMap.put("shippingCode", shippingCode);
			paramMap.put("shippingName", "");
			int sameCodeCount = shippingManagementMapper.checkSameRecord(paramMap);
			//验证是否存在同名承运商
			paramMap.put("shippingCode", "");
			paramMap.put("shippingName", shippingName);
			int sameNameCount = shippingManagementMapper.checkSameRecord(paramMap);
			if(sameCodeCount>0){
				code = "1";
				msg = "失败！存在相同的承运商编码！";
			}else if(sameNameCount>0){
				code = "1";
				msg = "失败！存在相同的承运商名称！";
			}else{//保存数据
				//保价费用拼装%(规则：大于0就拼装%，其他值不管)
				String insure = vo.getInsure();
				if(!"".equals(insure)&&insure!=null){
					if(!"0".equals(insure)){
						vo.setInsure(insure+"%");
					}
				}
				shippingManagementMapper.doSaveEdit(vo);
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
