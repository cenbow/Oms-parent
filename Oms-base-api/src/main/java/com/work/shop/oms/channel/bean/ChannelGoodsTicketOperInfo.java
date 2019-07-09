package com.work.shop.oms.channel.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 调整单信息操作
 * @author QuYachu
 *
 */
public class ChannelGoodsTicketOperInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	
	/**
	 * 执行或者移除中的时候，可以多个id，以逗号分隔
	 */
	private String ids;
	
	/**
	 * 调整单编号
	 */
	private String ticketCode;
	
	/**
	 * 渠道编码
	 */
	private String channelCode;
	
	/**
	 * 店铺编码
	 */
	private String shopCode;
	
	/**
	 * 经营店铺名称
	 */
	private String shopTitle;
	
	/**
	 * 单据生成时间
	 */
	private Date addTime;
	
	/**
	 * 单据状态
	 * ''0''未审核，''1''已审核，’2‘：已移除 ， 3‘已经执行’，4‘部分执行错误’，5‘执行错误’
	 */
	private String ticketStatus;
	
	/**
	 * 操作用户
	 */
	private String operUser;
	
	/**
	 * 执行结果
	 */
	private String note;
	
	/**
	 * 票据类型，0，修改价格，1,上下架维护，2，商品信息维护，3.卖点管理，4商品名称调整，5.商品添加
	 */
	private Byte ticketType;
	
	/**
	 * 执行时间
	 */
	private Date excetTime;
	
	/**
	 * 是否定时执行，0否1是
	 * 1定时执行
	 * 0立即执行
	 */
	private String isTiming;
	
	/**
	 * 调整单明细
	 */
	private List<TicketInfoOperInfo> ticketList;

	public int getId() {
		return id;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public String getShopTitle() {
		return shopTitle;
	}

	public Date getAddTime() {
		return addTime;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public String getOperUser() {
		return operUser;
	}

	public String getNote() {
		return note;
	}

	public Byte getTicketType() {
		return ticketType;
	}

	public String getIsTiming() {
		return isTiming;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setTicketType(Byte ticketType) {
		this.ticketType = ticketType;
	}

	public void setIsTiming(String isTiming) {
		this.isTiming = isTiming;
	}

	public List<TicketInfoOperInfo> getTicketList() {
		return ticketList;
	}

	public void setTicketList(List<TicketInfoOperInfo> ticketList) {
		this.ticketList = ticketList;
	}

	public Date getExcetTime() {
		return excetTime;
	}

	public void setExcetTime(Date excetTime) {
		this.excetTime = excetTime;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}
