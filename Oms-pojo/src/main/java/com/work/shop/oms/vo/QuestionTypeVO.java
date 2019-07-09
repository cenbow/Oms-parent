package com.work.shop.oms.vo;

public class QuestionTypeVO {

	private String desc;
	
	private String name;
	
	private String type;

	public QuestionTypeVO() {
		
	}

	public QuestionTypeVO(String desc, String name, String type) {
		this.desc = desc;
		this.name = name;
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "QuestionTypeVO [desc=" + desc + ", name=" + name + ", type="
				+ type + "]";
	}
}
