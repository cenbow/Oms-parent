Ext.define("MB.view.orderDetail.OrderDetailViewPort", {
	extend : "Ext.container.Viewport",
	alias : 'widget.orderDetailViewPort',
	requires: ['MB.view.orderDetail.OrderDetailNorth',
	           'MB.view.orderDetail.OrderDetailSouth',
	           'MB.view.orderDetail.OrderDetailCenter',
		         'MB.view.orderEdit.C2xPropertyEdit'],
	layout : "border",
	defaults : {
		collapsible : true,
		split : true
	},
	initComponent: function () {
		this.items = [ {
			xtype : 'orderDetailNorth',
			region : 'north',
			collapsible: false,
			align:'center'
		}, {
			xtype: 'orderDetailCenter',
			collapsible: false,
			region : 'center'
		} , {
			xtype : 'orderDetailSouth',
			region : 'south'
		}];
		this.callParent(arguments);
	}
	
});