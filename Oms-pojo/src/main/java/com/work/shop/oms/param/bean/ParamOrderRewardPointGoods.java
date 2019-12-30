package com.work.shop.oms.param.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.work.shop.oms.bean.OrderRewardPointGoodsDetailBean;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/12 11:24 上午
 * @Version V1.0
 **/

@Data
public class ParamOrderRewardPointGoods implements Serializable {

    private static final long serialVersionUID = 7644152663765882851L;

    //订单编号
    private String orderSN;
    //买家编号
    private String buyerSN;
    //取消人编号
    private String cancelSN;
    //所需积分总额
    private int totalPoint;
    //收货人姓名
    private String receiverName;
    //收货人电话
    private String receiverTel;
    //收货人地址
    private String receiverAddress;

    //订单状态
    //1、待确认 2、已发货 3、确认收货 4、取消
    private int orderStatus;

    //订单开始时间
    @JSONField(format = "yyyy-MM-dd")
    private Date beginOrderTime;

    //订单结束时间
    @JSONField(format = "yyyy-MM-dd")
    private Date endOrderTime;

    //订单取消时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;

    //排序条件：  按订单状态:order_status、按上架时间:create_time
    private String order;

    //正序:asc、倒序:desc
    private String sort;
    private int currentPage;
    private int pageSize;
    private int start;

    private List<OrderRewardPointGoodsDetailBean> detailBeanList;
}
