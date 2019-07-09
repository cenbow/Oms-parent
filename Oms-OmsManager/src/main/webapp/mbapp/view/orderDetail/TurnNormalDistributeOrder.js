Ext.define("MB.view.orderDetail.TurnNormalDistributeOrder", {
	extend: "Ext.window.Window",
	alias: "widget.turnNormalDistributeOrder",
	requires : [ 'MB.view.common.OrderCustomDefineCombo'],
	title: "交货单操作-返回正常单",
	width: '500',
	height: '310',
	layout: "fit",
	maskDisabled : false ,
	modal : true ,
	initComponent: function () {
		console.dir(parent.typeItems);
		var me = this;
		var orderCustomDefineCombo = Ext.widget("orderCustomDefineCombo");
		// 请求参数赋值
		orderCustomDefineCombo.getStore().on('beforeload', function (store){
			params = { "type" : 8};
			Ext.apply(store.proxy.extraParams, params);
		});
		orderCustomDefineCombo.getStore().load();
		this.items = {
				xtype: "form",
				margin: 5,
				border: false,
				frame: true,
				url : basePath + '/custom/orderStatus/sonNormal',
				fieldDefaults: {
					labelAlign: 'left',
					labelWidth: 90
				},
				items: [
					{
						xtype: 'textareafield',
						name : 'message',
						fieldLabel : '备注',
						grow : true,
						width: 420,
						height : 90
					},
					{
						xtype : 'radiogroup',
						name: "hasOccupyStock",
						fieldLabel: "是否占用库存",
						width: 260,
						items: [
							{ boxLabel: '是', name: 'hasOccupyStock', inputValue: '1' },
							{ boxLabel: '否', name: 'hasOccupyStock', inputValue: '0', checked: true}
						]
					},
					{
						xtype: 'hidden',
						name : 'orderSn',
						value : parent.orderSnForOperate
					},
					{
						xtype: 'displayfield',
						value : parent.desc
					},
					{
						xtype : 'radiogroup',
						name: "type",
						fieldLabel: "选择操作类别",
						width: 420,
						items: parent.typeItems
					}
				]
			};
			this.buttons = [
				{ text: "保存", action: "cancelOrder", handler : me.cancelOrder },
				{ text: "关闭", handler : function () { this.up("window").close();} }
			];
		me.callParent(arguments);
	},
	cancelOrder : function (btn) {
		var me = this;
		var win = btn.up("window");
		form = win.down("form");
		if (!form.isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}
		var json = {
			success : function(formPanel, action) {
				if (action.result.success == "false") {
					Ext.msgBox.remainMsg('交货单返回正常单', action.result.msg, Ext.MessageBox.ERROR);
				} else {
					win.close();
					Ext.getCmp('orderDetailCenter').initData();					
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg('交货单返回正常单', action.response.responseText, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		form.submit(json);
	}
});