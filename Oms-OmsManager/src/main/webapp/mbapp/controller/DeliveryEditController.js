Ext.define('MB.controller.DeliveryEditController', {
	extend : 'Ext.app.Controller',
	views : [ 'MB.view.orderDetail.DeliveryEditWin',
	          'MB.view.commonComb.CommonUseSystemShippingComb'],
	refs : [{
		ref : 'deliveryEditWin',
		selector : 'deliveryEditWin'
	}],
	init : function() {
	},
	onLaunch : function() {
		// 页面加载完成之后执行
		Ext.Ajax.request({
			url:  basePath + 'custom/orderInfo/getAvaliableDelivery',
			params: editDeliveryParams,
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				var code = respText.code;
				var avaliableShippingList = respText.avaliableShippingList;
				console.dir(avaliableShippingList);
				if (code=='1') {
					var win = Ext.widget('deliveryEditWin');
					var form = win.down('form');
					form.getForm().findField("depotCode").setValue(editDeliveryParams.depotCode);
					form.getForm().findField("orderSn").setValue(editDeliveryParams.orderSn);
					form.down('commonUseSystemShippingComb').store.loadData(avaliableShippingList);
					win.show();
				} else {
					Ext.msgBox.remainMsg('获取物流信息', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('获取物流信息', respText.msg, Ext.MessageBox.ERROR);
			}
		});
	}
});