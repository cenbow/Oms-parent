package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgapidb.YikeTrade;
import com.work.shop.oms.bean.bgapidb.YikeTradeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface YikeTradeMapper {
    @ReadOnly
    int countByExample(YikeTradeExample example);

    @Writer
    int deleteByExample(YikeTradeExample example);

    @Writer
    int deleteByPrimaryKey(String code);

    @Writer
    int insert(YikeTrade record);

    @Writer
    int insertSelective(YikeTrade record);

    @ReadOnly
    List<YikeTrade> selectByExample(YikeTradeExample example);

    YikeTrade selectByPrimaryKey(String code);

    @Writer
    int updateByExampleSelective(@Param("record") YikeTrade record, @Param("example") YikeTradeExample example);

    @Writer
    int updateByExample(@Param("record") YikeTrade record, @Param("example") YikeTradeExample example);

    @Writer
    int updateByPrimaryKeySelective(YikeTrade record);

    @Writer
    int updateByPrimaryKey(YikeTrade record);
}