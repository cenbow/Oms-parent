package com.work.shop.oms.dao;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.invocation.Writer;
import com.work.shop.oms.bean.ErpWarehouseList;
import com.work.shop.oms.bean.ErpWarehouseListExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ErpWarehouseListMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erp_warehouse_list
     *
     * @mbggenerated
     */
    @ReadOnly
    int countByExample(ErpWarehouseListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erp_warehouse_list
     *
     * @mbggenerated
     */
    @Writer
    int deleteByExample(ErpWarehouseListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erp_warehouse_list
     *
     * @mbggenerated
     */
    @Writer
    int insert(ErpWarehouseList record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erp_warehouse_list
     *
     * @mbggenerated
     */
    @Writer
    int insertSelective(ErpWarehouseList record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erp_warehouse_list
     *
     * @mbggenerated
     */
    @ReadOnly
    List<ErpWarehouseList> selectByExample(ErpWarehouseListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erp_warehouse_list
     *
     * @mbggenerated
     */
    @Writer
    int updateByExampleSelective(@Param("record") ErpWarehouseList record, @Param("example") ErpWarehouseListExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table erp_warehouse_list
     *
     * @mbggenerated
     */
    @Writer
    int updateByExample(@Param("record") ErpWarehouseList record, @Param("example") ErpWarehouseListExample example);
}