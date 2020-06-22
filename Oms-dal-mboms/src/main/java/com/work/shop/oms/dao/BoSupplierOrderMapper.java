package com.work.shop.oms.dao;

import com.work.shop.oms.bean.BoSupplierOrder;

public interface BoSupplierOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BoSupplierOrder record);

    int insertSelective(BoSupplierOrder record);

    BoSupplierOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BoSupplierOrder record);

    int updateByPrimaryKey(BoSupplierOrder record);
}