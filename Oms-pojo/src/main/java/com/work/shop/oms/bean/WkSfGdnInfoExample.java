package com.work.shop.oms.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WkSfGdnInfoExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    public WkSfGdnInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_sf_gdn_info
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
     * This method corresponds to the database table wk_sf_gdn_info
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
     * This method corresponds to the database table wk_sf_gdn_info
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wk_sf_gdn_info
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
     * This class corresponds to the database table wk_sf_gdn_info
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

        public Criteria andInvoiceNoIsNull() {
            addCriterion("invoice_no is null");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoIsNotNull() {
            addCriterion("invoice_no is not null");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoEqualTo(String value) {
            addCriterion("invoice_no =", value, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoNotEqualTo(String value) {
            addCriterion("invoice_no <>", value, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoGreaterThan(String value) {
            addCriterion("invoice_no >", value, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoGreaterThanOrEqualTo(String value) {
            addCriterion("invoice_no >=", value, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoLessThan(String value) {
            addCriterion("invoice_no <", value, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoLessThanOrEqualTo(String value) {
            addCriterion("invoice_no <=", value, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoLike(String value) {
            addCriterion("invoice_no like", value, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoNotLike(String value) {
            addCriterion("invoice_no not like", value, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoIn(List<String> values) {
            addCriterion("invoice_no in", values, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoNotIn(List<String> values) {
            addCriterion("invoice_no not in", values, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoBetween(String value1, String value2) {
            addCriterion("invoice_no between", value1, value2, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoNotBetween(String value1, String value2) {
            addCriterion("invoice_no not between", value1, value2, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andSkuIsNull() {
            addCriterion("sku is null");
            return (Criteria) this;
        }

        public Criteria andSkuIsNotNull() {
            addCriterion("sku is not null");
            return (Criteria) this;
        }

        public Criteria andSkuEqualTo(String value) {
            addCriterion("sku =", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotEqualTo(String value) {
            addCriterion("sku <>", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuGreaterThan(String value) {
            addCriterion("sku >", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuGreaterThanOrEqualTo(String value) {
            addCriterion("sku >=", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuLessThan(String value) {
            addCriterion("sku <", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuLessThanOrEqualTo(String value) {
            addCriterion("sku <=", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuLike(String value) {
            addCriterion("sku like", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotLike(String value) {
            addCriterion("sku not like", value, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuIn(List<String> values) {
            addCriterion("sku in", values, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotIn(List<String> values) {
            addCriterion("sku not in", values, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuBetween(String value1, String value2) {
            addCriterion("sku between", value1, value2, "sku");
            return (Criteria) this;
        }

        public Criteria andSkuNotBetween(String value1, String value2) {
            addCriterion("sku not between", value1, value2, "sku");
            return (Criteria) this;
        }

        public Criteria andProdNumIsNull() {
            addCriterion("prod_num is null");
            return (Criteria) this;
        }

        public Criteria andProdNumIsNotNull() {
            addCriterion("prod_num is not null");
            return (Criteria) this;
        }

        public Criteria andProdNumEqualTo(Integer value) {
            addCriterion("prod_num =", value, "prodNum");
            return (Criteria) this;
        }

        public Criteria andProdNumNotEqualTo(Integer value) {
            addCriterion("prod_num <>", value, "prodNum");
            return (Criteria) this;
        }

        public Criteria andProdNumGreaterThan(Integer value) {
            addCriterion("prod_num >", value, "prodNum");
            return (Criteria) this;
        }

        public Criteria andProdNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("prod_num >=", value, "prodNum");
            return (Criteria) this;
        }

        public Criteria andProdNumLessThan(Integer value) {
            addCriterion("prod_num <", value, "prodNum");
            return (Criteria) this;
        }

        public Criteria andProdNumLessThanOrEqualTo(Integer value) {
            addCriterion("prod_num <=", value, "prodNum");
            return (Criteria) this;
        }

        public Criteria andProdNumIn(List<Integer> values) {
            addCriterion("prod_num in", values, "prodNum");
            return (Criteria) this;
        }

        public Criteria andProdNumNotIn(List<Integer> values) {
            addCriterion("prod_num not in", values, "prodNum");
            return (Criteria) this;
        }

        public Criteria andProdNumBetween(Integer value1, Integer value2) {
            addCriterion("prod_num between", value1, value2, "prodNum");
            return (Criteria) this;
        }

        public Criteria andProdNumNotBetween(Integer value1, Integer value2) {
            addCriterion("prod_num not between", value1, value2, "prodNum");
            return (Criteria) this;
        }

        public Criteria andInComIsNull() {
            addCriterion("in_com is null");
            return (Criteria) this;
        }

        public Criteria andInComIsNotNull() {
            addCriterion("in_com is not null");
            return (Criteria) this;
        }

        public Criteria andInComEqualTo(String value) {
            addCriterion("in_com =", value, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComNotEqualTo(String value) {
            addCriterion("in_com <>", value, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComGreaterThan(String value) {
            addCriterion("in_com >", value, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComGreaterThanOrEqualTo(String value) {
            addCriterion("in_com >=", value, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComLessThan(String value) {
            addCriterion("in_com <", value, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComLessThanOrEqualTo(String value) {
            addCriterion("in_com <=", value, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComLike(String value) {
            addCriterion("in_com like", value, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComNotLike(String value) {
            addCriterion("in_com not like", value, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComIn(List<String> values) {
            addCriterion("in_com in", values, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComNotIn(List<String> values) {
            addCriterion("in_com not in", values, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComBetween(String value1, String value2) {
            addCriterion("in_com between", value1, value2, "inCom");
            return (Criteria) this;
        }

        public Criteria andInComNotBetween(String value1, String value2) {
            addCriterion("in_com not between", value1, value2, "inCom");
            return (Criteria) this;
        }

        public Criteria andDepotIsNull() {
            addCriterion("depot is null");
            return (Criteria) this;
        }

        public Criteria andDepotIsNotNull() {
            addCriterion("depot is not null");
            return (Criteria) this;
        }

        public Criteria andDepotEqualTo(String value) {
            addCriterion("depot =", value, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotNotEqualTo(String value) {
            addCriterion("depot <>", value, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotGreaterThan(String value) {
            addCriterion("depot >", value, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotGreaterThanOrEqualTo(String value) {
            addCriterion("depot >=", value, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotLessThan(String value) {
            addCriterion("depot <", value, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotLessThanOrEqualTo(String value) {
            addCriterion("depot <=", value, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotLike(String value) {
            addCriterion("depot like", value, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotNotLike(String value) {
            addCriterion("depot not like", value, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotIn(List<String> values) {
            addCriterion("depot in", values, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotNotIn(List<String> values) {
            addCriterion("depot not in", values, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotBetween(String value1, String value2) {
            addCriterion("depot between", value1, value2, "depot");
            return (Criteria) this;
        }

        public Criteria andDepotNotBetween(String value1, String value2) {
            addCriterion("depot not between", value1, value2, "depot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotIsNull() {
            addCriterion("goods_depot is null");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotIsNotNull() {
            addCriterion("goods_depot is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotEqualTo(String value) {
            addCriterion("goods_depot =", value, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotNotEqualTo(String value) {
            addCriterion("goods_depot <>", value, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotGreaterThan(String value) {
            addCriterion("goods_depot >", value, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotGreaterThanOrEqualTo(String value) {
            addCriterion("goods_depot >=", value, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotLessThan(String value) {
            addCriterion("goods_depot <", value, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotLessThanOrEqualTo(String value) {
            addCriterion("goods_depot <=", value, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotLike(String value) {
            addCriterion("goods_depot like", value, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotNotLike(String value) {
            addCriterion("goods_depot not like", value, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotIn(List<String> values) {
            addCriterion("goods_depot in", values, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotNotIn(List<String> values) {
            addCriterion("goods_depot not in", values, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotBetween(String value1, String value2) {
            addCriterion("goods_depot between", value1, value2, "goodsDepot");
            return (Criteria) this;
        }

        public Criteria andGoodsDepotNotBetween(String value1, String value2) {
            addCriterion("goods_depot not between", value1, value2, "goodsDepot");
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

        public Criteria andSynFlagIsNull() {
            addCriterion("syn_flag is null");
            return (Criteria) this;
        }

        public Criteria andSynFlagIsNotNull() {
            addCriterion("syn_flag is not null");
            return (Criteria) this;
        }

        public Criteria andSynFlagEqualTo(Integer value) {
            addCriterion("syn_flag =", value, "synFlag");
            return (Criteria) this;
        }

        public Criteria andSynFlagNotEqualTo(Integer value) {
            addCriterion("syn_flag <>", value, "synFlag");
            return (Criteria) this;
        }

        public Criteria andSynFlagGreaterThan(Integer value) {
            addCriterion("syn_flag >", value, "synFlag");
            return (Criteria) this;
        }

        public Criteria andSynFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("syn_flag >=", value, "synFlag");
            return (Criteria) this;
        }

        public Criteria andSynFlagLessThan(Integer value) {
            addCriterion("syn_flag <", value, "synFlag");
            return (Criteria) this;
        }

        public Criteria andSynFlagLessThanOrEqualTo(Integer value) {
            addCriterion("syn_flag <=", value, "synFlag");
            return (Criteria) this;
        }

        public Criteria andSynFlagIn(List<Integer> values) {
            addCriterion("syn_flag in", values, "synFlag");
            return (Criteria) this;
        }

        public Criteria andSynFlagNotIn(List<Integer> values) {
            addCriterion("syn_flag not in", values, "synFlag");
            return (Criteria) this;
        }

        public Criteria andSynFlagBetween(Integer value1, Integer value2) {
            addCriterion("syn_flag between", value1, value2, "synFlag");
            return (Criteria) this;
        }

        public Criteria andSynFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("syn_flag not between", value1, value2, "synFlag");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table wk_sf_gdn_info
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
     * This class corresponds to the database table wk_sf_gdn_info
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