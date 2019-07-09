Ext.define('MB.jsPages.masOrderDeliveryEditApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.DeliveryEditController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['DeliveryEditController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});