Ext.define("MB.view.handOrder.HandOrderPanlView", {
	extend : "Ext.form.Panel",
	alias : 'widget.handOrderPanlView',
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
				id : 'handOrderPanlViewBillNo',
				name : 'batchNo',
				fieldLabel : '<b>批次号</b>',
				columnWidth : .2
			},{
				xtype : 'processStatusCombo',
				id : 'handOrderPanlViewProcessStatus',
				name : 'processStatus',
				columnWidth : .2
			},{
				xtype : 'createOrderStatusCombo',
				id : 'handOrderPanlViewCreateOrderStatus',
				name : 'createOrderStatus',
				columnWidth : .2
			},{
				xtype : 'textfield',
				id : "handOrderPanlViewStartTime",
				name : 'startTime',
				fieldLabel : '<b>导入时间段(起)</b>',
				listeners : {
					render : function(p) {p.getEl().on('click',function() {
											WdatePicker({
												startDate : '%y-%M-%d 00:00:00',
												dateFmt : 'yyyy-MM-dd HH:mm:ss'
											});
										});
					}
				},
				columnWidth : .2
			},{
				xtype : 'textfield',
				id : "handOrderPanlViewEndTime",
				name : 'endTime',
				fieldLabel : '<b>导入时间段(止)</b>',
				listeners : {
					render : function(p) {p.getEl().on('click',function() {
											WdatePicker({
												startDate : '%y-%M-%d 23:59:59',
												dateFmt : 'yyyy-MM-dd HH:mm:ss'
											});
										});
					}
				},
				columnWidth : .2
			}]
		}];
		me.buttons = [ {
			text : '查询',
			action : 'search'
		}, {
			text : '重置',
			action : 'reset'
		}, {
			text : '导入订单',
			action : 'openImport'
		} ];
		me.callParent(arguments);
	}
});

