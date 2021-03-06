package com.work.shop.oms.bean;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderLineExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    public PurchaseOrderLineExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase_order_line
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
     * This method corresponds to the database table purchase_order_line
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
     * This method corresponds to the database table purchase_order_line
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table purchase_order_line
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
     * This class corresponds to the database table purchase_order_line
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

        public Criteria andPurchaseOrderCodeIsNull() {
            addCriterion("purchase_order_code is null");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeIsNotNull() {
            addCriterion("purchase_order_code is not null");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeEqualTo(String value) {
            addCriterion("purchase_order_code =", value, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeNotEqualTo(String value) {
            addCriterion("purchase_order_code <>", value, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeGreaterThan(String value) {
            addCriterion("purchase_order_code >", value, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeGreaterThanOrEqualTo(String value) {
            addCriterion("purchase_order_code >=", value, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeLessThan(String value) {
            addCriterion("purchase_order_code <", value, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeLessThanOrEqualTo(String value) {
            addCriterion("purchase_order_code <=", value, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeLike(String value) {
            addCriterion("purchase_order_code like", value, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeNotLike(String value) {
            addCriterion("purchase_order_code not like", value, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeIn(List<String> values) {
            addCriterion("purchase_order_code in", values, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeNotIn(List<String> values) {
            addCriterion("purchase_order_code not in", values, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeBetween(String value1, String value2) {
            addCriterion("purchase_order_code between", value1, value2, "purchaseOrderCode");
            return (Criteria) this;
        }

        public Criteria andPurchaseOrderCodeNotBetween(String value1, String value2) {
            addCriterion("purchase_order_code not between", value1, value2, "purchaseOrderCode");
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

        public Criteria andMasterOrderSnIsNull() {
            addCriterion("master_order_sn is null");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnIsNotNull() {
            addCriterion("master_order_sn is not null");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnEqualTo(String value) {
            addCriterion("master_order_sn =", value, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnNotEqualTo(String value) {
            addCriterion("master_order_sn <>", value, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnGreaterThan(String value) {
            addCriterion("master_order_sn >", value, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnGreaterThanOrEqualTo(String value) {
            addCriterion("master_order_sn >=", value, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnLessThan(String value) {
            addCriterion("master_order_sn <", value, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnLessThanOrEqualTo(String value) {
            addCriterion("master_order_sn <=", value, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnLike(String value) {
            addCriterion("master_order_sn like", value, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnNotLike(String value) {
            addCriterion("master_order_sn not like", value, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnIn(List<String> values) {
            addCriterion("master_order_sn in", values, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnNotIn(List<String> values) {
            addCriterion("master_order_sn not in", values, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnBetween(String value1, String value2) {
            addCriterion("master_order_sn between", value1, value2, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andMasterOrderSnNotBetween(String value1, String value2) {
            addCriterion("master_order_sn not between", value1, value2, "masterOrderSn");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIsNull() {
            addCriterion("goods_name is null");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIsNotNull() {
            addCriterion("goods_name is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsNameEqualTo(String value) {
            addCriterion("goods_name =", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotEqualTo(String value) {
            addCriterion("goods_name <>", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameGreaterThan(String value) {
            addCriterion("goods_name >", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameGreaterThanOrEqualTo(String value) {
            addCriterion("goods_name >=", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLessThan(String value) {
            addCriterion("goods_name <", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLessThanOrEqualTo(String value) {
            addCriterion("goods_name <=", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameLike(String value) {
            addCriterion("goods_name like", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotLike(String value) {
            addCriterion("goods_name not like", value, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameIn(List<String> values) {
            addCriterion("goods_name in", values, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotIn(List<String> values) {
            addCriterion("goods_name not in", values, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameBetween(String value1, String value2) {
            addCriterion("goods_name between", value1, value2, "goodsName");
            return (Criteria) this;
        }

        public Criteria andGoodsNameNotBetween(String value1, String value2) {
            addCriterion("goods_name not between", value1, value2, "goodsName");
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

        public Criteria andGoodsNumberIsNull() {
            addCriterion("goods_number is null");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberIsNotNull() {
            addCriterion("goods_number is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberEqualTo(Short value) {
            addCriterion("goods_number =", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberNotEqualTo(Short value) {
            addCriterion("goods_number <>", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberGreaterThan(Short value) {
            addCriterion("goods_number >", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberGreaterThanOrEqualTo(Short value) {
            addCriterion("goods_number >=", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberLessThan(Short value) {
            addCriterion("goods_number <", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberLessThanOrEqualTo(Short value) {
            addCriterion("goods_number <=", value, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberIn(List<Short> values) {
            addCriterion("goods_number in", values, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberNotIn(List<Short> values) {
            addCriterion("goods_number not in", values, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberBetween(Short value1, Short value2) {
            addCriterion("goods_number between", value1, value2, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsNumberNotBetween(Short value1, Short value2) {
            addCriterion("goods_number not between", value1, value2, "goodsNumber");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameIsNull() {
            addCriterion("goods_size_name is null");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameIsNotNull() {
            addCriterion("goods_size_name is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameEqualTo(String value) {
            addCriterion("goods_size_name =", value, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameNotEqualTo(String value) {
            addCriterion("goods_size_name <>", value, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameGreaterThan(String value) {
            addCriterion("goods_size_name >", value, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameGreaterThanOrEqualTo(String value) {
            addCriterion("goods_size_name >=", value, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameLessThan(String value) {
            addCriterion("goods_size_name <", value, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameLessThanOrEqualTo(String value) {
            addCriterion("goods_size_name <=", value, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameLike(String value) {
            addCriterion("goods_size_name like", value, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameNotLike(String value) {
            addCriterion("goods_size_name not like", value, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameIn(List<String> values) {
            addCriterion("goods_size_name in", values, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameNotIn(List<String> values) {
            addCriterion("goods_size_name not in", values, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameBetween(String value1, String value2) {
            addCriterion("goods_size_name between", value1, value2, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsSizeNameNotBetween(String value1, String value2) {
            addCriterion("goods_size_name not between", value1, value2, "goodsSizeName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameIsNull() {
            addCriterion("goods_color_name is null");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameIsNotNull() {
            addCriterion("goods_color_name is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameEqualTo(String value) {
            addCriterion("goods_color_name =", value, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameNotEqualTo(String value) {
            addCriterion("goods_color_name <>", value, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameGreaterThan(String value) {
            addCriterion("goods_color_name >", value, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameGreaterThanOrEqualTo(String value) {
            addCriterion("goods_color_name >=", value, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameLessThan(String value) {
            addCriterion("goods_color_name <", value, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameLessThanOrEqualTo(String value) {
            addCriterion("goods_color_name <=", value, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameLike(String value) {
            addCriterion("goods_color_name like", value, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameNotLike(String value) {
            addCriterion("goods_color_name not like", value, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameIn(List<String> values) {
            addCriterion("goods_color_name in", values, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameNotIn(List<String> values) {
            addCriterion("goods_color_name not in", values, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameBetween(String value1, String value2) {
            addCriterion("goods_color_name between", value1, value2, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsColorNameNotBetween(String value1, String value2) {
            addCriterion("goods_color_name not between", value1, value2, "goodsColorName");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbIsNull() {
            addCriterion("goods_thumb is null");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbIsNotNull() {
            addCriterion("goods_thumb is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbEqualTo(String value) {
            addCriterion("goods_thumb =", value, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbNotEqualTo(String value) {
            addCriterion("goods_thumb <>", value, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbGreaterThan(String value) {
            addCriterion("goods_thumb >", value, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbGreaterThanOrEqualTo(String value) {
            addCriterion("goods_thumb >=", value, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbLessThan(String value) {
            addCriterion("goods_thumb <", value, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbLessThanOrEqualTo(String value) {
            addCriterion("goods_thumb <=", value, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbLike(String value) {
            addCriterion("goods_thumb like", value, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbNotLike(String value) {
            addCriterion("goods_thumb not like", value, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbIn(List<String> values) {
            addCriterion("goods_thumb in", values, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbNotIn(List<String> values) {
            addCriterion("goods_thumb not in", values, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbBetween(String value1, String value2) {
            addCriterion("goods_thumb between", value1, value2, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andGoodsThumbNotBetween(String value1, String value2) {
            addCriterion("goods_thumb not between", value1, value2, "goodsThumb");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeIsNull() {
            addCriterion("supplier_code is null");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeIsNotNull() {
            addCriterion("supplier_code is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeEqualTo(String value) {
            addCriterion("supplier_code =", value, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeNotEqualTo(String value) {
            addCriterion("supplier_code <>", value, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeGreaterThan(String value) {
            addCriterion("supplier_code >", value, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_code >=", value, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeLessThan(String value) {
            addCriterion("supplier_code <", value, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeLessThanOrEqualTo(String value) {
            addCriterion("supplier_code <=", value, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeLike(String value) {
            addCriterion("supplier_code like", value, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeNotLike(String value) {
            addCriterion("supplier_code not like", value, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeIn(List<String> values) {
            addCriterion("supplier_code in", values, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeNotIn(List<String> values) {
            addCriterion("supplier_code not in", values, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeBetween(String value1, String value2) {
            addCriterion("supplier_code between", value1, value2, "supplierCode");
            return (Criteria) this;
        }

        public Criteria andSupplierCodeNotBetween(String value1, String value2) {
            addCriterion("supplier_code not between", value1, value2, "supplierCode");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table purchase_order_line
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
     * This class corresponds to the database table purchase_order_line
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