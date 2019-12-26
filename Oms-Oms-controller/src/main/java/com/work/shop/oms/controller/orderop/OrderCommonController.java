package com.work.shop.oms.controller.orderop;

import javax.annotation.Resource;

import com.work.shop.oms.api.orderinfo.service.BGOrderInfoService;
import com.work.shop.oms.bean.ChangeUserAndCompanyPointMQBean;
import com.work.shop.oms.bean.RewardPointChangeLogBean;
import com.work.shop.oms.controller.service.RewardPointRatioService;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.utils.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.common.bean.ConsigneeModifyInfo;
import com.work.shop.oms.common.bean.DistributeShippingBean;
import com.work.shop.oms.common.bean.OrderStatus;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.orderop.feign.bean.OrderStockQuestion;
import com.work.shop.oms.orderop.service.OrderCommonService;
import com.work.shop.oms.ship.request.DistOrderShipRequest;
import com.work.shop.oms.ship.response.DistOrderShipResponse;
import com.work.shop.oms.ship.service.DistributeShipService;

import java.math.BigDecimal;
import java.util.HashMap;

import static sun.security.krb5.Confounder.intValue;

/**
 * 订单服务操作
 *
 * @author QuYachu
 */
@RestController
@RequestMapping("/order")
public class OrderCommonController {

    private static final Logger logger = Logger.getLogger(OrderCommonController.class);

    @Resource
    private OrderCommonService orderCommonService;

    @Resource
    private DistributeShipService distributeShipService;

    @Autowired
    private RewardPointRatioService rewardPointRatioService;

    @Autowired
    private BGOrderInfoService bgOrderInfoService;

    @Resource(name = "changeUserAndCompanyPointJmsTemplate")
    private JmsTemplate changeUserAndCompanyPointJmsTemplate;

    @Resource(name = "addRewardPointChangeLogJmsTemplate")
    private JmsTemplate addRewardPointChangeLogJmsTemplate;


    /**
     * 主订单取消(共用方法)
     *
     * @param masterOrderSn 主订单号
     * @param orderStatus   code:取消原因code;note:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
     * @return ReturnInfo
     */
    @PostMapping("/cancelOrderByMasterSn")
    public ReturnInfo<Boolean> cancelOrderByMasterSn(@RequestParam(name = "masterOrderSn") String masterOrderSn, @RequestBody OrderStatus orderStatus) {

        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
        returnInfo.setMessage("取消失败");

        try {
            returnInfo = orderCommonService.cancelOrderByMasterSn(masterOrderSn, orderStatus);
        } catch (Exception e) {
            logger.error("取消订单:" + masterOrderSn + ",orderStatus:" + JSONObject.toJSONString(orderStatus) + "异常", e);
            returnInfo.setMessage("取消订单异常");
        }
        return returnInfo;
    }

    /**
     * 子订单取消(共用方法)
     *
     * @param orderStatus code:取消原因code;message:备注;actionUser:操作人; POS:POS端;FRONT:前端;OS:后台取消;type:是否创建退单 1：创建;2：不创建
     * @return ReturnInfo<Boolean>
     */
    @PostMapping("/cancelOrderByOrderSn")
    public ReturnInfo<Boolean> cancelOrderByOrderSn(@RequestBody OrderStatus orderStatus) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
        returnInfo.setMessage("取消失败");

        try {
            returnInfo = orderCommonService.cancelOrderByOrderSn(orderStatus);
        } catch (Exception e) {
            logger.error("取消订单异常orderStatus:" + JSONObject.toJSONString(orderStatus) + "异常", e);
            returnInfo.setMessage("取消订单异常");
        }
        return returnInfo;
    }

    /**
     * 设置主订单问题单
     *
     * @param orderSn     订单编码
     * @param orderStatus adminUser:操作人;message:备注;code:问题单CODE;switchFlag:库存占用释放开关（true:开;false:关）
     * @return ReturnInfo
     */
    @PostMapping("/questionOrderByOrderSn")
    public ReturnInfo<Boolean> questionOrderByOrderSn(@RequestParam(name = "orderSn") String orderSn, @RequestBody OrderStatus orderStatus) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
        returnInfo.setMessage("设置失败");

        try {
            returnInfo = orderCommonService.questionOrderByOrderSn(orderSn, orderStatus);
        } catch (Exception e) {
            logger.error("设置问题单异常:orderSn:" + orderSn + JSONObject.toJSONString(orderStatus), e);
            returnInfo.setMessage("设置问题单异常");
        }

        return returnInfo;
    }

    /**
     * 设置缺货问题单（短拣、短配、缺货问题单等）
     *
     * @param orderStockQuestion 缺货信息
     * @return ReturnInfo
     */
    @PostMapping("/addLackSkuQuestion")
    public ReturnInfo<Boolean> addLackSkuQuestion(@RequestBody OrderStockQuestion orderStockQuestion) {

        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
        returnInfo.setMessage("设置失败");
        try {
            returnInfo = orderCommonService.addLackSkuQuestion(orderStockQuestion.getOrderSn(), orderStockQuestion.getLackSkuParams(), orderStockQuestion.getOrderStatus());
        } catch (Exception e) {
            logger.error("设置缺货问题单异常:" + JSONObject.toJSONString(orderStockQuestion), e);
            returnInfo.setMessage("设置缺货异常");
        }

        return returnInfo;
    }

    /**
     * 第三方供应商发货前向OMS确认是否可以发货
     *
     * @param request
     * @return DistOrderShipResponse
     */
    @PostMapping("/shippedConfirm")
    public DistOrderShipResponse shippedConfirm(@RequestBody DistOrderShipRequest request) {
        DistOrderShipResponse response = new DistOrderShipResponse();
        response.setSuccess(false);

        try {
            response = orderCommonService.shippedConfirm(request);
        } catch (Exception e) {
            logger.error("确认是否可以发货异常 request：" + JSONObject.toJSONString(request), e);
            response.setMessage("确认是否可以发货异常");
        }

        return response;
    }

    /**
     * 订单锁定
     *
     * @param orderStatus 订单状态信息
     * @return ReturnInfo<String>
     */
    @PostMapping("/lockOrder")
    public ReturnInfo<String> lockOrder(@RequestBody OrderStatus orderStatus) {
        ReturnInfo<String> returnInfo = new ReturnInfo<String>();
        returnInfo.setMessage("失败");

        try {
            returnInfo = orderCommonService.lockOrder(orderStatus);
        } catch (Exception e) {
            logger.error("锁定异常:" + JSONObject.toJSONString(orderStatus), e);
            returnInfo.setMessage("锁定异常");
        }

        return returnInfo;
    }

    /**
     * 订单解锁
     *
     * @param orderStatus 订单状态信息
     * @return ReturnInfo<String>
     */
    @PostMapping("/unLockOrder")
    public ReturnInfo<String> unLockOrder(@RequestBody OrderStatus orderStatus) {
        ReturnInfo<String> returnInfo = new ReturnInfo<String>();
        returnInfo.setMessage("失败");

        try {
            returnInfo = orderCommonService.unLockOrder(orderStatus);
        } catch (Exception e) {
            logger.error("解锁异常:" + JSONObject.toJSONString(orderStatus), e);
            returnInfo.setMessage("解锁异常");
        }

        return returnInfo;
    }

    /**
     * 订单自提核销、订单配送签收
     *
     * @param distributeShipBean 订单配送信息
     * @return ReturnInfo<String>
     */
    @PostMapping("/cacWriteOff")
    public ReturnInfo<String> cacWriteOff(@RequestBody DistributeShippingBean distributeShipBean) {
        ReturnInfo<String> returnInfo = new ReturnInfo<String>();

        try {
            returnInfo = orderCommonService.cacWriteOff(distributeShipBean);
        } catch (Exception e) {
            logger.error("订单自提核销异常:distributeShipBean=" + JSONObject.toJSONString(distributeShipBean), e);
            returnInfo.setMessage("订单自提核销异常");
        }

        return returnInfo;
    }

    /**
     * 订单确认收货
     *
     * @param masterOrderSn 订单编码
     * @param actionUser    操作人
     * @return ReturnInfo<Boolean>
     */
    @PostMapping("/confirmReceipt")
    public ReturnInfo<Boolean> confirmReceipt(@RequestParam(name = "masterOrderSn") String masterOrderSn, @RequestParam(name = "actionUser", required = false) String actionUser) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();

        try {
            returnInfo = orderCommonService.confirmReceipt(masterOrderSn, actionUser);
        } catch (Exception e) {
            logger.error("订单系统确认收货异常:masterOrderSn=" + masterOrderSn, e);
            returnInfo.setMessage("订单系统确认收货异常");
        }

        return returnInfo;
    }

    /**
     * 处理仓库订单交货单发货
     *
     * @param distributeShipBean 订单交货单信息
     * @return ReturnInfo<String>
     */
    @PostMapping("/processShip")
    public ReturnInfo<String> processShip(@RequestBody DistributeShippingBean distributeShipBean) {
        ReturnInfo<String> returnInfo = new ReturnInfo<String>();

        try {
            returnInfo = orderCommonService.processShip(distributeShipBean);
        } catch (Exception e) {
            logger.error("仓库订单交货单发货异常:" + JSONObject.toJSONString(distributeShipBean), e);
            returnInfo.setMessage("仓库订单交货单发货异常");
        }

        return returnInfo;
    }

    /**
     * 分配单发货确认
     *
     * @param distributeShipBean 订单交货单信息
     * @return ReturnInfo<String>
     */
    @PostMapping("/distOrderConfirm")
    @Deprecated
    public ReturnInfo<String> distOrderConfirm(@RequestBody DistributeShippingBean distributeShipBean) {
        ReturnInfo<String> returnInfo = new ReturnInfo<String>();

        try {
            returnInfo = orderCommonService.distOrderConfirm(distributeShipBean);
        } catch (Exception e) {
            logger.error("分配单发货确认异常:" + JSONObject.toJSONString(distributeShipBean), e);
            returnInfo.setMessage("分配单发货确认异常");
        }

        return returnInfo;
    }

    /**
     * 编辑承运商
     *
     * @param modifyInfo
     * @return ReturnInfo<Boolean>
     */
    @PostMapping("/editShippingType")
    public ReturnInfo<Boolean> editShippingType(@RequestBody ConsigneeModifyInfo modifyInfo) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
        returnInfo.setMessage("编辑失败");

        try {
            returnInfo = orderCommonService.editShippingType(modifyInfo);
        } catch (Exception e) {
            logger.error("编辑承运商信息异常:" + JSONObject.toJSONString(modifyInfo), e);
            returnInfo.setMessage("编辑信息异常");
        }

        return returnInfo;
    }

    /**
     * 供应商订单发货结果
     *
     * @param request
     * @return DistOrderShipResponse
     */
    @PostMapping("/distOrderShip")
    public DistOrderShipResponse distOrderShip(@RequestBody DistOrderShipRequest request) {
        DistOrderShipResponse response = new DistOrderShipResponse();
        response.setSuccess(false);

        try {
            response = distributeShipService.distOrderShip(request);
        } catch (Exception e) {
            logger.error("供应商订单发货异常：" + JSON.toJSONString(request), e);
            response.setMessage("供应商订单发货异常：" + e.getMessage());
        }

        return response;
    }

    /**
     * 收货确认(按照快递单号签收，快递单号不填写时按照订单签收)
     *
     * @param bean
     * @return ReturnInfo<String>
     */
    @PostMapping("/confirmationOfReceipt")
    public ReturnInfo<String> confirmationOfReceipt(@RequestBody DistributeShippingBean bean) {
        ReturnInfo<String> returnInfo = new ReturnInfo<String>();

        try {
            returnInfo = orderCommonService.confirmationOfReceipt(bean);

            if (returnInfo.getIsOk() == Constant.OS_YES) {
                //查询订单来源
                String orderSN = bean.getOrderSn();
                HashMap<String, Object> orderMap = bgOrderInfoService.getOrderFromByOrderSN(orderSN);
                String buyerSN = (String) orderMap.get("user_id");
                String orderFrom = (String) orderMap.get("order_from");
                int totalPrice = ((BigDecimal) orderMap.get("money_paid")).intValue();

                if (orderMap != null && ("hbis").equals(orderFrom) && totalPrice >= 1000) {
                    //查询积分比例
                    int ratio = rewardPointRatioService.getRewardPointRatio();
                    int point = totalPrice / ratio;

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
        } catch (Exception e) {
            logger.error("订单系统收货确认异常:" + JSON.toJSONString(bean), e);
            returnInfo.setMessage("订单系统收货确认异常");
        }

        return returnInfo;
    }

    /**
     * 主订单编辑订单地址
     *
     * @param masterOrderSn
     * @param consignInfo
     * @return
     */
    @PostMapping("/editConsigneeInfoByMasterSn")
    public ReturnInfo<String> editConsigneeInfoByMasterSn(@RequestParam(name = "masterOrderSn") String masterOrderSn, @RequestBody ConsigneeModifyInfo consignInfo) {
        ReturnInfo<String> returnInfo = new ReturnInfo<String>();

        try {
            returnInfo = orderCommonService.editConsigneeInfoByMasterSn(masterOrderSn, consignInfo);
        } catch (Exception e) {
            logger.error(masterOrderSn + "主订单编辑订单地址异常:" + JSON.toJSONString(consignInfo), e);
            returnInfo.setMessage(masterOrderSn + "主订单编辑订单地址异常");
        }

        return returnInfo;
    }

    /**
     * 主订单编辑发票信息
     *
     * @param consignInfo
     * @return
     */
    @PostMapping("/editInvInfoByMasterSn")
    public ReturnInfo<String> editInvInfoByMasterSn(@RequestBody ConsigneeModifyInfo consignInfo) {
        ReturnInfo<String> returnInfo = new ReturnInfo<String>();

        try {
            returnInfo = orderCommonService.editInvInfoByMasterSn(consignInfo);
        } catch (Exception e) {
            logger.error("主订单编辑发票信息异常:" + JSON.toJSONString(consignInfo), e);
            returnInfo.setMessage("主订单编辑发票信息异常");
        }

        return returnInfo;
    }

    /**
     * 主订单编辑发票地址
     *
     * @param consignInfo
     * @return
     */
    @PostMapping("/editInvAddressInfoByMasterSn")
    public ReturnInfo<String> editInvAddressInfoByMasterSn(@RequestBody ConsigneeModifyInfo consignInfo) {
        ReturnInfo<String> returnInfo = new ReturnInfo<String>();

        try {
            returnInfo = orderCommonService.editInvAddressInfoByMasterSn(consignInfo);
        } catch (Exception e) {
            logger.error("主订单编辑订单地址异常:" + JSON.toJSONString(consignInfo), e);
            returnInfo.setMessage("主订单编辑订单地址异常");
        }

        return returnInfo;
    }
}
