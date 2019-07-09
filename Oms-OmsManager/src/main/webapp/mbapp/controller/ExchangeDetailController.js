Ext.define('MB.controller.ExchangeDetailController', {
	extend : 'Ext.app.Controller',
	stores : ['ReturnCommonStore','ReturnGoodsStore',
	           'OrderDetailStore', 'ProductLibBrands', 'ProductLibCategorys', 'SearchGoodss',
			   'GoodsColors', 'GoodsSizes', 'OrderGoodsStore','DeliveryDetailStore','PayDetailStore',
			   'SystemPaymentStore','CommonStatusStore','SettlementTypeStore','ProcessTypeStore','SystemShippingStore','ReturnErpWarehouseStore',
			   'GoodDetailStore', 'GoodsBonusStore'
			   ,'ExchangeGoodsStore','ReturnReasonStore','OrderActions','HaveRefundStore','OrderCustomDefines','ReturnBonusMoneyStore','ReturnShippingStore'],
	models : ['ReturnCommonModel','ReturnGoodsModel',
	          'OrderDetailModel', 'ProductLibCategory', 'ProductLibBrand', 'SearchGoods',
			  'ProductBarcodeList', 'OrderInfoEdit', 'OrderGoodsEdit','DeliveryDetailModel',
			  'PayDetailModel','GoodDetailModel','OrderShipEdit',
			  'SystemPaymentModel','CommonStatusModel','SystemShippingModel','ReturnErpWarehouseModel',
			  'ReturnCommonModel','ExchangeDetailModel','ReturnReasonModel', 'OrderAction','OrderCustomDefine'],
	views : ['returnOrder.ReturnSetModule','returnOrder.ReturnGoodsSetModule','returnOrder.ReturnGoodsList','returnOrder.ReturnPaySetModule',
	         'orderDetail.OrderCenter','orderDetail.OrderSetModule','orderDetail.ShipSetModule',
	         'orderDetail.GoodSetModule','orderDetail.OtherSetModule','orderDetail.GoodDetail','exchOrder.OrderDetailForm',
	         'orderDetail.DeliverySetModule','orderDetail.DeliveryDetail','orderDetail.PaySetModule',
	         'orderDetail.PayDetail','orderDetail.OrderQuestionType', /*'orderEdit.EditGoods',*/'orderEdit.ShipEdit','orderEdit.OtherEdit','orderEdit.PayEdit',
	         'exchOrder.ExchangeCenter','exchOrder.ExchangeSetModule','exchOrder.ExchangeGoodsSetModule','exchOrder.ExchangeGoodsList','exchOrder.ExchangePaySetModule','exchOrder.ExchangePayList','exchOrder.ExchangeAccountSetModule','exchOrder.ExchangeForm',
	         'exchOrder.ReturnForm','common.SystemPayment','common.CommonStatus','common.SettlementType','common.ProcessType','common.SystemShipping','common.ReturnErpWarehouse','common.ReturnReason',
	         'returnOrder.ReturnPayInfomation','orderDetail.PayEditForm','orderEdit.List','returnOrder.OrderReturnEdit','returnOrder.MessageEdit','common.HaveRefund','common.OrderCustomDefineCombo','common.ReturnBonusMoneyCombo','common.ReturnShippingCombo'],
	refs : [{
		ref : 'exchangeCenter',
		selector : 'ExchangeCenter'
	}/*,{
		ref : 'orderediteditgoods',
		selector : 'orderEditEditGoods'
	} */],
	init : function() {
		var me = this;
		me.control({
			'exchangeNorth button[action=saveExchange]' : {
				click : this.returnClick
			},
			'exchangeNorth button[action=unConfirmExchange]' : {
				click : this.returnClick
			},
			'exchangeNorth button[action=confirmExchange]' : {
				click : this.returnClick
			},
			'exchangeNorth button[action=cancelExchange]' : {
				click : this.returnClick
			},
			'exchangeNorth button[action=onlyReturnExchange]' : {
				click : this.returnClick
			},
			'exchangeNorth button[action=actionClick]' : {
				click : this.actionClick
			},
			'exchangeNorth button[action=sendMessage]' : {
				click : this.messageEdit
			},
			'exchangeForm tool[action=addGoods]' : { // 添加订单商品
				click : this.addOrderGoods
			},
			'orderediteditgoods button[action=saveOrderGoods]' : { // 保存订单商品
				click : this.saveOrderGoods
			},
			'returnSetModule tool[action=orderReturnEdit]':{
				click:this.orderReturnEdit
			},
			'orderReturnEdit button[action=orderReturnEditSave]' : {
				click : this.orderReturnEditSave
			}
		});
	},
	onLaunch : function() {
		var exchangeCenter=Ext.getCmp('exchangeCenter');
		exchangeCenter.initPage();
	},
	// editOrderInfo: function (grid, record) {
	editOrderInfo : function(btn) {
		// var form = Ext.widget("demoshow");
		// form.loadRecord(record);
		var win = Ext.widget("demoedit");
		win.down("form").load({
			url : basePath + '/custom/demo/readDemo',
			params : {
				"orderSn" : orderSn
			},
			success : function(form, action) {
				// 数据加载成功后操作
				// form.editStatus(opForm, action)
				win.initPage(form);
			},
			failure : function(form, action) {
				// 数据加载失败后操作
			}
		});
		win.show();
	},
	addOrderGoods : function(btn) { // 编辑订单商品
		//Ext.getCmp('exchangeForm').getForm()..reader.rawData
		//申请状态或未确认状态方可新增商品或者编辑商品
		if(exchangeOrderSn){
			var orderStatus = Ext.getCmp('exchangeForm').getForm().reader.rawData.result.orderStatus;
			if(orderStatus != 0){
				return ;
			}
		}
		var paramOrderSn='';
		if(exchangeOrderSn){
			paramOrderSn=exchangeOrderSn;
		}
		/*var win = Ext.widget("orderediteditgoods");
		win.down("form").load({
			url : basePath + '/custom/editOrder/editOrderGoods',
			params : {
				"orderSn" : paramOrderSn,
				"method" : 'init',
				"addGoodsType" : "1"
			},
			async:false,
			success : function(form, action) {
				// 数据加载成功后操作
				//console.dir(form.reader.rawData.info.orderGoodsUpdateInfos);
				win.initPage(win, form);
				Ext.msgBox.msg('添加商品', '加载商品数据成功', Ext.MessageBox.INFO);
			},
			failure : function(form, action) {
				// 数据加载失败后操作
				Ext.msgBox.msg('添加商品', '加载商品数据失败！', Ext.MessageBox.ERROR);
			}
		});
		win.down('grid').columns[6].editor=false;
		win.down('grid').doLayout();
		win.show();*/
		
		deliveryOrderSn = '';//编辑主单时把全局变量交货单号置空  以便判断当前编辑的是主单商品还是某个交货单商品
		Ext.require("MB.jsPages.orderGoodsEditPage", function () {
			var app = Ext.application('MB.jsPages.orderGoodsEditPage');
		}, self);
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
				}/*,
				{ text: "关闭", handler : function () { this.up("window").close(); }}*/
			]
		});
		win.show();
	},
	/**
	 * 换单操作按钮回调后台
	 * 参数：退单号/操作类型/退单Json
	 */
	returnClick : function(btn) {
		var me = this;
		if(btn.action == 'saveExchange'){
			//退款总金额
			var returnTotalFee=parseFloat(Ext.getCmp("returnPaySetModule").getForm().findField("returnTotalFee").getValue());
			//原订单已付款金额
			var orderPayedMoney = parseFloat(Ext.getCmp('returnForm').getForm().reader.rawData.returnCommon.orderPayedMoney);
			Ext.Msg.confirm('警告', '确认保存换单吗?',function(flag){
				if(flag == 'yes'){
					me.callReturnService(btn);
				}else{
					return false;
				}
			});
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
		var exchangeCenter=Ext.getCmp('exchangeCenter');
		var exchangeOrder = {};
		if(btn.action == 'saveExchange'){
			exchangeOrder = exchangeCenter.builtParams();
			exchangeOrder.relatingOrderSn = orderSn;
		}
		if(!exchangeOrder)return exchangeOrder;
		exchangeOrder.btnType = btn.action;
		exchangeOrder.exchangeOrderSn = exchangeOrderSn;
		if(actionWindow != undefined && actionWindow != null){
			exchangeOrder.actionMsg = actionWindow.down('form').getValues().message;
		}
		var json = {
				url:basePath + '/custom/exchangeorder/'+btn.action+"ButtonClick",
				params : exchangeOrder,
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
		exchangeCenter.submit(json);
	},
	saveOrderGoods : function(btn) {
		if(exchangeOrderSn){
			var me = this;
			var win = btn.up("window");
			form = win.down("form");
			var params = win.editOrderGoodsParams(form);
			params.orderSn=exchangeOrderSn;
			console.dir(params);
			var json = {
				params : params,
				success : function(formPanel, action) {
					if (action.result.success == "false") {
						Ext.msgBox.remainMsg('修改商品', action.result.msg, Ext.MessageBox.ERROR);
					} else {
						Ext.msgBox.msg('修改商品', action.result.msg, Ext.MessageBox.INFO);
						var orderForm = Ext.getCmp('orderCenter');
						orderForm.initData(orderForm);
						win.close();
					}
				},
				failure : function(formPanel, action) {
					Ext.msgBox.remainMsg('修改商品', action.response.statusText, Ext.MessageBox.ERROR);
				},
				waitMsg : 'Loading...'
			};
			form.submit(json);
		}else{
			var win = btn.up("window");
			var grid=win.down('ordereditlist');
			var newGoodStore=[];
			var total=0;
			var bonus=0;//红包
			grid.store.each(function(record,i){
				if(record.get('shareBonus')){
					record.data.shareBonus=record.get('shareBonus');
				}
				if(record.get('goodsThumb')){
					record.data.goodsThumb=record.get('goodsThumb');
				}
				newGoodStore.push(record.data);
				total+=record.get('transactionPrice')*record.get('goodsNumber');
				bonus+=record.get('shareBonus');
			});
			
			Ext.getCmp('exchangeGoodsSetModule').down('goodDetail').store.loadData(newGoodStore);
			//总计
			Ext.getCmp('exchangeGoodsSetModule').getForm().findField('total').setValue(total);
			Ext.getCmp('exchangeCenter').down('exchangePaySetModule').getForm().findField('goodsAmount').setValue(total);
			//红包
			Ext.getCmp('exchangeCenter').down('exchangePaySetModule').getForm().findField('bonus').setValue(bonus);
			win.close();
			Ext.msgBox.msg('结果', '添加商品成功！', Ext.MessageBox.INFO);
		}
		
		
	},
	closeOrderInfo : function(btn) {
		var win = btn.up("window");
		win.close();
	},
	reloadPage : function() {},
	orderReturnEdit : function(btn) {
		var win = Ext.widget("orderReturnEdit");
		win.initPage(win.down('form'));
		win.show();
	},
	orderReturnEditSave:function(btn){
		var me = this;
		var win = btn.up("window");
		form = win.down("form");
		var params = win.orderReturnEditParams(form);
		params.btnType = 'editReturn';
		console.dir(params);
		var json = {
			params : params,
			timeout:90000,
			success : function(formPanel, action) {
				if (action.result.success == "false") {
					errorMsg("结果", action.result.msg);
				} else {
					win.close();
					window.location.reload();
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		form.submit(json);
	
	},
	messageEdit:function(btn) {
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