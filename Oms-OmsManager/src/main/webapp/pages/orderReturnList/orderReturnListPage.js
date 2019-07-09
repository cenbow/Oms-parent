
Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [

	          'MB.view.orderReturnList.OrderReturnView',
	          'MB.controller.OrderReturnController'
	           ],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['OrderReturnController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('orderReturnView');
	}
});
