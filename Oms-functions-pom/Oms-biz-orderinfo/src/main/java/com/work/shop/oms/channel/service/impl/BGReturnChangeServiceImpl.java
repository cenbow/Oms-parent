package com.work.shop.oms.channel.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.api.bean.*;
import com.work.shop.oms.api.orderinfo.service.BGReturnChangeService;
import com.work.shop.oms.api.param.bean.CreateGoodsReturnChange;
import com.work.shop.oms.api.param.bean.GoodsReturnChangeDetailInfo;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.api.param.bean.ReturnChangeGoodsBean;
import com.work.shop.oms.bean.*;
import com.work.shop.oms.common.bean.*;
import com.work.shop.oms.dao.*;
import com.work.shop.oms.dao.define.DefineOrderMapper;
import com.work.shop.oms.dao.define.GoodsReturnChangePageListMapper;
import com.work.shop.oms.dao.define.GoodsReturnChangeStMapper;
import com.work.shop.oms.dao.define.OrderReturnSearchMapper;
import com.work.shop.oms.orderop.service.OrderQuestionService;
import com.work.shop.oms.utils.*;
import com.work.shop.oms.utils.TimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static javafx.scene.input.KeyCode.R;

@Service("bGReturnChangeServiceImpl")
public class BGReturnChangeServiceImpl implements BGReturnChangeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GoodsReturnChangeMapper goodsReturnChangeMapper;

    @Resource
    private GoodsReturnChangeActionMapper goodsReturnChangeActionMapper;
    @Resource
    private MasterOrderInfoMapper masterOrderInfoMapper;
    @Resource
    private OrderQuestionService orderQuestionService;
    @Resource
    private OrderReturnMapper orderReturnMapper;
    @Resource
    private OrderReturnShipMapper orderReturnShipMapper;
    @Resource
    private OrderReturnGoodsMapper orderReturnGoodsMapper;
    @Resource
    private GoodsReturnChangePageListMapper goodsReturnChangePageListMapper;

    @Resource
    private OrderReturnSearchMapper orderReturnSearchMapper;

    @Resource
    private SystemRegionAreaMapper systemRegionAreaMapper;
    @Resource
    DefineOrderMapper defineOrderMapper;
    @Resource
    private MasterOrderGoodsMapper masterOrderGoodsMapper;
    @Resource
    private MasterOrderGoodsHistoryMapper masterOrderGoodsHistoryMapper;
    @Resource
    private MasterOrderInfoHistoryMapper masterOrderInfoHistoryMapper;
    @Resource
    private OrderDistributeMapper orderDistributeMapper;
    @Resource
    private OrderDistributeHistoryMapper orderDistributeHistoryMapper;
    @Resource
    private MasterOrderAddressInfoMapper masterOrderAddressInfoMapper;
    @Resource
    private MasterOrderAddressInfoHistoryMapper masterOrderAddressInfoHistoryMapper;
    @Resource
    private GoodsReturnChangeDetailMapper goodsReturnChangeDetailMapper;
    @Resource
    private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;

    @Resource(name="goodsReturnChangeStMapper")
    private GoodsReturnChangeStMapper goodsReturnChangeStMapper;

    /**
     * 创建订单商品退换货信息
     * @param actionUser 操作人
     * @param actionNote 操作备注helper
     * @param content 申请内容
     * @param siteCode 站点编码
     * @return ReturnInfo
     */
    @Override
    public ReturnInfo<Boolean> createGoodsReturnChange(String actionUser, String actionNote, String content, String siteCode) {
        logger.info("创建订单商品退换货信息开始:actionUser="+actionUser+";actionNote=" + actionNote+";content="+content +";siteCode="+siteCode);
        ReturnInfo<Boolean> ri = new ReturnInfo<Boolean>(Constant.OS_NO);
        if (StringUtil.isEmpty(content)) {
            ri.setMessage("参数为空！");
            return ri;
        }
        List<GoodsReturnChange> returnChanges = null;
        try {
            returnChanges = JSON.parseArray(content, GoodsReturnChange.class);
            if (!StringUtil.isNotNullForList(returnChanges)) {
                logger.error("参数为空！");
                ri.setMessage("参数为空！");
                return ri;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("接受参数转Json对象异常：" , e);
            ri.setMessage("接受参数转Json对象异常：" + e.getMessage());
            return ri;
        }
        try {
            for (GoodsReturnChange returnChange : returnChanges) {
                String orderSn = returnChange.getOrderSn();
                if (null == orderSn || StringUtil.isEmpty(orderSn)) {
                    logger.error("订单编号不能为空！");
                    ri.setMessage("订单编号不能为空！");
                    return ri;
                }
                ri.setOrderSn(orderSn);
                logger.info("创建订单["+ orderSn +"]商品退换货信息开始！");
                MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
                if (null == masterOrderInfo) {
                    ri.setMessage("没有获取订单" + orderSn + "的信息！不能进行创建订单商品退换货操作！");
                    logger.error("没有获取订单" + orderSn + "的信息！不能进行创建订单商品退换货操作！");
                    return ri;
                }
                MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(orderSn);
                if (null == extend) {
                    ri.setMessage("没有获取订单" + orderSn + "扩展表信息！不能进行创建订单商品退换货操作！");
                    logger.error("没有获取订单" + orderSn + "扩展表信息！不能进行创建订单商品退换货操作！");
                    return ri;
                }
                if (StringUtil.isEmpty(masterOrderInfo.getUserId())) {
                    ri.setMessage("会员ID为空！不能进行创建订单商品退换货操作！");
                    logger.error("会员ID为空！不能进行创建订单商品退换货操作！");
                    return ri;
                }
                if (null == returnChange.getReturnType()) {
                    ri.setMessage("创建订单商品退换货类型不能为空！不能进行创建订单商品退换货操作！");
                    logger.error("创建订单商品退换货类型不能为空！不能进行创建订单商品退换货操作！");
                    return ri;
                }
                if (null == returnChange.getReason()) {
                    ri.setMessage("创建订单商品退换货原因不能为空！不能进行创建订单商品退换货操作！");
                    logger.error("创建订单商品退换货原因不能为空！不能进行创建订单商品退换货操作！");
                    return ri;
                }

                returnChange.setCreate(new Date());
                String mobile = returnChange.getContactMobile();
                returnChange.setContactMobile(mobile);
                //平台生成的申请退换货单加上申请单号
                for (GoodsReturnChange goodsReturnChange : returnChanges) {
                    if (StringUtil.isBlank(goodsReturnChange.getReturnchangeSn())) {
                        goodsReturnChange.setReturnchangeSn("HS" + DateTimeUtils.format(new Date(), DateTimeUtils.YYYYMMDDHHmmss));
                    }
                }
                returnChange.setShopCode(masterOrderInfo.getOrderFrom());
                returnChange.setShopName(masterOrderInfo.getShopName());
                returnChange.setSiteCode(siteCode);
                returnChange.setStoreCode(extend.getShopCode());
                returnChange.setStoreName(extend.getShopName());
                goodsReturnChangeMapper.insertSelective(returnChange);
                GoodsReturnChangeAction action = new GoodsReturnChangeAction();
                action.setActionUser(actionUser);
                // 待沟通
                action.setStatus(1);
                action.setOrderSn(orderSn);
                action.setActionNote("提交：" + actionNote);
                action.setLogTime(new Date());
                action.setReturnchangeSn(returnChange.getReturnchangeSn());
                goodsReturnChangeActionMapper.insert(action);
                //如果未发货 备货中 部分发货 则可以设为问题单
                if (masterOrderInfo.getShipStatus() == 0 || masterOrderInfo.getShipStatus() == 1 || masterOrderInfo.getShipStatus() == 2) {
                    // 19 问题单类型  退单转问题单
                    OrderStatus orderStatus = new OrderStatus();
                    orderStatus.setMasterOrderSn(orderSn);
                    orderStatus.setMessage("退换货申请设问题单！");
                    orderStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.FRONT);
                    orderStatus.setCode("19");
                    ReturnInfo returnInfo_q = orderQuestionService.questionOrderByMasterSn(orderSn, orderStatus);
                    logger.info("创建订单["+ orderSn +"]商品退换货信息完成！"+returnInfo_q.getMessage());
                }
            }
            ri.setIsOk(Constant.OS_YES);
            ri.setData(true);
            ri.setMessage("创建订单商品退换货记录成功！");
            logger.info("创建订单商品退换货信息完成！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建订单商品退换货信息异常",e);
            ri.setMessage("创建订单商品退换货信息异常：" + e.getMessage());
        }
        return ri;
    }

    /**
     * 退货信息设置
     * @param returnSn 退单编码
     * @param returnExpress 退货快递
     * @param returnInvoiceNo 退货单号
     * @param returnExpressImg 退货图片
     * @param siteCode 站点编码
     * @return ReturnInfo<Boolean>
     */
    @Override
    public ReturnInfo<Boolean> setOrderReturnShipInfo(String returnSn, String returnExpress,
                                             String returnInvoiceNo,String returnExpressImg, String siteCode) {
        logger.info("退回回寄快递消息获取接口：,returnSn="+returnSn+",returnExpress="+returnExpress
            +",returnInvoiceNo="+returnInvoiceNo +";siteCode="+ siteCode);
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>();
        if (StringUtil.isBlank(returnSn)) {
            returnInfo.setMessage("returnSn不能为空！");
            return returnInfo;
        }
        try {
            OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
            if (orderReturnShip == null) {
                returnInfo.setMessage("没有找到对应的退单！");
                return returnInfo;
            }
            OrderReturnShip orderReturnShipUpdate = new OrderReturnShip();
            orderReturnShipUpdate.setRelatingReturnSn(returnSn);
            orderReturnShipUpdate.setReturnExpress(returnExpress);
            orderReturnShipUpdate.setReturnInvoiceNo(returnInvoiceNo);
            orderReturnShipUpdate.setReturnExpressImg(returnExpressImg);
            orderReturnShipMapper.updateByPrimaryKeySelective(orderReturnShipUpdate);
            logger.info("保存退货回寄快递消息成功！");
            returnInfo.setIsOk(1);
            returnInfo.setData(true);
        } catch (Exception e) {
            logger.error("保存退货回寄快递消息失败！", e);
            returnInfo.setMessage("保存退货回寄快递消息失败！"+e.toString());
        }
        return returnInfo;
    }

    /**
     * 取消申请单
     * @param channelCode 店铺编码
     * @param returnChangeSn 申请单号
     * @param siteCode 站点编码
     * @return GoodsReturnChangeReturnInfo
     */
    @Override
    public GoodsReturnChangeReturnInfo cancelGoodsReturnChange(String channelCode, String returnChangeSn, String siteCode) {
        logger.info("渠道请求取消api调用开始:returnChangeSn=" + returnChangeSn + "渠道号：" + channelCode + ";siteCode=" + siteCode);
        GoodsReturnChangeReturnInfo goodsReturnChangeReturnInfo = new GoodsReturnChangeReturnInfo();
        if (channelCode == null||channelCode.length() == 0) {
            goodsReturnChangeReturnInfo.setIsOK(Constant.YESORNO_NO);
            goodsReturnChangeReturnInfo.setMessage("请传入渠道号！");
            return goodsReturnChangeReturnInfo;
        }
        if (returnChangeSn == null || returnChangeSn.length() == 0) {
            goodsReturnChangeReturnInfo.setIsOK(Constant.YESORNO_NO);
            goodsReturnChangeReturnInfo.setMessage("请传入申请单号！");
            return goodsReturnChangeReturnInfo;
        }
        try {
            GoodsReturnChangeExample example = new GoodsReturnChangeExample();
            GoodsReturnChangeExample.Criteria criteria = example.or();
            criteria.andReturnchangeSnEqualTo(returnChangeSn);
            GoodsReturnChange goodsReturnChange = new GoodsReturnChange();
            //0 为已取消
            goodsReturnChange.setStatus(0);
            List<GoodsReturnChange> list = goodsReturnChangeMapper.selectByExample(example);
            if (null == list || list.size() == 0) {
                goodsReturnChangeReturnInfo.setIsOK(Constant.YESORNO_NO);
                goodsReturnChangeReturnInfo.setMessage("没有找到该申请单信息！");
                goodsReturnChangeReturnInfo.setReturnChangeSn(returnChangeSn);
                logger.info("取消申请单api调用结束：没有找到该申请单信息！");
                return goodsReturnChangeReturnInfo;
            }
            goodsReturnChangeMapper.updateByExampleSelective(goodsReturnChange, example);
            GoodsReturnChangeAction action = new GoodsReturnChangeAction();
            action.setActionUser(channelCode);
            action.setStatus(0);
            action.setOrderSn(list.get(0).getOrderSn());
            action.setActionNote("系统提交：渠道"+channelCode+"请求取消申请单！");
            action.setLogTime(new Date());
            action.setReturnchangeSn(returnChangeSn);
            goodsReturnChangeActionMapper.insert(action);
            goodsReturnChangeReturnInfo.setIsOK(Constant.YESORNO_YES);
            goodsReturnChangeReturnInfo.setReturnChangeSn(returnChangeSn);
            //0 表示os问题单
            logger.info("渠道请求取消api调用成功!");
        } catch (Exception e) {
            goodsReturnChangeReturnInfo.setIsOK(0);
            goodsReturnChangeReturnInfo.setMessage("取消申请单异常！" + e.toString());
            goodsReturnChangeReturnInfo.setReturnChangeSn(returnChangeSn);
            logger.info("渠道请求取消api调用结束异常:" + e.toString());
        }
        return goodsReturnChangeReturnInfo;
    }

    /**
     * 前台退换货申请单列表获得数据接口
     * @param searchParam 查询对象
     * @param siteCode 站点编码
     * @return ApiReturnData<Paging<GoodsReturnPageInfo>>
     */
    @Override
    public ApiReturnData<Paging<GoodsReturnPageInfo>> getBGGoodsReturnPageList(String searchParam, String siteCode) {
        ApiReturnData<Paging<GoodsReturnPageInfo>> apiReturnData = new ApiReturnData<Paging<GoodsReturnPageInfo>>();
        apiReturnData.setIsOk(Constant.OS_STR_NO);
        logger.info("用户查询退换货申请单列表："+searchParam + ";siteCode=" + siteCode);

        try {
            PageListParam bGListParam = new PageListParam();
            bGListParam = JSON.parseObject(searchParam, PageListParam.class);
            Map<String, Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
            if (StringUtil.isNotNull(bGListParam.getIsHistory())) {
                params.put("isHistory", bGListParam.getIsHistory());
            } else {
                params.put("isHistory","0");
            }
            if (bGListParam.getPageNum() == 0) {
                bGListParam.setPageNum(1);
            }
            if (bGListParam.getPageSize() == 0) {
                bGListParam.setPageSize(10);
            }
            params.put("start", (bGListParam.getPageNum() - 1) * bGListParam.getPageSize());
            params.put("pageSize", bGListParam.getPageSize());
            params.put("userId", bGListParam.getUserId());
            params.put("siteCode", siteCode);
            if (bGListParam.getRstatus() >= 0) {
                params.put("rStatus", bGListParam.getRstatus());
            }
            if (StringUtil.isNotNull(bGListParam.getGoodsSn())) {
                params.put("goodsSn", bGListParam.getGoodsSn());
            }
            if (StringUtils.isNotBlank(bGListParam.getOrderSn())) {
                params.put("orderSn", bGListParam.getOrderSn());
            }
            if (bGListParam.getReturnType() != null) {
                params.put("returnType", bGListParam.getReturnType());
            }
            //申请单号查询
            if (StringUtils.isNotBlank(bGListParam.getOrderReturnSn())) {
                params.put("changeSn", bGListParam.getOrderReturnSn());
            }

            List<GoodsReturnPageInfo> rList = goodsReturnChangePageListMapper.selectByExampleMall(params);
            if (rList == null || rList.size() == 0 || rList.get(0) == null) {
                apiReturnData.setMessage("没有找到对应的申请单！");
                return apiReturnData;
            }

            List<String> changeSnList = new ArrayList<String>();
            for (GoodsReturnPageInfo info : rList) {
                changeSnList.add(info.getGoodsReturnChangeSn());
            }

            //填充申请商品信息
            Map<String, List<GoodsReturnChangeDetailBean>> detailMap = getDetailList(changeSnList);
            if (detailMap != null && detailMap.size() > 1) {
                for (GoodsReturnPageInfo info : rList) {
                    String changeSn = info.getGoodsReturnChangeSn();
                    if (StringUtil.isListNotNull(detailMap.get(changeSn))) {
                        info.setDetails(detailMap.get(changeSn));
                    }
                }
            }

            int count = goodsReturnChangePageListMapper.countByExampleMall(params);
            Paging<GoodsReturnPageInfo> paging = new Paging(count, rList);
            apiReturnData.setData(paging);
            apiReturnData.setIsOk(Constant.OS_STR_YES);
        } catch (Exception e) {
            logger.error("平台用户查询申请单异常！", e);
            apiReturnData.setMessage("平台用户查询申请单异常！"+e.toString());
        }
        return apiReturnData;
    }

    /**
     * 平台前台退换货申请单详情获得数据接口
     * @param orderSn 订单编码
     * @param userId 用户id
     * @param siteCode 站点编码
     * @return ApiReturnData<GoodsReturnDetailInfo>
     */
    @Override
    public ApiReturnData<GoodsReturnDetailInfo> getBGGoodsReturnInfo(String orderSn, String userId, String siteCode) {

        logger.info("用户查询退换货申请单详情：orderSn" + orderSn + ",userId" + userId +  ";siteCode=" + siteCode);

        ApiReturnData<GoodsReturnDetailInfo> apiReturnData = new ApiReturnData<GoodsReturnDetailInfo>();
        apiReturnData.setIsOk(Constant.OS_STR_NO);
        GoodsReturnDetailInfo bGGoodsReturnInfo = new GoodsReturnDetailInfo();
        try {
            if (StringUtil.isBlank(orderSn)) {
                apiReturnData.setMessage("orderSn不能为空！");
                return apiReturnData;
            }
            if (StringUtil.isBlank(userId)) {
                apiReturnData.setMessage("userId不能为空！");
                return apiReturnData;
            }
            MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
            if (orderInfo == null) {
                apiReturnData.setMessage("没有找到该订单！");
                return apiReturnData;
            }
            bGGoodsReturnInfo.setTotalOrderStatus(orderSStatus(orderInfo.getOrderStatus(), orderInfo.getPayStatus(), orderInfo.getShipStatus()));
            bGGoodsReturnInfo.setOrderSn(orderSn);
            bGGoodsReturnInfo.setOrderCreateTime(DateTimeUtils.format(orderInfo.getAddTime(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
            Map<String, Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
            params.put("orderSn", orderSn);
            params.put("userId", userId);
            // 订单商品
            List<OrderGoodsInfo> goodsList = goodsReturnChangePageListMapper.selectOrderGoods(params);

            List<OrderGoodsInfo> returnGoodsList = new ArrayList<OrderGoodsInfo>(Constant.DEFAULT_LIST_SIZE);
            for (OrderGoodsInfo orderGoods : goodsList) {
                OrderGoodsInfo bGGoodsReturnGoods = new OrderGoodsInfo();
                GoodsReturnChangeExample goodsReturnChangeExample = new GoodsReturnChangeExample();
                goodsReturnChangeExample.or().andOrderSnEqualTo(orderSn).andUserIdEqualTo(userId).andSkuSnEqualTo(orderGoods.getSkuSn()).andStatusBetween(1,3);
                List<GoodsReturnChange> goodsReturnChangeList = goodsReturnChangeMapper.selectByExample(goodsReturnChangeExample);
                if (goodsReturnChangeList.size() > 0) {
                    bGGoodsReturnGoods.setReturnGoodsNum(goodsReturnChangeList.get(0).getReturnSum());
                }

                MasterOrderGoodsExample orderGoodsExample = new MasterOrderGoodsExample();
                orderGoodsExample.or().andCustomCodeEqualTo(orderGoods.getSkuSn()).andMasterOrderSnEqualTo(orderSn);
                List<MasterOrderGoods> orderGoodsList = masterOrderGoodsMapper.selectByExample(orderGoodsExample);
                if (orderGoodsList.size() > 0 && null != orderGoodsList.get(0)) {
                    MasterOrderGoods masterOrderGoods = orderGoodsList.get(0);
                    // 商品规格
                    bGGoodsReturnGoods.setGoodsSize(masterOrderGoods.getGoodsSizeName());
                    // 商品颜色
                    bGGoodsReturnGoods.setGoodsColor(masterOrderGoods.getGoodsColorName());
                    // 商品6位码
                    if (org.apache.commons.lang.StringUtils.isNotBlank(masterOrderGoods.getGoodsSn())) {
                        bGGoodsReturnGoods.setGoodsSn(masterOrderGoods.getGoodsSn());
                    } else {
                        if (masterOrderGoods.getCustomCode() != null && masterOrderGoods.getCustomCode().length() > 6) {
                            bGGoodsReturnGoods.setGoodsSn(masterOrderGoods.getCustomCode().substring(0, 6));
                        }
                    }
                    // 商品财务价
                    bGGoodsReturnGoods.setTransactionPrice(masterOrderGoods.getTransactionPrice().doubleValue());
                    // 商品sku
                    bGGoodsReturnGoods.setSkuSn(masterOrderGoods.getCustomCode());
                    // 商品名称
                    bGGoodsReturnGoods.setGoodsName(masterOrderGoods.getGoodsName());
                    // 商品图片
                    bGGoodsReturnGoods.setGoodsUrl(masterOrderGoods.getGoodsThumb());
                }
                // 商品数量
                bGGoodsReturnGoods.setGoodsNum(orderGoods.getGoodsNum());
                returnGoodsList.add(bGGoodsReturnGoods);
            }
            Map<String, Object> addressParams = new HashMap<String, Object>(4);
            addressParams.put("isHistory", "0");
            addressParams.put("orderSn", orderSn);
            OrderAddressInfo address = defineOrderMapper.selectOrderAddressInfo(addressParams);
            // 国家
            String country = "";
            // 省
            String province = "";
            // 市
            String city = "";
            // 区
            String district = "";
            // 街道
            String street = "";
            if (address != null) {
                String mobile = address.getMobile();
                // 收货人手机号码
                bGGoodsReturnInfo.setMobile(mobile);
                // 收货人
                bGGoodsReturnInfo.setReceiver(address.getConsignee());
                if (address.getCountry() != null) {
                    SystemRegionArea countryArea = systemRegionAreaMapper.selectByPrimaryKey(address.getCountry());
                    if (countryArea != null) {
                        country=countryArea.getRegionName();
                    }
                }
                if (address.getProvince() != null && address.getProvince().length() > 0) {
                    SystemRegionArea provinceArea = systemRegionAreaMapper.selectByPrimaryKey(address.getProvince());
                    if (provinceArea != null) {
                        province = provinceArea.getRegionName();
                    }
                }
                if (address.getCity() != null && address.getCity().length() > 0) {
                    SystemRegionArea cityArea = systemRegionAreaMapper.selectByPrimaryKey(address.getCity());
                    if (cityArea != null) {
                        city = cityArea.getRegionName();
                    }
                }
                if (address.getDistrict() != null && address.getDistrict().length() > 0) {
                    SystemRegionArea districtArea = systemRegionAreaMapper.selectByPrimaryKey(address.getDistrict());
                    if (districtArea != null) {
                        district = districtArea.getRegionName();
                    }
                }
                if (address.getStreet() != null && address.getStreet().length() > 0) {
                    SystemRegionArea streetArea = systemRegionAreaMapper.selectByPrimaryKey(address.getStreet());
                    if (streetArea != null) {
                        street = streetArea.getRegionName();
                    }
                }
            }
            bGGoodsReturnInfo.setShippingAddress(country + "-" + province + "-" + city + "-" + district + "-" + street + "-" + address.getAddress());
            bGGoodsReturnInfo.setGoodsInfoList(returnGoodsList);
            apiReturnData.setData(bGGoodsReturnInfo);
            apiReturnData.setIsOk(Constant.OS_STR_YES);
        } catch (Exception e) {
            logger.error("平台用户查询申请单详情异常！", e);
            apiReturnData.setMessage("平台用户查询申请单详情异常！"+e.toString());
        }
        return apiReturnData;
    }

    /**
     * 订单状态
     * @param orderStatus
     * @param payStatus
     * @param shipStatus
     * @return 1 等待付款、2等待发货、3等待收货、4确认收货、5订单取消
     */
    private String orderSStatus(byte orderStatus,byte payStatus,byte shipStatus){
        //等待付款
        //订单状态：未确认 0
        //支付状态：未支付  部分付款 0 1
        if (orderStatus == 0 && (payStatus == 0 || payStatus == 1)) {
            return "1";
        }
        //等待发货
        //支付状态：已支付 2
        //订单状态：未确认，确认，锁定，解锁状态  0 1 5 6
        //发货状态：未发货，备货中 0 1
        if (payStatus == 2 &&
            (orderStatus == 0 || orderStatus==1 || orderStatus == 5 || orderStatus == 6) &&
            (shipStatus == 0 || shipStatus == 1)) {
            return "2";
        }
        //等待收货
        //发货状态：已发货 3
        if (shipStatus == 3) {
            return "3";
        }
        //确认收货
        //发货状态:已签收 5
        //支付状态：未结算  !3
        if (shipStatus == 5) {
            return "4";
        }
        //订单取消
        //订单状态：已取消  2
        if (orderStatus == 2) {
            return "5";//交易关闭
        }
        return "0";
    }

    /**
     * 手机查询申请单详情
     * @param mobileParams 查询条件
     * @return ApiReturnData
     */
    @Override
    public ApiReturnData<GoodsReturnChangeMobile> getGoodsReturnChange(MobileParams mobileParams) {
        String masterOrderSn = mobileParams.getOrderSn();
        String skuSn = mobileParams.getSkuSn();
        String isHistory = mobileParams.getIsHistory();
        String relatingOriginalSn = "";
        if (mobileParams.getStatus() == 1) {
            OrderDistribute orderInfo = orderDistributeMapper.selectByPrimaryKey(mobileParams.getOrderSn());
            if (orderInfo != null) {
                masterOrderSn = orderInfo.getMasterOrderSn();
            }
        }
        MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
        if( null != masterOrderInfo) {
            isHistory = "0";
        } else {
            //历史订单
            isHistory ="1";
        }
        logger.info("平台手机端查询申请单详情：orderSn="+masterOrderSn+",skuSn="+skuSn);
        ApiReturnData<GoodsReturnChangeMobile> apiReturnData = new ApiReturnData<GoodsReturnChangeMobile>();
        apiReturnData.setIsOk(Constant.OS_STR_NO);
        try {
            if (StringUtils.isBlank(masterOrderSn)) {
                apiReturnData.setMessage("orderSn不能为空！");
                return apiReturnData;
            }

            if (StringUtils.isBlank(skuSn)) {
                apiReturnData.setMessage("商品skuSn不能为空！");
                return apiReturnData;
            }

            if (StringUtils.isBlank(isHistory)) {
                isHistory = "0";
            }

            double returnPrice = 0d;
            double exchangePrice = 0d;

            List<GoodsMobile> returnGoodsList = new ArrayList<GoodsMobile>(Constant.DEFAULT_LIST_SIZE);
            List<GoodsMobile> orderGoodsList = new ArrayList<GoodsMobile>(Constant.DEFAULT_LIST_SIZE);
            String returnSn = mobileParams.getReturnSn();

            String clearTime = "";
            if (StringUtil.isNotBlank(returnSn)) {
                OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
                relatingOriginalSn = orderReturn.getMasterOrderSn();
                returnPrice = orderReturn.getReturnTotalFee().doubleValue();
                clearTime = TimeUtil.format(orderReturn.getClearTime(), TimeUtil.YYYY_MM_DD_HH_MM_SS);
                //退单商品
                OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
                orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
                List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
                if (CollectionUtils.isEmpty(orderReturnGoodsList)) {
                    apiReturnData.setMessage("退单"+returnSn+"商品数据为空！");
                    return apiReturnData;
                }
                for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
                    GoodsMobile goodsMobile = new GoodsMobile();
                    goodsMobile.setCustomCode(orderReturnGoods.getCustomCode());
                    goodsMobile.setGoodsName(orderReturnGoods.getGoodsName());
                    goodsMobile.setGoodsNum(orderReturnGoods.getGoodsReturnNumber());
                    goodsMobile.setGoodsColor(orderReturnGoods.getGoodsColorName());
                    goodsMobile.setGoodsSize(orderReturnGoods.getGoodsSizeName());
                    goodsMobile.setGoodsUrl(orderReturnGoods.getGoodsThumb());
                    goodsMobile.setSettlementPrice(orderReturnGoods.getSettlementPrice().doubleValue());
                    returnGoodsList.add(goodsMobile);
                }
            }
            //换单
            if (mobileParams.getStatus() == 2) {
                if (StringUtil.endsWithIgnoreCase(isHistory, "0")) {
                    MasterOrderGoodsExample example = new MasterOrderGoodsExample();
                    example.or().andMasterOrderSnEqualTo(masterOrderSn);
                    List<MasterOrderGoods> goodsList = masterOrderGoodsMapper.selectByExample(example);
                    for (MasterOrderGoods orderGoods : goodsList) {
                        exchangePrice += orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsNumber();

                        GoodsMobile goodsMobile = new GoodsMobile();
                        goodsMobile.setCustomCode(orderGoods.getCustomCode());
                        goodsMobile.setGoodsName(orderGoods.getGoodsName());
                        goodsMobile.setGoodsNum(orderGoods.getGoodsNumber());
                        goodsMobile.setGoodsColor(orderGoods.getGoodsColorName());
                        goodsMobile.setGoodsSize(orderGoods.getGoodsSizeName());
                        goodsMobile.setGoodsUrl(orderGoods.getGoodsThumb());
                        goodsMobile.setSettlementPrice(orderGoods.getSettlementPrice().doubleValue());
                        goodsMobile.setGoodsSn(orderGoods.getGoodsSn());
                        goodsMobile.setGoodsUrl(orderGoods.getGoodsThumb());
                        orderGoodsList.add(goodsMobile);
                    }
                } else {
                    MasterOrderGoodsHistoryExample example = new MasterOrderGoodsHistoryExample();
                    example.or().andMasterOrderSnEqualTo(masterOrderSn);
                    List<MasterOrderGoodsHistory> orderGoodsHistoryList = masterOrderGoodsHistoryMapper.selectByExample(example);
                    for (MasterOrderGoodsHistory orderGoodsHistory : orderGoodsHistoryList) {
                        exchangePrice += orderGoodsHistory.getSettlementPrice().doubleValue()*orderGoodsHistory.getGoodsNumber();
                        GoodsMobile goodsMobile = new GoodsMobile();
                        goodsMobile.setCustomCode(orderGoodsHistory.getCustomCode());
                        goodsMobile.setGoodsName(orderGoodsHistory.getGoodsName());
                        goodsMobile.setGoodsNum(orderGoodsHistory.getGoodsNumber());
                        goodsMobile.setGoodsColor(orderGoodsHistory.getGoodsColorName());
                        goodsMobile.setGoodsSize(orderGoodsHistory.getGoodsSizeName());
                        goodsMobile.setGoodsUrl(orderGoodsHistory.getGoodsThumb());
                        goodsMobile.setSettlementPrice(orderGoodsHistory.getSettlementPrice().doubleValue());
                        orderGoodsList.add(goodsMobile);
                    }
                }
            }
            GoodsReturnChangeExample goodsReturnChangeExample = new GoodsReturnChangeExample();
            goodsReturnChangeExample.setOrderByClause("`create` desc ");
            GoodsReturnChangeExample.Criteria criteria = goodsReturnChangeExample.or();
            if (mobileParams.getStatus() != 2) {
                criteria.andOrderSnEqualTo(masterOrderSn).andSkuSnEqualTo(skuSn);
            } else {
                criteria.andOrderSnEqualTo(relatingOriginalSn);
            }
            if (StringUtil.isNotBlank(mobileParams.getReturnType())) {
                criteria.andReturnTypeEqualTo(Integer.valueOf(mobileParams.getReturnType()));
            }
            List<GoodsReturnChange> goodsReturnChangeList = goodsReturnChangeMapper.selectByExample(goodsReturnChangeExample);

            GoodsReturnChangeMobile goodsReturnChangeMobile = new GoodsReturnChangeMobile();
            goodsReturnChangeMobile.setReturnPrice(returnPrice);
            goodsReturnChangeMobile.setExchangePrice(exchangePrice);
            goodsReturnChangeMobile.setOrderSn(masterOrderSn);
            goodsReturnChangeMobile.setClearTime(clearTime);

            if (CollectionUtils.isEmpty(goodsReturnChangeList)) {
                apiReturnData.setMessage("符合条件orderSn:"+relatingOriginalSn+",skuSn"+skuSn+"的申请单不存在！");
                setGoodsReturnChangesData(goodsReturnChangeMobile,null);
//              return apiReturnData;
            } else {
                GoodsReturnChange goodsReturnChange = goodsReturnChangeList.get(0);
                setGoodsReturnChangesData(goodsReturnChangeMobile,goodsReturnChange);
            }

            //退单详情状态
            if (mobileParams.getStatus() == 1) {
                OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
                if (orderReturn.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED.intValue()) {
                    //退单已结算
                    goodsReturnChangeMobile.setStatus(9);
                } else {//退单未结算
                    OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
                    if (orderReturnShip.getIsGoodReceived().intValue() == ConstantValues.ORDERRETURN_GOODS_RECEIVED.UNRECEIVED) {
                        //未收货(退单生成但未收货)
                        goodsReturnChangeMobile.setStatus(5);
                    } else {
                        //已收货
                        if (orderReturnShip.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE) {
                            //已入库(表明质检已通过)
                            goodsReturnChangeMobile.setStatus(7);
                        } else {
                            //未入库
                            if (orderReturnShip.getQualityStatus().intValue() == ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS) {
                                //质检通过
                                goodsReturnChangeMobile.setStatus(6);
                            } else {
                                //质检未通过
                                goodsReturnChangeMobile.setStatus(8);
                            }
                        }
                    }
                }
            } else if(mobileParams.getStatus() == 2){
                //换单详情
                //换单商品
                goodsReturnChangeMobile.setOrderGoodsList(orderGoodsList);
                //退单商品
                goodsReturnChangeMobile.setReturnGoodsList(returnGoodsList);

                OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
                if(orderReturnShip.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE ){
                    //退单已入库
                    int orderStatus = -1;
                    int shipStatus = -1;
                    if(StringUtil.endsWithIgnoreCase(isHistory, "0")){
                        MasterOrderInfoExample orderInfoExample = new MasterOrderInfoExample();
                        orderInfoExample.or().andMasterOrderSnEqualTo(masterOrderSn);
                        List<MasterOrderInfo> orderInfoList = masterOrderInfoMapper.selectByExample(orderInfoExample);
                        if(CollectionUtils.isEmpty(orderInfoList)){
                            apiReturnData.setMessage("订单号："+masterOrderSn+"对应订单不存在！");
                            return apiReturnData;
                        }
                        orderStatus = orderInfoList.get(0).getOrderStatus().intValue();
                        shipStatus = orderInfoList.get(0).getShipStatus().intValue();
                    }else{
                        MasterOrderInfoHistoryExample orderInfoHistoryExample = new MasterOrderInfoHistoryExample();
                        orderInfoHistoryExample.or().andMasterOrderSnEqualTo(masterOrderSn);
                        List<MasterOrderInfoHistory> orderInfoList = masterOrderInfoHistoryMapper.selectByExample(orderInfoHistoryExample);
                        if(CollectionUtils.isEmpty(orderInfoList)){
                            apiReturnData.setMessage("订单号："+masterOrderSn+"对应订单不存在！");
                            return apiReturnData;
                        }
                        orderStatus = orderInfoList.get(0).getOrderStatus().intValue();
                        shipStatus = orderInfoList.get(0).getShipStatus().intValue();

                    }

                    //退单已入库并且换单为未确认
                    if(orderStatus != 1){
                        goodsReturnChangeMobile.setStatus(11);
                    }else{
                        if(shipStatus == 3){
                            goodsReturnChangeMobile.setStatus(14);
                        }else if(shipStatus == 5){
                            goodsReturnChangeMobile.setStatus(15);
                        }else if(shipStatus <= 2){
                            goodsReturnChangeMobile.setStatus(13);
                        }
                    }

                }else{
                    //退单未入库

                    int orderStatus = -1;
                    if(StringUtil.endsWithIgnoreCase(isHistory, "0")){
                        MasterOrderInfoExample orderInfoExample = new MasterOrderInfoExample();
                        orderInfoExample.or().andMasterOrderSnEqualTo(masterOrderSn);
                        List<MasterOrderInfo> orderInfoList = masterOrderInfoMapper.selectByExample(orderInfoExample);
                        if(CollectionUtils.isEmpty(orderInfoList)){
                            apiReturnData.setMessage("订单号："+masterOrderSn+"对应订单不存在！");
                            return apiReturnData;
                        }
                        orderStatus = orderInfoList.get(0).getOrderStatus().intValue();
                    }else{
                        MasterOrderInfoHistoryExample orderInfoHistoryExample = new MasterOrderInfoHistoryExample();
                        orderInfoHistoryExample.or().andMasterOrderSnEqualTo(masterOrderSn);
                        List<MasterOrderInfoHistory> orderInfoList = masterOrderInfoHistoryMapper.selectByExample(orderInfoHistoryExample);
                        if(CollectionUtils.isEmpty(orderInfoList)){
                            apiReturnData.setMessage("订单号："+masterOrderSn+"对应订单不存在！");
                            return apiReturnData;
                        }
                        orderStatus = orderInfoList.get(0).getOrderStatus().intValue();

                    }

                    if(orderReturnShip.getQualityStatus().intValue() != ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PASS &&
                        orderReturnShip.getIsGoodReceived().intValue() == ConstantValues.ORDERRETURN_GOODS_RECEIVED.RECEIVED){
                        //已收货但质检未通过
                        goodsReturnChangeMobile.setStatus(12);
                    }else if(orderStatus == 2){
                        goodsReturnChangeMobile.setStatus(11);//换单已取消时，换单状态为换单已完成
                    }else{
                        goodsReturnChangeMobile.setStatus(10);//换货中

                    }
                }
            }
            apiReturnData.setData(goodsReturnChangeMobile);
            apiReturnData.setIsOk(Constant.OS_STR_YES);
        }catch(Exception e){
            logger.error("平台手机端查询申请单详情出错！",e);
            apiReturnData.setMessage("平台手机端查询申请单详情出错！"+e.toString());
        }
        return apiReturnData;
    };

    private void setGoodsReturnChangesData(GoodsReturnChangeMobile goodsReturnChangeMobile,GoodsReturnChange goodsReturnChange) {
        String mobile = "";
        String userName = "";
        Map<String,Object> channelParams = new HashMap<String, Object>();
        String masterOrderSn = goodsReturnChangeMobile.getOrderSn();
//        OrderDistribute orderInfo = orderDistributeMapper.selectByPrimaryKey(goodsReturnChangeMobile.getOrderSn());
        MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(masterOrderSn);
        MasterOrderAddressInfo masterOrderAddressInfo = masterOrderAddressInfoMapper.selectByPrimaryKey(masterOrderSn);
        if(masterOrderInfo == null ){
            OrderDistributeHistory  orderInfoHistory = orderDistributeHistoryMapper.selectByPrimaryKey(goodsReturnChangeMobile.getOrderSn());
            MasterOrderInfoHistory masterOrderInfoHistory = masterOrderInfoHistoryMapper.selectByPrimaryKey(masterOrderSn);
            MasterOrderAddressInfoHistory masterOrderAddressInfoHistory = masterOrderAddressInfoHistoryMapper.selectByPrimaryKey(masterOrderSn);
            mobile = masterOrderAddressInfoHistory.getMobile();
            userName = masterOrderInfoHistory.getUserName();
            //换单运费
            goodsReturnChangeMobile.setShippingPrice(orderInfoHistory.getShippingTotalFee().doubleValue());
            goodsReturnChangeMobile.setOrderCreateTime(TimeUtil.format(orderInfoHistory.getAddTime(),TimeUtil.YYYY_MM_DD_HH_MM_SS));
            //查询订单相关的店铺code及店铺名称
            channelParams.put("orderSn", orderInfoHistory.getOrderSn());
            channelParams.put("isHistory", "1");
        }else{
            mobile = masterOrderAddressInfo.getMobile();
            userName = masterOrderInfo.getUserName();
            //换单运费
            goodsReturnChangeMobile.setShippingPrice(masterOrderInfo.getShippingTotalFee().doubleValue());
            goodsReturnChangeMobile.setOrderCreateTime(TimeUtil.format(masterOrderInfo.getAddTime(),TimeUtil.YYYY_MM_DD_HH_MM_SS));
            //查询订单相关的店铺code及店铺名称
            channelParams.put("orderSn", masterOrderInfo.getMasterOrderSn());
            channelParams.put("isHistory", "0");
        }
        goodsReturnChangeMobile.setChannelCode(masterOrderInfo.getOrderFrom());
        goodsReturnChangeMobile.setChannelName(masterOrderInfo.getShopName());
        if(null != goodsReturnChange){
            mobile = goodsReturnChange.getContactMobile();
            goodsReturnChangeMobile.setMobile(mobile);
            goodsReturnChangeMobile.setExplain(goodsReturnChange.getExplain());
            goodsReturnChangeMobile.setReason(getReason(goodsReturnChange.getReason()));
            goodsReturnChangeMobile.setUserName(goodsReturnChange.getContactName());
            goodsReturnChangeMobile.setCreateTime(TimeUtil.format(goodsReturnChange.getCreate(),TimeUtil.YYYY_MM_DD_HH_MM_SS));
            //默认是换单详情状态
            goodsReturnChangeMobile.setStatus(goodsReturnChange.getStatus());

        }else{
            goodsReturnChangeMobile.setMobile(mobile);
            goodsReturnChangeMobile.setUserName(userName);
        }




        /*//申请单不是已完成或已取消
        if(goodsReturnChange.getStatus().intValue() != 0 && goodsReturnChange.getStatus().intValue() != 2){
            if(goodsReturnChange.getReturnType().intValue() == 1 ){//退货中
                goodsReturnChangeMobile.setGoodsStatus(ConstantValues.NEW_TOTAL_ORDER_STATUS.RETURN_GOODS_PROGRESS);
            }else if(goodsReturnChange.getReturnType().intValue() == 2 ){//换货中
                goodsReturnChangeMobile.setGoodsStatus(ConstantValues.NEW_TOTAL_ORDER_STATUS.EXCHANGE_GOODS_PROGRESS);
            }
        }else{
            if(goodsReturnChange.getReturnType().intValue() == 1 ){//退货完成
                goodsReturnChangeMobile.setGoodsStatus(ConstantValues.NEW_TOTAL_ORDER_STATUS.RETURN_GOODS_FINISH);
            }else if(goodsReturnChange.getReturnType().intValue() == 2 ){//换货完成
                goodsReturnChangeMobile.setGoodsStatus(ConstantValues.NEW_TOTAL_ORDER_STATUS.EXCHANGE_GOODS_FINISH);
            }
        }*/
//      goodsReturnChange.setContactMobile(encryptionMobile(mobile));


    }

    //1：商品质量不过关；2：商品在配送中损坏；3：商品与描述不符；4：尚未收到商品；5：其他（请具体说明）
    private String getReason(Integer status){
        String reason = "";
        if(status == 1){
            reason = "商品质量不过关";
        }else if(status == 2){
            reason = "商品在配送中损坏";
        }else if(status == 3){
            reason = "商品与描述不符";
        }else if(status == 4){
            reason = "尚未收到商品";
        }else{
            reason = "其他";
        }

        return reason;
    }

    /**
     * 查询退单列表
     * @param mobileParams 查询条件
     * @return ApiReturnData
     */
    @Override
    public ApiReturnData<Paging<ReturnMobile>> getOrderReturnList(MobileParams mobileParams) {
        logger.info("平台手机端查询退单列表：mobileParams：" + JSON.toJSONString(mobileParams));
        ApiReturnData<Paging<ReturnMobile>> apiReturnData = new ApiReturnData<Paging<ReturnMobile>>();
        apiReturnData.setIsOk(Constant.OS_STR_NO);
        try {
            if (StringUtil.isBlank(mobileParams.getUserId())) {
                apiReturnData.setMessage("用户id不能为空！");
                return apiReturnData;
            }
            OrderReturnExample orderReturnExample = new OrderReturnExample();
            OrderReturnExample.Criteria criteria = orderReturnExample.or();
            orderReturnExample.setOrderByClause(" add_time desc");
            criteria.andUserIdEqualTo(mobileParams.getUserId());

            if (mobileParams.getPageNum() <= 0) {
                mobileParams.setPageNum(1);
            }

            if (mobileParams.getPageSize() <= 0) {
                mobileParams.setPageSize(10);
            }

            if (StringUtil.isNotBlank(mobileParams.getOrderSn())) {
                criteria.andRelatingOrderSnEqualTo(mobileParams.getOrderSn());
            }
            if (StringUtil.isNotBlank(mobileParams.getSiteCode())) {
                criteria.andSiteCodeEqualTo(mobileParams.getSiteCode());
            }
            //保证退单不是换货单产生的退单
            criteria.andProcessTypeNotEqualTo(Byte.valueOf("4") );
            criteria.andReturnTypeBetween(Byte.valueOf("1"), Byte.valueOf("2"));
            List<OrderReturn> countList = orderReturnMapper.selectByExample(orderReturnExample);

            criteria.limit((mobileParams.getPageNum() - 1) * mobileParams.getPageSize(), mobileParams.getPageSize());

            List<OrderReturn> orderReturnList = orderReturnMapper.selectByExample(orderReturnExample);

            if (CollectionUtils.isEmpty(orderReturnList)) {
                apiReturnData.setMessage("符合条件的退单为空！");
                return apiReturnData;
            }
            List<ReturnMobile> returnMobileList = new ArrayList<ReturnMobile>(Constant.DEFAULT_LIST_SIZE);
            for (OrderReturn orderReturn : orderReturnList) {
                ReturnMobile returnMobile = new ReturnMobile();
                returnMobile.setRelatingOrderSn(orderReturn.getRelatingOrderSn());
                returnMobile.setReturnSn(orderReturn.getReturnSn());
                returnMobile.setClearTime(TimeUtil.format(orderReturn.getClearTime(), TimeUtil.YYYY_MM_DD_HH_MM_SS));
                if (orderReturn.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED || orderReturn.getReturnOrderStatus().intValue() == 4 || orderReturn.getReturnOrderStatus().intValue() == 10){
                    //退货完成
                    returnMobile.setStatus(ConstantValues.NEW_TOTAL_ORDER_STATUS.RETURN_GOODS_FINISH);
                }else{
                    //退货中
                    returnMobile.setStatus(ConstantValues.NEW_TOTAL_ORDER_STATUS.RETURN_GOODS_PROGRESS);
                }

                //与退单相关联的商品
                List<GoodsMobile> goodsMobileList = new ArrayList<GoodsMobile>(Constant.DEFAULT_LIST_SIZE);
                OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
                orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(orderReturn.getReturnSn());
                List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
                for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
                    GoodsMobile goodsMobile = new GoodsMobile();
                    goodsMobile.setId(orderReturnGoods.getId().toString());
                    goodsMobile.setGoodsName(orderReturnGoods.getGoodsName());
                    goodsMobile.setGoodsColor(orderReturnGoods.getGoodsColorName());
                    goodsMobile.setGoodsSize(orderReturnGoods.getGoodsSizeName());
                    goodsMobile.setGoodsNum(orderReturnGoods.getGoodsReturnNumber().intValue());
                    goodsMobile.setGoodsUrl(orderReturnGoods.getGoodsThumb());
                    goodsMobile.setCustomCode(orderReturnGoods.getCustomCode());
                    goodsMobile.setSettlementPrice(orderReturnGoods.getSettlementPrice().doubleValue());
                    goodsMobile.setGoodsSn(orderReturnGoods.getGoodsSn());
                    goodsMobileList.add(goodsMobile);
                }
                returnMobile.setGoodsMobileList(goodsMobileList);

                returnMobileList.add(returnMobile);
            }

            Paging<ReturnMobile> paging = new Paging<ReturnMobile>(countList.size(), returnMobileList);
            apiReturnData.setData(paging);
            apiReturnData.setIsOk(Constant.OS_STR_YES);

        } catch (Exception e) {
            logger.error("平台手机端查询退列表出错！" + e.getMessage(), e);
            apiReturnData.setMessage("平台手机端查询退单列表出错！");
        }
        return apiReturnData;
    }

    /**
     * 查询换单列表
     * @param mobileParams 查询条件
     * @return ApiReturnData
     */
    @Override
    public ApiReturnData<Paging<OrderMobile>> getExchangeOrderList(MobileParams mobileParams) {
        logger.info("平台手机端查询换单列表：mobileParams：" + JSON.toJSONString(mobileParams));
        ApiReturnData<Paging<OrderMobile>> apiReturnData = new ApiReturnData<Paging<OrderMobile>>();
        apiReturnData.setIsOk(Constant.OS_STR_NO);
        try {
            if (StringUtil.isBlank(mobileParams.getUserId())) {
                apiReturnData.setMessage("用户id不能为空！");
                return apiReturnData;
            }

            Map<String,Object> params = new HashMap<String, Object>(Constant.DEFAULT_MAP_SIZE);
            params.put("userId", mobileParams.getUserId());
            if (StringUtil.isBlank(mobileParams.getIsHistory())) {
                params.put("isHistory", 0);
            }

            List<OrderMobile> countList = defineOrderMapper.getExchangeOrderList(params);
            if (mobileParams.getPageSize() <= 0) {
                mobileParams.setPageSize(10);
                params.put("limits", 10);
            } else {
                params.put("limits", mobileParams.getPageSize());
            }

            if (mobileParams.getPageNum() <= 0) {
                params.put("start", 0);
            } else {
                params.put("start", (mobileParams.getPageNum() - 1) * mobileParams.getPageSize());
            }

            List<OrderMobile> orderMobileList = defineOrderMapper.getExchangeOrderList(params);
            if (CollectionUtils.isEmpty(orderMobileList)) {
                apiReturnData.setMessage("符合条件的换单为空！");
                return apiReturnData;
            }
            for (OrderMobile orderMobile : orderMobileList) {
                //订单状态 0，未确认；1，已确认；2，已取消；3，完成
                if (orderMobile.getStatus() == 2 || orderMobile.getStatus() == 3) {
                    //换货完成
                    orderMobile.setStatus(4);
                } else {
                    //换货中
                    orderMobile.setStatus(2);
                }

                //待补款金额
                MasterOrderInfo orderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderMobile.getOrderSn());

                //已付款金额
                orderMobile.setOrderPrice(orderInfo.getMoneyPaid().doubleValue());

                for (GoodsMobile goodsMobile : orderMobile.getGoodsMobileList()) {
                    goodsMobile.setGoodsSn(goodsMobile.getCustomCode().substring(0, 6));
                }
            }
            Paging<OrderMobile> paging = new Paging<OrderMobile>(countList.size(), orderMobileList);
            apiReturnData.setData(paging);
            apiReturnData.setIsOk(Constant.OS_STR_YES);
        } catch (Exception e) {
            logger.error("平台手机端查询换单列表出错！",e);
            apiReturnData.setMessage("平台手机端查询换单列表出错！");
        }
        return apiReturnData;
    }

    /**
     * 创建订单商品退换货信息新版（生成申请退换详情）
     * @param createGoodsReturnChange 退换信息
     * @return ReturnInfo
     */
    @Override
    public ReturnInfo<Boolean> createGoodsReturnChangeNew(CreateGoodsReturnChange createGoodsReturnChange) {
        logger.info("创建订单商品退换货信息开始: " + JSONObject.toJSONString(createGoodsReturnChange));
        ReturnInfo<Boolean> ri = new ReturnInfo<Boolean>(Constant.OS_NO);
        try {
            if (createGoodsReturnChange == null) {
                logger.error("申请退换单数据不能为空！");
                ri.setMessage("申请退换单数据不能为空！");
                return ri;
            }

            if (StringUtils.isBlank(createGoodsReturnChange.getActionUser())) {
                logger.error("操作人不能为空！");
                ri.setMessage("操作人不能为空！");
                return ri;
            }

            String orderSn = createGoodsReturnChange.getOrderSn();
            if (null == orderSn || StringUtil.isEmpty(orderSn)) {
                logger.error("订单编号不能为空！");
                ri.setMessage("订单编号不能为空！");
                return ri;
            }
            ri.setOrderSn(orderSn);
            logger.info("创建订单["+ orderSn +"]商品退换货信息开始！");
            MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(orderSn);
            if (null == masterOrderInfo) {
                ri.setMessage("没有获取订单" + orderSn + "的信息！不能进行创建订单商品退换货操作！");
                logger.error("没有获取订单" + orderSn + "的信息！不能进行创建订单商品退换货操作！");
                return ri;
            }
            MasterOrderInfoExtend extend = masterOrderInfoExtendMapper.selectByPrimaryKey(orderSn);
            if (null == extend) {
                ri.setMessage("没有获取订单" + orderSn + "扩展表信息！不能进行创建订单商品退换货操作！");
                logger.error("没有获取订单" + orderSn + "扩展表信息！不能进行创建订单商品退换货操作！");
                return ri;
            }
            if (StringUtil.isEmpty(masterOrderInfo.getUserId())) {
                ri.setMessage("会员ID为空！不能进行创建订单商品退换货操作！");
                logger.error("会员ID为空！不能进行创建订单商品退换货操作！");
                return ri;
            }
            if (createGoodsReturnChange.getReturnType() == null) {
                ri.setMessage("创建订单商品退换货类型不能为空！不能进行创建订单商品退换货操作！");
                logger.error("创建订单商品退换货类型不能为空！不能进行创建订单商品退换货操作！");
                return ri;
            }
            if (createGoodsReturnChange.getReason() == null) {
                ri.setMessage("创建订单商品退换货原因不能为空！不能进行创建订单商品退换货操作！");
                logger.error("创建订单商品退换货原因不能为空！不能进行创建订单商品退换货操作！");
                return ri;
            }

            //校验申请单是否有待处理或待沟通
            GoodsReturnChangeExample example = new GoodsReturnChangeExample();
            List<Integer> list = new ArrayList<Integer>(2);
            list.add(1);
            list.add(3);
            example.or().andOrderSnEqualTo(orderSn).andStatusIn(list).andIsDelEqualTo(0);
            List<GoodsReturnChange> goodsReturnChanges = goodsReturnChangeMapper.selectByExample(example);
            if (StringUtil.isListNotNull(goodsReturnChanges)) {
                ri.setMessage("该订单存在未完成申请单，请处理完或取消后再次申请");
                logger.error("该订单存在未完成申请单，请处理完或取消后再次申请");
                return ri;
            }

            GoodsReturnChange goodsReturnChange = new GoodsReturnChange();
            CommonUtils.copyPropertiesNew(createGoodsReturnChange, goodsReturnChange);
            String returnChangeSn = goodsReturnChange.getReturnchangeSn();
            if (StringUtil.isBlank(returnChangeSn)) {
                returnChangeSn = "HS" + DateTimeUtils.format(new Date(), DateTimeUtils.YYYYMMDDHHmmss);
                goodsReturnChange.setReturnchangeSn(returnChangeSn);
            }

            //批量插入明细
            ReturnInfo<Boolean> goodsReturnInfo = batchInsertGoodsReturnChangeDetail(createGoodsReturnChange, returnChangeSn);
            if (goodsReturnInfo.getIsOk() == 0) {
                ri.setMessage(goodsReturnInfo.getMessage());
                return ri;
            }

            //插入申请单
            goodsReturnChange.setCreate(new Date());
            goodsReturnChange.setUserId(masterOrderInfo.getUserId());
            goodsReturnChange.setShopCode(masterOrderInfo.getOrderFrom());
            goodsReturnChange.setShopName(masterOrderInfo.getShopName());
            goodsReturnChange.setStoreCode(extend.getShopCode());
            goodsReturnChange.setStoreName(extend.getShopName());
            goodsReturnChange.setReturnSum(createGoodsReturnChange.getReturnSum());
            goodsReturnChangeMapper.insertSelective(goodsReturnChange);

            GoodsReturnChangeAction action = new GoodsReturnChangeAction();
            action.setActionUser(createGoodsReturnChange.getActionUser());
            // 待沟通
            action.setStatus(1);
            action.setOrderSn(orderSn);
            action.setActionNote("提交：" + createGoodsReturnChange.getActionNote());
            action.setLogTime(new Date());
            action.setReturnchangeSn(returnChangeSn);
            goodsReturnChangeActionMapper.insert(action);
            //如果未发货 备货中 部分发货 则可以设为问题单
            if (masterOrderInfo.getShipStatus() == 0 || masterOrderInfo.getShipStatus() == 1 || masterOrderInfo.getShipStatus() == 2) {
                // 19 问题单类型  退单转问题单
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setMasterOrderSn(orderSn);
                orderStatus.setMessage("退换货申请设问题单！");
                orderStatus.setSource(ConstantValues.METHOD_SOURCE_TYPE.FRONT);
                orderStatus.setCode("19");
                ReturnInfo returnInfo_q = orderQuestionService.questionOrderByMasterSn(orderSn, orderStatus);
                logger.info("创建订单["+ orderSn +"]商品退换货信息完成！"+returnInfo_q.getMessage());
            }
            ri.setIsOk(Constant.OS_YES);
            ri.setData(true);
            ri.setMessage("创建订单商品退换货记录成功！");
            logger.info("创建订单商品退换货信息完成！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建订单商品退换货信息异常",e);
            ri.setMessage("创建订单商品退换货信息异常：" + e.getMessage());
        }
        return ri;
    }

    /**
     * 批量插入申请退换单明细
     * @param createGoodsReturnChange
     * @param returnChangeSn
     */
    private ReturnInfo<Boolean> batchInsertGoodsReturnChangeDetail(CreateGoodsReturnChange createGoodsReturnChange, String returnChangeSn) {
        ReturnInfo<Boolean> returnInfo = new ReturnInfo<Boolean>(Constant.OS_NO);

        try {
            List<GoodsReturnChangeDetailInfo> goodsList = createGoodsReturnChange.getGoodsList();
            logger.info("batchInsertGoodsReturnChangeDetail -> goodsList -> " + JSONObject.toJSONString(goodsList));
            if (StringUtil.isListNull(goodsList)) {
                return new ReturnInfo<Boolean>(Constant.OS_NO, "申请退单商品为空！退换单申请明细创建失败！");
            }

            //获取订单商品
            String orderSn = createGoodsReturnChange.getOrderSn();
            Map<String, List<MasterOrderGoods>> masterOrderGoodsMap = getMasterOrderGoodsMap(orderSn);
            logger.info("batchInsertGoodsReturnChangeDetail -> getMasterOrderGoodsMap -> " + JSONObject.toJSONString(masterOrderGoodsMap));
            if (masterOrderGoodsMap == null || masterOrderGoodsMap.size() < 1) {
                return new ReturnInfo<Boolean>(Constant.OS_NO, orderSn + "订单商品不存在！退换单申请明细创建失败！");
            }

            int returnNum = 0;
            List<GoodsReturnChangeDetail> details = new ArrayList<GoodsReturnChangeDetail>();
            for (GoodsReturnChangeDetailInfo detailInfo : goodsList) {
                String key = detailInfo.getCustomCode() + "_" + detailInfo.getExtensionCode();

                List<MasterOrderGoods> orderGoodsList = masterOrderGoodsMap.get(key);
                if (StringUtil.isListNull(orderGoodsList)) {
                    return new ReturnInfo<Boolean>(Constant.OS_NO, orderSn + "订单无" + key + "商品！退换单申请明细创建失败！");
                }
                //获取商品购买数量
                int goodsNum = 0;
                for (MasterOrderGoods masterOrderGoods : orderGoodsList) {
                    goodsNum += masterOrderGoods.getGoodsNumber();
                }

                //订单商品
                MasterOrderGoods goods = orderGoodsList.get(0);
                if (detailInfo.getReturnSum() > goodsNum) {
                    return new ReturnInfo<Boolean>(Constant.OS_NO, "订单号：" + orderSn + "商品编码：" + detailInfo.getCustomCode() + "退换商品数量大于购买数量！退换单申请明细创建失败！");
                }

                GoodsReturnChangeDetail goodsReturnChangeDetail = new GoodsReturnChangeDetail();

                CommonUtils.copyPropertiesNew(detailInfo, goodsReturnChangeDetail);
                goodsReturnChangeDetail.setOrderSn(orderSn);
                goodsReturnChangeDetail.setGoodsName(goods.getGoodsName());
                goodsReturnChangeDetail.setGoodsNumber((short) goodsNum);
                goodsReturnChangeDetail.setGoodsPrice(goods.getGoodsPrice());
                goodsReturnChangeDetail.setTransactionPrice(goods.getTransactionPrice());
                goodsReturnChangeDetail.setSettlementPrice(goods.getSettlementPrice());
                goodsReturnChangeDetail.setDiscount(goods.getDiscount().floatValue());
                goodsReturnChangeDetail.setGoodsSizeName(goods.getGoodsSizeName());
                goodsReturnChangeDetail.setGoodsColorName(goods.getGoodsColorName());
                goodsReturnChangeDetail.setGoodsThumb(goods.getGoodsThumb());
                goodsReturnChangeDetail.setMarketPrice(goods.getMarketPrice());
                goodsReturnChangeDetail.setReturnType(createGoodsReturnChange.getReturnType());
                goodsReturnChangeDetail.setReturnchangeSn(returnChangeSn);
                goodsReturnChangeDetail.setGoodsSn(goods.getGoodsSn());
                goodsReturnChangeDetail.setShareBonus(new BigDecimal(detailInfo.getShareBonus()));

                returnNum += detailInfo.getReturnSum();
                details.add(goodsReturnChangeDetail);
            }

            if (StringUtil.isListNull(details)) {
                return new ReturnInfo<Boolean>(Constant.OS_NO, orderSn + "订单无可新增明细！退换单申请明细创建失败！");
            }

            goodsReturnChangeStMapper.batchInsertReturnChangeDetail(details);
            createGoodsReturnChange.setReturnSum(returnNum);
            returnInfo.setIsOk(Constant.OS_YES);
        } catch (Exception e) {
            returnInfo.setIsOk(Constant.OS_NO);
            returnInfo.setMessage("新增退换单申请明细异常");
            e.printStackTrace();
        }

        return returnInfo;
    }

    /**
     * 根据订单号获取订单商品
     * @param orderSn
     * @return
     */
    private Map<String, List<MasterOrderGoods>> getMasterOrderGoodsMap(String orderSn) {
        MasterOrderGoodsExample example = new MasterOrderGoodsExample();
        example.or().andMasterOrderSnEqualTo(orderSn);
        List<MasterOrderGoods> masterOrderGoodsList = masterOrderGoodsMapper.selectByExample(example);
        if (StringUtil.isListNull(masterOrderGoodsList)) {
            return null;
        }

        Map<String, List<MasterOrderGoods>> map = new HashMap<String, List<MasterOrderGoods>>();
        for (MasterOrderGoods goods : masterOrderGoodsList) {
            String key = goods.getCustomCode() + "_" + goods.getExtensionCode();
            List<MasterOrderGoods> goodsList = map.get(key);
            if (goodsList == null) {
                goodsList = new ArrayList<MasterOrderGoods>();
            }
            goodsList.add(goods);
            map.put(key, goodsList);
        }

        return map;
    }

    /**
     * 平台手机端根据退换申请单号查询明细
     * @param returnChangeGoodsBean 查询条件
     * @return ApiReturnData<ReturnChangeDetailInfo>
     */
    @Override
    public ApiReturnData<ReturnChangeDetailInfo> getGoodsReturnChangeDetailList(ReturnChangeGoodsBean returnChangeGoodsBean) {
        ApiReturnData<ReturnChangeDetailInfo> apiReturnData = new ApiReturnData<ReturnChangeDetailInfo>();
        apiReturnData.setIsOk(Constant.OS_STR_NO);
        if (returnChangeGoodsBean == null) {
            apiReturnData.setMessage("请求参数不能为空");
            return apiReturnData;
        }
        logger.info("根据退换申请单号查询明细参数：" + JSONObject.toJSONString(returnChangeGoodsBean));
        try {
            String siteCode = returnChangeGoodsBean.getSiteCode();
            if (StringUtils.isBlank(siteCode)) {
                apiReturnData.setMessage("站点不能为空");
                return apiReturnData;
            }

            //订单号
            String orderSn = returnChangeGoodsBean.getOrderSn();
            if (StringUtils.isBlank(orderSn)) {
                apiReturnData.setMessage("订单号不能为空");
                return apiReturnData;
            }

            //申请单号
            String returnChangeSn = returnChangeGoodsBean.getReturnChangeSn();
            if (StringUtils.isBlank(returnChangeSn)) {
                apiReturnData.setMessage("申请单号不能为空");
                return apiReturnData;
            }

            //申请单
            GoodsReturnChangeExample example = new GoodsReturnChangeExample();
            example.or().andSiteCodeEqualTo(siteCode).andOrderSnEqualTo(orderSn).andReturnchangeSnEqualTo(returnChangeSn);
            List<GoodsReturnChange> goodsReturnChangeList = goodsReturnChangeMapper.selectByExample(example);
            if (StringUtil.isListNull(goodsReturnChangeList)) {
                apiReturnData.setMessage("没有找到对应申请单！");
                return apiReturnData;
            }
            GoodsReturnChange goodsReturnChange = goodsReturnChangeList.get(0);

            //填充申请单信息
            ReturnChangeDetailInfo returnChangeDetailInfo = new ReturnChangeDetailInfo();
            CommonUtils.copyPropertiesNew(goodsReturnChange, returnChangeDetailInfo);
            returnChangeDetailInfo.setReturnChangeTime(DateTimeUtils.format(goodsReturnChange.getCreate()));


            List<String> changeSnList = new ArrayList<String>();
            changeSnList.add(goodsReturnChange.getReturnchangeSn());
            Map<String, List<GoodsReturnChangeDetailBean>> listMap = getDetailList(changeSnList);

//            GoodsReturnChangeDetailExample detailExample = getGoodsReturnChangeDetailExample(returnChangeGoodsBean);
//            List<GoodsReturnChangeDetail> detailList = goodsReturnChangeDetailMapper.selectByExample(detailExample);
            apiReturnData.setIsOk(Constant.OS_STR_YES);
//            if (StringUtil.isListNull(detailList)) {
//                apiReturnData.setMessage("该申请单无符合条件明细");
//                return apiReturnData;
//            }
            if (listMap == null || listMap.size() < 1 || StringUtil.isListNull(listMap.get(goodsReturnChange.getReturnchangeSn()))) {
                apiReturnData.setMessage("该申请单无符合条件明细");
                return apiReturnData;
            }

            List<GoodsReturnChangeDetailBean> detailList = listMap.get(goodsReturnChange.getReturnchangeSn());

            //申请单商品总额
            BigDecimal totalFee = new BigDecimal(0);
            List<GoodsReturnChangeDetailVo> list = new ArrayList<GoodsReturnChangeDetailVo>();
            for (GoodsReturnChangeDetailBean detail : detailList) {
                //计算申请单商品总额
                BigDecimal transactionPrice = detail.getTransactionPrice();
                Integer returnSum = detail.getReturnSum();
                totalFee = totalFee.add(transactionPrice.multiply(new BigDecimal(returnSum)));

                GoodsReturnChangeDetailVo detailVo = new GoodsReturnChangeDetailVo();
                CommonUtils.copyPropertiesNew(detail, detailVo);
                list.add(detailVo);
            }

            returnChangeDetailInfo.setReturnChangeTotalFee(totalFee.doubleValue());
            returnChangeDetailInfo.setReturnChangeGoodsList(list);

            //获取申请日志
            GoodsReturnChangeActionExample actionExample = new GoodsReturnChangeActionExample();
            actionExample.or().andReturnchangeSnEqualTo(returnChangeSn);
            List<GoodsReturnChangeAction> returnChangeActions = goodsReturnChangeActionMapper.selectByExampleWithBLOBs(actionExample);
            returnChangeDetailInfo.setActions(returnChangeActions);

            apiReturnData.setMessage("查询成功");
            apiReturnData.setData(returnChangeDetailInfo);

        }catch (Exception e) {
            logger.error("获取退换申请单明细异常" + JSONObject.toJSONString(returnChangeGoodsBean), e);
            apiReturnData.setMessage("获取退换申请单明细异常");
        }

        return apiReturnData;
    }

    /**
     * 删除申请单
     * @param returnChangeGoodsBean
     * @return
     */
    @Override
    public ApiReturnData<Boolean> deleteGoodsReturnChange(ReturnChangeGoodsBean returnChangeGoodsBean) {
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk("0");
        String orderSn = returnChangeGoodsBean.getOrderSn();
        if (StringUtils.isBlank(orderSn)) {
            apiReturnData.setMessage("订单号为空");
            return apiReturnData;
        }

        String returnChangeSn = returnChangeGoodsBean.getReturnChangeSn();
        if (StringUtils.isBlank(returnChangeSn)) {
            apiReturnData.setMessage("申请单号为空");
            return apiReturnData;
        }

        String userId = returnChangeGoodsBean.getUserId();
        if (StringUtils.isBlank(userId)) {
            apiReturnData.setMessage("操作人为空");
            return apiReturnData;
        }

        String siteCode = returnChangeGoodsBean.getSiteCode();
        if (StringUtils.isBlank(siteCode)) {
            apiReturnData.setMessage("站点为空");
            return apiReturnData;
        }

        String remarks = returnChangeGoodsBean.getNote();
        if (StringUtils.isBlank(remarks)) {
            remarks = "删除申请单";
        }

        try {
            GoodsReturnChangeExample example = new GoodsReturnChangeExample();
            example.or().andReturnchangeSnEqualTo(returnChangeSn);
            GoodsReturnChange goodsReturnChange = new GoodsReturnChange();
            goodsReturnChange.setIsDel(1);
            goodsReturnChangeMapper.updateByExampleSelective(goodsReturnChange, example);

            apiReturnData.setIsOk("1");
            apiReturnData.setMessage("删除成功");
            apiReturnData.setData(true);

            GoodsReturnChangeAction action = new GoodsReturnChangeAction();
            action.setActionUser(userId);
            // 待沟通
            action.setStatus(1);
            action.setOrderSn(orderSn);
            action.setActionNote("提交：" + remarks);
            action.setLogTime(new Date());
            action.setReturnchangeSn(returnChangeSn);
            goodsReturnChangeActionMapper.insert(action);


        } catch (Exception e) {
            logger.error(returnChangeSn + "删除申请单异常", e);
            apiReturnData.setMessage("删除申请单失败");
        }
        return apiReturnData;
    }

    /**
     * 售后申请单沟通
     * @param returnChangeGoodsBean
     * @return
     */
    @Override
    public ApiReturnData<Boolean> communicationChange(ReturnChangeGoodsBean returnChangeGoodsBean) {
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        apiReturnData.setIsOk("0");

        if (returnChangeGoodsBean == null) {
            apiReturnData.setMessage("沟通失败");
            return apiReturnData;
        }

        String returnChangeSn = returnChangeGoodsBean.getReturnChangeSn();
        if (StringUtil.isTrimEmpty(returnChangeSn)) {
            apiReturnData.setMessage("申请单号为空");
            return apiReturnData;
        }
        String actionUser = returnChangeGoodsBean.getUserId();
        if (StringUtil.isTrimEmpty(actionUser)) {
            apiReturnData.setMessage("操作人为空");
            return apiReturnData;
        }

        try {
            GoodsReturnChangeExample example = new GoodsReturnChangeExample();
            example.or().andReturnchangeSnEqualTo(returnChangeSn);
            List<GoodsReturnChange> goodsReturnChanges = goodsReturnChangeMapper.selectByExample(example);
            if (StringUtil.isListNull(goodsReturnChanges)) {
                apiReturnData.setMessage("无此申请单");
                return apiReturnData;
            }
            GoodsReturnChange goodsReturnChange = goodsReturnChanges.get(0);

            //添加日志
            GoodsReturnChangeAction action = new GoodsReturnChangeAction();
            action.setActionUser(actionUser);
            // 状态
            action.setStatus(goodsReturnChange.getStatus());
            action.setOrderSn(goodsReturnChange.getOrderSn());
            action.setActionNote(returnChangeGoodsBean.getNote());
            action.setLogTime(new Date());
            action.setReturnchangeSn(returnChangeSn);
            action.setLogType((byte) 1);
            goodsReturnChangeActionMapper.insert(action);

            apiReturnData.setIsOk("1");
            apiReturnData.setMessage("沟通成功");
        } catch (Exception e) {
            logger.error("申请单沟通失败:" + e.getMessage(), e);
            apiReturnData.setMessage("沟通失败:" + e.getMessage());
        }

        return apiReturnData;
    }

    /**
     * 申请单列表页获取申请单类型数量
     * @param param
     * @return
     */
    @Override
    public ApiReturnData<UserOrderTypeNum> getChangeOrderTypeNum(PageListParam param) {
        ApiReturnData<UserOrderTypeNum>  apiReturnData = new ApiReturnData<UserOrderTypeNum>();
        apiReturnData.setIsOk("0");
        try {
            if (param == null) {
                apiReturnData.setMessage("参数为空");
                return apiReturnData;
            }

            if (StringUtils.isBlank(param.getSiteCode())) {
                apiReturnData.setMessage("站点为空");
                return apiReturnData;
            }

            if (StringUtils.isBlank(param.getUserId())) {
                apiReturnData.setMessage("用户id为空");
                return apiReturnData;
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("siteCode", param.getSiteCode());
            paramMap.put("userId", param.getUserId());
            List<OrderStatusTypeBean> typeMapList = goodsReturnChangePageListMapper.getChangeOrderTypeNum(paramMap);

            apiReturnData.setIsOk("1");
            apiReturnData.setMessage("查询成功");
            if (StringUtil.isListNotNull(typeMapList)) {
                UserOrderTypeNum userOrderTypeNum = new UserOrderTypeNum();
                for (OrderStatusTypeBean map : typeMapList) {
                    if (map == null) {
                        continue;
                    }

                    //查询类型数量
                    int num = map.getNum();
                    //状态
                    int status = map.getStatus();
                    //取消
                    if (status == 0) {
                        userOrderTypeNum.setCancelNum(num);
                        //待沟通
                    } else if (status == 1) {
                        userOrderTypeNum.setCommiuteNum(num);
                        //已完成
                    } else if (status == 2) {
                        userOrderTypeNum.setCompletedNum(num);
                        //待处理
                    } else if (status == 3) {
                        userOrderTypeNum.setProcessNum(num);
                    }
                }

                apiReturnData.setData(userOrderTypeNum);
            }

        } catch (Exception e) {
            logger.error("申请单列表页获取申请单类型数量异常", e);
            apiReturnData.setMessage("获取申请单类型数量异常");
        }

        return apiReturnData;

    }


    /**
     * 获取申请单查询参数
     */
    private GoodsReturnChangeDetailExample getGoodsReturnChangeDetailExample(ReturnChangeGoodsBean returnChangeGoodsBean){
        if (returnChangeGoodsBean == null) {
            return null;
        }
        GoodsReturnChangeDetailExample example = new GoodsReturnChangeDetailExample();
        GoodsReturnChangeDetailExample.Criteria criteria = example.createCriteria();

        //订单号
        String orderSn = returnChangeGoodsBean.getOrderSn();
        if (StringUtils.isNotBlank(orderSn)) {
            criteria.andOrderSnEqualTo(orderSn);
        }

        //申请单号
        String returnChangeSn = returnChangeGoodsBean.getReturnChangeSn();
        if (StringUtils.isNotBlank(returnChangeSn)) {
            criteria.andReturnchangeSnEqualTo(returnChangeSn);
        }

        //类型
        Integer returnType = returnChangeGoodsBean.getReturnType();
        if (returnType != null) {
            criteria.andReturnTypeEqualTo(returnType);
        }

        Integer pageNo = returnChangeGoodsBean.getPageNo();
        Integer pageSize = returnChangeGoodsBean.getPageSize();
        if (pageNo != null && pageSize != null) {
            int start = (pageNo - 1) * pageSize;
            criteria.limit(start, pageSize);
        }

        return example;
    }

    /**
     * 根据申请单号查询申请单详情
     * @param changeSnList
     * @return
     */
    private Map<String, List<GoodsReturnChangeDetailBean>> getDetailList(List<String> changeSnList) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("changeSnList", changeSnList);
        List<GoodsReturnChangeDetailBean> changDetailList = goodsReturnChangeStMapper.getGoodsReturnChangDetail(paramMap);
        if (StringUtil.isListNull(changDetailList)) {
            return null;
        }

        Map<String, List<GoodsReturnChangeDetailBean>> map = new HashMap<String, List<GoodsReturnChangeDetailBean>>();
        for (GoodsReturnChangeDetailBean detail : changDetailList) {
            String returnchangeSn = detail.getReturnChangeSn();
            List<GoodsReturnChangeDetailBean> returnChangeDetails = map.get(returnchangeSn);
            if (returnChangeDetails == null) {
                returnChangeDetails = new ArrayList<GoodsReturnChangeDetailBean>();
            }
            returnChangeDetails.add(detail);
            map.put(returnchangeSn, returnChangeDetails);
        }

        return map;
    }
}
