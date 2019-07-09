Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [ 
	           'MB.view.batchDecode.BatchDecodeViewport',
	           'MB.controller.BatchDecodeController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['BatchDecodeController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('batchDecodeViewport');
	
	}
});