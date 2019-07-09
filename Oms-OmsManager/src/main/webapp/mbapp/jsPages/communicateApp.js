Ext.define('MB.jsPages.communicateApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.CommunicateController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['CommunicateController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});