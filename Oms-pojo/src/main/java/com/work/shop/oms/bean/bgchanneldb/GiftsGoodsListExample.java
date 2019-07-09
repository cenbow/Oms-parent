package com.work.shop.oms.bean.bgchanneldb;

import java.util.ArrayList;
import java.util.List;

public class GiftsGoodsListExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GiftsGoodsListExample() {
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

        public Criteria andGoodsSnIsNull() {
            addCriterion("goods_sn is null");
            return (Criteria) this;
        }

        public Criteria andGoodsSnIsNotNull() {
            addCriterion("goods_sn is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsSnEqualTo(String value) {
            addCriterion("goods_sn =", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnNotEqualTo(String value) {
            addCriterion("goods_sn <>", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnGreaterThan(String value) {
            addCriterion("goods_sn >", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnGreaterThanOrEqualTo(String value) {
            addCriterion("goods_sn >=", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnLessThan(String value) {
            addCriterion("goods_sn <", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnLessThanOrEqualTo(String value) {
            addCriterion("goods_sn <=", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnLike(String value) {
            addCriterion("goods_sn like", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnNotLike(String value) {
            addCriterion("goods_sn not like", value, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnIn(List<String> values) {
            addCriterion("goods_sn in", values, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnNotIn(List<String> values) {
            addCriterion("goods_sn not in", values, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnBetween(String value1, String value2) {
            addCriterion("goods_sn between", value1, value2, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGoodsSnNotBetween(String value1, String value2) {
            addCriterion("goods_sn not between", value1, value2, "goodsSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSumIsNull() {
            addCriterion("gifts_sum is null");
            return (Criteria) this;
        }

        public Criteria andGiftsSumIsNotNull() {
            addCriterion("gifts_sum is not null");
            return (Criteria) this;
        }

        public Criteria andGiftsSumEqualTo(Integer value) {
            addCriterion("gifts_sum =", value, "giftsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsSumNotEqualTo(Integer value) {
            addCriterion("gifts_sum <>", value, "giftsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsSumGreaterThan(Integer value) {
            addCriterion("gifts_sum >", value, "giftsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsSumGreaterThanOrEqualTo(Integer value) {
            addCriterion("gifts_sum >=", value, "giftsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsSumLessThan(Integer value) {
            addCriterion("gifts_sum <", value, "giftsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsSumLessThanOrEqualTo(Integer value) {
            addCriterion("gifts_sum <=", value, "giftsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsSumIn(List<Integer> values) {
            addCriterion("gifts_sum in", values, "giftsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsSumNotIn(List<Integer> values) {
            addCriterion("gifts_sum not in", values, "giftsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsSumBetween(Integer value1, Integer value2) {
            addCriterion("gifts_sum between", value1, value2, "giftsSum");
            return (Criteria) this;
        }

        public Criteria andGiftsSumNotBetween(Integer value1, Integer value2) {
            addCriterion("gifts_sum not between", value1, value2, "giftsSum");
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