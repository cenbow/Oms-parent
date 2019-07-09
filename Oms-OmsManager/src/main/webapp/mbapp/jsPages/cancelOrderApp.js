Ext.define('MB.jsPages.cancelOrderApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.CancelOrderController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['CancelOrderController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});