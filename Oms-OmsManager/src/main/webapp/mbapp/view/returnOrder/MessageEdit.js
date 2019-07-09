Ext.define("MB.view.returnOrder.MessageEdit", {
	extend: "Ext.window.Window",
	id:'messageEdit',
	alias: "widget.messageEdit",
//	requires: ['MB.view.demo.OrderStatusCombo','MB.view.demo.ChannelInfoCombo'],
	title: "短信编辑发送界面",
	width: 750,
	height: 400,
	channelShopDatas:undefined,
	initComponent: function () {
		this.items = {
			xtype: "form",
//			margin: 5,
			border: false,
			frame: true,
			layout: "form",
			fieldDefaults: {
//				labelWidth: 300,
		        labelAlign: "right",
		        flex: 1,
		        margin:1
			},
			/*reader: new Ext.data.reader.Json({
				rootProperty : "orderInfo",
				model : 'MB.model.OrderDetailModel'
			}),*/
			items:[
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					columnWidth: 1,
					items: [
						{ xtype: "textfield", name: "mobile", fieldLabel: "短信手机号",labelWidth:150,width:'80%',value:'*** **** ****'}]
				}
				,
				/*{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{
			                fieldLabel: '退换货地址',
			                labelWidth:150,
			                xtype: 'radiogroup',
			                id:'depotRadio',
			                items: []
			            }]
				},*/
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{ xtype: "textareafield",fieldLabel: "退换货地址通知短信",name: "exchangeMsg",width:'80%',labelWidth:150,height:80 },
						{ xtype: "button", text: "发送短信",margin:'25 15' ,id:'sendExchangeMsg',
							handler:function(el){
								this.up('window').sendMsg(el.id);
							}
						}
						]
				}
				/*,
				{
					xtype: 'fieldcontainer',
					labelStyle: 'font-weight:bold;padding:0;',
					layout: 'column',
					columnWidth: 1,
					items: [
						{ xtype: "textareafield", name: "vipMsg",width:'80%',fieldLabel: "VIP邮箱通知短信",labelWidth:150,height:80 },
						{ xtype: "button", text: "发送短信",margin:'25 15',id:'sendVipMsg',
							handler:function(el){
								this.up('window').sendMsg(el.id);
							}
						}
						]
				}*/
			]
		};
		this.buttons = [
			{ text: "关闭", action: "close",handler : function () { this.up("window").close(); } }
		];
		this.callParent(arguments);
	},
	initPage:function(relOrderSn){
		//默认上海仓库
		//退换货地址通知短信
		var messageForm = this.down('form');
		var me = this;
		var json = {
				url:basePath + '/custom/orderReturn/getOrderReturnGoodsDepotList',
				params : {
					"relOrderSn" : relOrderSn
				},
				timeout:90000,
				success : function(formPanel, action) {
					if (action.result.success == "false") {
						Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
					} else {
						var channelShopList = action.result.channelShopList;
						channelShopDatas = channelShopList;
						if(channelShopList){
							me.down('form').getForm().findField('exchangeMsg').setValue(channelShopList[0].mobileMsg)
						}
						if(channelShopList.length > 0){
							for(var i = 0; i <channelShopList.length ;i++){
								var channelShop = channelShopList[i];
								var selectParam = (i == 0 ? true : false);
								Ext.ComponentQuery.query("#depotRadio")[0].add({boxLabel: channelShop.shopTitle, name: 'radio',width:100,checked:selectParam,
									listeners:{
										'change':function(){
											var obj = Ext.getCmp("depotRadio").items.items;
											for(var i in obj){
												if(obj[i].checked){
													me.showMobileMsg(obj[i].boxLabel);
												}
											}
										}}});
							}
						}
						me.down('form').doLayout();
					}
				},
				failure : function(formPanel, action) {
					if(action.result){
						Ext.msgBox.remainMsg("加载异常", action.result.errorMessage, Ext.MessageBox.ERROR);
					}
				},
				waitMsg : 'Loading...'
		};
		messageForm.submit(json);
		//vip邮箱通知短信
//		this.down('form').getForm().findField('vipMsg').setValue('您好！请您将商品进行拍照后发至邦购邮箱：banggovip@metersbonwe.com 请您将订单号为您的邮件主题以便我们能查看到您的邮件，为了能及时为您处理退换货请您发送邮件后联系客服4008219988,感谢您的配合!');
	},
	showMobileMsg: function (boxLabel) {
		if(channelShopDatas){
			for(var i=0;i<channelShopDatas.length;i++){
				if(boxLabel==channelShopDatas[i].shopTitle){
					this.down('form').getForm().findField('exchangeMsg').setValue(channelShopDatas[i].mobileMsg)
					break;
				}
			}
		}
	},
	sendMsg:function(id){
		console.dir(this.down('form').getValues());
		var win=this;
		var request = new Array();
		var params=this.down('form').getValues();
		if(id=='sendExchangeMsg'){
			request.message=params.exchangeMsg;
		}else if(id=='sendVipMsg'){
			request.message=params.vipMsg;
		}
		
		var reg=/^[1]\d{10}$/;
		if(params.mobile=='*** **** ****'){
			request.mobileNum="";
		}else if(reg.test(params.mobile)==false){
			Ext.msgBox.remainMsg("结果", '手机格式不匹配！', Ext.MessageBox.ERROR);
			return false;
		}else{
			request.mobileNum=params.mobile;			
		}
		request.returnSn=returnSn;
		var json = {
			url:basePath + '/custom/orderReturn/sendMessage',
			params :request,
			timeout:90000,
			success : function(formPanel, action) {
					win.close();
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg("结果", '', Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		this.down('form').submit(json);
	}
});
