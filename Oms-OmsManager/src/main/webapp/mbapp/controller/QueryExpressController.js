Ext.define('MB.controller.QueryExpressController', {
	extend : 'Ext.app.Controller',
	views : [ 'MB.view.orderDetail.OrderExpressWin'],
	refs : [{
		ref : 'orderExpressWin',
		selector : 'orderExpressWin'
	}],
	init : function() {
	},
	onLaunch : function() {
		// 页面加载完成之后执行
		var win = Ext.widget("orderExpressWin");
		var form = win.down('form');
		var grid = form.down('gridpanel');
		Ext.Ajax.request({
			url:  basePath + 'custom/orderInfo/queryExpress',
			params: queryExpressParams,
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				if (respText.code=='1') {
					form.getForm().findField("orderSn").setValue(queryExpressParams.orderSn);
					form.getForm().findField("shippingName").setValue(queryExpressParams.shippingName);
					form.getForm().findField("invoiceNo").setValue(queryExpressParams.invoiceNo);
					form.getForm().findField("deliveryTypeName").setValue(queryExpressParams.deliveryTypeName);
					grid.store.loadData(respText.logisticsInfo.data);
				} else {
					Ext.msgBox.remainMsg('获取物流信息', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('获取物流信息', respText.msg, Ext.MessageBox.ERROR);
			}
		});
		win.show();
	}
});