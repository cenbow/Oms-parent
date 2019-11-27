package com.work.shop.oms.bean;

import java.io.Serializable;

/**
 * 订单审批信息
 * @author QuYachu
 */
public class UserCompanyOrderApproval implements Serializable {

    /**
     * 下单用户id
     */
    private String userAccount;

    /**
     * 订单号
     */
    private String masterOrderSn;

    /**
     * 客户订单合同号
     */
    private String contractCode;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
}
