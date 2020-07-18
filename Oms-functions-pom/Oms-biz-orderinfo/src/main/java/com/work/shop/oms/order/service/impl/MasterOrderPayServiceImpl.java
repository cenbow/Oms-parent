package com.work.shop.oms.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.oms.bean.MasterOrderInfoBean;
import com.work.shop.oms.bean.MasterOrderPay;
import com.work.shop.oms.bean.MasterOrderPayExample;
import com.work.shop.oms.bean.OrderPeriodDetail;
import com.work.shop.oms.bean.SystemPayment;
import com.work.shop.oms.common.bean.MasterPay;
import com.work.shop.oms.common.bean.ReturnInfo;
import com.work.shop.oms.common.bean.ServiceReturnInfo;
import com.work.shop.oms.config.service.OrderPeriodDetailService;
import com.work.shop.oms.config.service.SystemPaymentService;
import com.work.shop.oms.dao.MasterOrderPayMapper;
import com.work.shop.oms.order.service.MasterOrderActionService;
import com.work.shop.oms.order.service.MasterOrderPayService;
import com.work.shop.oms.payment.feign.PayService;
import com.work.shop.oms.redis.RedisClient;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;

/**
 * 订单支付服务
 * @author QuYachu
 */
@Service
public class MasterOrderPayServiceImpl implements MasterOrderPayService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private MasterOrderPayMapper masterOrderPayMapper;

	@Resource
	private RedisClient redisClient;

	@Resource
	private SystemPaymentService systemPaymentService;

	@Resource
	private OrderPeriodDetailService orderPeriodDetailService;

	@Resource(name="masterOrderActionServiceImpl")
	private MasterOrderActionService masterOrderActionService;

	@Resource
	private PayService payService;

    /**
     * 填充支付单信息
     * @param masterOrderSn 订单编码
     * @param masterPaySn 支付单号
     * @param masterPay 支付单信息
     * @param payment 支付方式
     * @return MasterOrderPay
     */
	private MasterOrderPay fillMasterOrderPay(String masterOrderSn, String masterPaySn, MasterPay masterPay, SystemPayment payment) {

        MasterOrderPay mop = new MasterOrderPay();
        // 主付款单编号
        mop.setMasterPaySn(masterPaySn);
        // 主订单号
        mop.setMasterOrderSn(masterOrderSn.trim());
        // 支付状态
        mop.setPayStatus((byte) (masterPay.getPayStatus().intValue()));
        // 支付方式ID
        mop.setPayId(Byte.valueOf(masterPay.getPayId() + ""));
        // 支付方式名称
        mop.setPayName(payment.getPayName());
        // 付款单总金额
        mop.setPayTotalfee(new BigDecimal(masterPay.getPayTotalFee()));
        mop.setPayNote(masterPay.getPayNote());
        // 支付时间
        mop.setPayTime(masterPay.getPayTime());
        // 生成时间
        mop.setCreateTime(new Date());
        // 已付款状态
        if (masterPay.getPayStatus() == Constant.OI_PAY_STATUS_PAYED) {
            mop.setPayTime(masterPay.getPayTime() == null ? new Date() : masterPay.getPayTime());
        }
        // 期数
        mop.setPaymentPeriod(masterPay.getPaymentPeriod().shortValue());
        // 支付费率
        mop.setPaymentRate(BigDecimal.valueOf(masterPay.getPaymentRate()));
        // 预付款金额
        mop.setPrepayments(masterPay.getPrepayments());

        // 付款最后期限设置
        int secp = 0;
        String secpValue = null;
        int pe = 1;
        try {
            secpValue = redisClient.get("OS_PAYMENT_PERIOD_" + pe);
        } catch (Throwable e) {
            logger.error("redis is null error ," + e);
        }
        if (StringUtil.isNotEmpty(secpValue)) {
            secp = new Integer(secpValue);
        } else {
            secp = getPeriodDetailValue(pe, Constant.OS_PAYMENT_PERIOD);
            try {
                redisClient.set("OS_PAYMENT_PERIOD_" + pe, secp + "");
            } catch (Throwable e) {
                logger.error("redis is null error ," + e);
            }
        }

        // 付款最后期限
        if (secp != 0) {
            mop.setPayLasttime(getDate(new Date(), secp));
        }

        return mop;
    }
	
	/**
	 * 生成付款单
	 * @param masterOrderSn 订单号
	 * @param masterPays List<MasterPay> 付款单列表
	 * @return ServiceReturnInfo<List<String>>
	 */
	@Override
	public ServiceReturnInfo<List<String>> insertMasterOrderPay(String masterOrderSn, List<MasterPay> masterPays) {
		String logMessage = "创建订单" + masterOrderSn + "的付款单";
		logger.info(logMessage);
		if (!StringUtil.isListNotNull(masterPays)) {
			return new ServiceReturnInfo<List<String>>(false, "生成付款单失败,传入的PayList为" + masterPays, null);
		}
		
		if (StringUtil.isTrimEmpty(masterOrderSn)) {
			return new ServiceReturnInfo<List<String>>(false, "生成付款单失败,传入的orderSn为" + masterOrderSn, null);
		}
		List<String> orderPays = new ArrayList<String>();
		try {
			int i = 1;
			for (MasterPay masterPay : masterPays) {
				SystemPayment payment = systemPaymentService.selectSystemPayByCode(masterPay.getPayCode());
				if (null == payment) {
					return new ServiceReturnInfo<List<String>>("获取支付信息" + masterPay.getPayCode() + "失败");
				}

                // paySn支付单号
                String masterPaySn = Constant.OP_BEGIN_WITH_FK + masterOrderSn.trim() + genCode(i++, 2);
                logger.debug(logMessage + masterPaySn);
                MasterOrderPay mop = fillMasterOrderPay(masterOrderSn, masterPaySn, masterPay, payment);

				try {
					masterOrderPayMapper.insertSelective(mop);
				} catch (Exception e) {
					logger.error(masterOrderSn + "生成付款单失败-" + e.getMessage(), e);
					return new ServiceReturnInfo<List<String>>("创建付款单信息" + masterOrderSn + "失败" + e.getMessage());
				}
				// 未付款单
				if (masterPay.getPayStatus() == Constant.OI_PAY_STATUS_UNPAYED) {
					orderPays.add(masterPaySn);
				}
				// 记录日志
				masterOrderActionService.insertOrderAction(masterOrderSn, "生成付款单：" + masterPaySn, mop.getPayStatus());
			}
		} catch (Exception e) {
			logger.error(masterOrderSn + "生成付款单失败-" + e.getMessage(), e);
			return new ServiceReturnInfo<List<String>>(false, "生成付款单失败,传入的orderSn为" + masterOrderSn, null);
		}
		return new ServiceReturnInfo<List<String>>(true, "生成付款单成功", orderPays);
	}

	/**
	 * 编辑支付单
	 * @param masterOrderSn
	 * @param oldTotalPayable
	 * @param newMaster
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ReturnInfo editMasterOrderPay(String masterOrderSn, double oldTotalPayable, MasterOrderInfoBean newMaster) {
		ReturnInfo<String> info = new ReturnInfo<String>(Constant.OS_NO);
		// 编辑支付单
		// 更新后订单信息
		Byte payStatus = newMaster.getPayStatus();
		// 未付款
		MasterOrderPayExample orderPayExample = new MasterOrderPayExample();
		orderPayExample.or().andPayStatusEqualTo((byte) Constant.OP_PAY_STATUS_UNPAYED).andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderPay> unOrderPays = masterOrderPayMapper.selectByExample(orderPayExample);
		// 待确认
		orderPayExample = new MasterOrderPayExample();
		orderPayExample.or().andPayStatusEqualTo((byte) Constant.OP_PAY_STATUS_COMFIRM).andMasterOrderSnEqualTo(masterOrderSn);
		List<MasterOrderPay> waitConfirmPay = masterOrderPayMapper.selectByExample(orderPayExample);
		// 已付款
		orderPayExample = new MasterOrderPayExample();
		List<Byte> payStatusList = new ArrayList<Byte>();
		payStatusList.add((byte)2);
		payStatusList.add((byte)3);
		orderPayExample.or().andMasterOrderSnEqualTo(masterOrderSn).andPayStatusIn(payStatusList);
		List<MasterOrderPay> orderPays = masterOrderPayMapper.selectByExample(orderPayExample);
		StringBuffer msg = new StringBuffer();
		MasterOrderPay unOrderPay = null;
		// 未付款单、余额
		if (StringUtil.isListNotNull(unOrderPays)) {
			if (unOrderPays.size() > 1) {
				info.setMessage("订单[" + masterOrderSn + "]含有多个未支付单");
				return info;
			}
			unOrderPay = unOrderPays.get(0);
		}
		// 修改商品信息后应付款金额和修改前应付款金额比较  -0.005 0.005 之间不处理
		if (Math.abs(newMaster.getAfterChangePayable().doubleValue() - oldTotalPayable) < 0.005) {
			logger.info("订单" + masterOrderSn + "应付款费用变动前 " + oldTotalPayable
					+ "变更后 " + newMaster.getAfterChangePayable() + " 财务价格变动为 -0.005 至 0.005 之间");
			info.setIsOk(Constant.OS_YES);
			info.setMessage("支付单未变更");
			return info;
		}
		// 应付款进位小于一分钱时，支付状态为已支付状态
		if (newMaster.getAfterChangePayable().doubleValue() <= 0 || Math.abs(newMaster.getAfterChangePayable().doubleValue()) < 0.005) {
			if (unOrderPay != null) {
				msg.append("更新支付单:" + unOrderPay.getMasterPaySn() + "\n");
				payStatus = Constant.OI_PAY_STATUS_PAYED;
				msg.append(" 付款单总金额：" + unOrderPay.getPayTotalfee() + "→ 0.0");
				unOrderPay.setPayTotalfee(new BigDecimal(0.0));
				unOrderPay.setPayStatus((byte) Constant.OP_PAY_STATUS_PAYED);
				unOrderPay.setPayTime(new Date());
				unOrderPay.setUpdateTime(new Date());
				masterOrderPayMapper.updateByPrimaryKey(unOrderPay);
			} else {
				payStatus = Constant.OI_PAY_STATUS_PAYED;
			}
			// 待确认支付单不为空
			if (StringUtil.isListNotNull(waitConfirmPay)) {
				payStatus = Constant.OI_PAY_STATUS_PARTPAYED;
			}
		} else if (newMaster.getAfterChangePayable().doubleValue() > 0) {
			// 已付款新增 未付款支付单
			// 未付款删除原未付款支付单，新增 未付款支付单
			if (StringUtil.isListNotNull(orderPays)) {
				payStatus = Constant.OI_PAY_STATUS_PARTPAYED;
			} else {
				payStatus = Constant.OI_PAY_STATUS_UNPAYED;
			}
			// 新增未付款支付单
			List<MasterPay> masterPays = new ArrayList<MasterPay>();
			MasterPay masterPay = new MasterPay();
			masterPay.setPayTotalFee(newMaster.getAfterChangePayable().doubleValue());
			masterPays.add(masterPay);
			try {
				ReturnInfo<List<String>> tInfo = payService.createMasterPay(masterOrderSn, masterPays);
				if(tInfo != null && tInfo.getIsOk() == Constant.OS_YES) {
					List<String> paySns = tInfo.getData();
					if (StringUtil.isListNotNull(paySns)) {
						msg.append("新增支付单：" + paySns.get(0) + "</br>");
						msg.append("付款单总金额：" + newMaster.getAfterChangePayable() + "</br>");
					}
				} else if (tInfo == null) {
					tInfo = new ReturnInfo<List<String>>();
					tInfo.setIsOk(Constant.OS_NO);
					tInfo.setMessage("调用支付单创建接口失败：返回结果为空！");
					return tInfo;
				} else {
					return tInfo;
				}
			} catch (Exception e) {
				logger.error("订单[" + masterOrderSn + "]新增支付单异常" + e.getMessage(), e);
				info.setMessage("订单[" + masterOrderSn + "]新增支付单异常" + e.getMessage());
				return info;
			}
		}
		newMaster.setPayStatus(payStatus);
		info.setIsOk(Constant.OS_YES);
		info.setMessage("编辑支付单成功");
		info.setData(msg.toString());
		return info;
	}

    /**
     * 根据订单号查询主支付单
     * @param masterOrderSn
     * @return List<MasterOrderPay>
     */
    @Override
    public List<MasterOrderPay> getMasterOrderPayList(String masterOrderSn) {
        MasterOrderPayExample example = new MasterOrderPayExample();
        example.or().andMasterOrderSnEqualTo(masterOrderSn);
        return masterOrderPayMapper.selectByExample(example);
    }

	private int getPeriodDetailValue(int period, String type) {
		OrderPeriodDetail detail = orderPeriodDetailService.selectByPeriodAndType(period, type);
		if (detail != null) {
			return detail.getPeriodValue();
		}
		return 0;
	}

    /**
     * 根据主键更新
     * @param masterOrderPay
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(MasterOrderPay masterOrderPay) {
        return masterOrderPayMapper.updateByPrimaryKeySelective(masterOrderPay);
    }

	/**
	 * 根据订单号 更新主支付单最后支付时间
	 * @param masterOrderSn
	 * @return
	 */
	@Override
	public int updateBymasterOrderSnLastTime(String masterOrderSn) {
		int num = 0;
		if(StringUtils.isNotBlank(masterOrderSn)){
			List<MasterOrderPay> masterOrderPayList = getMasterOrderPayList(masterOrderSn);
			if (masterOrderPayList !=null && masterOrderPayList.size() > 0){
				// 付款最后期限设置
				int secp = 0;
				String secpValue = null;
				int pe = 1;
				try {
					secpValue = redisClient.get("OS_PAYMENT_PERIOD_" + pe);
				} catch (Throwable e) {
					logger.error("redis is null error ," + e);
				}
				if (StringUtil.isNotEmpty(secpValue)) {
					secp = new Integer(secpValue);
				} else {
					secp = getPeriodDetailValue(pe, Constant.OS_PAYMENT_PERIOD);
					try {
						redisClient.set("OS_PAYMENT_PERIOD_" + pe, secp + "");
					} catch (Throwable e) {
						logger.error("redis is null error ," + e);
					}
				}

				// 付款最后期限
				if (secp != 0) {
					for (MasterOrderPay orderPay : masterOrderPayList){
						MasterOrderPay  param = new MasterOrderPay();
						param.setMasterPaySn(orderPay.getMasterPaySn());
						param.setPayLasttime(getDate(new Date(), secp));
						num += masterOrderPayMapper.updateByPrimaryKeySelective(param);
					}
				}

			}
		}
		return num;
	}

	public Date getDate(Date t, int sec) {
		long milliseconds = t.getTime();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(milliseconds + sec * 1000);
		Date f = c.getTime();
		return f;
	}
	
	/**
	 * 获取指定位数字符
	 * @param i 数据
	 * @param size 位数
	 * @return String
	 */
	private String genCode(Integer i, Integer size) {
		String num = i.toString();
		StringBuilder code = new StringBuilder();
		for (int j = 0; j < size - num.length(); j++) {
			code.append("0");
		}
		return code + num;
	}
}
