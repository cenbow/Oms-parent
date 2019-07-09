package com.work.shop.oms.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.common.bean.OrderSku;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderget.service.ChannelManagerService;
import com.work.shop.oms.service.AbnormalSKUMaintainService;
import com.work.shop.oms.utils.PageHelper;

@Service("abnormalSKUMaintainService")
public class AbnormalSKUMaintainServiceImpl implements AbnormalSKUMaintainService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//@Resource
	private ChannelManagerService channelManagerService;

	@Override
	public Paging getAbnormalSKUList(OrderSku vo, PageHelper helper) {
		// TODO Auto-generated method stub
		Paging paging = new Paging();
		try{
			//封装查询条
			String orderOutSn = vo.getOrderOutSn();//外部订单号
			String channelType = vo.getChannelType();//平台类型
			int pageSize = helper.getLimit();//每页条数
			int pageStart = helper.getStart();//开始行数
			logger.info("===orderOutSn:"+orderOutSn+"===channelType:"+channelType);
			logger.info("===pageSize:"+pageSize+"===pageStart:"+pageStart);
			vo.setPageSize(pageSize);
			vo.setPageStart(pageStart);
			//获取查询结果
			paging = channelManagerService.findMissingSkuOrderList(vo);
		}catch(Exception e){
			e.printStackTrace();
		}
		return paging;
	}

	@Override
	public Map doSaveEdit(OrderSku vo) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String code = "1";//0：成功  1：失败
		String msg = "失败！";
		try{
			logger.info("===渠道码:"+vo.getChannelType());
			logger.info("===skuSn:"+vo.getSkuSn());
			logger.info("===outSkuName:"+vo.getOutSkuName());
			logger.info("===goodsNum:"+vo.getGoodsNum());
			ReturnInfo info = channelManagerService.fillSkuChannelOrder(vo);
			if(info!=null){
				int isOk = info.getIsOk();
				if(isOk==1){//成功
					code = "0";
					msg = "成功";
				}else{
					msg = "失败！"+info.getMessage();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}

	public Map selectColorAndSizeBySKU(String skuSn){
		Map<String,String> map = new HashMap<String,String>();
		String code = "1";//0：查到了  1：未查到
		String msg = "没有对应的sku码！";
		try{
			/*List<Map<String,String>> list = productBarcodeListDefinedMapper.selectColorAndSizeBySKU(skuSn);
			if(list!=null&&list.size()>0){
				code = "0";
				msg = "sku码对应【颜色："+list.get(0).get("colorname")+"】【尺寸："+list.get(0).get("sizename")+"】";
			}*/
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
}
