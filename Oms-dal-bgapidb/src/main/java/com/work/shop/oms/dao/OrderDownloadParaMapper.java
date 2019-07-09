package com.work.shop.oms.dao;

import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgapidb.OrderDownloadPara;
import com.work.shop.oms.bean.bgapidb.OrderDownloadParaExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderDownloadParaMapper {
	@Writer
    int countByExample(OrderDownloadParaExample example);

    @Writer
    int deleteByExample(OrderDownloadParaExample example);

    @Writer
    int deleteByPrimaryKey(Long id);

    @Writer
    int insert(OrderDownloadPara record);

    @Writer
    int insertSelective(OrderDownloadPara record);

    @Writer
    List<OrderDownloadPara> selectByExample(OrderDownloadParaExample example);

    OrderDownloadPara selectByPrimaryKey(Long id);

    @Writer
    int updateByExampleSelective(@Param("record") OrderDownloadPara record, @Param("example") OrderDownloadParaExample example);

    @Writer
    int updateByExample(@Param("record") OrderDownloadPara record, @Param("example") OrderDownloadParaExample example);

    @Writer
    int updateByPrimaryKeySelective(OrderDownloadPara record);

    @Writer
    int updateByPrimaryKey(OrderDownloadPara record);
    
    
    @Writer
    int insertNativeSql(String value);
    
    @Writer
    int updateNativeSql(String value);
    
    @Writer
    int deleteNativeSql(String value);
}