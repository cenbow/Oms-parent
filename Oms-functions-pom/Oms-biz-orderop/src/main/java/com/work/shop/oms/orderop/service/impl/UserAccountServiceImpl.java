package com.work.shop.oms.orderop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.cloud.account.UserAccountRequestBean;
import com.work.shop.cloud.api.bean.ResultMsgBean;
import com.work.shop.cloud.bean.account.UserBankAccountInfo;
import com.work.shop.cloud.bean.usercompany.UserAccountInfo;
import com.work.shop.cloud.feign.account.UserAccountInfoFeign;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.order.service.MasterOrderInfoExtendService;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.order.service.MasterOrderPayService;
import com.work.shop.oms.orderReturn.service.OrderReturnService;
import com.work.shop.oms.orderop.service.JmsSendQueueService;
import com.work.shop.oms.user.account.UserAccountService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.MqConfig;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户账号服务
 * @author QuYachu
 */
@Service("userAccountService")
public class UserAccountServiceImpl implements UserAccountService {

    private static final Logger logger = Logger.getLogger(UserAccountServiceImpl.class);

    @Resource
    private UserAccountInfoFeign userAccountInfoFeign;

    @Resource
    private MasterOrderInfoExtendService masterOrderInfoExtendService;

    @Resource
    private MasterOrderInfoService masterOrderInfoService;

    @Resource
    private MasterOrderPayService masterOrderPayService;

    @Resource
    private OrderReturnService orderReturnService;

    @Resource
    private JmsSendQueueService jmsSendQueueService;

    /**
     * 处理用户信用额度
     * @param userAccountBean
     * @return ReturnInfo<Boolean>
     */
    @Override
    public ReturnInfo<Boolean> doReduceUserAccount(UserAccountBean userAccountBean) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
        returnInfo.setMessage("处理失败");

        try {
            String orderNo = userAccountBean.getOrderNo();
            MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendService.getMasterOrderInfoExtendById(orderNo);
            if (masterOrderInfoExtend == null) {
                returnInfo.setMessage("订单号:" + orderNo + "无对应扩展信息");
                return returnInfo;
            }
            UserAccountRequestBean userAccountRequestBean = new UserAccountRequestBean();
            // 在线支付
            userAccountRequestBean.setType(userAccountBean.getType());
            userAccountRequestBean.setUserId(masterOrderInfoExtend.getCompanyCode());
            userAccountRequestBean.setMoney(userAccountBean.getMoney());
            userAccountRequestBean.setOrderNo(orderNo);
            userAccountRequestBean.setActionUser(userAccountBean.getActionUser());

            boolean accountResult = false;
            String message = null;
            int accountType = userAccountBean.getAccountType();
            if (accountType == 0) {
                ResultMsgBean<UserAccountInfo> resultMsgBean = userAccountInfoFeign.doUserAccountInfo(userAccountRequestBean);

                if (resultMsgBean != null && Constant.OS_YES == resultMsgBean.getIsOk()) {
                    accountResult = true;
                } else {
                    message = resultMsgBean.getMsg();
                }
            } else if (accountType == 1) {
                ResultMsgBean<UserBankAccountInfo> resultMsgBean = userAccountInfoFeign.doUserBankAccountInfo(userAccountRequestBean);

                if (resultMsgBean != null && Constant.OS_YES == resultMsgBean.getIsOk()) {
                    accountResult = true;
                } else {
                    message = resultMsgBean.getMsg();
                }
            }

            if (accountResult) {
                returnInfo.setMessage("处理成功");
                returnInfo.setIsOk(Constant.OS_YES);
                returnInfo.setData(true);
                return returnInfo;
            } else {
                logger.error("处理用户账户信息失败:" + JSONObject.toJSONString(userAccountRequestBean) + ",resultMsgBean:" + message);
                returnInfo.setMessage(message);
            }
        } catch (Exception e) {
            logger.error("处理用户账户信息异常:", e);
        }
        return returnInfo;
    }

    /**
     * 处理用户结算账户
     * @param accountSettlementOrderBean
     * @return
     */
    @Override
    public ReturnInfo<Boolean> doUserSettlementAccount(AccountSettlementOrderBean accountSettlementOrderBean) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
        returnInfo.setMessage("处理失败");

        try {
            String orderNo = accountSettlementOrderBean.getOrderNo();
            MasterOrderInfo masterOrderInfo = masterOrderInfoService.getOrderInfoBySn(orderNo);
            if (masterOrderInfo == null) {
                returnInfo.setMessage("订单号:" + orderNo + "无对应订单信息");
                return returnInfo;
            }

            List<MasterOrderPay> masterOrderPayList = masterOrderPayService.getMasterOrderPayList(orderNo);
            if (masterOrderPayList == null || masterOrderPayList.size() == 0) {
                returnInfo.setMessage("订单号:" + orderNo + "无对应支付信息");
                return returnInfo;
            }

            MasterOrderPay masterOrderPay = masterOrderPayList.get(0);
            int payId = masterOrderPay.getPayId();
            if (Constant.PAYMENT_SETTLEMENT_ID != payId) {
                returnInfo.setMessage("订单号:" + orderNo + "不是结算账户");
                return returnInfo;
            }

            MasterOrderInfoExtend masterOrderInfoExtend = masterOrderInfoExtendService.getMasterOrderInfoExtendById(orderNo);
            if (masterOrderInfoExtend == null) {
                returnInfo.setMessage("订单号:" + orderNo + "无对应扩展信息");
                return returnInfo;
            }

            // 订单已签收
            int shipStatus = masterOrderInfo.getShipStatus();
            if (shipStatus != Constant.OI_SHIP_STATUS_ALLRECEIVED) {
                returnInfo.setMessage("订单号:" + orderNo + "客户未签收");
                return returnInfo;
            }

            Byte isGroup = masterOrderInfoExtend.getIsGroup();
            if (isGroup != null && isGroup == 1) {
                returnInfo.setMessage("订单号:" + orderNo + "团购订单,已支付");
                return returnInfo;
            }

            String userId = masterOrderInfo.getUserId();
            String companyId = masterOrderInfoExtend.getCompanyCode();
            accountSettlementOrderBean.setCompanyId(companyId);
            accountSettlementOrderBean.setUserId(userId);

            String channelCode = masterOrderInfo.getChannelCode();
            String orderForm = masterOrderInfo.getOrderFrom();
            accountSettlementOrderBean.setSiteCode(channelCode);
            accountSettlementOrderBean.setChannelCode(orderForm);

            String payNo = masterOrderPay.getPayNote();
            BigDecimal payTotalFee = masterOrderPay.getPayTotalfee();
            accountSettlementOrderBean.setPayNo(payNo);
            accountSettlementOrderBean.setOrderMoney(payTotalFee);
            accountSettlementOrderBean.setMoney(payTotalFee);
            orderSettlementReturn(orderNo, accountSettlementOrderBean);

            logger.info("用户结算账户下发:" + JSONObject.toJSONString(accountSettlementOrderBean));
            // 下发MQ
            jmsSendQueueService.sendQueueMessage(MqConfig.order_account_settlement, JSON.toJSONString(accountSettlementOrderBean));
            returnInfo.setMessage("下发成功");
            returnInfo.setIsOk(Constant.YESORNO_YES);
            returnInfo.setData(true);
        } catch (Exception e) {
            logger.error("处理用户结算账户信息异常:", e);
        }

        return returnInfo;
    }

    /**
     * 填充订单已退款金额
     * @param masterOrderSn
     * @param accountSettlementOrderBean
     */
    private void orderSettlementReturn(String masterOrderSn, AccountSettlementOrderBean accountSettlementOrderBean) {

        ReturnInfo<List<OrderReturnMoneyBean>> returnInfoData = orderReturnService.getOrderReturnMoneyInfo(masterOrderSn);
        if (Constant.OS_YES != returnInfoData.getIsOk()) {
            return;
        }

        List<OrderReturnMoneyBean> list = returnInfoData.getData();
        if (list == null || list.size() == 0) {
            return;
        }

        BigDecimal returnMoney = BigDecimal.valueOf(0);
        for (OrderReturnMoneyBean orderReturnMoneyBean : list) {
            BigDecimal returnFee = orderReturnMoneyBean.getReturnFee();
            int status = orderReturnMoneyBean.getBackBalance();
            if (status == 1) {
                returnMoney.add(returnFee);
            }
        }

        accountSettlementOrderBean.setReturnMoney(returnMoney);
        BigDecimal money = accountSettlementOrderBean.getOrderMoney().subtract(returnMoney);
        accountSettlementOrderBean.setMoney(money);
    }

}
