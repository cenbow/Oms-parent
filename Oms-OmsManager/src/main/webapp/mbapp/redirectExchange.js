Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: ['MB.view.exchOrder.ExchangeDetailView', 'MB.controller.ExchangeDetailController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['ExchangeDetailController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('exchangeDetailView');
	}
});
