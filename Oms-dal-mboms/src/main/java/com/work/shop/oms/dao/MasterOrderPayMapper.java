package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MasterOrderPayMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(MasterOrderPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(MasterOrderPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(String masterPaySn);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    @Writer
    int insert(MasterOrderPay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(MasterOrderPay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    @ReadOnly
    List<MasterOrderPay> selectByExample(MasterOrderPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    MasterOrderPay selectByPrimaryKey(String masterPaySn);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") MasterOrderPay record, @Param("example") MasterOrderPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") MasterOrderPay record, @Param("example") MasterOrderPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(MasterOrderPay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_pay
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(MasterOrderPay record);

    /**
     * 根据订单号查询支付单
     *
     * @param masterOrderSn
     * @return com.work.shop.oms.bean.MasterOrderPay
     * @author matianqi
     * @date 2020-03-22 15:08
     */
    MasterOrderPay selectByMasterOrderSn(String masterOrderSn);
}