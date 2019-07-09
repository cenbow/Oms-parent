package com.work.shop.oms.common.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单接口系统基础信息
 * @author
 *
 */
public class SystemInfo implements Serializable {
	
	private static final long serialVersionUID = -1815662194084972034L;
	/** 订单创建时间 */
	private Date createTime;
	/** 本机IP */
	private String localIp;
	/** 远程调用者IP */
	private String remoteIp;
	/** 系统时间 */
	private Date systemTime;
	
	public SystemInfo() {}
	
	public SystemInfo(Date createTime, String localIp, String remoteIp, Date systemTime) {
		super();
		this.createTime = createTime;
		this.localIp = localIp;
		this.remoteIp = remoteIp;
		this.systemTime = systemTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public Date getSystemTime() {
		return systemTime;
	}
	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
	}
}
