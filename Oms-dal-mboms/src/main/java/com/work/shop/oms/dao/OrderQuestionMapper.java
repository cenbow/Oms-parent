package com.work.shop.oms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.OrderQuestion;
import com.work.shop.oms.bean.OrderQuestionExample;

public interface OrderQuestionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(OrderQuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(OrderQuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    @Writer
    int insert(OrderQuestion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(OrderQuestion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    @ReadOnly
    List<OrderQuestion> selectByExample(OrderQuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    OrderQuestion selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") OrderQuestion record, @Param("example") OrderQuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") OrderQuestion record, @Param("example") OrderQuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(OrderQuestion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(OrderQuestion record);
}