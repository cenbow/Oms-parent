package com.work.shop.oms.bean.bgapidb;

import java.io.Serializable;
import java.util.Date;

public class OrderDownloadPara implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String channelCode;

    private String shortName;

    private String shortText;

    private String shopCode;

    private Long lastupdatetime;

    private Integer defaulthouroffset;

    private Integer secpreoffset;

    private Integer secafteroffset;

    private Integer threadnumbers;

    private Integer tradenumperthread;

    private Integer tradenumperapi;

    private Integer useQueue;

    private Integer redisOrder;

    private Integer down2convert;

    private Date updatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText == null ? null : shortText.trim();
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public Long getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(Long lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public Integer getDefaulthouroffset() {
        return defaulthouroffset;
    }

    public void setDefaulthouroffset(Integer defaulthouroffset) {
        this.defaulthouroffset = defaulthouroffset;
    }

    public Integer getSecpreoffset() {
        return secpreoffset;
    }

    public void setSecpreoffset(Integer secpreoffset) {
        this.secpreoffset = secpreoffset;
    }

    public Integer getSecafteroffset() {
        return secafteroffset;
    }

    public void setSecafteroffset(Integer secafteroffset) {
        this.secafteroffset = secafteroffset;
    }

    public Integer getThreadnumbers() {
        return threadnumbers;
    }

    public void setThreadnumbers(Integer threadnumbers) {
        this.threadnumbers = threadnumbers;
    }

    public Integer getTradenumperthread() {
        return tradenumperthread;
    }

    public void setTradenumperthread(Integer tradenumperthread) {
        this.tradenumperthread = tradenumperthread;
    }

    public Integer getTradenumperapi() {
        return tradenumperapi;
    }

    public void setTradenumperapi(Integer tradenumperapi) {
        this.tradenumperapi = tradenumperapi;
    }

    public Integer getUseQueue() {
        return useQueue;
    }

    public void setUseQueue(Integer useQueue) {
        this.useQueue = useQueue;
    }

    public Integer getRedisOrder() {
        return redisOrder;
    }

    public void setRedisOrder(Integer redisOrder) {
        this.redisOrder = redisOrder;
    }

    public Integer getDown2convert() {
        return down2convert;
    }

    public void setDown2convert(Integer down2convert) {
        this.down2convert = down2convert;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}