package com.work.shop.oms.orderReturn.service;

import java.util.List;
import java.util.Map;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.vo.OrderRefundListVO;
import com.work.shop.oms.vo.ReturnOrderVO;

/**
 * 
 * @Description Oms-OmsManager查询退单接口
 * @author Cage
 * 
 */
public interface OrderReturnSearchService {
    
    /**
     * 得到退单信息
     * @param returnSn
     * @return
     */
    OrderReturn getOrderReturnByReturnSn(String returnSn);
    
    /**
     * 退单详情页面加载
     * @param returnSn
     * @param relOrderSn
     * @param returnType
     * @return
     */
    ReturnOrderVO getOrderReturnDetailVO(String returnSn,String relOrderSn,String returnType);
    
    /**
     * 加载退货仓库信息
     * @param orderSn
     * @return
     */
    public List<ChannelShop> getOrderReturnGoodsDepotList(String orderSn);
    
    /**
     * 退款单列表(分页)
     * 
     * @param model
     * @param helper
     * @return Paging
     */
    Paging getOrderRefundPage(OrderRefundListVO model, PageHelper helper) throws Exception;

    /**
     * 删除退单详情页图片信息中的指定图片
     * @param userName
     * @param returnSn
     * @param url
     * @return
     */
    public Map delReturnImg(String userName,String returnSn,String url);
}
