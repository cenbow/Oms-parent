Ext.define('MB.controller.SendMessageToolController', {
	extend : 'Ext.app.Controller',
	stores : [],
	models : [],
	views : [ 'MB.view.sendMessageTool.SendMessageToolViewport' ],
	refs : [{
		ref : 'sendMessageToolViewport',
		selector : 'sendMessageToolViewport'
	}],
	init : function() {

		var me = this;
		me.control({
			'sendMessageToolViewport button[action=reset]' : { //重置
				click : this.reset
			},
			'sendMessageToolViewport button[action=doSend]' : { //发送
				click : this.doSend
			}
		});
	},
	onLaunch : function() {},
	reset : function(btn){
		var form = btn.up('form');
		form.reset();
	},
	doSend : function(btn){
		var form = btn.up('form');
		//检查必填项
		var mobile = form.getForm().findField('mobile').getValue();
		var sendType = form.getForm().findField('sendType').getValue();
		var message = form.getForm().findField('message').getValue();
		if(mobile==''||mobile==null){
			Ext.msgBox.remainMsg('提示', '请填写联系方式！', Ext.MessageBox.ERROR);
			return ;
		}
		if(sendType==''||sendType==null){
			Ext.msgBox.remainMsg('提示', '请选择短信通道！', Ext.MessageBox.ERROR);
			return ;
		}
		if(message==''||message==null){
			Ext.msgBox.remainMsg('提示', '填写短信内容！', Ext.MessageBox.ERROR);
			return ;
		}
		var params = {"mobile" : mobile, "sendType" : sendType, "message" : message};
		Ext.Ajax.request({
			url: basePath + '/custom/orderInfo/toolsDoSendMessage',
			params: params,
			success: function(response){
				var results = Ext.JSON.decode(response.responseText);
				if (results.code=='1') {
					Ext.msgBox.remainMsg('结果', results.msg, Ext.MessageBox.INFO);
				} else {
					Ext.msgBox.remainMsg('结果', results.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('结果', respText.msg, Ext.MessageBox.ERROR);
			}
		});
	}
});