Ext.define('MB.jsPages.sendMessageApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.SendMessageController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['SendMessageController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});