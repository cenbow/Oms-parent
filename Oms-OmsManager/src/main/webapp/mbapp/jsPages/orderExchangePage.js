Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: ['MB.view.orderExchange.OrderExchangeViewPort', 'MB.controller.OrderExchangeController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['OrderExchangeController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('orderExchangeViewPort');
	}
});
