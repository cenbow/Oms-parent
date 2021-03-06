package com.work.shop.oms.bean;

import java.util.ArrayList;
import java.util.List;

public class QrderQuestionLackSkuExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    public QrderQuestionLackSkuExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
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

        public Criteria andOrderSnIsNull() {
            addCriterion("order_sn is null");
            return (Criteria) this;
        }

        public Criteria andOrderSnIsNotNull() {
            addCriterion("order_sn is not null");
            return (Criteria) this;
        }

        public Criteria andOrderSnEqualTo(String value) {
            addCriterion("order_sn =", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnNotEqualTo(String value) {
            addCriterion("order_sn <>", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnGreaterThan(String value) {
            addCriterion("order_sn >", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnGreaterThanOrEqualTo(String value) {
            addCriterion("order_sn >=", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnLessThan(String value) {
            addCriterion("order_sn <", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnLessThanOrEqualTo(String value) {
            addCriterion("order_sn <=", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnLike(String value) {
            addCriterion("order_sn like", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnNotLike(String value) {
            addCriterion("order_sn not like", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnIn(List<String> values) {
            addCriterion("order_sn in", values, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnNotIn(List<String> values) {
            addCriterion("order_sn not in", values, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnBetween(String value1, String value2) {
            addCriterion("order_sn between", value1, value2, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnNotBetween(String value1, String value2) {
            addCriterion("order_sn not between", value1, value2, "orderSn");
            return (Criteria) this;
        }

        public Criteria andCustomCodeIsNull() {
            addCriterion("custom_code is null");
            return (Criteria) this;
        }

        public Criteria andCustomCodeIsNotNull() {
            addCriterion("custom_code is not null");
            return (Criteria) this;
        }

        public Criteria andCustomCodeEqualTo(String value) {
            addCriterion("custom_code =", value, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeNotEqualTo(String value) {
            addCriterion("custom_code <>", value, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeGreaterThan(String value) {
            addCriterion("custom_code >", value, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeGreaterThanOrEqualTo(String value) {
            addCriterion("custom_code >=", value, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeLessThan(String value) {
            addCriterion("custom_code <", value, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeLessThanOrEqualTo(String value) {
            addCriterion("custom_code <=", value, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeLike(String value) {
            addCriterion("custom_code like", value, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeNotLike(String value) {
            addCriterion("custom_code not like", value, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeIn(List<String> values) {
            addCriterion("custom_code in", values, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeNotIn(List<String> values) {
            addCriterion("custom_code not in", values, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeBetween(String value1, String value2) {
            addCriterion("custom_code between", value1, value2, "customCode");
            return (Criteria) this;
        }

        public Criteria andCustomCodeNotBetween(String value1, String value2) {
            addCriterion("custom_code not between", value1, value2, "customCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeIsNull() {
            addCriterion("depot_code is null");
            return (Criteria) this;
        }

        public Criteria andDepotCodeIsNotNull() {
            addCriterion("depot_code is not null");
            return (Criteria) this;
        }

        public Criteria andDepotCodeEqualTo(String value) {
            addCriterion("depot_code =", value, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeNotEqualTo(String value) {
            addCriterion("depot_code <>", value, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeGreaterThan(String value) {
            addCriterion("depot_code >", value, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeGreaterThanOrEqualTo(String value) {
            addCriterion("depot_code >=", value, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeLessThan(String value) {
            addCriterion("depot_code <", value, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeLessThanOrEqualTo(String value) {
            addCriterion("depot_code <=", value, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeLike(String value) {
            addCriterion("depot_code like", value, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeNotLike(String value) {
            addCriterion("depot_code not like", value, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeIn(List<String> values) {
            addCriterion("depot_code in", values, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeNotIn(List<String> values) {
            addCriterion("depot_code not in", values, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeBetween(String value1, String value2) {
            addCriterion("depot_code between", value1, value2, "depotCode");
            return (Criteria) this;
        }

        public Criteria andDepotCodeNotBetween(String value1, String value2) {
            addCriterion("depot_code not between", value1, value2, "depotCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeIsNull() {
            addCriterion("question_code is null");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeIsNotNull() {
            addCriterion("question_code is not null");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeEqualTo(String value) {
            addCriterion("question_code =", value, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeNotEqualTo(String value) {
            addCriterion("question_code <>", value, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeGreaterThan(String value) {
            addCriterion("question_code >", value, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeGreaterThanOrEqualTo(String value) {
            addCriterion("question_code >=", value, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeLessThan(String value) {
            addCriterion("question_code <", value, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeLessThanOrEqualTo(String value) {
            addCriterion("question_code <=", value, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeLike(String value) {
            addCriterion("question_code like", value, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeNotLike(String value) {
            addCriterion("question_code not like", value, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeIn(List<String> values) {
            addCriterion("question_code in", values, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeNotIn(List<String> values) {
            addCriterion("question_code not in", values, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeBetween(String value1, String value2) {
            addCriterion("question_code between", value1, value2, "questionCode");
            return (Criteria) this;
        }

        public Criteria andQuestionCodeNotBetween(String value1, String value2) {
            addCriterion("question_code not between", value1, value2, "questionCode");
            return (Criteria) this;
        }

        public Criteria andLackNumIsNull() {
            addCriterion("lack_num is null");
            return (Criteria) this;
        }

        public Criteria andLackNumIsNotNull() {
            addCriterion("lack_num is not null");
            return (Criteria) this;
        }

        public Criteria andLackNumEqualTo(Short value) {
            addCriterion("lack_num =", value, "lackNum");
            return (Criteria) this;
        }

        public Criteria andLackNumNotEqualTo(Short value) {
            addCriterion("lack_num <>", value, "lackNum");
            return (Criteria) this;
        }

        public Criteria andLackNumGreaterThan(Short value) {
            addCriterion("lack_num >", value, "lackNum");
            return (Criteria) this;
        }

        public Criteria andLackNumGreaterThanOrEqualTo(Short value) {
            addCriterion("lack_num >=", value, "lackNum");
            return (Criteria) this;
        }

        public Criteria andLackNumLessThan(Short value) {
            addCriterion("lack_num <", value, "lackNum");
            return (Criteria) this;
        }

        public Criteria andLackNumLessThanOrEqualTo(Short value) {
            addCriterion("lack_num <=", value, "lackNum");
            return (Criteria) this;
        }

        public Criteria andLackNumIn(List<Short> values) {
            addCriterion("lack_num in", values, "lackNum");
            return (Criteria) this;
        }

        public Criteria andLackNumNotIn(List<Short> values) {
            addCriterion("lack_num not in", values, "lackNum");
            return (Criteria) this;
        }

        public Criteria andLackNumBetween(Short value1, Short value2) {
            addCriterion("lack_num between", value1, value2, "lackNum");
            return (Criteria) this;
        }

        public Criteria andLackNumNotBetween(Short value1, Short value2) {
            addCriterion("lack_num not between", value1, value2, "lackNum");
            return (Criteria) this;
        }

        public Criteria andDeliverySnIsNull() {
            addCriterion("delivery_sn is null");
            return (Criteria) this;
        }

        public Criteria andDeliverySnIsNotNull() {
            addCriterion("delivery_sn is not null");
            return (Criteria) this;
        }

        public Criteria andDeliverySnEqualTo(String value) {
            addCriterion("delivery_sn =", value, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnNotEqualTo(String value) {
            addCriterion("delivery_sn <>", value, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnGreaterThan(String value) {
            addCriterion("delivery_sn >", value, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnGreaterThanOrEqualTo(String value) {
            addCriterion("delivery_sn >=", value, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnLessThan(String value) {
            addCriterion("delivery_sn <", value, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnLessThanOrEqualTo(String value) {
            addCriterion("delivery_sn <=", value, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnLike(String value) {
            addCriterion("delivery_sn like", value, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnNotLike(String value) {
            addCriterion("delivery_sn not like", value, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnIn(List<String> values) {
            addCriterion("delivery_sn in", values, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnNotIn(List<String> values) {
            addCriterion("delivery_sn not in", values, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnBetween(String value1, String value2) {
            addCriterion("delivery_sn between", value1, value2, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andDeliverySnNotBetween(String value1, String value2) {
            addCriterion("delivery_sn not between", value1, value2, "deliverySn");
            return (Criteria) this;
        }

        public Criteria andLackReasonIsNull() {
            addCriterion("lack_reason is null");
            return (Criteria) this;
        }

        public Criteria andLackReasonIsNotNull() {
            addCriterion("lack_reason is not null");
            return (Criteria) this;
        }

        public Criteria andLackReasonEqualTo(String value) {
            addCriterion("lack_reason =", value, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonNotEqualTo(String value) {
            addCriterion("lack_reason <>", value, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonGreaterThan(String value) {
            addCriterion("lack_reason >", value, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonGreaterThanOrEqualTo(String value) {
            addCriterion("lack_reason >=", value, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonLessThan(String value) {
            addCriterion("lack_reason <", value, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonLessThanOrEqualTo(String value) {
            addCriterion("lack_reason <=", value, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonLike(String value) {
            addCriterion("lack_reason like", value, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonNotLike(String value) {
            addCriterion("lack_reason not like", value, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonIn(List<String> values) {
            addCriterion("lack_reason in", values, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonNotIn(List<String> values) {
            addCriterion("lack_reason not in", values, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonBetween(String value1, String value2) {
            addCriterion("lack_reason between", value1, value2, "lackReason");
            return (Criteria) this;
        }

        public Criteria andLackReasonNotBetween(String value1, String value2) {
            addCriterion("lack_reason not between", value1, value2, "lackReason");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeIsNull() {
            addCriterion("extension_code is null");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeIsNotNull() {
            addCriterion("extension_code is not null");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeEqualTo(String value) {
            addCriterion("extension_code =", value, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeNotEqualTo(String value) {
            addCriterion("extension_code <>", value, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeGreaterThan(String value) {
            addCriterion("extension_code >", value, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeGreaterThanOrEqualTo(String value) {
            addCriterion("extension_code >=", value, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeLessThan(String value) {
            addCriterion("extension_code <", value, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeLessThanOrEqualTo(String value) {
            addCriterion("extension_code <=", value, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeLike(String value) {
            addCriterion("extension_code like", value, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeNotLike(String value) {
            addCriterion("extension_code not like", value, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeIn(List<String> values) {
            addCriterion("extension_code in", values, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeNotIn(List<String> values) {
            addCriterion("extension_code not in", values, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeBetween(String value1, String value2) {
            addCriterion("extension_code between", value1, value2, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionCodeNotBetween(String value1, String value2) {
            addCriterion("extension_code not between", value1, value2, "extensionCode");
            return (Criteria) this;
        }

        public Criteria andExtensionIdIsNull() {
            addCriterion("extension_id is null");
            return (Criteria) this;
        }

        public Criteria andExtensionIdIsNotNull() {
            addCriterion("extension_id is not null");
            return (Criteria) this;
        }

        public Criteria andExtensionIdEqualTo(String value) {
            addCriterion("extension_id =", value, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdNotEqualTo(String value) {
            addCriterion("extension_id <>", value, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdGreaterThan(String value) {
            addCriterion("extension_id >", value, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdGreaterThanOrEqualTo(String value) {
            addCriterion("extension_id >=", value, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdLessThan(String value) {
            addCriterion("extension_id <", value, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdLessThanOrEqualTo(String value) {
            addCriterion("extension_id <=", value, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdLike(String value) {
            addCriterion("extension_id like", value, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdNotLike(String value) {
            addCriterion("extension_id not like", value, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdIn(List<String> values) {
            addCriterion("extension_id in", values, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdNotIn(List<String> values) {
            addCriterion("extension_id not in", values, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdBetween(String value1, String value2) {
            addCriterion("extension_id between", value1, value2, "extensionId");
            return (Criteria) this;
        }

        public Criteria andExtensionIdNotBetween(String value1, String value2) {
            addCriterion("extension_id not between", value1, value2, "extensionId");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table order_question_lack_sku
     *
     * @mbggenerated
     */
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