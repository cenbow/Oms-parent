Ext.define("MB.view.exchOrder.ExchangePaySetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.exchangePaySetModule',
	width: '100%',
	frame: true,
	title:'换单付款信息&nbsp;&nbsp;&nbsp;',
	head:true,
	layout:'column',
	buttonAlign : 'center',// 按钮居中
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
	//collapsed: true,
	initComponent: function () {
		this.items = [ {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .7},
				{xtype : "numberfield",  name : 'goodsAmount', fieldLabel : "商品总金额",width: 200,readOnly:true,value:0.00,
					listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
				} ,
				{xtype : "numberfield",  name : 'discount', fieldLabel : "- &nbsp;&nbsp;&nbsp;&nbsp;折让",width: 200,value:0.00,
					listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
				} ,
				{xtype : "numberfield",  name : 'shippingTotalFee', fieldLabel : "+  &nbsp;配送费用",width: 200,value:0.00,
					listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
				} 
			]
		}, {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .7 },
				{xtype : "displayfield",  width:400 } ,
				{xtype : "numberfield", name : 'totalFee', fieldLabel : "=&nbsp;订单总金额",width: 200,value:0.00,readOnly:true}
			]
		}, {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .7 },
				{xtype : "displayfield",  width:200 } ,
				{xtype : "numberfield", name : 'moneyPaid',id:'moneyPaid', fieldLabel : "-&nbsp;已付款金额",width: 200 ,readOnly:true,value:0.00,
					listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
				},
				{xtype : "numberfield", name : 'bonus', fieldLabel : "-&nbsp;&nbsp;使用红包",width: 200,value:0.00,
					listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
				}
			]
		}, {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .7 },
				{xtype : "displayfield",  width:400 } ,
				{xtype : "numberfield", name : 'totalPayable', fieldLabel : "=&nbsp;应再付款金额",width: 200,value:0.00,readOnly:true}
			]
		}
		];
		var me = this;
        me.callParent(arguments);
	},
    fillPayment: function (field) {
    	var goodsAmount=this.getForm().findField('goodsAmount').getValue();
    	var discount=this.getForm().findField('discount').getValue();
    	var shippingTotalFee=this.getForm().findField('shippingTotalFee').getValue();
    	var totalFee=goodsAmount-discount+shippingTotalFee;
    	var moneyPaid=this.getForm().findField('moneyPaid').getValue();
    	var bonus=this.getForm().findField('bonus').getValue();
    	var totalPayable=totalFee-moneyPaid-bonus;
    	this.getForm().findField('totalPayable').setValue(totalPayable);
    	this.getForm().findField('totalFee').setValue(totalFee);
    	
    	if(exchangeOrderSn){
    		var rawData=Ext.getCmp('exchangeForm').getForm().reader.rawData;
    		if(rawData){
    			this.getForm().findField('totalPayable').setValue(rawData.result.totalPayable);
    		}
    	}
    }
});