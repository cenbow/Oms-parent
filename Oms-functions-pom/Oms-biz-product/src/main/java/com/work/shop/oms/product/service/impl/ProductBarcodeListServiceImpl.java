package com.work.shop.oms.product.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.ProductBarcodeList;
import com.work.shop.oms.bean.ProductBarcodeListExample;
import com.work.shop.oms.product.service.ProductBarcodeListService;
import com.work.shop.oms.utils.StringUtil;

@Service
public class ProductBarcodeListServiceImpl implements ProductBarcodeListService{

//	@Resource
//	private ProductBarcodeListMapper productBarcodeListMapper;
	
	@Override
	public List<ProductBarcodeList> selectCustomList(ProductBarcodeList barcodeList) {
		if (barcodeList == null) {
			return null;
		}
		ProductBarcodeListExample example = new ProductBarcodeListExample();
		ProductBarcodeListExample.Criteria criteria = example.or();
		if (StringUtil.isNotEmpty(barcodeList.getCustumCode())) {
			criteria.andCustumCodeEqualTo(barcodeList.getCustumCode());
		}
		if (StringUtil.isNotEmpty(barcodeList.getGoodsSn())) {
			criteria.andGoodsSnEqualTo(barcodeList.getGoodsSn());
		}
		if (StringUtil.isNotEmpty(barcodeList.getGoodsSn())) {
			criteria.andGoodsSnEqualTo(barcodeList.getGoodsSn());
		}
//		List<ProductBarcodeList> items = productBarcodeListMapper.selectByExample(example);
		return null;
	}
}
