package com.work.shop.oms.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderTimeoutExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    public OrderTimeoutExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_timeout
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
     * This method corresponds to the database table order_timeout
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
     * This method corresponds to the database table order_timeout
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_timeout
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
     * This class corresponds to the database table order_timeout
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

        public Criteria andRelatingSnIsNull() {
            addCriterion("relating_sn is null");
            return (Criteria) this;
        }

        public Criteria andRelatingSnIsNotNull() {
            addCriterion("relating_sn is not null");
            return (Criteria) this;
        }

        public Criteria andRelatingSnEqualTo(String value) {
            addCriterion("relating_sn =", value, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnNotEqualTo(String value) {
            addCriterion("relating_sn <>", value, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnGreaterThan(String value) {
            addCriterion("relating_sn >", value, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnGreaterThanOrEqualTo(String value) {
            addCriterion("relating_sn >=", value, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnLessThan(String value) {
            addCriterion("relating_sn <", value, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnLessThanOrEqualTo(String value) {
            addCriterion("relating_sn <=", value, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnLike(String value) {
            addCriterion("relating_sn like", value, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnNotLike(String value) {
            addCriterion("relating_sn not like", value, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnIn(List<String> values) {
            addCriterion("relating_sn in", values, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnNotIn(List<String> values) {
            addCriterion("relating_sn not in", values, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnBetween(String value1, String value2) {
            addCriterion("relating_sn between", value1, value2, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingSnNotBetween(String value1, String value2) {
            addCriterion("relating_sn not between", value1, value2, "relatingSn");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeIsNull() {
            addCriterion("relating_type is null");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeIsNotNull() {
            addCriterion("relating_type is not null");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeEqualTo(Byte value) {
            addCriterion("relating_type =", value, "relatingType");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeNotEqualTo(Byte value) {
            addCriterion("relating_type <>", value, "relatingType");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeGreaterThan(Byte value) {
            addCriterion("relating_type >", value, "relatingType");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("relating_type >=", value, "relatingType");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeLessThan(Byte value) {
            addCriterion("relating_type <", value, "relatingType");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeLessThanOrEqualTo(Byte value) {
            addCriterion("relating_type <=", value, "relatingType");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeIn(List<Byte> values) {
            addCriterion("relating_type in", values, "relatingType");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeNotIn(List<Byte> values) {
            addCriterion("relating_type not in", values, "relatingType");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeBetween(Byte value1, Byte value2) {
            addCriterion("relating_type between", value1, value2, "relatingType");
            return (Criteria) this;
        }

        public Criteria andRelatingTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("relating_type not between", value1, value2, "relatingType");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonIsNull() {
            addCriterion("timeout_reason is null");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonIsNotNull() {
            addCriterion("timeout_reason is not null");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonEqualTo(String value) {
            addCriterion("timeout_reason =", value, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonNotEqualTo(String value) {
            addCriterion("timeout_reason <>", value, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonGreaterThan(String value) {
            addCriterion("timeout_reason >", value, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonGreaterThanOrEqualTo(String value) {
            addCriterion("timeout_reason >=", value, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonLessThan(String value) {
            addCriterion("timeout_reason <", value, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonLessThanOrEqualTo(String value) {
            addCriterion("timeout_reason <=", value, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonLike(String value) {
            addCriterion("timeout_reason like", value, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonNotLike(String value) {
            addCriterion("timeout_reason not like", value, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonIn(List<String> values) {
            addCriterion("timeout_reason in", values, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonNotIn(List<String> values) {
            addCriterion("timeout_reason not in", values, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonBetween(String value1, String value2) {
            addCriterion("timeout_reason between", value1, value2, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andTimeoutReasonNotBetween(String value1, String value2) {
            addCriterion("timeout_reason not between", value1, value2, "timeoutReason");
            return (Criteria) this;
        }

        public Criteria andProcessUserIsNull() {
            addCriterion("process_user is null");
            return (Criteria) this;
        }

        public Criteria andProcessUserIsNotNull() {
            addCriterion("process_user is not null");
            return (Criteria) this;
        }

        public Criteria andProcessUserEqualTo(String value) {
            addCriterion("process_user =", value, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserNotEqualTo(String value) {
            addCriterion("process_user <>", value, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserGreaterThan(String value) {
            addCriterion("process_user >", value, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserGreaterThanOrEqualTo(String value) {
            addCriterion("process_user >=", value, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserLessThan(String value) {
            addCriterion("process_user <", value, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserLessThanOrEqualTo(String value) {
            addCriterion("process_user <=", value, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserLike(String value) {
            addCriterion("process_user like", value, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserNotLike(String value) {
            addCriterion("process_user not like", value, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserIn(List<String> values) {
            addCriterion("process_user in", values, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserNotIn(List<String> values) {
            addCriterion("process_user not in", values, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserBetween(String value1, String value2) {
            addCriterion("process_user between", value1, value2, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessUserNotBetween(String value1, String value2) {
            addCriterion("process_user not between", value1, value2, "processUser");
            return (Criteria) this;
        }

        public Criteria andProcessStatusIsNull() {
            addCriterion("process_status is null");
            return (Criteria) this;
        }

        public Criteria andProcessStatusIsNotNull() {
            addCriterion("process_status is not null");
            return (Criteria) this;
        }

        public Criteria andProcessStatusEqualTo(Integer value) {
            addCriterion("process_status =", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusNotEqualTo(Integer value) {
            addCriterion("process_status <>", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusGreaterThan(Integer value) {
            addCriterion("process_status >", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("process_status >=", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusLessThan(Integer value) {
            addCriterion("process_status <", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusLessThanOrEqualTo(Integer value) {
            addCriterion("process_status <=", value, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusIn(List<Integer> values) {
            addCriterion("process_status in", values, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusNotIn(List<Integer> values) {
            addCriterion("process_status not in", values, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusBetween(Integer value1, Integer value2) {
            addCriterion("process_status between", value1, value2, "processStatus");
            return (Criteria) this;
        }

        public Criteria andProcessStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("process_status not between", value1, value2, "processStatus");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("start_time is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("start_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Date value) {
            addCriterion("start_time =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Date value) {
            addCriterion("start_time <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Date value) {
            addCriterion("start_time >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_time >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Date value) {
            addCriterion("start_time <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_time <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Date> values) {
            addCriterion("start_time in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Date> values) {
            addCriterion("start_time not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Date value1, Date value2) {
            addCriterion("start_time between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_time not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeIsNull() {
            addCriterion("confirm_time is null");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeIsNotNull() {
            addCriterion("confirm_time is not null");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeEqualTo(Date value) {
            addCriterion("confirm_time =", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeNotEqualTo(Date value) {
            addCriterion("confirm_time <>", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeGreaterThan(Date value) {
            addCriterion("confirm_time >", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("confirm_time >=", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeLessThan(Date value) {
            addCriterion("confirm_time <", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeLessThanOrEqualTo(Date value) {
            addCriterion("confirm_time <=", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeIn(List<Date> values) {
            addCriterion("confirm_time in", values, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeNotIn(List<Date> values) {
            addCriterion("confirm_time not in", values, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeBetween(Date value1, Date value2) {
            addCriterion("confirm_time between", value1, value2, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeNotBetween(Date value1, Date value2) {
            addCriterion("confirm_time not between", value1, value2, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andSuspendDescIsNull() {
            addCriterion("suspend_desc is null");
            return (Criteria) this;
        }

        public Criteria andSuspendDescIsNotNull() {
            addCriterion("suspend_desc is not null");
            return (Criteria) this;
        }

        public Criteria andSuspendDescEqualTo(String value) {
            addCriterion("suspend_desc =", value, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescNotEqualTo(String value) {
            addCriterion("suspend_desc <>", value, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescGreaterThan(String value) {
            addCriterion("suspend_desc >", value, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescGreaterThanOrEqualTo(String value) {
            addCriterion("suspend_desc >=", value, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescLessThan(String value) {
            addCriterion("suspend_desc <", value, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescLessThanOrEqualTo(String value) {
            addCriterion("suspend_desc <=", value, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescLike(String value) {
            addCriterion("suspend_desc like", value, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescNotLike(String value) {
            addCriterion("suspend_desc not like", value, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescIn(List<String> values) {
            addCriterion("suspend_desc in", values, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescNotIn(List<String> values) {
            addCriterion("suspend_desc not in", values, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescBetween(String value1, String value2) {
            addCriterion("suspend_desc between", value1, value2, "suspendDesc");
            return (Criteria) this;
        }

        public Criteria andSuspendDescNotBetween(String value1, String value2) {
            addCriterion("suspend_desc not between", value1, value2, "suspendDesc");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table order_timeout
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
     * This class corresponds to the database table order_timeout
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