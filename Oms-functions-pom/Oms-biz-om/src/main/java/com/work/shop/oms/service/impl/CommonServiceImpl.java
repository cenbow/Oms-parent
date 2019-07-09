package com.work.shop.oms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.ErpWarehouseList;
import com.work.shop.oms.bean.ErpWarehouseListExample;
import com.work.shop.oms.bean.OrderCustomDefine;
import com.work.shop.oms.bean.OrderCustomDefineExample;
import com.work.shop.oms.bean.SystemOmsResource;
import com.work.shop.oms.bean.SystemOmsResourceExample;
import com.work.shop.oms.bean.SystemShipping;
import com.work.shop.oms.bean.SystemShippingExample;
import com.work.shop.oms.dao.ErpWarehouseListMapper;
import com.work.shop.oms.dao.OrderCustomDefineMapper;
import com.work.shop.oms.dao.SystemOmsResourceMapper;
import com.work.shop.oms.dao.SystemShippingMapper;
import com.work.shop.oms.service.CommonService;
import com.work.shop.oms.utils.StringUtil;

@Service
public class CommonServiceImpl implements CommonService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private OrderCustomDefineMapper orderCustomDefineMapper;
	@Autowired
	private SystemShippingMapper systemShippingMapper;
	@Autowired
	private ErpWarehouseListMapper erpWarehouseListMapper;
	@Resource
	private SystemOmsResourceMapper systemOmsResourceMapper;

	@Override
	public List<OrderCustomDefine> selectOrderCustomDefine(
			OrderCustomDefine define) {
		// TODO Auto-generated method stub
		OrderCustomDefineExample example = new OrderCustomDefineExample();
		com.work.shop.oms.bean.OrderCustomDefineExample.Criteria criteria = example.or();
		if (define != null) {
			if (StringUtil.isNotEmpty(define.getCode())) {
				criteria.andCodeEqualTo(define.getCode());
			}
			if (define.getType() != null) {
				criteria.andTypeEqualTo(define.getType());
			}
			if (StringUtil.isNotEmpty(define.getName())) {
				criteria.andNameEqualTo(define.getName());
			}
			if(define.getDisplay() !=null){
				criteria.andDisplayEqualTo(define.getDisplay());
			}
		}
		return orderCustomDefineMapper.selectByExample(example);
	}
	
	/**
     * 获取配送方式（快递商家下拉框）
     */
    public List<SystemShipping> selectSystemShippingList(
            SystemShipping systemShipping) {
        SystemShippingExample systemShippingExample=new SystemShippingExample();
        if(StringUtil.isNotNull(systemShipping.getShippingCode())){
            systemShippingExample.or().andShippingCodeEqualTo(systemShipping.getShippingCode());
        }else{
            systemShippingExample.or();
        }
        return systemShippingMapper.selectByExample(systemShippingExample);
    }
    
    @Override
    public List<ErpWarehouseList> selectErpWarehouseList(ErpWarehouseList erpWarehouseList) {
        ErpWarehouseListExample warehouseListExample = new ErpWarehouseListExample();
        ErpWarehouseListExample.Criteria criteria = warehouseListExample.or();
        if(StringUtil.isNotBlank(erpWarehouseList.getWarehouseCode())){
            criteria.andWarehouseCodeEqualTo(erpWarehouseList.getWarehouseCode());
        }
        if(erpWarehouseList.getDepotType() != null){
            criteria.andDepotTypeGreaterThan(erpWarehouseList.getDepotType());
        }
        return erpWarehouseListMapper.selectByExample(warehouseListExample);
    }

	@Override
	public List<SystemOmsResource> selectSystemOmsResource(
			SystemOmsResource resource) {
		// TODO Auto-generated method stub
		SystemOmsResourceExample example = new SystemOmsResourceExample();
		com.work.shop.oms.bean.SystemOmsResourceExample.Criteria criteria = example.or();
		if (resource != null) {
			if (StringUtil.isNotEmpty(resource.getResourceType())) {
				criteria.andResourceTypeEqualTo(resource.getResourceType());
			}
			if (StringUtil.isNotEmpty(resource.getParentCode())) {
				criteria.andParentCodeEqualTo(resource.getParentCode());
			}
			if (StringUtil.isNotEmpty(resource.getResourceCode())) {
				criteria.andResourceCodeEqualTo(resource.getResourceCode());
			}
			if (resource.getIsShow() != null) {
				criteria.andIsShowEqualTo(resource.getIsShow());
			}
		}
		List<SystemOmsResource> list = systemOmsResourceMapper.selectByExample(example);
		return list;
	}
    
    

}
