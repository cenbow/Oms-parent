Ext.define("MB.view.orderEdit.PayEdit", {
	extend: "Ext.window.Window",
	alias: "widget.payEdit",
	requires: ['MB.view.orderDetail.PayEditForm'],
	title: "编辑付款信息",
	width: 600,
	height: 300,
	maskDisabled : false ,
	modal : true ,
	initComponent: function () {
		var me = this;
		this.items = {xtype:'payEditForm'};
		this.buttons = [
			{ text: "保存", handler : me.editDeliveryInfo },
			{ text: "关闭", handler : function () { this.up("window").close(); }}
		];
		this.callParent(arguments);
	},
	initPage: function (form) {
//		var orderStatus = Ext.getCmp("order.orderStatus").getValue();
//		var orderStatus = form.findField("orderStatus").getValue();
//		form.findField("orderStatus1").setValue(orderStatus);
	},
	editDeliveryInfo : function(btn) { // 添加选择商品至订单
		var win = btn.up("window");
		form = win.down("form");
		var me = this;
		console.dir(form.getValues());
		if (!form.isValid()) {
			Ext.msgBox.remainMsg('编辑邮费信息', "检查数据必填项是否填写", Ext.MessageBox.ERROR);
			return ;
		}
		console.dir(form.getValues());
		var json = {
			success : function(formPanel, action) {
				if (action.result.success == "false") {
					Ext.msgBox.remainMsg('编辑邮费信息', action.result.msg, Ext.MessageBox.ERROR);
				} else {
					Ext.msgBox.msg('编辑邮费信息', action.result.msg, Ext.MessageBox.INFO);
					var orderForm = Ext.getCmp('orderShow');
					orderForm.initData(orderForm);
					win.close();
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg('编辑邮费信息', action.result.msg, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		form.submit(json);
	}
});