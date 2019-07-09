Ext.define("MB.view.exchOrder.ExchangeAccountSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.exchangeAccountSetModule',
	width: '100%',
	frame: true,
	title:'换单账目信息&nbsp;&nbsp;&nbsp;',
	head:true,
	layout:'column',
	buttonAlign : 'center',// 按钮居中
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
	//collapsed: true,
	initComponent: function () {
		this.items = [{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .1 },
				{xtype : "displayfield",  columnWidth: .1 } ,
				{xtype : "displayfield",  columnWidth: .1 } ,
				{xtype : "displayfield",  columnWidth: .1 } ,
				{xtype : "displayfield", name : 'tel', fieldLabel : "商品总金额",columnWidth: .1 },
				{xtype : "displayfield", name : 'tel', fieldLabel : "- 折让",columnWidth: .1 },
				{xtype : "displayfield", name : 'tel', fieldLabel : "+ 发票税额",columnWidth: .1 },
				{xtype : "displayfield", name : 'tel', fieldLabel : "+ 配送费用",columnWidth: .1 },
				{xtype : "displayfield", name : 'tel', fieldLabel : "+ 保价费用",columnWidth: .1 },
				{xtype : "displayfield", name : 'tel', fieldLabel : "+ 支付费用",columnWidth: .1 }
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .2 },
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield", name : 'tel', fieldLabel : "= 订单总金额",columnWidth: .2 }
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .1 },
				{xtype : "displayfield",  columnWidth: .1 } ,
				{xtype : "displayfield",  columnWidth: .1 } ,
				{xtype : "displayfield",  columnWidth: .1 } ,
				{xtype : "displayfield",  columnWidth: .1 } ,
				{xtype : "displayfield",  columnWidth: .1 } ,
				{xtype : "displayfield",  columnWidth: .1 } ,
				{xtype : "displayfield", name : 'tel', fieldLabel : "- 已付款金额",columnWidth: .1 },
				{xtype : "displayfield", name : 'tel', fieldLabel : "- 使用余额",columnWidth: .1 },
				{xtype : "displayfield", name : 'tel', fieldLabel : "- 使用红包【】",columnWidth: .1 }
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .2 },
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield", name : 'tel', fieldLabel : "= 应付款金额",columnWidth: .2 }
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: 1 }
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .2 },
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield", name : 'tel', fieldLabel : "所退商品的总金额", columnWidth: .2 } ,
				{xtype : "displayfield", name : 'tel', fieldLabel : "- 所退商品的总折让值",columnWidth: .2 }
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .2 },
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield", name : 'tel', fieldLabel : "= 所退商品成交价总金额",columnWidth: .2 }
			]
		} ,{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .2 },
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield",  columnWidth: .2 } ,
				{xtype : "displayfield", name : 'tel', fieldLabel : "- 所退商品分摊红包总金额", columnWidth: .2 } ,
				{xtype : "displayfield", name : 'tel', fieldLabel : "= 所退商品财务总金额",columnWidth: .2 }
			]
		} 
		];
		var me = this;
        me.callParent(arguments);
	},
    onCloseClick: function () {
        if (this.ownerCt) {
            this.ownerCt.remove(this, true);
        }
    }
});