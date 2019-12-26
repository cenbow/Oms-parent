package com.work.shop.oms.controller.pojo;

import lombok.Data;

/**
 * 用户商城积分信息
 *
 * @author wk
 * @Date 2019/12/20
 */

@Data
public class UserShopPointBean {
    /**
     * 账户id
     */
    private String accountId;

    /**
     * 积分
     */
    private int point;
}
