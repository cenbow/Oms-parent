Ext.define("MB.view.orderExchange.OrderExchangeRetOrdModule", {
	extend : "Ext.form.Panel",
	alias : 'widget.orderExchangeRetOrdModule',
	id : 'orderExchangeRetOrdModule',
	width: '100%',
	frame: true,
	bodyStyle: {
		padding: '8px'
	},
	autoScroll : true,
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'right',
	},
	bodyBorder:true,
	initComponent: function(){
		var me = this;
		var selModel = false;
		var plugins = false;
		if(returnType!=2){
			selModel=new Ext.selection.CheckboxModel({checkOnly:true}),
			plugins={
				ptype: 'cellediting',
				clicksToEdit: 1,
				listeners: {
					beforeedit: me.onCellBeforeEdit,
					afteredit : me.onCellAfterEdit
				}
			}
		}
		me.items = [{//基本信息
			xtype : 'form',
			title:'基本信息&nbsp;&nbsp;&nbsp;',
			itemId:'baseInfoModule',
			frame: true,
			head:true,
			fieldDefaults: {
				labelAlign: 'right'
			},
			items : [{
				xtype: 'fieldcontainer',
				layout: 'column',
				columnWidth: 1,
				labelWidth: 200,
				items : [{
					xtype : "displayfield", name : 'returnSn', fieldLabel : "退单号", columnWidth: .33,
						renderer: function(value) {
							if (value != undefined && value != null ) {
								var url = order_return_url + value +"&isHistory=0";
								return "<a href=" +url + " target='_blank' >" + value + "</a>";
							}
						}
				},{
					xtype : "displayfield", name : 'returnType', fieldLabel : "退单类型", columnWidth: .33,
						renderer : function (value) { 
							if(value == 1){
								return "退货退款单";
							} else if(value == 2){
								return "拒收入库单";
							} else if(value == 3){
								return "无货退款单";
							} else if(value == 4){
								return "额外退款单";
							}
						}
				},{
					xtype : "commonhaverefund", name : 'haveRefund', fieldLabel : "是否退款", columnWidth: .24,labelWidth: 100
				},{
					xtype : "displayfield", columnWidth: .04
				},{
					xtype : "button" , text :"更新",columnWidth: .05,id:'updateHaveRefund', action: 'updateHaveRefund',labelAlign: 'right'
				}]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				columnWidth: 1,
				labelWidth: 200,
				items : [{
					xtype : "displayfield", name : 'relatingOrderSn', fieldLabel : "关联订单号", columnWidth: .33,
						renderer: function(value) {
							if (value != undefined && value != null ) {
								var url = order_info_url +"?masterOrderSn="+masterOrderSn+"&orderSn="+ value +"&isHistory=0";
								return "<a href=" +url + " target='_blank' >" + value + "</a>";
							}
						}
				},{
					xtype : "displayfield", name : 'channelName', fieldLabel : "渠道来源", columnWidth: .33
				},{
					xtype : "textfield", name : 'returnInvoiceNo', fieldLabel : "退货面单号", columnWidth: .24 
				},{
					xtype : "displayfield", columnWidth: .04
				},{
					xtype : "button" , text :"更新",columnWidth: .05,id:'updateInvoiceNo', action: 'updateInvoiceNo',labelAlign: 'right'
				}]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				columnWidth: 1,
				labelWidth: 200,
				items : [{
					xtype : "displayfield", name : 'addTime', fieldLabel : "退单时间", columnWidth: .33
				},{
					xtype : "displayfield", name : 'checkInTime', fieldLabel : "入库时间", columnWidth: .33 
				},{
					xtype : "commonsystemShipping", name : 'returnExpress',fieldLabel : "退货承运商", columnWidth: .24,labelWidth: 100
				},{
					xtype : "displayfield", columnWidth: .04
				},{
					xtype : "button" , text :"更新",columnWidth: .05,id:'updateExpress', action: 'updateExpress',labelAlign: 'right'
				}]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				columnWidth: 1,
				labelWidth: 200,
				items : [{
					xtype : "displayfield", name : 'clearTime', fieldLabel : "结算时间", columnWidth: .33 
				},{
					xtype : "displayfield", name : 'returnStatusDisplay', fieldLabel : "退单状态",style:'color:Red', columnWidth: .33,
					renderer : function (value) { 
						return "<span style='color:red;'>"+value+"</span>";
					}
				},{
					xtype : "commonreturnerpwarehouse",name : 'depotCode', fieldLabel : "退货仓库",  columnWidth: .24,labelWidth: 100
//					xtype : "textfield", fieldLabel : '退货仓库', name : 'depotCode', value : 'DEFAULT', columnWidth : .24,labelWidth: 100
				},{
					xtype : "displayfield", columnWidth: .04 
				},{
					xtype : "button" , text :"更新",columnWidth: .05,id:'updateDepotCode', action: 'updateDepotCode',labelAlign: 'right'  
				}]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				columnWidth: 1,
				labelWidth: 200,
				items : [{
					xtype : "commonprocessType", name : 'processType', fieldLabel : "处理方式", columnWidth: .33,labelWidth: 100 
				},{
					xtype : "commonsettlementType", name : 'returnSettlementType', fieldLabel : "退款类型", columnWidth: .33,labelWidth: 100
				},{
					xtype : "orderCustomDefineCombo", name : 'returnReason', fieldLabel : "退单原因", columnWidth: .33,labelWidth: 100
				}]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				columnWidth: 1,
				labelWidth: 200,
				items : [{
					xtype : "textfield", name : 'returnDesc', fieldLabel : "退单备注", columnWidth: .66
				},{
					xtype : "textfield", name : 'newOrderSn', fieldLabel : "换货单号", columnWidth: .33
				}]
			}],//基本信息的items
			builtParam: function(type,form){
				var params = form.getValues();
				var orderExchangeRetOrdModule=Ext.getCmp('orderExchangeRetOrdModule');
				if(orderExchangeRetOrdModule){
					var returnCommon=orderExchangeRetOrdModule.getForm().reader.rawData.returnCommon;
					if(type == 'updateHaveRefund'){
						params['createOrderReturn.haveRefund']=params.haveRefund;
						params['createOrderReturn.relatingReturnSn']=returnCommon.relatingOrderSn;
						params['createOrderReturn.returnSn']=returnCommon.returnSn;
					}else{
						params['createOrderReturnShip.relatingReturnSn']=returnCommon.returnSn;
						params['createOrderReturnShip.returnExpress']=params.returnExpress;
						params['createOrderReturnShip.returnInvoiceNo']=params.returnInvoiceNo;
						params['createOrderReturnShip.depotCode']=params.depotCode;
					}
					params.orderReturnSn=returnCommon.returnSn;//退单Sn
				}
				return params;
			}
		},{//商品信息
			xtype : 'grid',
			title : '商品信息',
			itemId:'goodsListModule',
			frame : true,
			columnLines : true,
			resizable : true,
			viewConfig:{
				enableTextSelection : true
			},
			store: Ext.create('Ext.data.Store', {
				model: "Ext.data.Model"
			}),
			selModel:selModel,
			plugins:plugins,
			columns : [{ text: '商品名称', width: 150,align: 'center', dataIndex: 'goodsName',
				renderer: function(value, meta, record) {
					if(value){
						var max = 15;  //显示多少个字符
						meta.tdAttr = 'data-qtip="' + value + '"';
						return value.length < max ? value :value.substring(0, max - 3) + '...';
					}else {
						return '';
					}
				}
			},
			{ dataIndex: 'extensionId' ,sortable:false,menuDisabled : true,hidden:true},
			{ dataIndex: 'baseBvValue' ,sortable:false,menuDisabled : true,hidden:true},
			{ text: '商品属性', width: 75,align: 'center', dataIndex: 'extensionCode' ,
				renderer : function (value) { 
					if(value == 'group'){
						return "套装";
					}else if(value.indexOf('gif') != -1){
						return "赠品";
					}else{
						return "普通商品";
					}
				}
			},
			{ text: '货号',align: 'center', dataIndex: 'goodsSn' ,sortable:false,menuDisabled : true},
			{ header: '规格', columns: 
				[ {
					header : "颜色",
					width : 80,
					dataIndex: 'goodsColorName',
					sortable : false,
					menuDisabled : true,
					renderer: function(value, meta, record) {
						if(value){
							var max = 15;  //显示多少个字符
							meta.tdAttr = 'data-qtip="' + value + '"';
							return value.length < max ? value :value.substring(0, max - 3) + '...';
						}
						return ' ';
					}
				} , {
					header : "尺寸",
					width : 80,
					dataIndex: 'goodsSizeName',
					sortable : false,
					menuDisabled : true,
					renderer: function(value, meta, record) {
						if(value){
							var max = 15;  //显示多少个字符
							meta.tdAttr = 'data-qtip="' + value + '"';
							return value.length < max ? value :value.substring(0, max - 3) + '...';
						}else{
							return '';
						}
					}
					
				} ]
			},
			{ text: '企业SKU码', width: 120,align: 'center', dataIndex: 'customCode',sortable:false,menuDisabled : true,
				renderer: function(value, meta, record) {
					var max = 15;  //显示多少个字符
					meta.tdAttr = 'data-qtip="' + value + '"';
					if(value)return value.length < max ? value :value.substring(0, max - 3) + '...';
				
				}},
			{ text: '商品价格', width: 75,align: 'center', dataIndex: 'marketPrice' ,sortable:false,menuDisabled : true,
					renderer : function (value) {return "￥"+value+"元"}
			},
			{ text: '成交价格', width: 75,align: 'center', dataIndex: 'goodsPrice' ,sortable:false,menuDisabled : true,
				renderer : function (value) {return "￥"+value+"元"}
			},
			{ text: '财务价格', width: 75,align: 'center', dataIndex: 'settlementPrice' ,sortable:false,menuDisabled : true,
				renderer : function (value) {return "￥"+value+"元"}
			},
			{ text: '分摊金额', width: 75,align: 'center', dataIndex: 'shareBonus',sortable:false,menuDisabled : true ,
				renderer : function (value) {return "￥"+value+"元"}
			},
			{ text: '购买数量', width: 75,align: 'center', dataIndex: 'goodsBuyNumber' ,sortable:false,menuDisabled : true},
			{ text: '财务分摊金额', width: 95,align: 'center', dataIndex: 'shareSettle' ,sortable:false,menuDisabled : true,
				renderer : function (value) {return "￥"+value+"元"}, hidden: true
			},
			{ text: '所属发货仓', width: 140,align: 'center', dataIndex: 'osDepotCode' ,sortable:false,menuDisabled : true},
			{ text: '赠送美力值', width: 75,align: 'center', dataIndex: 'bvValue' ,sortable:false,menuDisabled : true},
			{ text: '门店退货量', width: 75,align: 'center', dataIndex: 'shopReturnCount' ,sortable:false,menuDisabled : true},
			{ text: '已退货量', width: 75,align: 'center', dataIndex: 'havedReturnCount' ,sortable:false,menuDisabled : true},
			{ text: '退货量', width: 75,align: 'center', dataIndex: 'canReturnCount' ,sortable:false,menuDisabled : true,
				renderer : function (value, meta, record) {
					meta.style='background:'+editColor;
					return value;
				},
				editor: {
					xtype: 'numberfield',
					allowBlank: false,
					minValue: 0,
					allowDecimals : false
				}
			},
			{ text: '退差价数量', width: 75,align: 'center', dataIndex: 'priceDifferNum' ,sortable:false,menuDisabled : true,
				renderer : function (value, meta, record) {
					meta.style='background:'+editColor;
					return value;
				},
				editor: {
					xtype: 'numberfield',
					allowBlank: false,
					minValue: 0,
					allowDecimals : false
				},
				hidden: true
			},
			{ text: '退差价单价', width: 75,align: 'center', dataIndex: 'priceDifference' ,sortable:false,menuDisabled : true,
				renderer : function (value, meta, record) {
					meta.style='background:'+editColor;
					return value;
				},
				editor: {
					xtype: 'numberfield',
					allowBlank: false,
					minValue: 0,
					decimalPrecision : 2,//精确到小数点后两位  
					allowDecimals : true//允许输入小数  
				},
				hidden: true
			},
			{ text: '退差价小计', width: 75,align: 'center', dataIndex: 'priceDiffTotal' ,sortable:false,menuDisabled : true,
				renderer:function (value, cellMeta, record, rowIndex, columnIndex, store){
					var priceDifferNum=record.get('priceDifferNum');
					var priceDifference=record.get('priceDifference');
					return priceDifferNum*priceDifference;
				}
			},
			{ text: '原因', width: 100,align: 'center', dataIndex: 'returnReason' ,sortable:false,menuDisabled : true,
				renderer : function (value, meta, record) {
					meta.style='background:'+editColor;
					return value;
				},
				editor: {
					xtype: 'textfield',
					allowBlank: false,
					allowDecimals : false
				}
			},
			{ text: '图片url',align: 'center', dataIndex: 'goodsThumb' ,sortable:false,menuDisabled : true,hidden:true},
			{
				text : '商品销售模式', align : 'center', dataIndex : 'salesMode', sortable : false, menuDisabled : true, hidden : true
			},{
				text : '供销商',
				width : 120,
				align : 'center',
				dataIndex : 'seller',
				sortable : false,
				menuDisabled : true,
				hidden : true,
				renderer : function(value, meta, record) {
					var msg = value;
					if(!msg){
						msg ="";
					}
					if (value && (value == 'MB' || value == 'HQ01') ) {
						msg = '<font style="color:green">' + value + '</font>'
					} else {
						msg = '<font style="color:red">' + value + '</font>'
					}
					return msg;

				}
			}],
			listeners:{
				'selectionchange':function(sm,rowIndex,record,d){
					var orderExchangeRetOrdModule = Ext.getCmp('orderExchangeRetOrdModule');
					var goodsListModule = orderExchangeRetOrdModule.down('#goodsListModule');//商品列表
					var payDetailModule = orderExchangeRetOrdModule.down('#payDetailModule');//退单账目
					//获取选中的行
					var items=goodsListModule.getSelectionModel().selected.items;
					var goodData=[];
					for(var i=0;i<items.length;i++){
						goodData.push(items[i].data);
					}
					//根据选中商品计算各种金额
					me.paySet(goodData);
					if(items.length>0){
						var rawData={};
						var center = null;
						if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
							center = Ext.getCmp('orderExchangeRetOrdModule');
						}
						if(center){
							rawData=center.getForm().reader.rawData;
							if(items.length==rawData.returnGoods.length){//复选框全部选中时
								var orderPays=rawData.orderPays;
								if(orderPays){
					  				for(var i=0;i<orderPays.length;i++){
					  					if(center.getForm().findField("setPaymentNum"+(i+1)))
					  					center.getForm().findField("setPaymentNum"+(i+1)).setValue(orderPays[i].payTotalfee);
					  				}
					  			}
					  			return;
							}
						}
					}
				  },
				  'cellclick' : function(grid, rowIndex, columnIndex, e) { 
						//获取选中的行
						var rows=grid.getSelectionModel().selected.items;
						var items=grid.getSelectionModel().selected.items;
						var goodData=[];
						for(var i=0;i<items.length;i++){
							goodData.push(items[i].data);
						}
						me.paySet(goodData);
						var len=rows.length;
						var editFlag=false;
						for(var i=0;i<len;i++){
							//判断当前行的id是否在选中行里
							if(e.id==rows[i].id){
								editFlag=true;
								break;
							}
						}
						return editFlag;
					}
			}
		},{//退单付款信息
			xtype : 'form',
			title:'退单付款信息&nbsp;&nbsp;&nbsp;',
			itemId:'payInfoModule',
			frame : true,
			fieldDefaults: {
				labelAlign: 'left'
			},
			items : [{
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "displayfield", name : 'returnTotalFee', fieldLabel : "退款总金额",width:170,value:'0',labelAlign: 'right'},
					{xtype : "displayfield", width:30,value:'='},
					{xtype :'commonsystemPayment',itemId:'setPayment1',name:'setPayment1',fieldLabel:false,width: 150,
						listeners:{
							'change':function(field){
								me.fillPayment(field);
							}
						}
					},
					{xtype : "numberfield", name : 'setPaymentNum1', fieldLabel:false,value:'0',width:90,labelAlign: 'right',minValue:0,
						listeners:{
							'change':function(field, newvalue,oldvalue){
								me.fillPaymentNum(field);
							}
						}
					}
				]
			}]
		},{//退单账目
			xtype : 'form',
			title : '退单账目&nbsp;&nbsp;&nbsp;',
			itemId:'payDetailModule',
			frame : true,
			items : [{
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth:1,
				items: [
					{xtype : "displayfield", name : 'returnGoodsMoney', fieldLabel : "退商品金额（已减折让）",labelWidth:180,value:0.00,columnWidth:.25},
					{xtype : "displayfield", name : 'totalPriceDifference', fieldLabel : "+ 退商品差价",width:150,value:0.00},
					{xtype : "returnShippingCombo", name : 'returnShipping', fieldLabel : "+ 退配送费用",width:200,columnWidth:.17,labelWidth:100,
						decimalPrecision : 2,//精确到小数点后两位  
						allowDecimals : true,//允许输入小数  
						listeners:{
							'change':function(field){
								me.payTotalSet();
							}
						}
					},
					{xtype : "numberfield", name : 'returnOtherMoney', fieldLabel : "+ 退其他费用",value:0.00,columnWidth:.17,
						decimalPrecision : 2,//精确到小数点后两位  
						allowDecimals : true,//允许输入小数  
						nanText :'请输入有效的数字',//无效数字提示  
						listeners:{
							'change':function(field){
								me.payTotalSet();
							}
						}
					}, {
							xtype : "numberfield",
							name : 'totalIntegralMoney',
							fieldLabel : "- 使用积分金额",
							value : 0.00,
							columnWidth : .17,
							decimalPrecision : 2,// 精确到小数点后两位
							allowDecimals : true,// 允许输入小数
							nanText : '请输入有效的数字',// 无效数字提示
							listeners : {
								'change' : function(field) {
									me.payTotalSet();
								}
							}
						},
					{xtype : "returnBonusMoneyCombo", name : 'returnBonusMoney', fieldLabel : "- 红包金额",columnWidth:.17,labelWidth:100,
						listeners:{
							'change':function(field){
								me.payTotalSet();
							}
						}
					}
				]
			} , {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				itemId:'paySetFieldContainer',
				layout: 'column',
				columnWidth:1,
				items: []
			} , {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth:1,
				items: [
					{xtype : "displayfield", name : 'returnTotalFee', fieldLabel : "= 退款总金额",value:0.00,labelWidth:180, columnWidth:.2 ,
						listeners:{
							'change':function(field){
								//变更相应的换单页的已付款金额
								var moneyPaid = Ext.getCmp('orderExchangeChanOrdModule').down('#exchangePayInfo').down('#moneyPaid');
								if(moneyPaid){
									moneyPaid.setValue(field.getValue());
								}
							}
						}
					},{
						xtype : "displayfield",
						columnWidth : .75
					}
				]
			}]
		}];//退单页的items
		this.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "returnCommon",
			model : 'MB.model.ReturnCommonModel'
		}),
		this.callParent(arguments);
	},
	initData : function(){
		if(orderSn == '' && returnSn == ''){
			console.error('换货单-退单数据加载异常，退单号，关联订单号为空');
			return;
		}
		//获取退单tab的组件
		var me = this;
		var baseInfoModule = me.down('#baseInfoModule');//基本信息
		var goodsListModule = me.down('#goodsListModule');//商品列表
		var payInfoModule = me.down('#payInfoModule');//退单付款信息
		var payDetailModule = me.down('#payDetailModule');//退单账目信息
		//加载数据
		me.load({
			url : basePath + '/custom/orderReturn/getOrderReturnDetail',
			params : {
				"relOrderSn" : orderSn,
				"returnSn" :returnSn,
				"returnType" : returnType
			},
			timeout:90000,
			success : function(opForm, action) {
				//获取请求数据
				var returnData = opForm.reader.rawData;
				var returnCommon = returnData.returnCommon;
				var returnGoods = returnData.returnGoods;
				var returnAccount = returnData.returnAccount;
				var orderShipType = returnData.orderShipType;
				payDetailModule.getForm().findField('returnShipping').setValue(0);//退配送费
				payDetailModule.getForm().findField('returnOtherMoney').setValue(0);//退其他费用
				payDetailModule.getForm().findField('returnBonusMoney').setValue(0);//红包金额
				
				//根据退单类型隐藏商品列表的显示列
				if(returnCommon.returnType==1||returnCommon.returnType==2){
					goodsListModule.columns[12].hide();//财务分摊金额
					for(var i=17;i<=19;i++){//退差价
						goodsListModule.columns[i].hide();
					}
				}else if(returnCommon.returnType==3){
					for(var i=13;i<=19;i++){
						if(i!=16){
							goodsListModule.columns[i].hide();
						}
					}
				}else{
					for(var i=12;i<=16;i++){
						goodsListModule.columns[i].hide();
					}
				}
				
				//给商品列表赋值
				goodsListModule.getStore().loadData(returnGoods);
				/*原订单信息初始化的时候已经给全局变量channelCode赋值了，订单和退单的channelCode应该是相同的，这里先隐藏掉
				channelCode = opForm.reader.rawData.returnCommon.channelCode;*/
				var siteCode = returnCommon.siteCode;
				if(returnSn){
					var returnShipping = returnAccount.returnShipping;//退配送费
					var returnOtherMoney = returnAccount.returnOtherMoney;//退其他费用
					var returnBonusMoney = returnAccount.returnBonusMoney;//红包金额
					var totalIntegralMoney = returnAccount.totalIntegralMoney;//使用积分金额
					
					if(returnShipping) payDetailModule.getForm().findField('returnShipping').setValue(returnShipping);
					if(returnOtherMoney) payDetailModule.getForm().findField('returnOtherMoney').setValue(returnOtherMoney);
					if(returnBonusMoney) payDetailModule.getForm().findField('returnBonusMoney').setValue(returnBonusMoney);
					if(totalIntegralMoney) payDetailModule.getForm().findField('totalIntegralMoney').setValue(totalIntegralMoney);
				}
				// 退货仓
				baseInfoModule.down('commonreturnerpwarehouse').getStore().on('beforeload', function (store){
					params = { "siteCode" : siteCode};
					Ext.apply(store.proxy.extraParams, params);
				});
				baseInfoModule.down('commonreturnerpwarehouse').getStore().load();
				//退单原因
				baseInfoModule.down('orderCustomDefineCombo').getStore().on('beforeload', function (store){
					params = { "type" : 1};
					Ext.apply(store.proxy.extraParams, params);
				});
				baseInfoModule.down('orderCustomDefineCombo').getStore().load();
				if (siteCode != 'Chlitina') {
					baseInfoModule.down('orderCustomDefineCombo').setValue("R11");
				}
				if(returnSn == ''){
					baseInfoModule.getForm().findField("returnSettlementType").setValue(1);//退款类型
					if(returnType == '1'){
						baseInfoModule.getForm().findField("processType").setValue(2);//处理方式
					}else{
						baseInfoModule.getForm().findField("processType").setValue(0);//处理方式
					}
					if(returnType == '2' || exchangeOrderSn == ''){
						baseInfoModule.getForm().findField("haveRefund").setValue(0);//是否退款
					}else{
						baseInfoModule.getForm().findField("haveRefund").setValue(1);//是否退款
					}
				}
				//设置商品列表全选,商品列表会监听选择同时更新退单付款信息和退单账目信息的金额
				goodsListModule.getSelectionModel().selectAll();
				if (orderShipType == 0) {
					goodsListModule.getSelectionModel().setLocked ( true );
				}
				me.paySet(returnGoods);
				me.initPage(returnData);
				me.disablePage();
				//换单支付方式默认为订单的第一种支付方式
				var returnFirstPayId = payInfoModule.getForm().findField('setPayment1').getValue();
				if(returnFirstPayId){
					Ext.getCmp('orderExchangeChanOrdModule').getForm().findField('payMent').setValue(returnFirstPayId);
				}
			},
			failure : function(opForm, action) {
				console.dir(opForm);
				// 数据加载失败后操作
				/*Ext.msgBox.remainMsg("页面加载", '加载退单信息失败！', Ext.MessageBox.ERROR);*/
			}
		});
	},
	fillPayment:function(field){//支付方式与退单付款信息页面支付方式联动
		var orderExchangeRetOrdModule = Ext.getCmp('orderExchangeRetOrdModule');
		var payInfoModule = orderExchangeRetOrdModule.down('#payInfoModule');
		var payDetailModule = orderExchangeRetOrdModule.down('#payDetailModule');
		var i=parseInt(field.name.split('setPayment')[1]);
		var rawData=orderExchangeRetOrdModule.getForm().reader.rawData;
		var thisValue=parseFloat(field.getValue());
		if(!returnSn){//申请退单
			var orderPays=rawData.orderPays;
			var len=orderPays.length;
			for(var j=1;j<=len;j++){
				if(j!=i){
					if(payInfoModule.down('#setPayment'+j)){
						var otherValue=parseFloat(payInfoModule.down('#setPayment'+j).getValue());
						if(thisValue==otherValue){//支付方式相同
							Ext.Msg.alert('结果', "该支付方式已存在！", function(xx) {
								field.setValue("请选择支付方式");
							});
							return false;
						}
					}
				}
			}
		}
		if(payDetailModule.down('#paySetFieldContainer').down('#paySet'+i)){
			if(field.getRawValue()!="请选择支付方式"){
				if(i==1){
					Ext.DomQuery.selectNode('label[id=paySet'+i+'-labelEl]').innerHTML="="+field.getRawValue()+":";
				}else{
					Ext.DomQuery.selectNode('label[id=paySet'+i+'-labelEl]').innerHTML="+"+field.getRawValue()+":";
				}
			}
		}
	},
	fillPaymentNum:function(field){
		/**
		 * 1.与兄弟节点（其他支付方式）的钱联动：所有支付方式加起来的钱为总金额
		 * 2.本支付方式变化的钱会在相邻的下一个支付方式变动，加入为最后一个支付方式，则其变化的数据就在第一个上变化
		 * 3.支付方式数据与退单付款信息页面支付方式数据联动
		 */
		var payInfoModule = Ext.getCmp('orderExchangeRetOrdModule').down('#payInfoModule');//退单付款信息
		var payDetailModule = Ext.getCmp('orderExchangeRetOrdModule').down('#payDetailModule');//退单账目
		
		//原始退款总金额
		var returnTotalFee=payInfoModule.getForm().findField("returnTotalFee").getValue();
		var changeTotalFee=0;
		var paySetNum=(payInfoModule.down('fieldcontainer').items.length-1)/3;//获取有多少个支付方式
		for(var i=1;i<=paySetNum;i++){
			changeTotalFee+=payInfoModule.getForm().findField('setPaymentNum'+i).getValue();
		}
		var k=parseInt(field.name.split('setPaymentNum')[1]);
		if(field.name==('setPaymentNum'+k)){
				if(payDetailModule.down('#paySetFieldContainer').down('#paySet'+k)){
					payDetailModule.down('#paySetFieldContainer').down('#paySet'+k).setValue(parseFloat(field.getValue()).toFixed(2));
				}
			}
		var changeNum=0;
		changeNum=returnTotalFee-changeTotalFee;
		if(returnTotalFee==changeTotalFee){
			changePaymentNumFlag=true;
			return false;
		}
		if(changePaymentNumFlag){
			if(k==paySetNum&&k!=1){//说明为最后一个支付方式
				changePaymentNumFlag=false;
				var lastNum=payInfoModule.getForm().findField("setPaymentNum1").getValue();
				payInfoModule.getForm().findField("setPaymentNum1").setValue(lastNum+changeNum);
			}else{
				if(payInfoModule.getForm().findField("setPaymentNum"+(k+1))){
					var lastNum=payInfoModule.getForm().findField("setPaymentNum"+(k+1)).getValue();
					if((lastNum+changeNum)<=0){//当下一个支付方式的值小与需要变化的值时
						payInfoModule.getForm().findField("setPaymentNum"+(k+1)).setValue(0);
					}else{
						changePaymentNumFlag=false;
						payInfoModule.getForm().findField("setPaymentNum"+(k+1)).setValue(lastNum+changeNum);
					}
					return false;
				}
			}
		}
	},
	paySet:function(returnGoods){//给各种金额赋值
		var len=returnGoods.length;//商品数
		var returnGoodsMoney=0,//退商品金额
			totalPriceDifference=0,//退商品差价
			returnBonusMoney = 0,//红包金额
			totalIntegralMoney = 0,//使用积分金额
			shareSettle=0,
			bvValue = 0;//财务分摊金额
		var payDetailModule = Ext.getCmp('orderExchangeRetOrdModule').down('#payDetailModule');
		//计算各种金额
		for(var i=0;i<len;i++){
			returnGoodsMoney+=returnGoods[i].goodsPrice*(returnGoods[i].canReturnCount);
			totalPriceDifference+=returnGoods[i].priceDifferNum*returnGoods[i].priceDifference
			shareSettle+=returnGoods[i].shareSettle*returnGoods[i].canReturnCount;
			totalIntegralMoney += returnGoods[i].integralMoney*(returnGoods[i].canReturnCount);
			bvValue += returnGoods[i].bvValue*(returnGoods[i].canReturnCount);
			if(returnGoods[i].shareBonus)
			returnBonusMoney+=returnGoods[i].shareBonus*(returnGoods[i].goodsBuyNumber-returnGoods[i].shopReturnCount-returnGoods[i].havedReturnCount);
		}
		console.dir(bvValue);
		Ext.getCmp('orderExchangeChanOrdModule').down('#returnBV').setValue(bvValue);
		if(returnType==1||returnType==2){//退货单	退商品金额
			payDetailModule.getForm().findField('returnGoodsMoney').setValue(returnGoodsMoney.toFixed(2));
		}else if(returnType==3){//普通退款单	退商品金额	
			payDetailModule.getForm().findField('returnGoodsMoney').setValue(shareSettle.toFixed(2));
		}else if(returnType==4){//退款单	退商品差价
			payDetailModule.getForm().findField('totalPriceDifference').setValue(totalPriceDifference.toFixed(2));
		}
		payDetailModule.getForm().findField('totalIntegralMoney').setValue(totalIntegralMoney.toFixed(2));
		if(returnSn == '' && returnType == '1'){
			if(returnBonusMoney!=0){
				var storeData=[];
				payDetailModule.getForm().findField('returnBonusMoney').getStore().each(function(record,index){
					storeData.push([record.get('id'),record.get('name')]);
				});
				var oldValue=payDetailModule.getForm().findField('returnBonusMoney').getValue();
				if(oldValue!=returnBonusMoney){
					storeData.push(['3',returnBonusMoney.toFixed(2)]);
					payDetailModule.getForm().findField('returnBonusMoney').getStore().removeAll();
					payDetailModule.getForm().findField('returnBonusMoney').getStore().loadData(storeData);
					payDetailModule.getForm().findField('returnBonusMoney').setValue(3);
				}
			}else{
				payDetailModule.getForm().findField('returnBonusMoney').setValue(0);
			}
		}
		this.payTotalSet();
	},
	payTotalSet:function(){
		var payDetailModule = Ext.getCmp('orderExchangeRetOrdModule').down('#payDetailModule');//退单账目信息
		var payInfoModule = Ext.getCmp('orderExchangeRetOrdModule').down('#payInfoModule');//退单付款信息
		//退单账目模块
		var returnTotalFee=this.formatNum("returnTotalFee");//退款总金额
		var returnGoodsMoney=this.formatNum("returnGoodsMoney");//退商品金额
		var totalPriceDifference=this.formatNum("totalPriceDifference");//退商品差价
		var totalIntegralMoney = this.formatNum("totalIntegralMoney");//使用积分金额
		var returnShipping=payDetailModule.getForm().findField("returnShipping").getRawValue();//退配送费用
		if(returnShipping){
			returnShipping=parseFloat(returnShipping);
		}else {
			returnShipping=0;
		}
		var returnOtherMoney=this.formatNum("returnOtherMoney");//退其他费用
		var returnBonusMoney=payDetailModule.getForm().findField("returnBonusMoney").getRawValue();//红包金额
		if(returnBonusMoney){
			returnBonusMoney=parseFloat(returnBonusMoney);
		}else{
			returnBonusMoney=0;
		}
		//退款总金额
		returnTotalFee=(returnGoodsMoney+totalPriceDifference+returnShipping+returnOtherMoney-returnBonusMoney-totalIntegralMoney).toFixed(2);
		payDetailModule.getForm().findField("returnTotalFee").setValue(returnTotalFee);
		//退单付款信息模块
		if(payInfoModule){
			payInfoModule.getForm().findField("returnTotalFee").setValue(returnTotalFee);//退款总金额
			var length = payInfoModule.query('commonsystemPayment').length;//支付方式的数量
			var totalAmt = 0;//总支付金额
			var payMoneyArr=[];//总支付金额
			if(length > 0){
				for(var i=0;i<length;i++){
					totalAmt += parseFloat(payInfoModule.getForm().findField("setPaymentNum"+(i+1)).value);
					var moneyNum=payInfoModule.getForm().findField("setPaymentNum"+(i+1)).getValue();
					payMoneyArr.push(moneyNum);
				}
			}
			var diffMoney = returnTotalFee - totalAmt;
			var payMoneyTotal=0;
			var flagNum=0;
			for(var m=0;m<length;m++){
				payMoneyTotal+=payMoneyArr[m];
				if((payMoneyTotal+diffMoney)>=0){
					flagNum=m;
					break;
				}
			}
			for(var k=0;k<=flagNum;k++){
				if(k<flagNum){
					payInfoModule.getForm().findField("setPaymentNum"+(k+1)).setValue(0);
				}else{
					payInfoModule.getForm().findField("setPaymentNum"+(k+1)).setValue(payMoneyTotal+diffMoney);
				}
			}
		}
	},
	formatNum:function(name){
		var payDetailModule = Ext.getCmp('orderExchangeRetOrdModule').down('#payDetailModule');
		var num=payDetailModule.getForm().findField(name).getValue();
		if(num&&parseFloat(num)){
			return parseFloat(num);
		}else{
			return 0;
		}
	},
	initPage: function(data){
		var orderExchangeRetOrdModule = Ext.getCmp('orderExchangeRetOrdModule');
		var payInfoModule = orderExchangeRetOrdModule.down('#payInfoModule');//退单付款信息
		var payDetailModule = orderExchangeRetOrdModule.down('#payDetailModule');//退单账目信息
		
		var returnRefunds=data.returnRefunds;
		var returnAccount=data.returnAccount;
		var returnGoods=data.returnGoods;
		var orderPays=data.orderPays;
		var returnCommon=data.returnCommon;
		if(returnAccount.returnTotalFee){
			returnAccount.returnTotalFee=parseFloat(returnAccount.returnTotalFee).toFixed(2);
			//给本页面的退款总金额赋值
			payInfoModule.getForm().findField("returnTotalFee").setValue(returnAccount.returnTotalFee);
			//给退单付款信息的退款总金额赋值
			payDetailModule.getForm().findField("returnTotalFee").setValue(returnAccount.returnTotalFee);
		}
		if(returnCommon.returnSn){//判断是申请退单还是退单详情
			if(returnRefunds){
				var len=returnRefunds.length;
				if(len<=1){
					payInfoModule.getForm().findField('setPaymentNum1').setReadOnly(true);
				}
				for(var i=0;i<len;i++){
					if(i>0){
						if(!payInfoModule.getForm().findField('setPayment'+(i+1))){
							payInfoModule.down('fieldcontainer').add(
							{xtype : "displayfield", width:20,value:'&nbsp;&nbsp;+'},
							{xtype :'commonsystemPayment',itemId:'setPayment'+(i+1)+'',name:'setPayment'+(i+1)+'',fieldLabel:false,width: 150,
								listeners:{
									'change':function(field){
										orderExchangeRetOrdModule.fillPayment(field);
									}
								}
							},
							{xtype : "numberfield", name : 'setPaymentNum'+(i+1)+'', fieldLabel :false,width:80,value:"0",minValue:0,
								listeners:{
									'change':function(field){
										orderExchangeRetOrdModule.fillPaymentNum(field);
									}
								}
							}
							);
							this.doLayout();
						}
					}
					payInfoModule.down('#setPayment'+(i+1)+'').setValue(returnRefunds[i].returnPay);
					payInfoModule.getForm().findField("setPaymentNum"+(i+1)).setValue(returnRefunds[i].returnFee);
				}
				//与退单付款信息页面数据联动
				for (var i = 0; i < len; i++) {
					if(i==0){
						if(!payDetailModule.down('#paySetFieldContainer').down('#paySet1')){
							payDetailModule.down('#paySetFieldContainer').add(
								{
									xtype : "displayfield", 
									itemId:'paySet'+(i+1)+'', 
									fieldLabel :"="+payInfoModule.down('#setPayment'+(i+1)+'').getRawValue(),
									value:returnRefunds[i].returnFee,
									columnWidth:.15,
									labelWidth:180
								}
							);
						}
					}else if (i > 0) {
						if(!payDetailModule.down('#paySetFieldContainer').down('#paySet'+(i+1))){
								payDetailModule.down('#paySetFieldContainer').add(
								{
									xtype : "displayfield", 
									itemId:'paySet'+(i+1)+'', 
									fieldLabel :"+"+ payInfoModule.down('#setPayment'+(i+1)+'').getRawValue(),
									value:returnRefunds[i].returnFee,
									columnWidth:.15,
									labelWidth:180
								}
							);
							payDetailModule.doLayout();
						}
					}
				}
				if(returnAccount.returnTotalFee){
					payInfoModule.getForm().findField("returnTotalFee").setValue(returnAccount.returnTotalFee);
				}
			}
		}else{//申请退单
			if(orderPays){
				var returnTotalFee=0;
				var surplusPay={};
				var surplusFlag=false;
				//红包
				var returnBonusMoney = 0;
				var len=orderPays.length;
				if(len<=1){
					payInfoModule.getForm().findField('setPaymentNum1').setReadOnly(true);
				}
				if(returnCommon.returnType==1||returnCommon.returnType==2){//申请退货单
					for(var j=0;j<returnGoods.length;j++){//商品付款总金额
						returnTotalFee+=returnGoods[j].goodsPrice*returnGoods[j].canReturnCount;
						if(returnGoods[j].shareBonus)
						returnBonusMoney+=returnGoods[j].shareBonus*(returnGoods[j].goodsBuyNumber-returnGoods[j].shopReturnCount-returnGoods[j].havedReturnCount);
					}
				}else if(returnCommon.returnType==3||returnCommon.returnType==4){//申请退款单
					for(var j=0;j<returnGoods.length;j++){//商品付款总金额
						returnTotalFee+=returnGoods[j].priceDifferNum*returnGoods[j].priceDifference;
					}
				}
				returnTotalFee=returnTotalFee-returnBonusMoney;
				//给本页面的退款总金额赋值
				payInfoModule.getForm().findField("returnTotalFee").setValue(returnTotalFee);
				//(应财务要求，默认配送费用去掉，所以这里需要减去配送费用)
				var returnShipping = 0;
				if(returnAccount.returnShipping){
					returnShipping = returnAccount.returnShipping;
				}
				for(var i=0;i<len;i++){
					if(i>0){
						if(!payDetailModule.down('#paySetFieldContainer').down('#paySet'+(i+1))){
							payInfoModule.down('fieldcontainer').add(
							{xtype : "displayfield", width:20,value:'&nbsp;&nbsp;+'},
							{xtype :'commonsystemPayment',itemId:'setPayment'+(i+1)+'',name:'setPayment'+(i+1)+'',fieldLabel:false,width: 150,
								listeners:{
									'change':function(field){
										orderExchangeRetOrdModule.fillPayment(field);
									}
								}
							},
							{xtype : "numberfield",name : 'setPaymentNum'+(i+1)+'',fieldLabel :false,width:80,value:"0",minValue:0,
								listeners:{
									'change':function(field){
										orderExchangeRetOrdModule.fillPaymentNum(field);
									}
								}
							}
						);
						this.doLayout();
					}}
					payInfoModule.down('#setPayment'+(i+1)+'').setValue(orderPays[i].pId);
					var setPayValue = parseFloat(orderPays[i].payFee)-parseFloat(returnShipping);
					if(setPayValue>0){
						payInfoModule.getForm().findField("setPaymentNum"+(i+1)).setValue(setPayValue);
						returnShipping = 0;
					}else {
						payInfoModule.getForm().findField("setPaymentNum"+(i+1)).setValue(0);
						returnShipping = returnShipping - parseFloat(orderPays[i].payFee);
					}
				}
				if(returnTotalFee&&returnTotalFee!=0){
					returnTotalFee=returnTotalFee.toFixed(2);
				}
				//关联退单付款信息
				this.initPayment(orderPays,returnTotalFee,returnBonusMoney,returnAccount);
			}
		}
	},
	//非申请页面禁用组件
	disablePage : function() {
		if(returnSn == ''){
			return;
		}
		//退单基本信息
		var orderExchangeRetOrdModule = Ext.getCmp('orderExchangeRetOrdModule');
		var baseInfoModule = orderExchangeRetOrdModule.down('#baseInfoModule').getForm();
		baseInfoModule.findField("processType").readOnly = true;
		baseInfoModule.findField("returnInvoiceNo").setReadOnly(true);
		baseInfoModule.findField("depotCode").readOnly = true;
		baseInfoModule.findField("returnReason").readOnly = true;
		baseInfoModule.findField("returnDesc").setReadOnly(true);
		baseInfoModule.findField("newOrderSn").setReadOnly(true);
		baseInfoModule.findField("returnSettlementType").readOnly = true;
		baseInfoModule.findField("returnExpress").readOnly = true;
		//退单商品信息
		var goodGrid=orderExchangeRetOrdModule.down('#goodsListModule');
		var columns=goodGrid.columnManager.columns;//editable
		goodGrid.showHeaderCheckbox=false;
		for(var i=0;i<columns.length;i++){
			columns[i].processEvent = function(type) { // 加入这一句，可以防止点中修改  
				if (type == 'click')
					return false;
			};
		}
		//付款信息
		var payInfoModule = orderExchangeRetOrdModule.down('#payInfoModule');//退单付款信息
		var returnRefunds = orderExchangeRetOrdModule.reader.rawData.returnRefunds;
		var len=returnRefunds.length + 1;
		for(var i=1;i<len;i++){
			payInfoModule.getForm().findField("setPayment"+i).readOnly = true;
			payInfoModule.getForm().findField("setPaymentNum"+i).setReadOnly(true);
		}
		var payDetailModule = orderExchangeRetOrdModule.down('#payDetailModule');//退单账目信息
		//配送信息
		payDetailModule.getForm().findField("returnShipping").setReadOnly(true);
		//其他费用
		payDetailModule.getForm().findField("returnOtherMoney").setReadOnly(true);
		//红包金额
		payDetailModule.getForm().findField("returnBonusMoney").setReadOnly(true);
	},
	initPayment : function(orderPays,returnTotalFee,returnBonusMoney,returnAccount) {//申请退单过来联动数据
		var orderExchangeRetOrdModule = Ext.getCmp('orderExchangeRetOrdModule');
		var payInfoModule = orderExchangeRetOrdModule.down('#payInfoModule');//退单付款信息
		var payDetailModule = orderExchangeRetOrdModule.down('#payDetailModule');//退单账目信息
		//(应财务要求，默认配送费用去掉，所以这里需要减去配送费用)
		var returnShipping = 0;
		if(returnAccount.returnShipping){
			returnShipping = returnAccount.returnShipping;
		}
		var len = orderPays.length;
		for (var i = 0; i < len; i++) {
			if(!payDetailModule.down('#paySetFieldContainer').down('#paySet'+(i+1))){
				if(i==0){
					payDetailModule.down('#paySetFieldContainer').add(
						{
							xtype : "displayfield", 
							itemId:'paySet'+(i+1),
							columnWidth:.15, 
							fieldLabel :"=" + payInfoModule.down('#setPayment' + (i + 1) + '').getRawValue(),
							value:0,
							labelWidth:180
						}
					);
				}else if (i > 0) {
					payDetailModule.down('#paySetFieldContainer').add(
						{
							xtype : "displayfield", 
							itemId:'paySet'+(i+1),
							columnWidth:.15, 
							fieldLabel :"+"+ payInfoModule.down('#setPayment' + (i + 1) + '').getRawValue(),
							value:0,
							labelWidth:180
						}
					);
					this.doLayout();
				}
				var setPayValue = parseFloat(orderPays[i].payFee)-parseFloat(returnShipping);
				if(setPayValue>0){
					payDetailModule.down('#paySetFieldContainer').down('#paySet'+(i+1)).setValue(setPayValue.toFixed(2));
					returnShipping = 0;
				}else {
					payDetailModule.down('#paySetFieldContainer').down('#paySet'+(i+1)).setValue(0);
					returnShipping = returnShipping - parseFloat(orderPays[i].payFee);
				}
			}
		}
		//给本页面的退款总金额赋值
		payDetailModule.getForm().findField("returnTotalFee").setValue(returnTotalFee);
		payDetailModule.getForm().findField('returnBonusMoney').setValue('0');
		var storeData=[['0',0]];
		//红包
		if(returnAccount.returnBonusMoney&&parseFloat(returnAccount.returnBonusMoney)!=0){//原订单的红包总额
			storeData.push(['1',returnAccount.returnBonusMoney]);
		if(returnBonusMoney!=0&&returnBonusMoney!=returnAccount.returnBonusMoney){
			storeData.push(['2',returnBonusMoney]);
			payDetailModule.getForm().findField('returnBonusMoney').getStore().removeAll();
			payDetailModule.getForm().findField('returnBonusMoney').getStore().loadData(storeData);
			payDetailModule.getForm().findField('returnBonusMoney').setValue(2);
		}else{
			payDetailModule.getForm().findField('returnBonusMoney').getStore().removeAll();
			payDetailModule.getForm().findField('returnBonusMoney').getStore().loadData(storeData);
			if(returnSn == '' && returnType == '4'){
				payDetailModule.getForm().findField('returnBonusMoney').setValue(0);
			}else{
				payDetailModule.getForm().findField('returnBonusMoney').setValue(1);				
			}
		}
	  }
	  this.payTotalSet();
	},
	onCellBeforeEdit: function (editor, ctx, eOpts) { // 商品属性初始化
		var clickColIdx = ctx.colIdx; // grid column 展示列下标
		var record = ctx.record;
		if (clickColIdx == 15) { // 可退货量
			var  goodsNumber = record.get("goodsBuyNumber"),
				 shopReturnCount = record.get("shopReturnCount"),
				 havedReturnCount = record.get("havedReturnCount");
			var	canReturnCount=goodsNumber-shopReturnCount-havedReturnCount;
			var numberf = ctx.grid.columns[16].getEditor(ctx.record);
			numberf.setMaxValue(canReturnCount, false);
		}else if(clickColIdx == 12){//退差价数量
			var  goodsNumber = record.get("goodsBuyNumber");
			var numberf = ctx.grid.columns[17].getEditor(ctx.record);
			numberf.setMaxValue(goodsNumber, false);
		}
	},
	onCellAfterEdit: function(editor, ctx, eOpts) { // 修改商品信息后动态变化
		var clickColIdx = ctx.colIdx; // grid column 展示列下标
		var record = ctx.record;
		var items=ctx.grid.getSelectionModel().selected.items;
		var goodData=[];
		for(var i=0;i<items.length;i++){
			goodData.push(items[i].data);
		}
		Ext.getCmp('orderExchangeRetOrdModule').paySet(goodData);
	},
	builtReturn:function(params){
		//组件
		var orderExchangeRetOrdModule = Ext.getCmp('orderExchangeRetOrdModule');
		var payDetailModule = orderExchangeRetOrdModule.down('#payDetailModule');
		var goodsListModule = orderExchangeRetOrdModule.down('#goodsListModule');
		
		var param=orderExchangeRetOrdModule.getValues();
		var returnRefunds=orderExchangeRetOrdModule.getForm().reader.rawData.returnRefunds;
		var returnCommon=orderExchangeRetOrdModule.getForm().reader.rawData.returnCommon;
		var orderPays=orderExchangeRetOrdModule.getForm().reader.rawData.orderPays;
		params['orderReturnBean.returnType']=returnCommon.returnType;
		params['orderReturnBean.relatingOrderSn']=returnCommon.relatingOrderSn;
		var returnTotalFee=payDetailModule.getForm().findField("returnTotalFee").getValue();
		
		if(returnCommon.returnSn){
				if(returnRefunds != null){
				var len=returnRefunds.length;
				for(var i=0;i<len;i++){
					params['orderReturnBean.createOrderRefundList['+i+'].returnPaySn']=returnRefunds[i].returnPaySn;
					params['orderReturnBean.createOrderRefundList['+i+'].relatingReturnSn']=returnRefunds[i].relatingReturnSn;
					params['orderReturnBean.createOrderRefundList['+i+'].returnPay']=param["setPayment"+(i+1)];
					params['orderReturnBean.createOrderRefundList['+i+'].returnFee']=param["setPaymentNum"+(i+1)];
				}
			}
		}else if(orderPays!=null){
			var len=orderPays.length;
				for(var i=0;i<len;i++){
					params['orderReturnBean.createOrderRefundList['+i+'].returnPay']=param["setPayment"+(i+1)];
					params['orderReturnBean.createOrderRefundList['+i+'].returnFee']=param["setPaymentNum"+(i+1)];
				}
		}
		params['orderReturnBean.createOrderReturnShip.relatingReturnSn']=returnCommon.returnSn;
		params['orderReturnBean.createOrderReturnShip.returnExpress']=param.returnExpress;
		params['orderReturnBean.createOrderReturnShip.returnInvoiceNo']=param.returnInvoiceNo;
		params['orderReturnBean.createOrderReturnShip.depotCode']=param.depotCode;
		
		params['orderReturnBean.createOrderReturn.relatingOrderSn']=returnCommon.relatingOrderSn;
		params['orderReturnBean.createOrderReturn.newOrderSn']=param.newOrderSn;
		params['orderReturnBean.createOrderReturn.returnSettlementType']=param.returnSettlementType;
		params['orderReturnBean.createOrderReturn.returnSn']=returnCommon.returnSn;
		params['orderReturnBean.createOrderReturn.processType']=param.processType;
		params['orderReturnBean.createOrderReturn.returnTotalFee']=returnTotalFee;
		params['orderReturnBean.createOrderReturn.returnGoodsMoney']=payDetailModule.getForm().findField("returnGoodsMoney").getValue();
		params['orderReturnBean.createOrderReturn.totalPriceDifference']=payDetailModule.getForm().findField("totalPriceDifference").getValue();
		params['orderReturnBean.createOrderReturn.returnType']=returnCommon.returnType;
		params['orderReturnBean.createOrderReturn.returnShipping']=payDetailModule.getForm().findField("returnShipping").getRawValue();;
		params['orderReturnBean.createOrderReturn.returnOtherMoney']=param.returnOtherMoney;
		params['orderReturnBean.createOrderReturn.totalIntegralMoney']=param.totalIntegralMoney;//积分使用金额
		params['orderReturnBean.createOrderReturn.returnBonusMoney']=payDetailModule.getForm().findField("returnBonusMoney").getRawValue();;
		params['orderReturnBean.createOrderReturn.returnReason']=param.returnReason;
		if(param.returnReason == ""){
			Ext.Msg.alert('警告', "请选择退单原因！", function(xx) {
			});
			return false;
		}
		params['orderReturnBean.createOrderReturn.haveRefund']=param.haveRefund;
		params['orderReturnBean.createOrderReturn.returnDesc']=param.returnDesc;
		
		//商品数据
		var goodDataChecked = goodsListModule.getSelectionModel().selected.items;
		var checkedLen=goodDataChecked.length;
		if(checkedLen<=0){
			Ext.Msg.alert('警告', "请选择退货商品！", function(xx) {
			});
			return false;
		}
		var index = 0;
		for(var i=0;i<checkedLen;i++){
			var canReturnNumber = goodDataChecked[i].data.canReturnCount;
			if(returnType == '1' && canReturnNumber <=0){
				continue;
			}
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].relatingReturnSn']=returnCommon.returnSn;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].customCode']=goodDataChecked[i].data.customCode;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].extensionCode']=goodDataChecked[i].data.extensionCode;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].extensionId']=goodDataChecked[i].data.extensionId;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].osDepotCode']=goodDataChecked[i].data.osDepotCode;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].goodsBuyNumber']=goodDataChecked[i].data.goodsBuyNumber;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].chargeBackCount']=goodDataChecked[i].data.shopReturnCount;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].goodsReturnNumber']=goodDataChecked[i].data.canReturnCount;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].haveReturnCount']=goodDataChecked[i].data.havedReturnCount;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].returnReason']=goodDataChecked[i].data.returnReason;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].goodsThumb']=goodDataChecked[i].data.goodsThumb;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].payPoints']=goodDataChecked[i].data.settlementPrice;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].seller']=goodDataChecked[i].data.seller;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].integralMoney']=goodDataChecked[i].data.integralMoney;//积分使用金额
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].salesMode']=goodDataChecked[i].data.salesMode;//商品销售模式：1为自营，2为买断，3为寄售，4为直发
			//价格
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].goodsPrice']=goodDataChecked[i].data.goodsPrice;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].marketPrice']=goodDataChecked[i].data.marketPrice;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].settlementPrice']=goodDataChecked[i].data.settlementPrice;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].shareBonus']=goodDataChecked[i].data.shareBonus;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].integralMoney']=goodDataChecked[i].data.integralMoney;//积分使用金额
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].sap']=goodDataChecked[i].data.sap;//sap
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].bvValue']=goodDataChecked[i].data.bvValue;//bvValue
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].baseBvValue']=goodDataChecked[i].data.baseBvValue;//baseBvValue

			params['orderReturnBean.createOrderReturnGoodsList['+index+'].goodsSn']=goodDataChecked[i].data.goodsSn;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].goodsName']=goodDataChecked[i].data.goodsName;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].colorName']=goodDataChecked[i].data.goodsColorName;
			params['orderReturnBean.createOrderReturnGoodsList['+index+'].sizeName']=goodDataChecked[i].data.goodsSizeName;
			index ++;
			
			//校验可退货量
			var checkNum1=goodDataChecked[i].data.goodsBuyNumber-goodDataChecked[i].data.shopReturnCount-goodDataChecked[i].data.havedReturnCount - goodDataChecked[i].data.canReturnCount;
			if(checkNum1 < 0){
				Ext.Msg.alert('警告', "退货商品数量有误！", function(xx) {
				});
				return false;
			}
		}
		if(index == 0){
			Ext.Msg.alert('警告', "退货商品列表中有效商品为空！", function(xx) {
			});
			return false;
		}
		params.orderReturnSn=returnCommon.returnSn;//退单Sn
		return params;
	}
	
});