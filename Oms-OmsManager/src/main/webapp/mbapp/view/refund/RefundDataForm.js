Ext.define("MB.view.refund.RefundDataForm", {
	extend: "Ext.form.Panel",
	alias: 'widget.refundDataForm',
	id:'refundDataForm',
	width: '100%',
	frame: true,
//	title:'商品信息&nbsp;&nbsp;&nbsp;',
	head:true,
	layout:'fit',
	margin:10,
	buttonAlign : 'center',// 按钮居中
//	fieldDefaults: {
//		labelAlign: 'right'
//	},
//	collapsible:true,
	initComponent: function () {
		this.height=document.body.clientHeight*7/10;
		this.items = [{
			xtype : "refundGrid"
		} ];
//		this.callParent(arguments);
		var me = this;
		me.callParent(arguments);
	}
});