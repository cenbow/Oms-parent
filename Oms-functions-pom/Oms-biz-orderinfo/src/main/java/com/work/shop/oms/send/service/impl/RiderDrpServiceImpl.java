package com.work.shop.oms.send.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderRiderDistributeLog;
import com.work.shop.oms.bean.OrderRiderDistributeLogExample;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.dao.OrderRiderDistributeLogMapper;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.send.service.RiderDrpService;
import com.work.shop.oms.send.service.bean.DrpOrderInfo;
import com.work.shop.oms.send.service.bean.DrpRequest;
import com.work.shop.oms.send.service.bean.DrpToResult;
import com.work.shop.oms.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 推送drp
 * @author YeQingchao
 */
@Service("riderDrpService")
public class RiderDrpServiceImpl implements RiderDrpService {

    private static final Logger logger = Logger.getLogger(RiderDrpServiceImpl.class);

    @Resource(name="orderRiderDistributeLogMapper")
    private OrderRiderDistributeLogMapper orderRiderDistributeLogMapper;

    @Resource
    private MasterOrderActionService masterOrderActionService;

    @Resource
    private MasterOrderInfoService masterOrderInfoService;

    /**
     * 配送成功信息推送drp
     * @param request
     * @return
     */
    public ServiceReturnInfo<Boolean> sendDrpByDeliverySuccess(OrderQueryRequest request) {
        ServiceReturnInfo<Boolean> serviceReturnInfo = new ServiceReturnInfo<Boolean>();
        logger.info("------订单配送成功推送drp开始------");
        logger.info("sendDrpByDeliverySuccess -> request" + JSONObject.toJSONString(request));

        String msg = "";
        try {
            //校验订单，并填充drp请求参数
            ServiceReturnInfo<DrpRequest> verifyInfo = verifyOrderInfo(request);
            if (verifyInfo == null || verifyInfo.getResult() == null) {
                serviceReturnInfo.setMessage("校验订单异常");
                return serviceReturnInfo;
            } else if (!verifyInfo.isIsok()) {
                serviceReturnInfo.setMessage(verifyInfo.getMessage());
                return serviceReturnInfo;
            }

            //推送drp
            DrpRequest drpRequest = verifyInfo.getResult();
            ServiceReturnInfo<String> drpResult = sendOrderToDrp(drpRequest);
            if (drpResult == null) {
                serviceReturnInfo.setMessage("订单配送成功推送drp接口异常");
                msg = "订单配送成功推送drp接口异常";
            } else if (!drpResult.isIsok()) {
                serviceReturnInfo.setMessage(drpResult.getMessage());
                msg = drpResult.getMessage();
            } else {
                serviceReturnInfo.setIsok(true);
            }

        } catch (Exception e){
            logger.error("订单配送成功推送drp异常", e);
            serviceReturnInfo.setMessage("订单配送成功推送drp异常");
            msg = "订单配送成功推送drp异常";
        }

        //添加订单日志
        masterOrderActionService.insertOrderActionBySn(request.getMasterOrderSn(), msg, request.getUserId());
        logger.info("------订单配送成功推送drp结束------");
        return serviceReturnInfo;
    }

    /**
     * 校验订单配送
     * @param request
     * @return
     */
    private ServiceReturnInfo<DrpRequest> verifyOrderInfo(OrderQueryRequest request) {
        ServiceReturnInfo<DrpRequest> serviceReturnInfo = new ServiceReturnInfo<DrpRequest>();

        if (request == null) {
            serviceReturnInfo.setMessage("请求参数不能为空");
            return serviceReturnInfo;
        }

        String masterOrderSn = request.getMasterOrderSn();
        if (StringUtils.isBlank(masterOrderSn)) {
            serviceReturnInfo.setMessage("订单号不能为空");
            return serviceReturnInfo;
        }

        String actionUser = request.getActionUser();
        if (StringUtils.isBlank(actionUser)) {
            serviceReturnInfo.setMessage("操作人不能为空");
            return serviceReturnInfo;
        }

        MasterOrderInfo orderInfo = masterOrderInfoService.getOrderInfoBySn(masterOrderSn);
        if (orderInfo == null) {
            serviceReturnInfo.setMessage(masterOrderSn + "订单不存在");
            return serviceReturnInfo;
        }
        Byte shipStatus = orderInfo.getShipStatus();
        if (shipStatus != 5) {
            serviceReturnInfo.setMessage(masterOrderSn + "订单未收货");
            return serviceReturnInfo;
        }

        //获取配送日志信息
        OrderRiderDistributeLogExample example = new OrderRiderDistributeLogExample();
        OrderRiderDistributeLogExample.Criteria criteria = example.or();
        criteria.andMasterOrderSnEqualTo(masterOrderSn);
        criteria.andOrderStatusEqualTo((short) 2);
        List<OrderRiderDistributeLog> riderDistributeLogs = orderRiderDistributeLogMapper.selectByExample(example);
        if (StringUtil.isListNull(riderDistributeLogs)) {
            serviceReturnInfo.setMessage(masterOrderSn + "订单配送成功信息");
            return serviceReturnInfo;
        }

        //配送日志,组装参数
        OrderRiderDistributeLog riderDistributeLog = riderDistributeLogs.get(0);
        List<DrpOrderInfo> drpOrderInfoList = new ArrayList<DrpOrderInfo>();
        DrpOrderInfo drpOrderInfo = new DrpOrderInfo();
        //接单时间
        drpOrderInfo.setAccept_time(TimeUtil.format(riderDistributeLog.getAcceptTime(), TimeUtil.YYYY_MM_DD_HH_MM_SS));
        //配送距离
        drpOrderInfo.setDistance(riderDistributeLog.getDistance() == null ? "" : riderDistributeLog.getDistance().toString());
        //订单号
        drpOrderInfo.setOrderno(masterOrderSn);
        //送达时间
        drpOrderInfo.setPost_time(TimeUtil.format(riderDistributeLog.getFinishTime(), TimeUtil.YYYY_MM_DD_HH_MM_SS));
        drpOrderInfoList.add(drpOrderInfo);

        Map<String, List<DrpOrderInfo>> map = new HashMap<String, List<DrpOrderInfo>>();
        map.put("orders", drpOrderInfoList);

        DrpRequest drpRequest = new DrpRequest();
        drpRequest.setJsonstr(JSONObject.toJSONString(map));

        serviceReturnInfo.setIsok(true);
        serviceReturnInfo.setResult(drpRequest);
        return serviceReturnInfo;
    }

    /**
     * 推送drp
     * @param drpRequest
     * @return
     */
    private ServiceReturnInfo<String> sendOrderToDrp(DrpRequest drpRequest) {
        ServiceReturnInfo<String> serviceReturnInfo = new ServiceReturnInfo<String>();

        try {
            //组装请求参数
            List<NameValuePair> paramList = billParam(drpRequest);

            //请求接口
            String url = ConfigCenter.getProperty(Constant.DRP_HLA_POST_API_ADDRESS);
            logger.info("推送订单到Ec系统请求地址：" + url + ";请求参数：" + JSONObject.toJSONString(paramList));
            String result = HttpClientUtil.post(url, paramList);
            logger.info("推送订单到Ec系统请求返回结果：" + result);
            if (StringUtils.isEmpty(result) || JSONObject.parseObject(result, DrpToResult.class) == null) {
                serviceReturnInfo.setMessage("推送订单到Ec系统接口异常");
                return serviceReturnInfo;
            }

            //接口返回数据
            DrpToResult drpToResult = JSONObject.parseObject(result, DrpToResult.class);
            if (drpToResult.isSuccess()) {
                //成功
                serviceReturnInfo.setIsok(true);
                serviceReturnInfo.setResult(JSONObject.toJSONString(drpToResult.getMap()));
            } else {
                //失败
                String errMsg = "";
                Map<String, String> map = drpToResult.getMap();
                if (map == null) {
                    errMsg = "配送信息推送drp请求返回数据为空";
                } else {
                    errMsg = map.get("errorMsg");
                }
                serviceReturnInfo.setMessage(errMsg);
            }
        }catch (Exception e) {
            logger.error("配送信息推送drp请求异常" + JSONObject.toJSONString(drpRequest), e);
            serviceReturnInfo.setMessage("配送信息推送drp请求异常");
        }

        return serviceReturnInfo;
    }

    /**
     * 填充请求参数
     * @return
     */
    private List<NameValuePair> billParam(DrpRequest drpRequest) {
        List<NameValuePair> list = new ArrayList<NameValuePair>(8);
        String orgcode = ConfigCenter.getProperty(Constant.DRP_HLA_ORGCODE);
        list.add(HttpClientUtil.getNameValuePair("orgcode", orgcode));

        String orgmethod = ConfigCenter.getProperty(Constant.DRP_HLA_POST_API_NAME);
        list.add(HttpClientUtil.getNameValuePair("orgmethod", orgmethod));

        String brand = ConfigCenter.getProperty(Constant.DRP_HLA_BRAND);
        list.add(HttpClientUtil.getNameValuePair("brand", brand));

        String timestamp = TimeUtil.getTimestampByTen(new Date());
        list.add(HttpClientUtil.getNameValuePair("timestamp", timestamp));

        String format = drpRequest.getFormat();
        list.add(HttpClientUtil.getNameValuePair("format", format));

        String jsonstr = drpRequest.getJsonstr();
        list.add(HttpClientUtil.getNameValuePair("jsonstr", jsonstr));

        String sign = getSign(list);
        list.add(HttpClientUtil.getNameValuePair("sign", sign));

        return list;
    }

    /**
     * 获取签名
     * @param list
     * @return
     */
    private String getSign(List<NameValuePair> list) {
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>(8);
        paramsList.addAll(list);

//        String secret = ConfigCenter.getProperty(Constant.EC_YIXIAOSHIDA_SECRET);
//        paramsList.add(HttpClientUtil.getNameValuePair("secret", secret));

        try {
            String formatParams = null;
            // 将参数进行utf-8编码
            if (null != paramsList && paramsList.size() > 0) {
                formatParams = URLEncodedUtils.format(paramsList, HttpClientUtil.CHARSET_ENCODING);
            }

            return Md5Util.getMD5(formatParams);
        } catch (Exception e) {
            logger.error("数据加密异常" + JSONObject.toJSONString(paramsList), e);
        }

        return null;
    }
}
