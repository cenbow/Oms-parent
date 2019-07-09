Ext.Loader.setConfig({enabled: true});
//Ext.Loader.setPath('Ext.ux', 'ext4/ux');
// Ext.Loader.setPath('Ext.ux', basePath+'/ext4/ux');
Ext.require([
         'Ext.grid.*',
         'Ext.data.*',
         'Ext.selection.CheckboxModel',
         'MB.ComboModel',
//         'MB.model.ChannelInfo',
//         'MB.model.ChannelShop',
         'MB.GoodsReturnChangeQuery'
]);
var addAction;
var goodsActionList;
//用于编辑页面传参
var selectId;
Ext.onReady(function() {
	var queryUrl = basePath+"custom/goodsReturnChange/goodsReturnChangeList.spmvc";
	var returnChangeInfoUrl = basePath + "custom/goodsReturnChange/returnChangePage";
	var sm;
	var columns = null;
	var tbar;
	var pageSize=20;
	var goodsRCFormPanel = null;
	var goodsRCgridPanel = null;
	var goodsRCJsonStore = null;
	var isHistory = 0;
	var batchConfirmUrl=basePath+"custom/goodsReturnChange/updateStatusBatch.spmvc";

	//退换类型
	var returnTypeStore=new Ext.data.SimpleStore({
		data :[["-1","全部"],["1","退货"],["2","换货"]],
		fields : [ 'text', 'filed' ]
	});
	var returnTypeCombo=createComboBoxLocal(returnTypeStore, "OrderReturnPage_returnType", 'filed','text', "local", "退换类型", 200, 60, "returnType","请选择");
	//退换原因
	var reasonStore=new Ext.data.SimpleStore({
		data :[["-1","全部"],["1","商品质量不过关"],["2","商品在配送中损坏"],["3","商品与描述不符"],["4","尚未收到商品"],["5","其他"]],
		fields : [ 'text', 'filed' ]
	});
	var reasonCombo=createComboBoxLocal(reasonStore, "OrderReturnPage_reason", 'filed','text', "local", "退换原因", 200, 60, "reason","请选择");
	//换购选择类型
	var redemptionStore=new Ext.data.SimpleStore({
		data :[["-1","全部"],["1","换购本商品其他尺码或颜色"],["2","换购其他商品"]],
		fields : [ 'text', 'filed' ]
	});
	var redemptionCombo=createComboBoxLocal(redemptionStore, "OrderReturnPage_redemption", 'filed','text', "local", "换购选择类型", 240, 90, "redemption","请选择");
	//吊牌情况
	var tagTypeStore=new Ext.data.SimpleStore({
		data :[["-1","全部"],["1","吊牌完好"],["2","吊牌破损"],["3","无吊牌"]],
		fields : [ 'text', 'filed' ]
	});
	var tagTypeCombo=createComboBoxLocal(tagTypeStore, "OrderReturnPage_tagType", 'filed','text', "local", "吊牌情况", 200, 60, "tagType","请选择");
	//外观情况
	var exteriorTypeStore=new Ext.data.SimpleStore({
		data :[["-1","全部"],["1","外观完好"],["2","外观有破损"],["3","外观有污渍"]],
		fields : [ 'text', 'filed' ]
	});
	var exteriorTypeCombo=createComboBoxLocal(exteriorTypeStore, "OrderReturnPage_exteriorType", 'filed','text', "local", "外观情况", 200, 60, "exteriorType","请选择");
	//赠品情况
	var giftTypeStore=new Ext.data.SimpleStore({
		data :[["-1","全部"],["1","赠品完好"],["2","赠品破损"],["3","赠品不全"],["4","未收到赠品"]],
		fields : [ 'text', 'filed' ]
	});
	var giftTypeCombo=createComboBoxLocal(giftTypeStore, "OrderReturnPage_giftType", 'filed','text', "local", "赠品情况", 200, 60, "giftType","请选择");
	//状态
	var statusStore=new Ext.data.SimpleStore({
		data :[["-1","全部"],["0","已取消"],["1","待沟通"],["2","已完成"],["3","待处理"]],
		fields : [ 'text', 'filed' ]
	});
	var statusCombo=createComboBoxLocal(statusStore, "OrderReturnPage_status", 'filed','text', "local", "状态", 200, 60, "status","请选择");

	Ext.define('MB.model.ChannelInfo', {
		extend: 'Ext.data.Model',
		fields: [
			{ name: 'siteName', type: 'string' },
			{ name: 'siteCode', type: 'string' }
		]
	});
	
	Ext.define('MB.model.ChannelShop', {
		extend: 'Ext.data.Model',
		fields: [
			{name: 'channelCode', type: 'string'},
			{name: 'channelName',  type: 'string'}
		]
	});
	
	var channelStore= new Ext.data.Store({
		model:"MB.model.ChannelInfo",
		proxy: {
			type: 'ajax',
			url:basePath+'custom/common/getChannelInfos',
			reader:  {type:'json'}
		}
	});
	var changeChannelCode = function (combo, record, index) {
		shopCombo.setValue('');
		if(combo.value==null) {
			shopStore.getProxy().url=basePath+"custom/common/getChannelShops";
		}else {
			shopStore.getProxy().url=basePath+"custom/common/getChannelShops?channelCode="+ combo.value;
		}
		shopStore.load();
	};
	//渠道
	var channelCombo = createComboBoxLocal(channelStore,"OrderReturnPage_OrderFromSec",'siteName','siteCode',"remote", "平台商城", 200, 60, "siteCode","请选择渠道");
	channelCombo.addListener('change',changeChannelCode);

	var shopStore= new Ext.data.Store({
		model:"MB.model.ChannelShop",
		proxy:{
			type: 'ajax',
			url: basePath+'custom/common/getChannelShops',
			reader: {
				type:'json'
			}
		}
	});

	//渠道店铺
	var shopCombo = createComboBoxLocal(shopStore,"OrderReturnPage_OrderFrom",'channelName','channelCode',"remote", "渠道店铺",  200, 60, "channelCode","请选择渠道店铺");
	
	goodsRCFormPanel = Ext.widget('form', {
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		frame: true,
		fieldDefaults: {
			labelAlign: 'right'
//			labelWidth: 100
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
			items: [{
					width : 200, // 该列有整行中所占百分比
					xtype : "textfield",
//					id:'goodsRC_orderSn',
					name: 'orderSn',
					fieldLabel : "订单号",
					labelWidth: 60
				} ,{
					width : 200,
					xtype : 'textfield',
					fieldLabel : '退单号',
//					id:'goodsRC.returnSn',
					name: 'returnSn',
					labelWidth: 60
				},{
					width : 200,
					xtype : 'textfield',
					fieldLabel : '退款单号',
//					id:'goodsRC.returnPaySn',
					name: 'returnPaySn',
					labelWidth: 60
				},{
					width : 200, // 该列有整行中所占百分比
					xtype : "textfield",
//					id:'goodsRC.returnchangeSn',
					name: 'returnchangeSn',
					fieldLabel : "申请单号",
					labelWidth: 60
				}
			]
		} , { //line 2
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [channelCombo , shopCombo , {
				width : 200,
				labelWidth: 60,
				xtype : 'textfield',
				fieldLabel : '商品码',
//				id:'goodsRC.skuSn',
				name: 'skuSn'
			},returnTypeCombo,reasonCombo]
		}, { //line 3
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [ tagTypeCombo,exteriorTypeCombo,giftTypeCombo,statusCombo]
		}, { //line 4
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [{
				width : 200,
				xtype : 'textfield',
				fieldLabel : '下单人',
//				id:'goodsRC.userId',
				name: 'userId',
				labelWidth: 60
			},{
				width : 200, // 该列有整行中所占百分比
				layout : "form", // 从上往下的布局
				xtype : "textfield",
//				id:'goodsRC.contactName',
				name: 'contactName',
				fieldLabel : "收货人",
				labelWidth: 60
			},{
				width : 120,
				layout : "form", 
				xtype : "textfield",
//				id:'goodsRC.stReturnSum',
				name: 'stReturnSum',
				fieldLabel : "退换数量",
				labelWidth: 60
			} , {
				width : 80,
				layout : "form", 
				xtype : "textfield",
//				id:'goodsRC.enReturnSum',
				name: 'enReturnSum',
				fieldLabel : "至",
				labelWidth: 20
			} , {
				xtype: 'textfield',
				id:"goodsRC_startTime",
				width : 230,
				name: 'startTime',
				fieldLabel: '创建开始时间',
				labelWidth: 90,
				listeners:{
					render:function(p){
						p.getEl().on('click',function(){
							WdatePicker({startDate:'%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'});
						});
					}
				}
			} , {
				xtype: 'textfield',
				id:"goodsRC_endTime",
				width : 200,
				name: 'endTime',
				fieldLabel: '结束时间',
				labelWidth: 60,
				listeners:{
					render:function(p){
						p.getEl().on('click',function(){
							WdatePicker({startDate:'%y-%M-%d 23:59:59',dateFmt:'yyyy-MM-dd HH:mm:ss'});
						});
					}
				}
			}]
		}],
		buttons : [{
			text : '查询',
			columnWidth : .1,
			handler : search
		} , {
			text : '重置',
			handler : function () {
				resetButton(goodsRCFormPanel);
				reasonCombo.setValue("-1"); 
//				redemptionCombo.setValue("-1");
				tagTypeCombo.setValue("-1"); 
				exteriorTypeCombo.setValue("-1");
				giftTypeCombo.setValue("-1");
				statusCombo.setValue("-1");
				returnTypeCombo.setValue("-1");
				Ext.getDom("goodsRC_startTime").value="";
				Ext.getDom("goodsRC_endTime").value="";
			}
		}
		, {
			text : '导出',
			handler : exportRecord
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
			}
		}
	});
	
	goodsRCJsonStore = createObjectGridStore(queryUrl, pageSize, "MB.GoodsReturnChangeQuery");
	columns = [
		{ id : 'orderSn', header : "关联主订单号", align : "left", width : 180, dataIndex : 'orderSn', renderer: function(value, md, r) {
				if (value != undefined && value != null ) {
					var url = order_info_url + value +"&isHistory=" + isHistory;
					var wt="";
					var questionStatus =  r.get('questionStatus');
					if(questionStatus==1){
						 wt="<font color='red' >（问题单）</font>";
					}
					return "<a href=" +url + " target='_blank'  >"+value+"</a>"+wt;
				}
		}
		},
		{ id : 'id', header : "申请单id", align : "center", width : 90, dataIndex : 'id' , renderer: function(value, md, r) {
			if (value != undefined && value != null ) {
				var orderSn =  r.get('orderSn');
				var url = returnChangeInfoUrl + "?orderSn=" + orderSn +"&isHistory=" + isHistory + "&id=" + value;
				return "<a href=" +url + " target='_blank'  >"+value+"</a>";
			}
		}},
		{ id : 'returnchangeSn', header : "申请单号", align : "center", width : 160, dataIndex : 'returnchangeSn' },
		{ id : 'returnType', header : "退换类型", align : "center", width : 80, dataIndex: "returnTypeStr"},
		{ id : 'statusStr',header : "处理状态", align : "center",width : 90, dataIndex:"statusStr"},
		{ id : 'goodsName', header : "退换货商品名称", align : "center", width : 240, dataIndex : 'goodsName'},
		{ id : 'shopName', header : "订单店铺", align : "center", width : 90, dataIndex : 'shopName' },
		{ id : 'transTypeStr', header : "交易类型", align : "center", width : 90, dataIndex : 'transTypeStr' },
		{ id : 'userId', header : "下单人", align : "center", width : 100, dataIndex : 'userId' },
		{ id : 'contactName',header : "收货人", align : "center",width : 100,dataIndex : 'contactName'},
		{ id : 'totalFee', header : "订单金额", align : "center", width : 120, dataIndex : 'totalFee'},
		{ id : 'skuSn', header : "退换货商品码", align : "center", width : 120, dataIndex : 'skuSn'},
		{ id : 'transactionPrice', header : "退换货商品金额", align : "center", width : 100, dataIndex : 'transactionPrice'},
		{ id : 'returnSum',header : "货物数量", align : "center",width : 80,dataIndex : 'returnSum'},
		{ id : 'reason', header : "退换原因", align : "center", width : 160, dataIndex:'reasonStr'},
		{ id : 'explain', header : "退换说明", align : "center",dataIndex : 'explain', width : 140,renderer: function(value) {
			return '<font title="'+value+'">'+value+'</font>';
		}},
		{ id : 'redemption',header : "换购选择类型", align : "center",width : 160,dataIndex:'redemptionStr'},
		{ id : 'tagType',header : "吊牌情况", align : "center",width : 100,dataIndex:'tagTypeStr'},
		{ id : 'exteriorType',header : "外观情况", align : "center",width : 100,dataIndex:'exteriorTypeStr'},
		{ id : 'giftType',header : "赠品情况", align : "center",width : 100,dataIndex:'giftTypeStr'},
		{ id : 'create',header : "创建时间", align : "center",width : 160,dataIndex : 'create'}
	];

	sm = Ext.create('Ext.selection.CheckboxModel');

	var dItems = [{
		xtype: 'toolbar',
		dock: 'top',
		items: [{
			id : 'tBar@Pending',
			text : '待处理',
			tooltip : '待处理',
			iconCls : 'plugin',
			value: 3,
			handler : batchConfirm
		} , {
			id : 'tBar@communicate',
			text : '待沟通',
			tooltip : '待沟通',
			iconCls : 'user',
			value: 1,
			handler : batchConfirm
		} , {
			id : 'tBar@complete',
			text : '完成',
			tooltip : '完成',
			iconCls : 'accept',
			value: 2,
			handler : batchConfirm
		}, {
			id : 'tBar@Cancel',
			text : '取消',
			tooltip : '取消',
			iconCls : 'delete',
			value: 0,
			handler : batchConfirm
		} ]
	}];
	goodsRCgridPanel = createGridPanelByExt4('goodsRC_gridss_id', null, null, goodsRCJsonStore,
			pageSize, null, columns, sm, dItems);
//	var goodsRCCouponPanel = new Ext.panel.Panel( {
//		frame : true,
//		autoHeight: true,
//		items : [ goodsRCFormPanel, goodsRCgridPanel,{
//			xtype: 'panel',
//			autoHeight: true,
//			contentEl : 'goodsActions'
//		}]
//	});
//	goodsRCCouponPanel.render(Ext.getBody());
	
	var mainPanel = Ext.widget('panel', {
		renderTo: Ext.getBody(),
		title: "退换货申请单列表",
		width: '100%',
		//bodyPadding: 2,
		resizable: false,
		autoHeight: true,
		items: [ goodsRCFormPanel, goodsRCgridPanel ]
	});

	//查询翻页后带回查询数据
	goodsRCJsonStore.on('beforeload', function (store, options){
		Ext.apply(store.proxy.extraParams, getFormParams(goodsRCFormPanel)); 
	});


	function search(){
		var initParams = {start : 0, limit : pageSize };
		var searchParams = getFormParams(goodsRCFormPanel, initParams);
		goodsRCgridPanel.store.load({params : searchParams});
	}
	function exportRecord() {// 导出数据
//		var myMask = new Ext.LoadMask(Ext.getBody(),{msg:"请稍等,正在导出..."}); 
//		myMask.show();
		var myMask = new Ext.LoadMask({
	    msg    : '请稍等,正在下载...',
	    target : goodsRCFormPanel
		});
	
		myMask.show();
		var str = getFormParams(goodsRCFormPanel);
		Ext.Ajax.request({
			timeout: 100000,//100秒
			url: basePath+"custom/goodsReturnChange/goodsReturnChangeExportCsvFile.spmvc",
			params:str,
			success: function(response){
				if (myMask != undefined){ myMask.hide();}
				var obj = Ext.decode(response.responseText);
				if(obj.isok==true){
					window.location.href=basePath+"custom/downloadFile.spmvc?path="+obj.data;
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

	function batchConfirm(o) {
		//批量确认被选择的行订单
		var selModel = goodsRCgridPanel.getSelectionModel();
		if (selModel.hasSelection()) {
			var records = selModel.getSelection();
			var ids = new Array();
			for ( var i = 0; i < records.length; i++) {
				var id = records[i].get("id");
				var statusStr=records[i].get("statusStr");
//				if(statusStr=="已完成"||statusStr=="已取消"){
//					errorMsg("结果", "申请单状态为已完成或者已取消，无法修改！");
//					return false;
//				}
					if(id && id != ''){
						ids.push(id);
					}
			}
			if(ids && ids.length > 0){
						createAjaxData("batchConfirm", batchConfirmUrl, doSuccessfun, doFailurefun, {"ids":ids,"type":o.value}, 100000);
			}
		} else {
			alertMsg("错误", "请选择申请单!");
		}	
	}
	function doSuccessfun(id, response, opts) {
		var respText = Ext.JSON.decode(response.responseText);
		if(respText.success == "true"){
			goodsRCJsonStore.load();
			//刷新后取消选中，否则可能造成选取数据的getSelection()未刷新的情况
			 Ext.getCmp("goodsRC_gridss_id").getSelectionModel().deselectAll();
		}else{
			errorMsg("结果", respText.msg);
		}
	}
	function doFailurefun(id,response,opts) {
		var respText = Ext.JSON.decode(response.responseText);
		errorMsg("结果", respText.msg);
	}
	

	function format(shijianchuo) {
		var time = new Date(shijianchuo); 
		var y = time.getFullYear(); 
		var m = time.getMonth()+1; 
		var d = time.getDate(); 
		var h = time.getHours(); 
		var mm = time.getMinutes(); 
		var s = time.getSeconds(); 
		return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s); 
	}
	function add0(m) {
		return m < 10 ? '0'+m:m; 
	}

	window.onresize=function(){
		setResize(goodsRCFormPanel,goodsRCgridPanel, null, 30);
	};
	setResize(goodsRCFormPanel,goodsRCgridPanel, null, 30);
});

