package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.Date;

public class ReturnSearchParams implements Serializable {

	private static final long serialVersionUID = 7523094549896229606L;
	
	//支持按退货单号、发货单号、商品编码、商家货号、商品名称、退货人姓名、退货申请时间段、退货完成时间段查询对应的退货单
	
	
	private int pageSize;//页码
	
	private int pageNum;//每页数目
	
	private int start;//开始条目数
	
	private String returnSn;
	
	private String customCode;
	
	private String goodsSn;
	
	private String goodsName;
	
	private String userName;
	
	private Date addTimeStart;
	
	private Date addTimeEnd;
	
	private Date clearTimeStart;
	
	private Date clearTimeEnd;
	
	private Date updateTimeBegin;//更新时间
    
    private Date updateTimeEnd;
	
	private String orderBy;
	
	private String seller;//供销商编码
	
	private String relatingOrderSn;//子单号
	
	private Integer returnOrderStatus;//'退单状态：0未确定、1已确认、4无效、10已完成'
	
	private String returnInvoiceNo;//快递单号
	
	private String masterOrderSn;//主单号
	
	public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }

    public String getRelatingOrderSn() {
		return relatingOrderSn;
	}

	public void setRelatingOrderSn(String relatingOrderSn) {
		this.relatingOrderSn = relatingOrderSn;
	}

	public Integer getReturnOrderStatus() {
		return returnOrderStatus;
	}

	public void setReturnOrderStatus(Integer returnOrderStatus) {
		this.returnOrderStatus = returnOrderStatus;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getReturnSn() {
		return returnSn;
	}

	public void setReturnSn(String returnSn) {
		this.returnSn = returnSn;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getAddTimeStart() {
		return addTimeStart;
	}

	public Date getUpdateTimeBegin() {
        return updateTimeBegin;
    }

    public void setUpdateTimeBegin(Date updateTimeBegin) {
        this.updateTimeBegin = updateTimeBegin;
    }

    public Date getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public void setUpdateTimeEnd(Date updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }

    public void setAddTimeStart(Date addTimeStart) {
		this.addTimeStart = addTimeStart;
	}

	public Date getAddTimeEnd() {
		return addTimeEnd;
	}

	public void setAddTimeEnd(Date addTimeEnd) {
		this.addTimeEnd = addTimeEnd;
	}

	public Date getClearTimeStart() {
		return clearTimeStart;
	}

	public void setClearTimeStart(Date clearTimeStart) {
		this.clearTimeStart = clearTimeStart;
	}

	public Date getClearTimeEnd() {
		return clearTimeEnd;
	}

	public void setClearTimeEnd(Date clearTimeEnd) {
		this.clearTimeEnd = clearTimeEnd;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getReturnInvoiceNo() {
		return returnInvoiceNo;
	}

	public void setReturnInvoiceNo(String returnInvoiceNo) {
		this.returnInvoiceNo = returnInvoiceNo;
	}

}
