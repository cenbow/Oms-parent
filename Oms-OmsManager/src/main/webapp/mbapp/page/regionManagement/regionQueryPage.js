Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [ 
	           'MB.view.regionManagement.RegionQueryViewport',
	           'MB.controller.RegionQueryController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['RegionQueryController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('regionQueryViewport');
	
	}
});
