package com.work.shop.oms.api.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 请求参数对象
 * @author QuYachu
 */
public class PageListParam implements Serializable{
	
	private static final long serialVersionUID = -1281721048200815932L;
	
	/**
	 * 平台前台退换货申请单列表页面查询参数
	 */
	private String userId;
	
	private String isHistory;
	
	private String from;
	
	/**
	 * 退换申请时 
	 *   0：已取消；1：待沟通；2：已完成；3：待处理      
	 *  bg订货单时
	 *  订单列表
	 *   1 待付款、2待发货、3待收货、4已收货、5待评论、6交易关闭
	 *   9等待补款、10正在换货、11换货成功
	 */
	private int rstatus;

	/**
	 * 订单编码
	 */
	private String orderSn;
	
	private int pageSize;
	
	private int pageNum;

    /**
     * 商品编码
     */
	private String goodsSn;

    /**
     * 站点编码
     */
	private String siteCode;
	
	private String shopCode;
	
	private String orderReturnSn;

    /**
     * 多个订单编码
     */
	private List<String> orderSns;
	
	private String paySn;
	
	private String invoiceNo;
	
	private String newUserId;
	
	private boolean flag;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

    /**
     * sku列表
     */
	private List<String> skuSns;

    /**
     * 取消原因编码
     */
	private Integer cancelCode;

    /**
     * 备注
     */
	private String remarks;

    /**
     * 1创建退单，2不创建退单
     */
	private int type;

    /**
     * 退换类型，1：退货；2：换货；3：维修
     */
	private Integer returnType;

    /**
     * 查询时间天数
     */
	private Integer selectTimeDays;

    /**
     * 处理状态（0为待处理；1.退货中；2.退货成功；3.退款成功）
     */
	private Integer returnProcessStatus;

    /**
     * 物料编码
     */
	private String materielNo;

    /**
     * 客户合同编码
     */
	private String contractNum;

    /**
     * 仓库编码
     */
	private String depotCode;

    /**
     * 交货单号
     */
	private String distributeOrderSn;

    /**
     * 签章状态，0未签章1已签章
     */
	private Integer signStatus;

    /**
     * 公司id
     */
	private String companyId;

	/**
	 * 订单商品销售类型：0正常商品 1 非标定制 2 可改价商品
	 */
	private Integer goodsSaleType;

	/**
	 * 价格变更状态：0 无 1 未确认  2 平台确认 3 用户确认
	 */
	private Integer priceChangeStatus;

	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getIsHistory() {
		return isHistory;
	}
	
	public void setIsHistory(String isHistory) {
		this.isHistory = isHistory;
	}
	
	public String getOrderSn() {
		return orderSn;
	}
	
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
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
	
	public String getGoodsSn() {
		return goodsSn;
	}
	
	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}
	
	public int getRstatus() {
		return rstatus;
	}
	
	public void setRstatus(int rstatus) {
		this.rstatus = rstatus;
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getShopCode() {
		return shopCode;
	}
	
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	
	public String getSiteCode() {
		return siteCode;
	}
	
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	
	public String getOrderReturnSn() {
		return orderReturnSn;
	}
	
	public void setOrderReturnSn(String orderReturnSn) {
		this.orderReturnSn = orderReturnSn;
	}
	
	public List<String> getOrderSns() {
		return orderSns;
	}
	
	public void setOrderSns(List<String> orderSns) {
		this.orderSns = orderSns;
	}
	
	public String getPaySn() {
		return paySn;
	}
	
	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}
	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	public String getNewUserId() {
		return newUserId;
	}
	
	public void setNewUserId(String newUserId) {
		this.newUserId = newUserId;
	}
	
	public boolean isFlag() {
		return flag;
	}
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public List<String> getSkuSns() {
		return skuSns;
	}
	
	public void setSkuSns(List<String> skuSns) {
		this.skuSns = skuSns;
	}

    public Integer getCancelCode() {
        return cancelCode;
    }

    public void setCancelCode(Integer cancelCode) {
        this.cancelCode = cancelCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }

    public Integer getSelectTimeDays() {
        return selectTimeDays;
    }

    public void setSelectTimeDays(Integer selectTimeDays) {
        this.selectTimeDays = selectTimeDays;
    }

    public Integer getReturnProcessStatus() {
        return returnProcessStatus;
    }

    public void setReturnProcessStatus(Integer returnProcessStatus) {
        this.returnProcessStatus = returnProcessStatus;
    }

    public String getMaterielNo() {
        return materielNo;
    }

    public void setMaterielNo(String materielNo) {
        this.materielNo = materielNo;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }

    public String getDistributeOrderSn() {
        return distributeOrderSn;
    }

    public void setDistributeOrderSn(String distributeOrderSn) {
        this.distributeOrderSn = distributeOrderSn;
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

	public Integer getGoodsSaleType() {
		return goodsSaleType;
	}

	public void setGoodsSaleType(Integer goodsSaleType) {
		this.goodsSaleType = goodsSaleType;
	}

	public Integer getPriceChangeStatus() {
		return priceChangeStatus;
	}

	public void setPriceChangeStatus(Integer priceChangeStatus) {
		this.priceChangeStatus = priceChangeStatus;
	}

	@Override
	public String toString() {
		return "PageListParam [userId=" + userId + ", isHistory=" + isHistory
				+ ", from=" + from + ", rstatus=" + rstatus + ", orderSn="
				+ orderSn + ", pageSize=" + pageSize + ", pageNum=" + pageNum
				+ ", goodsSn=" + goodsSn + ", siteCode=" + siteCode
				+ ", shopCode=" + shopCode + ", orderReturnSn=" + orderReturnSn
				+ ", orderSns=" + orderSns + ", paySn=" + paySn
				+ ", invoiceNo=" + invoiceNo + ", newUserId=" + newUserId
				+ ", flag=" + flag + "]";
	}
}
