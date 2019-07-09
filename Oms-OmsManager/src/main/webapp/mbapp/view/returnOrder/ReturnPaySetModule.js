Ext.define("MB.view.returnOrder.ReturnPaySetModule", {
	extend: "Ext.form.Panel",
	id:"returnPaySetModule",
	alias: 'widget.returnPaySetModule',
	width: '100%',
	frame: true,
	title:'退单账目&nbsp;&nbsp;&nbsp;',
	head:true,
	margin:10,
    bodyPadding:10,
	layout:'column',
	buttonAlign : 'center',// 按钮居中
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
	//collapsed: true,
	initComponent: function () {
		var arr = new Array();
		for (var i =0 ; i < 3 ;i++) {
			arr.push({xtype : "displayfield",  columnWidth: .1 });
		}
		var first = {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: arr
			};
		
		this.items = [/*first, {
     		xtype: 'panel',
     		id:'returnPayDatas',
	        width: 200,
	        height: 280,
	        bodyPadding: 10
		},*/
			{
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
							this.up('form').payTotalSet();
						}
					}
				},
				{xtype : "numberfield", name : 'returnOtherMoney', fieldLabel : "+ 退其他费用",value:0.00,columnWidth:.17,
					decimalPrecision : 2,//精确到小数点后两位  
	                allowDecimals : true,//允许输入小数  
	                nanText :'请输入有效的数字',//无效数字提示  
					listeners:{
						'change':function(field){
							this.up('form').payTotalSet();
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
								this.up('form').payTotalSet();
							}
						}
					},
				{xtype : "returnBonusMoneyCombo", name : 'returnBonusMoney', fieldLabel : "- 红包金额",columnWidth:.17,labelWidth:100,
//					decimalPrecision : 2,//精确到小数点后两位  
//	                allowDecimals : true,//允许输入小数  
//	                nanText :'请输入有效的数字',//无效数字提示  
					listeners:{
						'change':function(field){
							this.up('form').payTotalSet();
						}
					}
				}
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			id:'paySetFieldContainer',
			layout: 'column',
			columnWidth:1,
			items: [
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth:1,
			items: [
				{xtype : "displayfield", columnWidth:.505},
				{xtype : "displayfield", name : 'returnTotalFee', fieldLabel : "= 退款总金额",value:0.00,labelWidth:200, columnWidth:.2 ,
					listeners:{
						'change':function(field){
							if(Ext.getCmp('moneyPaid')){
								Ext.getCmp('moneyPaid').setValue(field.getValue());
							}
						}
					}
				}
			]
		} 
		];
		this.tools=[
			 	{
					type: 'gear',
					id:'returnPaySetModuleG',
		            tooltip : '修改',
		            action: 'returnPayEdit',
		            hidden:true,
		            //handler: me.onCloseClick,
		            scope: me
				}];
		var me = this;
		
        me.callParent(arguments);
	},
	initPayment : function(orderPays,returnTotalFee,returnBonusMoney,returnAccount) {//申请退单过来联动数据
		
		//(应财务要求，默认配送费用去掉，所以这里需要减去配送费用)
		var returnShipping = 0;
		if(returnAccount.returnShipping){
			returnShipping = returnAccount.returnShipping;
		}
		
		var len = orderPays.length;
		for (var i = 0; i < len; i++) {
			if(!Ext.getCmp('paySet'+(i+1))){
				if(i==0){
					this.down('#paySetFieldContainer').add(
						{xtype : "displayfield", columnWidth:.305},
						{xtype : "displayfield", id:'paySet'+(i+1),columnWidth:.2, fieldLabel :"=" + Ext.getCmp('returnPayInfomation').down('#setPayment' + (i + 1) + '').getRawValue(),value:0,labelWidth:150}
					);
				}else if (i > 0) {
					this.down('#paySetFieldContainer').add(
						{xtype : "displayfield", id:'paySet'+(i+1),columnWidth:.2, fieldLabel :"+"+ Ext.getCmp('returnPayInfomation').down('#setPayment' + (i + 1) + '').getRawValue(),value:0,labelWidth:150}
					);
					this.doLayout();
				}
				var setPayValue = parseFloat(orderPays[i].payFee)-parseFloat(returnShipping);
					if(setPayValue>0){
						this.getForm().findField("paySet"+(i+1)).setValue(setPayValue.toFixed(2));
						returnShipping = 0;
					}else {
						this.getForm().findField("paySet"+(i+1)).setValue(0);
						returnShipping = returnShipping - parseFloat(orderPays[i].payFee);
					}
//				this.getForm().findField("paySet"+(i+1)).setValue(orderPays[i].payTotalfee);
			}
		}
		//给本页面的退款总金额赋值
		this.getForm().findField("returnTotalFee").setValue(returnTotalFee);
		
		this.getForm().findField('returnBonusMoney').setValue('0');
		var storeData=[['0',0]];
		//红包
		if(returnAccount.returnBonusMoney&&parseFloat(returnAccount.returnBonusMoney)!=0){//原订单的红包总额
			storeData.push(['1',returnAccount.returnBonusMoney]);
		if(returnBonusMoney!=0&&returnBonusMoney!=returnAccount.returnBonusMoney){
			storeData.push(['2',returnBonusMoney]);
			this.getForm().findField('returnBonusMoney').getStore().removeAll();
			this.getForm().findField('returnBonusMoney').getStore().loadData(storeData);
			this.getForm().findField('returnBonusMoney').setValue(2);
		}else{
			this.getForm().findField('returnBonusMoney').getStore().removeAll();
			this.getForm().findField('returnBonusMoney').getStore().loadData(storeData);
			if(returnSn == '' && returnType == '4'){
				this.getForm().findField('returnBonusMoney').setValue(0);
			}else{
				this.getForm().findField('returnBonusMoney').setValue(1);				
			}
		}
	  }
	  this.payTotalSet();
	 /* //配送费用
	  this.getForm().findField('returnShipping').setValue(0);
	  var shipData=[['0',0]];
	  if(returnAccount.returnShipping&&parseFloat(returnAccount.returnShipping)!=0){
	  		shipData.push(['1',returnAccount.returnShipping]);
	  		this.getForm().findField('returnShipping').getStore().removeAll();
			this.getForm().findField('returnShipping').getStore().loadData(shipData);
			this.getForm().findField('returnShipping').setValue(1);
	  }*/
	},
	/*initReturnPayment:function(returnRefunds,returnAccount){//退单详情过来数据联动
		var len = returnRefunds.length;
		for (var i = 0; i < len; i++) {
			if(i==0){
				this.down('#paySetFieldContainer').add(
					{xtype : "displayfield", width:400},
					{xtype : "displayfield", id:'paySet'+(i+1)+'', fieldLabel :"="+orderPays[i].payName,value:orderPays[i].payTotalfee}
				);
			}else if (i > 0) {
				this.down('#paySetFieldContainer').add(
					{xtype : "displayfield", id:'paySet'+(i+1)+'', fieldLabel :"+"+ orderPays[i].payName,value:orderPays[i].payTotalfee}
				);
				this.doLayout();
			}
			returnTotalFee += orderPays[i].payTotalfee;
		}
		//给本页面的退款总金额赋值
		this.getForm().findField("returnTotalFee").setValue(returnAccount.returnTotalFee);
	},*/
	paySet:function(returnGoods){
		var len=returnGoods.length;
		var returnGoodsMoney=0,
			totalPriceDifference=0,
			returnBonusMoney = 0,
			totalIntegralMoney = 0,
			shareSettle=0;
		for(var i=0;i<len;i++){
			returnGoodsMoney+=returnGoods[i].goodsPrice*(returnGoods[i].canReturnCount);
			totalPriceDifference+=returnGoods[i].priceDifferNum*returnGoods[i].priceDifference
			shareSettle+=returnGoods[i].shareSettle*returnGoods[i].canReturnCount;
			totalIntegralMoney += returnGoods[i].integralMoney*(returnGoods[i].canReturnCount);
			if(returnGoods[i].shareBonus)
			returnBonusMoney+=returnGoods[i].shareBonus*(returnGoods[i].goodsBuyNumber-returnGoods[i].shopReturnCount-returnGoods[i].havedReturnCount);
		}
		if(returnType==1||returnType==2){//退货单
//			console.dir(returnGoodsMoney);
			this.getForm().findField('returnGoodsMoney').setValue(returnGoodsMoney.toFixed(2));
		}else if(returnType==3){//普通退款单shareSettle
			this.getForm().findField('returnGoodsMoney').setValue(shareSettle.toFixed(2));
		}else if(returnType==4){//退款单shareSettle
			this.getForm().findField('totalPriceDifference').setValue(totalPriceDifference.toFixed(2));
		}
		this.getForm().findField('totalIntegralMoney').setValue(totalIntegralMoney.toFixed(2));
//		console.dir(returnBonusMoney);
		
		if(returnSn == '' && returnType == '1'){
			if(returnBonusMoney!=0){
				var storeData=[];
				this.getForm().findField('returnBonusMoney').getStore().each(function(record,index){
					storeData.push([record.get('id'),record.get('name')]);
				});
				var oldValue=this.getForm().findField('returnBonusMoney').getValue();
				if(oldValue!=returnBonusMoney){
					storeData.push(['3',returnBonusMoney.toFixed(2)]);
					this.getForm().findField('returnBonusMoney').getStore().removeAll();
					this.getForm().findField('returnBonusMoney').getStore().loadData(storeData);
					this.getForm().findField('returnBonusMoney').setValue(3);
				}
			}else{
					this.getForm().findField('returnBonusMoney').setValue(0);
				}
		}
		
		this.payTotalSet();
	},
	payTotalSet:function(){//动态给退款总金额赋值
		var returnTotalFee=this.formatNum("returnTotalFee");
		
		var returnGoodsMoney=this.formatNum("returnGoodsMoney");
		var totalPriceDifference=this.formatNum("totalPriceDifference");
		var totalIntegralMoney = this.formatNum("totalIntegralMoney");
		var returnShipping=this.getForm().findField("returnShipping").getRawValue();
		if(returnShipping){
			returnShipping=parseFloat(returnShipping);
		}else {
			returnShipping=0;
		}
		var returnOtherMoney=this.formatNum("returnOtherMoney");
		var returnBonusMoney=this.getForm().findField("returnBonusMoney").getRawValue();
		if(returnBonusMoney){
			returnBonusMoney=parseFloat(returnBonusMoney);
		}else{
			returnBonusMoney=0;
		}
		
		returnTotalFee=(returnGoodsMoney+totalPriceDifference+returnShipping+returnOtherMoney-returnBonusMoney-totalIntegralMoney).toFixed(2);
		
		//给本页面的退款总金额赋值
		this.getForm().findField("returnTotalFee").setValue(returnTotalFee);
		if(Ext.getCmp('returnPayInfomation')){
			//给付款信息页面退款总金额赋值
			Ext.getCmp('returnPayInfomation').getForm().findField("returnTotalFee").setValue(returnTotalFee);
			
			var length = Ext.getCmp('returnPayInfomation').query('commonsystemPayment').length;
			var totalAmt = 0;
			var payMoneyArr=[];//支付方式金额调整
			if(length > 0){
				for(var i=0;i<length;i++){
					totalAmt += parseFloat(Ext.getCmp('returnPayInfomation').getForm().findField("setPaymentNum"+(i+1)).value);
					var moneyNum=Ext.getCmp('returnPayInfomation').getForm().findField("setPaymentNum"+(i+1)).getValue();
					payMoneyArr.push(moneyNum);
				}
			}
			var diffMoney = returnTotalFee - totalAmt;
			//console.log('当前付款单原金额汇总:'+totalAmt+",退单总金额新值:"+returnTotalFee+",变化值:"+diffMoney);
			/*var firstPayMoney = Ext.getCmp('returnPayInfomation').getForm().findField("setPaymentNum1").value;
			if((firstPayMoney + diffMoney)>=0){
				Ext.getCmp('returnPayInfomation').getForm().findField("setPaymentNum1").setValue(firstPayMoney + diffMoney);
			}*/
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
					Ext.getCmp('returnPayInfomation').getForm().findField("setPaymentNum"+(k+1)).setValue(0);
				}else{
					Ext.getCmp('returnPayInfomation').getForm().findField("setPaymentNum"+(k+1)).setValue(payMoneyTotal+diffMoney);
				}
			}
			
		}
		
	},
    formatNum:function(name){
    	var num=this.getForm().findField(name).getValue();
    	if(num&&parseFloat(num)){
    		return parseFloat(num);
    	}else{
    		return 0;
    	}
    }
});