package com.work.shop.oms.send.service.bean;

import java.io.Serializable;
import java.util.List;

public class OrderToEcRequest implements Serializable {

    private static final long serialVersionUID = 8775762644321969191L;

    private String line;

    /**
     * 订单的下单时间（unix时间戳,10位）
     */
    private int add_time;

    /**
     * 订单的付款时间（unix时间戳，10位）
     */
    private int pay_time;

    /**
     * 订单号
     */
    private String order_sn;

    /**
     * 店铺代码（暂定以EC系统中的店铺代码）
     */
    private String sd_code;

    /**
     * 订单状态
     * （0，未确认；1，已确认；2，已取消；3，无效；4，退货；5，锁定；6，解锁；7，完成；
     * 8，拒收；9，已合并；10，已拆分；）
     */
    private int order_status;

    /**
     * 付款状态（0，未付款；1，付款中；2，已付款；3，已结算）
     */
    private int pay_status;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 省
     */
    private String province_name;

    /**
     * 市
     */
    private String city_name;

    /**
     * 区
     */
    private String district_name;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 会员名
     */
    private String user_name;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * O2O门店代码（快递为骑手快递时必填）
     */
    private String pos_code;

    /**
     * 支付方式代码(weixin:微信支付)
     */
    private String pay_code;

    /**
     * 会员编号
     */
    private int vip_no;

    /**
     * 快递公司代码(PTKD，普通快递配送；QSKD,骑手快递)
     */
    private String shipping_code;

    /**
     * 运费默认0.00
     */
    private String shipping_fee;

    /**
     * 实际已付款总额
     */
    private String order_amount;

    /**
     * 支付流水号
     */
    private String pay_sn;

    /**
     * 订单级别除了商品折让之外的其他折让
     */
    private String other_discount_fee;

    /**
     * 客户留言
     */
    private String buy_msg;

    /**
     * 订单的付款时间（unix时间戳，10位）
     */
    private List<OrderGoodsToEc> items;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getPay_time() {
        return pay_time;
    }

    public void setPay_time(int pay_time) {
        this.pay_time = pay_time;
    }

    public String getSd_code() {
        return sd_code;
    }

    public void setSd_code(String sd_code) {
        this.sd_code = sd_code;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPos_code() {
        return pos_code;
    }

    public void setPos_code(String pos_code) {
        this.pos_code = pos_code;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public int getVip_no() {
        return vip_no;
    }

    public void setVip_no(int vip_no) {
        this.vip_no = vip_no;
    }

    public String getShipping_code() {
        return shipping_code;
    }

    public void setShipping_code(String shipping_code) {
        this.shipping_code = shipping_code;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getOther_discount_fee() {
        return other_discount_fee;
    }

    public void setOther_discount_fee(String other_discount_fee) {
        this.other_discount_fee = other_discount_fee;
    }

    public String getBuy_msg() {
        return buy_msg;
    }

    public void setBuy_msg(String buy_msg) {
        this.buy_msg = buy_msg;
    }

    public List<OrderGoodsToEc> getItems() {
        return items;
    }

    public void setItems(List<OrderGoodsToEc> items) {
        this.items = items;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }
}
