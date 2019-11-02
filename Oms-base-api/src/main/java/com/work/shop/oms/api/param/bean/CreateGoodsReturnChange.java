package com.work.shop.oms.api.param.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建申请退换单信息
 * @author Yeqingchao
 */
public class CreateGoodsReturnChange implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作人
     */
    private String actionUser;

    /**
     * 操作备注
     */
    private String actionNote;

    /**
     * 站点
     */
    private String siteCode;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 手机号
     */
    private String contactMobile;

    /**
     * 联系人名
     */
    private String contactName;

    /**
     * 退换类型，1：退货；2：换货
     */
    private Integer returnType;

    /**
     * 退换原因
     * 1：商品质量不过关；2：商品在配送中损坏；3：商品与描述不符；4：尚未收到商品；5：其他（请具体说明）
     */
    private Integer reason;

    /**
     * 申请原因
     */
    private String reasonStr;

    /**
     * 退换说明
     */
    private String explain;

    /**
     * 申请退换商品
     */
    private List<GoodsReturnChangeDetailInfo> goodsList;

    /**
     * 退换单申请单号
     */
    private String returnChangeSn;

    /**
     * 退单商品总数量
     */
    private Integer returnSum;

    /**
     * 申请退款总金额
     */
    private Double changeReturnTotalFee;

    /**
     * 申请退款商品总金额
     */
    private Double changeReturnGoodsTotalFee;

    /**
     * 运费
     */
    private Double shipFee;

    /**
     * 退红包金额
     */
    private Double returnBouns;

    /**
     * 是否整单退，0为否，1为是,未发货
     */
    private int isAll = 0;

    /**
     * 交货单号
     */
    private String distributeSn;

    public String getActionUser() {
        return actionUser;
    }

    public void setActionUser(String actionUser) {
        this.actionUser = actionUser;
    }

    public String getActionNote() {
        return actionNote;
    }

    public void setActionNote(String actionNote) {
        this.actionNote = actionNote;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    public List<GoodsReturnChangeDetailInfo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsReturnChangeDetailInfo> goodsList) {
        this.goodsList = goodsList;
    }

    public String getReturnChangeSn() {
        return returnChangeSn;
    }

    public void setReturnChangeSn(String returnChangeSn) {
        this.returnChangeSn = returnChangeSn;
    }

    public Integer getReturnSum() {
        return returnSum;
    }

    public void setReturnSum(Integer returnSum) {
        this.returnSum = returnSum;
    }

    public Double getChangeReturnTotalFee() {
        return changeReturnTotalFee;
    }

    public void setChangeReturnTotalFee(Double changeReturnTotalFee) {
        this.changeReturnTotalFee = changeReturnTotalFee;
    }

    public Double getChangeReturnGoodsTotalFee() {
        return changeReturnGoodsTotalFee;
    }

    public void setChangeReturnGoodsTotalFee(Double changeReturnGoodsTotalFee) {
        this.changeReturnGoodsTotalFee = changeReturnGoodsTotalFee;
    }

    public Double getShipFee() {
        return shipFee;
    }

    public void setShipFee(Double shipFee) {
        this.shipFee = shipFee;
    }

    public Double getReturnBouns() {
        return returnBouns;
    }

    public void setReturnBouns(Double returnBouns) {
        this.returnBouns = returnBouns;
    }

    public int getIsAll() {
        return isAll;
    }

    public void setIsAll(int isAll) {
        this.isAll = isAll;
    }

    public String getReasonStr() {
        //1：商品质量不过关；2：商品在配送中损坏；3：商品与描述不符；4：尚未收到商品；5：其他（请具体说明）
        if (reason == null) {
            return "";
        } else if (reason == 1) {
            return "商品质量不过关";
        } else if (reason == 2) {
            return "商品在配送中损坏";
        } else if (reason == 3) {
            return "商品与描述不符";
        } else if (reason == 4) {
            return "尚未收到商品";
        } else if (reason == 5) {
            return "其他（请具体说明）";
        }
        return "";
    }

    public void setReasonStr(String reasonStr) {
        this.reasonStr = reasonStr;
    }

    public String getDistributeSn() {
        return distributeSn;
    }

    public void setDistributeSn(String distributeSn) {
        this.distributeSn = distributeSn;
    }
}
