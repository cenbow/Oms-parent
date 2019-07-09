package com.work.shop.oms.param.bean;

import java.io.Serializable;

/**
 * 退货单入库商品结构
 * @author huangl
 *
 */
public class LstSfSchTaskExecOosDtls implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String PROD_NUM; //编码
	public Integer QTY;//数量
	public Double PRICE;//价格
	public String LOC_ID;//货号
}
