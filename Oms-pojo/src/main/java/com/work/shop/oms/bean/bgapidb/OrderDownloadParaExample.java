package com.work.shop.oms.bean.bgapidb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDownloadParaExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrderDownloadParaExample() {
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andChannelCodeIsNull() {
            addCriterion("channel_code is null");
            return (Criteria) this;
        }

        public Criteria andChannelCodeIsNotNull() {
            addCriterion("channel_code is not null");
            return (Criteria) this;
        }

        public Criteria andChannelCodeEqualTo(String value) {
            addCriterion("channel_code =", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotEqualTo(String value) {
            addCriterion("channel_code <>", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeGreaterThan(String value) {
            addCriterion("channel_code >", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeGreaterThanOrEqualTo(String value) {
            addCriterion("channel_code >=", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeLessThan(String value) {
            addCriterion("channel_code <", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeLessThanOrEqualTo(String value) {
            addCriterion("channel_code <=", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeLike(String value) {
            addCriterion("channel_code like", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotLike(String value) {
            addCriterion("channel_code not like", value, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeIn(List<String> values) {
            addCriterion("channel_code in", values, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotIn(List<String> values) {
            addCriterion("channel_code not in", values, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeBetween(String value1, String value2) {
            addCriterion("channel_code between", value1, value2, "channelCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeNotBetween(String value1, String value2) {
            addCriterion("channel_code not between", value1, value2, "channelCode");
            return (Criteria) this;
        }

        public Criteria andShortNameIsNull() {
            addCriterion("short_name is null");
            return (Criteria) this;
        }

        public Criteria andShortNameIsNotNull() {
            addCriterion("short_name is not null");
            return (Criteria) this;
        }

        public Criteria andShortNameEqualTo(String value) {
            addCriterion("short_name =", value, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameNotEqualTo(String value) {
            addCriterion("short_name <>", value, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameGreaterThan(String value) {
            addCriterion("short_name >", value, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameGreaterThanOrEqualTo(String value) {
            addCriterion("short_name >=", value, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameLessThan(String value) {
            addCriterion("short_name <", value, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameLessThanOrEqualTo(String value) {
            addCriterion("short_name <=", value, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameLike(String value) {
            addCriterion("short_name like", value, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameNotLike(String value) {
            addCriterion("short_name not like", value, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameIn(List<String> values) {
            addCriterion("short_name in", values, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameNotIn(List<String> values) {
            addCriterion("short_name not in", values, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameBetween(String value1, String value2) {
            addCriterion("short_name between", value1, value2, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortNameNotBetween(String value1, String value2) {
            addCriterion("short_name not between", value1, value2, "shortName");
            return (Criteria) this;
        }

        public Criteria andShortTextIsNull() {
            addCriterion("short_text is null");
            return (Criteria) this;
        }

        public Criteria andShortTextIsNotNull() {
            addCriterion("short_text is not null");
            return (Criteria) this;
        }

        public Criteria andShortTextEqualTo(String value) {
            addCriterion("short_text =", value, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextNotEqualTo(String value) {
            addCriterion("short_text <>", value, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextGreaterThan(String value) {
            addCriterion("short_text >", value, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextGreaterThanOrEqualTo(String value) {
            addCriterion("short_text >=", value, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextLessThan(String value) {
            addCriterion("short_text <", value, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextLessThanOrEqualTo(String value) {
            addCriterion("short_text <=", value, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextLike(String value) {
            addCriterion("short_text like", value, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextNotLike(String value) {
            addCriterion("short_text not like", value, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextIn(List<String> values) {
            addCriterion("short_text in", values, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextNotIn(List<String> values) {
            addCriterion("short_text not in", values, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextBetween(String value1, String value2) {
            addCriterion("short_text between", value1, value2, "shortText");
            return (Criteria) this;
        }

        public Criteria andShortTextNotBetween(String value1, String value2) {
            addCriterion("short_text not between", value1, value2, "shortText");
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

        public Criteria andLastupdatetimeIsNull() {
            addCriterion("lastUpdateTime is null");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeIsNotNull() {
            addCriterion("lastUpdateTime is not null");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeEqualTo(Long value) {
            addCriterion("lastUpdateTime =", value, "lastupdatetime");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeNotEqualTo(Long value) {
            addCriterion("lastUpdateTime <>", value, "lastupdatetime");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeGreaterThan(Long value) {
            addCriterion("lastUpdateTime >", value, "lastupdatetime");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeGreaterThanOrEqualTo(Long value) {
            addCriterion("lastUpdateTime >=", value, "lastupdatetime");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeLessThan(Long value) {
            addCriterion("lastUpdateTime <", value, "lastupdatetime");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeLessThanOrEqualTo(Long value) {
            addCriterion("lastUpdateTime <=", value, "lastupdatetime");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeIn(List<Long> values) {
            addCriterion("lastUpdateTime in", values, "lastupdatetime");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeNotIn(List<Long> values) {
            addCriterion("lastUpdateTime not in", values, "lastupdatetime");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeBetween(Long value1, Long value2) {
            addCriterion("lastUpdateTime between", value1, value2, "lastupdatetime");
            return (Criteria) this;
        }

        public Criteria andLastupdatetimeNotBetween(Long value1, Long value2) {
            addCriterion("lastUpdateTime not between", value1, value2, "lastupdatetime");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetIsNull() {
            addCriterion("defaultHourOffset is null");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetIsNotNull() {
            addCriterion("defaultHourOffset is not null");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetEqualTo(Integer value) {
            addCriterion("defaultHourOffset =", value, "defaulthouroffset");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetNotEqualTo(Integer value) {
            addCriterion("defaultHourOffset <>", value, "defaulthouroffset");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetGreaterThan(Integer value) {
            addCriterion("defaultHourOffset >", value, "defaulthouroffset");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetGreaterThanOrEqualTo(Integer value) {
            addCriterion("defaultHourOffset >=", value, "defaulthouroffset");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetLessThan(Integer value) {
            addCriterion("defaultHourOffset <", value, "defaulthouroffset");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetLessThanOrEqualTo(Integer value) {
            addCriterion("defaultHourOffset <=", value, "defaulthouroffset");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetIn(List<Integer> values) {
            addCriterion("defaultHourOffset in", values, "defaulthouroffset");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetNotIn(List<Integer> values) {
            addCriterion("defaultHourOffset not in", values, "defaulthouroffset");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetBetween(Integer value1, Integer value2) {
            addCriterion("defaultHourOffset between", value1, value2, "defaulthouroffset");
            return (Criteria) this;
        }

        public Criteria andDefaulthouroffsetNotBetween(Integer value1, Integer value2) {
            addCriterion("defaultHourOffset not between", value1, value2, "defaulthouroffset");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetIsNull() {
            addCriterion("secPreOffset is null");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetIsNotNull() {
            addCriterion("secPreOffset is not null");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetEqualTo(Integer value) {
            addCriterion("secPreOffset =", value, "secpreoffset");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetNotEqualTo(Integer value) {
            addCriterion("secPreOffset <>", value, "secpreoffset");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetGreaterThan(Integer value) {
            addCriterion("secPreOffset >", value, "secpreoffset");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetGreaterThanOrEqualTo(Integer value) {
            addCriterion("secPreOffset >=", value, "secpreoffset");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetLessThan(Integer value) {
            addCriterion("secPreOffset <", value, "secpreoffset");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetLessThanOrEqualTo(Integer value) {
            addCriterion("secPreOffset <=", value, "secpreoffset");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetIn(List<Integer> values) {
            addCriterion("secPreOffset in", values, "secpreoffset");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetNotIn(List<Integer> values) {
            addCriterion("secPreOffset not in", values, "secpreoffset");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetBetween(Integer value1, Integer value2) {
            addCriterion("secPreOffset between", value1, value2, "secpreoffset");
            return (Criteria) this;
        }

        public Criteria andSecpreoffsetNotBetween(Integer value1, Integer value2) {
            addCriterion("secPreOffset not between", value1, value2, "secpreoffset");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetIsNull() {
            addCriterion("secAfterOffset is null");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetIsNotNull() {
            addCriterion("secAfterOffset is not null");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetEqualTo(Integer value) {
            addCriterion("secAfterOffset =", value, "secafteroffset");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetNotEqualTo(Integer value) {
            addCriterion("secAfterOffset <>", value, "secafteroffset");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetGreaterThan(Integer value) {
            addCriterion("secAfterOffset >", value, "secafteroffset");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetGreaterThanOrEqualTo(Integer value) {
            addCriterion("secAfterOffset >=", value, "secafteroffset");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetLessThan(Integer value) {
            addCriterion("secAfterOffset <", value, "secafteroffset");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetLessThanOrEqualTo(Integer value) {
            addCriterion("secAfterOffset <=", value, "secafteroffset");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetIn(List<Integer> values) {
            addCriterion("secAfterOffset in", values, "secafteroffset");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetNotIn(List<Integer> values) {
            addCriterion("secAfterOffset not in", values, "secafteroffset");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetBetween(Integer value1, Integer value2) {
            addCriterion("secAfterOffset between", value1, value2, "secafteroffset");
            return (Criteria) this;
        }

        public Criteria andSecafteroffsetNotBetween(Integer value1, Integer value2) {
            addCriterion("secAfterOffset not between", value1, value2, "secafteroffset");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersIsNull() {
            addCriterion("threadNumbers is null");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersIsNotNull() {
            addCriterion("threadNumbers is not null");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersEqualTo(Integer value) {
            addCriterion("threadNumbers =", value, "threadnumbers");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersNotEqualTo(Integer value) {
            addCriterion("threadNumbers <>", value, "threadnumbers");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersGreaterThan(Integer value) {
            addCriterion("threadNumbers >", value, "threadnumbers");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersGreaterThanOrEqualTo(Integer value) {
            addCriterion("threadNumbers >=", value, "threadnumbers");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersLessThan(Integer value) {
            addCriterion("threadNumbers <", value, "threadnumbers");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersLessThanOrEqualTo(Integer value) {
            addCriterion("threadNumbers <=", value, "threadnumbers");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersIn(List<Integer> values) {
            addCriterion("threadNumbers in", values, "threadnumbers");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersNotIn(List<Integer> values) {
            addCriterion("threadNumbers not in", values, "threadnumbers");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersBetween(Integer value1, Integer value2) {
            addCriterion("threadNumbers between", value1, value2, "threadnumbers");
            return (Criteria) this;
        }

        public Criteria andThreadnumbersNotBetween(Integer value1, Integer value2) {
            addCriterion("threadNumbers not between", value1, value2, "threadnumbers");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadIsNull() {
            addCriterion("tradeNumPerThread is null");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadIsNotNull() {
            addCriterion("tradeNumPerThread is not null");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadEqualTo(Integer value) {
            addCriterion("tradeNumPerThread =", value, "tradenumperthread");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadNotEqualTo(Integer value) {
            addCriterion("tradeNumPerThread <>", value, "tradenumperthread");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadGreaterThan(Integer value) {
            addCriterion("tradeNumPerThread >", value, "tradenumperthread");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadGreaterThanOrEqualTo(Integer value) {
            addCriterion("tradeNumPerThread >=", value, "tradenumperthread");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadLessThan(Integer value) {
            addCriterion("tradeNumPerThread <", value, "tradenumperthread");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadLessThanOrEqualTo(Integer value) {
            addCriterion("tradeNumPerThread <=", value, "tradenumperthread");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadIn(List<Integer> values) {
            addCriterion("tradeNumPerThread in", values, "tradenumperthread");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadNotIn(List<Integer> values) {
            addCriterion("tradeNumPerThread not in", values, "tradenumperthread");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadBetween(Integer value1, Integer value2) {
            addCriterion("tradeNumPerThread between", value1, value2, "tradenumperthread");
            return (Criteria) this;
        }

        public Criteria andTradenumperthreadNotBetween(Integer value1, Integer value2) {
            addCriterion("tradeNumPerThread not between", value1, value2, "tradenumperthread");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiIsNull() {
            addCriterion("tradeNumPerApi is null");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiIsNotNull() {
            addCriterion("tradeNumPerApi is not null");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiEqualTo(Integer value) {
            addCriterion("tradeNumPerApi =", value, "tradenumperapi");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiNotEqualTo(Integer value) {
            addCriterion("tradeNumPerApi <>", value, "tradenumperapi");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiGreaterThan(Integer value) {
            addCriterion("tradeNumPerApi >", value, "tradenumperapi");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiGreaterThanOrEqualTo(Integer value) {
            addCriterion("tradeNumPerApi >=", value, "tradenumperapi");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiLessThan(Integer value) {
            addCriterion("tradeNumPerApi <", value, "tradenumperapi");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiLessThanOrEqualTo(Integer value) {
            addCriterion("tradeNumPerApi <=", value, "tradenumperapi");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiIn(List<Integer> values) {
            addCriterion("tradeNumPerApi in", values, "tradenumperapi");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiNotIn(List<Integer> values) {
            addCriterion("tradeNumPerApi not in", values, "tradenumperapi");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiBetween(Integer value1, Integer value2) {
            addCriterion("tradeNumPerApi between", value1, value2, "tradenumperapi");
            return (Criteria) this;
        }

        public Criteria andTradenumperapiNotBetween(Integer value1, Integer value2) {
            addCriterion("tradeNumPerApi not between", value1, value2, "tradenumperapi");
            return (Criteria) this;
        }

        public Criteria andUseQueueIsNull() {
            addCriterion("use_queue is null");
            return (Criteria) this;
        }

        public Criteria andUseQueueIsNotNull() {
            addCriterion("use_queue is not null");
            return (Criteria) this;
        }

        public Criteria andUseQueueEqualTo(Integer value) {
            addCriterion("use_queue =", value, "useQueue");
            return (Criteria) this;
        }

        public Criteria andUseQueueNotEqualTo(Integer value) {
            addCriterion("use_queue <>", value, "useQueue");
            return (Criteria) this;
        }

        public Criteria andUseQueueGreaterThan(Integer value) {
            addCriterion("use_queue >", value, "useQueue");
            return (Criteria) this;
        }

        public Criteria andUseQueueGreaterThanOrEqualTo(Integer value) {
            addCriterion("use_queue >=", value, "useQueue");
            return (Criteria) this;
        }

        public Criteria andUseQueueLessThan(Integer value) {
            addCriterion("use_queue <", value, "useQueue");
            return (Criteria) this;
        }

        public Criteria andUseQueueLessThanOrEqualTo(Integer value) {
            addCriterion("use_queue <=", value, "useQueue");
            return (Criteria) this;
        }

        public Criteria andUseQueueIn(List<Integer> values) {
            addCriterion("use_queue in", values, "useQueue");
            return (Criteria) this;
        }

        public Criteria andUseQueueNotIn(List<Integer> values) {
            addCriterion("use_queue not in", values, "useQueue");
            return (Criteria) this;
        }

        public Criteria andUseQueueBetween(Integer value1, Integer value2) {
            addCriterion("use_queue between", value1, value2, "useQueue");
            return (Criteria) this;
        }

        public Criteria andUseQueueNotBetween(Integer value1, Integer value2) {
            addCriterion("use_queue not between", value1, value2, "useQueue");
            return (Criteria) this;
        }

        public Criteria andRedisOrderIsNull() {
            addCriterion("redis_order is null");
            return (Criteria) this;
        }

        public Criteria andRedisOrderIsNotNull() {
            addCriterion("redis_order is not null");
            return (Criteria) this;
        }

        public Criteria andRedisOrderEqualTo(Integer value) {
            addCriterion("redis_order =", value, "redisOrder");
            return (Criteria) this;
        }

        public Criteria andRedisOrderNotEqualTo(Integer value) {
            addCriterion("redis_order <>", value, "redisOrder");
            return (Criteria) this;
        }

        public Criteria andRedisOrderGreaterThan(Integer value) {
            addCriterion("redis_order >", value, "redisOrder");
            return (Criteria) this;
        }

        public Criteria andRedisOrderGreaterThanOrEqualTo(Integer value) {
            addCriterion("redis_order >=", value, "redisOrder");
            return (Criteria) this;
        }

        public Criteria andRedisOrderLessThan(Integer value) {
            addCriterion("redis_order <", value, "redisOrder");
            return (Criteria) this;
        }

        public Criteria andRedisOrderLessThanOrEqualTo(Integer value) {
            addCriterion("redis_order <=", value, "redisOrder");
            return (Criteria) this;
        }

        public Criteria andRedisOrderIn(List<Integer> values) {
            addCriterion("redis_order in", values, "redisOrder");
            return (Criteria) this;
        }

        public Criteria andRedisOrderNotIn(List<Integer> values) {
            addCriterion("redis_order not in", values, "redisOrder");
            return (Criteria) this;
        }

        public Criteria andRedisOrderBetween(Integer value1, Integer value2) {
            addCriterion("redis_order between", value1, value2, "redisOrder");
            return (Criteria) this;
        }

        public Criteria andRedisOrderNotBetween(Integer value1, Integer value2) {
            addCriterion("redis_order not between", value1, value2, "redisOrder");
            return (Criteria) this;
        }

        public Criteria andDown2convertIsNull() {
            addCriterion("down2convert is null");
            return (Criteria) this;
        }

        public Criteria andDown2convertIsNotNull() {
            addCriterion("down2convert is not null");
            return (Criteria) this;
        }

        public Criteria andDown2convertEqualTo(Integer value) {
            addCriterion("down2convert =", value, "down2convert");
            return (Criteria) this;
        }

        public Criteria andDown2convertNotEqualTo(Integer value) {
            addCriterion("down2convert <>", value, "down2convert");
            return (Criteria) this;
        }

        public Criteria andDown2convertGreaterThan(Integer value) {
            addCriterion("down2convert >", value, "down2convert");
            return (Criteria) this;
        }

        public Criteria andDown2convertGreaterThanOrEqualTo(Integer value) {
            addCriterion("down2convert >=", value, "down2convert");
            return (Criteria) this;
        }

        public Criteria andDown2convertLessThan(Integer value) {
            addCriterion("down2convert <", value, "down2convert");
            return (Criteria) this;
        }

        public Criteria andDown2convertLessThanOrEqualTo(Integer value) {
            addCriterion("down2convert <=", value, "down2convert");
            return (Criteria) this;
        }

        public Criteria andDown2convertIn(List<Integer> values) {
            addCriterion("down2convert in", values, "down2convert");
            return (Criteria) this;
        }

        public Criteria andDown2convertNotIn(List<Integer> values) {
            addCriterion("down2convert not in", values, "down2convert");
            return (Criteria) this;
        }

        public Criteria andDown2convertBetween(Integer value1, Integer value2) {
            addCriterion("down2convert between", value1, value2, "down2convert");
            return (Criteria) this;
        }

        public Criteria andDown2convertNotBetween(Integer value1, Integer value2) {
            addCriterion("down2convert not between", value1, value2, "down2convert");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNull() {
            addCriterion("updateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("updateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(Date value) {
            addCriterion("updateTime =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(Date value) {
            addCriterion("updateTime <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(Date value) {
            addCriterion("updateTime >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updateTime >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(Date value) {
            addCriterion("updateTime <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(Date value) {
            addCriterion("updateTime <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<Date> values) {
            addCriterion("updateTime in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<Date> values) {
            addCriterion("updateTime not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(Date value1, Date value2) {
            addCriterion("updateTime between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(Date value1, Date value2) {
            addCriterion("updateTime not between", value1, value2, "updatetime");
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