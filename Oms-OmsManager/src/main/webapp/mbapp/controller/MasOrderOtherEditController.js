Ext.define('MB.controller.MasOrderOtherEditController', {
	extend : 'Ext.app.Controller',
	views : [ 'MB.view.orderDetail.OtherEditWin' ],
	refs : [{
		ref : 'otherEditWin',
		selector : 'otherEditWin'
	}],
	init : function() {
		var me = this;
		me.control({
			'otherEditWin button[action=doSaveOtherEdit]' : { //保存邮费修改
				click : this.doSaveOtherEdit
			}
		});
	},
	onLaunch : function() {
		var win = Ext.widget("otherEditWin");
		var form = win.down('form');
		Ext.Ajax.request({
			url:  basePath + 'custom/orderInfo/getMasterOrderInfoExtend',
			timeout:90000,
			params : {
				"masterOrderSn" : masterOrderSn,
				"isHistory" : isHistory
			},
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				if (respText.code=='1') {
					var masterOrderInfo = respText.masterOrderInfo;
					var masterOrderInfoExtend = respText.masterOrderInfoExtend;
					
					var invContent = masterOrderInfoExtend.invContent;
					if(invContent!=''&&invContent!=null){
						form.getForm().findField("invContent").setValue(invContent);
					}
					form.getForm().findField("masterOrderSn").setValue(masterOrderSn);
					form.getForm().findField("invType").setValue(masterOrderInfoExtend.invType);
					form.getForm().findField("invPayee").setValue(masterOrderInfoExtend.invPayee);
					form.getForm().findField("howOos").setValue(masterOrderInfo.howOos);
					form.getForm().findField("postscript").setValue(masterOrderInfo.postscript);
					form.getForm().findField("toBuyer").setValue(masterOrderInfo.toBuyer);
				} else {
					Ext.msgBox.remainMsg('获取发票信息', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('获取发票信息', respText.msg, Ext.MessageBox.ERROR);
			}
		});
		win.show();
	},
	doSaveOtherEdit : function(btn){
		var win = btn.up("window");
		form = win.down("form");
		var formValues=form.getForm().getValues();
		//提交修改
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/orderInfo/doSaveOtherEdit',
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				if(respText.code=='1'){
					Ext.getCmp('orderDetailCenter').initData();
					win.close();
				}else{
					Ext.Msg.alert('保存发票信息', respText.msg);
				}
			},
			failure : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('保存发票信息', respText.msg);
			}
		});
	}
});