Ext.define("MB.view.orderDetail.OrderDetailCenter", {
	extend : "Ext.form.Panel",
	alias : 'widget.orderDetailCenter',
	id : 'orderDetailCenter',
	width: '100%',
	frame: true,
	bodyStyle: {
		padding: '8px'
	},
	autoScroll : true,
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'right',
	},
	bodyBorder:true,
	initComponent: function(){
		var spancss = '<span style="color:black;font-weight:bold";>';
		var tpl = ['交货单号：'+spancss+'{[values.rows[0].get("orderSn") == null? "": values.rows[0].get("orderSn")]}'+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '发货仓：'+'{[values.rows[0].get("deliveryType")=="3"?"<span style=\'color:red;font-weight:bold\'>"+values.rows[0].get("depotCode")+"</span>":"<span style=\'color:black;font-weight:bold\'>"+values.rows[0].get("depotCode")+"</span>"]}'+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '快递单号：'+spancss+'{[values.rows[0].get("invoiceNo") == null? "": values.rows[0].get("invoiceNo")]}'+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '配送方式：'+spancss+'{[values.rows[0].get("shippingName") == null? "": values.rows[0].get("shippingName")]}'+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '配送状态：'+spancss+'{[values.rows[0].get("shippingStatusName") == null? "": values.rows[0].get("shippingStatusName")]}'+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '发货时间：'+spancss+'{[values.rows[0].get("deliveryTime") == null? "": Ext.Date.format(new Date(values.rows[0].get("deliveryTime")),\'Y-m-d H:i:s\')]}'+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
		   		+ '合计金额：'+spancss+'{rows:this.getTotal}',
		   		{
		   			getTotal : function(rows){
		   				var totalPrice = 0.000000;
		   				for(var i= 0;i<rows.length;i++){
		   					totalPrice += rows[i].get("subTotal");
		   				}
		   				return numFixed(totalPrice, 2)+'元';
		   			}
		   		}];
		var me = this;
		me.items = [{//基本信息+收货人信息+其他信息
			xtype : 'tabpanel',
			deferredRender:false,
			enableTabScroll: true, //选项卡过多时，允许滚动
			defaults: { autoScroll: true },
			items : [{
				title : '基本信息',
				closable : false,
				items : [{
					xtype: 'form',
					id:'orderSetModule',
					itemId : 'orderSetModule',
					width: '100%',
					titleCollapse:true,
					defaults: {
						labelWidth: 200,
						columnWidth: 1
					},
					items : [ {
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [
							{xtype : "displayfield", name : 'masterOrderSn', fieldLabel : "主单号", columnWidth: .33 },
							{xtype : "displayfield", name : 'orderOutSn', fieldLabel : "外部交易号", columnWidth: .33},
							{xtype : "displayfield", name : 'ordertotalstatusStr', fieldLabel : "订单状态", columnWidth: .33,
								renderer : function (value) { return "<span style='color:red;'>"+value+"</span>"; } }
						]
					} ,
					{
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [
							{xtype : "displayfield", name : 'channelName', fieldLabel : "订单店铺", columnWidth: .33},
//							{xtype : "displayfield", name : 'clearTime', fieldLabel : "结算时间", columnWidth: .25} ,
							{xtype : "displayfield", name : 'addTime', fieldLabel : "下单时间", columnWidth: .33,
								renderer:function(value){ 
									if(value){
						            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
						             return createTime;  
									}
						         }
							}
							,
							{xtype : "displayfield", name : 'userName', fieldLabel : "购货人", columnWidth: .33}
							/*{xtype : "displayfield", name : 'transType', fieldLabel : "订单交易类型", columnWidth: .25,
								renderer : function (value) {
									if(value==1){
										return "款到发货";
									}else if(value==2){
										return "货到付款";
									}else if(value==3){
										return "担保交易";
									}
								}
							}*/
						]
					} ,
					{
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [
						    {xtype : "displayfield", name : 'warehName', fieldLabel : "订单仓库", columnWidth: .33},
							{xtype : "displayfield", name : 'orderType', fieldLabel : "订单类型", columnWidth: .33,
								renderer : function (value) {
									if(value==0){
										return "正常订单";
									}else if(value==1){
										return "补发订单";
									}else if(value==2){
										return "换货订单";
									}
								}
							},
							{xtype : "displayfield", name : 'prName', fieldLabel : "促销信息", columnWidth: .33}
						]
					} ,
					{
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [
							{xtype : "displayfield", name : 'relatingOriginalSn', fieldLabel : "关联原订单号", columnWidth: .33,
								renderer : function (v) {
									if (v != null && v != '') {
//										var array = v.split('=');
										var url = order_info_url + v
										return '<a href="'+url +'" target="_blank">' + v +'</a>';
									}
									return "";
								}
							},
							/*{xtype : "displayfield", name : 'invoicesOrganization', fieldLabel : "单据组织", columnWidth: .25} ,
							{xtype : "displayfield", name : 'orderCategory', fieldLabel : "订单种类", columnWidth: .25,
								renderer : function (value) {
									if(value==1){
										return "零售";
									}else if(value==2){
										return "物资领用";
									}else if(value==3){
										return "其他出库";
									}else if(value==4){
										return "c2b定制";
									}
								}
							},*/
							//是否为团购订单( 0:否  1;是)
							{xtype : "displayfield", name : 'isGroup', fieldLabel : "团购订单", columnWidth: .33,
								renderer : function (value) { 
									if(value==1){
										return "<span style='color:red;'>是</span>";
									}else{
										return "<span style='color:red;'>否</span>";
									}
								}
							},
							//是否为预售商品(0:否 1:是)
							{xtype : "displayfield", name : 'isAdvance', fieldLabel : "预售订单", columnWidth: .25,
								renderer : function (value) {
									if(value==1){
										return "<span style='color:red;'>是</span>";
									}else{
										return "<span style='color:red;'>否</span>";
									}
								}
							}
						]
					} ,
					{
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [
							//是否为预售商品(0:否 1:是)
							/*{xtype : "displayfield", name : 'isAdvance', fieldLabel : "订单含预售商品", columnWidth: .25,
								renderer : function (value) { 
									if(value==1){
										return "<span style='color:red;'>是</span>";
									}else{
										return "<span style='color:red;'>否</span>";
									}
								}
							},*/
							{xtype : "displayfield", name : 'cancelReason', fieldLabel : "取消订单原因", columnWidth: .33},
							/*{xtype : "displayfield", name : 'expectedShipDate', fieldLabel : "预计发货日", columnWidth: .33},*/
							{xtype : "displayfield", name : 'relatingReturnSn', fieldLabel : "关联退货单号", columnWidth: .33,
								renderer : function (v) {
									if (v != null && v != '') {
										var url = order_return_url + v
										return '<a href="'+url +'" target="_blank">' + v +'</a>';
									}
									return "";
								}
							},
							{xtype : "displayfield", name : 'source', fieldLabel : "下单类型", columnWidth: .25,
								renderer : function (value) {
									if(value==1){
										return "<span style='color:red;'>跨境订单</span>";
									} else if (value==3){
										return "<span style='color:red;'>线上订单</span>";
									} else if (value==4){
										return "<span style='color:red;'>首购赠品订单</span>";
									} else if (value==5){
										return "<span style='color:red;'>一般赠品订单</span>";
									} else if (value==6){
										return "<span style='color:red;'>首购订单</span>";
									} else if (value==7){
										return "<span style='color:red;'>一般订单</span>";
									}
								}
							},
							{xtype : "displayfield", name : 'shippingTotalFee', fieldLabel : "总运费", hidden :true}
						]
					}],
					tools : [ {
						disabled : false,
						type: 'refresh',
						handler: function(){
							Ext.getCmp('orderDetailCenter').initData();
							Ext.msgBox.msg('刷新', "刷新页面成功！", Ext.MessageBox.INFO);
						}
					} ]
				}]
			},{
				title : '收货人信息',
				closable : false,
				items : [{
					xtype:'form',
					id:'shipSetModule',
					itemId : 'shipSetModule',
					width: '100%',
					titleCollapse:true,
					defaults: {
						labelWidth: 200,
						columnWidth: 1
					},
					items: [ {
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [
							{xtype : "displayfield", name : 'consignee', fieldLabel : "收货人", columnWidth: .33 },
							{xtype : "displayfield", name : 'encodeMobile', fieldLabel : "手&nbsp;&nbsp;&nbsp;&nbsp;机", columnWidth: .33, value: '******'} ,
							{xtype : "hidden", name : 'mobile', fieldLabel : "手机"} ,
							{xtype : "displayfield", name : 'encodeTel', fieldLabel : "电&nbsp;&nbsp;&nbsp;&nbsp;话", columnWidth: .28, value: '******' },
							{xtype : "hidden", name : 'tel', fieldLabel : "电话" }
							/*,
							{
								itemId : 'decodeLinkMobile',
								xtype : 'button',
								text : '解密',
								columnWidth: .05,
								handler : me.decodeLinkMobile
							}*/
						]
					} , {
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [
						    {xtype : "displayfield", name : 'zipcode', fieldLabel : "邮&nbsp;&nbsp;&nbsp;&nbsp;编", columnWidth: .33 },
						    {
						    	xtype : "displayfield",
								name : 'email',
								fieldLabel : "电子邮件",
								columnWidth: .33 
						    },
						    {
						    	xtype : "displayfield",
								name : 'wayPaymentFreight',
								fieldLabel : "运费付款方式",
								renderer : function (value, meta, record) {
									var returnValue = '';
									if (value=='01') {
										returnValue = '到付';
									} else if (value=='02') {
										returnValue = '自付';
									}
									return returnValue;
								},
								columnWidth: .33
						    }
						]
					} , {
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [
							{xtype : "hidden", name : 'country', fieldLabel : "国家"},
							{xtype : "hidden", name : 'province', fieldLabel : "省" },
							{xtype : "hidden", name : 'city', fieldLabel : "市" },
							{xtype : "hidden", name : 'district', fieldLabel : "区" },
							{xtype : "hidden", name : 'street', fieldLabel : "街道" },
							{xtype : "hidden", name : 'address', fieldLabel : "地址" },
							{xtype : "displayfield", name : 'fullAddress', fieldLabel : "详细地址",columnWidth: 1}
						]
					} , {
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [{
							xtype : "displayfield",
							name : 'signBuilding',
							fieldLabel : "标志性建筑",
							columnWidth: .33
						},
						{xtype : "displayfield", name : 'userCardNo', fieldLabel : "身份证号码", columnWidth: .33 },
						{xtype : "displayfield", name : 'userCardName', fieldLabel : "身份证姓名", columnWidth: .33 },
						]
					} , {
						xtype: 'fieldcontainer',
						layout: 'column',
						items: [{
							xtype : "displayfield",
							name : 'bestTime',
							fieldLabel : "最佳送货时间",
							columnWidth: 1
						}]
					}],
					tools : [ {
						type: 'gear',
						tooltip : '修改收货人信息',
						disabled : true,
						action: 'shipEdit',
						scope: me
					} ]
				}]
			},{
				title : '其他信息',
				closable : false,
				items : [{
					xtype : 'form',
					id: 'otherSetModule',
					itemId: 'otherSetModule',
					titleCollapse:true,
					defaults: {
						xtype: 'fieldcontainer',
						layout: 'column',
						labelWidth: 200,
						columnWidth: 1
					},
					items : [ {
						items: [
							{xtype : "displayfield", name : 'invType', fieldLabel : "发票类型", columnWidth: .5},
							{xtype : "displayfield", name : 'invContent', fieldLabel : "发票内容", columnWidth: .5 }
						]
					} , {
						items: [
								{xtype : "displayfield", name : 'invPayee', fieldLabel : "发票抬头", columnWidth: 1 }
						]
					} , {
						items: [
							{xtype : "displayfield", name : 'postscript', fieldLabel : "客户留言", columnWidth: 1 }
						]
					} , {
						items: [
							{xtype : "displayfield", name : 'toBuyer', fieldLabel : "商家留言", columnWidth: 1 }
						]
					}, {
						items:  [ {
							xtype : "displayfield",
							name : 'howOos',
							fieldLabel : "缺货处理",
							value: '',
							columnWidth: 1
						} ]
					} ],
					tools : [ {
						type: 'gear',
						tooltip : '修改其他信息',
						action: 'otherEdit',
						disabled : true,
						scope: me
					} ]
				}]
			}]
		},{//主单商品信息
			titleCollapse:true,
			xtype : 'form',
			id:'goodSetModule',
			itemId: 'goodSetModule',
			title:'主单商品信息',
			width: '100%',
			items:[ {
				xtype : 'grid',
				store: Ext.create('Ext.data.Store', {
					model: "Ext.data.Model"
				}),
				autoRender : true,
				columnLines : true,
				loadMask : true, // 读取数据时的遮罩和提示功能即加载loding...
				resizable : true,
				viewConfig:{
					forceFit: true, // 注意不要用autoFill:true,那样设置的话当GridPanel的大小变化（比如你resize了它）时不会自动调整column的宽度
					scrollOffset: 0, //不加这个的话，会在grid的最右边有个空白，留作滚动条的位置
					enableTextSelection : true
				},
				//双击编辑商品属性
				columns : {
					defaults: {
						align: 'center',
						sortable:false,
						menuDisabled : true
					},
					items: [
					    { text:'序号', xtype: 'rownumberer', width : 30 },
						{ text : '商品名称', dataIndex : 'goodsName', align : 'left'},
						{ text : '商品属性', width : 75, dataIndex : 'extensionCode', 
							renderer : function (value, meta, record) {
								if (value == null || value== '' || value == 'common') {
									return '普通商品';
								} else if (value == 'gift') {
									return '赠品';
								} else if (value == 'c2b') {
									return 'C2B定制';
								} else if (value == 'group') {
									return '套装';
								} else if (value == 'prize') {
									return '奖品';
								}else if (value == 'c2m') {
									return 'C2M定制';
								}
							}
						},
						{ text : '货号', width : 75, dataIndex : 'goodsSn'},
						{
							header : '规格',
							columns : [
								{ header : "颜色", width : 80, dataIndex : 'goodsColorName', locked:true,sortable:false, menuDisabled : true},
								{ header : "尺寸", width : 80, dataIndex : 'goodsSizeName', locked:true,sortable:false, menuDisabled : true},
							]
						},
//						{ text : '产品条形码',width : 150, dataIndex : 'barcode' },
						{ text : '企业SKU码',width : 150, dataIndex : 'customCode'},
//						{ text : 'sap',width : 100, dataIndex : 'sap'},
						{ text : '商品价格', width : 75, dataIndex : 'goodsPrice' },
						{ text : '数量', width : 85, dataIndex : 'goodsNumber', 
							renderer : function(value, meta, record) {
								var returnNum = record.get('returnNum');
								var returnRemainNum = record.get('returnRemainNum');
								var lackNum = record.get('lackNum');
								var returnmsg = value;
								if (lackNum && lackNum > 0) {
									returnmsg += '</br><font style="color:red">（缺货 ' + lackNum + '）</font>'
								}
								if (returnRemainNum && returnRemainNum > 0) {
									returnmsg += '</br><font style="color:red">（待退 ' + returnRemainNum + '）</font>'
								}
								if (returnNum && returnNum > 0) {
									returnmsg += '</br><font style="color:red">（已退 ' + returnNum + '）</font>'
								}
								return returnmsg;
							}
						},
						{ text : '成交价格', width : 75, dataIndex : 'transactionPrice' },
						{ text : '财务价格', width : 75, dataIndex : 'settlementPrice' },
//						{ text : '税费', width : 75, dataIndex : 'tax' },
//						{ text : '税率%', width : 75, dataIndex : 'taxRate' },
						{ text : '分摊金额', width : 75, dataIndex : 'shareBonus' },
//						{ text : '积分金额', width : 75, dataIndex : 'integralMoney' },
//						{ text : '赠送美力值', width : 75, dataIndex : 'bvValue' },
//						{ text : '预计发货日', width : 90, dataIndex : 'expectedShipDate' },
						{ text : '商品促销', width : 90,dataIndex : 'promotionDesc' },
						/*{ text : '打折券', width : 150,dataIndex : 'useCard',
							renderer : function (value, meta, record) {
								//获取打折券详情列表
								var couponList = record.get('couponList');
								//拼装返回链接
								var returnValue = '';
								if (value&&value!=null&&value!='') {
									var array = value.split(":");
									for (var i=0 ; i< array.length ; i++)
									{
										var flag = '0';//用于标记本轮循环中  打折券卡号是否匹配到对应的详细信息  如果没有匹配到  就显示无链接的卡号[折扣未知]
										if(couponList&&couponList!=null){
											for(var j=0 ; j<couponList.length ; j++){
												if(couponList[j].cardNo==array[i]){
													returnValue = returnValue + '<a href="javascript:void(0);" onclick="showCouponInfo(\''+array[i]
																	+'\',\''+couponList[j].cardLn+'\',\''+couponList[j].cardMoney+'\',\''
																	+couponList[j].status+'\',\''+couponList[j].effectDateStr+'\',\''+couponList[j].expireTimeStr+'\');">' 
																	+ array[i] +'<font style="color:red">['+couponList[j].cardMoney+ '折券]</font></a></br>';
													flag = '1';
												}
											}
										}
										if(flag=='0'){
											returnValue = returnValue + array[i] + '[折扣未知]</br>';
										}
									} 
								}
								return returnValue;
							} 
						},*/
						{ text : '折让金额', width : 75, dataIndex : 'discount' },
						/*{ header : '可用数量',
							columns : [
										{ header: '数量', width : 50, dataIndex : 'availableNumber', locked:true,sortable:false, menuDisabled : true},
										{ header: '操作',xtype: 'widgetcolumn', width: clientWidth*0.04, widget: { xtype: 'button', text: '查', handler: me.queryStock } },
									]
						},*/
						{ text : '小计', width : 75, dataIndex : 'subTotal'}
					]
				},
				bbar : [ {
					xtype : 'component',
					itemId : 'subTotal',
					margin : '0 0 0 20'
				} ]
			}, {
				xtype:'label',
				itemId : 'total',
				height : 15,
				columnWidth: 1,
				style:"float: right;color:black;font-weight:bold"
			}]
			/*tools : [ {
				disabled : false,
				type: 'gear',
				tooltip : '修改主单商品信息',
				action: 'editMasterOrderGoods',
				scope: me
			} ]*/
		},{
			xtype : 'form',
			title:'交货单信息',
			id:'deliveryOrderToolForm',
			width: '100%',
			tools : [ {
				disabled : true,
				type: 'gear',
				tooltip : '修改交货单信息',
				action: 'editDistributeOrderGoods'
			}]
		},{//子单信息/交货单title信息
			xtype : 'tabpanel',
			id : 'deliveryOrderTabPanel',
			itemId : 'deliveryOrderTabPanel',
			enableTabScroll:true,         //tab标签过宽时自动显示滚动条
			deferredRender:false,
			listeners : {
				//监听  切换面板时重新给distributeOrderGoddsDetail面板即交货单商品、按钮、问题单类型、交货单配送信息赋值
				tabchange:function(tabPanel, newCard, oldCard, eOpts){
					var neworderSn = newCard.id;
					Ext.each(sonOrderListData,function(sonOrderMap){
						var sonOrder = sonOrderMap.sonOrder;//子单信息
						var orderSn = sonOrder.orderSn;//子单号
						if(orderSn==neworderSn){
							//控件
							var distributeOrderGoddsDetail = Ext.getCmp('distributeOrderGoddsDetail');
							var distributeOrderGoodsGrid = distributeOrderGoddsDetail.down('#distributeOrderGoodsGrid');//交货单商品列表
							var relatingExOrRetOrdSns = distributeOrderGoddsDetail.down('#relatingExOrRetOrdSns');//交货单关联的换货单或退单
							var distributeOrderQuesReason = distributeOrderGoddsDetail.down('#distributeOrderQuesReason');//交货单问题单类型
							var distributeOrderDepotShip = distributeOrderGoddsDetail.down('#distributeOrderDepotShip');//交货单配送信息
							var distributeForm = distributeOrderGoddsDetail.down('form');
							//数据
							var sonOrderGoodsList = sonOrderMap.sonOrderGoodsList;//子单商品列表
							var distributeOrderStatusUtils = sonOrderMap.distributeOrderStatusUtils;//子单按钮状态
							var sonOrderDepotShipList = sonOrderMap.sonOrderDepotShipList;//子单配送信息列表
							var subOrderQuesReason = sonOrderMap.questionReason//问题单原因
							var relatingExOrRetOrdSnsData = sonOrderMap.relatingExOrRetOrdSns;//交货单关联的换货单或退单
							//给交货单商品列表赋值
							distributeOrderGoodsGrid.store.loadData(sonOrderGoodsList);
							//交货单按钮是否可用
							if(distributeOrderStatusUtils['good']==1){
								Ext.getCmp('deliveryOrderToolForm').tools[0].enable();
							}else{
								Ext.getCmp('deliveryOrderToolForm').tools[0].disable();
							}
							var distributeButtons = distributeForm.down('#sonOrderButtons').items.items;
							distributeButtons.forEach(function(item,index){
								var value = item.value;
								var itemId = item.itemId;
								var distributeStatus = distributeOrderStatusUtils[value];
								if(itemId=='confirmBtn'||itemId=='setQuesBtn'){
									var buttonMenu = distributeOrderStatusUtils[itemId];
									var distributeStatus = distributeOrderStatusUtils[buttonMenu.action];
									item.value = buttonMenu.action;
									item.setText('<font style="color:white">'+buttonMenu.name+'</font>');
									item.setTooltip(buttonMenu.name);
									if (distributeStatus == 1) {
										item.enable();
									} else {
										item.disable();
									}
								}else if(distributeStatus==1){
									item.enable();
								}else{
									item.disable();
								}
							});
							//交货单关联的换货单
							if(relatingExOrRetOrdSnsData&&relatingExOrRetOrdSnsData!=null&&relatingExOrRetOrdSnsData!=''){
								relatingExOrRetOrdSns.removeAll();
								Ext.each(relatingExOrRetOrdSnsData,function(reason){
									var typeAndOrderSn = reason.split('|');
									if(typeAndOrderSn[0]=='1'){//换单
										relatingExOrRetOrdSns.add({
											text:'<font style="color:green">关联换单：'+typeAndOrderSn[1]+'</font>',
											handler: function(){
												window.open(order_info_url+typeAndOrderSn[1]); 
											}
										});
									}else if(typeAndOrderSn[0]=='2'){//退单
										relatingExOrRetOrdSns.add({
											text:'<font style="color:blue">关联'+typeAndOrderSn[2]+'：'+typeAndOrderSn[1]+'</font>',
											handler: function(){
												window.open(order_return_url+typeAndOrderSn[1]); 
											}
										});
									}
								});
							}else{
								relatingExOrRetOrdSns.removeAll();
							}
							//问题单原因
							if(subOrderQuesReason&&subOrderQuesReason!=null&&subOrderQuesReason!=''){
								distributeOrderQuesReason.removeAll();
								Ext.each(subOrderQuesReason,function(reason){
									distributeOrderQuesReason.add({
										text:'<font style="color:red">'+reason+'</font>'
									});
								});
							}else{
								distributeOrderQuesReason.removeAll();
							}
							//交货单配送信息
							distributeOrderDepotShip.store.loadData(sonOrderDepotShipList);
						}
					});//遍历
				}//tabchange
			}//listeners
		},{//交货单&配送详细信息
			xtype : 'form',
			id : 'distributeOrderGoddsDetail',
			items : [{
				titleCollapse:true,
				xtype : 'form',
				width: '100%',
				items:[ {
					xtype : 'grid',
					itemId : 'distributeOrderGoodsGrid',
					store: Ext.create('Ext.data.Store', {
						model: "Ext.data.Model",
						groupField:'depotCode'
					}),
					autoRender : true,
					columnLines : true,
					loadMask : true, // 读取数据时的遮罩和提示功能即加载loding...
					resizable : true,
					viewConfig:{
						forceFit: true, // 注意不要用autoFill:true,那样设置的话当GridPanel的大小变化（比如你resize了它）时不会自动调整column的宽度
						scrollOffset: 0, //不加这个的话，会在grid的最右边有个空白，留作滚动条的位置
						enableTextSelection : true
					},
					features : [ {
						ftype : 'grouping',
						groupHeaderTpl : tpl,
						hideGroupedHeader : true,
						enableGroupingMenu : false
					} ],
					columns : {
						defaults: {
							align: 'center',
							sortable:false,
							menuDisabled : true
						},
						items: [
						    { text:'序号', xtype: 'rownumberer',  width: clientWidth*0.03 },
							{ text : '商品名称', width: clientWidth*0.1,dataIndex : 'goodsName', align : 'left'},
							{ text : '商品属性', width: clientWidth*0.08, dataIndex : 'extensionCode', 
								renderer : function (value, meta, record) {
									if (value == null || value== '' || value == 'common') {
										return '普通商品';
									} else if (value == 'gift') {
										return '赠品';
									} else if (value == 'c2b') {
										return 'C2B定制';
									} else if (value == 'group') {
										return '套装';
									} else if (value == 'prize') {
										return '奖品';
									}else if (value == 'c2m') {
										return 'C2M定制';
									}
								}
							},
							{ text : '货号',width: clientWidth*0.08, dataIndex : 'goodsSn'},
							{
								header : '规格',
								columns : [
									{ header : "颜色",width: clientWidth*0.07, dataIndex : 'goodsColorName', locked:true,sortable:false, menuDisabled : true},
									{ header : "尺寸",width: clientWidth*0.07, dataIndex : 'goodsSizeName', locked:true,sortable:false, menuDisabled : true},
								]
							},
							{ text : '数量', width : clientWidth*0.07, dataIndex : 'goodsNumber', 
								renderer : function(value, meta, record) {
									var returnNum = record.get('returnNum');
									var returnRemainNum = record.get('returnRemainNum');
									var lackNum = record.get('lackNum');
									var returnmsg = value;
									if (lackNum && lackNum > 0) {
										returnmsg += '</br><font style="color:red">（缺货 ' + lackNum + '）</font>'
									}
									if (returnRemainNum && returnRemainNum > 0) {
										returnmsg += '</br><font style="color:red">（待退 ' + returnRemainNum + '）</font>'
									}
									if (returnNum && returnNum > 0) {
										returnmsg += '</br><font style="color:red">（已退 ' + returnNum + '）</font>'
									}
									return returnmsg;
								}
								
							},
//							{ text : '产品条形码',width: clientWidth*0.1, dataIndex : 'barcode' },
							{ text : '企业SKU码',width: clientWidth*0.1, dataIndex : 'customCode'},
							{ text : 'sap',width: clientWidth*0.08, dataIndex : 'sap'},
//							{ text : '供应商',width: clientWidth*0.1, dataIndex : 'supplierCode', 
//								renderer : function(value, meta, record) {
//									var salesMode = record.get('salesMode');
//									var msg = value;
//									if (salesMode && salesMode=='1' ) {
//										msg = '<font style="color:green">' + value + '</font>'
//									} else {
//										msg = '<font style="color:red">' + value + '</font>'
//									}
//									return msg;
//								}
//							},
							{ text : '财务价格',width: clientWidth*0.07, dataIndex : 'settlementPrice' },
							{ text : '商品促销',width: clientWidth*0.22, dataIndex : 'promotionDesc' }
						]
					}
				},{
					xtype : 'toolbar',
					itemId : 'relatingExOrRetOrdSns',
				},{
					xtype : 'toolbar',
					itemId : 'distributeOrderQuesReason',
				}],
				dockedItems : [{
					xtype : 'toolbar',
					itemId : 'sonOrderButtons',
					dock : 'bottom',
					items : [
//						{ itemId : 'confirmBtn', text : '<font style="color:white">交货单确认</font>', tooltip : '确认', disabled : true, value : 'sonConfirm', action : 'sonOrderOperation',style:'background-color:#1874CD;'},
//						{ itemId : 'cancelBtn', text : '<font style="color:white">交货单取消</font>', tooltip : '取消', disabled : true, value : 'sonCancel', action : 'sonOrderOperation',style:'background-color:#1874CD;'},
//						{ itemId : 'setQuesBtn', text : '<font style="color:white">交货单设问题单</font>', tooltip : '设问题单', disabled : true, value : 'sonQuestion', action : 'sonOrderOperation',style:'background-color:#1874CD;'},
//						{ itemId : 'swdiBtn', text : '<font style="color:white">交货单SWDI</font>', tooltip : '同步分仓发货信息', disabled : true, value : 'sonSwdi', action : 'sonOrderOperation',style:'background-color:#1874CD;'},
//						{ itemId : 'addrefuseBtn', text : '<font style="color:white">生成拒收入库单</font>', tooltip : '生成拒收入库单', disabled : true, value : 'sonAddrefuse', action : 'sonOrderOperation',style:'background-color:#1874CD;'},
						{ itemId : 'addReturnBtn', text : '<font style="color:white">生成退货单</font>', tooltip : '生成退货单', disabled : true, value : 'sonAddReturn', action : 'sonOrderOperation',style:'background-color:#1874CD;'},
//						{ itemId : 'addExtraBtn', text : '<font style="color:white">生成额外退款单</font>', tooltip : '额外退款单', disabled : true, value : 'sonAddExtra', action : 'sonOrderOperation',style:'background-color:#1874CD;'},
//						{ itemId : 'lostReturnBtn', text : '<font style="color:white">生成失货退货单</font>', tooltip : '生成失货退货单', disabled : true, value : 'sonAddLostReturn', action : 'sonOrderOperation',style:'background-color:#1874CD;'},
						{ itemId : 'sonAddExchangBtn', text : '<font style="color:white">生成换货单</font>', tooltip : '生成换货单', disabled : true, hidden :true, value : 'sonAddExchang', action : 'sonOrderOperation',style:'background-color:#1874CD;'}
					]
				}]
			},{
				xtype: 'gridpanel',
				itemId : 'distributeOrderDepotShip',
				title: '所选交货单配送信息',
				width: '100%',
				store: Ext.create('Ext.data.Store', {
					model: "Ext.data.Model"
				}),
				viewConfig:{
					enableTextSelection : true
				},
				autoRender:true,
				columnLines: true,
				loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
				resizable: true,
				forceFit: true,
				titleCollapse:true,
				columns : {
					items:[
							{ text: '交货单号', width: clientWidth*0.15, dataIndex: 'orderSn'},
//							{ text: '供应商', width: clientWidth*0.12, dataIndex: 'supplierCode'},
							{ text: '仓库', width: clientWidth*0.15, dataIndex: 'depotCode'},
							{ text: '承运商', width: clientWidth*0.18, dataIndex: 'shippingName'},
							{ text: '快递单号', width: clientWidth*0.15, dataIndex: 'invoiceNo'},
							{ text: '商品数量', width: clientWidth*0.1, dataIndex: 'goodsNumber'},
							{ text: '配送方式', width: clientWidth*0.15, dataIndex: 'deliveryType',hidden : true},
							{ text: '承运商id', width: clientWidth*0.15, dataIndex: 'shippingId',hidden : true},
							{ xtype: 'widgetcolumn', width: clientWidth*0.15, widget: { xtype: 'button', text: '物流状态查询', handler: me.queryExpress } }
					],
					defaults: {
						align: 'center',
						sortable:false,
						menuDisabled : true
					}
				}
			}]	
		},{//付款信息
			titleCollapse:true,
			xtype : 'form',
			id:'paySetModule',
			itemId:'paySetModule',
			title:'付款信息',
			width: '100%',
			items: [ {
				xtype: 'gridpanel',
				store: Ext.create('Ext.data.Store', {
					model: "Ext.data.Model"
				}),
				columnWidth: 1,
				autoRender:true,
				columnLines: true,
				width: '100%',
				loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
				resizable: true,
				viewConfig:{
					forceFit: true,
					scrollOffset: 0, //不加这个的话，会在grid的最右边有个空白，留作滚动条的位置
					enableTextSelection : true
				},
				columns : {
					defaults: {
						align: 'center',
						sortable:false,
						menuDisabled : true
					},
					items:[
					    { text: '付款单编号',width: clientWidth*0.12, dataIndex: 'masterPaySn'},
					   /* { text: '合并付款编号',width: clientWidth*0.12, dataIndex: 'mergePaySn'},*/
						{ text: '支付方式',width: clientWidth*0.1, dataIndex: 'payName'},
						{ text: '付款方式ID',dataIndex: 'payId',hidden: true},
						{ text: '付款总金额',width: clientWidth*0.1, dataIndex: 'payTotalfee'},
						{ text: '付款备注', width: clientWidth*0.1, dataIndex: 'payNote'},
						{ text: '订单支付时间', width: clientWidth*0.12, dataIndex: 'payTime'},
						{ text: '付款单生成时间',width: clientWidth*0.12, dataIndex: 'createTime'},
						{ text: '付款最后期限', width: clientWidth*0.12, dataIndex: 'payLasttime'},
						{ text: '付款状态',width: clientWidth*0.08, dataIndex: 'payStatus',
							renderer: function(value, metaData, record, rowIndex) {
								var returnValue = '';
								if(value=='0'){
									returnValue = '未付款';
								}else if(value=='1'){
									returnValue = '部分付款';
								}else if(value=='2'){
									returnValue = '已付款';
								}else if(value=='4'){
									returnValue = '待确认';
								}
								return returnValue;
							}
						},
						{ text: '操作',width: clientWidth*0.11,dataIndex: 'payStatus', 
							renderer: function(value, metaData, record, rowIndex) {
								var id = Ext.id();
								var payId = record.get('payId');
								var pay = record.get('pay');
								var unpay = record.get('unpay');
								var payStatus = record.get('payStatus');
								var editPay = true;
								var editUnpay = true;
								if (pay == 1) {
									editPay = false;
								}
								if (unpay == 1) {
									editUnpay = false;
								}
								//如果支付方式是 退单转入款  就不允许使用已付款按钮
								if(payId=='26'||payStatus=='2'||payStatus=='3'){
									editPay = true;
								}
								var paygrid = this;
								// 支付状态和权限
								setTimeout(function() {
									var panel = Ext.create('Ext.panel.Panel', {
										bodyPadding: 0,
										border:false,
										baseCls: 'my-panel-no-border',
										height: 20,
										layout: 'column',
										columnWidth : 1,
										items: [{
											columnWidth: 1,
											xtype: 'segmentedbutton',
											allowToggle: false,
											items: [{
												text: '已付款',
												disabled: editPay,
												handler: function () {
													me.doPay(rowIndex, paygrid);
												}
											},{
												text: '未付款',
												disabled: editUnpay,
												handler: function () {
													me.doUnPay(rowIndex, paygrid);
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
						}]
				}
			}, {
				xtype: 'fieldcontainer',
				itemId : 'showTotalFee',
				layout: 'column',
				margin : '10 0 0 0',
				columnWidth: 1
			} , {
				xtype: 'fieldcontainer',
				itemId : 'totalFee',
				layout: 'column',
				margin : '10 0 0 0',
				columnWidth: 1
			} , {
				xtype: 'fieldcontainer',
				itemId : 'showTotalPayable',
				layout: 'column',
				margin : '10 0 0 0',
				columnWidth: 1,
			} , {
				xtype: 'fieldcontainer',
				itemId : 'totalPayable',
				layout: 'column',
				margin : '10 0 0 0',
				columnWidth: 1,
			} , {
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: 1,
				labelWidth: 200,
				items: [
					{xtype : "displayfield",  columnWidth: 1 }
				]
			} , {
				// 退单相关信息
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				itemId : 'returnSettPrice',
				columnWidth: 1,
				labelWidth: 200
			} ],
			/*tools : [{
				type: 'gear',
				disabled : true,
				tooltip : '修改费用信息',
				action: 'payEdit',
				scope: me
			}]*/
		}];
		this.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "masterOrderInfo",
			model : 'Ext.data.Model'
		}),
		this.callParent(arguments);
	},
	initData : function(){
		var me = this;
		me.load({
			url : basePath + '/custom/orderInfo/getOrderDetail',
			params : {
				"masterOrderSn" : masterOrderSn,
				"isHistory": isHistory
			},
			async:true,
			timeout: 100000,
			success : function(form,action) {
				//获取订单数据
				var rawDatas = form.reader.rawData;
				var masterOrderInfoData = rawDatas.masterOrderInfo;
				var mergedMasOrdGoodsDetailListData = rawDatas.mergedMasOrdGoodsDetailList;
				sonOrderListData = rawDatas.sonOrderList;//数据存入全局变量
				var masterOrderPayTypeDetailListData = rawDatas.masterOrderPayTypeDetailList;
				var returnSettleMoney = rawDatas.returnSettleMoney;
				var orderStatusUtils = rawDatas.orderStatusUtils;
				//获取待赋值组件
				var orderSetModule = Ext.getCmp('orderSetModule').getForm();
				
				var shipSetModuleCmp = Ext.getCmp('shipSetModule');
				shipSetModule = shipSetModuleCmp.getForm();
				
				var otherSetModuleCmp = Ext.getCmp('otherSetModule');
				otherSetModule = otherSetModuleCmp.getForm();
				
				var goodSetModuleCmp = Ext.getCmp('goodSetModule');
				goodSetModule = goodSetModuleCmp.down('grid');
				
				var deliveryOrderTabPanel = Ext.getCmp('deliveryOrderTabPanel');
				var distributeOrderGoddsDetail = Ext.getCmp('distributeOrderGoddsDetail');
				
				var paySetModuleCmp = Ext.getCmp('paySetModule');
				paySetModule = paySetModuleCmp.down('gridpanel');
				
				var orderDetailSouth = Ext.getCmp('orderDetailSouth');
				orderDetailSouth.collapse();
				//给全局变量赋值
				channelCode = masterOrderInfoData.orderFrom;
				siteCode = masterOrderInfoData.siteCode;
				depotCode = masterOrderInfoData.depotCode;
				orderType = masterOrderInfoData.orderType;
				relatingReturnSn = masterOrderInfoData.relatingReturnSn;
				//给基本信息赋值
				orderSetModule.findField('ordertotalstatusStr').setValue(getCombineStatus(masterOrderInfoData.orderStatus,masterOrderInfoData.payStatus,masterOrderInfoData.shipStatus));//订单状态
				shipSetModule.findField('encodeMobile').setValue(masterOrderInfoData.mobile);
				shipSetModule.findField('encodeTel').setValue(masterOrderInfoData.tel);
				
				//异步加载商品数据
				setTimeout(function(){
					goodSetModule.store.loadData(mergedMasOrdGoodsDetailListData, false);
					var goodsTotal = 0.000000;
					goodSetModule.getStore().each(function(record) {
						goodsTotal += record.get("subTotal");
					});
					goodSetModuleCmp.down("#total").update('总计：' + numFixed(goodsTotal, 2) +' 元');
				}, 1);
				
				//设置问题单浮标
				var questionStatus=masterOrderInfoData.questionStatus;
				if (masterOrderInfoData.logqDesc != null && masterOrderInfoData.logqDesc != '') {
					var questionType = eval(masterOrderInfoData.logqDesc);
					var questionArr = new Array();
					questionArr.push('<font style="color:red">问题单</font>');
					questionArr.push({ xtype: 'tbspacer' });
					questionType.forEach(function (obj, index) {
						questionArr.push({ text:'<font style="color:red">'+obj+'</font>', margins: '50 50 1 5'});
					});
					form.owner.toolbar = Ext.widget({
						xtype: 'toolbar',
						rtl: false,
						id: 'orderToolbar',
						floating: true,
						fixed: true,
						preventFocusOnActivate: true,
						draggable: {
							constrain: true
						},
						constraintInsets: '0 -' + (Ext.getScrollbarSize().width + 5) + ' 0 0'
					});
					
					form.owner.toolbar.add(questionArr); 
					form.owner.toolbar.show();
					form.owner.toolbar.anchorTo(
						document.body,
						Ext.optionsToolbarAlign || 'tr-tr',
						[-(Ext.getScrollbarSize().width + 5), 50],
						true,
						true
					);
				} else {
					if (form.owner.toolbar) {
						form.owner.toolbar.hide();
					}
				}		
				
				//交货单/子单赋值  
				splitStatus = masterOrderInfoData.splitStatus;
				if(splitStatus == '0' || splitStatus == '1'){//未拆单|拆单中  不显示交货单
					Ext.getCmp('deliveryOrderToolForm').hide();
					deliveryOrderTabPanel.hide();
					distributeOrderGoddsDetail.hide();
				}else{//已拆单|重新拆单  显示交货单
					//获取交货单tabpanel上已存在的交货单id
					var Ids = '';
					deliveryOrderTabPanel.items.each(function(item){
						Ids = Ids+item.id+',';
	                });
					//异步加载交货单数据
					setTimeout(function(){
						//遍历交货单列表数据
						var i = 1;
						Ext.each(sonOrderListData,function(sonOrderMap){
							var sonOrder = sonOrderMap.sonOrder;//子单信息
							var orderSn = sonOrder.orderSn;//子单号
							var sonOrderGoodsList = sonOrderMap.sonOrderGoodsList;//子单商品列表
							var orderStatusDesc = me.getDistributeOrderStatusDesc(sonOrder.orderStatus);
							var shipStatusDesc = me.getDistributeOrderShipStatusDesc(sonOrder.shipStatus);
							var distributionStatusDesc = me.getDistributionStatusDesc(sonOrder.depotStatus);//分配状态
							var relatingExOrRetOrdSnsData = sonOrderMap.relatingExOrRetOrdSns;//交货单关联退单或换单
							var relatingExOrRetOrdSns = me.down('#relatingExOrRetOrdSns');
							//供应商
							var supplierCode = '无商品';
							if(sonOrderGoodsList.length > 0){
								supplierCode = sonOrder.supplierCode;//供应商
							}
							var questonFlag = '';
							//子单是否为问题单
							if(sonOrder.questionStatus == '1'){
								questonFlag = '<span style="color:red;font-weight:bold";>(问)</span>';
							}
							var newTitle = questonFlag+'交货单'+i+'('+supplierCode+')'+'&nbsp;'+orderStatusDesc+'|'+distributionStatusDesc+'|'+shipStatusDesc;
							
							if(!(Ids.indexOf(orderSn+',')<0)){//已有该交货单
								var oldTitle = deliveryOrderTabPanel.down('#'+orderSn).title;
								if(oldTitle!=newTitle){
									deliveryOrderTabPanel.down('#'+orderSn).setTitle(newTitle);
								}
							}else{//没有该交货单  就新增该交货单title
								deliveryOrderTabPanel.add({
									id:orderSn,
									title:newTitle
								});
							}
							if(i==1){//给第一个交货单加载数
								//控件
								var distributeOrderGoodsGrid = distributeOrderGoddsDetail.down('#distributeOrderGoodsGrid');//交货单商品雷彪
								var distributeOrderQuesReason = distributeOrderGoddsDetail.down('#distributeOrderQuesReason');//交货单问题单类型
								var distributeOrderDepotShip = distributeOrderGoddsDetail.down('#distributeOrderDepotShip');//交货单配送信息
								var distributeForm = distributeOrderGoddsDetail.down('form');
								//数据
								var distributeOrderStatusUtils = sonOrderMap.distributeOrderStatusUtils;//子单按钮状态
								var sonOrderDepotShipList = sonOrderMap.sonOrderDepotShipList;//子单配送信息列表
								var subOrderQuesReason = sonOrderMap.questionReason;//问题单原因
								//给交货单商品列表赋值
								distributeOrderGoodsGrid.store.loadData(sonOrderGoodsList);
								//交货单按钮是否可用
								if(distributeOrderStatusUtils['good']==1){
									Ext.getCmp('deliveryOrderToolForm').tools[0].enable();
								}else{
									Ext.getCmp('deliveryOrderToolForm').tools[0].disable();
								}
								var distributeButtons = distributeForm.down('#sonOrderButtons').items.items;
								distributeButtons.forEach(function(item,index){
									var value = item.value;
									var itemId = item.itemId;
									if (itemId == 'sonAddExchangBtn' && siteCode == 'Chlitina') {
										item.show();
									}
									if(itemId == 'confirmBtn' || itemId == 'setQuesBtn'){
										var buttonMenu = distributeOrderStatusUtils[itemId];
										item.value = buttonMenu.action;
										item.setText('<font style="color:white">'+buttonMenu.name+'</font>');
										item.setTooltip(buttonMenu.name);
										
										if (distributeOrderStatusUtils[buttonMenu.action] == 1) {
											item.enable();
										} else {
											item.disable();
										}
										
									}else if(distributeOrderStatusUtils[value] == 1){
										item.enable();
									}else{
										item.disable();
									}
								});
								//交货单关联的换货单
								if(relatingExOrRetOrdSnsData&&relatingExOrRetOrdSnsData!=null&&relatingExOrRetOrdSnsData!=''){
									relatingExOrRetOrdSns.removeAll();
									Ext.each(relatingExOrRetOrdSnsData,function(reason){
										var typeAndOrderSn = reason.split('|');
										if(typeAndOrderSn[0]=='1'){//换单
											relatingExOrRetOrdSns.add({
												text:'<font style="color:green">关联换单：'+typeAndOrderSn[1]+'</font>',
												handler: function(){
													window.open(order_info_url+typeAndOrderSn[1]); 
												}
											});
										}else if(typeAndOrderSn[0]=='2'){//退单
											relatingExOrRetOrdSns.add({
												text:'<font style="color:blue">关联'+typeAndOrderSn[2]+'：'+typeAndOrderSn[1]+'</font>',
												handler: function(){
													window.open(order_return_url+typeAndOrderSn[1]); 
												}
											});
										}
									});
								}else{
									relatingExOrRetOrdSns.removeAll();
								}
								//问题单原因
								if(subOrderQuesReason&&subOrderQuesReason!=null&&subOrderQuesReason!=''){
									distributeOrderQuesReason.removeAll();
									Ext.each(subOrderQuesReason,function(reason){
										distributeOrderQuesReason.add({
											text:'<font style="color:red">'+reason+'</font>'
										});
									});
								}else{
									distributeOrderQuesReason.removeAll();
								}
								//交货单配送信息
								distributeOrderDepotShip.store.loadData(sonOrderDepotShipList, false);
							}
							i = i+1;
						});
						if(initOrderSn != '' && initOrderSn != null){//如果传了交货单号  初始化这个交货单面板
							deliveryOrderTabPanel.setActiveTab(initOrderSn);
							initOrderSn='';
						} else {
							deliveryOrderTabPanel.setActiveTab(0);
						}
						deliveryOrderTabPanel.show();
						distributeOrderGoddsDetail.show();
					}, 1);
				}
				
				//异步给付款信息赋值
				setTimeout(function(){
					paySetModule.store.loadData(masterOrderPayTypeDetailListData, false);
				},1);

				//异步展示订单费用信息
				setTimeout(function(){
					var showTotalFee = '<div style= "float: right; color: #666;">'
						+ '商品总金额：  '+ numFixed(masterOrderInfoData.goodsAmount, 2)
						+ ' 元 - 折让：  '+numFixed(masterOrderInfoData.discount, 2)
//						+ ' 元 + 综合税费：  '+numFixed(masterOrderInfoData.tax, 2)
						+ ' 元 + 配送费用：  '+numFixed(masterOrderInfoData.shippingTotalFee, 2)
						+ ' 元</div>';
					paySetModuleCmp.down('#showTotalFee').update(showTotalFee);
					var totalFee = '<div style= "float: right; color: #666;">= 订单总金额：   '+numFixed(masterOrderInfoData.totalFee, 2)+'元</div>';
					paySetModuleCmp.down('#totalFee').update(totalFee);
					var showTotalPayable = '<div style= "float: right; color: #666;">'
						+ '- 已付款金额：  ' + numFixed(masterOrderInfoData.moneyPaid, 2)
//						+ ' 元 - 使用余额：  ' +numFixed(masterOrderInfoData.surplus, 2)
//						+ ' 元 - 使用积分：  ' +numFixed(masterOrderInfoData.integralMoney, 2)
						+ ' 元 - 使用红包【'+ masterOrderInfoData.bonusName +'】：  ' + numFixed(masterOrderInfoData.bonus, 2)
						+ ' 元</div>';
					paySetModuleCmp.down("#showTotalPayable").update(showTotalPayable);
					var totalPayable = '<div style= "float: right; color: #666;">= 应付款金额：   ' +numFixed(masterOrderInfoData.totalPayable, 2)+'元</div>';
					paySetModuleCmp.down("#totalPayable").update(totalPayable);
					
//					paySetModuleCmp.down("#settBvValue").update('<div style= "float: right; color: #666;">'
//							+ "支付赠送美力值：" + masterOrderInfoData.bvValue + "</div>");
					// 退货单信息
					var returnSettPrice = '<div style= "float: right; color: #666;">'
					+ '所退商品财务总金额：'+numFixed(returnSettleMoney, 2)
					+' 元</div>';
					paySetModuleCmp.down("#returnSettPrice").update(returnSettPrice);
				},1);
				
				//根据权限设置按钮的状态
				var orderDetailNorth = Ext.getCmp('orderDetailNorth');
				/*if(masterOrderInfoData.orderType==2){
					orderDetailNorth.down("#onlyReturn").show();
				}else{
					orderDetailNorth.down("#onlyReturn").hide();
				}*/
				if(isHistory==1){
					//移入近期
					if(orderStatusUtils['recent']==1){
						orderDetailNorth.down("#recentMenu").enable();
					}else{
						orderDetailNorth.down("#recentMenu").disable();
					}
					//需要切换的按钮
					var buttons = orderDetailNorth.down('toolbar').items.items;
					buttons.forEach(function(item,index){
						var itemId = item.itemId;
						if (itemId=='conUnConMenu' || itemId=='norQuesMenu'
							|| itemId == 'occRealMenu' || itemId=='cancelMenu'){
							var buttonMenu = orderStatusUtils[itemId];
							item.setText('<font style="color:white">'+buttonMenu.name+'</font>');
							item.setTooltip(buttonMenu.name);
						}
					});
					//解密按钮
//					shipSetModuleCmp.down('#decodeLinkMobile').disable();
				}else{
					if (orderStatusUtils['consignee'] == 1) {
						shipSetModuleCmp.tools[0].enable();
					} else {
						shipSetModuleCmp.tools[0].disable();
					}
					/*if (orderStatusUtils['decode'] == 1) {
						shipSetModuleCmp.down('#decodeLinkMobile').enable();
					} else {
						shipSetModuleCmp.down('#decodeLinkMobile').disable();
					}*/
					if (orderStatusUtils['other'] == 1) {
						otherSetModuleCmp.tools[0].enable();
					} else {
						otherSetModuleCmp.tools[0].disable();
					}
					/*if (orderStatusUtils['goods'] == 1) {
						goodSetModuleCmp.tools[0].enable();
					} else {
						goodSetModuleCmp.tools[0].disable();
					}*/
					/*if (orderStatusUtils['money'] == 1) {
						paySetModuleCmp.tools[0].enable();
					} else {
						paySetModuleCmp.tools[0].disable();
					}*/
					var buttons = orderDetailNorth.down('toolbar').items.items;
					buttons.forEach(function(item,index){
						var itemId = item.itemId;
						if (itemId=='conUnConMenu' || itemId=='norQuesMenu'
							|| itemId == 'occRealMenu' || itemId=='cancelMenu'){
							var buttonMenu = orderStatusUtils[itemId];
							item.value = buttonMenu.action;
							item.setText('<font style="color:white">'+buttonMenu.name+'</font>');
							item.setTooltip(buttonMenu.name);
							if (itemId == 'occRealMenu' || (orderStatusUtils[buttonMenu.action] == 1)) {
								item.enable();
							} else {
								item.disable();
							}
						} else if(orderStatusUtils[item.value] == '1'){
							item.enable();
						} else{
							item.disable();
						}
					});
				}
			},
			failure : function(form,action) {
				var msg = form.reader.rawData.msg;
				Ext.msgBox.remainMsg('加载订单数据异常',msg, Ext.MessageBox.ERROR);
			}
		});
	},
	decodeLinkMobile : function(btn){
		var form = btn.up('form');
		var tel = form.getForm().findField("tel").getValue();
		var oldMobile = form.getForm().findField("mobile").getValue();
		Ext.Ajax.request({
			url:  basePath + 'custom/commonLog/decodeLinkMobile',
			params: {tel : tel, mobile : oldMobile, masterOrderSn: masterOrderSn},
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
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('解密', respText.msg, Ext.MessageBox.ERROR);
			}
		});
	},
	queryExpress : function(record){
		//拼装入参
		if (siteCode == 'Chlitina') {
			return;
		}
		var widgetRecord = record.getWidgetRecord();
		var params = {};
		params.masterOrderSn = masterOrderSn;//订单号
		params.orderSn = widgetRecord.get('orderSn');//交货单号
		params.depotCode = widgetRecord.get('depotCode');//发货仓
		params.shippingName = widgetRecord.get('shippingName');//承运商
		params.invoiceNo = widgetRecord.get('invoiceNo');//运单号
		var deliveryType = widgetRecord.get('deliveryType');//送货方式
		var deliveryTypeName = '';
		if(deliveryType=='0'){
			deliveryTypeName = '工厂发货单';
		}else if(deliveryType=='1'){
			deliveryTypeName = '门店发货单';
		}else if(deliveryType=='2'){
			deliveryTypeName = '工厂发货单';
		}else if(deliveryType=='3'){
			deliveryTypeName = '第三方仓库发货单';
		}
		params.deliveryTypeName = "直发";
		
		queryExpressParams = params;
		
		var app = Ext.application('MB.jsPages.queryExpressApp');
		return ;
	},
	editDelivery : function(record){ 
		//获取入参
		var param = {};
		param.orderSn = record.get("orderSn");
		param.depotCode = record.get("depotCode");
		param.shippingId = record.get("shippingId");
		param.shippingTotalFee = Ext.getCmp('orderSetModule').getForm().findField('shippingTotalFee').getValue();
		param.transType = Ext.getCmp('orderSetModule').getForm().findField('transType').getValue();
		param.country = Ext.getCmp('shipSetModule').getForm().findField('country').getValue();
		param.province = Ext.getCmp('shipSetModule').getForm().findField('province').getValue();
		param.city = Ext.getCmp('shipSetModule').getForm().findField('city').getValue();
		param.district = Ext.getCmp('shipSetModule').getForm().findField('district').getValue();
		
		editDeliveryParams = param;
		
		var app = Ext.application('MB.jsPages.masOrderDeliveryEditApp');
		return ;
	},
	//已付款
	doPay: function(rowIndex, paygrid){ // 已付款
		this.payOperation(rowIndex, 'pay', '已付款', paygrid);
	},
	//未付款
	doUnPay: function(rowIndex, paygrid){ // 未付款
		this.payOperation(rowIndex, 'unPay', '未付款', paygrid);
	},
	payOperation : function(rowIndex, payType, payName, paygrid) {
		var record = paygrid.getStore().getAt(rowIndex);
		var masterPaySn = record.get('masterPaySn');
		var params = {"masterOrderSn" : masterOrderSn, "masterPaySn" : masterPaySn};
		Ext.Ajax.request({
			url: basePath + '/custom/orderStatus/' + payType,
			params: params,
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				if (results.code=='1') {
					Ext.getCmp('orderDetailCenter').initData();
				} else {
					Ext.msgBox.remainMsg('支付单' + payName, results.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('保存收货信息', respText.msg);
			}
		});
	},
	getDistributeOrderStatusDesc : function(orderStatus){
		var desc = '';
		var cssHead = '<span style="color:#191970;font-weight:bold";>';
		var cssTail = '</span>';
		if(orderStatus=='0'){
			desc = '未确认';
		}else if(orderStatus=='1'){
			desc = '已确认';
		}else if(orderStatus=='2'){
			desc = '已取消';
		}else if(orderStatus=='3'){
			desc = '已完成';
		}
		return cssHead+desc+cssTail;
	},
	getDistributeOrderShipStatusDesc : function(shipStatus){
		var desc = '';
		var cssHead = '<span style="color:#191970;font-weight:bold";>';
		var cssTail = '</span>';
		if(shipStatus=='0'){
			desc = '未发货';
		}else if(shipStatus=='1'){
			desc = '备货中';
		}else if(shipStatus=='2'){
			desc = '部分发货';
		}else if(shipStatus=='3'){
			desc = '已发货';
		}else if(shipStatus=='4'){
			desc = '部分收货';
		}else if(shipStatus=='5'){
			desc = '已收货';
		}
		return cssHead+desc+cssTail;
	},
	getDistributionStatusDesc : function(depotStatus){
		var desc = '';
		var cssHead = '<span style="color:#191970;font-weight:bold";>';
		var cssTail = '</span>';
		if(depotStatus=='2'){
			desc = '已分配';
		}else{
			desc = '未分配';
		}
		return cssHead+desc+cssTail;
	},
	queryStock : function(record){
		var source = Ext.getCmp('orderSetModule').getForm().findField('source').getValue();
		if(source=='1'){
			Ext.msgBox.remainMsg('提示', "全流通订单不提供库存查询！", Ext.MessageBox.ERROR);
			return ;
		}
		var widgetRecord = record.getWidgetRecord();
		var customCode = widgetRecord.get('customCode');
		if(customCode==null||customCode==''){
			Ext.msgBox.remainMsg('错误', "获取sku为空！", Ext.MessageBox.ERROR);
			return ;
		}
		if(channelCode==''||channelCode==null){
			Ext.msgBox.remainMsg('错误', "获取订单来源为空！", Ext.MessageBox.ERROR);
			return ;
		}
		var params = {};
		params.customCode = customCode;
		params.channelCode = channelCode;
		Ext.Ajax.request({
			url:  basePath + 'custom/orderInfo/getStock',
			params: params,
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				var stockNumber = respText.stockNumber;
				if (respText.code=='1') {
					widgetRecord.set('availableNumber',stockNumber);
				} else {
					Ext.msgBox.remainMsg('查询库存', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('查询库存', respText.msg, Ext.MessageBox.ERROR);
			}
		});
	},
    showC2mItem : function(record,extensionCode){
    	var c2mItem = record.get("c2mItem");
    	var obj = JSON.parse(c2mItem);
		var win = Ext.widget("c2xPropertyEdit");
		//获取颜色下拉菜单列表
		var colorCombo =  Ext.getCmp("c2xBasePropertyColorCombo");
		Ext.Ajax.request({
			timeout: 1800000,//1800秒 半小时
			url: basePath + 'custom/orderGoodsEdit/getPropertyListByClsId.spmvc',
			params: {"clsId":obj.diyId,"queryMode":"0"},
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				console.log(respText);
				colorCombo.getStore().loadData(respText);
				colorCombo.setValue(obj.color);
				colorCombo.readOnly=true;
			},
			failure:function(){
			} 
		});
		//获取尺寸下拉菜单
		var sizeCombo =  Ext.getCmp("c2xBasePropertySizeCombo");
		Ext.Ajax.request({
			timeout: 1800000,//1800秒 半小时
			url: basePath + 'custom/orderGoodsEdit/getPropertyListByClsId.spmvc',
			params: {"clsId":obj.diyId,"queryMode":"1"},
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				console.log(respText);
				sizeCombo.getStore().loadData(respText);
				sizeCombo.setValue(obj.size);
				sizeCombo.readOnly=true;
			},
			failure:function(){
			} 
		});
		//给基本属性面板赋值
		var basePropertyForm = Ext.getCmp("c2xBaseProperty").getForm();
		if(extensionCode=='c2m'){
			//定制人
			basePropertyForm.findField('buyer').setValue(obj.buyer);
			Ext.getCmp('c2xBasePropertyBuyer').hidden=false;
			//定制费用
			basePropertyForm.findField('cost').setValue(obj.cost);
			Ext.getCmp('c2xBasePropertyCost').hidden=false;
			//定制图片
			basePropertyForm.findField('c2mPicPath').setValue(obj.picPath1);
			if(obj.picPath1!=''&&obj.picPath1!=null){
				Ext.getCmp('c2mBasePropertyPicPath1').html='<img width="90" height="90"  onclick=\'showBigPic(this)\' src="'+obj.picPath1+'"/>'
			}
			Ext.getCmp('c2mBasePropertyPicField').hidden=false;
			//特殊定制信息
			var measureObj = obj.measure;
			var c2xMeasure = Ext.getCmp("c2xMeasure").store;
			c2xMeasure.add(measureObj);
			Ext.getCmp("c2xMeasure").hidden=false;
			//设置特殊定制信息不可以编辑
			Ext.getCmp("c2xMeasure").disabled=true;
		}else if(extensionCode=='c2b'){
			//定制人
			basePropertyForm.findField('name').setValue(obj.name);
			Ext.getCmp('c2xBasePropertyName').hidden=false;
			//定制费用
			basePropertyForm.findField('totalPrice').setValue(obj.totalPrice);
			Ext.getCmp('c2xBasePropertyTotalPrice').hidden=false;
			//效果图
			basePropertyForm.findField('c2bPicPath1').setValue(obj.picPath1);
			if(obj.picPath1!=''&&obj.picPath1!=null){
				Ext.getCmp('c2bBasePropertyPicPath11').html='<img width="90" height="90"  onclick=\'showBigPic(this)\' src="'+obj.picPath1+'"/>'
			}
			//组合图
			basePropertyForm.findField('c2bPicPath2').setValue(obj.picPath2);
			if(obj.picPath2!=''&&obj.picPath2!=null){
				Ext.getCmp('c2bBasePropertyPicPath22').html='<img width="90" height="90"  onclick=\'showBigPic(this)\' src="'+obj.picPath2+'"/>'
			}
			Ext.getCmp('c2bBasePropertyPicField').hidden=false;
			//坐标信息
			var elementObj = obj.element;
			var c2bCoordinage = Ext.getCmp("c2bCoordinage").store;
			c2bCoordinage.add(elementObj);
			Ext.getCmp("c2bCoordinage").hidden=false;
			//设置button不可用
			basePropertyForm.findField('file').disabled=true;
			basePropertyForm.findField('file2').disabled=true;
			//设置坐标信息不可编辑
			Ext.getCmp("c2bCoordinage").disabled=true;
		}
		basePropertyForm.findField('diyId').setValue(obj.diyId);//定制ID
		basePropertyForm.findField('num').setValue(obj.num);//购买数量
		basePropertyForm.findField('sn').setValue(obj.sn);//商品6位码
		basePropertyForm.findField('clothesID').setValue(obj.clothesID);//商品11位码
		basePropertyForm.findField('sellerPrice').setValue(obj.sellerPrice);//商品价格
		basePropertyForm.findField('customCode').setValue(record.get('customCode'));//sku
		/*basePropertyForm.findField('closeFlag').setValue('1');*///1表示查看时打开
		win.show();
		
    }
	
});