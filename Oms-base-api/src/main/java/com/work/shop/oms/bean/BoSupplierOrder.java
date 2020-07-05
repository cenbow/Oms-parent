package com.work.shop.oms.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BoSupplierOrder implements Serializable {
    private Integer id;

    private String boId;

    private String supplierCode;

    private Integer childCompanyId;

    private String masterOrderSn;

    private Byte hasInsurance;

    private Integer saleModel;

    private Integer paymentPeriod;

    private BigDecimal occupyDebt;

    private BigDecimal allocation;

    private BigDecimal allocationIncome;

    private BigDecimal platformOperatingFee;

    private BigDecimal basicProfitSharing;

    private BigDecimal excessProfitSharing;

    private Integer status;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    private String companyName;

    private String companyCode;

    private Integer payId;

    private String payName;

    private BigDecimal lastAllocation;

    private BigDecimal lastOccupyDebt;

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

    public String getMasterOrderSn() {
        return masterOrderSn;
    }

    public void setMasterOrderSn(String masterOrderSn) {
        this.masterOrderSn = masterOrderSn == null ? null : masterOrderSn.trim();
    }

    public Byte getHasInsurance() {
        return hasInsurance;
    }

    public void setHasInsurance(Byte hasInsurance) {
        this.hasInsurance = hasInsurance;
    }

    public Integer getSaleModel() {
        return saleModel;
    }

    public void setSaleModel(Integer saleModel) {
        this.saleModel = saleModel;
    }

    public Integer getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(Integer paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public BigDecimal getOccupyDebt() {
        return occupyDebt;
    }

    public void setOccupyDebt(BigDecimal occupyDebt) {
        this.occupyDebt = occupyDebt;
    }

    public BigDecimal getAllocation() {
        return allocation;
    }

    public void setAllocation(BigDecimal allocation) {
        this.allocation = allocation;
    }

    public BigDecimal getAllocationIncome() {
        return allocationIncome;
    }

    public void setAllocationIncome(BigDecimal allocationIncome) {
        this.allocationIncome = allocationIncome;
    }

    public BigDecimal getPlatformOperatingFee() {
        return platformOperatingFee;
    }

    public void setPlatformOperatingFee(BigDecimal platformOperatingFee) {
        this.platformOperatingFee = platformOperatingFee;
    }

    public BigDecimal getBasicProfitSharing() {
        return basicProfitSharing;
    }

    public void setBasicProfitSharing(BigDecimal basicProfitSharing) {
        this.basicProfitSharing = basicProfitSharing;
    }

    public BigDecimal getExcessProfitSharing() {
        return excessProfitSharing;
    }

    public void setExcessProfitSharing(BigDecimal excessProfitSharing) {
        this.excessProfitSharing = excessProfitSharing;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Integer getChildCompanyId() {
        return childCompanyId;
    }

    public void setChildCompanyId(Integer childCompanyId) {
        this.childCompanyId = childCompanyId;
    }

    public BigDecimal getLastAllocation() {
        return lastAllocation;
    }

    public void setLastAllocation(BigDecimal lastAllocation) {
        this.lastAllocation = lastAllocation;
    }

    public BigDecimal getLastOccupyDebt() {
        return lastOccupyDebt;
    }

    public void setLastOccupyDebt(BigDecimal lastOccupyDebt) {
        this.lastOccupyDebt = lastOccupyDebt;
    }
}