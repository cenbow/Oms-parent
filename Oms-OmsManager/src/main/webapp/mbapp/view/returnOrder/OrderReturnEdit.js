Ext.define("MB.view.returnOrder.OrderReturnEdit", {
	extend: "Ext.window.Window",
	id:'orderReturnEdit',
	alias: "widget.orderReturnEdit",
//	requires: ['MB.view.demo.OrderStatusCombo','MB.view.demo.ChannelInfoCombo'],
	title: "退单保存",
	width: 450,
	height: 450,
	initComponent: function () {
		this.items = {
			xtype: "form",
//			margin: 5,
			border: false,
			frame: true,
			layout: "form",
			url : basePath + '/custom/orderReturn/editReturnButtonClick',
			fieldDefaults: {
//				labelWidth: 300,
		        labelAlign: "right",
		        flex: 1,
		        margin:5
			},
			/*reader: new Ext.data.reader.Json({
				rootProperty : "orderInfo",
				model : 'MB.model.OrderDetailModel'
			}),*/
			items:[
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
					        { xtype: "displayfield", name: "returnSn", fieldLabel: "退单号", width: 300,labelWidth: 150 }
						   ]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "commonprocessType", name : 'processType', fieldLabel : "处理方式", width: 300,labelWidth: 150 }]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "commonsettlementType", name : 'returnSettlementType', fieldLabel : "退款类型",width: 300,labelWidth: 150}
						]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "commonsystemShipping", name : 'returnExpress', fieldLabel:"退货承运商",width: 300,labelWidth: 150 }
						]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "textfield", name : 'returnInvoiceNo', fieldLabel : "退货面单号", width: 300,labelWidth: 150  }
						]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "textfield", name : 'newOrderSn', fieldLabel : "换货单号", width: 300,labelWidth: 150 }
						]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "orderCustomDefineCombo", name : 'returnReason', fieldLabel : "退单原因", width: 300,labelWidth: 150 }
						]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "commonreturnerpwarehouse", name : 'depotCode',fieldLabel:"退货仓库",width: 370,labelWidth: 150 }]
				}/*,
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "displayfield", name : 'returnTotalFee', fieldLabel : "退款总金额(元)", width: 300,labelWidth: 150}]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					id:'paySetFieldcontainer1',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "displayfield", width: 100},
						{xtype : "displayfield", value:'&nbsp;&nbsp;=',width:20},
						{xtype :'commonsystemPayment',id:'payment1',name:'payment1',fieldLabel:false,width: 150,
							listeners:{
								'change':function(field){
									this.up('window').checkPayment(field);
								}
							}
						},
						{xtype : "numberfield", name : 'payment1Num', fieldLabel:false,value:'50',width:70,labelAlign: 'right',minValue:0,
							listeners:{
								'change':function(field){
									this.up('window').checkPaymentNum(field);
								}
							}
						},
						{xtype : "displayfield", id:"add1+",width:20,value:'&nbsp;&nbsp;+'}]
				}*//*,
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "displayfield", width: 130},
						{xtype :'commonsystemPayment',id:'payment2',fieldLabel:false,width: 150},
						{xtype : "numberfield", name : 'payment2Num', fieldLabel : false,width:70,value:'50' },
						{xtype : "displayfield", width:20,value:'&nbsp;&nbsp;+'}]
				},
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "displayfield", width:130 },
						{xtype :'commonsystemPayment',id:'payment3',fieldLabel:false,width: 150},
						{xtype : "numberfield", name : 'payment3Num', fieldLabel :false,width:70,value:"50"}]
				}*/
//				{ xtype: "textfield", name: "orderSn",hidden:true, fieldLabel: "订单编号" },
			]
		};
		this.buttons = [
			{ text: "关闭", action: "close",handler : function () { this.up("window").close(); } },
			{ text: "保存", action: "orderReturnEditSave" }
		];
		/*var returnRefunds=null;
		if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
			returnRefunds=Ext.getCmp('returnForm').getForm().reader.rawData.returnRefunds;
		}else{
			returnRefunds=Ext.getCmp('returnCenter').getForm().reader.rawData.returnRefunds;
		}
		var len=returnRefunds.length;
		
		
		for(var i=0;i<len;i++){
			if(i>0){
				this.items.items.push(
					{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{xtype : "displayfield", width: 130},
						{xtype :'commonsystemPayment',id:'payment'+(i+1),name:'payment'+(i+1),fieldLabel:false,width: 150,
							listeners:{
								'change':function(field){
									this.up('window').checkPayment(field);
								}
							}
						},
						{xtype : "numberfield", name : 'payment'+(i+1)+'Num', fieldLabel : false,width:70,value:0 ,minValue:0,
							listeners:{
								'change':function(field){
									this.up('window').checkPaymentNum(field);
								}
							}
						},
						{xtype : "displayfield",id:'add'+(i+1)+'+', width:20,value:'&nbsp;&nbsp;+'}]
				}
				);
			}
				
				
			
			
		}*/
		
		this.callParent(arguments);
	},
	initPage: function (form) {
//		var formValues=Ext.getCmp('returnCenter').getForm().getValues();
//		var returnCenter=Ext.getCmp('returnCenter');
//		console.dir(formValues);
//		console.dir(Ext.getCmp('returnCenter').getForm().reader.rawData.returnCommon);
//		console.dir(form.getForm().findField("returnSn"));
		var returnForm=null;
		if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
			returnForm=Ext.getCmp('returnForm');
		}else{
			returnForm=Ext.getCmp('returnCenter');
		}
		var returnCommon=returnForm.getForm().reader.rawData.returnCommon;
		
		
		
		//原始退款总金额
		var returnTotalFee=Ext.getCmp('returnPayInfomation').getForm().findField("returnTotalFee").getValue();
		
		form.getForm().setValues(returnCommon);
		
		/*var combo = form.getForm().findField('depotCode');
		if(!combo.getValue()){
			//仓库默认设置第一个仓库
			combo.getStore().load();
			combo.getStore().on('load',function(){
	//			console.dir(combo.getStore().getAt(0).get('warehouseCode'));
				if(combo.getStore().getAt(0)){
					combo.select(combo.getStore().getAt(0).get('warehouseCode'));
				}
			})
		}*/
		
		/*var returnRefunds=returnForm.getForm().reader.rawData.returnRefunds;
		var len=returnRefunds.length;
		
		
		for(var i=0;i<len;i++){
			
			if(i==0){
				form.getForm().findField("payment1").setValue(returnRefunds[i].returnPay);	
				form.getForm().findField("payment1Num").setValue(returnRefunds[i].returnFee);	
			}else{
				if(form.getForm().findField("payment"+(i+1))){
					form.getForm().findField("payment"+(i+1)).setValue(returnRefunds[i].returnPay);						
					form.getForm().findField("payment"+(i+1)+"Num").setValue(returnRefunds[i].returnFee);
				}
			}
			if(form.getForm().findField("payment"+(i+1)+"Num")){
				form.getForm().findField("payment"+(i+1)+"Num").setMaxValue(returnTotalFee);
			}
			//returnPaySn隐藏域
			var returnPaySnItem={xtype : "textfield",name:"returnPaySn"+i,value:returnRefunds[i].returnPaySn};
			form.add(returnPaySnItem);
			form.doLayout();
			console.dir(form);
		}
		if(len<=1){
			form.getForm().findField("payment1Num").setReadOnly(true);
		}
		//去掉支付方式后面的+号
		Ext.getCmp('add'+len+'+').setValue('');*/
	},
	orderReturnEditParams:function(from){
		var params = form.getValues();
		console.dir(params);
		var returnForm=null;
		if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
			returnForm=Ext.getCmp('returnForm');
		}else{
			returnForm=Ext.getCmp('returnCenter');
		}
		var returnRefunds=returnForm.getForm().reader.rawData.returnRefunds;
		var returnCommon=returnForm.getForm().reader.rawData.returnCommon;
		/*var len=returnRefunds.length;
		for(var i=0;i<len;i++){
			params['createOrderRefundList['+i+'].returnPaySn']=returnRefunds[i].returnPaySn;
			params['createOrderRefundList['+i+'].relatingReturnSn']=returnRefunds[i].relatingReturnSn;
			params['createOrderRefundList['+i+'].returnPay']=params["payment"+(i+1)];
			params['createOrderRefundList['+i+'].returnFee']=params["payment"+(i+1)+"Num"];
		}*/
		params['createOrderReturnShip.relatingReturnSn']=returnCommon.returnSn;
		params['createOrderReturnShip.returnExpress']=params.returnExpress;
		params['createOrderReturnShip.returnInvoiceNo']=params.returnInvoiceNo;
		params['createOrderReturnShip.depotCode']=params.depotCode;
		
		params['createOrderReturn.relatingReturnSn']=returnCommon.relatingOrderSn;
		params['createOrderReturn.newOrderSn']=params.newOrderSn;
		params['createOrderReturn.returnSettlementType']=params.returnSettlementType;
		params['createOrderReturn.returnSn']=returnCommon.returnSn;
		params['createOrderReturn.processType']=params.processType;
		params['createOrderReturn.returnReason']=params.returnReason;
		
		params.orderReturnSn=returnCommon.returnSn;//退单Sn
		return params;
	},
	checkPayment:function(field){
		var returnForm=null;
		if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
			returnForm=Ext.getCmp('returnForm');
		}else{
			returnForm=Ext.getCmp('returnCenter');
		}
		var i=parseInt(field.name.split('payment')[1]);
		var returnRefunds=returnForm.getForm().reader.rawData.returnRefunds;
		var len=returnRefunds.length;
		var thisValue=parseFloat(field.getValue());
		for(var j=1;j<=len;j++){
				if(j!=i){
					if(Ext.getCmp('payment'+j)){
						var otherValue=parseFloat(Ext.getCmp('payment'+j).getValue());
						if(thisValue==otherValue){//支付方式相同
//							errorMsg("结果", "该支付方式已存在！");
							Ext.Msg.alert('结果', "该支付方式已存在！", function(xx) {
								field.setValue("请选择支付方式");
							});
							return false;
						}
					
					}
				
				}
			}
	},
	checkPaymentNum:function(field){
		var returnForm=null;
		if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
			returnForm=Ext.getCmp('returnForm');
		}else{
			returnForm=Ext.getCmp('returnCenter');
		}
		 
		/**
		 * 1.与兄弟节点（其他支付方式）的钱联动：所有支付方式加起来的钱为总金额
		 * 2.本支付方式变化的钱会在相邻的下一个支付方式变动，加入为最后一个支付方式，则其变化的数据就在第一个上变化
		 * 3.支付方式数据与退单付款信息页面支付方式数据联动
		 */
		//原始退款总金额
		var returnTotalFee=Ext.getCmp('returnPayInfomation').getForm().findField("returnTotalFee").getValue();
//		console.dir(this.down('fieldcontainer'));
		var changeTotalFee=0;
		var paySetNum=returnForm.getForm().reader.rawData.returnRefunds.length;//获取有多少个支付方式
		for(var i=1;i<=paySetNum;i++){
			changeTotalFee+=this.down('form').getForm().findField("payment"+i+"Num").getValue();
		}
		var k=parseInt(field.name.split('payment')[1].split('Num')[0]);
		var changeNum=0;
		
		changeNum=returnTotalFee-changeTotalFee;
		if(returnTotalFee==changeTotalFee){
			editPaymentNumFlag=true;
			return false;
		}
		
		if(editPaymentNumFlag){
			if(k==paySetNum&&k!=1){//说明为最后一个支付方式
			editPaymentNumFlag=false;
			var lastNum=this.down('form').getForm().findField("payment1Num").getValue();
			this.down('form').getForm().findField("payment1Num").setValue(lastNum+changeNum);
			
		}else{
			if(this.down('form').getForm().findField("payment"+(k+1)+"Num")){
				var lastNum=this.down('form').getForm().findField("payment"+(k+1)+"Num").getValue();
				if((lastNum+changeNum)<=0){//当下一个支付方式的值小与需要变化的值时
					this.down('form').getForm().findField("payment"+(k+1)+"Num").setValue(0);
					
				}else{
					editPaymentNumFlag=false;
					this.down('form').getForm().findField("payment"+(k+1)+"Num").setValue(lastNum+changeNum);
				}
				return false;
			}
			
		
		}
		
		}
		
	
	}
});
var editPaymentNumFlag=true;