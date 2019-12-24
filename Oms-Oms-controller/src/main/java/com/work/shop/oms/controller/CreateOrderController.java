package com.work.shop.oms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.bean.ChangeUserAndCompanyPointMQBean;
import com.work.shop.oms.bean.OrderRewardPointGoodsDetailBean;
import com.work.shop.oms.bean.OrderRewardPointGoodsMasterBean;
import com.work.shop.oms.bean.RewardPointChangeLogBean;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.controller.feign.RewardPointGoodsFeign;
import com.work.shop.oms.controller.feign.UserPointFeign;
import com.work.shop.oms.controller.pojo.ChangeStockAndSaleVolumeBean;
import com.work.shop.oms.controller.pojo.ProductRewardPointGoodsBean;
import com.work.shop.oms.controller.pojo.UserShopPointBean;
import com.work.shop.oms.controller.pojo.UserShopPointsRequestBean;
import com.work.shop.oms.controller.service.OrderRewardPointGoodsService;
import com.work.shop.oms.distribute.service.OrderDistributeService;
import com.work.shop.oms.mq.bean.TextMessageCreator;
import com.work.shop.oms.order.service.MasterOrderInfoService;
import com.work.shop.oms.order.service.OrderValidateService;
import com.work.shop.oms.order.service.SystemOrderSnService;
import com.work.shop.oms.param.bean.ParamOrderRewardPointGoods;
import com.work.shop.oms.utils.CommonUtils;
import com.work.shop.oms.utils.Constant;
import com.work.shop.pca.common.ResultData;
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
import java.util.List;


/**
 * 订单创建接口
 *
 * @author lemon
 */
@Controller
public class CreateOrderController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
    private RewardPointGoodsFeign rewardPointGoodsFeign;

    @Autowired
    private UserPointFeign userPointFeign;

    @Resource(name = "changeStockAndSalesVolumeJmsTemplate")
    private JmsTemplate changeStockAndSalesVolumeJmsTemplate;

    @Resource(name = "addRewardPointChangeLogJmsTemplate")
    private JmsTemplate addRewardPointChangeLogJmsTemplate;

    @Resource(name = "changeUserAndCompanyPointJmsTemplate")
    private JmsTemplate changeUserAndCompanyPointJmsTemplate;

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

    //查询积分商品订单主表
    @PostMapping(value = "/getOrderRewardPointMaster")
    @ResponseBody
    public ResultData<List<OrderRewardPointGoodsMasterBean>> getOrderRewardPointMaster(@RequestBody ParamOrderRewardPointGoods param) {
        ResultData<List<OrderRewardPointGoodsMasterBean>> result = new ResultData<>();
        result.setIsOk(0);

        if (!StringUtils.isEmpty(param.getOrder()) && (!"order_status".equals(param.getOrder()) || !("create_time".equals(param.getOrder())))) {
            result.setMsg("排序条件参数错误！");
            return result;
        } else if (!StringUtils.isEmpty(param.getSort()) && !"desc".equals(param.getSort()) && !"asc".equals(param.getSort())) {
            result.setMsg("排序参数错误！");
            return result;
        }

        int count = orderRewardPointGoodsService.getCountOfOrderRewardPointGoodsMaster(param);
        if (count == 0) {
            result.setIsOk(1);
            result.setTotalCount(0);
            return result;
        }

        if (param.getPageSize() > 50 || param.getPageSize() == 0) {
            param.setPageSize(16);
        }

        int pageCount = (int) Math.ceil(count * 1.0 / param.getPageSize());
        if (param.getCurrentPage() > 1) {
            //查询最后一页的逻辑
            if (param.getCurrentPage() > pageCount) {
                result.setIsOk(1);
                result.setTotalCount(0);
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


        List<OrderRewardPointGoodsMasterBean> resultList = orderRewardPointGoodsService.getOrderRewardPointGoodsMaster(param);
        result.setIsOk(1);
        result.setData(resultList);
        return result;
    }

    //查询积分商品订单明细表
    @PostMapping(value = "/getOrderRewardPointDetail")
    @ResponseBody
    public ResultData<List<OrderRewardPointGoodsDetailBean>> getOrderRewardPointDetail(@RequestBody ParamOrderRewardPointGoods param) {
        ResultData<List<OrderRewardPointGoodsDetailBean>> result = new ResultData<>();
        result.setIsOk(0);

        logger.info("orderSN :" + param.getOrderSN());
        List<OrderRewardPointGoodsDetailBean> resultList = orderRewardPointGoodsService.getOrderRewardPointGoodsDetail(param.getOrderSN());
        if (resultList == null || resultList.size() == 0) {
            result.setMsg("查询积分商品订单明细表出错！");
            return result;
        }

        result.setIsOk(1);
        result.setData(resultList);
        result.setTotalCount(resultList.size());
        return result;
    }

    //创建积分商品订单
    @PostMapping(value = "/createOrderRewardPoint")
    @ResponseBody
    public ResultData<String> createOrderRewardPoint(@RequestBody ParamOrderRewardPointGoods param) {
        ResultData<String> result = new ResultData<>();
        result.setIsOk(0);

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

        ResultData<List<ProductRewardPointGoodsBean>> beanList = rewardPointGoodsFeign.getRewardPointGoodsBySNList(goodsSNList);
        if (beanList == null || beanList.getData() == null || beanList.getData().size() == 0) {
            result.setMsg("积分商品查询为空！");
            return result;
        } else if (beanList.getData().size() != goodsSNList.size()) {
            result.setMsg("积分商品查询出错！");
            return result;
        }
        List<ProductRewardPointGoodsBean> goodsList = beanList.getData();

        int totalPoint = 0;
        for (int i = 0; i < goodsList.size(); i++) {
            if (goodsList.get(i).getGoodsStatus() != 1) {
                result.setMsg("积分商品编号" + goodsList.get(i).getGoodsSN() + "的商品已经下架，无法兑换");
                return result;
            }
            if (goodsList.get(i).getGoodsStock() < param.getDetailBeanList().get(i).getSaleCount()) {
                result.setMsg("积分商品编号" + goodsList.get(i).getGoodsSN() + "的商品库存不足，无法兑换");
                return result;
            }

            Date now = new Date();
            if (goodsList.get(i).getBeginning().getTime() > now.getTime() || goodsList.get(i).getValidity().getTime() < now.getTime()) {
                result.setMsg("积分商品编号" + goodsList.get(i).getGoodsSN() + "的商品已经过了活动时间，无法兑换");
                return result;
            }
            param.getDetailBeanList().get(i).setGoodsSN(goodsList.get(i).getGoodsSN());
            param.getDetailBeanList().get(i).setGoodsName(goodsList.get(i).getGoodsName());
            param.getDetailBeanList().get(i).setGoodsBrand(goodsList.get(i).getGoodsBrand());
            param.getDetailBeanList().get(i).setNeedPoint(goodsList.get(i).getNeedPoint());
            totalPoint = totalPoint + goodsList.get(i).getNeedPoint() * param.getDetailBeanList().get(i).getSaleCount();
        }

        //查询用户积分
        UserShopPointsRequestBean userShopPointsRequestBean = new UserShopPointsRequestBean();
        userShopPointsRequestBean.setAccountSN(param.getBuyerSN());
        ResultData<UserShopPointBean> userShopPointResult = userPointFeign.getUserPointByUserAccount(userShopPointsRequestBean);
        if (userShopPointResult == null) {
            result.setMsg("用户：" + param.getBuyerSN() + "的积分查询出错！");
            return result;
        } else if (userShopPointResult.getData().getPoint() < totalPoint) {
            result.setMsg("用户：" + param.getBuyerSN() + "的积分小于" + totalPoint + ",无法兑换");
            return result;
        }

        ServiceReturnInfo<String> siSn = systemOrderSnService.createMasterOrderSn();

        // 生成订单号失败
        if (!siSn.isIsok()) {
            logger.error("生成订单号失败" + siSn.getMessage());
            result.setMsg(siSn.getMessage());
            return result;
        }
        final String orderSn = "JF" + siSn.getResult();
        param.setOrderSN(orderSn);
        param.setTotalPoint(totalPoint);

        for (int i = 0; i < param.getDetailBeanList().size(); i++) {
            param.getDetailBeanList().get(i).setOrderSN(orderSn);
        }

        orderRewardPointGoodsService.createOrderRewardPoint(param);

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
        logger.info("修改积分商品库存和销量下发{}:", changeStockAndSalesVolumeMQ);
        try {
            changeStockAndSalesVolumeJmsTemplate.send(new TextMessageCreator(changeStockAndSalesVolumeMQ));
        } catch (Exception e) {
            logger.error("下发修改积分商品库存和销量MQ信息异常", e.getMessage());
        }

        //下发"add_reward_point_change_log"信道，添加积分变更记录
        RewardPointChangeLogBean rewardPointChangeLogBean = new RewardPointChangeLogBean();
        rewardPointChangeLogBean.setAccountSN(param.getBuyerSN());
//        rewardPointChangeLogBean.setCompanySN(1);
        rewardPointChangeLogBean.setOrderSN(orderSn);
        rewardPointChangeLogBean.setDescrption("积分商品兑换");
        rewardPointChangeLogBean.setChangePoint(0 - param.getTotalPoint());

        String addRewardPointChangeLogMQ = JSONObject.toJSONString(rewardPointChangeLogBean);
        logger.info("添加积分变更记录下发{}:", addRewardPointChangeLogMQ);
        try {
            addRewardPointChangeLogJmsTemplate.send(new TextMessageCreator(addRewardPointChangeLogMQ));
        } catch (Exception e) {
            logger.error("下发添加积分变更记录MQ信息异常", e.getMessage());
        }

        //下发"change_user_and_company_point"信道，修改用户和公司积分
        ChangeUserAndCompanyPointMQBean changeUserAndCompanyPointMQBean = new ChangeUserAndCompanyPointMQBean();
        changeUserAndCompanyPointMQBean.setAccountSN(param.getBuyerSN());
//        changeUserAndCompanyPointMQBean.setCompanySN(11);
        changeUserAndCompanyPointMQBean.setChangePoint(0 - param.getTotalPoint());

        String changeUserAndCompanyPointMQ = JSONObject.toJSONString(changeUserAndCompanyPointMQBean);
        logger.info("修改用户和公司积分下发{}:", changeUserAndCompanyPointMQ);
        try {
            changeUserAndCompanyPointJmsTemplate.send(new TextMessageCreator(changeUserAndCompanyPointMQ));
        } catch (Exception e) {
            logger.error("下发修改用户和公司积分MQ信息异常", e.getMessage());
        }

        result.setIsOk(1);
        result.setData(orderSn);
        return result;
    }

    //取消积分商品订单
    @RequestMapping(value = "/cancelOrderRewardPoint")
    @ResponseBody
    public ResultData<String> cancelOrderRewardPoint(@RequestBody ParamOrderRewardPointGoods param) {
        ResultData<String> result = new ResultData<>();
        result.setIsOk(0);

        if (StringUtils.isEmpty(param.getOrderSN())) {
            result.setMsg("订单参数不能为空！");
            return result;
        } else {
            OrderRewardPointGoodsMasterBean orderRewardPointGoods = orderRewardPointGoodsService.getOrderRewardPointGoodsByOrderSN(param.getOrderSN());
            if (orderRewardPointGoods == null) {
                result.setMsg("订单:" + param.getOrderSN() + "不存在！");
                return result;
            } else if (orderRewardPointGoods.getOrderStatus() >= 2) {
                result.setMsg("订单已发货无法取消！");
                return result;
            }
        }

        param.setCancelTime(new Date());
        orderRewardPointGoodsService.cancelOrderRewardPoint(param);

        //下发"change_stock_and_salesvolumel"信道，修改商品库存和销量
        List<ChangeStockAndSaleVolumeBean> changeStockAndSaleVolumeBeanList = new ArrayList<>();
        for (int i = 0; i < param.getDetailBeanList().size(); i++) {
            ChangeStockAndSaleVolumeBean changeStockAndSaleVolumeBean = new ChangeStockAndSaleVolumeBean();
            changeStockAndSaleVolumeBean.setGoodsSN(param.getDetailBeanList().get(i).getGoodsSN());
            changeStockAndSaleVolumeBean.setChangeStock(param.getDetailBeanList().get(i).getSaleCount());
            changeStockAndSaleVolumeBean.setChangeSalesVolume(0 - param.getDetailBeanList().get(i).getSaleCount());
            changeStockAndSaleVolumeBeanList.add(changeStockAndSaleVolumeBean);
        }

        String changeStockAndSalesVolumeMQ = JSONObject.toJSONString(changeStockAndSaleVolumeBeanList);
        logger.info("修改积分商品库存和销量下发{}:", changeStockAndSalesVolumeMQ);
        try {
            changeStockAndSalesVolumeJmsTemplate.send(new TextMessageCreator(changeStockAndSalesVolumeMQ));
        } catch (Exception e) {
            logger.error("下发修改积分商品库存和销量MQ信息异常", e.getMessage());
        }

        //下发"add_reward_point_change_log"信道，添加积分变更记录
        RewardPointChangeLogBean rewardPointChangeLogBean = new RewardPointChangeLogBean();
        rewardPointChangeLogBean.setAccountSN(param.getBuyerSN());
//        rewardPointChangeLogBean.setCompanySN(1);
        rewardPointChangeLogBean.setOrderSN(param.getOrderSN());
        rewardPointChangeLogBean.setDescrption("积分商品订单取消");
        rewardPointChangeLogBean.setChangePoint(param.getTotalPoint());

        String addRewardPointChangeLogMQ = JSONObject.toJSONString(rewardPointChangeLogBean);
        logger.info("添加积分变更记录下发{}:", addRewardPointChangeLogMQ);
        try {
            addRewardPointChangeLogJmsTemplate.send(new TextMessageCreator(addRewardPointChangeLogMQ));
        } catch (Exception e) {
            logger.error("下发添加积分变更记录MQ信息异常", e.getMessage());
        }

        //下发"change_user_and_company_point"信道，修改用户和公司积分
        ChangeUserAndCompanyPointMQBean changeUserAndCompanyPointMQBean = new ChangeUserAndCompanyPointMQBean();
        changeUserAndCompanyPointMQBean.setAccountSN(param.getBuyerSN());
//        changeUserAndCompanyPointMQBean.setCompanySN(11);
        changeUserAndCompanyPointMQBean.setChangePoint(param.getTotalPoint());

        String changeUserAndCompanyPointMQ = JSONObject.toJSONString(changeUserAndCompanyPointMQBean);
        logger.info("修改用户和公司积分下发{}:", changeUserAndCompanyPointMQ);
        try {
            changeUserAndCompanyPointJmsTemplate.send(new TextMessageCreator(changeUserAndCompanyPointMQ));
        } catch (Exception e) {
            logger.error("下发修改用户和公司积分MQ信息异常", e.getMessage());
        }

        result.setIsOk(1);
        result.setData("取消订单成功！");
        return result;
    }
}
