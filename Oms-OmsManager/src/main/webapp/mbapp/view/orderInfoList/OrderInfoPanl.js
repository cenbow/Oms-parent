var pageSize = 20;

/*******************************************************************************
 * 订单列表查询
******************************************************************************/
Ext.define("MB.view.orderInfoList.OrderInfoPanl",
				{
					extend : "Ext.form.Panel",
					alias : 'widget.orderInfoPanl',
					frame : true,
					requires : [ 'MB.view.common.ChannelTypeCombo',
							'MB.view.common.ChannelInfoCombo',
							'MB.view.common.ChannelShopComboByMult',
							'MB.view.common.TransTypeCombo',
							'MB.view.common.ReasonCombo',
							'MB.view.common.InvoicesOrganizationCombo',
							'MB.view.common.CityCombo',
							'MB.view.common.ProvinceChannelTypeCombo',
							'MB.view.common.CountryChannelTypeCombo',
							'MB.view.common.AreaCombo',
							'MB.view.orderInfoList.OrderInfoExportTemplateEdit',
							'MB.view.common.OrderCategoryCombo',
							'MB.store.IsGroupStore' ],
					head : true,
					border: false,
					collapsible : true,
					fieldDefaults : {
						labelAlign : 'right'
					},
					id:"orderInfoListPanlId",
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
			
				       	var orderInfoListPanl =  Ext.getCmp("orderInfoListPanlId");
			
				       	var OrderInfoGrid =  Ext.getCmp("orderInfoGridId");	     
			 	
				        var he = 40;	
	
				    	var formHeight = orderInfoListPanl.getHeight();
				    	var clientHeight = document.body.clientHeight;
				    	var clientWidth = document.body.clientWidth;
				    	
				    	OrderInfoGrid.setHeight(clientHeight-0-he);
				    	OrderInfoGrid.setWidth('100%');
				    	orderInfoListPanl.setWidth('100%');
				        
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
				        
				       	var orderInfoListPanl =  Ext.getCmp("orderInfoListPanlId");
			
				       	var OrderInfoGrid =  Ext.getCmp("orderInfoGridId");
	
						var formHeight = orderInfoListPanl.getHeight();
						var clientHeight = document.body.clientHeight;
						var clientWidth = document.body.clientWidth;
						
						var he = 220;
				     	
				       if(!Ext.getCmp("moreSelect1").isHidden()){
				    	   he+=120;
				       }
						
						OrderInfoGrid.setHeight(clientHeight-formHeight-he);
						OrderInfoGrid.setWidth('100%');
						orderInfoListPanl.setWidth('100%');
	
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
												id : 'orderInfoPageMasterOrderSn',
												name : 'masterOrderSn',
												fieldLabel : "订单号",
												width : 220
											},{
												xtype : 'textfield',
												fieldLabel : '外部交易号',
												id : 'orderInfoPageOrderOutSn',
												name : 'orderOutSn',
												labelWidth : 80,
												width : 220
												/*xtype : "textfield",
												labelWidth : 80,
												id : 'orderInfoPageOrderSn',
												name : 'orderSn',
												fieldLabel : "交货单号",
												width : 220*/
											}, {
												xtype : "textfield",
												labelWidth : 80,
												id : 'orderInfoPageUserName',
												name : 'userName',
												fieldLabel : "下单人",
												width : 220
											}, {
												xtype : "textfield",
												labelWidth : 80,
												id : 'orderInfoPageConsignee',
												name : 'consignee',
												fieldLabel : "收货人",
												width : 220
											}
											/*,
											{
												xtype : 'combo',
												store : 'AdvanceStatusStore',
												id : 'orderInfoPageAdvance',
												name : 'isAdvance',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 220,
												fieldStyle : 'width:' + (220 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "是否预售",
												emptyText : "请选择预售状态",
												editable : false
											}*/
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
											/*{
												xtype : 'channeltypecombo',
												id:'orderInfoPageOrderFromFirst',
												name:'orderFromFirst',
												width : 220,
												labelWidth : 80,
												allowBlank : false,
												listeners : {
													'change' : me.changeChannelType
												}
											}
											,*/
											{
												xtype : 'channelinfocombo',
												id:'orderInfoPageOrderFromSec',
												width : 220,
												name:'orderFromSec',
												labelWidth : 80,
												allowBlank : false,
												listeners : {
													'change' : me.changeChannelCode
												}
											},{
												xtype : 'channelShopComboByMult',
												id:'orderInfoPageOrderFrom',
												width : 220,
												name:'orderFroms',
												labelWidth : 80,
												allowBlank : false
											}
											,
											{
												xtype : 'combo',
												store : 'OrderTypeStore',
												id : 'orderInfoPageOrderType',
												name : 'orderType',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 220,
												labelWidth : 80,
//												margin : '0 10 0 0',
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "订单类型",
												emptyText : "--请选择--",
												editable : false
											}
											,
											{
												xtype : 'combo',
												store : 'SplitStatusStore',
												id : 'orderInfoPageSplitStatus',
												name : 'splitStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 220,
												fieldStyle : 'width:' + (220 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "拆单状态",
												emptyText : "请选择拆单状态",
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
												xtype : 'combo',
												store : 'OrderStatusStore',
												id : 'orderInfoPageOrderStatus',
												name : 'orderStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 220,
												fieldStyle : 'width:'
														+ (220 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "订单状态",
												emptyText : "请选择订单状态",
												editable : false
											},{
												xtype : 'combo',
												store : 'PayStatusStore',
												id : 'orderInfoPagePayStatus',
												name : 'payStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 220,
												fieldStyle : 'width:'
														+ (220 - 80 - 26),
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
												id : 'orderInfoPageShipStatus',
												name : 'shipStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 220,
												fieldStyle : 'width:'
														+ (220 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "发货状态",
												emptyText : "请选择发货状态",
												editable : false
											}
											,
											{
												xtype : 'combo',
												store : 'DepotStatusStore',
												id : 'orderInfoPageQuestionStatus',
												name : 'questionStatus',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 220,
												fieldStyle : 'width:'
														+ (220 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "问题单状态",
												emptyText : "请选择问题单状态",
												editable : false
											}
									]
								},
								{
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
												xtype : 'combo',
												store : 'TimeStore',
												id : 'orderInfoPageSelectTimeType',
												name : 'selectTimeType',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 220,
												fieldStyle : 'width:'+ (220 - 80 - 26),
												labelWidth : 80,
												triggerAction : 'all',
												hideMode : 'offsets',
												forceSelection : true,
												fieldLabel : "时间类型",
												emptyText : "请选择时间类型",
												editable : false
											},{
												xtype : 'textfield',
												id : "orderInfoPageStartTime",
												width : 220,
												name : 'startTime',
												fieldLabel : '开始时间',
												labelWidth : 80,
												listeners : {
													render : function(p) {p.getEl().on('click',function() {
															WdatePicker({
																startDate : '%y-%M-%d 00:00:00',
																dateFmt : 'yyyy-MM-dd HH:mm:ss'
															});
														});
													}
												}
											},
											{
												xtype : 'textfield',
												id : "orderInfoPageEndTime",
												width : 220,
												name : 'endTime',
												fieldLabel : '结束时间',
												labelWidth : 80,
												listeners : {
													render : function(p) {p.getEl().on('click',function() {
															WdatePicker({startDate : '%y-%M-%d 23:59:59',dateFmt : 'yyyy-MM-dd HH:mm:ss'});
														});
													}
												}
											},
											{
												xtype : 'combo',
												store : 'OrderInfoViewStore',
												id : 'orderInfoPageOrderView',
												name : 'orderView',
												displayField : 'n',
												valueField : 'v',
												queryMode : "local",
												width : 220,
												labelWidth : 80,
//												margin : '0 10 0 0',
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
									// //第六行
									xtype : 'fieldcontainer',
									labelStyle : 'font-weight:bold;padding:0;',
									layout : 'column',
									id:"moreSelect1",
									columnWidth : 1,
									defaultType : 'displayfield',
									labelWidth : 80,
									margin : '10 0 0 0',
									items : [
									{
										xtype : "textfield",
										labelWidth : 80,
										id : 'orderInfoPageTel',
										name : 'tel',
										fieldLabel : "收货人电话",
										width : 220
									}, {
										xtype : "textfield",
										labelWidth : 80,
										id : 'orderInfoPageMobile',
										name : 'mobile',
										fieldLabel : "收货人手机",
										width : 220
									}
									/*, 
									{
										width : 220,
										labelWidth : 80,
										xtype : "textfield",
										id : 'orderInfoPageSkuSn',
										name : 'skuSn',
										fieldLabel : "商品编码"
									}*/
									/*{
										xtype : "textfield",
										labelWidth : 80,
										id : 'orderInfoPageInvoiceNo',
										name : 'invoiceNo',
										fieldLabel : "快递单号",
										width : 220
									}*/
									]
								}
						];
						me.buttons = [
								{
									xtype : 'combo',
									store : 'QueryPageStore',
									id : 'orderInfoPageQueryPageArray',
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
								},
								/*{
									text : '输入订单号查询',
									handler : function() {
										var win = Ext.widget("orderInfoExportTemplateEdit");
										win.show();
									}
								},*/
								{
									itemId : 'orderInfoQuery',
									text : '查询',
									columnWidth : .1,
									handler : me.search,
								},{
									text : '重置',
									handler : function(btn) {
										var orderInfoForm = btn.up('form');
										resetButton(orderInfoForm);
										Ext.getCmp("orderInfoPageOrderStatus").setValue(-1);
//										Ext.getCmp("orderInfoPageReferer").setValue(-1);
//										Ext.getCmp("orderInfoPageIsGroup").setValue(-1);
										Ext.getCmp("orderInfoPageOrderView").setValue('0');
										Ext.getCmp("orderInfoPagePayStatus").setValue(-1);
										Ext.getCmp("orderInfoPageShipStatus").setValue(-1);
										Ext.getCmp("orderInfoPageQuestionStatus").setValue(-1);
										Ext.getCmp("orderInfoPageAdvance").setValue(-1);
										Ext.getCmp("orderInfoPageSplitStatus").setValue(-1);
										Ext.getCmp("orderInfoPageUserName").setValue("");
										Ext.getCmp("orderInfoPageSkuSn").setValue("");
										Ext.getCmp("orderInfoPageStartTime").setValue("");
										Ext.getCmp("orderInfoPageEndTime").setValue("");
//										Ext.getCmp("orderInfoPageOrderFromFirst").setValue('0');
										Ext.getCmp("orderInfoPageOrderFromSec").setValue();
										Ext.getCmp("orderInfoPageOrderFrom").setValue();
										/*Ext.getCmp("orderInfoPageCountry").setValue('');
										Ext.getCmp("orderInfoPageProvince").setValue('');
										Ext.getCmp("orderInfoPageCity").setValue('');
										Ext.getCmp("orderInfoPageDistrict").setValue('');*/
										Ext.getCmp("orderInfoPageQueryPageArray").setValue(pageSize.toString());
										Ext.getCmp("orderInfoPageSelectTimeType").setValue("addTime");
										Ext.getCmp("orderInfoPageMobile").setValue("");
										Ext.getCmp("orderInfoPageTel").setValue("");
										Ext.getCmp("orderInfoPageInvoiceNo").setValue("");
										Ext.getCmp("orderInfoPageRegisterMobile").setValue("");
									}
								},{
									id : "orderInfoExportBtn",
									text : '导出',
									handler : me.exportDefault
								}];
						me.listeners = {
								afterRender: function(me, options){
									this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
										enter: function(){
											console.dir(me.down('#orderInfoQuery'));
											me.search(me.down('#orderInfoQuery'));
										}
									});
								}
							};
						me.buttonAlign ='right',

						me.callParent(arguments);
					},
					changeChannelType : function(combo, newValue, oldValue) {
						// 商品编码有变化时,加载尺码和颜色码列表
						var channelInfoCombo = this.up('panel').down('channelinfocombo');
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
						
				//		var channeltypecombo = this.up('panel').down('channeltypecombo'); //渠道类型
						
						if('-1' != newValue && null != newValue){
							//combo.clearValue();
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
						}
					},

					search : function(btn) {
						pageSize = Ext.getCmp('orderInfoPageQueryPageArray').getValue();
						var form = btn.up('form');// 当前按钮form
						var searchParams = getFormParams(form, null);
						var orderInfoGridOrderList = form.up('viewport').down("orderInfoGrid");
						orderInfoGridOrderList.store.on('beforeload', function(store, options) {
							Ext.apply(store.proxy.extraParams, searchParams);
						});
						orderInfoGridOrderList.store.currentPage = 1;// 翻页后重新查询 // 页面重置为1
						orderInfoGridOrderList.store.pageSize = pageSize;
						orderInfoGridOrderList.store.load({
							params : searchParams
						});
					},
					exportDefault :function (btn){
						var thisPage = btn.up("form").up("viewport");
						var myPanel =btn.up("form");
						var searchParams = btn.up("form").getValues();
						myPanel.exportAjax(thisPage,searchParams,"0")
					},
					exportFinancial :function (btn){
						var thisPage = btn.up("form").up("viewport");
						var myPanel = btn.up("form");
						var searchParams = btn.up("form").getValues();
						myPanel.exportAjax(thisPage,searchParams,"1")
					},
					exportLogistics :function (btn){
						var thisPage = btn.up("form").up("viewport");
						var myPanel =btn.up("form");
						var searchParams = btn.up("form").getValues();
						myPanel.exportAjax(thisPage,searchParams,"2")
					},
					exportAjax: function (thisPage,searchParams,exportTemplateType){
						var url = basePath+ '/custom/orderInfo/orderInfoExportCsvFile.spmvc?exportTemplateType='+exportTemplateType;
						Ext.getCmp('orderInfoExportBtn').setDisabled(true);
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
									Ext.getCmp('orderInfoExportBtn').setDisabled(false);
								},
								failure : function(response) {
									myMask.hide();
									Ext.Msg.alert("验证", "失败");
									Ext.getCmp('orderInfoExportBtn').setDisabled(false);
								}
						});
					},
					exportOrdersettle :function (btn){
						
						var thisPage = btn.up("form").up("viewport");
					
						var searchParams = btn.up("form").getValues();
						
						var url = basePath + '/custom/orderInfo/orderInfoOrdersettleExportCsvFile.spmvc?exportTemplateType=1';
						
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
											window.location.href = basePath
													+ "custom/downloadFtpFile.spmvc?path="
													+ path + "&fileName="
													+ fileName;
										} else {
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
});




