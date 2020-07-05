package com.work.shop.oms.bean;

import java.math.BigDecimal;
import java.util.Date;

public class BoSupplierContract {
    private Integer id;

    private String boId;

    private String childCompanyContractId;

    private String thingContractId;

    private Integer childCompanyId;

    private String supplierCode;

    private BigDecimal contractDebt;

    private Integer contractStatus;

    private String contractRemark;

    private String rejectRemark;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId == null ? null : boId.trim();
    }

    public String getChildCompanyContractId() {
        return childCompanyContractId;
    }

    public void setChildCompanyContractId(String childCompanyContractId) {
        this.childCompanyContractId = childCompanyContractId == null ? null : childCompanyContractId.trim();
    }

    public String getThingContractId() {
        return thingContractId;
    }

    public void setThingContractId(String thingContractId) {
        this.thingContractId = thingContractId == null ? null : thingContractId.trim();
    }

    public Integer getChildCompanyId() {
        return childCompanyId;
    }

    public void setChildCompanyId(Integer childCompanyId) {
        this.childCompanyId = childCompanyId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }

    public BigDecimal getContractDebt() {
        return contractDebt;
    }

    public void setContractDebt(BigDecimal contractDebt) {
        this.contractDebt = contractDebt;
    }

    public Integer getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Integer contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getContractRemark() {
        return contractRemark;
    }

    public void setContractRemark(String contractRemark) {
        this.contractRemark = contractRemark == null ? null : contractRemark.trim();
    }

    public String getRejectRemark() {
        return rejectRemark;
    }

    public void setRejectRemark(String rejectRemark) {
        this.rejectRemark = rejectRemark == null ? null : rejectRemark.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}