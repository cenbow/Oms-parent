package com.work.shop.oms.api.bean;

import com.work.shop.oms.bean.GoodsReturnChangeAction;
import com.work.shop.oms.common.bean.GoodsReturnChangeDetailVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 退换货申请单详情
 * @author YeQingchao
 */
public class ReturnChangeDetailInfo implements Serializable {

    private static final long serialVersionUID = 8454917455340083746L;

    private Integer id;

    /**
     * 申请单号
     */
    private String returnchangen;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 退换类型，1：退货；2：换货；3维修
     */
    private Integer returnType;

    /**
     * 退换原因，1：商品质量不过关；2：商品在配送中损坏；3：商品与描述不符；4：尚未收到商品；5：其他（请具体说明）
     */
    private Integer reason;

    /**
     * 退还说明
     */
    private String explain;

    /**
     * 退换数量
     */
    private Integer returnSum;

    /**
     * 联系人名
     */
    private String contactName;

    /**
     * 联系人手机
     */
    private String contactMobile;

    /**
     * 申请时间
     */
    private String returnChangeTime;

    /**
     * 申请退换商品总金额
     */
    private Double returnChangeTotalFee;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 申请状态：0：已取消；1：待沟通；2：已完成；3：待处理；4：已驳回
     */
    private Integer status;

    private String returnTypeStr;

    private String reasonStr;

    private String statusStr;

    /**
     * 申请退换商品信息
     */
    private List<GoodsReturnChangeDetailVo> returnChangeGoodsList;

    /**
     * 申请单日志
     */
    private List<GoodsReturnChangeAction> actions;

    public String getReturnchangen() {
        return returnchangen;
    }

    public void setReturnchangen(String returnchangen) {
        this.returnchangen = returnchangen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public Integer getReturnSum() {
        return returnSum;
    }

    public void setReturnSum(Integer returnSum) {
        this.returnSum = returnSum;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getReturnChangeTime() {
        return returnChangeTime;
    }

    public void setReturnChangeTime(String returnChangeTime) {
        this.returnChangeTime = returnChangeTime;
    }

    public Double getReturnChangeTotalFee() {
        return returnChangeTotalFee;
    }

    public void setReturnChangeTotalFee(Double returnChangeTotalFee) {
        this.returnChangeTotalFee = returnChangeTotalFee;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<GoodsReturnChangeDetailVo> getReturnChangeGoodsList() {
        return returnChangeGoodsList;
    }

    public void setReturnChangeGoodsList(List<GoodsReturnChangeDetailVo> returnChangeGoodsList) {
        this.returnChangeGoodsList = returnChangeGoodsList;
    }

    public String getReturnTypeStr() {
        if (getReturnType() == null) {
            return "";
        }
        switch (getReturnType()) {
            case 1: return "退货";
            case 2: return "换货";
            case 3: return "维修";
            case 4: return "退款";
            case 5: return "额外退款单";
            default:return "";
        }
    }
    public String getReasonStr() {
        if (getReason() == null) {
            return "";
        }
        switch (getReason()) {
            case 1: return "商品质量不过关";
            case 2: return "商品在配送中损坏";
            case 3: return "商品与描述不符";
            case 4: return "尚未收到商品";
            case 5: return "其他";
            default:return "";
        }
    }

    public String getStatusStr() {
        if (getStatus() == null) {
            return "";
        }
        switch (getStatus()) {
            case 0: return "已取消";
            case 1: return "待沟通";
            case 2: return "已完成";
            case 3: return "待处理";
            case 4: return "已驳回";
            default:return "";
        }
    }

    public void setReturnTypeStr(String returnTypeStr) {
        this.returnTypeStr = returnTypeStr;
    }

    public void setReasonStr(String reasonStr) {
        this.reasonStr = reasonStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public List<GoodsReturnChangeAction> getActions() {
        return actions;
    }

    public void setActions(List<GoodsReturnChangeAction> actions) {
        this.actions = actions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
