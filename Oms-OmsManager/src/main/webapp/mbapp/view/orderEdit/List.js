Ext.define("MB.view.orderEdit.List", {
	extend: "Ext.grid.Panel",
	alias: 'widget.ordereditlist',
	store: "OrderGoodsStore",
	requires : ['Ext.grid.plugin.CellEditing',
				'MB.view.common.ProductLibBrandCombo',
				'MB.view.common.ProductLibCategoryCombo',
				'MB.view.common.SearchGoodsCombo',
				'MB.view.common.GoodsColorCombo',
				'MB.view.common.GoodsSizeCombo'
				],
//	title: '渠道信息',
	autoRender:true,
	columnLines: true,
	width: '100%',
	height: '100%',
	loadMask: true, //读取数据时的遮罩和提示功能
	frame: true,
//	resizable: true,
//	forceFit: true,
//	collapsible: true,
//	defaults : {
//		align: "center"
//	},编辑
	initComponent: function () {
		var clientWidth = document.body.clientWidth;
		var me = this;
		var editType = parent.editGoodsType;
		var colorEditor;
		var sizeEditor;
		if (editType != 2) {
			colorEditor = {
				xtype : 'commongoodscolorcombo',
				fieldLabel: '',
				queryMode: 'local',
				displayField: 'colorName',
				valueField: 'colorName',
				hiddenName: 'colorName',
				emptyText: '请选择商品颜色',
				editable: false
			};
			sizeEditor = {
				xtype : 'commongoodssizecombo',
				fieldLabel: '',
				queryMode: 'local',
				displayField: 'sizeName',
				valueField: 'sizeName',
				hiddenName: 'sizeName',
				emptyText: '请选择商品颜色',
				editable: false
			};
		} else {
			colorEditor = null;
			sizeEditor = null;
		}
		var action = {
				width: clientWidth * 0.04,
				sortable: false,
				renderer: function(value, metaData, record, rowIndex) {
					var id = Ext.id();
					var shippingStatus = record.get('shippingStatus');
					var disable = false;
					if (shippingStatus > 0) {
						disable = true;
					}
					setTimeout(function() {
						var tool = Ext.create('Ext.panel.Tool', {
							iconCls: 'delete',
							tooltip: '删除商品',
							disabled : disable,
							handler: function () {
								me.onRemoveClick(rowIndex)
							}
						});
						if (Ext.get(id)) {
							tool.render(Ext.get(id));
						}
					}, 1);
					return '<div id="' + id + '"></div>';
				}
			};
		var columns = [
            { text: '商品名称', width: clientWidth * 0.09, dataIndex: 'goodsName' }, // 0
			{ text: '商品号', width: clientWidth * 0.05, dataIndex: 'goodsSn' }, // 1
			{ text: '企业SKU码', width: clientWidth * 0.08, dataIndex: 'customCode' }, // 2
			{ text: '商品价格', width: clientWidth * 0.07, dataIndex: 'goodsPrice' }, // 3
			{
				text: '成交价',
				width: clientWidth * 0.07,
				dataIndex: 'transactionPrice',
				renderer : me.editArearColor,
				editor: {
					xtype: 'numberfield',
					allowBlank: false,
					minValue: 0,
					maxValue: 999999.9999,
					decimalPrecision : 4
				}
			}, // 4
			{ text: '财务价格', width: clientWidth * 0.07, dataIndex: 'settlementPrice' }, // 5
			{ header: '红包积分', columns: 
				[ { // 6
					text: '红包金额',
					width: clientWidth * 0.05,
					dataIndex: 'shareBonus',
					renderer : me.editArearColor,
					editor: {
						xtype: 'numberfield',
						allowBlank: false,
						minValue: 0,
						maxValue: 999999.9999,
						decimalPrecision : 4
					}
				} , { // 7
					text: '积分金额',
					width: clientWidth * 0.05,
					dataIndex: 'integralMoney',
					align : "center"
				} ]
			},
			{ text: '初始商品数量', dataIndex: 'initGoodsNumber', hidden : true }, // 8
			{ header: '商品数量', columns: 
				[ { // 9
					text: '数量',
					width: clientWidth * 0.05,
					dataIndex: 'goodsNumber',
					renderer : me.editArearColor,
					editor: {
						xtype: 'numberfield',
						allowBlank: false,
						minValue: 1,
						maxValue: 99999,
						allowDecimals : false
					}
				} , { // 10
					text: '缺货量',
					width: clientWidth * 0.05,
					dataIndex: 'lackNum',
					align : "center",
					renderer : me.editLackNumbgColor
				} ]
			},
			{ text: '当前商品尺码', dataIndex: 'sizeCode', hidden: true }, // 10
			{ text: '当前商品尺码', dataIndex: 'currSizeCode', hidden: true },  // 11
			{ text: '当前商品尺码列表', dataIndex: 'sizeChild', hidden: true }, // 12
			{ text: '当前商品颜色码', dataIndex: 'colorCode', hidden: true }, // 13
			{ text: '当前商品颜色码', dataIndex: 'currColorCode', hidden: true }, // 14
			{ text: '当前商品颜色码列表', dataIndex: 'colorChild', hidden: true }, // 15
			{ header: '规格', columns: 
				[ {// 16
					header : "颜色",
					width : clientWidth * 0.07,
					dataIndex: 'currColorName',
					sortable : false,
					renderer : me.editArearColor,
					editor: colorEditor
				} , {// 17
					header : "尺寸",
					width : clientWidth * 0.07,
					dataIndex: 'currSizeName',
					sortable : false,
					renderer : me.editArearColor,
					editor: sizeEditor
				} ]
			},// 16 17
			{ text: "属性", align : "center", width : clientWidth * 0.06, dataIndex : 'extensionCode', sortable : false,renderer:me.formatECode }, //18
			{ text: "属性Id", align : "center", hidden : true, dataIndex : 'extensionId', sortable : false },
			{ text: "商品Id", align : "center", hidden : true, dataIndex : 'indexId', sortable : false },
			{ text: '订单编号', dataIndex: 'orderSn', hidden: true, sortable : false},
			{ text: '促销规则', width: clientWidth * 0.08, dataIndex: 'promotionDesc' },
			{ text: '小计', width: clientWidth * 0.06, dataIndex: 'subTotal' },
			{ text: '初始积分金额', dataIndex: 'initIntegralMoney', hidden : true },
			{ text: '供应商编码', dataIndex: 'supplierCode', hidden : true },
			{ text: 'c2m属性json字符串', dataIndex: 'c2mItem', hidden : true },
			action
        ];
		Ext.apply(this, {
			plugins: {
				ptype: 'cellediting',
				clicksToEdit: 1,
				listeners: {
					beforeedit: 'onCellBeforeEdit',
					afteredit : 'onCellAfterEdit'
				}
			},
			selModel: {
				selType: 'cellmodel'
			},
			features: [{
				id: 'group',
				ftype: 'grouping',
//				groupHeaderTpl: '发货仓：<tpl if="name != null && name != 0 && name != \'DEFAULT\' ">{name}</tpl><tpl if="name == null || name == 0 || name == \'DEFAULT\'>未分仓</tpl>',
				groupHeaderTpl: '发货仓：{[(values.rows[0].get("depotCode") != null && values.rows[0].get("depotCode") != 0 && values.rows[0].get("depotCode") != \'DEFAULT\') ? values.rows[0].get("depotCode") : "未分仓" ]}' + 
								'发货状态：{[ values.rows[0].get("shippingStatusStr") ]}',
				hideGroupedHeader: true,
				enableGroupingMenu: false
//				remoteRoot: 'summaryData'
			}],
			columns: columns,
			//双击编辑商品属性
			listeners:{
				itemdblclick:function(dataview,record, item, index, e){
					var extensionCode = record.get('extensionCode');
					if(extensionCode=='c2b'||extensionCode=='c2m'){
					   	this.edit(record,extensionCode);
					}
				}
			},
			viewConfig : { enableTextSelection: true },
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
		});
		me.callParent();
	},
	recalculate : function (btn) { // 红包试算
		var form = btn.up("form");
		var params = btn.up("window").editOrderGoodsParams(form);
		var grid = form.down("ordereditlist");
		Ext.Ajax.request({
			url:  basePath + '/custom/editOrder/recalculate',
			params: params,
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				if (results.success) {
					grid.getStore().loadData(results.info.orderGoodsUpdateInfos, false);
					var shareBonus = 0.0000;
					grid.getStore().each(function(record) {
						var goodsNumber = record.get("goodsNumber");
						shareBonus += (record.get("shareBonus") * goodsNumber);
					});
					grid.down('#goodsBonusTotal').update('商品使用红包合计: ' + numFixed(shareBonus, 4) + " 元");
					Ext.msgBox.msg('红包试算', results.msg, Ext.MessageBox.INFO);
				} else {
					Ext.msgBox.remainMsg('红包试算', results.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var text = response.responseText;
				Ext.msgBox.remainMsg('红包试算', text, Ext.MessageBox.ERROR);
				// process server response here
			}
		});
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
		var shippingStatus = record.get('shippingStatus');
		var editable = true;
//		console.dir(editor);
//		console.dir(ctx);
		// 已发货 禁用编辑
		var shippingStatus = record.get('shippingStatus');
		if (shippingStatus > 0) {
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
//		console.dir(record);
//		console.dir(clickColIdx);
	},
	onCellAfterEdit: function(editor, ctx, eOpts) { // 修改商品信息后动态变化
		var clickColIdx = ctx.colIdx; // grid column 展示列下标
		var record = ctx.record;
//		console.dir(ctx);
//		console.dir(ctx.colIdx);
//		console.dir(record);
		// 已发货 禁用编辑
		var shippingStatus = record.get('shippingStatus');
		if (shippingStatus > 0) {
			return false;
		}
		var value = ctx.value;
		if (clickColIdx == 4 || clickColIdx == 6 || clickColIdx == 8) { // 成交价 || 红包分摊金额 ||商品数量
			var transactionPrice = ctx.record.get("transactionPrice");
			var goodsNumber = ctx.record.get("goodsNumber");
			var initGoodsNumber = ctx.record.get("initGoodsNumber");
			var shareBonus = ctx.record.get("shareBonus");
			var initLackNum = ctx.record.get("initLackNum");
			var initIntegralMoney = ctx.record.get("initIntegralMoney");
			var c2mItem = ctx.record.get("c2mItem");
			// 重新分摊积分数量
			console.dir(initIntegralMoney);
			var perIntegralMoney = 0.00;
			if (initIntegralMoney > 0) {
				var totalIntegralMoney = initIntegralMoney * initGoodsNumber;
				perIntegralMoney = totalIntegralMoney/goodsNumber;
				ctx.record.set("integralMoney", numFixed(perIntegralMoney, 4));
			}
			ctx.record.set("settlementPrice", numFixed(transactionPrice - shareBonus - perIntegralMoney, 4));
			ctx.record.set("subTotal", transactionPrice * goodsNumber);
			// 缺货商品数量
			if (initLackNum && initLackNum > 0) {
//				console.log("changeNum" + changeNum);
//				console.log("newlackNum" + (initLackNum - changeNum));
				var changeNum = initGoodsNumber - goodsNumber;
				if (initLackNum - changeNum < 0 ) {
					ctx.record.set("lackNum", 0);
				} else {
					ctx.record.set("lackNum", initLackNum - changeNum);
				}
			}
			//商品数量更新到c2mItem字段值
			var obj = JSON.parse(c2mItem);
			obj.num = goodsNumber;
			ctx.record.set("c2mItem",JSON.stringify(obj));
		}
		if (clickColIdx == 10 || clickColIdx == 11) { // 选择颜色码和尺码后对颜色码尺码组合做有效性验证
//			console.dir(editor);
			var currColorName = ctx.record.get("currColorName");
			var currSizeName = ctx.record.get("currSizeName");
			var sizeChild = ctx.record.get("sizeChild");
			var colorChild = ctx.record.get("colorChild");
			var goodsNumber = ctx.record.get("goodsNumber");
			var sizeList = record.get("sizeChild");
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
					shopCode: parent.channelCode,
					"goodsSn" : goodsSn,
					"type": 1
				};
				Ext.Ajax.request({
					url: basePath + '/custom/common/getCustomStock',
					params: params,
					success: function(response){
						var text = response.responseText;
						var results = Ext.JSON.decode(text);
//						console.dir( results );
						if (results.isok) {
							// 有效性验证通过之后修改颜色码尺码
//							ctx.record.set("currColorCode", obj.colorCode);
//							ctx.record.set("currSizeCode", obj.sizeCode);
							ctx.record.set("customCode", results.data[0].sku);
//							if (results.data[0].allStock == 0) {
//								Ext.msgBox.msg('验证商品SKU', results.data[0].sku + "库存为0", Ext.MessageBox.ERROR);
//							}
						} else {
							// 有效性验证没有通过将修改颜色码尺码名称修改为原来值
							if (clickColIdx == 10) {
								ctx.record.set("currColorName", ctx.originalValue);
							} else {
								ctx.record.set("currSizeName", ctx.originalValue);
							}
							Ext.msgBox.remainMsg('验证商品SKU', results.message, Ext.MessageBox.ERROR);
						}
					},
					failure: function(response){
						var text = response.responseText;
						Ext.msgBox.msg('验证商品SKU', text, Ext.MessageBox.ERROR);
					}
				});
			}
//			console.dir(selectColorCode);
//			console.dir(selectSizeCode);
//			ctx.record.set("subTotal", transactionPrice * goodsNumber);
		}
//		else if (clickColIdx == 5) { // 红包分摊金额修改
//			var shareBonus = ctx.record.get("shareBonus");
//			var goodsNumber = ctx.record.get("goodsNumber");
//			ctx.grid.down('#goodsHonus').update('红包合计:111');
//		}
		ctx.grid.recalculateTotal(ctx.grid);
	},
	onRemoveClick: function(rowIndex){ // 删除商品
		var num = this.getStore().getData().length;
		if (num == 1) {
			Ext.msgBox.remainMsg('删除商品', "商品数据不能全部删除,必须保留一条记录！", Ext.MessageBox.ERROR);
			return ;
		}
		this.getStore().removeAt(rowIndex);
		Ext.msgBox.msg('删除商品', "删除成功！", Ext.MessageBox.INFO);
		this.recalculateTotal(this);
	},
	changeColorOrSize : function(combo, newValue ,oldValue) {
//		console.dir(this.up("editor"));
//		console.dir(this.up("record"));
//		var record = ctx.record;
//		if (clickColIdx == 3 || clickColIdx == 6) { // 成交价 || 商品数量
//			var transactionPrice = ctx.record.get("transactionPrice");
//			var goodsNumber = ctx.record.get("goodsNumber");
//			ctx.record.set("subTotal", transactionPrice * goodsNumber);
//		}
		// merger
//		var win = this.up("window");
//		var form = this.up("form");
//		var params = win.editOrderGoodsParams(form);
//		params.method =  'merger';
//		params.orderSn = parent.orderSn;
//		form.load({
//			url : basePath + '/custom/editOrder/editOrderGoods',
//			params : params,
//			success : function(form, action) {
//				// 数据加载成功后操作
//				win.initPage(win, form);
//				Ext.msgBox.msg('合并商品', '合并商品成功', Ext.MessageBox.INFO);
//			},
//			failure : function(form, action) {
//				// 数据加载失败后操作
//			}
//		});
	},
	uploadC2BImg : function (file, record) {
		var form = file.up('form');
//		console.dir(record);
		form.submit({
			url: basePath + 'custom/common/upload',
			params: {'sku' : record.get("customCode"), "type" : 'C2B'},
			waitMsg: 'Uploading your photo...',
			success: function(fp, action) {
				if (action.result.success == "true") {
					Ext.msgBox.msg('上传图片', "上传成功！", Ext.MessageBox.INFO);
					record.set("madeUrl", action.result.msg)
				} else {
					Ext.msgBox.remainMsg('上传图片', action.result.msg, Ext.MessageBox.ERROR);
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg('上传图片', action.result.msg, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		});
	},
	formatECode : function (value, meta, record) {
		if (value == null || value== '' || value == 'common') {
			return '普通商品';
		} else if (value == 'gift') {
			return '赠品';
		} else if (value == 'gift1') {
			return '赠品';
		} else if (value == 'c2b') {
			return 'C2B商品';
		} else if (value == 'group') {
			return '套装';
		} else if (value == 'prize') {
			return '奖品';
		}else if (value == 'c2m') {
			return 'C2M商品';
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
	edit : function(record,extensionCode){
		var c2mItem = record.get("c2mItem");
    	var obj = JSON.parse(c2mItem);
		var win = Ext.widget("c2xPropertyEdit");
		//获取颜色下拉菜单列表
		var colorCombo =  Ext.getCmp("c2xBasePropertyColorCombo");
		Ext.Ajax.request({
			timeout: 1800000,//1800秒 半小时
			url: basePath + 'custom/editOrder/getPropertyListByClsId.spmvc',
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
			url: basePath + 'custom/editOrder/getPropertyListByClsId.spmvc',
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
		var c2xMeasure = Ext.getCmp("c2xMeasure").store;//c2m定制信息
		var c2bCoordinage = Ext.getCmp("c2bCoordinage").store;//c2b坐标信息
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
			c2xMeasure.add(measureObj);
			Ext.getCmp("c2xMeasure").hidden=false;
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
			c2bCoordinage.add(elementObj);
			Ext.getCmp("c2bCoordinage").hidden=false;
		}
		basePropertyForm.findField('diyId').setValue(obj.diyId);//定制ID
		basePropertyForm.findField('num').setValue(obj.num);//购买数量
		basePropertyForm.findField('sn').setValue(obj.sn);//商品6位码
		basePropertyForm.findField('clothesID').setValue(obj.clothesID);//商品11位码
		basePropertyForm.findField('sellerPrice').setValue(obj.sellerPrice);//商品价格
		basePropertyForm.findField('customCode').setValue(record.get('customCode'));//sku

		win.addListener('beforeclose',function handler(){
			//获取window上的字段值  重新拼装c2mItem再set回record
			var newC2mItemStr = '';
			//拼装基本属性信息
			newC2mItemStr +='{'
							+'"num":"'+basePropertyForm.findField('num').value+'",'
							+'"diyId":"'+basePropertyForm.findField('diyId').value+ '",'
							+'"sn":"'+basePropertyForm.findField('sn').value+ '",'
							+'"size":"'+basePropertyForm.findField('size').value+ '",'
							+'"color":"'+basePropertyForm.findField('color').value+ '",'
							+'"clothesID":"'+basePropertyForm.findField('clothesID').value+ '",'
							+'"sellerPrice":"'+basePropertyForm.findField('sellerPrice').value+ '",'
			if(extensionCode=='c2m'){
				newC2mItemStr += '"buyer":"'+basePropertyForm.findField('buyer').value+ '",'//定制人
								+'"cost":"'+basePropertyForm.findField('cost').value+ '",'//定制费用
								+'"picPath1":"'+basePropertyForm.findField('c2mPicPath').value+ '",'//图片  取值注意
				//拼装measure特殊定制信息
				newC2mItemStr += '"measure":['
				c2xMeasure.each(function (record) {
					newC2mItemStr += '{'
						+'"value":"'+record.get('value')+'",'
						+'"id":"'+record.get('id')+'",'
						+'"name":"'+record.get('name')+'"'
						+'},'
				});
				newC2mItemStr = newC2mItemStr.substring(0,newC2mItemStr.length-1);
				newC2mItemStr += ']}';
			}else if(extensionCode=='c2b'){
				newC2mItemStr += '"name":"'+basePropertyForm.findField('name').value+ '",'//定制人
				+'"totalPrice":"'+basePropertyForm.findField('totalPrice').value+ '",'//定制费用
				+'"picPath1":"'+basePropertyForm.findField('c2bPicPath1').value+ '",'//取值注意
				+'"picPath2":"'+basePropertyForm.findField('c2bPicPath2').value+ '",'//取值注意
				//拼装坐标信息
				newC2mItemStr += '"element":['
				c2bCoordinage.each(function (record) {
					newC2mItemStr += '{'
						+'"id":"'+record.get('id')+'",'
						+'"x":"'+record.get('x')+'",'
						+'"y":"'+record.get('y')+'",'
						+'"width":"'+record.get('width')+'",'
						+'"height":"'+record.get('height')+'"'
						+'},'
				});
				newC2mItemStr = newC2mItemStr.substring(0,newC2mItemStr.length-1);
				newC2mItemStr += ']}';
			}
			record.set('c2mItem',newC2mItemStr);
		});
		win.show();
	} 
	
	
});