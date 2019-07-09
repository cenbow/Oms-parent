Ext.define('MB.jsPages.queryExpressApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.QueryExpressController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['QueryExpressController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});