package com.work.shop.pcs.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 积分商品bean
 * @Author caixiao
 * @Date 2019/12/6 11:06 上午
 * @Version V1.0
 **/

@Data
public class ProductRewardPointGoodsBean implements Serializable {

    private static final long serialVersionUID = -7695446925793885172L;

    private int id;

    //商品编号
    private String goodsSN;

    //商品名称
    private String goodsName;

    //所需积分
    private int needPoint;

    //品牌
    private String goodsBrand;

    //积分商品单位
    private String goodsUnit;

    //积分商品分类编号
    private int goodsClassCode;

    //积分商品描述
    private String goodsDescription;

    //积分商品品种：1、普通商品 2、兑换卷
    private int goodsType;

    //图片连接
    private String pictureURL;

    //库存
    private int goodsStock;

    //展示价格
    private double showPrice;

    //兑换量
    private int saleCount;

    //商品状态
    //1、上架 2、下架 3、删除
    private int goodsStatus;

    //开始日期
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date beginning;

    //截止日期
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date validity;

}
