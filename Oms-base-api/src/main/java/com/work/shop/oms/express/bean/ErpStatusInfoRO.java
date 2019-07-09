package com.work.shop.oms.express.bean;

import java.util.List;

public class ErpStatusInfoRO {
	public final static String CODE_SUCCESS = "1";
	public final static String CODE_EMPTY_RESULT = "0"; // 无ERP数据
	public final static String CODE_ERR_PARAMS = "-1"; // 参数异常
	public final static String CODE_ERR_SERVER = "-500"; // 服务异常
	
	private String code;
	private String orderSn;
	private List<ErpStatus> erpStatusList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public List<ErpStatus> getErpStatusList() {
		return erpStatusList;
	}

	public void setErpStatusList(List<ErpStatus> erpStatusList) {
		this.erpStatusList = erpStatusList;
	}

	public class ErpStatus {
		private int index;
		private String statusInfo;
		private String time; // yyyy-MM-dd HH:mm:ss
		private String docType; // 单据类别
		private String depotCode;
		private String deliverySn;
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getStatusInfo() {
			return statusInfo;
		}

		public void setStatusInfo(String statusInfo) {
			this.statusInfo = statusInfo;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getDocType() {
			return docType;
		}

		public void setDocType(String docType) {
			this.docType = docType;
		}

		public String getDepotCode() {
			return depotCode;
		}

		public void setDepotCode(String depotCode) {
			this.depotCode = depotCode;
		}

	}
}
