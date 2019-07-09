Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [
	           'MB.view.systemResource.SystemResourceViewport',
	           'MB.controller.SystemResourceController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['SystemResourceController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('systemResourceViewport');
	
	}
});
