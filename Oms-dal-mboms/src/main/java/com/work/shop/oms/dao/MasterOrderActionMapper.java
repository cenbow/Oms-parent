package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.MasterOrderAction;
import com.work.shop.oms.bean.MasterOrderActionExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MasterOrderActionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(MasterOrderActionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(MasterOrderActionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer actionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @Writer
    int insert(MasterOrderAction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(MasterOrderAction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @ReadOnly
    List<MasterOrderAction> selectByExampleWithBLOBs(MasterOrderActionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @ReadOnly
    List<MasterOrderAction> selectByExample(MasterOrderActionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    MasterOrderAction selectByPrimaryKey(Integer actionId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") MasterOrderAction record, @Param("example") MasterOrderActionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleWithBLOBs(@Param("record") MasterOrderAction record, @Param("example") MasterOrderActionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") MasterOrderAction record, @Param("example") MasterOrderActionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(MasterOrderAction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeyWithBLOBs(MasterOrderAction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_action
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(MasterOrderAction record);
}