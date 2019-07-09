package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgapidb.OtherTrade;
import com.work.shop.oms.bean.bgapidb.OtherTradeExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OtherTradeMapper {
	@Writer
    int countByExample(OtherTradeExample example);

    @Writer
    int deleteByExample(OtherTradeExample example);

    @Writer
    int deleteByPrimaryKey(String orderId);

    @Writer
    int insert(OtherTrade record);

    @Writer
    int insertSelective(OtherTrade record);

    @Writer
    List<OtherTrade> selectByExample(OtherTradeExample example);

    OtherTrade selectByPrimaryKey(String orderId);

    @Writer
    int updateByExampleSelective(@Param("record") OtherTrade record, @Param("example") OtherTradeExample example);

    @Writer
    int updateByExample(@Param("record") OtherTrade record, @Param("example") OtherTradeExample example);

    @Writer
    int updateByPrimaryKeySelective(OtherTrade record);

    @Writer
    int updateByPrimaryKey(OtherTrade record);
}