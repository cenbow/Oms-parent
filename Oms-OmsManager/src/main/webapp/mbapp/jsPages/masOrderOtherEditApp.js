Ext.define('MB.jsPages.masOrderOtherEditApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.MasOrderOtherEditController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['MasOrderOtherEditController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});