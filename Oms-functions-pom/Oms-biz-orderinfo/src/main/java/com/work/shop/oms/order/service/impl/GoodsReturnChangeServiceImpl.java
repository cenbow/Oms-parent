package com.work.shop.oms.order.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.bean.OrderReturnSkuInfo;
import com.work.shop.oms.bean.GoodsReturnChange;
import com.work.shop.oms.bean.GoodsReturnChangeAction;
import com.work.shop.oms.bean.GoodsReturnChangeExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.dao.GoodsReturnChangeActionMapper;
import com.work.shop.oms.dao.GoodsReturnChangeMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.order.service.GoodsReturnChangeService;
import com.work.shop.oms.param.bean.GoodsReturnChangeWriteBack;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.ConfigCenter;
import com.work.shop.oms.utils.StringUtil;

/**
 * 商品退换货服务
 * @author QuYachu
 */
@Service("goodsReturnChangeService")
public class GoodsReturnChangeServiceImpl implements GoodsReturnChangeService {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private GoodsReturnChangeMapper goodsReturnChangeMapper;
    
    @Resource
    private  MasterOrderInfoMapper masterOrderInfoMapper;
    
    @Resource
    private RedisClient redisClient;
    @Resource
    private GoodsReturnChangeActionMapper goodsReturnChangeActionMapper;

    @Resource
    private OrderReturnMapper orderReturnMapper;
    
    private static final String SHOP_CODE_ = "CH_SC_";
    
    private static final String mapKey = "app_url";

    @Override
    public ReturnInfo goodsReturnChangeClearing(String orderSn, String orderReturnSn,
            List<OrderReturnSkuInfo> orderReturnSkuInfoList) {
        logger.info("退货结算回写:orderSn="+orderSn+"退单："+orderReturnSn+"商品种类："+orderReturnSkuInfoList.size());
        ReturnInfo returnInfo = new ReturnInfo();
        returnInfo.setIsOk(0);
        try {
            for(OrderReturnSkuInfo SkuInfo: orderReturnSkuInfoList){
                GoodsReturnChangeExample example=new GoodsReturnChangeExample();
                GoodsReturnChangeExample.Criteria criteria = example.or();
                criteria.andOrderSnEqualTo(orderSn);
                criteria.andSkuSnEqualTo(SkuInfo.getSkuSn());
                criteria.andStatusNotEqualTo(2);
                List<GoodsReturnChange> list=goodsReturnChangeMapper.selectByExample(example);
                if(list.size()>0){
                for(int i=0;i<list.size();i++){
                    //已结算则将申请单状态改为已完成
                    updateStatus(2, list.get(i).getId(), orderSn, "system", "退单结算，系统自动设置申请单为已完成！");
                    logger.info("退单结算将申请单："+list.get(i).getReturnchangeSn()+"状态修改为已完成！");
                }
                }
            }
            //状态回写
            ReturnInfo ApireturnInfo=clearingWriteBackApi(orderSn,orderReturnSn, orderReturnSkuInfoList);
            returnInfo.setIsOk(ApireturnInfo.getIsOk());
            returnInfo.setMessage(ApireturnInfo.getMessage());
        } catch (Exception e) {
            logger.error("退单结算回写有范平台异常：", e);
            returnInfo.setMessage(e.toString());
            return returnInfo;
        }
        
        logger.info("退换货结算回写完成！");
        return returnInfo;
    }
    
    @Override
    public ReturnInfo updateStatus(Integer status, Integer id,String orderSn,String userCode,String actionNote) {
        ReturnInfo returnInfo=new ReturnInfo();
        try {
             GoodsReturnChange goodsReturnChange=new GoodsReturnChange();   
                goodsReturnChange=goodsReturnChangeMapper.selectByPrimaryKey(id);
                GoodsReturnChangeAction goodsReturnChangeAction=new GoodsReturnChangeAction();
                goodsReturnChangeAction.setActionUser(userCode);
                goodsReturnChangeAction.setLogTime(new Date());
                goodsReturnChangeAction.setOrderSn(goodsReturnChange.getOrderSn());
                goodsReturnChangeAction.setReturnchangeSn(goodsReturnChange.getReturnchangeSn());
                if(status!=null){
                    goodsReturnChangeAction.setStatus(status);
                StringBuffer str=new StringBuffer(actionNote);
                str.append("：订单编号为【");
                    str.append(goodsReturnChange.getOrderSn());
                    str.append("】的退换货商品处理状态由");
                    switch (goodsReturnChange.getStatus()) {
                    case 0:str.append("“已取消”");
                        break;
                    case 1:str.append("“待沟通”");
                        break;
                    case 2:str.append("“已完成”");
                        break;
                    case 3:str.append("“待处理”");
                        break;
                    default:str.append(goodsReturnChange.getStatus());
                        break;
                    }
                    str.append("修改为");
                    switch (status) {
                    case 0:str.append("“已取消”");
                        break;
                    case 1:str.append("“待沟通”");
                        break;
                    case 2:str.append("“已完成”");
                        break;
                    case 3:str.append("“待处理”");
                        break;
                    default:str.append(status);
                        break;
                    }
                    if (StringUtil.isEmpty(orderSn)) {
                        logger.error("订单号不能为空!");
                        returnInfo.setIsOk(0);
                        returnInfo.setMessage("订单号不能为空!");
                        return returnInfo;
                    }
             //       OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderSn);
                  
                    MasterOrderInfo masterOrderInfo  = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
                    
                    if (null == masterOrderInfo) {
                        logger.error("根据订单号未查询到关联订单");
                        returnInfo.setIsOk(0);
                        returnInfo.setMessage("根据订单号未查询到关联订单");
                        return returnInfo;
                    }
                    goodsReturnChangeAction.setActionNote(str.toString()+"</br>");
                    goodsReturnChange.setStatus(status);
                    goodsReturnChangeMapper.updateByPrimaryKey(goodsReturnChange);
                }else{
                    //只提交备注
                    goodsReturnChangeAction.setStatus(goodsReturnChange.getStatus());
                    goodsReturnChangeAction.setActionNote(actionNote);  
                }
                goodsReturnChangeActionMapper.insert(goodsReturnChangeAction);
                returnInfo.setIsOk(1);
        } catch (Exception e) {
            returnInfo.setIsOk(0);
            returnInfo.setMessage(e.getMessage());
        }
        return returnInfo;
        }
    
    public ReturnInfo clearingWriteBackApi(String orderSn,String orderReturnSn,List<OrderReturnSkuInfo> orderReturnSkuInfoList) throws Exception{
        ReturnInfo returnInfo=new ReturnInfo();
        returnInfo.setIsOk(0);
        try {
            GoodsReturnChangeWriteBack goodsReturnChangeWriteBack=new GoodsReturnChangeWriteBack();
            goodsReturnChangeWriteBack.setOrderSn(orderSn);
            MasterOrderInfo orderInfo=masterOrderInfoMapper.selectByPrimaryKey(orderSn);
            OrderReturn orderReturn=orderReturnMapper.selectByPrimaryKey(orderReturnSn);
            if(orderReturn!=null&&orderReturn.getReturnShipping()!=null){
                    goodsReturnChangeWriteBack.setReturnShipping(orderReturn.getReturnShipping().doubleValue());
            }else{
                logger.info("退款单条数错误！");
            }
            goodsReturnChangeWriteBack.setOrderOutSn(orderInfo.getOrderOutSn());
            if(orderReturn.getReturnType()==3){
                goodsReturnChangeWriteBack.setReturnType("1");
            }else if (orderReturn.getReturnType()==1){
                goodsReturnChangeWriteBack.setReturnType("2");
            }
            goodsReturnChangeWriteBack.setRerurnGoods(orderReturnSkuInfoList);
//          Map<String, Object> paramModel = new HashMap<String, Object>();
//          paramModel.put("returnData",JSONObject.toJSON(goodsReturnChangeWriteBack));
//          ObjectMapper objectMapper = new ObjectMapper();
//          final String requestBody = objectMapper
//                  .writeValueAsString(paramModel);
            String returnData=JSON.toJSONString(goodsReturnChangeWriteBack);
            String response="";
            String url="";
            String redisShopCodeKey = SHOP_CODE_ + orderInfo.getOrderFrom();
            String shopInfo = redisClient.get(redisShopCodeKey);
            JSONObject shopJson = JSONArray.parseObject(shopInfo);
            String shopIP=(String) shopJson.get(mapKey);
            url=shopIP+ConfigCenter.getProperty("ZXS_GoodsReturn_clearingURL")+"&returnData="+java.net.URLEncoder.encode(returnData,"utf-8");
            logger.info("退货单结算回写Api调用开始： url="+url+",data="+returnData);
            if(url!=null&&url.length()>0){
//              response=post(url, requestBody);
                response=get(url);
            }
            JSONObject json = JSONArray.parseObject(response);
            logger.info("退货单结算回写Api调用结果： " + json);
            String isOk = "" + json.get("isOk");
            String message = (String) json.get("message");
            if(isOk.equals("true")){
                returnInfo.setIsOk(1);
            }else{
                returnInfo.setMessage(message);
            }
        } catch (Exception e) {
            throw e;
        }
        return returnInfo;
    }
    
    private String get(String url) {
        logger.info("request url : " + url);
        String responseBody = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
        managerParams.setConnectionTimeout(100000);
        managerParams.setSoTimeout(120000);
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                responseBody = getMethod.getResponseBodyAsString();
            } else {
                System.out.println("Eorr occus");
                responseBody = "跳转失败，错误编码：" + statusCode;
            }
        } catch (Exception e) {
            responseBody = "网关限制无法跳转!";
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return responseBody;
    }

}
