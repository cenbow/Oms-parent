var pageSize = 20;
var questionType = 0;
var isHistory = 0;
var orderSns = new Array();// 选中订单数组

/*******************************************************************************
 * 订单列表查询
 ******************************************************************************/

//alert("OrderQuestionPanl");
Ext.define("MB.view.orderQuestionList.OrderQuestionPanl",
				{
					extend : "Ext.form.Panel",
					alias : 'widget.orderQuestionPanl',
					frame : true,
					requires : [ 
					            
					        'MB.view.common.ChannelTypeCombo',
							'MB.view.common.ChannelInfoCombo',
						//	'MB.view.common.WarehouseListOptionCombo',
						
							'MB.view.common.ChannelShopComboByMult',
						//	'MB.view.common.TransTypeCombo',
						//	'MB.view.common.ReasonCombo',
						//	'MB.view.common.InvoicesOrganizationCombo',
							'MB.view.common.CityCombo',
							'MB.view.common.ProvinceChannelTypeCombo',
							'MB.view.common.CountryChannelTypeCombo',
							'MB.view.common.AreaCombo'//,
					
					//		'MB.view.common.OrderCategoryCombo',
						//	'MB.model.ReturnErpWarehouseModel',
						//	'MB.store.IsGroupStore'
					
							],
					head : true,
					border: false,
					
					collapsible : true,
					buttonAlign : 'center',// 按钮居中
					fieldDefaults : {
						labelAlign : 'right'
					},
					id:"orderQuestionListPanlId",
				
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
				        
				        
				    	var orderQuestionListPanlId =  Ext.getCmp("orderQuestionListPanlId");
						var orderQuestionGridId =  Ext.getCmp("orderQuestionGridId");
				        
						var formHeight = orderQuestionListPanlId.getHeight();
						var clientHeight = document.body.clientHeight;
						var clientWidth = document.body.clientWidth;
						
						orderQuestionGridId.setHeight(clientHeight-0-40);
						orderQuestionGridId.setWidth('100%');
						orderQuestionListPanlId.setWidth('100%');
				        
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
				        
				  
				    	var orderQuestionListPanlId =  Ext.getCmp("orderQuestionListPanlId");
						var orderQuestionGridId =  Ext.getCmp("orderQuestionGridId");
				        
						var formHeight = orderQuestionListPanlId.getHeight();
						var clientHeight = document.body.clientHeight;
						var clientWidth = document.body.clientWidth;
						
						orderQuestionGridId.setHeight(clientHeight-formHeight-220);
						
						orderQuestionGridId.setWidth('100%');
						orderQuestionListPanlId.setWidth('100%');
	
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
												layout : "form", // 从上往下的布局
												xtype : "textfield",
												id:'orderQuestionPageMasterOrderSn',
												name: 'masterOrderSn',
												fieldLabel : "订单号",
												labelWidth:80,
												width : 220,
											},{
												layout : "form", // 从上往下的布局
												xtype : "textfield",
												id:'orderQuestionPageUserName',
												name: 'userName',
												fieldLabel : "下单人",
												labelWidth:80,
												width : 220,
											},{
												
												xtype : 'combo',
												store : 'TransTypeStore',
												id : 'orderQuestionPageTransType',
												name : 'transType',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "交易类型",
												emptyText : "请选择支付类型",
												editable : false
												
											},{
												xtype : "textfield",
												labelWidth : 80,
												id : 'OrderQuestionPageOrderSn',
												name : 'orderSn',
												fieldLabel : "交货单号",
												hidden:true,
												width : 230
											}
											
							
								       ]

								},{
									// 第二行
									xtype : 'fieldcontainer',
									labelStyle : 'font-weight:bold;padding:0;',
									layout : 'column',
									columnWidth : 1,
									defaultType : 'displayfield',
									labelWidth : 80,
									margin : '10 0 0 0',
									items : [
									         
									  /*   	{ 
												xtype : 'combo',
												store : 'ProcessStatusStore',
												id : 'orderQuestionPageProcessStatus',
												name : 'processStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "处理状态",
												emptyText : "请选择",
												editable : false
											},*/
									          {
												
												xtype : 'combo',
												store : 'PayStatusStore',
												id : 'orderQuestionPagePayStatus',
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
												fieldLabel : "支付状态",
												emptyText : "请选择支付状态",
												editable : false
								
											},{
												
												xtype : 'combo',
												store : 'ShipStatusStore',
												id : 'orderQuestionPageShipStatus',
												name : 'shipStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												hidden : true,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "发货状态",
												emptyText : "请选择发货状态",
												editable : false
									
										},{
											
												xtype : 'combo',
												store : 'ProcessStatusStore',
												id : 'orderQuestionPageProcessStatus',
												name : 'processStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 230,
												fieldStyle : 'width:'+ (230 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "处理状态",
												emptyText : "请选择",
												editable : false
									
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
													store : 'TimeOfOrderQuestionStore',
													id : 'orderQuestionPageSelectTimeType',
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
													id:"orderQuestionPageStartTime",
													width : 220,
													name: 'addTimeStart',
													fieldLabel: '开始时间',
													labelWidth: 80,
													listeners:{
														render:function(p){
															p.getEl().on('click',function(){
															    
																WdatePicker({startDate:'%y-%M-%d 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss'});
															});
														}
													}
												},{
													xtype: 'textfield',
													id:"orderQuestionPageEndTime",
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
												},{ 
														xtype : 'combo',
														store : 'RefererStore',
														id : 'orderQuestionPageReferer',
														name : 'referer',
														displayField : 'n',
														valueField : 'v',
														queryMode : "local",
														width : 230,
														fieldStyle : 'width:'+ (230 - 80 - 26),
														labelWidth : 80,
														triggerAction : 'all',
														hideMode : 'offsets',
														forceSelection : true,
														fieldLabel : "referer",
														emptyText : "请选择订单来源媒体",
														editable : false
														
													}
												
												
									         
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
											{
												width : 220,
												xtype: 'radiogroup',
												name:'listDataType',
												id:'orderQuestionPageListDataType',
												items: [
													{boxLabel: 'oms问题单', name: 'listDataType',inputValue:'true', checked: true},
													{boxLabel: '缺货问题单', name: 'listDataType',inputValue:'false'}
												],
												listeners: {
													
													

													"change" : function(radio, newV, oldV, e) {
														
														var orderQuestionReason = Ext.getCmp('orderQuestionPageReason');//问题单原因
														
														orderQuestionReason.getStore().removeAll();
														orderQuestionReason.setValue('');
														
														if(newV.listDataType=="true") {
															questionType = 0;
													
															orderQuestionReason.getStore().loadData(questionDataStoreList,false);
													
														} else if(newV.listDataType=="false"){
															questionType = 1;
													
															orderQuestionReason.getStore().loadData(mylogistics_question_reason,false);
														
														}
													}
											
												}
											
											
											}
											
											,{
									
												xtype : 'combo',
												store : 'QuestionStore',
												id : 'orderQuestionPageReason',
												name : 'reason',
												displayField : 'name',
												valueField : 'name',
												queryMode : "local",
												width : 440,
												fieldStyle : 'width:'+ 300,
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "问题单原因",
												emptyText : "请选择",
												editable : false
											
											},	{
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

								},{
									// 第五行
									xtype : 'fieldcontainer',
									labelStyle : 'font-weight:bold;padding:0;',
									layout : 'column',
									columnWidth : 1,
									defaultType : 'displayfield',
									labelWidth : 80,
									margin : '10 0 0 0',
									items : [
							
											{ 
												xtype : 'channeltypecombo',
												id:'orderQuestionListPageChanneltypecombo',
												width : 230,
												labelWidth : 80,
												
												name:'orderFormFirst',
												allowBlank : false,
												listeners : {
													'change' : me.changeChannelType
												}
											},{
												xtype : 'channelinfocombo',
												id:'orderQuestionListPageChannelinfocombo',
												width : 230,
												labelWidth : 80,
												name:'orderFormSec',
												allowBlank : false,
												listeners : {
													'change' : me.changeChannelCode
												}
											},{
												xtype : 'channelShopComboByMult',
												id:'orderQuestionListPageChannelshopcombo',
												width : 230,
												name:'orderFormsVo',
										
												labelWidth : 80,
												allowBlank : false
											},{
												
												xtype : 'radiogroup',
												id : 'orderQuestionListPageMainOrChild',
												width : 240,
												//name : 'mainOrChild',
												items : [ {
													boxLabel : '订单号',
													name : 'mainOrChild',
													inputValue : 'main',
													checked : true
												}, {
													boxLabel : '交货单',
													name : 'mainOrChild',
													inputValue : 'child'
												}],
												listeners: {
													"change" : function(radio, newV, oldV, e) {
														
														if(newV.mainOrChild=="main") { //主问题单
															
															Ext.getCmp("orderQuestionListPageChanneltypecombo").setHidden(false);
															Ext.getCmp("orderQuestionListPageChannelinfocombo").setHidden(false);
															Ext.getCmp("orderQuestionListPageChannelshopcombo").setHidden(false);
															Ext.getCmp("orderQuestionPageShipStatus").setHidden(true);
															Ext.getCmp("OrderQuestionPageOrderSn").setHidden(true);
															
														} else{ //子问题单
															
															Ext.getCmp("orderQuestionListPageChanneltypecombo").setHidden(true);
															Ext.getCmp("orderQuestionListPageChannelinfocombo").setHidden(true);
															Ext.getCmp("orderQuestionListPageChannelshopcombo").setHidden(true);
															Ext.getCmp("orderQuestionPageShipStatus").setHidden(false);
															Ext.getCmp("OrderQuestionPageOrderSn").setHidden(false);
															
															
														}
														
													}
												}
											}
									]

								}

						];
						me.buttons = [
					          	{
									xtype : 'combo',
									store : 'QueryPageStore',
									id : 'orderQuestionQueryPageArray',
									name : 'queryPageArray',
									displayField : 'n',
									valueField : 'v',
									queryMode : "local",
									width : 210,
									fieldStyle : 'width:' + (210 - 80 - 26),
									labelWidth : 80,
									triggerAction : 'all',
									hideMode : 'offsets',
									forceSelection : true,
									fieldLabel : "每页显示",
									emptyText : "请选择每页显示条数",
									editable : false
								},{
									text : '查询',
									columnWidth : .1,
									handler : me.search,
								},{
									text : '重置',
									handler : function(btn) {
								
										//第一行
						
										Ext.getCmp("orderQuestionPageUserName").setValue("");
										Ext.getCmp("orderQuestionPageTransType").setValue(-1);
										Ext.getCmp("OrderQuestionPageOrderSn").setValue("");
									//	Ext.getCmp("").setValue("newDate");
										
										//第二行								
										Ext.getCmp("orderQuestionPagePayStatus").setValue(-1);
										Ext.getCmp("orderQuestionPageShipStatus").setValue(-1);
										Ext.getCmp("orderQuestionPageProcessStatus").setValue(0);
										//Ext.getCmp("orderQuestionPageProcessStatus").setValue(-1);
											
										//第三行
										Ext.getCmp("orderQuestionPageSelectTimeType").setValue(-1);
										Ext.getCmp("orderQuestionPageStartTime").setValue("");
										Ext.getCmp("orderQuestionPageEndTime").setValue("");
										Ext.getCmp("orderQuestionPageReferer").setValue(-1);
							    
										//第四行
										
										
										
										
										//第五行
										Ext.getCmp("orderQuestionListPageChanneltypecombo").setValue('-1');
										Ext.getCmp("orderQuestionListPageChannelinfocombo").setValue();
										Ext.getCmp("orderQuestionListPageChannelshopcombo").setValue();
								
									}
								},{
									id : "orderReturnPageExportBtn",
									text : '导出',
									handler : me.exportRecord
								}];
						
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
						
						pageSize = Ext.getCmp('orderQuestionQueryPageArray').getValue();

					//	var form = btn.up('form');// 当前按钮form
						
						var form =  Ext.getCmp('orderQuestionListPanlId');
					//	var form = btn.up('viewport').down("orderQuestionPanl").down("form");
					/*	var initParams = {
							start : 0,
							limit : pageSize
						};*/
						
						var searchParams = getFormParams(form, null);
			
						var orderQuestionGridOrderList = form.up('viewport').down("orderQuestionGrid");
						orderQuestionGridOrderList.store.on('beforeload', function(store, options) {
							Ext.apply(store.proxy.extraParams, searchParams);
						});
						orderQuestionGridOrderList.store.currentPage = 1;// 翻页后重新查询
																		// 页面重置为1
						orderQuestionGridOrderList.store.pageSize = pageSize;
						orderQuestionGridOrderList.store.load({
							params : searchParams
						});

					},
					
					exportRecord :function (btn){ //模版导出
					
						var thisPage = btn.up("form").up("viewport");
						
						var myPanel = btn.up("form");
						
						var searchParams = btn.up("form").getValues();
						
						myPanel.exportAjax(thisPage,searchParams)
						
					},
		
					exportAjax: function (thisPage,searchParams){//导出ajax
					
						var exportCsvFileUrl = basePath+"/custom/orderQuestion/orderquestionExportCsvFile.spmvc?exportState=1";
						
						var myMask = new Ext.LoadMask({
						    msg    : '请稍等,正在导出...',
						    target : thisPage
						});
						myMask.show();
					
						Ext.Ajax.request({
							timeout: 1800000,//1800秒
							url: exportCsvFileUrl ,
							params:searchParams,
							success:function(response) {
								if (myMask != undefined){ myMask.hide();}
								var obj = Ext.decode(response.responseText);
								
								var path = obj.data.path;
								var fileName = obj.data.fileName;
								if(obj.isok==true){
									window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
									if (myMask != undefined){ myMask.hide();}
					
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

});


function setNormalQuestonResize() {

	var orderQuestionListPanlId = Ext.getCmp('orderQuestionListPanlId');
	
	var orderQuestionGridId = Ext.getCmp('orderQuestionGridId');
	
	var shortageQuestion_gridss_id = Ext.getCmp('shortageQuestion_gridss_id');
	
	var formHeight = orderQuestionListPanlId.getHeight();
	var clientHeight = document.body.clientHeight;
	
	orderQuestionGridId.setHeight(clientHeight-formHeight-30);
	shortageQuestion_gridss_id.setHeight(clientHeight-formHeight-30);
	orderQuestionListPanlId.setWidth('100%');
	if (questionType == 0) {
	//	Ext.getCmp("orderQuestionGridTable").columnWidth = 1;
		orderQuestionListPanlId.columnWidth = 1;
		shortageQuestion_gridss_id.setTitle("问题订单商品列表");
	} else {
		orderQuestionGridId.columnWidth = 0.78;
		shortageQuestion_gridss_id.columnWidth = 0.2;
	}
}
