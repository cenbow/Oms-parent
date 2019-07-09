/**
 * 转单工具
 */
Ext.onReady(function(){
	var channelDataStore=new Ext.data.SimpleStore( {
		model: Ext.create('Ext.data.Model',{
			fields: [
			            {name: 'shortText'},
			            {name: 'channelCode'}
			        ]
		}),
		autoLoad : true,
		proxy: {
			type: 'ajax',
			actionMethods: {
				read: 'POST'
			},
			url: basePath + 'custom/common/loadOrderShopData?dataType=1',//0拉单、1转单、2发货、3退单
			reader: {
				type:'json'
			}
		}
	});
 
	var typeOptionCombo = new Ext.form.ComboBox({
		id : 'channelCode',
		store :  channelDataStore,
		xtype : 'combo',
		valueField : 'channelCode',
		displayField : 'shortText',
		mode : 'remote',
		forceSelection : true,
		emptyText : '请选择渠道',
		editable : false,			
		triggerAction : 'all',
		fieldLabel : '渠道',
		labelWidth: 90,
		width : 250
	});
	
	
	var convertStatusDataStore=new Ext.data.SimpleStore( {
		data :[["","全部"],["0","未转单"],["1","已转单"]],
		fields : [ 'text', 'filed' ]
	});
	
	var convertStatusOptionCombo=new Ext.form.ComboBox({
		store: convertStatusDataStore,
		id: "convertStatus",
		name: "convertStatus",
		displayField: "filed",
		valueField: "text", 
		mode: "local",
		width:250,
		labelWidth: 90,
		triggerAction: 'all',
		selectOnFocus:true,
		forceSelection:true,
		hiddenName: "convertStatus",
		emptyText: "请转单状态",
		fieldLabel: "转单状态",
		editable: false // 不可输入
	});
	
	var shipStatusDataStore=new Ext.data.SimpleStore( {
		data :[["","全部"],["2","未发货"],["0","已发货未同步"],["1","已发货已同步"],["-1","已发货同步异常"]],
		fields : [ 'text', 'filed' ]
	});
	
	var shipStatusOptionCombo=new Ext.form.ComboBox({
		store: shipStatusDataStore,
		id: "shipStatus",
		name: "shipStatus",
		displayField: "filed",
		valueField: "text", 
		mode: "local",
		width:250,
		labelWidth: 90,
		triggerAction: 'all',
		selectOnFocus:true,
		forceSelection:true,
		hiddenName: "shipStatus",
		emptyText: "请转物流同步状态",
		fieldLabel: "物流同步状态",
		editable: false // 不可输入
	});
	
	
	var formPanel =new Ext.FormPanel({
		bodyStyle : 'padding:5px 5px 0',
		frame : true,
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		buttonAlign : 'right',// 按钮居右
		fieldDefaults: {
			labelAlign: 'right'
		},
		items : [{
			layout : "column", // 从左往右的布局
			bodyStyle : 'padding:5px 5px 0',
			items:[{
				  bodyStyle : 'padding:5px 5px 0',
//				  layout : 'form',
				  items:[{
				  width : 250,
				  labelWidth : 90,
	              xtype : "textfield",
	              id:'channelOrderSn',
	              name: 'channelOrderSn',
	              fieldLabel : "外部交易号"}]
	              },{
				  bodyStyle : 'padding:5px 5px 0',
//				  layout : 'form',
				  items:[{
				  width : 250,
				  labelWidth : 90,
	              xtype : "textfield",
	              id:'osOrderSn',
	              name: 'osOrderSn',
	              fieldLabel : "OS订单号" 
				  }]
	              },{
//	              layout : 'form',
	              bodyStyle : 'padding:5px 5px 0',	
				  items:[
				         typeOptionCombo]}]
		},{
			bodyStyle : 'padding:5px 5px 0',
			layout : "column", // 从左往右的布局
			fieldDefaults: {
				labelAlign: 'left'
			},
			items:[{
//				  layout : 'form',
	              bodyStyle : 'padding:5px 5px 0',	
				  items:[
			      convertStatusOptionCombo]},{
//	    	      layout : 'form',
	              bodyStyle : 'padding:5px 5px 0',	
				  items:[shipStatusOptionCombo]}]
		}],
		buttons : [{
			text : '查询',
			columnWidth : .1,
			handler : search
		}, {
			text : '重置',
			handler : function () {
				formPanel.form.reset();
			}
		}],
		buttonAlign: 'right'
	
		})
	var myMask = new Ext.LoadMask({
		    msg    : '请稍等...',
		    target : formPanel
		});
	// 定义菜单栏
	tbar = ["菜单栏：", "-",{
		text : '同步',
		width: 90,
//		iconCls : 'refresh',
		handler : function() {
			var selModel = channelOrderGrid.getSelectionModel();
			if (selModel.hasSelection()) {
				var records = selModel.selected.items;
				var channelOrderSns = "";
				for ( var i = 0; i < records.length; i++) {
					var channelOrderSn = records[i].get("channelOrderSn");
					var isShipToChannel = records[i].get("isShipToChannel");
					var companyCode = records[i].get("companyCode");
					var invoiceNo = records[i].get("invoiceNo");
					var flag=false;
					if(isShipToChannel<=0){
						if(companyCode && invoiceNo && invoiceNo.length > 0){
							flag=true;
						}
					}
					if(!flag){
						Ext.Msg.alert("结果","只有已发货,未同步状态才能进行同步操作！");
						return;
					}
					if (channelOrderSn && channelOrderSn != ''&& channelOrderSn != null) {
						channelOrderSns += "" + channelOrderSn + ",";
					}
			    }
				var channelCode = Ext.getCmp("channelCode").getValue();
				var channelName = Ext.getCmp('channelCode').getRawValue();
				if (channelOrderSns != "") {
									myMask.show(); 
									Ext.Ajax.request({
										waitMsg : '请稍等.....',
										url : basePath+ '/custom/pullAndTurn/deliveryCallOrder.ac',
										method : 'post',
										params : {channelOrderSns : channelOrderSns,channelCode:channelCode,channelName:channelName},
										success : function(response) {
											console.dir(response);
											var obj = Ext.decode(response.responseText);
											Ext.getCmp("channelResultMessage").setValue(
													response.responseText);
											win.show();
											if (myMask != undefined){ myMask.hide();}
											    channelOrderGrid.store.reload();
										},
										failure : function(response,options) {
											if (myMask != undefined){ myMask.hide();}
											Ext.Msg.alert("发货回调","执行异常！");
										}
									});
				}

			} 	 else {
				Ext.Msg.alert("结果","请选择需要同步的行!");
			}														
		
		}
	},{

		text : '强制同步',
		width:90,
//		iconCls : 'refresh',
		handler : function() {
			var selModel = channelOrderGrid.getSelectionModel();
			if (selModel.hasSelection()) {
				var records = selModel.selected.items;
				var channelOrderSns = "";
				for ( var i = 0; i < records.length; i++) {
					var channelOrderSn = records[i].get("channelOrderSn");
					var isShipToChannel = records[i].get("isShipToChannel");
					var companyCode = records[i].get("companyCode");
					var invoiceNo = records[i].get("invoiceNo");
					var flag=false;
					if(isShipToChannel<=0){
						if(companyCode && invoiceNo && invoiceNo.length > 0){
							flag=true;
						}
					}
					if(!flag){
						Ext.Msg.alert("结果","只有已发货,未同步状态才能进行强制同步操作！");
						return;
					}
					if (channelOrderSn && channelOrderSn != ''&& channelOrderSn != null) {
						channelOrderSns += "" + channelOrderSn + ",";
					}
			    }
				var channelCode = Ext.getCmp("channelCode").getValue();
				var channelName = Ext.getCmp('channelCode').getRawValue();
				if (channelOrderSns != "") {
					myMask.show(); 
									Ext.Ajax.request({
										url : basePath+ '/delivery/enforceDeliveryOrder.ac',
										method : 'post',
										params : {channelOrderSns : channelOrderSns,channelCode:channelCode,channelName:channelName},
										success : function(response) {
											if (myMask != undefined){ myMask.hide();}
											    channelOrderGrid.store.reload();
										},
										failure : function(response,options) {
											if (myMask != undefined){ myMask.hide();}
											Ext.Msg.alert("结果","失败！");
										}
									});
				}
			} 	 else {
				Ext.Msg.alert("结果","请选择需要强制同步的行!");
			}
		}
	},{

		text : '转单',
//		iconCls : 'refresh',
		width :90,
		handler : function() {
			var selModel = channelOrderGrid.getSelectionModel();
			if (selModel.hasSelection()) {
				var records = selModel.selected.items;
				var channelOrderSns = "";
				for ( var i = 0; i < records.length; i++) {
					var channelOrderSn = records[i].get("channelOrderSn");
					var osOrderSn = records[i].get("osOrderSn");
					var flag=false;
					if(null==osOrderSn||osOrderSn.length<2){
						flag=true;
					}
					
					if(!flag){
						Ext.Msg.alert("结果","只有未转单的订单才能进行转单操作！");
						return;
					}
					if (channelOrderSn && channelOrderSn != ''&& channelOrderSn != null) {
						channelOrderSns += "" + channelOrderSn + ",";
					}
			    }
				var channelCode = Ext.getCmp("channelCode").getValue();
				var channelName = Ext.getCmp('channelCode').getRawValue();
				if (channelOrderSns != "") {
									myMask.show();
									Ext.Ajax.request({
										url : basePath+ '/custom/pullAndTurn/convertOrder.ac',
										method : 'post',
										params : {channelOrderSns : channelOrderSns,channelCode:channelCode,channelName:channelName},
										success : function(response) {
											 if (myMask != undefined){ myMask.hide();}
											    channelOrderGrid.store.reload();
											    
										},
										failure : function(response,options) {
											 if (myMask != undefined){ myMask.hide();}
											Ext.Msg.alert("结果","失败！");
										}
									});
				}
			} 	 else {
				Ext.Msg.alert("结果","请选择需要转单的行!");
			}
		}
	}]
	
	
	var win = new Ext.Window({
		title : "执行结果",
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		width : 600,
		height : 180,
		shadow : true,
		modal : true,
		closable : true,
		bodyStyle : 'padding:5 5 5 5',
		animCollapse : true,
		items : [ {
			id : 'channelResultMessage',
			xtype : 'textarea',
			readOnly : true,
			width : 575,
			height : 158
		} ]
	});

	
//	var checkBoxSelect = new Ext.grid.CheckboxSelectionModel();
	
									var gridColumns = {
										defaults : {
											align : 'center',
											sortable : false,
											menuDisabled : true
										},
										items:[
											{
	//         									id : 'channelOrderSn',
	         									align : "center",
	         									header : "外部订单号",
	         									width : 140,
	         									dataIndex : 'channelOrderSn'
	         								}, 
	         								{
	//         									id : 'channelNameStr',
	         									align : "center",
	         									header : "渠道",
	         									width : 140,
	         									dataIndex : 'channelNameStr'
	         								}, {
	//         									id : 'transFormOrder',
	         									header : "是否转单",
	         									align : "center",
	         									width : 90,
	         									dataIndex : 'transFormOrder'
	         								}, {
	//         									id : 'osOrderSn',
	         									header : "OS订单号",
	         									align : "center",
	         									width : 120,
	         									dataIndex : 'osOrderSn'
	         								},
	         								{
	//         									id : 'addTime',
	         									align : "center",
	         									header : "下载时间",
	         									width : 140,
	         									dataIndex : 'addTime',
	         									renderer: function(value) {
	         										if (value) {
													var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
													return createTime;
													}	
												}
	         								}, {
	//         									id : 'companyCode',
	         									align : "center",
	         									header : "物流公司",
	         									width : 120,
	         									dataIndex : 'companyCode'
	         								}, {
	//         									id : 'invoiceNo',
	         									header : "物流单号",
	         									align : "center",
	         									width : 160,
	         									dataIndex : 'invoiceNo'
	         								}, 
												{
	//         									id : 'shipStatus',
	         									header : "发货/同步状态",
	         									align : "center",
	         									width : 120,
	         									dataIndex : 'shipStatus'
	         								},{
	//         									id : 'shipTime',
	         									header : "ERP发货时间",
	         									align : "center",
	         									width : 180,
	         									dataIndex : 'shipTime',
	         									renderer: function(value) {
	         										if (value) {
													var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
													return createTime;
													}
												}
	         								},{
	//         									id : 'requestNum',
	         									header : "同步失败次数",
	         									align : "center",
	         									width : 110,
	         									dataIndex : 'requestNum'
	         								}
										]
									};
									var channelOrderStore = Ext.create("Ext.data.Store", {
										model : Ext.create("Ext.data.Model", {
													fields : [
															{name : 'channelOrderSn'
															},{
																name : 'channelNameStr'
															}, {
																name : 'transFormOrder'
															}, {
																name : 'osOrderSn'
															}, {
																name : 'addTime'
															}, {
																name : 'companyCode'
															}, {
																name : 'invoiceNo'
															}, {
																name : 'shipStatus'
															}, {
																name : 'shipTime'
															},{
																name : 'isShipToChannel' 
															},{
																name:'requestNum'
															}]
												}),
										pageSize : 15,// 每页显示条目数量
									    proxy: {
									    	type: 'ajax',
											actionMethods: {
												read: 'POST'
											},
											url:  basePath + "/custom/pullAndTurn/channelOrderList.ac",
											reader: {
												rootProperty: 'root',
												type: 'json',
												totalProperty: 'totalProperty'
											}
									    },
										autoLoad : true
									})
									
									/*var channelOrderStore = new Ext.data.Store({
										autoLoad : {
											params : {
												start : 0,
												limit : 20
											}
										},
										proxy : couponproxy,
										reader : couponreader
									});*/
									
//									  如果不是用ajax的表单封装提交,就要做如下操作.
//							        这里很关键，如果不加，翻页后搜索条件就变没了，这里的意思是每次数据载入前先把搜索表单值加上去，这样就做到了翻页保留搜索条件了   
									channelOrderStore.on('beforeload', function(store){
										var outStock = {};
										outStock["channelCode"] = Ext.getCmp("channelCode").getValue();
										outStock["convertStatus"] = Ext.getCmp("convertStatus").getValue();
										outStock["shipStatus"] = Ext.getCmp("shipStatus").getValue();
										outStock["channelOrderSn"] = Ext.getCmp("channelOrderSn").getValue();
										outStock["osOrderSn"] = Ext.getCmp("osOrderSn").getValue();
										outStock["channelName"] = Ext.getCmp('channelCode').getRawValue();
										Ext.apply(store.proxy.extraParams, outStock);
//										channelOrderGrid.store.baseParams = outStock;   
							        });	
									
									
									var channelOrderGrid = new Ext.grid.GridPanel(
											{
												title : '渠道订单',
												store : channelOrderStore,
												trackMouseOver : false,
//												disableSelection : true,
												autoHeight:true,
												loadMask : true,
												frame : true,
												columnLines : true,
												tbar : tbar,
												columns:gridColumns,
												selModel:
													Ext.create('Ext.selection.CheckboxModel', {
//													injectCheckbox:1,//checkbox位于哪一列，默认值为0
													mode:'multi',//multi,simple,single；默认为多选multi
													checkOnly:true,//如果值为true，则只用点击checkbox列才能选中此条记录
													allowDeselect:true,//如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
													enableKeyNav:false}),
												viewConfig : {enableTextSelection: true},
												// grid columns
//												cm : couponcolumnGrid,
//												sm : checkBoxSelect,
												// paging bar on the bottom
												bbar : new Ext.PagingToolbar({
													store: channelOrderStore,
													pageSize:15,
													displayInfo: true,
													displayMsg: "当前显示从{0}条到{1}条,共{2}条",
													emptyMsg: "<span style='color:red;font-style:italic;'>对不起,没有找到数据</span>"
												}),
								refresh:function(){
									channelOrderStore.reload();
								},
								reset:function(){
									formPanel.form.reset();
								}
								
		
							});
									  function search() {
										var outStock = {};
										outStock["channelCode"] = Ext.getCmp("channelCode").getValue();
										outStock["convertStatus"] = Ext.getCmp("convertStatus").getValue();
										outStock["shipStatus"] = Ext.getCmp("shipStatus").getValue();
										outStock["channelOrderSn"] = Ext.getCmp("channelOrderSn").getValue();
										outStock["osOrderSn"] = Ext.getCmp("osOrderSn").getValue();
										channelOrderGrid.store.baseParams = outStock;
										channelOrderGrid.store.load({
											params : {
												start : 0,
												limit : 15
											}
										});
									}
									  
									  

										var	couponPanel = new Ext.Panel({
												renderTo : 'form-ct',
												items : [formPanel,channelOrderGrid ]
											});
									  
									
									
	
	
	
	
	
	
})