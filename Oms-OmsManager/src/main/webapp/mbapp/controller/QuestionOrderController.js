Ext.define('MB.controller.QuestionOrderController', {
	extend : 'Ext.app.Controller',
	stores : ['OrderCustomDefines' ],
	models : ['OrderCustomDefine' ],
	views : [ 'orderDetail.QuestionOrder' ],
	refs : [],
	init : function() {
	},
	onLaunch : function() {
		// 页面加载完成之后执行
		var win = Ext.widget("questionOrder");
		win.show();
	}
});