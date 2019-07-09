Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: ['MB.view.goodsReturnChange.GoodsReturnChangeView', 'MB.controller.GoodsReturnChangeController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['GoodsReturnChangeController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('goodsReturnChangeView');
	}
});
