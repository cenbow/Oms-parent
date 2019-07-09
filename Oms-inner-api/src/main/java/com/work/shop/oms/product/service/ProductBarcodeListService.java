package com.work.shop.oms.product.service;

import java.util.List;

import com.work.shop.oms.bean.ProductBarcodeList;

public interface ProductBarcodeListService {

	List<ProductBarcodeList> selectCustomList(ProductBarcodeList  barcodeList);
}
