package com.work.shop.oms.service;

public interface MessageService {

    
    
    /**
     * 发送message（订单确认使用、以及短息模板没有占位符的情况使用）
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
    /*void doReadySendSysMsg(String orderFrom, String userId, String targetIds, int msgType,
            String msgTitle, String msgDesc, int sendType, int sendOrder, String templateCode);*/
    
    
    /**
     * 发送message
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
    /*public void sendSysMsg(int msgType, String msgTitle, String msgContent, String targetIds,
            String msgDesc, String channelCode, int sendType, int sendOrder);*/
    
    /**
     * Oms短信发送
     * @param targetIds - 手机号
     * @param msgContent 短信内容
     * @param channelCode 渠道号
     * @param sendType 业务类型 *** 
     */
    public void sendMobileMessage(String targetIds,String msgContent,String channelCode, String sendType);
    
    public void sendSysMsgShipped(int msgType, String msgTitle, String msgContent, String targetIds,
            String msgDesc, String channelCode, int sendOrder);
    
}
