package com.work.shop.oms.bean;

import com.work.shop.oms.common.bean.OcpbStatus;
import com.work.shop.oms.common.bean.ValidateOrder;

import java.io.Serializable;

/**
 * 订单完成对象
 * @author QuYachu
 */
public class MasterOrderInfoFinishBean implements Serializable {

    /**
     * 订单编码
     */
    private String masterOrderSn;

    /**
     * 校验订单对象
     */
    private ValidateOrder validateOrder;

    /**
     * 平台币状态
     */
    private OcpbStatus ocpbStatus;

    /**
     * 问题单状态
     */
    private int qustionType;

    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }

    public ValidateOrder getValidateOrder() {
        return validateOrder;
    }

    public void setValidateOrder(ValidateOrder validateOrder) {
        this.validateOrder = validateOrder;
    }

    public OcpbStatus getOcpbStatus() {
        return ocpbStatus;
    }

    public void setOcpbStatus(OcpbStatus ocpbStatus) {
        this.ocpbStatus = ocpbStatus;
    }

    public int getQustionType() {
        return qustionType;
    }

    public void setQustionType(int qustionType) {
        this.qustionType = qustionType;
    }
}
