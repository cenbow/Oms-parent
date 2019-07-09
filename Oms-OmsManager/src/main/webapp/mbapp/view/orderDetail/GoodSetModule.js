Ext.define("MB.view.orderDetail.GoodSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.goodSetModule',
	id:'goodSetModule',
	title:'商品信息',
	width: '100%',
//	layout:"fit",
	margin:5,
	bodyPadding:5,
//	head:true,
//	autoWidth:true,
//	autoHeight:true,
//	autoScroll : true,
	collapsible:true,
//	collapsed: true,
//	bodyBorder:false,
//	border:false,
//	style:'border-width:0 0 0 0;',
	initComponent: function () {
		var me = this;
		this.items = [
		{
			xtype: 'goodDetail'
		}, {
			xtype: 'fieldcontainer',
			itemId : 'total',
			labelStyle: 'font-weight:bold;padding:0;',
			columnWidth: 1
		}];
		this.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "goodDetail",
			model : 'MB.model.GoodDetailModel'
		});
		me.callParent(arguments);
	}
});