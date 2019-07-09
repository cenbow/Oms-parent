Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: ['MB.view.systemRole.SystemRoleViewport',
	           'MB.controller.SystemRoleController'],  
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['SystemRoleController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('systemRoleViewport');
	}
});
