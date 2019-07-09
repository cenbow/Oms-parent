package com.work.shop.oms.param.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 退货单入库数据主体
 * @author cage
 *
 */
public class SfSchTaskExecOosInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String DISP_WAREH_CODE;//发货仓库
	public String SHOP_CODE;//收货门店
	public String HAD_LOCK_WAREH = "1";
	public String B2C_DOC_CODE ;//退货单号
	public String SRC_DOC_CODE ;//退货单号
	public String OS_DOC_CODE ;//退货单号
	public String SRC_DOC_TYPE ;//2：走账；3:冲单
	public String DOC_SOURCE = "04";
	public String IS_ONLINE = "0";
	public String IS_OOS = "0";
	public String IS_ATUO_GDN = "1";
	public String IS_ATUO_GRN = "1";
	public String DATA_SOURCE = "01";
	public String IS_OLD_ERP = "1";
	public List<LstSfSchTaskExecOosDtls> lstSfSchTaskExecOosDtls;
	
}
