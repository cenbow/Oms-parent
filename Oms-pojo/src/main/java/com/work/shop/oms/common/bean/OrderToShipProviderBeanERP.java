package com.work.shop.oms.common.bean;

public class OrderToShipProviderBeanERP {

	private String DEPOT_ID;
	private String ORDER_SN;
	private String MAJOR_SEND_WAREH_CODE;
	private String SHIP_CODE;
	private String DISTRATE;
	private String DWARH_CODE;
	private String AGDIST;
	private String SHIP_SN;
	private String DISTQTY;
	private String COUNTRY;
	private String CITY;
	private String PROVINCE;
	private String COUNTY;
	private String STYPE;
	private String SHIP_FLAG;
	private String PROD_ID;

	public String getDEPOT_ID() {
		return DEPOT_ID;
	}

	public void setDEPOT_ID(String dEPOT_ID) {
		DEPOT_ID = dEPOT_ID;
	}

	public String getORDER_SN() {
		return ORDER_SN;
	}

	public void setORDER_SN(String oRDER_SN) {
		ORDER_SN = oRDER_SN;
	}

	public String getMAJOR_SEND_WAREH_CODE() {
		return MAJOR_SEND_WAREH_CODE;
	}

	public void setMAJOR_SEND_WAREH_CODE(String mAJOR_SEND_WAREH_CODE) {
		MAJOR_SEND_WAREH_CODE = mAJOR_SEND_WAREH_CODE;
	}

	public String getSHIP_CODE() {
		return SHIP_CODE;
	}

	public void setSHIP_CODE(String sHIP_CODE) {
		SHIP_CODE = sHIP_CODE;
	}

	public String getDISTRATE() {
		return DISTRATE;
	}

	public void setDISTRATE(String dISTRATE) {
		DISTRATE = dISTRATE;
	}

	public String getDWARH_CODE() {
		return DWARH_CODE;
	}

	public void setDWARH_CODE(String dWARH_CODE) {
		DWARH_CODE = dWARH_CODE;
	}

	public String getAGDIST() {
		return AGDIST;
	}

	public void setAGDIST(String aGDIST) {
		AGDIST = aGDIST;
	}

	public String getSHIP_SN() {
		return SHIP_SN;
	}

	public void setSHIP_SN(String sHIP_SN) {
		SHIP_SN = sHIP_SN;
	}

	public String getDISTQTY() {
		return DISTQTY;
	}

	public void setDISTQTY(String dISTQTY) {
		DISTQTY = dISTQTY;
	}

	public String getCOUNTRY() {
		return COUNTRY;
	}

	public void setCOUNTRY(String cOUNTRY) {
		COUNTRY = cOUNTRY;
	}

	public String getCITY() {
		return CITY;
	}

	public void setCITY(String cITY) {
		CITY = cITY;
	}

	public String getPROVINCE() {
		return PROVINCE;
	}

	public void setPROVINCE(String pROVINCE) {
		PROVINCE = pROVINCE;
	}

	public String getCOUNTY() {
		return COUNTY;
	}

	public void setCOUNTY(String cOUNTY) {
		COUNTY = cOUNTY;
	}

	public String getSTYPE() {
		return STYPE;
	}

	public void setSTYPE(String sTYPE) {
		STYPE = sTYPE;
	}

	public String getSHIP_FLAG() {
		return SHIP_FLAG;
	}

	public void setSHIP_FLAG(String sHIP_FLAG) {
		SHIP_FLAG = sHIP_FLAG;
	}

	public String getPROD_ID() {
		return PROD_ID;
	}

	public void setPROD_ID(String pROD_ID) {
		PROD_ID = pROD_ID;
	}

	@Override
	public String toString() {
		return "OrderToShipProviderBeanERP [DEPOT_ID=" + DEPOT_ID + ", ORDER_SN=" + ORDER_SN + ", MAJOR_SEND_WAREH_CODE=" + MAJOR_SEND_WAREH_CODE + ", SHIP_CODE=" + SHIP_CODE + ", DISTRATE=" + DISTRATE + ", DWARH_CODE=" + DWARH_CODE + ", AGDIST=" + AGDIST + ", SHIP_SN=" + SHIP_SN + ", DISTQTY="
				+ DISTQTY + ", COUNTRY=" + COUNTRY + ", CITY=" + CITY + ", PROVINCE=" + PROVINCE + ", COUNTY=" + COUNTY + ", STYPE=" + STYPE + ", SHIP_FLAG=" + SHIP_FLAG + ", PROD_ID=" + PROD_ID + "]";
	}

}