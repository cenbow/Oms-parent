Ext.define("MB.view.orderDetail.OtherEditForm", {
	extend : "Ext.form.Panel",
	alias : 'widget.otherEditForm',
	width : '100%',
	requires : [ 'MB.model.ComboModel'],
	frame : true,
	margin : 1,
	bodyPadding : 2,
	layout : 'column',
	buttonAlign : 'center',// 按钮居中
	fieldDefaults : {
		width : 450,
		labelWidth : 120,
		labelAlign : "right",
		margin : 1
	},
	url : basePath + '/custom/orderStatus/editOrderOther',
	initComponent : function() {
		this.items = [ {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			items : [ {
				xtype : "textfield",
				name : "invType",
				fieldLabel : "发票类型"
			}, ]
		}, {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			items : [ {
				xtype : "textfield",
				name : "invPayee",
				fieldLabel : "发票抬头"
			} ]
		}, {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			items : [ {
				xtype : 'combo',
				fieldLabel : '发票内容',
				name: 'invContent',
				store : new Ext.data.Store({
					data : [ {
						v : '服饰',
						n : '服装'
					} ],
					model : "MB.model.ComboModel"
				}),
				queryMode : 'local',
				displayField : 'v',
				valueField : 'n',
				hiddenName : 'invContent',
				emptyText : '请选择发票内容',
				value : '服装',
				editable : false,
				allowBlank : false
			} ]
		}, {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			items : [ {
				xtype : "textfield",
				name : "howOos",
				fieldLabel : "缺货处理"
			} ]
		}, {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			items : [ {
				xtype : "textareafield",
				name : "postscript",
				fieldLabel : "客户给商家的留言"
			} ]
		}, {
			xtype : "textfield",
			name : "orderSn",
			hidden : true,
			fieldLabel : "订单编号",
			value : parent.orderSn
		}, ]
		// 以json形式读取数据
		this.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "orderInfo",
			model : 'MB.model.OrderDetailModel'
		});
		var me = this;
		me.callParent(arguments);
	}
});