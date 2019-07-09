Ext.define('MB.jsPages.questionDistributeOrderApp', {
	extend: 'Ext.app.Application',
	name: 'MB',
	appFolder: 'mbapp',
	requires: ['MB.controller.QuestionDistributeOrderController'],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['QuestionDistributeOrderController'];
		this.callParent(arguments);
	},
	launch: function () {

	}
});