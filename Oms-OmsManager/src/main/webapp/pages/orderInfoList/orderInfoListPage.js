
Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [

	          'MB.view.orderInfoList.OrderInfoViewport',
	           'MB.controller.OrderInfoController'
	           ],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['OrderInfoController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('orderInfoViewport');
	}
});
