package com.work.shop.oms.send.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.dao.SystemRegionAreaMapper;
import com.work.shop.oms.order.request.OrderQueryRequest;
import com.work.shop.oms.order.service.*;
import com.work.shop.oms.send.service.OrderToEcService;
import com.work.shop.oms.send.service.bean.OrderGoodsToEc;
import com.work.shop.oms.send.service.bean.OrderToEcRequest;
import com.work.shop.oms.send.service.bean.OrderToEcRequestInfo;
import com.work.shop.oms.send.service.bean.OrderToEcResult;
import com.work.shop.oms.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service("orderToEcService")
public class OrderToEcServiceImpl implements OrderToEcService {

    private static final Logger logger = Logger.getLogger(OrderToEcServiceImpl.class);

    @Resource
    private MasterOrderInfoService masterOrderInfoService;

    @Resource
    private MasterOrderPayService masterOrderPayService;

    @Resource
    private MasterOrderAddressInfoService masterOrderAddressInfoService;

    @Resource
    private MasterOrderGoodsService masterOrderGoodsService;

    @Resource(name="systemRegionAreaMapper")
    private SystemRegionAreaMapper systemRegionAreaMapper;

    @Resource
    private MasterOrderActionService masterOrderActionService;

    /**
     * 订单确认后推送百胜Ec
     * @param orderQueryRequest
     * @return
     */
    public ServiceReturnInfo<Boolean> sendOrderToEc(OrderQueryRequest orderQueryRequest) {
        ServiceReturnInfo<Boolean> serviceReturnInfo = new ServiceReturnInfo<Boolean>();
        logger.error("------推送已确认订单至Ec系统开始-----");
        logger.error("OrderToEcServiceImpl -> orderConfirmToEc -> 推送数据：" + JSONObject.toJSONString(orderQueryRequest));
        String message = "订单推送Ec系统一小时达失败";
        try {
            //校验订单参数,并获取发送百胜Ec系统数据
            ServiceReturnInfo<OrderToEcRequestInfo> verifyInfo = verifyOrderInfo(orderQueryRequest);
            if (verifyInfo == null) {
                serviceReturnInfo.setMessage("参数校验异常" + JSONObject.toJSONString(orderQueryRequest));
                return serviceReturnInfo;
            } else if (!verifyInfo.isIsok()) {
                serviceReturnInfo.setMessage(verifyInfo.getMessage());
                return serviceReturnInfo;
            }
            OrderToEcRequestInfo request = verifyInfo.getResult();
            if (request == null) {
                serviceReturnInfo.setMessage("推送Ec数据异常");
                return serviceReturnInfo;
            }

            //推送订单至Ec系统
            ServiceReturnInfo<String> sendResult = sendOrderConfirmToEc(request);
            if (sendResult == null) {
                serviceReturnInfo.setMessage("推送订单至Ec系统异常" + JSONObject.toJSONString(request));
                message = "订单推送Ec系统一小时达异常";
            } else if (!sendResult.isIsok()) {
                serviceReturnInfo.setMessage(sendResult.getMessage());
                message = "订单推送Ec系统一小时达失败 ：" + sendResult.getMessage();
            } else {
                serviceReturnInfo.setIsok(true);
                serviceReturnInfo.setMessage("推送成功");
                message = "订单推送Ec系统一小时达成功:" + sendResult.getMessage();
            }

        } catch (Exception e) {
            logger.error("推送已确认订单至Ec系统异常" + JSONObject.toJSONString(orderQueryRequest), e);
            serviceReturnInfo.setMessage("推送已确认订单至Ec系统异常");
        }

        logger.error("------推送已确认订单至Ec系统结束-----");

        //添加订单日志
        masterOrderActionService.insertOrderActionBySn(orderQueryRequest.getMasterOrderSn(), message, "system");
        return serviceReturnInfo;
    }

    /**
     * 推送订单到Ec系统
     * @param request
     * @return
     */
    private ServiceReturnInfo<String> sendOrderConfirmToEc(OrderToEcRequestInfo request) {
        ServiceReturnInfo<String> serviceReturnInfo = new ServiceReturnInfo<String>();

        try {
            //组装请求参数
            List<NameValuePair> paramList = billParam(request);

            //请求接口
            String url = ConfigCenter.getProperty(Constant.EC_YIXIAOSHIDA_SEND_ORDER_ADDRESS);

            logger.error("推送订单到Ec系统请求地址：" + url + ";请求参数：" + JSONObject.toJSONString(paramList));
            String result = HttpClientUtil.post(url, paramList);
            logger.error("推送订单到Ec系统请求返回结果：" + result);
            if (StringUtils.isEmpty(result) || JSONObject.parseObject(result, OrderToEcResult.class) == null) {
                serviceReturnInfo.setMessage("推送订单到Ec系统接口异常");
                return serviceReturnInfo;
            }

            //接口返回数据
            OrderToEcResult orderToEcResult = JSONObject.parseObject(result, OrderToEcResult.class);
            if ("api-success".equals(orderToEcResult.getStatus())) {
                //成功
                serviceReturnInfo.setIsok(true);
                serviceReturnInfo.setResult(orderToEcResult.getData());
            }
            serviceReturnInfo.setMessage(orderToEcResult.getMessage());
        } catch (Exception e) {
            logger.error("调用Ec系统接口异常" + JSONObject.toJSONString(request), e);
            serviceReturnInfo.setMessage("调用Ec系统接口异常");
        }
        return serviceReturnInfo;
    }

    /**
     * 填充请求参数
     * @return
     */
    private List<NameValuePair> billParam(OrderToEcRequestInfo request) {
        List<NameValuePair> list = new LinkedList<NameValuePair>();
        String key = ConfigCenter.getProperty(Constant.EC_YIXIAOSHIDA_KEY);
        list.add(HttpClientUtil.getNameValuePair("key", key));

        String requestTime = TimeUtil.getDate("yyyyMMddHHmmss");
        list.add(HttpClientUtil.getNameValuePair("requestTime", requestTime));

        String version = ConfigCenter.getProperty(Constant.EC_YIXIAOSHIDA_VERSION);
        list.add(HttpClientUtil.getNameValuePair("version", version));

        String serviceType = ConfigCenter.getProperty(Constant.EC_YIXIAOSHIDA_API);
        list.add(HttpClientUtil.getNameValuePair("serviceType", serviceType));

        String data = JSONObject.toJSONString(request);
        list.add(HttpClientUtil.getNameValuePair("data", data));

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
        List<NameValuePair> paramsList = new LinkedList<NameValuePair>();
        paramsList.addAll(list);

        String secret = ConfigCenter.getProperty(Constant.EC_YIXIAOSHIDA_SECRET);
        paramsList.add(2, HttpClientUtil.getNameValuePair("secret", secret));
        try {
            String formatParams = null;
            // 将参数进行utf-8编码
            if (null != paramsList && paramsList.size() > 0) {
                formatParams = HttpClientUtil.format(paramsList);
//                formatParams = URLEncodedUtils.format(paramsList, HttpClientUtil.CHARSET_ENCODING);
            }

            return Md5Util.encrypt(formatParams);
        } catch (Exception e) {
            logger.error("数据加密异常" + JSONObject.toJSONString(paramsList), e);
        }

        return null;
    }


    /**
     * 校验订单数据，并填充发送数据
     * @param orderQueryRequest
     * @return
     */
    private ServiceReturnInfo<OrderToEcRequestInfo> verifyOrderInfo(OrderQueryRequest orderQueryRequest) {
        ServiceReturnInfo<OrderToEcRequestInfo> serviceReturnInfo = new ServiceReturnInfo<OrderToEcRequestInfo>();
        if (orderQueryRequest == null) {
            serviceReturnInfo.setMessage("请求参数为空");
            return serviceReturnInfo;
        }

        String masterOrderSn = orderQueryRequest.getMasterOrderSn();
        if (StringUtils.isBlank(masterOrderSn)) {
            serviceReturnInfo.setMessage("主订单号不能为空为空");
            return serviceReturnInfo;
        }

        //主订单
        MasterOrderInfo masterOrderInfo = masterOrderInfoService.getOrderInfoBySn(masterOrderSn);
        if (masterOrderInfo == null) {
            serviceReturnInfo.setMessage("无" + masterOrderSn + "订单信息");
            return serviceReturnInfo;
        }
        if (masterOrderInfo.getOrderStatus() == 0 || masterOrderInfo.getOrderStatus() == 2) {
            serviceReturnInfo.setMessage(masterOrderSn + "订单未确认");
            return serviceReturnInfo;
        }

        //主支付单
        List<MasterOrderPay> masterOrderPayList = masterOrderPayService.getMasterOrderPayList(masterOrderSn);
        if (masterOrderPayList == null) {
            serviceReturnInfo.setMessage(masterOrderSn + "支付单不存在");
            return serviceReturnInfo;
        }
        MasterOrderPay masterOrderPay = masterOrderPayList.get(0);
        if (masterOrderInfo.getPayStatus() < 2 || masterOrderPay.getPayStatus() < 2) {
            serviceReturnInfo.setMessage(masterOrderSn + "订单未付款完成");
            return serviceReturnInfo;
        }

        //收货信息
        MasterOrderAddressInfo addressInfo = masterOrderAddressInfoService.selectAddressInfo(masterOrderSn);
        if (addressInfo == null) {
            serviceReturnInfo.setMessage(masterOrderSn + "订单无收货信息");
            return serviceReturnInfo;
        }

        //订单商品
        List<MasterOrderGoods> goodsList = masterOrderGoodsService.selectByMasterOrderSn(masterOrderSn);
        if (goodsList == null) {
            serviceReturnInfo.setMessage(masterOrderSn + "订单无商品");
            return serviceReturnInfo;
        }
        //合并订单商品
        List<OrderGoodsToEc> mergeGoodsList = mergeGoods(goodsList);

        //填充请求参数
        OrderToEcRequest orderToEcRequest = new OrderToEcRequest();
        orderToEcRequest.setItems(mergeGoodsList);
        //下单时间
        String addTime = TimeUtil.getTimestampByTen(masterOrderInfo.getAddTime());
        orderToEcRequest.setAdd_time(Integer.valueOf(addTime));
        //支付时间
        String payTime = TimeUtil.getTimestampByTen(masterOrderPay.getPayTime());
        orderToEcRequest.setPay_time(Integer.valueOf(payTime));
        orderToEcRequest.setOrder_sn(masterOrderSn);
        orderToEcRequest.setOrder_status(1);
        orderToEcRequest.setPay_status(2);
        orderToEcRequest.setConsignee(addressInfo.getConsignee());

        //省市区信息
        List<String> regionIds = new ArrayList<>(3);
        regionIds.add(addressInfo.getProvince());
        regionIds.add(addressInfo.getCity());
        regionIds.add(addressInfo.getDistrict());
        Map<String, SystemRegionArea> areaInfo = getAreaInfo(regionIds);
        if (areaInfo == null) {
            serviceReturnInfo.setMessage(masterOrderSn + "省市区为空");
            return serviceReturnInfo;
        }
        SystemRegionArea province = areaInfo.get(addressInfo.getProvince());
        SystemRegionArea city = areaInfo.get(addressInfo.getCity());
        SystemRegionArea district = areaInfo.get(addressInfo.getDistrict());
        orderToEcRequest.setProvince_name(province == null ? "" : province.getRegionName());
        orderToEcRequest.setCity_name(city == null ? "" : city.getRegionName());
        orderToEcRequest.setDistrict_name(district == null ? "" : district.getRegionName());

        orderToEcRequest.setAddress(addressInfo.getAddress());
        orderToEcRequest.setUser_name(masterOrderInfo.getUserName());
        orderToEcRequest.setMobile(addressInfo.getMobile());
        orderToEcRequest.setPos_code(masterOrderInfo.getOrderFrom());
        orderToEcRequest.setPay_code("weixin");
        //骑手配送
        orderToEcRequest.setShipping_code("QSKD");

        //运费
        BigDecimal shippingTotalFee = masterOrderInfo.getShippingTotalFee();
        if (shippingTotalFee != null) {
            orderToEcRequest.setShipping_fee(shippingTotalFee.toString());
        }

        //订单实付金额
        String orderAmount = "0.00";
        BigDecimal payTotalFee = masterOrderInfo.getPayTotalFee();
        if (payTotalFee != null) {
            orderAmount = payTotalFee.toString();
        }
        orderToEcRequest.setOrder_amount(orderAmount);

        //支付流水号
        String paySn = masterOrderPay.getPayNote();
        orderToEcRequest.setPay_sn(paySn);
        orderToEcRequest.setBuy_msg(masterOrderInfo.getPostscript());

        //订单级别折扣金额
        String otherDiscountFee = "0.00";
        BigDecimal totalFee = masterOrderInfo.getTotalFee();
        if (totalFee != null && payTotalFee != null) {
            otherDiscountFee = totalFee.subtract(payTotalFee).toString();
        }
        orderToEcRequest.setOther_discount_fee(otherDiscountFee);
        orderToEcRequest.setLine("1");

        List<OrderToEcRequest> orderToEcRequests = new ArrayList<OrderToEcRequest>();
        orderToEcRequests.add(orderToEcRequest);

        OrderToEcRequestInfo info = new OrderToEcRequestInfo();
        info.setTotal("1");
        info.setData(orderToEcRequests);
        serviceReturnInfo.setResult(info);
        serviceReturnInfo.setIsok(true);
        return serviceReturnInfo;
    }

    /**
     * 合并商品
     * @param goodsList
     * @return
     */
    private List<OrderGoodsToEc> mergeGoods(List<MasterOrderGoods> goodsList) {
        Map<String, List<MasterOrderGoods>> map = new HashMap<String, List<MasterOrderGoods>>();
        for (MasterOrderGoods goods : goodsList) {
            String customCode = goods.getCustomCode();
            if (StringUtils.isBlank(customCode)) {
                continue;
            }
            String extensionCode = goods.getExtensionCode();
            String key = customCode + "_" + extensionCode;
            List<MasterOrderGoods> values = map.get(key);
            if (values == null) {
                values = new ArrayList<MasterOrderGoods>();
            }
            values.add(goods);
            map.put(key, values);
        }
        if (map.size() < 1) {
            return null;
        }

        //填充ec系统数据
        List<OrderGoodsToEc> orderGoodsToEcs = new ArrayList<OrderGoodsToEc>();
        for (Map.Entry<String, List<MasterOrderGoods>> entry : map.entrySet()) {
            List<MasterOrderGoods> masterOrderGoodsList = entry.getValue();
            String key = entry.getKey();
            String[] split = key.split("_");
            String customCode = split[0];
            String extensionCode =split[1];

            int goodsNum = 0;
//            BigDecimal discountTotalFee = new BigDecimal(0);
            for (MasterOrderGoods goods : masterOrderGoodsList) {
                goodsNum += goods.getGoodsNumber();
//                BigDecimal disFee = new BigDecimal(goods.getDiscount()).multiply(new BigDecimal(goods.getGoodsNumber()));
//                discountTotalFee = discountTotalFee.add(disFee);
            }

            MasterOrderGoods goods = masterOrderGoodsList.get(0);

            OrderGoodsToEc orderGoodsToEc = new OrderGoodsToEc();
            String sku = goods.getSellerCustomCode();
            if (StringUtils.isBlank(sku)) {
                sku = customCode;
            }
            orderGoodsToEc.setSku(sku);
            BigDecimal marketPrice = goods.getMarketPrice();
            orderGoodsToEc.setMarket_price(marketPrice == null ? "" : marketPrice.toString());
            BigDecimal transactionPrice = goods.getTransactionPrice();
            orderGoodsToEc.setGoods_price(transactionPrice == null ? "" : transactionPrice.toString());
            orderGoodsToEc.setGoods_number(goodsNum);
            int isGift = 0;
            if ("gift".equals(extensionCode)) {
                isGift = 1;
            }
            orderGoodsToEc.setIs_gift(isGift);

//            BigDecimal discount = transactionPrice.divide(marketPrice);
//            orderGoodsToEc.setDiscount(discount.toString());
//
//            orderGoodsToEc.setDiscount_fee(discountTotalFee.toString());
            orderGoodsToEcs.add(orderGoodsToEc);
        }

        return orderGoodsToEcs;
    }

    /**
     * 获取省市区信息
     * @param regionIds
     * @return
     */
    private Map<String, SystemRegionArea> getAreaInfo(List<String> regionIds) {
        SystemRegionAreaExample example = new SystemRegionAreaExample();
        example.or().andRegionIdIn(regionIds);
        List<SystemRegionArea> systemRegionAreas = systemRegionAreaMapper.selectByExample(example);
        if (StringUtil.isListNull(systemRegionAreas)) {
            return null;
        }

        Map<String, SystemRegionArea> map = new HashMap<String, SystemRegionArea>();
        for (SystemRegionArea area : systemRegionAreas) {
            map.put(area.getRegionId(), area);
        }

        return map;
    }

}
