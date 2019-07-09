package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgchanneldb.InterfaceProperties;
import com.work.shop.oms.bean.bgchanneldb.InterfacePropertiesExample;

public interface InterfacePropertiesMapper {
	@Writer
    int countByExample(InterfacePropertiesExample example);

    @Writer
    int deleteByExample(InterfacePropertiesExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(InterfaceProperties record);

    @Writer
    int insertSelective(InterfaceProperties record);

    @Writer
    List<InterfaceProperties> selectByExample(InterfacePropertiesExample example);

    InterfaceProperties selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") InterfaceProperties record, @Param("example") InterfacePropertiesExample example);

    @Writer
    int updateByExample(@Param("record") InterfaceProperties record, @Param("example") InterfacePropertiesExample example);

    @Writer
    int updateByPrimaryKeySelective(InterfaceProperties record);

    @Writer
    int updateByPrimaryKey(InterfaceProperties record);
    
    @Writer
    int insertNativeSql(String value);
    
    @Writer
    int updateNativeSql(String value);
    
    @Writer
    int deleteNativeSql(String value);
}