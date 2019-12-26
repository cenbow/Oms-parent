package com.work.shop.oms.bean;

import lombok.Data;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/24 2:32 下午
 * @Version V1.0
 **/

@Data
public class RewardPointRatioBean {

    private int id;

    //金额，多少块积一分
    private int amount;

    private int status;
}
