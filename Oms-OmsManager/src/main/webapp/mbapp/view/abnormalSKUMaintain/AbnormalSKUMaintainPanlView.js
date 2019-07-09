/**
 * 异常SKU调整：查询面板
 */
var pageSize = 20;
Ext.define("MB.view.abnormalSKUMaintain.AbnormalSKUMaintainPanlView", {
	extend : "Ext.form.Panel",
	alias : 'widget.abnormalSKUMaintainPanlView',
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
				id : 'abnormalSKUMaintainPanlViewOrderOutSn',
				name : 'orderOutSn',
				fieldLabel : "<b>外部订单号</b>",
				width : 200,
				columnWidth : .3
			},{
				xtype : 'channelCombo',
				id : 'abnormalSKUMaintainPanlViewChannelCombo',
				name : 'channelType',
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
		}];
		me.callParent(arguments);
	}
});

