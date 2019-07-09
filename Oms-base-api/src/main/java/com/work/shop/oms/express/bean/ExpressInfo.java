package com.work.shop.oms.express.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 物流信息
 * @author lemon
 */
public class ExpressInfo {
	
	private List<ExpressContent> data = new ArrayList<ExpressContent>();

	private String message;
	
	private String nu;
	
	private String companytype;

	/**
	 * 承运商编码
	 */
	private String com;
	
	//结果状态（返回0、1和408。0，表示无查询结果；1，表示查询成功 ） 
	private Integer status;
	
	private String condition;
	
	private String codenumber;

	/**
	 * 	快递单的状态（返回0、2、3、4。0表示状态未知（不能根据查询结果判断出单号状态）
	 *  2表示疑难件（目前暂时只支持申通）；3表示已签收；4表示已退货）
	 */
	private Integer state;

	public List<ExpressContent> getData() {
		return data;
	}

	public void setData(List<ExpressContent> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getCompanytype() {
		return companytype;
	}

	public void setCompanytype(String companytype) {
		this.companytype = companytype;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCodenumber() {
		return codenumber;
	}

	public void setCodenumber(String codenumber) {
		this.codenumber = codenumber;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
