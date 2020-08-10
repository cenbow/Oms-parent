package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import java.util.List;

import com.work.shop.oms.bean.ProductGroupBuyBean;
import org.apache.ibatis.annotations.Param;

public interface MasterOrderGoodsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(MasterOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(MasterOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int insert(MasterOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(MasterOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @ReadOnly
    List<MasterOrderGoods> selectByExampleWithBLOBs(MasterOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @ReadOnly
    List<MasterOrderGoods> selectByExample(MasterOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    MasterOrderGoods selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") MasterOrderGoods record, @Param("example") MasterOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleWithBLOBs(@Param("record") MasterOrderGoods record, @Param("example") MasterOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") MasterOrderGoods record, @Param("example") MasterOrderGoodsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(MasterOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeyWithBLOBs(MasterOrderGoods record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table master_order_goods
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(MasterOrderGoods record);

    List<MasterOrderGoods> selectByOrderSnList(@Param("bean") ProductGroupBuyBean productGroupBuyBean);
}