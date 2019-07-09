Ext.define("MB.view.exchOrder.ExchangeDetailView", {
	extend : "Ext.container.Viewport",
	alias : 'widget.exchangeDetailView',
	requires: ['MB.view.exchOrder.ExchangeNorth', 'MB.view.exchOrder.ExchangeSouth', 'MB.view.exchOrder.ExchangeCenter', 'MB.view.exchOrder.ExchangeWest'],
	layout : "border",
//	collapsible : true,
	defaults : {
//		collapsible : true,
		split : true
	},
	items : [ {
		xtype: 'exchangeNorth',
		region : "north",
		align:'center',
		height : 40,
		collapsible: false
	},{
		title : "换单概要",
		xtype: 'exchangeWest',
		region : "west",
		height : 60,
		collapsible: true
	},
		{
		xtype: 'exchangeCenter',
//		title : "换单信息",
		region : "center",
		collapsible: true
	} , {
		xtype : 'exchangeSouth',
		region : "south",
		title : '操作日志',
		height: 300,
		collapsed: true,//初始不展开
		collapsible: true,
		titleCollapse:true
	}]
});