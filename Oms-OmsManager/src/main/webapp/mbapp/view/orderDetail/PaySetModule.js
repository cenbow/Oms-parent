Ext.define("MB.view.orderDetail.PaySetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.paySetModule',
	id:'paySetModule',
	title:'付款信息',
	width: '100%',
//	head:true,
	margin:5,
	bodyPadding:5,
	buttonAlign : 'center',// 按钮居中
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
	initComponent: function () {
		var me = this;
		me.items = [ {
				xtype: 'payDetail'
			}, {
				xtype: 'fieldcontainer',
				itemId : 'showTotalFee',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				margin : '10 0 0 0',
				columnWidth: 1,
				defaultType: 'displayfield',
			} , {
				xtype: 'fieldcontainer',
				itemId : 'totalFee',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				margin : '10 0 0 0',
				columnWidth: 1,
				defaultType: 'displayfield',
			} , {
				xtype: 'fieldcontainer',
				itemId : 'showTotalPayable',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				margin : '10 0 0 0',
				columnWidth: 1,
				defaultType: 'displayfield',
			} , {
				xtype: 'fieldcontainer',
				itemId : 'totalPayable',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				margin : '10 0 0 0',
				columnWidth: 1,
				defaultType: 'displayfield',
			} , {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "displayfield",  columnWidth: 1 }
				]
			} , {
				 // 退单相关信息
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				itemId : 'returnTransPrice',
				columnWidth: 1,
				labelWidth: 200
			} , {
				 // 退单相关信息
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				itemId : 'returnSettPrice',
				columnWidth: 1,
				labelWidth: 200
			}
		];
		/*me.tools=[{
			type: 'gear',
			disabled : true,
			tooltip : '修改费用信息',
			action: 'payEdit',
			scope: me
		}];*/
		me.callParent(arguments);
	}
});