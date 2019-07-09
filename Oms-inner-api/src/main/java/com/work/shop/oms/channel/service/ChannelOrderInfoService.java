package com.work.shop.oms.channel.service;

import com.work.shop.oms.api.bean.PageListParam;
import com.work.shop.oms.common.bean.ApiReturnData;

/**
 * 渠道订单信息接口
 * @author QuYachu
 */
public interface ChannelOrderInfoService {

    /**
     * 平台前台查询用户订单列表
     * @param searchParam
     * @return
     */
    public ApiReturnData orderPageList(PageListParam searchParam);

}
