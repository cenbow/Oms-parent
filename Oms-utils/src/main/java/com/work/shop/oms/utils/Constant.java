package com.work.shop.oms.utils;


/**
 * 常量设置类
 * 
 * @author
 * 
 */
public class Constant {
	/**
	 * 默认的每夜显示记录数
	 */
	public static final int RECORD_NUM_PER_PAGE = 15;

	/**
	 * 一般订单
	 */
	public static final String NORMAL = "normal";
	/**
	 * 问题单
	 */
	public static final String QUESTION = "question";
	/**
	 * 物流问题单
	 */
	public static final String LOGISTICS_QUESTION = "logistics_question";
	/**
	 * 超时单
	 */
	public static final String TIMEOUT = "timeout";
	/**
	 * 挂起单
	 */
	public static final String SUSPEND = "suspend";
	/**
	 * 合并单
	 */
	public static final String COMBINE = "combine";
	/**
	 * 客服待处理单
	 */
	public static final String PENDING = "pending";
	/**
	 * 待付款单
	 */
	public static final String GATHERING = "gathering";
	/**
	 * 待结算单
	 */
	public static final String ACCOUNT = "account";

	public static final String SEARCH_MODEL_SIMPLE = "simple";
	public static final String SEARCH_MODEL_DETAIL = "detail";

	/**
	 * YesOrNo
	 */
	public static final Integer YESORNO_YES = 1;

	public static final Integer YESORNO_NO = 0;

	/**
	 * 否
	 */
	public static final int OS_NO = 0;

	/**
	 * 是
	 */
	public static final int OS_YES = 1;

	/**
	 * 失败
	 */
	public static final int OS_ERROR = -1;

	// 0失败
	public static final String OS_STR_NO = "0";
	// 1 成功
	public static final String OS_STR_YES = "1";

	/**
	 * 合并支付单号
	 */
	public static final String ORDER_PAY_MFK = "MFK";

	/**
	 * 订单锁定状态
	 */
	public static final int OS_LOCK_NULL = 0; // 订单编号不能为空！
	public static final int OS_LOCK_SUCCESS = 1; // 顾客锁定订单成功！
	public static final int OS_LOCK_ERROR = -1; // 内部处理发生异常！
	public static final int OS_LOCK_NOT_EXIST = -100; // 订单不存在或异常！
	public static final int OS_LOCK_CLOSE = -200; // 订单已关闭
	public static final int OS_LOCK_CANNEL = -300; // 订单已取消
	public static final int OS_LOCK_VAIN = -400; // 订单已无效
	public static final int OS_LOCK_MERGE = -500; // 订单已合并
	public static final int OS_LOCK_SPLIT = -600; // 订单已拆分
	public static final int OS_LOCK_OTHER = -700; // 订单被他人锁定
	public static final int OS_LOCK_ALREADY = -800; // 本人已锁定
	public static final int OS_LOCK_PAY = -900; // 订单已付款
	public static final int OS_LOCK_OVER = -1000; // 订单已结算

	public static final int OS_HAS_OUT_ORDER_SN = -1; /* 1 是 */
	/**
	 * 顾客付款时锁定订单ID号
	 */
	public static final int CUSTOMER_PAY_LOCK_ID = 10000;

	/**
	 * system 字符串，系统日志用户名
	 */
	public static final String OS_STRING_SYSTEM = "system";

	/**
	 * 平台官方网店渠道编码
	 */
	public static final String OS_REFERER_YHJ = "YHJ";

	/**
	 * POS
	 */
	public static final String OS_POS = "POS";

	/**
	 * OS订单总金额低于指定值
	 */

	public static final String OS_ORDER_MONEY_QUESTION = "351";

	public static final String OS_ORDER_GOODS_MONEY_QUESTION = "352";

	/**
	 * 品牌特卖
	 */
	public static final String OS_ORDER_BRAND_ON_SALE_QUESTION = "997";

	/**
	 * 平台币虚拟商品
	 */
	public static final String OS_ORDER_BANGGO_MONEY_QUESTION = "321";

	/**
	 * 免邮卡
	 */
	public static final String OS_ORDER_FREE_SHIPPING_CARD_QUESTION = "322";

	/**
	 * OS订单总金额最低值（含邮费）
	 */
	public static final Double OS_ORDER_MONEY_VALUE = 20.0;

	public static final int OS_CLOSED = 3; /* 3 已关闭 */
	public static final int OS_CANCEL = 4; /* 4 已取消 */
	public static final int OS_INVALID = 5; /* 5 已无效 */
	public static final int OS_FINISHED = 6; /* 6 已完成 */
	public static final int OS_INEXISTENCE = 7; /* 7 不在近三个月的数据信息中 */
	public static final String OS_DAY_FROM = ConfigCenter.getProperty("OS.DAY.FROM");

    /**
     * 发货单单号开头字母
     */
	public static final String OS_BEGIN_WITH_FH = "FH";

    /**
     * 付款单单号开头字母
     */
	public static final String OP_BEGIN_WITH_FK = "FK";

    /**
     * 合并付款单单号开头字母
     */
    public static final String OP_BEGIN_WITH_MFK = "MFK";

    /**
     * 退单单号开头字母
     */
    public static final String OR_BEGIN_WITH_TD = "TD";

    /**
     * 退货单单号开头字母
     */
    public static final String RS_BEGIN_WITH_TH = "TH";

    /**
     * 退款单单号开头字母
     */
    public static final String RP_BEGIN_WITH_TK = "TK";

	/**
	 * order_info.order_status: 订单状态
	 * 0，未确认 1，已确认 2，已取消 3，完成
	 */
	public static final int OI_ORDER_STATUS_UNCONFIRMED = 0;
	public static final int OI_ORDER_STATUS_CONFIRMED = 1;
	public static final int OI_ORDER_STATUS_CANCLED = 2;
	public static final int OI_ORDER_STATUS_FINISHED = 3;

	/**
	 * order_info.trans_type 交易类型 1：款到发货 2：货到付款 3：担保交易
	 */
	public static final int OI_TRANS_TYPE_PREPAY = 1;
	public static final int OI_TRANS_TYPE_PRESHIP = 2;
	public static final int OI_TRANS_TYPE_GUARANTEE = 3;

	/**
	 * order_info.order_category 订单种类 1：零售 2：物资领用 3：其它出库
	 */
	public static final int OI_ORDER_CATEGORY_SALE = 1; /* 1：零售 */
	public static final int OI_ORDER_CATEGORY_GIFT = 2; /* 2：物资领用 */
	public static final int OI_ORDER_CATEGORY_LOSS = 3; /* 3：其它出库 */

	/**
	 * order_info.reason 原因：sale, gift, loss
	 */
	public static final String OI_REASON_SALE = "S"; /* sale：零售 */
	public static final String OI_REASON_GIFT = "J"; /* gift：物资领用 */
	public static final String OI_REASON_LOSS = "T"; /* loss：其它出库 */

	/**
	 * order_info.money_treatment_type 钱款处理类型 1.补款 2,退款 3,不补不退',
	 */
	public static final int OI_MONEY_TREATMENT_TYPE_PAY = 1; /* 1.补款 */
	public static final int OI_MONEY_TREATMENT_TYPE_REFUND = 2; /* 2,退款 */
	public static final int OI_MONEY_TREATMENT_TYPE_DONOTHING = 3; /* 3,不补不退 */

	/**
	 * order_info.pay_status: 支付总状态
	 * 0，未付款 1，部分付款 2，已付款 3，已结算
	 */
	public static final byte OI_PAY_STATUS_UNPAYED = 0;
	public static final byte OI_PAY_STATUS_PARTPAYED = 1;
	public static final byte OI_PAY_STATUS_PAYED = 2;
	public static final byte OI_PAY_STATUS_SETTLED = 3;

	/**
	 * order_info.ship_status 发货总状态
	 */
	/**
	 * 0，未发货
	 */
	public static final int OI_SHIP_STATUS_UNSHIPPED = 0;

	/**
	 * 1，备货中
	 */
	public static final int OI_SHIP_STATUS_PREPARING = 1;

	/**
	 * 2，部分发货
	 */
	public static final int OI_SHIP_STATUS_PARTSHIPPED = 2;

	/**
	 * 3，已发货
	 */
	public static final int OI_SHIP_STATUS_ALLSHIPED = 3;

	/**
	 * 4，部分收货
	 */
	public static final int OI_SHIP_STATUS_PARTRECEIVED = 4;

	/**
	 * 5，客户已收货
	 */
	public static final int OI_SHIP_STATUS_ALLRECEIVED = 5;

	/**
	 * 8，备货完成
	 */
	public static final int OI_SHIP_STATUS_PREPARED = 8;

	/**
	 * order_info.question_status 问题单状态 0正常单、1问题单
	 */
	public static final int OI_QUESTION_STATUS_QUESTION = 1;
	public static final int OI_QUESTION_STATUS_NORMAL = 0;

	/**
	 * order_info.order_type 订单类型
	 */
	public static final int OI_ORDER_TYPE_NORMAL_ORDER = 0;
	public static final int OI_ORDER_TYPE_REISSUE_ORDER = 1;
	public static final int OI_ORDER_TYPE_EXCHANGE_ORDER = 2;

	/**
	 * order_info.lock_status 订单锁定状态 0未锁定、1已锁定
	 */
	public static final int OI_LOCK_STATUS_UNLOCKED = 0;
	public static final int OI_LOCK_STATUS_LOCKED = 1;

	/**
	 * order_info.process_status 处理状态
	 */
	public static final int OI_PROCESS_STATUS_NORMAL = 0; /* 0 正常 */
	public static final int OI_PROCESS_STATUS_SUSPENDED = 1; /* 1 挂起 */

	/**
	 * order_info.notice_status 通知收款状态
	 */
	public static final int OI_NOTICE_STATUS_UNNOTICED = 0; /* 0 未通知 */
	public static final int OI_NOTICE_STATUS_NOTICED = 1; /* 1 已通知 */

	/**
	 * order_info.timeout_status 是否超时单
	 */
	public static final int OI_TIMEOUT_STATUS_NORMAL = 0; /* 0 正常 */
	public static final int OI_TIMEOUT_STATUS_TIMEOUT = 1; /* 1 超时 */

	/**
	 * order_into.depot_status 分仓发货状态
	 */
	public static final int OI_DEPOT_STATUS_UNDEPOTED = 0; /* 0 未分仓 */
	public static final int OI_DEPOT_STATUS_DEPOTED_UNNOTICED = 1; /* 1 已分仓未通知 */
	public static final int OI_DEPOT_STATUS_DEPOTED_NOTICED = 2; /* 2 已分仓已通知 */

	/**
	 * order_info.return_status
	 */
	public static final int OI_RETURN_STATUS_UNRETURNED = 0; /* 0未退货 */
	public static final int OI_RETURN_STATUS_PART_IN = 1; /* 1部分退货申请中 */
	public static final int OI_RETURN_STATUS_ALL_IN = 2; /* 2退货申请中 */
	public static final int OI_RETURN_STATUS_PART_DONE = 3; /* 3已部分退货 */
	public static final int OI_RETURN_STATUS_ALL_DONE = 4; /* 4已退货 */

	/**
	 * order_info.refund_status
	 */
	public static final int OI_REFUND_STATUS_UNREFUNDED = 0; /* 0未退款 */
	public static final int OI_REFUND_STATUS_PART_IN = 1; /* 1部分退款申请中 */
	public static final int OI_REFUND_STATUS_ALL_IN = 2; /* 2退款申请中 */
	public static final int OI_REFUND_STATUS_PART_DONE = 3; /* 3已部分退款 */
	public static final int OI_REFUND_STATUS_ALL_DONE = 4; /* 4已退款 */

	/**
	 * order_info.is_separate
	 */
	public static final int OI_IS_SEPARATE_UNORWAIT_SEPARATED = 0; /*
																	 * 0
																	 * 未分成或等待分成
																	 */
	public static final int OI_IS_SEPARATE_SAPERATED = 1; /* 1 已分成 */
	public static final int OI_IS_SEPARATE_CANCEL_SAPERATED = 2; /* 2 取消分成 */

	/**
	 * order_info.chased_or_not
	 */
	public static final int OI_CHASED_OR_NOT_NO = 0; /* 未追单 */
	public static final int OI_CHASED_OR_NOT_YES = 1; /* 已追单 */

	/**
	 * order_pay.pay_status 支付状态 0未付款、1付款中、2已付款、3已结算、4待确认
	 */
	public static final int OP_PAY_STATUS_UNPAYED = 0;
	public static final int OP_PAY_STATUS_PAYING = 1;
	public static final int OP_PAY_STATUS_PAYED = 2;
	public static final int OP_PAY_STATUS_SETTLED = 3;
	public static final int OP_PAY_STATUS_COMFIRM = 4;
	/**
	 * 
	 */
	public static final int OP_MERGE_PAY_STATUS_UNPAYED = 0; /* 0，未付款 */
	public static final int OP_MERGE_PAY_STATUS_PAYED = 1; /* 1已付款 */
	/**
	 * order_pay.pay_code 支付方式
	 * 
	 */
	public static final String OP_PAY_CODE_ALIPAY = "alipay";
	public static final String OP_PAY_CODE_BALANCE = "balance";
	public static final int OP_PAY_ID_BALANCE = 3;
       

	/**
	 * order_ship.shipping_status 商品配送情况
	 * 0，未发货、1，已发货、2，已收货、3，备货中、4，备货完成、13 客户确认收货
	 */
	public static final int OS_SHIPPING_STATUS_UNSHIPPED = 0;
	public static final int OS_SHIPPING_STATUS_SHIPPED = 1;
	public static final int OS_SHIPPING_STATUS_RECEIVED = 2;
	public static final int OS_SHIPPING_STATUS_PREPARING = 3;
	public static final int OS_SHIPPING_STATUS_PREPARED = 4;
	public static final int OS_SHIPPING_STATUS_CONFIRM = 13;

	/**
	 * order_ship.range_status 订单配货状态
	 */
	public static final int OS_RANGE_STATUS_UNPICK = 0; /* 0 未配货 */
	public static final int OS_RANGE_STATUS_PICKED = 1; /* 1 已配货 */
	

	/**
	 * order_return.return_order_status 退单状态
	 */
	public static final int OR_RETURN_ORDER_STATUS_UNCONFIRMED = 0; /* 0未确定 */
	public static final int OR_RETURN_ORDER_STATUS_CONFIRMED = 1; /* 1已确认 */
	public static final int OR_RETURN_ORDER_STATUS_PASSED = 2; /* 2质检通过 */
	public static final int OR_RETURN_ORDER_STATUS_UNPASSED = 3; /* 3质检不通过 */
	public static final int OR_RETURN_ORDER_STATUS_INVALID = 4; /* 4无效 */
	public static final int OR_RETURN_ORDER_STATUS_SUBMIT_IDEA = 5; /* 5已提交处理意见 */
	public static final int OR_RETURN_ORDER_STATUS_UNSUBMIT_IDEA = 6; /* 6未提交处理意见 */
	public static final int OR_RETURN_ORDER_STATUS_CHECK_OK = 7; /* 7终审通过 */
	public static final int OR_RETURN_ORDER_STATUS_CHECK_FAIL = 8; /* 8终审不通过 */
	public static final int OR_RETURN_ORDER_STATUS_REPAIR = 9; /* 9已修补 */
	public static final int OR_RETURN_ORDER_STATUS_FINISH = 10; /* 10已完成 */

	/**
	 * order_return.lock_status
	 */
	public static final int OR_LOCK_STATUS_UNLOCKED = 0; /* 0未锁定 */
	public static final int OR_LOCK_STATUS_LOCKED = 1; /* 1锁定 */
	/***
	 * order_return.disposition提交处理意见
	 */
	public static final int OR_RETURN_DISPOSITION_AGAIN = 1; /* 1再次质检 */
	public static final int OR_RETURN_DISPOSITION_NOTRETURN = 2; /* 2不允许客户退货 */
	public static final int OR_RETURN_DISPOSITION_ALLOWRETURN = 3; /* 3允许客户退货 */

	/**
	 * order_return.return_type 退单类型：1退货单、2拒收入库单、3退款单、4额外退款单 5换货退货单
	 */
	public static final int OR_RETURN_TYPE_RETURN = 1;
	public static final int OR_RETURN_TYPE_REJECT = 2;
	public static final int OR_RETURN_TYPE_REFUND = 3;
	public static final int OR_RETURN_TYPE_EXREFUND = 4;
	public static final int OR_RETURN_TYPE_LOSERETURN = 5;

	/**
	 * order_return.process_type 处理状态
	 * 0无操作、1退货、2换货
	 */
	public static final int OR_PROCESS_TYPE_NOOP = 0;
	public static final int OR_PROCESS_TYPE_RETURN = 1;
	public static final int OR_PROCESS_TYPE_EXCHANGE = 2;

	/**
	 * order_return.order_type 是否是换货时产生的退货、退款单 1是 0否 2额外退款单'
	 */
	public static final int OR_ORDER_TYPE_NO = 0; /* 0不是换货时产生的退货、退款单 */
	public static final int OR_ORDER_TYPE_YES = 1; /* 1是换货时产生的退货、退款单 */
	public static final int OR_ORDER_TYPE_OTHER = 2; /* 2额外退款单 */

	/**
	 * order_return.timeout_status 是否超时单
	 */
	public static final int OR_TIMEOUT_STATUS_NORMAL = 0; /* 0 正常 */
	public static final int OR_TIMEOUT_STATUS_TIMEOUT = 1; /* 1 超时 */

	/**
	 * order_return.pay_status 退款总状态
	 */
	public static final int OR_PAY_STATUS_UNSETTLED = 0; /* 0，未结算 */
	public static final int OR_PAY_STATUS_SETTLED = 1; /* 1，已结算 */
	public static final int OR_PAY_STATUS_WAITSETTLE = 2; /* 2，待结算 */
	
	/**
	 * order_return.is_good_received 消费者是否收到货
	 */
	public static final int OR_SHIP_GOODS_RECEIVED_NO = 0; /* 客户没有收到货 */
	public static final int OR_SHIP_GOODS_RECEIVED_YES = 1; /* 客户收到货 */
	/**
	 * order_return.return_settlement_type 预付款/保证金
	 */
	public static final int OR_SETTLEMENT_TYPE_ADVANCE_PAYMENT = 1; /* 1，预付款 */
	public static final int OR_SETTLEMENT_TYPE_DEPOSIT = 2; /* 2，保证金 */

	/**
	 * order_return.is_update 下发ERP
	 */
	public static final int OR_IS_UPDATE_YES = 1; /* 1,下发ERP */
	public static final int OR_IS_UPDATE_NO = 2; /* 2,不下发ERP */

	/**
	 * order_refund.return_pay_status 退单财务状态
	 */
	public static final int ORF_PAY_STATUS_UNSETTLED = 0; /* 0，未结算 */
	public static final int ORF_PAY_STATUS_SETTLED = 1; /* 1，已结算 */
	public static final int ORF_PAY_STATUS_WAITSETTLE = 2; /* 2，待结算 */

	/**
	 * order_return_ship.return_shipping_status 退单物流状态
	 */
	public static final int ORS_RETURN_SHIPPING_STATUS_UNRECEIVED = 0; /* 0，未收货 */
	public static final int ORS_RETURN_SHIPPING_STATUS_RECEIVED = 1; /* 1，已收货,未入库 */
	public static final int ORS_RETURN_SHIPPING_STATUS_STORAGED = 2; /* 2，已入库 */
	public static final int ORS_RETURN_SHIPPING_STATUS_TOSTORAGE = 3; /* 3，可入库 */
	public static final int ORS_RETURN_SHIPPING_STATUS_WAITSTORAGE = 4; /*
																		 * 4，快递收货
																		 * ,
																		 * 仓库未收货
																		 */

	/**
	 * order_return_ship.chased_or_not 是否已追单（0 否 1 是）
	 */
	public static final int ORS_RETURN_SHIPPING_STATUS_UNCHASED = 0;
	public static final int ORS_RETURN_SHIPPING_STATUS_CHASED = 1;

	/**
	 * order_return_ship.return_order_ispass 退单质检状态
	 */
	public static final int ORS_RETURN_ORDER_ISPASS_PASS = 0; /* 0质检通过 */
	public static final int ORS_RETURN_ORDER_ISPASS_NOPASS = 1; /* 1质检不通过 */

	/*
	 * 显示类型 0:默认显示有效订单,1:显示全部订单,2:显示隐藏订单
	 */
	public static final int ORT_SHOW_EFFICIENT_ITEM = 0;
	public static final int ORT_SHOW_ALL_ITEM = 1;
	public static final int ORT_SHOW_HIDDEN_ITEM = 2;

	/*
	 * //时间类型 1:产生退单时间;2:入库时间;3:结算时间;4：确认时间;
	 */
	public static final int ORT_ADD_TIME = 1;
	public static final int ORT_CHECKIN_TIME = 2;
	public static final int ORT_CLEAR_TIME = 3;
	public static final int ORT_CONFIRM_TIME = 4;
	/*
	 * 周期ID
	 */
	public static final String OS_PAYMENT_ALARM_PERIOD = "6101"; /* 付款提醒周期 */
	public static final String OS_PAYMENT_PERIOD = "6102"; /* 付款周期 */
	public static final String OS_EXPRESS_RECEIPT_PERIOD = "6103"; /* 快递收款周期 */
	public static final String OS_PAYMENT_CONFIRM_PERIOD = "6104"; /* 付款确认周期 */
	public static final String OS_ORDER_PROCESS_PERIOD = "6201"; /* 订单全程处理周期 */
	public static final String OS_ORDER_CONFIRM_PERIOD = "6202"; /* 订单确认周期 */
	public static final String OS_ORDER_FINISH_PERIOD = "6203"; /* 订单完成周期 */
	public static final String OS_ORDER_SETTLE_PERIOD = "6204"; /* 订单结算周期 */
	public static final String OS_DELIVERY_PERIOD = "6301"; /* 发货周期 */
	public static final String OS_CUSTOMER_CONFIRM_RECEIVE_PERIOD = "6302"; /* 客户确认收货周期 */
	public static final String OS_REFUND_PERIOD = "6401"; /* 退款周期 */
	public static final String OS_REFUDN_CONFIRM_PERIOD = "6402"; /* 退款确认周期 */
	public static final String OS_CHASE_PERIOD = "6501"; /* 追单周期 */
	public static final String OS_EXPRESS_RECIEVE_ALARM_PERIOD = "6502"; /* 快递收货提醒周期 */
	public static final String OS_EXPRESS_RECIEVE_PERIOD = "6503"; /* 快递收货周期 */
	public static final String OS_WAREHOUSE_CONFIRM_RECIEVE_PERIOD = "6504"; /* 仓库确认收货周期 */
	public static final String OS_INCOMING_QUALITY_INSPECTION_PERIOD = "6505"; /* 入库质检周期 */
	public static final String OS_RECEIVING_WAREHOUSING_PERIOD = "6506"; /* 收货入库周期 */
	public static final String OS_ORDER_RETURN_PROCESS_PERIOD = "6601"; /* 退单全程处理周期 */
	public static final String OS_ORDER_RETURN_SHIP_FINISH_PERIOD = "6602"; /* 退货单完成周期 */
	public static final String OS_ORDER_RETURN_SHIP_SETTLE_PERIOD = "6603"; /* 退货单结算周期 */
	public static final String OS_ADDITION_PAYMENT_PERIOD = "6701"; /* 补款周期 */
	public static final String OS_ADDITION_PAYMENT_ALARM_PERIOD = "6702"; /* 补款提醒周期 */
	public static final String OS_ADDITION_PAYMENT_CONFIRM_PERIOD = "6703"; /* 补款确认周期 */
	public static final String OS_ORDER_EXCHANGE_PROCESS_PERIOD = "6801"; /* 换单全程处理周期 */
	public static final String OS_ORDER_EXCHANGE_CONFIRM_PERIOD = "6802"; /* 换货确认周期 */
	public static final String OS_ORDER_EXCHANGE_FINISH_PERIOD = "6803"; /* 换货完成周期 */
	public static final String OS_ORDER_EXCHANGE_SETTLE_PERIOD = "6804"; /* 换货结算周期 */
	public static final String OS_ORDER_CANCLE_FONT_PERIOD = "6806"; /* 前端订单取消周期 */
	
	/**
	 * 订单周期缓存前缀
	 */
	public static final String OS_ORDER_PERIOD = "OS_ORDER_PERIOD_";

	/**
	 * 添加订单错误代码
	 */
	public static final String OS_SAVE_ORDER_ERROR_CODE = "OS501"; /* 保存订单错误 */
	public static final String OS_CONVERT_FORMAT_ERROR_CODE = "OS502";/* 数据转换格式错误 */
	public static final String OS_ORDER_USER_NULL_CODE = "OS503";/* 下单人不存在错误 */
	public static final String OS_SAVE_SHIP_ERROR_CODE = "OS504";/* 保存发货单错误 */
	public static final String OS_SAVE_ORDER_GOODS_ERROR_CODE = "OS505";/* 保存货物信息错误 */
	public static final String OS_SAVE_ORDER_PAY_ERROR_CODE = "OS506";/* 保存付款单错误 */
	public static final String OS_SURE_ORDER_ERROR_CODE = "OS507";/* 自动调用确认接口 */
	public static final String OS_SET_QUESTION_ORDER_ERROR_CODE = "OS508";/* 设为问题单错误 */

	/*
	 * 添加退单错误代码
	 */
	public static final String OR_GET_MAX_RETURN_SN_ERROR_CODE = "OR601"; /* 获取当天最大退单编号错误 */
	public static final String OR_SAVE_RETURN_SN_ERROR_CODE = "OR602"; /* 保存退单编号错误 */
	public static final String OR_GENERATE_RETURN_SN_ERROR_CODE = "OR603"; /* 生成退单编号错误 */
	public static final String OR_GENERATE_ORDER_RETURN_ERROR_CODE = "OR604"; /* 生成退单错误 */
	public static final String OR_GET_ORDER_SHIP_NULL_CODE = "OR605"; /* 发货单不存在错误 */
	public static final String OR_GENERATE_ORDER_RETURN_NULL_CODE = "OR606"; /* 生成退单返回为空错误 */
	public static final String OR_GENERATE_ORDER_RETURN_SHIP_ERROR_CODE = "OR607"; /* 保存退货单错误 */
	public static final String OR_FIND_ORDER_RETURN_GOODS_ERROR_CODE = "OR608"; /* 查找退货商品信息错误 */
	public static final String OR_SAVEORUPDATE_ORDER_RETURN_GOODS_ERROR_CODE = "OR609"; /* 保存退货商品信息错误 */
	public static final String OR_GET_REFUND_PERIOD_VALUE_ERROR_CODE = "OR610"; /* 获取退款周期值错误 */
	public static final String OR_SAVE_ORDER_REFUND_ERROR_CODE = "OR611"; /* 保存退款单错误 */
	public static final String OR_ADD_RETURN_ACTION_ERROR_CODE = "OR612"; /* 添加退单日志错误 */
	public static final String OR_GET_PRODUCT_BARCODE_LIST_NULL_CODE = "OR613"; /* 商品条码表无此skuSn信息 */
	public static final String OR_GET_ORDER_GOODS_NULL_CODE = "OR614"; /* 订单商品表中对应发货单无此商品 */

	public static final String OR_CONVERT_FORMAT_ERROR_CODE = "OR615"; /* 数据转换格式错误 */
	public static final String OR_ADD_ORDER_RETURN_GOODS_ERROR_CODE = "OR616"; /* 退货单添加商品错误 */

	/*
	 * 退单基本信息对账标示表中status值
	 */
	public static final int ORB_STATUS_UNCHECK = 0; // 未对帐
	public static final int ORB_STATUS_CHECKED = 1; // 已平帐
	public static final int ORB_STATUS_OS_UNSETTLE = 2; // os未结算
	public static final int ORB_STATUS_ERP_LOST = 3; // erp漏单
	public static final int ORB_STATUS_OS_MOREMONEY = 4; // 总金额os多
	public static final int ORB_STATUS_ERP_MOREMONEY = 5; // 总金额erp多
	public static final int ORB_STATUS_OS_MOREGOODS = 6; // os商品数量多
	public static final int ORB_STATUS_ERP_MOREGOODS = 7; // erp商品数量多
	public static final int ORB_STATUS_OTHER_QUESTION = 8; // 其他问题
	public static final int ORB_STATUS_ERP_REDUPLICATE = 9; // erp重复退单
	public static final int ORB_STATUS_JUST_DETAIL_OK = 10; // 详细信息平帐

	/*
	 * 退单详细信息对账标示表中status值
	 */
	public static final int ORDB_STATUS_UNCHECK = 0; // 未对帐
	public static final int ORDB_STATUS_ERP_GOODS_LOST = 1; // erp缺失商品
	public static final int ORDB_STATUS_OS_GOODS_LOST = 2; // os缺失商品
	public static final int ORDB_STATUS_ERP_GOODS_MORE = 3; // erp商品数量多
	public static final int ORDB_STATUS_OS_GOODS_MORE = 4; // os商品数量多
	public static final int ORDB_STATUS_ERP_PRICE_MORE = 5; // erp商品单价高
	public static final int ORDB_STATUS_OS_PRICE_MORE = 6; // os商品单价高
	public static final int ORDB_STATUS_OTHER_QUESTION = 7; // 其他问题
	public static final int ORDB_STATUS_CHECK_OK = 8; // 此商品对账正确

	/*
	 * 订单对账标示表中status值
	 */
	public static final int OB_STATUS_UNCHECK = 0; // 未对帐
	public static final int OB_STATUS_CHECKED = 1; // 已平帐
	public static final int OB_STATUS_OS_UNSETTLE = 2; // os未结算
	public static final int OB_STATUS_ERP_LOST = 3; // erp漏单
	public static final int OB_STATUS_OS_MOREMONEY = 4; // 总金额os多
	public static final int OB_STATUS_ERP_MOREMONEY = 5; // 总金额erp多
	public static final int OB_STATUS_OS_MOREGOODS = 6; // os商品数量多
	public static final int OB_STATUS_ERP_MOREGOODS = 7; // erp商品数量多
	public static final int OB_STATUS_OTHER_QUESTION = 8; // 其他问题
	public static final int OB_STATUS_ERP_REDUPLICATE = 9; // erp重复退单
	public static final int OB_STATUS_JUST_DETAIL_OK = 10; // 详细信息平帐

	/*
	 * 订单详细信息对账标示表中status值
	 */
	public static final int ODB_STATUS_UNCHECK = 0; // 未对帐
	public static final int ODB_STATUS_ERP_GOODS_LOST = 1; // erp缺失商品
	public static final int ODB_STATUS_OS_GOODS_LOST = 2; // os缺失商品
	public static final int ODB_STATUS_ERP_GOODS_MORE = 3; // erp商品数量多
	public static final int ODB_STATUS_OS_GOODS_MORE = 4; // os商品数量多
	public static final int ODB_STATUS_ERP_PRICE_MORE = 5; // erp商品单价高
	public static final int ODB_STATUS_OS_PRICE_MORE = 6; // os商品单价高
	public static final int ODB_STATUS_OTHER_QUESTION = 7; // 其他问题
	public static final int ODB_STATUS_CHECK_OK = 8; // 此商品对账正确
	/*
	 * 批量任务类型CODE logistics_question
	 */
	public static final String BT_CONFIRM_CODE = "110"; // 批量确认操作
	public static final String BT_ERP_ADD_CODE = "111"; // 订单进行批量下发ERP,增加update_type操作
	public static final String BT_ERP_UPDATE_CODE = "112"; // 订单进行批量下发ERP,变更update_type操作
	public static final String BT_LOGISTICS_QUESTION_CODE = "113"; // 物流问题单批量导入
	public static final String BT_OUT_STOCK_CODE = "114"; // 缺货问题单批量导入操作

	/*
	 * 批量任务类型导入模板列数
	 */
	public static final int BT_CONFIRM_COLUMNS = 1; // 批量确认模板列数为1
	public static final int BT_ERP_COLUMNS = 3; // 批量下发ERP模板列数为3
	public static final int BT_LOGISTICS_QUESTION_COLUMNS = 4; // 批量导入物流问题单模板列数为4
	public static final int BT_OUT_STOCK_COLUMNS = 4; // 批量导入缺货问题单模板列数为4

	/*
	 * ORDER_CATEGORY
	 */
	// 订单种类 1：零售 2：物资领用 3：其它出库 4: C2b定制
	public static final Integer ORDER_CATEGORY_C2B = 4; // 活动类型：无活动

	public static final String CLOUD_SHOP = "全流通"; // 云货架,全流通
	/** 赠品标示 */
	public static final String GOODS_GIFT = "gift";

	public static final String COOKIE_ID_KEY = "LOGIN_USER_ID";

	/**
	 * 红包类型
	 */
	public static final String CARD_RAND_CODE_ALL = "0"; // 全场红包
	public static final String CARD_RAND_CODE_BRAND = "1"; // 品牌红包
	public static final String CARD_RAND_CODE_SKU = "2"; // 单品红包

	/**
	 * 当前权限session
	 ***/
	public static final String SYSTEM_RESOURCE = "systemResource";

	/**
	 * 打折券使用情况
	 */
	public static final String COUPON_USE = "0"; // 使用红包
	public static final String COUPON_NO_USE = "1"; // 不使用红包

	public static final String ORDER_LIST = "orderList";
	public static final String RETURN_LIST = "returnList";
	public static final String ORDER_QUESTION = "orderQuestion";

	public static final String CHANGE_ORDER_INFO = "changeOrderInfo";

	// 退单结算操作，平台 数据推送造型师
	public static final String OMS_SETTLE_RETURN_ZXS_INTERFACE = "oms_settle_return_call_zxs";

	/**
	 * 列表详情
	 */
	public static final String DETAILS_DISPLAY = "display";
	public static final String ORDER_RETURN_DISPLAY = "orderReturnDisplay";

	public static final String BATCH_LOCK = "batchLock";
	public static final String BATCH_UNLOCK = "batchUnlock";
	public static final String BATCH_CONFIRM = "batchConfirm";

	// Oms 发货 队列 推送开关
	public static final String OMS_DELIVERY_QUEUE_OFF = "oms_delivery_queue_off";

	public static final String BATCH_RETURN_NORMAL = "batchReturnNormal";
	public static final String IMPORT_LOGISTICS_QUESTION = "importLogisticsQuestion";

	public static final String PLEASE_SELECT_STRING = "-1";

	public static final Integer PLEASE_SELECT_Integer = -1;

	/**
	 * session User key
	 */
	public static final String SESSION_USER_KEY = "SESSION_USER_KEY";
	
	public static final String SESSION_ROLE_KEY = "SESSION_ROLE_KEY";

	public static final String SESSION_RES_KEY = "SESSION_RES_KEY";
	/** 管理员session key **/
	public static final String SESSION_SUP_KEY = "sup";
	
	public static final String SESSION_UNLOCK_KEY = "unlock";
	
	public static final String SITE_MANAGER_KEY = "SITE_MANAGER";

	/**
	 * order_goods.has_stock 库存占用
	 */
	public static final int ORG_HAS_STOCK_YES = 1; /* 已占用 */
	public static final int ORG_HAS_STOCK_NO = 0; /* 未占用 */
	public static final int ORG_HAS_STOCK_SOME = 2; /* 部分占用 */
	public static final int ORG_HAS_STOCK_FINISH = 2; /* 占用释放完成 */

	public static final String ERP_DOC_SOURCE_B2C = "03";// B2C
	public static final String ERP_DOC_SOURCE_YHJ = "02"; // 运货架
	public static final String ERP_DOC_SOURCE_POS = "01"; // 全流通

	public static final String EXPORT_THE_RESULTS_DO_NOT_INCLUDE_THE_COMMODITY_INFO = "1";// 导出不包含商品信息
	/** ERP 付款备注 款到发货 */
	public static final String ERP_PAY_STYLE_KDFH = "4";
	/** 货到付款 */
	public static final String ERP_PAY_STYLE_HDFK = "2";
	/** 门店支付 */
	public static final String ERP_PAY_STYLE_MDZF = "06";
 
	/**
	 * OS通知ERP业务类型
	 */
	public static final Integer ERP_BUSS_TYPE_SETTLE = 1; // OS通知ERP业务类型-结算
	public static final Integer ERP_BUSS_TYPE_SETTLE_CANCLE = 8; // OS通知ERP业务类型-结算
	public static final Integer ERP_BUSS_TYPE_SETTLE_ADJUST = 2; // OS通知ERP业务类型-结算调整单
	public static final Integer ERP_BUSS_TYPE_INPUT = 0; // OS通知ERP业务类型-入库
	public static final Integer ERP_BUSS_TYPE_INPUT_CANCLE = 9; // OS通知ERP业务类型-撤销入库
	
	/**
	 * 入库结算业务类型
	 */
	public static final Integer ORDER_BUSS_TYPE_ORDER_SETTLE = 0; // 订单结算
	public static final Integer ORDER_BUSS_TYPE_RETURN_INPUT = 1; // 退单入库
	public static final Integer ORDER_BUSS_TYPE_RETURN_SETTLE = 2; // 退单结算
	
	/**
	 * 队列推送标志(0:未推送,1:入库已推送，2:入库已处理, 3:入库处理失败, 4:结算已推送, 5:结算已处理 ,6:结算处理失败
	 */
	public static final String ORDER_SETTLE_QUEUE_NOPUSH = "0"; // 未推送
	public static final String ORDER_SETTLE_QUEUE_PUSHED = "1;"; // 推送ERP
	public static final String ORDER_SETTLE_QUEUE_PUSH_FAIL = "2;"; // 推送失败
	public static final String ORDER_SETTLE_QUEUE_DEAL_SUCC = "3;"; // 操作成功
	public static final String ORDER_SETTLE_QUEUE_DEAL_FAIL = "4;"; // 操作失败

	/** 业务监控平台 MQ队列名称 **/
	public static final String BUSINESS_MONITOR_ORDER_CANCEL = "business_monitor_order_cancel"; // 撤单
	public static final String BUSINESS_MONITOR_ORDER_DELETE = "business_monitor_order_delete"; // 删除明细
	public static final String BUSINESS_MONITOR_ORDER_MODIFY = "business_monitor_order_modify"; // 修改明细
	public static final String BUSINESS_MONITOR_ORDER_PROBLEM = "business_monitor_order_problem"; // 问题单
	public static final String BUSINESS_MONITOR_ORDER_NORMAL = "business_monitor_order_recover"; // 转正常单
	public static final String BUSINESS_MONITOR_ORDER_ERP = "business_monitor_order_erp"; // 下发ERP
	public static final String BUSINESS_MONITOR_ORDER_EXCEPTION = "business_monitor_order_exception"; // 异常
	public static final String BUSINESS_MONITOR_ORDER_MODIFY_DELIVERY = "business_monitor_order_modify_delivery"; // 修改配发明细
	public static final String BUSINESS_MONITOR_ORDER_DELETE_DELIVERY = "business_monitor_order_delete_delivery"; // 删除配发明细
	public static final String BUSINESS_MONITOR_ORDER_BG = "business_monitor_order_bg";
	public static final String BUSINESS_MONITOR_ORDER_POS = "business_monitor_order_pos";
	
	// 汇通快运承运商编号
	public static final String SHIPPING_CODE_HTKY = "htky";
	// 没分仓默认仓库编号
	public static final String DETAILS_DEPOT_CODE = "DEFAULT";
	
	public static final String ORDER_TYPE_C2B = "c2b";
	public static final String ORDER_TYPE_C2M = "c2m";
	
	/************* 接受请求方式类型 *************/
	public static final String REQ_TYPE_GET = "get";
	
	public static final String REQ_TYPE_POST = "post";
	
	/*****************split_status*****************/
	/**
	 * 未拆单
	 */
	public static final Byte SPLIT_STATUS_UNSPLITED = 0;

	/**
	 * 拆单中
	 */
	public static final Byte SPLIT_STATUS_SPLITTING = 1;

	/**
	 * 已拆单
	 */
	public static final Byte SPLIT_STATUS_SPLITED = 2;

	/**
	 * 重新拆单
	 */
	public static final Byte SPLIT_STATUS_RESPLITED = 3;

	/************* 接受请求方式类型 *************/
	public static final String SUPPLIER_TYPE_MB = "MB";
	
	public static final String SUPPLIER_TYPE_THIRD = "THIRD";
	
	public static final Integer IS_DEL_NO = 0;
	public static final Integer IS_DEL_DEL = 1;
	
	/**
	 * order_distribute.order_status 交货单状态 0：未确认；1：已确认；2：已取消；3：完成；
	 * OD_表示交货单
	 */
	public static final Byte OD_ORDER_STATUS_UNCONFIRMED = 0;
	public static final Byte OD_ORDER_STATUS_CONFIRMED = 1;
	public static final Byte OD_ORDER_STATUS_CANCLED = 2;
	public static final Byte OD_ORDER_STATUS_FINISHED = 3;
	
	/**
	 * order_distribute.ship_status 发货总状态（0：未发货；1：备货中；2：部分发货；3：已发货；4，部分收货；5，客户已收货，6门店部分收货 7门店收货）
	 * OD_表示交货单
	 */
	public static final Byte OD_SHIP_STATUS_UNSHIPPED = 0;

	public static final Byte OD_SHIP_STATUS_PREPARING = 1;

	public static final Byte OD_SHIP_STATUS_PARTSHIPPED = 2;

	public static final Byte OD_SHIP_STATUS_ALLSHIPED = 3;

	public static final Byte OD_SHIP_STATUS_CUSTOMER_PARTRECEIVED = 4;

	public static final Byte OD_SHIP_STATUS_CUSTOMER_ALLRECEIVED = 5;

	public static final Byte OD_SHIP_STATUS_STORE_PARTRECEIVED = 6;

	public static final Byte OD_SHIP_STATUS_STORE_ALLRECEIVED = 7;

	public static final Byte OD_SHIP_STATUS_PREPARED = 8;
	
	/**
	 * order_distribute.question_status 问题单状态 （0 否 1 是）
	 * OD_表示交货单
	 */
	public static final Integer OD_QUESTION_STATUS_NO = 0;//不是问题单
	public static final Integer OD_QUESTION_STATUS_YES = 1;//是问题单
	
	/**
	 * order_distribute.trans_type 交易类型 1：款到发货 2：货到付款 3：担保交易
	 * OD_表示交货单
	 */
	public static final int OD_TRANS_TYPE_PREPAY = 1; /* 1：款到发货 */
	public static final int OD_TRANS_TYPE_PRESHIP = 2; /* 2：货到付款 */
	public static final int OD_TRANS_TYPE_GUARANTEE = 3; /* 3：担保交易 */
	
	/**
	 * order_distribute.source 0:未处理;1:POS(全流通);2:云货架(YHJ);3:线上订单(B2C);4:C2M;5:C2B
	 * OD_表示交货单
	 */
	public static final int OD_SOURCE_UNDEAL = 0;//
	public static final int OD_SOURCE_POS = 1;//
	public static final int OD_SOURCE_YHJ = 2;//
	public static final int OD_SOURCE_B2C = 3;//
	public static final int OD_SOURCE_C2M = 4;//
	public static final int OD_SOURCE_C2B = 5;//
	public static final int OD_SOURCE_BG = 6;//平台闪购标识
	
	/**
	 * order_distribute.depot_status 分仓发货状态（0，未分仓 1，已分仓未通知 2，已分仓已通知）
	 * OD_表示交货单
	 */
	public static final int OD_UNDEPOT = 0;//未分仓
	public static final int OD_DEPOT_UNNOTICE = 1;//已分仓未通知
	public static final int OD_DEPOT_NOTICE = 2;//已分仓已通知
	
	/**
	 * master_order_info.source 0:未处理;1:POS(全流通);2:云货架(YHJ);3:线上订单(B2C);4:C2M;5:C2B;6:B2b
	 * mo_表示主单
	 */
	public static final int MO_SOURCE_UNDEAL = 0;//
	public static final int MO_SOURCE_POS = 1;//
	public static final int MO_SOURCE_YHJ = 2;//
	public static final int MO_SOURCE_B2C = 3;//
	public static final int MO_SOURCE_C2M = 4;//
	public static final int MO_SOURCE_C2B = 5;
	public static final int MO_SOURCE_B2B = 6;

	/**************************红包打折券使用状态**************************/
	public static final Integer CARD_CANCEL = 3;
	public static final Integer CARD_USERED = 4;
	
	/**************************红包打折券使用状态**************************/
	public static final String STRING_SPLIT_COLON = ":";
	public static final String STRING_SPLIT_COMMA = ",";
	
	public final static String order_type_master = "0";
	public final static String order_type_distribute = "1";
	
	/**
	 * 入库向erp推送数据状态：0无需推送、1需要推送、2已推送
	 */
	public static final byte TOERP_NOT_NEED = 0;
	public static final byte TOERP_NEED = 1;
	public static final byte TOERP_ALREADY = 2;
	/**
	 * ERP处理入库数据：0未处理、1处理中、2处理完成、3处理失败
	 */
	public static final byte STORAGE_STATUS_UNTRATED = 0;
	public static final byte STORAGE_STATUS_DOING = 1;
	public static final byte STORAGE_STATUS_FINISHED = 2;
	public static final byte STORAGE_STATUS_FAILED = 3;
	
	/*****************************接口请求类型*******************************/
	public static final String request_type_dubbo = "dubbo";
	public static final String request_type_json = "json";
	public static final String request_type_http = "http";
	
	/**
	 * 快递单状态 0：在途中,1：已发货，2：疑难件，3： 已签收 ，4：退签 5：派件 6：退回
	 */
	public static final Float express_status_onway = 0F;
	public static final Float express_status_shipped = 1F;
	public static final Float express_status_difficult = 2F;
	public static final Float express_status_sign = 3F;
	public static final Float express_status_returnSign = 4F;
	public static final Float express_status_send = 5F;
	public static final Float express_status_return = 6F;
	
	/********************************渠道编码*********************************/
	public static final String NEWFORCE = "NEWFORCE";
	public static final String KELTI = "KELTI";
	public static final String Chlitina = "Chlitina"; // 店长
	public static final String CONSULTANT = "CONSULTANT"; // 咨询师
	
	/********************************订单类型*********************************/
	public static final int OI_ORDER_TYPE_NORNAL = 0; //正常订单
	public static final int OI_ORDER_TYPE_CHANGE = 2; //换货订单
	
	/**
	 * order_pay.pay_id 支付方式
	 */
	public static final String OP_PAY_WAY_PAYCODE_PINGTAI = "pingtai"; /* 支付方式：三方平台担保交易 */

	/**
	 * 支付方式：线下支付
	 */
	public static final String OP_PAY_WAY_PAYCODE_XIANXIA = "xianxia";

	/**
	 * 承运商 京东快递
	 */
	public static final String SHIP_CODE_JDKD = "jdkd";

	/**
	 * 外卖配送
	 */
	public static final String TASK_OUT = "taskout";

	/**
	 * 自提配送
	 */
	public static final String SHIP_CODE_CAC = "cac";
	
	/**
	 * 0未配送、1配送中、2配送完成、3配送取消、4骑手接单、5平台接单、6平台取消
	 */
	public static final int OI_ORDER_DIS_DEFAULT = 0;
	public static final int OI_ORDER_DIS_DOING = 1;
	public static final int OI_ORDER_DIS_DONE = 2;
	public static final int OI_ORDER_DIS_CANCEL = 3;
	public static final int OI_ORDER_DIS_ACCEPT = 4;
	public static final int OI_ORDER_PLATFORM_ACCEPT = 5;
	public static final int OI_ORDER_PLATFORM_CANCEL = 6;

	/**
	 * 骑手状态 1成功、0失败、2平台异常
	 */
	public static final String RIDER_CODE_SUCESS = "1";
	public static final String RIDER_CODE_FAIL = "0";
	public static final String RIDER_CODE_ERROR = "2";
	
	/**
	 * 自提状态 0:未提;1:已提
	 */
	public static final int GOT_STATUS_NO = 0;
	public static final int GOT_STATUS_YES = 1;

	/**
	 * 短信发送状态 0:未提;1:已提
	 */
	public static final int SMS_STATUS_NO = 0;
	public static final int SMS_STATUS_YES = 1;

	/**
	 * list默认size
	 */
	public static final int DEFAULT_LIST_SIZE = 10;

	/**
	 * map默认size
	 */
	public static final int DEFAULT_MAP_SIZE = 16;

    /**
     * 百胜EC 一小时达
     */
    public static final String EC_YIXIAOSHIDA_KEY = "ec.yixiaoshida.key";
    public static final String EC_YIXIAOSHIDA_SECRET = "ec.yixiaoshida.secret";
    public static final String EC_YIXIAOSHIDA_SEND_ORDER_ADDRESS = "ec.yixiaoshida.send.order.address";
    public static final String EC_YIXIAOSHIDA_API = "ec.yixiaoshida.api";
    public static final String EC_YIXIAOSHIDA_VERSION = "ec.yixiaoshida.version";

    /**
     * DRP
     */
    public static final String DRP_HLA_TOKEN = "drp.hla.token";
    public static final String DRP_HLA_BRAND = "drp.hla.brand";
    public static final String DRP_HLA_POST_API_ADDRESS = "drp.hla.post.api.address";
    public static final String DRP_HLA_ORGCODE = "drp.hla.orgcode";
    public static final String DRP_HLA_POST_API_NAME = "drp.hla.post.api.name";

    /**
     * 支付方式 账期
     */
    public static final int PAYMENT_ZHANGQI_ID = 35;

	/**
	 * 支付方式 信用
	 */
	public static final int PAYMENT_XINYONG_ID = 39;

	/**
	 * 支付方式 结算账户
	 */
	public static final int PAYMENT_SETTLEMENT_ID = 45;

	/**
	 * 支付方式 保函
	 */
	public static final int PAYMENT_BAOHAN_ID = 47;
    
    /**
     * 问题单类型 0：普通问题单；1：缺货问题单；2：审核问题单；3签章问题单
     */
    public static final int QUESTION_TYPE_NORMAL = 0;
    public static final int QUESTION_TYPE_LACK = 1;
    public static final int QUESTION_TYPE_REVIEW = 2;
    public static final int QUESTION_TYPE_SIGN = 3;

    /**
     * 待审批问题单
     */
    public static final String QUESTION_CODE_REVIEW = "17";

    /**
     * 待签章问题单
     */
    public static final String QUESTION_CODE_SIGN = "18";

    /**
     * 交货单号前缀
     */
    public static final String ORDER_DISTRIBUTE_BEFORE = "S";

    /**
     * 默认店铺
     */
    public static final String DEFAULT_SHOP = "hbis";
}
