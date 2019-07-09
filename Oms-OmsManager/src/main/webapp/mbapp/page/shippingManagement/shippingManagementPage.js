Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [ 
	           'MB.view.shippingManagement.ShippingManagementViewport',
	           'MB.controller.ShippingManagementController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['ShippingManagementController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('shippingManagementViewport');
	
	}
});
