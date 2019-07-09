/**
 * 支付方式管理：查询面板
 */
var pageSize = 20;
Ext.define("MB.view.paymentManagement.PaymentQueryPanlView", {
	extend : "Ext.form.Panel",
	alias : 'widget.paymentQueryPanlView',
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
				xtype : 'textfield',
				id : 'paymentQueryPanlViewPayCode',
				name : 'payCode',
				fieldLabel : "<b>支付方式编码 </b>",
				width : 200,
				columnWidth : .25
			},{
				xtype : 'textfield',
				id : 'paymentQueryPanlViewPayName',
				name : 'payName',
				fieldLabel : "<b>支付方式名称 </b>",
				width : 200,
				columnWidth : .25
			},{
				xtype : 'isOnlineCombo',
				id : 'paymentQueryPanlViewisOnline',
				name : 'isOnline',
				width : 200,
				columnWidth : .25
			},{
				xtype : 'enabledCombo',
				id : 'paymentQueryPanlViewEnabled',
				name : 'enabled',
				width : 200,
				columnWidth : .25
			}]
		}];
		me.buttons = [ {
			text : '添加支付方式',
			action : 'openAdd'
		}, {
			text : '查询',
			action : 'search'
		}, {
			text : '重置',
			action : 'reset'
		} ];
		me.callParent(arguments);
	}
});

