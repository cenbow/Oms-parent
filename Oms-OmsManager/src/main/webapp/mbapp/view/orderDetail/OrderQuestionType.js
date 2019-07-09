Ext.define("MB.view.orderDetail.OrderQuestionType", {
	extend: "Ext.form.Panel",
	alias: 'widget.orderQuestionType',
	width: '100%',
	id:'orderQuestionType',
	//frame: true,
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
		this.dockedItems = [{
			xtype: 'toolbar',
			dock: 'top',
			items: [{
				text : "<span style='color:black;font-weight:bold';>问题单类型:</span>",
//				tooltip : '问题单',
				margin: '1 5 1 90',
				//iconCls : 'plugin',
				value: 1,
				action: 'process'
			} 
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