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

var pageSize = 20;

var orderSettleBillListUrl = basePath+"/custom/orderSettleBill/orderDepositList.spmvc";

var getOrderSettleBillUrl = basePath+"/custom/orderSettleBill/getOrderSettleBill.spmvc";

var mqUrl = basePath+"/custom/orderSettleBill/depositActiveMQ.spmvc";

var deleteOrderSettleBillUrl =  basePath+"/custom/orderSettleBill/deleteOrderSettleBill.spmvc";

var orderDepositForm = null;

var orderDepositGrid = null;

var orderDepositJsonStore = null;

Ext.onReady(function(){
	Ext.QuickTips.init();

	var columns = null;

	var isHistory = 0;

	//单据状态model
	var documentsStateDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.documents_settlement_state
	});
	//单据状态
	var orderTypeOptionCombo = createComboBoxLocal(documentsStateDataStore,'orderTicketPageIsSync', 'n','v', 'local','同步状态', 230, 80, 'isSync', '请选择同步状态');

	var collapseResize = false;
	
	var settlementBillTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'MB.ComboModel',
		data: SELECT.documents_settlement_bill_type
	});
	
	var billTypeOptionCombo = createComboBoxLocal(settlementBillTypeDataStore,'orderTicketPageBillType', 'n','v', 'local','业务类型', 230, 80, 'billType', '请选择业务类型');

	orderDepositForm = Ext.widget('form', {
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,

		frame: true,
		collapsible: true,
		title: "保证金列表",
		fieldDefaults: {
			labelAlign: 'right'
		
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
			        
			   /*     { 
						xtype : "textfield",
						labelWidth: 80,
						id:'orderTicketPageChannelCode',
						name: 'channelCode',
						fieldLabel : "渠道号",
						width : 230
					}, */
			   		{ 
						xtype : "textfield",
						labelWidth: 80,
						id:'orderTicketPageBillNo',
						name: 'billNo',
						fieldLabel : "调整单号",
						width : 230
					},
					orderTypeOptionCombo//,
				//	billTypeOptionCombo
					
			]
			
		}	
	],
	buttons : [
   
	     {
			text : '查询',
			columnWidth : .1,
			handler : search
		}, {
			text : '重置',
			handler : function () {
				resetButton(orderDepositForm);
	
	/*	     	Ext.getCmp("orderInfoPageOrderCategory").setValue(-1);
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
				Ext.getCmp("orderInfoPageUserName").setValue("");*/
		
				
	/*			Ext.getCmp("orderInfoPageSkuSn").setValue("");
				
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
				Ext.getCmp("orderInfoPageInvoiceNo").setValue("");*/
			}
		}
		
		/*, {
			 id: "orderInfoExportBtn",
			text : '导出',
			menu: {
			    items: [
    	            {
    	                text: '默认模版',
    	                handler: function () {
	
    	                //	exportAjax("0");
    	                }
    	            }, {
    	                text: '财务模版',
    	                handler: function () {	               
    	                //	exportAjax("1");               	
    	                }	
    	            },  {
    	                text: '物流模版',
    	                handler: function () {           
    	                //exportAjax("2");     	
    	                }	
    	            }
			    ]
			}
		}*/
		
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
				if (collapseResize) {
					setResize(orderDepositForm,orderDepositGrid, null, 5);
				}
			}
		}
	});
	var selModel = Ext.create('Ext.selection.CheckboxModel', {
		
		/*listeners: {
			selectionchange: function(sm, selections, aa) {
				orderDepositGrid.down('#confirmButton').setDisabled((selections.length === 0 || batchConfirmAuth));
			}
		}*/
		
	});

	orderDepositJsonStore = createObjectGridStore(orderSettleBillListUrl, pageSize, "MB.OrderSettleBill");
	
//	orderDepositJsonStore = createObjectGridStoreLazy(orderSettleBillListUrl, pageSize, "MB.OrderSettleBill");

	columns = [
		{ id : 'billNo', header : "单据批次号", align : "center", width : 220, dataIndex:'billNo', /* renderer: fixLockStatus, */sortable:false},
//		{ id : 'orderCode', header : "交易号", align : "center", width : 180, dataIndex : 'orderCode', sortable:false},
		{ id : 'channelCode', header : "渠道号", align : "center", width : 120, dataIndex : 'channelCode', sortable:false},
		{ id : 'billTypeStr', header : "业务类型", align : "center", width : 200, dataIndex : 'billTypeStr',sortable:false},
		{ id : 'actionUser', header : "操作人", align : "center", width : 120, dataIndex : 'actionUser',sortable:false},
		{ id : 'note', header : "备注", align : "center", width : 120, dataIndex : 'note',sortable:false},	
		{ id : 'isSycStr', header : "同步状态", align : "center", width : 120, dataIndex : 'isSycStr',sortable:false},
		{ id : 'execTime', header : "执行时间", align : "center", width : 220, dataIndex : 'execTime',sortable:false},
		{ id : 'addTime', header : "添加时间", align : "center", width : 220, dataIndex : 'addTime',sortable:false},
		{ id : 'updateTime', header : "更新时间", align : "center", width : 220, dataIndex : 'updateTime',sortable:false},	
		{  header : "执行结果", align : "center", width : 220, dataIndex : 'isSync',sortable:false,
			renderer: function(value, metaData, record, rowIndex) {
					var id = Ext.id();
					var billNo = record.get('billNo');
					var downLoadFlag = false;
					if (value == 9 || value == 0) {
						downLoadFlag = true;
					}
					// 支付状态和权限
					setTimeout(function() {
						var panel = Ext.create('Ext.panel.Panel', {
							bodyPadding: 0,
							border:false,
							baseCls: 'my-panel-no-border',
//							width: 100,
							height: 20,
							layout: 'column',
							columnWidth : 1,
							items: [{
								columnWidth: 1,
								xtype: 'segmentedbutton',
								margin:'0 50 ',
								allowToggle: false,
								items: [{
									text: '下载',
									disabled: downLoadFlag,
									handler: function () {
										 downLoadMsg(billNo,billTypeOptionCombo.getValue());
									}
								}]
							}]
						});
						if (Ext.get(id)) {
							panel.render(Ext.get(id));
						}
					}, 1);
					return '<div id="' + id + '"></div>';
				}
		}
	];

	var dItems = [{
		xtype: 'toolbar',
		dock: 'top',
		items: [
			{
				id: 'order_settle_bill_tBar-normal',
				iconCls: 'add',
				text: '生成调整单',
			//	tooltip : '生成调整单',
			//	disabled: ButtonDis(toNormal),
				handler : function() {
					FormEditWin.showAddDirWin("popWins",basePath+"/custom/orderSettleBill/createOrderDeposit.spmvc?method=init",
							"orderSettlement_winID","创建订单结算 ",900,500);
				}
			} , {
				id : 'order_settle_bill_tBar-perform',
				text : '执行调整单',
			//	tooltip : '执行调整单',
				iconCls : 'add',
			//	disabled: ButtonDis(addQuestion),
				handler : function() {
					
					var selModel = orderDepositGrid.getSelectionModel();
		
					
					if (selModel.hasSelection()) {
								
						       var records = selModel.getSelection();
						
								var ids = "";
						
								for ( var i = 0; i < records.length; i++) {
									var id =records[i].get("billNo");
							
									//var is_timing = records[i].get("isTiming");
								//	var note = records[i].get("note");
									
								//	if()
									
								/*	if (error == "2" || error == "3") {
										alertMsg("结果", "请检查单据状态！");
										return;
									}
									if (error == "1" && note != "") {
										alertMsg("结果", "请检查单据状态！");
										return;
									}*/
									
									if (id && id != '' && id != null) {
										ids += "" + id + ",";
									}
			
								}
						
								if(null != ids && "" != ids ){
									Ext.Ajax.request({
											url: basePath+"/custom/orderSettleBill/checkSettleList",
											waitMsg : '请稍等.....',
											timeout:100000,
											async : false,
											success: function(response,opts){
												var respText = Ext.JSON.decode(response.responseText);
													if (respText.isok == true) {
//														Ext.Msg.alert('结果',respText.message); 
														createAjaxDataBySyn("perform", mqUrl, doSuccessfun, doFailurefun, {"ids":ids}, 100000);
													} else {			
														errorMsg("结果", respText.message);	 		
													}
											},
											failure: function(response,opts){
													createAlert("提示信息", "请求数据失败!");
											},
											//scriptTag: true,
											params: {"ids":ids}
										});
								}
					}
	
				}
			} , {
				id : 'order_settle_bill_tBar-import',
				text : '删除调整单',
			//	tooltip : '删除调整单',
				iconCls : 'del',
			//	disabled: ButtonDis(addQuestion),
				handler : function() {
					
					  var records = selModel.getSelection();
					
					var billNos = "";
			
					for ( var i = 0; i < records.length; i++) {
						var billNo =records[i].get("billNo");
				     
						if (billNo && billNo != '' && billNo != null) {
							billNos += "" + billNo + ",";
						}

					}
					
					if(null != billNo && "" != billNo ){
						createAjaxDataBySyn("deleteOrderTicket", deleteOrderSettleBillUrl, doSuccessfun, doFailurefun, {"billNos":billNos}, 100000);
					}
			
				}
			}
	    ]
	}];

	orderDepositGrid = createGridPanelByExt4('orderBillListPageorderDepositGrid', null, null, orderDepositJsonStore,
			pageSize, null, columns, selModel,dItems);
	
	//双击
	var billNoListener = function (combo, record,index) {
		var billNo = record.get("billNo");
		
		FormEditWin.showAddDirWin("popWins",basePath+"/custom/orderSettleBill/createOrderDeposit.spmvc?method=update&billNo="+billNo,
				"orderSettlement_winID","创建订单结算 ",900,500);
	
	//	var channelCode = record.get("channelCode");

	/*	FormEditWin.showAddDirWin("popWins",basePath+"/custom/orderSettleBill/createOrderSettleBill.spmvc?method=update&billNo="+billNo+"&channelCode="+channelCode,
				"orderSettlement_winID","创建订单结算 ",900,500);*/
	};
	
	orderDepositGrid.addListener('rowdblclick',billNoListener);
	
	var mainPanel = Ext.widget('panel', {
		renderTo: Ext.getBody(),
		width: '100%',
		resizable: false,
		autoHeight: true,
		items: [orderDepositForm, orderDepositGrid]
	});
	
	//查询翻页后带入查询数据
	orderDepositJsonStore.on('beforeload', function (store, options){
		
	/*	var searchParams = getFormParams(orderDepositForm);
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
		
	});
	
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
		setResize(orderDepositForm,orderDepositGrid, null, 5);
	}*/
	
	function batchConfirm() {
		//批量确认被选择的行订单
		var selModel = orderDepositGrid.getSelectionModel();
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
			orderDepositJsonStore.load();
		}else{
			orderDepositJsonStore.load();
			errorMsg("结果", respText.msg);
		}
	}
	
	function doFailurefun(){
		var respText = Ext.JSON.decode(response.responseText);
		errorMsg("结果", respText.msg);
	}
	
	function search(){
		
	 var initParams = {start : 0, limit : pageSize };
		var searchParams = getFormParams(orderDepositForm, initParams);
	/*	var userName = Ext.getCmp("orderInfoPageUserName").getValue();
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
		searchParams.invoiceNo = Ext.getCmp("orderInfoPageInvoiceNo").getValue();*/
		
		var isSync = Ext.getCmp("orderTicketPageIsSync").getValue(); //同步状态
		var billType = Ext.getCmp("orderTicketPageBillType").getValue();
		
		searchParams.isSync= isSync;
		searchParams.billType = billType;
		
		
		orderDepositGrid.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		orderDepositGrid.store.pageSize = pageSize;
		orderDepositGrid.store.load({params : searchParams});
	
		
	}
	
	function exportRecord() {// 导数据
		var searchParams = getFormParams(orderDepositForm);
		if (orderDepositGrid.store.totalCount > 10000) {
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
		setResize(orderDepositForm,orderDepositGrid, null, 5);
	};
	setResize(orderDepositForm,orderDepositGrid, null, 5);
});

function searchInExport(){
	
	var searchParams = getFormParams(orderDepositForm);
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
	    target : orderDepositForm
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

function doSuccessfun(id, response, opts) {
	if ("perform" == id) {
		var respText = Ext.JSON.decode(response.responseText);
		
		if (respText.isok == true) {
			//showResultMsg("结果", respText.message);	
			
			Ext.Msg.alert('结果',"执行成功！"); 
			orderDepositJsonStore.load();
		} else {			
			errorMsg("结果", respText.message);	 		
		}
	} else  if ("deleteOrderTicket" == id) {
		var respText = Ext.JSON.decode(response.responseText);
		
		var data = respText.data;
		
		if (respText.isok == true) {
			//showResultMsg("结果", respText.message);	
			
			 Ext.Msg.alert('结果',"删除成功！"); 
			orderDepositJsonStore.load();
		} else {			
			errorMsg("结果", respText.message);	 		
		}
	}
}

function doFailurefun(id, response, opts) {
	
	errorMsg("结果", respText.message);
}


//退单结算日志下载
function downLoadMsg(billNo,billType){
	var myMask = new Ext.LoadMask({
	    msg    : '请稍等,正在下载...',
	    target : orderDepositForm
	});
	
	myMask.show();
	Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : basePath+ '/custom/orderSettleBill/orderReturnSettleExportCsv',
			method : 'post',
			timeout : 7200000,
			method : 'post',
			params: {"billNo":billNo,"billType":billType},
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
//				Ext.getCmp('orderInfoExportBtn').setDisabled(false);
			},
			failure : function(response) {
				myMask.hide();
				Ext.Msg.alert("验证", "失败");
//				Ext.getCmp('orderInfoExportBtn').setDisabled(false);
			}
	});
}


