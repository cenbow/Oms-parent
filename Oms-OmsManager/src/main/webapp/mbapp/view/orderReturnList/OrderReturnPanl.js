 var pageSize = 20;
/*******************************************************************************
 * 订单列表查询
 ******************************************************************************/
Ext.define("MB.view.orderReturnList.OrderReturnPanl",
				{
					extend : "Ext.form.Panel",
					alias : 'widget.orderReturnPanl',
					frame : true,
					requires : [ 
					        'MB.view.common.ChannelTypeCombo',
							'MB.view.common.ChannelInfoCombo',
							'MB.view.common.WarehouseListOptionCombo',
							
							'MB.view.common.ChannelShopComboByMult',
							
							'MB.view.common.TransTypeCombo',
							'MB.view.common.ReasonCombo',
							'MB.view.common.InvoicesOrganizationCombo',
							'MB.view.common.CityCombo',
							'MB.view.common.ProvinceChannelTypeCombo',
							'MB.view.common.CountryChannelTypeCombo',
							'MB.view.common.AreaCombo',
					
							'MB.view.common.OrderCategoryCombo',
							'MB.model.ReturnErpWarehouseModel',
							'MB.store.IsGroupStore' 
							],
					head : true,
					border: false,	
					collapsible : true,
					buttonAlign : 'center',// 按钮居中
					fieldDefaults : {
						labelAlign : 'right'
					},
					id:"orderReturnListPanlId",
					collapse: function(direction, animate) {
				        var me = this,
				            collapseDir = direction || me.collapseDirection,
				            ownerCt = me.ownerCt,
				            layout = me.ownerLayout,
				            rendered = me.rendered;
				        if (me.isCollapsingOrExpanding) {
				            return me;
				        }
				        if (arguments.length < 2) {
				            animate = me.animCollapse;
				        }
				        if (me.collapsed || me.fireEvent('beforecollapse', me, direction, animate) === false) {
				            return me;
				        }
				        if (layout && layout.onBeforeComponentCollapse) {
				            if (layout.onBeforeComponentCollapse(me) === false) {
				                return me;
				            }
				        }
				        if (rendered && ownerCt && me.isPlaceHolderCollapse()) {
				            return me.placeholderCollapse(direction, animate);
				        }
				        me.collapsed = collapseDir;
				        if (rendered) {
				            me.beginCollapse();
				        }
				        me.getInherited().collapsed = true;
				        me.fireHierarchyEvent('collapse');
				        if (rendered) {
				            me.doCollapseExpand(1, animate);
				        }
				     
				    	var orderReturnListPanlId =  Ext.getCmp("orderReturnListPanlId");
						var orderReturnGridId =  Ext.getCmp("orderReturnGridId");

						var formHeight = orderReturnListPanlId.getHeight();
						var clientHeight = document.body.clientHeight;
						var clientWidth = document.body.clientWidth;
						orderReturnGridId.setHeight(clientHeight-0-40);
						orderReturnGridId.setWidth('100%');
						orderReturnListPanlId.setWidth('100%');
				        return me;
				    },
				    expand: function(animate) {
				    	
				        var me = this,
				            layout = me.ownerLayout,
				            rendered = me.rendered;
				        if (me.isCollapsingOrExpanding) {
				            return me;
				        }
				        if (!arguments.length) {
				            animate = me.animCollapse;
				        }
				        if (!me.collapsed && !me.floatedFromCollapse) {
				            return me;
				        }
				        if (me.fireEvent('beforeexpand', me, animate) === false) {
				            return me;
				        }
				        if (layout && layout.onBeforeComponentExpand) {
				            if (layout.onBeforeComponentExpand(me) === false) {
				                return me;
				            }
				        }
				        delete me.getInherited().collapsed;
				        if (rendered && me.isPlaceHolderCollapse()) {
				            return me.placeholderExpand(animate);
				        }
				        me.restoreHiddenDocked();
				        if (rendered) {
				            me.beginExpand();
				        }
				        me.collapsed = false;
				        if (me.rendered) {
				            me.doCollapseExpand(2, animate);
				        }
						var orderReturnListPanlId =  Ext.getCmp("orderReturnListPanlId");
						var orderReturnGridId =  Ext.getCmp("orderReturnGridId");
						var formHeight = orderReturnListPanlId.getHeight();
						var clientHeight = document.body.clientHeight;
						orderReturnGridId.setHeight(clientHeight-formHeight-350);
						orderReturnGridId.setWidth('100%');
						orderReturnListPanlId.setWidth('100%');
				        return me;
				    },
					initComponent : function() {
						var me = this;
						me.items = [
								{
									// 第一行
									xtype : 'fieldcontainer',
									labelStyle : 'font-weight:bold;padding:0;',
									layout : 'column',
									columnWidth : 1,
									defaultType : 'displayfield',
									labelWidth : 80,
									margin : '10 0 0 0',
									items : [
											{ 
												xtype : "textfield",
												labelWidth : 80,
												id : 'orderReturnPageReturnSn',
												name : 'returnSn',
												fieldLabel : "退单号",
												width : 230
											},{
												xtype : 'textfield',
												labelWidth : 80,
												fieldLabel : '订单号',
												id : 'orderReturnPageRelatingOrderSn',
												name : 'masterOrderSn',
												width : 230
											},
											{
												xtype : "textfield",
												labelWidth : 80,
												id : 'orderReturnListPageOrderSn',
												name : 'orderSn',
												fieldLabel : "交货单号",
												width : 230
											}
											,
											{
												xtype : 'combo',
												store : 'OrderViewStore',
												id : 'orderReturnPageOrderView',
												name : 'orderView',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "显示类型",
												emptyText : "请选择",
												editable : false
											}
									    ]
								},
								{
									// 第二行
									xtype : 'fieldcontainer',
									labelStyle : 'font-weight:bold;padding:0;',
									layout : 'column',
									columnWidth : 1,
									defaultType : 'displayfield',
									labelWidth : 80,
									margin : '10 0 0 0',
									items : [
											/*{
												xtype : 'channeltypecombo',
												id:'orderReturnListPageChanneltypecombo',
												width : 230,
												labelWidth : 80,
												name:'orderFromFirst',
												allowBlank : false,
												listeners : {
													'change' : me.changeChannelType
												}
											}
											,*/
											{
												xtype : 'channelinfocombo',
												id:'orderReturnListPageChannelinfocombo',
												width : 230,
												labelWidth : 80,
												name:'orderFromSec',
												allowBlank : false,
												listeners : {
													'change' : me.changeChannelCode
												}
											},{
												xtype : 'channelShopComboByMult',
												id:'orderReturnListPageChannelshopcombo',
												width : 230,
												name:'orderFroms',
												labelWidth : 80,
												allowBlank : false
											},
											{
												xtype : 'textfield',
												labelWidth : 80,
												fieldLabel : '退货快递号',
												id : 'orderReturnPageReturnInvoiceNo',
												name : 'returnInvoiceNo',
												width : 230
											}
										]
								},{
									// 第三行
									xtype : 'fieldcontainer',
									labelStyle : 'font-weight:bold;padding:0;',
									layout : 'column',
									columnWidth : 1,
									defaultType : 'displayfield',
									labelWidth : 80,
									margin : '10 0 0 0',
									items : [
											{
												xtype : 'combo',
												store : 'ReturnOrderStatusStore',
												id : 'orderReturnPageReturnOrderStatus',
												name : 'returnOrderStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "退单状态",
												emptyText : "请选择退单状态",
												editable : false
											},
											/*{ 
												xtype : 'combo',
												store : 'ReturnPayStatusByListStore',
												id : 'orderReturnPageReturnPayStatus',
												name : 'payStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "财务状态",
												emptyText : "请选择财务状态",
												editable : false
											},*/
											{ 
												
												xtype : 'combo',
												store : 'IsReceivedGoodsStore',
												id : 'orderReturnPageIsGoodReceived',
												name : 'isGoodReceived',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "是否收到货",
												emptyText : "请选择",
												editable : false
												
											}
											,
											{
												xtype : 'combo',
												store : 'ReturnReasonForListStore',
												id : 'orderReturnPageReturnReason',
												name : 'returnReason',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "退单原因",
												emptyText : "请选择退单原因",
												editable : false
											}
											,
											{ 
												xtype : 'combo',
												store : 'ProcessTypeForListStore',
												id : 'orderReturnPageProcessType',
												name : 'processType',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "处理方式",
												emptyText : "请选择处理方式",
												editable : false
											}
											/*,
											 { 
												xtype : 'combo',
												store : 'CheckinStatusStore',
												id : 'orderReturnPageCheckinStatus',
												name : 'checkinStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "是否入库",
												emptyText : "请选择",
												editable : false
											}*/
										]
								},{
									// 第四行
									xtype : 'fieldcontainer',
									labelStyle : 'font-weight:bold;padding:0;',
									layout : 'column',
									columnWidth : 1,
									defaultType : 'displayfield',
									labelWidth : 80,
									margin : '10 0 0 0',
									items : [
										/*{
											
											xtype : 'combo',
											store : 'OrderStatusStore',
											id : 'orderReturnPageOrderOrderStatus',
											name : 'orderOrderStatus',
											displayField : 'n',
											valueField : 'v',
											queryMode : "local",
											width : 230,
											fieldStyle : 'width:'+ (230 - 80 - 26),
											labelWidth : 80,
											triggerAction : 'all',
											hideMode : 'offsets',
											forceSelection : true,
											fieldLabel : "订单状态",
											emptyText : "请选择订单状态",
											editable : false
											
										},
										{ 
											xtype : 'combo',
											store : 'PayStatusStore',
											id : 'orderReturnPageOrderPayStatus',
											name : 'orderPayStatus',
											displayField : 'n',
											valueField : 'v',
											queryMode : "local",
											width : 230,
											fieldStyle : 'width:'+ (230 - 80 - 26),
											labelWidth : 80,
											triggerAction : 'all',
											hideMode : 'offsets',
											forceSelection : true,
											fieldLabel : "支付状态",
											emptyText : "请选择支付状态",
											editable : false
											
										},
										{ 
											xtype : 'combo',
											store : 'ShipStatusStore',
											id : 'orderReturnPageOrderShipStatus',
											name : 'orderShipStatus',
											displayField : 'n',
											valueField : 'v',
											queryMode : "local",
											width : 230,
											fieldStyle : 'width:'+ (230 - 80 - 26),
											labelWidth : 80,
											triggerAction : 'all',
											hideMode : 'offsets',
											forceSelection : true,
											fieldLabel : "发货状态",
											emptyText : "请选择发货状态",
											editable : false
											
										},
										{
												width : 220,
												layout : "form", 
												xtype : "textfield",
												id:'orderReturnPageUserName',
												name: 'userName',
												fieldLabel : "下单人",
												labelWidth: 80
										}*/
										,
										{
											width : 230, // 该列有整行中所占百分比
											layout : "form", // 从上往下的布局
											xtype : "textfield",
											id:'orderReturnPageReturnMobile',
											name: 'returnMobile',
											fieldLabel : "手机号",
											labelWidth: 80	
										}
										,
										{
											width : 230,
											layout : "form", 
											xtype : "textfield",
											id:'orderReturnPageUserName',
											name: 'userName',
											fieldLabel : "下单人",
											labelWidth: 80
										}
										,
										{
											width : 230,
											xtype : "textfield",
											id:'OrderReturnConsignee',
											name: 'consignee',
											fieldLabel : "收件人",
											labelWidth: 80
										}
										,
										{
											width : 230,
											xtype : "textfield",
											id:'OrderReturnPageSkuSn',
											name: 'skuSn',
											fieldLabel : "商品编码",
											labelWidth: 80
										}
									]

								}
								,
								{
									// //第七行
									xtype : 'fieldcontainer',
									labelStyle : 'font-weight:bold;padding:0;',
									layout : 'column',
									columnWidth : 1,
									id:"moreSelect2",
									defaultType : 'displayfield',
									labelWidth : 80,
									margin : '10 0 0 0',
									items : [
									         {
													xtype : 'combo',
													store : 'TimeOfOrderReturnStore',
													id : 'orderReturnPageSelectTimeType',
													name : 'selectTimeType',
													displayField : 'n',
													valueField : 'v',
													queryMode : "local",
													width : 230,
													fieldStyle : 'width:'+ (230 - 80 - 26),
													labelWidth : 80,
													triggerAction : 'all',
													hideMode : 'offsets',
													forceSelection : true,
													fieldLabel : "时间类型",
													emptyText : "请选择时间类型",
													editable : false
													
												},{
													xtype: 'textfield',
													id:"orderReturnPageStartTime",
													width : 230,
													name: 'startTime',
													fieldLabel: '开始时间',
													labelWidth: 80,
													listeners:{
														render:function(p){
															p.getEl().on('click',function(){
															    
																WdatePicker({startDate:'%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'});
															});
														}
													}
												} , {
													xtype: 'textfield',
													id:"orderReturnPageEndTime",
													width : 230,
													name: 'endTime',
													fieldLabel: '结束时间',
													labelWidth: 80,
													listeners:{
														render:function(p){
															p.getEl().on('click',function(){
																
																WdatePicker({startDate:'%y-%M-%d 23:59:59',dateFmt:'yyyy-MM-dd HH:mm:ss'});
															});
														}
													}
												},{
													xtype : 'warehouseListOptionCombo',
													id:'orderReturnPageWarehouseListOptionCombo',
													width : 230,
													labelWidth : 80,
													allowBlank : false,
												}
												/*,
												{
													xtype:'checkboxfield',
													fieldLabel:'导出结果不包含商品信息',
													name:'noOrderGoods',
													id:'orderReturnPageNoOrderGoods',
													inputValue:'1',//将checkboxfield传递到后台的值定义为1
													labelWidth: 170,
													labelAlign: 'right'
												}*/
									]
								}
								/*,
								{
									// 第九行
									xtype : 'fieldcontainer',
									labelStyle : 'font-weight:bold;padding:0;',
									layout : 'column',
									id:"moreSelect4",
									hidden:true,
									columnWidth : 1,
									defaultType : 'displayfield',
									labelWidth : 80,
									margin : '10 0 0 0',
									items : [
									         { 
													xtype : 'combo',
													store : 'ReturnPayStore',
													id : 'orderReturnPageReturnPay',
													name : 'returnPay',
													displayField : 'n',
													valueField : 'v',
													queryMode : "local",
													width : 230,
													fieldStyle : 'width:'+ (230 - 80 - 26),
													labelWidth : 80,
													triggerAction : 'all',
													hideMode : 'offsets',
													forceSelection : true,
													fieldLabel : "退款方式",
													emptyText : "请选择退款方式",
													editable : false				
												},{ 
													xtype : 'combo',
													store : 'QualityStatusStore',
													id : 'orderReturnPageQualityStatus',
													name : 'qualityStatus',
													displayField : 'n',
													valueField : 'v',
													queryMode : "local",
													width : 230,
													fieldStyle : 'width:'+ (230 - 80 - 26),
													labelWidth : 80,
													triggerAction : 'all',
													hideMode : 'offsets',
													forceSelection : true,
													fieldLabel : "质检状态",
													emptyText : "请选择",
													editable : false				
												},{
													
													xtype : 'combo',
													store : 'HaveRefundForListStore',
													id : 'orderReturnPageHaveRefund',
													name : 'haveRefund',
													displayField : 'n',
													valueField : 'v',
													queryMode : "local",
													width : 230,
													fieldStyle : 'width:'+ (230 - 80 - 26),
													labelWidth : 80,
													triggerAction : 'all',
													hideMode : 'offsets',
													forceSelection : true,
													fieldLabel : "是否退款",
													emptyText : "请选择",
													editable : false				
													
												}
												,{
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

								}*/

						];
						me.buttons = [
				
								{
									text : '查询',
									columnWidth : .1,
									handler : me.search,
								},{
									text : '重置',
									handler : function(btn) {

										var orderReturnForm = btn.up('form');
										resetButton(orderReturnForm);
										
										//第一行
										Ext.getCmp("orderReturnPageReturnSn").setValue("");
										Ext.getCmp("orderReturnPageRelatingOrderSn").setValue("");
										Ext.getCmp("orderReturnPageReturnInvoiceNo").setValue("");
										
										//第二行
										Ext.getCmp("orderReturnListPageChanneltypecombo").setValue('-1');
										Ext.getCmp("orderReturnListPageChannelinfocombo").setValue();
										Ext.getCmp("orderReturnListPageChannelshopcombo").setValue();
									
										//第三行
										Ext.getCmp("orderReturnPageReturnOrderStatus").setValue(-1);
//										Ext.getCmp("orderReturnPageReturnPayStatus").setValue(-1);
										Ext.getCmp("orderReturnPageIsGoodReceived").setValue(-1);
										Ext.getCmp("orderReturnPageCheckinStatus").setValue(-1);
								
										//第四行
										Ext.getCmp("orderReturnPageReturnMobile").setValue("");
			
										//第六行
										Ext.getCmp("orderReturnPageReturnReason").setValue(-1);
										Ext.getCmp("orderReturnPageProcessType").setValue(-1);
										Ext.getCmp("orderReturnPageUserName").setValue("");
								
										//第七行
										
										Ext.getCmp("orderReturnPageStartTime").setValue("");
										Ext.getCmp("orderReturnPageEndTime").setValue("");
										Ext.getCmp("orderReturnPageWarehouseListOptionCombo").setValue(-1);
										
										//第八行
										Ext.getCmp("orderReturnPageOrderView").setValue(-1);
										Ext.getCmp("OrderReturnPageSkuSn").setValue("");
										Ext.getCmp("OrderReturnConsignee").setValue("");
										Ext.getCmp("orderReturnPageSelectTimeType").setValue(-1);
//										Ext.getCmp("orderReturnPageListDataType").setValue("newDate");
										//第九行
//										Ext.getCmp("orderReturnPageReturnPay").setValue(-1);
//										Ext.getCmp("orderReturnPageQualityStatus").setValue(-1);
//										Ext.getCmp("orderReturnPageHaveRefund").setValue(-1);
									}
								},{
									id : "orderReturnPageExportBtn",
									text : '导出',
									handler : me.exportDefault
								} ];
						
						me.buttonAlign ='right',
						me.callParent(arguments);
					},
					changeChannelType : function(combo, newValue, oldValue) {
						// 商品编码有变化时,加载尺码和颜色码列表
						var channelInfoCombo = this.up('panel').down(
								'channelinfocombo');
						channelInfoCombo.clearValue();
						channelInfoCombo.store.on('beforeload',
							function(store) {
								params = {
									"channelType" : newValue
								};
								Ext.apply(store.proxy.extraParams, params);
							});
						channelInfoCombo.store.load();
					},
					changeChannelCode : function(combo, newValue, oldValue) {
						var channelShopCombo = this.up('panel').down('channelShopComboByMult');
						channelShopCombo.clearValue();
						channelShopCombo.store.on('beforeload',
							function(store) {
								params = {
									"channelCode" : newValue
								};
								Ext.apply(store.proxy.extraParams, params);
							});
						channelShopCombo.store.load();
					},
					changeOrdercategorycombo : function(combo, newValue, oldValue) {
						var reasonCombo = this.up('panel').down('reasoncombo');
						var invoicesOrganizationCombo = this.up('panel').down(
								'invoicesorganizationcombo');
						if (2 == newValue) {
							reasonCombo.setValue('J');
							invoicesOrganizationCombo.getStore().loadData(
									SELECT.system_cost_center, false);
							invoicesOrganizationCombo.setValue('1700100100');
						} else if (3 == newValue) {
							reasonCombo.setValue('T');
							invoicesOrganizationCombo.getStore().loadData(
									SELECT.shipping_id, false);
							invoicesOrganizationCombo.setValue('13');
						} else {
							reasonCombo.setValue('S');
							invoicesOrganizationCombo.getStore().loadData([ {
								v : '',
								n : '请选择...（零售时为空）'
							}, ], false);
							invoicesOrganizationCombo.setValue('');
						}
						reasonCombo.setDisabled(true);
					},

					changeCountryChannelType : function(combo, newValue, oldValue) {// 选中国家

						var provinceChannelTypeCombo = this.up('panel').down(
								'provinceChannelTypeCombo');
						provinceChannelTypeCombo.clearValue();

						var cityCombo = this.up('panel').down('cityCombo');
						cityCombo.clearValue();

						var areaCombo = this.up('panel').down('areaCombo');
						areaCombo.clearValue();

						if (combo.value != null && combo.value != '') {
							provinceChannelTypeCombo.getStore().getProxy().url = basePath
									+ "custom/orderInfo/getArea?type=1&regionId="
									+ combo.value;
							provinceChannelTypeCombo.getStore().load();
						}
					},

					changeProvinceChannelType : function(combo, newValue, oldValue) {// 选中省份
						var cityCombo = this.up('panel').down('cityCombo');
						cityCombo.clearValue();

						var areaCombo = this.up('panel').down('areaCombo');
						areaCombo.clearValue();

						if (combo.value != null && combo.value != '') {
							cityCombo.getStore().getProxy().url = basePath
									+ "custom/orderInfo/getArea?type=2&regionId="
									+ combo.value;
							cityCombo.getStore().load();
						}
					},

					changeCity : function(combo, newValue, oldValue) {// 选中城市
						var areaCombo = this.up('panel').down('areaCombo');
						areaCombo.clearValue();

						if (combo.value != null && combo.value != '') {
							areaCombo.getStore().getProxy().url = basePath
									+ "custom/orderInfo/getArea?type=3&regionId="
									+ combo.value;
							areaCombo.getStore().load();
						}
					},

					search : function(btn) {

						var form = btn.up('form');// 当前按钮form
					/*	var initParams = {
							start : 0,
							limit : pageSize
						};*/
						var searchParams = getFormParams(form, null);
			
						var orderReturnGridOrderList = form.up('viewport').down("orderReturnGrid");
						orderReturnGridOrderList.store.on('beforeload', function(store, options) {
							Ext.apply(store.proxy.extraParams, searchParams);
						});
						orderReturnGridOrderList.store.currentPage = 1;// 翻页后重新查询
																		// 页面重置为1
						orderReturnGridOrderList.store.pageSize = pageSize;
						orderReturnGridOrderList.store.load({
							params : searchParams
						});

					},
					
					exportDefault :function (btn){ //默认模版导出
						var thisPage = btn.up("form").up("viewport");
						var myPanel = btn.up("form");
						var searchParams = btn.up("form").getValues();
						myPanel.exportAjax(thisPage,searchParams,"0");
					},
					exportFinancial :function (btn){ //财务模版导出
						var thisPage = btn.up("form").up("viewport");
						var myPanel = btn.up("form");
						var searchParams = btn.up("form").getValues();
						myPanel.exportAjax(thisPage,searchParams,"1")
					},
					exportLogistics :function (btn){//物流模版导出
						var thisPage = btn.up("form").up("viewport");
						var myPanel = btn.up("form");
						var searchParams = btn.up("form").getValues();
						myPanel.exportAjax(thisPage,searchParams,"2")
					},
						
					exportAjax: function (thisPage,searchParams,exportTemplateType){
						var url = basePath+ '/custom/orderReturn/orderReturnExportCsvFile.spmvc?exportTemplateType='+exportTemplateType;
						Ext.getCmp('orderReturnPageExportBtn').setDisabled(true);
						var myMask = new Ext.LoadMask({
							msg    : '请稍等,正在导出...',
							target : thisPage
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
									Ext.getCmp('orderReturnPageExportBtn').setDisabled(false);
								},
								failure : function(response) {
									myMask.hide();
									Ext.Msg.alert("验证", "失败");
									Ext.getCmp('orderReturnPageExportBtn').setDisabled(false);
								}
						});
					},
					
				exportOrdersettle :function (btn){
						
						var thisPage = btn.up("form").up("viewport");
			
						var searchParams = btn.up("form").getValues();
						
					//	var url = basePath + '/custom/orderInfo/orderInfoOrdersettleExportCsvFile.spmvc?exportTemplateType=1';
						
						var url = basePath+ '/custom/orderReturn/orderReturnOrdersettleExportCsvFile.spmvc?exportTemplateType=1';
						
						var myMask = new Ext.LoadMask({
							msg : '请稍等,正在导出...',
							target : thisPage
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

										if (myMask != undefined) {
											myMask.hide();
										}
										var obj = Ext.decode(response.responseText);
										var path = obj.data.path;
										var fileName = obj.data.fileName;

										if (obj.isok == true) {
											window.location.href = basePath+ "custom/downloadFtpFile.spmvc?path="+ path + "&fileName="+ fileName;
										} else {
											Ext.Msg.alert("错误", "导出失败");
										}
										Ext.getCmp('orderReturnPageExportBtn').setDisabled(false);
									},
									failure : function(response) {
										myMask.hide();
										Ext.Msg.alert("验证", "失败");
										Ext.getCmp('orderReturnPageExportBtn').setDisabled(false);
									}
								});
					},
					/*
					exportOrdersettle : function() {

						var searchParams = searchInExport(searchInExport);
						var url = basePath + '/custom/orderInfo/orderInfoOrdersettleExportCsvFile.spmvc?exportTemplateType=1';

						var myMask = new Ext.LoadMask({
							msg : '请稍等,正在导出...',
							target : orderInfoForm
						});
						myMask.show();

						Ext.Ajax.request({
								waitMsg : '请稍等.....',
								url : url,
								method : 'post',
								timeout : 7200000,
								params : searchParams,
								success : function(response) {

									if (myMask != undefined) {
										myMask.hide();
									}
									var obj = Ext.decode(response.responseText);
									var path = obj.data.path;
									var fileName = obj.data.fileName;

									if (obj.isok == true) {
										window.location.href = basePath+ "custom/downloadFtpFile.spmvc?path="+ path + "&fileName="+ fileName;
									} else {
										Ext.Msg.alert("错误", "导出失败");
									}
									Ext.getCmp('orderReturnPageExportBtn')
											.setDisabled(false);
								},
								failure : function(response) {
									myMask.hide();
									Ext.Msg.alert("验证", "失败");
									Ext.getCmp('orderReturnPageExportBtn')
											.setDisabled(false);
								}
						});

					}*/

});
