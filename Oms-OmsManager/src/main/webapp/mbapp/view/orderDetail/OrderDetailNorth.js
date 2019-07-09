Ext.define("MB.view.orderDetail.OrderDetailNorth", {
	extend : "Ext.form.Panel",
	alias : 'widget.orderDetailNorth',
	id : 'orderDetailNorth',
	height : 35,
	initComponent : function() {
		var me = this;
		me.dockedItems = [{
			xtype : 'toolbar',
			dock : 'top',
			items : [//设置itemId项用于按钮的切换
			    { text : '<font style="color:white">锁定</font>', tooltip : '锁定', disabled : true, value : 'lock', action : 'OrderOperation',style:'background-color:#1874CD;'},
			    { text : '<font style="color:white">解锁</font>', tooltip : '解锁', disabled : true, value : 'unlock', action : 'OrderOperation',style:'background-color:#1874CD;'},
			    { itemId : 'conUnConMenu', text : '<font style="color:white">订单确认</font>', tooltip : '确认', disabled : true, value : 'confirm', action : 'OrderOperation',style:'background-color:#1874CD;'},
			    { itemId : 'cancelMenu', text : '<font style="color:white">订单取消</font>', tooltip : '取消', disabled : true, value : 'cancel', action : 'OrderOperation',style:'background-color:#1874CD;'},
			    { itemId : 'norQuesMenu', text : '<font style="color:white">订单设问题单</font>', tooltip : '设问题单', disabled : true, value : 'question', action : 'OrderOperation',style:'background-color:#1874CD;'},
			    { text : '<font style="color:white">通知收款</font>', tooltip : '通知收款', disabled : true, value : 'notice', action : 'OrderOperation',style:'background-color:#1874CD;'},
//			    { text : '<font style="color:white">结算</font>', tooltip : '结算', disabled : true, value : 'settle', action : 'OrderOperation',style:'background-color:#1874CD;'},
//			    { itemId : 'recentMenu', text : '<font style="color:white">移入近期</font>', tooltip : '移入近期', disabled : true, value : 'recent', action : 'OrderOperation',style:'background-color:#1874CD;'},
//			    { text : '<font style="color:white">生成额外退款单</font>', tooltip : '生成额外退款单', disabled : true, value : 'addExtra', action : 'OrderOperation',style:'background-color:#1874CD;'},
			    { text : '<font style="color:white">沟通</font>', tooltip : '沟通', disabled : true, value : 'communicate', action : 'OrderOperation',style:'background-color:#1874CD;'},
//			    { text : '<font style="color:white">复活</font>', tooltip : '复活', disabled : true, value : 'relive', action : 'OrderOperation',style:'background-color:#1874CD;'},
//			    { itemId : 'onlyReturn', text : '<font style="color:white">仅退货</font>', tooltip : '仅退货', disabled : true, value : 'onlyReturn', action : 'OrderOperation',style:'background-color:#1874CD;'}
			]
		}];
		me.callParent(arguments);
	}
});