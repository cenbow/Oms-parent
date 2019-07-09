package com.work.shop.oms.dao.define;

import java.util.List;
import java.util.Map;

import com.work.shop.invocation.Writer;

public interface MBOMSDBToolMapper {
	
    
    @Writer
    int insertNativeSql(String value);
    
    @Writer
    int updateNativeSql(String value);
    
    @Writer
    int deleteNativeSql(String value);
    
    List<String>  findOrderOutSn(Map<String, Object> params);

}
