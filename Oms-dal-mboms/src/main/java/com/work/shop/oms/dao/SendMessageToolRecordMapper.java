package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.SendMessageToolRecord;
import com.work.shop.oms.bean.SendMessageToolRecordExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SendMessageToolRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(SendMessageToolRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(SendMessageToolRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @Writer
    int insert(SendMessageToolRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(SendMessageToolRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @ReadOnly
    List<SendMessageToolRecord> selectByExampleWithBLOBs(SendMessageToolRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @ReadOnly
    List<SendMessageToolRecord> selectByExample(SendMessageToolRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    SendMessageToolRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") SendMessageToolRecord record, @Param("example") SendMessageToolRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleWithBLOBs(@Param("record") SendMessageToolRecord record, @Param("example") SendMessageToolRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") SendMessageToolRecord record, @Param("example") SendMessageToolRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(SendMessageToolRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeyWithBLOBs(SendMessageToolRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table send_message_tool_record
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(SendMessageToolRecord record);
}