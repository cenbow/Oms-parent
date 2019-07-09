package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgchanneldb.GroupGoodsList;
import com.work.shop.oms.bean.bgchanneldb.GroupGoodsListExample;

public interface GroupGoodsListMapper {
    @ReadOnly
    int countByExample(GroupGoodsListExample example);

    @Writer
    int deleteByExample(GroupGoodsListExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(GroupGoodsList record);

    @Writer
    int insertSelective(GroupGoodsList record);

    @ReadOnly
    List<GroupGoodsList> selectByExample(GroupGoodsListExample example);

    GroupGoodsList selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") GroupGoodsList record, @Param("example") GroupGoodsListExample example);

    @Writer
    int updateByExample(@Param("record") GroupGoodsList record, @Param("example") GroupGoodsListExample example);

    @Writer
    int updateByPrimaryKeySelective(GroupGoodsList record);

    @Writer
    int updateByPrimaryKey(GroupGoodsList record);
}