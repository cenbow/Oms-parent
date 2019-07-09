package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgchanneldb.PromotionsListGoods;
import com.work.shop.oms.bean.bgchanneldb.PromotionsListGoodsExample;

public interface PromotionsListGoodsMapper {
	@Writer
    int countByExample(PromotionsListGoodsExample example);

    @Writer
    int deleteByExample(PromotionsListGoodsExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(PromotionsListGoods record);

    @Writer
    int insertSelective(PromotionsListGoods record);

    @Writer
    List<PromotionsListGoods> selectByExample(PromotionsListGoodsExample example);

    PromotionsListGoods selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") PromotionsListGoods record, @Param("example") PromotionsListGoodsExample example);

    @Writer
    int updateByExample(@Param("record") PromotionsListGoods record, @Param("example") PromotionsListGoodsExample example);

    @Writer
    int updateByPrimaryKeySelective(PromotionsListGoods record);

    @Writer
    int updateByPrimaryKey(PromotionsListGoods record);
}