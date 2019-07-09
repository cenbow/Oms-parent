package com.work.shop.oms.bean.bgchanneldb;

import java.util.ArrayList;
import java.util.List;

public class CsChannelInfoExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    public CsChannelInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_info
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
     * This method corresponds to the database table channel_info
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
     * This method corresponds to the database table channel_info
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_info
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
     * This class corresponds to the database table channel_info
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

        public Criteria andChanelCodeIsNull() {
            addCriterion("chanel_code is null");
            return (Criteria) this;
        }

        public Criteria andChanelCodeIsNotNull() {
            addCriterion("chanel_code is not null");
            return (Criteria) this;
        }

        public Criteria andChanelCodeEqualTo(String value) {
            addCriterion("chanel_code =", value, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeNotEqualTo(String value) {
            addCriterion("chanel_code <>", value, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeGreaterThan(String value) {
            addCriterion("chanel_code >", value, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeGreaterThanOrEqualTo(String value) {
            addCriterion("chanel_code >=", value, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeLessThan(String value) {
            addCriterion("chanel_code <", value, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeLessThanOrEqualTo(String value) {
            addCriterion("chanel_code <=", value, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeLike(String value) {
            addCriterion("chanel_code like", value, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeNotLike(String value) {
            addCriterion("chanel_code not like", value, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeIn(List<String> values) {
            addCriterion("chanel_code in", values, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeNotIn(List<String> values) {
            addCriterion("chanel_code not in", values, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeBetween(String value1, String value2) {
            addCriterion("chanel_code between", value1, value2, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChanelCodeNotBetween(String value1, String value2) {
            addCriterion("chanel_code not between", value1, value2, "chanelCode");
            return (Criteria) this;
        }

        public Criteria andChannelTitleIsNull() {
            addCriterion("channel_title is null");
            return (Criteria) this;
        }

        public Criteria andChannelTitleIsNotNull() {
            addCriterion("channel_title is not null");
            return (Criteria) this;
        }

        public Criteria andChannelTitleEqualTo(String value) {
            addCriterion("channel_title =", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleNotEqualTo(String value) {
            addCriterion("channel_title <>", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleGreaterThan(String value) {
            addCriterion("channel_title >", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleGreaterThanOrEqualTo(String value) {
            addCriterion("channel_title >=", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleLessThan(String value) {
            addCriterion("channel_title <", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleLessThanOrEqualTo(String value) {
            addCriterion("channel_title <=", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleLike(String value) {
            addCriterion("channel_title like", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleNotLike(String value) {
            addCriterion("channel_title not like", value, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleIn(List<String> values) {
            addCriterion("channel_title in", values, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleNotIn(List<String> values) {
            addCriterion("channel_title not in", values, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleBetween(String value1, String value2) {
            addCriterion("channel_title between", value1, value2, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelTitleNotBetween(String value1, String value2) {
            addCriterion("channel_title not between", value1, value2, "channelTitle");
            return (Criteria) this;
        }

        public Criteria andChannelStatusIsNull() {
            addCriterion("channel_status is null");
            return (Criteria) this;
        }

        public Criteria andChannelStatusIsNotNull() {
            addCriterion("channel_status is not null");
            return (Criteria) this;
        }

        public Criteria andChannelStatusEqualTo(Short value) {
            addCriterion("channel_status =", value, "channelStatus");
            return (Criteria) this;
        }

        public Criteria andChannelStatusNotEqualTo(Short value) {
            addCriterion("channel_status <>", value, "channelStatus");
            return (Criteria) this;
        }

        public Criteria andChannelStatusGreaterThan(Short value) {
            addCriterion("channel_status >", value, "channelStatus");
            return (Criteria) this;
        }

        public Criteria andChannelStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("channel_status >=", value, "channelStatus");
            return (Criteria) this;
        }

        public Criteria andChannelStatusLessThan(Short value) {
            addCriterion("channel_status <", value, "channelStatus");
            return (Criteria) this;
        }

        public Criteria andChannelStatusLessThanOrEqualTo(Short value) {
            addCriterion("channel_status <=", value, "channelStatus");
            return (Criteria) this;
        }

        public Criteria andChannelStatusIn(List<Short> values) {
            addCriterion("channel_status in", values, "channelStatus");
            return (Criteria) this;
        }

        public Criteria andChannelStatusNotIn(List<Short> values) {
            addCriterion("channel_status not in", values, "channelStatus");
            return (Criteria) this;
        }

        public Criteria andChannelStatusBetween(Short value1, Short value2) {
            addCriterion("channel_status between", value1, value2, "channelStatus");
            return (Criteria) this;
        }

        public Criteria andChannelStatusNotBetween(Short value1, Short value2) {
            addCriterion("channel_status not between", value1, value2, "channelStatus");
            return (Criteria) this;
        }

        public Criteria andBackupIsNull() {
            addCriterion("backup is null");
            return (Criteria) this;
        }

        public Criteria andBackupIsNotNull() {
            addCriterion("backup is not null");
            return (Criteria) this;
        }

        public Criteria andBackupEqualTo(String value) {
            addCriterion("backup =", value, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupNotEqualTo(String value) {
            addCriterion("backup <>", value, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupGreaterThan(String value) {
            addCriterion("backup >", value, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupGreaterThanOrEqualTo(String value) {
            addCriterion("backup >=", value, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupLessThan(String value) {
            addCriterion("backup <", value, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupLessThanOrEqualTo(String value) {
            addCriterion("backup <=", value, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupLike(String value) {
            addCriterion("backup like", value, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupNotLike(String value) {
            addCriterion("backup not like", value, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupIn(List<String> values) {
            addCriterion("backup in", values, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupNotIn(List<String> values) {
            addCriterion("backup not in", values, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupBetween(String value1, String value2) {
            addCriterion("backup between", value1, value2, "backup");
            return (Criteria) this;
        }

        public Criteria andBackupNotBetween(String value1, String value2) {
            addCriterion("backup not between", value1, value2, "backup");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIsNull() {
            addCriterion("channel_type is null");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIsNotNull() {
            addCriterion("channel_type is not null");
            return (Criteria) this;
        }

        public Criteria andChannelTypeEqualTo(Short value) {
            addCriterion("channel_type =", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotEqualTo(Short value) {
            addCriterion("channel_type <>", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeGreaterThan(Short value) {
            addCriterion("channel_type >", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("channel_type >=", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLessThan(Short value) {
            addCriterion("channel_type <", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLessThanOrEqualTo(Short value) {
            addCriterion("channel_type <=", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIn(List<Short> values) {
            addCriterion("channel_type in", values, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotIn(List<Short> values) {
            addCriterion("channel_type not in", values, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeBetween(Short value1, Short value2) {
            addCriterion("channel_type between", value1, value2, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotBetween(Short value1, Short value2) {
            addCriterion("channel_type not between", value1, value2, "channelType");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table channel_info
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
     * This class corresponds to the database table channel_info
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