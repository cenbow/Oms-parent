package com.work.shop.oms.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CardLogExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table card_log
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table card_log
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table card_log
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_log
     *
     * @mbggenerated
     */
    public CardLogExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_log
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_log
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_log
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_log
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_log
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_log
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_log
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
     * This method corresponds to the database table card_log
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
     * This method corresponds to the database table card_log
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table card_log
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
     * This class corresponds to the database table card_log
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

        public Criteria andClIdIsNull() {
            addCriterion("cl_id is null");
            return (Criteria) this;
        }

        public Criteria andClIdIsNotNull() {
            addCriterion("cl_id is not null");
            return (Criteria) this;
        }

        public Criteria andClIdEqualTo(Integer value) {
            addCriterion("cl_id =", value, "clId");
            return (Criteria) this;
        }

        public Criteria andClIdNotEqualTo(Integer value) {
            addCriterion("cl_id <>", value, "clId");
            return (Criteria) this;
        }

        public Criteria andClIdGreaterThan(Integer value) {
            addCriterion("cl_id >", value, "clId");
            return (Criteria) this;
        }

        public Criteria andClIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("cl_id >=", value, "clId");
            return (Criteria) this;
        }

        public Criteria andClIdLessThan(Integer value) {
            addCriterion("cl_id <", value, "clId");
            return (Criteria) this;
        }

        public Criteria andClIdLessThanOrEqualTo(Integer value) {
            addCriterion("cl_id <=", value, "clId");
            return (Criteria) this;
        }

        public Criteria andClIdIn(List<Integer> values) {
            addCriterion("cl_id in", values, "clId");
            return (Criteria) this;
        }

        public Criteria andClIdNotIn(List<Integer> values) {
            addCriterion("cl_id not in", values, "clId");
            return (Criteria) this;
        }

        public Criteria andClIdBetween(Integer value1, Integer value2) {
            addCriterion("cl_id between", value1, value2, "clId");
            return (Criteria) this;
        }

        public Criteria andClIdNotBetween(Integer value1, Integer value2) {
            addCriterion("cl_id not between", value1, value2, "clId");
            return (Criteria) this;
        }

        public Criteria andClDescIsNull() {
            addCriterion("cl_desc is null");
            return (Criteria) this;
        }

        public Criteria andClDescIsNotNull() {
            addCriterion("cl_desc is not null");
            return (Criteria) this;
        }

        public Criteria andClDescEqualTo(String value) {
            addCriterion("cl_desc =", value, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescNotEqualTo(String value) {
            addCriterion("cl_desc <>", value, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescGreaterThan(String value) {
            addCriterion("cl_desc >", value, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescGreaterThanOrEqualTo(String value) {
            addCriterion("cl_desc >=", value, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescLessThan(String value) {
            addCriterion("cl_desc <", value, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescLessThanOrEqualTo(String value) {
            addCriterion("cl_desc <=", value, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescLike(String value) {
            addCriterion("cl_desc like", value, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescNotLike(String value) {
            addCriterion("cl_desc not like", value, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescIn(List<String> values) {
            addCriterion("cl_desc in", values, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescNotIn(List<String> values) {
            addCriterion("cl_desc not in", values, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescBetween(String value1, String value2) {
            addCriterion("cl_desc between", value1, value2, "clDesc");
            return (Criteria) this;
        }

        public Criteria andClDescNotBetween(String value1, String value2) {
            addCriterion("cl_desc not between", value1, value2, "clDesc");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andClTypeIsNull() {
            addCriterion("cl_type is null");
            return (Criteria) this;
        }

        public Criteria andClTypeIsNotNull() {
            addCriterion("cl_type is not null");
            return (Criteria) this;
        }

        public Criteria andClTypeEqualTo(Byte value) {
            addCriterion("cl_type =", value, "clType");
            return (Criteria) this;
        }

        public Criteria andClTypeNotEqualTo(Byte value) {
            addCriterion("cl_type <>", value, "clType");
            return (Criteria) this;
        }

        public Criteria andClTypeGreaterThan(Byte value) {
            addCriterion("cl_type >", value, "clType");
            return (Criteria) this;
        }

        public Criteria andClTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("cl_type >=", value, "clType");
            return (Criteria) this;
        }

        public Criteria andClTypeLessThan(Byte value) {
            addCriterion("cl_type <", value, "clType");
            return (Criteria) this;
        }

        public Criteria andClTypeLessThanOrEqualTo(Byte value) {
            addCriterion("cl_type <=", value, "clType");
            return (Criteria) this;
        }

        public Criteria andClTypeIn(List<Byte> values) {
            addCriterion("cl_type in", values, "clType");
            return (Criteria) this;
        }

        public Criteria andClTypeNotIn(List<Byte> values) {
            addCriterion("cl_type not in", values, "clType");
            return (Criteria) this;
        }

        public Criteria andClTypeBetween(Byte value1, Byte value2) {
            addCriterion("cl_type between", value1, value2, "clType");
            return (Criteria) this;
        }

        public Criteria andClTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("cl_type not between", value1, value2, "clType");
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

        public Criteria andCardNoIsNull() {
            addCriterion("card_no is null");
            return (Criteria) this;
        }

        public Criteria andCardNoIsNotNull() {
            addCriterion("card_no is not null");
            return (Criteria) this;
        }

        public Criteria andCardNoEqualTo(String value) {
            addCriterion("card_no =", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoNotEqualTo(String value) {
            addCriterion("card_no <>", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoGreaterThan(String value) {
            addCriterion("card_no >", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoGreaterThanOrEqualTo(String value) {
            addCriterion("card_no >=", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoLessThan(String value) {
            addCriterion("card_no <", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoLessThanOrEqualTo(String value) {
            addCriterion("card_no <=", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoLike(String value) {
            addCriterion("card_no like", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoNotLike(String value) {
            addCriterion("card_no not like", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoIn(List<String> values) {
            addCriterion("card_no in", values, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoNotIn(List<String> values) {
            addCriterion("card_no not in", values, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoBetween(String value1, String value2) {
            addCriterion("card_no between", value1, value2, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoNotBetween(String value1, String value2) {
            addCriterion("card_no not between", value1, value2, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameIsNull() {
            addCriterion("card_type_en_name is null");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameIsNotNull() {
            addCriterion("card_type_en_name is not null");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameEqualTo(String value) {
            addCriterion("card_type_en_name =", value, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameNotEqualTo(String value) {
            addCriterion("card_type_en_name <>", value, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameGreaterThan(String value) {
            addCriterion("card_type_en_name >", value, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameGreaterThanOrEqualTo(String value) {
            addCriterion("card_type_en_name >=", value, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameLessThan(String value) {
            addCriterion("card_type_en_name <", value, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameLessThanOrEqualTo(String value) {
            addCriterion("card_type_en_name <=", value, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameLike(String value) {
            addCriterion("card_type_en_name like", value, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameNotLike(String value) {
            addCriterion("card_type_en_name not like", value, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameIn(List<String> values) {
            addCriterion("card_type_en_name in", values, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameNotIn(List<String> values) {
            addCriterion("card_type_en_name not in", values, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameBetween(String value1, String value2) {
            addCriterion("card_type_en_name between", value1, value2, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardTypeEnNameNotBetween(String value1, String value2) {
            addCriterion("card_type_en_name not between", value1, value2, "cardTypeEnName");
            return (Criteria) this;
        }

        public Criteria andCardMoneyIsNull() {
            addCriterion("card_money is null");
            return (Criteria) this;
        }

        public Criteria andCardMoneyIsNotNull() {
            addCriterion("card_money is not null");
            return (Criteria) this;
        }

        public Criteria andCardMoneyEqualTo(Float value) {
            addCriterion("card_money =", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyNotEqualTo(Float value) {
            addCriterion("card_money <>", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyGreaterThan(Float value) {
            addCriterion("card_money >", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyGreaterThanOrEqualTo(Float value) {
            addCriterion("card_money >=", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyLessThan(Float value) {
            addCriterion("card_money <", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyLessThanOrEqualTo(Float value) {
            addCriterion("card_money <=", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyIn(List<Float> values) {
            addCriterion("card_money in", values, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyNotIn(List<Float> values) {
            addCriterion("card_money not in", values, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyBetween(Float value1, Float value2) {
            addCriterion("card_money between", value1, value2, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyNotBetween(Float value1, Float value2) {
            addCriterion("card_money not between", value1, value2, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andComefromIsNull() {
            addCriterion("comefrom is null");
            return (Criteria) this;
        }

        public Criteria andComefromIsNotNull() {
            addCriterion("comefrom is not null");
            return (Criteria) this;
        }

        public Criteria andComefromEqualTo(String value) {
            addCriterion("comefrom =", value, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromNotEqualTo(String value) {
            addCriterion("comefrom <>", value, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromGreaterThan(String value) {
            addCriterion("comefrom >", value, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromGreaterThanOrEqualTo(String value) {
            addCriterion("comefrom >=", value, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromLessThan(String value) {
            addCriterion("comefrom <", value, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromLessThanOrEqualTo(String value) {
            addCriterion("comefrom <=", value, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromLike(String value) {
            addCriterion("comefrom like", value, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromNotLike(String value) {
            addCriterion("comefrom not like", value, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromIn(List<String> values) {
            addCriterion("comefrom in", values, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromNotIn(List<String> values) {
            addCriterion("comefrom not in", values, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromBetween(String value1, String value2) {
            addCriterion("comefrom between", value1, value2, "comefrom");
            return (Criteria) this;
        }

        public Criteria andComefromNotBetween(String value1, String value2) {
            addCriterion("comefrom not between", value1, value2, "comefrom");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table card_log
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
     * This class corresponds to the database table card_log
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