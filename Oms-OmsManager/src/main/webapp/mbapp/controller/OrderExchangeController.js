Ext.define('MB.controller.OrderExchangeController', {
	extend : 'Ext.app.Controller',
	stores : ['HaveRefundStore','SystemShippingStore','SettlementTypeStore','ProcessTypeStore','ReturnErpWarehouseStore','OrderCustomDefines','SystemPaymentStore',
	          'ReturnShippingStore','ReturnBonusMoneyStore','CommonStatusStore','ExchangeGoodsStore'],
	models : ['SystemShippingModel','ReturnErpWarehouseModel','OrderCustomDefine','SystemPaymentModel','CommonStatusModel','MB.model.ReturnCommonModel',
	          'MB.model.ExchangeDetailModel'],
	views : ['MB.view.orderExchange.OrderExchangeViewPort',
	         'MB.view.orderExchange.OrderExchangeOriOrdModule',
	         'MB.view.orderExchange.OrderExchangeRetOrdModule',
	         'MB.view.orderExchange.OrderExchangeChanOrdModule',
	         'MB.view.orderEdit.C2xPropertyEdit',
	         'common.HaveRefund','common.SystemShipping','common.SettlementType','common.ProcessType','common.ReturnErpWarehouse','common.OrderCustomDefineCombo',
	         'common.SystemPayment','common.ReturnShippingCombo','common.ReturnBonusMoneyCombo','common.CommonStatus','returnOrder.MessageEdit'],
	refs : [{
		ref : 'orderExchangeViewPort',
		selector : 'orderExchangeViewPort'
	},{
		ref : 'orderExchangeOriOrdModule',
		selector : 'orderExchangeOriOrdModule'
	},{
		ref : 'orderExchangeRetOrdModule',
		selector : 'orderExchangeRetOrdModule'
	},{
		ref : 'orderExchangeChanOrdModule',
		selector : 'orderExchangeChanOrdModule'
	}],
	init : function() {
		var me = this;
		me.control({
			'orderExchangeRetOrdModule button[action=updateHaveRefund]' : {//是否退款
				click : this.updateHaveRefund
			},
			'orderExchangeRetOrdModule button[action=updateInvoiceNo]' : {//退货面单号
				click : this.updateInvoiceNo
			},
			'orderExchangeRetOrdModule button[action=updateExpress]' : {//退货承运商
				click : this.updateExpress
			},
			'orderExchangeRetOrdModule button[action=updateDepotCode]' : {//退货仓库
				click : this.updateDepotCode
			},
			'orderExchangeChanOrdModule tool[action=addGoods]' : {//添加换货商品
				click : this.addGoods
			},
			'orderExchangeViewPort button[action=saveExchange]' : {//保存换单
				click : this.returnClick
			},
			/*'orderExchangeViewPort button[action=confirmExchange]' : {//确认
				click : this.returnClick
			},
			'orderExchangeViewPort button[action=unConfirmExchange]' : {//未确认
				click : this.returnClick
			},*/
			'orderExchangeViewPort button[action=cancelExchange]' : {//作废
				click : this.returnClick
			},
			'orderExchangeViewPort button[action=onlyReturnExchange]' : {//仅退货
				click : this.returnClick
			},
			'orderExchangeViewPort button[action=actionClick]' : {//沟通
				click : this.actionClick
			}
			/*,
			'orderExchangeViewPort button[action=sendMessage]' : {//发送短信
				click : this.sendMessage
			}*/
			
		});
	},
	onLaunch : function() {
		//获取组件
		var orderExchangeCenter=Ext.getCmp('orderExchangeCenter');
		var orderExchangeOriOrdModule = orderExchangeCenter.down('#orderExchangeOriOrdModule');//原订单
		var orderExchangeRetOrdModule = orderExchangeCenter.down('#orderExchangeRetOrdModule');//退单
		var orderExchangeChanOrdModule = orderExchangeCenter.down('#orderExchangeChanOrdModule');//换单
		//切换换单商品grid绑定的store，要不然原先订单详情的store数据会自动加载
		var store_t = Ext.data.StoreMgr.lookup('ExchangeGoodsStore');
		var grid=orderExchangeChanOrdModule.down('#exchangeGoodsList');
		grid.store = store_t;  
		grid.bindStore(store_t);
		//初始化原订单页面
		orderExchangeOriOrdModule.initData();
		//初始化退单页面
		orderExchangeRetOrdModule.initData();
		//初始化换单
		orderExchangeChanOrdModule.initData();
	},
	updateHaveRefund : function(btn){
		var form = btn.up('form');
		this.updateReturnInfo('updateHaveRefund',form);
	},
	updateInvoiceNo : function(btn){
		var form = btn.up('form');
		this.updateReturnInfo('updateExpress',form);
	},
	updateExpress : function(btn){
		var form = btn.up('form');
		this.updateReturnInfo('updateInvoiceNo',form);
	},
	updateDepotCode : function(btn){
		var form = btn.up('form');
		this.updateReturnInfo('updateDepotCode',form);
	},
	updateReturnInfo: function(type,form){
		var me = this;
		var param = form.builtParam(type,form);
		param.btnType = 'editReturn';
		console.dir(param);
		Ext.Ajax.request({
			url:  basePath + '/custom/orderReturn/editReturnButtonClick',
			params: param,
			timeout:90000,
			data:"json",
			success: function(response){
				var text = response.responseText;
				var result = Ext.JSON.decode(text);
				console.dir(result);
				if (result) {
					Ext.getCmp('returnShow').initData();
					Ext.msgBox.msg('基本信息', '数据更新成功', Ext.MessageBox.INFO);
					
				}
			},
			failure: function(response){
				Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
			}
		});
	},
	addGoods : function(){ // 编辑订单商品
		//申请状态或未确认状态方可新增商品或者编辑商品
		if(exchangeOrderSn){
			var orderStatus = Ext.getCmp('orderExchangeChanOrdModule').getForm().reader.rawData.result.orderStatus;
			if(orderStatus != 0){
				return ;
			}
		}
		var paramOrderSn='';
		if(exchangeOrderSn){
			paramOrderSn=exchangeOrderSn;
		}
		deliveryOrderSn = '';//编辑主单时把全局变量交货单号置空  以便判断当前编辑的是主单商品还是某个交货单商品
		Ext.require("MB.jsPages.orderGoodsEditPage", function () {
			var app = Ext.application('MB.jsPages.orderGoodsEditPage');
		}, self);
	},
	/**
	 * 换单操作按钮回调后台
	 * 参数：退单号/操作类型/退单Json
	 */
	returnClick : function(btn) {
		var me = this;
		if(btn.action == 'saveExchange'){
			//var orderExchangeRetOrdModule = Ext.getCmp('orderExchangeRetOrdModule');
			//var payDetailModule = orderExchangeRetOrdModule.down('#payDetailModule');
			var orderExchangeChanOrdModule = Ext.getCmp('orderExchangeChanOrdModule');
			var params=orderExchangeChanOrdModule.getValues();
			var userId = orderExchangeChanOrdModule.getForm().findField('userName').getValue();
			console.dir("换单参数：" + userId);
			Ext.Ajax.request({
				url:  basePath + '/custom/exchangeorder/getUserRealMoney',
				params: {"userId": userId},
				success: function(response){
					var text = response.responseText;
					var results = Ext.JSON.decode(text);
					console.dir(results);
					if (results.isOk == 1) {
						var data = results.data;
						console.dir(data);
						var totalPayable = parseFloat(numFixed(params.totalPayable, 2));
						if (data < totalPayable) {
							Ext.msgBox.remainMsg('预付卡余额检查', "最新余额：" + data + "小于换单应付款金额：" + totalPayable + "，不能操作换单", Ext.MessageBox.INFO);
							return false;
						}
						Ext.Msg.confirm('警告', '确认保存换单吗?',function(flag){
							if(flag == 'yes'){
								me.callReturnService(btn);
							}else{
								return false;
							}
						});
					} else {
						Ext.msgBox.remainMsg('预付卡余额检查', results.message, Ext.MessageBox.ERROR);
						return false;
					}
				},
				failure: function(response){
					var text = response.responseText;
					Ext.msgBox.remainMsg('户预付卡余额', "户预付卡余额失败！", Ext.MessageBox.ERROR);
				}
			});
			//退款总金额
			//var returnTotalFee=parseFloat(payDetailModule.getForm().findField("returnTotalFee").getValue());
			//原订单已付款金额
			//var orderPayedMoney = parseFloat(orderExchangeRetOrdModule.getForm().reader.rawData.returnCommon.orderPayedMoney);
			/*Ext.Msg.confirm('警告', '确认保存换单吗?',function(flag){
				if(flag == 'yes'){
					me.callReturnService(btn);
				}else{
					return false;
				}
			});*/
			/*if(returnTotalFee > orderPayedMoney){
				Ext.Msg.confirm('警告', '退单总金额('+returnTotalFee+') 大于 原订单已付款在线支付金额('+orderPayedMoney+')，确认保存吗?',function(flag){
					if(flag == 'yes'){
						me.callReturnService(btn);
					}else{
						return false;
					}
				});
			}else{
				me.callReturnService(btn);
			}*/
		}else{
			me.callReturnService(btn);
		}
	},
	callReturnService : function(btn,actionWindow){
		var me = this;
		var params = {};
		var orderExchangeRetOrdModule = Ext.getCmp('orderExchangeRetOrdModule');
		var orderExchangeChanOrdModule = Ext.getCmp('orderExchangeChanOrdModule');
		if(btn.action == 'saveExchange'){
			var params = orderExchangeChanOrdModule.builtExchange();
			if(params == false){
				return false;
			}
			params = orderExchangeRetOrdModule.builtReturn(params);
			if(params == false){
				return false;
			}
			params.relatingOrderSn = orderSn;
			// 退货单商品金额与换货单商品金额必须一致
			console.dir(params);
			var totalPayable = params['pageOrder.totalPayable'];
			var totalFee = params['pageOrder.totalFee'];
			var returnTotalFee = params['orderReturnBean.createOrderReturn.returnTotalFee'];
				// 应付款金额为0
			if (parseFloat(numFixed(totalPayable, 2)) < 0.00 || parseFloat(numFixed(totalFee, 2)) < parseFloat(numFixed(returnTotalFee, 2))) {
				Ext.Msg.alert('保存换单', "退货单金额：" + returnTotalFee + ";换货单金额：" + totalFee + ",换货金额小于退货金额，不能创建换单");
				return ;
			}
		}
		if(!params)return params;
		params.btnType = btn.action;
		params.exchangeOrderSn = exchangeOrderSn;
		if(actionWindow != undefined && actionWindow != null){
			params.actionMsg = actionWindow.down('form').getValues().message;
		}
		console.dir(params);
		var json = {
				url:basePath + '/custom/exchangeorder/'+btn.action+"ButtonClick",
				params : params,
				timeout:90000,
				success : function(formPanel, action) {
					console.dir(action.result);
					if (action.result.success == "false") {
						Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
					} else {
						var isOk = action.result.ReturnInfo.isOk;
						var callExchangeOrderSn = action.result.ReturnInfo.orderSn;
						if(isOk > 0){
							if(exchangeOrderSn == ''){
								exchangeOrderSn = callExchangeOrderSn;						
							}
						}
						if(btn.action == 'saveExchange'){
							window.location.href = basePath + '/custom/exchangeorder/exchangeDetailPage?exchangeOrderSn='+exchangeOrderSn;							
						}else{
							me.onLaunch();
							Ext.msgBox.msg('业务操作', '操作成功', Ext.MessageBox.INFO);	
						}
						if(btn.action == "actionClick"){
							actionWindow.close();
						}
					}
				},
				failure : function(formPanel, action) {
					console.dir(action);
					if(action.result){
						Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);						
					}
				},
				waitMsg : 'Loading...'
			};
		orderExchangeChanOrdModule.submit(json);
	},
	/**
	 * 退单操作日志
	 */
	actionClick : function(btn) {
		var me = this;
		var win = Ext.create('Ext.window.Window', {
			id: 'action_note',
			height: 175,
			width: 367,
			scrollable: false,
			bodyPadding: 2,
			border:true,
			resizable:false,
			plain: true,
			maskDisabled : false ,
			modal : true ,
			items: [{
				xtype : 'form',
				items: [{
					xtype: 'textarea',
					emptyText : '日志内容', 
					name : 'message',
					style:'border:solid 0.5px gray;',
					width: 360,
					height : 100
				}]
			}],
			buttons : [
				{ text: "保存", handler : function () {
						var values = this.up("window").down('form').getValues();
						me.callReturnService(btn,this.up("window"));
					}
				}
			]
		});
		win.show();
	},
	sendMessage:function(btn) {
		if(exchangeOrderSn != ''){
			var relOrderSn = Ext.ComponentQuery.query("displayfield[name='relatingOriginalSn']")[1].value;
			if(relOrderSn == '' || relOrderSn == undefined){
				console.error("关联原订单号获取失败无法加载仓库信息");
				return;
			}
			var win = Ext.widget("messageEdit");
			win.initPage(relOrderSn);
			win.show();
		}
	}
});