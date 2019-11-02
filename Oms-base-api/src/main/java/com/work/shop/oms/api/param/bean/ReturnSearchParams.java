package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 退单查询
 * @author QuYachu
 */
public class ReturnSearchParams implements Serializable {

	private static final long serialVersionUID = 7523094549896229606L;
	
	//支持按退货单号、发货单号、商品编码、商家货号、商品名称、退货人姓名、退货申请时间段、退货完成时间段查询对应的退货单

	/**
	 * 页码
	 */
	private int pageSize;

	/**
	 * 每页数目
	 */
	private int pageNum;

	/**
	 * 开始条目数
	 */
	private int start;

	/**
	 * 退单编码
	 */
	private String returnSn;

	/**
	 * 退单编码列表
	 */
	private List<String> returnSnList;
	
	private String customCode;
	
	private String goodsSn;
	
	private String goodsName;
	
	private String userName;
	
	private Date addTimeStart;
	
	private Date addTimeEnd;
	
	private Date clearTimeStart;
	
	private Date clearTimeEnd;
	
	private Date updateTimeBegin;
    
    private Date updateTimeEnd;
	
	private String orderBy;

	/**
	 * 供销商编码
	 */
	private String seller;

	/**
	 * 子单号
	 */
	private String relatingOrderSn;

	/**
	 * 退单状态：0未确定、1已确认、4无效、10已完成
	 */
	private Integer returnOrderStatus;

	/**
	 * 快递单号
	 */
	private String returnInvoiceNo;

	/**
	 * 主单号
	 */
	private String masterOrderSn;

    /**
     * 结算状态 0未结算；1，已结算；2，待结算
     */
	private Integer payStatus;

    /**
     * 配送状态 0未收货;1已收货未质检;2.质检通过待入库;3已入库
     */
	private Integer shipStatus;

    /**
     * 对账单状态 0未生成、1已生成
     */
	private Integer selltedBillStatus;
	
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

    public List<String> getReturnSnList() {
        return returnSnList;
    }

    public void setReturnSnList(List<String> returnSnList) {
        this.returnSnList = returnSnList;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(Integer shipStatus) {
        this.shipStatus = shipStatus;
    }

    public Integer getSelltedBillStatus() {
        return selltedBillStatus;
    }

    public void setSelltedBillStatus(Integer selltedBillStatus) {
        this.selltedBillStatus = selltedBillStatus;
    }
}
