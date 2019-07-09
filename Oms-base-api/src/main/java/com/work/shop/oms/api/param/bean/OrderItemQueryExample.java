package com.work.shop.oms.api.param.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderItemQueryExample {

    protected String orderByClause;
    
    protected boolean distinct;
    
    protected boolean userOp;
    
    protected boolean userOg;
    
    protected boolean userOAI;
    
    protected boolean useWkUdDistribute;
    
    protected boolean isMainOrderInfo; //是否是主订单
    
	public boolean isMainOrderInfo() {
		return isMainOrderInfo;
	}

	public void setMainOrderInfo(boolean isMainOrderInfo) {
		this.isMainOrderInfo = isMainOrderInfo;
	}

	public boolean isUseWkUdDistribute() {
		return useWkUdDistribute;
	}

	public void setUseWkUdDistribute(boolean useWkUdDistribute) {
		this.useWkUdDistribute = useWkUdDistribute;
	}

	protected boolean finance;
    
    public boolean isFinance() {
		return finance;
	}

	public void setFinance(boolean finance) {
		this.finance = finance;
	}

	public boolean isUserOAI() {
		return userOAI;
	}

	public void setUserOAI(boolean userOAI) {
		this.userOAI = userOAI;
	}

	protected boolean listDataType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order_info
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbggenerated
     */
    public OrderItemQueryExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * 连接查询order_pay
     * @param userOp
     */
    public void setUserOp(boolean userOp) {
        this.userOp = userOp;
    }
    /**
     * 连接查询order_goods
     * @param userOg
     */
    public void setUserOg(boolean userOg) {
        this.userOg = userOg;
    }
    
    /**
     * 切换列表数据（true为近3个月，false为历史数据）
     * @param listDateType
     */
    public void setListDataType(boolean listDataType) {
        this.listDataType = listDataType;
    }
    
    
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
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
     * This method corresponds to the database table order_info
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
     * This method corresponds to the database table order_info
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
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
     * This class corresponds to the database table order_info
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
        
        public Criteria andOrderAndAddress() {
            addCriterion("oi.master_order_sn = oai.master_order_sn");
            return (Criteria) this;
        }
        
        public Criteria andOrderAndExtend() {
            addCriterion("oi.master_order_sn = oie.master_order_sn");
            return (Criteria) this;
        }
        
        public Criteria andOrderAndGoods() {
            addCriterion("oi.master_order_sn = mog.master_order_sn");
            return (Criteria) this;
        }
        
        public Criteria andOrderStatusNotIn(List<Byte> values) {
            addCriterion("oi.order_status not in", values, "orderStatus");
            return (Criteria) this;
        }
        
        public Criteria andIsGroupEqualTo(Integer value) {
            addCriterion("oie.is_group =", value, "isGroup");
            return (Criteria) this;
        }
        
        public Criteria andIsAdvanceEqualTo(Integer value) {
            addCriterion("oie.is_advance =", value, "isAdvance");
            return (Criteria) this;
        }
        
        public Criteria andPayIdEqualTo(Integer value) {
            addCriterion("op.pay_id =", value, " payId");
            return (Criteria) this;
        }

        public Criteria andPayTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("op.pay_time >=", value, "payTime");
            return (Criteria) this;
        }


        public Criteria andPayTimeLessThanOrEqualTo(Date value) {
            addCriterion("op.pay_time <=", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeBetween(Date value1, Date value2) {
            addCriterion("op.pay_time between", value1, value2, "payTime");
            return (Criteria) this;
        }
        
        public Criteria andShippingTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("os.shipping_time >=", value, "shippingTime");
            return (Criteria) this;
        }
        
        public Criteria andDeliveryTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("os.delivery_time >=", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andShippingTimeLessThanOrEqualTo(Date value) {
            addCriterion("os.shipping_time <=", value, "shippingTime");
            return (Criteria) this;
        }
        
        public Criteria andDeliveryTimeLessThanOrEqualTo(Date value) {
            addCriterion("os.delivery_time <=", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andShippingTimeBetween(Date value1, Date value2) {
            addCriterion("os.shipping_time between", value1, value2, "shippingTime");
            return (Criteria) this;
        }
        
        public Criteria andDeliveryTimeBetween(Date value1, Date value2) {
            addCriterion("os.delivery_time between", value1, value2, "deliveryTime");
            return (Criteria) this;
        }
        
        public Criteria andAddressLike(String value) {
            addCriterion("os.address like", value, "address");
            return (Criteria) this;
        }
        
        public Criteria andShippingIdEqualTo(Integer value) {
            addCriterion("os.shipping_id =", value, "shippingId");
            return (Criteria) this;
        }
        
        public Criteria andDistrictEqualTo(Integer value) {
            addCriterion("oai.district =", value, "district");
            return (Criteria) this;
        }
        
        public Criteria andCityEqualTo(Integer value) {
            addCriterion("oai.city =", value, "city");
            return (Criteria) this;
        }
        
        public Criteria andProvinceEqualTo(Integer value) {
            addCriterion("oai.province =", value, "province");
            return (Criteria) this;
        }
        
        public Criteria andCountryEqualTo(Integer value) {
            addCriterion("oai.country =", value, "country");
            return (Criteria) this;
        }

        public Criteria andInvoiceNoEqualTo(String value) {
            addCriterion("os.invoice_no =", value, "invoiceNo");
            return (Criteria) this;
        }

        public Criteria andOrderSnIsNull() {
            addCriterion("oi.order_sn is null");
            return (Criteria) this;
        }

        public Criteria andOrderSnIsNotNull() {
            addCriterion("oi.order_sn is not null");
            return (Criteria) this;
        }

        public Criteria andOrderSnEqualTo(String value) {
            addCriterion("od.order_sn =", value, "orderSn");
            return (Criteria) this;
        }
        
        public Criteria andMasterOrderSnEqualTo(String value) {
            addCriterion("oi.master_order_sn =", value, "masterOrderSn");
            return (Criteria) this;
        }
        
        public Criteria andMasterOrderSnLike(String value) {
            addCriterion("oi.master_order_sn like", value, "masterOrderSn");
            return (Criteria) this;
        }
        
        public Criteria andOrderSnNotEqualTo(String value) {
            addCriterion("oi.order_sn <>", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnGreaterThan(String value) {
            addCriterion("oi.order_sn >", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnGreaterThanOrEqualTo(String value) {
            addCriterion("oi.order_sn >=", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnLessThan(String value) {
            addCriterion("oi.order_sn <", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnLessThanOrEqualTo(String value) {
            addCriterion("oi.order_sn <=", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnLike(String value) {
            addCriterion("oi.order_sn like", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnNotLike(String value) {
            addCriterion("oi.order_sn not like", value, "orderSn");
            return (Criteria) this;
        }

        public Criteria andOrderSnIn(List<String> values) {
            addCriterion("oi.master_order_sn in", values, "orderSn");
            return (Criteria) this;
        }
        
        public Criteria andMasterOrderSnIn(List<String> values) {
            addCriterion("oi.master_order_sn in", values, "masterOrderSn");
            return (Criteria) this;
        }
        
        public Criteria andRefererNotInt(List<String> values) {
            addCriterion("oi.referer not in", values, "referer");
            return (Criteria) this;
        }

        public Criteria andOrderSnNotIn(List<String> values) {
            addCriterion("oi.order_sn not in", values, "orderSn");
            return (Criteria) this;
        }

        public Criteria andTransTypeEqualTo(Byte value) {
            addCriterion("oi.trans_type =", value, "transType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeEqualTo(Integer value) {
            addCriterion("oie.order_type =", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andRelatingExchangeSnEqualTo(String value) {
            addCriterion("oi.relating_exchange_sn =", value, "relatingExchangeSn");
            return (Criteria) this;
        }

        public Criteria andRelatingReturnSnEqualTo(String value) {
            addCriterion("oi.relating_return_sn =", value, "relatingReturnSn");
            return (Criteria) this;
        }

        public Criteria andRelatingRemoneySnEqualTo(String value) {
            addCriterion("oi.relating_remoney_sn =", value, "relatingRemoneySn");
            return (Criteria) this;
        }


        public Criteria andUserIdEqualTo(String value) {
            addCriterion("oi.user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("oi.user_id <>", value, "userId");
            return (Criteria) this;
        }


        public Criteria andUserNameEqualTo(String value) {
            addCriterion("oi.user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andConsigneeEqualTo(String value) {
            addCriterion("oai.consignee =", value, "consignee");
            return (Criteria) this;
        }

        public Criteria andOrderStatusEqualTo(Byte value) {
            addCriterion("oi.order_status =", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotEqualTo(Byte value) {
            addCriterion("oi.order_status <>", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusEqualTo(Byte value) {
            addCriterion("oi.pay_status =", value, "payStatus");
            return (Criteria) this;
        }

        public Criteria andPayStatusIn(List<Byte> values) {
            addCriterion("oi.pay_status in", values, "payStatus");
            return (Criteria) this;
        }

        public Criteria andShipStatusEqualTo(Byte value) {
            addCriterion("oi.ship_status =", value, "shipStatus");
            return (Criteria) this;
        }

        public Criteria andLockStatusEqualTo(Integer value) {
            addCriterion("oi.lock_status =", value, "lockStatus");
            return (Criteria) this;
        }

        public Criteria andQuestionStatusEqualTo(Integer value) {
            addCriterion("oi.question_status =", value, "questionStatus");
            return (Criteria) this;
        }

        public Criteria andQuestionStatusNotEqualTo(Integer value) {
            addCriterion("oi.question_status <>", value, "questionStatus");
            return (Criteria) this;
        }

        public Criteria andSplitStatusEqualTo(Byte value) {
            addCriterion("oi.split_status =", value, "splitStatus");
            return (Criteria) this;
        }

        public Criteria andSplitStatusNotEqualTo(Byte value) {
            addCriterion("oi.split_status <>", value, "splitStatus");
            return (Criteria) this;
        }

        public Criteria andIsAdvanceEqualTo(Byte value) {
            addCriterion("oi.is_advance =", value, "isAdvance");
            return (Criteria) this;
        }

        public Criteria andNoticeStatusEqualTo(Integer value) {
            addCriterion("oi.notice_status =", value, "noticeStatus");
            return (Criteria) this;
        }

        public Criteria andBeforeMarch() {
            addCriterion("oi.DATEDIFF(oi.`add_time`, NOW()) > - 90");
            return (Criteria) this;
        }

        public Criteria andOrderFromIsNull() {
            addCriterion("oi.order_from is null");
            return (Criteria) this;
        }

        public Criteria andOrderFromIsNotNull() {
            addCriterion("oi.order_from is not null");
            return (Criteria) this;
        }

        public Criteria andOrderFromEqualTo(String value) {
            addCriterion("oi.order_from =", value, "orderFrom");
            return (Criteria) this;
        }

        public Criteria andOrderFromIn(List<String> values) {
            addCriterion("oi.order_from in", values, "orderFrom");
            return (Criteria) this;
        }

        public Criteria andChannelCodeEqualTo(String value) {
            addCriterion("oi.channel_code =", value, "channelCode");
            return (Criteria) this;
        }
        
        public Criteria andInsteadUserIdEqualTo(String value) {
            addCriterion("oi.instead_user_id =", value, "insteadUserId");
            return (Criteria) this;
        }
        
        public Criteria andStoreCodeEqualTo(String value) {
            addCriterion("oie.shop_code =", value, "storeCode");
            return (Criteria) this;
        }
        
        public Criteria andStoreCodeIn(List<String> values) {
            addCriterion("oie.shop_code in", values, "storeCode");
            return (Criteria) this;
        }

        public Criteria andChannelCodeIn(List<String> values) {
            addCriterion("oi.channel_code in", values, "channelCode");
            return (Criteria) this;
        }

        public Criteria andAddTimeIsNull() {
            addCriterion("oi.add_time is null");
            return (Criteria) this;
        }

        public Criteria andAddTimeIsNotNull() {
            addCriterion("oi.add_time is not null");
            return (Criteria) this;
        }

        public Criteria andAddTimeEqualTo(Date value) {
            addCriterion("oi.add_time =", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeNotEqualTo(Date value) {
            addCriterion("oi.add_time <>", value, "addTime");
            return (Criteria) this;
        }
  
        public Criteria andAddTimeGreaterThan(Date value) {
            addCriterion("oi.add_time >", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("oi.add_time >=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThan(Date value) {
            addCriterion("oi.add_time <", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeLessThanOrEqualTo(Date value) {
            addCriterion("oi.add_time <=", value, "addTime");
            return (Criteria) this;
        }

        public Criteria andAddTimeBetween(Date value1, Date value2) {
            addCriterion("oi.add_time between", value1, value2, "addTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("oi.confirm_time >=", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeLessThanOrEqualTo(Date value) {
            addCriterion("oi.confirm_time <=", value, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andConfirmTimeBetween(Date value1, Date value2) {
            addCriterion("oi.confirm_time between", value1, value2, "confirmTime");
            return (Criteria) this;
        }

        public Criteria andClearTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("oi.clear_time >=", value, "clearTime");
            return (Criteria) this;
        }
        
        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("oi.create_time >=", value, "clearTime");
            return (Criteria) this;
        }

        public Criteria andClearTimeLessThanOrEqualTo(Date value) {
            addCriterion("oi.clear_time <=", value, "clearTime");
            return (Criteria) this;
        }
        
        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("oi.create_time <=", value, "clearTime");
            return (Criteria) this;
        }

        public Criteria andClearTimeBetween(Date value1, Date value2) {
            addCriterion("oi.clear_time between", value1, value2, "clearTime");
            return (Criteria) this;
        }
        
        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("oi.create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeEqualTo(Date value) {
            addCriterion("oi.notice_time =", value, "noticeTime");
            return (Criteria) this;
        }


        public Criteria andOrderOutSnIsNull() {
            addCriterion("oi.order_out_sn is null");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnIsNotNull() {
            addCriterion("oi.order_out_sn is not null");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnEqualTo(String value) {
            addCriterion("oi.order_out_sn =", value, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnNotEqualTo(String value) {
            addCriterion("oi.order_out_sn <>", value, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnGreaterThan(String value) {
            addCriterion("oi.order_out_sn >", value, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnGreaterThanOrEqualTo(String value) {
            addCriterion("oi.order_out_sn >=", value, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnLessThan(String value) {
            addCriterion("oi.order_out_sn <", value, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnLessThanOrEqualTo(String value) {
            addCriterion("oi.order_out_sn <=", value, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnLike(String value) {
            addCriterion("oi.order_out_sn like", value, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnNotLike(String value) {
            addCriterion("oi.order_out_sn not like", value, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnIn(List<String> values) {
            addCriterion("oi.order_out_sn in", values, "orderOutSn");
            return (Criteria) this;
        }
        
        public Criteria andReturnSnIn(List<String> values) {
            addCriterion("oi.relating_return_sn in", values, "relatingReturnSn");
            return (Criteria) this;
        }

        public Criteria andReturnSnNotIn(List<String> values) {
            addCriterion("oi.relating_return_sn not in", values, "relatingReturnSn");
            return (Criteria) this;
        }
        
        public Criteria andOrderOutSnNotIn(List<String> values) {
            addCriterion("oi.order_out_sn not in", values, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnBetween(String value1, String value2) {
            addCriterion("oi.order_out_sn between", value1, value2, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andOrderOutSnNotBetween(String value1, String value2) {
            addCriterion("oi.order_out_sn not between", value1, value2, "orderOutSn");
            return (Criteria) this;
        }

        public Criteria andRefererEqualTo(String value) {
            addCriterion("oi.referer =", value, "referer");
            return (Criteria) this;
        }

        public Criteria andTelEqualTo(String value) {
            addCriterion("oai.tel =", value, "tel");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("oai.mobile =", value, "mobile");
            return (Criteria) this;
        }
        
        public Criteria andMobileLike(String value) {
            addCriterion("oai.mobile like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("oi.email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andCancelCodeEqualTo(String value) {
            addCriterion("oi.cancel_code =", value, "cancelCode");
            return (Criteria) this;
        }

        public Criteria andDeliveryTypeEqualTo(String value) {
            addCriterion("os.delivery_type =", value, "deliveryType");
            return (Criteria) this;
        }
        
        public Criteria andSourceEqualTo(Integer value) {
            addCriterion("common.source =", value, "source");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table order_info
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
     * This class corresponds to the database table order_info
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
