package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgapidb.OtherOrder;
import com.work.shop.oms.bean.bgapidb.OtherOrderExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OtherOrderMapper {
	@Writer
    int countByExample(OtherOrderExample example);

    @Writer
    int deleteByExample(OtherOrderExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(OtherOrder record);

    @Writer
    int insertSelective(OtherOrder record);

    @Writer
    List<OtherOrder> selectByExample(OtherOrderExample example);

    OtherOrder selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") OtherOrder record, @Param("example") OtherOrderExample example);

    @Writer
    int updateByExample(@Param("record") OtherOrder record, @Param("example") OtherOrderExample example);

    @Writer
    int updateByPrimaryKeySelective(OtherOrder record);

    @Writer
    int updateByPrimaryKey(OtherOrder record);
}