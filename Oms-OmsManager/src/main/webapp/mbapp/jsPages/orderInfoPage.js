Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: ['MB.view.orderDetail.OrderDetailViewPort', 'MB.controller.OrderDetailController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['OrderDetailController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('orderDetailViewPort');
	}
});
