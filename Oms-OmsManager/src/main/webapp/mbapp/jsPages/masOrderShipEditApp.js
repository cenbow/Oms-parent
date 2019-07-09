Ext.define('MB.jsPages.masOrderShipEditApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.MasOrderShipEditController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['MasOrderShipEditController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});