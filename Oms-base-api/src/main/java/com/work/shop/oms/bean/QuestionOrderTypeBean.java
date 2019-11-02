package com.work.shop.oms.bean;

import java.io.Serializable;

/**
 * 问题单类型信息
 * @author QuYachu
 */
public class QuestionOrderTypeBean implements Serializable {

    /**
     * OS问题单处理标识
     */
    private boolean normalFlg = false;

    /**
     * 物流问题单处理标识
     */
    private boolean lackFlg = false;

    /**
     * 审核问题单
     */
    private boolean reviewFlg = false;

    /**
     * 签章问题单
     */
    private boolean signFlag = false;

    public boolean isNormalFlg() {
        return normalFlg;
    }

    public void setNormalFlg(boolean normalFlg) {
        this.normalFlg = normalFlg;
    }

    public boolean isLackFlg() {
        return lackFlg;
    }

    public void setLackFlg(boolean lackFlg) {
        this.lackFlg = lackFlg;
    }

    public boolean isReviewFlg() {
        return reviewFlg;
    }

    public void setReviewFlg(boolean reviewFlg) {
        this.reviewFlg = reviewFlg;
    }

    public boolean isSignFlag() {
        return signFlag;
    }

    public void setSignFlag(boolean signFlag) {
        this.signFlag = signFlag;
    }
}
