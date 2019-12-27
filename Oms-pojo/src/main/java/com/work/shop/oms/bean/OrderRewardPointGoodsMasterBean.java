package com.work.shop.oms.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/12 9:03 上午
 * @Version V1.0
 **/

@Data
public class OrderRewardPointGoodsMasterBean implements Serializable {

    private static final long serialVersionUID = -4707565654225172935L;

    private int id;

    //订单编号
    private String orderSN;

    //买家编号
    private String buyerSN;

    //取消人编号
    private String cancelSN;

    //所需总积分
    private int totalPoint;

    //订单状态
    //1、待确认 2、已发货 3、确认收货 4、取消
    private int orderStatus;

    //收货人名称
    private String receiverName;

    //收货人电话
    private String receiverTel;

    //收货人地址
    private String receiverAddress;

    //发货时间
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date expressTime;

    //创建时间
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    //取消时间
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date cancelTime;

    //备注
    private String comment;

}
