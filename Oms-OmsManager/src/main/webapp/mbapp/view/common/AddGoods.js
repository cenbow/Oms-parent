Ext.define("MB.view.common.AddGoods", {
	extend: "Ext.panel.Panel",
	alias: 'widget.commonaddgoods',
	title: '添加商品',
	width: '100%',
	requires : [ 'MB.view.common.ProductLibBrandCombo',
				'MB.view.common.ProductLibCategoryCombo',
				'MB.view.common.SearchGoodsCombo',
				'MB.view.common.GoodsColorCombo',
				'MB.view.common.GoodsSizeCombo',
				'MB.model.ComboModel'],
	frame: true,
	autoWidth:true,
	autoHeight:true, 
	autoScroll : true,
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'right'
	},
	bodyBorder:false,
	border:false,
	margin : '0 0 0 40',
	style:'border-width:0 0 0 0;',
	initComponent: function () {
		var me = this;
		var extensionData;
		if (parent.activityType == 'c2b') {
			extensionData = [
				{v: '普通商品', n: 'common'},
				{v: '赠品', n: 'gift'},
				{v: 'C2B', n: 'c2b'}
			]
		} else {
			extensionData = [
				{v: '普通商品', n: 'common'},
				{v: '赠品', n: 'gift'}
			]
		}
		this.items = [ {
			xtype: 'form',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			margin : '8 0 8 0',
			items : {
				xtype : 'form',
				layout: 'column',
				url : basePath + '/custom/editOrder/searchGoods',
				items: [
//					{ xtype: 'commonproductlibbrandcombo' },
//					{ xtype: 'commonproductlibcategorycombo' },
					{
						xtype: 'textfield',
						name : 'goodsSn',
						fieldLabel : '商品货号(或者11位码)',
						labelWidth: 150
//						allowBlank : false
					},
//					{ xtype: 'textfield', name : 'goodsName', fieldLabel : '商品名称' },
//					{ xtype: 'hidden', name : 'channelCode', fieldLabel : '店铺Code', itemId : "channelCode", value : 'HQ01S115'},
					{
						xtype: 'button' ,
						text : '检索商品',
						margin: '0 0 0 10',
						action : 'searchGoods',
						handler : me.searchGoods
					}
				]
			}
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 100,
			items: [
				{
					xtype: 'commonsearchgoodscombo',
					fieldLabel: '检索商品',
					width: 450,
					listeners:{
						'change': me.changeGoods
					}
				},
				{ xtype: 'button' , text : '查看库存', margin: '0 0 0 490', handler : me.queryStock }
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 100,
			items: [
				{
					xtype: 'commongoodscolorcombo',
					disabled : true,
					forceSelection : true,
					listeners:{
						'change': me.changeColorCodeOrSizeCode
					}
				},
				{
					xtype: 'commongoodssizecombo',
					disabled : true,
					forceSelection : true,
					listeners:{
						'change': me.changeColorCodeOrSizeCode
					}
				},
				{ xtype: 'hidden' , fieldLabel : '11位码', itemId : 'customCode'},
				{ xtype: 'hidden' , fieldLabel : '6位码', itemId : 'selectColorCode'},
				{ xtype: 'hidden' , fieldLabel : '6位码', itemId : 'selectSizeCode'},
				{ xtype: 'hidden' , fieldLabel : '6位码', itemId : 'selectGoodsSn'},
				{ xtype: 'hidden' , fieldLabel : '商品名', itemId : 'selectGoodsName'},
				{ xtype: 'textfield' , fieldLabel : '库存量', readOnly : true, itemId : 'skuStock'},
				{
					xtype: 'numberfield',
					itemId : 'addGoodsNumber',
					fieldLabel : '添加数量',
					value: 1,
					minValue: 1,
					maxValue: 999999,
					allowDecimals : false
				},
				{ xtype: 'button' , text : '添加商品', margin: '0 0 0 10', itemId: 'addGoodsButton', handler : me.addOrderGoods}
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 100,
			items: [
//			   {
//				xtype: 'radiogroup',
//				fieldLabel: '价格',
//				width : 600,
//				items: [
//					{boxLabel: '市场价[0.00]', name: 'goodsPrice', inputValue: 1, checked: true},
//					{boxLabel: '本店价[0.00]', name: 'goodsPrice', inputValue: 2},
//					{boxLabel: '会员价[0.00]', name: 'goodsPrice', inputValue: 3},
//					{boxLabel: '自定义', name: 'goodsPrice', inputValue: 4}
//				],
//				listeners : {
//					'change': me.changePrice
//				}
//			} , 
			{
				xtype : 'combo',
				fieldLabel: '价格',
				itemId : 'goodsPriceCombo',
				store : new Ext.data.Store({
					fields: ['price', 'priceDesc']
				}),
				queryMode: 'local',
				displayField: 'priceDesc',
				valueField: 'price',
				hiddenName: 'searchgoodsSn',
				emptyText: '请选择商品价格',
				editable: false,
				disabled : true,
				listeners : {
					'change': me.changePrice
				}
			} , {
				xtype: 'numberfield',
				name : 'customPrice',
				itemId : 'customPrice',
				disabled : true,
				margin : '0 0 0 5',
				minValue: 0,
				maxValue: 999999.99,
				decimalPrecision : 2
			} , {
				xtype : 'hidden',
				itemId : 'goodsPrice'
			} , {
				xtype : 'combo',
				fieldLabel: '商品属性',
				itemId : 'goodsExtension',
				store : new Ext.data.Store({
					data: extensionData,
					model: "MB.model.ComboModel"
				}),
				queryMode: 'local',
				displayField: 'v',
				valueField: 'n',
				hiddenName: 'extensionCode',
				emptyText: '请选择商品属性',
				editable: false,
				disabled : true
			}]
		} ];
		this.callParent(arguments);
	},
	changeGoods: function (combo, newValue ,oldValue) { // 查询商品列表选择
		// 商品编码有变化时,加载尺码和颜色码列表
		if (newValue != null && newValue != '' && newValue != oldValue) {
			var goodsPanel = this.up("panel");
			// 查询商品切换时 颜色码列表、尺码列表  数据加载 
			var commongoodscolorcombo = goodsPanel.down("commongoodscolorcombo");
			var commongoodssizecombo = goodsPanel.down("commongoodssizecombo");
			commongoodscolorcombo.setDisabled( false );
			commongoodssizecombo.setDisabled( false );
			var params;
			/*commongoodssizecombo.store.on('beforeload', function (store){
				params = {"goodsSn" : newValue, "type" : 1};
				Ext.apply(store.proxy.extraParams, params);
			});*/
			params = {"goodsSn" : newValue, "type" : 1};
			commongoodssizecombo.store.load({params : params});
			var selectSizeCode = goodsPanel.down("#selectSizeCode").getValue();
			if (selectSizeCode && selectSizeCode != '') {
				commongoodssizecombo.setValue(selectSizeCode);
				goodsPanel.down("#selectSizeCode").setValue(null);
			}
			/*commongoodscolorcombo.store.on('beforeload', function (store){
				params = {"goodsSn" : newValue, "type" : 2};
				Ext.apply(store.proxy.extraParams, params);
			});*/
			var params2 = {"goodsSn" : newValue, "type" : 2};
			commongoodscolorcombo.store.load({params : params2});
			var selectColorCode = goodsPanel.down("#selectColorCode").getValue();
			if (selectColorCode && selectColorCode != '') {
				commongoodscolorcombo.setValue(selectColorCode);
				goodsPanel.down("#selectColorCode").setValue(null);
			}
			var goodsPriceCombo = goodsPanel.down("#goodsPriceCombo");
			goodsPriceCombo.setDisabled( false );
			combo.getStore().each(function(record, i) {
				if (newValue == record.get("goodsSn")) {
					var priceDesc = new Array();
					var channelPrice = record.get("channelPrice");
					var marketPrice = record.get("marketPrice");
					priceDesc.push({"priceDesc" : "市场价" + marketPrice, "price" : "marketPrice," + marketPrice});
					priceDesc.push({"priceDesc" : "本店价" + channelPrice, "price"  : "channelPrice," + channelPrice});
					priceDesc.push({"priceDesc" : "自定义", "price" : "custom"});
					goodsPriceCombo.getStore().loadData(priceDesc, false);
					goodsPriceCombo.setValue( "channelPrice," + channelPrice);
					goodsPanel.down("#goodsPrice").setValue(channelPrice);
				}
			});
			var goodsExtensionCombo = goodsPanel.down("#goodsExtension");
			goodsExtensionCombo.setDisabled( false );
			goodsExtensionCombo.setValue("common");
			
			goodsPanel.down("#selectGoodsSn").setValue(newValue);
			goodsPanel.down("#selectGoodsName").setValue(combo.getRawValue());
			
			// 添加商品数量
			var addGoodsNumber = goodsPanel.down("#addGoodsNumber");
			addGoodsNumber.setDisabled( false );
			// 添加商品按钮
			var addGoodsButton = goodsPanel.down("#addGoodsButton");
			addGoodsButton.setDisabled( false );
		}
	},
	searchGoods: function(btn) { // 查询商品
		var form = btn.up("form");
		var params = form.getValues();
		if (params.goodsSn == null || params.goodsSn == "") {
			Ext.msgBox.remainMsg('验证', "请填写【商品货号(或者11位码)】再查询！", Ext.MessageBox.ERROR);
			return ;
		}
		params.channelCode = parent.channelCode;
		Ext.Ajax.request({
			url:  basePath + '/custom/editOrder/searchGoods',
			params: params,
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				var editGoodsPanel = btn.up("form").up("form").up("panel");
//				console.dir(results);
				if (results != null && results.isok == true && results.data.length > 0) {
					// 查询结果成功后
					// 颜色码列表、尺码列表 不可选
					// 展示查询商品列表
					// 颜色码列表
					var commongoodscolorcombo = editGoodsPanel.down("commongoodscolorcombo");
					commongoodscolorcombo.clearValue();
					commongoodscolorcombo.setDisabled( true );
					// 尺码列表
					var commongoodssizecombo = editGoodsPanel.down("commongoodssizecombo");
					commongoodssizecombo.clearValue();
					commongoodssizecombo.setDisabled( true );
//					// 商品价格radio展示
					var goodsPriceCombo = editGoodsPanel.down("#goodsPriceCombo");
					goodsPriceCombo.clearValue();
					goodsPriceCombo.setDisabled( true );
					// 添加商品数量
					var addGoodsNumber = editGoodsPanel.down("#addGoodsNumber");
					addGoodsNumber.setDisabled( true );
					addGoodsNumber.setValue(1);
					// 添加商品按钮
					var addGoodsButton = editGoodsPanel.down("#addGoodsButton");
					addGoodsButton.setDisabled( true );
					
					var commonsearchgoodscombo = editGoodsPanel.down("commonsearchgoodscombo");
					commonsearchgoodscombo.clearValue();
					commonsearchgoodscombo.store.loadData(results.data ,false);
					if (params.goodsSn != undefined && params.goodsSn != null) {
						if (params.goodsSn.length == 6) {
							commonsearchgoodscombo.setValue(params.goodsSn);
						} else {
//							console.dir(params.goodsSn.substring(0,6));
							editGoodsPanel.down("#selectSizeCode").setValue(results.data[0].currSizeCode);
							editGoodsPanel.down("#selectColorCode").setValue(results.data[0].currColorCode);
							commonsearchgoodscombo.setValue(params.goodsSn.substring(0,6));
						}
					}
					Ext.msgBox.msg('查询商品', "查询商品成功！", Ext.MessageBox.INFO);
				} else {
					var panel = btn.up("form").up("form").up("panel");
					var commongoodscolorcombo = editGoodsPanel.down("commongoodscolorcombo");
					commongoodscolorcombo.clearValue();
					commongoodscolorcombo.setDisabled( true );
					// 尺码列表
					var commongoodssizecombo = editGoodsPanel.down("commongoodssizecombo");
					commongoodssizecombo.clearValue();
					commongoodssizecombo.setDisabled( true );
//					// 商品价格radio展示
					var goodsPriceCombo = editGoodsPanel.down("#goodsPriceCombo");
					goodsPriceCombo.clearValue();
					goodsPriceCombo.setDisabled( true );
					// 添加商品数量
					var addGoodsNumber = editGoodsPanel.down("#addGoodsNumber");
					addGoodsNumber.setDisabled( true );
					addGoodsNumber.setValue(1);
					// 添加商品按钮
					var addGoodsButton = editGoodsPanel.down("#addGoodsButton");
					addGoodsButton.setDisabled( true );
					
					var commonsearchgoodscombo = editGoodsPanel.down("commonsearchgoodscombo");
					commonsearchgoodscombo.clearValue();
					Ext.msgBox.remainMsg('查询商品', results.message, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var text = response.responseText;
//				console.dir('failure:' + text);
			}
		});
	},
	changeColorCodeOrSizeCode: function (combo, newValue ,oldValue) { // 选择查询商品的颜色码尺码时,定位到相应的11位码和6位码
		// 商品编码有变化时,加载尺码和颜色码列表
//		console.dir(combo);
		var goodsPanel = this.up("panel");
		var colorCode;
		var sizeCode;
		if (combo.xtype == 'commongoodscolorcombo') {
			var commongoodssizecombo = goodsPanel.down("commongoodssizecombo");
			sizeCode = commongoodssizecombo.getValue();
			colorCode = newValue;
			//联动商品尺码选项
			/*console.dir('commongoodscolorcombo' + newValue);
			if(newValue!=''&&newValue!=null&&newValue!=undefined){
				var goodsSn = goodsPanel.down("#selectGoodsSn").getValue();
				params = {"colorCode": newValue, "goodSn" : goodsSn};
				Ext.Ajax.request({
					url: basePath + '/custom/editOrder/getSizeListByColorCode',
					params: params,
					success: function(response){
						var respText = Ext.JSON.decode(response.responseText);
						console.dir( respText.list);
						if(respText.code=='1'){
							commongoodssizecombo.store.removeAll();
							commongoodssizecombo.store.loadData(respText.list);
						}else{
							commongoodssizecombo.store.loadData([]);
							commongoodssizecombo.clearValue();
						}
					},
					failure: function(response){
						
					}
				});
			}*/
		} else if (combo.xtype == 'commongoodssizecombo') {
			var commongoodscolorcombo = goodsPanel.down("commongoodscolorcombo");
			colorCode = commongoodscolorcombo.getValue();
			sizeCode = newValue;
			//联动商品颜色选项
			console.dir('commongoodssizecombo' + newValue);
			/*if(newValue!=''&&newValue!=null&&newValue!=undefined){
				var goodsSn = goodsPanel.down("#selectGoodsSn").getValue();
				params = {"sizeCode": newValue, "goodSn" : goodsSn};
				Ext.Ajax.request({
					url: basePath + '/custom/editOrder/getColorListBySizeCode',
					params: params,
					success: function(response){
						var respText = Ext.JSON.decode(response.responseText);
						if(respText.code=='1'){
							commongoodscolorcombo.store.removeAll();
							commongoodscolorcombo.store.loadData(respText.list);
						}else{
							commongoodscolorcombo.store.loadData([]);
							commongoodscolorcombo.clearValue();
						}
					},
					failure: function(response){
						
					}
				});
			}*/
		}
		// 添加商品按钮
		var addGoodsButton = goodsPanel.down("#addGoodsButton");
		if (sizeCode != null && sizeCode != "" && colorCode != null && colorCode != "") {
			var goodsSn = goodsPanel.down("#selectGoodsSn").getValue();
			params = {"colorCode": colorCode,"sizeCode": sizeCode, shopCode: parent.channelCode, "goodsSn" : goodsSn,"type": 1};
			Ext.Ajax.request({
				url: basePath + '/custom/common/getCustomStock',
				params: params,
				success: function(response){
					var text = response.responseText;
					var results = Ext.JSON.decode(text);
//					console.dir( results );
					if (results.isok) {
						addGoodsButton.setDisabled( false );
						goodsPanel.down("#customCode").setValue(results.data[0].sku);
						goodsPanel.down("#skuStock").setValue(results.data[0].allStock);
					} else {
						// 添加商品按钮
						addGoodsButton.setDisabled( true );
						Ext.msgBox.remainMsg('验证商品SKU', results.message, Ext.MessageBox.ERROR);
					}
				},
				failure: function(response){
					var text = response.responseText;
//					console.dir('failure:' + text);
				}
			});
		}
	},
	changePrice: function (radio, newValue, oldValue) { // 查询商品列表选择
		// 商品编码有变化时,加载尺码和颜色码列表
		if (newValue != null && newValue != '' && newValue != oldValue) {
//			console.dir(this.up("panel"));
			if (newValue != null && newValue != '' && newValue != oldValue) {
				if (newValue == 'custom') {
					this.up("panel").down("#customPrice").setDisabled(false);
				} else {
					var priceDesc = newValue.split(",");
					this.up("panel").down("#customPrice").setValue(priceDesc[1]);
					this.up("panel").down("#customPrice").setDisabled(true);
				}
			}
		}
	},
	addOrderGoods: function(btn) { // 添加选择商品至订单
		var goodsPanel = btn.up("panel");
		var goodsColorCode = goodsPanel.down("commongoodscolorcombo").getValue();
		var goodsSizeCode = goodsPanel.down("commongoodssizecombo").getValue();
		var addGoodsNumber = goodsPanel.down("#addGoodsNumber").getValue();
		var customPrice = goodsPanel.down("#customPrice").getValue();
		var extensionCode = goodsPanel.down("#goodsExtension").getValue();
//		console.dir(extensionCode);
		if (goodsColorCode == null || goodsColorCode == '') {
			Ext.msgBox.remainMsg('验证', "请选择商品颜色", Ext.MessageBox.ERROR);
			return;
		}
		if (goodsSizeCode == null || goodsSizeCode == '') {
			Ext.msgBox.remainMsg('验证', "请选择商品尺码", Ext.MessageBox.ERROR);
			return;
		}
		if (addGoodsNumber == null || addGoodsNumber == '') {
			Ext.msgBox.remainMsg('验证', "请填写添加商品数量", Ext.MessageBox.ERROR);
			return;
		}
		if (customPrice == undefined || customPrice == null) {
			Ext.msgBox.remainMsg('验证', "请填写添加商品价格", Ext.MessageBox.ERROR);
			return;
		}
		var customCode = goodsPanel.down("#customCode").getValue();

		var goodsSn = goodsPanel.down("#selectGoodsSn").getValue();
		var goodsName = goodsPanel.down("#selectGoodsName").getValue();
		var ordereditlist = goodsPanel.up('window').down("ordereditlist");
		var flag = false;
		ordereditlist.getStore().each(function(record, i) {
			var gridCustomCode = record.get("customCode");
			var gridExtensionCode = record.get("extensionCode");
//			var gridExtensionId = record.get("extensionId");
			if (extensionCode != "c2b") {
				if ((customCode + extensionCode) == (gridCustomCode + gridExtensionCode)) {
					flag = true;
				}
			}
		});
		if (flag) {
			Ext.msgBox.remainMsg('验证', "添加的商品[SkuSn:" + customCode + ";商品名称：" +  goodsName + "]在商品列表已经存在,请修改商品数量",
					Ext.MessageBox.ERROR);
			return;
		}
		var goodsPrice = goodsPanel.down("#goodsPrice").getValue();
		var params = goodsPanel.up("window").editOrderGoodsParams(goodsPanel.up("form"));
		params['addOrderGoods.goodsSn'] = goodsSn;
		params['addOrderGoods.goodsName'] = goodsName;
		params['addOrderGoods.customCode'] = customCode;
		params['addOrderGoods.currColorCode'] = goodsColorCode;
		params['addOrderGoods.currSizeCode'] = goodsSizeCode;
		params['addOrderGoods.goodsNumber'] = addGoodsNumber;
		params['addOrderGoods.initGoodsNumber'] = addGoodsNumber;
		params['addOrderGoods.goodsPrice'] = goodsPrice;
		params['addOrderGoods.shareBonus'] = 0;
		params['addOrderGoods.transactionPrice'] = customPrice;
		params['addOrderGoods.settlementPrice'] = customPrice;
		params['addOrderGoods.extensionCode'] = extensionCode;
		params['addOrderGoods.extensionId'] = 0;
		params['addOrderGoods.orderSn'] = parent.orderSn;
		params['addOrderGoods.depotCode'] = "DEFAULT";
		if (parent.editGoodsType == 0 || parent.editGoodsType == 3) {
			params['addGoodsType'] = "0";
		} else {
			params['addGoodsType'] = "1";
		}
		params.method = 'add';
		params['channelCode'] = parent.channelCode;
		params['orderSn'] = parent.orderSn;
		// Ext Ajax Request
		Ext.Ajax.request({
			url:  basePath + '/custom/editOrder/editOrderGoods',
			params: params,
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				if (results.success) {
					ordereditlist.getStore().loadData(results.info.orderGoodsUpdateInfos, false);
					ordereditlist.recalculateTotal(ordereditlist);
					Ext.msgBox.msg('添加商品', "添加成功！", Ext.MessageBox.INFO);
				} else {
					Ext.msgBox.remainMsg('添加商品', results.info.message, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var text = response.responseText;
//				console.dir('failure:' + text);
			}
		});
	},
	selectGoodsStock: function (combo, newValue ,oldValue) { // 查询选择商品库存
		// 商品编码有变化时,加载尺码和颜色码列表
		if (newValue != null && newValue != '' && newValue != oldValue) {
//			console.dir(this.up("panel"));
			var commongoodscolorcombo = this.up("panel").down("commongoodscolorcombo");
			var commongoodssizecombo = this.up("panel").down("commongoodssizecombo");
			commongoodscolorcombo.setDisabled( false );
			commongoodssizecombo.setDisabled( false );
			var params;
			commongoodssizecombo.store.on('beforeload', function (store){
				params = {"goodsSn" : newValue, "type" : 1};
				Ext.apply(store.proxy.extraParams, params);
			});
			commongoodssizecombo.store.load({params : params});
			commongoodscolorcombo.store.on('beforeload', function (store){
				params = {"goodsSn" : newValue, "type" : 2};
				Ext.apply(store.proxy.extraParams, params);
			});
			commongoodscolorcombo.store.load();
		}
	},
});