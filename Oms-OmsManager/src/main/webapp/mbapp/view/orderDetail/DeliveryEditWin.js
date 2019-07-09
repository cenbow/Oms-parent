Ext.define("MB.view.orderDetail.DeliveryEditWin", {
	extend: "Ext.window.Window",
	alias: "widget.deliveryEditWin",
	requires: ['MB.view.commonComb.CommonUseSystemShippingComb'],
	title: "修改承运商",
	width: 900,
	height: 150,
	layout: "fit",
	maskDisabled : false,
	modal : true,
	initComponent: function () {
		var me = this;
		me.items = [{
			xtype: "form",
			margin: 10,
			border: false,
			frame: true,
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 70
			},
			items: [{
				xtype : 'hidden',
				name : 'depotCode'
			},{
				xtype : 'hidden',
				name : 'orderSn'
			},{
				xtype : 'commonUseSystemShippingComb',
				width : 800,
				margin: 10,
				queryMode : 'local',
				name : 'shippingCode',
				hiddenName : 'shippingCode',
				allowBlank : false
			}]
		}];
		this.buttons = [
		    { text: "保存", handler : me.doSaveDeliveryChange },
			{ text: "关闭", handler : function () { this.up("window").close();} }
		];
		this.callParent(arguments);
	},
	doSaveDeliveryChange : function(btn){
		var win = btn.up("window");
		form = win.down("form");
		if (!form.isValid()) {
			Ext.msgBox.remainMsg('变更承运商', "请选择要变更的承运商！", Ext.MessageBox.ERROR);
			return ;
		}
		console.dir(form.getValues());
		var depotCode = form.getValues().depotCode;
		var orderSn = form.getValues().orderSn;
		var shippingCode = form.getValues().shippingCode;
		Ext.Ajax.request({
			url:  basePath + '/custom/orderInfo/doSaveDeliveryChange',
			params: {
				shippingCode : shippingCode,
				orderSn : orderSn,
				depotCode : depotCode
			},
			success: function(response){
				var result = Ext.JSON.decode(response.responseText);
				if (result.code=='1') {
					Ext.getCmp('orderDetailCenter').initData();
					win.close();
				} else {
					Ext.msgBox.remainMsg('变更承运商', result.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var result = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('变更承运商', result.msg, Ext.MessageBox.ERROR);
			}
		});		
	}
});