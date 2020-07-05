package com.work.shop.oms.dao;

import com.work.shop.oms.bean.BoSupplierContract;

import java.util.List;

public interface BoSupplierContractMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BoSupplierContract record);

    int insertSelective(BoSupplierContract record);

    BoSupplierContract selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BoSupplierContract record);

    int updateByPrimaryKey(BoSupplierContract record);

    List<BoSupplierContract> selectByBoId(String boId);
}