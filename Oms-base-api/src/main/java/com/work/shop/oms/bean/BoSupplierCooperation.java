package com.work.shop.oms.bean;

import java.math.BigDecimal;
import java.util.Date;

public class BoSupplierCooperation {
    private Integer id;

    private String boId;

    private String supplierCode;

    private Integer childCompanyId;

    private BigDecimal totalDebt;

    private Byte cooperationStatus;

    private String cooperationRemark;

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

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }

    public Integer getChildCompanyId() {
        return childCompanyId;
    }

    public void setChildCompanyId(Integer childCompanyId) {
        this.childCompanyId = childCompanyId;
    }

    public BigDecimal getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(BigDecimal totalDebt) {
        this.totalDebt = totalDebt;
    }

    public Byte getCooperationStatus() {
        return cooperationStatus;
    }

    public void setCooperationStatus(Byte cooperationStatus) {
        this.cooperationStatus = cooperationStatus;
    }

    public String getCooperationRemark() {
        return cooperationRemark;
    }

    public void setCooperationRemark(String cooperationRemark) {
        this.cooperationRemark = cooperationRemark == null ? null : cooperationRemark.trim();
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