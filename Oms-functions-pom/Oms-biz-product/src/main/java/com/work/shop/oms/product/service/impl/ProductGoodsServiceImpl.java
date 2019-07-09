package com.work.shop.oms.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.work.shop.pca.common.ResultData;
import com.work.shop.pca.feign.BgProductService;
import com.work.shop.pca.model.BgCartResponse;
import com.work.shop.pca.model.BgProductChannelBarcode;
import com.work.shop.pca.model.BgProductInfo;
import com.work.shop.pca.model.BgProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.common.bean.ChannelGoodsVo;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.product.service.ProductGoodsService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 商品服务
 * @author QuYachu
 */
@Service
public class ProductGoodsServiceImpl implements ProductGoodsService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private BgProductService bgProductService;

	@Override
	public ReturnInfo selectChannelGoods(String channelCode, String goodsSn) {
		ReturnInfo<List<ChannelGoodsVo>> info = new ReturnInfo<List<ChannelGoodsVo>>(Constant.OS_NO);
		List<ChannelGoodsVo> list = new ArrayList<ChannelGoodsVo>();
		if(StringUtil.isTrimEmpty(goodsSn)){
			info.setMessage("[goodsSn] 商品编码不能为空！");
			return info;
		}
		try{
			String queryGoodsSn = null;
			// 11位码处理
			if (goodsSn.length() > 6) {
				queryGoodsSn = goodsSn.substring(0, 6);
			} else {
				queryGoodsSn = goodsSn;
			}
			BgProductRequest bgReq = new BgProductRequest();
			bgReq.setChannelCode(channelCode);
			bgReq.setProductSysCode(queryGoodsSn);
			ResultData<BgProductInfo> resultData = bgProductService.getProductDetailInfo(bgReq);
			if (resultData == null) {
				info.setMessage("查询商品服务接口返回为空！");
				return info;
			}
			if (resultData.getIsOk() != Constant.OS_YES) {
				info.setMessage(resultData.getMsg());
				return info;
			}
			BgProductInfo bgProductInfo = resultData.getData();
			if (bgProductInfo != null) {
				ChannelGoodsVo goodsVo = new ChannelGoodsVo();
				goodsVo.setChannelCode(channelCode);
				goodsVo.setGoodsSn(queryGoodsSn);
				goodsVo.setGoodsTitle(bgProductInfo.getProductName());
				goodsVo.setGoodsName(bgProductInfo.getProductName());
				goodsVo.setMarketPrice(bgProductInfo.getMarketPrice());
				goodsVo.setChannelPrice(bgProductInfo.getMarketPrice());
				goodsVo.setBrandCode(bgProductInfo.getBrandCode());
				goodsVo.setPlatformPrice(bgProductInfo.getMarketPrice());
				if (StringUtil.isNotEmpty(goodsSn) && goodsSn.length() > 6) {
					BgProductRequest skuRequest = new BgProductRequest();
					skuRequest.setChannelCode(channelCode);
					List<String> values = new ArrayList<String>();
					values.add(queryGoodsSn);
					skuRequest.setProductSysCodeList(values);
					ResultData<BgCartResponse> cartRes = bgProductService.getProductAllInfoForCart(skuRequest);
					if (cartRes != null && cartRes.getIsOk() == Constant.OS_YES && cartRes.getData() != null) {
						if (StringUtil.isListNotNull(cartRes.getData().getProductChannelBarcodeList())) {
							for (BgProductChannelBarcode channelBarcode : cartRes.getData().getProductChannelBarcodeList()) {
								if (channelBarcode.getBarcodeSysCode().equals(goodsSn)) {
									goodsVo.setCurrColorCode(channelBarcode.getSaleAttr1ValueCode());
									goodsVo.setCurrSizeCode(channelBarcode.getSaleAttr2ValueCode());
									break;
								}
							}
						}
					}
				}
				list.add(goodsVo);
			}
			info.setMessage("查询成功");
			info.setIsOk(Constant.OS_YES);
			info.setData(list);
		} catch (Exception e) {
			logger.error("查询商品 goodsSn:"+goodsSn+",channelCode:"+channelCode+"异常", e);
			info.setMessage("查询商品"+goodsSn+ "异常" + e.getMessage());
		}
		return info;
	}

	@Override
	public ReturnInfo selectChannelSku(String channelCode, String Sku) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
