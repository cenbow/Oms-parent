Ext.define("MB.view.orderDetail.PayEditForm", {
	extend: "Ext.form.Panel",
	alias: 'widget.payEditForm',
	id:'payEditForm',
	//title: '订单详情',
	width: '100%',
	frame: true,
//	head:true,
	bodyPadding:10,
	layout:'column',
	buttonAlign : 'center',// 按钮居中
	fieldDefaults: {
		labelAlign: 'right',
		margin:2
	},
	url : basePath + '/custom/orderStatus/editDeliveryInfo',
	initComponent: function () {
		this.items = [
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "displayfield", name : 'goodsAmount', fieldLabel : "商品总金额",value:0 ,columnWidth: .5 ,
							listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
						},
						{xtype : "numberfield", name : 'totalPriceDiscount', fieldLabel : "商品总价折扣",value:0,readOnly:true ,columnWidth: .5,
							listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
						} , {
							xtype : "textfield",
							name : "orderSn",
							hidden : true,
							fieldLabel : "订单编号",
							value : parent.orderSn
						}]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "numberfield", name : 'tax', fieldLabel : "发票税额",value:0,readOnly:true,columnWidth: .5 ,
							listeners:{
									'change':function(field){
										this.up('form').fillPayment(field);
									}
								}
						},
						{xtype : "numberfield", name : 'discount', fieldLabel : "折让金额",value:0,readOnly:true,columnWidth: .5,
							listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
						}
					]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{
							xtype: "numberfield",
							name: "shippingTotalFee",
							fieldLabel: "配送费用",
							value: 0,
							minValue: 0,
							maxValue: 999999,
							columnWidth: .5,
							listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
						},
						{ xtype: "numberfield", name: "moneyPaid", fieldLabel: "已付款金额",value:0,readOnly:true,columnWidth: .5 ,
							listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
						}
					]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{ xtype: "numberfield", name: "insureTotalFee", fieldLabel: "保价费用",value:0 ,readOnly:true,columnWidth: .5,
							listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
						},
						{ xtype: "numberfield", name: "surplus", fieldLabel: "使用余额",value:0,readOnly:true,columnWidth: .5 ,
							listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
						}
					]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{ xtype: "numberfield", name: "payTotalFee", fieldLabel: "支付费用",value:0,readOnly:true ,columnWidth: .5,
							listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
						},
						{ xtype: "numberfield", name: "integralMoney", fieldLabel: "使用积分",value:0 ,readOnly:true,columnWidth: .5,
							listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
						}
					]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{ xtype: "displayfield", name: "totalFee", fieldLabel: "订单总金额", columnWidth: .5,value:0 },
						{ xtype: "displayfield", name: "payTotal", fieldLabel: "应付款金额", columnWidth: .5,value:0 }
					]
				}
			]
		//以json形式读取数据
		this.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "orderInfo",
			model : 'MB.model.OrderDetailModel'
		});
		var me = this;
		me.callParent(arguments);
		
	},
	fillPayment:function(field){
		var goodsAmount=this.getForm().findField('goodsAmount').getValue(),
			tax=this.getForm().findField('tax').getValue(),
			shippingTotalFee=this.getForm().findField('shippingTotalFee').getValue(),
			insureTotalFee=this.getForm().findField('insureTotalFee').getValue(),
			payTotalFee=this.getForm().findField('payTotalFee').getValue();
			
		var	totalPriceDiscount=this.getForm().findField('totalPriceDiscount').getValue(),
			discount=this.getForm().findField('discount').getValue(),
			moneyPaid=this.getForm().findField('moneyPaid').getValue(),
			surplus=this.getForm().findField('surplus').getValue(),
			integralMoney=this.getForm().findField('integralMoney').getValue();
		
		var totalFee=goodsAmount+tax+shippingTotalFee+insureTotalFee+payTotalFee;
		var payTotal=(totalFee-totalPriceDiscount-discount-moneyPaid-surplus-integralMoney).toFixed(2);
		
		this.getForm().findField('totalFee').setValue(totalFee);
		this.getForm().findField('payTotal').setValue(payTotal);
	}
});