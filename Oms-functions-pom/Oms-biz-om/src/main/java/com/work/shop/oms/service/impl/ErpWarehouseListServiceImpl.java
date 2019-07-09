package com.work.shop.oms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;



import com.work.shop.oms.bean.ErpWarehouseList;
import com.work.shop.oms.bean.ErpWarehouseListExample;
import com.work.shop.oms.dao.ErpWarehouseListMapper;
import com.work.shop.oms.service.ErpWarehouseListService;

@Service("erpWarehouseListService")
public class ErpWarehouseListServiceImpl implements ErpWarehouseListService {

	@Resource
	private ErpWarehouseListMapper erpWarehouseListMapper;
	
	@Override
	public List<ErpWarehouseList> getErpWarehouseList() {
		ErpWarehouseListExample example = new ErpWarehouseListExample();
		ErpWarehouseListExample.Criteria criteria = example.or();
		return erpWarehouseListMapper.selectByExample(example);
	}

	

}
