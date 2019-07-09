Ext.define('MB.jsPages.cancelDistributeOrderApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.CancelDistributeOrderController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['CancelDistributeOrderController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});