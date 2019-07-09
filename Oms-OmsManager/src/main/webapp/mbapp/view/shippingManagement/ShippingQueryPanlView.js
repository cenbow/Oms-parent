/**
 * 承运商管理：查询面板
 */
var pageSize = 20;
Ext.define("MB.view.shippingManagement.ShippingQueryPanlView", {
	extend : "Ext.form.Panel",
	alias : 'widget.shippingQueryPanlView',
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
				id : 'shippingManagementPanlViewShippingCode',
				name : 'shippingCode',
				fieldLabel : "<b>承运商编码 </b>",
				width : 200,
				columnWidth : .25
			},{
				xtype : 'textfield',
				id : 'shippingManagementPanlViewShippingName',
				name : 'shippingName',
				fieldLabel : "<b>承运商名称 </b>",
				width : 200,
				columnWidth : .25
			},{
				xtype : 'supportCodCombo',
				id : 'shippingManagementPanlViewSupportCod',
				name : 'supportCod',
				width : 200,
				columnWidth : .25
			},{
				xtype : 'enabledCombo',
				id : 'shippingManagementPanlViewEnabled',
				name : 'enabled',
				width : 200,
				columnWidth : .25
			}]
		}];
		me.buttons = [ {
			text : '添加承运商',
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

