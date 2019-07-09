package com.work.shop.oms.bean.bgapidb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YikeTradeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public YikeTradeExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        /**  
         * limit 
         * 
         */ 
        public Criteria limit(int offset, int len) {
            if (len==0 )
              throw new RuntimeException("len is 0");
            addCriterion("limit",offset,len,"null");
            return (Criteria) this;
        }

        /**  
         * top 
         * 
         */ 
        public Criteria limit(int len) {
            if (len==0 )
              throw new RuntimeException("len is 0");
            addCriterion("limit", 0, len, "null");
            return (Criteria) this;
        }

        public Criteria getLimitValue() {
            return (Criteria)criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(String value) {
            addCriterion("code =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("code <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(String value) {
            addCriterion("code >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("code >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(String value) {
            addCriterion("code <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("code <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLike(String value) {
            addCriterion("code like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotLike(String value) {
            addCriterion("code not like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("code not between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andShopcodeIsNull() {
            addCriterion("shopcode is null");
            return (Criteria) this;
        }

        public Criteria andShopcodeIsNotNull() {
            addCriterion("shopcode is not null");
            return (Criteria) this;
        }

        public Criteria andShopcodeEqualTo(String value) {
            addCriterion("shopcode =", value, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeNotEqualTo(String value) {
            addCriterion("shopcode <>", value, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeGreaterThan(String value) {
            addCriterion("shopcode >", value, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeGreaterThanOrEqualTo(String value) {
            addCriterion("shopcode >=", value, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeLessThan(String value) {
            addCriterion("shopcode <", value, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeLessThanOrEqualTo(String value) {
            addCriterion("shopcode <=", value, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeLike(String value) {
            addCriterion("shopcode like", value, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeNotLike(String value) {
            addCriterion("shopcode not like", value, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeIn(List<String> values) {
            addCriterion("shopcode in", values, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeNotIn(List<String> values) {
            addCriterion("shopcode not in", values, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeBetween(String value1, String value2) {
            addCriterion("shopcode between", value1, value2, "shopcode");
            return (Criteria) this;
        }

        public Criteria andShopcodeNotBetween(String value1, String value2) {
            addCriterion("shopcode not between", value1, value2, "shopcode");
            return (Criteria) this;
        }

        public Criteria andTotalQtyIsNull() {
            addCriterion("total_qty is null");
            return (Criteria) this;
        }

        public Criteria andTotalQtyIsNotNull() {
            addCriterion("total_qty is not null");
            return (Criteria) this;
        }

        public Criteria andTotalQtyEqualTo(Integer value) {
            addCriterion("total_qty =", value, "totalQty");
            return (Criteria) this;
        }

        public Criteria andTotalQtyNotEqualTo(Integer value) {
            addCriterion("total_qty <>", value, "totalQty");
            return (Criteria) this;
        }

        public Criteria andTotalQtyGreaterThan(Integer value) {
            addCriterion("total_qty >", value, "totalQty");
            return (Criteria) this;
        }

        public Criteria andTotalQtyGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_qty >=", value, "totalQty");
            return (Criteria) this;
        }

        public Criteria andTotalQtyLessThan(Integer value) {
            addCriterion("total_qty <", value, "totalQty");
            return (Criteria) this;
        }

        public Criteria andTotalQtyLessThanOrEqualTo(Integer value) {
            addCriterion("total_qty <=", value, "totalQty");
            return (Criteria) this;
        }

        public Criteria andTotalQtyIn(List<Integer> values) {
            addCriterion("total_qty in", values, "totalQty");
            return (Criteria) this;
        }

        public Criteria andTotalQtyNotIn(List<Integer> values) {
            addCriterion("total_qty not in", values, "totalQty");
            return (Criteria) this;
        }

        public Criteria andTotalQtyBetween(Integer value1, Integer value2) {
            addCriterion("total_qty between", value1, value2, "totalQty");
            return (Criteria) this;
        }

        public Criteria andTotalQtyNotBetween(Integer value1, Integer value2) {
            addCriterion("total_qty not between", value1, value2, "totalQty");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyIsNull() {
            addCriterion("total_money is null");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyIsNotNull() {
            addCriterion("total_money is not null");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyEqualTo(BigDecimal value) {
            addCriterion("total_money =", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyNotEqualTo(BigDecimal value) {
            addCriterion("total_money <>", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyGreaterThan(BigDecimal value) {
            addCriterion("total_money >", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_money >=", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyLessThan(BigDecimal value) {
            addCriterion("total_money <", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_money <=", value, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyIn(List<BigDecimal> values) {
            addCriterion("total_money in", values, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyNotIn(List<BigDecimal> values) {
            addCriterion("total_money not in", values, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_money between", value1, value2, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andTotalMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_money not between", value1, value2, "totalMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyIsNull() {
            addCriterion("discount_money is null");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyIsNotNull() {
            addCriterion("discount_money is not null");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyEqualTo(BigDecimal value) {
            addCriterion("discount_money =", value, "discountMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyNotEqualTo(BigDecimal value) {
            addCriterion("discount_money <>", value, "discountMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyGreaterThan(BigDecimal value) {
            addCriterion("discount_money >", value, "discountMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("discount_money >=", value, "discountMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyLessThan(BigDecimal value) {
            addCriterion("discount_money <", value, "discountMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("discount_money <=", value, "discountMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyIn(List<BigDecimal> values) {
            addCriterion("discount_money in", values, "discountMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyNotIn(List<BigDecimal> values) {
            addCriterion("discount_money not in", values, "discountMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("discount_money between", value1, value2, "discountMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("discount_money not between", value1, value2, "discountMoney");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIsNull() {
            addCriterion("discount_amount is null");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIsNotNull() {
            addCriterion("discount_amount is not null");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountEqualTo(BigDecimal value) {
            addCriterion("discount_amount =", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotEqualTo(BigDecimal value) {
            addCriterion("discount_amount <>", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountGreaterThan(BigDecimal value) {
            addCriterion("discount_amount >", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("discount_amount >=", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountLessThan(BigDecimal value) {
            addCriterion("discount_amount <", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("discount_amount <=", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIn(List<BigDecimal> values) {
            addCriterion("discount_amount in", values, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotIn(List<BigDecimal> values) {
            addCriterion("discount_amount not in", values, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("discount_amount between", value1, value2, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("discount_amount not between", value1, value2, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityIsNull() {
            addCriterion("return_quantity is null");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityIsNotNull() {
            addCriterion("return_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityEqualTo(Integer value) {
            addCriterion("return_quantity =", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityNotEqualTo(Integer value) {
            addCriterion("return_quantity <>", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityGreaterThan(Integer value) {
            addCriterion("return_quantity >", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("return_quantity >=", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityLessThan(Integer value) {
            addCriterion("return_quantity <", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("return_quantity <=", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityIn(List<Integer> values) {
            addCriterion("return_quantity in", values, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityNotIn(List<Integer> values) {
            addCriterion("return_quantity not in", values, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityBetween(Integer value1, Integer value2) {
            addCriterion("return_quantity between", value1, value2, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("return_quantity not between", value1, value2, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeIsNull() {
            addCriterion("order_pre_shiptime is null");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeIsNotNull() {
            addCriterion("order_pre_shiptime is not null");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeEqualTo(String value) {
            addCriterion("order_pre_shiptime =", value, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeNotEqualTo(String value) {
            addCriterion("order_pre_shiptime <>", value, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeGreaterThan(String value) {
            addCriterion("order_pre_shiptime >", value, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeGreaterThanOrEqualTo(String value) {
            addCriterion("order_pre_shiptime >=", value, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeLessThan(String value) {
            addCriterion("order_pre_shiptime <", value, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeLessThanOrEqualTo(String value) {
            addCriterion("order_pre_shiptime <=", value, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeLike(String value) {
            addCriterion("order_pre_shiptime like", value, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeNotLike(String value) {
            addCriterion("order_pre_shiptime not like", value, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeIn(List<String> values) {
            addCriterion("order_pre_shiptime in", values, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeNotIn(List<String> values) {
            addCriterion("order_pre_shiptime not in", values, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeBetween(String value1, String value2) {
            addCriterion("order_pre_shiptime between", value1, value2, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShiptimeNotBetween(String value1, String value2) {
            addCriterion("order_pre_shiptime not between", value1, value2, "orderPreShiptime");
            return (Criteria) this;
        }

        public Criteria andPayAmountIsNull() {
            addCriterion("pay_amount is null");
            return (Criteria) this;
        }

        public Criteria andPayAmountIsNotNull() {
            addCriterion("pay_amount is not null");
            return (Criteria) this;
        }

        public Criteria andPayAmountEqualTo(BigDecimal value) {
            addCriterion("pay_amount =", value, "payAmount");
            return (Criteria) this;
        }

        public Criteria andPayAmountNotEqualTo(BigDecimal value) {
            addCriterion("pay_amount <>", value, "payAmount");
            return (Criteria) this;
        }

        public Criteria andPayAmountGreaterThan(BigDecimal value) {
            addCriterion("pay_amount >", value, "payAmount");
            return (Criteria) this;
        }

        public Criteria andPayAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("pay_amount >=", value, "payAmount");
            return (Criteria) this;
        }

        public Criteria andPayAmountLessThan(BigDecimal value) {
            addCriterion("pay_amount <", value, "payAmount");
            return (Criteria) this;
        }

        public Criteria andPayAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("pay_amount <=", value, "payAmount");
            return (Criteria) this;
        }

        public Criteria andPayAmountIn(List<BigDecimal> values) {
            addCriterion("pay_amount in", values, "payAmount");
            return (Criteria) this;
        }

        public Criteria andPayAmountNotIn(List<BigDecimal> values) {
            addCriterion("pay_amount not in", values, "payAmount");
            return (Criteria) this;
        }

        public Criteria andPayAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pay_amount between", value1, value2, "payAmount");
            return (Criteria) this;
        }

        public Criteria andPayAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("pay_amount not between", value1, value2, "payAmount");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueIsNull() {
            addCriterion("usecoupon_value is null");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueIsNotNull() {
            addCriterion("usecoupon_value is not null");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueEqualTo(BigDecimal value) {
            addCriterion("usecoupon_value =", value, "usecouponValue");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueNotEqualTo(BigDecimal value) {
            addCriterion("usecoupon_value <>", value, "usecouponValue");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueGreaterThan(BigDecimal value) {
            addCriterion("usecoupon_value >", value, "usecouponValue");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("usecoupon_value >=", value, "usecouponValue");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueLessThan(BigDecimal value) {
            addCriterion("usecoupon_value <", value, "usecouponValue");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueLessThanOrEqualTo(BigDecimal value) {
            addCriterion("usecoupon_value <=", value, "usecouponValue");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueIn(List<BigDecimal> values) {
            addCriterion("usecoupon_value in", values, "usecouponValue");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueNotIn(List<BigDecimal> values) {
            addCriterion("usecoupon_value not in", values, "usecouponValue");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("usecoupon_value between", value1, value2, "usecouponValue");
            return (Criteria) this;
        }

        public Criteria andUsecouponValueNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("usecoupon_value not between", value1, value2, "usecouponValue");
            return (Criteria) this;
        }

        public Criteria andRewardFeeIsNull() {
            addCriterion("reward_fee is null");
            return (Criteria) this;
        }

        public Criteria andRewardFeeIsNotNull() {
            addCriterion("reward_fee is not null");
            return (Criteria) this;
        }

        public Criteria andRewardFeeEqualTo(BigDecimal value) {
            addCriterion("reward_fee =", value, "rewardFee");
            return (Criteria) this;
        }

        public Criteria andRewardFeeNotEqualTo(BigDecimal value) {
            addCriterion("reward_fee <>", value, "rewardFee");
            return (Criteria) this;
        }

        public Criteria andRewardFeeGreaterThan(BigDecimal value) {
            addCriterion("reward_fee >", value, "rewardFee");
            return (Criteria) this;
        }

        public Criteria andRewardFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("reward_fee >=", value, "rewardFee");
            return (Criteria) this;
        }

        public Criteria andRewardFeeLessThan(BigDecimal value) {
            addCriterion("reward_fee <", value, "rewardFee");
            return (Criteria) this;
        }

        public Criteria andRewardFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("reward_fee <=", value, "rewardFee");
            return (Criteria) this;
        }

        public Criteria andRewardFeeIn(List<BigDecimal> values) {
            addCriterion("reward_fee in", values, "rewardFee");
            return (Criteria) this;
        }

        public Criteria andRewardFeeNotIn(List<BigDecimal> values) {
            addCriterion("reward_fee not in", values, "rewardFee");
            return (Criteria) this;
        }

        public Criteria andRewardFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reward_fee between", value1, value2, "rewardFee");
            return (Criteria) this;
        }

        public Criteria andRewardFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("reward_fee not between", value1, value2, "rewardFee");
            return (Criteria) this;
        }

        public Criteria andUseBonusIsNull() {
            addCriterion("use_bonus is null");
            return (Criteria) this;
        }

        public Criteria andUseBonusIsNotNull() {
            addCriterion("use_bonus is not null");
            return (Criteria) this;
        }

        public Criteria andUseBonusEqualTo(Integer value) {
            addCriterion("use_bonus =", value, "useBonus");
            return (Criteria) this;
        }

        public Criteria andUseBonusNotEqualTo(Integer value) {
            addCriterion("use_bonus <>", value, "useBonus");
            return (Criteria) this;
        }

        public Criteria andUseBonusGreaterThan(Integer value) {
            addCriterion("use_bonus >", value, "useBonus");
            return (Criteria) this;
        }

        public Criteria andUseBonusGreaterThanOrEqualTo(Integer value) {
            addCriterion("use_bonus >=", value, "useBonus");
            return (Criteria) this;
        }

        public Criteria andUseBonusLessThan(Integer value) {
            addCriterion("use_bonus <", value, "useBonus");
            return (Criteria) this;
        }

        public Criteria andUseBonusLessThanOrEqualTo(Integer value) {
            addCriterion("use_bonus <=", value, "useBonus");
            return (Criteria) this;
        }

        public Criteria andUseBonusIn(List<Integer> values) {
            addCriterion("use_bonus in", values, "useBonus");
            return (Criteria) this;
        }

        public Criteria andUseBonusNotIn(List<Integer> values) {
            addCriterion("use_bonus not in", values, "useBonus");
            return (Criteria) this;
        }

        public Criteria andUseBonusBetween(Integer value1, Integer value2) {
            addCriterion("use_bonus between", value1, value2, "useBonus");
            return (Criteria) this;
        }

        public Criteria andUseBonusNotBetween(Integer value1, Integer value2) {
            addCriterion("use_bonus not between", value1, value2, "useBonus");
            return (Criteria) this;
        }

        public Criteria andIsCodIsNull() {
            addCriterion("is_cod is null");
            return (Criteria) this;
        }

        public Criteria andIsCodIsNotNull() {
            addCriterion("is_cod is not null");
            return (Criteria) this;
        }

        public Criteria andIsCodEqualTo(String value) {
            addCriterion("is_cod =", value, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodNotEqualTo(String value) {
            addCriterion("is_cod <>", value, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodGreaterThan(String value) {
            addCriterion("is_cod >", value, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodGreaterThanOrEqualTo(String value) {
            addCriterion("is_cod >=", value, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodLessThan(String value) {
            addCriterion("is_cod <", value, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodLessThanOrEqualTo(String value) {
            addCriterion("is_cod <=", value, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodLike(String value) {
            addCriterion("is_cod like", value, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodNotLike(String value) {
            addCriterion("is_cod not like", value, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodIn(List<String> values) {
            addCriterion("is_cod in", values, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodNotIn(List<String> values) {
            addCriterion("is_cod not in", values, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodBetween(String value1, String value2) {
            addCriterion("is_cod between", value1, value2, "isCod");
            return (Criteria) this;
        }

        public Criteria andIsCodNotBetween(String value1, String value2) {
            addCriterion("is_cod not between", value1, value2, "isCod");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeIsNull() {
            addCriterion("cod_service_fee is null");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeIsNotNull() {
            addCriterion("cod_service_fee is not null");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeEqualTo(BigDecimal value) {
            addCriterion("cod_service_fee =", value, "codServiceFee");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeNotEqualTo(BigDecimal value) {
            addCriterion("cod_service_fee <>", value, "codServiceFee");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeGreaterThan(BigDecimal value) {
            addCriterion("cod_service_fee >", value, "codServiceFee");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cod_service_fee >=", value, "codServiceFee");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeLessThan(BigDecimal value) {
            addCriterion("cod_service_fee <", value, "codServiceFee");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cod_service_fee <=", value, "codServiceFee");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeIn(List<BigDecimal> values) {
            addCriterion("cod_service_fee in", values, "codServiceFee");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeNotIn(List<BigDecimal> values) {
            addCriterion("cod_service_fee not in", values, "codServiceFee");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cod_service_fee between", value1, value2, "codServiceFee");
            return (Criteria) this;
        }

        public Criteria andCodServiceFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cod_service_fee not between", value1, value2, "codServiceFee");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeIsNull() {
            addCriterion("buyer_code is null");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeIsNotNull() {
            addCriterion("buyer_code is not null");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeEqualTo(String value) {
            addCriterion("buyer_code =", value, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeNotEqualTo(String value) {
            addCriterion("buyer_code <>", value, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeGreaterThan(String value) {
            addCriterion("buyer_code >", value, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeGreaterThanOrEqualTo(String value) {
            addCriterion("buyer_code >=", value, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeLessThan(String value) {
            addCriterion("buyer_code <", value, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeLessThanOrEqualTo(String value) {
            addCriterion("buyer_code <=", value, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeLike(String value) {
            addCriterion("buyer_code like", value, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeNotLike(String value) {
            addCriterion("buyer_code not like", value, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeIn(List<String> values) {
            addCriterion("buyer_code in", values, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeNotIn(List<String> values) {
            addCriterion("buyer_code not in", values, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeBetween(String value1, String value2) {
            addCriterion("buyer_code between", value1, value2, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andBuyerCodeNotBetween(String value1, String value2) {
            addCriterion("buyer_code not between", value1, value2, "buyerCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeIsNull() {
            addCriterion("store_code is null");
            return (Criteria) this;
        }

        public Criteria andStoreCodeIsNotNull() {
            addCriterion("store_code is not null");
            return (Criteria) this;
        }

        public Criteria andStoreCodeEqualTo(String value) {
            addCriterion("store_code =", value, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeNotEqualTo(String value) {
            addCriterion("store_code <>", value, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeGreaterThan(String value) {
            addCriterion("store_code >", value, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeGreaterThanOrEqualTo(String value) {
            addCriterion("store_code >=", value, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeLessThan(String value) {
            addCriterion("store_code <", value, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeLessThanOrEqualTo(String value) {
            addCriterion("store_code <=", value, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeLike(String value) {
            addCriterion("store_code like", value, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeNotLike(String value) {
            addCriterion("store_code not like", value, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeIn(List<String> values) {
            addCriterion("store_code in", values, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeNotIn(List<String> values) {
            addCriterion("store_code not in", values, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeBetween(String value1, String value2) {
            addCriterion("store_code between", value1, value2, "storeCode");
            return (Criteria) this;
        }

        public Criteria andStoreCodeNotBetween(String value1, String value2) {
            addCriterion("store_code not between", value1, value2, "storeCode");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailIsNull() {
            addCriterion("buyer_email is null");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailIsNotNull() {
            addCriterion("buyer_email is not null");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailEqualTo(String value) {
            addCriterion("buyer_email =", value, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailNotEqualTo(String value) {
            addCriterion("buyer_email <>", value, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailGreaterThan(String value) {
            addCriterion("buyer_email >", value, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailGreaterThanOrEqualTo(String value) {
            addCriterion("buyer_email >=", value, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailLessThan(String value) {
            addCriterion("buyer_email <", value, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailLessThanOrEqualTo(String value) {
            addCriterion("buyer_email <=", value, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailLike(String value) {
            addCriterion("buyer_email like", value, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailNotLike(String value) {
            addCriterion("buyer_email not like", value, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailIn(List<String> values) {
            addCriterion("buyer_email in", values, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailNotIn(List<String> values) {
            addCriterion("buyer_email not in", values, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailBetween(String value1, String value2) {
            addCriterion("buyer_email between", value1, value2, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andBuyerEmailNotBetween(String value1, String value2) {
            addCriterion("buyer_email not between", value1, value2, "buyerEmail");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeIsNull() {
            addCriterion("recv_consignee is null");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeIsNotNull() {
            addCriterion("recv_consignee is not null");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeEqualTo(String value) {
            addCriterion("recv_consignee =", value, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeNotEqualTo(String value) {
            addCriterion("recv_consignee <>", value, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeGreaterThan(String value) {
            addCriterion("recv_consignee >", value, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeGreaterThanOrEqualTo(String value) {
            addCriterion("recv_consignee >=", value, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeLessThan(String value) {
            addCriterion("recv_consignee <", value, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeLessThanOrEqualTo(String value) {
            addCriterion("recv_consignee <=", value, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeLike(String value) {
            addCriterion("recv_consignee like", value, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeNotLike(String value) {
            addCriterion("recv_consignee not like", value, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeIn(List<String> values) {
            addCriterion("recv_consignee in", values, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeNotIn(List<String> values) {
            addCriterion("recv_consignee not in", values, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeBetween(String value1, String value2) {
            addCriterion("recv_consignee between", value1, value2, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvConsigneeNotBetween(String value1, String value2) {
            addCriterion("recv_consignee not between", value1, value2, "recvConsignee");
            return (Criteria) this;
        }

        public Criteria andRecvMobileIsNull() {
            addCriterion("recv_mobile is null");
            return (Criteria) this;
        }

        public Criteria andRecvMobileIsNotNull() {
            addCriterion("recv_mobile is not null");
            return (Criteria) this;
        }

        public Criteria andRecvMobileEqualTo(String value) {
            addCriterion("recv_mobile =", value, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileNotEqualTo(String value) {
            addCriterion("recv_mobile <>", value, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileGreaterThan(String value) {
            addCriterion("recv_mobile >", value, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileGreaterThanOrEqualTo(String value) {
            addCriterion("recv_mobile >=", value, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileLessThan(String value) {
            addCriterion("recv_mobile <", value, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileLessThanOrEqualTo(String value) {
            addCriterion("recv_mobile <=", value, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileLike(String value) {
            addCriterion("recv_mobile like", value, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileNotLike(String value) {
            addCriterion("recv_mobile not like", value, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileIn(List<String> values) {
            addCriterion("recv_mobile in", values, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileNotIn(List<String> values) {
            addCriterion("recv_mobile not in", values, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileBetween(String value1, String value2) {
            addCriterion("recv_mobile between", value1, value2, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvMobileNotBetween(String value1, String value2) {
            addCriterion("recv_mobile not between", value1, value2, "recvMobile");
            return (Criteria) this;
        }

        public Criteria andRecvTelIsNull() {
            addCriterion("recv_tel is null");
            return (Criteria) this;
        }

        public Criteria andRecvTelIsNotNull() {
            addCriterion("recv_tel is not null");
            return (Criteria) this;
        }

        public Criteria andRecvTelEqualTo(String value) {
            addCriterion("recv_tel =", value, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelNotEqualTo(String value) {
            addCriterion("recv_tel <>", value, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelGreaterThan(String value) {
            addCriterion("recv_tel >", value, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelGreaterThanOrEqualTo(String value) {
            addCriterion("recv_tel >=", value, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelLessThan(String value) {
            addCriterion("recv_tel <", value, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelLessThanOrEqualTo(String value) {
            addCriterion("recv_tel <=", value, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelLike(String value) {
            addCriterion("recv_tel like", value, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelNotLike(String value) {
            addCriterion("recv_tel not like", value, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelIn(List<String> values) {
            addCriterion("recv_tel in", values, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelNotIn(List<String> values) {
            addCriterion("recv_tel not in", values, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelBetween(String value1, String value2) {
            addCriterion("recv_tel between", value1, value2, "recvTel");
            return (Criteria) this;
        }

        public Criteria andRecvTelNotBetween(String value1, String value2) {
            addCriterion("recv_tel not between", value1, value2, "recvTel");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNull() {
            addCriterion("province is null");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNotNull() {
            addCriterion("province is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceEqualTo(String value) {
            addCriterion("province =", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotEqualTo(String value) {
            addCriterion("province <>", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThan(String value) {
            addCriterion("province >", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("province >=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThan(String value) {
            addCriterion("province <", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThanOrEqualTo(String value) {
            addCriterion("province <=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLike(String value) {
            addCriterion("province like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotLike(String value) {
            addCriterion("province not like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceIn(List<String> values) {
            addCriterion("province in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotIn(List<String> values) {
            addCriterion("province not in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceBetween(String value1, String value2) {
            addCriterion("province between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotBetween(String value1, String value2) {
            addCriterion("province not between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andCityIsNull() {
            addCriterion("city is null");
            return (Criteria) this;
        }

        public Criteria andCityIsNotNull() {
            addCriterion("city is not null");
            return (Criteria) this;
        }

        public Criteria andCityEqualTo(String value) {
            addCriterion("city =", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotEqualTo(String value) {
            addCriterion("city <>", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThan(String value) {
            addCriterion("city >", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThanOrEqualTo(String value) {
            addCriterion("city >=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThan(String value) {
            addCriterion("city <", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThanOrEqualTo(String value) {
            addCriterion("city <=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("city like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotLike(String value) {
            addCriterion("city not like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityIn(List<String> values) {
            addCriterion("city in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotIn(List<String> values) {
            addCriterion("city not in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityBetween(String value1, String value2) {
            addCriterion("city between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotBetween(String value1, String value2) {
            addCriterion("city not between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCountyIsNull() {
            addCriterion("county is null");
            return (Criteria) this;
        }

        public Criteria andCountyIsNotNull() {
            addCriterion("county is not null");
            return (Criteria) this;
        }

        public Criteria andCountyEqualTo(String value) {
            addCriterion("county =", value, "county");
            return (Criteria) this;
        }

        public Criteria andCountyNotEqualTo(String value) {
            addCriterion("county <>", value, "county");
            return (Criteria) this;
        }

        public Criteria andCountyGreaterThan(String value) {
            addCriterion("county >", value, "county");
            return (Criteria) this;
        }

        public Criteria andCountyGreaterThanOrEqualTo(String value) {
            addCriterion("county >=", value, "county");
            return (Criteria) this;
        }

        public Criteria andCountyLessThan(String value) {
            addCriterion("county <", value, "county");
            return (Criteria) this;
        }

        public Criteria andCountyLessThanOrEqualTo(String value) {
            addCriterion("county <=", value, "county");
            return (Criteria) this;
        }

        public Criteria andCountyLike(String value) {
            addCriterion("county like", value, "county");
            return (Criteria) this;
        }

        public Criteria andCountyNotLike(String value) {
            addCriterion("county not like", value, "county");
            return (Criteria) this;
        }

        public Criteria andCountyIn(List<String> values) {
            addCriterion("county in", values, "county");
            return (Criteria) this;
        }

        public Criteria andCountyNotIn(List<String> values) {
            addCriterion("county not in", values, "county");
            return (Criteria) this;
        }

        public Criteria andCountyBetween(String value1, String value2) {
            addCriterion("county between", value1, value2, "county");
            return (Criteria) this;
        }

        public Criteria andCountyNotBetween(String value1, String value2) {
            addCriterion("county not between", value1, value2, "county");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNull() {
            addCriterion("order_status is null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNotNull() {
            addCriterion("order_status is not null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusEqualTo(String value) {
            addCriterion("order_status =", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotEqualTo(String value) {
            addCriterion("order_status <>", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThan(String value) {
            addCriterion("order_status >", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThanOrEqualTo(String value) {
            addCriterion("order_status >=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThan(String value) {
            addCriterion("order_status <", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThanOrEqualTo(String value) {
            addCriterion("order_status <=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLike(String value) {
            addCriterion("order_status like", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotLike(String value) {
            addCriterion("order_status not like", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIn(List<String> values) {
            addCriterion("order_status in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotIn(List<String> values) {
            addCriterion("order_status not in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusBetween(String value1, String value2) {
            addCriterion("order_status between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotBetween(String value1, String value2) {
            addCriterion("order_status not between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkIsNull() {
            addCriterion("buyer_remark is null");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkIsNotNull() {
            addCriterion("buyer_remark is not null");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkEqualTo(String value) {
            addCriterion("buyer_remark =", value, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkNotEqualTo(String value) {
            addCriterion("buyer_remark <>", value, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkGreaterThan(String value) {
            addCriterion("buyer_remark >", value, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("buyer_remark >=", value, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkLessThan(String value) {
            addCriterion("buyer_remark <", value, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkLessThanOrEqualTo(String value) {
            addCriterion("buyer_remark <=", value, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkLike(String value) {
            addCriterion("buyer_remark like", value, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkNotLike(String value) {
            addCriterion("buyer_remark not like", value, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkIn(List<String> values) {
            addCriterion("buyer_remark in", values, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkNotIn(List<String> values) {
            addCriterion("buyer_remark not in", values, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkBetween(String value1, String value2) {
            addCriterion("buyer_remark between", value1, value2, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andBuyerRemarkNotBetween(String value1, String value2) {
            addCriterion("buyer_remark not between", value1, value2, "buyerRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkIsNull() {
            addCriterion("order_remark is null");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkIsNotNull() {
            addCriterion("order_remark is not null");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkEqualTo(String value) {
            addCriterion("order_remark =", value, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkNotEqualTo(String value) {
            addCriterion("order_remark <>", value, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkGreaterThan(String value) {
            addCriterion("order_remark >", value, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("order_remark >=", value, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkLessThan(String value) {
            addCriterion("order_remark <", value, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkLessThanOrEqualTo(String value) {
            addCriterion("order_remark <=", value, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkLike(String value) {
            addCriterion("order_remark like", value, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkNotLike(String value) {
            addCriterion("order_remark not like", value, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkIn(List<String> values) {
            addCriterion("order_remark in", values, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkNotIn(List<String> values) {
            addCriterion("order_remark not in", values, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkBetween(String value1, String value2) {
            addCriterion("order_remark between", value1, value2, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andOrderRemarkNotBetween(String value1, String value2) {
            addCriterion("order_remark not between", value1, value2, "orderRemark");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeIsNull() {
            addCriterion("service_salercode is null");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeIsNotNull() {
            addCriterion("service_salercode is not null");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeEqualTo(String value) {
            addCriterion("service_salercode =", value, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeNotEqualTo(String value) {
            addCriterion("service_salercode <>", value, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeGreaterThan(String value) {
            addCriterion("service_salercode >", value, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeGreaterThanOrEqualTo(String value) {
            addCriterion("service_salercode >=", value, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeLessThan(String value) {
            addCriterion("service_salercode <", value, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeLessThanOrEqualTo(String value) {
            addCriterion("service_salercode <=", value, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeLike(String value) {
            addCriterion("service_salercode like", value, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeNotLike(String value) {
            addCriterion("service_salercode not like", value, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeIn(List<String> values) {
            addCriterion("service_salercode in", values, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeNotIn(List<String> values) {
            addCriterion("service_salercode not in", values, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeBetween(String value1, String value2) {
            addCriterion("service_salercode between", value1, value2, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalercodeNotBetween(String value1, String value2) {
            addCriterion("service_salercode not between", value1, value2, "serviceSalercode");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameIsNull() {
            addCriterion("service_salername is null");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameIsNotNull() {
            addCriterion("service_salername is not null");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameEqualTo(String value) {
            addCriterion("service_salername =", value, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameNotEqualTo(String value) {
            addCriterion("service_salername <>", value, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameGreaterThan(String value) {
            addCriterion("service_salername >", value, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameGreaterThanOrEqualTo(String value) {
            addCriterion("service_salername >=", value, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameLessThan(String value) {
            addCriterion("service_salername <", value, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameLessThanOrEqualTo(String value) {
            addCriterion("service_salername <=", value, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameLike(String value) {
            addCriterion("service_salername like", value, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameNotLike(String value) {
            addCriterion("service_salername not like", value, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameIn(List<String> values) {
            addCriterion("service_salername in", values, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameNotIn(List<String> values) {
            addCriterion("service_salername not in", values, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameBetween(String value1, String value2) {
            addCriterion("service_salername between", value1, value2, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceSalernameNotBetween(String value1, String value2) {
            addCriterion("service_salername not between", value1, value2, "serviceSalername");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeIsNull() {
            addCriterion("service_channelcode is null");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeIsNotNull() {
            addCriterion("service_channelcode is not null");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeEqualTo(String value) {
            addCriterion("service_channelcode =", value, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeNotEqualTo(String value) {
            addCriterion("service_channelcode <>", value, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeGreaterThan(String value) {
            addCriterion("service_channelcode >", value, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeGreaterThanOrEqualTo(String value) {
            addCriterion("service_channelcode >=", value, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeLessThan(String value) {
            addCriterion("service_channelcode <", value, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeLessThanOrEqualTo(String value) {
            addCriterion("service_channelcode <=", value, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeLike(String value) {
            addCriterion("service_channelcode like", value, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeNotLike(String value) {
            addCriterion("service_channelcode not like", value, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeIn(List<String> values) {
            addCriterion("service_channelcode in", values, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeNotIn(List<String> values) {
            addCriterion("service_channelcode not in", values, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeBetween(String value1, String value2) {
            addCriterion("service_channelcode between", value1, value2, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andServiceChannelcodeNotBetween(String value1, String value2) {
            addCriterion("service_channelcode not between", value1, value2, "serviceChannelcode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeIsNull() {
            addCriterion("seller_code is null");
            return (Criteria) this;
        }

        public Criteria andSellerCodeIsNotNull() {
            addCriterion("seller_code is not null");
            return (Criteria) this;
        }

        public Criteria andSellerCodeEqualTo(String value) {
            addCriterion("seller_code =", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeNotEqualTo(String value) {
            addCriterion("seller_code <>", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeGreaterThan(String value) {
            addCriterion("seller_code >", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeGreaterThanOrEqualTo(String value) {
            addCriterion("seller_code >=", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeLessThan(String value) {
            addCriterion("seller_code <", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeLessThanOrEqualTo(String value) {
            addCriterion("seller_code <=", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeLike(String value) {
            addCriterion("seller_code like", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeNotLike(String value) {
            addCriterion("seller_code not like", value, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeIn(List<String> values) {
            addCriterion("seller_code in", values, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeNotIn(List<String> values) {
            addCriterion("seller_code not in", values, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeBetween(String value1, String value2) {
            addCriterion("seller_code between", value1, value2, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerCodeNotBetween(String value1, String value2) {
            addCriterion("seller_code not between", value1, value2, "sellerCode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeIsNull() {
            addCriterion("seller_sys_shopcode is null");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeIsNotNull() {
            addCriterion("seller_sys_shopcode is not null");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeEqualTo(String value) {
            addCriterion("seller_sys_shopcode =", value, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeNotEqualTo(String value) {
            addCriterion("seller_sys_shopcode <>", value, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeGreaterThan(String value) {
            addCriterion("seller_sys_shopcode >", value, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeGreaterThanOrEqualTo(String value) {
            addCriterion("seller_sys_shopcode >=", value, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeLessThan(String value) {
            addCriterion("seller_sys_shopcode <", value, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeLessThanOrEqualTo(String value) {
            addCriterion("seller_sys_shopcode <=", value, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeLike(String value) {
            addCriterion("seller_sys_shopcode like", value, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeNotLike(String value) {
            addCriterion("seller_sys_shopcode not like", value, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeIn(List<String> values) {
            addCriterion("seller_sys_shopcode in", values, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeNotIn(List<String> values) {
            addCriterion("seller_sys_shopcode not in", values, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeBetween(String value1, String value2) {
            addCriterion("seller_sys_shopcode between", value1, value2, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andSellerSysShopcodeNotBetween(String value1, String value2) {
            addCriterion("seller_sys_shopcode not between", value1, value2, "sellerSysShopcode");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeIsNull() {
            addCriterion("invoice_type is null");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeIsNotNull() {
            addCriterion("invoice_type is not null");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeEqualTo(String value) {
            addCriterion("invoice_type =", value, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeNotEqualTo(String value) {
            addCriterion("invoice_type <>", value, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeGreaterThan(String value) {
            addCriterion("invoice_type >", value, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeGreaterThanOrEqualTo(String value) {
            addCriterion("invoice_type >=", value, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeLessThan(String value) {
            addCriterion("invoice_type <", value, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeLessThanOrEqualTo(String value) {
            addCriterion("invoice_type <=", value, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeLike(String value) {
            addCriterion("invoice_type like", value, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeNotLike(String value) {
            addCriterion("invoice_type not like", value, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeIn(List<String> values) {
            addCriterion("invoice_type in", values, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeNotIn(List<String> values) {
            addCriterion("invoice_type not in", values, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeBetween(String value1, String value2) {
            addCriterion("invoice_type between", value1, value2, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceTypeNotBetween(String value1, String value2) {
            addCriterion("invoice_type not between", value1, value2, "invoiceType");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameIsNull() {
            addCriterion("invoice_name is null");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameIsNotNull() {
            addCriterion("invoice_name is not null");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameEqualTo(String value) {
            addCriterion("invoice_name =", value, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameNotEqualTo(String value) {
            addCriterion("invoice_name <>", value, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameGreaterThan(String value) {
            addCriterion("invoice_name >", value, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameGreaterThanOrEqualTo(String value) {
            addCriterion("invoice_name >=", value, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameLessThan(String value) {
            addCriterion("invoice_name <", value, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameLessThanOrEqualTo(String value) {
            addCriterion("invoice_name <=", value, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameLike(String value) {
            addCriterion("invoice_name like", value, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameNotLike(String value) {
            addCriterion("invoice_name not like", value, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameIn(List<String> values) {
            addCriterion("invoice_name in", values, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameNotIn(List<String> values) {
            addCriterion("invoice_name not in", values, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameBetween(String value1, String value2) {
            addCriterion("invoice_name between", value1, value2, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andInvoiceNameNotBetween(String value1, String value2) {
            addCriterion("invoice_name not between", value1, value2, "invoiceName");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeIsNull() {
            addCriterion("pay_time is null");
            return (Criteria) this;
        }

        public Criteria andPayTimeIsNotNull() {
            addCriterion("pay_time is not null");
            return (Criteria) this;
        }

        public Criteria andPayTimeEqualTo(Date value) {
            addCriterion("pay_time =", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotEqualTo(Date value) {
            addCriterion("pay_time <>", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeGreaterThan(Date value) {
            addCriterion("pay_time >", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("pay_time >=", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeLessThan(Date value) {
            addCriterion("pay_time <", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeLessThanOrEqualTo(Date value) {
            addCriterion("pay_time <=", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeIn(List<Date> values) {
            addCriterion("pay_time in", values, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotIn(List<Date> values) {
            addCriterion("pay_time not in", values, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeBetween(Date value1, Date value2) {
            addCriterion("pay_time between", value1, value2, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotBetween(Date value1, Date value2) {
            addCriterion("pay_time not between", value1, value2, "payTime");
            return (Criteria) this;
        }

        public Criteria andShipTimeIsNull() {
            addCriterion("ship_time is null");
            return (Criteria) this;
        }

        public Criteria andShipTimeIsNotNull() {
            addCriterion("ship_time is not null");
            return (Criteria) this;
        }

        public Criteria andShipTimeEqualTo(Date value) {
            addCriterion("ship_time =", value, "shipTime");
            return (Criteria) this;
        }

        public Criteria andShipTimeNotEqualTo(Date value) {
            addCriterion("ship_time <>", value, "shipTime");
            return (Criteria) this;
        }

        public Criteria andShipTimeGreaterThan(Date value) {
            addCriterion("ship_time >", value, "shipTime");
            return (Criteria) this;
        }

        public Criteria andShipTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("ship_time >=", value, "shipTime");
            return (Criteria) this;
        }

        public Criteria andShipTimeLessThan(Date value) {
            addCriterion("ship_time <", value, "shipTime");
            return (Criteria) this;
        }

        public Criteria andShipTimeLessThanOrEqualTo(Date value) {
            addCriterion("ship_time <=", value, "shipTime");
            return (Criteria) this;
        }

        public Criteria andShipTimeIn(List<Date> values) {
            addCriterion("ship_time in", values, "shipTime");
            return (Criteria) this;
        }

        public Criteria andShipTimeNotIn(List<Date> values) {
            addCriterion("ship_time not in", values, "shipTime");
            return (Criteria) this;
        }

        public Criteria andShipTimeBetween(Date value1, Date value2) {
            addCriterion("ship_time between", value1, value2, "shipTime");
            return (Criteria) this;
        }

        public Criteria andShipTimeNotBetween(Date value1, Date value2) {
            addCriterion("ship_time not between", value1, value2, "shipTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeIsNull() {
            addCriterion("modified_time is null");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeIsNotNull() {
            addCriterion("modified_time is not null");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeEqualTo(Date value) {
            addCriterion("modified_time =", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeNotEqualTo(Date value) {
            addCriterion("modified_time <>", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeGreaterThan(Date value) {
            addCriterion("modified_time >", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("modified_time >=", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeLessThan(Date value) {
            addCriterion("modified_time <", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeLessThanOrEqualTo(Date value) {
            addCriterion("modified_time <=", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeIn(List<Date> values) {
            addCriterion("modified_time in", values, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeNotIn(List<Date> values) {
            addCriterion("modified_time not in", values, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeBetween(Date value1, Date value2) {
            addCriterion("modified_time between", value1, value2, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeNotBetween(Date value1, Date value2) {
            addCriterion("modified_time not between", value1, value2, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andIsOsIsNull() {
            addCriterion("is_os is null");
            return (Criteria) this;
        }

        public Criteria andIsOsIsNotNull() {
            addCriterion("is_os is not null");
            return (Criteria) this;
        }

        public Criteria andIsOsEqualTo(Integer value) {
            addCriterion("is_os =", value, "isOs");
            return (Criteria) this;
        }

        public Criteria andIsOsNotEqualTo(Integer value) {
            addCriterion("is_os <>", value, "isOs");
            return (Criteria) this;
        }

        public Criteria andIsOsGreaterThan(Integer value) {
            addCriterion("is_os >", value, "isOs");
            return (Criteria) this;
        }

        public Criteria andIsOsGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_os >=", value, "isOs");
            return (Criteria) this;
        }

        public Criteria andIsOsLessThan(Integer value) {
            addCriterion("is_os <", value, "isOs");
            return (Criteria) this;
        }

        public Criteria andIsOsLessThanOrEqualTo(Integer value) {
            addCriterion("is_os <=", value, "isOs");
            return (Criteria) this;
        }

        public Criteria andIsOsIn(List<Integer> values) {
            addCriterion("is_os in", values, "isOs");
            return (Criteria) this;
        }

        public Criteria andIsOsNotIn(List<Integer> values) {
            addCriterion("is_os not in", values, "isOs");
            return (Criteria) this;
        }

        public Criteria andIsOsBetween(Integer value1, Integer value2) {
            addCriterion("is_os between", value1, value2, "isOs");
            return (Criteria) this;
        }

        public Criteria andIsOsNotBetween(Integer value1, Integer value2) {
            addCriterion("is_os not between", value1, value2, "isOs");
            return (Criteria) this;
        }

        public Criteria andAddTimeIsNull() {
            addCriterion("add_time is null");
            return (Criteria) this;
        }

        public Criteria andAddTimeIsNotNull() {
            addCriterion("add_time is not null");
            return (Criteria) this;
        }

        public Criteria andAddTimeEqualTo(Date value) {
            addCriterion("add_time =", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotEqualTo(Date value) {
            addCriterion("add_time <>", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThan(Date value) {
            addCriterion("add_time >", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("add_time >=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThan(Date value) {
            addCriterion("add_time <", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThanOrEqualTo(Date value) {
            addCriterion("add_time <=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeIn(List<Date> values) {
            addCriterion("add_time in", values, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotIn(List<Date> values) {
            addCriterion("add_time not in", values, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeBetween(Date value1, Date value2) {
            addCriterion("add_time between", value1, value2, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotBetween(Date value1, Date value2) {
            addCriterion("add_time not between", value1, value2, "addTime");
            return (Criteria) this;
        }

        public Criteria andChannelCodeIsNull() {
            addCriterion("channel_code is null");
            return (Criteria) this;
        }

        public Criteria andChannelCodeIsNotNull() {
            addCriterion("channel_code is not null");
            return (Criteria) this;
        }

        public Criteria andChannelCodeEqualTo(String value) {
            addCriterion("channel_code =", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotEqualTo(String value) {
            addCriterion("channel_code <>", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeGreaterThan(String value) {
            addCriterion("channel_code >", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeGreaterThanOrEqualTo(String value) {
            addCriterion("channel_code >=", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeLessThan(String value) {
            addCriterion("channel_code <", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeLessThanOrEqualTo(String value) {
            addCriterion("channel_code <=", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeLike(String value) {
            addCriterion("channel_code like", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotLike(String value) {
            addCriterion("channel_code not like", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeIn(List<String> values) {
            addCriterion("channel_code in", values, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotIn(List<String> values) {
            addCriterion("channel_code not in", values, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeBetween(String value1, String value2) {
            addCriterion("channel_code between", value1, value2, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotBetween(String value1, String value2) {
            addCriterion("channel_code not between", value1, value2, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelNameIsNull() {
            addCriterion("channel_name is null");
            return (Criteria) this;
        }

        public Criteria andChannelNameIsNotNull() {
            addCriterion("channel_name is not null");
            return (Criteria) this;
        }

        public Criteria andChannelNameEqualTo(String value) {
            addCriterion("channel_name =", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotEqualTo(String value) {
            addCriterion("channel_name <>", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameGreaterThan(String value) {
            addCriterion("channel_name >", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameGreaterThanOrEqualTo(String value) {
            addCriterion("channel_name >=", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLessThan(String value) {
            addCriterion("channel_name <", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLessThanOrEqualTo(String value) {
            addCriterion("channel_name <=", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLike(String value) {
            addCriterion("channel_name like", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotLike(String value) {
            addCriterion("channel_name not like", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameIn(List<String> values) {
            addCriterion("channel_name in", values, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotIn(List<String> values) {
            addCriterion("channel_name not in", values, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameBetween(String value1, String value2) {
            addCriterion("channel_name between", value1, value2, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotBetween(String value1, String value2) {
            addCriterion("channel_name not between", value1, value2, "channelName");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishIsNull() {
            addCriterion("download_finish is null");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishIsNotNull() {
            addCriterion("download_finish is not null");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishEqualTo(Integer value) {
            addCriterion("download_finish =", value, "downloadFinish");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishNotEqualTo(Integer value) {
            addCriterion("download_finish <>", value, "downloadFinish");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishGreaterThan(Integer value) {
            addCriterion("download_finish >", value, "downloadFinish");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishGreaterThanOrEqualTo(Integer value) {
            addCriterion("download_finish >=", value, "downloadFinish");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishLessThan(Integer value) {
            addCriterion("download_finish <", value, "downloadFinish");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishLessThanOrEqualTo(Integer value) {
            addCriterion("download_finish <=", value, "downloadFinish");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishIn(List<Integer> values) {
            addCriterion("download_finish in", values, "downloadFinish");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishNotIn(List<Integer> values) {
            addCriterion("download_finish not in", values, "downloadFinish");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishBetween(Integer value1, Integer value2) {
            addCriterion("download_finish between", value1, value2, "downloadFinish");
            return (Criteria) this;
        }

        public Criteria andDownloadFinishNotBetween(Integer value1, Integer value2) {
            addCriterion("download_finish not between", value1, value2, "downloadFinish");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnIsNull() {
            addCriterion("outer_order_sn is null");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnIsNotNull() {
            addCriterion("outer_order_sn is not null");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnEqualTo(String value) {
            addCriterion("outer_order_sn =", value, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnNotEqualTo(String value) {
            addCriterion("outer_order_sn <>", value, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnGreaterThan(String value) {
            addCriterion("outer_order_sn >", value, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnGreaterThanOrEqualTo(String value) {
            addCriterion("outer_order_sn >=", value, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnLessThan(String value) {
            addCriterion("outer_order_sn <", value, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnLessThanOrEqualTo(String value) {
            addCriterion("outer_order_sn <=", value, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnLike(String value) {
            addCriterion("outer_order_sn like", value, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnNotLike(String value) {
            addCriterion("outer_order_sn not like", value, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnIn(List<String> values) {
            addCriterion("outer_order_sn in", values, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnNotIn(List<String> values) {
            addCriterion("outer_order_sn not in", values, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnBetween(String value1, String value2) {
            addCriterion("outer_order_sn between", value1, value2, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOuterOrderSnNotBetween(String value1, String value2) {
            addCriterion("outer_order_sn not between", value1, value2, "outerOrderSn");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgIsNull() {
            addCriterion("order_error_msg is null");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgIsNotNull() {
            addCriterion("order_error_msg is not null");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgEqualTo(String value) {
            addCriterion("order_error_msg =", value, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgNotEqualTo(String value) {
            addCriterion("order_error_msg <>", value, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgGreaterThan(String value) {
            addCriterion("order_error_msg >", value, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgGreaterThanOrEqualTo(String value) {
            addCriterion("order_error_msg >=", value, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgLessThan(String value) {
            addCriterion("order_error_msg <", value, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgLessThanOrEqualTo(String value) {
            addCriterion("order_error_msg <=", value, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgLike(String value) {
            addCriterion("order_error_msg like", value, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgNotLike(String value) {
            addCriterion("order_error_msg not like", value, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgIn(List<String> values) {
            addCriterion("order_error_msg in", values, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgNotIn(List<String> values) {
            addCriterion("order_error_msg not in", values, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgBetween(String value1, String value2) {
            addCriterion("order_error_msg between", value1, value2, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andOrderErrorMsgNotBetween(String value1, String value2) {
            addCriterion("order_error_msg not between", value1, value2, "orderErrorMsg");
            return (Criteria) this;
        }

        public Criteria andIsOccupyIsNull() {
            addCriterion("is_occupy is null");
            return (Criteria) this;
        }

        public Criteria andIsOccupyIsNotNull() {
            addCriterion("is_occupy is not null");
            return (Criteria) this;
        }

        public Criteria andIsOccupyEqualTo(Byte value) {
            addCriterion("is_occupy =", value, "isOccupy");
            return (Criteria) this;
        }

        public Criteria andIsOccupyNotEqualTo(Byte value) {
            addCriterion("is_occupy <>", value, "isOccupy");
            return (Criteria) this;
        }

        public Criteria andIsOccupyGreaterThan(Byte value) {
            addCriterion("is_occupy >", value, "isOccupy");
            return (Criteria) this;
        }

        public Criteria andIsOccupyGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_occupy >=", value, "isOccupy");
            return (Criteria) this;
        }

        public Criteria andIsOccupyLessThan(Byte value) {
            addCriterion("is_occupy <", value, "isOccupy");
            return (Criteria) this;
        }

        public Criteria andIsOccupyLessThanOrEqualTo(Byte value) {
            addCriterion("is_occupy <=", value, "isOccupy");
            return (Criteria) this;
        }

        public Criteria andIsOccupyIn(List<Byte> values) {
            addCriterion("is_occupy in", values, "isOccupy");
            return (Criteria) this;
        }

        public Criteria andIsOccupyNotIn(List<Byte> values) {
            addCriterion("is_occupy not in", values, "isOccupy");
            return (Criteria) this;
        }

        public Criteria andIsOccupyBetween(Byte value1, Byte value2) {
            addCriterion("is_occupy between", value1, value2, "isOccupy");
            return (Criteria) this;
        }

        public Criteria andIsOccupyNotBetween(Byte value1, Byte value2) {
            addCriterion("is_occupy not between", value1, value2, "isOccupy");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIsNull() {
            addCriterion("order_type is null");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIsNotNull() {
            addCriterion("order_type is not null");
            return (Criteria) this;
        }

        public Criteria andOrderTypeEqualTo(String value) {
            addCriterion("order_type =", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotEqualTo(String value) {
            addCriterion("order_type <>", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeGreaterThan(String value) {
            addCriterion("order_type >", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeGreaterThanOrEqualTo(String value) {
            addCriterion("order_type >=", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLessThan(String value) {
            addCriterion("order_type <", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLessThanOrEqualTo(String value) {
            addCriterion("order_type <=", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLike(String value) {
            addCriterion("order_type like", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotLike(String value) {
            addCriterion("order_type not like", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIn(List<String> values) {
            addCriterion("order_type in", values, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotIn(List<String> values) {
            addCriterion("order_type not in", values, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeBetween(String value1, String value2) {
            addCriterion("order_type between", value1, value2, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotBetween(String value1, String value2) {
            addCriterion("order_type not between", value1, value2, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIsNull() {
            addCriterion("order_time is null");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIsNotNull() {
            addCriterion("order_time is not null");
            return (Criteria) this;
        }

        public Criteria andOrderTimeEqualTo(Date value) {
            addCriterion("order_time =", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotEqualTo(Date value) {
            addCriterion("order_time <>", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeGreaterThan(Date value) {
            addCriterion("order_time >", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("order_time >=", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeLessThan(Date value) {
            addCriterion("order_time <", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeLessThanOrEqualTo(Date value) {
            addCriterion("order_time <=", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIn(List<Date> values) {
            addCriterion("order_time in", values, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotIn(List<Date> values) {
            addCriterion("order_time not in", values, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeBetween(Date value1, Date value2) {
            addCriterion("order_time between", value1, value2, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotBetween(Date value1, Date value2) {
            addCriterion("order_time not between", value1, value2, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeIsNull() {
            addCriterion("order_pre_ship_time is null");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeIsNotNull() {
            addCriterion("order_pre_ship_time is not null");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeEqualTo(Date value) {
            addCriterion("order_pre_ship_time =", value, "orderPreShipTime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeNotEqualTo(Date value) {
            addCriterion("order_pre_ship_time <>", value, "orderPreShipTime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeGreaterThan(Date value) {
            addCriterion("order_pre_ship_time >", value, "orderPreShipTime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("order_pre_ship_time >=", value, "orderPreShipTime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeLessThan(Date value) {
            addCriterion("order_pre_ship_time <", value, "orderPreShipTime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeLessThanOrEqualTo(Date value) {
            addCriterion("order_pre_ship_time <=", value, "orderPreShipTime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeIn(List<Date> values) {
            addCriterion("order_pre_ship_time in", values, "orderPreShipTime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeNotIn(List<Date> values) {
            addCriterion("order_pre_ship_time not in", values, "orderPreShipTime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeBetween(Date value1, Date value2) {
            addCriterion("order_pre_ship_time between", value1, value2, "orderPreShipTime");
            return (Criteria) this;
        }

        public Criteria andOrderPreShipTimeNotBetween(Date value1, Date value2) {
            addCriterion("order_pre_ship_time not between", value1, value2, "orderPreShipTime");
            return (Criteria) this;
        }

        public Criteria andIsPayedIsNull() {
            addCriterion("is_payed is null");
            return (Criteria) this;
        }

        public Criteria andIsPayedIsNotNull() {
            addCriterion("is_payed is not null");
            return (Criteria) this;
        }

        public Criteria andIsPayedEqualTo(String value) {
            addCriterion("is_payed =", value, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedNotEqualTo(String value) {
            addCriterion("is_payed <>", value, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedGreaterThan(String value) {
            addCriterion("is_payed >", value, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedGreaterThanOrEqualTo(String value) {
            addCriterion("is_payed >=", value, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedLessThan(String value) {
            addCriterion("is_payed <", value, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedLessThanOrEqualTo(String value) {
            addCriterion("is_payed <=", value, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedLike(String value) {
            addCriterion("is_payed like", value, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedNotLike(String value) {
            addCriterion("is_payed not like", value, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedIn(List<String> values) {
            addCriterion("is_payed in", values, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedNotIn(List<String> values) {
            addCriterion("is_payed not in", values, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedBetween(String value1, String value2) {
            addCriterion("is_payed between", value1, value2, "isPayed");
            return (Criteria) this;
        }

        public Criteria andIsPayedNotBetween(String value1, String value2) {
            addCriterion("is_payed not between", value1, value2, "isPayed");
            return (Criteria) this;
        }

        public Criteria andFinishTimeIsNull() {
            addCriterion("finish_time is null");
            return (Criteria) this;
        }

        public Criteria andFinishTimeIsNotNull() {
            addCriterion("finish_time is not null");
            return (Criteria) this;
        }

        public Criteria andFinishTimeEqualTo(Date value) {
            addCriterion("finish_time =", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotEqualTo(Date value) {
            addCriterion("finish_time <>", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeGreaterThan(Date value) {
            addCriterion("finish_time >", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("finish_time >=", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeLessThan(Date value) {
            addCriterion("finish_time <", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeLessThanOrEqualTo(Date value) {
            addCriterion("finish_time <=", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeIn(List<Date> values) {
            addCriterion("finish_time in", values, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotIn(List<Date> values) {
            addCriterion("finish_time not in", values, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeBetween(Date value1, Date value2) {
            addCriterion("finish_time between", value1, value2, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotBetween(Date value1, Date value2) {
            addCriterion("finish_time not between", value1, value2, "finishTime");
            return (Criteria) this;
        }

        public Criteria andExpressFeeIsNull() {
            addCriterion("express_fee is null");
            return (Criteria) this;
        }

        public Criteria andExpressFeeIsNotNull() {
            addCriterion("express_fee is not null");
            return (Criteria) this;
        }

        public Criteria andExpressFeeEqualTo(BigDecimal value) {
            addCriterion("express_fee =", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeNotEqualTo(BigDecimal value) {
            addCriterion("express_fee <>", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeGreaterThan(BigDecimal value) {
            addCriterion("express_fee >", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("express_fee >=", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeLessThan(BigDecimal value) {
            addCriterion("express_fee <", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("express_fee <=", value, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeIn(List<BigDecimal> values) {
            addCriterion("express_fee in", values, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeNotIn(List<BigDecimal> values) {
            addCriterion("express_fee not in", values, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("express_fee between", value1, value2, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("express_fee not between", value1, value2, "expressFee");
            return (Criteria) this;
        }

        public Criteria andExpressCostIsNull() {
            addCriterion("express_cost is null");
            return (Criteria) this;
        }

        public Criteria andExpressCostIsNotNull() {
            addCriterion("express_cost is not null");
            return (Criteria) this;
        }

        public Criteria andExpressCostEqualTo(BigDecimal value) {
            addCriterion("express_cost =", value, "expressCost");
            return (Criteria) this;
        }

        public Criteria andExpressCostNotEqualTo(BigDecimal value) {
            addCriterion("express_cost <>", value, "expressCost");
            return (Criteria) this;
        }

        public Criteria andExpressCostGreaterThan(BigDecimal value) {
            addCriterion("express_cost >", value, "expressCost");
            return (Criteria) this;
        }

        public Criteria andExpressCostGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("express_cost >=", value, "expressCost");
            return (Criteria) this;
        }

        public Criteria andExpressCostLessThan(BigDecimal value) {
            addCriterion("express_cost <", value, "expressCost");
            return (Criteria) this;
        }

        public Criteria andExpressCostLessThanOrEqualTo(BigDecimal value) {
            addCriterion("express_cost <=", value, "expressCost");
            return (Criteria) this;
        }

        public Criteria andExpressCostIn(List<BigDecimal> values) {
            addCriterion("express_cost in", values, "expressCost");
            return (Criteria) this;
        }

        public Criteria andExpressCostNotIn(List<BigDecimal> values) {
            addCriterion("express_cost not in", values, "expressCost");
            return (Criteria) this;
        }

        public Criteria andExpressCostBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("express_cost between", value1, value2, "expressCost");
            return (Criteria) this;
        }

        public Criteria andExpressCostNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("express_cost not between", value1, value2, "expressCost");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeIsNull() {
            addCriterion("buyer_old_code is null");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeIsNotNull() {
            addCriterion("buyer_old_code is not null");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeEqualTo(String value) {
            addCriterion("buyer_old_code =", value, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeNotEqualTo(String value) {
            addCriterion("buyer_old_code <>", value, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeGreaterThan(String value) {
            addCriterion("buyer_old_code >", value, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeGreaterThanOrEqualTo(String value) {
            addCriterion("buyer_old_code >=", value, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeLessThan(String value) {
            addCriterion("buyer_old_code <", value, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeLessThanOrEqualTo(String value) {
            addCriterion("buyer_old_code <=", value, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeLike(String value) {
            addCriterion("buyer_old_code like", value, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeNotLike(String value) {
            addCriterion("buyer_old_code not like", value, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeIn(List<String> values) {
            addCriterion("buyer_old_code in", values, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeNotIn(List<String> values) {
            addCriterion("buyer_old_code not in", values, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeBetween(String value1, String value2) {
            addCriterion("buyer_old_code between", value1, value2, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerOldCodeNotBetween(String value1, String value2) {
            addCriterion("buyer_old_code not between", value1, value2, "buyerOldCode");
            return (Criteria) this;
        }

        public Criteria andBuyerNickIsNull() {
            addCriterion("buyer_nick is null");
            return (Criteria) this;
        }

        public Criteria andBuyerNickIsNotNull() {
            addCriterion("buyer_nick is not null");
            return (Criteria) this;
        }

        public Criteria andBuyerNickEqualTo(String value) {
            addCriterion("buyer_nick =", value, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickNotEqualTo(String value) {
            addCriterion("buyer_nick <>", value, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickGreaterThan(String value) {
            addCriterion("buyer_nick >", value, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickGreaterThanOrEqualTo(String value) {
            addCriterion("buyer_nick >=", value, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickLessThan(String value) {
            addCriterion("buyer_nick <", value, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickLessThanOrEqualTo(String value) {
            addCriterion("buyer_nick <=", value, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickLike(String value) {
            addCriterion("buyer_nick like", value, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickNotLike(String value) {
            addCriterion("buyer_nick not like", value, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickIn(List<String> values) {
            addCriterion("buyer_nick in", values, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickNotIn(List<String> values) {
            addCriterion("buyer_nick not in", values, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickBetween(String value1, String value2) {
            addCriterion("buyer_nick between", value1, value2, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andBuyerNickNotBetween(String value1, String value2) {
            addCriterion("buyer_nick not between", value1, value2, "buyerNick");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeIsNull() {
            addCriterion("express_fee_type is null");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeIsNotNull() {
            addCriterion("express_fee_type is not null");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeEqualTo(String value) {
            addCriterion("express_fee_type =", value, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeNotEqualTo(String value) {
            addCriterion("express_fee_type <>", value, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeGreaterThan(String value) {
            addCriterion("express_fee_type >", value, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeGreaterThanOrEqualTo(String value) {
            addCriterion("express_fee_type >=", value, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeLessThan(String value) {
            addCriterion("express_fee_type <", value, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeLessThanOrEqualTo(String value) {
            addCriterion("express_fee_type <=", value, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeLike(String value) {
            addCriterion("express_fee_type like", value, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeNotLike(String value) {
            addCriterion("express_fee_type not like", value, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeIn(List<String> values) {
            addCriterion("express_fee_type in", values, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeNotIn(List<String> values) {
            addCriterion("express_fee_type not in", values, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeBetween(String value1, String value2) {
            addCriterion("express_fee_type between", value1, value2, "expressFeeType");
            return (Criteria) this;
        }

        public Criteria andExpressFeeTypeNotBetween(String value1, String value2) {
            addCriterion("express_fee_type not between", value1, value2, "expressFeeType");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean limitValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isLimitValue() {
            return limitValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            if("limit".equals(condition))
              this.limitValue = true;
            else
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}