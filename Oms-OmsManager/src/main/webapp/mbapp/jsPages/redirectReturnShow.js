Ext.Loader.setConfig({ enabled: true, disableCaching: false });
Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: ['MB.view.returnOrder.ReturnShowView', 'MB.controller.ReturnShowController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['ReturnShowController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('returnShowView');
	}
});
