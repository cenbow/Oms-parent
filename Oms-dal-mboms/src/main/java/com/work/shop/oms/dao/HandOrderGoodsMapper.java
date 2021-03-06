package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.HandOrderGoods;
import com.work.shop.oms.bean.HandOrderGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HandOrderGoodsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(HandOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(HandOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int insert(HandOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(HandOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    @ReadOnly
    List<HandOrderGoods> selectByExample(HandOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    HandOrderGoods selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") HandOrderGoods record, @Param("example") HandOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") HandOrderGoods record, @Param("example") HandOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(HandOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(HandOrderGoods record);
}