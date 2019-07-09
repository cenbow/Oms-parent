Ext.define("MB.view.orderDetail.DeliverySetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.deliverySetModule',
	id:'deliverySetModule',
	title: '配送信息',
	width: '100%',
//	frame: true,
//	head:true,
	margin:5,
	bodyPadding:5,
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
	initComponent: function () {
		var me = this;
		this.items = [ {
			xtype: 'deliveryDetail'
		}
		];
		me.callParent(arguments);
	}
});