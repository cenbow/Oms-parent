Ext.define('MB.jsPages.masOrderPayEditApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.MasOrderPayEditController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['MasOrderPayEditController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});