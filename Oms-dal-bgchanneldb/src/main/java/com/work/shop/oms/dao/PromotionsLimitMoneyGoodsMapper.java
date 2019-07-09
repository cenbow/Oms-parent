package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgchanneldb.PromotionsLimitMoneyGoods;
import com.work.shop.oms.bean.bgchanneldb.PromotionsLimitMoneyGoodsExample;

public interface PromotionsLimitMoneyGoodsMapper {
	@Writer
    int countByExample(PromotionsLimitMoneyGoodsExample example);

    @Writer
    int deleteByExample(PromotionsLimitMoneyGoodsExample example);

    @Writer
    int insert(PromotionsLimitMoneyGoods record);

    @Writer
    int insertSelective(PromotionsLimitMoneyGoods record);

    @Writer
    List<PromotionsLimitMoneyGoods> selectByExample(PromotionsLimitMoneyGoodsExample example);

    @Writer
    int updateByExampleSelective(@Param("record") PromotionsLimitMoneyGoods record, @Param("example") PromotionsLimitMoneyGoodsExample example);

    @Writer
    int updateByExample(@Param("record") PromotionsLimitMoneyGoods record, @Param("example") PromotionsLimitMoneyGoodsExample example);
}