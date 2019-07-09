Ext.define("MB.view.handOrder.HandOrderAddWin", {
	extend: "Ext.window.Window",
	alias: "widget.HandOrderAddWin",
	title: "手动打单导入",
	width: 1000,
	height: 500,
	layout: "fit",
	maskDisabled : false,
	modal : true,
	initComponent: function () {
		this.items = {
			xtype: "form",
			margin: 5,
			border: false,
			frame: true,
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 90
			},
			items: [{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{ xtype: "textfield", name: "orderId", fieldLabel: "<font color='red'>订单编号</font>", columnWidth:.25},
					{ xtype: "textfield", name: "skuId", fieldLabel: "<font color='red'>平台商品码</font>" , columnWidth:.25},
					{ xtype: "textfield", name: "iid", fieldLabel: "平台商品款码" , columnWidth:.25},
					{ xtype: 'textfield', name: 'outerId' ,fieldLabel: "<font color='red'>11位商品码</font>", columnWidth:.25}     
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{ xtype: "numberfield", name: "price", fieldLabel: "<font color='red'>商品成交价格</font>" , columnWidth:.33,allowDecimals: true,decimalPrecision:2,minValue: 0},
					{ xtype: "textfield", name: "receiverName", fieldLabel: "<font color='red'>收货人</font>" , columnWidth:.33},
					{ xtype: "textfield", name: "receiverPhone", fieldLabel: "<font color='red'>收货人电话</font>" , columnWidth:.33}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{ xtype: "numberfield", name: "num", fieldLabel: "<font color='red'>商品数量</font>" , columnWidth:.25,allowDecimals: false,minValue: 0},
					{
						xtype : "combobox",
						store : Ext.create('Ext.data.Store', {
									model : "MB.model.ComboModel",
									data : [['common', "普通商品|common"], ['gift', '赠品|gift'],['group', "套装|group"], ['prize', '奖品|prize']]
								}),
						displayField : 'n',
						valueField : 'v',
						queryMode : 'local',
						hiddenName : 'extensionCode',
						editable : false, 
						name : 'extensionCode', 
						fieldLabel : "<font color='red'>商品扩展属性</font>",
						value : 'common',
						columnWidth:.25
					},
					{ xtype: "textfield", name: "province", fieldLabel: "<font color='red'>地区省</font>" , columnWidth:.25},
					{ xtype: "textfield", name: "city", fieldLabel: "<font color='red'>地区市</font>" , columnWidth:.25}   
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{ xtype: "textfield", name: "county", fieldLabel: "<font color='red'>地区区</font>" , columnWidth:.33},
					{ xtype: "textfield", name: "goodsSn", fieldLabel: "6位商品码" , columnWidth:.33},
					{ xtype: "numberfield", name: "goodsNum", fieldLabel: "<font color='red'>订单商品数量</font>" , columnWidth:.33,allowDecimals: false,minValue: 0}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{ xtype: "textfield", name: "orderStatus", fieldLabel: "订单状态" , columnWidth:.25},
					{ xtype: "textfield", name: "payStatus", fieldLabel: "支付状态" , columnWidth:.25},
					{ xtype: "numberfield", name: "totalFee", fieldLabel: "订单总金额" , columnWidth:.25,allowDecimals: true,decimalPrecision:2,minValue: 0},
					{ xtype: "numberfield", name: "shippingFee", fieldLabel: "<font color='red'>运费</font>" , columnWidth:.25,allowDecimals: true,decimalPrecision:2,minValue: 0}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{ xtype: "textfield", name: "createTime", fieldLabel: "<font color='red'>订单创建时间</font>" , columnWidth:.25,
						listeners : {
							render : function(p) {p.getEl().on('click',function() {
													WdatePicker({
														startDate : '%y-%M-%d 00:00:00',
														dateFmt : 'yyyy-MM-dd HH:mm:ss'
													});
												});
							}
						},
					},
					{ xtype: "textfield", name: "payTime", fieldLabel: "<font color='red'>支付时间</font>" , columnWidth:.25,
						listeners : {
							render : function(p) {p.getEl().on('click',function() {
													WdatePicker({
														startDate : '%y-%M-%d 00:00:00',
														dateFmt : 'yyyy-MM-dd HH:mm:ss'
													});
												});
							}
						},	
					},
					{ xtype: "textfield", name: "modifiedTime", fieldLabel: "订单更新时间" , columnWidth:.25,
						listeners : {
							render : function(p) {p.getEl().on('click',function() {
													WdatePicker({
														startDate : '%y-%M-%d 00:00:00',
														dateFmt : 'yyyy-MM-dd HH:mm:ss'
													});
												});
							}
						},	
					},
					{ xtype: "textfield", name: "addTime", fieldLabel: "订单添加时间" , columnWidth:.25,
						listeners : {
							render : function(p) {p.getEl().on('click',function() {
													WdatePicker({
														startDate : '%y-%M-%d 00:00:00',
														dateFmt : 'yyyy-MM-dd HH:mm:ss'
													});
												});
							}
						},	
					}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{ xtype: "numberfield", name: "payment", fieldLabel: "<font color='red'>订单支付金额</font>" , columnWidth:.25,allowDecimals: true,decimalPrecision:2,minValue: 0},
					{ xtype : 'onlineChannelShopCombo',name : 'channelCode', fieldLabel : "<font color='red'>线上店铺 </font>",columnWidth:.25},
					{ xtype: "textfield", name: "invoiceType", fieldLabel: "发票类型" , columnWidth:.25},
					{ xtype: "textfield", name: "shippingCode", fieldLabel: "承运商编码" , columnWidth:.25}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
						{ xtype: "textfield", name: "title", fieldLabel: "<font color='red'>商品名称</font>" , columnWidth:.5},
						{ xtype: "textfield", name: "district", fieldLabel: "地区街道" , columnWidth:.5}	
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
				        { xtype: "textfield", name: "sellerRemark", fieldLabel: "卖家留言" , columnWidth:1}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
				        { xtype: "textfield", name: "remark", fieldLabel: "买家留言" , columnWidth:1}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
				        { xtype: "textfield", name: "invoiceName", fieldLabel: "发票抬头" , columnWidth:.5},
				        { xtype: "textfield", name: "payWay", fieldLabel: "支付方式" , columnWidth:.25},
				        { xtype: "textfield", name: "bgUserId", fieldLabel: "邦购币账号ID" , columnWidth:.25}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
				        { xtype: "textfield", name: "address", fieldLabel: "<font color='red'>地区详细地址</font>" , columnWidth:1}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
				        { xtype: "textfield", name: "receiverAddress", fieldLabel: "收货人完整地址" , columnWidth:.75,labelWidth: 100},
				        {
				        	xtype : "combobox",
							store : Ext.create('Ext.data.Store', {
										model : "MB.model.ComboModel",
										data : [['01', "到付"], ['02', '自付']]
									}),
							displayField : 'n',
							valueField : 'v',
							queryMode : 'local',
							hiddenName : 'wayPaymentFreight',
							editable : false, 
							name : 'wayPaymentFreight', 
							value : '02',
							fieldLabel : "运费付款方式",
							columnWidth:.25
				        },
				        { xtype: "textfield", name: "channelName", fieldLabel: "线上店铺名称",hidden:true},
				        { xtype: "textfield", name: "note", fieldLabel: "备注",hidden:true},
				        { xtype: "textfield", name: "flag", fieldLabel: "flag",hidden:true,value:'0'},
				        { xtype: "textfield", name: "errorMsg", fieldLabel: "errorMsg",hidden:true,value:''},
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				html:'<div align="right"><font color="red">(注意：红色标题为必填项！)</font></div>'
			}]
		};
		this.buttons = [
			{ text: "保存", action: "save" },
			{ text: "关闭", action: "close" }
		];
		this.callParent(arguments);
	}
});