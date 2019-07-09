Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: ['MB.view.refund.RefundListView', 'MB.controller.RefundListController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['RefundListController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('refundListView');
	}
});
