package com.work.shop.oms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.oms.action.service.OrderActionService;
import com.work.shop.oms.api.param.bean.Paging;
import com.work.shop.oms.bean.MasterOrderGoods;
import com.work.shop.oms.bean.MasterOrderGoodsExample;
import com.work.shop.oms.bean.MasterOrderInfo;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.bean.OrderAttachment;
import com.work.shop.oms.bean.OrderAttachmentExample;
import com.work.shop.oms.bean.OrderDistribute;
import com.work.shop.oms.bean.OrderPay;
import com.work.shop.oms.bean.OrderPayExample;
import com.work.shop.oms.bean.OrderRefund;
import com.work.shop.oms.bean.OrderRefundExample;
import com.work.shop.oms.bean.OrderReturn;
import com.work.shop.oms.bean.OrderReturnExample;
import com.work.shop.oms.bean.OrderReturnGoods;
import com.work.shop.oms.bean.OrderReturnGoodsExample;
import com.work.shop.oms.bean.OrderReturnShip;
import com.work.shop.oms.bean.ProductBarcodeListExample;
import com.work.shop.oms.bean.SystemPayment;
import com.work.shop.oms.bean.bgchanneldb.ChannelShop;
import com.work.shop.oms.channel.service.ChannelInfoService;
import com.work.shop.oms.common.bean.ConstantValues;
import com.work.shop.oms.common.bean.JsonResult;
import com.work.shop.oms.common.bean.PayType;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.config.service.SystemPaymentService;
import com.work.shop.oms.dao.MasterOrderGoodsMapper;
import com.work.shop.oms.dao.MasterOrderInfoMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.dao.OrderAttachmentMapper;
import com.work.shop.oms.dao.OrderDepotShipMapper;
import com.work.shop.oms.dao.OrderDistributeMapper;
import com.work.shop.oms.dao.OrderRefundMapper;
import com.work.shop.oms.dao.OrderRefundSearchMapper;
import com.work.shop.oms.dao.OrderReturnGoodsMapper;
import com.work.shop.oms.dao.OrderReturnMapper;
import com.work.shop.oms.dao.OrderReturnShipMapper;
import com.work.shop.oms.dao.SystemPaymentMapper;
import com.work.shop.oms.orderReturn.service.OrderReturnSearchService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.service.OrderInfoService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.DateTimeUtils;
import com.work.shop.oms.utils.PageHelper;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.utils.TimeUtil;
import com.work.shop.oms.vo.OrderRefundListVO;
import com.work.shop.oms.vo.ReturnAccountVO;
import com.work.shop.oms.vo.ReturnCommonVO;
import com.work.shop.oms.vo.ReturnGoodsVO;
import com.work.shop.oms.vo.ReturnOrderVO;

@Service
public class OrderReturnSearchServiceImpl implements OrderReturnSearchService{
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private OrderReturnMapper orderReturnMapper;
    @Autowired
    private OrderDistributeMapper orderDistributeMapper;
    @Autowired
    private MasterOrderInfoMapper masterOrderInfoMapper;
    @Autowired
    private OrderDepotShipMapper orderDepotShipMapper;
    @Autowired
    private MasterOrderGoodsMapper masterOrderGoodsMapper;
    @Autowired
    private OrderReturnGoodsMapper orderReturnGoodsMapper;
    @Autowired
    private SystemPaymentMapper systemPaymentMapper;
    @Autowired
    private OrderReturnShipMapper orderReturnShipMapper;
    @Autowired
    private OrderRefundMapper orderRefundMapper;
    @Autowired
    private OrderRefundSearchMapper orderRefundSearchMapper;
    @Resource
    private RedisClient redisClient;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private SystemPaymentService systemPaymentService;
    @Resource
    private MasterOrderPayMapper masterOrderPayMapper;
    @Resource
    private OrderAttachmentMapper orderAttachmentMapper;
    @Autowired
    private OrderActionService orderActionService;
	@Resource
	private ChannelInfoService channelInfoService;
    
    @Override
    public OrderReturn getOrderReturnByReturnSn(String returnSn) {
        return orderReturnMapper.selectByPrimaryKey(returnSn);
    }
    
    @Override
    public ReturnOrderVO getOrderReturnDetailVO(String returnSn,
            String relOrderSn,String returnType) {
        
        ReturnOrderVO returnOrderVO = new ReturnOrderVO();
        ReturnCommonVO returnCommon = new ReturnCommonVO();
        List<ReturnGoodsVO> returnGoodsVOList = new ArrayList<ReturnGoodsVO>();
        
        if(StringUtils.isBlank(returnSn)){
            if(StringUtils.isBlank(relOrderSn)){
                throw new RuntimeException("关联原订单为空无法加载退单数据");
            }
            if(StringUtils.isBlank(returnType)){
                throw new RuntimeException("退单申请时退单类型缺失");
            }
            //根据原订单加载退单信息-用于退单申请
            OrderDistribute orderInfo = orderDistributeMapper.selectByPrimaryKey(relOrderSn);
            MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(relOrderSn); 
            if(orderInfo == null && masterOrderInfo == null){
                throw new RuntimeException("关联原订单"+relOrderSn+" 不存在，请迁移三个月内操作");
            }
            if(null != orderInfo){
                returnCommon.setChannelName(orderInfo.getReferer());
                returnCommon.setChannelCode(orderInfo.getOrderFrom());
                returnCommon.setUserId(getUserId(orderInfo));
                returnCommon.setOrderPayedMoney(orderInfo.getMoneyPaid().doubleValue());//订单已付款
                returnCommon.setMasterOrderSn(orderInfo.getMasterOrderSn());//主单号
                if (masterOrderInfo == null) {
                    MasterOrderInfo master = masterOrderInfoMapper.selectByPrimaryKey(orderInfo.getMasterOrderSn());
                    returnCommon.setSiteCode(master.getChannelCode());
                }
                if (orderInfo.getShipStatus().intValue() >= Constant.OI_SHIP_STATUS_PARTSHIPPED) {
                	returnOrderVO.setOrderShipType(1);
                }
            }else{
            	if (masterOrderInfo.getShipStatus().intValue() >= Constant.OI_SHIP_STATUS_PARTSHIPPED) {
                	returnOrderVO.setOrderShipType(1);
                }
                returnCommon.setChannelName(masterOrderInfo.getReferer());
                returnCommon.setChannelCode(masterOrderInfo.getOrderFrom());
                returnCommon.setUserId(masterOrderInfo.getUserId());
                returnCommon.setOrderPayedMoney(masterOrderInfo.getMoneyPaid().doubleValue());//订单已付款
                returnCommon.setMasterOrderSn(masterOrderInfo.getMasterOrderSn());//主单号
                returnCommon.setSiteCode(masterOrderInfo.getChannelCode());
            }
            
            returnCommon.setRelatingOrderSn(relOrderSn);
            returnCommon.setReturnType(Integer.valueOf(returnType));
            returnCommon.setReturnStatusDisplay("申请中");
            returnCommon.setDepotCode(Constant.DETAILS_DEPOT_CODE);
            //已发货发货单
            /*OrderDepotShipExample orderDepotShipExample = new OrderDepotShipExample();
            orderDepotShipExample.or().andOrderSnEqualTo(relOrderSn).andShippingStatusNotEqualTo((byte)0);
            List<OrderDepotShip> orderShipList = orderDepotShipMapper.selectByExample(orderDepotShipExample);
            List<String> shipDepotList = new ArrayList<String>();
            if(CollectionUtils.isNotEmpty(orderShipList)){
                for (OrderDepotShip ship : orderShipList) {
                    shipDepotList.add(ship.getDepotCode());
                }
            }*/
            MasterOrderGoodsExample masterOrderGoodsExample = new MasterOrderGoodsExample();
            MasterOrderGoodsExample.Criteria criteria = masterOrderGoodsExample.or();
            if(orderInfo !=null){
                criteria.andOrderSnEqualTo(relOrderSn);
            }else{
                criteria.andMasterOrderSnEqualTo(relOrderSn);
            }
            
            /*if(CollectionUtils.isNotEmpty(shipDepotList)){
                criteria.andDepotCodeIn(shipDepotList); 
            }*/
            criteria.andIsDelNotEqualTo(ConstantValues.YESORNO_YES);
            List<MasterOrderGoods> orderGoodsList = masterOrderGoodsMapper.selectByExample(masterOrderGoodsExample);
            if(CollectionUtils.isEmpty(orderGoodsList)){
                throw new RuntimeException("原订单已发货商品列表为空");
            }
            
            //获取已退货量
            List<Byte> typeList = new ArrayList<Byte>();
            typeList.add(ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS);
            typeList.add(ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS);
            OrderReturnExample orderReturnExample=new OrderReturnExample();
            orderReturnExample.or().andReturnTypeIn(typeList)
            .andReturnOrderStatusNotEqualTo(ConstantValues.ORDERRETURN_STATUS.INVALIDITY)
            .andRelatingOrderSnEqualTo(relOrderSn);
            List<OrderReturn>orderReturnList=orderReturnMapper.selectByExample(orderReturnExample);
            List<OrderReturnGoods>orderReturnGoodsList=null;
            Double oldReturnBonusMoney = 0d;
            
            //已退货量汇总统计
            Map<String,Integer> haveReturnGoodsMap = new HashMap<String,Integer>();
            if(orderReturnList.size()>0){
                //根据查出来的退单sn从退单商品表获取所有关联商品的已退货商品数量
                List<String>returnSns=new ArrayList<String>();
                for (OrderReturn orderReturn : orderReturnList) {
                    returnSns.add(orderReturn.getReturnSn());
                    oldReturnBonusMoney += orderReturn.getReturnBonusMoney().doubleValue();
                }
                OrderReturnGoodsExample orderReturnGoodsExample=new OrderReturnGoodsExample();
                orderReturnGoodsExample.or().andRelatingReturnSnIn(returnSns);
                orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
                if(CollectionUtils.isNotEmpty(orderReturnGoodsList)){
                    for(OrderReturnGoods orderReturnGoods:orderReturnGoodsList){
                        Integer haveReturn = orderReturnGoods.getGoodsReturnNumber().intValue();
                        Integer haveReturn2 = orderReturnGoods.getGoodsReturnNumber().intValue();
                        String returnKey = orderReturnGoods.getCustomCode()+orderReturnGoods.getExtensionCode()+orderReturnGoods.getExtensionId()+orderReturnGoods.getOsDepotCode();
                        String returnKey2 = orderReturnGoods.getCustomCode()+orderReturnGoods.getExtensionCode()+orderReturnGoods.getOsDepotCode();
                        if(haveReturnGoodsMap.containsKey(returnKey)){
                            haveReturn = haveReturnGoodsMap.get(returnKey) + orderReturnGoods.getGoodsReturnNumber().intValue();
                        }
                        if(haveReturnGoodsMap.containsKey(returnKey2)){
                            haveReturn2 = haveReturnGoodsMap.get(returnKey2) + orderReturnGoods.getGoodsReturnNumber().intValue();
                        }
                        haveReturnGoodsMap.put(returnKey, haveReturn);
                        haveReturnGoodsMap.put(returnKey2, haveReturn2);
                    }
                }
            }
            
            //商品信息
            returnGoodsVOList = new ArrayList<ReturnGoodsVO>();
            ReturnGoodsVO returnGoodsVO = new ReturnGoodsVO();
            double totalGoodsMoney = 0D;
            double settlementPrice = 0D;
            for (MasterOrderGoods orderGoods : orderGoodsList) {
                returnGoodsVO = new ReturnGoodsVO();
                returnGoodsVO.setSap(orderGoods.getSap());
                returnGoodsVO.setBvValue(StringUtil.isTrimEmpty(orderGoods.getBvValue()) ? 0 : Integer.valueOf(orderGoods.getBvValue()));
                returnGoodsVO.setCustomCode(orderGoods.getCustomCode());
                returnGoodsVO.setGoodsSizeName(orderGoods.getGoodsSizeName());
                returnGoodsVO.setGoodsColorName(orderGoods.getGoodsColorName());
                returnGoodsVO.setGoodsThumb(orderGoods.getGoodsThumb());//图片url
                returnGoodsVO.setGoodsName(orderGoods.getGoodsName());
                returnGoodsVO.setExtensionId(orderGoods.getExtensionId());
                returnGoodsVO.setExtensionCode(orderGoods.getExtensionCode());
                returnGoodsVO.setGoodsSn(orderGoods.getGoodsSn());
                returnGoodsVO.setMarketPrice(orderGoods.getGoodsPrice().doubleValue());
                returnGoodsVO.setGoodsPrice(orderGoods.getTransactionPrice().doubleValue());
                returnGoodsVO.setSettlementPrice(orderGoods.getSettlementPrice().doubleValue());
                returnGoodsVO.setShareBonus(orderGoods.getShareBonus().doubleValue());
                returnGoodsVO.setGoodsBuyNumber(orderGoods.getGoodsNumber().intValue());
                returnGoodsVO.setDiscount(orderGoods.getDiscount().doubleValue());
                returnGoodsVO.setOsDepotCode(orderGoods.getDepotCode());
                returnGoodsVO.setShopReturnCount(orderGoods.getChargeBackCount());
                if (orderGoods.getSalesMode() == null) {
                    returnGoodsVO.setSalesMode(1);//商品销售模式：1为自营，2为买断，3为寄售，4为直发
                } else {
                    returnGoodsVO.setSalesMode(orderGoods.getSalesMode().intValue());//商品销售模式：1为自营，2为买断，3为寄售，4为直发
                }
                returnGoodsVO.setSeller(orderGoods.getSupplierCode());//供销商编码
                if(Integer.valueOf(returnType).intValue() == ConstantValues.YESORNO_YES.intValue() || Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()){//退货单时才让积分展示
                    returnGoodsVO.setIntegralMoney(orderGoods.getIntegralMoney().doubleValue());//使用积分金额
                }
                returnGoodsVO.setPayPoints(orderGoods.getPayPoints().doubleValue());//消费积分
                returnGoodsVO.setProdScanNum(0);//默认扫描数量为0
                returnGoodsVO.setPriceDifferNum(orderGoods.getGoodsNumber().intValue());//退差价数量
                //退货单
                returnGoodsVO.setOsDepotCode(orderGoods.getDepotCode());
                returnGoodsVO.setShopReturnCount(orderGoods.getChargeBackCount());
                //已退货数量
                String returnKey = orderGoods.getCustomCode()+orderGoods.getExtensionCode()+orderGoods.getExtensionId()+orderGoods.getDepotCode();
                String returnKey2 = orderGoods.getCustomCode()+orderGoods.getExtensionCode()+orderGoods.getDepotCode();
                if(MapUtils.isNotEmpty(haveReturnGoodsMap) && haveReturnGoodsMap.containsKey(returnKey)){
                    if(haveReturnGoodsMap.get(returnKey) > haveReturnGoodsMap.get(returnKey2)){
                        haveReturnGoodsMap.put(returnKey,0); 
                    }
                    returnGoodsVO.setHavedReturnCount(haveReturnGoodsMap.get(returnKey));
                    haveReturnGoodsMap.put(returnKey2, haveReturnGoodsMap.get(returnKey2)-haveReturnGoodsMap.get(returnKey));
                }else if(MapUtils.isNotEmpty(haveReturnGoodsMap) && haveReturnGoodsMap.containsKey(returnKey2)){
                    returnGoodsVO.setHavedReturnCount(haveReturnGoodsMap.get(returnKey2));
                    haveReturnGoodsMap.put(returnKey2, 0);
                }else{
                    returnGoodsVO.setHavedReturnCount(0);
                }
                //可退货量为零
                int canReturnCount = orderGoods.getGoodsNumber().intValue()-orderGoods.getChargeBackCount()-returnGoodsVO.getHavedReturnCount();
                if(canReturnCount < 1 && (returnCommon.getReturnType() == 1 || returnCommon.getReturnType() == 2 || returnCommon.getReturnType() == 5)){
                    continue;
                }
                returnGoodsVO.setCanReturnCount(canReturnCount);
                settlementPrice += orderGoods.getSettlementPrice().doubleValue()*orderGoods.getGoodsNumber().intValue();
                totalGoodsMoney += returnGoodsVO.getGoodsPrice().doubleValue() * returnGoodsVO.getCanReturnCount().intValue();
                returnGoodsVO.setBvValue(StringUtil.isTrimEmpty(orderGoods.getBvValue()) ? 0 : Integer.valueOf(orderGoods.getBvValue()));
                returnGoodsVO.setBaseBvValue(orderGoods.getBaseBvValue());
                returnGoodsVOList.add(returnGoodsVO);
            }
            
            //支付方式
            List<PayType> createOrderPayList = new ArrayList<PayType>();
            double shippingTotalFee = 0d;
            //账目信息
            ReturnAccountVO returnAccount = new ReturnAccountVO();
            if(null != orderInfo){
                returnAccount.setReturnBonusMoney(orderInfo.getBonus().doubleValue() - oldReturnBonusMoney);
//                returnAccount.setReturnShipping(orderInfo.getShippingTotalFee().doubleValue());
                shippingTotalFee = orderInfo.getShippingTotalFee().doubleValue();
                returnAccount.setTotalIntegralMoney(orderInfo.getIntegralMoney().doubleValue());//积分使用金额
                
//                createOrderPayList =  JSON.parseArray(orderInfo.getPayInfo(), PayType.class);
                MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
                masterOrderPayExample.or().andMasterOrderSnEqualTo(orderInfo.getMasterOrderSn());
                List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
                for (MasterOrderPay masterOrderPay : masterOrderPayList) {
                    if(settlementPrice <= masterOrderPay.getPayTotalfee().doubleValue()){
                        PayType payType = new PayType();
                        payType.setPId(masterOrderPay.getPayId());
                        payType.setPayFee(settlementPrice);
                        createOrderPayList.add(payType);
                        settlementPrice = 0;
                    }else{
                        PayType payType = new PayType();
                        payType.setPId(masterOrderPay.getPayId());
                        payType.setPayFee(masterOrderPay.getPayTotalfee().doubleValue());
                        createOrderPayList.add(payType);
                        settlementPrice = settlementPrice - masterOrderPay.getPayTotalfee().doubleValue();
                    }
                }
                
               /* //检查对应子单是否原先因删除商品或其他原因产生过退货单,假如有，就用pay_Info里面的钱减去refund里面的钱
                OrderReturnExample returnExample = new OrderReturnExample();
                returnExample.or().andRelatingOrderSnEqualTo(orderInfo.getOrderSn()).andReturnTypeLessThanOrEqualTo(ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE);
                List<OrderReturn> returnList = orderReturnMapper.selectByExample(returnExample);
                List<String> returnSnList = new ArrayList<String>();
                
                if(CollectionUtils.isNotEmpty(returnList)){
                    for (OrderReturn orderReturn : returnList) {
                        returnSnList.add(orderReturn.getReturnSn());
                    }
                    OrderRefundExample orderRefundExample = new OrderRefundExample();
                    orderRefundExample.or().andRelatingReturnSnIn(returnSnList);
                    List<OrderRefund> refundList = orderRefundMapper.selectByExample(orderRefundExample);
                    
                    List<PayType> payTypeList =  new ArrayList<PayType>();
                    if(CollectionUtils.isNotEmpty(refundList)){
                        for(PayType payType : createOrderPayList){
                            Double payFee = payType.getPayFee();
                            for (OrderRefund orderRefund : refundList) {
                                if(Integer.valueOf(payType.getpId()).intValue() == orderRefund.getReturnPay().intValue()){
                                    payFee = payFee.doubleValue() - orderRefund.getReturnFee().doubleValue();
                                }
                            }
                            payType.setPayFee(payFee);
                            payTypeList.add(payType);
                            
                        }
                    }
                    
                    if(CollectionUtils.isNotEmpty(payTypeList)){
                        createOrderPayList = payTypeList;
                    }
                    
                }*/
                
            }else{
                MasterOrderPayExample masterOrderPayExample = new MasterOrderPayExample();
                masterOrderPayExample.or().andMasterOrderSnEqualTo(relOrderSn);
                List<MasterOrderPay> masterOrderPayList = masterOrderPayMapper.selectByExample(masterOrderPayExample);
                for (MasterOrderPay masterOrderPay : masterOrderPayList) {
                    PayType payType = new PayType();
                    payType.setPId(masterOrderPay.getPayId());
                    payType.setPayFee(masterOrderPay.getPayTotalfee().doubleValue());
                    createOrderPayList.add(payType);
                }
                returnAccount.setReturnBonusMoney(masterOrderInfo.getBonus().doubleValue());
//                returnAccount.setReturnShipping(masterOrderInfo.getShippingTotalFee().doubleValue());
                shippingTotalFee = masterOrderInfo.getShippingTotalFee().doubleValue();
                returnAccount.setTotalIntegralMoney(masterOrderInfo.getIntegralMoney().doubleValue());//积分使用金额
            }
            returnAccount.setReturnGoodsMoney(totalGoodsMoney);
            if(Integer.valueOf(returnType).intValue() == ConstantValues.YESORNO_YES.intValue() || Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()){//退货单和失货退货单时才让积分展示
            }else{
                returnAccount.setTotalIntegralMoney(0d);//积分使用金额
            }
            
            //付款备注
//          returnAccount.setOrderPayDesc(processOrderPayDesc(orderInfo.getOrderSn()));
            //汇总退单总金额 -初始值
            if(Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue() 
                    || Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()
                    || Integer.valueOf(returnType).intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()){
                //总货款 
                returnAccount.setReturnTotalFee(returnAccount.getReturnGoodsMoney().doubleValue() -returnAccount.getTotalIntegralMoney().doubleValue()-returnAccount.getReturnBonusMoney().doubleValue());
            }else{
                //运费
                returnAccount.setReturnTotalFee(shippingTotalFee);
            }
//            //退单申请 - 付款信息平摊
//            List<OrderPay> createOrderPayList = processOrderPayShareList(orderInfo.getOrderSn(), orderInfo.getTransType().intValue(), returnAccount.getReturnTotalFee().doubleValue());
//            logger.debug("getOrderReturnDetailVO.orderSn:"+orderInfo.getOrderSn()+",createOrderPayList:"+JSON.toJSONString(createOrderPayList));
            
            
            returnOrderVO.setReturnCommon(returnCommon);
            returnOrderVO.setReturnGoodsList(returnGoodsVOList);
            returnOrderVO.setReturnAccount(returnAccount);
            returnOrderVO.setOrderPayList(createOrderPayList);
        }else{
            OrderReturn orderReturn = orderReturnMapper.selectByPrimaryKey(returnSn);
            if(orderReturn == null){
                throw new RuntimeException("退单数据不存在");
            }
            ReturnInfo<ChannelShop> info = channelInfoService.findShopInfoByShopCode(orderReturn.getChannelCode());
            if (info == null || info.getIsOk() == Constant.OS_NO) {
            	throw new RuntimeException("渠道数据不存在"+orderReturn.getChannelCode());
            }
            if(info.getData() == null){
                throw new RuntimeException("渠道数据不存在"+orderReturn.getChannelCode());
            }
            ChannelShop channelInfo = info.getData();
            //配送信息（配送方式，快递号）
            OrderReturnShip orderReturnShip = orderReturnShipMapper.selectByPrimaryKey(returnSn);
            if(orderReturnShip == null){
                throw new RuntimeException("退单发货扩展表不存在");
            }
            relOrderSn = orderReturn.getMasterOrderSn();
            if(StringUtils.isBlank(relOrderSn)){
                throw new RuntimeException("关联原订单为空无法加载退单数据");
            }
            if(StringUtils.isBlank(returnType)){
                throw new RuntimeException("退单申请时退单类型缺失");
            }
            //根据原订单加载退单信息-用于退单申请
            OrderDistribute orderInfo = orderDistributeMapper.selectByPrimaryKey(relOrderSn);
            MasterOrderInfo masterOrderInfo = masterOrderInfoMapper.selectByPrimaryKey(relOrderSn); 
            if(orderInfo == null && masterOrderInfo == null){
                throw new RuntimeException("关联原订单"+relOrderSn+" 不存在，请迁移三个月内操作");
            }
            
            returnCommon.setReturnSn(orderReturn.getReturnSn());
            returnCommon.setRelatingOrderSn(orderReturn.getRelatingOrderSn());
            returnCommon.setReturnType(orderReturn.getReturnType().intValue());
            returnCommon.setReturnOrderStatus(orderReturn.getReturnOrderStatus().intValue());
            returnCommon.setPayStatus(orderReturn.getPayStatus().intValue());
            returnCommon.setShipsStatus(orderReturnShip.getCheckinStatus().intValue());
            returnCommon.setHaveRefund(orderReturn.getHaveRefund().intValue());
            returnCommon.setChannelName(channelInfo.getShopTitle());
            returnCommon.setChannelCode(channelInfo.getChannelCode());
            returnCommon.setUserId(orderReturn.getUserId());
            returnCommon.setReturnDesc(orderReturn.getReturnDesc());
            returnCommon.setIsGoodReceived(orderReturnShip.getIsGoodReceived().intValue());
            returnCommon.setChasedOrNot(orderReturnShip.getChasedOrNot().intValue());
            returnCommon.setReturnSettlementType(orderReturn.getReturnSettlementType().intValue());
            returnCommon.setProcessType(orderReturn.getProcessType().intValue());
            returnCommon.setReturnExpress(orderReturnShip.getReturnExpress());
            returnCommon.setReturnInvoiceNo(orderReturnShip.getReturnInvoiceNo());
            returnCommon.setNewOrderSn(orderReturn.getNewOrderSn());
            returnCommon.setDepotCode(orderReturnShip.getDepotCode());
            returnCommon.setAddTime(DateTimeUtils.format(orderReturn.getAddTime()));
            returnCommon.setCheckInTime(DateTimeUtils.format(orderReturnShip.getCheckinTime()));
            returnCommon.setClearTime(DateTimeUtils.format(orderReturn.getClearTime()));
            returnCommon.setReturnOrderIspass(orderReturnShip.getQualityStatus());
            returnCommon.setLockStatus(orderReturn.getLockStatus());
            returnCommon.setCheckinStatus(orderReturnShip.getCheckinStatus().intValue());
            returnCommon.setBackToCs(orderReturn.getBackToCs());
            returnCommon.setQualityStatus(orderReturnShip.getQualityStatus());
            returnCommon.setReturnStatusDisplay(displayReturnStatus(returnCommon));
            returnCommon.setReturnReason(orderReturn.getReturnReason());
            returnCommon.setRefundType(orderReturn.getRefundType().intValue());
            returnCommon.setMasterOrderSn(orderReturn.getMasterOrderSn());//主单号
            returnCommon.setToErp(orderReturn.getToErp().intValue());
            returnCommon.setSiteCode(orderReturn.getSiteCode());
            //商品数据
            OrderReturnGoodsExample orderReturnGoodsExample = new OrderReturnGoodsExample();
            orderReturnGoodsExample.or().andRelatingReturnSnEqualTo(returnSn);
            List<OrderReturnGoods> orderReturnGoodsList = orderReturnGoodsMapper.selectByExample(orderReturnGoodsExample);
            returnGoodsVOList = new ArrayList<ReturnGoodsVO>();
            ReturnGoodsVO returnGoodsVO = new ReturnGoodsVO();
            if(CollectionUtils.isNotEmpty(orderReturnGoodsList)){
                for (OrderReturnGoods orderReturnGoods : orderReturnGoodsList) {
                    returnGoodsVO = new ReturnGoodsVO();
                    
                    ProductBarcodeListExample productBarcodeListExample = new ProductBarcodeListExample();
                    productBarcodeListExample.or().andCustumCodeEqualTo(orderReturnGoods.getCustomCode());
//                  List<ProductBarcodeList> productList = productBarcodeListMapper.selectByExample(productBarcodeListExample);
                    /*if(CollectionUtils.isEmpty(productList)){
                        throw new RuntimeException("无法获取有效的商品基础数据");
                    }*/
                    returnGoodsVO.setId(orderReturnGoods.getId());
                    returnGoodsVO.setIsGoodReceived(orderReturnGoods.getIsGoodReceived().intValue());
                    returnGoodsVO.setQualityStatus(orderReturnGoods.getQualityStatus().intValue());
                    returnGoodsVO.setCheckinStatus(orderReturnGoods.getCheckinStatus().intValue());
                    
                    returnGoodsVO.setExtensionId(orderReturnGoods.getExtensionId());
                    returnGoodsVO.setShareSettle(orderReturnGoods.getShareSettle().doubleValue());
                    returnGoodsVO.setGoodsName(orderReturnGoods.getGoodsName());
                    returnGoodsVO.setExtensionCode(orderReturnGoods.getExtensionCode());
                    if(null != orderReturnGoods.getCustomCode()){
                        String goodsSn = orderReturnGoods.getCustomCode().substring(0, 6);
                        returnGoodsVO.setGoodsSn(goodsSn);
                    }
                    returnGoodsVO.setGoodsColorName(orderReturnGoods.getGoodsColorName());
                    returnGoodsVO.setGoodsSizeName(orderReturnGoods.getGoodsSizeName());
                    returnGoodsVO.setCustomCode(orderReturnGoods.getCustomCode());
                    returnGoodsVO.setMarketPrice(orderReturnGoods.getMarketPrice().doubleValue());
                    returnGoodsVO.setGoodsPrice(orderReturnGoods.getGoodsPrice().doubleValue());
                    returnGoodsVO.setSettlementPrice(orderReturnGoods.getSettlementPrice().doubleValue());
                    returnGoodsVO.setShareBonus(orderReturnGoods.getShareBonus().doubleValue());
                    returnGoodsVO.setGoodsBuyNumber(orderReturnGoods.getGoodsBuyNumber().intValue());
                    returnGoodsVO.setDiscount((orderReturnGoods.getMarketPrice().doubleValue() - orderReturnGoods.getGoodsPrice().doubleValue())*orderReturnGoods.getGoodsBuyNumber().intValue());
                    returnGoodsVO.setProdScanNum(orderReturnGoods.getProdscannum().intValue());//实际扫描数量
                    returnGoodsVO.setSalesMode(orderReturnGoods.getSalesMode().intValue());
                    //退货单
                    returnGoodsVO.setOsDepotCode(orderReturnGoods.getOsDepotCode());
                    returnGoodsVO.setShopReturnCount(orderReturnGoods.getChargeBackCount());
                    returnGoodsVO.setHavedReturnCount(orderReturnGoods.getHaveReturnCount());
                    returnGoodsVO.setCanReturnCount(orderReturnGoods.getGoodsReturnNumber().intValue());//可退货量（退货详情页面的可退货量直接从表里取字段就可以了）
                    returnGoodsVO.setReturnReason(orderReturnGoods.getReturnReason());
                    //退款单
                    returnGoodsVO.setPriceDifferNum(orderReturnGoods.getPriceDifferNum().intValue());//退差价数量
                    returnGoodsVO.setPriceDifference(orderReturnGoods.getPriceDifference().doubleValue());//退差价单价
                    if(orderReturn.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_EXTRA_PAY.intValue()){
                        returnGoodsVO.setReturnReason(orderReturnGoods.getPriceDifferReason());
                    }else{
                        returnGoodsVO.setReturnReason(orderReturnGoods.getReturnReason());
                    }
                    returnGoodsVO.setSeller(orderReturnGoods.getSeller());
                    returnGoodsVO.setIntegralMoney(orderReturnGoods.getIntegralMoney().doubleValue());
                    returnGoodsVOList.add(returnGoodsVO);
                }
            }
            
            //付款信息（支付方式+金额）
            OrderRefundExample orfEx=new OrderRefundExample();
            orfEx.or().andRelatingReturnSnEqualTo(returnSn);
            List<OrderRefund> oRefundList=orderRefundMapper.selectByExample(orfEx);
            
            //账目信息
            ReturnAccountVO returnAccount = new ReturnAccountVO();
            if(orderInfo !=null){
                returnAccount.setBonus(orderInfo.getBonus().doubleValue());//原订单红包
                returnAccount.setShippingTotalFee(orderInfo.getShippingTotalFee().doubleValue());//原订单配送费用
            }else{
                returnAccount.setBonus(masterOrderInfo.getBonus().doubleValue());//原订单红包
                returnAccount.setShippingTotalFee(masterOrderInfo.getShippingTotalFee().doubleValue());//原订单配送费用
                
            }
            returnAccount.setReturnTotalFee(orderReturn.getReturnTotalFee().doubleValue());
            returnAccount.setReturnGoodsMoney(orderReturn.getReturnGoodsMoney().doubleValue());
            returnAccount.setReturnShipping(orderReturn.getReturnShipping().doubleValue());
            returnAccount.setReturnBonusMoney(orderReturn.getReturnBonusMoney().doubleValue());
            returnAccount.setReturnOtherMoney(orderReturn.getReturnOtherMoney().doubleValue());
            returnAccount.setTotalIntegralMoney(orderReturn.getIntegralMoney().doubleValue());
            //付款备注
//          returnAccount.setOrderPayDesc(processOrderPayDesc(orderInfo.getOrderSn()));
            returnOrderVO.setReturnCommon(returnCommon);
            returnOrderVO.setReturnGoodsList(returnGoodsVOList);
            returnOrderVO.setReturnAccount(returnAccount);
            returnOrderVO.setReturnRefundList(oRefundList);
        }
        return returnOrderVO;
    }
    
    private String getUserId(OrderDistribute orderInfo) {
        if(StringUtils.isNotBlank(orderInfo.getMasterOrderSn())){
          return masterOrderInfoMapper.selectByPrimaryKey(orderInfo.getMasterOrderSn()).getUserId();
        }else{
            return null;
        }
    }

    /**
     * 退单-付款单平台总退单金额，默认在线支付
     * @param orderSn 
     * @param totalReturnMoney //退款总金额已经减掉积分使用金额和红包的
     */
    private List<OrderPay> processOrderPayShareList(String orderSn,Integer transType,Double totalReturnMoney){
        OrderPayExample orderPayExample = new OrderPayExample();
        if(Constant.OI_TRANS_TYPE_PRESHIP == transType.intValue()){
            //货到付款
            orderPayExample.or().andOrderSnEqualTo(orderSn);
        }else{
            orderPayExample.or().andOrderSnEqualTo(orderSn).andPayStatusIn(Arrays.asList(new Byte[]{ConstantValues.OP_ORDER_PAY_STATUS.PAYED.byteValue(),ConstantValues.OP_ORDER_PAY_STATUS.SETTLEMENT.byteValue()}));
            
//          .andPayStatusEqualTo(ConstantValues.OP_ORDER_PAY_STATUS.PAYED.byteValue());
        }
        List<OrderPay> createOrderPayList = new ArrayList<OrderPay>();
        /*List<OrderPay> orderPayList = orderPayMapper.selectByExample(orderPayExample);
        
        Byte surplusPay = Byte.valueOf("3");//余额支付
        String surplusPayName = systemPaymentMapper.selectByPrimaryKey(surplusPay).getPayName();
        boolean existSurplusPay = false;//是否存在余额支付
       
        List<OrderPay> surplusPayList = new ArrayList<OrderPay>();
        Map<Byte,Double> limitOrderPay = new HashMap<Byte, Double>();
        
        if(CollectionUtils.isNotEmpty(orderPayList)){
            for (OrderPay orderPay : orderPayList) {
                double totalPayFee = 0D;
                if(orderPay.getPayId().byteValue() == surplusPay.byteValue()){
                    totalPayFee = orderPay.getSurplus().doubleValue();
                }else{
                    totalPayFee = orderPay.getPayTotalfee().doubleValue();
                }
                if(!limitOrderPay.containsKey(orderPay.getPayId())){
                    limitOrderPay.put(orderPay.getPayId(), totalPayFee);
                }else{
                    limitOrderPay.put(orderPay.getPayId(), limitOrderPay.get(orderPay.getPayId()).doubleValue() + totalPayFee);
                }
                //非余额支付的付款单中，存在余额
                if(orderPay.getPayId().byteValue() != surplusPay.byteValue() && orderPay.getSurplus().doubleValue() > 0){
                    if(!limitOrderPay.containsKey(surplusPay)){
                        limitOrderPay.put(surplusPay, orderPay.getSurplus().doubleValue());
                    }else{
                        limitOrderPay.put(surplusPay, limitOrderPay.get(surplusPay).doubleValue() + orderPay.getSurplus().doubleValue());
                    }
                }
            }
        }

        logger.debug("processOrderPayShareList.orderSn:"+orderSn+",limitOrderPay:"+JSON.toJSONString(limitOrderPay));
        if(limitOrderPay.containsKey(surplusPay)){
            //存在余额支付
            limitOrderPay.remove(surplusPay);
            existSurplusPay = true;
        }
        if(MapUtils.isEmpty(limitOrderPay)){
            //当前只有余额支付
            OrderPay orderPay = new OrderPay();
            orderPay.setPayTotalfee(BigDecimal.valueOf(totalReturnMoney));
            orderPay.setPayId(surplusPay);
            orderPay.setPayName(surplusPayName);
            surplusPayList.add(orderPay);
        }else{
            //平摊非余额支付
            Set<Byte> payIdSet = limitOrderPay.keySet();
            for (Byte payId : payIdSet) {
                if(totalReturnMoney <= 0){
                    //主要针对额外退款单  退单金额为零情况
                    OrderPay orderPay = new OrderPay();
                    orderPay.setPayTotalfee(BigDecimal.valueOf(totalReturnMoney));
                    String payName = systemPaymentMapper.selectByPrimaryKey(payId).getPayName();
                    orderPay.setPayId(payId);
                    orderPay.setPayName(payName);
                    createOrderPayList.add(orderPay);
                    break;
                }
                //按照支付方式进行平摊
                double returnMoney = 0D;
                if(totalReturnMoney > limitOrderPay.get(payId)){
                    returnMoney = limitOrderPay.get(payId);
                    totalReturnMoney = totalReturnMoney - returnMoney;
                }else{
                    returnMoney = totalReturnMoney;
                    totalReturnMoney = 0D;
                }
                OrderPay orderPay = new OrderPay();
                orderPay.setPayTotalfee(BigDecimal.valueOf(returnMoney));
                String payName = systemPaymentMapper.selectByPrimaryKey(payId).getPayName();
                orderPay.setPayId(payId);
                orderPay.setPayName(payName);
                createOrderPayList.add(orderPay);
            }
            if(existSurplusPay && totalReturnMoney >= 0){
                OrderPay orderPay = new OrderPay();
                orderPay.setPayTotalfee(BigDecimal.valueOf(totalReturnMoney));
                orderPay.setPayId(surplusPay);
                orderPay.setPayName(surplusPayName);
                surplusPayList.add(orderPay);
            }
        }
        //将余额放到最后
        if(CollectionUtils.isNotEmpty(surplusPayList)){
            createOrderPayList.addAll(surplusPayList);
        }*/
        return createOrderPayList;          
        
    }
    
    /**
     * 退单状态显示
     * @param returnOrderVO
     * @return
     */
    private String displayReturnStatus(ReturnCommonVO common){
        
        //退单状态
        String orderStatus = StringUtils.EMPTY;
        if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.UN_CONFIRM){
            orderStatus = "未确认";
        }else if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.CONFIRMED){
            orderStatus = "已确认";
        }else if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.INVALIDITY){
            orderStatus = "无效";
        }else if(common.getReturnOrderStatus().intValue() == ConstantValues.ORDERRETURN_STATUS.COMPLETE){
            orderStatus = "已完成";
        }else{
            orderStatus = "异常";
        }
        //付款状态
        String payStatus = StringUtils.EMPTY;
        if(common.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.SETTLED){
            payStatus = "已结算";
        }else if(common.getPayStatus().intValue() == ConstantValues.ORDERRETURN_PAY_STATUS.WAITSETTLE){
            payStatus = "待结算";
        }else{
            payStatus = "未结算";
        }
        String receiveStatus = StringUtils.EMPTY;
        String passStatus = StringUtils.EMPTY;
        String checkInStatus = StringUtils.EMPTY;
        if(common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_GOODS.intValue()
                || common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.REJECTION_AND_WAREHOUSE.intValue()
                || common.getReturnType().intValue() == ConstantValues.ORDERRETURN_TYPE.RETURN_LOSE_GOODS.intValue()){
            //收货状态
        	/*if(common.getIsGoodReceived().intValue() == ConstantValues.YESORNO_YES){
                receiveStatus = "已收货";
            }else if(common.getIsGoodReceived().intValue() == ConstantValues.ORDERRETURN_GOODS_RECEIVED.PARTRECEIVED.intValue()){
                receiveStatus = "部分收货";
            }else{
                receiveStatus = "未收货";
            }
            //质检状态
            if(common.getReturnOrderIspass().intValue() == ConstantValues.YESORNO_YES){
                passStatus = "质检通过";
            }else if(common.getReturnOrderIspass().intValue() == ConstantValues.ORDERRETURNSHIP_ISPASS_STATUS.PARTPASS.intValue()){
                passStatus = "部分质检通过";
            }else{
                passStatus = "质检未通过";
            }*/
            //入库状态
            if(common.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.STORAGE){
                checkInStatus = "已入库";
            }else if(common.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.WAITSTORAGE){
                checkInStatus = "待入库";
            }else if(common.getCheckinStatus().intValue() == ConstantValues.ORDERRETURNSHIP_CHECKIN_STATUS.PARTSTORAGE){
                checkInStatus = "部分入库";
            }else{
                checkInStatus = "未入库";
            }
        }
//        return orderStatus + "&nbsp;" + payStatus + "&nbsp;" + receiveStatus + "&nbsp;" + passStatus + "&nbsp;" + checkInStatus;
        return orderStatus + "&nbsp;" + payStatus + "&nbsp;" + passStatus + "&nbsp;" + checkInStatus;
    }
    
    /**
     * 加载发货仓库信息
     * @param orderSn
     * @return
     */
    @Override
    public List<ChannelShop> getOrderReturnGoodsDepotList(String orderSn){
        List<ChannelShop> channelShops = new ArrayList<ChannelShop>();
        OrderDistribute orderDistribute = orderDistributeMapper.selectByPrimaryKey(orderSn);
        channelShops=null;
        if(orderDistribute !=null ){
            if(orderDistribute.getSource().intValue() == Constant.OD_SOURCE_BG){
                channelShops = new ArrayList<ChannelShop>();
            }
        }
        
        String mobileMsg = StringUtils.EMPTY;
        List<String> depotList = new ArrayList<String>();
        MasterOrderGoodsExample masterOrderGoodsExample = new MasterOrderGoodsExample();
        masterOrderGoodsExample.or().andOrderSnEqualTo(orderSn);
        List<MasterOrderGoods> orderGoodsList = masterOrderGoodsMapper.selectByExample(masterOrderGoodsExample);
        if(CollectionUtils.isNotEmpty(orderGoodsList)){
            for (MasterOrderGoods orderGoods : orderGoodsList) {
                if(StringUtils.isNotBlank(orderGoods.getDepotCode()) 
                        && !depotList.contains(orderGoods.getDepotCode())){
                    depotList.add(orderGoods.getDepotCode());
                    ChannelShop channelShop = getChannelShopListByDepotCode(orderGoods.getDepotCode());
                    if(channelShop != null){
//                        mobileMsg=ConstantsUtil.returnOrderMobileMsg(channelShop);
//                        channelShop.setMobileMsg(mobileMsg);
                        channelShops.add(channelShop);
                    }
                }
            }
        }
        return channelShops;
    }
    
    /**
     * 店铺信息缓存Redis
     * @param depotCode
     * @return
     */
    private ChannelShop getChannelShopListByDepotCode(String depotCode){
        if(StringUtils.isBlank(depotCode)) return null;
        String response = null;
        try {
            String redisKey = "Os_Refund_Depot_list_Channel_" + depotCode;
            if(redisClient.exists(redisKey)){
                response = redisClient.get(redisKey);
                logger.debug(depotCode + "Redis."+redisKey+"'s datas from Redis...response:"+response);
            }else{
                ReturnInfo<ChannelShop> returnInfo = channelInfoService.findShopInfoByShopCode(depotCode);
                response = JSON.toJSONString(returnInfo);
                logger.debug(depotCode + "Redis."+redisKey+"'s datas from ChannelService ...,response:"+response);
                redisClient.set(redisKey, response);
                redisClient.expire(redisKey, 3600*5);
            }
        } catch (Exception e) {
            logger.error(">>getChannelShopListByDepotCode.Redis,depotCode:"+depotCode+",error:"+e.getMessage(),e);
        }
        if(StringUtils.isBlank(response)) return null;
        JsonResult result = JSON.parseObject(response, JsonResult.class);
        ChannelShop channelShop = JSONObject.toJavaObject((JSONObject)result.getData(), ChannelShop.class);
        return channelShop;
    }
    

    public Paging getOrderRefundPage(OrderRefundListVO model, PageHelper helper)
            throws Exception {
        Map<String,Object>paramMap=new HashMap<String, Object>();
        
        //默认退单时间  降序
        paramMap.put("orderByClause","temp.add_time desc");
        paramMap.put("limitStart",helper.getStart());
        paramMap.put("limitNum",helper.getLimit());
        
        OrderDistribute distribute = orderDistributeMapper.selectByPrimaryKey(model.getRelatingOrderSn());
        if (null != model) {
            
            //是否历史订单
            if(null!=model.getIsHistory()&&model.getIsHistory()==1){
                paramMap.put("listDataType", false);
            }else {
                paramMap.put("listDataType", true);
            }
            
            //关联退单号
            if(StringUtil.isNotNull(model.getRelatingReturnSn())){
                paramMap.put("relatingReturnSn", model.getRelatingReturnSn());
            }
            
            //关联订单号
            if(StringUtil.isNotNull(model.getRelatingOrderSn())){
                if(distribute != null){
                    paramMap.put("relatingOrderSn", distribute.getMasterOrderSn());
                }else{
                    paramMap.put("relatingOrderSn", model.getRelatingOrderSn());
                }
            }
            
            //关联外部交易号
            if(StringUtil.isNotNull(model.getOrderOutSn())){
                paramMap.put("orderOutSn", model.getOrderOutSn());
            }
            
            
            //是否需要退款
            if(null != model.getHaveRefund() ){
                paramMap.put("haveRefund", model.getHaveRefund());
            }
            
            //退单财务状态
            if(null != model.getReturnPayStatus()){
                paramMap.put("returnPayStatus", model.getReturnPayStatus());
            }
            
            //支付方式
            if(null!=model.getReturnPay()&&model.getReturnPay()>=0){
                paramMap.put("returnPay", model.getReturnPay());
            }
            
            //退余额状态：0未退，1已退
            if(null!=model.getBackbalance()){
                paramMap.put("backBalance", model.getBackbalance());
            }
            
            // 退单类型
            if(null!=model.getReturnType()&&model.getReturnType()>=0){
                if(model.getReturnType() > -1){
                    if(model.getReturnType() == 99) { // 额外退款单类型
                        paramMap.put("returnType", 4);
                    } else {
                        paramMap.put("returnType", model.getReturnType());
                    }
                }
            }
            
            // 订单所属店铺
            if (StringUtil.isNotEmpty(model.getOrderFromFirst()) && !model.getOrderFromFirst().equals("-1")) {
                List<String> orderForms = orderInfoService.getOrderForms(model.getOrderFromFirst(), model.getOrderFromSec(),
                        model.getOrderFroms());
                if (StringUtil.isListNotNull(orderForms)) {
                    paramMap.put("orderForms", orderForms);
                } else {
                    logger.error("查询订单来源列表失败！");
                    Paging paging = new Paging(0, null);
                    paging.setMessage("查询订单来源列表失败！");
                    return paging;
                }
            }
        }
        List<OrderRefundListVO> list = orderRefundSearchMapper.selectOrderRefundListByExample(paramMap);
        List<SystemPayment>paymentList=systemPaymentService.getSystemPaymentList();
        
        //支付方式名称
        for(OrderRefundListVO orderRefundListVO:list){
            for(SystemPayment systemPayment:paymentList){
                if(orderRefundListVO.getReturnPay()==systemPayment.getPayId().intValue()){
                    orderRefundListVO.setPayName(systemPayment.getPayName());
                }
            }
            
            //支付日志（默认取第一条）
            MasterOrderPayExample orderPayExample = new MasterOrderPayExample();
            String masterOrderSn = "";
            if(distribute != null){
                masterOrderSn = distribute.getMasterOrderSn();
            }else{
                masterOrderSn = orderRefundListVO.getRelatingOrderSn();
            }
             
            orderPayExample.or().andPayIdEqualTo(orderRefundListVO.getReturnPay().byteValue()).andMasterOrderSnEqualTo(masterOrderSn);
            List<MasterOrderPay> payList = masterOrderPayMapper.selectByExample(orderPayExample);
            if(CollectionUtils.isNotEmpty(payList)){
                orderRefundListVO.setPayNote(payList.get(0).getPayNote());
            }
        }
        
        int count = orderRefundSearchMapper.countOrderRefundListByExample(paramMap);
        Paging paging = new Paging(count, list);
        return paging;
    }

	@Override
	public Map delReturnImg(String userName, String returnSn, String url) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String code = "0";//0失败，1成功
		String msg = "删除图片失败！";
		try{
			if(StringUtils.isEmpty(returnSn)||StringUtils.isEmpty(url)){
				msg = "参数缺失！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
			//根据退单号找到图片列表
			OrderAttachmentExample example = new OrderAttachmentExample();
			example.or().andSnEqualTo(returnSn);
			List<OrderAttachment> imgList = orderAttachmentMapper.selectByExample(example);
			if(imgList!=null&&imgList.size()>0){
				//删除图片
				String actionNote = "删除退单图片：";
				for(OrderAttachment bean : imgList){
					String filePath = bean.getFilepath();
					if(url.endsWith(filePath)){
						orderAttachmentMapper.deleteByPrimaryKey(bean.getId());
						String formatDate = TimeUtil.formatDate(bean.getAddTime());
						actionNote += "id为"+bean.getId()+",图片地址为["+bean.getFilepath()+"],添加时间为"+formatDate;
						break;
					}
				}
				//记录日志
				orderActionService.addOrderReturnAction(returnSn, actionNote, userName);
				code = "1";
				msg = "删除图片成功！";
			}else{
				msg = "图片列表为空！";
				map.put("code", code);
				map.put("msg", msg);
				return map;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
    
}
