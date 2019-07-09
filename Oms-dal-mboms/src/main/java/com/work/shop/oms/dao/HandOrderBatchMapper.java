package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.HandOrderBatch;
import com.work.shop.oms.bean.HandOrderBatchExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HandOrderBatchMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(HandOrderBatchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(HandOrderBatchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    @Writer
    int insert(HandOrderBatch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(HandOrderBatch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    @ReadOnly
    List<HandOrderBatch> selectByExample(HandOrderBatchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    HandOrderBatch selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") HandOrderBatch record, @Param("example") HandOrderBatchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") HandOrderBatch record, @Param("example") HandOrderBatchExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(HandOrderBatch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table hand_order_batch
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(HandOrderBatch record);
}