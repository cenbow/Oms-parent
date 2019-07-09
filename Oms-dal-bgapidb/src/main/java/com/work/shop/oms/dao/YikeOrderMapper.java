package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.bgapidb.YikeOrder;
import com.work.shop.oms.bean.bgapidb.YikeOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface YikeOrderMapper {
    @ReadOnly
    int countByExample(YikeOrderExample example);

    @Writer
    int deleteByExample(YikeOrderExample example);

    @Writer
    int deleteByPrimaryKey(Integer id);

    @Writer
    int insert(YikeOrder record);

    @Writer
    int insertSelective(YikeOrder record);

    @ReadOnly
    List<YikeOrder> selectByExample(YikeOrderExample example);

    YikeOrder selectByPrimaryKey(Integer id);

    @Writer
    int updateByExampleSelective(@Param("record") YikeOrder record, @Param("example") YikeOrderExample example);

    @Writer
    int updateByExample(@Param("record") YikeOrder record, @Param("example") YikeOrderExample example);

    @Writer
    int updateByPrimaryKeySelective(YikeOrder record);

    @Writer
    int updateByPrimaryKey(YikeOrder record);
}