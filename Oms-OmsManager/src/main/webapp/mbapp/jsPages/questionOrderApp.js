Ext.define('MB.jsPages.questionOrderApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.QuestionOrderController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['QuestionOrderController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});