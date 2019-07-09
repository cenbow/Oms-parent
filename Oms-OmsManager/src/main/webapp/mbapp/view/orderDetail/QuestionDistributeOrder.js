Ext.define("MB.view.orderDetail.QuestionDistributeOrder", {
	extend: "Ext.window.Window",
	alias: "widget.questionDistributeOrder",
	requires : [ 'MB.view.common.OrderCustomDefineCombo'],
	title: "交货单操作-设置问题单",
	width: '500',
	height: '310',
	layout: "fit",
	maskDisabled : false ,
	modal : true ,
	initComponent: function () {
		var me = this;
		var orderCustomDefineCombo = Ext.widget("orderCustomDefineCombo");
		// 请求参数赋值
		orderCustomDefineCombo.getStore().on('beforeload', function (store){
			params = { "type" : 3,"display" : 1};
			Ext.apply(store.proxy.extraParams, params);
		});
		orderCustomDefineCombo.getStore().load();
		this.items = {
				xtype: "form",
				margin: 5,
				border: false,
				frame: true,
				url : basePath + '/custom/orderStatus/sonQuestion',
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
						width: 400,
						height : 90
					},
					{
						xtype: 'hidden',
						name : 'orderSn',
						value : parent.orderSnForOperate
					},
					orderCustomDefineCombo,
					{
						xtype : 'radiogroup',
						name: "hasReleaseStock",
						fieldLabel: "是否释放库存",
						width: 260,
						items: [
							{ boxLabel: '是', name: 'hasReleaseStock', inputValue: '1' },
							{ boxLabel: '否', name: 'hasReleaseStock', inputValue: '0', checked: true}
						]
					},
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
					Ext.msgBox.remainMsg('交货单设置问题单', action.result.msg, Ext.MessageBox.ERROR);
				} else {
					win.close();
					Ext.getCmp('orderDetailCenter').initData();
				}
			},
			failure : function(formPanel, action, bb ,aa ) {
				console.dir(action);
				Ext.msgBox.remainMsg('交货单设置问题单', action.response.responseText, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		form.submit(json);
	}
});