Ext.define('MB.jsPages.turnNormalDistributeOrderApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.TurnNormalDistributeOrderController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['TurnNormalDistributeOrderController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});