package com.work.shop.oms.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author 蔡啸
 * @Date 2019/12/12 9:03 上午
 * @Version V1.0
 **/

@Data
public class OrderRewardPointGoodsDetailBean implements Serializable {

    private static final long serialVersionUID = -5886563754234009645L;

    private int id;

    //订单编号
    private String orderSN;

    //积分商品编号
    private String goodsSN;

    //积分商品名称
    private String goodsName;

    //积分商品品牌
    private String goodsBrand;

    //积分商品图片
    private String pictureURL;

    //兑换数量
    private int saleCount;

    //所需积分
    private int needPoint;

    //快递单号
    private String expressSN;

    //快递公司名称
    private String expressCompany;

    //发货时间
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date expressTime;

}
