package com.work.shop.oms.common.bean;

import java.util.ArrayList;
import java.util.List;

import com.work.shop.oms.bean.OrderGoods;
import com.work.shop.oms.common.bean.OrderDetailsInfo;

public class OrderInfoMappingBean {
	private List<OrderGoods> deleteOrderGoods=new ArrayList<OrderGoods>(); //number 原始量
	private List<OrderGoods> add_updateOrderGoods=new ArrayList<OrderGoods>();//number 为变化量
//	private List<OrderGoodsWarehouse> deleteOrderGoodsWarehouse=new ArrayList<OrderGoodsWarehouse>();//number 原始量
//	private List<OrderGoodsWarehouse> updateOrderGoodsWarehouse=new ArrayList<OrderGoodsWarehouse>();//number 为变换量
	private List<OrderDetailsInfo> detail_ERP=new ArrayList<OrderDetailsInfo>();
	
	public List<OrderGoods> getDeleteOrderGoods() {
		return deleteOrderGoods;
	}
	public void setDeleteOrderGoods(List<OrderGoods> deleteOrderGoods) {
		this.deleteOrderGoods = deleteOrderGoods;
	}
	public List<OrderGoods> getAdd_updateOrderGoods() {
		return add_updateOrderGoods;
	}
	public void setAdd_updateOrderGoods(List<OrderGoods> add_updateOrderGoods) {
		this.add_updateOrderGoods = add_updateOrderGoods;
	}
	public List<OrderDetailsInfo> getDetail_ERP() {
		return detail_ERP;
	}
	public void setDetail_ERP(List<OrderDetailsInfo> detail_ERP) {
		this.detail_ERP = detail_ERP;
	}

	@Override
	public String toString() {
		return "OrderInfoMappingBean [detail_ERP=" + detail_ERP + "]";
	}
}
