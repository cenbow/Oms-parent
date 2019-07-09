Ext.define("MB.view.orderEdit.ShipEdit", {
	extend: "Ext.window.Window",
	alias: "widget.shipEdit",
	requires: ['MB.view.orderDetail.ShipEditForm'],
	title: "编辑收货信息",
	width: 650,
	height: 358,
	maskDisabled : false ,
	modal : true ,
	initComponent: function () {
		var me = this;
		this.items = {
			xtype: "shipEditForm"
		};
		this.buttons = [
			{ text: "保存", handler : me.editOrderAddress },
			{ text: "关闭", handler : function () { this.up("window").close(); }}
		];
		this.callParent(arguments);
	},
	editOrderAddress : function(btn) { // 添加选择商品至订单
		var win = btn.up("window");
		form = win.down("form");
		var me = this;
		console.dir(form.getValues());
		if (!form.isValid()) {
			Ext.msgBox.remainMsg('编辑收货信息', "检查数据必填项是否填写", Ext.MessageBox.ERROR);
			return ;
		}
		var tel = form.getForm().findField("tel").getValue();
		var mobile = form.getForm().findField("mobile").getValue();
		if ((tel == null || tel== '' || tel== '--') && (mobile == null || mobile== '')) {
			Ext.msgBox.remainMsg('编辑收货信息', "电话、手机至少选一", Ext.MessageBox.ERROR);
			return ;
		}
		form.getForm().findField("country").setValue(form.down("#selectCountry").getValue());
		form.getForm().findField("province").setValue(form.down("#selectProvince").getValue());
		form.getForm().findField("city").setValue(form.down("#selectCity").getValue());
		form.getForm().findField("district").setValue(form.down("#selectDistrict").getValue());
		form.getForm().findField("street").setValue(form.down("#selectStreet").getValue());
		console.dir(form.getValues());
		var json = {
			success : function(formPanel, action) {
				if (action.result.success == "false") {
					form.getForm().findField("country").setValue(null);
					form.getForm().findField("province").setValue(null);
					form.getForm().findField("city").setValue(null);
					form.getForm().findField("district").setValue(null);
					form.getForm().findField("street").setValue(null);
					Ext.msgBox.remainMsg('编辑收货信息', action.result.msg, Ext.MessageBox.ERROR);
				} else {
					Ext.msgBox.msg('编辑收货信息', action.result.msg, Ext.MessageBox.INFO);
					var orderForm = Ext.getCmp('orderShow');
					orderForm.initData(orderForm);
					win.close();
				}
			},
			failure : function(formPanel, action) {
				form.getForm().findField("country").setValue(null);
				form.getForm().findField("province").setValue(null);
				form.getForm().findField("city").setValue(null);
				form.getForm().findField("district").setValue(null);
				form.getForm().findField("street").setValue(null);
				Ext.msgBox.remainMsg('编辑收货信息', action.result.msg, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		form.submit(json);
	}
});