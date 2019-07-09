package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SellerParam implements Serializable {

	private static final long serialVersionUID = 211252459267330290L;
	
	private List<String> sellers;
	
	private Date dateFrom;
	
	private Date dateTo;
	
	private Integer returnOrderStatus;
	
	private Date updateTimeBegin;//更新时间
	
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
