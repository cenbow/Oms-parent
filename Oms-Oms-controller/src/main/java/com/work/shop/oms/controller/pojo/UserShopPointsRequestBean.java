package com.work.shop.oms.controller.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户积分处理请求
 *
 * @author wk
 * @date 2019/12/19
 */
@Data
public class UserShopPointsRequestBean implements Serializable {

    private static final long serialVersionUID = 5848801464658227676L;

    /**
     * 账户id
     */
    private String accountSN;

    /**
     * 公司id
     */
    private String companySN;

    /**
     * 变动积分
     */
    private int changePoint;

    /**
     * 变动类型 1、增加 2、减少
     */
    private int type;

    /**
     * 积分账户类型 1、个人 2、公司
     */
    private int pointUserType;

    /**
     * 交易号
     */
    private String orderNo;

    /**
     * 操作用户
     */
    private String actionUser;
}
