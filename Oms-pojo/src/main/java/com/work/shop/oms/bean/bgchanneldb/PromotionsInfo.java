package com.work.shop.oms.bean.bgchanneldb;

import java.util.Date;

public class PromotionsInfo {
    private Integer id;

    private String promCode;

    private String promTitle;

    private Byte promStatus;

    private String shopCode;

    private String shopTitle;

    private Byte promType;

    private Date beginTime;

    private Date endTime;

    private String backup;

    private Date addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromCode() {
        return promCode;
    }

    public void setPromCode(String promCode) {
        this.promCode = promCode == null ? null : promCode.trim();
    }

    public String getPromTitle() {
        return promTitle;
    }

    public void setPromTitle(String promTitle) {
        this.promTitle = promTitle == null ? null : promTitle.trim();
    }

    public Byte getPromStatus() {
        return promStatus;
    }

    public void setPromStatus(Byte promStatus) {
        this.promStatus = promStatus;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle == null ? null : shopTitle.trim();
    }

    public Byte getPromType() {
        return promType;
    }

    public void setPromType(Byte promType) {
        this.promType = promType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup == null ? null : backup.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}