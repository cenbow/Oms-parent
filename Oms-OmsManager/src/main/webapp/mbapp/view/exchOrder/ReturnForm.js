Ext.define("MB.view.exchOrder.ReturnForm", {
	extend: "Ext.form.Panel",
	alias: 'widget.returnForm',
	id:'returnForm',
	title: "<span style='color:black;font-weight:bold';>退货单信息</span>",
	width: '100%',
	frame: true,
	autoWidth:true,
	autoHeight:true,
	autoScroll : false,
	collapsible:true,
	titleCollapse:true,
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'right'
	},
	bodyBorder:true,
	//store: 'Demos',
	initComponent: function () {
		this.items = [
		//退单
		{
			xtype:'returnSetModule'
		}, {
			xtype:'returnGoodsSetModule'
		}, {
			xtype:'returnPayInfomation'
		},{
			xtype:'returnPaySetModule'
		}
		];
//		//以json形式读取数据
		this.reader = Ext.create('Ext.data.reader.Json', {
					rootProperty : "returnCommon",
					model : 'MB.model.ReturnCommonModel'
		});
		this.callParent(arguments);
	},
	initData: function() {
		if(orderSn == '' && returnSn == ''){
			console.error('换货单-退单数据加载异常，退单号，关联订单号为空');
			return;
		}
		var me = this;
		//展开退货单，后台发送请求
		if(returnSn == ''){
			me.loadReturn();			
		}else{
			Ext.getCmp('returnForm').collapse();
			Ext.getCmp('returnForm').addListener('expand',function(){
				me.loadReturn();
			});
		}
	},
	loadReturn:function(){
		console.dir("returnForm-1");
		var returnCenter =this,
		returnGoodsList = Ext.getCmp('returnGoodsList'),
		returnSetModule = Ext.getCmp('returnSetModule'),
//		returnSouth = Ext.getCmp('returnSouth'),
		returnChannelDepot = Ext.getCmp('returnChannelDepot'),
		returnPaySetModule=Ext.getCmp('returnPaySetModule');
		returnPayInfomation=Ext.getCmp('returnPayInfomation');
		var me = this;
		
		/*Ext.Ajax.request({
			url:  basePath + '/custom/orderReturn/getOrderReturnDetail',
			params: {
						"relOrderSn" : orderSn,
						"returnSn" :returnSn,
						"returnType" : returnType
					},
			success: function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				returnSetModule.initData(respText.returnCommon);
				returnCenter.getForm().findField('returnShipping').setValue(0);
				returnCenter.getForm().findField('returnOtherMoney').setValue(0);
				returnCenter.getForm().findField('returnBonusMoney').setValue(0);
				
				returnGoodsList.hideColumn(respText.returnCommon,returnGoodsList);
				returnGoodsList.store.loadData(respText.returnGoods);
				channelCode = respText.returnCommon.channelCode;
				if(returnSn){
					//三个金额无法正常渲染，故人工指定
					var returnShipping = respText.returnAccount.returnShipping;
					var returnOtherMoney = respText.returnAccount.returnOtherMoney;
					var returnBonusMoney = respText.returnAccount.returnBonusMoney;
					var totalIntegralMoney = respText.returnAccount.totalIntegralMoney;
					
					if(returnShipping) returnCenter.getForm().findField('returnShipping').setValue(returnShipping);
					if(returnOtherMoney) returnCenter.getForm().findField('returnOtherMoney').setValue(returnOtherMoney);
					if(returnBonusMoney) returnCenter.getForm().findField('returnBonusMoney').setValue(returnBonusMoney);
					if(totalIntegralMoney) returnCenter.getForm().findField('totalIntegralMoney').setValue(totalIntegralMoney);
				}
				
				//退单原因
				returnCenter.down('orderCustomDefineCombo').getStore().on('beforeload', function (store){
					params = { "type" : 1};
					Ext.apply(store.proxy.extraParams, params);
				});
				returnCenter.down('orderCustomDefineCombo').getStore().load();
				if(returnSn == ''){
					returnCenter.getForm().findField("returnSettlementType").setValue(1);
					if(returnType == '1'){
						returnCenter.getForm().findField("processType").setValue(4);						
					}else{
						returnCenter.getForm().findField("processType").setValue(0);
					}
					if(returnType == '2' || exchangeOrderSn == ''){
						returnCenter.getForm().findField("haveRefund").setValue(0);						
					}else{
						returnCenter.getForm().findField("haveRefund").setValue(1);
					}
				}
				returnGoodsList.getSelectionModel().selectAll();
				returnPaySetModule.paySet(respText.returnGoods);
				returnPayInfomation.initPage(respText);
				returnCenter.disablePage();
				
				//换单支付方式默认为订单的第一种支付方式
				var returnFirstPayId = Ext.getCmp('returnForm').getForm().findField('setPayment1').getValue();
				if(returnFirstPayId){
					Ext.getCmp('exchangeForm').getForm().findField('payMent').setValue(returnFirstPayId);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('页面加载', respText.errorMessage, Ext.MessageBox.ERROR);
			}
		});*/
		
		returnCenter.load({
			url : basePath + '/custom/orderReturn/getOrderReturnDetail',
			params : {
				"relOrderSn" : orderSn,
				"returnSn" :returnSn,
				"returnType" : returnType
			},//TD150707348610
			timeout:90000,
			success : function(opForm, action) {
				console.dir("returnForm-2"+opForm);
				returnCenter.getForm().findField('returnShipping').setValue(0);
				returnCenter.getForm().findField('returnOtherMoney').setValue(0);
				returnCenter.getForm().findField('returnBonusMoney').setValue(0);
				
				returnGoodsList.hideColumn(opForm.reader.rawData.returnCommon,returnGoodsList);
				returnGoodsList.store.loadData(opForm.reader.rawData.returnGoods);
				channelCode = opForm.reader.rawData.returnCommon.channelCode;
				if(returnSn){
					//三个金额无法正常渲染，故人工指定
					var returnShipping = opForm.reader.rawData.returnAccount.returnShipping;
					var returnOtherMoney = opForm.reader.rawData.returnAccount.returnOtherMoney;
					var returnBonusMoney = opForm.reader.rawData.returnAccount.returnBonusMoney;
					var totalIntegralMoney = opForm.reader.rawData.returnAccount.totalIntegralMoney;
					
					if(returnShipping) returnCenter.getForm().findField('returnShipping').setValue(returnShipping);
					if(returnOtherMoney) returnCenter.getForm().findField('returnOtherMoney').setValue(returnOtherMoney);
					if(returnBonusMoney) returnCenter.getForm().findField('returnBonusMoney').setValue(returnBonusMoney);
					if(totalIntegralMoney) returnCenter.getForm().findField('totalIntegralMoney').setValue(totalIntegralMoney);
				}
				
				//退单原因
				returnCenter.down('orderCustomDefineCombo').getStore().on('beforeload', function (store){
					params = { "type" : 1};
					Ext.apply(store.proxy.extraParams, params);
				});
				returnCenter.down('orderCustomDefineCombo').getStore().load();
				if(returnSn == ''){
					returnCenter.getForm().findField("returnSettlementType").setValue(1);
					if(returnType == '1'){
						returnCenter.getForm().findField("processType").setValue(4);						
					}else{
						returnCenter.getForm().findField("processType").setValue(0);
					}
					if(returnType == '2' || exchangeOrderSn == ''){
						returnCenter.getForm().findField("haveRefund").setValue(0);						
					}else{
						returnCenter.getForm().findField("haveRefund").setValue(1);
					}
				}
				returnGoodsList.getSelectionModel().selectAll();
				returnPaySetModule.paySet(opForm.reader.rawData.returnGoods);
				returnPayInfomation.initPage(opForm.reader.rawData);
				returnCenter.disablePage();
				
				//换单支付方式默认为订单的第一种支付方式
				var returnFirstPayId = Ext.getCmp('returnForm').getForm().findField('setPayment1').getValue();
				if(returnFirstPayId){
					Ext.getCmp('exchangeForm').getForm().findField('payMent').setValue(returnFirstPayId);
				}
			},
			failure : function(opForm, action) {
				console.dir("shibai");
				// 数据加载失败后操作
//				Ext.msgBox.remainMsg("页面加载", opForm.reader.rawData.errorMessage, Ext.MessageBox.ERROR);
			}
		});
	},
	//非申请页面禁用组件
	disablePage : function() {
		if(returnSn == ''){
			return;
		}
		//退单基本信息
		var returnCenter = this.getForm();
		returnCenter.findField("processType").readOnly = true;
		returnCenter.findField("returnInvoiceNo").setReadOnly(true);
		returnCenter.findField("depotCode").readOnly = true;
		returnCenter.findField("returnReason").readOnly = true;
		returnCenter.findField("returnDesc").setReadOnly(true);
		returnCenter.findField("newOrderSn").setReadOnly(true);
		returnCenter.findField("returnSettlementType").readOnly = true;
		returnCenter.findField("returnExpress").readOnly = true;
		//退单商品信息
		var goodGrid=Ext.getCmp('returnGoodsList');
		var columns=goodGrid.columnManager.columns;//editable
		goodGrid.showHeaderCheckbox=false;
		for(var i=0;i<columns.length;i++){
			columns[i].processEvent = function(type) { // 加入这一句，可以防止点中修改  
	            if (type == 'click')  
	                return false;  
	        };  
		}
		
		//goodGrid.disable();
		//付款信息
		var returnRefunds = returnCenter.reader.rawData.returnRefunds;
		var len=returnRefunds.length + 1;
		for(var i=1;i<len;i++){
			Ext.getCmp("returnPayInfomation").getForm().findField("setPayment"+i).readOnly = true;
			Ext.getCmp("returnPayInfomation").getForm().findField("setPaymentNum"+i).setReadOnly(true);
		}
		//配送信息
		returnCenter.findField("returnShipping").setReadOnly(true);
		//其他费用
		returnCenter.findField("returnOtherMoney").setReadOnly(true);
		//红包金额
		returnCenter.findField("returnBonusMoney").setReadOnly(true);
	},
	builtReturn:function(params){
		var form=this;
		var param=form.getValues();
		var returnRefunds=form.getForm().reader.rawData.returnRefunds;
		var returnCommon=form.getForm().reader.rawData.returnCommon;
		var orderPays=form.getForm().reader.rawData.orderPays;
		params['orderReturnBean.returnType']=returnCommon.returnType;
		params['orderReturnBean.relatingOrderSn']=returnCommon.relatingOrderSn;
		var returnTotalFee=Ext.getCmp("returnPaySetModule").getForm().findField("returnTotalFee").getValue();
		
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
		
		/*if(returnTotalFee<=0){
			Ext.Msg.alert('警告', "退款申请单金额不能为0！", function(xx) {});
			return false;
		}*/
		
		params['orderReturnBean.createOrderReturnShip.relatingReturnSn']=returnCommon.returnSn;
		params['orderReturnBean.createOrderReturnShip.returnExpress']=param.returnExpress;
		params['orderReturnBean.createOrderReturnShip.returnInvoiceNo']=param.returnInvoiceNo;
		params['orderReturnBean.createOrderReturnShip.depotCode']=param.channelDepot;
		
		params['orderReturnBean.createOrderReturn.relatingOrderSn']=returnCommon.relatingOrderSn;
		params['orderReturnBean.createOrderReturn.newOrderSn']=param.newOrderSn;
		params['orderReturnBean.createOrderReturn.returnSettlementType']=param.returnSettlementType;
		params['orderReturnBean.createOrderReturn.returnSn']=returnCommon.returnSn;
		params['orderReturnBean.createOrderReturn.processType']=param.processType;
		params['orderReturnBean.createOrderReturn.returnTotalFee']=returnTotalFee;
		params['orderReturnBean.createOrderReturn.returnGoodsMoney']=Ext.getCmp("returnPaySetModule").getForm().findField("returnGoodsMoney").getValue();
		params['orderReturnBean.createOrderReturn.totalPriceDifference']=Ext.getCmp("returnPaySetModule").getForm().findField("totalPriceDifference").getValue();
		params['orderReturnBean.createOrderReturn.returnType']=returnCommon.returnType;
		params['orderReturnBean.createOrderReturn.returnShipping']=Ext.getCmp("returnPaySetModule").getForm().findField("returnShipping").getRawValue();;
		params['orderReturnBean.createOrderReturn.returnOtherMoney']=param.returnOtherMoney;
		params['orderReturnBean.createOrderReturn.totalIntegralMoney']=param.totalIntegralMoney;//积分使用金额
		params['orderReturnBean.createOrderReturn.returnBonusMoney']=Ext.getCmp("returnPaySetModule").getForm().findField("returnBonusMoney").getRawValue();;
		params['orderReturnBean.createOrderReturn.returnReason']=param.returnReason;
		if(param.returnReason == ""){
			Ext.Msg.alert('警告', "请选择退单原因！", function(xx) {
			});
			return false;
		}
		params['orderReturnBean.createOrderReturn.haveRefund']=param.haveRefund;
		params['orderReturnBean.createOrderReturn.returnDesc']=param.returnDesc;
		
		//商品数据
		var goodGrid=Ext.getCmp('returnGoodsList');
		var goodDataChecked = goodGrid.getSelectionModel().selected.items;
		var checkedLen=goodDataChecked.length;
		if(checkedLen<=0){
			Ext.Msg.alert('警告', "请选择商品！", function(xx) {
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
		/*params['orderReturnBean.createOrderRefundList['+0+'].returnPaySn']='';
		params['orderReturnBean.createOrderRefundList['+0+'].relatingReturnSn']='';
		params['orderReturnBean.createOrderRefundList['+0+'].returnPay']='3';
		params['orderReturnBean.createOrderRefundList['+0+'].returnFee']=101;*/
		
		params.orderReturnSn=returnCommon.returnSn;//退单Sn
		return params;
	}
});
