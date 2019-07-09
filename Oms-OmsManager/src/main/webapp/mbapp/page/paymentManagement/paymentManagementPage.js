Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [ 
	           'MB.view.paymentManagement.PaymentManagementViewport',
	           'MB.controller.PaymentManagementController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['PaymentManagementController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('paymentManagementViewport');
	
	}
});