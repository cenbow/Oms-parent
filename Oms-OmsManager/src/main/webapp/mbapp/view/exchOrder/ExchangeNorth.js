Ext.define("MB.view.exchOrder.ExchangeNorth", {
	extend: "Ext.form.Panel",
	alias: 'widget.exchangeNorth',
	width: '100%',
	frame: true,
	id:'exchangeNorth',
//	bodyStyle: {
//		padding: '10px'
//	},
	autoWidth:true,
	autoHeight:true, 
	autoScroll : true,
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'center'
	},
	layout: {
        type: 'hbox',
        pack: 'center',
        align: 'middle'             //对齐方式 top、middle、bottom：顶对齐、居中、底对齐；stretch：延伸；stretchmax：以最大的元素为标准延伸
    },
	bodyBorder:true,
	initComponent: function () {
		// TODO 根据订单状态展示可操作功能按钮
		this.dockedItems = [{
			xtype: 'toolbar',
			dock: 'top',
			items: [{
				text : '保&nbsp;&nbsp;存',
				tooltip : '保存',
				margin: '1 5 1 160',
				value: 1,
				action: 'saveExchange'
			} , '-' , {
				text : '确&nbsp;&nbsp;认',
				tooltip : '确认',
				margin: '1 5 1 15',
				value: 1,
				action: 'confirmExchange'
			} , '-' , {
				text : '未确认&nbsp;&nbsp;',
				tooltip : '未确认',
				margin: '1 5 1 15',
				value: 1,
				action: 'unConfirmExchange'
			} , '-' , {
				text : '作&nbsp;&nbsp;废',
				tooltip : '作废',
				margin: '1 5 1 15',
				value: 1,
				action: 'cancelExchange'
			}, '-' , {
				text : '仅&nbsp;退&nbsp;货',
				tooltip : '作废',
				margin: '1 5 1 15',
				value: 1,
				action: 'onlyReturnExchange'
			}, '-' , {
				text : '沟&nbsp;&nbsp;通',
				tooltip : '沟通',
				margin: '1 5 1 15',
				value: 1,
				action: 'actionClick'
			}
			/*, '-' , {
				text : '发送短信',
				tooltip : '发送短信',
				margin: '1 5 1 15',
				value: 1,
				action: 'sendMessage'
			}*/
			]
		}];
		this.callParent(arguments);
	},
	editStatus: function(form, action) {
//		var orderStatus = Ext.getCmp('orderInfo.orderStatus').getValue();
//		var payStatus = Ext.getCmp('orderInfo.payStatus').getValue();
//		var shipStatus = Ext.getCmp('orderInfo.shipStatus').getValue();
		var orderStatus = form.findField("orderStatus").getValue();
		var payStatus = form.findField("payStatus").getValue();
		var shipStatus = form.findField("shipStatus").getValue();
		form.findField("orderStatusName").setValue(getCombineStatus(orderStatus, payStatus, shipStatus));
	}
});