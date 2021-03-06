package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.Date;

public class GoodsReturnChangeAction implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2875523944852876744L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change_action.action_id
     *
     * @mbggenerated
     */
    private Integer actionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change_action.order_sn
     *
     * @mbggenerated
     */
    private String orderSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change_action.action_user
     *
     * @mbggenerated
     */
    private String actionUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change_action.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change_action.log_time
     *
     * @mbggenerated
     */
    private Date logTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change_action.returnchange_sn
     *
     * @mbggenerated
     */
    private String returnchangeSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column goods_return_change_action.action_note
     *
     * @mbggenerated
     */
    private String actionNote;

    /**
     * 日志类型：0为订单操作，1为沟通
     */
    private Byte logType = 0;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change_action.action_id
     *
     * @return the value of goods_return_change_action.action_id
     *
     * @mbggenerated
     */
    public Integer getActionId() {
        return actionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change_action.action_id
     *
     * @param actionId the value for goods_return_change_action.action_id
     *
     * @mbggenerated
     */
    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change_action.order_sn
     *
     * @return the value of goods_return_change_action.order_sn
     *
     * @mbggenerated
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change_action.order_sn
     *
     * @param orderSn the value for goods_return_change_action.order_sn
     *
     * @mbggenerated
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change_action.action_user
     *
     * @return the value of goods_return_change_action.action_user
     *
     * @mbggenerated
     */
    public String getActionUser() {
        return actionUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change_action.action_user
     *
     * @param actionUser the value for goods_return_change_action.action_user
     *
     * @mbggenerated
     */
    public void setActionUser(String actionUser) {
        this.actionUser = actionUser == null ? null : actionUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change_action.status
     *
     * @return the value of goods_return_change_action.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change_action.status
     *
     * @param status the value for goods_return_change_action.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change_action.log_time
     *
     * @return the value of goods_return_change_action.log_time
     *
     * @mbggenerated
     */
    public Date getLogTime() {
        return logTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change_action.log_time
     *
     * @param logTime the value for goods_return_change_action.log_time
     *
     * @mbggenerated
     */
    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change_action.returnchange_sn
     *
     * @return the value of goods_return_change_action.returnchange_sn
     *
     * @mbggenerated
     */
    public String getReturnchangeSn() {
        return returnchangeSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change_action.returnchange_sn
     *
     * @param returnchangeSn the value for goods_return_change_action.returnchange_sn
     *
     * @mbggenerated
     */
    public void setReturnchangeSn(String returnchangeSn) {
        this.returnchangeSn = returnchangeSn == null ? null : returnchangeSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column goods_return_change_action.action_note
     *
     * @return the value of goods_return_change_action.action_note
     *
     * @mbggenerated
     */
    public String getActionNote() {
        return actionNote;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column goods_return_change_action.action_note
     *
     * @param actionNote the value for goods_return_change_action.action_note
     *
     * @mbggenerated
     */
    public void setActionNote(String actionNote) {
        this.actionNote = actionNote == null ? null : actionNote.trim();
    }

    /**
     * 0：已取消；1：待沟通；2：已完成；3：待处理；4：已驳回
     */
    private String statusStr;

    public String getStatusStr() {
        if (status == 0) {
            return "已取消";
        } else if (status == 1) {
            return "待沟通";
        } else if (status == 2) {
            return "已完成";
        } else if (status == 3) {
            return "待处理";
        } else if (status == 4) {
            return "已驳回";
        }
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public Byte getLogType() {
        return logType;
    }

    public void setLogType(Byte logType) {
        this.logType = logType;
    }
}
