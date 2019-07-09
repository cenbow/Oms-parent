Ext.define("MB.view.returnOrder.ReturnShow", {
	extend : "Ext.form.Panel",
	alias : 'widget.returnShow',
	id : 'returnShow',
	width : '100%',
	frame : true,
	autoScroll : true,
	bodyStyle : {
		padding : '8px'
	},
	fieldDefaults : {
		labelAlign : 'right'
	},
	initComponent : function() {
		var selModel = false, plugins = false;
		if (returnType != 2) {//非拒收入库单添加监听
			selModel = new Ext.selection.CheckboxModel({
						checkOnly : false
					}),
			plugins = {
				ptype : 'cellediting',
				clicksToEdit : 1,
				listeners : {
					beforeedit : 'onCellBeforeEdit',
					afteredit : 'onCellAfterEdit'
				}
			}
		}

		// 支付方式组件
		Ext.define("MB.view.common.SystemPayment", {
			extend : "Ext.form.field.ComboBox",
			alias : 'widget.commonsystemPayment',
			store : Ext.create("Ext.data.Store", {
						model : Ext.create("Ext.data.Model", {
									fields : [{
												name : 'payCode',
												type : 'string'
											}, {
												name : 'payName',
												type : 'string'
											}]
								}),
						proxy : {
							type : 'ajax',
							actionMethods : {
								read : 'POST'
							},
							timeout : 90000,
							url : basePath + 'custom/common/selectSystemPaymentList',
							reader : {
								type : 'json'
							}
						},
						autoLoad : true
					}),
			displayField : 'payName',
			valueField : 'payId',
			queryMode : 'local',
			width : '220',
			labelWidth : '70',
			fieldLabel : '支付方式',
			name : 'payId',
			hiddenName : 'payId',
			emptyText : '请选择支付方式',
			editable : false
			});
		var me = this;
		this.items = [{//基本信息
			xtype : 'form',
			id : 'returnSetModule',
			itemId : 'returnSetModule',
			title : '基本信息',
			width : '100%',
			defaults : {
				labelWidth : 200,
				columnWidth : 1
			},
			items : [{//第一行
				xtype : 'fieldcontainer',
				layout : 'column',
				items : [{
					xtype : "displayfield", name : 'returnSn', fieldLabel : "退单号", columnWidth : .33,
					renderer : function(value) {
						if (value != undefined && value != null) {
							var url = order_return_url + value + "&isHistory=0";
							return returnALabel(url,value);
						}
					}
				},{
					xtype : "displayfield", name : 'returnType', fieldLabel : "退单类型", columnWidth : .33,
					renderer : function(value) {
						if (value == 1) {
							return "退货退款单";
						} else if (value == 2) {
							return "拒收入库单";
						} else if (value == 3) {
							return "无货退款单";
						} else if (value == 4) {
							return "额外退款单";
						} else if (value == 5) {
							return "失货退货单";
						}
					}
				}, {
					xtype : "combobox",
					store : Ext.create('Ext.data.Store', {
								model : "MB.model.CommonStatusModel",
								data : [['1', "需要退款"], ['0', '无须退款']]
							}),
					displayField : 'name',
					valueField : 'id',
					queryMode : 'local',
					name : 'haveRefund',
					hiddenName : 'id',
					emptyText : '请选择',
					editable : false,
					fieldLabel : "是否退款",
					columnWidth : .24
				}, {
					xtype : "displayfield", columnWidth : .04
				}, {
					xtype : "button", text : "更新", columnWidth : .05, id : 'updateHaveRefund', action : 'updateHaveRefund'
				}]
			}, {//第二行
				xtype : 'fieldcontainer',
				layout : 'column',
				items : [{
					xtype : "displayfield", name : 'relatingOrderSn', fieldLabel : "关联子订单号", columnWidth : .33,
					renderer : function(value) {
						if (value != undefined && value != null) {
							var url = order_info_url +"?masterOrderSn="+masterOrderSn+"&orderSn="+ value + "&isHistory=0";
							return returnALabel(url,value);
						}
					}
				}, {
					xtype : "displayfield", name : 'returnStatusDisplay', fieldLabel : "退单状态", columnWidth : .33,
					renderer : function(value) {
						return "<span style='color:red;'>" + value + "</span>";
					}
				}
				,
				{
					xtype : "textfield", name : 'returnInvoiceNo', fieldLabel : "退货快递单号", columnWidth : .24
				}
				,
				{
					xtype : "displayfield", columnWidth : .04
				}
				,
				{
					xtype : "button", text : "更新", columnWidth : .05, id : 'updateInvoiceNo', action : 'updateInvoiceNo'
				}]
			}, {//第三行
				xtype : 'fieldcontainer',
				layout : 'column',
				items : [{
						xtype : "displayfield", name : 'masterOrderSn', fieldLabel : "关联主单号", columnWidth : .33,
						renderer : function(value) {
							if (value != undefined && value != null) {
								var url = order_info_url +"?masterOrderSn="+ value + "&isHistory=0";
								return returnALabel(url,value);
							}
						}
				},{
					xtype : "displayfield", name : 'channelName', fieldLabel : "渠道来源", columnWidth : .33
				},{
					xtype : "textfield", name : 'returnExpress', fieldLabel : "退货承运商", columnWidth : .24
				}, {
					xtype : "displayfield", columnWidth : .04
				}, {
					xtype : "button", text : "更新", columnWidth : .05, id : 'updateExpress', action : 'updateExpress'
				}]
			}, {//第四行
				xtype : 'fieldcontainer',
				layout : 'column',
				items : [{
							xtype : "displayfield", name : 'newOrderSn', fieldLabel : "换货单号", columnWidth : .33
						},{
							xtype : "displayfield", name : 'userId', fieldLabel : "下单人ID", columnWidth : .33
						}
						,
						{
							xtype : "combobox",
							store : Ext.create("Ext.data.Store", {
								model : Ext.create("Ext.data.Model", {
									fields : [{
											name : 'warehouseCode',
											type : 'string'
										}, {
											name : 'warehouseName',
											type : 'string'
									}]
								}),
								proxy : {
									type : 'ajax',
									actionMethods : {
										read : 'POST'
									},
									url : basePath + 'custom/common/getReturnGoodsDepotList',
									reader : {
										type : 'json'
									}
								},
								autoLoad : true
							}),
							displayField : 'warehouseName',
							valueField : 'warehouseCode',
							queryMode : 'remote',
							fieldLabel : '退货仓库',
							hiddenName : 'warehouseCode',
							emptyText : '请选择退货仓库',
							editable : false,
							name : 'depotCode',
							fieldLabel : "退货仓库",
							columnWidth : .24
						}
						/*{
							xtype : "textfield",
							fieldLabel : '退货仓库',
							name : 'depotCode',
							value : 'DEFAULT',
							columnWidth : .24
						}*/
						,
						{
							xtype : "displayfield",
							columnWidth : .04
						}
						,
						{
							xtype : "button",
							text : "更新",
							columnWidth : .05,
							id : 'updateDepotCode',
							action : 'updateDepotCode'
						}]
			}, {//第五行
				xtype : 'fieldcontainer',
				layout : 'column',
				items : [{
					xtype : "combobox",
					store : Ext.create('Ext.data.Store', {
								model : "MB.model.CommonStatusModel",
								data : [
										['0', "无操作"],
										['1', '退货'],
										['2', '换货']
									]
							}),
					displayField : 'name',
					valueField : 'id',
					queryMode : 'local',
					hiddenName : 'id',
					emptyText : '请选择',
					editable : false,
					name : 'processType',
					fieldLabel : "处理方式",
					columnWidth : .33
				},{
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
					columnWidth : .33
				},{
					xtype : "combobox",
					store : Ext.create("Ext.data.Store", {
						model : Ext.create("Ext.data.Model", {
							fields : [{ name : 'code' }, // 编码
								{ name : 'name' }, // 名称
								{ name : 'note' }, // 备注
								{ name : 'type' } // 类型
							]
						}),
						proxy : {
							type : 'ajax',
							actionMethods : {
								read : 'POST'
							},
							url : basePath + 'custom/common/getOrderCustomDefine',
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
					columnWidth : .33
				}]
			},{//第六行
				xtype : 'fieldcontainer',
				layout : 'column',
				items : [{
							xtype : "displayfield", name : 'addTime', fieldLabel : "退单时间", columnWidth : .33
						}, {
							xtype : "displayfield", name : 'checkInTime', fieldLabel : "入库时间", columnWidth : .33
						}, {
							xtype : "displayfield", name : 'clearTime', fieldLabel : "结算时间", columnWidth : .33
						}]
			},{//第七行
				xtype : 'fieldcontainer',
				layout : 'column',
				items : [{
							xtype : "textfield", name : 'returnDesc', fieldLabel : "退单备注", columnWidth : .66
						}]
			}],
			tools : [{
						type : 'gear',
						id : 'returnSetModuleG',
						tooltip : '修改',
						action : 'orderReturnEdit',
						hidden : true,
						scope : me
					}],
			builtParam: function(type,form){
		    	var params = form.getValues();
				var returnForm=null;
				if(typeof exchangeOrderSn != 'undefined' && exchangeOrderSn){
					returnForm=Ext.getCmp('returnForm');
				}else{
					returnForm=Ext.getCmp('returnShow');
				}
				var returnCommon=returnForm.getForm().reader.rawData.returnCommon;
				
				if(type == 'updateHaveRefund'){
					params['createOrderReturn.haveRefund']=params.haveRefund;
					params['createOrderReturn.relatingReturnSn']=returnCommon.relatingOrderSn;
					params['createOrderReturn.returnSn']=returnCommon.returnSn;
				}else{
					params['createOrderReturnShip.relatingReturnSn']=returnCommon.returnSn;
					params['createOrderReturnShip.returnExpress']=params.returnExpress;
					params['createOrderReturnShip.returnInvoiceNo']=params.returnInvoiceNo;
					params['createOrderReturnShip.depotCode']=params.depotCode;
				}
				params.orderReturnSn=returnCommon.returnSn;//退单Sn
				return params;
			}
		},{//商品列表
			titleCollapse : true,
			xtype : 'form',
			itemId : 'returnGoodsSetModule',
			id : 'returnGoodsSetModule',
			width : '100%',
			frame : true,
			title : '商品信息',
			head : true,
			layout : 'fit',
			bodyPadding : 10,
			items : [{
				xtype : 'grid',
				id : "returnGoodsList",
				requires : ['Ext.grid.plugin.CellEditing'],
				store : Ext.create("Ext.data.Store", {
					id : "returnGoodsStore",
					model : Ext.create("Ext.data.Model", {
						fields : [{ name : 'goodsName' },// 商品名称
								{ name : 'extensionCode' },// 商品属性
								{ name : 'extensionId' },// 商品扩展属性id
								{ name : 'goodsSn' },// 货号
								{ name : 'goodsColorName' },// 颜色
								{ name : 'goodsSizeName' },// 尺寸
								{ name : 'customCode' },// 企业SKU码
								{ name : 'marketPrice' },// 商品价格
								{ name : 'goodsPrice' },// 成交价格
								{ name : 'settlementPrice' },// 财务价格
								{ name : 'shareBonus' },// 分摊金额
								{ name : 'goodsBuyNumber' },// 购买数量
								{ name : 'discount' },// 折扣
								{ name : 'shareSettle' },// 财务分摊金额
								{ name : 'osDepotCode' },// 所属发货仓
								{ name : 'shopReturnCount' },// 门店退货量
								{ name : 'havedReturnCount' },// 已退货量
								{ name : 'canReturnCount' },// 可退货量
								{ name : 'returnReason' },// 退换货原因
								{ name : 'priceDifferNum' },// 退差价数量
								{ name : 'priceDifference' },// 退差价单价
								{ name : 'returnReason' },
								{ name : 'isGoodReceived' },
								{ name : 'qualityStatus' },
								{ name : 'checkinStatus' },
								{ name : 'seller' },
								{ name : 'integralMoney' },
								{ name : 'salesMode' },
								{ name : 'sap' },
								{ name : 'bvValue' },
								{ name : 'baseBvValue' }
								]
					})
				}),
				autoRender : true,
				columnLines : true,
				width : '100%',
				loadMask : true, // 读取数据时的遮罩和提示功能即加载loding...
				frame : true,
				listeners : {
					'selectionchange' : function(sm, rowIndex, record, d) {
						// 获取选中的行
						var returnCenter = Ext.getCmp('returnShow');
						var returnPaySetModule = Ext.getCmp('returnPaySetModule');
						var items = this.getSelectionModel().selected.items;
						var goodData = [];
						for (var i = 0; i < items.length; i++) {
							goodData.push(items[i].data);
						}
						returnPaySetModule.paySet(goodData);
						if (items.length > 0) {
							var rawData = {};
							if (typeof exchangeOrderSn != 'undefined' && exchangeOrderSn) {
								center = Ext.getCmp('returnForm');
							} else {
								center = Ext.getCmp('returnShow')
							}
						}
					},
					'cellclick' : function(grid, rowIndex, columnIndex, e) {
						// 获取选中的行
						var rows = grid.getSelectionModel().selected.items;

						var returnCenter = Ext.getCmp('returnShow');
						var returnPaySetModule = Ext.getCmp('returnPaySetModule');
						var items = grid.getSelectionModel().selected.items;
						var goodData = [];
						for (var i = 0; i < items.length; i++) {
							goodData.push(items[i].data);
						}
						returnPaySetModule.paySet(goodData);
						var len = rows.length;
						var editFlag = false;
						for (var i = 0; i < len; i++) {
							// 判断当前行的id是否在选中行里
							if (e.id == rows[i].id) {
								editFlag = true;
								break;
							}
						}
						return editFlag;
					}
				},
				resizable : true,
				viewConfig : {
					forceFit : true, // 注意不要用autoFill:true,那样设置的话当GridPanel的大小变化（比如你resize了它）时不会自动调整column的宽度
					scrollOffset : 0,
					// 不加这个的话，会在grid的最右边有个空白，留作滚动条的位置
					enableTextSelection: true
				},
				selModel : selModel,
				plugins : plugins,
				columns : {
					defaults : {
						align : 'center',
						sortable : false,
						menuDisabled : true
					},
					items : [{
						text : '商品名称',
						width : 150,
						align : 'center',
						dataIndex : 'goodsName',
						renderer : function(value, meta, record) {
							if(value){
								var max = 15; // 显示多少个字符
								meta.tdAttr = 'data-qtip="' + value + '"';
								return value.length < max ? value : value.substring(0, max - 3) + '...';
							}else {
								return '';
							}
						}
					},
					{
						dataIndex : 'extensionId',
						sortable : false,
						menuDisabled : true,
						hidden : true
					}, {
						text : '商品属性',
						width : 75,
						align : 'center',
						dataIndex : 'extensionCode',
						renderer : function(value,metaData,record,rowIndex) {
							
							if (value == 'group') {
								return "套装";
							} else if (value.indexOf('gif') != -1) {
								return "赠品";
							} else {
								return "普通商品";
							}
						}
					}, {
						text : '货号',
						align : 'center',
						dataIndex : 'goodsSn',
						sortable : false,
						menuDisabled : true
					}, {
						header : '规格',
						columns : [{
							header : "颜色",
							width : 80,
							dataIndex : 'goodsColorName',
							sortable : false,
							menuDisabled : true,
							renderer : function(value, meta, record) {
								if (value) {
									var max = 15; // 显示多少个字符
									meta.tdAttr = 'data-qtip="' + value + '"';
									return value.length < max ? value : value.substring(0, max - 3) + '...';
								}
								return ' ';
							}

						}, {
							header : "尺寸",
							width : 80,
							dataIndex : 'goodsSizeName',
							sortable : false,
							menuDisabled : true,
							renderer : function(value, meta, record) {
								if (value) {
									var max = 15; // 显示多少个字符
									meta.tdAttr = 'data-qtip="' + value + '"';
									return value.length < max ? value : value.substring(0, max - 3) + '...';
								} else {
									return '';
								}
							}

						}]
					}, {
						text : '企业SKU码',
						width : 120,
						align : 'center',
						dataIndex : 'customCode',
						sortable : false,
						menuDisabled : true,
						renderer : function(value, meta, record) {
							var max = 15; // 显示多少个字符
							meta.tdAttr = 'data-qtip="' + value + '"';
							if (value)
								return value.length < max ? value : value.substring(0, max - 3) + '...';

						}
					}
					,
					/*{
						text : '入库状态',
						width : 120,
						align : 'center',
						dataIndex : 'seller',
						sortable : false,
						menuDisabled : true,
						renderer : function(value, meta, record) {
							var checkinStatus= record.get('checkinStatus');
							var msg = "";
							if(checkinStatus == 1){
								msg = '<font style="color:green">已入库 </font>';
							}else if(checkinStatus == 2){
								msg = '<font style="color:red">待入库 </font>';
							}else if(checkinStatus == 3){
								msg = '<font style="color:red">部分入库 </font>';
							}else{
								msg = '<font style="color:red">未入库 </font>';
							}
							return msg;
						}
					}
					,
					{
						text : '扫描入库数量',
						width : 75,
						align : 'center',
						dataIndex : 'prodScanNum',
						sortable : false,
						menuDisabled : true
					} 
					,*/
					{
						text : '退货量',
						width : 75,
						align : 'center',
						dataIndex : 'canReturnCount',
						sortable : false,
						menuDisabled : true,
						renderer : function(value, meta, record) {
							meta.style = 'background:' + editColor;
							return value;
						},
						editor : {
							xtype : 'numberfield',
							allowBlank : false,
							minValue : 0,
							allowDecimals : false
						}
					}
					/*,
					{
						text : '供销商',
						width : 120,
						align : 'center',
						dataIndex : 'seller',
						sortable : false,
						menuDisabled : true,
						renderer : function(value, meta, record) {
							var msg = value;
							if(!msg){
								msg ="";
							}
							if (value && (value == 'MB' || value == 'HQ01') ) {
								msg = '<font style="color:green">' + value + '</font>'
							} else {
								msg = '<font style="color:red">' + value + '</font>'
							}
							return msg;

						}
					}*/
					,
					{
						text : '商品价格',
						width : 75,
						align : 'center',
						dataIndex : 'marketPrice',
						sortable : false,
						menuDisabled : true
					}, {
						text : '成交价格',
						width : 75,
						align : 'center',
						dataIndex : 'goodsPrice',
						sortable : false,
						menuDisabled : true
					}, {
						text : '财务价格',
						width : 75,
						align : 'center',
						dataIndex : 'settlementPrice',
						sortable : false,
						menuDisabled : true
					}, {
						text : '分摊金额',
						width : 75,
						align : 'center',
						dataIndex : 'shareBonus',
						sortable : false,
						menuDisabled : true
					}, {
						text : '购买数量',
						width : 75,
						align : 'center',
						dataIndex : 'goodsBuyNumber',
						sortable : false,
						menuDisabled : true
					}
					/*,
					{
						text : '财务分摊金额',
						width : 95,
						align : 'center',
						dataIndex : 'shareSettle',
						sortable : false,
						menuDisabled : true
					}*/
					,
					{
						text : '折让金额',
						width : 95,
						align : 'center',
						dataIndex : 'discount',
						sortable : false,
						menuDisabled : true
					}, 
					
					{
						text : '所属发货仓',
						width : 75,
						align : 'center',
						dataIndex : 'osDepotCode',
						sortable : false,
						menuDisabled : true
					}
					,
					{
						text : '已退货量',
						width : 75,
						align : 'center',
						dataIndex : 'havedReturnCount',
						sortable : false,
						menuDisabled : true
					}
					/*,
					{
						text : '退差价数量',
						width : 75,
						align : 'center',
						dataIndex : 'priceDifferNum',
						sortable : false,
						menuDisabled : true,
						renderer : function(value, meta, record) {
							meta.style = 'background:' + editColor;
							return value;
						},
						editor : {
							xtype : 'numberfield',
							allowBlank : false,
							minValue : 0,
							allowDecimals : false
						}
					}
					,
					{
						text : '退差价单价',
						width : 75,
						align : 'center',
						dataIndex : 'priceDifference',
						sortable : false,
						menuDisabled : true,
						renderer : function(value, meta, record) {
							meta.style = 'background:' + editColor;
							return value;
						},
						editor : {
							xtype : 'numberfield',
							allowBlank : false,
							minValue : 0,
							decimalPrecision : 2,// 精确到小数点后两位
							allowDecimals : true
							// 允许输入小数
						}
					}
					,
					{
						text : '退差价小计',
						width : 75,
						align : 'center',
						dataIndex : 'priceDiffTotal',
						sortable : false,
						menuDisabled : true,
						renderer : function(value, cellMeta, record, rowIndex,
								columnIndex, store) {
							var priceDifferNum = record.get('priceDifferNum');
							var priceDifference = record.get('priceDifference');
							return priceDifferNum * priceDifference;
						}
					}
					*/
					,
					 {
						text : '原因',
						width : 100,
						align : 'center',
						dataIndex : 'returnReason',
						sortable : false,
						menuDisabled : true,
						renderer : function(value, meta, record) {
							meta.style = 'background:' + editColor;
							return value;
						},
						editor : {
							xtype : 'textfield',
							allowBlank : false,
							allowDecimals : false
						}
					}
					,
					{
						text : '图片url',
						align : 'center',
						dataIndex : 'goodsThumb',
						sortable : false,
						menuDisabled : true,
						hidden : true
					}
					,
					{
						text : 'sap',
						align : 'center',
						dataIndex : 'sap',
						sortable : false,
						menuDisabled : true,
						hidden : true
					}
					,
					{
						text : 'bvValue',
						align : 'center',
						dataIndex : 'bvValue',
						sortable : false,
						menuDisabled : true,
						hidden : true
					},
					{
						text : 'baseBvValue',
						align : 'center',
						dataIndex : 'baseBvValue',
						sortable : false,
						menuDisabled : true,
						hidden : true
					}
					,
					/*{
							text : '操作',
							width : 250,
							align : 'center',
							sortable : false,
							menuDisabled : true,
							renderer: function(value, metaData, record, rowIndex) {
								var id = Ext.id();
								var goodsId = record.get('id');
								console.dir(record);
								var isGoodReceived ="收货",
								isGoodReceivedFlag = false,
								qualityStatus = "质检通过",
								qualityStatusFlag = false,
								checkinStatus = "入库",
								checkinStatusFlag = false ;
								if(record.get('isGoodReceived') == 1){
									isGoodReceived = "未收货";
								}else{
									qualityStatusFlag = true;//未收货不能质检
									checkinStatusFlag = true;//未收货不能入库
								}
								if(record.get('qualityStatus') == 1){
									qualityStatus = "质检不通过";
									isGoodReceivedFlag = true;
								}else{
									checkinStatusFlag = true;//未质检通过不能入库
								}
								if(record.get('checkinStatus') == 1){
									checkinStatusFlag = true;//已入库不能再入库
									qualityStatusFlag = true;
									isGoodReceivedFlag = true;
								}
								
								if(parseInt(returnType)>2){//退款单禁用这些按钮
									heckinStatusFlag = true;
									qualityStatusFlag = true;
									isGoodReceivedFlag = true;
								}
								
								if(!auth[rolePrefix + 'storageReturn'] || !auth[rolePrefix + 'virtualStorageReturn']){
									heckinStatusFlag = true;
									qualityStatusFlag = true;
									isGoodReceivedFlag = true;
								}
								// 支付状态和权限
								setTimeout(function() {
									var panel = Ext.create('Ext.panel.Panel', {
										bodyPadding: 0,
										border:false,
										baseCls: 'my-panel-no-border',
										height: 15,
										layout: 'column',
										columnWidth : 1,
										items: [{
											columnWidth: 1,
											xtype: 'segmentedbutton',
											allowToggle: false,
											items: [{
												text: isGoodReceived,
												disabled: isGoodReceivedFlag,
												handler: function () {
													Ext.getCmp('returnShow').returnOrderUpdate(record,"isGoodReceived");
												}
											},{
												text: qualityStatus,
												disabled: qualityStatusFlag,
												handler: function () {
													Ext.getCmp('returnShow').returnOrderUpdate(record,"qualityStatus");
												}
											},{
												text: checkinStatus,
												disabled: checkinStatusFlag,
												handler: function () {
													var checkFlag = true;
													Ext.Ajax.request({
													url:  basePath + '/custom/orderReturn/checkDepotCode',
													params: {"returnSn":returnSn,"type":"haveDepotFlag"},
													async: false,
													timeout:90000,
													data:"json",
													success: function(response){
														var text = response.responseText;
														var result = Ext.JSON.decode(text);
														checkFlag = result.success;
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
													//第三方商品不允许oms入库
													var salesMode = Ext.getCmp('returnGoodsList').store.getAt(0).get('salesMode');
													if(salesMode && parseInt(salesMode) == 4){
														Ext.msgBox.remainMsg("结果", "第三方商品不允许oms入库！", Ext.MessageBox.ERROR);
														return false;
													}
													Ext.getCmp('returnShow').returnOrderUpdate(record,"checkinStatus");
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
					,*/
					{
						text : '商品销售模式',
						align : 'center',
						dataIndex : 'salesMode',
						sortable : false,
						menuDisabled : true,
						hidden : true
					}
					]
				},
				hideColumn : function(data, grid) {
					if (data.returnType == 1 || data.returnType == 2 || data.returnType == 5) {
						grid.columns[15].hide();// 财务分摊金额
						for (var i = 20; i <= 22; i++) {// 退差价
							grid.columns[i].hide();
						}
					} else if (data.returnType == 3) {
						for (var i = 16; i <= 22; i++) {
							if (i != 19) {
								grid.columns[i].hide();
							}
						}
					} else {
						for (var i = 15; i <= 19; i++) {
							grid.columns[i].hide();
						}
					}

				},
				onCellBeforeEdit : function(editor, ctx, eOpts) { // 商品属性初始化
					var clickColIdx = ctx.colIdx; // grid column 展示列下标
					var record = ctx.record;
					if (clickColIdx == 18) { // 可退货量
						var goodsNumber = record.get("goodsBuyNumber"), shopReturnCount = record.get("shopReturnCount"), havedReturnCount = record.get("havedReturnCount");
						var canReturnCount = goodsNumber - shopReturnCount - havedReturnCount;
						var numberf = ctx.grid.columns[19].getEditor(ctx.record);
						numberf.setMaxValue(canReturnCount, false);
					} else if (clickColIdx == 15) {// 退差价数量
						var goodsNumber = record.get("goodsBuyNumber");
						var numberf = ctx.grid.columns[20].getEditor(ctx.record);
						numberf.setMaxValue(goodsNumber, false);
					}
				},
				onCellAfterEdit : function(editor, ctx, eOpts) { // 修改商品信息后动态变化
					var clickColIdx = ctx.colIdx; // grid column 展示列下标
					var record = ctx.record;
					var returnCenter = Ext.getCmp('returnShow');
					var returnPaySetModule = Ext.getCmp('returnPaySetModule');
					var items = ctx.grid.getSelectionModel().selected.items;
					var goodData = [];
					for (var i = 0; i < items.length; i++) {
						goodData.push(items[i].data);
					}
					returnPaySetModule.paySet(goodData);
				}
			}],
			tools : [{
						type : 'gear',
						id : 'returnGoodsSetModuleG',
						tooltip : '修改',
						hidden : true,
						action : 'returnGoodsEdit',
						scope : me
				}]
		}
		,
		{
			xtype : 'form',
			id : 'returnShipModule',
			itemId : 'returnShipModule',
			title : '收货人信息',
			width : '100%',
			defaults : {
				labelWidth : 200,
				columnWidth : 1
			},
			items : [{
						xtype : 'fieldcontainer',
						labelStyle : 'font-weight:bold;padding:0;',
						layout : 'column',
						columnWidth : 1,
						defaultType : 'displayfield',
						labelWidth : 200,
						items : [{
									xtype : "displayfield",
									name : 'consignee',
									fieldLabel : "收货人",
									columnWidth : .33
								}
								,
								{
									xtype : "displayfield",
//									name : 'encodeMobile',
									name : 'mobile',
									fieldLabel : "手机",
									columnWidth : .33
//									value : '******'
								}
								,
								/*{
									xtype : "hidden",
									name : 'mobile',
									fieldLabel : "手机"
								}
								,*/
								{
									xtype : "displayfield",
//									name : 'encodeTel',
									name : 'tel',
									fieldLabel : "电话",
									columnWidth : .28
//									value : '******'
								}
								/*,
								{
									xtype : "hidden",
									name : 'tel',
									fieldLabel : "电话"
								}
								,
								{
									xtype : 'button',
									text : '解密',
									id : 'returnDecodeMobile',
									columnWidth : .05,
									handler : me.decodeLinkMobile
								}*/
							]
					}, {
						xtype : 'fieldcontainer',
						labelStyle : 'font-weight:bold;padding:0;',
						layout : 'column',
						columnWidth : 1,
						defaultType : 'displayfield',
						labelWidth : 200,
						items : [{
									xtype : "displayfield",
									name : 'address',
									fieldLabel : "详细地址",
									columnWidth : .66
								}, {
									xtype : "displayfield",
									name : 'zipcode',
									fieldLabel : "邮编",
									columnWidth : .33
								}]
					}, {
						xtype : 'fieldcontainer',
						labelStyle : 'font-weight:bold;padding:0;',
						layout : 'column',
						columnWidth : 1,
						defaultType : 'displayfield',
						labelWidth : 200,
						items : [{
									xtype : "displayfield",
									name : 'email',
									fieldLabel : "电子邮件",
									columnWidth : .33
								}, {
									xtype : "displayfield",
									name : 'signBuilding',
									fieldLabel : "标志性建筑",
									columnWidth : .33
								}, {
									xtype : "displayfield",
									name : 'bestTime',
									fieldLabel : "最佳送货时间",
									columnWidth : .33
								}]
					}]
		}, 
		{
			xtype : 'form',
			id : 'orderPayInfo',
			itemId : 'orderPayInfo',
			width : '100%',
			frame : true,
			title : '原订单支付信息',
			head : true,
			layout : 'fit',
			bodyPadding : 10,
			buttonAlign : 'center',// 按钮居中
			items : [{
				xtype : "grid",
				id : 'orderPayList',
				store : Ext.create('Ext.data.Store', {
					model : Ext.create("Ext.data.Model", {
								fields : [{
											name : 'masterPaySn'
										},// 付款单编号
										{
											name : 'mergePaySn'
										},//合并付款编号
										{
											name : 'payName'
										},// 支付方式
										{
											name : 'payNote'
										},// 付款备注
										{
											name : 'bonusName'
										},// 使用红包
										{
											name : 'goodsNumber'
										},// 商品数量
										{
											name : 'surplus'
										},// 余额支付
										{
											name : 'payTotalfee'
										},// 付款总金额
										{
											name : 'payStatus'
										},// 支付状态
										{
											name : 'payTime'
										},// 支付时间
										{
											name : 'createTime'
										},// 付款单生成时间
										{
											name : 'payLasttime'
										},// 付款最后期限
										{
											name : 'payStatus'
										}// 操作
								]
							})
				}),
				autoRender : true,
				columnLines : true,
				width : '100%',
				loadMask : true, // 读取数据时的遮罩和提示功能即加载loding...
				resizable : true,
				forceFit : true,
				viewConfig: {enableTextSelection: true},
				columns : [
					    { text: '付款单编号',align : 'center',width: clientWidth*0.12, dataIndex: 'masterPaySn'},
					    { text: '合并付款编号',align : 'center',width: clientWidth*0.12, dataIndex: 'mergePaySn'},
						{ text: '支付方式',align : 'center',width: clientWidth*0.08, dataIndex: 'payName'},
						{ text: '付款方式ID',align : 'center',dataIndex: 'payId',hidden: true},
						{ text: '付款总金额',align : 'center',width: clientWidth*0.08, dataIndex: 'payTotalfee'},
						{ text: '付款备注', align : 'center',align : 'center',width: clientWidth*0.08, dataIndex: 'payNote'},
						{ text: '订单支付时间',align : 'center', width: clientWidth*0.1, dataIndex: 'payTime',
							renderer:function(value){
								if (value) {
									var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
									return createTime;
								}
							}
						},
						{ text: '付款单生成时间',align : 'center',width: clientWidth*0.1, dataIndex: 'createTime',
							renderer:function(value){
								if (value) {
									var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
									return createTime;
								}
							}
						},
						{ text: '付款最后期限', align : 'center',width: clientWidth*0.1, dataIndex: 'payLasttime',
							renderer:function(value){
								if (value) {
									var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
									return createTime;
								}
							}
						},
						{ text: '付款状态',align : 'center',width: clientWidth*0.08, dataIndex: 'payStatus',
							renderer: function(value, metaData, record, rowIndex) {
								var returnValue = '';
								if(value=='0'){
									returnValue = '未付款';
								}else if(value=='1'){
									returnValue = '部分付款';
								}else if(value=='2'){
									returnValue = '已付款';
								}else if(value=='3'){
									returnValue = '已结算';
								}else if(value=='4'){
									returnValue = '待确认';
								}
								return returnValue;
							}
						}]
			}]
		},
		{
			xtype : 'form',
			id : 'returnPayInfomation',
			width : '100%',
			frame : true,
			title : '退单付款信息',
			head : true,
			bodyPadding : 10,
			layout : 'column',
			buttonAlign : 'center',// 按钮居中
			fieldDefaults : {
				labelAlign : 'left'
			},
			items : [{
						xtype : 'fieldcontainer',
						labelStyle : 'font-weight:bold;padding:0;',
						layout : 'column',
						columnWidth : 1,
						defaultType : 'displayfield',
						labelWidth : 200,
						items : [{
									xtype : "displayfield",
									name : 'returnTotalFee',
									fieldLabel : "退款总金额",
									width : clientWidth * 0.12,
									value : '0',
									labelAlign : 'right'
								}, {
									xtype : "displayfield",
									width : clientWidth * 0.03,
									value : '='
								}, {
									xtype : 'commonsystemPayment',
									id : 'setPayment1',
									name : 'setPayment1',
									fieldLabel : false,
									width : clientWidth * 0.1,
									listeners : {
										'change' : function(field) {
											this.up('form').fillPayment(field);
										}
									}
								}, {
									xtype : "numberfield",
									name : 'setPaymentNum1',
									fieldLabel : false,
									value : '0',
									width : clientWidth * 0.1,
									labelAlign : 'right',
									minValue : 0,
									listeners : {
										'change' : function(field, newvalue,oldvalue) {
											this.up('form').fillPaymentNum(field);
										}
									}
								},{
									xtype : "displayfield",
									name : 'setPaymentDesc1',
									width : clientWidth * 0.6,
									value : ''
								}]
					}],
			initPage : function(data) {
				var returnPaySetModule = Ext.getCmp('returnPaySetModule');
				var returnRefunds = data.returnRefunds;
				var returnAccount = data.returnAccount;
				var returnGoods = data.returnGoods;
				var orderPays = data.orderPays;
				var returnCommon = data.returnCommon;
				if (returnAccount.returnTotalFee) {
					returnAccount.returnTotalFee = parseFloat(returnAccount.returnTotalFee).toFixed(2);
					// 给本页面的退款总金额赋值
					this.getForm().findField("returnTotalFee").setValue(returnAccount.returnTotalFee);
					// 给退单付款信息的退款总金额赋值
					returnPaySetModule.getForm().findField("returnTotalFee").setValue(returnAccount.returnTotalFee);
				}
				if (returnCommon.returnSn) {// 判断是申请退单还是退单详情
					if (returnRefunds) {
						var len = returnRefunds.length;
						if (len <= 1) {
							this.getForm().findField('setPaymentNum1').setReadOnly(true);
						}
						for (var i = 0; i < len; i++) {
							if (i > 0) {
								if (!this.getForm().findField('setPayment' + (i + 1))) {
									this.down('fieldcontainer').add({
										xtype : "displayfield",
										width : clientWidth * 0.12
									},{
										xtype : "displayfield",
										width : clientWidth * 0.03,
										value : '+'
									}, {
										xtype : 'commonsystemPayment',
										id : 'setPayment' + (i + 1) + '',
										name : 'setPayment' + (i + 1) + '',
										fieldLabel : false,
										width : clientWidth * 0.1,
										listeners : {
											'change' : function(field) {
												this.up('form').fillPayment(field);
											}
										}
									}, {
										xtype : "numberfield",
										name : 'setPaymentNum'
												+ (i + 1) + '',
										fieldLabel : false,
										width : clientWidth * 0.1,
										value : "0",
										minValue : 0,
										listeners : {
											'change' : function(field) {
												this.up('form').fillPaymentNum(field);
											}
										}
									},{
										xtype : "displayfield",
										name : 'setPaymentDesc'+ (i + 1) + '',
										width : clientWidth * 0.6,
										value : ''
									});
									this.doLayout();
								}
							}
							this.down('#setPayment' + (i + 1) + '').setValue(returnRefunds[i].returnPay);
							this.getForm().findField("setPaymentNum" + (i + 1)).setValue(returnRefunds[i].returnFee);
							var describe = '支付流水号：'+returnRefunds[i].returnPaySn;
//								+'&nbsp;&nbsp;&nbsp;结算状态：'+me.getReturnPayStatusName(returnRefunds[i].returnPayStatus);
							this.getForm().findField("setPaymentDesc" + (i + 1)).setValue(describe);
						}
						// 与退单付款信息页面数据联动
						for (var i = 0; i < len; i++) {
							if (i == 0) {
								if (!Ext.getCmp('paySet1')) {
									returnPaySetModule.down('#paySetFieldContainer').add(
											{
												xtype : "displayfield",
												id : 'paySet' + (i + 1) + '',
												fieldLabel : "=" + this.down('#setPayment' + (i + 1) + '').getRawValue(),
												value : returnRefunds[i].returnFee,
												columnWidth : .15,
												labelWidth : 180
											});
								}
							} else if (i > 0) {
								if (!Ext.getCmp('paySet' + (i + 1))) {
									returnPaySetModule.down('#paySetFieldContainer').add(
											{
												xtype : "displayfield",
												id : 'paySet' + (i + 1) + '',
												fieldLabel : "+" + this.down('#setPayment' + (i + 1) + '').getRawValue(),
												value : returnRefunds[i].returnFee,
												columnWidth : .15,
												labelWidth : 180
											});
									returnPaySetModule.doLayout();
								}
							}
						}
						if (returnAccount.returnTotalFee) {
							this.getForm().findField("returnTotalFee").setValue(returnAccount.returnTotalFee);
						}
					}
				} else {// 申请退单
					if (orderPays) {
						var returnTotalFee = 0;
						var surplusPay = {};
						var surplusFlag = false;
						// 红包
						var returnBonusMoney = 0;

						var len = orderPays.length;
						if (len <= 1) {
							this.getForm().findField('setPaymentNum1').setReadOnly(true);
						}

						if (returnCommon.returnType == 1 || returnCommon.returnType == 2 || returnCommon.returnType == 5) {// 申请退货单
							for (var j = 0; j < returnGoods.length; j++) {// 商品付款总金额
								returnTotalFee += returnGoods[j].goodsPrice * returnGoods[j].canReturnCount-returnGoods[j].integralMoney;
								if (returnGoods[j].shareBonus)
									returnBonusMoney += returnGoods[j].shareBonus * (returnGoods[j].goodsBuyNumber - returnGoods[j].shopReturnCount - returnGoods[j].havedReturnCount);
							}
						} else if (returnCommon.returnType == 3 || returnCommon.returnType == 4) {// 申请退款单
							for (var j = 0; j < returnGoods.length; j++) {// 商品付款总金额
								returnTotalFee += returnGoods[j].priceDifferNum * returnGoods[j].priceDifference;
							}
						}
						returnTotalFee = returnTotalFee - returnBonusMoney;
						// 给本页面的退款总金额赋值
						this.getForm().findField("returnTotalFee").setValue(returnTotalFee);

						// (应财务要求，默认配送费用去掉，所以这里需要减去配送费用)
						var returnShipping = 0;
						if (returnAccount.returnShipping) {
							returnShipping = returnAccount.returnShipping;
						}

						for (var i = 0; i < len; i++) {
							if (i > 0) {
								if (!Ext.getCmp('paySet' + (i + 1))) {
									this.down('fieldcontainer').add({
										xtype : "displayfield",
										width : clientWidth * 0.12
									},{
										xtype : "displayfield",
										width : clientWidth * 0.03,
										value : '+'
									}, {
										xtype : 'commonsystemPayment',
										id : 'setPayment' + (i + 1) + '',
										name : 'setPayment' + (i + 1) + '',
										fieldLabel : false,
										width : clientWidth * 0.1,
										listeners : {
											'change' : function(field) {
												this.up('form').fillPayment(field);
											}
										}
									}, {
										xtype : "numberfield",
										name : 'setPaymentNum' + (i + 1) + '',
										fieldLabel : false,
										width : clientWidth * 0.1,
										value : "0",
										minValue : 0,
										listeners : {
											'change' : function(field) {
												this.up('form').fillPaymentNum(field);
											}
										}
									},{
										xtype : "displayfield",
										name : 'setPaymentDesc'+ (i + 1) + '',
										width : clientWidth * 0.6,
										value : ''
									});
									this.doLayout();
								}
							}
							this.down('#setPayment' + (i + 1) + '').setValue(orderPays[i].pId);
							var setPayValue = parseFloat(orderPays[i].payFee) - parseFloat(returnShipping);
							if (setPayValue > 0) {
								this.getForm().findField("setPaymentNum" + (i + 1)).setValue(setPayValue);
								returnShipping = 0;
							} else {
								this.getForm().findField("setPaymentNum" + (i + 1)).setValue(0);
								returnShipping = returnShipping - parseFloat(orderPays[i].payFee);
							}

						}

						if (returnTotalFee && returnTotalFee != 0) {
							returnTotalFee = returnTotalFee.toFixed(2);
						}

						// 关联退单付款信息
						returnPaySetModule.initPayment(orderPays, returnTotalFee,returnBonusMoney, returnAccount);

					}
				}

			},
			fillPayment : function(field) {// 支付方式与退单付款信息页面支付方式联动
				var i = parseInt(field.name.split('setPayment')[1]);
				var rawData = null;
				if (Ext.getCmp('returnShow')) {
					rawData = Ext.getCmp('returnShow').getForm().reader.rawData;
				} else if (Ext.getCmp('returnForm')) {
					rawData = Ext.getCmp('returnForm').getForm().reader.rawData;
				}
				/**
				 * 支付方式校验
				 */
				var thisValue = parseFloat(field.getValue());
				/*if (!returnSn) {// 申请退单
					var orderPays = rawData.orderPays;
					var len = orderPays.length;
					for (var j = 1; j <= len; j++) {
						if (j != i) {
							if (Ext.getCmp('setPayment' + j)) {
								var otherValue = parseFloat(Ext.getCmp('setPayment' + j).getValue());
								if (thisValue == otherValue) {// 支付方式相同
									Ext.Msg.alert('结果', "该支付方式已存在！", function(xx) {
												field.setValue("请选择支付方式");
											});
									return false;
								}

							}

						}
					}
				}*/
				/**
				 * 业务逻辑：退单详情展示界面不可编辑，所以不需要支付方式校验
				 */
				if (Ext.getCmp('returnPaySetModule').getForm().findField('paySet' + i)) {
					if (field.getRawValue() != "请选择支付方式") {
						if (i == 1) {
							Ext.DomQuery.selectNode('label[id=paySet' + i + '-labelEl]').innerHTML = "=" + field.getRawValue() + ":";
						} else {
							Ext.DomQuery.selectNode('label[id=paySet' + i + '-labelEl]').innerHTML = "+" + field.getRawValue() + ":";
						}
					}
				}
			},

			fillPaymentNum : function(field) {
				/**
				 * 1.与兄弟节点（其他支付方式）的钱联动：所有支付方式加起来的钱为总金额
				 * 2.本支付方式变化的钱会在相邻的下一个支付方式变动，加入为最后一个支付方式，则其变化的数据就在第一个上变化
				 * 3.支付方式数据与退单付款信息页面支付方式数据联动
				 */
				// 原始退款总金额
				var returnTotalFee = this.up('form').getForm().findField("returnTotalFee").getValue();
				var changeTotalFee = 0;
				var paySetNum = (this.down('fieldcontainer').items.length ) / 5;// 获取有多少个支付方式
				for (var i = 1; i <= paySetNum; i++) {
					changeTotalFee += this.up('form').getForm().findField('setPaymentNum' + i).getValue();
				}
				var k = parseInt(field.name.split('setPaymentNum')[1]);
				if (field.name == ('setPaymentNum' + k)) {
					if (Ext.getCmp('returnPaySetModule').getForm().findField('paySet' + k)) {
						Ext.getCmp('returnPaySetModule').getForm().findField('paySet' + k).setValue(parseFloat(field.getValue()).toFixed(2));
					}
				}
				var changeNum = 0;

				changeNum = returnTotalFee - changeTotalFee;
				if (returnTotalFee == changeTotalFee) {
					changePaymentNumFlag = true;
					return false;
				}

				if (changePaymentNumFlag) {
					if (k == paySetNum && k != 1) {// 说明为最后一个支付方式
						changePaymentNumFlag = false;
						var lastNum = this.up('form').getForm().findField("setPaymentNum1").getValue();
						this.up('form').getForm().findField("setPaymentNum1").setValue(lastNum + changeNum);

					} else {
						if (this.up('form').getForm().findField("setPaymentNum"
								+ (k + 1))) {
							var lastNum = this.up('form').getForm().findField("setPaymentNum" + (k + 1)).getValue();
							if ((lastNum + changeNum) <= 0) {// 当下一个支付方式的值小与需要变化的值时
								this.up('form').getForm().findField("setPaymentNum" + (k + 1)).setValue(0);

							} else {
								changePaymentNumFlag = false;
								this.up('form').getForm().findField("setPaymentNum" + (k + 1)).setValue(lastNum + changeNum);
							}
							return false;
						}
					}

				}

			}
		},
		{
			xtype : "form",
			id : "returnPaySetModule",
			alias : 'widget.returnPaySetModule',
			width : '100%',
			frame : true,
			title : '退单账目',
			head : true,
			bodyPadding : 10,
			layout : 'column',
			buttonAlign : 'center',// 按钮居中
			fieldDefaults : {
				labelAlign : 'right'
			},
			items : [{
						xtype : 'fieldcontainer',
						labelStyle : 'font-weight:bold;padding:0;',
						layout : 'column',
						columnWidth : 1,
						items : [{
									xtype : "displayfield",
									name : 'returnGoodsMoney',
									fieldLabel : "退商品金额（已减折让）",
									labelWidth : 180,
									value : 0.00,
									columnWidth : .25
								}
								,
								{
									xtype : "numberfield",
									name : 'returnShipping',
									fieldLabel : "+ 退配送费用",
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
								}
								/*,
								{
									xtype : "numberfield",
									name : 'returnOtherMoney',
									fieldLabel : "+ 退其他费用",
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
								}*/
								,
								{
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
								}
								,
								{
									xtype : "combobox",
									store : Ext.create("Ext.data.Store", {
										model : "MB.model.CommonStatusModel",
										data : [['0', 0]]
									}),
									displayField : 'name',
									valueField : 'id',
									queryMode : 'local',
									hiddenName : 'id',
									emptyText : '请选择',
									editable : true,
									name : 'returnBonusMoney',
									fieldLabel : "- 红包金额",
									columnWidth : .17,
									labelWidth : 100,
									listeners : {
										'change' : function(field) {
											this.up('form').payTotalSet();
										}
									}
								}]
					}, {
						xtype : 'fieldcontainer',
						labelStyle : 'font-weight:bold;padding:0;',
						id : 'paySetFieldContainer',
						layout : 'column',
						columnWidth : 1,
						items : []
					}, {
						xtype : 'fieldcontainer',
						labelStyle : 'font-weight:bold;padding:0;',
						layout : 'column',
						columnWidth : 1,
						items : [{
							xtype : "displayfield",
							name : 'returnTotalFee',
							fieldLabel : "= 退款总金额",
							value : 0.00,
							labelWidth : 180,
							columnWidth : .25,
							listeners : {
								'change' : function(field) {
									if (Ext.getCmp('moneyPaid')) {
										Ext.getCmp('moneyPaid').setValue(field.getValue());
									}
								}
							}
						},{
							xtype : "displayfield",
							columnWidth : .75
						}]
					}],
			tools : [{
				type: 'gear',
				id:'returnPaySetModuleG',
				tooltip : '修改',
				action: 'returnPayEdit',
				hidden:true,
				scope: me
			}],
			paySet : function(returnGoods) {
				var len = returnGoods.length;
				var returnGoodsMoney = 0, totalPriceDifference = 0, returnBonusMoney = 0, shareSettle = 0,totalIntegralMoney = 0;
				for (var i = 0; i < len; i++) {
					returnGoodsMoney += returnGoods[i].goodsPrice
							* (returnGoods[i].canReturnCount);
					totalPriceDifference += returnGoods[i].priceDifferNum
							* returnGoods[i].priceDifference;
					totalIntegralMoney += returnGoods[i].integralMoney*(returnGoods[i].canReturnCount);
					shareSettle += returnGoods[i].shareSettle
							* returnGoods[i].canReturnCount;
					if (returnGoods[i].shareBonus)
						returnBonusMoney += returnGoods[i].shareBonus
								* (returnGoods[i].goodsBuyNumber
										- returnGoods[i].shopReturnCount - returnGoods[i].havedReturnCount);
				}
				if (returnType == 1 || returnType == 2 || returnType == 5) {// 退货单
					this.getForm().findField('returnGoodsMoney').setValue(returnGoodsMoney.toFixed(2));
				} else if (returnType == 3) {// 普通退款单shareSettle
					this.getForm().findField('returnGoodsMoney').setValue(shareSettle.toFixed(2));
				} else if (returnType == 4) {// 退款单shareSettle
//					this.getForm().findField('totalPriceDifference').setValue(totalPriceDifference.toFixed(2));
				}
				this.getForm().findField('totalIntegralMoney').setValue(totalIntegralMoney.toFixed(2));

				if (returnSn == '' && returnType == '1') {
					if (returnBonusMoney != 0) {
						var storeData = [];
						this.getForm().findField('returnBonusMoney').getStore().each(function(record, index) {
							storeData.push([record.get('id'), record.get('name')]);
						});
						var oldValue = this.getForm().findField('returnBonusMoney').getRawValue();
						if (oldValue != returnBonusMoney) {
							storeData.push(['3', returnBonusMoney.toFixed(2)]);
							this.getForm().findField('returnBonusMoney').getStore().removeAll();
							this.getForm().findField('returnBonusMoney').getStore().loadData(storeData);
							this.getForm().findField('returnBonusMoney').setValue(3);
						}
					}else {
						this.getForm().findField('returnBonusMoney').setValue(0);
					}
				}
				this.payTotalSet();
			},
			payTotalSet : function() {// 动态给退款总金额赋值
				var returnTotalFee = this.formatNum("returnTotalFee");
				var returnGoodsMoney = this.formatNum("returnGoodsMoney");
				var totalIntegralMoney = this.formatNum("totalIntegralMoney");
//				var totalPriceDifference = this.formatNum("totalPriceDifference");
				var returnShipping = this.formatNum("returnShipping");
//				var returnOtherMoney = this.formatNum("returnOtherMoney");
				var returnOtherMoney = 0;
				var returnBonusMoney = this.getForm().findField("returnBonusMoney").getRawValue();
				if (returnBonusMoney) {
					returnBonusMoney = parseFloat(returnBonusMoney);
				} else {
					returnBonusMoney = 0;
				}
				returnTotalFee = (returnGoodsMoney
						+ returnShipping + returnOtherMoney - returnBonusMoney-totalIntegralMoney).toFixed(2);
				// 给本页面的退款总金额赋值
				this.getForm().findField("returnTotalFee").setValue(returnTotalFee);
				if (Ext.getCmp('returnPayInfomation')) {
					// 给付款信息页面退款总金额赋值
					Ext.getCmp('returnPayInfomation').getForm().findField("returnTotalFee").setValue(returnTotalFee);

					var length = Ext.getCmp('returnPayInfomation').query('commonsystemPayment').length;
					var totalAmt = 0;
					var payMoneyArr = [];// 支付方式金额调整
					if (length > 0) {
						for (var i = 0; i < length; i++) {
							totalAmt += parseFloat(Ext.getCmp('returnPayInfomation').getForm().findField("setPaymentNum" + (i + 1)).value);
							var moneyNum = Ext.getCmp('returnPayInfomation').getForm().findField("setPaymentNum" + (i + 1)).getValue();
							payMoneyArr.push(moneyNum);
						}
					}
					var diffMoney = returnTotalFee - totalAmt;
					var payMoneyTotal = 0;
					var flagNum = 0;
					for (var m = 0; m < length; m++) {
						payMoneyTotal += payMoneyArr[m];
						if ((payMoneyTotal + diffMoney) >= 0) {
							flagNum = m;
							break;
						}
					}
					for (var k = 0; k <= flagNum; k++) {
						if (k < flagNum) {
							Ext.getCmp('returnPayInfomation').getForm().findField("setPaymentNum" + (k + 1)).setValue(0);
						} else {
							Ext.getCmp('returnPayInfomation').getForm().findField("setPaymentNum" + (k + 1)).setValue(payMoneyTotal + diffMoney);
						}
					}

				}

			},
			formatNum : function(name) {
				var num = this.getForm().findField(name).getValue();
				if (num && parseFloat(num)) {
					return parseFloat(num);
				} else {
					return 0;
				}
			},
			initPayment : function(orderPays, returnTotalFee, returnBonusMoney,
					returnAccount) {// 申请退单过来联动数据

				// (应财务要求，默认配送费用去掉，所以这里需要减去配送费用)
				var returnShipping = 0;
				if (returnAccount.returnShipping) {
					returnShipping = returnAccount.returnShipping;
				}

				var len = orderPays.length;
				for (var i = 0; i < len; i++) {
					if (!Ext.getCmp('paySet' + (i + 1))) {
						if (i == 0) {
							this.down('#paySetFieldContainer').add({
								xtype : "displayfield",
								id : 'paySet' + (i + 1),
								columnWidth : .15,
								fieldLabel : "=" + Ext.getCmp('returnPayInfomation').down('#setPayment' + (i + 1) + '').getRawValue(),
								value : 0,
								labelWidth : 180
							});
				} else if (i > 0) {
					this.down('#paySetFieldContainer').add({
								xtype : "displayfield",
								id : 'paySet' + (i + 1),
								columnWidth : .15,
								fieldLabel : "+" + Ext.getCmp('returnPayInfomation').down('#setPayment' + (i + 1) + '').getRawValue(),
								value : 0,
								labelWidth : 180
							});
							this.doLayout();
						}
						var setPayValue = parseFloat(orderPays[i].payFee)
								- parseFloat(returnShipping);
						if (setPayValue > 0) {
							this.getForm().findField("paySet" + (i + 1))
									.setValue(setPayValue.toFixed(2));
							returnShipping = 0;
						} else {
							this.getForm().findField("paySet" + (i + 1))
									.setValue(0);
							returnShipping = returnShipping
									- parseFloat(orderPays[i].payTotalfee);
						}
					}
				}
				// 给本页面的退款总金额赋值
				this.getForm().findField("returnTotalFee").setValue(returnTotalFee);

				this.getForm().findField('returnBonusMoney').setValue('0');
				var storeData = [['0', 0]];
				// 红包
				if (returnAccount.returnBonusMoney
						&& parseFloat(returnAccount.returnBonusMoney) != 0) {// 原订单的红包总额
					storeData.push(['1', returnAccount.returnBonusMoney]);
					if (returnBonusMoney != 0
							&& returnBonusMoney != returnAccount.returnBonusMoney) {
						storeData.push(['2', returnBonusMoney]);
						this.getForm().findField('returnBonusMoney').getStore().removeAll();
						this.getForm().findField('returnBonusMoney').getStore().loadData(storeData);
						this.getForm().findField('returnBonusMoney').setValue(2);
					} else {
						this.getForm().findField('returnBonusMoney').getStore().removeAll();
						this.getForm().findField('returnBonusMoney').getStore().loadData(storeData);
						if (returnSn == '' && returnType == '4') {
							this.getForm().findField('returnBonusMoney').setValue(0);
						} else {
							this.getForm().findField('returnBonusMoney').setValue(1);
						}
					}
				}
			this.payTotalSet();
			}
		}, 
		{
			xtype : "form",
			id : 'returnPictureSetModule',
			width : '100%',
			frame : true,
			title : '图片信息&nbsp;&nbsp;&nbsp;',
			head : true,
			layout : 'fit',
			bodyPadding : 10,
			buttonAlign : 'center',// 按钮居中
			collapsible : true,
			collapsed : true,
			items : [{
						xtype : 'fieldcontainer',
						columnWidth : 1,
						layout : 'column',
						width : '100%',
						items : [{
									xtype : 'filefield',
									emptyText : '请选择一张图片',
									fieldLabel : '图片上传',
									name : 'returnImage',
									buttonText : '浏览图片',
									labelWidth : 150,
									width : 500,
									columnWidth : 0.4,
									buttonCfg : {
										iconCls : 'button-browser'
									}
								}, {
									xtype : 'button',
									text : '上传',
									style : 'margin-left:5px;',
									width : 100,
									handler : me.uploadImage
								}, {
									xtype : 'button',
									text : '重置',
									style : 'margin-left:5px;',
									width : 100,
									handler : me.resetImage
								}]
					}, {
						xtype : 'panel',
						style : 'margin-top:10px;padding:10px;',
						name : 'imageList',
						html : ""
					}]

		}];
		// //以json形式读取数据
		this.reader = Ext.create('Ext.data.reader.Json', {
					rootProperty : "returnCommon",
					model : 'MB.model.ReturnCommonModel'
				});
		this.callParent(arguments);
	},
	initData : function(orderForm) {
		var me = this;
		// 退单原因
		me.getForm().findField('returnReason').getStore().on(
				'beforeload', function(store) {
					params = {
						"type" : 1
					};
					Ext.apply(store.proxy.extraParams, params);
				});
		me.getForm().findField('returnReason').getStore().load();
		me.getForm().findField('depotCode').getStore().on(
				'beforeload', function(store) {
					params = {
						"siteCode" : siteCode
					};
					Ext.apply(store.proxy.extraParams, params);
				});
		me.getForm().findField('depotCode').getStore().load();
		
		if(returnType == '5'){
				me.getForm().findField('depotCode').getStore().on(
						'beforeload', function(store) {
							params = {
								"depotType" : 0
							};
							Ext.apply(store.proxy.extraParams, params);
						});
				me.getForm().findField('depotCode').getStore().load();
				
				me.getForm().findField('returnExpress').getStore().on(
						'beforeload', function(store) {
							params = {
								"returnSn" : returnSn
							};
							Ext.apply(store.proxy.extraParams, params);
						});
				me.getForm().findField('returnExpress').getStore().load();
			}
		me.load({
			url : basePath + '/custom/orderReturn/getOrderReturnDetail',
			params : {
				"relOrderSn" : relatingOrderSn,
				"returnSn" : returnSn,
				"returnType" : returnType
			},
			timeout : 90000,
			success : function(opForm, action) {
				//获取组件
				var returnShipModule = Ext.getCmp('returnShipModule'),//收货人信息
				returnGoodsList = Ext.getCmp('returnGoodsList'),//商品列表
				returnPaySetModule = Ext.getCmp('returnPaySetModule'), //退单账目
				orderPayList = Ext.getCmp('orderPayList'), //原订单支付信息
				returnPayInfomation = Ext.getCmp('returnPayInfomation');//退单付款信息
				
				//获取数据
				var orderReturnDetailData = opForm.reader.rawData;
				var returnCommon = orderReturnDetailData.returnCommon;
				var returnGoods = orderReturnDetailData.returnGoods;
				var storageTimesList = orderReturnDetailData.storageTimesList;
			
				//按钮状态赋值
				me.refreshButtons(orderReturnDetailData);
				
//				returnGoodsList.hideColumn(returnCommon,returnGoodsList);
				returnGoodsList.store.loadData(returnGoods);
				 console.dir(returnGoods);
				returnShipModule.getForm().setValues(orderReturnDetailData.addressInfo);
				orderPayList.store.loadData(orderReturnDetailData.orderPayList);

				me.getForm().findField('returnShipping').setValue(0);
//				me.getForm().findField('returnOtherMoney').setValue(0);
				me.getForm().findField('returnBonusMoney').setValue(0);
				
				if (returnSn == '') {
					me.getForm().findField("returnSettlementType").setValue(1);
					if (returnType == '1') {
						me.getForm().findField("processType").setValue(1);
					} else {
						me.getForm().findField("processType").setValue(0);
					}
					if (returnType == '2') {
						me.getForm().findField("haveRefund").setValue(0);
					} else {
						me.getForm().findField("haveRefund").setValue(1);
					}
					if (siteCode != 'Chlitina') {
						me.getForm().findField("returnReason").setValue("R11");
					}
				}
				me.expandImageList();
				returnGoodsList.getSelectionModel().selectAll();
				if (returnSn) {
					var returnAccount = orderReturnDetailData.returnAccount;
					var returnGoodsMoney = returnAccount.returnGoodsMoney;
					if (returnGoodsMoney)
						me.getForm().findField('returnGoodsMoney').setValue(returnGoodsMoney);
					var returnShipping = returnAccount.returnShipping;
//					var returnOtherMoney = returnAccount.returnOtherMoney;
					var returnBonusMoney = returnAccount.returnBonusMoney;
					var totalIntegralMoney = returnAccount.totalIntegralMoney;
					
					if (returnShipping)
						me.getForm().findField('returnShipping').setValue(returnShipping);
//					if (returnOtherMoney)
//						me.getForm().findField('returnOtherMoney').setValue(returnOtherMoney);
					if (returnBonusMoney)me.getForm().findField('returnBonusMoney').setValue(returnBonusMoney);
					if (totalIntegralMoney)me.getForm().findField('totalIntegralMoney').setValue(totalIntegralMoney);

				}
				returnPaySetModule.paySet(returnGoods);
				returnPayInfomation.initPage(orderReturnDetailData);
				me.disablePage();
				var shipStatus = returnCommon.shipsStatus;
				var payStatus = returnCommon.payStatus;
				if (shipStatus != 1) {
					if (auth[rolePrefix + 'updateHaveRefund']) {
						me.getForm().findField("haveRefund").readOnly = false;
					}
					if(payStatus == 1){
						Ext.getCmp('updateHaveRefund').disable();
					}
					if (auth[rolePrefix + 'updateInvoiceNo']) {
						me.getForm().findField("returnInvoiceNo").setReadOnly(false);
					}
					if (auth[rolePrefix + 'updateDepotCode']) {
						me.getForm().findField("depotCode").readOnly = false;
					}
					if (auth[rolePrefix + 'updateExpress']) {
						me.getForm().findField("returnExpress").readOnly = false;
					}

				} else {
					me.getForm().findField("haveRefund").readOnly = false;
					if(payStatus == 1){
						Ext.getCmp('updateHaveRefund').disable();
					}
					//更新按钮权限
					Ext.getCmp('updateExpress').disable();
					Ext.getCmp('updateInvoiceNo').disable();
					Ext.getCmp('updateDepotCode').disable();

				}

			},
			failure : function(opForm, action) {
				// 数据加载失败后操作
				Ext.msgBox.remainMsg("页面加载",
						opForm.reader.rawData.errorMessage,
						Ext.MessageBox.ERROR);
			}
		});

	},
	//刷新按钮权限
	refreshButtons : function(rawData) {
		var buttonStatus = rawData.buttonStatus;
		var common = rawData.returnCommon;
		var lockStatus = common.lockStatus;
		console.dir('lockStatus:' + buttonStatus);
		var backToCs = common.backToCs;

		//加密按钮权限
		if (Ext.getCmp('returnDecodeMobile')) {
			Ext.getCmp('returnDecodeMobile').disable();
			if (auth[rolePrefix + 'decodeMobile']) {
				Ext.getCmp('returnDecodeMobile').enable();
			}

		}
		//更新按钮权限
		if (Ext.getCmp('updateHaveRefund')) {
			Ext.getCmp('updateHaveRefund').disable();
			if (auth[rolePrefix + 'updateHaveRefund']) {
				Ext.getCmp('updateHaveRefund').enable();
			}

		}
		if (Ext.getCmp('updateExpress')) {
			Ext.getCmp('updateExpress').disable();
			if (auth[rolePrefix + 'updateExpress']) {
				Ext.getCmp('updateExpress').enable();
			}

		}
		if (Ext.getCmp('updateInvoiceNo')) {
			Ext.getCmp('updateInvoiceNo').disable();
			if (auth[rolePrefix + 'updateInvoiceNo']) {
				Ext.getCmp('updateInvoiceNo').enable();
			}

		}
		if (Ext.getCmp('updateDepotCode')) {
			Ext.getCmp('updateDepotCode').disable();
			if (auth[rolePrefix + 'updateDepotCode']) {
				Ext.getCmp('updateDepotCode').enable();
			}

		}
		
		//退单金额编辑按钮权限控制
		if(auth[rolePrefix+'accountEdit']){
			Ext.getCmp('returnPaySetModuleG').enable();
		}else {
			Ext.getCmp('returnPaySetModuleG').disable();
		}
		var editReturn = buttonStatus.editReturn;
		console.dir(buttonStatus);
		if (editReturn == 0) {
			Ext.getCmp('updateHaveRefund').disable();
			Ext.getCmp('updateInvoiceNo').disable();
			Ext.getCmp('updateExpress').disable();
			Ext.getCmp('updateDepotCode').disable();
			Ext.getCmp('returnSetModuleG').disable();
		}
		
		//是否已确认
		var confirmStatus = Ext.ComponentQuery.query('displayfield[name=returnStatusDisplay]')[0].value.indexOf("未确认");
		var checkInStatus = Ext.ComponentQuery.query('displayfield[name=returnStatusDisplay]')[0].value.indexOf("已入库");
		var settleStatus = Ext.ComponentQuery.query('displayfield[name=returnStatusDisplay]')[0].value.indexOf("已退款");
		Ext.each(Ext.getCmp("returnNorth").down('toolbar').items.items,
				function(item) {
					if (item.action != undefined) {
						//操作权限
						var role = auth[rolePrefix + item.action];
						if (buttonStatus == undefined) {
							item.disable();
						} else {
							var status = buttonStatus[item.action];
							if (status != undefined && status == '1' && role) {
								item.enable();
							} else {
								item.disable();
							}
						}
						if (item.action == 'backToCsReturn') {
							if (backToCs == '0') {
								item.setText('<font style="color:white">回退客服</font>');
								item.setTooltip('回退客服');
							} else {
								item.setText('<font style="color:white">取消回退</font>');
								item.setTooltip('取消回退');
							}
						}
						if (item.action == 'lockReturn' || item.action == 'unLockReturn') {
							console.log('returnSn:' + !returnSn);
							console.log('settleStatus:' + settleStatus);
							console.log('checkInStatus:' + checkInStatus);
							console.log('confirmStatus:' + confirmStatus);
							if (lockStatus == '0') {
								item.setText('<font style="color:white">锁&nbsp;&nbsp;定</font>');
								item.setTooltip('锁&nbsp;&nbsp;定');
								item.action = 'lockReturn';
							} else {
								item.setText('<font style="color:white">解&nbsp;&nbsp;锁</font>');
								item.setTooltip('解&nbsp;&nbsp;锁');
								item.action = 'unLockReturn';
								if (returnSn) {
									if (confirmStatus < 0) {
										Ext.getCmp('returnSetModuleG').setHidden(false);
									}
									if (editReturn == 0) {//已入库隐藏
										Ext.getCmp('returnGoodsSetModuleG').setHidden(true);
									}   else {
										Ext.getCmp('returnGoodsSetModuleG').setHidden(false);
									}
									if (editReturn == 0) {//已退款隐藏
										Ext.getCmp('returnPaySetModuleG').setHidden(true);
									} else {
										Ext.getCmp('returnPaySetModuleG').setHidden(false);
									}
								}
							}
						}
						if(item.action == 'messageEdit'){
							item.disable();
							if(status != undefined && status == '1' 
								&& (returnType == '1' || returnType == '2')
								&& returnSn != '' && role){
								item.enable();
							}
						}
						
						if(returnType == '5'){
							if(item.action == 'unPassReturn' || item.action == 'storageReturn'){
								item.disable();
							}
						}else{
							if(item.action == 'virtualStorageReturn' ){
								item.disable();
							}
						}
					}
				});
	},
	//图片上传
	uploadImage : function(btn) {
		if (returnSn == '') {
			Ext.msgBox.remainMsg('上传图片', "请将退单保存后上传图片操作", Ext.MessageBox.ERROR);
			return;
		}
		var imagePanel = Ext.getCmp('returnPictureSetModule');
		var returnImage = imagePanel.getForm().findField('returnImage')
				.getValue();
		if (returnImage == '') {
			Ext.msgBox.remainMsg("警告", "请选择退货图片信息上传", Ext.MessageBox.INFO);
			return;
		}
		imagePanel.getForm().submit({
			url : basePath + '/custom/orderReturn/uploadImageButtonClick',
			params : {
				"returnSn" : returnSn
			},
			waitMsg : 'Uploading your photo...',
			success : function(fp, action) {
				if (action.result.success) {
					Ext.msgBox.msg('上传图片', "上传成功！", Ext.MessageBox.INFO);
					Ext.getCmp("returnShow").loadImageList();
				} else {
					Ext.msgBox.remainMsg('上传图片', action.result.errorMessage,
							Ext.MessageBox.ERROR);
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg('上传图片', action.result.errorMessage,
						Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		});
	},
	//重置上传图片
	resetImage : function(btn) {
		Ext.getCmp('returnPictureSetModule').getForm().findField('returnImage').reset();
	},
	//展开Panel 加载图片数据
	expandImageList : function() {
		//展开原订单信息，后台发送请求
		var imageObj = Ext.getCmp('returnPictureSetModule');
		var me = this;
		imageObj.collapse();
		imageObj.addListener('expand', function() {
					me.loadImageList();
				});
	},
	//从后台获取图片数据
	loadImageList : function() {
		//展开原订单信息，后台发送请求
		var imageObj = Ext.getCmp('returnPictureSetModule');
		var me = this;
		var json = {
			url : basePath + '/custom/orderReturn/getOrderReturnImageList?returnSn='+returnSn,
			waitMsg : 'loading image...',
			success : function(formPanel, action) {
				var length = action.result.returnImageList.length;
				var serverUrl = action.result.serverUrl;
				if (length > 0 && serverUrl != '') {
					var imageList = "";
					for (var i = 0; i < length; i++) {
						var url = serverUrl + action.result.returnImageList[i].filepath;
						var delPicUrl = basePath+'/ext5.1/shared/icons/fam/delete.gif';
						imageList += "<img src='" + url + "' onclick ='showBigPic(this)' class='returnImage' />"
								+ "<input type='image' src='"+delPicUrl +"' value='"+url+"|"+returnSn+"' onclick ='delReturnImg(this.value);return false;' class='delIcon' />";							
							
						if ((i + 1) % 6 == 0) {
							imageList += "<br />";
						}
					}
					imageObj.down('panel').update(imageList);
				}else{
					imageObj.down('panel').update('');
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg('加载图片异常', action.result.errorMessage,
						Ext.MessageBox.ERROR);
			}
		};
		imageObj.getForm().submit(json);
	},
	updateHaveRefund : function(btn) {
		var form = btn.up('form');
		this.updateReturnInfo('updateHaveRefund', form);
	},
	updateExpress : function(btn) {
		var form = btn.up('form');
		this.updateReturnInfo('updateExpress', form);
	},
	updateInvoiceNo : function(btn) {
		var form = btn.up('form');
		this.updateReturnInfo('updateInvoiceNo', form);
	},
	updateDepotCode : function(btn) {
		var form = btn.up('form');
		this.updateReturnInfo('updateDepotCode', form);
	},
	updateReturnInfo : function(type, form) {
		var me = this;
		var param = form.builtParam(type, form);
		param.btnType = 'editReturn';
		Ext.Ajax.request({
					url : basePath + '/custom/orderReturn/editReturnButtonClick',
					params : param,
					timeout : 90000,
					data : "json",
					success : function(response) {
						var text = response.responseText;
						var result = Ext.JSON.decode(text);
						console.dir(result);
						if (result) {
							me.initData();
							Ext.msgBox.msg('基本信息', '数据更新成功', Ext.MessageBox.INFO);}
					},
					failure : function(response) {
						Ext.msgBox.remainMsg("结果", action.result.errorMessage,Ext.MessageBox.ERROR);}
				});
	},
	//非申请页面禁用组件
	disablePage : function() {
		//退单基本信息
		var returnCenter = Ext.getCmp('returnShow').getForm();
		//退单商品信息
		var goodGrid=Ext.getCmp('returnGoodsList');
		var columns=goodGrid.columnManager.columns;//editable
		//表头复选框禁用
		goodGrid.headerCt.on("headerclick", function(ct,column,e,t,opts) {
			return false;
		});
		if(!returnSn){
			Ext.getCmp('updateHaveRefund').disable();
			Ext.getCmp('updateExpress').disable();
			Ext.getCmp('updateInvoiceNo').disable();
			Ext.getCmp('updateDepotCode').disable();
			returnCenter.findField("depotCode").readOnly = true;
			return;
		}
		returnCenter.findField("processType").readOnly = true;
		returnCenter.findField("haveRefund").readOnly = true;
		returnCenter.findField("returnInvoiceNo").setReadOnly(true);
		returnCenter.findField("depotCode").readOnly = true;
		returnCenter.findField("returnReason").readOnly = true;
		returnCenter.findField("returnDesc").setReadOnly(true);
		returnCenter.findField("newOrderSn").setReadOnly(true);
		returnCenter.findField("returnSettlementType").readOnly = true;
		returnCenter.findField("returnExpress").readOnly = true;
		if(columns != null && columns != undefined){
			for(var i=0;i<columns.length;i++){
				columns[i].processEvent = function(type) { // 加入这一句，可以防止点中修改  
					if (type == 'click')  
						return false;  
				};  
			}
		}
		//付款信息
		var returnRefunds = returnCenter.reader.rawData.returnRefunds;
		var len=returnRefunds.length + 1;
		for(var i=1;i<len;i++){
			Ext.getCmp("returnPayInfomation").getForm().findField("setPayment"+i).readOnly = true;
			Ext.getCmp("returnPayInfomation").getForm().findField("setPaymentNum"+i).setReadOnly(true);
		}
		//配送信息
		returnCenter.findField("returnShipping").setReadOnly(true);
		//其他费用
//		returnCenter.findField("returnOtherMoney").setReadOnly(true);
		//红包金额
		returnCenter.findField("returnBonusMoney").setReadOnly(true);
		returnCenter.findField("totalIntegralMoney").setReadOnly(true);
	},
	decodeLinkMobile : function (btn) {
		var form = btn.up('form');
		var oldMobile = form.getForm().findField("mobile").getValue();
		var tel = form.getForm().findField("tel").getValue();
		Ext.Ajax.request({
			url:  basePath + 'custom/commonLog/decodeLinkMobile',
			params: {mobile : tel,mobile : oldMobile,returnSn :returnSn},
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				if (respText.code=='1') {
					form.getForm().findField("encodeMobile").setValue(respText.mobile);
					form.getForm().findField("encodeTel").setValue(respText.tel);
				} else {
					Ext.msgBox.remainMsg('解密', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var text = response.responseText;
				console.dir('failure:' + text);
			}
		});
	},
	builtOrderReturnParams:function(form){
		var params=form.getValues();
		var returnRefunds=form.getForm().reader.rawData.returnRefunds;
		var returnCommon=form.getForm().reader.rawData.returnCommon;
		var orderPays=form.getForm().reader.rawData.orderPays;
		params.returnType=returnCommon.returnType;
		params.relatingOrderSn=returnCommon.relatingOrderSn;
		var returnTotalFee=Ext.getCmp("returnPaySetModule").getForm().findField("returnTotalFee").getValue();
		//订单已付款金额
		var orderPayed = Ext.getCmp('returnShow').getForm().reader.rawData.returnCommon.orderPayedMoney;
		
		if(returnCommon.returnSn){
				if(returnRefunds != null){
				var len=returnRefunds.length;
				for(var i=0;i<len;i++){
					params['createOrderRefundList['+i+'].returnPaySn']=returnRefunds[i].returnPaySn;
					params['createOrderRefundList['+i+'].relatingReturnSn']=returnRefunds[i].relatingReturnSn;
					params['createOrderRefundList['+i+'].returnPay']=params["setPayment"+(i+1)];
					params['createOrderRefundList['+i+'].returnFee']=params["setPaymentNum"+(i+1)];
				}
			}
		}else if(orderPays!=null){
				//获取有几种支付方式
				var len=(Ext.getCmp('returnPayInfomation').down('fieldcontainer').items.length)/5;
				for(var i=0;i<len;i++){
					params['createOrderRefundList['+i+'].returnPay']=params["setPayment"+(i+1)];
					params['createOrderRefundList['+i+'].returnFee']=params["setPaymentNum"+(i+1)];
				}
		}
		if(returnTotalFee<0){
			Ext.Msg.alert('警告', "退款申请单金额不能为零！", function(xx) {});
			return false;
		}
		
		params['createOrderReturnShip.relatingReturnSn']=returnCommon.returnSn;
		params['createOrderReturnShip.returnExpress']=params.returnExpress;
		params['createOrderReturnShip.returnInvoiceNo']=params.returnInvoiceNo;
		params['createOrderReturnShip.depotCode']=params.depotCode;
		
		params['createOrderReturn.relatingOrderSn']=returnCommon.relatingOrderSn;
		params['createOrderReturn.newOrderSn']=params.newOrderSn;
		params['createOrderReturn.returnSettlementType']=params.returnSettlementType;
		params['createOrderReturn.returnSn']=returnCommon.returnSn;
		params['createOrderReturn.processType']=params.processType;
		params['createOrderReturn.returnTotalFee']=returnTotalFee;
		params['createOrderReturn.returnGoodsMoney']=Ext.getCmp("returnPaySetModule").getForm().findField("returnGoodsMoney").getValue();
//		params['createOrderReturn.totalPriceDifference']=Ext.getCmp("returnPaySetModule").getForm().findField("totalPriceDifference").getValue();
		params['createOrderReturn.returnType']=returnCommon.returnType;
		params['createOrderReturn.returnShipping']=Ext.getCmp("returnPaySetModule").getForm().findField("returnShipping").getValue();
//		params['createOrderReturn.returnOtherMoney']=params.returnOtherMoney;
		params['createOrderReturn.totalIntegralMoney']=params.totalIntegralMoney;//积分使用金额
		params['createOrderReturn.returnBonusMoney']=Ext.getCmp("returnPaySetModule").getForm().findField("returnBonusMoney").getRawValue();
		if(params.returnReason == ""){
			Ext.Msg.alert('警告', "请选择退单原因！", function(xx) {
			});
			return false;
		}
		params['createOrderReturn.returnReason']=params.returnReason;
		params['createOrderReturn.haveRefund']=params.haveRefund;
		params['createOrderReturn.returnDesc']=params.returnDesc;
		
		//商品数据
		var goodGrid=Ext.getCmp('returnGoodsList');
		var goodDataChecked = goodGrid.getSelectionModel().selected.items;
		if(returnType==2){
			goodDataChecked=this.getForm().reader.rawData.returnGoods;
		}
		var checkedLen=goodDataChecked.length;
		if(checkedLen<=0){
			Ext.Msg.alert('警告', "待保存的退单商品数据为空！", function(xx) {
			});
			return false;
		}
		var index = 0;
		for(var i=0;i<checkedLen;i++){
			if(returnType==2){
				goodDataChecked[i].data=goodDataChecked[i];
			}
			var canReturnNumber = goodDataChecked[i].data.canReturnCount;
			if(returnType == '1' && canReturnNumber <=0){
				continue;
			}
			params['createOrderReturnGoodsList['+index+'].relatingReturnSn']=returnCommon.returnSn;
			params['createOrderReturnGoodsList['+index+'].customCode']=goodDataChecked[i].data.customCode;
			params['createOrderReturnGoodsList['+index+'].extensionCode']=goodDataChecked[i].data.extensionCode;
			params['createOrderReturnGoodsList['+index+'].extensionId']=goodDataChecked[i].data.extensionId;
			params['createOrderReturnGoodsList['+index+'].osDepotCode']=goodDataChecked[i].data.osDepotCode;
			params['createOrderReturnGoodsList['+index+'].goodsBuyNumber']=goodDataChecked[i].data.goodsBuyNumber;
			params['createOrderReturnGoodsList['+index+'].chargeBackCount']=goodDataChecked[i].data.shopReturnCount;
			params['createOrderReturnGoodsList['+index+'].goodsReturnNumber']=goodDataChecked[i].data.canReturnCount;
			params['createOrderReturnGoodsList['+index+'].haveReturnCount']=goodDataChecked[i].data.havedReturnCount;
			params['createOrderReturnGoodsList['+index+'].returnReason']=goodDataChecked[i].data.returnReason;
			params['createOrderReturnGoodsList['+index+'].goodsThumb']=goodDataChecked[i].data.goodsThumb;
			params['createOrderReturnGoodsList['+index+'].payPoints']=goodDataChecked[i].data.settlementPrice;
			params['createOrderReturnGoodsList['+index+'].seller']=goodDataChecked[i].data.seller;
			params['createOrderReturnGoodsList['+index+'].integralMoney']=goodDataChecked[i].data.integralMoney;//积分使用金额
			params['createOrderReturnGoodsList['+index+'].salesMode']=goodDataChecked[i].data.salesMode;//商品销售模式：1为自营，2为买断，3为寄售，4为直发
			
			//退差价
			params['createOrderReturnGoodsList['+index+'].priceDifferNum']=goodDataChecked[i].data.priceDifferNum;
			params['createOrderReturnGoodsList['+index+'].priceDifference']=goodDataChecked[i].data.priceDifference;
			
			//价格
			params['createOrderReturnGoodsList['+index+'].goodsPrice']=goodDataChecked[i].data.goodsPrice;
			params['createOrderReturnGoodsList['+index+'].marketPrice']=goodDataChecked[i].data.marketPrice;
			params['createOrderReturnGoodsList['+index+'].settlementPrice']=goodDataChecked[i].data.settlementPrice;
			params['createOrderReturnGoodsList['+index+'].discount']=goodDataChecked[i].data.discount;

			params['createOrderReturnGoodsList['+index+'].shareBonus']=goodDataChecked[i].data.shareBonus;
			params['createOrderReturnGoodsList['+index+'].sap']=goodDataChecked[i].data.sap;
			params['createOrderReturnGoodsList['+index+'].bvValue']=goodDataChecked[i].data.bvValue;
			params['createOrderReturnGoodsList['+index+'].baseBvValue']=goodDataChecked[i].data.baseBvValue;
			params['createOrderReturnGoodsList['+index+'].goodsSn']=goodDataChecked[i].data.goodsSn;
			
			params['createOrderReturnGoodsList['+index+'].goodsName']=goodDataChecked[i].data.goodsName;
			params['createOrderReturnGoodsList['+index+'].colorName']=goodDataChecked[i].data.goodsColorName;
			params['createOrderReturnGoodsList['+index+'].sizeName']=goodDataChecked[i].data.goodsSizeName;

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
			if(checkNum2<0){
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
		params.orderReturnSn=returnCommon.returnSn;//退单Sn
		return params;
		
	},
	returnOrderUpdate:function(record,type){
		var url = "";
		var me = this;
		var param ={};
		param.btnType = "";
		param.returnSn = returnSn;
		
		if(record.get('id')){
			param.goodsId = record.get('id');
		}else{
			alert("商品id不存在！");
		}
		if(type == "isGoodReceived"){//收货
			if(record.get("isGoodReceived") == 0){
				param.btnType = "receiveReturn";
				url ="receiveReturnButtonClick";
			}else{
				param.btnType = "unReceiveReturn";
				url ="unReceiveReturnButtonClick";
			}
		}else if(type == "qualityStatus"){//质检
			if(record.get("qualityStatus") == 0){
				url ="passReturnButtonClick";
				param.btnType = "passReturn";
			}else{
				url ="unPassReturnButtonClick";
				param.btnType = "unPassReturn";
			}
		}else if(type == "checkinStatus"){//入库
			if(record.get("checkinStatus") == 0 || record.get("checkinStatus") == 2){
				param.btnType = "storageReturn";
				url = "storageReturnButtonClick";
			}
		}
		Ext.Ajax.request({
			url:  basePath + '/custom/orderReturn/'+url,
			params: param,
			async: false,
			timeout:90000,
			data:"json",
			success: function(response){
				var text = response.responseText;
				var result = Ext.JSON.decode(text);
				checkFlag = result.success;
				me.initData();
				console.dir(result);
			},
			failure: function(response){
				Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
			}
		});
	},
	getReturnPayStatusName : function(payStatus){
		if(payStatus=='0'){
			return '未退款';
		}else if(payStatus=='1'){
			return '已退款';
		}else if(payStatus=='2'){
			return '待退款';
		}else{
			return '';
		}
	}
});