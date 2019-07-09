Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [ 
	           'MB.view.bfbRefundSettlement.BfbRefundSettlementViewport',
	           'MB.controller.BfbRefundSettlementController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['BfbRefundSettlementController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('bfbRefundSettlementViewport');
	
	}
});