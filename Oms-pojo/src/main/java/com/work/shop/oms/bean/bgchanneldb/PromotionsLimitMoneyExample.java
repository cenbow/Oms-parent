package com.work.shop.oms.bean.bgchanneldb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PromotionsLimitMoneyExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PromotionsLimitMoneyExample() {
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
            addCriterion("Id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("Id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("Id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("Id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("Id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("Id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("Id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("Id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("Id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("Id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("Id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("Id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPromCodeIsNull() {
            addCriterion("prom_code is null");
            return (Criteria) this;
        }

        public Criteria andPromCodeIsNotNull() {
            addCriterion("prom_code is not null");
            return (Criteria) this;
        }

        public Criteria andPromCodeEqualTo(String value) {
            addCriterion("prom_code =", value, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeNotEqualTo(String value) {
            addCriterion("prom_code <>", value, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeGreaterThan(String value) {
            addCriterion("prom_code >", value, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeGreaterThanOrEqualTo(String value) {
            addCriterion("prom_code >=", value, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeLessThan(String value) {
            addCriterion("prom_code <", value, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeLessThanOrEqualTo(String value) {
            addCriterion("prom_code <=", value, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeLike(String value) {
            addCriterion("prom_code like", value, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeNotLike(String value) {
            addCriterion("prom_code not like", value, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeIn(List<String> values) {
            addCriterion("prom_code in", values, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeNotIn(List<String> values) {
            addCriterion("prom_code not in", values, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeBetween(String value1, String value2) {
            addCriterion("prom_code between", value1, value2, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromCodeNotBetween(String value1, String value2) {
            addCriterion("prom_code not between", value1, value2, "promCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeIsNull() {
            addCriterion("prom_details_code is null");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeIsNotNull() {
            addCriterion("prom_details_code is not null");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeEqualTo(String value) {
            addCriterion("prom_details_code =", value, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeNotEqualTo(String value) {
            addCriterion("prom_details_code <>", value, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeGreaterThan(String value) {
            addCriterion("prom_details_code >", value, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeGreaterThanOrEqualTo(String value) {
            addCriterion("prom_details_code >=", value, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeLessThan(String value) {
            addCriterion("prom_details_code <", value, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeLessThanOrEqualTo(String value) {
            addCriterion("prom_details_code <=", value, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeLike(String value) {
            addCriterion("prom_details_code like", value, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeNotLike(String value) {
            addCriterion("prom_details_code not like", value, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeIn(List<String> values) {
            addCriterion("prom_details_code in", values, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeNotIn(List<String> values) {
            addCriterion("prom_details_code not in", values, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeBetween(String value1, String value2) {
            addCriterion("prom_details_code between", value1, value2, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andPromDetailsCodeNotBetween(String value1, String value2) {
            addCriterion("prom_details_code not between", value1, value2, "promDetailsCode");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyIsNull() {
            addCriterion("limit_money is null");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyIsNotNull() {
            addCriterion("limit_money is not null");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyEqualTo(BigDecimal value) {
            addCriterion("limit_money =", value, "limitMoney");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyNotEqualTo(BigDecimal value) {
            addCriterion("limit_money <>", value, "limitMoney");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyGreaterThan(BigDecimal value) {
            addCriterion("limit_money >", value, "limitMoney");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("limit_money >=", value, "limitMoney");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyLessThan(BigDecimal value) {
            addCriterion("limit_money <", value, "limitMoney");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("limit_money <=", value, "limitMoney");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyIn(List<BigDecimal> values) {
            addCriterion("limit_money in", values, "limitMoney");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyNotIn(List<BigDecimal> values) {
            addCriterion("limit_money not in", values, "limitMoney");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("limit_money between", value1, value2, "limitMoney");
            return (Criteria) this;
        }

        public Criteria andLimitMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("limit_money not between", value1, value2, "limitMoney");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnIsNull() {
            addCriterion("gifts_goods_Sn is null");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnIsNotNull() {
            addCriterion("gifts_goods_Sn is not null");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnEqualTo(String value) {
            addCriterion("gifts_goods_Sn =", value, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnNotEqualTo(String value) {
            addCriterion("gifts_goods_Sn <>", value, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnGreaterThan(String value) {
            addCriterion("gifts_goods_Sn >", value, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnGreaterThanOrEqualTo(String value) {
            addCriterion("gifts_goods_Sn >=", value, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnLessThan(String value) {
            addCriterion("gifts_goods_Sn <", value, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnLessThanOrEqualTo(String value) {
            addCriterion("gifts_goods_Sn <=", value, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnLike(String value) {
            addCriterion("gifts_goods_Sn like", value, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnNotLike(String value) {
            addCriterion("gifts_goods_Sn not like", value, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnIn(List<String> values) {
            addCriterion("gifts_goods_Sn in", values, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnNotIn(List<String> values) {
            addCriterion("gifts_goods_Sn not in", values, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnBetween(String value1, String value2) {
            addCriterion("gifts_goods_Sn between", value1, value2, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSnNotBetween(String value1, String value2) {
            addCriterion("gifts_goods_Sn not between", value1, value2, "giftsGoodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountIsNull() {
            addCriterion("gifts_goods_count is null");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountIsNotNull() {
            addCriterion("gifts_goods_count is not null");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountEqualTo(Integer value) {
            addCriterion("gifts_goods_count =", value, "giftsGoodsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountNotEqualTo(Integer value) {
            addCriterion("gifts_goods_count <>", value, "giftsGoodsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountGreaterThan(Integer value) {
            addCriterion("gifts_goods_count >", value, "giftsGoodsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("gifts_goods_count >=", value, "giftsGoodsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountLessThan(Integer value) {
            addCriterion("gifts_goods_count <", value, "giftsGoodsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountLessThanOrEqualTo(Integer value) {
            addCriterion("gifts_goods_count <=", value, "giftsGoodsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountIn(List<Integer> values) {
            addCriterion("gifts_goods_count in", values, "giftsGoodsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountNotIn(List<Integer> values) {
            addCriterion("gifts_goods_count not in", values, "giftsGoodsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountBetween(Integer value1, Integer value2) {
            addCriterion("gifts_goods_count between", value1, value2, "giftsGoodsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsCountNotBetween(Integer value1, Integer value2) {
            addCriterion("gifts_goods_count not between", value1, value2, "giftsGoodsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumIsNull() {
            addCriterion("gifts_goods_sum is null");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumIsNotNull() {
            addCriterion("gifts_goods_sum is not null");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumEqualTo(Integer value) {
            addCriterion("gifts_goods_sum =", value, "giftsGoodsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumNotEqualTo(Integer value) {
            addCriterion("gifts_goods_sum <>", value, "giftsGoodsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumGreaterThan(Integer value) {
            addCriterion("gifts_goods_sum >", value, "giftsGoodsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumGreaterThanOrEqualTo(Integer value) {
            addCriterion("gifts_goods_sum >=", value, "giftsGoodsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumLessThan(Integer value) {
            addCriterion("gifts_goods_sum <", value, "giftsGoodsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumLessThanOrEqualTo(Integer value) {
            addCriterion("gifts_goods_sum <=", value, "giftsGoodsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumIn(List<Integer> values) {
            addCriterion("gifts_goods_sum in", values, "giftsGoodsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumNotIn(List<Integer> values) {
            addCriterion("gifts_goods_sum not in", values, "giftsGoodsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumBetween(Integer value1, Integer value2) {
            addCriterion("gifts_goods_sum between", value1, value2, "giftsGoodsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsGoodsSumNotBetween(Integer value1, Integer value2) {
            addCriterion("gifts_goods_sum not between", value1, value2, "giftsGoodsSum");
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