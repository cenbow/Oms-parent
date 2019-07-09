Ext.define("MB.view.goodsReturnChange.GoodsSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.goodsReturnChangeGoodsSetModule',
	id:'goodsReturnChangeGoodsSetModule',
	width: '100%',
	frame: true,
	title:'退货商品信息',
	head:true,
	layout:'fit',
	margin:10,
    bodyPadding:10,
	buttonAlign : 'center',// 按钮居中
//	fieldDefaults: {
//		labelAlign: 'right'
//	},
	collapsible:true,
//	collapsed: true,
//	bodyBorder:false,
//	border:false,
//	style:'border-width:0 0 0 0;',
	initComponent: function () {
		this.items = [{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			columnWidth: 1,
			labelWidth: 200,
			width: '100%',
			items:  [ {
//				title:'商品信息&nbsp;&nbsp;&nbsp;',
				xtype : "goodsReturnChangeGoodsList",
				border:true,
				margins:'5 5 5 5'
			} ]
		} ];
//		this.callParent(arguments);
		var me = this;
		me.callParent(arguments);
	}
});