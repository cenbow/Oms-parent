Ext.Loader.setConfig({
	enabled: true
});
//Ext.Loader.setPath('Ext.ux', 'ext4.2/ux');
// Ext.Loader.setPath('Ext.ux', basePath+'/ext4/ux');
var orderSn;
var result;
var orderQuestionGrid = null;
var orderQuestionJsonStore = null;
// 选中订单数组
var orderSns = new Array();
var shortageQuestionGrid = null;
var shortageQuestionJsonStore = null;
var orderQuestionListUrl = basePath+"custom/orderQuestion/orderQuestionList.spmvc";
var shortageQuestionListUrl = basePath+"custom/orderQuestion/getShortageQuestionList.spmvc";
var questionDataUrl = basePath+"/custom/customDefine/customDefineList.spmvc";
var questionDataStoreList = null;
var couponPanel = null;
var mainPanel = null;
var sm;
var columns = null;
var orderMemberOptionCombo = null;
var shortageQuestionColumns = null;
var shopOptionCombo = null;
var tbar;
var pageSize=20;
var orderQuestionFormPanel = null;
var isHistory = 0;
var questionType = 0;
Ext.require([
	'Ext.grid.*',
	'Ext.data.*',
	'Ext.selection.CheckboxModel',
    'MB.ComboModel',
    'MB.Channel',
    'MB.Shop',
    'MB.OrderQuestionQuery',
    'MB.QuestionType'
]);
Ext.onReady(function() {
	var batchLockUrl = basePath + "/custom/orderStatus/batchLock.spmvc";
	var batchUnLockUrl = basePath + "/custom/orderStatus/batchUnlock.spmvc";
	//交易类型
	var transTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.trans_type
	});
	var transTypeOptionCombo = createComboBoxLocal(transTypeDataStore, "OrderQuestionPageTransType", 'n','v', "local", "交易类型", 220, 80, "transType","请选择支付类型");
	//订单状态
	var orderStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.order_status
	});
	var orderStatusOptionCombo = createComboBoxLocal(orderStatusDataStore, "OrderQuestionPageOrderStatus", 'n','v', "local", "订单状态", 220, 80, "orderStatus","请选择订单状态");
	//支付状态
	var payStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.pay_status
	});
	var payStatusOptionCombo = createComboBoxLocal(payStatusDataStore, "OrderQuestionPagePayStatus", 'n','v', "local", "支付状态", 220, 80, "payStatus","请选择支付状态");
	//发货状态
	var shipStatusDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.ship_status
	});
	var shipStatusOptionCombo = createComboBoxLocal(shipStatusDataStore, "OrderQuestionPageShipStatus", 'n','v', "local", "发货状态", 220, 80, "shipStatus","请选择发货状态");

	//关联订单来源1
	//一级订单来源
	var channelTypeStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.channel_type
	});
	
	var changeChannelType = function (combo, record,index) {
		channelOptionCombo.setValue('');
		//orderMemberOptionCombo.clearValue();
			//改变二级菜单
			if(combo.value==null) {
				orderRelatedSourceDataStore2.getProxy().url=basePath+'custom/common/getChannelInfos?channelType=0';
			}else {
				orderRelatedSourceDataStore2.getProxy().url=basePath+'custom/common/getChannelInfos?channelType='+combo.value;
			}
			orderRelatedSourceDataStore2.load();
	};
	var channelTypeOptionCombo = createComboBoxLocal(channelTypeStore,'OrderQuestionPageOrderFormFirst', 'n','v', 'local','订单来源', 220, 80, 'orderFormFirst', '请选择渠道类型');
	// 添加change监听事件
	channelTypeOptionCombo.addListener('change',changeChannelType);
	//关联订单来源2  ajax 加载
	var orderRelatedSourceDataStore2= new Ext.data.Store({
		model:"MB.Channel",
		proxy: {
			type: 'ajax',
			url:basePath+'custom/common/getChannelInfos?channelType=0', //这里是参数可以顺便写,这个数据源是在第一个下拉框select的时候load的
			reader: {
				type:'json'
			}
		}
	});
	var changeChannelCode = function (combo, record, index) {
		//shopOptionCombo.clearValue();
		shopOptionCombo.setValue('');
//		orderMemberOptionCombo.setValue(-1);
		if(combo.value==null) {
			orderRelatedSourceDataStore3.getProxy().url=basePath+"custom/common/getChannelShops";
		}else {
			if(combo.value=="TB_CHANNEL_CODE" || combo.value=="TMALL_CHANNEL_CODE" || combo.value=="TBFX_CHANNEL_CODE") {
				orderMemberDataStore.loadData(getdata(SELECT.taobao),false);
			} else if(combo.value=="BG_CHANNEL_CODE") {
				orderMemberDataStore.loadData(getdata(SELECT.mall),false);
			} else {
				orderMemberDataStore.removeAll();
				orderMemberDataStore.loadData([]);
			}
			orderRelatedSourceDataStore3.getProxy().url=basePath+"custom/common/getChannelShops?channelCode="+ combo.value;
		}
		orderRelatedSourceDataStore3.load();
	};
	

	//订单来源媒体
	var refererDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.referer_type
	});

	var refererOptionCombo= createComboBoxLocal(refererDataStore, 'orderInfoPageReferer', 'n','v', "local", "referer", 230, 80, "referer","请选择订单来源媒体");

	var channelOptionCombo = createComboBoxLocal(orderRelatedSourceDataStore2,"OrderQuestionPageOrderFormSec",'channelTitle','chanelCode',"remote", "渠道", 220, 80, "orderFormSec","请选择渠道");
	channelOptionCombo.addListener('change',changeChannelCode);
	
	var orderRelatedSourceDataStore3= new Ext.data.Store({
		model:"MB.Shop",
		proxy:{
			type: 'ajax',
			url:basePath+'custom/common/getChannelShops' ,
			reader: {
				type:'json'
			}
		}
	});
	shopOptionCombo = createComboBoxLocalByMult(orderRelatedSourceDataStore3,"OrderQuestionPageOrderForm",'shopTitle','shopCode',"remote", "店铺渠道", 330, 80, "orderFormsVo","请选择店铺渠道");
	
/*	//渠道会员等级 级联的 根据 这个也要动态的改变
	Ext.define('Member', {
		extend: 'Ext.data.Model',
		fields: [
			{name: 'code'},
			{name: 'name'},
		]
	});*/
	//会员等级
	var orderMemberDataStore=new Ext.data.ArrayStore( {
		autoDestroy: true,
		fields : [ 'text', 'filed' ],
		autoLoad :false,
		data:[]
	});
	orderMemberOptionCombo = createComboBoxLocal(orderMemberDataStore, "OrderQuestionPageUseLevel", "filed", "text", "local", "会员等级", 220,80, "useLevel","请选择"); 
	
	createAjaxDataBySyn("questionData", questionDataUrl, doSuccessfun, doFailurefun, {"type":3}, 100000);

	//交易类型
	var questionDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.QuestionType',
		data: questionDataStoreList
	});
	
	var orderQuestionOptionCombo=createComboBoxLocal(questionDataStore, "OrderQuestionPageReason", "name", "name", "local", "问题单原因",440, 80, "reason","请选择");
	
	//处理状态
	var processStatusDataStore=new Ext.data.Store({
		data :SELECT.process_status,
		model: 'MB.ComboModel'
	});
	var processStatusOptionCombo=createComboBoxLocal(processStatusDataStore, "OrderQuestionPageProcessStatus", "n", "v", "local", "处理状态", 220,80, "processStatus","请选择"); 

	//设置处理状态默认值: 0.待处理
	processStatusOptionCombo.setValue("0"); 

	questionDataStore.on('load', function (store, records, successful, options) {
		if (successful && Ext.typeOf(orderQuestionOptionCombo.getPicker().loadMask) !== "boolean") {
			orderQuestionOptionCombo.getPicker().loadMask.hide();  
		}
	});
	
	orderRelatedSourceDataStore2.on('load', function (store, records, successful, options) {
		if (successful && Ext.typeOf(channelOptionCombo.getPicker().loadMask) !== "boolean") {
			channelOptionCombo.getPicker().loadMask.hide();
		}
	});
	orderRelatedSourceDataStore3.on('load', function (store, records, successful, options) {
		if (successful && Ext.typeOf(shopOptionCombo.getPicker().loadMask) !== "boolean") {
			shopOptionCombo.getPicker().loadMask.hide();
		}
	});
	orderMemberDataStore.on('load', function (store, records, successful, options) {
		if (successful && Ext.typeOf(orderMemberOptionCombo.getPicker().loadMask) !== "boolean") {
			orderMemberOptionCombo.getPicker().loadMask.hide();
		}
	});
	
	var queryPageStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.query_pagesize_type
	});
	var changePageSize = function (combo, record, index) {
		if (orderQuestionGrid != null && orderQuestionGrid != 'undefined') {
			var newPageSize = parseInt(record);
			if(null != record && pageSize != newPageSize){
				pageSize = newPageSize;
				search();
			}
		}
	};
	
	var queryPageCombo= createComboBoxLocal(queryPageStore, "orderQuestionPageQueryPageArray", 'n','v', "local", "每页显示", 210, 80, "queryPageArray","请选择每页显示条数");
	queryPageCombo.addListener('change',changePageSize);
	queryPageCombo.setValue(pageSize.toString());
	
	//时间类型查询
	var timeDataStore=new Ext.data.SimpleStore( {
		data :[["addTime","生成时间"],
		       ["questionTime","问题时间"]//,
		      // ["clearTime","结算时间"],
		      // ["confirm_time","确定时间"]
		],
		fields : [ 'text', 'filed' ],
	});
	
	var timeOptionCombo=createComboBoxLocal(timeDataStore, "orderQuestionPageSelectTimeType", 'filed','text', "local", "时间类型", 220, 80, "selectTimeType","请选择时间类型");
	timeOptionCombo.setValue("addTime");
	
	orderQuestionFormPanel = Ext.widget('form', {
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		frame: true,
	//	collapsible: true,
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
			items: [
					{
						layout : "form", // 从上往下的布局
						xtype : "textfield",
						id:'OrderQuestionPageOrderSn',
						name: 'orderSn',
						fieldLabel : "订单号",
						labelWidth:80,
						width : 220,
					} , {
						layout : "form", // 从上往下的布局
						xtype : "textfield",
						id:'OrderQuestionPageUserName',
						name: 'userName',
						fieldLabel : "下单人",
						labelWidth:80,
						width : 220,
					} , transTypeOptionCombo,
					{
						width : 300,
						xtype: 'radiogroup',
						name:'listDataTypehis',
						items: [
							{boxLabel: '近三个月数据', name: 'listDataTypehis',inputValue:'newDate', checked: true},
							{boxLabel: '历史数据', name: 'listDataTypehis',inputValue:'historyDate'},
						],
						listeners: {
							'change' : function (r , a) {
								if(a.listDataTypehis=="newDate") {
									isHistory = 0;
								} else {
									isHistory = 1;
								}
							}
						}
					}
				]
		}, { //line 2
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [orderStatusOptionCombo,
			        payStatusOptionCombo,
			        shipStatusOptionCombo,
			        processStatusOptionCombo]
		}, { //line 3
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [
			  timeOptionCombo,
				{
					xtype: 'textfield',
					id:"addTimeStart",
					width : 220,
					name: 'addTimeStart',
					fieldLabel: '开始时间',
					labelWidth: 80,
					listeners:{
						render:function(p){
							p.getEl().on('click',function(){
								WdatePicker({startDate:'%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'});
							});
						}}
				} , {
					xtype: 'textfield',
					id:"addTimeEnd",
					width : 220,
					name: 'addTimeEnd',
					fieldLabel: '结束时间',
					labelWidth: 80,
					listeners:{
						render:function(p){
							p.getEl().on('click',function(){
								WdatePicker({startDate:'%y-%M-%d 23:59:59',dateFmt:'yyyy-MM-dd HH:mm:ss'});
							});
						}
					}
				},refererOptionCombo
				
				/*, {
					xtype: 'textfield',
					id:"questionTimeStart",
					width : 220,
					name: 'questionTimeStart',
					fieldLabel: '生成时间',
					labelWidth: 80,
					listeners:{
						render:function(p){
							p.getEl().on('click',function(){
								WdatePicker({startDate:'%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'});
							});
						}
					}
				} ,*//* {
					xtype: 'textfield',
					id:"questionTimeEnd",
					width : 220,
					name: 'questionTimeEnd',
					fieldLabel: '结束时间',
					labelWidth: 80,
					listeners:{
						render:function(p){
							p.getEl().on('click',function(){
								WdatePicker({startDate:'%y-%M-%d 23:59:59',dateFmt:'yyyy-MM-dd HH:mm:ss'});
							});
						}
					}
				}*/
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
					{
						width : 220,
						xtype: 'radiogroup',
						name:'listDataType',
						id:'OrderQuestionPageListDataType',
						items: [
							{boxLabel: 'OS问题单', name: 'listDataType',inputValue:'true', checked: true},
							{boxLabel: '缺货问题单', name: 'listDataType',inputValue:'false'}
						],
						listeners: {
							"change" : function(radio, newV, oldV, e) {
								questionDataStore.removeAll();
								orderQuestionOptionCombo.setValue('');
								if(newV.listDataType=="true") {
									questionType = 0;
								//	alert(questionDataStoreList);
									questionDataStore.loadData(questionDataStoreList,false);
									//隐藏缺货列表
									Ext.getCmp('shortageQuestion_gridss_id').setVisible(false);
									setNormalQuestonResize();
									Ext.getCmp('OrderQuestionPageDepotCode').setVisible(false);
									Ext.getCmp('OrderQuestionPageDeliverySn').setVisible(false);
									Ext.getCmp('OrderQuestionPageDepotCode').setValue(null);
									Ext.getCmp('OrderQuestionPageDeliverySn').setValue(null);
									search();
								} else if(newV.listDataType=="false"){
									questionType = 1;
									//更新物流问题单原因;
								//	alert(mylogistics_question_reason);
									questionDataStore.loadData(mylogistics_question_reason,false);
//									mainPanel.setWidth('100%');
//						
									orderQuestionGrid.setWidth('78%');
//									shortageQuestionGrid.setWidth('20%');
									//显示缺货列表;
									Ext.getCmp('shortageQuestion_gridss_id').setVisible(true);
//									selfAdaptionHigh();
//									setNormalQuestonResize();
									setNormalQuestonResize();
									//发货仓库编码;
									Ext.getCmp('OrderQuestionPageDepotCode').setVisible(true);
									//交货单编码;
									Ext.getCmp('OrderQuestionPageDeliverySn').setVisible(true);
									search();
								}
							}
						}
					},
					orderQuestionOptionCombo,
				//	orderMemberOptionCombo,
					
					{
						xtype : "textfield",
						id:'OrderQuestionPageDepotCode',
						name: 'depotCode',
						fieldLabel : "发货仓库编码",
						labelWidth: 80,
						hidden : true,
						width : 220,
					} , {
						xtype : "textfield",
						id:'OrderQuestionPageDeliverySn',
						name: 'deliverySn',
						fieldLabel : "交货单编码",
						hidden : true,
						labelWidth: 80,
						width : 220
					} 
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
					channelTypeOptionCombo,
					channelOptionCombo,
					shopOptionCombo//,
					//orderMemberOptionCombo
			]
		}
	],
	buttons : [
	           
queryPageCombo,
	           {
			text : '查询',
			columnWidth : .1,
			handler : search,
		}, {
			text : '重置',
			handler : function () {
				resetButton(orderQuestionFormPanel);
				Ext.getDom("addTimeStart").value="";
				Ext.getDom("addTimeEnd").value="";
			//	Ext.getDom("questionTimeStart").value="";
			//	Ext.getDom("questionTimeEnd").value="";
				transTypeOptionCombo.setValue(-1);
				orderQuestionOptionCombo.setValue('');
				processStatusOptionCombo.setValue("0");
				channelTypeOptionCombo.setValue(0);
				channelOptionCombo.setValue('');
				shopOptionCombo.setValue('');
			//	orderMemberOptionCombo.setValue(-1);
				orderStatusOptionCombo.setValue(-1);
				payStatusOptionCombo.setValue(-1);
				shipStatusOptionCombo.setValue(-1);
				timeOptionCombo.setValue("addTime");
				Ext.getCmp("orderQuestionPageQueryPageArray").setValue(pageSize.toString());
			}
		}, {
			text : '导出',
			handler : exportRecord,
		}],
		buttonAlign: 'right',
		listeners: {
			afterRender: function(thisForm, options){
				this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
					enter: function(){  
						search();
					}
				});
			}
		}
	});

	//得到分页数据
	orderQuestionJsonStore = createObjectGridStoreLazy(orderQuestionListUrl, pageSize, "MB.OrderQuestionQuery");
	columns = [
		{ id : 'no', header : "序号", align : "center", width : 120, dataIndex : 'no', hidden:true},
		{ id : 'orderSn', header : "关联订单号", align : "center", width : 120, dataIndex : 'orderSn',renderer: function(value) {
			if (value != undefined && value != null ) {
				var url = order_info_url + value +"&isHistory=" + isHistory;
				return "<a href=" +url + " target='_blank'  >"+value+"</a>";
			}
		}},
		{ id : 'orderOutSn', header : "外部交易号", align : "center", width : 220, dataIndex : 'orderOutSn',renderer: function(value, md, r) {
			if (value != undefined && value != null ) {
				var orderSn =  r.get('orderSn');
				var url = order_info_url + orderSn +"&isHistory=" + isHistory;
				return "<a href=" +url + " target='_blank' >" + value + "</a>";
			}
		}},
		{ id : 'lockStatus', header : "锁定状态", align : "center", width : 105, dataIndex : 'lockStatus', 
			renderer: function(v, md, r) {
				var str = v;
				if (v == 0) {
					str = '<font style="color:green;">未锁定</font>';
				} else {
					var userName = r.get('lockUserName');
					if (v == 10000 && (userName == undefined || userName == null || userName == '')) {
						str = '<font style="color:red;">其他管理员锁定</font>';
					} else {
						str = '<font style="color:red;">'+ userName +'</font>';
					}
				}
				return str;
			}
		},
	/*	{ id : 'deliverySn', header : "交货单编码", align : "center", width : 120, dataIndex : 'deliverySn'},*/
		{ id : 'questionTypeStr', header : "问题单类型", align : "center", width : 110, dataIndex : 'questionTypeStr'},
		{ id : 'questionReason', header : "问题单原因", align : "center", width : 180, dataIndex : 'questionReason'},
		{ id : 'processStatusStr', header : "订单状态", align : "center", width : 180, dataIndex : 'processStatusStr',renderer: editQuestionOrderStatus},
		{ id : 'addTime', header : "下单时间", align : "center", width : 140, dataIndex : 'addTime'},
		{ id : 'questionAddTime', header : "问题单时间", align : "center", width : 140, dataIndex : 'questionAddTime'},
		{ id : 'transTypeStr', header : "订单类型", align : "center", width : 80, dataIndex : 'transTypeStr'},
		{ id : 'orderFrom', header : "订单来源", align : "center", width : 160, dataIndex : 'orderFrom', hidden: true},
		{ id : 'channelName', header : "订单来源", align : "center", width : 160, dataIndex : 'channelName'},
		{ id : 'userName',header : "下单人", align : "center", sortable : true, width : 120, dataIndex : 'userName'},
		{ id : 'totalFee', header : "总金额", align : "center", width : 80, dataIndex : 'totalFee'},
		{ id : 'totalPayable', header : "应付金额", align : "center", width : 80, dataIndex : 'totalPayable'},
		{ id : 'referer', header : "referer", align : "center", width : 120, dataIndex : 'referer'},
		{ id : 'customerNote', header : "客服备注", align : "center", width : 120, dataIndex : 'customerNote'},
		{ id : 'businessNote', header : "商家备注", align : "center", width : 180, dataIndex : 'businessNote'},
		{ id : 'useLevelStr', header : "渠道会员等级类型", align : "center", width : 120, dataIndex : 'useLevelStr'},
	//	{ id : 'outStockCode', header : "缺货条码", align : "center", width : 120, dataIndex : 'outStockCode'},
		{ id : 'logisticsType', header : "物流类型", align : "center", width : 120, dataIndex : 'logisticsType' ,renderer: function(value, cellmeta, record) {
			if(null != value && "" != value){
				if("0"==value) {
					return "<font>物流问题单</font>";
				}else if ("1"==value){
					return "<font>缺货问题单</font>";
				}
			}
		}}
	];
	sm = Ext.create('Ext.selection.CheckboxModel');
	

	var dItems = [{
		xtype: 'toolbar',
		dock: 'top',
		items: [{
			id: 'question_order_tBar-normal',
			iconCls: 'add',
			text: '批量返回正常单',
			tooltip : '批量返回正常单',
			disabled: ButtonDis(toNormal),
			handler: returnNormal
		} , {
			id : 'question_order_tBar-import',
			text : '批量导入问题单',
			tooltip : '批量导入问题单',
			iconCls : 'add',
			disabled: ButtonDis(addQuestion),
			handler : function() {
				FormEditWin.showAddDirWin("popWins",basePath+"/custom/orderQuestion/gotoImportQuestionPage",
						"importOrderLogistics_winID","导入问题单",800,414);
			}
		}]
	}];
	var selModel = Ext.create('Ext.selection.CheckboxModel', {
		listeners: {
			selectionchange: function(sm, selections) {
				if (ButtonDis(toNormal) == false) {
					orderQuestionGrid.down('#question_order_tBar-normal').setDisabled(selections.length === 0);
				}
			}
		}
	});
	orderQuestionGrid = createGridPanelByExt4('orderQuestionQuery', null, null, orderQuestionJsonStore,
			pageSize, null, columns, selModel, dItems);

	//添加单击列表
	orderQuestionGrid.on('itemclick',function(dataview, record, item, index, e){
		// 物流问题单
		if(questionType == 1){
			var sOrderSn  = record.get("orderSn");
			var no = record.get("no");
			var questionReason = record.get("questionReason");
			var processStatus = Ext.getCmp("OrderQuestionPageProcessStatus").getValue()
				//查询翻页后带回查询数据
			var param = {"orderSn" : sOrderSn, "reason": questionReason, "processStatus": processStatus};
			shortageQuestionJsonStore.on('beforeload', function (store, options) {
				Ext.apply(store.proxy.extraParams, param);
			});
			shortageQuestionJsonStore.load(); 
			shortageQuestionGrid.setTitle("问题单："+sOrderSn + "商品列表");
		}
	});
	
	//缺货store
	Ext.define('shortageQuestionPage', {
		extend: 'Ext.data.Model',
		fields: [
			{ name: 'id' },
			{ name: 'orderSn' },
			{ name: 'customCode' },
			{ name: 'questionType'},
			{ name: 'orderQuestionId'},
			{ name: 'depotCode'},
			{ name: 'deliverySn'},
			{ name: 'lackReason'}
		]
	});

	shortageQuestionColumns = [
		{ xtype: 'rownumberer', header : "序号", width: 40, sortable: false},
		{ id : 'customCode', header : "SKU码", align : "center", width : 100, dataIndex : 'customCode'},
	//	{ id : 'questionType', header : "问题单状态", align : "center", width : 180, dataIndex : 'questionType'},
		{ id : 'depotCode', header : "发货仓库编码", align : "center", width : 120, dataIndex : 'depotCode'},
		{ id : 'deliverySn', header : "交货单编码", align : "center", width : 120, dataIndex : 'deliverySn'},
		{ id : 'lackReason', header : "缺货原因", align : "center", width : 160, dataIndex : 'lackReason'}
	];

	//得到分页数据;
	shortageQuestionJsonStore = createObjectGridStore(shortageQuestionListUrl, pageSize, "shortageQuestionPage");
	
	//缺货列表;
	shortageQuestionGrid = createGridPanelByExt4('shortageQuestion_gridss_id', "问题订单商品列表", 600, shortageQuestionJsonStore,
			pageSize, null, shortageQuestionColumns, null, null);
	
	shortageQuestionGrid.setWidth('20%');
	shortageQuestionGrid.setVisible(false);
	
	var mainPanel = Ext.widget('panel', {
		renderTo: Ext.getBody(),
		title: "问题单列表",
		width: '100%',
		//bodyPadding: 2,
		resizable: false,
//		autoHeight: true,
		items: [
			orderQuestionFormPanel,
			{
				xtype: 'panel',
				layout: 'column',
				//width: '100%',
				autoScroll: true,
				defaultType: 'container',
				items: [{
						id: 'orderQuestionGridTable',
						columnWidth: 0.78,
						items:[orderQuestionGrid]
					},{
						id: 'shortageQuestionGridTable',
						columnWidth: 0.2,
						items:[shortageQuestionGrid]
					}
				]
			}
		]
	});
	Ext.getCmp("orderQuestionGridTable").columnWidth = 1;
	//查询翻页后带回查询数据
	orderQuestionJsonStore.on('beforeload', function (store, options) {
		Ext.apply(store.proxy.extraParams, getFormParams(orderQuestionFormPanel));
	});
	
	function doSuccessfun(id, response, opts) {
		if("questionData"==id){
			var respText = Ext.JSON.decode(response.responseText);
			var data = respText.data;
			var arr = [];
			for(var i=0; i<data.length; i++){
				var o = {};
				o.code = data[i].code;
				o.name = data[i].name;
				arr.push(o);
			}
			questionDataStoreList = arr;
		} else{
			var respText = Ext.JSON.decode(response.responseText);
			if(respText.success == "true"){
				questionDataStore.load();
			}else{
				errorMsg("结果", respText.msg);
			}
		}
	
	}
	function search(){
		var initParams = {start : 0, limit : pageSize };
		var searchParams = getFormParams(orderQuestionFormPanel, initParams);
		orderQuestionGrid.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		//orderInfoGrid.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		orderQuestionGrid.store.pageSize = pageSize;
		orderQuestionGrid.store.load({params : searchParams});
		//已处理
		if( Ext.getCmp("OrderQuestionPageProcessStatus") && 1 == Ext.getCmp("OrderQuestionPageProcessStatus").getValue()) {
			Ext.getCmp("question_order_tBar-normal").setDisabled(true);
			Ext.getCmp("question_order_tBar-import").setDisabled(true);
		//待处理
		}else {
			Ext.getCmp("question_order_tBar-normal").setDisabled(ButtonDis(toNormal));
			Ext.getCmp("question_order_tBar-import").setDisabled(ButtonDis(addQuestion));
		}
	}
	
	function exportRecord() {// 导数据
		var searchParams = getFormParams(orderQuestionFormPanel);
		
		//var myMask = new Ext.LoadMask(Ext.getBody(),{msg:"请稍等,正在导出..."});

		//id: 'question_order_tBar-normal',
		//	id : 'question_order_tBar-import',
		
		Ext.getCmp("question_order_tBar-normal").setDisabled(true);
		Ext.getCmp("question_order_tBar-import").setDisabled(true);
	
		var myMask = new Ext.LoadMask({
		    msg    : '请稍等,正在导出...',
		    target : orderQuestionFormPanel
		});

		myMask.show();
		Ext.Ajax.request({
			timeout: 1800000,//1800秒
			url:basePath+"/custom/orderQuestion/orderquestionExportCsvFile.spmvc",
			params:searchParams,
			success:function(response) {
				if (myMask != undefined){ myMask.hide();}
				var obj = Ext.decode(response.responseText);
				
				var path = obj.data.path;
				var fileName = obj.data.fileName;
				if(obj.isok==true){
					window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
					//window.location.href=basePath+"custom/downloadFile.spmvc?path="+obj.data;
					
					Ext.getCmp("question_order_tBar-normal").setDisabled(false);
					Ext.getCmp("question_order_tBar-import").setDisabled(false);
				}else{
					alert("导出失败");
				};
			},
			failure:function(){
				if (myMask != undefined){ myMask.hide();}
				alert("导出失败");
			}
		});
	}
	
	function editQuestionOrderStatus(value, cellmeta, record) {
		var orderStatus = record.get('orderStatus');
		var payStatus = record.get('payStatus');
		var shipStatus = record.get('shipStatus');
		return getCombineStatus(orderStatus, payStatus, shipStatus);
	}
	function doFailurefun(id,response,opts) {
		errorMsg("结果", respText.msg);
	}
	
	
	function returnNormal() {
		//批量返回正常单被选择的行数据
		var selModel = orderQuestionGrid.getSelectionModel();
		orderSns = new Array();
		if (selModel.hasSelection()) {
			var records = selModel.getSelection();
			for ( var i = 0; i < records.length; i++) {
				var orderSn = records[i].get("orderSn"); 
				if(orderSn && orderSn != ''){
					orderSns.push(orderSn);
				}
			}
			if(orderSns && orderSns.length > 0){
				FormEditWin.showAddDirWin("popWins", basePath+"/pages/orderInfo/setBatchNormalOrder.jsp",
						"batch_normalOrder","订单操作 - 批量返回正常单", 624, 318);
			}
		} else {
			alertMsg("错误", "请选择要批量返回正常单的记录!");
		}
	}

	
	//窗口自适应
	window.onresize=function(){
		setNormalQuestonResize();
	};
	setNormalQuestonResize();
	
	function setNormalQuestonResize() {
		var formHeight = orderQuestionFormPanel.getHeight();
		var clientHeight = document.body.clientHeight;
		orderQuestionGrid.setHeight(clientHeight-formHeight-30);
		shortageQuestionGrid.setHeight(clientHeight-formHeight-30);
		orderQuestionFormPanel.setWidth('100%');
		if (questionType == 0) {
			Ext.getCmp("orderQuestionGridTable").columnWidth = 1;
			shortageQuestionGrid.setTitle("问题订单商品列表");
		} else {
			Ext.getCmp("orderQuestionGridTable").columnWidth = 0.78;
			Ext.getCmp("shortageQuestionGridTable").columnWidth = 0.2;
		}
	}
});
