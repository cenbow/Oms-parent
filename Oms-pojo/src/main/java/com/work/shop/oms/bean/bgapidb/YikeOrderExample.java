package com.work.shop.oms.bean.bgapidb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class YikeOrderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public YikeOrderExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
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

        public Criteria andBarCodeIsNull() {
            addCriterion("bar_code is null");
            return (Criteria) this;
        }

        public Criteria andBarCodeIsNotNull() {
            addCriterion("bar_code is not null");
            return (Criteria) this;
        }

        public Criteria andBarCodeEqualTo(String value) {
            addCriterion("bar_code =", value, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeNotEqualTo(String value) {
            addCriterion("bar_code <>", value, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeGreaterThan(String value) {
            addCriterion("bar_code >", value, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeGreaterThanOrEqualTo(String value) {
            addCriterion("bar_code >=", value, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeLessThan(String value) {
            addCriterion("bar_code <", value, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeLessThanOrEqualTo(String value) {
            addCriterion("bar_code <=", value, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeLike(String value) {
            addCriterion("bar_code like", value, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeNotLike(String value) {
            addCriterion("bar_code not like", value, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeIn(List<String> values) {
            addCriterion("bar_code in", values, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeNotIn(List<String> values) {
            addCriterion("bar_code not in", values, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeBetween(String value1, String value2) {
            addCriterion("bar_code between", value1, value2, "barCode");
            return (Criteria) this;
        }

        public Criteria andBarCodeNotBetween(String value1, String value2) {
            addCriterion("bar_code not between", value1, value2, "barCode");
            return (Criteria) this;
        }

        public Criteria andItemNoIsNull() {
            addCriterion("item_no is null");
            return (Criteria) this;
        }

        public Criteria andItemNoIsNotNull() {
            addCriterion("item_no is not null");
            return (Criteria) this;
        }

        public Criteria andItemNoEqualTo(String value) {
            addCriterion("item_no =", value, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoNotEqualTo(String value) {
            addCriterion("item_no <>", value, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoGreaterThan(String value) {
            addCriterion("item_no >", value, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoGreaterThanOrEqualTo(String value) {
            addCriterion("item_no >=", value, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoLessThan(String value) {
            addCriterion("item_no <", value, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoLessThanOrEqualTo(String value) {
            addCriterion("item_no <=", value, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoLike(String value) {
            addCriterion("item_no like", value, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoNotLike(String value) {
            addCriterion("item_no not like", value, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoIn(List<String> values) {
            addCriterion("item_no in", values, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoNotIn(List<String> values) {
            addCriterion("item_no not in", values, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoBetween(String value1, String value2) {
            addCriterion("item_no between", value1, value2, "itemNo");
            return (Criteria) this;
        }

        public Criteria andItemNoNotBetween(String value1, String value2) {
            addCriterion("item_no not between", value1, value2, "itemNo");
            return (Criteria) this;
        }

        public Criteria andPriceSellIsNull() {
            addCriterion("price_sell is null");
            return (Criteria) this;
        }

        public Criteria andPriceSellIsNotNull() {
            addCriterion("price_sell is not null");
            return (Criteria) this;
        }

        public Criteria andPriceSellEqualTo(BigDecimal value) {
            addCriterion("price_sell =", value, "priceSell");
            return (Criteria) this;
        }

        public Criteria andPriceSellNotEqualTo(BigDecimal value) {
            addCriterion("price_sell <>", value, "priceSell");
            return (Criteria) this;
        }

        public Criteria andPriceSellGreaterThan(BigDecimal value) {
            addCriterion("price_sell >", value, "priceSell");
            return (Criteria) this;
        }

        public Criteria andPriceSellGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("price_sell >=", value, "priceSell");
            return (Criteria) this;
        }

        public Criteria andPriceSellLessThan(BigDecimal value) {
            addCriterion("price_sell <", value, "priceSell");
            return (Criteria) this;
        }

        public Criteria andPriceSellLessThanOrEqualTo(BigDecimal value) {
            addCriterion("price_sell <=", value, "priceSell");
            return (Criteria) this;
        }

        public Criteria andPriceSellIn(List<BigDecimal> values) {
            addCriterion("price_sell in", values, "priceSell");
            return (Criteria) this;
        }

        public Criteria andPriceSellNotIn(List<BigDecimal> values) {
            addCriterion("price_sell not in", values, "priceSell");
            return (Criteria) this;
        }

        public Criteria andPriceSellBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price_sell between", value1, value2, "priceSell");
            return (Criteria) this;
        }

        public Criteria andPriceSellNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price_sell not between", value1, value2, "priceSell");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalIsNull() {
            addCriterion("price_original is null");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalIsNotNull() {
            addCriterion("price_original is not null");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalEqualTo(BigDecimal value) {
            addCriterion("price_original =", value, "priceOriginal");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalNotEqualTo(BigDecimal value) {
            addCriterion("price_original <>", value, "priceOriginal");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalGreaterThan(BigDecimal value) {
            addCriterion("price_original >", value, "priceOriginal");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("price_original >=", value, "priceOriginal");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalLessThan(BigDecimal value) {
            addCriterion("price_original <", value, "priceOriginal");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("price_original <=", value, "priceOriginal");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalIn(List<BigDecimal> values) {
            addCriterion("price_original in", values, "priceOriginal");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalNotIn(List<BigDecimal> values) {
            addCriterion("price_original not in", values, "priceOriginal");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price_original between", value1, value2, "priceOriginal");
            return (Criteria) this;
        }

        public Criteria andPriceOriginalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("price_original not between", value1, value2, "priceOriginal");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(BigDecimal value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(BigDecimal value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(BigDecimal value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(BigDecimal value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<BigDecimal> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<BigDecimal> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount not between", value1, value2, "amount");
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

        public Criteria andFrDiscountAmountIsNull() {
            addCriterion("fr_discount_amount is null");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountIsNotNull() {
            addCriterion("fr_discount_amount is not null");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountEqualTo(BigDecimal value) {
            addCriterion("fr_discount_amount =", value, "frDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountNotEqualTo(BigDecimal value) {
            addCriterion("fr_discount_amount <>", value, "frDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountGreaterThan(BigDecimal value) {
            addCriterion("fr_discount_amount >", value, "frDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fr_discount_amount >=", value, "frDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountLessThan(BigDecimal value) {
            addCriterion("fr_discount_amount <", value, "frDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fr_discount_amount <=", value, "frDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountIn(List<BigDecimal> values) {
            addCriterion("fr_discount_amount in", values, "frDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountNotIn(List<BigDecimal> values) {
            addCriterion("fr_discount_amount not in", values, "frDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fr_discount_amount between", value1, value2, "frDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andFrDiscountAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fr_discount_amount not between", value1, value2, "frDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountIsNull() {
            addCriterion("coupon_discount_amount is null");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountIsNotNull() {
            addCriterion("coupon_discount_amount is not null");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountEqualTo(BigDecimal value) {
            addCriterion("coupon_discount_amount =", value, "couponDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountNotEqualTo(BigDecimal value) {
            addCriterion("coupon_discount_amount <>", value, "couponDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountGreaterThan(BigDecimal value) {
            addCriterion("coupon_discount_amount >", value, "couponDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("coupon_discount_amount >=", value, "couponDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountLessThan(BigDecimal value) {
            addCriterion("coupon_discount_amount <", value, "couponDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("coupon_discount_amount <=", value, "couponDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountIn(List<BigDecimal> values) {
            addCriterion("coupon_discount_amount in", values, "couponDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountNotIn(List<BigDecimal> values) {
            addCriterion("coupon_discount_amount not in", values, "couponDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("coupon_discount_amount between", value1, value2, "couponDiscountAmount");
            return (Criteria) this;
        }

        public Criteria andCouponDiscountAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("coupon_discount_amount not between", value1, value2, "couponDiscountAmount");
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

        public Criteria andIsGiftIsNull() {
            addCriterion("is_gift is null");
            return (Criteria) this;
        }

        public Criteria andIsGiftIsNotNull() {
            addCriterion("is_gift is not null");
            return (Criteria) this;
        }

        public Criteria andIsGiftEqualTo(String value) {
            addCriterion("is_gift =", value, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftNotEqualTo(String value) {
            addCriterion("is_gift <>", value, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftGreaterThan(String value) {
            addCriterion("is_gift >", value, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftGreaterThanOrEqualTo(String value) {
            addCriterion("is_gift >=", value, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftLessThan(String value) {
            addCriterion("is_gift <", value, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftLessThanOrEqualTo(String value) {
            addCriterion("is_gift <=", value, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftLike(String value) {
            addCriterion("is_gift like", value, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftNotLike(String value) {
            addCriterion("is_gift not like", value, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftIn(List<String> values) {
            addCriterion("is_gift in", values, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftNotIn(List<String> values) {
            addCriterion("is_gift not in", values, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftBetween(String value1, String value2) {
            addCriterion("is_gift between", value1, value2, "isGift");
            return (Criteria) this;
        }

        public Criteria andIsGiftNotBetween(String value1, String value2) {
            addCriterion("is_gift not between", value1, value2, "isGift");
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

        public Criteria andReturnQuantityEqualTo(String value) {
            addCriterion("return_quantity =", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityNotEqualTo(String value) {
            addCriterion("return_quantity <>", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityGreaterThan(String value) {
            addCriterion("return_quantity >", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityGreaterThanOrEqualTo(String value) {
            addCriterion("return_quantity >=", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityLessThan(String value) {
            addCriterion("return_quantity <", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityLessThanOrEqualTo(String value) {
            addCriterion("return_quantity <=", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityLike(String value) {
            addCriterion("return_quantity like", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityNotLike(String value) {
            addCriterion("return_quantity not like", value, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityIn(List<String> values) {
            addCriterion("return_quantity in", values, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityNotIn(List<String> values) {
            addCriterion("return_quantity not in", values, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityBetween(String value1, String value2) {
            addCriterion("return_quantity between", value1, value2, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnQuantityNotBetween(String value1, String value2) {
            addCriterion("return_quantity not between", value1, value2, "returnQuantity");
            return (Criteria) this;
        }

        public Criteria andReturnPriceIsNull() {
            addCriterion("return_price is null");
            return (Criteria) this;
        }

        public Criteria andReturnPriceIsNotNull() {
            addCriterion("return_price is not null");
            return (Criteria) this;
        }

        public Criteria andReturnPriceEqualTo(BigDecimal value) {
            addCriterion("return_price =", value, "returnPrice");
            return (Criteria) this;
        }

        public Criteria andReturnPriceNotEqualTo(BigDecimal value) {
            addCriterion("return_price <>", value, "returnPrice");
            return (Criteria) this;
        }

        public Criteria andReturnPriceGreaterThan(BigDecimal value) {
            addCriterion("return_price >", value, "returnPrice");
            return (Criteria) this;
        }

        public Criteria andReturnPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("return_price >=", value, "returnPrice");
            return (Criteria) this;
        }

        public Criteria andReturnPriceLessThan(BigDecimal value) {
            addCriterion("return_price <", value, "returnPrice");
            return (Criteria) this;
        }

        public Criteria andReturnPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("return_price <=", value, "returnPrice");
            return (Criteria) this;
        }

        public Criteria andReturnPriceIn(List<BigDecimal> values) {
            addCriterion("return_price in", values, "returnPrice");
            return (Criteria) this;
        }

        public Criteria andReturnPriceNotIn(List<BigDecimal> values) {
            addCriterion("return_price not in", values, "returnPrice");
            return (Criteria) this;
        }

        public Criteria andReturnPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_price between", value1, value2, "returnPrice");
            return (Criteria) this;
        }

        public Criteria andReturnPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_price not between", value1, value2, "returnPrice");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyIsNull() {
            addCriterion("return_money is null");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyIsNotNull() {
            addCriterion("return_money is not null");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyEqualTo(BigDecimal value) {
            addCriterion("return_money =", value, "returnMoney");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyNotEqualTo(BigDecimal value) {
            addCriterion("return_money <>", value, "returnMoney");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyGreaterThan(BigDecimal value) {
            addCriterion("return_money >", value, "returnMoney");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("return_money >=", value, "returnMoney");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyLessThan(BigDecimal value) {
            addCriterion("return_money <", value, "returnMoney");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("return_money <=", value, "returnMoney");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyIn(List<BigDecimal> values) {
            addCriterion("return_money in", values, "returnMoney");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyNotIn(List<BigDecimal> values) {
            addCriterion("return_money not in", values, "returnMoney");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_money between", value1, value2, "returnMoney");
            return (Criteria) this;
        }

        public Criteria andReturnMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_money not between", value1, value2, "returnMoney");
            return (Criteria) this;
        }

        public Criteria andQuantityIsNull() {
            addCriterion("quantity is null");
            return (Criteria) this;
        }

        public Criteria andQuantityIsNotNull() {
            addCriterion("quantity is not null");
            return (Criteria) this;
        }

        public Criteria andQuantityEqualTo(Integer value) {
            addCriterion("quantity =", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotEqualTo(Integer value) {
            addCriterion("quantity <>", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityGreaterThan(Integer value) {
            addCriterion("quantity >", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("quantity >=", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityLessThan(Integer value) {
            addCriterion("quantity <", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("quantity <=", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityIn(List<Integer> values) {
            addCriterion("quantity in", values, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotIn(List<Integer> values) {
            addCriterion("quantity not in", values, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityBetween(Integer value1, Integer value2) {
            addCriterion("quantity between", value1, value2, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("quantity not between", value1, value2, "quantity");
            return (Criteria) this;
        }

        public Criteria andProductNameIsNull() {
            addCriterion("product_name is null");
            return (Criteria) this;
        }

        public Criteria andProductNameIsNotNull() {
            addCriterion("product_name is not null");
            return (Criteria) this;
        }

        public Criteria andProductNameEqualTo(String value) {
            addCriterion("product_name =", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotEqualTo(String value) {
            addCriterion("product_name <>", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameGreaterThan(String value) {
            addCriterion("product_name >", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameGreaterThanOrEqualTo(String value) {
            addCriterion("product_name >=", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLessThan(String value) {
            addCriterion("product_name <", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLessThanOrEqualTo(String value) {
            addCriterion("product_name <=", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLike(String value) {
            addCriterion("product_name like", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotLike(String value) {
            addCriterion("product_name not like", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameIn(List<String> values) {
            addCriterion("product_name in", values, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotIn(List<String> values) {
            addCriterion("product_name not in", values, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameBetween(String value1, String value2) {
            addCriterion("product_name between", value1, value2, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotBetween(String value1, String value2) {
            addCriterion("product_name not between", value1, value2, "productName");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueIsNull() {
            addCriterion("use_coupon_value is null");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueIsNotNull() {
            addCriterion("use_coupon_value is not null");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueEqualTo(String value) {
            addCriterion("use_coupon_value =", value, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueNotEqualTo(String value) {
            addCriterion("use_coupon_value <>", value, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueGreaterThan(String value) {
            addCriterion("use_coupon_value >", value, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueGreaterThanOrEqualTo(String value) {
            addCriterion("use_coupon_value >=", value, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueLessThan(String value) {
            addCriterion("use_coupon_value <", value, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueLessThanOrEqualTo(String value) {
            addCriterion("use_coupon_value <=", value, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueLike(String value) {
            addCriterion("use_coupon_value like", value, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueNotLike(String value) {
            addCriterion("use_coupon_value not like", value, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueIn(List<String> values) {
            addCriterion("use_coupon_value in", values, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueNotIn(List<String> values) {
            addCriterion("use_coupon_value not in", values, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueBetween(String value1, String value2) {
            addCriterion("use_coupon_value between", value1, value2, "useCouponValue");
            return (Criteria) this;
        }

        public Criteria andUseCouponValueNotBetween(String value1, String value2) {
            addCriterion("use_coupon_value not between", value1, value2, "useCouponValue");
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