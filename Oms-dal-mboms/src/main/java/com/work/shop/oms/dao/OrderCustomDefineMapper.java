package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.OrderCustomDefine;
import com.work.shop.oms.bean.OrderCustomDefineExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderCustomDefineMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(OrderCustomDefineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(OrderCustomDefineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(String code);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    @Writer
    int insert(OrderCustomDefine record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(OrderCustomDefine record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    @ReadOnly
    List<OrderCustomDefine> selectByExample(OrderCustomDefineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    OrderCustomDefine selectByPrimaryKey(String code);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") OrderCustomDefine record, @Param("example") OrderCustomDefineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") OrderCustomDefine record, @Param("example") OrderCustomDefineExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(OrderCustomDefine record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_custom_define
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(OrderCustomDefine record);
}