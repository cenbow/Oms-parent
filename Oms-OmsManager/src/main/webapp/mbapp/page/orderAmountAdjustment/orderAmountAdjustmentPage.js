Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [ 
	           'MB.view.orderAmountAdjustment.OrderAmountAdjustmentViewport',
	           'MB.controller.OrderAmountAdjustmentController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['OrderAmountAdjustmentController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('orderAmountAdjustmentViewport');
	
	}
});