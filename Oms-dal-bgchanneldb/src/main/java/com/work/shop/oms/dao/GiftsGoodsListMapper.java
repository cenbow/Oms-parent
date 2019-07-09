package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgchanneldb.GiftsGoodsList;
import com.work.shop.oms.bean.bgchanneldb.GiftsGoodsListExample;
import com.work.shop.oms.bean.bgchanneldb.PromotionsInfo;
import com.work.shop.oms.bean.bgchanneldb.PromotionsLimitMoney;
import com.work.shop.oms.bean.bgchanneldb.defined.GiftsGoods;

public interface GiftsGoodsListMapper {
	@Writer
    int countByExample(GiftsGoodsListExample example);

    @Writer
    int deleteByExample(GiftsGoodsListExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(GiftsGoodsList record);

    @Writer
    int insertSelective(GiftsGoodsList record);

    @Writer
    List<GiftsGoodsList> selectByExample(GiftsGoodsListExample example);

    GiftsGoodsList selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") GiftsGoodsList record, @Param("example") GiftsGoodsListExample example);

    @Writer
    int updateByExample(@Param("record") GiftsGoodsList record, @Param("example") GiftsGoodsListExample example);

    @Writer
    int updateByPrimaryKeySelective(GiftsGoodsList record);

    @Writer
    int updateByPrimaryKey(GiftsGoodsList record);
    
    @Writer
	int updateGiftsGoodsListGiftsGoodsCountById(GiftsGoodsList ggl);
    /**
     * 满赠
     * @param promotionsLimitMoney
     * @return
     */
    @ReadOnly
    List<GiftsGoods> queryGiftListLimitMoney(PromotionsLimitMoney promotionsLimitMoney);
    /**
     * 买赠
     * @param promotionsLimitSn
     * @return
     */
    @ReadOnly
    List<GiftsGoods> queryGiftListLimitSn(PromotionsInfo promotionsLimitSn);
    
}