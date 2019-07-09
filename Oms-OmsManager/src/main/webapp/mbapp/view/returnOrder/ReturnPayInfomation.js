Ext.define("MB.view.returnOrder.ReturnPayInfomation", {
	extend: "Ext.form.Panel",
	id:'returnPayInfomation',
	alias: 'widget.returnPayInfomation',
	width: '100%',
	frame: true,
	title:'退单付款信息&nbsp;&nbsp;&nbsp;',
	head:true,
	margin:10,
    bodyPadding:10,
	layout:'column',
	buttonAlign : 'center',// 按钮居中
	fieldDefaults: {
		labelAlign: 'left'
	},
	collapsible:true,
	//collapsed: true,
	initComponent: function () {
		this.items = [
		   /* {xtype : "displayfield", name : 'orderPayDesc', fieldLabel : "订单付款备注",columnWidth: .33,labelAlign: 'right',renderer: function(value) {
		    	return "<span style='color:red;'>"+value+"</span>";
			}},*/
		    {xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield", name : 'returnTotalFee', fieldLabel : "退款总金额",width:170,value:'0',labelAlign: 'right'},
				{xtype : "displayfield", width:30,value:'='},
				{xtype :'commonsystemPayment',id:'setPayment1',name:'setPayment1',fieldLabel:false,width: 150,
					listeners:{
						'change':function(field){
							this.up('form').fillPayment(field);
						}
					}
				},
				{xtype : "numberfield", name : 'setPaymentNum1', fieldLabel:false,value:'0',width:90,labelAlign: 'right',minValue:0,
					listeners:{
						'change':function(field, newvalue,oldvalue){
//							console.dir(111);
//							console.dir(this);
							this.up('form').fillPaymentNum(field);
						}
					}
				}
				/*{xtype : "displayfield", width:20,value:'&nbsp;&nbsp;+'},
				{xtype :'commonsystemPayment',id:'setPayment2',name:'setPayment2',fieldLabel:false,width: 150},
				{xtype : "numberfield", name : 'setPaymentNum2', fieldLabel : false,width:70,value:'0' },
				{xtype : "displayfield", width:20,value:'&nbsp;&nbsp;+'},
				{xtype :'commonsystemPayment',id:'setPayment3',name:'setPayment3',fieldLabel:false,width: 150},
				{xtype : "numberfield", name : 'setPaymentNum3', fieldLabel :false,width:70,value:"0"}*/
			]
		}];
		
		var me = this;
        me.callParent(arguments);
        
	},
	initPage: function(data){
		var returnPaySetModule = Ext.getCmp('returnPaySetModule');
		var returnRefunds=data.returnRefunds;
		var returnAccount=data.returnAccount;
		var returnGoods=data.returnGoods;
		var orderPays=data.orderPays;
		var returnCommon=data.returnCommon;
//		this.getForm().findField("orderPayDesc").setValue(returnAccount.orderPayDesc);
		if(returnAccount.returnTotalFee){
			returnAccount.returnTotalFee=parseFloat(returnAccount.returnTotalFee).toFixed(2);
			//给本页面的退款总金额赋值
			this.getForm().findField("returnTotalFee").setValue(returnAccount.returnTotalFee);
			//给退单付款信息的退款总金额赋值
			returnPaySetModule.getForm().findField("returnTotalFee").setValue(returnAccount.returnTotalFee);
		}
		if(returnCommon.returnSn){//判断是申请退单还是退单详情
			if(returnRefunds){
			var len=returnRefunds.length;
			if(len<=1){
				this.getForm().findField('setPaymentNum1').setReadOnly(true);
			}
			for(var i=0;i<len;i++){
				if(i>0){
					if(!this.getForm().findField('setPayment'+(i+1))){
						this.down('fieldcontainer').add(
						{xtype : "displayfield", width:20,value:'&nbsp;&nbsp;+'},
						{xtype :'commonsystemPayment',id:'setPayment'+(i+1)+'',name:'setPayment'+(i+1)+'',fieldLabel:false,width: 150,
							listeners:{
								'change':function(field){
									this.up('form').fillPayment(field);
								}
							}
						},
						{xtype : "numberfield", name : 'setPaymentNum'+(i+1)+'', fieldLabel :false,width:80,value:"0",minValue:0,
							listeners:{
								'change':function(field){
		//							console.dir(111);
		//							console.dir(this);
									this.up('form').fillPaymentNum(field);
								}
							}
						}
						);
						this.doLayout();
					}
				}
				this.down('#setPayment'+(i+1)+'').setValue(returnRefunds[i].returnPay);
				this.getForm().findField("setPaymentNum"+(i+1)).setValue(returnRefunds[i].returnFee);
				/*if(returnAccount.returnTotalFee&&returnAccount.returnTotalFee!=0){
						this.getForm().findField("setPaymentNum"+(i+1)).setMaxValue(returnAccount.returnTotalFee);
					}*/
			}
			//与退单付款信息页面数据联动
			for (var i = 0; i < len; i++) {
				if(i==0){
					if(!Ext.getCmp('paySet1')){
					returnPaySetModule.down('#paySetFieldContainer').add(
						{xtype : "displayfield", columnWidth:.305},
						{xtype : "displayfield", id:'paySet'+(i+1)+'', fieldLabel :"="+this.down('#setPayment'+(i+1)+'').getRawValue(),value:returnRefunds[i].returnFee,columnWidth:.2,labelWidth:150}
					);
					}
				}else if (i > 0) {
					if(!Ext.getCmp('paySet'+(i+1))){
					returnPaySetModule.down('#paySetFieldContainer').add(
						{xtype : "displayfield", id:'paySet'+(i+1)+'', fieldLabel :"+"+ this.down('#setPayment'+(i+1)+'').getRawValue(),value:returnRefunds[i].returnFee,columnWidth:.2,labelWidth:150}
					);
					returnPaySetModule.doLayout();
					}
				}
			}
			if(returnAccount.returnTotalFee){
				this.getForm().findField("returnTotalFee").setValue(returnAccount.returnTotalFee);
				}
			//returnPaySetModule.initReturnPayment(returnRefunds);
		}
		}else{//申请退单
			if(orderPays){
				var returnTotalFee=0;
				var surplusPay={};
				var surplusFlag=false;
				/*//排除支付方式重复
				var newPays=[];
				var flag=false;
				for(var j=0;j<orderPays.length;j++){
					for(var k=0;k<newPays.length;k++){
						if(orderPays[j].payId==newPays[k].payId){//支付方式重复
							flag=true;
						}
					}
						
					if(!flag){
						newPays.push(orderPays[j]);
					}
				}*/
				//红包
				var returnBonusMoney = 0;
				
//				orderPays=newPays;
				var len=orderPays.length;
				if(len<=1){
					this.getForm().findField('setPaymentNum1').setReadOnly(true);
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
				this.getForm().findField("returnTotalFee").setValue(returnTotalFee);
				
				//(应财务要求，默认配送费用去掉，所以这里需要减去配送费用)
				var returnShipping = 0;
				if(returnAccount.returnShipping){
					returnShipping = returnAccount.returnShipping;
				}
				
				for(var i=0;i<len;i++){
					if(i>0){
						if(!Ext.getCmp('paySet'+(i+1))){
						this.down('fieldcontainer').add(
							{xtype : "displayfield", width:20,value:'&nbsp;&nbsp;+'},
							{xtype :'commonsystemPayment',id:'setPayment'+(i+1)+'',name:'setPayment'+(i+1)+'',fieldLabel:false,width: 150,
								listeners:{
									'change':function(field){
										this.up('form').fillPayment(field);
									}
								}
							},
							{xtype : "numberfield",name : 'setPaymentNum'+(i+1)+'',fieldLabel :false,width:80,value:"0",minValue:0,
								listeners:{
									'change':function(field){
										this.up('form').fillPaymentNum(field);
									}
								}
							}
						);
						this.doLayout();
					}}
					this.down('#setPayment'+(i+1)+'').setValue(orderPays[i].pId);
					var setPayValue = parseFloat(orderPays[i].payFee)-parseFloat(returnShipping);
					if(setPayValue>0){
						this.getForm().findField("setPaymentNum"+(i+1)).setValue(setPayValue);
						returnShipping = 0;
					}else {
						this.getForm().findField("setPaymentNum"+(i+1)).setValue(0);
						returnShipping = returnShipping - parseFloat(orderPays[i].payFee);
					}
					
				}
			
				if(returnTotalFee&&returnTotalFee!=0){
					returnTotalFee=returnTotalFee.toFixed(2);
				}
				
				//关联退单付款信息
				returnPaySetModule.initPayment(orderPays,returnTotalFee,returnBonusMoney,returnAccount);
			
			}
		}
		
	},
	fillPayment:function(field){//支付方式与退单付款信息页面支付方式联动
		var i=parseInt(field.name.split('setPayment')[1]);
		var rawData=null;
		if(Ext.getCmp('returnCenter')){
			rawData=Ext.getCmp('returnCenter').getForm().reader.rawData;
		}else if(Ext.getCmp('returnForm')){
			rawData=Ext.getCmp('returnForm').getForm().reader.rawData;
		}
//		console.dir(rawData);
		/**
		 * 支付方式校验
		 */
		var thisValue=parseFloat(field.getValue());
		if(!returnSn){//申请退单
			var orderPays=rawData.orderPays;
			var len=orderPays.length;
			for(var j=1;j<=len;j++){
				if(j!=i){
					if(Ext.getCmp('setPayment'+j)){
						var otherValue=parseFloat(Ext.getCmp('setPayment'+j).getValue());
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
		}
		/**
		 * 业务逻辑：退单详情展示界面不可编辑，所以不需要支付方式校验
		 */
		/*else{//退单详情
			var returnRefunds=rawData.returnRefunds;
			var len=returnRefunds.length;
			for(var j=1;j<=len;j++){
				if(j!=i){
					if(Ext.getCmp('setPayment'+j)){
						var otherValue=parseFloat(Ext.getCmp('setPayment'+j).getValue());
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
		}*/
//		console.dir(field.name.split('setPayment'));
		if(Ext.getCmp('returnPaySetModule').getForm().findField('paySet'+i)){
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
		//原始退款总金额
		var returnTotalFee=this.up('form').getForm().findField("returnTotalFee").getValue();
//		console.dir(this.down('fieldcontainer'));
		var changeTotalFee=0;
		var paySetNum=(this.down('fieldcontainer').items.length-1)/3;//获取有多少个支付方式
		for(var i=1;i<=paySetNum;i++){
			changeTotalFee+=this.up('form').getForm().findField('setPaymentNum'+i).getValue();
		}
		var k=parseInt(field.name.split('setPaymentNum')[1]);
		if(field.name==('setPaymentNum'+k)){
//				console.dir(Ext.getCmp('returnPaySetModule'));
//				 Ext.getCmp('returnPaySetModule')
				if(Ext.getCmp('returnPaySetModule').getForm().findField('paySet'+k)){
					Ext.getCmp('returnPaySetModule').getForm().findField('paySet'+k).setValue(parseFloat(field.getValue()).toFixed(2));
				}
//				break;
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
			var lastNum=this.up('form').getForm().findField("setPaymentNum1").getValue();
			this.up('form').getForm().findField("setPaymentNum1").setValue(lastNum+changeNum);
			
		}else{
//			console.dir(this.up('form'));
			if(this.up('form').getForm().findField("setPaymentNum"+(k+1))){
//			console.dir(this.up('form').getForm().findField("setPaymentNum"+(k+1)));
				var lastNum=this.up('form').getForm().findField("setPaymentNum"+(k+1)).getValue();
				if((lastNum+changeNum)<=0){//当下一个支付方式的值小与需要变化的值时
					this.up('form').getForm().findField("setPaymentNum"+(k+1)).setValue(0);
					
				}else{
					changePaymentNumFlag=false;
					this.up('form').getForm().findField("setPaymentNum"+(k+1)).setValue(lastNum+changeNum);
				}
				return false;
			}
			
		
		}
		
		}
		
	},
    onCloseClick: function () {
        if (this.ownerCt) {
            this.ownerCt.remove(this, true);
        }
    }
});

var changePaymentNumFlag=true;