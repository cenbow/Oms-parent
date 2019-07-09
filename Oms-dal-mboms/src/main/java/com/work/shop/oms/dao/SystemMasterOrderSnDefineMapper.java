package com.work.shop.oms.dao;

import java.util.Date;

import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.SystemMasterOrderSn;

public interface SystemMasterOrderSnDefineMapper {
	
	@Writer
	int insertSelectAutoColum(SystemMasterOrderSn record);

	Date selectSystemDate();
}
