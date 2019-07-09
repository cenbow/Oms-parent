Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [ 
	           'MB.view.handOrder.HandOrderViewport',
	           'MB.controller.HandOrderController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['HandOrderController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('handOrderViewport');
	
	}
});