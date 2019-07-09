/**
 * 订单金额调整：查询面板
 */
var pageSize = 20;
Ext.define("MB.view.orderAmountAdjustment.OrderAmountAdjustmentPanlView", {
	extend : "Ext.form.Panel",
	alias : 'widget.orderAmountAdjustmentPanlView',
	frame : true,
	head : true,
	buttonAlign : 'center',// 按钮居中
	fieldDefaults : {
		labelAlign : 'right'
	},
	collapsible : true,
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 80,
			margin: '20 20 0 0',
			items: [{
				columnWidth : .2
			},{
				xtype : 'textfield',
				id : 'orderAmountAdjustmentPanlViewBillNo',
				name : 'billNo',
				fieldLabel : "<b>调整单号 </b>",
				width : 200,
				columnWidth : .3
			},{
				xtype : 'isSyncCombo',
				id : 'bfbRefundSettlementPanlViewIsSync',
				name : 'isSync',
				width : 200,
				columnWidth : .3
			}]
		}];
		me.buttons = [ {
			text : '查询',
			action : 'search'
		}, {
			text : '重置',
			action : 'reset'
		}, {
			text : '生成调整单',
			action : 'openImport'
		}, {
			text : '执行调整单',
			action : 'doAdjust'
		}, {
			text : '删除调整单',
			action : 'delAdjust'
		} ];
		me.callParent(arguments);
	}
});

