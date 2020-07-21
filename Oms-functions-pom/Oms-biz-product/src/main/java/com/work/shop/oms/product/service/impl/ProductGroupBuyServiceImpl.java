package com.work.shop.oms.product.service.impl;

import com.work.shop.oms.common.bean.ChannelGoodsVo;
import com.work.shop.oms.common.bean.ProductGroupBuy;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.utils.CachedBeanCopier;
import com.work.shop.oms.product.service.ProductGoodsService;
import com.work.shop.oms.product.service.ProductGroupBuyService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.pca.common.ResultData;
import com.work.shop.pca.feign.BgProductService;
import com.work.shop.pca.model.*;
import org.apache.commons.collections.list.PredicatedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 团购信息
 *
 * @author yaokan
 * @date 2020-07-20 15:34
 */
@Service
public class ProductGroupBuyServiceImpl implements ProductGroupBuyService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private BgProductService bgProductService;

	@Override
	public ReturnInfo selectProductGroupBuy() {
		ReturnInfo<List<ProductGroupBuy>> info = new ReturnInfo<List<ProductGroupBuy>>(Constant.OS_NO);
		List<ProductGroupBuy> productGroupBuyList = new ArrayList<>();
		ResultData<List<BgGroupBuyInfo>> successProductGroupBuy = bgProductService.getSuccessProductGroupBuy();
		if(successProductGroupBuy.getData() != null && successProductGroupBuy.getData().size() >0){
			List<BgGroupBuyInfo> data = successProductGroupBuy.getData();
			for (BgGroupBuyInfo datum : data) {
				ProductGroupBuy productGroupBuy = new ProductGroupBuy();
				CachedBeanCopier.copy(datum, productGroupBuy);
				productGroupBuyList.add(productGroupBuy);
			}
		}
		info.setData(productGroupBuyList);
		info.setIsOk(1);
		return info;
	}
}
