package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.WkUdDistribute;
import com.work.shop.oms.bean.WkUdDistributeExample;

public interface WkUdDistributeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(WkUdDistributeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(WkUdDistributeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer wkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    @Writer
    int insert(WkUdDistribute record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(WkUdDistribute record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    @ReadOnly
    List<WkUdDistribute> selectByExample(WkUdDistributeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    WkUdDistribute selectByPrimaryKey(Integer wkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") WkUdDistribute record, @Param("example") WkUdDistributeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") WkUdDistribute record, @Param("example") WkUdDistributeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(WkUdDistribute record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_ud_distribute
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(WkUdDistribute record);
}