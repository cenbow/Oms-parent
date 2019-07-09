Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [ 
	           'MB.view.abnormalSKUMaintain.AbnormalSKUMaintainViewport',
	           'MB.controller.AbnormalSKUMaintainController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['AbnormalSKUMaintainController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('abnormalSKUMaintainViewport');
	
	}
});