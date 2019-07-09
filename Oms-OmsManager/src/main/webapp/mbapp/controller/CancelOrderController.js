Ext.define('MB.controller.CancelOrderController', {
	extend : 'Ext.app.Controller',
	stores : [ 'OrderCustomDefines'],
	models : [ 'OrderCustomDefine'],
	views : [ 'orderDetail.CancelOrder' ],
	refs : [ ],
	init : function() {
	},
	onLaunch : function() {
		// 页面加载完成之后执行
		var win = Ext.widget("cancelOrder");
		win.show();
	}
});