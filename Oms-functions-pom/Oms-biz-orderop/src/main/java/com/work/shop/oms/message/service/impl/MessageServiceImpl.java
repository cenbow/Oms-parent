package com.work.shop.oms.message.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.dao.SystemConfigMapper;
import com.work.shop.oms.dao.SystemMsgTemplateMapper;
import com.work.shop.oms.service.BrandUtilService;
import com.work.shop.oms.service.MessageService;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.sms.bean.Message;
import com.work.shop.sms.bean.State;
import com.work.shop.sms.bean.User;
import com.work.shop.sms.send.api.SMSService;

/**
 * 消息服务接口
 * @author QuYachu
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {


    Logger logger = Logger.getLogger(MessageServiceImpl.class);

    //@Resource(name="sMSService")
    private SMSService smsService;
    @Resource
    private SystemConfigMapper systemConfigMapper;
    @Resource
    private SystemMsgTemplateMapper systemMsgTemplateMapper;
    @Resource
    BrandUtilService brandUtil;

    /**
     * 发送短信前准备工作（订单确认使用、以及短息模板没有占位符的情况使用）
     * @param orderFrom
     * @param userId
     * @param targetIds
     * @param msgType
     * @param msgName
     * @param msgDesc
     * @param sendType
     * @param sendOrder
     * @param templateCode
     */
    /*public void doReadySendSysMsg(String orderFrom, String userId, String targetIds,
            int msgType, String msgTitle, String msgDesc, int sendType, int sendOrder,
            String templateCode) {
        if (StringUtil.isEmpty(orderFrom) || StringUtil.isEmpty(userId)) {
            return;
        }
        try {
            // 信息模板
            SystemMsgTemplate smt = systemMsgTemplateMapper.selectByPrimaryKey(templateCode);
            if (smt == null) {
                return;
            }
            // 用户信息
            UserUsers uu = UserInfoService.getUserInfo(userId.trim());
            if (uu == null) {
                if (targetIds == null || targetIds.equals("")) {
                    logger.debug(" no user in user_users and can not send messqge!");
                    return;
                }
            } else {
                if (!StringUtils.isEmpty(uu.getMobile()) && uu.getMobile().length() > 7) {
                    targetIds = uu.getMobile();
                } else {
                    if (targetIds == null || targetIds.equals("")) {
                        logger.debug(" the user in user_users but have null mobile and order_ship`s user-mobile is null, so can not send messqge!");
                        return;
                    }
                }
            }
            // 发送短信
            //sendSysMsg(2, msgTitle, smt.getTemplate(), targetIds, "", orderFrom, 115, 2);
            sendMobileMessage(targetIds, smt.getTemplate(), orderFrom, sendType);
        } catch (Exception e) {
            logger.error("调用MSG处理失败" + e.getMessage() + ";orderForm=" + orderFrom + "; userId=" + userId);
        }
    }*/

    /**
     * 添加message表
     * @param msgType 1：邮件 2：短信  3：站内消息
     * @param msgTitle
     * @param msgContent 信息内容
     * @param targetIds 发送目标设备号 类似：'111@11.com    1391345098',
     * @param msgDesc
     * @param channelCode
     * @param sendType '业务类型 0.未知; 1.用户注册;2.免登下单;3.手机找回密码;4.订单发货通知;
     *                  5.订单配货中通知; 6.订单退货通知;7.退货收到通知;8.退货验收通知;9.退货金额返还通知;'
     * @param sendOrder '发送优先级别 1.快; 2.中; 3.慢',一般默认3
     */
    /*public void sendSysMsg(int msgType, String msgTitle, String msgContent,
            String targetIds, String msgDesc, String channelCode, int sendType,
            int sendOrder) {
        try {
            
            targetIds = orderMobileEncService.analysisMobile(targetIds);
            User u = new User();
            u.setUsername(ConfigCenter.getProperty("smsUser"));
            u.setPassword(ConfigCenter.getProperty("smsPwd"));
            Message msg = new Message();
            msg.setPhoneNO(targetIds);
            msg.setChannelCode(channelCode);
            if (Constant.OS_CHANNEL_BANGGO.equals(channelCode)) {
                msg.setSendType("bg_" + String.valueOf(sendType));
            } else {
                msg.setSendType("mb_" + String.valueOf(sendType));
            }
            msg.setMsgContent(msgContent);
            SystemConfigExample example = new SystemConfigExample();
            example.or().andCodeEqualTo("is_send_friend_sms");
            List<SystemConfig> systemConfigs =systemConfigMapper.selectByExample(example);
            String isSendVal = "";
            if (StringUtil.isNotNullForList(systemConfigs)) {
                isSendVal = systemConfigs.get(0).getValue();
            }
            SystemConfigExample configExample = new SystemConfigExample();
            configExample.or().andCodeEqualTo("is_send_friend_sms");
            List<SystemConfig> systemConfigs2 =systemConfigMapper.selectByExample(configExample);
            String sendChlVal = "";
            if (StringUtil.isNotNullForList(systemConfigs2)) {
                sendChlVal = systemConfigs2.get(0).getValue();
            }

            SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date ddDate = new Date();
            String nDate = dateformat1.format(ddDate);

            StringTokenizer st = new StringTokenizer(isSendVal, "|");
            List<String> fdList = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                fdList.add(st.nextToken());
            }

            String fh = fdList.get(0);
            String fhDate1 = fdList.get(1);
            String fhDate2 = fdList.get(2);

            String fk = fdList.get(3);
            String fkDate1 = fdList.get(4);
            String fkDate2 = fdList.get(5);

            StringTokenizer stChl = new StringTokenizer(sendChlVal, "|");
            List<String> fdListChl = new ArrayList<String>();

            while (stChl.hasMoreTokens()) {
                fdListChl.add(stChl.nextToken());
            }
            boolean doSent = false;
            if (sendType == 119 && "fh_on".equals(fh) && fdListChl.contains(channelCode)
                    && nDate.compareTo(fhDate1) > 0 && nDate.compareTo(fhDate2) < 0) {
                logger.debug("执行发货短信,手机号" + targetIds);
                doSent = true;
            }
            if (sendType == 115 && "fk_on".equals(fk) && fdListChl.contains(channelCode)
                    && nDate.compareTo(fkDate1) > 0 && nDate.compareTo(fkDate2) < 0) {
                logger.debug("执行付款短信,手机号" + targetIds);
                doSent = true;
            }
            if(sendType==121){
                logger.debug("执行付款短信,手机号"+targetIds);
                doSent=true;
            }
            if(sendType==123){
                logger.debug("执行付款短信,手机号"+targetIds);
                doSent=true;
            }
            State resultState = null;
            if (doSent) {
                resultState = smsService.send(u, msg);
                if (!State.SUCCESSFULLY.equals(resultState.getState())) {
                    logger.error(resultState.getPhoneNo() + "短信发送失败,原因:" + resultState.getMessage());
                }
            }
            logger.debug("sendSysMsg.mobile:"+targetIds+",doSent:"+doSent+",resultState:"+JSON.toJSONString(resultState));
        } catch (Exception e) {
            logger.error("短信发送:"+e.getMessage(),e);
            e.printStackTrace() ;
        }
    }*/
    
    /**
     * 最新版-短信发送
     * @param targetIds
     * @param msgContent
     * @param channelCode
     * @param sendType
     */
    public void sendMobileMessage(String targetIds,String msgContent,String channelCode, String sendType) {
        try {
            User u = new User();
            u.setUsername(ConfigCenter.getProperty("smsUser"));
            u.setPassword(ConfigCenter.getProperty("smsPwd"));
            Message msg = new Message();
            msg.setPhoneNO(targetIds);
            msg.setChannelCode(channelCode);
            msg.setMsgContent(msgContent);
            //目前三个通道
            msg.setSendType("mb_" + StringUtils.trimToEmpty(sendType));
            boolean doSent = true;
            String notMSMChannel=ConfigCenter.getProperty("not.send.msm");
            if(StringUtil.isNotNull(notMSMChannel)&&notMSMChannel.indexOf(channelCode)>=0){
                doSent=false;
            }
            
            State resultState = null;
            if (doSent) {
                resultState = smsService.send(u, msg);
                if (!State.SUCCESSFULLY.equals(resultState.getState())) {
                    logger.error(resultState.getPhoneNo() + "短信发送失败,原因:" + resultState.getMessage());
                }
            }
            logger.debug("sendSysMsg.mobile:"+targetIds+",doSent:"+doSent+",resultState:"+JSON.toJSONString(resultState));
        } catch (Exception e) {
            e.printStackTrace() ;
            logger.error("短信发送("+targetIds+"):"+e.getMessage(),e);
        }
    }
    
    /**
     * 发货接口专用
     * @param msgType
     * @param msgTitle
     * @param msgContent
     * @param targetIds
     * @param msgDesc
     * @param channelCode
     * @param sendOrder
     */
    public void sendSysMsgShipped(int msgType, String msgTitle, String msgContent,
            String targetIds, String msgDesc, String channelCode, 
            int sendOrder) {
        try {
            User u = new User();
            u.setUsername(ConfigCenter.getProperty("smsUser"));
            u.setPassword(ConfigCenter.getProperty("smsPwd"));
            Message msg = new Message();
            msg.setPhoneNO(targetIds);
            msg.setChannelCode(channelCode); 
            msg.setSendType("mb_" + String.valueOf("119"));
            msg.setMsgContent(msgContent); 
            State resultState = smsService.send(u, msg);
            if (!State.SUCCESSFULLY.equals(resultState.getState())) {
                logger.error(resultState.getPhoneNo() + "短信发送失败,原因:" + resultState.getMessage());
            } 
            logger.debug("最终发送状态----------------------------------------------"+targetIds + resultState);
            
        } catch (Exception e) {
            // logger.error(ExceptionStackTraceUtil.getExceptionTrace(e));

             e.printStackTrace() ;
        }
    }

   

}
