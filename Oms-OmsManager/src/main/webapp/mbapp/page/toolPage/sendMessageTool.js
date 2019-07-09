Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [ 
	           'MB.view.sendMessageTool.SendMessageToolViewport',
	           'MB.controller.SendMessageToolController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['SendMessageToolController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('sendMessageToolViewport');
	
	}
});