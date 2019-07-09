Ext.define("MB.view.orderDetail.SendMessageWin", {
	extend: "Ext.window.Window",
	alias: "widget.sendMessageWin",
	title: "订单操作-发送短信",
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
				url : basePath + '/custom/orderStatus/cancel',
				fieldDefaults: {
					labelAlign: 'right',
					labelWidth: 100
				},
				items: [
					{
						xtype : 'textfield',
						name: "mobile",
						margin: '15 0 0 0',
						fieldLabel: "联系方式",
						width: 400,
						value : '***********',
						readOnly : true
					},
					{
						xtype : 'textfield',
						name: "shopCodeName",
						margin: '15 0 15 0',
						fieldLabel: "短信通道",
						width: 400,
						readOnly : true
					},
					{
						xtype: 'hidden',
						name : 'encodedMobile'
					},
					{
						xtype: 'hidden',
						name : 'shopCode'
					},
					{
						xtype: 'hidden',
						name : 'masterOrderSn',
						value : parent.masterOrderSn
					},
					{
						xtype: 'hidden',
						name : 'channelCode',
						value : parent.channelCode
					},
					{
						xtype: 'textareafield',
						name : 'message',
						fieldLabel : '短信内容',
						grow : true,
						allowBlank : false,
						width: 400,
						height : 90
					}
				]
			};
			this.buttons = [
				{ text: "发送", action: "sendMessage", handler : me.sendMessage },
				{ text: "关闭", handler : function () { this.up("window").close();} }
				
			];
		me.callParent(arguments);
	},
	sendMessage : function (btn) {
		var me = this;
		var win = btn.up("window");
		form = win.down("form");
		if (!form.isValid()) {
			Ext.msgBox.remainMsg('提示', '必须填写短信内容!', Ext.MessageBox.ERROR);
			return;
		}
		var param = form.getValues();
		console.dir(param);
		Ext.Ajax.request({
			url:  basePath + '/custom/orderInfo/doSendMessage',
			params: param,
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				var code = respText.code;
				if (code=='1') {
					win.close();
				} else {
					Ext.msgBox.remainMsg('提示', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('提示', respText.msg, Ext.MessageBox.ERROR);
			}
		});
	}
});