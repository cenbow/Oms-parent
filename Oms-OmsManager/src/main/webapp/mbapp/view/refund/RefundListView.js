Ext.define("MB.view.refund.RefundListView", {
	extend : "Ext.container.Viewport",
	alias : 'widget.refundListView',
	requires: ['MB.view.refund.RefundList'],
	layout : "border",
//	collapsible : true,
	defaults : {
//		collapsible : true,
		split : true
	},
	items : [ {
		xtype: 'refundList',
		region : "center",
		collapsible: false
	}]
});