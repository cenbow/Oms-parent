package com.work.shop.oms.vo;

import java.io.Serializable;
import java.util.List;
/**
 * 接收退单操作动作param
 * @author cage
 *
 */
public class ReturnOrderParam implements Serializable {

	private static final long serialVersionUID = -135657135944865832L;
	
	private String returnSn;//退单号
	
//	private String goodsId;//商品id
	private List<StorageGoods> storageGoods;
	
	private String actionNote;//操作备注
	
	private Integer userId;//操作人id
	
	private String userName;//操作人名字
	
	private boolean pullInAll;
	
	private boolean toERP;
	
	public List<StorageGoods> getStorageGoods() {
		return storageGoods;
	}

	public void setStorageGoods(List<StorageGoods> storageGoods) {
		this.storageGoods = storageGoods;
	}

	public boolean isToERP() {
		return toERP;
	}

	public void setToERP(boolean toERP) {
		this.toERP = toERP;
	}

	public boolean isPullInAll() {
		return pullInAll;
	}

	public void setPullInAll(boolean pullInAll) {
		this.pullInAll = pullInAll;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}


	public String getActionNote() {
		return actionNote;
	}

	public void setActionNote(String actionNote) {
		this.actionNote = actionNote;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
