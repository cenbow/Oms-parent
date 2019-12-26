package com.work.shop.oms.controller.pojo;

import lombok.Data;

/**
 * @Description
 * @Author caixiao
 * @Date 2019/12/6 2:14 下午
 * @Version V1.0
 **/
@Data
public class ChangeStockAndSaleVolumeBean {

    //商品编号
    private String goodsSN;

    //修改库存数量
    private int changeStock;

    //修改销量数量
    private int changeSalesVolume;
}
