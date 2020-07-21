package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.MasterOrderInfoExtendExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MasterOrderInfoExtendMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(MasterOrderInfoExtendExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(MasterOrderInfoExtendExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(String masterOrderSn);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    @Writer
    int insert(MasterOrderInfoExtend record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(MasterOrderInfoExtend record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    @ReadOnly
    List<MasterOrderInfoExtend> selectByExample(MasterOrderInfoExtendExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    MasterOrderInfoExtend selectByPrimaryKey(String masterOrderSn);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") MasterOrderInfoExtend record, @Param("example") MasterOrderInfoExtendExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") MasterOrderInfoExtend record, @Param("example") MasterOrderInfoExtendExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(MasterOrderInfoExtend record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_info_extend
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(MasterOrderInfoExtend record);

    List<MasterOrderInfoExtend> selectOrderSnByGroupId(Integer groupId);
}