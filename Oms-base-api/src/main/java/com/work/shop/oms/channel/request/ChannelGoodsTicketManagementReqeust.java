package com.work.shop.oms.channel.request;

import java.io.Serializable;

/**
 * 渠道商品调整单
 * @author QuYachu
 *
 */
public class ChannelGoodsTicketManagementReqeust<T> implements Serializable {

	private static final long serialVersionUID = -1467246836491597167L;
	
	public static final Integer oper_type_price = 0;
	
	public static final Integer oper_type_down_and_up = 1;
	
	public static final Integer oper_type_goods_update = 2;
	
	public static final Integer oper_type_goods_seller = 3;

	public static final Integer oper_type_goods_name = 4;
	
	public static final Integer oper_type_goods_add = 5;
	
	public static final Integer oper_type_add = 1; // 新增
	
	public static final Integer oper_type_update = 2; // 状态变更
	
	public static final Integer oper_type_list = 3; // 列表
	
	public static final Integer oper_type_model = 4; // 查询对象
	
	/**
	 * 0，修改价格，1,上下架维护，2，商品信息维护（添加商品），3.卖点管理，4商品名称调整，5.商品添加
	 */
	private Integer opertype;
	
	private Integer pageNo; // 页码
	
	private Integer pageSize; // 每页记录数
	
	private T infoBean;

	public Integer getOpertype() {
		return opertype;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public T getInfoBean() {
		return infoBean;
	}

	public void setOpertype(Integer opertype) {
		this.opertype = opertype;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setInfoBean(T infoBean) {
		this.infoBean = infoBean;
	}

}
