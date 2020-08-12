package com.work.shop.oms.bean;

import java.io.Serializable;

/**
 * 账期支付
 * @author YeQingchao
 */
public class PayPeriod implements Serializable {

    private static final long serialVersionUID = -4549489968263872291L;

    /**
     * 账期code
     */
    private String periodCode;

    /**
     * 账期名称
     */
    private String periodName;

    /**
     * 支付方式编码
     */
    private transient String payCode;

    /**
     * 费率
     */
    private String periodRate;

    /**
     * 最高赔付率
     */
    private String maxPayforRate;

    /**
     * 保费率
     */
    private String premiumRate;

    public String getPeriodCode() {
        return periodCode;
    }

    public void setPeriodCode(String periodCode) {
        this.periodCode = periodCode;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPeriodRate() {
        return periodRate;
    }

    public void setPeriodRate(String periodRate) {
        this.periodRate = periodRate;
    }

    public String getMaxPayforRate() {
        return maxPayforRate;
    }

    public void setMaxPayforRate(String maxPayforRate) {
        this.maxPayforRate = maxPayforRate;
    }

    public String getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(String premiumRate) {
        this.premiumRate = premiumRate;
    }
}
