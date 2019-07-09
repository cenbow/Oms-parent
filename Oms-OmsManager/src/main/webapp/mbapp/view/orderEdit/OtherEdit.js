Ext.define("MB.view.orderEdit.OtherEdit", {
	extend: "Ext.window.Window",
	alias: "widget.otherEdit",
	requires: ['MB.view.orderDetail.OtherEditForm'],
	title: "编辑其他信息",
	width: 500,
	height: 300,
	maskDisabled : false ,
	modal : true ,
	initComponent: function () {
		var me = this;
		this.items = {
			xtype: "otherEditForm"
		};
		this.buttons = [
			{ text: "保存", action: "save", handler : me.editOrderOther },
			{ text: "关闭", handler : function () { this.up("window").close(); }}
		];
		this.callParent(arguments);
	},
	initPage: function (form) {
//		var orderStatus = Ext.getCmp("order.orderStatus").getValue();
//		var orderStatus = form.findField("orderStatus").getValue();
//		form.findField("orderStatus1").setValue(orderStatus);
	},
	editOrderOther: function(btn) { // 添加选择商品至订单
		var win = btn.up("window");
		form = win.down("form");
		console.dir(form.getValues());
		var me = this;
		var json = {
			success : function(formPanel, action) {
				if (action.result.success == "false") {
					Ext.msgBox.remainMsg('编辑其他信息', action.result.msg, Ext.MessageBox.ERROR);
				} else {
					Ext.msgBox.msg('编辑其他信息', action.result.msg, Ext.MessageBox.INFO);
					var orderForm = Ext.getCmp('orderShow');
					orderForm.initData(orderForm);
					win.close();
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg('编辑其他信息', action.result.msg, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		form.submit(json);
	}
});