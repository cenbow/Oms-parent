Ext.define("MB.view.orderEdit.AddGoodsModule", {
	extend: "Ext.panel.Panel",
	alias: 'widget.addGoodsModule',
	title: '添加商品',
	width: '100%',
	requires : ['MB.view.common.ProductLibBrandCombo',
				'MB.view.common.ProductLibCategoryCombo',
				'MB.view.common.SearchGoodsCombo',
				'MB.store.SearchGoodss',
				'MB.view.commonComb.GoodsColorComb',
				'MB.view.commonComb.GoodsSizeComb',
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
		this.items = [{
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
				items: [
					{
						xtype: 'textfield',
						name : 'goodsSn',
						fieldLabel : '商品货号',
						labelWidth: 150
					},
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
				{ xtype: 'textfield' , fieldLabel : '名称', readOnly : true, itemId : 'goodsName'},
				{ xtype: 'textfield' , fieldLabel : '颜色', readOnly : true, itemId : 'colorName'},
				{ xtype: 'textfield' , fieldLabel : '尺码', readOnly : true, itemId : 'sizeName'},
				{ xtype: 'hidden' , fieldLabel : '11位码', itemId : 'customCode'},
				{ xtype: 'hidden' , fieldLabel : 'baseBvValue', itemId : 'baseBvValue'},
				{ xtype: 'hidden' , fieldLabel : 'bvValue', itemId : 'bvValue'},
				{ xtype: 'hidden' , fieldLabel : 'selectGoodsSn', itemId : 'selectGoodsSn'},
				{ xtype: 'hidden' , fieldLabel : 'marketPrice', itemId : 'marketPrice'},
				{ xtype: 'hidden' , fieldLabel : 'goodsThumb', itemId : 'goodsThumb'},
				{ xtype: 'hidden' , fieldLabel : 'intlCode', itemId : 'intlCode'},
				{ xtype: 'hidden' , fieldLabel : 'salesMode', itemId : 'salesMode'},
				{ xtype: 'hidden' , fieldLabel : 'depotCode', itemId : 'depotCode'},
				{ xtype: 'hidden' , fieldLabel : 'supplierCode', itemId : 'supplierCode'},
				{
					xtype: 'numberfield',
					itemId : 'addGoodsNumber',
					fieldLabel : '添加数量',
					value: 1,
					minValue: 1,
					maxValue: 999999,
					allowDecimals : false
				},
				{ xtype: 'button' , text : '添加商品', margin: '0 0 0 10', itemId: 'addGoodsButton', handler : me.addOrderGoods, disabled : false}
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 100,
			items: [/*{
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
			} ,*/ {
				fieldLabel: '价格',
				xtype: 'numberfield',
				name : 'goodsPrice',
				itemId : 'goodsPrice',
				margin : '0 0 0 5',
				minValue: 0,
				maxValue: 999999.99,
				decimalPrecision : 2
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
				editable: false
			}]
		} ];
		this.callParent(arguments);
	},
	changeGoods: function (combo, newValue ,oldValue) { // 查询商品列表选择
		// 商品编码有变化时,加载尺码和颜色码列表
		if (newValue != null && newValue != '' && newValue != oldValue) {
			var goodsPanel = this.up("panel");
			// 查询商品切换时 颜色码列表、尺码列表  数据加载 
			var goodsColorComb = goodsPanel.down("goodsColorComb");
			var goodsSizeComb = goodsPanel.down("goodsSizeComb");
			goodsColorComb.setDisabled( false );
			goodsSizeComb.setDisabled( false );
			var params;
			params = {"goodsSn" : newValue, "type" : 1};
			goodsSizeComb.store.load({params : params});
			var selectSizeCode = goodsPanel.down("#selectSizeCode").getValue();
			if (selectSizeCode && selectSizeCode != '') {
				goodsSizeComb.setValue(selectSizeCode);
				goodsPanel.down("#selectSizeCode").setValue(null);
			}
			var params2 = {"goodsSn" : newValue, "type" : 2};
			goodsColorComb.store.load({params : params2});
			var selectColorCode = goodsPanel.down("#selectColorCode").getValue();
			if (selectColorCode && selectColorCode != '') {
				goodsColorComb.setValue(selectColorCode);
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
		//拼装入参
		var params = form.getValues();
		if (params.goodsSn == null || params.goodsSn == "") {
			Ext.msgBox.remainMsg('验证', "请填写【商品货号】再查询！", Ext.MessageBox.ERROR);
			return ;
		}
		params.channelCode = channelCode;
		params.userId = userId;
		console.dir(channelCode);
		//查询
		Ext.Ajax.request({
			url:  basePath + '/custom/orderGoodsEdit/searchGoods',
			params: params,
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				var editGoodsPanel = btn.up("form").up("form").up("panel");
				if (results.code=='1'&&results.bgGoodsInfo != null) {
					var bgGoodsInfo = results.bgGoodsInfo;
					console.dir(editGoodsPanel.down("#selectGoodsSn"));
					editGoodsPanel.down("#selectGoodsSn").setValue(bgGoodsInfo.goodsSn);
					editGoodsPanel.down("#customCode").setValue(bgGoodsInfo.customCode);
					editGoodsPanel.down("#marketPrice").setValue(bgGoodsInfo.goodsPrice);
					editGoodsPanel.down("#goodsPrice").setValue(bgGoodsInfo.goodsPrice);
					editGoodsPanel.down("#goodsName").setValue(bgGoodsInfo.goodsName);
					editGoodsPanel.down("#goodsThumb").setValue(bgGoodsInfo.goodsThumb);
					editGoodsPanel.down("#colorName").setValue(bgGoodsInfo.colorName);
					editGoodsPanel.down("#sizeName").setValue(bgGoodsInfo.sizeName);
					editGoodsPanel.down("#salesMode").setValue(bgGoodsInfo.salesMode);
					editGoodsPanel.down("#supplierCode").setValue(bgGoodsInfo.supplierCode);
					editGoodsPanel.down("#bvValue").setValue(bgGoodsInfo.bvValue);
					editGoodsPanel.down("#baseBvValue").setValue(bgGoodsInfo.baseBvValue);
					editGoodsPanel.down("#intlCode").setValue(bgGoodsInfo.intlCode);
					editGoodsPanel.down("#depotCode").setValue(bgGoodsInfo.depotCode);
					
					var goodsExtensionCombo = editGoodsPanel.down("#goodsExtension");
					goodsExtensionCombo.setValue("common");
					Ext.msgBox.msg('查询商品', results.msg, Ext.MessageBox.INFO);
				} else {
					console.dir(editGoodsPanel.down("#selectGoodsSn"));
					editGoodsPanel.down("#selectGoodsSn").setValue(null);
					editGoodsPanel.down("#customCode").setValue(null);
					editGoodsPanel.down("#marketPrice").setValue(null);
					editGoodsPanel.down("#goodsPrice").setValue(null);
					editGoodsPanel.down("#goodsName").setValue(null);
					editGoodsPanel.down("#goodsThumb").setValue(null);
					editGoodsPanel.down("#colorName").setValue(null);
					editGoodsPanel.down("#sizeName").setValue(null);
					editGoodsPanel.down("#salesMode").setValue(null);
					editGoodsPanel.down("#supplierCode").setValue(null);
					editGoodsPanel.down("#bvValue").setValue(null);
					editGoodsPanel.down("#baseBvValue").setValue(null);
					editGoodsPanel.down("#intlCode").setValue(null);
					editGoodsPanel.down("#depotCode").setValue(null);
					Ext.msgBox.remainMsg('查询商品', results.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var text = response.responseText;
				Ext.msgBox.remainMsg('查询商品', "查询商品失败！", Ext.MessageBox.ERROR);
			}
		});
	},
	changeColorCodeOrSizeCode: function (combo, newValue ,oldValue) { // 选择查询商品的颜色码尺码时,定位到相应的11位码和6位码
		// 商品编码有变化时,加载尺码和颜色码列表
		var goodsPanel = this.up("panel");
		var colorCode;
		var sizeCode;
		if (combo.xtype == 'goodsColorComb') {
			var goodsSizeComb = goodsPanel.down("goodsSizeComb")
			sizeCode = goodsSizeComb.getValue();
			colorCode = newValue;
			/*//联动商品尺码选项
			if(newValue!=''&&newValue!=null&&newValue!=undefined){
				var goodsSn = goodsPanel.down("#selectGoodsSn").getValue();
				params = {"colorCode": newValue, "goodSn" : goodsSn};
				Ext.Ajax.request({
					url: basePath + '/custom/orderGoodsEdit/getSizeListByColorCode',
					params: params,
					success: function(response){
						var respText = Ext.JSON.decode(response.responseText);
						if(respText.code=='1'){
							goodsSizeComb.store.removeAll();
							goodsSizeComb.store.loadData(respText.list);
						}else{
							goodsSizeComb.store.loadData([]);
							goodsSizeComb.clearValue();
						}
					},
					failure: function(response){
						
					}
				});
			}*/
			
		} else if (combo.xtype == 'goodsSizeComb') {
			var goodsColorComb = goodsPanel.down("goodsColorComb");
			colorCode = goodsColorComb.getValue();
			sizeCode = newValue;
			/*//联动商品颜色选项
			if(newValue!=''&&newValue!=null&&newValue!=undefined){
				var goodsSn = goodsPanel.down("#selectGoodsSn").getValue();
				params = {"sizeCode": newValue, "goodSn" : goodsSn};
				Ext.Ajax.request({
					url: basePath + '/custom/orderGoodsEdit/getColorListBySizeCode',
					params: params,
					success: function(response){
						var respText = Ext.JSON.decode(response.responseText);
						if(respText.code=='1'){
							goodsColorComb.store.removeAll();
							goodsColorComb.store.loadData(respText.list);
						}else{
							goodsColorComb.store.loadData([]);
							goodsColorComb.clearValue();
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
			params = {"colorCode": colorCode,"sizeCode": sizeCode, shopCode: channelCode, "goodsSn" : goodsSn,"type": 1};
			Ext.Ajax.request({
				url: basePath + '/custom/orderGoodsEdit/getCustomStock',
				params: params,
				success: function(response){
					var text = response.responseText;
					var results = Ext.JSON.decode(text);
					if (results.code=='1') {
						goodsPanel.down("#customCode").setValue(results.list[0].sku);
						goodsPanel.down("#skuStock").setValue(results.list[0].allStock);
					} else {
						Ext.msgBox.remainMsg('验证商品SKU', results.msg, Ext.MessageBox.ERROR);
					}
					// 添加商品按钮
					addGoodsButton.setDisabled( false );
				},
				failure: function(response){
					var text = response.responseText;
					// 添加商品按钮
					addGoodsButton.setDisabled( false );
				}
			});
		}
	},
	changePrice: function (radio, newValue, oldValue) { // 查询商品列表选择
		// 商品编码有变化时,加载尺码和颜色码列表
		if (newValue != null && newValue != '' && newValue != oldValue) {
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
		/*var goodsColorCode = goodsPanel.down("goodsColorComb").getValue();
		var goodsColorName = goodsPanel.down("goodsColorComb").getRawValue();
		var goodsSizeCode = goodsPanel.down("goodsSizeComb").getValue();
		var goodsSizeName = goodsPanel.down("goodsSizeComb").getRawValue();
		var addGoodsNumber = goodsPanel.down("#addGoodsNumber").getValue();
		var customPrice = goodsPanel.down("#customPrice").getValue();
		var extensionCode = goodsPanel.down("#goodsExtension").getValue();
		var addGoodsNumber = goodsPanel.down("#addGoodsNumber").getValue();
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
		if(addGoodsNumber<1){
			Ext.msgBox.remainMsg('验证', "至少添加一件商品", Ext.MessageBox.ERROR);
			return;
		}
		if(parent.isVerifyStock=='1'){
			var skuStock = parseInt(goodsPanel.down("#skuStock").getValue());
			if(!skuStock||!(skuStock>0)){
				Ext.msgBox.remainMsg('验证', "该商品没库存，不允许添加！", Ext.MessageBox.ERROR);
				return;
			}
		}*/
		var addGoodsNumber = goodsPanel.down("#addGoodsNumber").getValue();
		if(addGoodsNumber<1){
			Ext.msgBox.remainMsg('验证', "至少添加一件商品", Ext.MessageBox.ERROR);
			return;
		}
		btn.disable();
		var goodsColorName = goodsPanel.down("#colorName").getValue();
		var goodsSizeName = goodsPanel.down("#sizeName").getRawValue();
//		var customPrice = goodsPanel.down("#customPrice").getValue();
		var extensionCode = goodsPanel.down("#goodsExtension").getValue();
		var baseBvValue = goodsPanel.down("#baseBvValue").getValue();
		var bvValue = goodsPanel.down("#bvValue").getValue();
		var goodsSn = goodsPanel.down("#selectGoodsSn").getValue();
		var goodsThumb = goodsPanel.down("#goodsThumb").getValue();
		var customCode = goodsPanel.down("#customCode").getValue();
		var goodsName = goodsPanel.down("#goodsName").getValue();
		var ordereditlist = goodsPanel.up('window').down("#districtGoods");
		var goodsPrice = goodsPanel.down("#goodsPrice").getValue();
		var marketPrice = goodsPanel.down("#marketPrice").getValue();
		var intlCode = goodsPanel.down("#intlCode").getValue();
		var salesMode = goodsPanel.down("#salesMode").getValue();
		var depotCode = goodsPanel.down("#depotCode").getValue();
		//新增商品的extensionId取商品列表中最大的extensionId+1
		var maxFlag = 0;
		ordereditlist.getStore().each(function(record, i) {
			var gridExtensionId = parseInt(record.get("extensionId")==''||record.get("extensionId")==null?'0':record.get("extensionId"));
			if(gridExtensionId>maxFlag){
				maxFlag = gridExtensionId;
			}
		});
		maxFlag = maxFlag+1;
		//这里逻辑跟之前有变  之前把商品列表的所有商品一并提交  这里只提交新增的商品信息  把新增的商品按标准格式返回并追加到商品列表的store中
		var params = {};
		params['goodsSn'] = goodsSn;
		params['goodsName'] = goodsName;
		params['customCode'] = customCode;
		params['currColorName'] = goodsColorName;
		params['currSizeName'] = goodsSizeName;
		params['goodsNumber'] = addGoodsNumber;
		params['initGoodsNumber'] = addGoodsNumber;
		params['goodsPrice'] = marketPrice;
		params['shareBonus'] = 0;
		params['transactionPrice'] = goodsPrice;
		params['settlementPrice'] = goodsPrice;
		params['extensionCode'] = extensionCode;
		params['extensionId'] = maxFlag;
		if (depotCode == null || depotCode == '') {
			params['depotCode'] = "DEFAULT";
		} else {
			params['depotCode'] = depotCode;
		}
		params['channelCode'] = channelCode;
		params['masterOrderSn'] = masterOrderSn;
		params['baseBvValue'] = baseBvValue;
		params['bvValue'] = bvValue;
		params['sap'] = intlCode;
		params['goodsThumb'] = goodsThumb;
		params['marketPrice'] = marketPrice;
		params['salesMode'] = salesMode;
		Ext.Ajax.request({
			url:  basePath + '/custom/orderGoodsEdit/addOrderGoods',
			params: params,
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				if (results.code=='1') {
					var store = ordereditlist.getStore();
					var list = results.list;
					Ext.each(list,function(record,i){
						store.insert(0,record);
					});
					goodsPanel.up('window').recalculateTotal(ordereditlist);
					Ext.msgBox.msg('添加商品', "添加成功！", Ext.MessageBox.INFO);
					btn.enable();
				} else {
					Ext.msgBox.remainMsg('添加商品', results.msg, Ext.MessageBox.ERROR);
					btn.enable();
				}
			},
			failure: function(response){
				var text = response.responseText;
				btn.enable();
			}
		});
	}
});