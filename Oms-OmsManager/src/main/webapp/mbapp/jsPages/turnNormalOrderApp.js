Ext.define('MB.jsPages.turnNormalOrderApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.TurnNormalOrderController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['TurnNormalOrderController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});