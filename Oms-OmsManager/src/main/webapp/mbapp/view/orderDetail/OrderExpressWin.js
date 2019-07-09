Ext.define("MB.view.orderDetail.OrderExpressWin", {
	extend: "Ext.window.Window",
	alias: "widget.orderExpressWin",
	title: "查看物理信息",
	width: 550,
	height: 600,
	layout: "fit",
	maskDisabled : false,
	modal : true,
	initComponent: function () {
		this.items = [{
			xtype: "form",
			margin: 5,
			border: false,
			frame: true,
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 90
			},
			items: [{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10',
				columnWidth : 1,
				items : [{
					xtype : "displayfield",
					fieldLabel : "交货单",
					name : "orderSn",
					columnWidth : .5
				},{
					xtype : "displayfield",
					fieldLabel : "送货方式",
					name : "deliveryTypeName",
					columnWidth : .5
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10',
				columnWidth : 1,
				items : [{
					xtype : "displayfield",
					fieldLabel : "承运商",
					name : "shippingName",
					columnWidth : .5
				},{
					xtype : "displayfield",
					fieldLabel : "货运单号",
					name : "invoiceNo",
					columnWidth : .5
				}]
			},{
				xtype: 'gridpanel',
				store: Ext.create('Ext.data.Store', {
					model: "Ext.data.Model",
				}),
				autoRender:true,
				width: '100%',
				loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
				resizable: true,
				forceFit: true,
				autoScroll : true,
				height: 450,
				columns: [{
					text: '时间',
					width: document.body.clientWidth * 0.3,
					align: 'center',
					dataIndex: 'time',
					sortable : false,
					menuDisabled : true
				},{
					text: '内容',
					width: document.body.clientWidth * 0.7,
					dataIndex: 'context',
					sortable : false,
					menuDisabled : true,
					renderer: function (value,meta) {
						meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';
						return value;
					}
				}]
			}]
		}];
		this.buttons = [
			{ text: "关闭", handler : function () { this.up("window").close();} }
		];
		this.callParent(arguments);
	}
});