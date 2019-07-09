Ext.define("MB.view.goodsReturnChange.GoodsReturnChangeView", {
	extend : "Ext.container.Viewport",
	alias : 'widget.goodsReturnChangeView',
	requires: [/*'MB.view.goodsReturnChange.GoodsReturnChangeWest',*/'MB.view.goodsReturnChange.GoodsReturnChangeCenter','MB.view.goodsReturnChange.GoodsReturnChangeSouth'],
	layout : "border",
//	collapsible : true,
	defaults : {
//		collapsible : true,
		split : true
	},
	items : [/* {
		title : "退换货概要",
		titleCollapse:true,
		xtype: 'goodsReturnChangeWest',
		region : "west",
		height : 60,
		collapsible: true
	} , */{
		xtype: 'goodsReturnChangeCenter',
		title : "退换货申请信息",
		region : "center",
		collapsible: false
	} , {
		xtype : 'goodsReturnChangeSouth',
		region : "south",
		title : '操作日志',
		height: 200,
		collapsible: true,
		collapsed: true//初始不展开
	}]
});