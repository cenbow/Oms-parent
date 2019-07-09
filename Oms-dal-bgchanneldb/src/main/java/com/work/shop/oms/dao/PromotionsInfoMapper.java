package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgchanneldb.PromotionsInfo;
import com.work.shop.oms.bean.bgchanneldb.PromotionsInfoExample;

public interface PromotionsInfoMapper {
	@Writer
    int countByExample(PromotionsInfoExample example);

    @Writer
    int deleteByExample(PromotionsInfoExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(PromotionsInfo record);

    @Writer
    int insertSelective(PromotionsInfo record);

    @Writer
    List<PromotionsInfo> selectByExample(PromotionsInfoExample example);

    PromotionsInfo selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") PromotionsInfo record, @Param("example") PromotionsInfoExample example);

    @Writer
    int updateByExample(@Param("record") PromotionsInfo record, @Param("example") PromotionsInfoExample example);

    @Writer
    int updateByPrimaryKeySelective(PromotionsInfo record);

    @Writer
    int updateByPrimaryKey(PromotionsInfo record);
}