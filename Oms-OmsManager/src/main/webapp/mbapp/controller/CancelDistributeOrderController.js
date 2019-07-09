Ext.define('MB.controller.CancelDistributeOrderController', {
	extend : 'Ext.app.Controller',
	stores : [ 'OrderCustomDefines'],
	models : [ 'OrderCustomDefine'],
	views : [ 'orderDetail.CancelDistributeOrder' ],
	refs : [ ],
	init : function() {
	},
	onLaunch : function() {
		// 页面加载完成之后执行
		var win = Ext.widget("cancelDistributeOrder");
		win.show();
	}
});