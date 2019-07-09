Ext.define("MB.view.refund.RefundList", {
	extend: "Ext.form.Panel",
	alias: 'widget.refundList',
	id:'refundList',
	//title: '订单详情',
	width: '100%',
	height:'100%',
	frame: true,
	bodyStyle: {
		padding: '10px'
	},
	autoWidth:true,
	autoHeight:true,
	autoScroll : true,
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'right'
	},
	bodyBorder:true,
	initComponent: function () {
		this.items = [{
			xtype:'refundContent',
			titleCollapse:true
		},
		{
			xtype:'refundDataForm',
			titleCollapse:true
		}
		];
		
		

		this.callParent(arguments);
	}
	
});