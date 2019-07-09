package com.work.shop.oms.express.bean;

import java.util.ArrayList;
import java.util.List;
/**
 * 韵达物流数据返回结果
 * @author huangl
 *
 */
public class YDExpressInfo {

	// 不同节点
	private List<YDExpressStep> steps = new ArrayList<YDExpressStep>();
	
	// 运单号
	private String mailno;
	
	private String result;
	private String time;
	private String remark;
	private String status;
	private String weight;
	
	
	public List<YDExpressStep> getSteps() {
		return steps;
	}
	public void setSteps(List<YDExpressStep> steps) {
		this.steps = steps;
	}
	public String getMailno() {
		return mailno;
	}
	public void setMailno(String mailno) {
		this.mailno = mailno;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	
}

