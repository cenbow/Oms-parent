Ext.define('MB.controller.TurnNormalDistributeOrderController', {
	extend : 'Ext.app.Controller',
	stores : [ 'OrderCustomDefines'],
	models : [ 'OrderCustomDefine'],
	views : [ 'orderDetail.TurnNormalDistributeOrder' ],
	refs : [],
	init : function() {
	},
	onLaunch : function() {
		// 页面加载完成之后执行
		Ext.Ajax.request({
			url: basePath + '/custom/orderStatus/sonNormal',
			params: {'orderSn' : parent.orderSnForOperate, 'method' : 'init'},
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				typeItems = new Array();
				parent.desc = "";
				if (results.length > 0) {
					for (var i = 0; i < results.length; i++) {
						var checked = false;
						if (i == 0) {
							var checked = true;
							parent.desc = '<font color="red">' + results[i].desc + '</font>'
						} 
						var typeObj = {
							name: "type",
							inputValue: results[i].type,
							boxLabel: results[i].name,
							checked: checked
						};
						typeItems.push(typeObj);
					}
				}
				var win = Ext.widget("turnNormalDistributeOrder");
				win.show();
			},
			failure: function(response){
				var text = response.responseText;
				console.dir('failure:' + text);
			}
		});
	}
});