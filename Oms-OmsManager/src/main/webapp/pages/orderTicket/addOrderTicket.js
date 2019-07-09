Ext.Loader.setConfig({
	enabled: true
});

Ext.require([
	'Ext.grid.*',
	'Ext.data.*',
	'Ext.selection.CheckboxModel',
    'MB.ComboModel',
    'MB.OrderSettleBill',
    'Ext.dom.Element'
]);

var addOrderTicketForm = null;
var addOrderTicketGrid = null;
var addOrderTicketJsonStore = null;
var columns = null;

var channelTypeCombo  =null;

var channelCombo = null;

var shopCombo = null;

var orderSettleBillListUrl = basePath+"/custom/orderSettleBill/createOrderSettleBill.spmvc";


var insertOrderBillListUrl = basePath+"/custom/orderSettleBill/insertOrderBillList.spmvc";

	//	http://localhost:8080/OmsManager//custom/orderSettleBill/insertOrderBillList.spmvc

//按钮是否置灰
var isDisabled = $("#isDisabled").val();

var billNo = $("#billNo").val() == null ?  "": $("#billNo").val();

Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var pageSize = 20;
	
	var isHistory = 0;

	//单据状态model
/*	var documentsStateDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.documents_state
	});*/
	//单据状态
//	var orderTypeOptionCombo = createComboBoxLocal(documentsStateDataStore,'orderTicketPageDocumentsState', 'n','v', 'local','单据状态', 230, 80, 'documentsState', '请选择单据状态');
	
	//var collapseResize = false;
	
/**********************************	*/
	
	var channelStore= new Ext.data.Store({
		model:"MB.Channel",
		proxy: {
			type: 'ajax',
			url:basePath+'custom/common/getChannelInfos?channelType=0', //这里是参数可以顺便写,这个数据源是在第一个下拉框select的时候load的
			reader:  {type:'json'}
		}
	});
	
	//一级订单来源
	var channelTypeStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.channel_type
	});

	var selectChannelType = function (combo, record,index) {
		var orderFromFirst = $("#orderFromFirst").val();
		
		if(null != orderFromFirst && "" != orderFromFirst){
			combo.setValue(orderFromFirst);	
			
			
			Ext.getCmp('addOrderTicketPageOrderFromFirst').setDisabled(true);
		}
	}
	
	
	var changeChannelType = function (combo, record,index) {
	
		channelCombo.clearValue();
		channelCombo.setValue();
			//改变二级菜单
			if(combo.value==null) {
				channelStore.getProxy().url=basePath+'custom/common/getChannelInfos?channelType=0';
			}else {
				channelStore.getProxy().url=basePath+'custom/common/getChannelInfos?channelType='+combo.value;
			}
			channelStore.load();
	};
	
	//订单来源
	 channelTypeCombo = createComboBoxLocal(channelTypeStore,'addOrderTicketPageOrderFromFirst', 'n','v', 'local','订单来源', 240, 80, 'orderFromFirst', '请选择渠道类型');
	// 添加change监听事件
	channelTypeCombo.addListener('change',changeChannelType);
	channelTypeCombo.addListener('afterRender',selectChannelType);
	


	
/*	var orderFromFirst = $("#orderFromFirst").val();
	
	if(null != orderFromFirst && "" != orderFromFirst){
		channelTypeCombo.setValue(orderFromFirst);	

	}
*/
	
	
//////////////////////////////////////////////	
	
	
	var selectChannelCode = function (combo, record,index) {
		var channelCode = $("#channelCode").val();
			
			if(null != channelCode && "" != channelCode){
				combo.setValue(channelCode)
				Ext.getCmp('addOrderTicketPageOrderFromSec').setDisabled(true);
			}
	}
	
	var changeChannelCode = function (combo, record, index) {

		var isDisabled = $("#isDisabled").val();
		
		if(1 == isDisabled){
			shopCombo.setValue('');
		}
	
	//	orderMemberOptionCombo.setValue(-1);
		if(combo.value==null) {
			shopStore.getProxy().url=basePath+"custom/common/getChannelShops";
		}else {
		/*	if(combo.value=="TB_CHANNEL_CODE" || combo.value=="TMALL_CHANNEL_CODE" || combo.value=="TBFX_CHANNEL_CODE") {
			//	orderMemberDataStore.loadData(getdata(SELECT.taobao),false);
			} else if(combo.value=="BG_CHANNEL_CODE") {
			//	orderMemberDataStore.loadData(getdata(SELECT.mall),false);
			} else {
			//	orderMemberDataStore.removeAll();
			//	orderMemberDataStore.loadData([]);
			}*/
			shopStore.getProxy().url=basePath+"custom/common/getChannelShops?channelCode="+ combo.value;
		}
		shopStore.load();

	};
	//渠道
	 channelCombo = createComboBoxLocal(channelStore,"addOrderTicketPageOrderFromSec",'channelTitle','chanelCode',"remote", "渠道", 230, 80, "orderFromSec","请选择渠道");
	 channelCombo.addListener('change',changeChannelCode);
	 channelCombo.addListener('afterRender',selectChannelCode);
	 
/*	   var channelCode = $("#channelCode").val();
		
		if(null != channelCode && "" != channelCode){
			channelCombo.setValue(channelCode);	

		}*/
	
	/////////////////////
	
	
	
    var selectShopCombo = function (combo, record, index) {
	
		var shopCode = $("#shopCode").val();
	
		if(null != shopCode && "" != shopCode){
			combo.setValue(shopCode);
			Ext.getCmp('addOrderTicketPageOrderFrom').setDisabled(true);
		}

	};
	
	var shopStore= new Ext.data.Store({
		model:"MB.Shop",
		proxy:{
			type: 'ajax',
			url: basePath+'custom/common/getChannelShops' ,
			reader: {
				type:'json'
			}
		}
	});
	
	//渠道店铺
	 shopCombo = createComboBoxLocal(shopStore,"addOrderTicketPageOrderFrom",'shopTitle','shopCode',"remote", "渠道店铺",  300, 80, "orderFroms","请选择渠道店铺");
	 shopCombo.addListener('afterRender',selectShopCombo);
	 
/*	 var shopCode = $("#shopCode").val();
		
		
	if(null != shopCode && "" != shopCode){
		shopCombo.setValue(shopCode);	

	}*/

/**************************/	
	addOrderTicketForm = Ext.widget('form', {
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
//		alias : 'widget.addOrderTicket',
		frame: true,
		collapsible: true,
		title: "订单结算列表",
		fieldDefaults: {
			labelAlign: 'right'
		
		},

		items: [
			  {//第一行
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'hbox',
				defaultType: 'textfield',
				fieldDefaults: {
					labelAlign: 'right'
				},
				items: [  
				        
						channelTypeCombo,
						channelCombo,
						shopCombo
		
				]
			},{//第二行
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'hbox',
				defaultType: 'textfield',
				fieldDefaults: {
					labelAlign: 'right'
				},
				items: [  			  
				       { 
							xtype : "textfield",
							labelWidth: 80,
							id:'addOrderTicketPageBillNo',
							disabled: true,
							name: 'billNo',
							fieldLabel : "调整单号",
							value:billNo,
							width : 240
						}
				
				]
			}
		
	   ]//,
	/*buttons : [
      
	     {
			text : '查询',
			columnWidth : .1,
			handler : search,
		}, {
			text : '重置',
			handler : function () {
				resetButton(addOrderTicketForm);
	
		     	Ext.getCmp("orderInfoPageOrderCategory").setValue(-1);
				Ext.getCmp("orderInfoPageTransType").setValue(-1);
				Ext.getCmp("orderInfoPageOrderStatus").setValue(-1);
				Ext.getCmp("orderInfoPageReferer").setValue(-1);
				Ext.getCmp("orderInfoPageIsGroup").setValue(-1);
				Ext.getCmp("orderInfoPageIsAdvance").setValue(-1);
				Ext.getCmp("orderInfoPageOrderView").setValue(-1);
				Ext.getCmp("orderInfoPagePayId").setValue(-1);
				Ext.getCmp("orderInfoPagePayStatus").setValue(-1);
				Ext.getCmp("orderInfoPageShipStatus").setValue(-1);
				Ext.getCmp("orderInfoPageShippingId").setValue(-1);
				Ext.getCmp("orderInfoPageDepotStatus").setValue(-1);
				Ext.getCmp("orderInfoPageOrderType").setValue(-1);
				Ext.getCmp("orderInfoPageUserName").setValue("");
			
				Ext.getCmp("orderInfoPageSkuSn").setValue("");
				
				orderViewOptionCombo.setValue('0');
				orderMemberOptionCombo.setValue(-1);
				channelTypeCombo.setValue(0);
				channelCombo.setValue('');
				shopCombo.setValue('');
				countryCombo.setValue('');
				provinceCombo.setValue('');
				cityCombo.setValue('');
				districtCombo.setValue('');

				timeOptionCombo.setValue("addTime");
				
				Ext.getCmp("orderInfoPageMobile").setValue("");
				Ext.getCmp("orderInfoPageTel").setValue("");
				Ext.getCmp("orderInfoPageInvoiceNo").setValue("");
				
			}
		}
		],*/
	/*	buttonAlign: 'right',
		listeners: {
			afterRender: function(thisForm, options){
				this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
					enter: function(){  
						search();
					}
				});
			},
			resize: function(){
				if (collapseResize) {
					setResize(addOrderTicketForm,addOrderTicketGrid, null, 5);
				}
			}
		}*/
	});
	var selModel = Ext.create('Ext.selection.CheckboxModel', {
		
		/*listeners: {
			selectionchange: function(sm, selections, aa) {
				addOrderTicketGrid.down('#confirmButton').setDisabled((selections.length === 0 || batchConfirmAuth));
			}
		}*/
		
	});
	
    //调整单号不为空
//	if(null != $("#billNo").val() && "" != $("#billNo").val()){
//		var billNo	 =  $("#billNo").val();
	
	
	addOrderTicketJsonStore = createObjectGridStore(orderSettleBillListUrl+"?billNo="+billNo, pageSize, "MB.OrderSettleBill");
		
	//} else {
//		addOrderTicketJsonStore = createObjectGridStore("", pageSize, "MB.OrderSettleBill");
//	}

	//addOrderTicketJsonStore = createObjectGridStore(orderSettleBillListUrl, pageSize, "MB.OrderSettleBill");
	
	columns = [
		{ id : 'billNo', header : "单据批次号", align : "center", width : 120, dataIndex:'billNo', sortable:false},
		{ id : 'orderCode', header : "订单号或外部交易号", align : "center", width : 220, dataIndex : 'orderCode', sortable:false},
		{ id : 'money', header : "金额", align : "center", width : 495, dataIndex : 'money', sortable:false}
	];
	
	var dItems = [{
		xtype: 'toolbar',
		dock: 'top',
		items: [
			{
				id: 'orderSettleBillTBarAdd',
				iconCls: 'add',
				text: '批量添加',
			//	tooltip : '生成调整单',
			//	disabled: true,
			    disabled: ButtonDis(isDisabled),
				handler : function() {
			
					//批次号
					var billNo = $("#billNo").val() == null ?  "": $("#billNo").val();
					
					FormEditWin.showAddDirWin("popWins",basePath+"/custom/orderSettleBill/enterExportTicketPage.spmvc?billNo="+billNo,
							"orderSettlement_winID","导入订单结算单",550,300);
				}
			}/*,{
				id : 'orderSettleBillTBarSave',
				text : '保存调整单',
			//	tooltip : '执行调整单',
				iconCls : 'save',
			//	disabled: ButtonDis(addQuestion),
			//	disabled: ButtonDis(isDisabled),
				handler : function() {
					
					var orderFrom =  Ext.getCmp("addOrderTicketPageOrderFrom").getValue();
					
					alert(orderFrom);
					var billNo = $("#billNo").val();
			
					createAjaxData("saveOrderBillList", insertOrderBillListUrl, doSuccessfun, doFailurefun, {"billNo":billNo,"channelCode":orderFrom}, 100000);
					
				//	FormEditWin.showAddDirWin("popWins",basePath+"/custom/orderQuestion/gotoImportQuestionPage",
					//		"importOrderLogistics_winID","导入问题单",700,394);
					
				}
			}*/
	
			// ,
			/* {
				id : 'order_settle_bill_tBar-del',
				text : '批量删除',
			//	tooltip : '执行调整单',
				iconCls : 'del',
			//	disabled: ButtonDis(addQuestion),
				handler : function() {
				//	FormEditWin.showAddDirWin("popWins",basePath+"/custom/orderQuestion/gotoImportQuestionPage",
					//		"importOrderLogistics_winID","导入问题单",700,394);
				}
			} ,*/
		/*	{
				id : 'order_settle_bill_tBar-update',
				text : '更新调整单',
			//	tooltip : '删除调整单',
				iconCls : 'update',
			//	disabled: ButtonDis(addQuestion),
				handler : function() {
					
					
				//	FormEditWin.showAddDirWin("popWins",basePath+"/custom/orderQuestion/gotoImportQuestionPage",
					//		"importOrderLogistics_winID","导入问题单",700,394);
				}
			}*/
	    ]
	}];
	
	addOrderTicketGrid = createGridPanelByExt4('orderSettleBillPageAddOrderTicketGrid', null, null, addOrderTicketJsonStore,pageSize, null, columns, null,dItems);
	
//	addOrderTicketGrid = createGridPanelByExt4('orderSettleBill', null, null, null,pageSize, null, columns, selModel,dItems);
	
	var mainPanel = Ext.widget('panel', {
		renderTo: Ext.getBody(),
		width: '100%',
		height:'500',
		resizable: false,
		autoHeight: true,
		items: [addOrderTicketForm, addOrderTicketGrid]
	});
	
	//查询翻页后带入查询数据
//	addOrderTicketJsonStore.on('beforeload', function (store, options){
		
	/*	var searchParams = getFormParams(addOrderTicketForm);
		var userName = Ext.getCmp("orderInfoPageUserName").getValue();
		var consignee = Ext.getCmp("orderInfoPageConsignee").getValue();
		var shippingId = shippingIdOptionCombo.getValue();
		var country = countryCombo.getValue();
		var province = provinceCombo.getValue();
		var city = cityCombo.getValue();
		var district = districtCombo.getValue();

		var skuSn = Ext.getCmp("orderInfoPageSkuSn").getValue();
		searchParams.userName = userName;
		searchParams.consignee = consignee;
		searchParams.shippingId = shippingId;
		searchParams.country = country;
		searchParams.province = province;
		searchParams.city = city;
		searchParams.district = district;
	
		searchParams.skuSn = skuSn;
		searchParams.mobile = Ext.getCmp("orderInfoPageMobile").getValue();
		searchParams.tel = Ext.getCmp("orderInfoPageTel").getValue();
		searchParams.invoiceNo = Ext.getCmp("orderInfoPageInvoiceNo").getValue();
		Ext.apply(store.proxy.extraParams, searchParams);*/
		
//	});
	
/*	function editOrderStatus(value, cellmeta, record) {
		var orderStatus = record.get('orderStatus');
		var payStatus = record.get('payStatus');
		var shipStatus = record.get('shipStatus');
		return getCombineStatus(orderStatus, payStatus, shipStatus);
	}*/
	
/*	function showHiddenCondition(b, c){
		// 显示 隐藏 
		collapseResize = false;
		
	
		if(Ext.getCmp("moreSelect1").isHidden()) {

			b.setText('隐藏更多检索条件');

			Ext.getCmp("moreSelect1").setHidden(false);
			Ext.getCmp("moreSelect2").setHidden(false);
			Ext.getCmp("moreSelect3").setHidden(false);
			Ext.getCmp("moreSelect4").setHidden(false);

		}else {
		
			b.setText('展示更多检索条件');
	
			Ext.getCmp("moreSelect1").setHidden(true);
			Ext.getCmp("moreSelect2").setHidden(true);
			Ext.getCmp("moreSelect3").setHidden(true);
			Ext.getCmp("moreSelect4").setHidden(true);
		
		
		}
		setResize(addOrderTicketForm,addOrderTicketGrid, null, 5);
	}*/
	
	function batchConfirm() {
		//批量确认被选择的行订单
		var selModel = addOrderTicketGrid.getSelectionModel();
		if (selModel.hasSelection()) {
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				var orderSn = records[i].get("orderSn");
				var orderStatus = records[i].get("orderStatus");
				if (orderStatus == 0) {
					if(orderSn && orderSn != ''){
						orderSns.push(orderSn);
					}
				}
			}
			if(orderSns && orderSns.length > 0){
				promptMsg("确认被选择的订单！","备注：", function(btn, text) {
					if (btn == "ok") {
						createAjaxData("batchConfirm", batchConfirmUrl, confirmSuccFun,
								doFailurefun, {"orderSns":orderSns,"message": text}, 100000);
					}
				});
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有未确认订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量确认的订单!");
		}	
	}
	
	function confirmSuccFun (id, response, opts) {
		var respText = Ext.JSON.decode(response.responseText);
		if(respText.success == "true"){
			addOrderTicketJsonStore.load();
		}else{
			addOrderTicketJsonStore.load();
			errorMsg("结果", respText.msg);
		}
	}
	
	function doFailurefun(){
		var respText = Ext.JSON.decode(response.responseText);
		errorMsg("结果", respText.msg);
	}
	
	function search(){
		
	/*	var initParams = {start : 0, limit : pageSize };
		var searchParams = getFormParams(addOrderTicketForm, initParams);
		var userName = Ext.getCmp("orderInfoPageUserName").getValue();
		var consignee = Ext.getCmp("orderInfoPageConsignee").getValue();
		var shippingId = shippingIdOptionCombo.getValue();
		var country = countryCombo.getValue();
		var province = provinceCombo.getValue();
		var city = cityCombo.getValue();
		var district = districtCombo.getValue();
	
		var skuSn = Ext.getCmp("orderInfoPageSkuSn").getValue();
		searchParams.userName = userName;
		searchParams.consignee = consignee;
		searchParams.shippingId = shippingId;
		searchParams.country = country;
		searchParams.province = province;
		searchParams.city = city;
		searchParams.district = district;
	
		searchParams.skuSn = skuSn;
		searchParams.mobile = Ext.getCmp("orderInfoPageMobile").getValue();
		searchParams.tel = Ext.getCmp("orderInfoPageTel").getValue();
		searchParams.invoiceNo = Ext.getCmp("orderInfoPageInvoiceNo").getValue();
		addOrderTicketGrid.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		addOrderTicketGrid.store.pageSize = pageSize;
		addOrderTicketGrid.store.load({params : searchParams});
		*/
	}
	
	function exportRecord() {// 导数据
		var searchParams = getFormParams(addOrderTicketForm);
		if (addOrderTicketGrid.store.totalCount > 10000) {
			confirmMsg("确认","根据查询结果导出数据条数大于10000条，要继续导出吗?", function(btn) {
				if (btn == "yes") {
					var myMask = new Ext.LoadMask(Ext.getBody(),{msg:"请稍等,正在导出..."}); 
					myMask.show(); 
					Ext.Ajax.request({
						timeout: 1800000,//1800秒 半小时
						url: basePath+"custom/orderInfo/orderInfoExportCsvFile.spmvc",
						params: searchParams,
						success: function(response){
							if (myMask != undefined){ myMask.hide();}
							var obj = Ext.decode(response.responseText);
							var path = obj.data.path;
							var fileName = obj.data.fileName;
							
							if(obj.isok==true){
								window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
							}else{
								Ext.Msg.alert("错误", "导出失败");
							}
						},
						failure:function(){
							if (myMask != undefined){ myMask.hide();}
							Ext.Msg.alert("错误", "导出失败");
						} 
					});
				} else {
					return ;
				}
			});
		} else {
			var myMask = new Ext.LoadMask(Ext.getBody(),{msg:"请稍等,正在导出..."}); 
			myMask.show(); 
			Ext.Ajax.request({
				timeout: 1800000,//1800秒 半小时
				url: basePath+"custom/orderInfo/orderInfoExportCsvFile.spmvc",
				params:searchParams,
				success: function(response){
					if (myMask != undefined){ myMask.hide();}
					var obj = Ext.decode(response.responseText);
					var path = obj.data.path;
					var fileName = obj.data.fileName;
					if(obj.isok==true){
						window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
					}else{
						alert("导出失败");
					}
				},
				failure:function(){
					if (myMask != undefined){ myMask.hide();}
					alert("导出失败");
				} 
			});
		}
	}
	function fixOrderSn(value) {
		if("1" == display && value != undefined && value != null ){
			var url = order_info_url + value +'&isHistory=' + isHistory;
			return "<a href=" +url + " target='_blank'  >" + value + "</a>";
		} else {
			return '<font>' + value + '</font>';
		}
	}
	
	function fixLockStatus(v, md, r) {
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
	
	function fixOrderOutSn(value, md, r) {
		if (value != undefined && value != null ) {
			var orderSn =  r.get('orderSn');
			var url = order_info_url + orderSn +"&isHistory=" + isHistory;
			return "<a href=" +url + " target='_blank' >" + value + "</a>";
		}
	}
	function fixRelatingExchangeSn(value, md, r) {
		if (value != undefined && value != null ) {
			var orderSn =  r.get('relatingExchangeSn');
			var orderType =  r.get('orderType');
			if (orderType && orderType == '2') {
				var url = order_info_url + orderSn + "&isHistory=" + isHistory;
				return "<a href=" +url + " target='_blank' >" + value + "</a>";
			}
		}
	}
	function fixQuestionStatus(v) {
		var str = "";
		if (v == 0) {
			str = '<font style="color:green;">正常单</font>';
		} else {
			str = '<font style="color:red;">问题单</font>';
		}
		return str;
	} 
	function fixTimeoutStatus(v) {
		var str = "";
		if (v == 0) {
			str = '<font style="color:green;">正常单</font>';
		} else {
			str = '<font style="color:red;">超时单</font>';
		}
		return str;
	}
	
	window.onresize=function(){
		setResize(addOrderTicketForm,addOrderTicketGrid, null, 5);
	};
	setResize(addOrderTicketForm,addOrderTicketGrid, null, 5);
});

function searchInExport(){
	
	var searchParams = getFormParams(addOrderTicketForm);
	var userName = Ext.getCmp("orderInfoPageUserName").getValue();
	var consignee = Ext.getCmp("orderInfoPageConsignee").getValue();
	var shippingId = shippingIdOptionCombo.getValue();
	var country = countryCombo.getValue();
	var province = provinceCombo.getValue();
	var city = cityCombo.getValue();
	var district = districtCombo.getValue();

	var skuSn = Ext.getCmp("orderInfoPageSkuSn").getValue();
	searchParams.userName = userName;
	searchParams.consignee = consignee;
	searchParams.shippingId = shippingId;
	searchParams.country = country;
	searchParams.province = province;
	searchParams.city = city;
	searchParams.district = district;
	
	searchParams.skuSn = skuSn;
	searchParams.mobile = Ext.getCmp("orderInfoPageMobile").getValue();
	searchParams.tel = Ext.getCmp("orderInfoPageTel").getValue();
	searchParams.invoiceNo = Ext.getCmp("orderInfoPageInvoiceNo").getValue();
	return searchParams;
	
}

function exportAjax(exportTemplateType){
	var searchParams = searchInExport();

	var url = basePath
			+ '/custom/orderInfo/orderInfoExportCsvFile.spmvc?exportTemplateType='+exportTemplateType;

	Ext.getCmp('orderInfoExportBtn').setDisabled(true);

	var myMask = new Ext.LoadMask({
	    msg    : '请稍等,正在导出...',
	    target : addOrderTicketForm
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

/*function buttonShow(){
	var isDisabled = $("#isDisabled").val();
	
	if("1"==isDisabled){
		Ext.getCmp("orderSettleBillTBarAdd").setDisabled(true);
	}
}*/

//buttonShow();

/***
 *ajax结果 
 ***/
function doSuccessfun(id, response, opts) {

	if ("saveOrderBillList" == id) {
		var respText = Ext.JSON.decode(response.responseText);
		
		var data = respText.data;
		
		if(respText.isok == true){
		//	Ext.getCmp("orderSettleBillTBarAdd").setDisabled(false);
			Ext.Msg.alert("结果",respText.message);
			
			
			addOrderTicketJsonStore.load();
			parent.FormEditWin.close();
		}else{
			addOrderTicketJsonStore.load();
			errorMsg("结果", respText.message);
		}
		
	}

}

/***
 *ajax错误 
 ***/
function doFailurefun(id, response, opts) {
	errorMsg("结果", respText.msg);
}
