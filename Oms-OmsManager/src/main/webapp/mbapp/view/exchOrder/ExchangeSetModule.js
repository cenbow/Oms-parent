Ext.define("MB.view.exchOrder.ExchangeSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.exchangeSetModule',
	id:'exchangeSetModule',
	width: '100%',
	frame: true,
	title:'换单基本信息&nbsp;&nbsp;&nbsp;',
	head:true,
	layout:'column',
	buttonAlign : 'center',// 按钮居中
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
	//collapsed: true,
	initComponent: function () {
		this.items = [ {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield", name : 'masterOrderSn', fieldLabel : "换货单号", columnWidth: .33,renderer: function(value) {
					if (value) {
						var url = order_info_url+"?masterOrderSn=" + value +"&isHistory=" + isHistory;
						return "<a href=" +url + " target='_blank' >" + value + "</a>";
					}
				}},
				{xtype : "displayfield", name : 'ordertotalstatusStr', fieldLabel : "订单状态", columnWidth: .33,
					renderer : function (value) { 
						if(!value){
							return "<span style='color:red;'>申请中</span>";
						}else{
			        		return "<span style='color:red;'>"+value+"</span>";
						}
		        	}
				},
				{xtype : "displayfield", name : 'addTime', fieldLabel : "下单时间", columnWidth: .33}
					
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield", name : 'userName', fieldLabel : "收货人", columnWidth: .33},
				{xtype : "displayfield", name : 'clearTime', fieldLabel : "结算时间", columnWidth: .33 } ,
				{xtype : "displayfield", name : 'userPayTime', fieldLabel : "付款时间", columnWidth: .33 } 
			]
		} ,
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
			    {xtype : "commonsystemPayment", name : 'payMent', fieldLabel : "支付方式", columnWidth: .33,labelWidth:100 } ,
				{xtype : "commoncommonStatus", name : 'isAgent', fieldLabel : "是否代理换货", columnWidth: .33 ,labelWidth:100} ,
//				{xtype : "displayfield", name : 'orderShipName', fieldLabel : "配送方式", columnWidth: .33}
				{xtype : "displayfield", name : 'referer', fieldLabel : "订单来源", columnWidth: .33 } 
			]
		} ,
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
//				{xtype : "displayfield", name : 'deliveryTime', fieldLabel : "发货时间", columnWidth: .33},
				
				{xtype : "displayfield", name : 'relatingOriginalSn', fieldLabel : "关联原订单号", columnWidth: .33,renderer: function(value) {
					if (value) {
						var array = value.split('=');
						var url = order_info_url +"?masterOrderSn="+masterOrderSn+"&orderSn="+ value +"&isHistory=" + isHistory;
						return "<a href=" +url + " target='_blank' >" + array[1] + "</a>";
					}
				}},
				{xtype : "displayfield", name : 'relatingReturnSn', fieldLabel : "关联退单号", columnWidth: .33,renderer: function(value) {
					if (value) {
						var url = order_return_url + value +"&isHistory=" + isHistory;
						return "<a href=" +url + " target='_blank' >" + value + "</a>";
					}
				}} ,
				{xtype : "displayfield", name : 'orderType', fieldLabel : "订单类型", columnWidth: .33,
					renderer:function(value){
						return "换货单";
					}
				
				}
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				
				{xtype : "displayfield", name : 'relatingRemoneySn', fieldLabel : "关联退款单号", columnWidth: .33 } 
			]
		} 
		];
		var me = this;
        me.callParent(arguments);
	},
    onCloseClick: function () {
        if (this.ownerCt) {
            this.ownerCt.remove(this, true);
        }
    }
});