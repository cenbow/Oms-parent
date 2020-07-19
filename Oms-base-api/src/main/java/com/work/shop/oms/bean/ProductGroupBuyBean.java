package com.work.shop.oms.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

@Data
public class ProductGroupBuyBean {

    /**
     *id
     */
    private Integer id;

    /**
     *团购编码（yyyyMMdd+流水号）
     */
    private String groupBuyCode;

    /**
     *活动名称
     */
    private String groupBuyName;

    /**
     *开始时间
     */
    private Date beginTime;

    /**
     *结束时间
     */
    private Date endTime;

    /**
     *站点编码
     */
    private String siteCode;

    /**
     *店铺编码
     */
    private String channelCode;

    /**
     *团购商品类型（1品牌，2商品）
     */
    private byte groupGoodsType;

    /**
     *品牌编码
     */
    private String brandCode;

    /**
     *限购规则（0不限购、1限购）
     */
    private Short limitType;

    /**
     *限购类型（1件数、2重量）
     */
    private Short goodsType;

    /**
     *起订量(件数、总量吨)
     */
    private BigDecimal limitMin;

    /**
     *最大量(件数、重量吨)
     */
    private BigDecimal limitMax;

    /**
     *付款方式：0全额、1预付款
     */
    private Short payType;

    /**
     *付款比例
     */
    private BigDecimal payRate;

    /**
     *价格规则[{“10000”:"9.7"},{“20000”:"9.2"}]
     */
    private String priceRule;

    /**
     *状态（1团购中、2团购成功、3团购失败）
     */
    private Short groupBuyStatus;

    /**
     *订单数
     */
    private Integer orderTotal;

    /**
     *订单金额
     */
    private BigDecimal orderMoney;

    /**
     *成团人数
     */
    private Integer orderUser;

    /**
     *团购已购总件数/重量
     */
    private BigDecimal orderAmount;

    /**
     *渠道类型（1为pc，2为H5）
     */
    private Short channelType;

    /**
     *图标url
     */
    private String logoUrl;

    /**
     *团状态（-1为删除，0为禁用，1为正常，2为待批）
     */
    private Short status;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *创建人
     */
    private String createUser;

    /**
     *最后修改时间
     */
    private Date lastUpdateTime;

    /**
     *最后修改人
     */
    private String lastUpdateUser;

    /**
     * 订单编码
     */
    private String masterOrderSn;



}
