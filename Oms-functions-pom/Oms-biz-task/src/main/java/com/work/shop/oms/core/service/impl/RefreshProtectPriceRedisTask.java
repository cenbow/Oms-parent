package com.work.shop.oms.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.core.beans.BaseTask;
import com.work.shop.oms.core.beans.ConstantTask;
import com.work.shop.oms.core.beans.ProductPrice;
import com.work.shop.oms.core.beans.ReturnTask;
import com.work.shop.oms.core.service.ATaskServiceProcess;
import com.work.shop.oms.redis.RedisClient;

/**
 * 刷新商品保护价Redis
 * @author huangl
 *
 */
@Service("refreshProtectPriceRedisTask")
public class RefreshProtectPriceRedisTask extends ATaskServiceProcess{
	
	private static Logger logger = LoggerFactory.getLogger(RefreshProtectPriceRedisTask.class);
	
//	@Resource
//	private ChannelGoodsDefinedMapper channelGoodsDefinedMapper;
	
	@Resource(name = "redisClient")
	private RedisClient redisClient;
	
	@Override
	public void initTaskConfig() {
		super.taskName = ConstantTask.TASK_JOB_TYPE_REFRESH_PROTECT_PRICE_ORDER;
		super.initTaskConfig();
	}
	
	@Override
	public List<BaseTask> queryServiceData(List<String> orderIdList,Integer dataLimit) {
//		List<ProductGoods> productGoodsList = channelGoodsDefinedMapper.selectGoodsProtectPrice();
//		logger.debug("productGoodsList.length:" + CollectionUtils.size(productGoodsList));
		List<BaseTask> taskDatas = new ArrayList<BaseTask>();
		/*if(CollectionUtils.isNotEmpty(productGoodsList)){
			for (ProductGoods product : productGoodsList) {
				ProductPrice obj = new ProductPrice();
				obj.setGoodsSn(product.getGoodsSn());
				obj.setPrice(product.getProtectPrice() == null ? 0D :product.getProtectPrice().doubleValue());
				taskDatas.add(obj);
			}
		}*/
		return taskDatas;
	}

	@Override
	public ReturnTask executeTask(BaseTask obj) {
		//logger.debug("executeTask.BaseObj:" + JSON.toJSONString(obj));
		String orderSn = obj.getOrderSn();
		ReturnTask returnTask = new ReturnTask();
		returnTask.setIsOk(ConstantValues.YESORNO_NO);
		try {
			ProductPrice product = (ProductPrice)obj;
			returnTask.setOrderSn(product.getGoodsSn());
			String key = "productGoodsProtectedPrice_goodsSn_" + product.getGoodsSn();
			String response = redisClient.set(key, product.getPrice()+StringUtils.EMPTY);
			//logger.debug("executeTask.redisGetValue:" + "key :"+key+",values:"+redisClient.get(key));
			returnTask.setIsOk(ConstantValues.YESORNO_YES);
			returnTask.setMsg("商品"+product.getGoodsSn()+"保护价"+product.getPrice()+"Redis成功,response:"+response);
		} catch (Exception e) {
			logger.debug("executeTask.orderSn:"+orderSn+" 错误信息:"+e.getMessage(),e);
			returnTask.setIsOk(ConstantValues.YESORNO_NO);
			returnTask.setMsg("商品保护价刷新成功异常：");
		}
		return returnTask;
	}

}
