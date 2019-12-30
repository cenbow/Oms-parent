package com.work.shop.oms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.controller.feign.RewardPointGoodsFeign;
import com.work.shop.oms.controller.feign.UserPointFeign;
import com.work.shop.oms.controller.pojo.ChangeStockAndSaleVolumeBean;
import com.work.shop.oms.controller.pojo.ProductRewardPointGoodsBean;
import com.work.shop.oms.controller.pojo.UserShopPointBean;
import com.work.shop.oms.controller.pojo.UserShopPointsRequestBean;
import com.work.shop.oms.controller.service.OrderRewardPointGoodsService;
import com.work.shop.oms.controller.service.RewardPointRatioService;
import com.work.shop.oms.distribute.service.OrderDistributeService;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.order.service.OrderValidateService;
import com.work.shop.oms.order.service.SystemOrderSnService;
import com.work.shop.oms.param.bean.ParamOrderRewardPointGoods;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.CommonUtils;
import com.work.shop.oms.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * 订单创建接口
 *
 * @author lemon
 */
@Controller
public class CreateOrderController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String LOCK_ORDER_REWARD_COUNT = "LOCK_ORDER_REWARD_COUNT";

    @Resource(name = "masterOrderinfoServiceImpl")
    private MasterOrderInfoService masterOrderInfoService;

    @Resource
    private OrderValidateService orderValidateService;

    @Resource
    private OrderDistributeService orderDistributeService;

    @Resource
    private SystemOrderSnService systemOrderSnService;

    @Resource(name = "orderRewardPointGoodsService")
    private OrderRewardPointGoodsService orderRewardPointGoodsService;

    @Autowired
    private RewardPointRatioService rewardPointRatioService;

    @Autowired
    private RewardPointGoodsFeign rewardPointGoodsFeign;

    @Autowired
    private UserPointFeign userPointFeign;

    @Resource(name = "changeStockAndSalesVolumeJmsTemplate")
    private JmsTemplate changeStockAndSalesVolumeJmsTemplate;

    @Resource(name = "addRewardPointChangeLogJmsTemplate")
    private JmsTemplate addRewardPointChangeLogJmsTemplate;

    @Resource(name = "changeUserAndCompanyPointJmsTemplate")
    private JmsTemplate changeUserAndCompanyPointJmsTemplate;

    @Resource(name = "redisClient")
    private RedisClient redisClient;

    @RequestMapping(value = "/createOrder111")
    @ResponseBody
    public String createOrder(HttpServletRequest request, @RequestParam(value = "orderInfo", required = false, defaultValue = "") String orderParam) {
        logger.info("createOrder get create order request : " + orderParam);
        // 获取系统信息
        SystemInfo systemInfo = CommonUtils.getSystemInfo(request);
        ServiceReturnInfo<MasterOrder> validateinfo = orderValidateService.orderFormat(orderParam);
        // 验证不通过直接返回错误信息不会创建订单
        if (!validateinfo.isIsok()) {
            logger.error("validate error : " + validateinfo.getMessage() + orderParam);
            return orderValidateService.errorMessage(systemInfo, validateinfo);
        }
        MasterOrder masterOrder = validateinfo.getResult();
        OrderCreateReturnInfo returninfo = masterOrderInfoService.createOrder(masterOrder);
        if (returninfo.getIsOk() == Constant.OS_NO) {
            return errorMessage(systemInfo, returninfo);
        }
        return successMessage(systemInfo, returninfo);
    }

    @RequestMapping(value = "/createOrders111")
    @ResponseBody
    public String createOrders(HttpServletRequest request, @RequestParam(value = "orderInfos", required = false, defaultValue = "") String orderParam) {
        logger.info("createOrder get create order request : " + orderParam);
        // 获取系统信息
        SystemInfo systemInfo = CommonUtils.getSystemInfo(request);
        ServiceReturnInfo<List<MasterOrder>> validateinfo = orderValidateService.orderListFormat(orderParam);
        // 验证不通过直接返回错误信息不会创建订单
        if (!validateinfo.isIsok()) {
            logger.error("validate error : " + validateinfo.getMessage() + orderParam);
            return orderValidateService.errorMessage(systemInfo, validateinfo);
        }
        List<MasterOrder> masterOrders = validateinfo.getResult();
        OrdersCreateReturnInfo returninfo = masterOrderInfoService.createOrders(masterOrders);
        if (returninfo.getIsOk() == Constant.OS_NO) {
            return errorMessage(systemInfo, returninfo);
        }
        return successMessage(systemInfo, returninfo);
    }

    public String errorMessage(SystemInfo systemInfo, OrderCreateReturnInfo returninfo) {
        String s = JSON.toJSONString(returninfo);
        logger.error("订单生成：" + s);
        try {
            return URLEncoder.encode(s, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String successMessage(SystemInfo systemInfo, OrderCreateReturnInfo returninfo) {
		/*if (returninfo.getDepotInfos().isEmpty()) {
			returninfo.setDepotInfos(null);
		}*/
        String s = JSON.toJSONString(returninfo);
        logger.info("订单生成：" + s);
        try {
            return URLEncoder.encode(s, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    public String errorMessage(SystemInfo systemInfo, OrdersCreateReturnInfo returninfo) {
        String s = JSON.toJSONString(returninfo);
        logger.error("订单生成：" + s);
        try {
            return URLEncoder.encode(s, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String successMessage(SystemInfo systemInfo, OrdersCreateReturnInfo returninfo) {
        String s = JSON.toJSONString(returninfo);
        logger.info("订单生成：" + s);
        try {
            return URLEncoder.encode(s, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/orderDistribute.do")
    @ResponseBody
    public String orderDistribute(HttpServletRequest request, String masterOrderSn) {
        OrderDepotResult result = orderDistributeService.orderDistribute(masterOrderSn);
        return JSON.toJSONString(result);
    }

    //查询积分比例表
    @PostMapping(value = "/getRewardPointRatio")
    @ResponseBody
    public CommonResultData<Integer> getRewardPointRatio() {
        CommonResultData<Integer> result = new CommonResultData<>();
        result.setIsOk("0");

        int ratio = rewardPointRatioService.getRewardPointRatio();
        if (ratio > 0) {
            result.setIsOk("1");
            result.setResult(ratio);
        }
        return result;
    }

    //查询积分商品订单
    @PostMapping(value = "/getOrderRewardPoint")
    @ResponseBody
    public CommonResultData<List<ResultRewardPointGoodsBean>> getOrderRewardPoint(@RequestBody ParamOrderRewardPointGoods param) {
        CommonResultData<List<ResultRewardPointGoodsBean>> result = new CommonResultData<>();
        result.setIsOk("0");

        int count = orderRewardPointGoodsService.getCountOfOrderRewardPointGoods(param);
        if (count == 0) {
            result.setIsOk("1");
            result.setTotal(0);
            return result;
        }

        if (param.getPageSize() > 50 || param.getPageSize() == 0) {
            param.setPageSize(16);
        }

        int pageCount = (int) Math.ceil(count * 1.0 / param.getPageSize());
        if (param.getCurrentPage() > 1) {
            //查询最后一页的逻辑
            if (param.getCurrentPage() > pageCount) {
                result.setIsOk("1");
                result.setTotal(0);
                return result;
            } else if (param.getCurrentPage() == pageCount) {
                param.setStart(param.getPageSize() * (pageCount - 1));
                param.setPageSize(count - param.getPageSize() * (pageCount - 1));
            } else {
                param.setStart(param.getCurrentPage() * (param.getPageSize() - 1));
            }
        } else {
            param.setStart(0);
        }

        List<ResultRewardPointGoodsBean> resultData = orderRewardPointGoodsService.getOrderRewardPointGoods(param);
        if (resultData == null || resultData.size() == 0) {
            result.setMsg("查询积分商品订单出错！");
            return result;
        }

        result.setIsOk("1");
        result.setTotal(count);
        result.setResult(resultData);
        return result;
    }

    //创建积分商品订单
    @PostMapping(value = "/createOrderRewardPoint")
    @ResponseBody
    public CommonResultData<String> createOrderRewardPoint(@RequestBody ParamOrderRewardPointGoods param) {
        CommonResultData<String> result = new CommonResultData<>();
        result.setIsOk("0");

        if (StringUtils.isEmpty(param.getReceiverName()) || StringUtils.isEmpty(param.getReceiverTel()) || StringUtils.isEmpty(param.getReceiverAddress())) {
            result.setMsg("收货人、收货人电话、收货地址不能为空！");
            return result;
        }

        if (param.getDetailBeanList() == null || param.getDetailBeanList().size() == 0) {
            result.setMsg("积分商品订单明细不能为空！");
            return result;
        }

        List<String> goodsSNList = new ArrayList<>();
        for (int i = 0; i < param.getDetailBeanList().size(); i++) {
            if (param.getDetailBeanList().get(i).getGoodsSN() == null) {
                result.setMsg("积分商品编号不能为空！");
                return result;
            }
            goodsSNList.add(param.getDetailBeanList().get(i).getGoodsSN());
        }

        // 生成订单号
        ServiceReturnInfo<String> siSn = systemOrderSnService.createMasterOrderSn();
        if (!siSn.isIsok()) {
            logger.error("生成订单号失败" + siSn.getMessage());
            result.setMsg(siSn.getMessage());
            return result;
        }
        final String orderSN = "JF" + siSn.getResult();
        param.setOrderSN(orderSN);


        try {
            CommonResultData<List<ProductRewardPointGoodsBean>> beanList = rewardPointGoodsFeign.getRewardPointGoodsBySNList(goodsSNList);
            if (beanList == null || beanList.getResult() == null || beanList.getResult().size() == 0) {
                result.setMsg("积分商品查询为空！");
                return result;
            } else if (beanList.getResult().size() != goodsSNList.size()) {
                result.setMsg("积分商品查询出错！");
                return result;
            }
            List<ProductRewardPointGoodsBean> goodsList = beanList.getResult();

            int totalPoint = 0;
            for (int i = 0; i < goodsList.size(); i++) {
                if (goodsList.get(i).getGoodsStatus() != 1) {
                    result.setMsg("积分商品:" + goodsList.get(i).getGoodsName() + "已经下架，无法兑换");
                    return result;
                }

                Date now = new Date();
                if (goodsList.get(i).getBeginning() != null && goodsList.get(i).getValidity() != null) {
                    if (goodsList.get(i).getBeginning().getTime() > now.getTime() || goodsList.get(i).getValidity().getTime() < now.getTime()) {
                        result.setMsg("积分商品:" + goodsList.get(i).getGoodsName() + "已经过了活动时间，无法兑换");
                        return result;
                    }
                }

                //查找对应的积分商品信息并填充
                for (int j = 0; j < param.getDetailBeanList().size(); j++) {
                    if (goodsList.get(i).getGoodsSN().equals(param.getDetailBeanList().get(j).getGoodsSN())) {
                        if (param.getDetailBeanList().get(j).getSaleCount() <= 0) {
                            result.setMsg("积分商品:" + goodsList.get(i).getGoodsName() + "兑换数量必须为正数");
                            return result;
                        }

                        //获取锁定库存数量
                        int lockCount = Integer.parseInt(redisClient.get(LOCK_ORDER_REWARD_COUNT + "-" + param.getDetailBeanList().get(j).getGoodsSN()));
//                        logger.info("获取库存锁 lockCount:" + param.getDetailBeanList().get(j).getGoodsSN() + "   :   " + lockCount);

                        if (goodsList.get(i).getGoodsStock() - lockCount < param.getDetailBeanList().get(j).getSaleCount()) {
                            result.setMsg("积分商品:" + goodsList.get(i).getGoodsName() + "库存不足，无法兑换");
                            return result;
                        }

                        param.getDetailBeanList().get(j).setOrderSN(orderSN);
                        param.getDetailBeanList().get(j).setGoodsSN(goodsList.get(i).getGoodsSN());
                        param.getDetailBeanList().get(j).setGoodsName(goodsList.get(i).getGoodsName());
                        param.getDetailBeanList().get(j).setGoodsBrand(goodsList.get(i).getGoodsBrand());
                        param.getDetailBeanList().get(j).setNeedPoint(goodsList.get(i).getNeedPoint());
                        param.getDetailBeanList().get(j).setPictureURL(goodsList.get(i).getPictureURL());
                        totalPoint = totalPoint + goodsList.get(i).getNeedPoint() * param.getDetailBeanList().get(j).getSaleCount();

                        //加库存锁
                        redisClient.incrBy(LOCK_ORDER_REWARD_COUNT + "-" + param.getDetailBeanList().get(j).getGoodsSN(), param.getDetailBeanList().get(j).getSaleCount());
//                        logger.info("添加库存锁 lockCount:" + param.getDetailBeanList().get(j).getGoodsSN() + "   :   " + param.getDetailBeanList().get(j).getSaleCount());
                        break;
                    }
                }
            }

            //查询用户积分
            UserShopPointsRequestBean userShopPointsRequestBean = new UserShopPointsRequestBean();
            userShopPointsRequestBean.setAccountSN(param.getBuyerSN());
            CommonResultData<UserShopPointBean> userShopPointResult = userPointFeign.getUserPointByUserAccount(userShopPointsRequestBean);
            if (userShopPointResult == null) {
                result.setMsg("用户：" + param.getBuyerSN() + "的积分查询出错！");
                return result;
            } else if (userShopPointResult.getResult().getPoint() < totalPoint) {
                result.setMsg("用户：" + param.getBuyerSN() + "的积分小于" + totalPoint + ",无法兑换");
                return result;
            }

            param.setTotalPoint(totalPoint);
            orderRewardPointGoodsService.createOrderRewardPoint(param);
        } finally {
            for (int i = 0; i < param.getDetailBeanList().size(); i++) {
//                logger.info("释放库存锁 lockCount:" + param.getDetailBeanList().get(i).getGoodsSN() + "   :   " + param.getDetailBeanList().get(i).getSaleCount());
                if (redisClient.exists(LOCK_ORDER_REWARD_COUNT + "-" + param.getDetailBeanList().get(i).getGoodsSN())) {
                    redisClient.decrBy(LOCK_ORDER_REWARD_COUNT + "-" + param.getDetailBeanList().get(i).getGoodsSN(), param.getDetailBeanList().get(i).getSaleCount());
                }
            }
        }

        //下发"change_stock_and_salesvolumel"信道，修改商品库存和销量
        List<ChangeStockAndSaleVolumeBean> changeStockAndSaleVolumeBeanList = new ArrayList<>();
        for (int i = 0; i < param.getDetailBeanList().size(); i++) {
            ChangeStockAndSaleVolumeBean changeStockAndSaleVolumeBean = new ChangeStockAndSaleVolumeBean();
            changeStockAndSaleVolumeBean.setGoodsSN(param.getDetailBeanList().get(i).getGoodsSN());
            changeStockAndSaleVolumeBean.setChangeStock(0 - param.getDetailBeanList().get(i).getSaleCount());
            changeStockAndSaleVolumeBean.setChangeSalesVolume(param.getDetailBeanList().get(i).getSaleCount());
            changeStockAndSaleVolumeBeanList.add(changeStockAndSaleVolumeBean);
        }

        String changeStockAndSalesVolumeMQ = JSONObject.toJSONString(changeStockAndSaleVolumeBeanList);
        logger.info("修改积分商品库存和销量下发{}", changeStockAndSalesVolumeMQ);
        try {
            changeStockAndSalesVolumeJmsTemplate.send(new TextMessageCreator(changeStockAndSalesVolumeMQ));
        } catch (Exception e) {
            logger.error("下发修改积分商品库存和销量MQ信息异常{}", e.getMessage());
        }

        //下发"add_reward_point_change_log"信道，添加积分变更记录
        RewardPointChangeLogBean rewardPointChangeLogBean = new RewardPointChangeLogBean();
        rewardPointChangeLogBean.setAccountSN(param.getBuyerSN());
//        rewardPointChangeLogBean.setCompanySN(1);
        rewardPointChangeLogBean.setOrderSN(orderSN);
        rewardPointChangeLogBean.setDescription("兑换积分商品扣减积分");
        rewardPointChangeLogBean.setChangePoint(0 - param.getTotalPoint());

        String addRewardPointChangeLogMQ = JSONObject.toJSONString(rewardPointChangeLogBean);
        logger.info("添加积分变更记录下发{}", addRewardPointChangeLogMQ);
        try {
            addRewardPointChangeLogJmsTemplate.send(new TextMessageCreator(addRewardPointChangeLogMQ));
        } catch (Exception e) {
            logger.error("下发添加积分变更记录MQ信息异常{}", e.getMessage());
        }

        //下发"change_user_and_company_point"信道，修改用户和公司积分
        ChangeUserAndCompanyPointMQBean changeUserAndCompanyPointMQBean = new ChangeUserAndCompanyPointMQBean();
        changeUserAndCompanyPointMQBean.setAccountSN(param.getBuyerSN());
//        changeUserAndCompanyPointMQBean.setCompanySN(11);
        changeUserAndCompanyPointMQBean.setChangePoint(0 - param.getTotalPoint());

        String changeUserAndCompanyPointMQ = JSONObject.toJSONString(changeUserAndCompanyPointMQBean);
        logger.info("修改用户和公司积分下发{}", changeUserAndCompanyPointMQ);
        try {
            changeUserAndCompanyPointJmsTemplate.send(new TextMessageCreator(changeUserAndCompanyPointMQ));
        } catch (Exception e) {
            logger.error("下发修改用户和公司积分MQ信息异常{}", e.getMessage());
        }

        result.setIsOk("1");
        result.setResult(orderSN);
        return result;
    }

    //取消积分商品订单
    @RequestMapping(value = "/cancelOrderRewardPoint")
    @ResponseBody
    public CommonResultData<String> cancelOrderRewardPoint(@RequestBody ParamOrderRewardPointGoods param) {
        CommonResultData<String> result = new CommonResultData<>();
        result.setIsOk("0");

        if (StringUtils.isEmpty(param.getOrderSN())) {
            result.setMsg("订单参数不能为空！");
            return result;
        }

        List<ResultRewardPointGoodsBean> rewardPointGoodsBeanList = orderRewardPointGoodsService.getOrderRewardPointGoodsByOrderSN(param.getOrderSN());

        if (rewardPointGoodsBeanList == null) {
            result.setMsg("订单：" + param.getOrderSN() + "不存在！");
            return result;
        } else if (rewardPointGoodsBeanList.get(0).getOrderStatus() == 4) {
            result.setMsg("订单已经取消！");
            return result;
        }

        param.setCancelTime(new Date());
        orderRewardPointGoodsService.cancelOrderRewardPoint(param);

        //下发"change_stock_and_salesvolumel"信道，修改商品库存和销量
        List<ChangeStockAndSaleVolumeBean> changeStockAndSaleVolumeBeanList = new ArrayList<>();
        for (int i = 0; i < rewardPointGoodsBeanList.size(); i++) {
            ChangeStockAndSaleVolumeBean changeStockAndSaleVolumeBean = new ChangeStockAndSaleVolumeBean();
            changeStockAndSaleVolumeBean.setGoodsSN(rewardPointGoodsBeanList.get(i).getGoodsSN());
            changeStockAndSaleVolumeBean.setChangeStock(rewardPointGoodsBeanList.get(i).getSaleCount());
            changeStockAndSaleVolumeBean.setChangeSalesVolume(0 - rewardPointGoodsBeanList.get(i).getSaleCount());
            changeStockAndSaleVolumeBeanList.add(changeStockAndSaleVolumeBean);
        }

        String changeStockAndSalesVolumeMQ = JSONObject.toJSONString(changeStockAndSaleVolumeBeanList);
        logger.info("修改积分商品库存和销量下发{}:", changeStockAndSalesVolumeMQ);
        try {
            changeStockAndSalesVolumeJmsTemplate.send(new TextMessageCreator(changeStockAndSalesVolumeMQ));
        } catch (Exception e) {
            logger.error("下发修改积分商品库存和销量MQ信息异常{}", e.getMessage());
        }

        //下发"add_reward_point_change_log"信道，添加积分变更记录
        RewardPointChangeLogBean rewardPointChangeLogBean = new RewardPointChangeLogBean();
        rewardPointChangeLogBean.setAccountSN(rewardPointGoodsBeanList.get(0).getBuyerSN());
//        rewardPointChangeLogBean.setCompanySN(1);
        rewardPointChangeLogBean.setOrderSN(param.getOrderSN());
        rewardPointChangeLogBean.setDescription("取消积分商品订单增加积分");
        rewardPointChangeLogBean.setChangePoint(rewardPointGoodsBeanList.get(0).getTotalPoint());

        String addRewardPointChangeLogMQ = JSONObject.toJSONString(rewardPointChangeLogBean);
        logger.info("添加积分变更记录下发{}:", addRewardPointChangeLogMQ);
        try {
            addRewardPointChangeLogJmsTemplate.send(new TextMessageCreator(addRewardPointChangeLogMQ));
        } catch (Exception e) {
            logger.error("下发添加积分变更记录MQ信息异常{}", e.getMessage());
        }

        //下发"change_user_and_company_point"信道，修改用户和公司积分
        ChangeUserAndCompanyPointMQBean changeUserAndCompanyPointMQBean = new ChangeUserAndCompanyPointMQBean();
        changeUserAndCompanyPointMQBean.setAccountSN(rewardPointGoodsBeanList.get(0).getBuyerSN());
//        changeUserAndCompanyPointMQBean.setCompanySN(11);
        changeUserAndCompanyPointMQBean.setChangePoint(rewardPointGoodsBeanList.get(0).getTotalPoint());

        String changeUserAndCompanyPointMQ = JSONObject.toJSONString(changeUserAndCompanyPointMQBean);
        logger.info("修改用户和公司积分下发{}:", changeUserAndCompanyPointMQ);
        try {
            changeUserAndCompanyPointJmsTemplate.send(new TextMessageCreator(changeUserAndCompanyPointMQ));
        } catch (Exception e) {
            logger.error("下发修改用户和公司积分MQ信息异常{}", e.getMessage());
        }

        result.setIsOk("1");
        result.setResult("取消订单成功！");
        return result;
    }
}
