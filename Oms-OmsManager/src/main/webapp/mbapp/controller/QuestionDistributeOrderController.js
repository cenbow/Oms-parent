Ext.define('MB.controller.QuestionDistributeOrderController', {
	extend : 'Ext.app.Controller',
	stores : ['OrderCustomDefines' ],
	models : ['OrderCustomDefine' ],
	views : [ 'orderDetail.QuestionDistributeOrder' ],
	refs : [],
	init : function() {
	},
	onLaunch : function() {
		// 页面加载完成之后执行
		var win = Ext.widget("questionDistributeOrder");
		win.show();
	}
});