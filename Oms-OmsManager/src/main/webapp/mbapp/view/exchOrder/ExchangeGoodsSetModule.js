Ext.define("MB.view.exchOrder.ExchangeGoodsSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.exchangeGoodsSetModule',
	id:'exchangeGoodsSetModule',
	width: '100%',
	frame: true,
	title:'换单商品信息&nbsp;&nbsp;&nbsp;',
	head:true,
	layout:'fit',
	autoWidth:true,
	autoHeight:true,
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
		this.items = [ 
			{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			width:'100%',
			items:  [ {
//				title:'商品信息&nbsp;&nbsp;&nbsp;',
				xtype : "goodDetail"
			} ,
			{xtype : "displayfield", name : 'total', fieldLabel : "<span style='color:black;font-weight:bold';>总计</span>",labelWidth:800 }]
		}];
//		this.callParent(arguments);
		this.tools=[{
			type: 'plus',
            tooltip : '添加商品',
            action: 'addGoods',
            //handler: me.onCloseClick,
            scope: me
		}];
		var me = this;
		me.callParent(arguments);
	}
});