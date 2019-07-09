package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgchanneldb.PromotionsListInfor;
import com.work.shop.oms.bean.bgchanneldb.PromotionsListInforExample;

public interface PromotionsListInforMapper {
	@Writer
    int countByExample(PromotionsListInforExample example);

    @Writer
    int deleteByExample(PromotionsListInforExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(PromotionsListInfor record);

    @Writer
    int insertSelective(PromotionsListInfor record);

    @Writer
    List<PromotionsListInfor> selectByExample(PromotionsListInforExample example);

    PromotionsListInfor selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") PromotionsListInfor record, @Param("example") PromotionsListInforExample example);

    @Writer
    int updateByExample(@Param("record") PromotionsListInfor record, @Param("example") PromotionsListInforExample example);

    @Writer
    int updateByPrimaryKeySelective(PromotionsListInfor record);

    @Writer
    int updateByPrimaryKey(PromotionsListInfor record);
}