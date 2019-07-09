Ext.define("MB.view.orderDetail.CancelOrder", {
	extend: "Ext.window.Window",
	alias: "widget.cancelOrder",
	requires : [ 'MB.view.common.OrderCustomDefineCombo'],
	title: "订单操作-取消(作废)订单",
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
				url : basePath + '/custom/orderStatus/cancel',
				fieldDefaults: {
					labelAlign: 'right',
					labelWidth: 100
				},
				items: [
					{
						margin: '10 0 0 0',
						xtype: 'displayfield',
						value: "<span style='color:red;'>&nbsp;&nbsp;&nbsp;&nbsp;友情提示，订单取消前，请确认 SAP 单据已经回转、删除。</span>"
					},
					{
						xtype : 'radiogroup',
						name: "type",
						margin: '10 0 0 0',
						fieldLabel: "是创建退单",
						width: 260,
						items: [
							{ boxLabel: '创建', name: 'type', inputValue: '1', checked: true},
							{ boxLabel: '不创建', name: 'type', inputValue: '2'}
						]
					},
					{
						xtype: 'hidden',
						name : 'masterOrderSn',
						value : parent.masterOrderSn
					},{
						xtype: 'hidden',
						name : 'orderType',
						value : parent.orderType
					},{
						xtype: 'hidden',
						name : 'relatingReturnSn',
						value : parent.relatingReturnSn
					},
					orderCustomDefineCombo,
					{
						xtype: 'textareafield',
						name : 'message',
						fieldLabel : '备注',
						grow : true,
						width: 400,
						height : 90
					}
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
		console.dir(param);
		Ext.Ajax.request({
			url:  basePath + '/custom/orderStatus/cancel',
			params: param,
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				var code = respText.code;
				if (code=='1') {
					win.close();
				} else {
					Ext.msgBox.remainMsg('取消(作废)订单', respText.msg, Ext.MessageBox.ERROR);
				}
				Ext.getCmp('orderDetailCenter').initData();
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('取消(作废)订单', respText.msg, Ext.MessageBox.ERROR);
			}
		});
	}
});