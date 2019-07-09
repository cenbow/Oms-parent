package com.work.shop.oms.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.MasterOrderAddressInfo;
import com.work.shop.oms.bean.ShortageQuestion;
import com.work.shop.oms.common.bean.Common;
import com.work.shop.oms.common.bean.OrderQuestionSearchVO;
import com.work.shop.oms.common.utils.MasterOrderInfoVO;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.vo.AdminUser;
import com.work.shop.oms.vo.DeliveryInfoParam;

public interface OrderInfoService {
	
	public MasterOrderInfoVO getOrderDetail(AdminUser adminUser, String masterOrderSn, Integer isHistory);
	
	public Map getMasterOrderAddressInfo(AdminUser adminUser, String masterOrderSn, Integer isHistory);
	
	public Map doSaveAddrEdit(AdminUser adminUser,MasterOrderAddressInfo formParam,String oldTel,String oldMobile);
	
	public Map getMasterOrderInfoExtend(AdminUser adminUser, String masterOrderSn, Integer isHistory);
	
	public Map doSaveOtherEdit(AdminUser adminUser, String masterOrderSn,String invType,String invPayee,String invContent,String howOos,String postscript,String toBuyer);
	
	public Map queryExpress(String masterOrderSn,String depotCode,String invoiceNo);
	
	public Map getAvaliableDelivery(DeliveryInfoParam param);
	
	public Map doSaveDeliveryChange(AdminUser adminUser,String shippingCode,String orderSn,String depotCode);
	
	public Map getMasterOrderPayInfo(String masterOrderSn);
	
	public Map doSaveShippingFee(AdminUser adminUser,String masterOrderSn,String shippingTotalFee);
	
	public Map getCouponInfo(String cardNo,String couponType);
	
	public Map getStock(String customCode,String channelCode);
	
	/**
     * 根据选择不同级别订单来源获取订单来源列表
     * @param channelType
     * @param channelCode
     * @param shopCodes
     * @return
     */
    public List<String> getOrderForms(String channelType, String channelCode, String[] shopCodes);
    

    public Map<String, Object> getExchangeOrderDetail(AdminUser adminUser, String orderSn, Integer isHistory);
    
    
    
	/**
	 * 订单列表(分页)
	 * 
	 * @param model
	 * @param helper
	 * @return Paging
	 */
	Paging getOrderInfoPage(Common model, PageHelper helper) throws Exception;
	
	
	
	Common InputIsDerivedOrderListByOrderSnOrOrderOutSn(InputStream is, StringBuffer sb, int logType) throws Exception;
	
	
	/**
	 * 获取问题订单列表(分页)
	 * 
	 * @param model
	 * @param helper
	 * @return Paging
	 */
	Paging getorderQuestionSearchResultVOsPage(OrderQuestionSearchVO orderQuestionSearchVO) throws Exception;
	
	
	/**
	 * 获取物流问题单列表
	 * 
	 * @param orderSn
	 * @param id
	 * @return 
	 */
	List<ShortageQuestion> getShortageQuestionList(String orderSn,Integer id);


	/**
	 * 初始化订单详情页发送短信的页面数据
	 * @param masterOrderSn
	 * @param channelCode
	 * @return
	 */
    public Map initSendMessage(String masterOrderSn,String channelCode);
    
    /**
     * 订单详情页发送短信
     * @param adminUser
     * @param encodedMobile
     * @param shopCode
     * @param masterOrderSn
     * @param channelCode
     * @param message
     * @return
     */
    public Map doSendMessage(AdminUser adminUser,String encodedMobile,String shopCode,String masterOrderSn,String channelCode,String message);
	
    /**
     * 工具页发送短信
     * @param adminUser
     * @param mobile
     * @param sendType
     * @param message
     * @return
     */
	public Map toolsDoSendMessage(AdminUser adminUser,String mobile,String sendType,String message);
}
