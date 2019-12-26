package com.work.shop.oms.param.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/12 11:24 上午
 * @Version V1.0
 **/

@Data
public class ParamRewardPointChangeLog implements Serializable {

    private static final long serialVersionUID = -3414429637268090899L;

    //账户编号
    private String accountSN;

    //公司编号
    private String companySN;

    private int currentPage;
    private int pageSize;
    private int start;

}
