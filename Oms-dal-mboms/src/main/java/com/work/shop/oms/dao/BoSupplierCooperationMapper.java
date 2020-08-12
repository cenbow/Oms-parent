package com.work.shop.oms.dao;

import com.work.shop.oms.bean.BoSupplierContract;
import com.work.shop.oms.bean.BoSupplierCooperation;

import java.util.List;

public interface BoSupplierCooperationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BoSupplierCooperation record);

    int insertSelective(BoSupplierCooperation record);

    BoSupplierCooperation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BoSupplierCooperation record);

    int updateByPrimaryKey(BoSupplierCooperation record);

    List<BoSupplierCooperation> selectByBoId(String boId);
}