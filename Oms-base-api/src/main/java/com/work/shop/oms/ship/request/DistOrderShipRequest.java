package com.work.shop.oms.ship.request;

import java.util.List;

import com.work.shop.oms.order.request.OmsRequest;
import com.work.shop.oms.ship.bean.DistOrderPackages;

/**
 * 供应商订单发货请求参数
 * @author QuYachu
 */
public class DistOrderShipRequest extends OmsRequest {

	private static final long serialVersionUID = 1402127580757539546L;

	/**
	 * 仓库编码
	 */
	private String warehouseCode;

	/**
	 * 包裹信息
	 */
	private List<DistOrderPackages> packages;

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public List<DistOrderPackages> getPackages() {
		return packages;
	}

	public void setPackages(List<DistOrderPackages> packages) {
		this.packages = packages;
	}
}