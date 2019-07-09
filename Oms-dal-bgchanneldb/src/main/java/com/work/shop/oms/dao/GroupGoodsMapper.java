package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgchanneldb.GroupGoods;
import com.work.shop.oms.bean.bgchanneldb.GroupGoodsExample;

public interface GroupGoodsMapper {
    @ReadOnly
    int countByExample(GroupGoodsExample example);

    @Writer
    int deleteByExample(GroupGoodsExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(GroupGoods record);

    @Writer
    int insertSelective(GroupGoods record);

    @ReadOnly
    List<GroupGoods> selectByExample(GroupGoodsExample example);

    GroupGoods selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") GroupGoods record, @Param("example") GroupGoodsExample example);

    @Writer
    int updateByExample(@Param("record") GroupGoods record, @Param("example") GroupGoodsExample example);

    @Writer
    int updateByPrimaryKeySelective(GroupGoods record);

    @Writer
    int updateByPrimaryKey(GroupGoods record);
}