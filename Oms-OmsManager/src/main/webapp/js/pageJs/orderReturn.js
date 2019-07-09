Ext.Loader.setConfig({enabled: true});
//Ext.Loader.setPath('Ext.ux', 'ext4.2/ux');
// Ext.Loader.setPath('Ext.ux', basePath+'/ext4/ux');

Ext.require([
         'Ext.grid.*',
         'Ext.data.*',
         'Ext.selection.CheckboxModel',
         'MB.ComboModel',
         'MB.Channel',
         'MB.Shop',
         'MB.WareHouse',
         'MB.OrderReturnQuery'
]);

var orderReturnFormPanel = null;
var pageSize=20;
var columns = null;
var orderReturngridPanel = null;
var orderReturnJsonStore = null;

var orderViewOptionCombo = null;

var isGoodReceivedOptionCombo =null;

var checkinStatusOptionCombo = null;

var qualityStatusOptionCombo = null;
var returnOrderStatusOptionCombo = null;
var isHistory= 0;

var returnTypeOptionCombo = null;

var returnPayOptionCombo = null;

var returnPayStatusOptionCombo = null;

var settleType = $("#settleType").val()

if("orderReturnSettleList" == settleType ){//待订单结算	
	//var orderInfoListUrl = basePath+"/custom/orderInfo/orderInfoList.spmvc?type=orderInfoSettle";
	var queryUrl = basePath+"/custom/orderReturn/orderReturnList.spmvc?type=settle";
} else{
	//var orderInfoListUrl = basePath+"/custom/orderInfo/orderInfoList.spmvc";
	var queryUrl = basePath+"/custom/orderReturn/orderReturnList.spmvc";
}

Ext.onReady(function() {
	Ext.QuickTips.init();
	
//	var queryUrl = basePath+"/custom/orderReturn/orderReturnList.spmvc?orderView=0&isGoodReceived=1&checkinStatus=1&qualityStatus=1&orderStatus=1&payStatus=1";
	

	// 将创建组建过程封装   data 需要自己创建
	//订单类型
	var relatingOrderTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.order_type
	});
	var relatingOrderTypeOptionCombo = createComboBoxLocal(relatingOrderTypeDataStore,"orderReturnPageRelatingOrderType", 'n','v', 'local','订单类型', 220, 80, 'relatingOrderType', '请选择订单类型');

	//回退客服
	var backToCsDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.back_to_cs
	});
	var backToCsOptionCombo = createComboBoxLocal(backToCsDataStore,"orderReturnPageBackToCs", 'n','v', 'local','回退客服', 220, 80, 'backToCs', '请选择');

	var orderViewDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.order_view
	});
	orderViewOptionCombo= createComboBoxLocal(orderViewDataStore, "orderReturnPageOrderView", 'n','v', "local", "显示类型", 220, 80, "orderView","请选择");
	//默认显示类型：1,显示有效订单
	orderViewOptionCombo.setValue('0');
	
	//referer
	var refererDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.referer_type
	});
	var refererOptionCombo= createComboBoxLocal(refererDataStore, "orderReturnPageReferer", 'n','v', "local", "referer", 220, 80, "referer","请选择订单来源媒体");
	
	//时间类型查询
	var timeDataStore=new Ext.data.SimpleStore( {
		data :[["addTime","生成时间"],
		       ["checkinTime","入库时间"],
		       ["clearTime","结算时间"],
		       ["confirm_time","确定时间"]],
		fields : [ 'text', 'filed' ],
	});
	var timeOptionCombo=createComboBoxLocal(timeDataStore, "orderReturnPageSelectTimeType", 'filed','text', "local", "时间类型", 220, 80, "selectTimeType","请选择时间类型");
	timeOptionCombo.setValue("addTime");
	//退单状态
	var returnOrderStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.return_order_status
	});
	 returnOrderStatusOptionCombo = createComboBoxLocal(returnOrderStatusDataStore, "orderReturnPageReturnOrderStatus", 'n','v', "local", "退单状态", 220, 80, "returnOrderStatus","请选择退单状态");
	
	//财务状态
	var returnPayStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.pay_status_2
	});
	 returnPayStatusOptionCombo = createComboBoxLocal(returnPayStatusDataStore, "orderReturnPageReturnPayStatus", 'n','v', "local", "财务状态", 220, 80, "payStatus","请选择财务状态");
	
	//物流状态
/*	var returnShipStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.ship_status_2
	});*/
	//var returnShipStatusOptionCombo = createComboBoxLocal(returnShipStatusDataStore, "orderReturnPageReturnShipStatus", 'n','v', "local", "物流状态", 220, 80, "shipStatus","请选择物流状态");

	//订单状态
	var  orderStatusStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.order_status
	});
	
	var orderStatusOptionCombo = createComboBoxLocal(orderStatusStore, "orderReturnPageOrderOrderStatus", 'n','v', "local", "订单状态", 220, 80, "orderOrderStatus","请选择订单状态");
	
	//订单财务状态
	var payStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.pay_status
	});
	var payStatusOptionCombo = createComboBoxLocal(payStatusDataStore, "orderReturnPageOrderPayStatus", 'n','v', "local", "支付状态", 220, 80, "orderPayStatus","请选择支付状态");
	
	//订单物流状态
	var shipStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.ship_status
	});
	var shipStatusOptionCombo= createComboBoxLocal(shipStatusDataStore, "orderReturnPageOrderShipStatus", 'n','v', "local", "发货状态", 220, 80, "orderShipStatus","请选择发货状态");
	
	//一级订单来源
	var channelTypeStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.channel_type
	});
	var changeChannelType = function (combo, record,index) {
	//	channelCombo.clearValue();
		channelCombo.setValue('');
			//改变二级菜单
			if(combo.value==null) {
				channelDataStore.getProxy().url=basePath+'custom/common/getChannelInfos?channelType=0';
			}else {
				channelDataStore.getProxy().url=basePath+'custom/common/getChannelInfos?channelType='+combo.value;
			}
			channelDataStore.load();
	};
	var channelTypeCombo = createComboBoxLocal(channelTypeStore,'orderReturnPageOrderFromFirst', 'n','v', 'local','订单来源', 220, 80, 'orderFromFirst', '请选择渠道类型');
	// 添加change监听事件
	channelTypeCombo.addListener('change',changeChannelType);
	
	//关联订单来源2  ajax 加载
	var channelDataStore= new Ext.data.Store({
		model:"MB.Channel",
		proxy: {
			type: 'ajax',
			url:basePath+'custom/common/getChannelInfos.spmvc?channelType=0', //这里是参数可以顺便写,这个数据源是在第一个下拉框select的时候load的
			reader:  {type:'json'}
		}
	});
	var changeChannelCode = function (combo, record, index) {
	//	shopCombo.clearValue();
		shopCombo.setValue();
		if(combo.value==null) {
			shopDataStore.getProxy().url=basePath+"custom/common/getChannelShops";
		}else {
			shopDataStore.getProxy().url=basePath+"custom/common/getChannelShops?channelCode="+ combo.value;
		}
		shopDataStore.load();
	};
	var channelCombo = createComboBoxLocal(channelDataStore,"OrderReturnPageOrderFromSec",'channelTitle','chanelCode',"remote", "渠道", 220, 80, "orderFromSec","请选择渠道");
	channelCombo.addListener('change',changeChannelCode);
	
	var shopDataStore= new Ext.data.Store({
		model:"MB.Shop",
		proxy:{
			type: 'ajax',
			url:basePath+'custom/common/getChannelShops',
			reader: {
				type:'json'
			}
		}
	});
	var shopCombo = createComboBoxLocalByMult(shopDataStore,"orderReturnPageOrderFrom",'shopTitle','shopCode',"remote", "渠道店铺",  330, 80, "orderFroms","请选择渠道店铺");

	var returnPayDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.return_pay
	});
	returnPayOptionCombo = createComboBoxLocal(returnPayDataStore, "orderReturnPageReturnPay", 'n','v', "local", "退款方式", 220, 80, "returnPay","请选择退款方式");
	
	var processTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.process_type
	});
	var processTypeOptionCombo= createComboBoxLocal(processTypeDataStore, "orderReturnPageProcessType", 'n','v', "local", "处理方式", 220,80, "processType","请选择处理方式");
	
	//退单类型
	var returnTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.return_type
	});
	 returnTypeOptionCombo= createComboBoxLocal(returnTypeDataStore, "orderReturnPageReturnType", 'n','v', "local", "退单类型", 220, 80, "returnType","请选择退单类型");

	//退款类型
	var returnSettlementTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.return_settlement_type
	});
	var returnSettlementTypeOptionCombo= createComboBoxLocal(returnSettlementTypeDataStore, "orderReturnPageReturnSettlementType", 'n','v', "local", "退款类型", 220, 80, "returnSettlementType","请选择退款类型");
	
	//退单原因
	var returnReasonDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.return_reason
	});
	var returnReasonOptionCombo= createComboBoxLocal(returnReasonDataStore, "orderReturnPageReturnReason", 'n','v', "local", "退单原因", 220, 80, "returnReason","请选择退单原因");

//	`is_good_received` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否收到货 （0 否  1 是）',
//	  `checkin_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否入库 （0未入库 1已入库 2待入库）',
//	  `quality_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '退单质检状态 （0质检不通过、1质检通过）',
	
	var qualityStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.qualityStatus
	});

	var isShowStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.is_show_2
	});
	
	var checkinStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.checkin_status
	});
	
	//是否收到货
	 isGoodReceivedOptionCombo = createComboBoxLocal(isShowStatusDataStore, "orderReturnPageIsGoodReceived", 'n','v', "local", "是否收到货", 220, 80, "isGoodReceived","请选择");

	//是否入库
	checkinStatusOptionCombo = createComboBoxLocal(checkinStatusDataStore, "orderReturnPageCheckinStatus", 'n','v', "local", "是否入库 ", 220, 80, "checkinStatus","请选择");

	//质检状态
	 qualityStatusOptionCombo = createComboBoxLocal(qualityStatusDataStore, "orderReturnPageQualityStatus", 'n','v', "local", /*"退单"*/"质检状态", 220, 80, "qualityStatus","请选择");

	
	var warehouseListSourceDataStore= new Ext.data.Store({
		model:"MB.WareHouse",
		proxy:{
			type: 'ajax',
			url:basePath+'custom/orderReturn/getWarehouseList.spmvc',
			reader: {
				type:'json'
			}
		}
	});
	var warehouseListOptionCombo = createComboBoxLocal(warehouseListSourceDataStore,"orderReturnPageWarehouseCode",'warehouseName','warehouseCode', 'remote','退货入库仓', 230, 80, 'warehouseCode', '请选择仓库');

	//是否退款
	var haveRefundDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.haveRefund
	});
	haveRefundOptionCombo = createComboBoxLocal(haveRefundDataStore, "orderReturnPageHaveRefund", 'n','v', "local", "是否退款", 220, 80, "haveRefund","请选择");

	orderReturnFormPanel = Ext.widget('form', {
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		frame: true,
		collapsible: true,
		title: "退单列表",
		fieldDefaults: {
			labelAlign: 'right'//,
		//	labelWidth: 100
			//labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [{// line 1
					width : 220, // 该列有整行中所占百分比
					layout : "form", // 从上往下的布局
					xtype : "textfield",
					id:'orderReturnPageReturnSn',
					name: 'returnSn',
					fieldLabel : "退单号",
					labelWidth: 80
				} , {
					width : 220,
					layout : "form",
					xtype : 'textfield',
					fieldLabel : '订单号',
					id:'orderReturnPageRelatingOrderSn',
					name: 'relatingOrderSn',
					labelWidth: 80
				} , {
					width : 220,
					labelWidth: 80,
					layout : "form",
					xtype : 'textfield',
					fieldLabel : '外部交易号',
					id:'orderReturnPageOrderOutSn',
					name: 'orderOutSn'
				} , {
					width : 220,
					layout : "form", 
					xtype : "textfield",
					id:'orderReturnPageReturnInvoiceNo',
					name: 'returnInvoiceNo',
					fieldLabel : "退货快递单号",
					labelWidth: 110
				} /*, {
					id : 'orderReturnPageListDataType',
					width : 250,
					xtype: 'radiogroup',
					name:'listDataType',
					items: [
						{boxLabel: '近三个月数据', name: 'listDataType', inputValue:'newDate', checked: true},
						{boxLabel: '历史数据', name: 'listDataType', inputValue:'historyDate'},
					],
					listeners: {
						'change' : function (v , a) {
							if(a.listDataType=="newDate") {
								isHistory = 0;
							} else {
								isHistory = 1;
							}
						}
					}
				}*/
			]
		}, { //line 2
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [channelTypeCombo,
					channelCombo,
					shopCombo,
					//refererOptionCombo
					]
		}, { //line 3
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [
				returnOrderStatusOptionCombo,
				returnPayStatusOptionCombo,
				isGoodReceivedOptionCombo,
				checkinStatusOptionCombo
		//		returnShipStatusOptionCombo,
			//	returnPayOptionCombo
			]
		}, {  //line 4
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [
				orderStatusOptionCombo,
				payStatusOptionCombo,
				shipStatusOptionCombo,
				{
					width : 220, // 该列有整行中所占百分比
					layout : "form", // 从上往下的布局
					xtype : "textfield",
					id:'orderReturnPageReturnMobile',
					name: 'returnMobile',
					fieldLabel : "手机号",
					labelWidth: 80	
				}//,
				//orderViewOptionCombo
			]
		}, {//line 5
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [
				returnTypeOptionCombo,
				returnSettlementTypeOptionCombo,
				relatingOrderTypeOptionCombo ,
				backToCsOptionCombo/*,{
					width : 260,
					xtype : "textfield",
					id:'OrderReturnPageSkuSn',
					name: 'skuSn',
					fieldLabel : "11位码或6位码",
					labelWidth: 110
				}*/
				/*{
					width : 200,
					xtype : "textfield",
					id:'OrderReturnPage.jsfs',
					name: 'jsfs',
					fieldLabel : "结算方式",
					labelWidth: 60
				}*/
			]
		} , {//line 6
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [
					returnReasonOptionCombo,
					processTypeOptionCombo,
					{
						width : 220,
						layout : "form", 
						xtype : "textfield",
						id:'orderReturnPageUserName',
						name: 'userName',
						fieldLabel : "下单人",
						labelWidth: 80
					} , {
						width : 120,
						layout : "form", 
						xtype : "textfield",
						id:'orderReturnPageStReturnTotalFee',
						name: 'stReturnTotalFee',
						fieldLabel : "退款金额",
						labelWidth: 80
					} , {
						width : 100,
						layout : "form", 
						xtype : "textfield",
						id:'orderReturnPageEnReturnTotalFee',
						name: 'enReturnTotalFee',
						fieldLabel : "至",
						labelWidth: 30
					}
				]
		} , {
			//line 7
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [
				timeOptionCombo , 
				{
					xtype: 'textfield',
					id:"orderReturnPageStartTime",
					width : 220,
					name: 'startTime',
					fieldLabel: '开始时间',
					labelWidth: 80,
					listeners:{
						render:function(p){
							p.getEl().on('click',function(){
							    //WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
								WdatePicker({startDate:'%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'});
							});
						}
					}
				} , {
					xtype: 'textfield',
					id:"orderReturnPageEndTime",
					width : 220,
					name: 'endTime',
					fieldLabel: '结束时间',
					labelWidth: 80,
					listeners:{
						render:function(p){
							p.getEl().on('click',function(){
								//WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
								WdatePicker({startDate:'%y-%M-%d 23:59:59',dateFmt:'yyyy-MM-dd HH:mm:ss'});
							});
						}
					}
				}, 
				warehouseListOptionCombo,
				{
					xtype:'checkboxfield',
					fieldLabel:'导出结果不包含商品信息',
					name:'noOrderGoods',
					inputValue:'1',//将checkboxfield传递到后台的值定义为1
					labelWidth: 170,
					labelAlign: 'right'
				}
			]
		}, {//line 8
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [
			       refererOptionCombo,
			       orderViewOptionCombo,
			       {
						width : 260,
						xtype : "textfield",
						id:'OrderReturnPageSkuSn',
						name: 'skuSn',
						fieldLabel : "11位码或6位码",
						labelWidth: 110
					}, 
				//	consignee
					{
						width : 260,
						xtype : "textfield",
						id:'OrderReturnConsignee',
						name: 'consignee',
						fieldLabel : "收件人",
						labelWidth: 110
					}
					,
					{
						id : 'orderReturnPageListDataType',
						width : 250,
						xtype: 'radiogroup',
						name:'listDataType',
						items: [
							{boxLabel: '近三个月数据', name: 'listDataType', inputValue:'newDate', checked: true},
							{boxLabel: '历史数据', name: 'listDataType', inputValue:'historyDate'},
						],
						listeners: {
							'change' : function (v , a) {
								if(a.listDataType=="newDate") {
									isHistory = 0;
								} else {
									isHistory = 1;
								}
							}
						}
					}
	
				]
		} , {//line 9
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [
				returnPayOptionCombo,
				//isGoodReceivedOptionCombo,
				//checkinStatusOptionCombo,
				qualityStatusOptionCombo,
				haveRefundOptionCombo
			    /*   refererOptionCombo,
			       orderViewOptionCombo,
			       {
						width : 260,
						xtype : "textfield",
						id:'OrderReturnPageSkuSn',
						name: 'skuSn',
						fieldLabel : "11位码或6位码",
						labelWidth: 110
					}, {
						id : 'orderReturnPageListDataType',
						width : 250,
						xtype: 'radiogroup',
						name:'listDataType',
						items: [
							{boxLabel: '近三个月数据', name: 'listDataType', inputValue:'newDate', checked: true},
							{boxLabel: '历史数据', name: 'listDataType', inputValue:'historyDate'},
						],
						listeners: {
							'change' : function (v , a) {
								if(a.listDataType=="newDate") {
									isHistory = 0;
								} else {
									isHistory = 1;
								}
							}
						}
					}*/
	
				]
		} 
		
		
		],
		buttons : [{
			text : '查询',
			columnWidth : .1,
			handler : search
		} , {
			text : '重置',
			handler : function () {
				resetButton(orderReturnFormPanel);
				Ext.getDom("orderReturnPageStartTime").value="";
				Ext.getDom("orderReturnPageEndTime").value="";
				returnOrderStatusOptionCombo.setValue(-1);
				channelTypeCombo.setValue(0);
				channelCombo.setValue('');
				shopCombo.setValue('');
				refererOptionCombo.setValue(-1);
				//returnShipStatusOptionCombo.setValue(-1);  物流状态
				returnPayOptionCombo.setValue(-1);
				processTypeOptionCombo.setValue(-1);
				returnTypeOptionCombo.setValue(-1);
				returnPayStatusOptionCombo.setValue(-1);
				returnSettlementTypeOptionCombo.setValue(-1);
				returnReasonOptionCombo.setValue(-1);
				relatingOrderTypeOptionCombo.setValue(-1);
				timeOptionCombo.setValue("addTime");
				orderStatusOptionCombo.setValue(-1);
				shipStatusOptionCombo.setValue(-1);
				payStatusOptionCombo.setValue(-1);
				orderViewOptionCombo.setValue('0');
				warehouseListOptionCombo.setValue(-1);
				haveRefundOptionCombo.setValue(-1);
				
			}
		}, {
			id: "orderReturnExportBtn",
			text : '导出',
			//handler : exportRecord
			menu:
			{
	    	    items: [{
	    	                text: '默认模版',
	    	                handler: function () {
	    	                	exportAjax("0");
	    	                }
    	                }, {
    	                text: '财务模版',
	    	                handler: function () {
	    	                	exportAjax("1");
	    	                }
    	        },  {
	                text: '物流模版',
	                handler: function () {           
	                	exportAjax("2");     	
	                }	
	            }
    	                
    	                ]    	
		    }			
		},{
			id:"orderReturnSellteExportBtn",
			text : '待结算退单导出',
			columnWidth : .1,
			hidden:true,
			handler : exportOrderReturnSettle
		}
		],
		buttonAlign: 'right',
		listeners: {
			afterRender: function(thisForm, options){  
				this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
					enter: function(){
						search();
					}
				});
			},
			resize: function(){
				setResize(orderReturnFormPanel,orderReturngridPanel, null, 5);
			}
		}
	});

	orderReturnJsonStore = createObjectGridStoreLazy(queryUrl, pageSize, "MB.OrderReturnQuery");
	columns = [
		{ id : 'returnSn', header : "退单号", align : "left", width : 280, dataIndex : 'returnSn' , renderer: function(value, md, r) {
			var backToCs =  r.get('backToCs');
			//var backToCs = 	Ext.getCmp("OrderReturnPage.backToCs").value; //回退客服;
			var backToCsStr="";
			
			//是回退客服
			if(1==backToCs){
				//alert(backToCs);
				backToCsStr="<font color='red'><b>(回退)</b></font>";
			}
			var returnReason =  r.get('returnReason');
		//	var returnReason =  Ext.getCmp("OrderReturnPage.returnReason").value; // 退单原因;
			var returnReasonStr="";
			
			//质量问题
			if("21"==returnReason){
				//alert(returnReason);
				returnReasonStr="<font color='red'><b>(质量问题)</b></font>";
			}

			if("1" == $("#orderReturnDisplay").val()){
				if (value != undefined && value != null ) {
					var url = order_return_url + value + '&isHistory=0';
					return "<a href='"+url+"' target='_blank' >"+value+"</a>"+backToCsStr+returnReasonStr;
				}
			} else {
				return "<font>"+value+"</font>"+backToCsStr+returnReasonStr;
			}
		}
		},
		{ id : 'relatingOrderSn', header : "关联订单号", align : "center", width : 120, dataIndex : 'relatingOrderSn', renderer: function(value) {
			if (value != undefined && value != null ) {
				var url = order_info_url + value +"&isHistory=" + isHistory;
				return "<a href=" +url + " target='_blank' >" + value + "</a>";
			}
		}
		},
		{ id : 'orderOutSn', header : "关联外部交易号", align : "center", width : 200, dataIndex : 'orderOutSn', renderer: function(value, md, r) {
			if (value != undefined && value != null ) {
				var orderSn =  r.get('relatingOrderSn');
				var url = order_info_url + orderSn +"&isHistory=" + isHistory;
				return "<a href=" +url + " target='_blank' >" + value + "</a>";
			}
		}},
		{ id : 'channelName', header : "订单来源", align : "center", width : 200, dataIndex : 'channelName' },
		{ id : 'referer', header : "referer", align : "center", width : 200, dataIndex : 'referer'},
		{ id : 'returnTypeStr', header : "退单类型", align : "center", width : 90, dataIndex : 'returnTypeStr' },
		{ id : 'returnStatus',header : "退单状态", align : "center", sortable : true,  width : 180, renderer: editReturnStatus},
		{ id : 'orderStatus',header : "订单状态", align : "center", sortable : true,  width : 180, renderer: editOrderStatus},
		{ id : 'returnPayStr',header : "退款方式", align : "center", sortable : true,  width : 160, dataIndex : 'returnPayStr' },
		{ id : 'addTime', header : "退单时间", align : "center", width : 160, dataIndex : 'addTime',sortable:true},
		{ id : 'checkinTime', header : "入库时间", align : "center", width : 160, dataIndex : 'checkinTime' ,sortable:true},	
		{ id : 'clearTime', header : "结算时间", align : "center", width : 160, dataIndex : 'clearTime' ,sortable:true},
		
	//	{ id : 'consignee', header : "收货人", align : "center", width : 160, dataIndex : 'consignee'},
		{ id : 'userName', header : "下单人", align : "center", width : 100, dataIndex : 'userName'},
	//	{ id : 'returnShippingStatusStr', header : "入库状态", align : "center", width : 90, dataIndex : 'returnShippingStatusStr'},
		
		{ id : 'returnGoodsCount', header : "退货数量", align : "center", width : 120, dataIndex : 'returnGoodsCount'},
		{ id : 'returnTotalFee', header : "退款金额", 	align : "center", width : 120, dataIndex : 'returnTotalFee'},
		
		{ id : 'returnShipping', header : "邮费", 	align : "center", width : 120, dataIndex : 'returnShipping'},  //order_refund
		{ id : 'returnGoodsMoney', header : "退商品金额", align : "center", width : 120, dataIndex : 'returnGoodsMoney'}, //order_refund		
		{ id : 'returnInvoiceNo', header : "退货快递单号", align : "center", width : 120, dataIndex : 'returnInvoiceNo'}, //order_return_ship
		 
		{ id : 'returnReasonStr', header : "退单原因", align : "center", width : 250, dataIndex : 'returnReasonStr'},
		
		{ id : 'haveRefund', header : "是否退款", align : "center", width : 250, renderer: editHaveRefund},
		
		{ id : 'consignee', header : "收件人", align : "center", width : 250, dataIndex : 'consignee'},

	//	{ id : 'totalFee', header : "成交价格", align : "center", width : 180, dataIndex : 'totalFee'},
	//	{ id : 'goodsAmount', header : "财务价格", align : "center", width : 180, dataIndex : 'goodsAmount'},
	//	{ id : 'bonus', header : "红包金额 ", align : "center", width : 180, dataIndex : 'bonus'},
		{ id : 'returnOtherMoney', header : "退其他费用", align : "center", width : 180, dataIndex : 'returnOtherMoney'},
		{ id : 'warehouseName', header : "退货入库仓", align : "center", width : 180, dataIndex : 'warehouseName'},

		{ id : 'integralMoney', header : "使用积分金额", align : "left", width : 200, dataIndex : 'integralMoney',sortable:false}
	];

	var selModel = Ext.create('Ext.selection.CheckboxModel');
	orderReturngridPanel = createGridPanelByExt4('orderReturnQuery', null, null, orderReturnJsonStore,
			pageSize, null, columns, null, null);
	
	var orderReturnCouponPanel = Ext.widget('panel', {
		renderTo: Ext.getBody(),
		//title: "退单列表",
		width: '100%',
		//bodyPadding: 2,
		resizable: false,
		autoHeight: true,
		items: [orderReturnFormPanel, orderReturngridPanel]
	});

	//查询翻页后带入查询数据
	orderReturnJsonStore.on('beforeload', function (store, options){
		Ext.apply(store.proxy.extraParams, getFormParams(orderReturnFormPanel));
	});

	channelDataStore.on('load', function (store, records, successful, options) {
		if (successful && Ext.typeOf(channelCombo.getPicker().loadMask) !== "boolean") {
			channelCombo.getPicker().loadMask.hide();
		}
	});

	shopDataStore.on('load', function (store, records, successful, options) {
		if (successful && Ext.typeOf(shopCombo.getPicker().loadMask) !== "boolean") {
			shopCombo.getPicker().loadMask.hide();
		}
	});

	function search(){
		var searchParams = {start : 0, limit : pageSize };
		orderReturngridPanel.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		orderReturngridPanel.store.load({params : getFormParams(orderReturnFormPanel, searchParams)});
	}
	
	function exportRecord() {// 导出数据
		var myMask = new Ext.LoadMask(Ext.getBody(),{msg:"请稍等,正在导出..."});
		myMask.show(); 
		Ext.Ajax.request({
			timeout: 1800000,//1800秒 半小时
			url: basePath+"custom/orderReturn/orderReturnExportCsvFile.spmvc",
			params: getFormParams(orderReturnFormPanel),
			success: function(response){
				if (myMask != undefined){ myMask.hide();}
				var obj = Ext.decode(response.responseText);
				var path = obj.data.path;
				var fileName = obj.data.fileName;
				if(obj.isok==true){
					window.location.href = basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
				} else {
					Ext.Msg.alert("错误", "导出失败");
				}
			},
			failure:function(){
				if (myMask != undefined){ myMask.hide();}
				Ext.Msg.alert("错误", "导出失败");
			}
		});
	}
	function editOrderStatus(value, cellmeta, record) {
		var orderStatus = record.get('orderOrderStatus');
		var payStatus = record.get('orderPayStatus');
		var shipStatus = record.get('orderShipStatus');
		return getCombineStatus(orderStatus, payStatus, shipStatus);
	}
	
	function editHaveRefund(value, cellmeta, record) {
		var haveRefund = record.get('haveRefund');
	//	var payStatus = record.get('orderPayStatus');
	//	var shipStatus = record.get('orderShipStatus');
		return getHaveRefundStatus(haveRefund);
		       
	}
	
	//退单状态
	function editReturnStatus(value, cellmeta, record) {
		var orderStatus = record.get('returnOrderStatus');
		var payStatus = record.get('payStatus');
		
	//	var checkinStatus  = 0;
		
	//	var isGoodReceived = 0;
		
	//	var qualityStatus = 0;
		

		var checkinStatus = record.get('checkinStatus'); // '是否入库 （0未入库 1已入库 2待入库）',
		
		var isGoodReceived = record.get('isGoodReceived'); //'是否收到货 （0 否  1 是）',
		
		var qualityStatus = record.get('qualityStatus'); //退单质检状态 （0质检不通过、1质检通过）',
		
		return getCombineReturnStatusNew(orderStatus, payStatus, isGoodReceived, checkinStatus, qualityStatus);
	}
	
	window.onresize=function(){
		setResize(orderReturnFormPanel,orderReturngridPanel, null, 5);
	};
	setResize(orderReturnFormPanel,orderReturngridPanel, null, 5);
	
	var settleType = $("#settleType").val()
 
	//待结算退单列表
	if("orderReturnSettleList"==settleType) {
		
		Ext.getCmp("orderReturnExportBtn").setHidden(true);
		Ext.getCmp("orderReturnSellteExportBtn").setHidden(false);
		
		searchForSettlement();
	}
	
	//待入库结算单
	if("bePutInStorage"==settleType) {
		searchBePutInStorage();
	}
	   	
});

function exportAjax(exportTemplateType){
	
	var searchParams = searchInExport();
	//var exportTemplateType = Ext.getCmp('orderReturn.exportTemplateType').value;
	
	var url = basePath+'/custom/orderReturn/orderReturnExportCsvFile.spmvc?exportTemplateType='+exportTemplateType;

	Ext.getCmp('orderReturnExportBtn').setDisabled(true);
	
	var myMask = new Ext.LoadMask({
	    msg    : '请稍等,正在导出...',
	    target : orderReturnFormPanel
	});
	
	myMask.show();
	Ext.Ajax.request({
		waitMsg : '请稍等.....',
		url : url,
		method : 'post',
		timeout : 7200000,
		 method : 'post',
		params : searchParams,
		success : function(response) {
			if (myMask != undefined){ 
				myMask.hide();
			}
			var obj = Ext.decode(response.responseText);
			var path = obj.data.path;
			var fileName = obj.data.fileName;
			
			if(obj.isok==true){
				window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
			}else{
				Ext.Msg.alert("错误", "导出失败");
			}
			Ext.getCmp('orderReturnExportBtn').setDisabled(false);
		},
		failure : function(response) {
			myMask.hide();
			Ext.Msg.alert("验证", "失败");
			Ext.getCmp('orderReturnExportBtn').setDisabled(false);
		}
	});
	
}


function exportOrderReturnSettle(){
	
	var searchParams = searchInExport();
	
	var url = basePath+ '/custom/orderReturn/orderReturnOrdersettleExportCsvFile.spmvc?exportTemplateType=1';

	var myMask = new Ext.LoadMask({
	    msg    : '请稍等,正在导出...',
	    target : orderReturnFormPanel
	});
	myMask.show();
	
	Ext.Ajax.request({
		waitMsg : '请稍等.....',
		url : url,
		method : 'post',
		timeout : 7200000,
		method : 'post',
		params: searchParams,
		success : function(response) {

			if (myMask != undefined){ 
				myMask.hide();
			}
			var obj = Ext.decode(response.responseText);
			var path = obj.data.path;
			var fileName = obj.data.fileName;
			
			if(obj.isok==true){
				window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
			}else{
				Ext.Msg.alert("错误", "导出失败");
			}
			Ext.getCmp('orderInfoExportBtn').setDisabled(false);
		},
		failure : function(response) {
			myMask.hide();
			Ext.Msg.alert("验证", "失败");
			Ext.getCmp('orderInfoExportBtn').setDisabled(false);
		}
	});
	
}

function searchInExport(){
	var searchParams = getFormParams(orderReturnFormPanel);
	return searchParams;
}

function searchForSettlement(){

	/*
	var searchParams = {start : 0, limit : pageSize };
	

	var orderView = Ext.getCmp("orderReturnPageOrderView").getValue();
	
	var isGoodReceived = Ext.getCmp("orderReturnPageIsGoodReceived").getValue();
	
	var checkinStatus = Ext.getCmp("orderReturnPageCheckinStatus").getValue();
	
	var qualityStatus = Ext.getCmp("orderReturnPageQualityStatus").getValue();
	
	var payStatus = Ext.getCmp("orderReturnPageReturnPayStatus").getValue();
	
	searchParams.orderView = orderView;
	searchParams.isGoodReceived = isGoodReceived;
	searchParams.checkinStatus = checkinStatus;
	searchParams.qualityStatus = qualityStatus;
	searchParams.payStatus = payStatus;
	
	orderReturngridPanel.store.currentPage = 1;// 翻页后重新查询 页面重置为1
	orderReturngridPanel.store.load({params : getFormParams(orderReturnFormPanel, searchParams)});*/

	var initParams = {start : 0, limit : pageSize };
	var searchParams = getFormParams(orderReturnFormPanel);
	
//	var orderView = Ext.getCmp("orderReturnPageOrderView").getValue();
	
	//显示状态
	var orderView =0;
	orderViewOptionCombo.setValue(0);
	
//	var isGoodReceived = Ext.getCmp("orderReturnPageIsGoodReceived").getValue();
	
//	var isGoodReceived =1;
//	isGoodReceivedOptionCombo.setValue(1);
	
//	var checkinStatus = Ext.getCmp("orderReturnPageCheckinStatus").getValue();
	
//	var checkinStatus =1
//	checkinStatusOptionCombo.setValue(1);
	
//	var qualityStatus = Ext.getCmp("orderReturnPageQualityStatus").getValue();
	
	//var qualityStatus =1
//	qualityStatusOptionCombo.setValue(1);
	
	//var payStatus = Ext.getCmp("orderReturnPageReturnPayStatus").getValue();
	
	var payStatus =2
	returnPayStatusOptionCombo.setValue(2);
	
	//是否退款:1是0否;
	var haveRefund=1;
	haveRefundOptionCombo.setValue(1);
	
	var returnPay = 1;
	returnPayOptionCombo.setValue(1);
	
	searchParams.orderView = orderView;
//	searchParams.isGoodReceived = isGoodReceived;
//	searchParams.checkinStatus = checkinStatus;
//	searchParams.qualityStatus = qualityStatus;
	searchParams.payStatus = payStatus;
	searchParams.haveRefund = haveRefund;
	searchParams.returnPay = returnPay;
	searchParams.isNotQuanQiuTong=true
	
	orderReturngridPanel.store.currentPage = 1;// 翻页后重新查询 页面重置为1
	orderReturngridPanel.store.pageSize = pageSize;
	orderReturngridPanel.store.load({params : searchParams});
	
}


function searchBePutInStorage(){

	var initParams = {start : 0, limit : pageSize };
	var searchParams = getFormParams(orderReturnFormPanel);
	
	var orderView = 0;
	orderViewOptionCombo.setValue(0);
	
	var isGoodReceived = 0;
	isGoodReceivedOptionCombo.setValue(0);
	
	var checkinStatus = 0
	checkinStatusOptionCombo.setValue(0);
	
	var payStatus = 0
	returnPayStatusOptionCombo.setValue(0);
	
	var returnOrderStatus = 1
	returnOrderStatusOptionCombo.setValue(1);
	
	var returnType = 1
	returnTypeOptionCombo.setValue(1); //退单类型
	
	searchParams.orderView = orderView;
	searchParams.isGoodReceived = isGoodReceived;
	searchParams.checkinStatus = checkinStatus;
	searchParams.returnOrderStatus = returnOrderStatus;
	searchParams.payStatus = payStatus;
	searchParams.returnType = returnType;
	
	orderReturngridPanel.store.currentPage = 1;// 翻页后重新查询 页面重置为1
	orderReturngridPanel.store.pageSize = pageSize;
	orderReturngridPanel.store.load({params : searchParams});
	
}

