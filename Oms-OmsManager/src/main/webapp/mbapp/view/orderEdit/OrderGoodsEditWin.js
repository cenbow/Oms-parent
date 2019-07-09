Ext.define("MB.view.orderEdit.OrderGoodsEditWin", {
	extend: "Ext.window.Window",
	alias: "widget.orderGoodsEditWin",
	requires : ['MB.model.ComboModel',
	            'MB.store.OrderSnCombStore',
	            'MB.view.orderEdit.OrderSnComb',
	            'MB.view.orderEdit.AddGoodsModule',
	            'MB.view.orderEdit.GoodsBonusModule',
	            'MB.view.commonComb.GoodsColorComb',
				'MB.view.commonComb.GoodsSizeComb',
				'Ext.grid.plugin.CellEditing',
				'MB.store.SearchGoodss',
				'MB.view.common.SearchGoodsCombo'],
	title: "编辑订单商品",
	width: '100%',
	height: '100%',
	layout: "fit",
	maskDisabled : false ,
	draggable : false,
	modal : true ,
	initComponent: function () {
		var me = this;
		if(editGoodsType == 3){
			clientHeight = document.body.clientHeight;
			clientWidth = document.body.clientWidth;
		}
		var tabPanelHeight= clientHeight * 0.45;
		
		var editType = parent.editGoodsType;
		var colorEditor;
		var sizeEditor;
		if (editType != 2) {
			colorEditor = new Ext.create('Ext.form.field.ComboBox',{
				store : Ext.create('Ext.data.Store',{
					fields: ['colorName','colorCode']
				}),
				displayField : 'colorName',
				valueField : 'colorName',
				queryMode : 'local',
				emptyText : '请选择颜色',
				editable : false,
			});
			sizeEditor = new Ext.create('Ext.form.field.ComboBox',{
				store : Ext.create('Ext.data.Store',{
					fields: ['sizeName','sizeCode']
				}),
				displayField : 'sizeName',
				valueField : 'sizeName',
				queryMode : 'local',
				emptyText : '请选择尺码',
				editable : false,
			});
		} else {
			colorEditor = null;
			sizeEditor = null;
		}
		//定义组件  后期如果做新增订单  这里需要做修改
		var tabPanel = {
			xtype : 'tabpanel',
			itemId : 'goodsEditTab',
			width: '100%',
			height: tabPanelHeight,
			items: [{
				xtype : 'addGoodsModule',
				tooltip: '添加商品'
			} , {
				xtype : 'goodsBonusModule',
				tooltip: '红包信息'
			}]
		}
		
		//定义items
		me.items = [{
			xtype: "form",
			border: false,
			frame: true,
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 120
			},
			items: [
			{//商品列表
				xtype : 'grid',
				height : clientHeight * 0.55,
				id : 'districtGoods',
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
				plugins: {
					ptype: 'cellediting',
					clicksToEdit: 1,
					listeners: {
						beforeedit: me.onCellBeforeEdit,
						afteredit : me.onCellAfterEdit
					}
				},
				selModel: {
					selType: 'cellmodel'
				},
				features : [ {
					ftype : 'grouping',
					groupHeaderTpl : ['交货单号：{[values.rows[0].get("orderSn")==null?"":values.rows[0].get("orderSn")]}'
					                +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
					                +'发货仓：{[(values.rows[0].get("depotCode") != null && values.rows[0].get("depotCode") != 0 && values.rows[0].get("depotCode") != \'DEFAULT\') ? values.rows[0].get("depotCode") : "未分仓" ]}'
									+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
						      		+'发货状态：{[ values.rows[0].get("shippingStatusName") ]}'
						      		+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
						      		+'交货单状态：{rows:this.getDisOrdStaName}',
							   		{
										getDisOrdStaName : function(rows){
							   				var orderStatus = rows[0].get("orderStatus");
							   				if(orderStatus=='0'){
							   					return '未确认';
							   				}else if(orderStatus=='1'){
							   					return '已确认';
							   				}else if(orderStatus=='2'){
							   					return '已取消';
							   				}else if(orderStatus=='3'){
							   					return '已完成';
							   				}else{
							   					return '';
							   				}
							   			}
							   		}],
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
				            { text: '商品名称', width: clientWidth * 0.09, dataIndex: 'goodsName' },//0
							{ text: '商品号', width: clientWidth * 0.05, dataIndex: 'goodsSn' },//1
							{ text: '企业SKU码', width: clientWidth * 0.08, dataIndex: 'customCode' },//2
							{ text: 'sap', width: clientWidth * 0.07, dataIndex: 'sap' },//3
							{ text: '商品价格', width: clientWidth * 0.05, dataIndex: 'goodsPrice' },//4
							{
								text: '成交价',
								width: clientWidth * 0.05,
								dataIndex: 'transactionPrice',
								renderer : me.editArearColor,
								editor: {
									xtype: 'numberfield',
									allowBlank: false,
									minValue: 0,
									maxValue: 999999.9999,
									decimalPrecision : 4
								}
							},// 5
							{ text: '财务价格', width: clientWidth * 0.07, dataIndex: 'settlementPrice' },// 5
							{ header: '红包积分', columns: 
								[ {// 6
									text: '红包金额',
									width: clientWidth * 0.05,
									dataIndex: 'shareBonus',
									editor: {
										xtype: 'numberfield',
										allowBlank: false,
										minValue: 0,
										maxValue: 999999.9999,
										decimalPrecision : 4
									}
								} , {// 7
									text: '积分金额',
									width: clientWidth * 0.05,
									dataIndex: 'integralMoney',
									align : "center"
								} ]
							},
							{ text: '初始商品数量', dataIndex: 'initGoodsNumber', hidden : true },// 8
							{
								text: '商品数量', 
								width: clientWidth * 0.05,
								dataIndex: 'goodsNumber'
							},
							{ text: '当前商品尺码', dataIndex: 'sizeCode', hidden: true }, // 11
							{ text: '当前商品尺码', dataIndex: 'currSizeCode', hidden: true },  // 12
							{ text: '当前商品尺码列表', dataIndex: 'sizeChild', hidden: true }, // 13
							{ text: '当前商品颜色码', dataIndex: 'colorCode', hidden: true }, // 14
							{ text: '当前商品颜色码', dataIndex: 'currColorCode', hidden: true }, // 15
							{ text: '当前商品颜色码列表', dataIndex: 'colorChild', hidden: true }, // 16
							{ header: '规格', columns: 
								[ {// 17
									header : "颜色",
									width : clientWidth * 0.07,
									dataIndex: 'currColorName',
									sortable : false
								} , {// 18
									header : "尺寸",
									width : clientWidth * 0.07,
									dataIndex: 'currSizeName',
									sortable : false
								} ]
							},
							{ text: "属性", align : "center", width : clientWidth * 0.06, dataIndex : 'extensionCode', sortable : false,
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
									}
								}
							},// 19
							{ text: "属性Id", align : "center", hidden : true, dataIndex : 'extensionId', sortable : false },
							{ text: "商品Id", align : "center", hidden : true, dataIndex : 'id', sortable : false },
							{ text: '订单编号', dataIndex: 'orderSn', hidden: true, sortable : false},
							{ text: '促销规则', width: clientWidth * 0.08, dataIndex: 'promotionDesc' },
							{ text: '折让', dataIndex: 'discount', hidden: true, sortable : false},
							{ text: 'bvValue', dataIndex: 'bvValue', hidden: true, sortable : false},
							{ text: 'baseBvValue', dataIndex: 'baseBvValue', hidden: true, sortable : false},
							{ text: '小计', width: clientWidth * 0.06, dataIndex: 'subTotal' },
					        { text:'序号', xtype: 'rownumberer', width: clientWidth* 0.03 },
							{ text: '初始积分金额', dataIndex: 'initIntegralMoney', hidden : true },
							{ text: '供应商编码', dataIndex: 'supplierCode', hidden : true },
							{
								width: clientWidth * 0.04,
								sortable: false,
								renderer: function(value, metaData, record, rowIndex) {
									var id = Ext.id();
									var shippingStatus = record.get('shippingStatus');
									var orderStatus = record.get('orderStatus');
									var disable = false;
									if (shippingStatus > 0||orderStatus=='1'||orderStatus=='2'||orderStatus=='3') {
										disable = true;
									}
									setTimeout(function() {
										var tool = Ext.create('Ext.panel.Tool', {
											iconCls: 'delete',
											tooltip: '删除商品',
											disabled : disable,
											handler: function () {
												me.onRemoveClick(rowIndex,record)
											}
										});
										if (Ext.get(id)) {
											tool.render(Ext.get(id));
										}
									}, 1);
									return '<div id="' + id + '"></div>';
								}
							}
				        ]
				},
				bbar: {
					items: [ {
						xtype : 'button',
						itemId: 'recalculateButton',
						text : '红包试算',
						handler : me.recalculate
					} , { // 红包合计
						xtype: 'component',
						itemId: 'bonusTotal',
						margin : '0 0 0 150'
					} , { // 商品使用红包合计
						xtype: 'component',
						itemId: 'goodsBonusTotal',
						margin : '0 0 0 150'
					} , { // 商品合计
						xtype: 'component',
						itemId: 'orderTotal',
						margin : '0 0 0 150'
					}]
				}
			},{
				xtype : 'hidden',
				id : 'orderGoodsEditWinDelGoods',
				lableField: '删除商品的id|数量保存'
			},{
				xtype : 'hidden',
				lableField: '请求类型',
				name : 'method',
				value : 'start'
			},{
				xtype : 'hidden',
				lableField: '使用红包列表',
				itemId : 'bonusIds',
				name : 'bonusIds'
			},tabPanel]
			
		}];
		me.buttons = [
			{ text: "重置", action : 'reset',id : 'editGoodsReset'},
			{ text: "保存", align : 'center', handler : me.saveOrderGoods},
			{ text: "关闭", align : 'center', id : 'editGoodsClose',
				handler : function (){
					this.up("window").close();
				}
			},

		];
		me.callParent(arguments);
	},
	initPage: function (win,result) {
		// 数据加载成功后操作
		//删除商品记录置空
		Ext.getCmp('orderGoodsEditWinDelGoods').setValue('');
		// 商品列表数据展示
		var doERPA = result.doERP;
		var masterOrderGoodsDetailList = win.down("form").down("#districtGoods");
		var goodsListData = result.masterOrderGoodsDetailList;
		if(goodsListData){
			masterOrderGoodsDetailList.getStore().loadData(goodsListData, false);
		}
		this.recalculateTotal(masterOrderGoodsDetailList);
		// 商品红包数据
		var bonusListData = result.bonusList;
		var editGoodsForm = win.down("form");
		var tabpanel = editGoodsForm.down("tabpanel");
		//设置添加商品面板是否可用
		if (doERPA == 1) {
			parent.editGoodsType = 2;
			editGoodsForm.down("addGoodsModule").setDisabled( true );
		} else {
			parent.editGoodsType = 1;
		}
		var goodsbonusGrid = editGoodsForm.down("goodsBonusModule");
		var orderGoodsGrid = editGoodsForm.down("#districtGoods");
		var bonusStore = goodsbonusGrid.store;
		//赋值
		if (bonusListData != null && bonusListData.length > 0) {
			bonusStore.loadData(bonusListData, false);
			var bonusIds = new Array();
			var bonusTotal = 0.0000;
			bonusListData.forEach(function (obj, index) {
				bonusIds.push(obj.cardNo);
				bonusTotal += obj.cardMoney;
			})
			var editabled = false;
			goodsListData.forEach(function (obj, index) {
				if (obj.shippingStatus > 0) {
					editabled = true;
				}
			})
			editGoodsForm.down("#districtGoods").down("#recalculateButton").setDisabled( editabled );
			orderGoodsGrid.down('#bonusTotal').update('红包面值合计: ' + bonusTotal + " 元");
			editGoodsForm.down("#bonusIds").setValue(bonusIds);
			tabpanel.setActiveTab(1);
		} else {
			orderGoodsGrid.down('#bonusTotal').update('红包面值合计: 0  元');
			goodsbonusGrid.setDisabled( true );
			editGoodsForm.down("#districtGoods").down("#recalculateButton").setDisabled( true );
			tabpanel.setActiveTab(0);
		}
	},
	editArearColor : function (value, meta, record) {
		meta.style='background:' + editColor;
		return value;
	},
	editLackNumbgColor : function (value, meta, record) {
		if (value && value > 0) {
			meta.style='background: #FF0000';
			return value;
		} else {
			return 0;
		}
	},
	recalculateTotal : function(grid) { // 商品编辑属性初始化.
		var goodsTotal = 0.0000;
		var shareBonus = 0.0000;
		grid.getStore().each(function(record) {
			var goodsNumber = record.get("goodsNumber");
			goodsTotal += record.get("subTotal");
			shareBonus += (record.get("shareBonus") * goodsNumber);
		});
		grid.down('#goodsBonusTotal').update('商品使用红包合计: ' + numFixed(shareBonus, 4) + " 元");
		grid.down('#orderTotal').update('商品合计: ' + numFixed(goodsTotal, 4) + " 元");
	},
	onCellBeforeEdit: function (editor, ctx, eOpts) { // 商品编辑属性初始化
		var clickColIdx = ctx.colIdx; // grid column 展示列下标
		var record = ctx.record;
		// 已发货 禁用编辑
		var shippingStatus = record.get('shippingStatus');
		var orderStatus = record.get('orderStatus');
		if (shippingStatus > 0||orderStatus=='1'||orderStatus=='2'||orderStatus=='3') {
			return false;
		}
		if (clickColIdx == 8 && parent.editGoodsType == 2) { // 商品数量
			var numberf = ctx.grid.columns[9].getEditor(ctx.record);
			numberf.setMaxValue(record.get("initGoodsNumber"), false);
		} else if (clickColIdx == 10 && parent.editGoodsType != 2) { // 颜色
			var colorCombo = ctx.grid.columns[17].getEditor(ctx.record);
			
			colorCombo.getStore().loadData(record.get("colorChild"), false);
		} else if (clickColIdx == 11 && parent.editGoodsType != 2) { // 尺码
			var sizeCombo = ctx.grid.columns[18].getEditor(ctx.record);
			
			sizeCombo.getStore().loadData(record.get("sizeChild"), false);
		}
	},
	onCellAfterEdit: function(editor, ctx, eOpts) { // 修改商品信息后动态变化
		var clickColIdx = ctx.colIdx; // grid column 展示列下标
		var record = ctx.record;
		// 已发货 禁用编辑
		var shippingStatus = record.get('shippingStatus');
		if (shippingStatus > 0) {
			return false;
		}
		var value = ctx.value;
		if (clickColIdx == 5 || clickColIdx == 6 || clickColIdx == 8) { // 成交价 || 红包分摊金额 ||商品数量
			var goodsPrice = ctx.record.get("goodsPrice");
			var transactionPrice = ctx.record.get("transactionPrice");

			var goodsNumber = ctx.record.get("goodsNumber");
			var initGoodsNumber = ctx.record.get("initGoodsNumber");
			var shareBonus = ctx.record.get("shareBonus");
			var initLackNum = ctx.record.get("initLackNum");
			var initIntegralMoney = ctx.record.get("initIntegralMoney");
			// 重新分摊积分数量
			var perIntegralMoney = 0.00;
			if (initIntegralMoney > 0) {
				var totalIntegralMoney = initIntegralMoney * initGoodsNumber;
				perIntegralMoney = totalIntegralMoney/goodsNumber;
				ctx.record.set("integralMoney", numFixed(perIntegralMoney, 4));
			}
			ctx.record.set("settlementPrice", numFixed(transactionPrice - shareBonus - perIntegralMoney, 4));
			ctx.record.set("discount", numFixed(goodsPrice - transactionPrice, 4));
			ctx.record.set("subTotal", transactionPrice * goodsNumber);
			// 缺货商品数量
			if (initLackNum && initLackNum > 0) {
				var changeNum = initGoodsNumber - goodsNumber;
				if (initLackNum - changeNum < 0 ) {
					ctx.record.set("lackNum", 0);
				} else {
					ctx.record.set("lackNum", initLackNum - changeNum);
				}
			}
		}
		if (clickColIdx == 10 || clickColIdx == 11) { // 选择颜色码和尺码后对颜色码尺码组合做有效性验证
			var currColorName = ctx.record.get("currColorName");
			var currSizeName = ctx.record.get("currSizeName");
			var sizeChild = ctx.record.get("sizeChild");
			var colorChild = ctx.record.get("colorChild");
			var goodsNumber = ctx.record.get("goodsNumber");
			var goodsSn = record.get("goodsSn");
			var selectColorCode;
			var oldColorName;
			var selectSizeCode;
			var oldSizeName;
			if (colorChild != null && colorChild.length > 0) {
				colorChild.forEach(function (obj, index) {
					if (obj.colorName == currColorName) {
						selectColorCode = obj.colorCode;
					}
				});
			}
			if (sizeChild != null && sizeChild.length > 0) {
				sizeChild.forEach(function (obj, index) {
					if (obj.sizeName == currSizeName) {
						selectSizeCode = obj.sizeCode;
					}
				});
			}
			if (selectSizeCode != null && selectSizeCode != "" && selectColorCode != null && selectColorCode != "") {
				params = {
					"colorCode": selectColorCode,
					"sizeCode": selectSizeCode,
					shopCode: channelCode,
					"goodsSn" : goodsSn,
					"type": 1
				};
				//等待查库存接口
				Ext.Ajax.request({
					url: basePath + '/custom/orderGoodsEdit/getCustomStock',
					params: params,
					success: function(response){
						var text = response.responseText;
						var results = Ext.JSON.decode(text);
						if (results.code=='1') {
							// 有效性验证通过之后修改颜色码尺码
							ctx.record.set("customCode", results.list[0].sku);
							ctx.record.set("colorCode", selectColorCode);
							ctx.record.set("sizeCode", selectSizeCode);
							ctx.record.set("currColorCode", selectColorCode);
							ctx.record.set("currSizeCode", selectSizeCode);
						} else {
							// 有效性验证没有通过将修改颜色码尺码名称修改为原来值
							if (clickColIdx == 10) {
								ctx.record.set("currColorName", ctx.originalValue);
							} else {
								ctx.record.set("currSizeName", ctx.originalValue);
							}
							Ext.msgBox.remainMsg('验证商品SKU', results.msg, Ext.MessageBox.ERROR);
						}
					},
					failure: function(response){
						var text = response.responseText;
						var results = Ext.JSON.decode(text);
						// 有效性验证没有通过将修改颜色码尺码名称修改为原来值
						if (clickColIdx == 10) {
							ctx.record.set("currColorName", ctx.originalValue);
						} else {
							ctx.record.set("currSizeName", ctx.originalValue);
						}
						Ext.msgBox.msg('验证商品SKU', results.msg, Ext.MessageBox.ERROR);
					}
				});
			}
			ctx.grid.up('window').recalculateTotal(ctx.grid);
		}
	},
	editOrderGoodsParams : function (form,method) {
		//已删除的商品
		var delOrderGoods = Ext.getCmp('orderGoodsEditWinDelGoods').getValue();
		
		var params = form.getValues();
		var orderGoodsGrid = form.down("grid");
		var orderBonusGrid = form.down("goodsBonusModule");
		//拼装入参
		params['masterOrderSn'] = masterOrderSn;
		//拼装订单商品
		var j = 0;
		orderGoodsGrid.store.each(function(record, i) {
			if(method!='saveOrderGoods'){//保存时不需要传尺码列表和颜色列表参数
				//尺码列表
				var sizeList = record.get("sizeChild");
				if (sizeList != null && sizeList.length > 0) {
					sizeList.forEach(function (obj, index) {
						params['orderGoodsUpdateInfos['+ i +'].sizeChild['+ index +'].colorCode'] = obj.colorCode;
						params['orderGoodsUpdateInfos['+ i +'].sizeChild['+ index +'].colorName'] = obj.colorName;
						params['orderGoodsUpdateInfos['+ i +'].sizeChild['+ index +'].colorSeries'] = obj.colorSeries;
						params['orderGoodsUpdateInfos['+ i +'].sizeChild['+ index +'].custumCode'] = obj.custumCode;
						params['orderGoodsUpdateInfos['+ i +'].sizeChild['+ index +'].goodsSn'] = obj.goodsSn;
						params['orderGoodsUpdateInfos['+ i +'].sizeChild['+ index +'].sizeCode'] = obj.sizeCode;
						params['orderGoodsUpdateInfos['+ i +'].sizeChild['+ index +'].sizeName'] = obj.sizeName;
						params['orderGoodsUpdateInfos['+ i +'].sizeChild['+ index +'].skuSn'] = obj.skuSn;
					});
				}
				//颜色列表
				var colorList = record.get("colorChild");
				if (colorList != null && colorList.length > 0) {
					colorList.forEach(function (obj, index) {
						params['orderGoodsUpdateInfos['+ i +'].colorChild['+ index +'].colorCode'] = obj.colorCode;
						params['orderGoodsUpdateInfos['+ i +'].colorChild['+ index +'].colorName'] = obj.colorName;
						params['orderGoodsUpdateInfos['+ i +'].colorChild['+ index +'].colorSeries'] = obj.colorSeries;
						params['orderGoodsUpdateInfos['+ i +'].colorChild['+ index +'].custumCode'] = obj.custumCode;
						params['orderGoodsUpdateInfos['+ i +'].colorChild['+ index +'].goodsSn'] = obj.goodsSn;
						params['orderGoodsUpdateInfos['+ i +'].colorChild['+ index +'].sizeCode'] = obj.sizeCode;
						params['orderGoodsUpdateInfos['+ i +'].colorChild['+ index +'].sizeName'] = obj.sizeName;
						params['orderGoodsUpdateInfos['+ i +'].colorChild['+ index +'].skuSn'] = obj.skuSn;
					});
				}
			}
			var discount = 0;
			if(record.get("goodsPrice")!=null&&record.get("goodsPrice")!=''&&record.get("goodsPrice")
					&&record.get("transactionPrice")!=null&&record.get("transactionPrice")!=''&&record.get("transactionPrice")){
				discount = record.get("goodsPrice") - record.get("transactionPrice");
			}
			//商品信息
			params['orderGoodsUpdateInfos['+i+'].id'] = record.get('goodsId');
			params['orderGoodsUpdateInfos['+ i +'].orderSn'] = record.get("orderSn");
			params['orderGoodsUpdateInfos['+ i +'].masterOrderSn'] = masterOrderSn;
			params['orderGoodsUpdateInfos['+ i +'].goodsSn'] = record.get("goodsSn");
			params['orderGoodsUpdateInfos['+ i +'].goodsName'] = record.get("goodsName");
			params['orderGoodsUpdateInfos['+ i +'].goodsThumb'] = record.get("goodsThumb");
			params['orderGoodsUpdateInfos['+ i +'].customCode'] = record.get("customCode");
			params['orderGoodsUpdateInfos['+ i +'].goodsPrice'] = record.get("goodsPrice");
			params['orderGoodsUpdateInfos['+ i +'].transactionPrice'] = record.get("transactionPrice");
			params['orderGoodsUpdateInfos['+ i +'].settlementPrice'] = record.get("settlementPrice");
			params['orderGoodsUpdateInfos['+ i +'].shareBonus'] = record.get("shareBonus");
			params['orderGoodsUpdateInfos['+ i +'].initGoodsNumber'] = record.get("initGoodsNumber");
			params['orderGoodsUpdateInfos['+ i +'].goodsNumber'] = record.get("goodsNumber");
			params['orderGoodsUpdateInfos['+ i +'].currColorCode'] = record.get("currColorCode");
			params['orderGoodsUpdateInfos['+ i +'].currSizeCode'] = record.get("currSizeCode");
			params['orderGoodsUpdateInfos['+ i +'].currColorName'] = record.get("currColorName");
			params['orderGoodsUpdateInfos['+ i +'].currSizeName'] = record.get("currSizeName");
			params['orderGoodsUpdateInfos['+ i +'].depotCode'] = record.get("depotCode");//编辑商品信息时  放在tpl用于分组
			params['orderGoodsUpdateInfos['+ i +'].extensionCode'] = record.get("extensionCode");
			params['orderGoodsUpdateInfos['+ i +'].extensionId'] = record.get("extensionId");
			params['orderGoodsUpdateInfos['+ i +'].useCard'] = record.get("useCard");
			params['orderGoodsUpdateInfos['+ i +'].promotionDesc'] = record.get("promotionDesc");
			params['orderGoodsUpdateInfos['+ i +'].subTotal'] = record.get("subTotal");
			params['orderGoodsUpdateInfos['+ i +'].madeUrl'] = record.get("madeUrl");//编辑商品信息时  没有这个字段
			params['orderGoodsUpdateInfos['+ i +'].lackNum'] = record.get("lackNum");
			params['orderGoodsUpdateInfos['+ i +'].initLackNum'] = record.get("initLackNum");
			params['orderGoodsUpdateInfos['+ i +'].integralMoney'] = record.get("integralMoney");
			params['orderGoodsUpdateInfos['+ i +'].supplierCode'] = record.get("supplierCode");
			params['orderGoodsUpdateInfos['+ i +'].discount'] = discount;
			params['orderGoodsUpdateInfos['+ i +'].sap'] = record.get("sap");
			params['orderGoodsUpdateInfos['+ i +'].bvValue'] = record.get("bvValue");
			params['orderGoodsUpdateInfos['+ i +'].baseBvValue'] = record.get("baseBvValue");
			j = i;
		});
		var index = j;
		//拼装删除商品数据
		if(delOrderGoods&&delOrderGoods!=null&&delOrderGoods!=''){
			var array = delOrderGoods.split(',');
			for(var i=0;i<array.length;i++){
				if(array[i]&&array[i]!=''&&array[i]!=null){
					var index = index+1;
					var idAndNum = array[i].split('|');
					params['orderGoodsUpdateInfos['+index+'].id'] = idAndNum[0];
					params['orderGoodsUpdateInfos['+index+'].goodsNumber'] = idAndNum[1];
				}
			}
		}
		//拼装红包
		orderBonusGrid.store.each(function(record, i) {
			params['cardPackageUpdateInfos['+i+'].cardMoney'] = record.get('cardMoney');
			params['cardPackageUpdateInfos['+i+'].cardNo'] = record.get('cardNo');
		});
		console.dir(params);
		return params;
	},
	saveOrderGoods : function(btn) {
		if (resource == 0) {//订单
			var me = this;
			var win = btn.up("window");
			form = win.down("form");
			var params = win.editOrderGoodsParams(form,'saveOrderGoods');
			//如果保存交货单商品  传入交货单号
			params['orderSn'] = sonOrderEditSn;
			Ext.Ajax.request({
				url:  basePath + '/custom/orderGoodsEdit/doSaveOrderGoodsEdit',
				method : 'post',
				params: params,
				success: function(response){
					var result = Ext.JSON.decode(response.responseText);
					if (result.code=='1') {
						Ext.getCmp('orderDetailCenter').initData();
						win.close();
					} else {
						Ext.msgBox.remainMsg('保存订单商品信息', result.msg, Ext.MessageBox.ERROR);
					}
				},
				failure: function(response){
					var result = Ext.JSON.decode(response.responseText);
					Ext.msgBox.remainMsg('保存订单商品信息', result.msg, Ext.MessageBox.ERROR);
				}
			});
		} else if (resource == 1) {//换单
			if(exchangeOrderSn){//换单详情
				/**
				 * 这段代码可能没有执行到
				 */
				var me = this;
				var win = btn.up("window");
				form = win.down("form");
				var params = win.editOrderGoodsParams(form);
				params.orderSn=exchangeOrderSn;
				console.dir(params);
				var json = {
					params : params,
					success : function(formPanel, action) {
						if (action.result.success == "false") {
							Ext.msgBox.remainMsg('修改商品', action.result.msg, Ext.MessageBox.ERROR);
						} else {
							Ext.msgBox.msg('修改商品', action.result.msg, Ext.MessageBox.INFO);
							var orderForm = Ext.getCmp('orderCenter');
							orderForm.initData(orderForm);
							win.close();
						}
					},
					failure : function(formPanel, action) {
						Ext.msgBox.remainMsg('修改商品', action.response.statusText, Ext.MessageBox.ERROR);
					},
					waitMsg : 'Loading...'
				};
				form.submit(json);
			}else{//申请换单
				var win = btn.up("window");
				var grid=win.down('#districtGoods');
				var newGoodStore=[];
				var goodsAmount=0;
				var discount = 0;
				var total=0;
				var bonus=0;//红包
				var bvValue = 0;
				grid.store.each(function(record,i){
					if(record.get('shareBonus')){
						record.data.shareBonus=record.get('shareBonus');
					}
					if(record.get('goodsThumb')){
						record.data.goodsThumb=record.get('goodsThumb');
					}
					record.data.goodsColorName = record.get('currColorName');
					record.data.goodsSizeName = record.get('currSizeName');
					newGoodStore.push(record.data);
					total+=record.get('transactionPrice')*record.get('goodsNumber');
					bvValue+=record.get('bvValue')*record.get('goodsNumber');
					goodsAmount+=record.get('goodsPrice')*record.get('goodsNumber');
					discount+= (record.get('goodsPrice') - record.get('transactionPrice')) * record.get('goodsNumber');
					bonus+=record.get('shareBonus');
				});
				
				//组件
				var orderExchangeChanOrdModule = Ext.getCmp('orderExchangeChanOrdModule');
				var exchangeGoodsList = orderExchangeChanOrdModule.down('#exchangeGoodsList');
				var exchangePayTotal = orderExchangeChanOrdModule.down('#exchangePayTotal');
				var exchangePayInfo = orderExchangeChanOrdModule.down('#exchangePayInfo');
				var exchangeBV = orderExchangeChanOrdModule.down('#exchangeBV');
				//赋值
				exchangeGoodsList.store.loadData(newGoodStore);
				//总计
				exchangePayTotal.setValue(total);
				exchangeBV.setValue(bvValue);
				exchangePayInfo.getForm().findField('goodsAmount').setValue(goodsAmount);
				exchangePayInfo.getForm().findField('discount').setValue(discount);
				exchangePayInfo.getForm().findField('totalFee').setValue(total);

				//红包
				exchangePayInfo.getForm().findField('bonus').setValue(bonus);
				win.close();
				Ext.msgBox.msg('结果', '添加商品成功！', Ext.MessageBox.INFO);
			}
		}
	},
	recalculate : function (btn) { // 红包试算
		var form = btn.up("form");
		var params = btn.up("window").editOrderGoodsParams(form);
		var grid = form.down("#districtGoods");
		Ext.Ajax.request({
			url:  basePath + '/custom/orderGoodsEdit/recalculate',
			params: params,
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				if (results.code=='1') {
					var orderGoodsUpdateInfos = results.orderGoodsList;
					var goodsBonusTotal = results.goodsBonusTotal;
					//更新对应的record
					grid.getStore().each(function(record) {
						Ext.each(orderGoodsUpdateInfos,function(bean,i){
							if(record.get('extensionId')==bean.extensionId&&
									record.get('extensionCode')==bean.extensionCode&&
									record.get('customCode')==bean.customCode){
								record.set('shareBonus',bean.shareBonus);
								record.set('settlementPrice',bean.settlementPrice);
							}
						}); 
					});
					//计算商品使用红包合计
					grid.down('#goodsBonusTotal').update('商品使用红包合计: ' + numFixed(goodsBonusTotal, 4) + " 元");
					Ext.msgBox.msg('红包试算', results.msg, Ext.MessageBox.INFO);
				} else {
					Ext.msgBox.remainMsg('红包试算', results.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				Ext.msgBox.remainMsg('红包试算', results.msg, Ext.MessageBox.ERROR);
			}
		});
	},
	onRemoveClick: function(rowIndex,record){ // 删除商品
		var store = this.down('grid').getStore();
		var OrderSns = '';
		store.each(function(record) {
			OrderSns = OrderSns+record.get('orderSn')+',';
		});
		var delOrderSn = record.get('orderSn');
		if(delOrderSn){//已拆单
			var num = 0;
			delOrderSn = eval("/"+delOrderSn+"/g");
			num = OrderSns.match(delOrderSn).length;
			if (num == 1) {
				Ext.msgBox.remainMsg('删除商品', "同一个交货单下商品数据不能全部删除,至少保留一条记录！", Ext.MessageBox.ERROR);
				return ;
			}
		}else{//未拆单
			var num = store.getData().length;
			if (num == 1) {
				Ext.msgBox.remainMsg('删除商品', "商品数据不能全部删除,至少保留一条记录！", Ext.MessageBox.ERROR);
				return ;
			}	
		}
		var orderGoodsEditWinDelGoods = Ext.getCmp('orderGoodsEditWinDelGoods').getValue();
		var id = record.get('goodsId');
		this.down('grid').getStore().removeAt(rowIndex);
		Ext.msgBox.msg('删除商品', "删除成功！", Ext.MessageBox.INFO);
		//记录删除商品的id和数量  新增商品不记录
		if(id!=''&&id!=null){
			var goodsNumber = record.get('goodsNumber');
			if(orderGoodsEditWinDelGoods&&orderGoodsEditWinDelGoods!=null&&orderGoodsEditWinDelGoods!=''){
				orderGoodsEditWinDelGoods = orderGoodsEditWinDelGoods+id+'|-'+goodsNumber+',';
				Ext.getCmp('orderGoodsEditWinDelGoods').setValue(orderGoodsEditWinDelGoods);
			}else{
				Ext.getCmp('orderGoodsEditWinDelGoods').setValue(id+'|-'+goodsNumber+',');
			}
		}
		this.recalculateTotal(this.down('grid'));
	}
});