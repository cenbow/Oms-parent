package com.work.shop.oms.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/20 8:18 上午
 * @Version V1.0
 **/

@Data
public class RewardPointChangeLogBean implements Serializable {

    private static final long serialVersionUID = -5242254361610014894L;

    private int id;

    private String accountSN;

    private String companySN;

    private String orderSN;

    //描述
    private String description;

    private int changePoint;

    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
}
