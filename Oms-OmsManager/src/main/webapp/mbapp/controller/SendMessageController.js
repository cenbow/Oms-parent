Ext.define('MB.controller.SendMessageController', {
	extend : 'Ext.app.Controller',
	stores : [],
	models : [],
	views : [ 'orderDetail.SendMessageWin' ],
	refs : [],
	init : function() {
	},
	onLaunch : function() {
		// 页面加载完成之后执行
		var win = Ext.widget("sendMessageWin");
		var form = win.down('form');
		Ext.Ajax.request({
			url:  basePath + 'custom/orderInfo/initSendMessage',
			params: {'masterOrderSn': parent.masterOrderSn,'channelCode':parent.channelCode},
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				if (respText.code=='1') {
					form.getForm().findField("encodedMobile").setValue(respText.encodedMobile);
					var shopCode = respText.shopCode;
					if(shopCode=='bg'){
						form.getForm().findField("shopCodeName").setValue('邦购');
					}else if(shopCode=='yf'){
						form.getForm().findField("shopCodeName").setValue('有范');
					}else if(shopCode=='mb'){
						form.getForm().findField("shopCodeName").setValue('美邦/全网');
					}else{
						form.getForm().findField("shopCodeName").setValue('未知');
					}
					form.getForm().findField("shopCode").setValue(shopCode);
				} else {
					Ext.msgBox.remainMsg('结果', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('结果', respText.msg, Ext.MessageBox.ERROR);
			}
		});
		win.show();
	}
});