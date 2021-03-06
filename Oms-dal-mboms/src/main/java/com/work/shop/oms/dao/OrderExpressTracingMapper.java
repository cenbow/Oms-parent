package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.OrderExpressTracing;
import com.work.shop.oms.bean.OrderExpressTracingExample;
import com.work.shop.oms.bean.OrderExpressTracingKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderExpressTracingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(OrderExpressTracingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(OrderExpressTracingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(OrderExpressTracingKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @Writer
    int insert(OrderExpressTracing record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(OrderExpressTracing record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @ReadOnly
    List<OrderExpressTracing> selectByExampleWithBLOBs(OrderExpressTracingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @ReadOnly
    List<OrderExpressTracing> selectByExample(OrderExpressTracingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    OrderExpressTracing selectByPrimaryKey(OrderExpressTracingKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") OrderExpressTracing record, @Param("example") OrderExpressTracingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleWithBLOBs(@Param("record") OrderExpressTracing record, @Param("example") OrderExpressTracingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") OrderExpressTracing record, @Param("example") OrderExpressTracingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(OrderExpressTracing record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeyWithBLOBs(OrderExpressTracing record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_express_tracing
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(OrderExpressTracing record);
}