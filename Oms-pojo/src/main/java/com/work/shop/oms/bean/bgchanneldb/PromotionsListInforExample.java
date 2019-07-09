package com.work.shop.oms.bean.bgchanneldb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PromotionsListInforExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PromotionsListInforExample() {
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

        public Criteria andGoodsCountIsNull() {
            addCriterion("goods_count is null");
            return (Criteria) this;
        }

        public Criteria andGoodsCountIsNotNull() {
            addCriterion("goods_count is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsCountEqualTo(Integer value) {
            addCriterion("goods_count =", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountNotEqualTo(Integer value) {
            addCriterion("goods_count <>", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountGreaterThan(Integer value) {
            addCriterion("goods_count >", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("goods_count >=", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountLessThan(Integer value) {
            addCriterion("goods_count <", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountLessThanOrEqualTo(Integer value) {
            addCriterion("goods_count <=", value, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountIn(List<Integer> values) {
            addCriterion("goods_count in", values, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountNotIn(List<Integer> values) {
            addCriterion("goods_count not in", values, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountBetween(Integer value1, Integer value2) {
            addCriterion("goods_count between", value1, value2, "goodsCount");
            return (Criteria) this;
        }

        public Criteria andGoodsCountNotBetween(Integer value1, Integer value2) {
            addCriterion("goods_count not between", value1, value2, "goodsCount");
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

        public Criteria andGiftsCountIsNull() {
            addCriterion("gifts_count is null");
            return (Criteria) this;
        }

        public Criteria andGiftsCountIsNotNull() {
            addCriterion("gifts_count is not null");
            return (Criteria) this;
        }

        public Criteria andGiftsCountEqualTo(Integer value) {
            addCriterion("gifts_count =", value, "giftsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsCountNotEqualTo(Integer value) {
            addCriterion("gifts_count <>", value, "giftsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsCountGreaterThan(Integer value) {
            addCriterion("gifts_count >", value, "giftsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("gifts_count >=", value, "giftsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsCountLessThan(Integer value) {
            addCriterion("gifts_count <", value, "giftsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsCountLessThanOrEqualTo(Integer value) {
            addCriterion("gifts_count <=", value, "giftsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsCountIn(List<Integer> values) {
            addCriterion("gifts_count in", values, "giftsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsCountNotIn(List<Integer> values) {
            addCriterion("gifts_count not in", values, "giftsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsCountBetween(Integer value1, Integer value2) {
            addCriterion("gifts_count between", value1, value2, "giftsCount");
            return (Criteria) this;
        }

        public Criteria andGiftsCountNotBetween(Integer value1, Integer value2) {
            addCriterion("gifts_count not between", value1, value2, "giftsCount");
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