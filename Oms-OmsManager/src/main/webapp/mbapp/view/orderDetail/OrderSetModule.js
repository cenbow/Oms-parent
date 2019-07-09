Ext.define("MB.view.orderDetail.OrderSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.orderSetModule',
	id:'orderSetModule',
	title:'基本信息',
	width: '100%',
//	frame: true,
	head:true,
	margin:5,
	bodyPadding:5,
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
	initComponent: function () {
		var me = this;
		this.items = [ {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield", name : 'masterOrderSn', fieldLabel : "订单号", columnWidth: .33 },
				{xtype : "displayfield", name : 'orderOutSn', fieldLabel : "外部交易号", columnWidth: .33} ,
				{xtype : "displayfield", name : 'ordertotalstatusStr', fieldLabel : "订单状态", columnWidth: .33,
					renderer : function (value) {
						return "<span style='color:red;'>"+value+"</span>";
					}
				}
			]
		} ,
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			labelWidth: 200,
			defaultType: 'displayfield',
			items: [
				{xtype : "displayfield", name : 'channelName', fieldLabel : "订单来源", columnWidth: .33 } ,
				{xtype : "displayfield", name : 'clearTime', fieldLabel : "结算时间", columnWidth: .33 } ,
				{xtype : "displayfield", name : 'userName', fieldLabel : "购货人", columnWidth: .33}
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
				{xtype : "displayfield", name : 'addTime', fieldLabel : "下单时间", columnWidth: .33},
				{xtype : "displayfield", name : 'transType', fieldLabel : "订单交易类型", columnWidth: .33,
					renderer : function (value) {
						if(value=='1'){
							return '款到发货';
						}else if(value=='2'){
							return '货到付款';
						}else if(value=='3'){
							return '担保交易';
						}else{
							return '';
						}
					}
				} ,
				//分仓发货状态（0，未分仓 1，已分仓未通知 2，已分仓已通知）
				{xtype : "displayfield", name : 'depotStatus', fieldLabel : "分仓发货状态", columnWidth: .33,
					renderer : function (value) {
						if(value==2){
							return "已分仓已通知";
						}else if(value==1){
							return "已分仓未通知";
						}else{
							return "未分仓";
						}
					}
				}
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
				{xtype : "displayfield", name : 'orderType', fieldLabel : "订单类型", columnWidth: .33,
					renderer : function (value) {
						if(value=='0'){
							return "正常订单";
						}else if(value=='1'){
							return "补发订单";
						}else if(value=='2'){
							return "换货订单";
						}else{
							return "";
						}
					}	
				},
				{xtype : "displayfield", name : 'relatingReturnSn', fieldLabel : "关联退货单号", columnWidth: .33,
					renderer : function (v) {
						if (v != null && v != '') {
							var url = order_return_url + v
							return '<a href="'+url +'" target="_blank">' + v +'</a>';
						}
						return "";
					}
				},
				{xtype : "displayfield", name : 'prName', fieldLabel : "促销信息", columnWidth: .33}
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
				{xtype : "displayfield", name : 'relatingOriginalSn', fieldLabel : "关联原订单号", columnWidth: .33,
					renderer : function (v) {
						if (v != null && v != '') {
							var array = v.split('=');
							var url = basePath + 'custom/orderInfo/orderDetail?masterOrderSn=' + v
							return '<a href="'+url +'" target="_blank">' + array[1] +'</a>';
						}
						return "";
					}
				},
				{xtype : "displayfield", name : 'invoicesOrganization', fieldLabel : "单据组织", columnWidth: .33 } ,
				{xtype : "displayfield", name : 'orderCategory', fieldLabel : "订单种类", columnWidth: .33,
					renderer : function (value) {
						if(value==1){
							return "零售";
						}else if(value==2){
							return "物资领用";
						}else if(value==3){
							return "其他出库";
						}else if(value==4){
							return "c2b定制";
						}
					}				
				}
			]
		},
		/*{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				//是否为团购订单( 0:否  1;是)
				{xtype : "displayfield", name : 'isGroup', fieldLabel : "团购订单", columnWidth: .33,
					renderer : function (value) { 
						if(value==1){
							return "<span style='color:red;'>是</span>";
						}else{
							return "<span style='color:red;'>否</span>";
						}
					}
				},
				//是否为预售商品(0:否 1:是)
				{xtype : "displayfield", name : 'isAdvance', fieldLabel : "订单含预售商品", columnWidth: .33,
					renderer : function (value) { 
						if(value==1){
							return "<span style='color:red;'>是</span>";
						}else{
							return "<span style='color:red;'>否</span>";
						}
					}
				}, {

					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					defaultType: 'displayfield',
					labelWidth: 200,
					items: [
						//原因： S(零售) ；J (物资领用 )； T(其它出库 )
						{xtype : "displayfield", name : 'reason', fieldLabel : "原因", columnWidth: 1,
							renderer : function (value) {
								if(value=="S"){
									return "零售";
								}else if(value=="J"){
									return "物资领用";
								}else if(value=="T"){
									return "其它出库";
								}
							}
						}
					]
				
				}
			]
		} ,*/
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield", name : 'cancelReason', fieldLabel : "取消订单原因", columnWidth: 1}
			]
		} ];
		me.tools=[{
			disabled : true,
			type: 'refresh',
			handler: function(){
				me.up('orderCenter').initData();
				Ext.msgBox.msg('刷新', "刷新页面成功！", Ext.MessageBox.INFO);
			}
		}];
		me.callParent(arguments);
	}
});