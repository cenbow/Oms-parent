Ext.define('MB.controller.MasOrderPayEditController', {
	extend : 'Ext.app.Controller',
	views : [ 'MB.view.orderDetail.PayEditWin' ],
	refs : [{
		ref : 'payEditWin',
		selector : 'payEditWin'
	}],
	init : function() {
		var me = this;
		me.control({
			'payEditWin button[action=doSaveShippingFee]' : { //保存邮费修改
				click : this.doSaveShippingFee
			}
		});
	},
	onLaunch : function() {
		// 页面加载完成之后执行
		var win = Ext.widget("payEditWin");
		var form = win.down('form');
		Ext.Ajax.request({
			url:  basePath + 'custom/orderInfo/getMasterOrderPayInfo',
			timeout:90000,
			params : {
				"masterOrderSn" : masterOrderSn
			},
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				if (respText.code=='1') {
					var masterOrderPayInfo = respText.masterOrderPayInfo;
					form.getForm().findField("masterOrderSn").setValue(masterOrderPayInfo.masterOrderSn);
					form.getForm().findField("goodsAmount").setValue(masterOrderPayInfo.goodsAmount);
					form.getForm().findField("totalPriceDiscount").setValue(masterOrderPayInfo.totalPriceDiscount);
					form.getForm().findField("tax").setValue(masterOrderPayInfo.tax);
					form.getForm().findField("discount").setValue(masterOrderPayInfo.discount);
					form.getForm().findField("shippingTotalFee").setValue(masterOrderPayInfo.shippingTotalFee);
					form.getForm().findField("moneyPaid").setValue(masterOrderPayInfo.moneyPaid);
					form.getForm().findField("insureTotalFee").setValue(masterOrderPayInfo.insureTotalFee);
					form.getForm().findField("surplus").setValue(masterOrderPayInfo.surplus);
					form.getForm().findField("payTotalFee").setValue(masterOrderPayInfo.payTotalFee);
					form.getForm().findField("integralMoney").setValue(masterOrderPayInfo.integralMoney);
					form.getForm().findField("totalFee").setValue(masterOrderPayInfo.totalFee);
					form.getForm().findField("totalPayable").setValue(masterOrderPayInfo.totalPayable);
				} else {
					Ext.msgBox.remainMsg('获取付款单信息', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('获取付款单信息', respText.msg, Ext.MessageBox.ERROR);
			}
		});
		win.show();
	},
	doSaveShippingFee : function(btn){
		var win = btn.up("window");
		form = win.down("form");
		if (!form.isValid()) {
			Ext.msgBox.remainMsg('编辑付款单信息', "请选填写运费！", Ext.MessageBox.ERROR);
			return ;
		}
		var params = {};
		params.masterOrderSn = form.getForm().findField("masterOrderSn").getValue();
		params.shippingTotalFee = form.getForm().findField("shippingTotalFee").getValue();
		Ext.Ajax.request({
			url:  basePath + '/custom/orderInfo/doSaveShippingFee',
			params: params,
			success: function(response){
				var result = Ext.JSON.decode(response.responseText);
				if (result.code=='1') {
					Ext.getCmp('orderDetailCenter').initData();
					win.close();
				} else {
					Ext.msgBox.remainMsg('编辑付款单信息', result.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var result = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('编辑付款单信息', result.msg, Ext.MessageBox.ERROR);
			}
		});	
	}
});