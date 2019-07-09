package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.Date;

public class StorageMessageReturn implements Serializable{

    
    /** @Fields serialVersionUID: */
      	
    private static final long serialVersionUID = 7218078933147660803L;
    
    private String returnId;//退单入库时间戳
    
    private String erpEntryException;//处理信息，空为处理成功;失败则为失败信息
    
    private Date currentTime; //返回信息时间

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public String getErpEntryException() {
        return erpEntryException;
    }

    public void setErpEntryException(String erpEntryException) {
        this.erpEntryException = erpEntryException;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

}
