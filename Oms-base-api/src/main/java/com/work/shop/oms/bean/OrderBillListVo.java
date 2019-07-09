package com.work.shop.oms.bean;

import com.work.shop.oms.bean.OrderBillList;

public class OrderBillListVo extends OrderBillList {
	
	private int start = 0;												//分页 起始页
	
	private int limits = 20;
	
	//'业务类型:0未定义 1.订单结算2.订单货到付款结算3.退单退款方式结算',
	public String getBillTypeStr(){
		if(null==this.getBillType()){
			return "";
		}
		StringBuffer str=new StringBuffer();
		switch (this.getBillType()) {
		case 0:
			str.append("未定义");
			break;
		case 1:
			str.append("订单结算");
			break;
		case 2:
			str.append("订单货到付款结算");
			break;
		case 3:
			str.append("退单退款方式结算");
			break;
		case 4:
			str.append("保证金");
			break;
		case 5:
			str.append("调整日志");
			break;
		default:
			str.append(this.getBillType().toString());
			break;
		};
		return str.toString();
		
	}
	
	public String getIsSycStr(){
		if(null==this.getIsSync()) {
			return "";
		}
		StringBuffer str=new StringBuffer();	
		switch (this.getIsSync()) {
		case 0:
			str.append("未同步 ");
			break;
		case 1:
			str.append("已经同步");
			break;
		case 2:
			str.append("作废");
			break;
		case 3:
			str.append("部分同步");
			break;
		case 4:
			str.append("同步失败");
			break;
		case 9:
			str.append("同步中");
			break;
	
		default:
			str.append(this.getBillType().toString());
			break;
		};
		return str.toString();
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimits() {
		return limits;
	}

	public void setLimits(int limits) {
		this.limits = limits;
	}	
	
}
