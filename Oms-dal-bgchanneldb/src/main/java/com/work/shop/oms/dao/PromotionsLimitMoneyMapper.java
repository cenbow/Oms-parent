package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgchanneldb.PromotionsLimitMoney;
import com.work.shop.oms.bean.bgchanneldb.PromotionsLimitMoneyExample;

public interface PromotionsLimitMoneyMapper {
	@Writer
    int countByExample(PromotionsLimitMoneyExample example);

    @Writer
    int deleteByExample(PromotionsLimitMoneyExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(PromotionsLimitMoney record);

    @Writer
    int insertSelective(PromotionsLimitMoney record);

    @Writer
    List<PromotionsLimitMoney> selectByExample(PromotionsLimitMoneyExample example);

    PromotionsLimitMoney selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") PromotionsLimitMoney record, @Param("example") PromotionsLimitMoneyExample example);

    @Writer
    int updateByExample(@Param("record") PromotionsLimitMoney record, @Param("example") PromotionsLimitMoneyExample example);

    @Writer
    int updateByPrimaryKeySelective(PromotionsLimitMoney record);

    @Writer
    int updateByPrimaryKey(PromotionsLimitMoney record);
}