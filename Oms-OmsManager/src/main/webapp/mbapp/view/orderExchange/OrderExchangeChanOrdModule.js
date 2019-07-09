Ext.define("MB.view.orderExchange.OrderExchangeChanOrdModule", {
	extend : "Ext.form.Panel",
	alias : 'widget.orderExchangeChanOrdModule',
	id : 'orderExchangeChanOrdModule',
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
		useFlag = false;
		if(exchangeOrderSn){//如果换单详情   不允许修改商品
			useFlag = true;
		}
		me.items = [{
			xtype : 'form',
			title : '换单基本信息',
			itemId : 'exchangeBaseInfo',
			frame : true,
			items : [ {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "displayfield", name : 'masterOrderSn', fieldLabel : "换货单号", columnWidth: .33,renderer: function(value) {
						if (value) {
							var url = order_info_url+"?masterOrderSn=" + value +"&isHistory=" + isHistory;
							return "<a href=" +url + " target='_blank' >" + value + "</a>";
						}
					}},
					{xtype : "displayfield", name : 'ordertotalstatusStr', fieldLabel : "订单状态", columnWidth: .33,
						renderer : function (value) { 
							if(!value){
								return "<span style='color:red;'>申请中</span>";
							}else{
				        		return "<span style='color:red;'>"+value+"</span>";
							}
			        	}
					},
					{xtype : "displayfield", name : 'addTime', fieldLabel : "下单时间", columnWidth: .33}
				]
			} , {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "displayfield", name : 'userName', fieldLabel : "收货人", columnWidth: .33},
					{xtype : "displayfield", name : 'clearTime', fieldLabel : "结算时间", columnWidth: .33 } ,
					{xtype : "displayfield", name : 'userPayTime', fieldLabel : "付款时间", columnWidth: .33 } 
				]
			} ,
			{
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "commonsystemPayment", name : 'payMent', fieldLabel : "支付方式", columnWidth: .33,labelWidth:100 } ,
					{xtype : "displayfield", name : 'relatingRemoneySn', fieldLabel : "关联退款单号", columnWidth: .33 },
					{xtype : "displayfield", name : 'referer', fieldLabel : "订单来源", columnWidth: .33 } 
				]
			} ,
			{
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "displayfield", name : 'relatingOriginalSn', fieldLabel : "关联原订单号", columnWidth: .33,renderer: function(value) {
						if (value) {
							var array = value.split('=');
							if(array[1]){//值拼接了主单号
								var url = order_info_url +"?masterOrderSn="+array[0]+"="+ array[1] +"&isHistory=" + isHistory;
								return "<a href=" +url + " target='_blank' >" + array[1] + "</a>";
							}else{//值只包含交货单号
								var url = order_info_url +"?masterOrderSn="+masterOrderSn+"&orderSn="+ value +"&isHistory=" + isHistory;
								return "<a href=" +url + " target='_blank' >" + value + "</a>";
							}
						}
					}},
					{xtype : "displayfield", name : 'relatingReturnSn', fieldLabel : "关联退单号", columnWidth: .33,renderer: function(value) {
						if (value) {
							var url = order_return_url + value +"&isHistory=" + isHistory;
							return "<a href=" +url + " target='_blank' >" + value + "</a>";
						}
					}} ,
					{xtype : "displayfield", name : 'orderType', fieldLabel : "订单类型", columnWidth: .33,
						renderer:function(value){
							return "换货单";
						}
					
					}
				]
			} , {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "displayfield", name : 'userRealMoney', fieldLabel : "预付卡余额", columnWidth: .33,
						renderer:function(value){
							return "<span style='color:red;'>" + value + "</span>"; 
						}
					}
				]
			}]
		},{
			xtype : 'grid',
			title : '商品信息',
			itemId : 'exchangeGoodsList',
			frame : true,
			columnLines : true,
			resizable : true,
			viewConfig:{
				enableTextSelection : true
			},
			columns : [{
				text : '商品名称',
				// width : 75,
				dataIndex : 'goodsName',
				sortable : false,
				menuDisabled : true,
			},{
				text : '商品属性',
				width : 75,
				align : 'center',
				dataIndex : 'extensionCode',
				sortable : false,
				menuDisabled : true,
				renderer : function (value, meta, record) {
								if (value == null || value== '' || value == 'common') {
									return '普通商品';
								} else if (value == 'gift') {
									return '赠品';
								} else if (value == 'group') {
									return '套装';
								} else if (value == 'prize') {
									return '奖品';
								}
				}
			},{
				header : 'depotCode',
				hidden : true,
				sortable : false,
				dataIndex : 'depotCode'
			},{
				text : '货号',
				width : 75,
				align : 'center',
				dataIndex : 'goodsSn',
				sortable : false,
				menuDisabled : true
			},{
				header : '规格',
				columns : [{
					header : "颜色",
					width : 80,
					dataIndex : 'goodsColorName',
					sortable : false,
					locked:true,
					menuDisabled : true
				}, {
					header : "尺寸",
					width : 80,
					dataIndex : 'goodsSizeName',
					sortable : false,
					locked:true,
					menuDisabled : true
				}]
			},{
				text : '产品条形码',
				dataIndex : 'barcode',
				sortable : false,
				menuDisabled : true,
				hidden : true
			},{
				text : '企业SKU码',
				dataIndex : 'customCode',
				sortable : false,
				menuDisabled : true
			},{
				text : 'sap',
				dataIndex : 'sap',
				sortable : false,
				menuDisabled : true
			},{
				text : '商品价格',
				width : 75,
				align : 'center',
				dataIndex : 'goodsPrice',
				sortable : false,
				menuDisabled : true
			},{
				text : '数量',
				width : 85,
				align : 'center',
				dataIndex : 'goodsNumber',
				sortable : false,
				menuDisabled : true,
				renderer : function(value, meta, record) {
					var returnNum = record.get('returnNum');
					var returnRemainNum = record.get('returnRemainNum');
					var lackNum = record.get('lackNum');
					var returnmsg = value;
					if (lackNum && lackNum > 0) {
						returnmsg += '</br><font style="color:red">（缺货 ' + lackNum + '）</font>';
					}
					if (returnRemainNum && returnRemainNum > 0) {
						returnmsg += '</br><font style="color:red">（待退 ' + returnRemainNum + '）</font>';
					}
					if (returnNum && returnNum > 0) {
						returnmsg += '</br><font style="color:red">（已退 ' + returnNum + '）</font>';
					}
					return returnmsg;
				}
			},{
				text : '成交价格',
				width : 75,
				align : 'center',
				dataIndex : 'transactionPrice',
				sortable : false,
				menuDisabled : true
			},{
				text : '财务价格',
				width : 75,
				align : 'center',
				dataIndex : 'settlementPrice',
				sortable : false,
				menuDisabled : true
			},{
				text : '分摊金额',
				width : 75,
				align : 'center',
				dataIndex : 'shareBonus',
				sortable : false,
				menuDisabled : true
			},{
				text : '积分金额',
				width : 75,
				align : 'center',
				dataIndex : 'integralMoney',
				sortable : false,
				menuDisabled : true
			},{
				text : 'bvValue',
				width : 75,
				align : 'center',
				dataIndex : 'bvValue',
				sortable : false,
				menuDisabled : true
			},{
				text : 'baseBvValue',
				width : 75,
				align : 'center',
				dataIndex : 'baseBvValue',
				sortable : false,
				hidden: true
			},{
				text : '商品促销',
				align : 'center',
				dataIndex : 'promotionDesc',
				sortable : false,
				menuDisabled : true
			},
			{ text : '打折券', width : 200,dataIndex : 'useCard', hidden : true,
				renderer : function (value, meta, record) {
					//获取打折券详情列表
					var couponList = record.get('couponList');
					//拼装返回链接
					var returnValue = '';
					if (value&&value!=null&&value!='') {
						var array = value.split(":");
						for (var i=0 ; i< array.length ; i++){
							var flag = '0';//用于标记本轮循环中  打折券卡号是否匹配到对应的详细信息  如果没有匹配到  就显示无链接的卡号[折扣未知]
							if(couponList&&couponList!=null){
								for(var j=0 ; j<couponList.length ; j++){
									if(couponList[j].cardNo==array[i]){
										returnValue = returnValue + array[i] +'<font style="color:red">['+couponList[j].cardMoney+ '折券]</font></br>';
										flag = '1';
									}
								}
							}
							if(flag=='0'){
								returnValue = returnValue + array[i] + '[折扣未知]</br>';
							}
						} 
					}
					return returnValue;
				} 
			},{
				text : '折让金额',
				width : 75,
				align : 'center',
				dataIndex : 'discount',
				sortable : false,
				menuDisabled : true
			},{
				text : '小计',
				width : 75,
				align : 'center',
				dataIndex : 'subTotal',
				sortable : false,
				menuDisabled : true
			}],
			tools : [{
				type: 'plus',
				disabled : useFlag,
				tooltip : '添加商品',
				action: 'addGoods',
			}]
		},{
			//xtype : "displayfield",itemId : 'exchangePayTotal', name : 'total', fieldLabel : "<span style='color:black;font-weight:bold';>总计</span>",labelWidth:1200

			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield",  columnWidth: .77},
				{xtype : "displayfield", itemId : 'returnBV', name : 'returnBV', fieldLabel : "退JF",columnWidth: .1,value:0.00 } ,
				{xtype : "displayfield", itemId : 'exchangeBV', name : 'exchangeBV', fieldLabel : "换JF",columnWidth: .1,value:0.00},
				{xtype : "hidden", itemId : 'exchangePayTotal', name : 'total', fieldLabel : "总计",columnWidth: .1,readOnly:true,value:0.00}
			]
		},{
			xtype : 'form',
			title : '换单付款信息&nbsp;&nbsp;&nbsp;',
			itemId : 'exchangePayInfo',
			frame : true,
			items : [{
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "displayfield",  columnWidth: .7},
					{xtype : "displayfield",  width:200 } ,
					{xtype : "numberfield",  name : 'goodsAmount', fieldLabel : "换出商品总金额",width: 200,readOnly:true,value:0.00,
						listeners:{
							'change':function(field){
								me.fillPayment(field);
							}
						}
					} ,
					/*{xtype : "numberfield",  name : 'discount', fieldLabel : "- &nbsp;&nbsp;&nbsp;&nbsp;折让",width: 200,value:0.00,
						listeners:{
							'change':function(field){
								me.fillPayment(field);
							}
						}
					} ,*/
					{
						xtype: 'hidden',
						name : 'discount',
						value: 0.00
					},
					{xtype : "numberfield",  name : 'shippingTotalFee', fieldLabel : "+  &nbsp;配送费用",width: 200,value:0.00,
						listeners:{
							'change':function(field){
								me.fillPayment(field);
							}
						}
					}]
			},{
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "displayfield",  columnWidth: .7 },
					{xtype : "displayfield",  width:400 } ,
					{xtype : "numberfield", name : 'totalFee', fieldLabel : "=&nbsp;订单总金额",width: 200,value:0.00,readOnly:true}]
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
					{xtype : "numberfield", name : 'moneyPaid',itemId:'moneyPaid', fieldLabel : "-&nbsp;退回款金额",width: 200 ,readOnly:true,value:0.00,
						listeners:{
							'change':function(field){
								me.fillPayment(field);
							}
						}
					},
					{xtype : "numberfield", name : 'bonus', fieldLabel : "-&nbsp;&nbsp;使用红包",width: 200,value:0.00,
						listeners:{
							'change':function(field){
								me.fillPayment(field);
							}
						}
					}]
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
					{xtype : "numberfield", name : 'totalPayable', fieldLabel : "=&nbsp;应再付款金额",width: 200,value:0.00,readOnly:true}]
			}]
		}];
		this.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "result",
			model : 'MB.model.ExchangeDetailModel'
		});
		this.callParent(arguments);
	},
	initData : function(){
		var me = this;
		if(exchangeOrderSn){//换单详情页
			me.load({
				url : basePath + '/custom/exchangeorder/getExchangeOrderDetail',
				params : {
					"exchangeOrderSn" : exchangeOrderSn,
					"isHistory": isHistory
				},
				timeout:90000,
				success : function(opForm, action) {
					//组件
					var orderExchangeChanOrdModule = Ext.getCmp('orderExchangeChanOrdModule');
					var exchangeBaseInfo = orderExchangeChanOrdModule.down('#exchangeBaseInfo');
					var exchangeGoodsList = orderExchangeChanOrdModule.down('#exchangeGoodsList');
					var exchangePayTotal = orderExchangeChanOrdModule.down('#exchangePayTotal');
					var exchangeBV = orderExchangeChanOrdModule.down('#exchangeBV');

//					var exchangePayInfo = orderExchangeChanOrdModule.down('#exchangePayInfo');
					
					//数据
					var rawDatas = opForm.reader.rawData;
					var masterOrderInfoData = rawDatas.result;
					var goodDetail = rawDatas.goodDetail;
					var payDetail = rawDatas.payDetail;
					
					setTimeout(function(){
						exchangeBaseInfo.getForm().findField('ordertotalstatusStr').setValue(getCombineStatus(masterOrderInfoData.orderStatus,masterOrderInfoData.payStatus,masterOrderInfoData.shipStatus));//订单状态
					}, 1);
					if(rawDatas){
						orderExchangeChanOrdModule.refreshButtons(rawDatas);					
					}
					if(goodDetail){
						exchangeGoodsList.store.loadData(goodDetail);
						var len = goodDetail.length;
						var total=0;
						var bvValue=0;
						for(var i=0;i<len;i++){
							total+=goodDetail[i].transactionPrice;
							bvValue+=goodDetail[i].bvValue;
						}
						//总计
						exchangePayTotal.setValue(total);
						exchangeBV.setValue(bvValue);
					}
					
					//换单支付方式
					if(exchangeOrderSn){
						if(payDetail){
							if(payDetail.length>0)
							var payIdValue = payDetail[0].payId;
							exchangeBaseInfo.getForm().findField('payMent').setValue(payIdValue);
						}
					}
					me.disablePage();
				},
				failure : function(opForm, action) {
					// 数据加载失败后操作
				}
			});
		}else{//换单申请页
			//一些基本数据赋值在orderExchangeOriOrdModule的initdata方法中、orderExchangeRetOrdModule中
			//刷新按钮
			this.refreshButtons();
		}
	},
	refreshButtons : function(rawData) {//刷新换单页按钮
		var buttonStatus = undefined;
		if(rawData != null && rawData != undefined){
			buttonStatus = rawData.buttonStatus;
		}
		Ext.each(Ext.getCmp("orderExchangeNorth").down('toolbar').items.items,function(item){
			if(item.action != undefined){
				var role = true;
				if(buttonStatus == undefined){
					item.disable();
				}else{
					var status = buttonStatus[item.action];
					if(status != undefined && status == '1' && role){
						item.enable();
					}else{
						item.disable();
					}
				}
				if(exchangeOrderSn == ''){
					if(item.action == 'saveExchange' && role){
						item.enable();
					}	
				}
			}
			if(returnSn &&  item.action == 'sendMessage' && role){
				item.enable();
			}
		});
	},
    fillPayment: function (field) {
    	var orderExchangeChanOrdModule = Ext.getCmp('orderExchangeChanOrdModule');
    	var exchangePayInfo = orderExchangeChanOrdModule.down('#exchangePayInfo');
    	
    	var goodsAmount=exchangePayInfo.getForm().findField('goodsAmount').getValue();
    	var discount=exchangePayInfo.getForm().findField('discount').getValue();
    	var shippingTotalFee=exchangePayInfo.getForm().findField('shippingTotalFee').getValue();
    	var totalFee=goodsAmount-discount+shippingTotalFee;
    	var moneyPaid=exchangePayInfo.getForm().findField('moneyPaid').getValue();
    	var bonus=exchangePayInfo.getForm().findField('bonus').getValue();
    	var totalPayable=totalFee-moneyPaid-bonus;
    	exchangePayInfo.getForm().findField('totalPayable').setValue(totalPayable);
    	exchangePayInfo.getForm().findField('totalFee').setValue(totalFee);
    	
    	if(exchangeOrderSn){
    		var rawData=orderExchangeChanOrdModule.getForm().reader.rawData;
    		if(rawData){
    			exchangePayInfo.getForm().findField('totalPayable').setValue(rawData.result.totalPayable);
    		}
    	}
    },
	disablePage : function() {//非申请页面禁用组件
		if(returnSn == ''){
			return;
		}
		//组件
		var orderExchangeChanOrdModule = Ext.getCmp('orderExchangeChanOrdModule');
		var exchangeBaseInfo = orderExchangeChanOrdModule.down('#exchangeBaseInfo');
		var exchangePayInfo = orderExchangeChanOrdModule.down('#exchangePayInfo');
		
		exchangeBaseInfo.getForm().findField("payMent").readOnly = true;
//		exchangeBaseInfo.getForm().findField("isAgent").readOnly = true;
		exchangePayInfo.getForm().findField("goodsAmount").setReadOnly(true);
		exchangePayInfo.getForm().findField("discount").setReadOnly(true);
		exchangePayInfo.getForm().findField("shippingTotalFee").setReadOnly(true);
		exchangePayInfo.getForm().findField("bonus").setReadOnly(true);
	},
	builtExchange:function(){//构建换单保存的参数
		var orderExchangeChanOrdModule = Ext.getCmp('orderExchangeChanOrdModule');
		var exchangeGoodsList = orderExchangeChanOrdModule.down('#exchangeGoodsList'); 
		
		var params=orderExchangeChanOrdModule.getValues();
		params.relatingOrderSn=orderExchangeChanOrdModule.getForm().findField('relatingOriginalSn').getValue();
		params['pageOrder.orderSn']=orderExchangeChanOrdModule.getForm().findField('masterOrderSn').getValue();
		params['pageOrder.relatingOriginalSn']=orderExchangeChanOrdModule.getForm().findField('relatingOriginalSn').getValue();
		params['pageOrder.relatingReturnSn']=orderExchangeChanOrdModule.getForm().findField('relatingReturnSn').getValue();
		params['pageOrder.relatingRemoneySn']=orderExchangeChanOrdModule.getForm().findField('relatingRemoneySn').getValue();
//		params['pageOrder.isAgent']=params.isAgent;
		params['pageOrder.payId']=params.payMent;
		params['pageOrder.goodsAmount']=params.goodsAmount;
		params['pageOrder.totalFee']=params.totalFee;
		params['pageOrder.moneyPaid']=params.moneyPaid;
		params['pageOrder.totalPayable']=params.totalPayable;
		params['pageOrder.shippingTotalFee']=params.shippingTotalFee;
		params['pageOrder.bonus']=params.bonus;
		params['pageOrder.discount']=params.discount;
		
		if(exchangeGoodsList.store.data.length < 1){
			Ext.Msg.alert('警告', "换货商品列表为空！", function(xx) {});
			return false;
		}
		exchangeGoodsList.store.each(function(record,i){
			params['pageGoods['+i+'].customCode']=record.get('customCode');
			params['pageGoods['+i+'].extensionCode']=record.get('extensionCode');
			params['pageGoods['+i+'].marketPrice']=record.get('goodsPrice');
			params['pageGoods['+i+'].goodsPrice']=record.get('goodsPrice');
			params['pageGoods['+i+'].transactionPrice']=record.get('transactionPrice');
			params['pageGoods['+i+'].goodsNumber']=record.get('goodsNumber');
			params['pageGoods['+i+'].settlementPrice']=record.get('settlementPrice');
			params['pageGoods['+i+'].shareBonus']=record.get('shareBonus');
			params['pageGoods['+i+'].goodsThumb']=record.get('goodsThumb');
			
			params['pageGoods['+i+'].goodsSn']=record.get('goodsSn');
			params['pageGoods['+i+'].goodsName']=record.get('goodsName');
			params['pageGoods['+i+'].colorName']=record.get('goodsColorName');
			params['pageGoods['+i+'].sizeName']=record.get('goodsSizeName');
			params['pageGoods['+i+'].barCode']=record.get('sap');
//			params['pageGoods['+i+'].sap']=record.get('sap');
			params['pageGoods['+i+'].discount']=record.get('discount');
			params['pageGoods['+i+'].bvValue']=record.get('bvValue');
			params['pageGoods['+i+'].baseBvValue']=record.get('baseBvValue');
			params['pageGoods['+i+'].depotCode']=record.get('depotCode');
		});
		return params;
	}
	
});