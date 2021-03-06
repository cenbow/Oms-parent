package com.work.shop.oms.ship.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.work.shop.oms.api.express.feign.OrderExpressService;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.bean.bgchanneldb.ChannelShopExample;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.common.utils.CachedBeanCopier;
import com.work.shop.oms.config.service.SystemShippingService;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.mq.ws.DataUtil;
import com.work.shop.oms.mq.ws.OrderUtil;
import com.work.shop.oms.order.service.DistributeActionService;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.order.service.MasterOrderPayService;
import com.work.shop.oms.orderop.service.JmsSendQueueService;
import com.work.shop.oms.orderop.service.OrderCancelService;
import com.work.shop.oms.orderop.service.OrderConfirmService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.service.RewardPointRatioService;
import com.work.shop.oms.ship.bean.DistConfirmOwner;
import com.work.shop.oms.ship.bean.DistOrderPackageItems;
import com.work.shop.oms.ship.bean.DistOrderPackages;
import com.work.shop.oms.ship.request.DistOrderShipRequest;
import com.work.shop.oms.ship.response.DistOrderShipResponse;
import com.work.shop.oms.ship.service.DistributeShipService;
import com.work.shop.oms.users.UserInfoService;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.utils.*;
import com.work.shop.sms.bean.Message;
import com.work.shop.sms.bean.State;
import com.work.shop.sms.bean.User;
import com.work.shop.sms.send.api.SMSService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.math.BigDecimal;
import java.util.*;

/**
 * 交货单配送服务
 *
 * @author QuYachu
 */
@Service("distributeShipService")
public class DistributeShipServiceImpl implements DistributeShipService {

    Logger logger = Logger.getLogger(DistributeShipServiceImpl.class);

    @Resource
    DistributeActionService distributeActionService;

    @Resource
    private SystemMsgTemplateMapper systemMsgTemplateMapper;

    @Resource
    OrderDepotShipMapper orderDepotShipMapper;

    @Resource
    ChannelShopMapper channelShopMapper;

    //@Resource
    SMSService sMSService;

    @Resource
    MasterOrderGoodsMapper masterOrderGoodsMapper;

    @Resource
    private OrderExpressService orderExpressService;

    @Resource
    private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;

    @Resource
    private WkSfGdnMapper wkSfGdnMapper;

    @Resource
    private WkSfGdnInfoMapper wkSfGdnInfoMapper;

    @Resource
    private WkSfGdnTmpMapper wkSfGdnTmpMapper;

    @Resource
    private OrderDistributeMapper orderDistributeMapper;

    @Resource
    private SystemShippingService systemShippingService;

    @Resource(name = "noticeShipProducerJmsTemplate")
    private JmsTemplate noticeShipProducerJmsTemplate;

    @Resource
    private MasterOrderInfoMapper masterOrderInfoMapper;

    @Resource
    private MasterOrderPayService masterOrderPayService;

    @Resource(name = "orderCancelService")
    private OrderCancelService orderCancelService;

    @Resource
    private OrderConfirmService orderConfirmService;

    @Resource
    private MasterOrderActionService masterOrderActionService;

    @Resource(name = "orderDeliveryJmsTemplate")
    private JmsTemplate orderDeliveryJmsTemplate;

    @Resource
    private SystemShippingMapper systemShippingMapper;

    @Resource
    private JmsSendQueueService jmsSendQueueService;

    @Resource
    private MasterOrderInfoService masterOrderInfoService;

    @Autowired
    private RewardPointRatioService rewardPointRatioService;

    @Resource
    private RedisClient redisClient;

    @Resource(name = "changeUserAndCompanyPointJmsTemplate")
    private JmsTemplate changeUserAndCompanyPointJmsTemplate;

    @Resource(name = "addRewardPointChangeLogJmsTemplate")
    private JmsTemplate addRewardPointChangeLogJmsTemplate;

    @Resource(name = "uploadSaleSettlementJmsTemplate")
    private JmsTemplate uploadSaleSettlementJmsTemplate;

    private ThreadLocal<Boolean> isSend = new ThreadLocal<Boolean>();

    // 操作类型 0：不变 1：新增
    private static final int oper_type_no = 0;

    private static final int oper_type_add = 1;

    @Override
    public String acceptShipData(String data) {
        logger.info("发货工具data=" + data);
        // 将发货信息列表拆分为按订单
        if (StringUtil.isTrimEmpty(data)) {
            logger.error("订单发货信息为空！");
            return "0";
        }
        try {
            Gson gson = new Gson();
            List<OrderToShippedProviderBeanERP> oe = gson.fromJson(data, new TypeToken<List<OrderToShippedProviderBeanERP>>() {
            }.getType());
            List<DistributeShipBean> obj = DataUtil.ToOrderToShippedProviderBean(oe);
            OrderUtil ou = new OrderUtil();
            Map<String, List<DistributeShipBean>> dataMap = ou.mergeDataShipByOrderSN(obj);
            Set<String> orderSns = dataMap.keySet();
            for (String orderSn : orderSns) {
                try {
                    logger.debug("收到发货订单请求" + orderSn + ",数量:" + orderSns.size());
                    List<DistributeShipBean> shipProviderList = dataMap.get(orderSn);
                    if (shipProviderList == null || shipProviderList.size() < 1) {
                        logger.error("订单号" + orderSn + "无发货单信息");
                    } else {
                        OrderToShippedProviderBeanParam providerBeanParam = new OrderToShippedProviderBeanParam();
                        providerBeanParam.setOrderSn(orderSn);
                        providerBeanParam.setShipBeans(shipProviderList);
                        final String msg = JSON.toJSONString(providerBeanParam);
                        noticeShipProducerJmsTemplate.send(new TextMessageCreator(msg));
                    }
                } catch (Exception e) {
                    logger.error("订单[" + orderSn + "]接收处理发货订单请求异常：" + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            logger.error("接受发货数据异常" + e.getMessage(), e);
        }
        return "1";
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ReturnInfo acceptShip(
            List<OrderToShippedProviderBeanParam> providerBeanParams) {
        logger.info("接受物流发货数据providerBeanParams=" + JSON.toJSONString(providerBeanParams));
        ReturnInfo info = new ReturnInfo(Constant.OS_NO);
        if (StringUtil.isListNull(providerBeanParams)) {
            info.setMessage("接受物流发货数据为空！");
            return info;
        }
        for (OrderToShippedProviderBeanParam param : providerBeanParams) {
            try {
                final String msg = JSON.toJSONString(param);
                noticeShipProducerJmsTemplate.send(new TextMessageCreator(msg));
            } catch (Exception e) {
                logger.error("订单[" + param.getOrderSn() + "]接受物流发货数据异常" + e.getMessage(), e);
            }
        }
        info.setIsOk(Constant.OS_YES);
        info.setMessage("接受物流发货数据成功");
        return info;
    }

    /**
     * 供应商发货前发货确认
     *
     * @param request
     * @return DistOrderShipResponse
     */
    @Override
    public DistOrderShipResponse shippedConfirm(DistOrderShipRequest request) {
        DistOrderShipResponse response = new DistOrderShipResponse();
        response.setSuccess(false);
        if (request == null) {
            response.setMessage("参数不能为空");
            return response;
        }
        String orderSn = request.getOrderSn();
        if (StringUtil.isTrimEmpty(orderSn)) {
            response.setMessage("订单[orderSn]参数不能为空");
            return response;
        }
        OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
        if (distribute == null) {
            response.setMessage("订单[" + orderSn + "]数据不存在");
            return response;
        }
        MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
        if (master == null) {
            response.setMessage("订单[" + distribute.getMasterOrderSn() + "]数据不存在");
            return response;
        }
        if (Constant.OI_ORDER_STATUS_UNCONFIRMED == distribute.getOrderStatus().intValue()) {
            response.setMessage("订单[" + orderSn + "]处于未确认状态，不能发货");
            return response;
        }
        if (Constant.OI_QUESTION_STATUS_QUESTION == distribute.getQuestionStatus().intValue()) {
            response.setMessage("订单[" + orderSn + "]处于问题单状态，不能发货");
            return response;
        }
        if (!OrderStatusUtils.isPayed(master.getTransType().shortValue(), master.getPayStatus())) {
            response.setMessage("订单[" + orderSn + "]处于未付款状态，不能发货");
            return response;
        }
        response.setSuccess(true);
        response.setMessage("可以发货");
        return response;
    }

    /**
     * 配送单发货
     *
     * @param distributeShipBean 发货信息
     * @param isSystem           是否系统分仓  true:是; false:否
     * @return ReturnInfo<String>
     */
    @Override
    public ReturnInfo<String> processShip(DistributeShippingBean distributeShipBean, boolean isSystem) {
        logger.info("开始处理订单发货 shipProviderList=" + JSONObject.toJSONString(distributeShipBean));
        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
        if (distributeShipBean == null) {
            info.setMessage("distributeShipBean 不能为空");
            return info;
        }
        if (StringUtil.isTrimEmpty(distributeShipBean.getOrderSn())) {
            info.setMessage("distributeShipBean.orderSn 不能为空");
            return info;
        }
        String masterOrderSn = distributeShipBean.getOrderSn();
        OrderDistributeExample distributeExample = new OrderDistributeExample();
        distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn);
        List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
        if (StringUtil.isListNull(distributes)) {
            info.setMessage("订单[" + masterOrderSn + "]下没有交货单数据");
            return info;
        }
        String orderSn = distributes.get(0).getOrderSn();
        List<OrderDepotShip> orderShipList = selectEffectiveShips(orderSn);
        if (orderShipList == null || orderShipList.isEmpty()) {
            logger.error("订单：" + orderSn + "发货单数量为0终止发货");
            info.setMessage("订单[" + orderSn + "]发货单数量为0终止发货");
            return info;
        }
        OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
        // 设定要保存的退单操作日志信息
        DistributeAction orderAction = new DistributeAction();
        // 客户已收货
        if (checkOrderShipStatus(distribute)) {
            delShippedByOrderSn(orderSn);
            // 添加分仓日志
            fillOrderAction(orderSn, distribute, orderAction);
            distributeActionService.saveOrderAction(orderAction);
            logger.debug("订单[" + orderSn + "]不符合发货条件");
            info.setMessage("订单[" + orderSn + "]不符合发货条件");
            return info;
        }

        SystemShipping shipping = systemShippingService.getSystemShipByShipCode(distributeShipBean.getShippingCode());
        if (shipping == null) {
            logger.error("订单[" + orderSn + "] 承运商编码[" + distributeShipBean.getShippingCode() + "]不存在");
            info.setMessage("订单[" + orderSn + "] 承运商编码[" + distributeShipBean.getShippingCode() + "]不存在");
            return info;
        }

        // 更新发货状态
        OrderDepotShip depotShip = new OrderDepotShip();
        // 快递单号
        depotShip.setInvoiceNo(distributeShipBean.getInvoiceNo());
        // 配送方式id
        depotShip.setShippingId(shipping.getShippingId());
        // 配送方式的名称
        depotShip.setShippingName(shipping.getShippingName());
        // 商品配送状态（0，未发货；1，已发货；2，已收货；3，备货中；6,门店收货10，快递取件；11，运输中；12，派件中；13，客户签收；14，客户拒签；15，货物遗失；16，货物损毁）
        depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_SHIPPED);
        // 订单配送时间
        depotShip.setDeliveryTime(distributeShipBean.getShipDate());
        // 单仓交货单
        if (StringUtil.isTrimEmpty(distributeShipBean.getDepotCode())
                || Constant.DETAILS_DEPOT_CODE.equalsIgnoreCase(distributeShipBean.getDepotCode())) {

            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);

            OrderDistribute updateDistribute = new OrderDistribute();
            // 发货总状态（0：未发货；1：备货中；2：部分发货；3：已发货；4，部分收货；5，客户已收货，6门店部分收货 7门店收货）
            distribute.setShipStatus(Constant.OD_SHIP_STATUS_ALLSHIPED);
            updateDistribute.setShipStatus(Constant.OD_SHIP_STATUS_ALLSHIPED);
            updateDistribute.setOrderSn(orderSn);
            updateDistribute.setUpdateTime(new Date());
            updateByPrimaryKey(updateDistribute);
        } else {
            // 更新发货状态
            String depotCode = distributeShipBean.getDepotCode();
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(depotCode).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);
            // 判断发货仓总状态并更新
            ReturnInfo<Byte> tInfo = judgeDistributeShipedStatus(orderSn);
            distribute.setShipStatus(tInfo.getData());
        }
        // 已发货发货单推送至物流监控
		/*try {
			MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
			if (!Constant.Chlitina.equals(master.getChannelCode())) {
				// orderExpressPullService.orderExpress(distribute.getMasterOrderSn());
			}
		} catch (Exception e) {
			logger.error(orderSn + "已发货数据推送至物流监控异常：", e);
		}*/
        //合并快递单号相同发货仓不同的发货数据，发送短信给用户
		/*try {
			List<String> depotCodes = new ArrayList<String>();
			depotCodes.add(distributeShipBean.getDepotCode());
			callbackChannel(orderSn, depotCodes, distribute, distributeShipBean.getShippingCode(), distributeShipBean.getInvoiceNo());
		} catch (Exception e) {
			logger.error("发货后调用回写发货接口异常：", e);
		}*/

        logger.debug("处理完毕订单" + orderSn);
        // 添加发货日志
        orderAction = new DistributeAction();
        fillOrderAction(orderSn, distribute, orderAction);
        distributeActionService.saveOrderAction(orderAction);
        isSend.set(null);
        ReturnInfo shipInfo = judgeMasterShipedStatus(distribute.getMasterOrderSn());
		/*if (shipInfo.getIsOk() == Constant.OS_YES) {
			// 回调退单接口
			callOrderReturn(distribute.getMasterOrderSn());
		}*/
        info.setMessage("订单[" + orderSn + "]发货成功！");
        info.setIsOk(Constant.OS_YES);
        return info;
    }

    /**
     * 配送单仓库发货确认
     *
     * @param distributeShipBean 配送出库对象
     * @return ReturnInfo<String>
     */
    public ReturnInfo<String> distDeliveryConfirm(DistributeShippingBean distributeShipBean) {
        logger.info("配送单发货确认 distributeShipBean:" + JSONObject.toJSONString(distributeShipBean));
        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
        String orderSn = distributeShipBean.getShipSn();
        OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
        if (distribute == null) {
            logger.error("订单：" + orderSn + "]不存在");
            info.setMessage("订单[" + orderSn + "]不存在");
            return info;
        }
        List<OrderDepotShip> orderShipList = selectEffectiveShips(orderSn);
        if (StringUtil.isListNull(orderShipList)) {
            logger.error("订单：" + orderSn + "发货单数量为0终止发货");
            info.setMessage("订单[" + orderSn + "]发货单数量为0终止发货");
            return info;
        }
        // 设定要保存的退单操作日志信息
        DistributeAction orderAction = new DistributeAction();
        // 客户已收货
        if (checkOrderShipStatus(distribute)) {
            delShippedByOrderSn(orderSn);
            // 添加分仓日志
            fillOrderAction(orderSn, distribute, orderAction);
            distributeActionService.saveOrderAction(orderAction);
            logger.error("订单[" + orderSn + "]不符合发货条件");
            info.setMessage("订单[" + orderSn + "]不符合发货条件");
            return info;
        }
		
		/*SystemShipping shipping = systemShippingService.getSystemShipByShipCode(distributeShipBean.getShippingCode());
		if (shipping == null) {
			logger.error("订单[" + orderSn + "] 承运商编码[" + distributeShipBean.getShippingCode() + "]不存在");
			info.setMessage("订单[" + orderSn + "] 承运商编码[" + distributeShipBean.getShippingCode() + "]不存在");
			return info;
		}*/
        // 单仓交货单
        if (StringUtil.isTrimEmpty(distributeShipBean.getDepotCode())
                || Constant.DETAILS_DEPOT_CODE.equalsIgnoreCase(distributeShipBean.getDepotCode())) {
            // 更新发货状态
            OrderDepotShip depotShip = new OrderDepotShip();
            // 快递单号
            depotShip.setInvoiceNo(distributeShipBean.getInvoiceNo());
//			depotShip.setShippingId(shipping.getShippingId()); // 配送方式id
//			depotShip.setShippingName(shipping.getShippingName()); // 配送方式的名称
            // 商品配送状态（0，未发货；1，已发货；2，已收货；3，备货中；6,门店收货10，快递取件；11，运输中；12，派件中；13，客户签收；14，客户拒签；15，货物遗失；16，货物损毁）
            depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_SHIPPED);

            // 订单配送时间
            depotShip.setDeliveryTime(distributeShipBean.getShipDate());
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);

            OrderDistribute updateDistribute = new OrderDistribute();
            // 发货总状态（0：未发货；1：备货中；2：部分发货；3：已发货；4，部分收货；5，客户已收货，6门店部分收货 7门店收货）
            distribute.setShipStatus(Constant.OD_SHIP_STATUS_ALLSHIPED);
            updateDistribute.setShipStatus(Constant.OD_SHIP_STATUS_ALLSHIPED);
            updateDistribute.setOrderSn(orderSn);
            updateDistribute.setUpdateTime(new Date());
            updateByPrimaryKey(updateDistribute);
        } else {
            // 更新发货状态
            String depotCode = distributeShipBean.getDepotCode();
            OrderDepotShip depotShip = new OrderDepotShip();
            // 快递单号
            depotShip.setInvoiceNo(distributeShipBean.getInvoiceNo());
//			depotShip.setShippingId(shipping.getShippingId()); // 配送方式id
//			depotShip.setShippingName(shipping.getShippingName()); // 配送方式的名称
            // // 商品配送状态（0，未发货；1，已发货；2，已收货；3，备货中；6,门店收货10，快递取件；11，运输中；12，派件中；13，客户签收；14，客户拒签；15，货物遗失；16，货物损毁）
            depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_SHIPPED);
            // 订单配送时间
            depotShip.setDeliveryTime(distributeShipBean.getShipDate());
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(depotCode).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);
            // 判断发货仓总状态并更新
            ReturnInfo<Byte> tInfo = judgeDistributeShipedStatus(orderSn);
            distribute.setShipStatus(tInfo.getData());
        }
        logger.debug("处理完毕订单" + orderSn);
        // 添加发货日志
        orderAction = new DistributeAction();
        fillOrderAction(orderSn, distribute, orderAction);
        distributeActionService.saveOrderAction(orderAction);
        ReturnInfo<String> shipInfo = judgeMasterShipedStatus(distribute.getMasterOrderSn());
        // 订单收货状态判断
        if (shipInfo != null && StringUtil.isNotEmpty(shipInfo.getData())) {
            masterOrderActionService.insertOrderActionBySn(distribute.getMasterOrderSn(), shipInfo.getData(), distributeShipBean.getActionUser());
        }
        info.setMessage("订单[" + orderSn + "]发货成功！");
        info.setIsOk(Constant.OS_YES);
        return info;
    }

    /**
     * 判断订单的有效性
     *
     * @param masterOrderSn
     * @return
     */
    private ReturnInfo<String> checkMasterOrderInfo(String masterOrderSn) {

        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);

        // 订单信息
        MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
        if (masterOrderInfo == null) {
            info.setMessage("订单[" + masterOrderSn + "]不存在");
            return info;
        }

        if (masterOrderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_UNCONFIRMED) {
            info.setMessage(" 订单[" + masterOrderSn + "]处于未确定状态");
            return info;
        }
        if (masterOrderInfo.getQuestionStatus() != Constant.OI_QUESTION_STATUS_NORMAL) {
            info.setMessage(" 订单[" + masterOrderSn + "]要处于正常单状态");
            return info;
        }
        if (masterOrderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED) {
            info.setMessage(" 订单" + masterOrderSn + "已经取消,请重新操作！");
            return info;
        }
        if (masterOrderInfo.getPayStatus() == Constant.OI_PAY_STATUS_UNPAYED) {
            info.setMessage(" 订单" + masterOrderSn + "未付款！");
            return info;
        }
        info.setIsOk(Constant.OS_YES);
        info.setMessage("成功");
        return info;
    }

    /**
     * 配送单拣货中
     *
     * @param distributeShipBean
     * @return
     */
    public ReturnInfo<String> processDistributePicking(DistributeShippingBean distributeShipBean) {
        logger.info("开始处理订单配送单拣货 distributeShipBean=" + JSONObject.toJSONString(distributeShipBean));
        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
        if (distributeShipBean == null) {
            info.setMessage("distributeShipBean 不能为空");
            return info;
        }
        if (StringUtil.isTrimEmpty(distributeShipBean.getOrderSn())) {
            info.setMessage("distributeShipBean.orderSn 不能为空");
            return info;
        }

        /**
         * 交货单
         */
        String orderSn = distributeShipBean.getShipSn();
        if (StringUtils.isBlank(orderSn)) {
            info.setMessage("交货单号不能为空");
            return info;
        }

        /**
         * 仓库编码
         */
        String depotCode = distributeShipBean.getDepotCode();

        // 订单编码
        String masterOrderSn = distributeShipBean.getOrderSn();
        ReturnInfo<String> checkReturnInfo = checkMasterOrderInfo(masterOrderSn);
        if (checkReturnInfo.getIsOk() == Constant.OS_NO) {
            return checkReturnInfo;
        }

        // 获取订单的交货单
        OrderDistributeExample distributeExample = new OrderDistributeExample();
        distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn).andOrderSnEqualTo(orderSn);
        List<OrderDistribute> distributeList = orderDistributeMapper.selectByExample(distributeExample);
        if (StringUtil.isListNull(distributeList)) {
            info.setMessage("订单[" + masterOrderSn + "]下没有交货单[" + orderSn + "]数据");
            return info;
        }

        List<OrderDepotShip> orderShipList = selectEffectiveShips(orderSn);
        if (orderShipList == null || orderShipList.isEmpty()) {
            logger.error("订单：" + orderSn + "发货单数量为0");
            info.setMessage("订单[" + orderSn + "]发货单数量为0");
            return info;
        }

        // 获取配货单信息
        OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
        // 发货状态 1：备货中；2：部分发货；3：已发货；4，部分收货；5，客户已收货，6门店部分收货 7门店收货, 8备货完成
        int shipStatus = distribute.getShipStatus();
		
		/*if (shipStatus == Constant.OD_SHIP_STATUS_PREPARING) {
			info.setMessage("订单[" + orderSn + "]拣货中");
			return info;
		}*/

        if (shipStatus > Constant.OD_SHIP_STATUS_PREPARING) {
            info.setMessage("订单[" + orderSn + "]拣货完成");
            return info;
        }

        String actionNote = "拣货中";
        // 单仓交货单
        if (StringUtil.isTrimEmpty(distributeShipBean.getDepotCode())
                || depotCode.equals(Constant.DETAILS_DEPOT_CODE)) {

            // 更新发货状态
            OrderDepotShip depotShip = new OrderDepotShip();
            // 备货中
            depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_PREPARING);

            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);

            // 更新配送单,拣货=备货中
            OrderDistribute updateDistribute = new OrderDistribute();
            distribute.setShipStatus(Constant.OD_SHIP_STATUS_PREPARING);
            updateDistribute.setShipStatus(Constant.OD_SHIP_STATUS_PREPARING);
            updateDistribute.setOrderSn(orderSn);
            updateDistribute.setUpdateTime(new Date());
            updateByPrimaryKey(updateDistribute);
        } else {
            // 更新发货状态
            OrderDepotShip depotShip = new OrderDepotShip();
            depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_PREPARING);
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(depotCode).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);
            // 判断发货仓总状态并更新
            ReturnInfo<Byte> tInfo = judgeDistributeShipedStatus(orderSn);
            distribute.setShipStatus(tInfo.getData());
            actionNote = "仓[" + depotCode + "]" + actionNote;
        }

        // 添加发货日志
        // 设定要保存的退单操作日志信息
        DistributeAction orderAction = new DistributeAction();
        orderAction.setActionNote(actionNote);
        fillOrderActionByDistribute(orderSn, distribute, orderAction);
        orderAction.setActionUser(distributeShipBean.getActionUser());
        distributeActionService.saveOrderAction(orderAction);
        //ReturnInfo shipInfo = judgeMasterShipedStatus(distribute.getMasterOrderSn());
        //masterOrderActionService.insertOrderActionBySn(masterOrderSn, message, distributeShipBean.getActionUser());
        info.setMessage("订单[" + orderSn + "]拣货设置成功！");
        info.setIsOk(Constant.OS_YES);

        return info;
    }

    /**
     * 配送单拣货完成
     *
     * @param distributeShipBean
     * @return
     */
    public ReturnInfo<String> processDistributePickUp(DistributeShippingBean distributeShipBean) {
        logger.info("完成处理订单配送单拣货 distributeShipBean=" + JSONObject.toJSONString(distributeShipBean));
        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
        if (distributeShipBean == null) {
            info.setMessage("distributeShipBean 不能为空");
            return info;
        }
        if (StringUtil.isTrimEmpty(distributeShipBean.getOrderSn())) {
            info.setMessage("distributeShipBean.orderSn 不能为空");
            return info;
        }

        /**
         * 交货单
         */
        String orderSn = distributeShipBean.getShipSn();
        if (StringUtils.isBlank(orderSn)) {
            info.setMessage("交货单号不能为空");
            return info;
        }

        // 订单编码
        String masterOrderSn = distributeShipBean.getOrderSn();

        // 判断主订单的有效性
        ReturnInfo<String> checkReturnInfo = checkMasterOrderInfo(masterOrderSn);
        if (checkReturnInfo.getIsOk() == Constant.OS_NO) {
            return checkReturnInfo;
        }

        // 根据订单编码和配货单编码，获取配货单信息
        OrderDistributeExample distributeExample = new OrderDistributeExample();
        distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn).andOrderSnEqualTo(orderSn);
        List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
        if (StringUtil.isListNull(distributes)) {
            info.setMessage("订单[" + masterOrderSn + "]下没有交货单[" + orderSn + "]数据");
            return info;
        }

        // 获取配货单下的发货单列表
        List<OrderDepotShip> orderShipList = selectEffectiveShips(orderSn);
        if (orderShipList == null || orderShipList.isEmpty()) {
            logger.error("订单：" + orderSn + "发货单数量为0");
            info.setMessage("订单[" + orderSn + "]发货单数量为0");
            return info;
        }

        // 获取配货单信息
        //OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
        OrderDistribute distribute = distributes.get(0);
        // 发货状态 1：备货中；2：部分发货；3：已发货；4，部分收货；5，客户已收货，6门店部分收货 7门店收货, 8备货完成
        int shipStatus = distribute.getShipStatus();

        if (shipStatus == Constant.OD_SHIP_STATUS_PREPARED) {
            info.setMessage("订单[" + orderSn + "]拣货完成");
            return info;
        }

        if (shipStatus > Constant.OD_SHIP_STATUS_PREPARING) {
            info.setMessage("订单[" + orderSn + "]已出库");
            return info;
        }

        String actionNote = "拣货完成";
        // 单仓交货单
        if (StringUtil.isTrimEmpty(distributeShipBean.getDepotCode())
                || distributeShipBean.getDepotCode().equals(Constant.DETAILS_DEPOT_CODE)) {

            // 更新发货状态
            OrderDepotShip depotShip = new OrderDepotShip();

            // 4备货完成
            depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_PREPARED);
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);

            // 更新配送单,拣货完成=备货完成
            OrderDistribute updateDistribute = new OrderDistribute();
            distribute.setShipStatus(Constant.OD_SHIP_STATUS_PREPARED);
            updateDistribute.setShipStatus(Constant.OD_SHIP_STATUS_PREPARED);
            updateDistribute.setOrderSn(orderSn);
            updateDistribute.setUpdateTime(new Date());
            updateByPrimaryKey(updateDistribute);
        } else {
            // 更新发货状态
            String depotCode = distributeShipBean.getDepotCode();
            OrderDepotShip depotShip = new OrderDepotShip();
            depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_PREPARED);
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(depotCode).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);
            // 判断发货仓总状态并更新
            //ReturnInfo<Byte> tInfo = judgeDistributeShipedStatus(orderSn);
            //distribute.setShipStatus(Constant.OD_SHIP_STATUS_PREPARED);
            // 判断发货仓总状态并更新
            ReturnInfo<Byte> tInfo = judgeDistributeShipedStatus(orderSn);
            distribute.setShipStatus(tInfo.getData());
            actionNote = "仓[" + depotCode + "]" + actionNote;
        }

        // 添加发货日志
        // 设定要保存的退单操作日志信息
        DistributeAction orderAction = new DistributeAction();
        orderAction.setActionNote(actionNote);
        fillOrderActionByDistribute(orderSn, distribute, orderAction);
        orderAction.setActionUser(distributeShipBean.getActionUser());
        distributeActionService.saveOrderAction(orderAction);
        //ReturnInfo shipInfo = judgeMasterShipedStatus(distribute.getMasterOrderSn());
        //masterOrderActionService.insertOrderActionBySn(masterOrderSn, message, distributeShipBean.getActionUser());
        info.setMessage("订单[" + orderSn + "]拣货完成设置成功！");
        info.setIsOk(Constant.OS_YES);

        return info;
    }

    /**
     * 订单取消日志记录
     *
     * @param distributeShipBean
     * @return
     */
    public ReturnInfo<String> processOrderDistributeCancel(DistributeShippingBean distributeShipBean) {
        logger.info("接收处理订单取消结果=" + JSONObject.toJSONString(distributeShipBean));
        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
        if (distributeShipBean == null) {
            info.setMessage("distributeShipBean 不能为空");
            return info;
        }

        // 订单编码
        String masterOrderSn = distributeShipBean.getOrderSn();
        if (StringUtil.isTrimEmpty(distributeShipBean.getOrderSn())) {
            info.setMessage("distributeShipBean.orderSn 不能为空");
            return info;
        }

        // 判断主订单的有效性
        // 订单信息
        MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
        if (masterOrderInfo == null) {
            info.setMessage("订单[" + masterOrderSn + "]不存在");
            return info;
        }

        String actionNote = "订单取消成功通知";

        // 操作用户
        String actionUser = distributeShipBean.getActionUser();
        if (StringUtils.isBlank(actionUser)) {
            actionUser = "system";
        }

        masterOrderActionService.insertOrderActionBySn(masterOrderSn, actionNote, actionUser);
        info.setMessage("订单[" + masterOrderSn + "]取消通知接收成功！");
        info.setIsOk(Constant.OS_YES);

        return info;
    }

    /**
     * 订单自提核销、签收成功
     *
     * @param distributeShipBean 订单交货单信息
     * @return ReturnInfo<String>
     */
    @Override
    public ReturnInfo<String> cacWriteOff(DistributeShippingBean distributeShipBean) {
        logger.info("开始处理订单自提核销 distributeShipBean=" + JSONObject.toJSONString(distributeShipBean));
        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
        if (distributeShipBean == null) {
            info.setMessage("distributeShipBean 不能为空");
            return info;
        }
        if (StringUtil.isTrimEmpty(distributeShipBean.getOrderSn())) {
            info.setMessage("distributeShipBean.orderSn 不能为空");
            return info;
        }
        String masterOrderSn = distributeShipBean.getOrderSn();
        OrderDistributeExample distributeExample = new OrderDistributeExample();
        distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn);
        List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
        if (StringUtil.isListNull(distributes)) {
            info.setMessage("订单[" + masterOrderSn + "]下没有交货单数据");
            return info;
        }
        String orderSn = distributes.get(0).getOrderSn();
        List<OrderDepotShip> orderShipList = selectEffectiveShips(orderSn);
        if (orderShipList == null || orderShipList.isEmpty()) {
            logger.error("订单：" + orderSn + "发货单数量为0终止发货");
            info.setMessage("订单[" + orderSn + "]发货单数量为0终止发货");
            return info;
        }
        OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
        SystemShipping shipping = systemShippingService.getSystemShipByShipCode(distributeShipBean.getShippingCode());
        if (shipping == null) {
            logger.error("订单[" + orderSn + "] 承运商编码[" + distributeShipBean.getShippingCode() + "]不存在");
            info.setMessage("订单[" + orderSn + "] 承运商编码[" + distributeShipBean.getShippingCode() + "]不存在");
            return info;
        }
        // 单仓交货单
        if (StringUtil.isTrimEmpty(distributeShipBean.getDepotCode())
                || distributeShipBean.getDepotCode().equals(Constant.DETAILS_DEPOT_CODE)) {
            // 更新发货状态
            OrderDepotShip depotShip = new OrderDepotShip();
            depotShip.setInvoiceNo(distributeShipBean.getInvoiceNo());
            depotShip.setShippingId(shipping.getShippingId());
            depotShip.setShippingName(shipping.getShippingName());
            depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_RECEIVED);
            depotShip.setDeliveryTime(distributeShipBean.getShipDate());
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);
            OrderDistribute updateDistribute = new OrderDistribute();
            distribute.setShipStatus(Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED);
            updateDistribute.setShipStatus(Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED);
            updateDistribute.setOrderStatus(Constant.OD_ORDER_STATUS_FINISHED);
            updateDistribute.setOrderSn(orderSn);
            updateDistribute.setUpdateTime(new Date());
            updateByPrimaryKey(updateDistribute);
            MasterOrderInfo updateMaster = new MasterOrderInfo();
            updateMaster.setMasterOrderSn(masterOrderSn);
            updateMaster.setUpdateTime(new Date());
            updateMaster.setFinishTime(new Date());
            updateMaster.setOrderStatus((byte) Constant.OI_ORDER_STATUS_FINISHED);
            updateMaster.setShipStatus((byte) Constant.OI_SHIP_STATUS_ALLRECEIVED);
            masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
        } else {
            // 更新发货状态
            String depotCode = distributeShipBean.getDepotCode();
            OrderDepotShip depotShip = new OrderDepotShip();
            depotShip.setInvoiceNo(distributeShipBean.getInvoiceNo());
            depotShip.setShippingId(shipping.getShippingId());
            depotShip.setShippingName(shipping.getShippingName());
            // 已发货状态，已签收状态由调度任务完成
            depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_SHIPPED);
            depotShip.setDeliveryTime(distributeShipBean.getShipDate());
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(depotCode).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);
            // 判断发货仓总状态并更新
            ReturnInfo<Byte> tInfo = judgeDistributeShipedStatus(orderSn);
            distribute.setShipStatus(tInfo.getData());

            // 更新主订单的配送状态
            ReturnInfo shipInfo = judgeMasterShipedStatus(distribute.getMasterOrderSn());
        }
        OrderDistribute updateDistribute = new OrderDistribute();
        updateDistribute.setOrderSn(orderSn);
        updateDistribute.setGotStatus(Constant.GOT_STATUS_YES);
        updateDistribute.setUpdateTime(new Date());
        orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
        // 添加发货日志
        // 设定要保存的退单操作日志信息
        String message = "订单自提核销成功" + distributeShipBean.getInvoiceNo();
        if (!Constant.SHIP_CODE_CAC.equals(distributeShipBean.getShippingCode())) {
            message = "订单签收成功" + distributeShipBean.getInvoiceNo();
        }
        DistributeAction orderAction = new DistributeAction();
        fillOrderAction(orderSn, distribute, orderAction);
        orderAction.setActionNote(message);
        orderAction.setActionUser(distributeShipBean.getActionUser());
        distributeActionService.saveOrderAction(orderAction);
        masterOrderActionService.insertOrderActionBySn(masterOrderSn, message, distributeShipBean.getActionUser());
        info.setMessage("订单[" + orderSn + "]签收成功！");
        info.setIsOk(Constant.OS_YES);
        return info;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ReturnInfo processShip(String orderSn, List<DistributeShipBean> shipProviderList, boolean isSystem) {
        logger.info("开始处理订单发货" + orderSn + ";shipProviderList=" + JSONObject.toJSONString(shipProviderList));
        ReturnInfo info = new ReturnInfo(Constant.OS_NO);
        isSend.set(isSystem);
        List<OrderDepotShip> orderShipList = selectEffectiveShips(orderSn);
        if (orderShipList == null || orderShipList.isEmpty()) {
            logger.error("订单：" + orderSn + "发货单数量为0终止发货");
            info.setMessage("订单[" + orderSn + "]发货单数量为0终止发货");
            return info;
        }
        logger.debug("获取orderShip" + orderSn + "orderShipList : " + orderShipList);
        OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
        // 设定要保存的退单操作日志信息
        DistributeAction orderAction = new DistributeAction();
        if (checkOrderShipStatus(distribute)) {// 客户已收款
            delShippedByOrderSn(orderSn);
            // 添加分仓日志
            fillOrderAction(orderSn, distribute, orderAction);
            distributeActionService.saveOrderAction(orderAction);
            logger.debug("订单[" + orderSn + "]不符合发货条件");
            info.setMessage("订单[" + orderSn + "]不符合发货条件");
            return info;
        }

        // 实际仓库代码
        Set<String> vDecodeSet = buildVdeCodeSet(orderSn, shipProviderList);
        //实际发货单号
        Set<String> invoiceNoSet = buildinvoiceNoSet(shipProviderList);

        boolean smsFlag = checkShipStatus(orderShipList) == 0;
        logger.debug(smsFlag);
        logger.debug("获取vDecodeSet" + orderSn + "vDecodeSet : " + vDecodeSet);
        List<OrderDepotShip> ships = new ArrayList<OrderDepotShip>();
        for (Iterator<String> iterator = vDecodeSet.iterator(); iterator.hasNext(); ) {
            // 虚拟仓库编码
            String vDecode = iterator.next();
            logger.debug("发货仓库:" + vDecode);
            OrderDepotShip orderShip = findOrderShipByDecode(orderShipList, vDecode);
            if (orderShip == null)
                continue;
            logger.debug("找到ship" + orderShip.getDepotCode());
            orderShip.setShippingStatus((byte) 1);
            DistributeShipBean shippedBean = getOrderToShippedProviderBean(shipProviderList, vDecode);
            logger.debug("找到" + shippedBean);
            // 填充ship
            fillOrderShip(orderShip, shippedBean);
            // 更新发货单状态
            updateByPrimaryKeySelective(orderShip);
            ships.add(orderShip);
        }
        //合并快递单号相同发货仓不同的发货数据，发送短信给用户
        for (String invoiceNo : invoiceNoSet) {
            List<String> depotCodes = new ArrayList<String>();
            OrderDepotShip depotShip = new OrderDepotShip();
            DistributeShipBean shippedBean = getInvoiceNoShippedProviderBean(shipProviderList, invoiceNo);
            fillOrderShip(depotShip, shippedBean);
            for (String vDecode : vDecodeSet) {
                OrderDepotShip orderShipDecode = findOrderShipByDecode(orderShipList, vDecode);
                if (orderShipDecode == null) {
                    continue;
                }
                if (invoiceNo.equals(orderShipDecode.getInvoiceNo())) {
                    depotCodes.add(orderShipDecode.getDepotCode());
                }
            }
            try {
                // 发送发货短信
                fahuoSMS(distribute, distribute.getOrderFrom(), depotShip.getShippingName(), depotShip.getInvoiceNo());
            } catch (Exception e) {
                logger.error("发送发货短信.调回写发货接口异常：", e);
            }
        }
        // 已发货发货单推送至物流监控
        try {
//			orderExpressPullService.orderExpress(distribute.getMasterOrderSn());
        } catch (Exception e) {
            logger.error(orderSn + "已发货数据推送至LMS异常：", e);
        }
        // 已发货ship的数量
        int orderShipStatus = checkShipStatus(orderShipList);
        if (orderShipStatus == orderShipList.size()) {
            distribute.setShipStatus((byte) Constant.OI_SHIP_STATUS_ALLSHIPED);
            updateByPrimaryKey(distribute);
            // 订单全部已经发货
            if (distribute.getOrderStatus().intValue() == Constant.OI_ORDER_STATUS_UNCONFIRMED) {
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setOrderSn(orderSn);
                orderStatus.setAdminUser(Constant.OS_STRING_SYSTEM);
                orderStatus.setMessage("发货确认:");
                orderStatus.setType(Constant.order_type_distribute);
                orderConfirmService.asynConfirmOrderByOrderSn(orderStatus);
            }
        } else if (orderShipStatus < orderShipList.size()) {
            // 订单部分发货
            distribute.setShipStatus((byte) Constant.OI_SHIP_STATUS_PARTSHIPPED);
            updateByPrimaryKey(distribute);
        }
        logger.debug("处理完毕订单" + orderSn);
        // 添加发货日志
        orderAction = new DistributeAction();
        fillOrderAction(orderSn, distribute, orderAction);
        distributeActionService.saveOrderAction(orderAction);
        saveGdnInfo(orderSn, shipProviderList);
        delShippedByOrderSn(orderSn);
        isSend.set(null);
        ReturnInfo shipInfo = judgeMasterShipedStatus(distribute.getMasterOrderSn());
		/*if (shipInfo.getIsOk() == Constant.OS_YES) {
			// 回调退单接口
			callOrderReturn(distribute.getMasterOrderSn());
		}*/
        info.setMessage("订单[" + orderSn + "]发货成功！");
        info.setIsOk(Constant.OS_YES);
        return info;
    }

    @Override
    public void reShipped(String orderSn) {
        List<DistributeShipBean> item = null;
        try {
        } catch (Exception e) {
            logger.error("订单[" + orderSn + "]获取发货信息出错: " + e.getMessage(), e);
            return;
        }
        if (item == null || item.isEmpty()) {
            logger.debug("没有获取到发货信息 orderSn : " + orderSn);
            return;
        }
    }

    /**
     * 订单发货状态处理
     * @param masterOrderSn 订单编号
     * @return ReturnInfo<String>
     */
    @SuppressWarnings("rawtypes")
    @Override
    public ReturnInfo<String> judgeMasterShipedStatus(String masterOrderSn) {
        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
        info.setData("未发货");
        if (StringUtil.isTrimEmpty(masterOrderSn)) {
            info.setMessage("[masterOrderSn] 参数为空");
            return info;
        }
        try {
            // 有效交货单
            List<OrderDistribute> distributes = this.selectEffectiveDistributes(masterOrderSn);
            if (StringUtil.isListNull(distributes)) {
                info.setMessage("订单[ " + masterOrderSn + "] 交货单列表为空！");
                return info;
            }
            MasterOrderInfo master = this.masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
            MasterOrderInfo updateMaster = new MasterOrderInfo();
            List<Byte> shippingStatus = new ArrayList<Byte>();
            for (OrderDistribute distribute : distributes) {
                shippingStatus.add(distribute.getShipStatus());
            }
            byte maxStatus = Collections.max(shippingStatus);
            byte minStatus = Collections.min(shippingStatus);
            updateMaster.setShipStatus(master.getShipStatus());
            String deliDesc = "";
            if (minStatus != maxStatus) {
                if (minStatus == Constant.OD_SHIP_STATUS_UNSHIPPED || minStatus == Constant.OD_SHIP_STATUS_PARTSHIPPED) {
                    updateMaster.setShipStatus((byte) Constant.OI_SHIP_STATUS_PARTSHIPPED);
                    deliDesc = "部分发货";
                }
                if (minStatus == Constant.OD_SHIP_STATUS_ALLSHIPED || minStatus == Constant.OD_SHIP_STATUS_CUSTOMER_PARTRECEIVED) {
                    updateMaster.setShipStatus((byte) Constant.OI_SHIP_STATUS_PARTRECEIVED);
                    deliDesc = "部分收货";
                }
            } else if (minStatus == maxStatus) {
                updateMaster.setShipStatus(minStatus);
                if (maxStatus == Constant.OD_SHIP_STATUS_ALLSHIPED) {
                    updateMaster.setShipStatus((byte) Constant.OI_SHIP_STATUS_ALLSHIPED);
                    deliDesc = "已发货";
                } else if (maxStatus == Constant.OD_SHIP_STATUS_CUSTOMER_PARTRECEIVED) {
                    updateMaster.setShipStatus((byte) Constant.OI_SHIP_STATUS_PARTRECEIVED);
                    deliDesc = "部分收货";
                } else if (maxStatus == Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED) {
                    updateMaster.setShipStatus((byte) Constant.OI_SHIP_STATUS_ALLRECEIVED);
                    deliDesc = "已收货";
                } else if (maxStatus == Constant.OI_SHIP_STATUS_PREPARED || maxStatus == Constant.OI_SHIP_STATUS_PREPARING) {
                    // 备货中
                    updateMaster.setShipStatus((byte) Constant.OI_SHIP_STATUS_PREPARING);
                    deliDesc = "备货中";
                }
            }
            updateMaster.setMasterOrderSn(masterOrderSn);
            updateMaster.setUpdateTime(new Date());
            masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
            info.setData(deliDesc);
            if (updateMaster.getShipStatus() >= Constant.OI_SHIP_STATUS_ALLSHIPED) {
                info.setMessage("订单已发货");
                info.setIsOk(Constant.OS_YES);
            } else {
                info.setMessage("订单发货状态未发货.部分发货");
                return info;
            }
        } catch (Exception e) {
            logger.error("订单[" + masterOrderSn + "]更新发货单状态" + e.getMessage(), e);
            info.setMessage("订单[" + masterOrderSn + "]更新发货单状态" + e.getMessage());
            return info;
        }
        info.setMessage("判断更新订单发货状态成功");
        info.setIsOk(Constant.OS_YES);
        return info;
    }

    /**
     * 处理交货单发货状态
     *
     * @param orderSn 交货单编码
     * @return ReturnInfo<Byte>
     */
    @Override
    public ReturnInfo<Byte> judgeDistributeShipedStatus(String orderSn) {
        ReturnInfo<Byte> info = new ReturnInfo<Byte>(Constant.OS_NO);
        if (StringUtil.isTrimEmpty(orderSn)) {
            info.setMessage("[orderSn] 参数为空");
            return info;
        }
        try {
            // 分配单发货状态 有效交货单
            List<OrderDepotShip> depotShips = this.selectEffectiveShips(orderSn);
            if (StringUtil.isListNull(depotShips)) {
                info.setMessage("订单[ " + orderSn + "] 分仓发货单列表为空！");
                return info;
            }
            OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
            if (distribute == null) {
                info.setMessage("订单[ " + orderSn + "] 不存在！");
                return info;
            }
            OrderDistribute updateDistribute = new OrderDistribute();
            updateDistribute.setOrderSn(orderSn);
            updateDistribute.setShipStatus(distribute.getShipStatus());
            updateDistribute.setUpdateTime(new Date());
            List<Byte> shippingStatus = new ArrayList<Byte>();
            for (OrderDepotShip ship : depotShips) {
                shippingStatus.add(ship.getShippingStatus());
            }
            byte maxStatus = Collections.max(shippingStatus);
            byte minStatus = Collections.min(shippingStatus);
            if (minStatus != maxStatus) {
                // 最小状态未发货
                if (minStatus == Constant.OS_SHIPPING_STATUS_UNSHIPPED) {
                    // 最大状态为备货中，或者备货完成
                    if (maxStatus == Constant.OS_SHIPPING_STATUS_PREPARING || maxStatus == Constant.OS_SHIPPING_STATUS_PREPARED) {
                        // 备货中
                        updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_PREPARING);
                    } else {
                        // 部分发货
                        updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_PARTSHIPPED);
                    }
                } else if (minStatus == Constant.OS_SHIPPING_STATUS_SHIPPED) {
                    // 最小状态已发货
                    // 最大状态为备货中，或者备货完成
                    if (maxStatus == Constant.OS_SHIPPING_STATUS_PREPARING || maxStatus == Constant.OS_SHIPPING_STATUS_PREPARED) {
                        // 部分发货
                        updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_PARTSHIPPED);
                    } else {
                        // 部分收货
                        updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_CUSTOMER_PARTRECEIVED);
                    }
                } else if (minStatus == Constant.OS_SHIPPING_STATUS_RECEIVED) {
                    // 最小状态已收货
                    // 最大状态为备货中，或者备货完成
                    if (maxStatus == Constant.OS_SHIPPING_STATUS_PREPARING || maxStatus == Constant.OS_SHIPPING_STATUS_PREPARED) {
                        // 部分收货
                        updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_CUSTOMER_PARTRECEIVED);
                    } else {
                        // 客户已收货
                        updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED);
                    }
                } else if (minStatus == Constant.OS_SHIPPING_STATUS_CONFIRM) {
                    // 最小状态为客户确认收货
                    // 客户已收货
                    updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED);
                }
            } else if (minStatus == maxStatus) {
                if (maxStatus == Constant.OS_SHIPPING_STATUS_PREPARING) {
                    // 备货中
                    updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_PREPARING);
                } else if (maxStatus == Constant.OS_SHIPPING_STATUS_PREPARED) {
                    // 备货完成
                    updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_PREPARED);
                } else if (maxStatus == Constant.OS_SHIPPING_STATUS_SHIPPED) {
                    // 已发货
                    updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_ALLSHIPED);
                } else if (maxStatus == Constant.OS_SHIPPING_STATUS_RECEIVED || maxStatus == Constant.OS_SHIPPING_STATUS_CONFIRM) {
                    // 已收货 | 确认收货
                    // 客户已收货
                    updateDistribute.setShipStatus((byte) Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED);
                }
            }
            this.orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
            // 交货单状态
            info.setData(updateDistribute.getShipStatus());
            if (updateDistribute.getShipStatus() >= Constant.OD_SHIP_STATUS_ALLSHIPED) {
                info.setMessage("交货单已发货");
                info.setIsOk(Constant.OS_YES);
            } else {
                info.setMessage("订单发货状态未发货.部分发货");
                return info;
            }
        } catch (Exception e) {
            logger.error("订单[" + orderSn + "]更新发货单状态" + e.getMessage(), e);
            info.setMessage("订单[" + orderSn + "]更新发货单状态" + e.getMessage());
            return info;
        }
        info.setMessage("判断更新订单发货状态成功");
        info.setIsOk(Constant.OS_YES);
        return info;
    }

    /**
     * 确认收货
     *
     * @param masterOrderSn 订单编码
     * @param actionUser    操作人
     * @return ReturnInfo
     */
    @SuppressWarnings("rawtypes")
    @Override
    public ReturnInfo confirmReceipt(String masterOrderSn, String actionUser) {
        logger.info("订单发货14天后确认收货更新masterOrderSn:" + masterOrderSn + ",actionUser:" + actionUser);
        ReturnInfo ri = new ReturnInfo(Constant.OS_NO);
        try {
            if (StringUtil.isTrimEmpty(masterOrderSn)) {
                ri.setMessage("订单号不能为空");
                return ri;
            }
            if (StringUtil.isTrimEmpty(actionUser)) {
                ri.setMessage("actionUser不能为空！");
                return ri;
            }
            MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
            if (master == null) {
                ri.setMessage("订单[" + masterOrderSn + "]不存在");
                return ri;
            }
            if (master.getShipStatus() != Constant.OI_SHIP_STATUS_ALLSHIPED) {
                ri.setMessage("订单[" + masterOrderSn + "]非已发货状态！");
                return ri;
            }
            MasterOrderInfo updateMaster = new MasterOrderInfo();
            updateMaster.setMasterOrderSn(masterOrderSn);
            updateMaster.setShipStatus((byte) Constant.OI_SHIP_STATUS_ALLRECEIVED);
            updateMaster.setUpdateTime(new Date());
            masterOrderInfoMapper.updateByPrimaryKeySelective(updateMaster);
            OrderDistributeExample distributeExample = new OrderDistributeExample();
            distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn).andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
            List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
            if (StringUtil.isListNull(distributes)) {
                ri.setMessage("订单[" + masterOrderSn + "]交货单为空！");
                return ri;
            }
            for (OrderDistribute distribute : distributes) {
                if (distribute.getShipStatus() != Constant.OI_SHIP_STATUS_ALLSHIPED) {
                    ri.setMessage("订单[" + masterOrderSn + "]非已发货状态！");
                    continue;
                }
                OrderDistribute updateDistribute = new OrderDistribute();
                updateDistribute.setOrderSn(distribute.getOrderSn());
                updateDistribute.setUpdateTime(new Date());
                updateDistribute.setShipStatus((byte) Constant.OI_SHIP_STATUS_ALLRECEIVED);
                orderDistributeMapper.updateByPrimaryKeySelective(updateDistribute);
                OrderDepotShipExample example = new OrderDepotShipExample();
                example.or().andOrderSnEqualTo(distribute.getOrderSn()).andIsDelEqualTo(0);
                List<OrderDepotShip> depotShips = this.orderDepotShipMapper.selectByExample(example);
                if (StringUtil.isListNull(depotShips)) {
                    continue;
                }
                for (OrderDepotShip depotShip : depotShips) {
                    OrderDepotShip updateDepotShip = new OrderDepotShip();
                    updateDepotShip.setDepotCode(depotShip.getDepotCode());
                    updateDepotShip.setInvoiceNo(depotShip.getInvoiceNo());
                    updateDepotShip.setOrderSn(depotShip.getOrderSn());
                    updateDepotShip.setDeliveryConfirmTime(new Date());
                    updateDepotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_RECEIVED);
                    orderDepotShipMapper.updateByPrimaryKeySelective(updateDepotShip);
                }
            }
            masterOrderActionService.insertOrderActionBySn(masterOrderSn, "客户确认收货！", actionUser);
            ri.setIsOk(ConstantValues.YESORNO_YES);
            ri.setMessage("确认收货更新完成！");
        } catch (Exception e) {
            logger.error(masterOrderSn + "确认收货更新失败:" + e.getMessage(), e);
            ri.setMessage("确认收货更新失败:" + e.toString());
        }
        return ri;

    }

    /**
     * 分配单发货确认
     *
     * @param distributeShipBean 分配单发货信息
     * @return ReturnInfo<String>
     */
    @Override
    public ReturnInfo<String> distOrderConfirm(DistributeShippingBean distributeShipBean) {
        logger.info("开始处理订单发货 distributeShipBean:" + JSONObject.toJSONString(distributeShipBean));
        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
        if (distributeShipBean == null) {
            info.setMessage("distributeShipBean 不能为空");
            return info;
        }
        if (StringUtil.isTrimEmpty(distributeShipBean.getOrderSn())) {
            info.setMessage("distributeShipBean.orderSn 不能为空");
            return info;
        }
        String masterOrderSn = distributeShipBean.getOrderSn();
        OrderDistributeExample distributeExample = new OrderDistributeExample();
        distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn);
        List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
        if (StringUtil.isListNull(distributes)) {
            info.setMessage("订单[" + masterOrderSn + "]下没有交货单数据");
            return info;
        }
        String orderSn = distributes.get(0).getOrderSn();
        List<OrderDepotShip> orderShipList = selectEffectiveShips(orderSn);
        if (orderShipList == null || orderShipList.isEmpty()) {
            logger.error("订单：" + orderSn + "发货单数量为0终止发货");
            info.setMessage("订单[" + orderSn + "]发货单数量为0终止发货");
            return info;
        }
        OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
        // 设定要保存的退单操作日志信息
        DistributeAction orderAction = new DistributeAction();
        // 客户已收款
        if (checkOrderShipStatus(distribute)) {
            delShippedByOrderSn(orderSn);
            // 添加分仓日志
            fillOrderAction(orderSn, distribute, orderAction);
            distributeActionService.saveOrderAction(orderAction);
            logger.debug("订单[" + orderSn + "]不符合发货条件");
            info.setMessage("订单[" + orderSn + "]不符合发货条件");
            return info;
        }

        SystemShipping shipping = systemShippingService.getSystemShipByShipCode(distributeShipBean.getShippingCode());
        if (shipping == null) {
            logger.error("订单[" + orderSn + "] 承运商编码[" + distributeShipBean.getShippingCode() + "]不存在");
            info.setMessage("订单[" + orderSn + "] 承运商编码[" + distributeShipBean.getShippingCode() + "]不存在");
            return info;
        }

        // 更新发货状态
        OrderDepotShip depotShip = new OrderDepotShip();
        // 快递单号
        depotShip.setInvoiceNo(distributeShipBean.getInvoiceNo());
        // 配送方式id
        depotShip.setShippingId(shipping.getShippingId());
        // 配送方式的名称
        depotShip.setShippingName(shipping.getShippingName());
        // 商品配送状态（0，未发货；1，已发货；2，已收货；13，客户签收；）
        depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_SHIPPED);
        // 订单配送时间
        depotShip.setDeliveryTime(distributeShipBean.getShipDate());

        // 单仓交货单
        if (StringUtil.isTrimEmpty(distributeShipBean.getDepotCode())
                || Constant.DETAILS_DEPOT_CODE.equalsIgnoreCase(distributeShipBean.getDepotCode())) {

            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);
            OrderDistribute updateDistribute = new OrderDistribute();
            // 发货总状态（0：未发货；1：备货中；2：部分发货；3：已发货；4，部分收货；5，客户已收货）
            distribute.setShipStatus(Constant.OD_SHIP_STATUS_ALLSHIPED);
            updateDistribute.setShipStatus(Constant.OD_SHIP_STATUS_ALLSHIPED);
            updateDistribute.setOrderSn(orderSn);
            updateDistribute.setUpdateTime(new Date());
            updateByPrimaryKey(updateDistribute);
        } else {
            // 更新发货状态
            String depotCode = distributeShipBean.getDepotCode();
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(depotCode).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);
            // 判断发货仓总状态并更新
            ReturnInfo<Byte> tInfo = judgeDistributeShipedStatus(orderSn);
            distribute.setShipStatus(tInfo.getData());
        }
        // 判断订单发货状态
        ReturnInfo<String> returnInfo = judgeMasterShipedStatus(distribute.getMasterOrderSn());
        if (Constant.OS_YES == returnInfo.getIsOk()) {
            masterOrderActionService.insertOrderActionBySn(distributeShipBean.getOrderSn(), "已发货", distributeShipBean.getActionUser());
        }
        // 添加发货日志
        orderAction = new DistributeAction();
        fillOrderAction(orderSn, distribute, orderAction);
        distributeActionService.saveOrderAction(orderAction);
        info.setMessage("订单[" + orderSn + "]发货成功！");
        info.setIsOk(Constant.OS_YES);
        return info;
    }

    /**
     * 交货单收货确认
     *
     * @param distributeShipBean 分配单发货信息
     * @return ReturnInfo<String>
     */
    @Override
    public ReturnInfo<String> distReceiptConfirm(DistributeShippingBean distributeShipBean) {
        // 订单，配送单，仓库编码
        logger.info("配送单收货确认 distributeShipBean:" + JSON.toJSONString(distributeShipBean));
        ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
        String orderSn = distributeShipBean.getOrderSn();
        String distSn = distributeShipBean.getShipSn();
        String actionUser = distributeShipBean.getActionUser();
        String depotCode = distributeShipBean.getDepotCode();
        OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(distSn);
        if (distribute == null) {
            logger.error("订单：" + distSn + "]不存在");
            info.setMessage("订单[" + distSn + "]不存在");
            return info;
        }
        List<OrderDepotShip> orderShipList = selectEffectiveShips(distSn);
        if (StringUtil.isListNull(orderShipList)) {
            logger.error("订单：" + distSn + "配送单配货仓数量为0终止收货确认");
            info.setMessage("订单：" + distSn + "配送单配货仓数量为0终止收货确认");
            return info;
        }
        // 单仓交货单
        if (StringUtil.isTrimEmpty(depotCode) || Constant.DETAILS_DEPOT_CODE.equalsIgnoreCase(depotCode)) {
            // 更新发货状态
            OrderDepotShip depotShip = new OrderDepotShip();
            // 商品配送状态（0，未发货；1，已发货；2，已收货；3，备货中；6,门店收货10，快递取件；11，运输中；12，派件中；13，客户签收；14，客户拒签；15，货物遗失；16，货物损毁）
            // 13，客户签收
            depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_CONFIRM);
            // 确认收货时间
            depotShip.setDeliveryConfirmTime(new Date());
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(distSn).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);

            OrderDistribute updateDistribute = new OrderDistribute();
            // 发货总状态（0：未发货；1：备货中；2：部分发货；3：已发货；4，部分收货；5，客户已收货，6门店部分收货 7门店收货）
            // 5，客户已收货
            distribute.setShipStatus(Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED);
            // 5，客户已收货
            updateDistribute.setShipStatus(Constant.OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED);
            updateDistribute.setOrderSn(distSn);
            updateDistribute.setUpdateTime(new Date());
            updateByPrimaryKey(updateDistribute);
            // 添加发货日志
            distributeActionService.addOrderAction(distSn, "已收货", actionUser);
        } else {
            // 更新发货状态
            OrderDepotShip depotShip = new OrderDepotShip();
            // 商品配送状态（0，未发货；1，已发货；2，已收货；3，备货中；6,门店收货10，快递取件；11，运输中；12，派件中；13，客户签收；14，客户拒签；15，货物遗失；16，货物损毁）
            // 13，客户签收
            depotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_CONFIRM);
            // 确认收货时间
            depotShip.setDeliveryConfirmTime(new Date());
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(distSn).andDepotCodeEqualTo(depotCode).andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);
            // 添加发货日志
            distributeActionService.addOrderAction(distSn, "配送单[" + distSn + "]已收货", actionUser);
            // 判断发货仓总状态并更新
            ReturnInfo<Byte> tInfo = judgeDistributeShipedStatus(distSn);
            distribute.setShipStatus(tInfo.getData());
        }
        ReturnInfo<String> shipInfo = judgeMasterShipedStatus(orderSn);
        // 订单收货状态判断
        if (shipInfo != null && Constant.OS_YES == shipInfo.getIsOk()) {
            masterOrderActionService.insertOrderActionBySn(orderSn, shipInfo.getData(), actionUser);
        }
        info.setMessage("订单[" + orderSn + "]收货确认成功！");
        info.setIsOk(Constant.OS_YES);
        return info;
    }

    /**
     * 收货确认(按照快递单号签收，快递单号为空时按照订单签收)
     *
     * @param bean
     * @return ReturnInfo<String>
     */
    @Override
    public ReturnInfo<String> confirmationOfReceipt(DistributeShippingBean bean) {
        logger.info("收货确认 bean:" + JSON.toJSONString(bean));
        ReturnInfo<String> ri = new ReturnInfo<String>(Constant.OS_NO);
        if (bean == null) {
            ri.setMessage("参数为空");
            return ri;
        }
        String masterOrderSn = bean.getOrderSn();
        String actionUser = bean.getActionUser();
        try {
            if (StringUtil.isTrimEmpty(masterOrderSn)) {
                ri.setMessage("订单号不能为空");
                return ri;
            }
            if (StringUtil.isTrimEmpty(actionUser)) {
                ri.setMessage("actionUser不能为空！");
                return ri;
            }
            MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
            if (master == null) {
                ri.setMessage("订单[" + masterOrderSn + "]不存在");
                return ri;
            }
            // 未发货不处理
            if (master.getShipStatus() < Constant.OI_SHIP_STATUS_PARTSHIPPED) {
                ri.setMessage("订单[" + masterOrderSn + "]未发货状态！");
                return ri;
            }
            OrderDistributeExample distributeExample = new OrderDistributeExample();
            distributeExample.or().andMasterOrderSnEqualTo(masterOrderSn).andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
            List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
            if (StringUtil.isListNull(distributes)) {
                ri.setMessage("订单[" + masterOrderSn + "]交货单数据为空！");
                return ri;
            }

            String message = "";
            // 0 默认按发货单签收
            int type = bean.getType();
            if (type == 0) {
                List<String> strings = new ArrayList<>();
                for (OrderDistribute item : distributes) {
                    strings.add(item.getOrderSn());
                }
                // 查询订单下所有已发货配货仓数据
                OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
                depotShipExample.or().andOrderSnIn(strings).andShippingStatusEqualTo((byte) Constant.OS_SHIPPING_STATUS_SHIPPED).andIsDelEqualTo(0);
                List<OrderDepotShip> depotShips = orderDepotShipMapper.selectByExample(depotShipExample);
                if (StringUtil.isListNull(depotShips)) {
                    ri.setMessage("客户收货确认！");
                    ri.setIsOk(Constant.OS_YES);
                    return ri;
                }
                // 处理订单交货单发货信息
                processOrderDepotShip(depotShips, bean);
                message = "客户收货确认！";
            } else if (type == 1) {
                // 处理通过交货单收货处理
                processOrderDistributeShip(distributes, bean);
                message = "系统发货超时自动收货确认";
            }

            for (OrderDistribute distribute : distributes) {
                // 判断交货单发货状态
                judgeDistributeShipedStatus(distribute.getOrderSn());
            }
            // 判断订单发货状态
            judgeMasterShipedStatus(masterOrderSn);
            masterOrderActionService.insertOrderActionBySn(masterOrderSn, message, actionUser);
            ri.setIsOk(Constant.OS_YES);
            ri.setMessage("收货确认更新完成！");
            processMasterShipResult(masterOrderSn, 1);
        } catch (Exception e) {
            logger.error(masterOrderSn + "收货确认更新失败:" + e.getMessage(), e);
            ri.setMessage("收货确认更新失败:" + e.toString());
        }
        return ri;
    }

    /**
     * 处理订单确认收货结果
     * @param masterOrderSn 订单编号
     * @param type 类型 0整单签收、1发货单签收
     */
    @Override
    public void processMasterShipResult(String masterOrderSn, int type) {

        MasterOrderInfo masterOrderInfo = masterOrderInfoService.getOrderInfoBySn(masterOrderSn);
        if (masterOrderInfo == null) {
            return;
        }

        int shipStatus = masterOrderInfo.getShipStatus();
        int orderStatus = masterOrderInfo.getOrderStatus();
        logger.info("processMasterShipResult:--shipStatus:" + shipStatus + ",orderStatus:" + orderStatus);
        if (orderStatus != Constant.OD_ORDER_STATUS_CANCLED && shipStatus == Constant.OI_SHIP_STATUS_ALLRECEIVED) {

            if (type == 0) {
                // 订单确认收货, 设置账期支付时间
                masterOrderInfoService.processOrderPayPeriod(masterOrderInfo);
            }
            // 下发MQ
            AccountSettlementOrderBean accountSettlementOrderBean = new AccountSettlementOrderBean();
            accountSettlementOrderBean.setOrderNo(masterOrderSn);

            logger.info("processMasterShipResult:--order_sn_account_settlement:" + JSONObject.toJSONString(accountSettlementOrderBean));
            jmsSendQueueService.sendQueueMessage(MqConfig.order_sn_account_settlement, JSON.toJSONString(accountSettlementOrderBean));

            if (Constant.DEFAULT_SHOP.equals(masterOrderInfo.getOrderFrom())) {
                //查询订单来源
                String orderSN = masterOrderInfo.getMasterOrderSn();
                String buyerSN = masterOrderInfo.getUserId();
                int totalPrice = masterOrderInfo.getMoneyPaid().intValue();
                int ratio = rewardPointRatioService.getRewardPointRatio();
                int point = 0;
                // ratio 为零时不计积分
                if (ratio != 0){
                    point = totalPrice / ratio;
                }
                if (point >= 1) {
                    //查询积分比例
                    //下发"add_reward_point_change_log"信道，添加积分变更记录
                    RewardPointChangeLogBean rewardPointChangeLogBean = new RewardPointChangeLogBean();
                    rewardPointChangeLogBean.setAccountSN(buyerSN);
//        rewardPointChangeLogBean.setCompanySN(1);
                    rewardPointChangeLogBean.setOrderSN(orderSN);
                    rewardPointChangeLogBean.setDescription("下单增加积分");
                    rewardPointChangeLogBean.setChangePoint(point);

                    String addRewardPointChangeLogMQ = JSONObject.toJSONString(rewardPointChangeLogBean);
                    logger.info("添加积分变更记录下发:" + addRewardPointChangeLogMQ);
                    try {
                        addRewardPointChangeLogJmsTemplate.send(new TextMessageCreator(addRewardPointChangeLogMQ));
                    } catch (Exception e) {
                        logger.error("下发添加积分变更记录MQ信息异常：" + e.getMessage());
                    }

                    //下发"change_user_and_company_point"信道，修改用户和公司积分
                    ChangeUserAndCompanyPointMQBean changeUserAndCompanyPointMQBean = new ChangeUserAndCompanyPointMQBean();
                    changeUserAndCompanyPointMQBean.setAccountSN(buyerSN);
//        changeUserAndCompanyPointMQBean.setCompanySN(11);
                    changeUserAndCompanyPointMQBean.setChangePoint(point);

                    String changeUserAndCompanyPointMQ = JSONObject.toJSONString(changeUserAndCompanyPointMQBean);
                    logger.info("修改用户和公司积分下发:" + changeUserAndCompanyPointMQ);
                    try {
                        changeUserAndCompanyPointJmsTemplate.send(new TextMessageCreator(changeUserAndCompanyPointMQ));
                    } catch (Exception e) {
                        logger.error("下发修改用户和公司积分MQ信息异常:" + e.getMessage());
                    }
                }
            }
        }
        //查询当前订单的所有交货单将已收货的交货单生成销售结算单推送供应链-财智云

        //确认收货后将销售结算单数据推送供应链-财智云
        try {
            OrderDistributeExample distributeExample = new OrderDistributeExample();
            distributeExample.or().andMasterOrderSnEqualTo(masterOrderInfo.getMasterOrderSn()).andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
            List<OrderDistribute> distributes = orderDistributeMapper.selectByExample(distributeExample);
            for(OrderDistribute orderDistribute:distributes){
                if(orderDistribute.getShipStatus()==Constant.OI_SHIP_STATUS_ALLRECEIVED){
                    Map map=new HashMap();
                    map.put("masterOrderSn",masterOrderInfo.getMasterOrderSn());
                    map.put("orderSn",orderDistribute.getOrderSn());
                    map.put("userId",masterOrderInfo.getUserId());
                    logger.info("下发销售结算单MQ信息:" + JSON.toJSONString(map));
                    uploadSaleSettlementJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(map)));
                }
            }
        } catch (Exception e) {
            logger.error("下发销售结算单MQ信息异常:" + e.getMessage());
        }
    }

    /**
     * 处理订单交货单配送信息
     * @param distributes 交货单列表
     * @param bean 签收信息
     */
    private void processOrderDistributeShip(List<OrderDistribute> distributes, DistributeShippingBean bean) {

        // 操作人员
        String actionUser = bean.getActionUser();
        // 周期时间
        int periodTime = bean.getPeriodTime();
        for (OrderDistribute orderDistribute : distributes) {

            // 交货单号
            String orderSn = orderDistribute.getOrderSn();
            // 查询订单下所有已发货配货仓数据
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andShippingStatusEqualTo((byte) Constant.OS_SHIPPING_STATUS_SHIPPED).andIsDelEqualTo(0);
            List<OrderDepotShip> depotShips = orderDepotShipMapper.selectByExample(depotShipExample);
            if (StringUtil.isListNull(depotShips)) {
                continue;
            }
            // 订单号
            String masterOrderSn = orderDistribute.getMasterOrderSn();
            // 处理交货单下的发货单
            List<String> invoiceNoList = new ArrayList<String>();
            for (OrderDepotShip depotShip : depotShips) {
                // 时间
                Date deliveryTime = depotShip.getDeliveryTime();
                if (deliveryTime == null) {
                    continue;
                }

                Date lastConfirmTime = TimeUtil.getBeforeSecond(deliveryTime, periodTime);
                // 是否延时收货
                Integer isReceipt = depotShip.getIsReceipt();
                if (isReceipt != null && isReceipt == 1) {
                    lastConfirmTime = TimeUtil.getBeforeSecond(deliveryTime, 2 * periodTime);
                }

                Date date = new Date();
                if (lastConfirmTime.getTime() > date.getTime()) {
                    continue;
                }

                String invoiceNo = depotShip.getInvoiceNo();
                invoiceNoList.add(invoiceNo);
                OrderDepotShip updateDepotShip = new OrderDepotShip();
                updateDepotShip.setDepotCode(depotShip.getDepotCode());
                updateDepotShip.setInvoiceNo(invoiceNo);
                updateDepotShip.setOrderSn(depotShip.getOrderSn());
                updateDepotShip.setDeliveryConfirmTime(date);
                updateDepotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_RECEIVED);
                updateDepotShip.setUpdateTime(date);
                orderDepotShipMapper.updateByPrimaryKeySelective(updateDepotShip);
                distributeActionService.addOrderAction(depotShip.getOrderSn(), "包裹[" + depotShip.getInvoiceNo() + "]系统自动收货确认", actionUser);
            }

            if (invoiceNoList != null && invoiceNoList.size() > 0) {
                // 签收后通知供应商签收
                DistributeShippingBean message = new DistributeShippingBean();
                message.setOrderSn(orderSn);
                message.setActionUser(actionUser);
                message.setInvoiceNoList(invoiceNoList);
                jmsSendQueueService.sendQueueMessage(MqConfig.supplier_order_receive_task, JSON.toJSONString(message));

                // 通知发货单签收计算扣费信息
                DistributeShippingBean orderReceiveBean = new DistributeShippingBean();
                orderReceiveBean.setOrderSn(masterOrderSn);
                orderReceiveBean.setShipSn(orderSn);
                orderReceiveBean.setInvoiceNoList(invoiceNoList);
                sendOrderReceiveSettlementInfo(orderReceiveBean);
            }
        }
    }

    /**
     * 发货单签收
     * @param message 订单发货单信息
     */
    @Override
    public void sendOrderReceiveSettlementInfo(DistributeShippingBean message) {
        jmsSendQueueService.sendQueueMessage(MqConfig.order_receive_settlement, JSON.toJSONString(message));
    }

    /**
     * 处理订单仓库配送信息
     * @param depotShips 发货单列表
     * @param bean 签收数据
     */
    private void processOrderDepotShip(List<OrderDepotShip> depotShips, DistributeShippingBean bean) {

        // 快递单号
        String invoiceNo = bean.getInvoiceNo();
        String actionUser = bean.getActionUser();
        // 订单号
        String masterOrderSn = bean.getOrderSn();
        for (OrderDepotShip depotShip : depotShips) {

            // 已经签收
            if (depotShip.getShippingStatus() == (byte) Constant.OS_SHIPPING_STATUS_RECEIVED) {
                continue;
            }

            // 快递单号不为空的情况下，匹配到的快递单号已确认收货
            //快递单号不为空的情况下，匹配到的快递单号配送状态不为已发货过滤
            if (StringUtil.isNotEmpty(invoiceNo) && !invoiceNo.equals(depotShip.getInvoiceNo())) {
                continue;
            }

            // 交货单号
            String orderSn = depotShip.getOrderSn();
            Date date = new Date();
            OrderDepotShip updateDepotShip = new OrderDepotShip();
            updateDepotShip.setDepotCode(depotShip.getDepotCode());
            updateDepotShip.setInvoiceNo(depotShip.getInvoiceNo());
            updateDepotShip.setOrderSn(depotShip.getOrderSn());
            updateDepotShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_RECEIVED);
            updateDepotShip.setDeliveryConfirmTime(date);
            updateDepotShip.setUpdateTime(date);
            orderDepotShipMapper.updateByPrimaryKeySelective(updateDepotShip);
            distributeActionService.addOrderAction(depotShip.getOrderSn(), "包裹[" + depotShip.getInvoiceNo() + "]收货确认", actionUser);
            // 签收后通知供应商签收
            DistributeShippingBean message = new DistributeShippingBean();
            message.setOrderSn(orderSn);
            message.setInvoiceNo(depotShip.getInvoiceNo());
            message.setActionUser(actionUser);
            jmsSendQueueService.sendQueueMessage(MqConfig.supplier_order_receive, JSON.toJSONString(message));

            // 通知发货单签收计算扣费信息
            DistributeShippingBean orderReceiveBean = new DistributeShippingBean();
            orderReceiveBean.setOrderSn(masterOrderSn);
            orderReceiveBean.setShipSn(orderSn);
            List<String> invoiceNoList = new ArrayList<>();
            invoiceNoList.add(depotShip.getInvoiceNo());
            orderReceiveBean.setInvoiceNoList(invoiceNoList);
            sendOrderReceiveSettlementInfo(orderReceiveBean);
        }
    }

    /**
     * 处理订单发货单金额信息
     * @param distributeShippingBean 发货单信息
     * @return ReturnInfo<String>
     */
    @Override
    public ReturnInfo<Boolean> processOrderDepotShip(DistributeShippingBean distributeShippingBean) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>(Constant.OS_NO);

        // 订单编号
        String masterOrderSn = distributeShippingBean.getOrderSn();
        if (StringUtils.isBlank(masterOrderSn)) {
            returnInfo.setMessage("订单编号为空");
            return returnInfo;
        }
        // 交货单号
        String orderSn = distributeShippingBean.getShipSn();
        if (StringUtils.isBlank(orderSn)) {
            returnInfo.setMessage("交货单号为空");
            return returnInfo;
        }
        // 快递单号
        List<String> invoiceNoList = distributeShippingBean.getInvoiceNoList();
        if (invoiceNoList == null || invoiceNoList.size() == 0) {
            returnInfo.setMessage("发货单为空");
            return returnInfo;
        }

        MasterOrderInfo masterOrderInfo = masterOrderInfoService.getOrderInfoBySn(masterOrderSn);
        if (masterOrderInfo == null) {
            returnInfo.setMessage("订单不存在");
            return returnInfo;
        }

        // 处理订单支付信息
        ReturnInfo<MasterOrderPay> masterOrderPayReturnInfo = checkOrderPayPeriod(masterOrderInfo);
        if (masterOrderPayReturnInfo.getIsOk() != Constant.OS_YES) {
            returnInfo.setMessage(masterOrderPayReturnInfo.getMessage());
            return returnInfo;
        }

        MasterOrderPay masterOrderPay = masterOrderPayReturnInfo.getData();
        if (masterOrderPay == null) {
            returnInfo.setMessage("订单支付信息为空");
            return returnInfo;
        }

        returnInfo = processOrderDepotShipMoney(distributeShippingBean, masterOrderPay);
        return returnInfo;
    }

    /**
     * 设置账期支付支付时间和扣款
     * @param masterOrderInfo 订单信息
     * @return ReturnInfo<MasterOrderPay>
     */
    private ReturnInfo<MasterOrderPay> checkOrderPayPeriod(MasterOrderInfo masterOrderInfo) {
        ReturnInfo<MasterOrderPay> returnInfo = new ReturnInfo<MasterOrderPay>();
        returnInfo.setIsOk(Constant.OS_NO);

        try {
            String masterOrderSn = masterOrderInfo.getMasterOrderSn();
            List<MasterOrderPay> masterOrderPayList = masterOrderPayService.getMasterOrderPayList(masterOrderSn);
            if (masterOrderPayList == null || masterOrderPayList.size() == 0) {
                returnInfo.setMessage("订单:" + masterOrderSn + "支付单信息不存在");
                return returnInfo;
            }

            MasterOrderPay masterOrderPay = masterOrderPayList.get(0);
            int payId = masterOrderPay.getPayId();
            if (Constant.PAYMENT_ZHANGQI_ID  != payId && Constant.PAYMENT_YINCHENG != payId) {
                returnInfo.setMessage("订单:" + masterOrderSn + "不是内行现金和内行银承支付");
                return returnInfo;
            }

            returnInfo.setIsOk(Constant.OS_YES);
            returnInfo.setMessage("成功");
            returnInfo.setData(masterOrderPay);

        } catch (Exception e) {
            logger.error("处理订单账期支付异常", e);
            returnInfo.setMessage("订单支付信息异常");
            returnInfo.setIsOk(Constant.OS_NO);
        }

        return returnInfo;
    }

    /**
     * 处理订单仓库发货单金额信息
     * @param distributeShippingBean 发货单信息
     * @param masterOrderPay 订单支付信息
     * @return ReturnInfo<Boolean>
     */
    private ReturnInfo<Boolean> processOrderDepotShipMoney(DistributeShippingBean distributeShippingBean, MasterOrderPay masterOrderPay) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>(Constant.OS_NO);

        // 交货单号
        String orderSn = distributeShippingBean.getShipSn();
        List<String> invoiceNoList = distributeShippingBean.getInvoiceNoList();

        List<Byte> shippingStatusList = new ArrayList<>();
        shippingStatusList.add((byte) Constant.OS_SHIPPING_STATUS_RECEIVED);
        shippingStatusList.add((byte) Constant.OS_SHIPPING_STATUS_CONFIRM);
        // 查询交货单下所有已签收的发货单数据
        OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
        depotShipExample.or().andOrderSnEqualTo(orderSn).andInvoiceNoIn(invoiceNoList).andPayPeriodStatusNotEqualTo(1)
                .andShippingStatusIn(shippingStatusList).andIsDelEqualTo(0);
        List<OrderDepotShip> depotShips = orderDepotShipMapper.selectByExample(depotShipExample);

        if (depotShips == null || depotShips.size() == 0) {
            returnInfo.setMessage("交货单下无签收发货单");
            return returnInfo;
        }

        // 订单编号
        String masterOrderSn = distributeShippingBean.getOrderSn();
        List<String> orderSnList = getOrderSnList(masterOrderSn);
        if (orderSnList == null || orderSnList.size() == 0) {
            returnInfo.setMessage("订单无交货单信息");
            return returnInfo;
        }

        String redisKey = "OrderDepotShip-" + masterOrderSn;
        try {
            // redis锁防止多发货单同时签收
            while (lockRedisKey(redisKey, masterOrderSn) != 1) {
                Thread.sleep(1000);
            }

            // 获取交货单下的发货单列表
            OrderDepotShipExample orderDepotShipExample = new OrderDepotShipExample();
            orderDepotShipExample.or().andOrderSnIn(orderSnList).andIsDelEqualTo(0);
            List<OrderDepotShip> orderDepotShipList = orderDepotShipMapper.selectByExample(orderDepotShipExample);

            // 发货单序号
            int maxOrderNumber = 0;
            // 是否全部已签收
            boolean last = true;
            for (OrderDepotShip orderDepotShip : orderDepotShipList) {
                Integer orderNumber = orderDepotShip.getOrderNumber();
                if (orderNumber != null && orderNumber > maxOrderNumber) {
                    maxOrderNumber = orderNumber;
                }
                if (orderDepotShip.getShippingStatus() != Constant.OS_SHIPPING_STATUS_RECEIVED && orderDepotShip.getShippingStatus() != Constant.OS_SHIPPING_STATUS_CONFIRM) {
                    last = false;
                    continue;
                }
            }

            OrderDepotShip lastOrderDepotShip = null;
            if (last) {
                lastOrderDepotShip = getLastOrderDepotShip(orderDepotShipList, masterOrderPay);
            }
            processOrderDepotShipMoney(distributeShippingBean, masterOrderPay, depotShips, maxOrderNumber, lastOrderDepotShip);
        } catch (Exception e) {
            logger.error("发货单签收处理异常:" + JSONObject.toJSONString(distributeShippingBean), e);
        } finally {
            redisClient.del(redisKey);
        }

        return returnInfo;
    }

    /**
     * 获取最后一笔发货单数据和金额
     * @param orderDepotShipList 发货单列表
     * @param masterOrderPay 订单支付单
     * @return OrderDepotShip
     */
    private OrderDepotShip getLastOrderDepotShip(List<OrderDepotShip> orderDepotShipList, MasterOrderPay masterOrderPay) {

        OrderDepotShip lastOrderDepotShip = null;
        // 订单费用
        BigDecimal payTotalFee = masterOrderPay.getPayTotalfee();
        // 发货单总金额
        BigDecimal payTotalMoney = BigDecimal.valueOf(0);

        for (OrderDepotShip orderDepotShip : orderDepotShipList) {
            payTotalMoney = payTotalMoney.add(orderDepotShip.getPayMoney());
            // 是否已扣款
            Integer payPeriodStatus = orderDepotShip.getPayPeriodStatus();
            if (payPeriodStatus != null && payPeriodStatus != 0) {
                continue;
            }
            lastOrderDepotShip = orderDepotShip;
        }

        // 订单支付金额与发货单金额差异
        BigDecimal resultMoney = payTotalFee.subtract(payTotalMoney);
        if (lastOrderDepotShip != null) {
            // 差异放到最后一个发货单上
            BigDecimal payMoney = lastOrderDepotShip.getPayMoney();
            payMoney = payMoney.add(resultMoney);

            lastOrderDepotShip.setPayMoney(payMoney);
        }

        return lastOrderDepotShip;
    }

    /**
     * 处理订单仓库发货单金额信息
     * @param distributeShippingBean 发货单信息
     * @param masterOrderPay 订单支付信息
     * @param maxOrderNumber 最大序号
     * @param lastOrderDepotShip 最后的发货单
     * @return ReturnInfo<Boolean>
     */
    private void processOrderDepotShipMoney(DistributeShippingBean distributeShippingBean, MasterOrderPay masterOrderPay, List<OrderDepotShip> depotShips, int maxOrderNumber, OrderDepotShip lastOrderDepotShip) {
        // 支付期数
        Short paymentPeriod = masterOrderPay.getPaymentPeriod();
        Date nowDate = new Date();
        Date lastPayDate = nowDate;
        if (paymentPeriod > 0) {
            lastPayDate = com.work.shop.oms.common.bean.TimeUtil.getBeforeMonth(nowDate, paymentPeriod.intValue());
        }

        int size = depotShips.size();
        for (int i = 0; i < size; i++) {
            OrderDepotShip orderDepotShip = depotShips.get(i);
            OrderDepotShip updateOrderDepotShip = new OrderDepotShip();
            updateOrderDepotShip.setOrderSn(orderDepotShip.getOrderSn());
            updateOrderDepotShip.setDepotCode(orderDepotShip.getDepotCode());
            updateOrderDepotShip.setInvoiceNo(orderDepotShip.getInvoiceNo());
            updateOrderDepotShip.setPayPeriodStatus(0);
            updateOrderDepotShip.setLastPayDate(lastPayDate);
            maxOrderNumber += 1;
            // 序号
            updateOrderDepotShip.setOrderNumber(maxOrderNumber);
            if (lastOrderDepotShip != null) {
                if (lastOrderDepotShip.getOrderSn().equals(orderDepotShip.getOrderSn())
                        && lastOrderDepotShip.getDepotCode().equalsIgnoreCase(orderDepotShip.getDepotCode())
                        && lastOrderDepotShip.getInvoiceNo().equalsIgnoreCase(orderDepotShip.getInvoiceNo())) {
                    updateOrderDepotShip.setPayMoney(lastOrderDepotShip.getPayMoney());
                }
            }
            // 金额
            orderDepotShipMapper.updateByPrimaryKeySelective(updateOrderDepotShip);
        }
    }

    private long lockRedisKey(String redisKey, String value) {
        // redis锁防止多发货单同时签收
        long result = redisClient.setnx(redisKey, value);
        return result;
    }

    /**
     * 处理仓库发货商品
     * @param goodsList 商品列表
     * @param deliveryDepotMap
     */
    private void processDepotShipGoodsMap(List<MasterOrderGoods> goodsList, Map<String, Map<String, List<MasterOrderGoods>>> deliveryDepotMap) {
        // 组装每个仓的上面明细
        for (MasterOrderGoods item : goodsList) {
            // 仓库编码
            String depotCode = item.getDepotCode();
            // 商品sku编码
            String itemCode = item.getCustomCode();
            Map<String, List<MasterOrderGoods>> deliveryItemsMap = deliveryDepotMap.get(depotCode);
            if (deliveryItemsMap == null) {
                deliveryItemsMap = new HashMap<String, List<MasterOrderGoods>>(Constant.DEFAULT_MAP_SIZE);
            }
            List<MasterOrderGoods> deliveryItems = deliveryItemsMap.get(itemCode);
            if (StringUtil.isListNull(deliveryItems)) {
                deliveryItems = new ArrayList<MasterOrderGoods>();
            }
            deliveryItems.add(item);
            deliveryItemsMap.put(itemCode, deliveryItems);
            deliveryDepotMap.put(depotCode, deliveryItemsMap);
        }
    }

    /**
     * 处理发货单已发货商品数据
     *
     * @param orderSn         交货单编码
     * @param confirmOwnerMap 仓库发货信息
     */
    private void processOrderShip(String orderSn, Map<String, DistConfirmOwner> confirmOwnerMap) {
        // 已发货信息保存
        for (String ownerCode : confirmOwnerMap.keySet()) {
            DistConfirmOwner owner = confirmOwnerMap.get(ownerCode);

            // 无需物流则不校验
//            if (!"无需物流".equals(owner.getShippingName())){
                //验证配货仓发货单 依据订单号、快递单号、发货仓
                OrderDepotShipKey verifyKey = new OrderDepotShipKey();
                verifyKey.setOrderSn(owner.getOrderSn());
                verifyKey.setInvoiceNo(owner.getInvoiceNo());
                verifyKey.setDepotCode(owner.getOwnerCode());
                OrderDepotShip orderDepotShip = orderDepotShipMapper.selectByPrimaryKey(verifyKey);
                if (orderDepotShip != null) {
                    throw new RuntimeException("该订单的此快递单已进行发货，不能重复添加");
                }
//            }

            // 更新商品所属包裹信息
            List<MasterOrderGoods> goodsItems = owner.getGoodsItems();
            if (StringUtil.isListNull(goodsItems)) {
                continue;
            }

            BigDecimal payMoney = BigDecimal.valueOf(0);
            for (MasterOrderGoods item : goodsItems) {
                MasterOrderGoods updateItem = new MasterOrderGoods();
                updateItem.setId(item.getId());
                updateItem.setInvoiceNo(owner.getInvoiceNo());
                Integer goodsNumber = item.getGoodsNumber();
                updateItem.setGoodsNumber(goodsNumber);

                BigDecimal currentGoodsMoney = MathOperation.mul(item.getSettlementPrice(), BigDecimal.valueOf(goodsNumber), 2);
                payMoney = payMoney.add(currentGoodsMoney);
                masterOrderGoodsMapper.updateByPrimaryKeySelective(updateItem);
            }

            // 更新发货仓包裹信息
            OrderDepotShip updateShip = new OrderDepotShip();
            // 交货单编码
            updateShip.setOrderSn(orderSn);
            // 快递单编码
            updateShip.setInvoiceNo(owner.getInvoiceNo());
            // 已发货
            updateShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_SHIPPED);
            // 仓库编码
            updateShip.setDepotCode(owner.getOwnerCode());
            updateShip.setShippingId(owner.getShippingId());
            updateShip.setShippingName(owner.getShippingName());
            updateShip.setDeliveryTime(owner.getDeliveryTime());
            updateShip.setDepotTime(owner.getDepotTime());
            updateShip.setOverTransCycle(owner.getOverTransCycle());
            updateShip.setToUser(owner.getToUser());
            updateShip.setToUserPhone(owner.getToUserPhone());
            updateShip.setProvincecity(owner.getProvincecity());
            updateShip.setPdwarhCode(owner.getPdwarhCode());
            updateShip.setPdwarhName(owner.getPdwarhName());
            updateShip.setIsDel(Constant.IS_DEL_NO);
            updateShip.setPayMoney(payMoney);
            OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
            depotShipExample.or().andOrderSnEqualTo(orderSn).andDepotCodeEqualTo(owner.getOwnerCode()).andInvoiceNoEqualTo("").andIsDelEqualTo(0);
            orderDepotShipMapper.updateByExampleSelective(updateShip, depotShipExample);
        }
    }

    /**
     * 获取交货单列表
     * @param masterOrderSn 订单编号
     * @return List<String>
     */
    private List<String> getOrderSnList(String masterOrderSn) {
        // 获取交货单
        List<OrderDistribute> orderDistributeList = selectEffectiveDistributes(masterOrderSn);
        if (orderDistributeList == null || orderDistributeList.size() == 0) {
            return null;
        }

        List<String> orderSnList = new ArrayList<>();
        for (OrderDistribute orderDistribute : orderDistributeList) {
            orderSnList.add(orderDistribute.getOrderSn());
        }

        return orderSnList;
    }

    /**
     * 处理未发货商品发货单
     *
     * @param orderSn          交货单编码
     * @param deliveryDepotMap 发货仓关系
     * @param depotMap         仓库信息
     */
    private void processDefaultOrderShip(String orderSn, Map<String, Map<String, List<MasterOrderGoods>>> deliveryDepotMap, Map<String, OrderDepotShip> depotMap) {
        // 剩余未发货仓明细重新创建一个新发货仓
        if (deliveryDepotMap.isEmpty()) {
            return;
        }

        for (String depotCode : deliveryDepotMap.keySet()) {
            OrderDepotShip depotShip = depotMap.get(depotCode);
            OrderDepotShipKey shipKey = new OrderDepotShipKey();
            shipKey.setDepotCode(depotCode);
            shipKey.setInvoiceNo("");
            shipKey.setOrderSn(orderSn);
            OrderDepotShip item = orderDepotShipMapper.selectByPrimaryKey(shipKey);
            if (null == item) {
                OrderDepotShip insertShip = new OrderDepotShip();
                insertShip.setOrderSn(orderSn);
                insertShip.setCreatTime(new Date());
                insertShip.setInvoiceNo("");
                insertShip.setShippingStatus((byte) Constant.OS_SHIPPING_STATUS_UNSHIPPED);
                insertShip.setDepotCode(depotCode);
                insertShip.setShippingId(depotShip.getShippingId());
                insertShip.setShippingName(depotShip.getShippingName());
                insertShip.setDeliveryType(depotShip.getDeliveryType());
                insertShip.setIsDel(Constant.IS_DEL_NO);
                insertShip.setDeliveryTime(depotShip.getDeliveryTime());
                insertShip.setDepotTime(depotShip.getDepotTime());
                insertShip.setOverTransCycle(depotShip.getOverTransCycle());
                insertShip.setToUser(depotShip.getToUser());
                insertShip.setToUserPhone(depotShip.getToUserPhone());
                insertShip.setProvincecity(depotShip.getProvincecity());
                insertShip.setPdwarhCode(depotShip.getPdwarhCode());
                insertShip.setPdwarhName(depotShip.getPdwarhName());
                orderDepotShipMapper.insertSelective(insertShip);
            }
        }

    }

    /**
     * 处理部分发货明细
     * @param orderSn
     * @param list
     */
    private void processOrderItem(String orderSn, List<MasterOrderGoods> list) {
        // 剩余未发货仓明细重新创建一个新发货仓
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        for (MasterOrderGoods item : list) {
            // 商品下标补0防止主键冲突
            item.setExtensionId(item.getExtensionId() + "0");
            item.setId(null);
            masterOrderGoodsMapper.insertSelective(item);
        }

    }

    /**
     * 获取未发货的交货单发货单仓库映射
     * @param orderSn 交货单
     * @return Map<String, OrderDepotShip>
     */
    private Map<String, OrderDepotShip> getOrderDepotShipByNotDelivered(String orderSn) {
        OrderDepotShipExample example = new OrderDepotShipExample();
        example.or().andOrderSnEqualTo(orderSn).andShippingStatusEqualTo((byte) Constant.OS_SHIPPING_STATUS_UNSHIPPED).andIsDelEqualTo(0);
        List<OrderDepotShip> depotShips = this.orderDepotShipMapper.selectByExample(example);

        // 交货单仓库与发货单
        Map<String, OrderDepotShip> depotMap = new HashMap<String, OrderDepotShip>(Constant.DEFAULT_MAP_SIZE);
        if (StringUtil.isListNotNull(depotShips)) {
            for (OrderDepotShip ship : depotShips) {
                depotMap.put(ship.getDepotCode(), ship);
            }
        }

        return depotMap;
    }

    /**
     * 处理供应商发货数据
     * @param orderSn 交货单号
     * @param orderPackages 供应商发货信息
     * @param deliveryDepotMap 仓库对应的订单商品信息
     * @param depotMap 仓库对应的发货单信息
     * @param confirmOwnerMap
     * @return ReturnInfo<List<MasterOrderGoods>>
     */
    private ReturnInfo<List<MasterOrderGoods>> processOrderDepotShipSendGoods(String orderSn, List<DistOrderPackages> orderPackages, Map<String, Map<String, List<MasterOrderGoods>>> deliveryDepotMap, Map<String, OrderDepotShip> depotMap, Map<String, DistConfirmOwner> confirmOwnerMap) {

        ReturnInfo<List<MasterOrderGoods>> response = new ReturnInfo<List<MasterOrderGoods>>();
        response.setIsOk(Constant.OS_NO);
        response.setMessage("供应商发货失败");

        // 拆分后新增商品列表
        List<MasterOrderGoods> insertItems = new ArrayList<>();
        // 校验发货数据校验发货量
        for (DistOrderPackages orderPackage : orderPackages) {
            // 物流公司编码
            String logisticsCode = orderPackage.getLogisticsCode();
            if (StringUtil.isTrimEmpty(logisticsCode)) {
                response.setMessage("参数[request.packages.logisticsCode] 不能为空");
                return response;
            }
            // 运单号
            String expressCode = orderPackage.getExpressCode();
            // 不是无需物流时 单号必填
            //if (!"NOEX".equals(logisticsCode) && StringUtil.isTrimEmpty(expressCode)) {
            if (StringUtil.isTrimEmpty(expressCode)) {
                response.setMessage("参数[request.packages.expressCode] 不能为空");
                return response;
            }
            // 发货时间
            Date deliveryTime = orderPackage.getDeliveryTime();
            if (deliveryTime == null) {
                response.setMessage("参数[request.packages.deliveryTime] 不能为空");
                return response;
            }
            // 发货明细
            List<DistOrderPackageItems> packageItems = orderPackage.getItems();
            if (StringUtil.isListNull(packageItems)) {
                response.setMessage("参数[request.packages.items] 不能为空");
                return response;
            }
            Map<String, String> ownerCodeMap = new HashMap<String, String>(Constant.DEFAULT_MAP_SIZE);
            for (DistOrderPackageItems item : packageItems) {
                // 商品编码
                String itemCode = item.getItemCode();
                if (StringUtil.isTrimEmpty(itemCode)) {
                    response.setMessage("参数[request.packages.items.itemCode] 不能为空");
                    return response;
                }
                // 仓库编码
                String ownerCode = item.getOwnerCode();
                if (StringUtil.isTrimEmpty(ownerCode)) {
                    response.setMessage("参数[request.packages.items.ownerCode] 不能为空");
                    return response;
                }
                ownerCodeMap.put(ownerCode, ownerCode);
                // 发货数量
                Integer quantity = item.getQuantity();
                if (null == quantity) {
                    response.setMessage("参数[request.packages.items.quantity] 不能为空");
                    return response;
                }
                // 根据仓库编码，获取对应的商品货号对应的列表
                Map<String, List<MasterOrderGoods>> deliveryItemsMap = deliveryDepotMap.get(ownerCode);
                if (deliveryItemsMap == null) {
                    response.setMessage("仓[" + ownerCode + "] 不存在");
                    return response;
                }
                // 根据商品货号，获取对应的商品列表
                List<MasterOrderGoods> deliveryItems = deliveryItemsMap.get(itemCode);
                if (StringUtil.isListNull(deliveryItems)) {
                    response.setMessage("仓[" + ownerCode + "] 商品为空，商品不一致");
                    return response;
                }

                // 仓库编码+运单号
                String ownerExpressKey = ownerCode + orderPackage.getExpressCode();
                DistConfirmOwner owner = confirmOwnerMap.get(ownerExpressKey);
                if (null == owner) {
                    OrderDepotShip depotShip = depotMap.get(ownerCode);
                    owner = new DistConfirmOwner();
                    owner.setDeliveryTime(orderPackage.getDeliveryTime());
                    owner.setInvoiceNo(orderPackage.getExpressCode());
                    owner.setOrderSn(orderSn);
                    owner.setOwnerCode(ownerCode);
                    Byte shippingId = (byte) -1;
                    String shippingName = "未知";
                    SystemShipping shipping = getSystemShipByShipCode(orderPackage.getLogisticsCode());
                    if (shipping != null) {
                        shippingId = shipping.getShippingId();
                        shippingName = shipping.getShippingName();
                        orderPackage.setLogisticsName(shippingName);
                    } else {
                        response.setMessage("承运商编码[" + orderPackage.getLogisticsCode() + "] 不存在");
                        return response;
                    }
                    if (depotShip != null) {
                        owner.setDepotTime(depotShip.getDepotTime());
                        owner.setOverTransCycle(depotShip.getOverTransCycle());
                        owner.setToUser(depotShip.getToUser());
                        owner.setToUserPhone(depotShip.getToUserPhone());
                        owner.setProvincecity(depotShip.getProvincecity());
                        owner.setPdwarhCode(depotShip.getPdwarhCode());
                        owner.setPdwarhName(depotShip.getPdwarhName());
                    }
                    owner.setShippingId(shippingId);
                    owner.setShippingName(shippingName);
                    owner.setDeliveryType(0);
                    owner.setPreInvoiceNo("");
                }
                List<MasterOrderGoods> deliveryGoodsItems = owner.getGoodsItems();
                if (StringUtil.isListNull(deliveryGoodsItems)) {
                    deliveryGoodsItems = new ArrayList<MasterOrderGoods>();
                }

                // oms商品数量
                // 一个sku是多件时，遍历商品
                int totalItemNum = 0;
                Iterator<MasterOrderGoods> it = deliveryItems.iterator();
                while (it.hasNext()) {
                    if (quantity == 0) {
                        break;
                    }
                    MasterOrderGoods goodsItem = it.next();
                    MasterOrderGoods shipItem = new MasterOrderGoods();
                    CachedBeanCopier.copy(goodsItem, shipItem);

                    int num = goodsItem.getGoodsNumber();
                    totalItemNum += num;
                    if (num <= quantity) {
                        it.remove();
                        quantity -= num;
                    } else {
                        int newGoodsNumber = num - quantity;
                        MasterOrderGoods insertItem = new MasterOrderGoods();
                        CachedBeanCopier.copy(goodsItem, insertItem);
                        insertItem.setGoodsNumber(newGoodsNumber);
                        insertItems.add(insertItem);

                        shipItem.setGoodsNumber(quantity);
                        quantity = 0;
                    }

                    deliveryGoodsItems.add(shipItem);
                }
                int totalQuantity = item.getQuantity();
                // 发货商品量不能大于订单商品量
                if (totalItemNum < totalQuantity) {
                    response.setMessage("仓[" + ownerCode + "] 实发货数" + totalQuantity + "大于应发数" + totalItemNum);
                    return response;
                }

                owner.setGoodsItems(deliveryGoodsItems);
                // 置空发货单号MAP
                deliveryItemsMap.put(itemCode, deliveryItems);
                // 该货主仓有未发商品为部分发货新增配货单
                if (StringUtil.isListNull(deliveryItems)) {
                    deliveryItemsMap.remove(itemCode);
                }
                if (deliveryItemsMap == null || deliveryItemsMap.isEmpty()) {
                    deliveryDepotMap.remove(ownerCode);
                } else {
                    deliveryDepotMap.put(ownerCode, deliveryItemsMap);
                }
                confirmOwnerMap.put(ownerExpressKey, owner);
            }
            orderPackage.setOwnerCodeMap(ownerCodeMap);
        }

        response.setData(insertItems);
        response.setIsOk(Constant.OS_YES);
        return response;
    }

    /**
     * 供应商发货
     * @param request 发货数据
     * @return DistOrderShipResponse
     */
    @Override
    public DistOrderShipResponse distOrderShip(DistOrderShipRequest request) {
        logger.info("供应商发货request:" + JSON.toJSONString(request));
        DistOrderShipResponse response = new DistOrderShipResponse();
        response.setSuccess(false);
        response.setMessage("供应商发货失败");
        String orderSn = null;
        try {
            if (request == null) {
                response.setMessage("参数不能为空");
                return response;
            }
            orderSn = request.getOrderSn();
            if (StringUtil.isTrimEmpty(orderSn)) {
                response.setMessage("交货单不能为空");
                return response;
            }

            List<DistOrderPackages> orderPackages = request.getPackages();
            if (StringUtil.isListNull(orderPackages)) {
                response.setMessage("参数[request.packages] 不能为空");
                return response;
            }

            OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
            if (null == distribute) {
                response.setMessage("交货单[" + orderSn + "] 不存在");
                return response;
            }
            if (checkOrderStatus(distribute)) {
                response.setMessage("交货单[" + orderSn + "] 状态不符合发货条件");
                return response;
            }
            String masterOrderSn = distribute.getMasterOrderSn();
            MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
            if (null == master) {
                response.setMessage("订单[" + masterOrderSn + "] 不存在");
                return response;
            }
            if (checkMasterStatus(master)) {
                response.setMessage("订单[" + masterOrderSn + "] 状态不符合发货条件");
                return response;
            }

            // 交货单仓库与发货单
            Map<String, OrderDepotShip> depotMap = getOrderDepotShipByNotDelivered(orderSn);

            MasterOrderGoodsExample goodsExample = new MasterOrderGoodsExample();
            goodsExample.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(Constant.IS_DEL_NO).andInvoiceNoEqualTo("");
            List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(goodsExample);
            if (StringUtil.isListNull(goodsList)) {
                response.setMessage("交货单[" + orderSn + "] 没有关联商品");
                return response;
            }

            /**
             * 1.查询未发货商品明细
             * 2.已发货明细关联oms商品
             *  2.1 整仓已发货
             *  2.2 部分发货（原仓变为已发货，重新创建一个未发货仓）
             *  2.2 不发货仓不处理
             */
            // 组装每个仓的上面明细
            Map<String, Map<String, List<MasterOrderGoods>>> deliveryDepotMap = new HashMap<String, Map<String, List<MasterOrderGoods>>>(Constant.DEFAULT_MAP_SIZE);
            processDepotShipGoodsMap(goodsList, deliveryDepotMap);

            // 仓库-快递单号
            Map<String, DistConfirmOwner> confirmOwnerMap = new HashMap<String, DistConfirmOwner>(Constant.DEFAULT_MAP_SIZE);

            ReturnInfo<List<MasterOrderGoods>> orderDepotShipData = processOrderDepotShipSendGoods(orderSn, orderPackages, deliveryDepotMap, depotMap, confirmOwnerMap);
            if (orderDepotShipData.getIsOk() != Constant.OS_YES) {
                response.setMessage(orderDepotShipData.getMessage());
                return response;
            }

            // 已发货信息保存
            processOrderShip(orderSn, confirmOwnerMap);

            // 剩余未发货仓明细重新创建一个新发货仓
            processDefaultOrderShip(orderSn, deliveryDepotMap, depotMap);

            // 剩余部分发货明细重新创建一个新明细记录
            List<MasterOrderGoods> insertItems = orderDepotShipData.getData();
            processOrderItem(orderSn, insertItems);

            // 已发货发货单推送至物流监控
            try {
                orderExpressService.orderExpress(distribute.getMasterOrderSn());
            } catch (Exception e) {
                logger.error("交货单[" + orderSn + "]已发货数据推送至物流监控异常：", e);
            }
            // 判断交货单状态和订单状态
            // 交货单发货状态
            judgeDistributeShipedStatus(orderSn);
            logger.debug("处理完毕订单" + orderSn);
            // 添加发货日志
            DistributeAction orderAction = new DistributeAction();
            orderAction.setActionUser(request.getActionUser());
            fillOrderAction(orderSn, distribute, orderAction);
            distributeActionService.saveOrderAction(orderAction);
            isSend.set(null);
            // 订单发货状态
            ReturnInfo<String> shipInfo = judgeMasterShipedStatus(distribute.getMasterOrderSn());
			/*if (shipInfo.getIsOk() == Constant.OS_YES) {
				// 回调退单接口
				callOrderReturn(distribute.getMasterOrderSn());
			}*/
            response.setSuccess(true);
            response.setMessage("发货单发货确认成功");
        } catch (Exception e) {
            logger.error(orderSn + "发货单发货确认异常：" + e.getMessage(), e);
            response.setMessage("发货单发货确认异常：" + e.getMessage());
        } finally {
            logger.info(orderSn + "发货单发货确认通知 response：" + JSON.toJSONString(response));
        }
        return response;
    }

    /**
     * 出库单表临时表记录发货数据
     *
     * @param record
     */
    public void insertTempShipped(DistributeShipBean record) {
        logger.info(record.toString());
        WkSfGdn wkSfGdn = new WkSfGdn();
        wkSfGdn.setOuterCode(record.getOrderSn());
        wkSfGdn.setCode(record.getCode());
        wkSfGdn.setBfOrgWarehId(Integer.valueOf(record.getBfOrgWarehId()));
        wkSfGdn.setDistWarehCode(record.getDistWarehCode());
        wkSfGdn.setRegionId(Short.valueOf(record.getRegionId()));
        wkSfGdn.setTtlQty(Integer.valueOf(record.getTtlQty()));
        wkSfGdn.setBfOrgTspComId(Integer.valueOf(record.getBfOrgTspComId()));
        wkSfGdn.setShipCode(record.getShipCode());
        wkSfGdn.setCsbNum(record.getCsbNum());
        wkSfGdn.setShipDate(TimeUtil.parseDayDate(record.getShipDate(), "yyyy-MM-dd HH:mm:ss"));
        wkSfGdn.setMajorSendWarehCode(record.getMajorSendWarehCode());
        wkSfGdn.setShipType(Integer.valueOf(record.getShipType()));
        wkSfGdnMapper.insert(wkSfGdn);
        WkSfGdnTmp wkSfGdnTmp = new WkSfGdnTmp();
        wkSfGdnTmp.setOuterCode(record.getOrderSn());
        wkSfGdnTmp.setCode(record.getCode());
        wkSfGdnTmp.setBfOrgWarehId(Integer.valueOf(record.getBfOrgWarehId()));
        wkSfGdnTmp.setDistWarehCode(record.getDistWarehCode());
        wkSfGdnTmp.setRegionId(Short.valueOf(record.getRegionId()));
        wkSfGdnTmp.setTtlQty(Integer.valueOf(record.getTtlQty()));
        wkSfGdnTmp.setBfOrgTspComId(Integer.valueOf(record.getBfOrgTspComId()));
        wkSfGdnTmp.setShipCode(record.getShipCode());
        wkSfGdnTmp.setCsbNum(record.getCsbNum());
        wkSfGdnTmp.setShipDate(TimeUtil.parseDayDate(record.getShipDate(), "yyyy-MM-dd HH:mm:ss"));
        wkSfGdnTmp.setMajorSendWarehCode(record.getMajorSendWarehCode());
        wkSfGdnTmp.setShipType(Integer.valueOf(record.getShipType()));
        wkSfGdnTmpMapper.insert(wkSfGdnTmp);
    }

    /**
     * 根据订单号删除出库单临时表信息
     *
     * @param orderSn 订单号
     */
    public void delShippedByOrderSn(String orderSn) {
        WkSfGdnExample sfGdnExample = new WkSfGdnExample();
        sfGdnExample.or().andOuterCodeEqualTo(orderSn);
        wkSfGdnMapper.deleteByExample(sfGdnExample);
    }

    /**
     * 判断订单发货状态
     *
     * @param orderInfo
     * @return
     */
    public boolean checkOrderShipStatus(OrderDistribute orderInfo) {
        // 订单状态：取消2、完成3
        // 订单支付单状态：已结算3
        // 订单发货状态：已收货5
        return orderInfo == null || orderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_CANCLED
                || orderInfo.getOrderStatus() == Constant.OI_ORDER_STATUS_FINISHED
                || orderInfo.getPayStatus() == Constant.OI_PAY_STATUS_SETTLED
                || orderInfo.getShipStatus() == Constant.OI_SHIP_STATUS_ALLRECEIVED;
    }

    /**
     * 填充订单发货日志
     *
     * @param orderSn     订单编码
     * @param distribute  订单交货单信息
     * @param orderAction 交货单日志
     */
    private void fillOrderAction(String orderSn, OrderDistribute distribute, DistributeAction orderAction) {
        orderAction.setOrderSn(orderSn);
        String actionUser = orderAction.getActionUser();
        if (StringUtils.isBlank(actionUser)) {
            actionUser = "system";
        }
        orderAction.setActionUser(actionUser);
        orderAction.setOrderStatus(new Byte("" + distribute.getOrderStatus()));
        orderAction.setShippingStatus(new Byte("" + distribute.getShipStatus()));
        orderAction.setPayStatus(new Byte("" + distribute.getPayStatus()));
        orderAction.setQuestionStatus(new Byte("" + distribute.getQuestionStatus()));
        orderAction.setRangeStatus((byte) -1);
        orderAction.setActionNote("已发货");
        orderAction.setLogTime(new Date());
    }

    /**
     * 填充分仓数据
     *
     * @param orderSn
     * @param distribute
     * @param orderAction
     */
    private void fillOrderActionByDistribute(String orderSn, OrderDistribute distribute, DistributeAction orderAction) {
        orderAction.setOrderSn(orderSn);
        orderAction.setActionUser("system");
        orderAction.setOrderStatus(new Byte("" + distribute.getOrderStatus()));
        orderAction.setShippingStatus(new Byte("" + distribute.getShipStatus()));
        orderAction.setPayStatus(new Byte("" + distribute.getPayStatus()));
        orderAction.setQuestionStatus(new Byte("" + distribute.getQuestionStatus()));
        orderAction.setRangeStatus((byte) -1);
        orderAction.setLogTime(new Date());
    }

    private Set<String> buildVdeCodeSet(String orderSn, List<DistributeShipBean> distributeShipBeans) {
        Set<String> vDecodeSet = new HashSet<String>();
        for (int i = 0; i < distributeShipBeans.size(); i++) {
            DistributeShipBean orderToShippedBean = distributeShipBeans.get(i);
            // 保存数据
            insertTempShipped(orderToShippedBean);
            logger.debug("订单号" + orderSn + "插入临时表完毕");
            // 实际仓
            String vDecode = orderToShippedBean.getMajorSendWarehCode();
            if (!vDecodeSet.contains(vDecode)) {
                vDecodeSet.add(vDecode);
            }
        }
        return vDecodeSet;
    }

    private Set<String> buildinvoiceNoSet(List<DistributeShipBean> distributeShipBeans) {
        Set<String> invoiceNoSet = new HashSet<String>();
        for (DistributeShipBean orderToShippedBean : distributeShipBeans) {
            String invoiceNo = orderToShippedBean.getCsbNum();
            if (!invoiceNoSet.contains(invoiceNo)) {
                invoiceNoSet.add(invoiceNo);
            }
        }
        return invoiceNoSet;
    }

    /**
     * 统计已经发货的发货单的数量
     *
     * @param orderShipList
     * @return
     */
    private int checkShipStatus(List<OrderDepotShip> orderShipList) {
        int flag_count = 0;
        Byte status_temp = 1;
        for (int i = 0; i < orderShipList.size(); i++) {
            OrderDepotShip orderShip = orderShipList.get(i);
            if (orderShip.getShippingStatus() == null || orderShip.getShippingStatus() == 0)
                continue;
            if (orderShip.getShippingStatus().equals(status_temp)) {
                flag_count++;
            }
        }
        return flag_count;
    }

    private OrderDepotShip findOrderShipByDecode(List<OrderDepotShip> orderShipList, String vDecode) {
        for (int i = 0; i < orderShipList.size(); i++) {
            OrderDepotShip orderDepotShip = orderShipList.get(i);
            if (orderDepotShip == null)
                continue;
            if (StringUtil.equals(orderDepotShip.getDepotCode(), vDecode)) {
                return orderDepotShip;
            }
        }
        return null;
    }

    /**
     * 根据虚拟仓code获取第一个发货数据
     *
     * @param shipProviderList
     * @param vDecode
     * @return
     */
    private DistributeShipBean getOrderToShippedProviderBean(List<DistributeShipBean> shipProviderList, String vDecode) {
        for (int i = 0; i < shipProviderList.size(); i++) {
            DistributeShipBean bean = shipProviderList.get(i);
            if (bean == null)
                continue;
            if (StringUtil.equals(bean.getMajorSendWarehCode(), vDecode)) {
                return bean;
            }
        }
        return null;
    }

    private void fillOrderShip(OrderDepotShip orderShip, DistributeShipBean shippedBean) {
        Byte shippingId = (byte) -1;
        String shippingName = "未知";
        SystemShipping shipping = systemShippingService.getSystemShipByShipCode(shippedBean.getShipCode());
        if (shipping != null) {
            shippingId = shipping.getShippingId();
            shippingName = shipping.getShippingName();
        }
        orderShip.setShippingId(shippingId);
        orderShip.setShippingName(shippingName);
        orderShip.setInvoiceNo(shippedBean.getCsbNum());
        orderShip.setDeliveryType(shippedBean.getShipType());
        if (StringUtil.isEmpty(shippedBean.getShipDate())) {
            orderShip.setDeliveryTime(new Date());
        } else {
            try {
                orderShip.setDeliveryTime(FieldUtil.obj2Date(shippedBean.getShipDate()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新发货单数据
     * @param depotShip
     * @return int
     */
    public int updateByPrimaryKeySelective(OrderDepotShip depotShip) {
        OrderDepotShipExample depotShipExample = new OrderDepotShipExample();
        depotShipExample.or().andOrderSnEqualTo(depotShip.getOrderSn())
                .andDepotCodeEqualTo(depotShip.getDepotCode());
        return orderDepotShipMapper.updateByExampleSelective(depotShip, depotShipExample);
    }

    /**
     * 根据发货单号获取第一个发货数据
     *
     * @param shipProviderList
     * @param invoiceNo
     * @return DistributeShipBean
     */
    private DistributeShipBean getInvoiceNoShippedProviderBean(List<DistributeShipBean> shipProviderList, String invoiceNo) {
        for (int i = 0; i < shipProviderList.size(); i++) {
            DistributeShipBean bean = shipProviderList.get(i);
            if (bean == null) {
                continue;
            }
            if (StringUtil.equals(bean.getCsbNum(), invoiceNo)) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 获取短信模板
     *
     * @param orderFrom
     * @param type      1:分仓信息BG-217,MB-217 2，分仓发货信息BG-201,MB-201,3:全流通收货提醒短信220
     * @return
     */
    private SystemMsgTemplate getSystemMsgTemplate(String orderFrom, int type) {
        String templateCode = getSmsTemplateCode(orderFrom, type);
        SystemMsgTemplate smt = systemMsgTemplateMapper.selectByPrimaryKey(templateCode);
        return smt;
    }

    /**
     * 获取短信模板CODE
     *
     * @param orderFrom
     * @param type      1:分仓信息BG-217,MB-217 2，分仓发货信息BG-201,MB-201,3:全流通收货提醒短信220
     * @return
     */
    private String getSmsTemplateCode(String orderFrom, int type) {
        String templateCode = "";
        if (type == 1) {
            templateCode = "217";
        } else if (type == 2) {
            templateCode = "201";
        } else if (type == 3) {
            templateCode = "220";
        }
        return templateCode;
    }

    private String getUserMobile(OrderDistribute distribute) {
        String mobileUsers = "";
        MasterOrderAddressInfoExample example = new MasterOrderAddressInfoExample();
        example.or().andMasterOrderSnEqualTo(distribute.getMasterOrderSn());
        List<MasterOrderAddressInfo> addressInfoList = masterOrderAddressInfoMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(addressInfoList)) {
            logger.error("订单号" + distribute.getOrderSn() + "获取收货人信息为空！");
            return null;
        }
        String mobile = addressInfoList.get(0).getMobile();
        if (mobile != null && !"".equalsIgnoreCase(mobile)) {
            mobileUsers = mobile;
        } else {
            MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(distribute.getMasterOrderSn());
            if (master == null) {
                return null;
            }
            UserUsers uu = UserInfoService.getUserInfo(master.getUserId() + "");
            if (uu == null || uu.getMobile() == null || "".equalsIgnoreCase(uu.getMobile())) {
                return null;
            }
            mobileUsers = uu.getMobile();
        }
        return mobileUsers;
    }

    public void saveSysMsg(int msgType, String msgName, String msgContent, String targetIds, String msgDesc, String channelCode, int sendType, int sendOrder) {
        if (!isSend.get()) {
            logger.debug("不用发送短信");
            return;
        }
        try {
            logger.debug("this_send_mobile:|" + targetIds + "|");
            User u = new User();
            u.setUsername(ConfigCenter.getProperty("smsUser"));
            u.setPassword(ConfigCenter.getProperty("smsPwd"));
            Message msg = new Message();
            msg.setPhoneNO(targetIds.trim());
            msg.setChannelCode(channelCode);
            msg.setSendType("mb_" + String.valueOf(sendType));
            msg.setMsgContent(msgContent);
            State resultState = sMSService.send(u, msg);
            logger.debug("[SystemMessageQueueWDaoImpl.saveSysMsg].data=>phone:" + targetIds.trim() + ",user:" + JSON.toJSONString(u) + ",msg:" + JSON.toJSONString(msg));
            if (!State.SUCCESSFULLY.equals(resultState.getState())) {
                logger.debug(resultState.getPhoneNo() + "短信发送失败,原因:" + resultState.getMessage());
                logger.debug("[SystemMessageQueueWDaoImpl.saveSysMsg].failed! phone:" + resultState.getPhoneNo() + ",user:" + JSON.toJSONString(u) + ",msg:" + JSON.toJSONString(msg) + ",exception:" + resultState.getMessage());
            } else {
                logger.debug("[SystemMessageQueueWDaoImpl.saveSysMsg].success! phone:" + resultState.getPhoneNo() + ",user:" + JSON.toJSONString(u) + ",msg:" + JSON.toJSONString(msg));
            }
        } catch (Exception e) {
            logger.error("短信发送失败:", e);
        }
    }

    private void callOrderReturn(final String masterOrderSn) {
        try {
            MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
            if (master == null) {
                throw new RuntimeException("订单数据不存在");
            }
            // TODO 赠送积分
            if (master.getTotalPayable().doubleValue() < 0
                    && master.getShipStatus().intValue() == Constant.OI_SHIP_STATUS_ALLSHIPED
                    && (master.getOrderType().intValue() == Constant.OI_ORDER_TYPE_NORMAL_ORDER
                    || master.getOrderType().intValue() == Constant.OI_ORDER_TYPE_EXCHANGE_ORDER)) {
                //正常订单|换货单 全发货 - 应付款小于零
                logger.debug("callOrderReturn.orderSn:" + masterOrderSn + " 退单创建中,totalPayable:" + master.getTotalPayable().doubleValue()
                        + ",shipStatus:" + master.getShipStatus().intValue() + ",orderType:" + master.getOrderType().intValue());
                OrderStatus orderStatus = new OrderStatus("订单发货生成退单", Constant.OS_STRING_SYSTEM);
                orderStatus.setType(ConstantValues.CREATE_RETURN.YES);
                ReturnInfo info = orderCancelService.createReturnOrder(masterOrderSn, orderStatus, 1);
                if (info.getIsOk() == Constant.OS_NO) {
                    logger.error("订单[" + masterOrderSn + "]发货创建退单失败：" + info.getMessage());
                }
            } else {
                logger.debug("callOrderReturn.orderSn:" + masterOrderSn + " 退单创建条件不足");
            }
        } catch (Exception e) {
            logger.error("callOrderReturn.orderSn:" + masterOrderSn + ",error:" + e.getMessage(), e);
        }
    }

    /**
     * 保存gndinfo信息
     *
     * @param orderSn
     * @param distributeShipBeans
     */
    private void saveGdnInfo(String orderSn, List<DistributeShipBean> distributeShipBeans) {
        for (int i = 0; i < distributeShipBeans.size(); i++) {
            DistributeShipBean tempBean = distributeShipBeans.get(i);
            WkSfGdnInfoExample sfGdnInfoExample = new WkSfGdnInfoExample();
            sfGdnInfoExample.or().andOrderSnEqualTo(orderSn).andSkuEqualTo(tempBean.getCustomCode()).
                    andInvoiceNoEqualTo(tempBean.getCsbNum()).andDepotEqualTo(tempBean.getMajorSendWarehCode());
            int count = wkSfGdnInfoMapper.countByExample(sfGdnInfoExample);
            logger.debug("订单商品发货信息" + orderSn + "," + tempBean.getCustomCode() + "," + tempBean.getCsbNum() +
                    "," + tempBean.getMajorSendWarehCode() + "数量：" + count);
            if (count > 0) {
                logger.debug("订单商品发货信息已经存在" + orderSn + "," + tempBean.getCustomCode() + "," + tempBean.getCsbNum()
                        + "," + tempBean.getMajorSendWarehCode());
                continue;
            }
            WkSfGdnInfo wkSfGdnInfo = new WkSfGdnInfo();
            wkSfGdnInfo.setAddTime(new Date());
//			wkSfGdnInfo.setCom(tempBean.getShipCode());
            wkSfGdnInfo.setDepot(tempBean.getMajorSendWarehCode());
            wkSfGdnInfo.setGoodsDepot(tempBean.getMajorSendWarehCode());
            wkSfGdnInfo.setInvoiceNo(tempBean.getCsbNum());
            wkSfGdnInfo.setOrderSn(orderSn);
//			wkGdninfo.setProdNum(tempBean.getTtlQty());
            wkSfGdnInfo.setSku(tempBean.getCustomCode());
            wkSfGdnInfoMapper.insert(wkSfGdnInfo);
        }
    }

    /**
     * 将数据放入队列中
     *
     * @param jms
     * @param data
     */
    private void sendMessage(JmsTemplate jms, final String data) {
        jms.send(new MessageCreator() {
            @Override
            public javax.jms.Message createMessage(Session paramSession) throws JMSException {
                TextMessage message = paramSession.createTextMessage();
                message.setText(data);
                logger.info("OrderDelivery-put message to delivery Queue:" + data);
                return message;
            }
        });
    }

    private ChannelShop getChannelInfoByOrderForm(OrderDistribute orderInfo) {
        ChannelShopExample example = new ChannelShopExample();
        ChannelShopExample.Criteria criteria1 = example.createCriteria();
        criteria1.andChannelCodeEqualTo((orderInfo.getOrderFrom()));
        List<ChannelShop> channelInfoList = channelShopMapper.selectByExample(example);
        if (channelInfoList == null || channelInfoList.isEmpty()) {
            return null;
        }
        ChannelShop shop = channelInfoList.get(0);
        return shop;
    }

    public int updateByPrimaryKey(OrderDistribute ose) {
        return orderDistributeMapper.updateByPrimaryKeySelective(ose);
    }

    /**
     * 发货短信
     *
     * @param orderFrom 渠道（订单来源）
     */
    private void fahuoSMS(OrderDistribute distribute, String orderFrom, String shippingCompany, String shippingNo) {
        if (orderFrom == null || orderFrom.equals("")) {
            return;
        }
        // 2012/05/22 add 发货短信区分平台和非平台（客服热线不同）
        SystemMsgTemplate smt = getSystemMsgTemplate(orderFrom, 2);
        String uuMobile = getUserMobile(distribute);
        try {
            if (smt == null) {
                return;
            }
            if (StringUtil.isNotEmpty(uuMobile)) {
                try {
                    saveSysMsg(2, "发货成功", smt.getTemplate().replace("{$shipping_company}", shippingCompany).replace("{$shipping_no}", shippingNo), uuMobile, "", orderFrom, 119, 2);
                } catch (Exception e) {
                    logger.error("shiped send message make error:" + shippingNo + " " + uuMobile, e);
                }
            }
        } catch (Exception e) {
            logger.error("调用MSG处理失败" + e.getMessage() + ";orderForm=" + orderFrom);
        }
    }

    /**
     * 查询有效交货单列表
     *
     * @param masterOrderSn 订单编码
     * @return List<OrderDistribute>
     */
    private List<OrderDistribute> selectEffectiveDistributes(String masterOrderSn) {
        if (StringUtil.isTrimEmpty(masterOrderSn)) {
            return null;
        }
        OrderDistributeExample example = new OrderDistributeExample();
        example.or().andMasterOrderSnEqualTo(masterOrderSn).andOrderStatusNotEqualTo((byte) Constant.OI_ORDER_STATUS_CANCLED);
        List<OrderDistribute> distributes = this.orderDistributeMapper.selectByExample(example);
        return distributes;
    }

    /**
     * 根据交货单编码查询发货单列表
     *
     * @param orderSn 交货单编码
     * @return List<OrderDepotShip>
     */
    private List<OrderDepotShip> selectEffectiveShips(String orderSn) {
        if (StringUtil.isTrimEmpty(orderSn)) {
            return null;
        }
        OrderDepotShipExample example = new OrderDepotShipExample();
        example.or().andOrderSnEqualTo(orderSn).andIsDelEqualTo(0);
        List<OrderDepotShip> depotShips = this.orderDepotShipMapper.selectByExample(example);
        return depotShips;
    }

    private void callbackChannel(String orderSn, List<String> depotCodes, OrderDistribute orderInfo, String scode, String invoiceNo) {
        logger.debug("callbackChannel.begin.orderSn:" + orderSn);
        if (orderInfo.getOrderType() != 0) {
            logger.debug("订单" + orderSn + "不是正常单,ordertype = " + orderInfo.getOrderType());
            return;
        }
        ChannelDeliveryQueue deliveryQueue = new ChannelDeliveryQueue();
        deliveryQueue.setOrderId(orderInfo.getOrderOutSn());
        deliveryQueue.setChannelCode(orderInfo.getOrderFrom());
        deliveryQueue.setShipSn(StringUtil.join(depotCodes, ","));//使用同一字段传递分仓
        deliveryQueue.setCompanyCode(scode);
        deliveryQueue.setCompanySid(invoiceNo);
        logger.debug("callbackChannel.procccess.orderSn:" + orderSn);
        int splitFlag = 0;
        StringBuilder skuList = new StringBuilder();
        // 只传递当前的传递发货单下的商品
        String tempShipSn = "";
        MasterOrderGoodsExample orderGoodsExample = new MasterOrderGoodsExample();
        orderGoodsExample.or().andOrderSnEqualTo(orderSn).andDepotCodeIn(depotCodes).andIsDelEqualTo(0);
        List<MasterOrderGoods> ogList = masterOrderGoodsMapper.selectByExample(orderGoodsExample);
        if (ogList != null && ogList.size() > 0) {
            MasterOrderGoodsExample oge = new MasterOrderGoodsExample();
            oge.or().andMasterOrderSnEqualTo(orderInfo.getMasterOrderSn()).andIsDelEqualTo(0);
            List<MasterOrderGoods> allOrderGoods = masterOrderGoodsMapper.selectByExample(oge);
            for (int i = 0; i < allOrderGoods.size(); i++) {
                MasterOrderGoods og = allOrderGoods.get(i);
                if (!StringUtil.equals(og.getDepotCode(), tempShipSn) && !StringUtil.isEmpty(tempShipSn)) {
                    splitFlag = 1;
                    break;
                } else if (StringUtil.isEmpty(tempShipSn)) {
                    tempShipSn = og.getDepotCode();
                }
            }
            Map<String, String> sendSkuMap = new HashMap<String, String>();
            for (int kk = 0; kk < ogList.size(); kk++) {
                MasterOrderGoods orderGoods = ogList.get(kk);
                String customCode = orderGoods.getCustomCode();
                if (StringUtil.isBlank(orderGoods.getCustomCode())) {
                    logger.error("callbackChannel.procccess.orderSn:" + orderSn + "，商品编码为空无法同步第三方发货数据");
                    return;
                }
                String sendsku = sendSkuMap.get(customCode);
                if (!"".equals(customCode) && StringUtil.isTrimEmpty(sendsku)) {
                    sendSkuMap.put(customCode, customCode);
                    if (kk == 0)
                        skuList.append(customCode);
                    else
                        skuList.append(",").append(customCode);
                }
            }
        }
        if (skuList.length() < 1) {
            logger.debug("callbackChannel.procccess.orderSn:" + orderSn + ",skuList is empty.");
            return;
        }
        deliveryQueue.setSplitFlag(splitFlag);
        deliveryQueue.setOuterSkuList(skuList.toString());
        try {
            //排除外部交易号为空或者外部交易号和订单号相等错误情况排除
            if (StringUtil.isNotBlank(orderInfo.getOrderOutSn()) && !StringUtil.equalsIgnoreCase(orderInfo.getOrderSn(), orderInfo.getOrderOutSn())) {
                logger.debug("【回写各个网站】----------发货队列数据推送------订单号" + orderSn + "关联的发货单号" + invoiceNo + ",deliveryQueue=" + JSON.toJSONString(deliveryQueue));
//				sendMessage(orderDeliveryJmsTemplate, JSON.toJSONString(deliveryQueue));
                orderDeliveryJmsTemplate.send(new TextMessageCreator(JSON.toJSONString(deliveryQueue)));
            } else {
                logger.debug("【回写各个网站】----------------订单号" + orderSn + "，外部交易号" + orderInfo.getOrderOutSn() + "，调用失败，由于订单外部交易号为空或者订单号与外部交易号相同。");
            }
        } catch (Exception e) {
            logger.error("callbackChannel..orderSn:" + orderSn + e.getMessage(), e);
        }
    }

    /**
     * 判断订单状态时候可以进行发货
     *
     * @param master
     * @return 订单处于取消状态或者订单处于无效状态的时候不需要再进行分仓处理
     */
    private boolean checkMasterStatus(MasterOrderInfo master) {
        if (master == null) {
            return true;
        }
        // 订单不是已付款状态
        if (!OrderStatusUtils.isPayed(master.getTransType().shortValue(), master.getPayStatus())) {
            return true;
        }
        // 订单处于取消状态
        return Constant.OD_ORDER_STATUS_CANCLED.equals(master.getOrderStatus());
    }

    /**
     * 判断订单状态时候可以进行进行发货
     * @param distribute 交货单信息
     * @return 订单处于取消状态或者订单处于无效状态的时候不需要再进行分仓处理
     */
    private boolean checkOrderStatus(OrderDistribute distribute) {
        // 订单处于未确认状态
        if (Constant.OI_ORDER_STATUS_UNCONFIRMED == distribute.getOrderStatus().intValue()) {
            return true;
        }
        // 订单处于取消状态
        if (Constant.OI_ORDER_STATUS_CANCLED == distribute.getOrderStatus().intValue()) {
            return true;
        }
        // 订单处于问题单状态
        if (Constant.OI_QUESTION_STATUS_QUESTION == distribute.getQuestionStatus().intValue()) {
            return true;
        }
        return false;
    }

    private SystemShipping getSystemShipByShipCode(String shippingCode) {
        if (StringUtil.isNotBlank(shippingCode)) {
            SystemShippingExample systemShipExa = new SystemShippingExample();
            systemShipExa.or().andShippingCodeEqualTo(shippingCode);
            List<SystemShipping> resultList = systemShippingMapper.selectByExample(systemShipExa);
            if (resultList != null && resultList.size() > 0) {
                return resultList.get(0);
            }
        }
        return null;
    }

    /**
     * 更新发货单内行扣款
     * @param orderSn 交货单号
     * @param invoiceNo 快递单号
     * @param paySourceId 内行扣款业务id
     * @param payStatus 状态
     * @return boolean
     */
    @Override
    public boolean updateOrderDepotShipPayPeriod(String orderSn, String invoiceNo, String paySourceId, boolean payStatus) {
        boolean bl = false;
        try {
            OrderDepotShipExample extendExample = new OrderDepotShipExample();
            extendExample.or().andOrderSnEqualTo(orderSn).andInvoiceNoEqualTo(invoiceNo);

            OrderDepotShip orderDepotShip = new OrderDepotShip();
            orderDepotShip.setOrderSn(orderSn);
            orderDepotShip.setInvoiceNo(invoiceNo);
            if (payStatus) {
                orderDepotShip.setPaySourceId(paySourceId);
                orderDepotShip.setPayPeriodStatus(1);
            } else {
                orderDepotShip.setPayPeriodStatus(2);
            }

            int result = orderDepotShipMapper.updateByExampleSelective(orderDepotShip, extendExample);
            bl = result > 0;
        } catch (Exception e) {
            logger.error("设置账期支付已扣款状态异常:" + orderSn + "," + invoiceNo, e);
        }
        return bl;
    }

    public static void main(String[] args) {
        DistributeShipServiceImpl distributeShipService = new DistributeShipServiceImpl();
        MasterOrderPay masterOrderPay = new MasterOrderPay();
        masterOrderPay.setMasterOrderSn("2006131512026035");
        masterOrderPay.setPayTotalfee(BigDecimal.valueOf(1533.36));

        List<OrderDepotShip> list = new ArrayList<>();
        OrderDepotShip orderDepotShip = new OrderDepotShip();
        orderDepotShip.setOrderSn("2006131512026035S01");
        orderDepotShip.setInvoiceNo("eeqqq1111");
        orderDepotShip.setOrderNumber(1);
        orderDepotShip.setShippingStatus((byte)2);
        orderDepotShip.setPayMoney(BigDecimal.valueOf(266.68));
        list.add(orderDepotShip);

        orderDepotShip = new OrderDepotShip();
        orderDepotShip.setOrderSn("2006131512026035S01");
        orderDepotShip.setInvoiceNo("eeqqq11112222");
        orderDepotShip.setOrderNumber(1);
        orderDepotShip.setShippingStatus((byte)2);
        orderDepotShip.setPayMoney(BigDecimal.valueOf(133.34));
        list.add(orderDepotShip);

        orderDepotShip = new OrderDepotShip();
        orderDepotShip.setOrderSn("2006131512026035S01");
        orderDepotShip.setInvoiceNo("yuancheng1112222");
        orderDepotShip.setShippingStatus((byte)2);
        orderDepotShip.setOrderNumber(1);
        orderDepotShip.setPayMoney(BigDecimal.valueOf(133.34));
        list.add(orderDepotShip);

        OrderDepotShip lastOrderDepotShip = distributeShipService.getLastOrderDepotShip(list, masterOrderPay);

        System.out.println(JSONObject.toJSONString(lastOrderDepotShip));
    }
}
