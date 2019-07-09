package com.work.shop.oms.stock.bean;

import java.io.Serializable;
import java.util.List;

import com.work.shop.stockcenter.client.dto.SkuStock;

public class StockResultBean implements Serializable {
	
	private static final long serialVersionUID = -3560661058505703071L;
	private int code;//1 成功 ，0 失败 ，-500 处理发生异常,2缺货
	private String msg;//操作结果描述，缺货的话组装信息
	private Integer stockType;//调用接口类型 0 库存中心 1 老版本 2兼容方式
	private String exception;//处理发生异常的话，出错信息记录
	private List<SkuStock> skuStockList; //接受占用释放操作会把执行后对应sku的最新库存值
	private	List<String> newSku ;//为兼容模式的话，白名单走库存中心的sku，以便于在返回正常单的时候，不同步渠道
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public List<SkuStock> getSkuStockList() {
		return skuStockList;
	}
	public void setSkuStockList(List<SkuStock> skuStockList) {
		this.skuStockList = skuStockList;
	}
	public Integer getStockType() {
		return stockType;
	}
	public void setStockType(Integer stockType) {
		this.stockType = stockType;
	}
	public List<String> getNewSku() {
		return newSku;
	}
	public void setNewSku(List<String> newSku) {
		this.newSku = newSku;
	}
	
	
}
