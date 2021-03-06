package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.GoodsReturnChangeDetail;
import com.work.shop.oms.bean.GoodsReturnChangeDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsReturnChangeDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(GoodsReturnChangeDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(GoodsReturnChangeDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    @Writer
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    @Writer
    int insert(GoodsReturnChangeDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(GoodsReturnChangeDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    @ReadOnly
    List<GoodsReturnChangeDetail> selectByExample(GoodsReturnChangeDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    GoodsReturnChangeDetail selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") GoodsReturnChangeDetail record, @Param("example") GoodsReturnChangeDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") GoodsReturnChangeDetail record, @Param("example") GoodsReturnChangeDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKeySelective(GoodsReturnChangeDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_return_change_detail
     *
     * @mbggenerated
     */
    @Writer
    int updateByPrimaryKey(GoodsReturnChangeDetail record);
}