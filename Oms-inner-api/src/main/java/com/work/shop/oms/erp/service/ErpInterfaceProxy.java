package com.work.shop.oms.erp.service;

import java.util.Map;

import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.OrderCancelInfo;
import com.work.shop.oms.common.bean.UpdateOrderInfo;
import com.work.shop.oms.webservice.ErpResultBean;
import com.work.shop.oms.webservice.ErpWebserviceResultBean;

/**
 * ERP webservice 订单操作编辑服务
 * @author lemon
 *
 */
public interface ErpInterfaceProxy {
	/**
	 * 取消订单
	 * @param orderSn  订单ID
	 * @param cancelReasonCade 取消原因
	 * @param pJsonData  暂时为空
	 * @return
	 */
	public ErpResultBean UpdateCancelIdt(OrderCancelInfo orderCancelInfo);
	/**
	 * 修改收货地址
	 * @param ordersn  订单ID
	 * @param pJsonData 
	 * @return
	 */
	public ErpWebserviceResultBean UPdateReviceAddress(String ordersn,String pJsonData);
	
	/**
	 * 修改配送方式
	 * @param ordersn
	 * @param pJsonData
	 * @return
	 */
	public ErpWebserviceResultBean UpdateShippingMethod(String ordersn,Map<String,Object> pJsonData);
	
	 /**
	  * 商品变更
	  * @param ordersn
	  * @param pJsonData
	  * @return
	  */
	public ErpWebserviceResultBean UpdateProductByIdtDtl(String ordersn, Map pJsonData);
	
	
	 /**
	  * 商品变更
	  * @param updateOrderGoods
	  * @return
	  */
	public ErpResultBean UpdateProductByIdtDtl(UpdateOrderInfo updateOrderGoods);
	

    /**
     * 转问题单(optionType=1)/转正常单(optionType=2)
     * @param ordersn
     * @param optionType
     * @param pJsonData
     * @return
     */
	public ErpWebserviceResultBean UpdateIdtToQnOrToR(String ordersn, int optionType, Map<String,Object> pJsonData);

    /**
     * 挂起(optionType=1)/解挂(optionType=2)
     * @param ordersn
     * @param optionType
     * @param pJsonData
     * @return
     */
	public ErpWebserviceResultBean UpdateIdtToHangUpOrToHangR(String ordersn, int optionType, Map<String,Object> pJsonData);
	/**
	 * 总费用信息修改处理
	 * @param ordersn
	 * @param pJsonData
	 * @return
	 */
	ErpWebserviceResultBean UpdateOrderAmount(String ordersn, Map pJsonData);
	/**
	 * 订单确认/未确认
	 * @param ordersn
	 * @param pJsonData
	 * @return
	 */
	ErpWebserviceResultBean confirmOrder(String ordersn,int optionType, Map pJsonData);
	/**
	 * 更新收货地址(Bean)
	 * @param orderSn
	 * @param consignInfo
	 * @return
	 * @throws Exception
	 */
	ErpResultBean changeConsigneeInfo(String orderSn, ConsigneeModifyInfo consignInfo) throws Exception;


}
