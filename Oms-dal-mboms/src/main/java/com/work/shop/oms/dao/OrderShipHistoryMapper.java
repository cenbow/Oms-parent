package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.OrderShipHistory;
import com.work.shop.oms.bean.OrderShipHistoryExample;
import com.work.shop.oms.bean.OrderShipHistoryKey;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderShipHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(OrderShipHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(OrderShipHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(OrderShipHistoryKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    @Writer
    int insert(OrderShipHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(OrderShipHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    @ReadOnly
    List<OrderShipHistory> selectByExample(OrderShipHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    OrderShipHistory selectByPrimaryKey(OrderShipHistoryKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") OrderShipHistory record, @Param("example") OrderShipHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") OrderShipHistory record, @Param("example") OrderShipHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(OrderShipHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_ship_history
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(OrderShipHistory record);
}