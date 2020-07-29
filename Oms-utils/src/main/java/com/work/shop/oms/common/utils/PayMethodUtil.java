package com.work.shop.oms.common.utils;

import com.work.shop.oms.bean.PayPeriod;
import com.work.shop.oms.utils.MathOperation;

import java.math.BigDecimal;

/**
 * 支付方式
 * @author QuYachu
 */
public class PayMethodUtil {

    /**
     * 账期支付, 加入支付费率
     * @param price
     * @param payRate
     * @return
     */
    public static Double getPayRateMoney(Double price, BigDecimal payRate) {

        BigDecimal priceBig = new BigDecimal(price.toString());

        BigDecimal payRateBig = payRate.add(BigDecimal.valueOf(100));

        BigDecimal resultPrice = MathOperation.mul(priceBig, payRateBig, 4);
        double result = MathOperation.div(resultPrice.doubleValue(), 100, 2);
        return result;
    }

    /**
     * 账期支付, 加入支付费率
     * @param priceBig
     * @param payRate
     * @return
     */
    public static Double getPayRateMoney(BigDecimal priceBig, BigDecimal payRate) {
        BigDecimal payRateBig = payRate.add(BigDecimal.valueOf(100));

        BigDecimal resultPrice = MathOperation.mul(priceBig, payRateBig, 4);
        double result = MathOperation.div(resultPrice.doubleValue(), 100, 2);
        return result;
    }

    /**
     * 获取银行保函加价金额
     * @param price
     * @param payPeriod
     * @return
     */
    public static Double getBankPayPeriodMoney(Double price, PayPeriod payPeriod) {
        // 资金利率
        String periodRate = payPeriod.getPeriodRate();
        BigDecimal periodRateBig = new BigDecimal(periodRate);

        // 资金利率
        double d3 = MathOperation.div(periodRateBig.doubleValue(), 4, 4);

        //  保费率
        double d2 = MathOperation.add(1, d3);

        BigDecimal priceBig = BigDecimal.valueOf(price);

        BigDecimal result = MathOperation.mul(priceBig, BigDecimal.valueOf(d2), 2);

        return result.doubleValue();
    }

    /**
     * 信用额度支付, 费用计算
     * @param price 商品价格
     * @param payPeriod 信用额度支付信息
     * @return Double
     */
    public static Double getPayPeriodMoney(Double price, PayPeriod payPeriod) {
        // 最高赔付率
        String maxPayforRate = payPeriod.getMaxPayforRate();
        // 资金利率
        String periodRate = payPeriod.getPeriodRate();
        // 保费率
        String premiumRate = payPeriod.getPremiumRate();

        // (销售基础价格*（2-最高赔付率)) * (1+保费率) + 销售基础价格*资金利率/4
        BigDecimal maxPayforRateBig = new BigDecimal(maxPayforRate);
        BigDecimal periodRateBig = new BigDecimal(periodRate);
        BigDecimal premiumRateBig = new BigDecimal(premiumRate);

        // 最高赔付率
        double d1 = MathOperation.sub(2, maxPayforRateBig.doubleValue());
        //  保费率
        double d2 = MathOperation.add(1, premiumRateBig.doubleValue());
        // 资金利率
        double d3 = MathOperation.div(periodRateBig.doubleValue(), 4, 4);

        BigDecimal priceBig = BigDecimal.valueOf(price);

        BigDecimal result = MathOperation.mul(priceBig, BigDecimal.valueOf(d1), 2);
        result = MathOperation.mul(result, BigDecimal.valueOf(d2), 2);

        BigDecimal rate = MathOperation.mul(priceBig, BigDecimal.valueOf(d3), 2);

        double money = MathOperation.add(result.doubleValue(), rate.doubleValue());
        return money;
    }

    public static void main(String[] args) {
        PayPeriod payPeriod = new PayPeriod();
        payPeriod.setPeriodRate("0.08");

        Double money = PayMethodUtil.getBankPayPeriodMoney(105d,payPeriod);
        System.out.println(money);
    }
}
