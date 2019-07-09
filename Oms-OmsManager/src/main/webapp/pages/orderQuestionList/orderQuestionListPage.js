
//alert("orderquestionPage")
Ext.application({
	name: "MB",
	appFolder: 'mbapp',
	requires: [

	          'MB.view.orderQuestionList.OrderQuestionView',
	          'MB.controller.OrderQuestionController'
	           ],
	autoCreateViewport: false,
	init: function () {
		this.controllers = ['OrderQuestionController'];
		this.callParent(arguments);
	},
	launch: function () {
		// 页面加载完成之后执行
		Ext.widget('orderQuestionView');
	}
});
