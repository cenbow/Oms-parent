package com.work.shop.oms.utils;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.ChannelShop;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.service.BrandUtilService;
import com.work.shop.oms.utils.ExceptionStackTraceUtil;
import com.work.shop.oms.utils.StringUtil;

@Service
public class BrandUtil implements BrandUtilService {
    
    public static Logger logger = Logger.getLogger(BrandUtil.class);

    @Resource
    private RedisClient redisClient;
//    @Resource(name = "channelInfoService")
//    private ChannelInfoService channelInfoService;
    
    private static final String SHOP_CODE_PREFIX = "OS_SHOP_CODE_";
    private static final String CHANNEL_CODE_PREFIX = "OS_CHANNEL_CODE_";
    
    /**
     * 根据订单店铺号获取店铺渠道号
     *   获取订单店铺号以及将店铺信息放入redis中
     * @param orderFrom
     * @return
     */
    public String getChannelCode(String orderFrom) {
        String channelCode = "";
        String redisChannelCodeKey = CHANNEL_CODE_PREFIX + orderFrom;
        String redisChannelCode = redisClient.get(redisChannelCodeKey);
        if (StringUtil.isEmpty(redisChannelCode)) {
            try {
                JsonResult jsonResult = new JsonResult();
//                String result = channelInfoService.findShopInfoByShopCode(orderFrom, 0);
                String result = null;
                if (StringUtil.isNotEmpty(result)) {
                    jsonResult = JSONObject.parseObject(result, JsonResult.class);
                    if (null != jsonResult && jsonResult.getData() != null) {
                        logger.info(jsonResult.getData());
                        ChannelShop channelShop = JSONObject.toJavaObject((JSONObject)jsonResult.getData(), ChannelShop.class);
                        // 店铺属于分销渠道使用该店铺的父店铺号
                        if (channelShop != null && ConstantValues.TBFX_CHANNEL_CODE.equals(channelShop.getChannelCode())) {
                            channelCode = channelShop.getChannelCode();
                        } else {
                            if (channelShop != null && StringUtil.isNotNull(channelShop.getShopCode())) {
                                channelCode = channelShop.getChannelCode();
                            } else {
                                logger.error("订单店铺号[" + orderFrom + "] 在渠道店铺表中没有查到相关信息！请相关人员确认！");
                            }
                        }
                        if (StringUtil.isNotEmpty(channelCode)) {
                            redisClient.set(redisChannelCodeKey, channelCode);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("根据店铺号获取店铺信息异常" + ExceptionStackTraceUtil.getExceptionTrace(e));
            }
        } else {
            channelCode = redisChannelCode;
        }
        return channelCode;
    }

    /**
     * 根据订单店铺号和订单referer 获取库存目标店铺号
     *   获取订单店铺占用库存的店铺号以及将店铺信息放入redis中
     * @param orderFrom
     * @param referer
     * @return
     */
    public String getShopCode(String orderFrom, String referer) {/*
        String targetChannel = orderFrom;
        if (!BrandUtil.noERPWithO2O(referer)) {//全流通
            targetChannel = null;
        } else {
            String redisShopCodeKey = SHOP_CODE_PREFIX + orderFrom;
            String redisChannelCodeKey = CHANNEL_CODE_PREFIX + orderFrom;
            String targetShopCode = redisClient.get(redisShopCodeKey);
            String channelCode = redisClient.get(redisChannelCodeKey);
            if (StringUtil.isEmpty(targetShopCode) || StringUtil.isEmpty(channelCode)) {
                try {
                    JsonResult jsonResult = new JsonResult();
                    String result = channelInfoService.findShopInfoByShopCode(orderFrom, 0);
                    if (StringUtil.isNotEmpty(result)) {
                        jsonResult = JSONObject.parseObject(result, JsonResult.class);
                        if (null != jsonResult && jsonResult.getData() != null) {
                            logger.info(jsonResult.getData());
                            ChannelShop channelShop = JSONObject.toJavaObject((JSONObject)jsonResult.getData(), ChannelShop.class);
                            // 店铺属于分销渠道使用该店铺的父店铺号
                            if (channelShop != null && ConstantValues.TBFX_CHANNEL_CODE.equals(channelShop.getChannelCode())) {
                                targetShopCode = channelShop.getParentShopCode();
                                channelCode = channelShop.getChannelCode();
                            } else {
                                if (channelShop != null && StringUtil.isNotNull(channelShop.getShopCode())) {
                                    targetShopCode = channelShop.getShopCode();
                                    channelCode = channelShop.getChannelCode();
                                } else {
                                    logger.error("订单店铺号[" + orderFrom + "] 在渠道店铺表中没有查到相关信息！请相关人员确认！");
                                }
                            }
                            if (StringUtil.isNotEmpty(targetShopCode)) {
                                redisClient.set(redisShopCodeKey, targetShopCode);
                                targetChannel = targetShopCode;
                            }
                            if (StringUtil.isNotEmpty(channelCode)) {
                                redisClient.set(redisChannelCodeKey, channelCode);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("根据店铺号获取店铺信息异常" + ExceptionStackTraceUtil.getExceptionTrace(e));
                }
            } else {
                targetChannel = targetShopCode;
            }
        }
        return targetChannel;
    */
        return null;}
    
 
}
