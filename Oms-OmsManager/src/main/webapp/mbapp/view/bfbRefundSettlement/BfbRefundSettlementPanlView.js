/**
 * 邦付宝退款批量结算：查询面板
 */
var pageSize = 20;
Ext.define("MB.view.bfbRefundSettlement.BfbRefundSettlementPanlView", {
	extend : "Ext.form.Panel",
	alias : 'widget.bfbRefundSettlementPanlView',
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
				/*xtype : 'textfield',
				id : 'bfbRefundSettlementPanlViewChannelCode',
				name : 'channelCode',
				fieldLabel : "<b>渠道号 </b>",
				width : 200,*/
				columnWidth : .2
			},{
				xtype : 'textfield',
				id : 'bfbRefundSettlementPanlViewBillNo',
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
			action : 'doSettlement'
		}, {
			text : '删除调整单',
			action : 'delSettlement'
		} ];
		me.callParent(arguments);
	}
});

