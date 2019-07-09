Ext.define("MB.view.orderDetail.PayEditWin", {
	extend: "Ext.window.Window",
	alias: "widget.payEditWin",
	title: "编辑付款信息",
	width: 600,
	height: 300,
	layout: "fit",
	maskDisabled : false,
	modal : true,
	initComponent: function () {
		this.items = [{
			xtype: "form",
			margin: 10,
			border: false,
			frame: true,
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 90
			},
			items: [{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '5',
				columnWidth : 1,
				items : [{
					xtype : "displayfield",
					fieldLabel : "商品总金额",
					name : "goodsAmount",
					columnWidth : .5
				},{
					xtype : "displayfield",
					fieldLabel : "商品总价折扣",
					name : "totalPriceDiscount",
					columnWidth : .5
				},{
					xtype : "textfield",
					name : "masterOrderSn",
					hidden : true,
					fieldLabel : "主单编号",
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '5',
				columnWidth : 1,
				items : [{
					xtype : "displayfield",
					fieldLabel : "发票税额",
					name : "tax",
					columnWidth : .5
				},{
					xtype : "displayfield",
					fieldLabel : "折让金额",
					name : "discount",
					columnWidth : .5
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '5',
				columnWidth : 1,
				items : [{
					xtype : "numberfield",
					fieldLabel : "配送费用",
					name : "shippingTotalFee",
					allowBlank : false,
					allowDecimals: true,
					decimalPrecision:2,
					minValue: 0,
					columnWidth : .5
				},{
					xtype : "displayfield",
					fieldLabel : "已付款金额",
					name : "moneyPaid",
					columnWidth : .5
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '5',
				columnWidth : 1,
				items : [{
					xtype : "displayfield",
					fieldLabel : "保价费用",
					name : "insureTotalFee",
					columnWidth : .5
				},{
					xtype : "displayfield",
					fieldLabel : "使用余额",
					name : "surplus",
					columnWidth : .5
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '5',
				columnWidth : 1,
				items : [{
					xtype : "displayfield",
					fieldLabel : "支付费用",
					name : "payTotalFee",
					columnWidth : .5
				},{
					xtype : "displayfield",
					fieldLabel : "使用积分",
					name : "integralMoney",
					columnWidth : .5
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '5',
				columnWidth : 1,
				items : [{
					xtype : "displayfield",
					fieldLabel : "订单总金额",
					name : "totalFee",
					columnWidth : .5
				},{
					xtype : "displayfield",
					fieldLabel : "应付款金额",
					name : "totalPayable",
					columnWidth : .5
				}]
			}]
		}];
		this.buttons = [
		    { text: "保存", action: "doSaveShippingFee" },
		    { text: "关闭", handler: 
				function (btn) {
					var win = btn.up("window");
					win.close();
				} 
			}
		];
		this.callParent(arguments);
	}
});