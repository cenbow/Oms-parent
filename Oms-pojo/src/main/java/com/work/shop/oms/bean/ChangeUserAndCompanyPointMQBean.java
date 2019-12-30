package com.work.shop.oms.bean;

import lombok.Data;

/**
 * @Description
 * @Author caixiaol
 * @Date 2019/12/20 8:18 上午
 * @Version V1.0
 **/

@Data
public class ChangeUserAndCompanyPointMQBean {

    private String accountSN;

    private String companySN;

    private int changePoint;

}
