package com.work.shop.oms.bean.bgchanneldb;

import java.util.ArrayList;
import java.util.List;

public class PromotionsLimitMoneyGoodsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PromotionsLimitMoneyGoodsExample() {
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

        public Criteria andGiftsSkuSnIsNull() {
            addCriterion("gifts_sku_Sn is null");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnIsNotNull() {
            addCriterion("gifts_sku_Sn is not null");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnEqualTo(String value) {
            addCriterion("gifts_sku_Sn =", value, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnNotEqualTo(String value) {
            addCriterion("gifts_sku_Sn <>", value, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnGreaterThan(String value) {
            addCriterion("gifts_sku_Sn >", value, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnGreaterThanOrEqualTo(String value) {
            addCriterion("gifts_sku_Sn >=", value, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnLessThan(String value) {
            addCriterion("gifts_sku_Sn <", value, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnLessThanOrEqualTo(String value) {
            addCriterion("gifts_sku_Sn <=", value, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnLike(String value) {
            addCriterion("gifts_sku_Sn like", value, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnNotLike(String value) {
            addCriterion("gifts_sku_Sn not like", value, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnIn(List<String> values) {
            addCriterion("gifts_sku_Sn in", values, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnNotIn(List<String> values) {
            addCriterion("gifts_sku_Sn not in", values, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnBetween(String value1, String value2) {
            addCriterion("gifts_sku_Sn between", value1, value2, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuSnNotBetween(String value1, String value2) {
            addCriterion("gifts_sku_Sn not between", value1, value2, "giftsSkuSn");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountIsNull() {
            addCriterion("gifts_sku_count is null");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountIsNotNull() {
            addCriterion("gifts_sku_count is not null");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountEqualTo(Integer value) {
            addCriterion("gifts_sku_count =", value, "giftsSkuCount");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountNotEqualTo(Integer value) {
            addCriterion("gifts_sku_count <>", value, "giftsSkuCount");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountGreaterThan(Integer value) {
            addCriterion("gifts_sku_count >", value, "giftsSkuCount");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("gifts_sku_count >=", value, "giftsSkuCount");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountLessThan(Integer value) {
            addCriterion("gifts_sku_count <", value, "giftsSkuCount");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountLessThanOrEqualTo(Integer value) {
            addCriterion("gifts_sku_count <=", value, "giftsSkuCount");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountIn(List<Integer> values) {
            addCriterion("gifts_sku_count in", values, "giftsSkuCount");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountNotIn(List<Integer> values) {
            addCriterion("gifts_sku_count not in", values, "giftsSkuCount");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountBetween(Integer value1, Integer value2) {
            addCriterion("gifts_sku_count between", value1, value2, "giftsSkuCount");
            return (Criteria) this;
        }

        public Criteria andGiftsSkuCountNotBetween(Integer value1, Integer value2) {
            addCriterion("gifts_sku_count not between", value1, value2, "giftsSkuCount");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityIsNull() {
            addCriterion("gifts_priority is null");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityIsNotNull() {
            addCriterion("gifts_priority is not null");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityEqualTo(Byte value) {
            addCriterion("gifts_priority =", value, "giftsPriority");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityNotEqualTo(Byte value) {
            addCriterion("gifts_priority <>", value, "giftsPriority");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityGreaterThan(Byte value) {
            addCriterion("gifts_priority >", value, "giftsPriority");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityGreaterThanOrEqualTo(Byte value) {
            addCriterion("gifts_priority >=", value, "giftsPriority");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityLessThan(Byte value) {
            addCriterion("gifts_priority <", value, "giftsPriority");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityLessThanOrEqualTo(Byte value) {
            addCriterion("gifts_priority <=", value, "giftsPriority");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityIn(List<Byte> values) {
            addCriterion("gifts_priority in", values, "giftsPriority");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityNotIn(List<Byte> values) {
            addCriterion("gifts_priority not in", values, "giftsPriority");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityBetween(Byte value1, Byte value2) {
            addCriterion("gifts_priority between", value1, value2, "giftsPriority");
            return (Criteria) this;
        }

        public Criteria andGiftsPriorityNotBetween(Byte value1, Byte value2) {
            addCriterion("gifts_priority not between", value1, value2, "giftsPriority");
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