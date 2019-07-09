Ext.define("MB.view.orderExchange.OrderExchangeViewPort", {
	extend : "Ext.container.Viewport",
	alias : 'widget.orderExchangeViewPort',
	requires: [],
	layout : "border",
	defaults : {
		split : true
	},
	initComponent : function() {
		this.items = [{//顶部操作按钮
			xtype : 'form',
			region : "north",
			id : 'orderExchangeNorth',
			itemId : 'orderExchangeNorth',
			align : 'center',
			height : 35,
			collapsible : false,
			dockedItems : [{
				xtype : 'toolbar',
				dock : 'top',
				items : [
				   { text : '<font style="color:white">保&nbsp;&nbsp;存</font>', tooltip : '保存', value : 1, action : 'saveExchange',style:'background-color:#1874CD;' }, 
				   /*{ text : '<font style="color:white">确&nbsp;&nbsp;认</font>', tooltip : '确认', value : 1, action : 'confirmExchange',style:'background-color:#1874CD;' },
				   { text : '<font style="color:white">未&nbsp;确&nbsp;认</font>', tooltip : '未确认', value : 1, action : 'unConfirmExchange',style:'background-color:#1874CD;' },*/
//				   { text : '<font style="color:white">作&nbsp;&nbsp;废</font>', tooltip : '作废', value : 1, action : 'cancelExchange',style:'background-color:#1874CD;' },
//				   { text : '<font style="color:white">仅&nbsp;退&nbsp;货</font>', tooltip : '仅退货', value : 1, action : 'onlyReturnExchange',style:'background-color:#1874CD;' },
				   { text : '<font style="color:white">沟&nbsp;&nbsp;通</font>', tooltip : '沟通', value : 1, action : 'actionClick',style:'background-color:#1874CD;' }
//				   { text : '<font style="color:white">发送短信</font>', tooltip : '发送短信', value : 1, action : 'sendMessage',style:'background-color:#1874CD;' }
				 ]
			}]
		},
		{//中部换单信息
			xtype : 'tabpanel',
			region : "center",
			id : 'orderExchangeCenter',
			itemId : 'orderExchangeCenter',
			activeTab: 1,
			items : [{
				title : '原订单信息',
				xtype : 'orderExchangeOriOrdModule'
			},{
				title : '退货单信息',
				xtype : 'orderExchangeRetOrdModule'
			},{
				title : '换货单信息',
				xtype : 'orderExchangeChanOrdModule'
			}]
		}];
		this.callParent(arguments);
	}
});