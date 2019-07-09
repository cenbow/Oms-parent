package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.SystemConfig;
import com.work.shop.oms.bean.SystemConfigExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemConfigMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(SystemConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(SystemConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @Writer
    int insert(SystemConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(SystemConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @ReadOnly
    List<SystemConfig> selectByExampleWithBLOBs(SystemConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @ReadOnly
    List<SystemConfig> selectByExample(SystemConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    SystemConfig selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") SystemConfig record, @Param("example") SystemConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleWithBLOBs(@Param("record") SystemConfig record, @Param("example") SystemConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") SystemConfig record, @Param("example") SystemConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(SystemConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeyWithBLOBs(SystemConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system_config
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(SystemConfig record);
}