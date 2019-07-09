package com.work.shop.oms.bean.bgchanneldb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PromotionsInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PromotionsInfoExample() {
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

        public Criteria andPromTitleIsNull() {
            addCriterion("prom_title is null");
            return (Criteria) this;
        }

        public Criteria andPromTitleIsNotNull() {
            addCriterion("prom_title is not null");
            return (Criteria) this;
        }

        public Criteria andPromTitleEqualTo(String value) {
            addCriterion("prom_title =", value, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleNotEqualTo(String value) {
            addCriterion("prom_title <>", value, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleGreaterThan(String value) {
            addCriterion("prom_title >", value, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleGreaterThanOrEqualTo(String value) {
            addCriterion("prom_title >=", value, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleLessThan(String value) {
            addCriterion("prom_title <", value, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleLessThanOrEqualTo(String value) {
            addCriterion("prom_title <=", value, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleLike(String value) {
            addCriterion("prom_title like", value, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleNotLike(String value) {
            addCriterion("prom_title not like", value, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleIn(List<String> values) {
            addCriterion("prom_title in", values, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleNotIn(List<String> values) {
            addCriterion("prom_title not in", values, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleBetween(String value1, String value2) {
            addCriterion("prom_title between", value1, value2, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromTitleNotBetween(String value1, String value2) {
            addCriterion("prom_title not between", value1, value2, "promTitle");
            return (Criteria) this;
        }

        public Criteria andPromStatusIsNull() {
            addCriterion("prom_status is null");
            return (Criteria) this;
        }

        public Criteria andPromStatusIsNotNull() {
            addCriterion("prom_status is not null");
            return (Criteria) this;
        }

        public Criteria andPromStatusEqualTo(Byte value) {
            addCriterion("prom_status =", value, "promStatus");
            return (Criteria) this;
        }

        public Criteria andPromStatusNotEqualTo(Byte value) {
            addCriterion("prom_status <>", value, "promStatus");
            return (Criteria) this;
        }

        public Criteria andPromStatusGreaterThan(Byte value) {
            addCriterion("prom_status >", value, "promStatus");
            return (Criteria) this;
        }

        public Criteria andPromStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("prom_status >=", value, "promStatus");
            return (Criteria) this;
        }

        public Criteria andPromStatusLessThan(Byte value) {
            addCriterion("prom_status <", value, "promStatus");
            return (Criteria) this;
        }

        public Criteria andPromStatusLessThanOrEqualTo(Byte value) {
            addCriterion("prom_status <=", value, "promStatus");
            return (Criteria) this;
        }

        public Criteria andPromStatusIn(List<Byte> values) {
            addCriterion("prom_status in", values, "promStatus");
            return (Criteria) this;
        }

        public Criteria andPromStatusNotIn(List<Byte> values) {
            addCriterion("prom_status not in", values, "promStatus");
            return (Criteria) this;
        }

        public Criteria andPromStatusBetween(Byte value1, Byte value2) {
            addCriterion("prom_status between", value1, value2, "promStatus");
            return (Criteria) this;
        }

        public Criteria andPromStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("prom_status not between", value1, value2, "promStatus");
            return (Criteria) this;
        }

        public Criteria andShopCodeIsNull() {
            addCriterion("shop_code is null");
            return (Criteria) this;
        }

        public Criteria andShopCodeIsNotNull() {
            addCriterion("shop_code is not null");
            return (Criteria) this;
        }

        public Criteria andShopCodeEqualTo(String value) {
            addCriterion("shop_code =", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotEqualTo(String value) {
            addCriterion("shop_code <>", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeGreaterThan(String value) {
            addCriterion("shop_code >", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeGreaterThanOrEqualTo(String value) {
            addCriterion("shop_code >=", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLessThan(String value) {
            addCriterion("shop_code <", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLessThanOrEqualTo(String value) {
            addCriterion("shop_code <=", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeLike(String value) {
            addCriterion("shop_code like", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotLike(String value) {
            addCriterion("shop_code not like", value, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeIn(List<String> values) {
            addCriterion("shop_code in", values, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotIn(List<String> values) {
            addCriterion("shop_code not in", values, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeBetween(String value1, String value2) {
            addCriterion("shop_code between", value1, value2, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopCodeNotBetween(String value1, String value2) {
            addCriterion("shop_code not between", value1, value2, "shopCode");
            return (Criteria) this;
        }

        public Criteria andShopTitleIsNull() {
            addCriterion("shop_title is null");
            return (Criteria) this;
        }

        public Criteria andShopTitleIsNotNull() {
            addCriterion("shop_title is not null");
            return (Criteria) this;
        }

        public Criteria andShopTitleEqualTo(String value) {
            addCriterion("shop_title =", value, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleNotEqualTo(String value) {
            addCriterion("shop_title <>", value, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleGreaterThan(String value) {
            addCriterion("shop_title >", value, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleGreaterThanOrEqualTo(String value) {
            addCriterion("shop_title >=", value, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleLessThan(String value) {
            addCriterion("shop_title <", value, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleLessThanOrEqualTo(String value) {
            addCriterion("shop_title <=", value, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleLike(String value) {
            addCriterion("shop_title like", value, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleNotLike(String value) {
            addCriterion("shop_title not like", value, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleIn(List<String> values) {
            addCriterion("shop_title in", values, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleNotIn(List<String> values) {
            addCriterion("shop_title not in", values, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleBetween(String value1, String value2) {
            addCriterion("shop_title between", value1, value2, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andShopTitleNotBetween(String value1, String value2) {
            addCriterion("shop_title not between", value1, value2, "shopTitle");
            return (Criteria) this;
        }

        public Criteria andPromTypeIsNull() {
            addCriterion("prom_type is null");
            return (Criteria) this;
        }

        public Criteria andPromTypeIsNotNull() {
            addCriterion("prom_type is not null");
            return (Criteria) this;
        }

        public Criteria andPromTypeEqualTo(Byte value) {
            addCriterion("prom_type =", value, "promType");
            return (Criteria) this;
        }

        public Criteria andPromTypeNotEqualTo(Byte value) {
            addCriterion("prom_type <>", value, "promType");
            return (Criteria) this;
        }

        public Criteria andPromTypeGreaterThan(Byte value) {
            addCriterion("prom_type >", value, "promType");
            return (Criteria) this;
        }

        public Criteria andPromTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("prom_type >=", value, "promType");
            return (Criteria) this;
        }

        public Criteria andPromTypeLessThan(Byte value) {
            addCriterion("prom_type <", value, "promType");
            return (Criteria) this;
        }

        public Criteria andPromTypeLessThanOrEqualTo(Byte value) {
            addCriterion("prom_type <=", value, "promType");
            return (Criteria) this;
        }

        public Criteria andPromTypeIn(List<Byte> values) {
            addCriterion("prom_type in", values, "promType");
            return (Criteria) this;
        }

        public Criteria andPromTypeNotIn(List<Byte> values) {
            addCriterion("prom_type not in", values, "promType");
            return (Criteria) this;
        }

        public Criteria andPromTypeBetween(Byte value1, Byte value2) {
            addCriterion("prom_type between", value1, value2, "promType");
            return (Criteria) this;
        }

        public Criteria andPromTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("prom_type not between", value1, value2, "promType");
            return (Criteria) this;
        }

        public Criteria andBeginTimeIsNull() {
            addCriterion("begin_time is null");
            return (Criteria) this;
        }

        public Criteria andBeginTimeIsNotNull() {
            addCriterion("begin_time is not null");
            return (Criteria) this;
        }

        public Criteria andBeginTimeEqualTo(Date value) {
            addCriterion("begin_time =", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeNotEqualTo(Date value) {
            addCriterion("begin_time <>", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeGreaterThan(Date value) {
            addCriterion("begin_time >", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("begin_time >=", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeLessThan(Date value) {
            addCriterion("begin_time <", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeLessThanOrEqualTo(Date value) {
            addCriterion("begin_time <=", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeIn(List<Date> values) {
            addCriterion("begin_time in", values, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeNotIn(List<Date> values) {
            addCriterion("begin_time not in", values, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeBetween(Date value1, Date value2) {
            addCriterion("begin_time between", value1, value2, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeNotBetween(Date value1, Date value2) {
            addCriterion("begin_time not between", value1, value2, "beginTime");
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