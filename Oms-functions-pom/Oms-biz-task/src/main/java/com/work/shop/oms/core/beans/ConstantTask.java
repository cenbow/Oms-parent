package com.work.shop.oms.core.beans;

/**
 * 任务常量
 * @author QuYachu
 */
public class ConstantTask {

	/**
	 * Task类型
	 */
	public static String TASK_JOB_TYPE_CLOSE_NIGHT_NOPAYORDER = "closeNightNoPayOrderTask";

	/**
	 * 平台未支付订单超时取消
	 */
	public static String TASK_JOB_TYPE_CLOSE_NOPAY_BANGGO_ORDER = "closeNoPayBanggoOrderTask";

	public static String TASK_JOB_TYPE_CLOSE_NOPAY_GROUP_ORDER = "closeNoPayGroupOrderTask";
	public static String TASK_JOB_TYPE_CLOSE_POS_NOPAYORDER = "closeNoPayPosOrderTask";
	public static String TASK_JOB_TYPE_COPY_2_HISTORY_ORDER = "copy2HistoryOrderTask";
	public static String TASK_JOB_TYPE_EXPRESS_O2O_PUSH_HTKY_ORDER = "expressO2OPushHtkyTask";
	public static String TASK_JOB_TYPE_EXPRESS_O2O_PUSH_YTO_ORDER = "expressO2OPushYtoTask";
	public static String TASK_JOB_TYPE_REFRESH_PROTECT_PRICE_ORDER = "refreshProtectPriceRedisTask";
	public static String TASK_JOB_TYPE_RELEASE_NOPAYORDER_STOCK = "releaseNoPayOrderStockTask";

	/**
	 * 平台自动解锁支付
	 */
	public static String TASK_JOB_TYPE_UNLOCK_OVERTIME_ORDER = "unLockOverTimeOrderTask";

	public static String TASK_JOB_TYPE_ORDER_RECEIVE_CHANGE = "orderReceiveChangeTask";
	public static String TASK_JOB_TYPE_SWDI_ORDER = "swdiOrderTask";
	public static String TASK_JOB_TYPE_PUSHERP_ORDER = "pushERPOrderTask";
	public static String TASK_JOB_TYPE_CLOSE_COPY_ORDER = "colseCopyOrderTask";
	public static String TASK_JOB_TYPE_RETURN_STORAGE_TASK = "returnStorageTask";
	public static String TASK_JOB_TYPE_GOTORDER_NOTICE_TASK = "gotOrderNoticeTask";
	public static String TASK_JOB_TYPE_RIDER_DIST_TASK = "riderDistTask";
	public static String TASK_JOB_TYPE_AUTO_RECEIPT_TASK = "autoReceiptTask";
	
	public static String TASK_JOB_TYPE_ORDER_DISTRIBUTE_OUT_TASK = "orderDistributeOutTask";

	/**
	 * 快递100物流信息任务
	 */
	public static String TASK_JOB_TYPE_ORDER_EXPRESS_TASK = "orderExpressTask";

	/**
	 * 公司账期支付
	 */
	public static String TASK_JOB_TYPE_COMPANY_PAY_TASK = "companyPayPeriodTask";

	/**
	 * 订单发货单自动签收
	 */
	public static String TASK_JOB_TYPE_ORDER_SHIP_RECEIVE_TASK = "orderShipReceiveTask";

	/**
	 * 团购订单未支付尾款，直接取消
	 */
	public static String TASK_JOB_TYPE_ORDER_GROUP_BUY_TASK = "orderGroupBuyTask";
}
