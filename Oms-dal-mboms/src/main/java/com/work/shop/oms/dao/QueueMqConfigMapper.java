package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.QueueMqConfig;
import com.work.shop.oms.bean.QueueMqConfigExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QueueMqConfigMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(QueueMqConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(QueueMqConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    @Writer
    int insert(QueueMqConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(QueueMqConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    @ReadOnly
    List<QueueMqConfig> selectByExample(QueueMqConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    QueueMqConfig selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") QueueMqConfig record, @Param("example") QueueMqConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") QueueMqConfig record, @Param("example") QueueMqConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(QueueMqConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table queue_mq_config
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(QueueMqConfig record);
}