Ext.define("MB.view.orderDetail.CancelDistributeOrder", {
	extend: "Ext.window.Window",
	alias: "widget.cancelDistributeOrder",
	requires : [ 'MB.view.common.OrderCustomDefineCombo'],
	title: "交货单操作-取消(作废)订单",
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
			params = { "type" : 8};
			Ext.apply(store.proxy.extraParams, params);
		});
		orderCustomDefineCombo.getStore().load();
		this.items = {
				xtype: "form",
				margin: 5,
				border: false,
				frame: true,
				url : basePath + '/custom/orderStatus/sonCancel',
				fieldDefaults: {
					labelAlign: 'left',
					labelWidth: 70
				},
				items: [{
						xtype : 'radiogroup',
						name: "type",
						margin: '15 0 0 0',
						fieldLabel: "是创建退单",
						width: 260,
						items: [
							{ boxLabel: '创建', name: 'type', inputValue: '1', checked: true},
							{ boxLabel: '不创建', name: 'type', inputValue: '2'}
						]
					},
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
					orderCustomDefineCombo
				]
			};
			this.buttons = [
				{ text: "保存", action: "cancelOrder", handler : me.cancelOrder },
				{ text: "关闭", handler : function () { this.up("window").close();} }
				
			];
		me.callParent(arguments);
		orderCustomDefineCombo.setValue("8011");
	},
	cancelOrder : function (btn) {
		var me = this;
		var win = btn.up("window");
		form = win.down("form");
		if (!form.isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}
		var param = form.getValues();
		Ext.Ajax.request({
			url:  basePath + '/custom/orderStatus/sonCancel',
			params: param,
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				var code = respText.code;
				if (code=='1') {
					win.close();
					Ext.getCmp('orderDetailCenter').initData();
				} else {
					Ext.msgBox.remainMsg('取消(作废)交货单', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('取消(作废)交货单', respText.msg, Ext.MessageBox.ERROR);
			}
		});
	}
});