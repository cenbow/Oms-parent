Ext.Loader.setConfig({ enabled: true, disableCaching: false });
Ext.define('MB.jsPages.orderGoodsEditPage', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.OrderGoodsEditController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['OrderGoodsEditController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});