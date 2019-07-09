package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgchanneldb.PromotionsLimitSn;
import com.work.shop.oms.bean.bgchanneldb.PromotionsLimitSnExample;

public interface PromotionsLimitSnMapper {
	@Writer
    int countByExample(PromotionsLimitSnExample example);

    @Writer
    int deleteByExample(PromotionsLimitSnExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(PromotionsLimitSn record);

    @Writer
    int insertSelective(PromotionsLimitSn record);

    @Writer
    List<PromotionsLimitSn> selectByExample(PromotionsLimitSnExample example);

    PromotionsLimitSn selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") PromotionsLimitSn record, @Param("example") PromotionsLimitSnExample example);

    @Writer
    int updateByExample(@Param("record") PromotionsLimitSn record, @Param("example") PromotionsLimitSnExample example);

    @Writer
    int updateByPrimaryKeySelective(PromotionsLimitSn record);

    @Writer
    int updateByPrimaryKey(PromotionsLimitSn record);
}