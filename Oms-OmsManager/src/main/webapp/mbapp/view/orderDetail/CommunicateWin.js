Ext.define("MB.view.orderDetail.CommunicateWin", {
	extend: "Ext.window.Window",
	alias: "widget.communicateWin",
	title: "沟通",
	width: '500',
	height: '310',
	layout: "fit",
	maskDisabled : false ,
	modal : true ,
	initComponent: function () {
		var me = this;
		this.items = {
				xtype: "form",
				margin: 5,
				border: false,
				frame: true,
				fieldDefaults: {
					labelAlign: 'right',
					labelWidth: 100
				},
				items: [
					{
						xtype : 'radiogroup',
						name: "orderSn",
						margin: '0 0 0 0',
						fieldLabel: "沟通对象",
						columns: 1,
						items: [
							{ boxLabel: '订单'+masterOrderSn, name: 'orderSn', inputValue: masterOrderSn, checked: true}
						]
					},
					{
						xtype: 'textareafield',
						name : 'message',
						fieldLabel : '备注',
						allowBlank : false,
						grow : true,
						width: 400,
						height : 90
					}
				]
			};
			this.buttons = [
				{ text: "保存", action: "doSaveCommunicate", handler : me.doSaveCommunicate },
				{ text: "关闭", handler : function () { this.up("window").close();} }
				
			];
		me.callParent(arguments);
	},
	doSaveCommunicate : function (btn) {
		var me = this;
		var win = btn.up("window");
		form = win.down("form");
		var radiogroup = form.down('radiogroup');
		if (!form.isValid()) {
			Ext.msgBox.remainMsg('验证', "请填写必填项！", Ext.MessageBox.ERROR);
			return;
		}
		var param = form.getValues();
		var checkedBoxArray = radiogroup.getChecked();
		checkedBoxLabel = checkedBoxArray && checkedBoxArray[0].boxLabel;
		console.dir(checkedBoxLabel);
		if(checkedBoxLabel.indexOf("订单") >= 0){//type：0主单，1交货单
			param.type = '0';
		}else{
			param.type = '1';
		}
		Ext.Ajax.request({
			url:  basePath + '/custom/orderStatus/communicate',
			params: param,
			timeout : 40000,
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				var code = respText.code;
				if (code=='1') {
					win.close();
					Ext.getCmp('orderDetailCenter').initData();
				} else {
					Ext.msgBox.remainMsg('沟通', respText.msg, Ext.MessageBox.ERROR);
				}  
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('沟通', respText.msg, Ext.MessageBox.ERROR);
			}
		});
	}
});