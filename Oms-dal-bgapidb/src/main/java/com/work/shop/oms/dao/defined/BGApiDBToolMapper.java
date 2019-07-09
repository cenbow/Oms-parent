package com.work.shop.oms.dao.defined;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.Writer;

public interface BGApiDBToolMapper {
	@Writer
	List<String> findOrderOutSn(Map<String,Object> param);
	 

}
