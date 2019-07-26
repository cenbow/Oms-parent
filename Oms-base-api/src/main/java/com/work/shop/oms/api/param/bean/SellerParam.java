package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 供应商查询参数
 * @author QuYachu
 */
public class SellerParam implements Serializable {

	private static final long serialVersionUID = 211252459267330290L;

    /**
     * 供应商编码列表
     */
	private List<String> sellers;
	
	private Date dateFrom;
	
	private Date dateTo;

    /**
     * 退单状态：0未确定、1已确认、4无效、10已完成
     */
	private Integer returnOrderStatus;
	
	private Date updateTimeBegin;
	
	private Date updateTimeEnd;

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

    public Integer getReturnOrderStatus() {
		return returnOrderStatus;
	}

	public void setReturnOrderStatus(Integer returnOrderStatus) {
		this.returnOrderStatus = returnOrderStatus;
	}

	public List<String> getSellers() {
		return sellers;
	}

	public void setSellers(List<String> sellers) {
		this.sellers = sellers;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	
	
}
