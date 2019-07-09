Ext.define('MB.controller.ReturnShowController', {
	extend : 'Ext.app.Controller',
	models : ['ReturnCommonModel','CommonStatusModel'
			],
	views : [ 'returnOrder.ReturnShow', 'returnOrder.ReturnShowView' ],
	refs : [{
		ref : 'orderDetailOrderShowView',
		selector : 'orderShowView'
	}],
	init : function() {
		var me = this;
		me.control({
			'returnShowView button[action=lockReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=unLockReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=saveReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=confirmReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=unConfirmReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=receiveReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=unReceiveReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=passReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=unPassReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=storageReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=storageCancle]' : {
				click : this.returnClick
			},
			'returnShowView button[action=virtualStorageReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=settleReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=invalidReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=invalidReturn]' : {
				click : this.returnClick
			},
			'returnShowView button[action=communicateReturn]' : {
				click : this.actionClick
			},
			'returnShowView button[action=returnForward]' : {
				click : this.returnForward
			},
			'returnShowView button[action=messageEdit]' : {
				click : this.messageEdit
			},
			'returnShowView button[action=backToCsReturn]' : {
				click : this.returnClick
			},
			'returnShow tool[action=orderReturnEdit]':{
				click:this.orderReturnEdit
			},
			'returnShow tool[action=returnGoodsEdit]':{
				click:this.returnGoodsEdit
			},
			'returnShow tool[action=returnPayEdit]':{
				click:this.returnPayEdit
			},
			'returnPictureSetModule button[action=uploadImage]':{
				click:this.uploadImage
			},
			'returnPictureSetModule button[action=resetImage]':{
				click:this.resetImage
			},
			'returnShow button[action=updateHaveRefund]':{
				click:this.updateHaveRefund
			},
			'returnShow button[action=updateExpress]':{
				click:this.updateExpress
			},
			'returnShow button[action=updateInvoiceNo]':{
				click:this.updateInvoiceNo
			},
			'returnShow button[action=updateDepotCode]':{
				click:this.updateDepotCode
			}
		});
	},
	onLaunch : function() {
		//初始化数据
		var returnForm = Ext.getCmp('returnShow');
		returnForm.initData();
		//日志面板初始不展示
		var returnActionGrid = returnForm.up('returnShowView').down('#returnSouth');
		returnActionGrid.collapse();
		//日志面板监听展开加载
		returnActionGrid.addListener('expand',function(){
			returnActionGrid.store.on('beforeload', function (store){
				params = {"returnSn" : returnSn};
				Ext.apply(store.proxy.extraParams, params);
			});
			returnActionGrid.getStore().load();
		});
	},
	/**
	 * 退单操作按钮回调后台
	 * 参数：退单号/操作类型/退单Json
	 */
	returnClick : function(btn) {
		var me = this;
		if(btn.action == 'saveReturn'){
			//退单总金额
			//var returnTotalFee=Ext.getCmp("returnPaySetModule").getForm().findField("returnTotalFee").getValue();
			//订单已付款金额
			//var orderPayed = Ext.getCmp('returnShow').getForm().reader.rawData.returnCommon.orderPayedMoney;
			Ext.Msg.confirm('警告', '确认保存退单吗?',function(flag){
				if(flag == 'yes'){
					me.callReturnService(btn);
				}else{
					return false;
				}
			});
			/*if(returnTotalFee > orderPayed){
				Ext.Msg.confirm('警告', '退单总金额('+returnTotalFee+') 大于 原订单已付款在线支付金额('+orderPayed+')，确认保存吗?',function(flag){
					if(flag == 'yes'){
						me.callReturnService(btn);
					}else{
						return false;
					}
				});
			}else{
				me.callReturnService(btn);
			}*/
		}else{
			me.callReturnService(btn);
		}
	},
	callReturnService :function(btn,actionWindow){
		var me = this;
		var returnOrder = {};
		var returnCenter = Ext.getCmp('returnShow');
		if(btn.action == 'saveReturn'){
			returnOrder = returnCenter.builtOrderReturnParams(returnCenter);
			if(!returnOrder) return returnOrder;
		}
		if(btn.action == 'storageReturn' || btn.action == 'virtualStorageReturn'){
			
			//第三方商品不允许oms入库
			var salesMode = Ext.getCmp('returnGoodsList').store.getAt(0).get('salesMode');
			if(salesMode && parseInt(salesMode) == 4){
				Ext.msgBox.remainMsg("结果", "第三方商品不允许oms入库！", Ext.MessageBox.ERROR);
				return false;
			}
			var checkFlag = true;
			var returnExpressFlag = true;
			var params = {};
			params.returnSn = returnSn;
			params.type = "haveDepotFlag";
			Ext.Ajax.request({
			url:  basePath + '/custom/orderReturn/checkDepotCode',
			params: params,
			async: false,
			timeout:90000,
			data:"json",
			success: function(response){
				var text = response.responseText;
				var result = Ext.JSON.decode(text);
				checkFlag = result.success;
				returnExpressFlag = result.returnExpressFlag;
				console.dir(result);
			},
			failure: function(response){
				Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
			}
		});
			if(!checkFlag){
				Ext.msgBox.remainMsg("结果", "请选择仓库！", Ext.MessageBox.ERROR);
				return false;
			}
			
			if(btn.action == 'virtualStorageReturn'){
				if(!returnExpressFlag){
					Ext.msgBox.remainMsg("结果", "请选择承运商！", Ext.MessageBox.ERROR);
					return false;
				}
			}
		}
		returnOrder.btnType = btn.action;
		returnOrder.returnSn = returnSn;
		if(actionWindow != undefined && actionWindow != null){
			returnOrder.actionNote = actionWindow.down('form').getValues().message;			
		}
		var loadMarsk = new Ext.LoadMask({
			msg : 'Please wait...',
			target : returnCenter.up("viewport")
		});
		loadMarsk.show();
		returnCenter.getForm().load({
			url : basePath + '/custom/orderReturn/'+btn.action+"ButtonClick",
			params : returnOrder,
			timeout:90000,
			success : function(opForm, action) {
				var isOk = opForm.reader.rawData.ReturnInfo.isOk;
				var callReturnSn = opForm.reader.rawData.ReturnInfo.returnSn;
				if(isOk > 0){
					if(returnSn == ''){
						returnSn = callReturnSn;						
					}
				}
				if(btn.action == 'saveReturn'){
					window.location.href = basePath + '/custom/orderReturn/orderReturnPage?returnSn='+returnSn;
				}else{
					returnCenter.initData();
					Ext.msgBox.msg('业务操作', '操作成功', Ext.MessageBox.INFO);
				}
				if(btn.action == "communicateReturn"){
					actionWindow.close();
				}
				loadMarsk.hide();
			},
			failure : function(opForm, action) {
				Ext.Msg.alert('错误信息', opForm.reader.rawData.errorMessage, function(xx) {
					window.location.href = basePath + '/custom/orderReturn/orderReturnPage?returnSn='+returnSn;
				});
				loadMarsk.hide();
			}
		});
	},
	editOrderGoods : function(btn) { // 编辑订单商品
		Ext.require("MB.model.OrderInfoEdit", function () {
			var app = Ext.application('MB.orderGoodsEditApp');
		}, self);
	},
	orderReturnEdit : function(btn) {
		console.log('returnSn:'+returnSn);
		var me = this;
		var checkSettle = Ext.getCmp('returnShow').getForm().findField('returnStatusDisplay').value.indexOf('已结算');
		if(returnSn == '' || checkSettle > 0){
			return;
		}
		var win = Ext.create("Ext.window.Window",{
			id:'orderReturnEdit',
			title: "退单保存",
			width: 450,
			height: 450,
			items:{
				xtype: "form",
				border: false,
				layout: "form",
				url : basePath + '/custom/orderReturn/editReturnButtonClick',
				fieldDefaults: {
			        labelAlign: "right",
			        flex: 1,
			        margin:10
				},
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
							{
							xtype : "combobox",
							store : Ext.create('Ext.data.Store', {
										model : "MB.model.CommonStatusModel",
										data : [['0', "无操作"], ['1', '退货'],
												['2', '修补'], ['3', '销毁'],
												['4', '换货']]
									}),
							displayField : 'name',
							valueField : 'id',
							queryMode : 'local',
							hiddenName : 'id',
							emptyText : '请选择',
							editable : false, 
							name : 'processType', 
							fieldLabel : "处理方式", 
							width: 300,
							labelWidth: 150 }]
					},
					{
						xtype: 'fieldcontainer',
						labelStyle: 'font-weight:bold;padding:0;',
						layout: 'column',
						columnWidth: 1,
						items: [
							{
							xtype : "combobox",
							store : Ext.create('Ext.data.Store', {
										model : "MB.model.CommonStatusModel",
										data : [['0', '请选择'], ['1', '预付款'],
												['2', '保证金']]
									}),
							displayField : 'name',
							valueField : 'id',
							queryMode : 'local',
							hiddenName : 'id',
							emptyText : '请选择',
							editable : false, 
							name : 'returnSettlementType', 
							fieldLabel : "退款类型",
							width: 300,
							labelWidth: 150}
							]
					},
					{
						xtype: 'fieldcontainer',
						labelStyle: 'font-weight:bold;padding:0;',
						layout: 'column',
						columnWidth: 1,
						items: [
							{xtype : "displayfield", name : 'newOrderSn', fieldLabel : "换货单号", width: 300,labelWidth: 150 }
							]
					},
					{
						xtype: 'fieldcontainer',
						labelStyle: 'font-weight:bold;padding:0;',
						layout: 'column',
						columnWidth: 1,
						items: [
							{
							xtype : "combobox",
							store : Ext.create("Ext.data.Store", {
										model : Ext.create("Ext.data.Model", {
													fields : [{
																name : 'code'
															}, // 编码
															{
																name : 'name'
															}, // 名称
															{
																name : 'note'
															}, // 备注
															{
																name : 'type'
															} // 类型
													]
												}),
										proxy : {
											type : 'ajax',
											actionMethods : {
												read : 'POST'
											},
											url : basePath
													+ 'custom/common/getOrderCustomDefine',
											reader : {
												type : 'json'
											}
										}
									}),
							displayField : 'name',
							valueField : 'code',
							queryMode : 'remote',
							hiddenName : 'code',
							emptyText : '请选择操作原因',
							allowBlank : true,
							editable : false,
							name : 'returnReason', 
							fieldLabel : "退单原因", 
							width: 300,
							labelWidth: 150 }
							]
					}
				]
		
			},
			buttons:[
				{ text: "关闭", action: "close",handler : function () { this.up("window").close(); } },
				{ text: "保存", handler : me.orderReturnEditSave }
			],
			initPage: function (form) {
				var returnForm=null;
				if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
					returnForm=Ext.getCmp('returnForm');
				}else{
					returnForm=Ext.getCmp('returnShow');
				}
				var returnCommon=returnForm.getForm().reader.rawData.returnCommon;
				
				//原始退款总金额
				var returnTotalFee=Ext.getCmp('returnPayInfomation').getForm().findField("returnTotalFee").getValue();
				
				// 退单原因
				form.getForm().findField('returnReason').getStore().on(
						'beforeload', function(store) {
							params = {
								"type" : 1
							};
							Ext.apply(store.proxy.extraParams, params);
						});
				form.getForm().findField('returnReason').getStore().load();
				
				form.getForm().setValues(returnCommon);
				
			},
			orderReturnEditParams:function(from){
				var params = form.getValues();
				console.dir(params);
				var returnForm=null;
				if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
					returnForm=Ext.getCmp('returnForm');
				}else{
					returnForm=Ext.getCmp('returnShow');
				}
				var returnCommon=returnForm.getForm().reader.rawData.returnCommon;
				
				params['createOrderReturn.relatingReturnSn']=returnCommon.relatingOrderSn;
				params['createOrderReturn.newOrderSn']=params.newOrderSn;
				params['createOrderReturn.returnSettlementType']=params.returnSettlementType;
				params['createOrderReturn.returnSn']=returnCommon.returnSn;
				params['createOrderReturn.processType']=params.processType;
				params['createOrderReturn.returnReason']=params.returnReason;
				
				params.orderReturnSn=returnCommon.returnSn;//退单Sn
				return params;
			}
		})
		win.initPage(win.down('form'));
		win.show();
	},
	returnForward:function(btn){
		var me = this;
		var win = Ext.create("Ext.window.Window",{
			id:'orderReturnEdit',
			title: "退单转发",
			width: 400,
			height: 400,
			items:{
				xtype: "form",
				border: false,
				layout: "form",
				url : basePath + '/custom/orderReturn/returnForward',
				fieldDefaults: {
			        labelAlign: "right",
			        flex: 1,
			        margin:10
				},
				items:[
					{
						xtype: 'fieldcontainer',
						labelStyle: 'font-weight:bold;padding:0;',
						layout: 'column',
						columnWidth: 1,
						items: [
						        { xtype: "displayfield", name: "returnSn", fieldLabel: "退单号" }
							   ]
					},
					{
						xtype: 'fieldcontainer',
						labelStyle: 'font-weight:bold;padding:0;',
						layout: 'column',
						columnWidth: 1,
						items: [
						{
						xtype : "combobox",
						store : Ext.create('Ext.data.Store', {
									model : Ext.create('Ext.data.Model', {
												fields : [{
															name : 'shippingCode',
															type : 'string'
														}, {
															name : 'shippingName',
															type : 'string'
														}]
											}),
									proxy : {
										type : 'ajax',
										actionMethods : {
											read : 'POST'
										},
										timeout : 90000,
										url : basePath
												+ 'custom/common/selectSystemShippingList',
										reader : {
											type : 'json'
										}
									},
									autoLoad : true
								}),
						displayField : 'shippingName',
						valueField : 'shippingCode',
						queryMode : 'local',
						hiddenName : 'payId',
						emptyText : '请选择承运商',
						editable : false,
						name : 'returnExpress',
						fieldLabel : "退货承运商",
						labelWidth : 100}
							]
				},{
						xtype: 'fieldcontainer',
						labelStyle: 'font-weight:bold;padding:0;',
						layout: 'column',
						columnWidth: 1,
						items: [
							{
							xtype : "textfield",
							name : 'invoiceNo',
							fieldLabel : "快递单号",
							columnWidth : .24}
							]
				},{
						xtype: 'fieldcontainer',
						labelStyle: 'font-weight:bold;padding:0;',
						layout: 'column',
						columnWidth: 1,
						items: [
							{
							xtype : "textfield",
							name : 'userName',
							fieldLabel : "处理人",
							columnWidth : .24
							}
							]
				}
				]
		
			},
			buttons:[
				{ text: "关闭", action: "close",handler : function () { this.up("window").close(); } },
				{ text: "保存", handler : me.returnforwardSave }
			],
			initPage: function (form) {
						
				Ext.Ajax.request({
						url: basePath + '/custom/orderReturn/returnForward',
						params: {
								"returnSn" : returnSn,
								"type" : 'init'
							},
						timeout : 90000,
						success: function(response){
							var text = response.responseText;
							var results = Ext.JSON.decode(text);
							if(results.returnForward){
								form.getForm().setValues(results.returnForward);
								if(results.returnForward.expressCode !=""){
									form.getForm().findField('returnExpress').setValue(results.returnForward.expressCode);
								}
							}else{
								form.getForm().findField('returnSn').setValue(returnSn);
							}
						},
						failure: function(response, action){
							
						}
					});
				
			}
			
		})
		win.initPage(win.down('form'));
		win.show();
	},
	orderReturnEditSave:function(btn){
		var me = Ext.getCmp('returnShow');
		var win = btn.up("window");
		form = win.down("form");
		var params = win.orderReturnEditParams(form);
		params.btnType = 'editReturn';
		console.dir(params);
		var json = {
			params : params,
			timeout:90000,
			success : function(formPanel, action) {
				if (action.result.success == "false") {
					errorMsg("结果", action.result.msg);
				} else {
					win.close();
					me.initData();
					Ext.msgBox.msg('退货信息', '数据保存成功', Ext.MessageBox.INFO);
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		form.submit(json);
	
	},
	returnforwardSave:function(btn){
				var win = Ext.getCmp('orderReturnEdit');
				var from = win.down("form");
				var params = from.getForm().getValues();
				params.type = "edit";
				params.returnSn = from.getForm().findField('returnSn').getValue();
				params.expressName = from.getForm().findField('returnExpress').getRawValue();
				params.expressCode = params.returnExpress;
				var json = {
					params : params,
					timeout:90000,
					success : function(formPanel, action) {
						if (action.result.success == "false") {
							errorMsg("结果", action.result.msg);
						} else {
							win.close();
							Ext.msgBox.msg('退单转运', '数据保存成功', Ext.MessageBox.INFO);
						}
					},
					failure : function(formPanel, action) {
						Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
					},
					waitMsg : 'Loading...'
				};
				from.submit(json);
			},
	returnGoodsEdit:function(){
		var me = this;
		var selModel = false, plugins = false;
		selModel = new Ext.selection.CheckboxModel({
					checkOnly : true
				}), 
		plugins = {
			ptype : 'cellediting',
			clicksToEdit : 1,
			listeners : {
				beforeedit : 'onCellBeforeEdit',
				afteredit : 'onCellAfterEdit'
			}
		}
		
		var win = Ext.create("Ext.window.Window",{
			id:'returnGoodsEdit',
			alias: "widget.returnGoodsEdit",
			title: "退单商品编辑",
			width: '100%',
			height: '100%',
			items:{
				xtype: "form",
				border: false,
				frame: true,
				layout: "fit",
				autoWidth:true,
				autoHeight:true,
				url : basePath + '/custom/orderReturn/updateReturnGoods',
				fieldDefaults: {
			        labelAlign: "right",
			        flex: 1,
			        margin:2
				},
				buttons:[
					{ text: "关闭", action: "close",handler : function () { this.up("window").close(); } },
					{ text: "保存", handler: me.returnGoodEditSave }
				],
				items:[
					{
					xtype:'grid',
					id:"returnPayGoodsEdit",
					requires : ['Ext.grid.plugin.CellEditing'],
					store: Ext.create("Ext.data.Store",{
						id:"ReturnPayGoodsEditStore",
						model: Ext.create("Ext.data.Model",{
							fields : [
								{ name : 'goodsName' },//商品名称
								{ name : 'extensionCode' },//商品属性
								{ name : 'extensionId' },//商品扩展属性id
								{ name : 'goodsSn' },//货号
								{name:'goodsColorName'},//颜色
								{name:'goodsSizeName'},//尺寸
								{ name : 'customCode' },//企业SKU码
								{ name : 'marketPrice' },//商品价格
								{ name : 'goodsPrice' },//成交价格
								{ name : 'settlementPrice' },//财务价格
								{ name : 'shareBonus' },//分摊金额
								{ name : 'goodsBuyNumber' },//购买数量
								{ name : 'discount' },//折扣
								{ name : 'shareSettle' },//财务分摊金额
								{ name : 'osDepotCode' },//所属发货仓
								{ name : 'shopReturnCount' },//门店退货量
								{ name : 'havedReturnCount' },//已退货量
								{ name : 'canReturnCount' },//可退货量
								{ name : 'returnReason' },//退换货原因
								{ name : 'priceDifferNum' },//退差价数量
								{ name : 'priceDifference' },//退差价单价
								{ name : 'returnReason' },
								{name : 'isGoodReceived'},
								{name : 'qualityStatus'},
								{name : 'checkinStatus'}
								]
						})
					}),
					
					selModel:selModel,
					plugins: plugins,
					
					autoRender:true,
					autoWidth:true,
					columnLines: true,
					loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
					frame: true,
					listeners:{
							      'selectionchange':function(sm,rowIndex,record){
							      //获取选中的行
								    var returnCenter=Ext.getCmp('returnShow');
									var returnPaySetModule=Ext.getCmp('returnPaySetModule');
									var items=this.getSelectionModel().selected.items;
									var goodData=[];
									for(var i=0;i<items.length;i++){
										goodData.push(items[i].data);
									}
									returnPaySetModule.paySet(goodData);
							      },
							      'cellclick' : function(grid, rowIndex, columnIndex, e) { 
								    //获取选中的行
								    var rows=grid.getSelectionModel().selected.items;
								    
								    var returnCenter=Ext.getCmp('returnShow');
									var returnPaySetModule=Ext.getCmp('returnPaySetModule');
									var items=grid.getSelectionModel().selected.items;
									var goodData=[];
									for(var i=0;i<items.length;i++){
										goodData.push(items[i].data);
									}
									returnPaySetModule.paySet(goodData);
								    
								    var len=rows.length;
								    var editFlag=false;
								    for(var i=0;i<len;i++){
								    	//判断当前行的id是否在选中行里
								    	if(e.id==rows[i].id){
								    		editFlag=true;
								    		break;
								    	}
								    }
								    return editFlag;
								}  
					},
					resizable: true,
					viewConfig:{
						forceFit: true, // 注意不要用autoFill:true,那样设置的话当GridPanel的大小变化（比如你resize了它）时不会自动调整column的宽度
						scrollOffset: 0 //不加这个的话，会在grid的最右边有个空白，留作滚动条的位置
					},
					columns:[
						{ text: '商品名称', width: 150,align: 'center', dataIndex: 'goodsName' ,sortable:false,menuDisabled : true,
							renderer: function(value, meta, record) {
							    var max = 15;  //显示多少个字符
							    meta.tdAttr = 'data-qtip="' + value + '"';
							    return value.length < max ? value :value.substring(0, max - 3) + '...';}},
							    
						{  dataIndex: 'extensionId' ,sortable:false,menuDisabled : true,hidden:true},
						{ text: '商品属性', width: 75,align: 'center', dataIndex: 'extensionCode' ,sortable:false,menuDisabled : true ,
				        	renderer : function (value) { 
								if(value == 'group'){
									return "套装";
								}else if(value.indexOf('gif') != -1){
									return "赠品";
								}else{
									return "普通商品";
								}
							}},
						{ text: '货号',align: 'center', dataIndex: 'goodsSn' ,sortable:false,menuDisabled : true},
						{ header: '规格',columns: 
											[ {
												header : "颜色",
												width : 80,
												dataIndex: 'goodsColorName',
												sortable : false,
												menuDisabled : true,
												renderer: function(value, meta, record) {
												    if(value){
												    var max = 15;  //显示多少个字符
												    meta.tdAttr = 'data-qtip="' + value + '"';
												    return value.length < max ? value :value.substring(0, max - 3) + '...';
												    }
													return ' ';
												}
												
											} , {
												header : "尺寸",
												width : 80,
												dataIndex: 'goodsSizeName',
												sortable : false,
												menuDisabled : true,
												renderer: function(value, meta, record) {
												    if(value){
												    var max = 15;  //显示多少个字符
												    meta.tdAttr = 'data-qtip="' + value + '"';
												    return value.length < max ? value :value.substring(0, max - 3) + '...';
												    }else{
												    	return '';
												    }
												}
												
											} ]
										},
						{ text: '企业SKU码', width: 120,align: 'center', dataIndex: 'customCode',sortable:false,menuDisabled : true,
							renderer: function(value, meta, record) {
							    var max = 15;  //显示多少个字符
							    meta.tdAttr = 'data-qtip="' + value + '"';
							    if(value)return value.length < max ? value :value.substring(0, max - 3) + '...';
							}},
						{ text: '商品价格', width: 75,align: 'center', dataIndex: 'marketPrice' ,sortable:false,menuDisabled : true,
								renderer : function (value) {return "￥"+value+"元"}
						},
						{ text: '成交价格', width: 75,align: 'center', dataIndex: 'goodsPrice' ,sortable:false,menuDisabled : true,
							renderer : function (value) {return "￥"+value+"元"}
						},
						{ text: '财务价格', width: 75,align: 'center', dataIndex: 'settlementPrice' ,sortable:false,menuDisabled : true,
							renderer : function (value) {return "￥"+value+"元"}
						},
						{ text: '分摊金额', width: 75,align: 'center', dataIndex: 'shareBonus',sortable:false,menuDisabled : true ,
							renderer : function (value) {return "￥"+value+"元"}
						},
						{ text: '购买数量', width: 75,align: 'center', dataIndex: 'goodsBuyNumber' ,sortable:false,menuDisabled : true},
						{ text: '财务分摊金额', width: 95,align: 'center', dataIndex: 'shareSettle' ,sortable:false,menuDisabled : true,
							renderer : function (value) {return "￥"+value+"元"}, hidden: true
						},
						{ text: '所属发货仓', width: 75,align: 'center', dataIndex: 'osDepotCode' ,sortable:false,menuDisabled : true},
						{ text: '门店退货量', width: 75,align: 'center', dataIndex: 'shopReturnCount' ,sortable:false,menuDisabled : true},
						{ text: '已退货量', width: 75,align: 'center', dataIndex: 'havedReturnCount' ,sortable:false,menuDisabled : true},
						{ text: '可退货量', width: 75,align: 'center', dataIndex: 'canReturnCount' ,sortable:false,menuDisabled : true,
							renderer : function (value, meta, record) {
								meta.style='background:'+editColor;
								return value;
							},
							editor: {
									xtype: 'numberfield',
									allowBlank: false,
									minValue: 0,
									allowDecimals : false
								}
						},
						{ text: '退差价数量', width: 75,align: 'center', dataIndex: 'priceDifferNum' ,sortable:false,menuDisabled : true,
							renderer : function (value, meta, record) {
								meta.style='background:'+editColor;
								return value;
							},
							editor: {
									xtype: 'numberfield',
									allowBlank: false,
									minValue: 0,
									allowDecimals : false
								}
						},
						{ text: '退差价单价', width: 75,align: 'center', dataIndex: 'priceDifference' ,sortable:false,menuDisabled : true,
							renderer : function (value, meta, record) {
								meta.style='background:'+editColor;
								return value;
							},
							editor: {
									xtype: 'numberfield',
									allowBlank: false,
									minValue: 0,
									decimalPrecision : 2,//精确到小数点后两位  
				                	allowDecimals : true//允许输入小数  
								}
						},
						{ text: '退差价小计', width: 75,align: 'center', dataIndex: 'priceDiffTotal' ,sortable:false,menuDisabled : true,
							renderer:function (value, cellMeta, record, rowIndex, columnIndex, store){
								var priceDifferNum=record.get('priceDifferNum');
								var priceDifference=record.get('priceDifference');
								return priceDifferNum*priceDifference;
							}
						},
						{ text: '原因', width: 100,align: 'center', dataIndex: 'returnReason' ,sortable:false,menuDisabled : true,
							renderer : function (value, meta, record) {
								meta.style='background:'+editColor;
								return value;
							},
							editor: {
									xtype: 'textfield',
									allowBlank: false,
									allowDecimals : false
								}
						},
						{ text: '操作', width: 100,align: 'center', sortable:false,menuDisabled : true,
							renderer: function(value, metaData, record, rowIndex) {
									var id = Ext.id();
									var disabledFlag = false;
									if(record.get('checkinStatus') == 1){
										disabledFlag = true;
									}
									setTimeout(function() {
										var tool = Ext.create('Ext.panel.Tool', {
											iconCls: 'delete',
											tooltip: '删除商品',
											disabled : disabledFlag,
											handler: function () {
												console.dir(me);
												me.onRemoveClick(rowIndex)
											}
										});
										if (Ext.get(id)) {
											tool.render(Ext.get(id));
										}
									}, 1);
									return '<div id="' + id + '"></div>';
								}
						}
					
					],
					hideColumn:function(data,grid){
						if(data.returnType==1||data.returnType==2||data.returnType==5){
							grid.columns[12].hide();//财务分摊金额
							for(var i=17;i<=19;i++){//退差价
								grid.columns[i].hide();
							}
						}else if(data.returnType==3){
							for(var i=13;i<=19;i++){
								if(i!=16){
									grid.columns[i].hide();
								}
							}
						}else{
							for(var i=12;i<=16;i++){
								grid.columns[i].hide();
							}
						}
						
					},
					onCellBeforeEdit: function (editor, ctx, eOpts) { // 商品属性初始化
						var clickColIdx = ctx.colIdx; // grid column 展示列下标
						var record = ctx.record;
						if (clickColIdx == 17) { // 可退货量
							var  goodsNumber = record.get("goodsBuyNumber"),
								 shopReturnCount = record.get("shopReturnCount"),
								 havedReturnCount = record.get("havedReturnCount");
							var	canReturnCount=goodsNumber-shopReturnCount-havedReturnCount;
							var numberf = ctx.grid.columns[17].getEditor(ctx.record);
							numberf.setMaxValue(canReturnCount, false);
						}else if(clickColIdx == 18){//退差价数量
							var  goodsNumber = record.get("goodsBuyNumber");
							var numberf = ctx.grid.columns[18].getEditor(ctx.record);
							numberf.setMaxValue(goodsNumber, false);
						}
					},
					onCellAfterEdit: function(editor, ctx, eOpts) { // 修改商品信息后动态变化
						var clickColIdx = ctx.colIdx; // grid column 展示列下标
						var record = ctx.record;
						var returnCenter=Ext.getCmp('returnShow');
						var returnPaySetModule=Ext.getCmp('returnPaySetModule');
						var items=ctx.grid.getSelectionModel().selected.items;
						var goodData=[];
						for(var i=0;i<items.length;i++){
							goodData.push(items[i].data);
						}
						returnPaySetModule.paySet(goodData);
					}
					
					}
					]
	
			
			},
			initPage: function (form) {
					var returnForm=null;
					if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
						returnForm=Ext.getCmp('returnForm');
					}else{
						returnForm=Ext.getCmp('returnShow');
					}
					console.dir(returnForm.reader.rawData);
					var grid=Ext.getCmp('returnPayGoodsEdit');
					grid.store.loadData(returnForm.reader.rawData.returnGoods);
					grid.getSelectionModel().selectAll();
					grid.hideColumn(returnForm.reader.rawData.returnCommon,grid);
				},
				builtParams:function(grid){
					var param={};
					var goodDataChecked = grid.getSelectionModel().selected.items;
					var checkedLen=goodDataChecked.length;
					var index = 0;
					var gridTotal = Ext.getCmp('returnPayGoodsEdit').getStore().data.items.length;
					for(var i=0;i<checkedLen;i++){
						var canReturnNumber = goodDataChecked[i].data.canReturnCount;
						if(canReturnNumber < 0){
							continue;
						}
						if(returnType == '1' && canReturnNumber == 0 && checkedLen == 1 && gridTotal == 1){
							Ext.Msg.alert('警告', "退货商品当前只有一件，不可删除！", function(xx) {
							});
							return false;
						}
						param['createOrderReturnGoodsList['+index+'].relatingReturnSn']=returnSn;
						param['createOrderReturnGoodsList['+index+'].customCode']=goodDataChecked[i].data.customCode;
						param['createOrderReturnGoodsList['+index+'].extensionCode']=goodDataChecked[i].data.extensionCode;
						param['createOrderReturnGoodsList['+index+'].extensionId']=goodDataChecked[i].data.extensionId;
						param['createOrderReturnGoodsList['+index+'].osDepotCode']=goodDataChecked[i].data.osDepotCode;
						param['createOrderReturnGoodsList['+index+'].goodsBuyNumber']=goodDataChecked[i].data.goodsBuyNumber;
						param['createOrderReturnGoodsList['+index+'].chargeBackCount']=goodDataChecked[i].data.shopReturnCount;
						param['createOrderReturnGoodsList['+index+'].goodsReturnNumber']=goodDataChecked[i].data.canReturnCount;
						param['createOrderReturnGoodsList['+index+'].haveReturnCount']=goodDataChecked[i].data.havedReturnCount;
						param['createOrderReturnGoodsList['+index+'].returnReason']=goodDataChecked[i].data.returnReason;
						
						//退差价
						param['createOrderReturnGoodsList['+index+'].priceDifferNum']=goodDataChecked[i].data.priceDifferNum;
						param['createOrderReturnGoodsList['+index+'].priceDifference']=goodDataChecked[i].data.priceDifference;
						
						//价格
						param['createOrderReturnGoodsList['+index+'].goodsPrice']=goodDataChecked[i].data.goodsPrice;
						param['createOrderReturnGoodsList['+index+'].marketPrice']=goodDataChecked[i].data.marketPrice;
						param['createOrderReturnGoodsList['+index+'].settlementPrice']=goodDataChecked[i].data.settlementPrice;
						param['createOrderReturnGoodsList['+index+'].shareBonus']=goodDataChecked[i].data.shareBonus;
						param['createOrderReturnGoodsList['+index+'].checkinStatus']=goodDataChecked[i].data.checkinStatus;
						param['createOrderReturnGoodsList['+index+'].qualityStatus']=goodDataChecked[i].data.qualityStatus;
						param['createOrderReturnGoodsList['+index+'].isGoodReceived']=goodDataChecked[i].data.isGoodReceived;
						index ++;
						//校验可退货量
						var checkNum1=goodDataChecked[i].data.goodsBuyNumber-goodDataChecked[i].data.shopReturnCount-goodDataChecked[i].data.havedReturnCount - goodDataChecked[i].data.canReturnCount;
						if(checkNum1 < 0){
							Ext.Msg.alert('警告', "退货商品当前可退货量不足！", function(xx) {
							});
							return false;
						}
						
						//校验可退差价数量
						var checkNum2=goodDataChecked[i].data.goodsBuyNumber-goodDataChecked[i].data.priceDifferNum;
						if(checkNum2<0 && returnType == 4){
							Ext.Msg.alert('警告', "退差价数量有误！", function(xx) {
							});
							return false;
						}
					}
					if(index == 0){
						Ext.Msg.alert('警告', "退货商品列表中有效商品为空！", function(xx) {
						});
						return false;
					}
					param.orderReturnSn=returnSn;//退单Sn
					return param;
				}
		});
		win.initPage();
		win.show();
	},
	returnGoodEditSave:function(btn){
		var me = this;
		var win = btn.up("window");
		var form=win.down("form");
		var grid = win.down("grid");
		var params = win.builtParams(grid);
		if(!params) return params;
		var json = {
			params : params,
			timeout:90000,
			success : function(formPanel, action) {
				if (action.result.success == "false") {
					errorMsg("结果", action.result.message);
				} else {
					win.close();
					Ext.getCmp('returnShow').initData();
					Ext.msgBox.msg('退货信息', '数据保存成功', Ext.MessageBox.INFO);
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg("结果", action.result.message, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		Ext.getCmp('returnShow').initData();
		form.submit(json);
	},
	returnPayEdit : function(btn) {
		var me = this ;
		var win = Ext.create("Ext.window.Window",{
			id:'returnPayEdit',
			alias: "widget.returnPayEdit",
			title: "退单账目编辑",
			width: 750,
			height: 300,
			items:{
				xtype: "form",
				border: false,
				frame: true,
				layout: "form",
				url : basePath + '/custom/orderReturn/editReturnButtonClick',
				fieldDefaults: {
			        labelAlign: "right",
			        flex: 1,
			        margin:2
				},
				items:[
				      {
							xtype: 'fieldcontainer',
							labelStyle: 'font-weight:bold;padding:0;',
							layout: 'column',
							columnWidth:1,
							items: [
								{xtype : "numberfield", name : 'returnGoodsMoney', fieldLabel : "退商品金额（已减折让）",labelWidth:180,value:0.00,width:280,readOnly:true},
								{xtype : "numberfield", name : 'totalPriceDifference', fieldLabel : "+ 退商品差价",value:0.00,width:200,readOnly:true},
								{xtype : "numberfield", name : 'returnShipping', fieldLabel : "+ 退配送费用",value:0.00,width:200,
									decimalPrecision : 2,//精确到小数点后两位  
					                allowDecimals : true,//允许输入小数  
									listeners:{
										'change':function(field){
											this.up('window').payTotalSet();
										}
									}
								}
								
							]
						} , {
							xtype: 'fieldcontainer',
							labelStyle: 'font-weight:bold;padding:0;',
							layout: 'column',
							columnWidth:1,
							items: [
								{xtype : "numberfield", name : 'returnOtherMoney', fieldLabel : "+&nbsp;&nbsp; 退其他费用（保价+支付）",value:0.00,labelWidth:180,width:280,
									decimalPrecision : 2,//精确到小数点后两位  
					                allowDecimals : true,//允许输入小数  
									listeners:{
										'change':function(field){
											this.up('window').payTotalSet();
										}
									}
								}, {
									xtype : "numberfield",
									name : 'totalIntegralMoney',
									fieldLabel : "- 使用积分金额",
									value : 0.00,
									width:200,
									decimalPrecision : 2,// 精确到小数点后两位
									allowDecimals : true,// 允许输入小数
									nanText : '请输入有效的数字',// 无效数字提示
									listeners : {
										'change' : function(field) {
											this.up('window').payTotalSet();
										}
									}
								},
								{xtype : "numberfield", name : 'returnBonusMoney', fieldLabel : "-&nbsp;&nbsp;&nbsp;&nbsp; 红包金额",value:0.00,width:200,
									decimalPrecision : 2,//精确到小数点后两位  
					                allowDecimals : true,//允许输入小数  
					                minValue:0,
									listeners:{
										'change':function(field){
											this.up('window').payTotalSet();
										}
									}
								}
							]
						} , {
							xtype: 'fieldcontainer',
							labelStyle: 'font-weight:bold;padding:0;',
							layout: 'column',
							columnWidth:1,
							items: [
								{xtype : "numberfield", name : 'returnTotalFee', fieldLabel : "= 退款总金额",value:0.00,labelWidth:180,readOnly:true, width:280 ,
									listeners:{
										'change':function(field, newvalue,oldvalue){
											if(Ext.getCmp('moneyPaid')){
												Ext.getCmp('moneyPaid').setValue(field.getValue());
											}
											this.up('window').paymentNumSet(field, newvalue,oldvalue);
										}
									}
								}
							]
						}, {
							xtype: 'fieldcontainer',
							labelStyle: 'font-weight:bold;padding:0;',
							layout: 'column',
							id:"returnPayEditSystemPayment",
							columnWidth:1,
							items: [
								{xtype : "displayfield", width:145},
								{xtype : "displayfield", width:30,value:'='},
								{xtype :'commonsystemPayment',name:'setPayment1',fieldLabel:false,width: 150,
									listeners:{
										'change':function(field){
											this.up('window').checkPayment(field);
										}
									}
								},
								{xtype : "numberfield", name : 'setPaymentNum1', fieldLabel:false,value:'0',width:70,labelAlign: 'right',minValue:0,
									listeners:{
										'change':function(field, newvalue,oldvalue){
											this.up('window').checkPaymentNum(field);
										}
									}
								}
							]
						}  
				]
			},
			buttons:[
				{ text: "关闭", action: "close",handler : function () { this.up("window").close(); } },
				{ text: "保存", handler: me.returnPayEditSave }
			],
			initPage: function (form) {
				
				var returnForm=null;
				var returnRefunds=null;
				if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
					returnForm=Ext.getCmp('returnForm');
					returnRefunds=Ext.getCmp('returnForm').getForm().reader.rawData.returnRefunds;
				}else{
					returnForm=Ext.getCmp('returnShow');
					returnRefunds=Ext.getCmp('returnShow').getForm().reader.rawData.returnRefunds;
				}
				var len=returnRefunds.length;
				
				for(var i=0;i<len;i++){
					if(i>0){
						Ext.getCmp('returnPayEditSystemPayment').add(
								{xtype : "displayfield", width:20,value:'&nbsp;&nbsp;+'},
								{xtype :'commonsystemPayment',name:'setPayment'+(i+1)+'',fieldLabel:false,width: 150,
									listeners:{
										'change':function(field){
											this.up('window').checkPayment(field);
										}
									}
								},
								{xtype : "numberfield", name : 'setPaymentNum'+(i+1)+'', fieldLabel :false,width:70,value:"0",minValue:0,
									listeners:{
										'change':function(field){
											this.up('window').checkPaymentNum(field);
										}
									}
								}
						)
					}
				}
				
				var values=returnForm.getForm().getValues();
				values.returnGoodsMoney=returnForm.getForm().findField('returnGoodsMoney').getValue();
				values.totalPriceDifference=returnForm.getForm().findField('totalPriceDifference').getValue();
				values.returnTotalFee=returnForm.getForm().findField('returnTotalFee').getValue();
				console.dir(values);
				form.getForm().setValues(values);
				form.getForm().findField("totalIntegralMoney").setReadOnly(true);
				
				var returnRefunds=returnForm.getForm().reader.rawData.returnRefunds;
				var len=returnRefunds.length;
				if(len<=1){//当只有一种支付方式的时候，不允许手动调支付方式钱
					form.getForm().findField("setPaymentNum1").setReadOnly(true);
				}
				
				//再次对支付方式金额赋值，保证与退单页面支付方式金额一致（初始化加载页面）
				for(var i=0;i<len;i++){
					form.getForm().findField('setPaymentNum'+(i+1)).setValue(values['setPaymentNum'+(i+1)]);
				}
			},
			returnPayEditParams:function(from){
				var params = form.getValues();
				console.dir(params);
				var returnForm=null;
				if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
					returnForm=Ext.getCmp('returnForm');
				}else{
					returnForm=Ext.getCmp('returnShow');
				}
				var returnRefunds=returnForm.getForm().reader.rawData.returnRefunds;
				var returnCommon=returnForm.getForm().reader.rawData.returnCommon;
				var len=returnRefunds.length;
				for(var i=0;i<len;i++){
					params['createOrderRefundList['+i+'].returnPaySn']=returnRefunds[i].returnPaySn;
					params['createOrderRefundList['+i+'].relatingReturnSn']=returnRefunds[i].relatingReturnSn;
					params['createOrderRefundList['+i+'].returnPay']=params["setPayment"+(i+1)];
					params['createOrderRefundList['+i+'].returnFee']=params["setPaymentNum"+(i+1)];
				}
				
				params['createOrderReturn.relatingReturnSn']=returnCommon.relatingOrderSn;
				params['createOrderReturn.returnSn']=returnCommon.returnSn;
				params['createOrderReturn.returnTotalFee']=params.returnTotalFee;
				params['createOrderReturn.returnShipping']=params.returnShipping;
				params['createOrderReturn.returnOtherMoney']=params.returnOtherMoney;
				params['createOrderReturn.returnBonusMoney']=params.returnBonusMoney;		
				params['createOrderReturn.totalIntegralMoney']=params.totalIntegralMoney;		
				params['createOrderReturn.haveRefund']=params.haveRefund;
				params.orderReturnSn=returnCommon.returnSn;//退单Sn
				return params;
			},
			checkPayment:function(field){
				var returnForm=null;
				if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
					returnForm=Ext.getCmp('returnForm');
				}else{
					returnForm=Ext.getCmp('returnShow');
				}
				var i=parseInt(field.name.split('setPayment')[1]);
				var returnRefunds=returnForm.getForm().reader.rawData.returnRefunds;
				var len=returnRefunds.length;
				var thisValue=parseFloat(field.getValue());
				for(var j=1;j<=len;j++){
						if(j!=i){
							if(Ext.getCmp('setPayment'+j)){
								var otherValue=parseFloat(Ext.getCmp('setPayment'+j).getValue());
								if(thisValue==otherValue){//支付方式相同
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
					returnForm=Ext.getCmp('returnShow');
				}
				 
				//原始退款总金额
				var returnTotalFee=this.down('form').getForm().findField("returnTotalFee").getValue();;
				if(returnTotalFee==0){
					returnTotalFee=Ext.getCmp('returnPayInfomation').getForm().findField("returnTotalFee").getValue();
				}
				
				var changeTotalFee=0;
				var paySetNum=returnForm.getForm().reader.rawData.returnRefunds.length;//获取有多少个支付方式
				for(var i=1;i<=paySetNum;i++){
					changeTotalFee+=this.down('form').getForm().findField("setPaymentNum"+i).getValue();
				}
				var k=parseInt(field.name.split('setPaymentNum')[1]);
				var changeNum=0;
				
				changeNum=returnTotalFee-changeTotalFee;
				if(returnTotalFee==changeTotalFee){
					returnPayEditFlag=true;
					return false;
				}
				
				if(returnPayEditFlag){
					if(k==paySetNum&&k!=1){//说明为最后一个支付方式
					returnPayEditFlag=false;
					var lastNum=this.down('form').getForm().findField("setPaymentNum1").getValue();
					this.down('form').getForm().findField("setPaymentNum1").setValue(lastNum+changeNum);
					
				}else{
					if(this.down('form').getForm().findField("setPaymentNum"+(k+1))){
						var lastNum=this.down('form').getForm().findField("setPaymentNum"+(k+1)).getValue();
							returnPayEditFlag=false;
							this.down('form').getForm().findField("setPaymentNum"+(k+1)).setValue(lastNum+changeNum);
						return false;
					}
					
				
				}
				
				}
				
			
			},
			payTotalSet:function(){
				var form = this.down('form');
				var returnTotalFee=form.getForm().findField("returnTotalFee").getValue();
				
				var returnGoodsMoney=form.getForm().findField("returnGoodsMoney").getValue();
				var totalIntegralMoney=form.getForm().findField("totalIntegralMoney").getValue();
				var totalPriceDifference=form.getForm().findField("totalPriceDifference").getValue();
				var returnShipping=form.getForm().findField("returnShipping").getValue();
				var returnOtherMoney=form.getForm().findField("returnOtherMoney").getValue();
				var returnBonusMoney=form.getForm().findField("returnBonusMoney").getValue();
				
				
				var newReturnTotalFee=(returnGoodsMoney+totalPriceDifference+returnShipping+returnOtherMoney-returnBonusMoney-totalIntegralMoney).toFixed(2);
				form.getForm().findField('returnTotalFee').setValue(newReturnTotalFee);
				
			},
			paymentNumSet:function(field, newvalue,oldvalue){
				
				var returnRefunds=null;
				if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
					returnRefunds=Ext.getCmp('returnForm').getForm().reader.rawData.returnRefunds;
				}else{
					returnRefunds=Ext.getCmp('returnShow').getForm().reader.rawData.returnRefunds;
				}
				var len=returnRefunds.length;
				
				var form = this.down('form');
				var setPaymentNum1=form.getForm().findField("setPaymentNum1").getValue();
				if(len>1){
					if(oldvalue!=0)
					form.getForm().findField('setPaymentNum1').setValue(newvalue-oldvalue+setPaymentNum1);
				}else{
					form.getForm().findField('setPaymentNum1').setValue(newvalue);
				}
			}
		});
		win.initPage(win.down('form'));
		win.show();
	},
	returnPayEditSave:function(btn){
		var me = this;
		var win = btn.up("window");
		form = win.down("form");
		var params = win.returnPayEditParams(form);
		params.btnType = 'returnPayEdit';
		console.dir(params);
		var json = {
			params : params,
			timeout:90000,
			success : function(formPanel, action) {
				win.close();
				Ext.getCmp('returnShow').initData();
				Ext.msgBox.msg('账目信息', '数据保存成功', Ext.MessageBox.INFO);
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		form.submit(json);
	},
	updateHaveRefund: function(btn){
		var form = btn.up('form');
		this.updateReturnInfo('updateHaveRefund',form);
	},
	updateExpress: function(btn){
		var form = btn.up('form');
		this.updateReturnInfo('updateExpress',form);
	},
	updateInvoiceNo: function(btn){
		var form = btn.up('form');
		this.updateReturnInfo('updateInvoiceNo',form);
	},
	updateDepotCode: function(btn){
		var form = btn.up('form');
		this.updateReturnInfo('updateDepotCode',form);
	},
	updateReturnInfo: function(type,form){
		var me = this;
		var param = form.builtParam(type,form);
		param.btnType = 'editReturn';
		Ext.Ajax.request({
			url:  basePath + '/custom/orderReturn/editReturnButtonClick',
			params: param,
			timeout:90000,
			data:"json",
			success: function(response){
				var text = response.responseText;
				var result = Ext.JSON.decode(text);
				console.dir(result);
				if (result) {
					Ext.getCmp('returnShow').initData();
					Ext.msgBox.msg('基本信息', '数据更新成功', Ext.MessageBox.INFO);
					
				}
			},
			failure: function(response){
				Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
			}
		});
	},
	messageEdit : function(btn) {
		if(returnSn != ''){
			var relOrderSn = Ext.ComponentQuery.query("displayfield[name='relatingOrderSn']")[0].value;
			if(relOrderSn == '' || relOrderSn == undefined){
				console.error("关联原订单号获取失败无法加载仓库信息");
				return;
			}
			var win = Ext.create("Ext.window.Window",{
				id:'messageEdit',
				alias: "widget.messageEdit",
				title: "短信编辑发送界面",
				width: 750,
				height: 400,
				channelShopDatas:undefined,
				items:{
					xtype: "form",
					border: false,
					frame: true,
					layout: "form",
					fieldDefaults: {
				        labelAlign: "right",
				        flex: 1,
				        margin:1
					},
					items:[
						{
							xtype: 'fieldcontainer',
							labelStyle: 'font-weight:bold;padding:0;',
							columnWidth: 1,
							items: [
								{ xtype: "textfield", name: "mobile", fieldLabel: "短信手机号",labelWidth:150,width:'80%',value:'*** **** ****'}]
						},
						{
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
					                columns: 4,
					                items: []
					            }]
						}
						/*,
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
						,
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
				
				},
				buttons:[{ text: "关闭", action: "close",handler : function () { this.up("window").close(); } }],
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
//					this.down('form').getForm().findField('vipMsg').setValue('您好！请您将商品进行拍照后发至邦购邮箱：banggovip@metersbonwe.com 请您将订单号为您的邮件主题以便我们能查看到您的邮件，为了能及时为您处理退换货请您发送邮件后联系客服4008219988,感谢您的配合!');
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
			win.initPage(relOrderSn);
			win.show();
		}
	},
	/**
	 * 退单操作日志
	 */
	actionClick : function(btn) {
		var me = this;
		var win = Ext.create('Ext.window.Window', {
			id: 'action_note',
			height: 175,
			width: 367,
			scrollable: false,
			bodyPadding: 2,
			border:true,
			resizable:false,
			plain: true,
			maskDisabled : false ,
			modal : true ,
			items: [{
				xtype : 'form',
				items: [{
					xtype: 'textarea',
					emptyText : '日志内容', 
					name : 'message',
					style:'border:solid 0.5px gray;',
					width: 360,
					height : 100
				}]
			}],
			buttons : [
				{ text: "保存", handler : function () {
						var values = this.up("window").down('form').getValues();
						me.callReturnService(btn,this.up("window"));
					}
				}
			]
		});
		win.show();
	},
	onRemoveClick: function(rowIndex){ // 删除商品
		var grid = Ext.getCmp('returnGoodsEdit').down('grid');
		var num = grid.getStore().getData().length;
		if (num == 1) {
			Ext.msgBox.remainMsg('删除商品', "商品数据不能全部删除,必须保留一条记录！", Ext.MessageBox.ERROR);
			return ;
		}
		grid.getStore().removeAt(rowIndex);
		var store =[];
		grid.getStore().each(function(record){
			store.push(record);
		}) 
		grid.store.loadData(store);
		Ext.msgBox.msg('删除商品', "删除成功！", Ext.MessageBox.INFO);
	}
});