package com.work.shop.oms.order.service.impl;

import com.work.shop.oms.api.bean.OrderPayInfo;
import com.work.shop.oms.bean.MasterOrderInfoExtend;
import com.work.shop.oms.bean.MasterOrderInfoExtendExample;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.common.bean.ApiReturnData;
import com.work.shop.oms.common.bean.MasterOrder;
import com.work.shop.oms.common.bean.TimeUtil;
import com.work.shop.oms.dao.MasterOrderInfoExtendMapper;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.order.service.MasterOrderInfoExtendService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单扩展表信息
 * @author QuYachu
 */
@Service
public class MasterOrderInfoExtendServiceImpl implements MasterOrderInfoExtendService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MasterOrderInfoExtendMapper masterOrderInfoExtendMapper;

	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;

	/**
	 * 创建订单扩展信息
	 * @param masterOrderSn 订单号
	 * @param masterOrder 订单信息
	 */
	@Override
	public void insertOrderInfoExtend(String masterOrderSn, MasterOrder masterOrder) {
		MasterOrderInfoExtend moie = new MasterOrderInfoExtend();
		moie.setMasterOrderSn(masterOrderSn);
		// 订单二次分配总数
		moie.setAgdist((byte) 0);
		// 是否为团购订单( 0:否  1;是)
		moie.setIsGroup((byte) masterOrder.getIsGroup());
		// 是否为预售商品(0:否 1:是)
		moie.setIsAdvance((byte) masterOrder.getIsAdvance());
		// 门店ID
		moie.setShopId(masterOrder.getShopId());
		// 门店名称
		moie.setShopName(masterOrder.getShopName());
		// 门店接单时间
		moie.setShopTime(masterOrder.getShopTime());
		// 订单生成时当时促销镜像编码
		moie.setRulePromotion(masterOrder.getRulePromotion());
		// 下单时会员等级
		moie.setUseLevel(masterOrder.getUseLevel());
		// 下单时的淘宝拍拍会员等级
		moie.setChannelUserLevel(masterOrder.getChannelUserLevel());
		// 是否要发票
		moie.setIsOrderPrint(masterOrder.getIsOrderPrint() == null ? 0 : masterOrder.getIsOrderPrint().intValue());
		// 发票抬头
		moie.setInvPayee(masterOrder.getInvPayee());
		// 发票内容
		moie.setInvContent(masterOrder.getInvContent());
		// 发票类型
		moie.setInvType(masterOrder.getInvType());
		// 发票税额
		moie.setTax(new BigDecimal(masterOrder.getTax()));
		// 发票状态
		moie.setInvoiceStatus(0);
		// 发票ID
		moie.setDeliveryStationId(0);
		// 纳税人识别号
		moie.setInvTaxer(masterOrder.getInvTaxer());
		// 发票开户行
        moie.setInvBank(masterOrder.getInvBank());
        // 发票银行账号
        moie.setInvBankNo(masterOrder.getInvBankNo());
        // 发票公司名称
        moie.setInvCompanyName(masterOrder.getInvCompanyName());
        // 发票公司地址
        moie.setInvCompanyAddress(masterOrder.getInvCompanyAddress());
		// 免邮券卡号
		moie.setFreePostCard(masterOrder.getFreePostCard());
		// 免邮类型 0:不免邮;1:单品免邮;2:整单免邮;3:满足规则免邮;
		moie.setFreePostType(masterOrder.getFreePostType());
		// 免邮前邮费金额
		moie.setFreePostFee(new BigDecimal(masterOrder.getFreePostFee()));
		// 是否自提
		moie.setIsCac(masterOrder.getIsCac());
		// 线下店铺编码
		moie.setShopCode(masterOrder.getShopCode());
		// 线下店铺名称
		moie.setShopName(masterOrder.getShopName());
		// 订单当日流水号
		moie.setOrderIndex(masterOrder.getOrderIndex());
		// 备注
        moie.setRemark(masterOrder.getRemark());
        // 客户合同号
        moie.setCustomerContractNum(masterOrder.getCustomerContractNum());
        // 配送时间
        moie.setRiderTime(masterOrder.getRiderTime());
        // 用户银行卡号
		moie.setUserBankNo(masterOrder.getUserBankNo());
		// 是否已支付状态
        moie.setUserPayApply(masterOrder.getUserPayApply());

        //创建订单类型，0为一般订单，1为联采订单
        Integer createOrderType = masterOrder.getCreateOrderType();
        if (createOrderType == null) {
			createOrderType = 0;
        }
		moie.setOrderType(createOrderType.shortValue());
        //账期支付最后支付时间
        if (masterOrder.getLastPayDate() != null) {
            moie.setLastPayDate(masterOrder.getLastPayDate());
        }
        //公司编码
        if (StringUtils.isNotBlank(masterOrder.getCompanyCode())) {
            moie.setCompanyCode(masterOrder.getCompanyCode());
        }
        //公司名称
        if (StringUtils.isNotBlank(masterOrder.getCompanyName())) {
            moie.setCompanyName(masterOrder.getCompanyName());
        }

        moie.setInvPhone(masterOrder.getInvPhone());

        moie.setSaleBd(masterOrder.getSaleBd());

        masterOrderInfoExtendMapper.insertSelective(moie);
	}

	/**
	 * 设置订单下单状态完成
	 * @param masterOrderSn 订单编码
	 */
	@Override
	public void masterOrderFinished(String masterOrderSn) {
		logger.debug("开始设置订单" + masterOrderSn + "订单完成状态");
		try {
			MasterOrderInfoExtend record = new MasterOrderInfoExtend();
			record.setMasterOrderSn(masterOrderSn);
			record.setOrderFinished(1);
			// 设置订单已经完成
			masterOrderInfoExtendMapper.updateByPrimaryKeySelective(record);
		} catch (Exception e) {
			logger.error("开始设置订单" + masterOrderSn + "订单完成状态异常", e);
		}
		logger.debug("设置订单" + masterOrderSn + "订单完成状态完成");
	}

    /**
     * 通过订单编码获取订单扩展信息列表
     * @param masterOrderSn 订单编码
     * @return List<MasterOrderInfoExtend>
     */
    @Override
	public List<MasterOrderInfoExtend> getMasterOrderInfoExtendByOrder(String masterOrderSn) {
        MasterOrderInfoExtendExample extendExample = new MasterOrderInfoExtendExample();
        extendExample.or().andMasterOrderSnEqualTo(masterOrderSn);
        List<MasterOrderInfoExtend> infoExtends = masterOrderInfoExtendMapper.selectByExample(extendExample);
        return infoExtends;
    }

    /**
     * 通过订单编码获取订单扩展信息
     * @param masterOrderSn
     * @return MasterOrderInfoExtend
     */
    @Override
    public MasterOrderInfoExtend getMasterOrderInfoExtendById(String masterOrderSn) {
       return masterOrderInfoExtendMapper.selectByPrimaryKey(masterOrderSn);
    }

    /**
     * 更新订单账期支付扣款状态
     * @param masterOrderSn
     * @return boolean
     */
    @Override
    public boolean updateMasterPayPeriod(String masterOrderSn) {
        boolean bl = false;
        try {
            MasterOrderInfoExtendExample extendExample = new MasterOrderInfoExtendExample();
            extendExample.or().andMasterOrderSnEqualTo(masterOrderSn);

            MasterOrderInfoExtend extend = new MasterOrderInfoExtend();
            extend.setPayPeriodStatus(1);
            int result = masterOrderInfoExtendMapper.updateByExampleSelective(extend, extendExample);

            bl = result > 0;
        } catch (Exception e) {
            logger.error("设置账期支付已扣款状态异常:" + masterOrderSn, e);
        }

        return bl;
    }

    /**
     * 填充账期支付最后支付单时间
     * @param masterOrderSn 订单号
     * @param startDate 开始时间
     */
    @Override
    public void fillPayLastDate(String masterOrderSn, Date startDate) {
        if (StringUtils.isBlank(masterOrderSn) || startDate == null) {
            return;
        }
        try {
            MasterOrderPayExample example = new MasterOrderPayExample();
            example.or().andMasterOrderSnEqualTo(masterOrderSn);
            List<MasterOrderPay> orderPayInfos = masterOrderPayMapper.selectByExample(example);
            if (StringUtil.isListNull(orderPayInfos)) {
                return;
            }

            MasterOrderPay orderPayInfo = orderPayInfos.get(0);
            ApiReturnData<Boolean> apiReturnData = checkAccountPeriodPay(orderPayInfo.getPayId());
            if (apiReturnData == null || Constant.OS_STR_NO.equals(apiReturnData.getIsOk()) || !apiReturnData.getData()) {
                return;
            }

            //期数（一期一月）
            Short paymentPeriod = orderPayInfo.getPaymentPeriod();
            Date lastPayDate = TimeUtil.getBeforeMonth(startDate, paymentPeriod.intValue());
            MasterOrderInfoExtend extend = new MasterOrderInfoExtend();
            extend.setMasterOrderSn(masterOrderSn);
            extend.setLastPayDate(lastPayDate);
            masterOrderInfoExtendMapper.updateByPrimaryKeySelective(extend);
        } catch (Exception e) {
            logger.error("填充账期支付最后支付单时间异常");
        }
    }

    /**
     * 校验是否为账期支付
     * @param payId 支付id
     * @return
     */
    @Override
    public ApiReturnData<Boolean> checkAccountPeriodPay(int payId) {
        ApiReturnData<Boolean> apiReturnData = new ApiReturnData<Boolean>();
        try {
            apiReturnData.setIsOk(Constant.OS_STR_NO);
            apiReturnData.setMessage("校验成功");
            if (Constant.PAYMENT_ZHANGQI_ID == payId) {
                apiReturnData.setData(true);
                apiReturnData.setIsOk(Constant.OS_STR_YES);
            } else {
                apiReturnData.setData(false);
            }

        } catch (Exception e) {
            logger.error("校验是否为账期支付异常", e);
            apiReturnData.setMessage("校验异常");
        }

        return apiReturnData;
    }

    /**
     * 更新订单推送供应链状态
     * @param masterOrderSn
     * @return
     */
    @Override
    public boolean updatePushSupplyChain(String masterOrderSn) {
        boolean bl = false;
        try {
            MasterOrderInfoExtend extend = new MasterOrderInfoExtend();
            extend.setMasterOrderSn(masterOrderSn);
            extend.setPushSupplyChain((byte) 1);
            int result = masterOrderInfoExtendMapper.updateByPrimaryKeySelective(extend);

            bl = result > 0;
        } catch (Exception e) {
            logger.error("设置订单推送供应链状态状态异常:" + masterOrderSn, e);
        }

        return bl;
    }
}
