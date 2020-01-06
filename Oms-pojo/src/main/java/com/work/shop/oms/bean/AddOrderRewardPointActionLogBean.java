package com.work.shop.oms.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author caixiaol
 * @Date 2019/12/20 8:18 上午
 * @Version V1.0
 **/

@Data
public class AddOrderRewardPointActionLogBean {

    //订单编号
    private String orderSN;

    //操作人员编号
    private String actionUser;

    //订单状态1、未发货 2、已发货 3、确认收货 4、取消
    private int orderStatus;

    private String actionNote;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date logTime;

}
