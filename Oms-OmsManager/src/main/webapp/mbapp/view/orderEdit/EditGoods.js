Ext.define("MB.view.orderEdit.EditGoods", {
	extend: "Ext.window.Window",
	alias: "widget.orderediteditgoods",
	requires : [ 'MB.view.common.AddGoods','MB.view.common.GoodsBonus',
				'MB.view.orderEdit.List','MB.view.orderDetail.PaymentEditForm'],
	title: "编辑订单商品",
	width: '100%',
	height: '100%',
	layout: "fit",
	maskDisabled : false ,
	modal : true ,
	initComponent: function () {
		var clientHeight = document.body.clientHeight;
		var goodsListHeight = clientHeight * 0.6;
		var addGoodsHeight = clientHeight - goodsListHeight;
		var clientWidth = document.body.clientWidth;
		var me = this;
		var editType = parent.editGoodsType;
		console.dir(editType);
		var addGoods;
		// 订单修改 换单添加商品
		if (editType == 1 || editType == 2 || editType == 3) {
			addGoods = {
				xtype : 'tabpanel',
				width: '100%',
				height: addGoodsHeight,
				items: [{
					xtype : 'commonaddgoods',
					tooltip: '添加商品'
				} , {
					xtype : 'commongoodsbonus',
					tooltip: '红包信息'
				} ]
			};
		} else { // 新订单生成
			addGoods = {
				xtype : 'tabpanel',
				width: '100%',
				height: addGoodsHeight,
				tabIndex: 0,
				items: [{
					xtype : 'commonaddgoods',
					tooltip: '添加商品',
					value : 0
				} , {
					xtype : 'commongoodsbonus',
					tooltip: '红包信息',
					disabled: true,
					value : 1
				} /*, {
					xtype : 'shipEditForm',
					title : '收货人信息',
					tooltip: '收货人信息',
					scrollable : true,
					disabled: true,
					width: '80%',
					value : 2
				} , {
					xtype : 'deliveryEditForm',
					title : '选择配送方式',
					tooltip: '选择配送方式',
//					scrollable : true,
					disabled: true,
					value : 3
				} , {
					xtype : 'paymentEditForm',
					title : '选择支付方式',
					tooltip: '选择支付方式',
					scrollable : true,
					disabled: true,
					value : 4
				} , {
					xtype : 'otherEditForm',
					title : '编辑其他信息',
					tooltip: '编辑其他信息',
					scrollable : true,
					disabled: true,
					value : 5
				}*/]
			};
		}
		me.items = {
			xtype: "form",
//			margin: 5,
			border: false,
			frame: true,
			url : basePath + '/custom/editOrder/editOrderGoods',
			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 70
			},
			reader: new Ext.data.reader.Json({
				rootProperty : "info",
				model : 'MB.model.OrderInfoEdit'
			}),
			items: [
				/*{
					xtype : 'ordereditlist',
//					title : '订单商品列表',
					height: goodsListHeight,
					collapsed: false
				} , {
					xtype : 'hidden',
					lableField: '请求类型',
					name : 'method',
					value : 'start'
				} , {
					xtype : 'hidden',
					lableField: '使用红包列表',
					itemId : 'bonusIds',
					name : 'bonusIds'
				} , */addGoods
			]
		};
		me.buttons = [
			{ text: "上一步", itemId:'previous', handler : me.previous, disabled: true},
			{ text: "下一步", itemId:'next', handler : me.next , disabled: true},
			{ text: "重置", handler : function () {
				var win = this.up("window");
				win.down("form").load({
					url : basePath + '/custom/editOrder/editOrderGoods',
					params : {
						"orderSn" : parent.orderSn,
						"method" : 'init'
					},
					success : function(form, action) {
						// 数据加载成功后操作
						win.initPage(win, form);
						Ext.msgBox.msg('修改商品', '加载商品数据成功', Ext.MessageBox.INFO);
					},
					failure : function(form, action) {
						// 数据加载失败后操作
					}
				});
			} },
			{ text: "保存", action: "saveOrderGoods", align : 'center'/*, handler : me.saveOrderGoods*/},
			{ text: "关闭", handler : function () { this.up("window").close(); } }

		];
		me.callParent(arguments);
	},
	initPage: function (win, form) {
		// 数据加载成功后操作
		// 商品列表数据展示
		var doERPA = form.reader.rawData.info.doERP;
		var orderEditList = win.down("form").down("ordereditlist");
		var goodsList = form.reader.rawData.info.orderGoodsUpdateInfos;
		console.dir(goodsList);
		if(goodsList){
			orderEditList.getStore().loadData(goodsList, false);
		}
		orderEditList.recalculateTotal(orderEditList);
		// 商品红包数据
		var packageVOs = form.reader.rawData.info.cardPackageUpdateInfos;
		var editGoodsForm = win.down("form");
		var tabpanel = editGoodsForm.down("tabpanel");
		if (doERPA == 1) {
			parent.editGoodsType = 2;
			editGoodsForm.down("commonaddgoods").setDisabled( true );
		} else {
			parent.editGoodsType = 1;
		}
		var goodsbonusGrid = editGoodsForm.down("commongoodsbonus");
		var orderGoodsGrid = editGoodsForm.down("ordereditlist");
		if (packageVOs != null && packageVOs.length > 0) {
			goodsbonusGrid.store.loadData(packageVOs, false);
//			goodsbonusGrid.getSelectionModel().setLocked( true );
			console.dir(goodsbonusGrid.getSelectionModel());
			var bonusIds = new Array();
			var bonusTotal = 0.0000;
			packageVOs.forEach(function (obj, index) {
				bonusIds.push(obj.cardNo);
				bonusTotal += obj.cardMoney;
			})
			var editabled = false;
			goodsList.forEach(function (obj, index) {
				if (obj.shippingStatus > 0) {
					editabled = true;
				}
			})
			editGoodsForm.down("ordereditlist").down("#recalculateButton").setDisabled( editabled );
			orderGoodsGrid.down('#bonusTotal').update('红包面值合计: ' + bonusTotal + " 元");
			editGoodsForm.down("#bonusIds").setValue(bonusIds);
			tabpanel.setActiveTab(1);
		} else {
			orderGoodsGrid.down('#bonusTotal').update('红包面值合计: 0  元');
			goodsbonusGrid.setDisabled( true );
			editGoodsForm.down("ordereditlist").down("#recalculateButton").setDisabled( true );
			tabpanel.setActiveTab(0);
		}
	},
	previous : function (btn) {
		var win = btn.up('window');
		var tabpanel = win.down('tabpanel');
		var oldTab = tabpanel.getActiveTab();
		var tabIndex = oldTab.value;
		if (tabIndex > 0) {
			tabpanel.setActiveTab(tabIndex - 1);
			console.dir(tabpanel.getActiveTab());
			if (tabIndex - 1 == 0) {
				btn.setDisabled( true );
			} else {
				win.down('#next').setDisabled( false );
			}
		}
	},
	next : function (btn) {
		var win = btn.up('window');
		var tabpanel = win.down('tabpanel');
		var oldTab = tabpanel.getActiveTab();
		var tabIndex = oldTab.value;
		if (tabIndex == 0) { // 校验是否添加商品
			var goodsGrid = win.down('ordereditlist');
			console.dir(goodsGrid);
			var num = goodsGrid.getStore().getData().length;
			if (num == 0) {
				Ext.msgBox.remainMsg('添加商品', "请先添加商品再做下一步操作", Ext.MessageBox.ERROR);
				return ;
			}
		} /*else if (tabIndex == 2) { // 收货人信息校验同事加载配送信息
			var shipEditForm = tabpanel.down('shipEditForm');
			if (!shipEditForm.isValid()) {
				Ext.msgBox.remainMsg('编辑收货信息', "检查数据必填项是否填写", Ext.MessageBox.ERROR);
				return ;
			}
			var tel = shipEditForm.getForm().findField("tel").getValue();
			var mobile = shipEditForm.getForm().findField("mobile").getValue();
			if ((tel == null || tel== '' || tel== '--') && (mobile == null || mobile== '')) {
				Ext.msgBox.remainMsg('编辑收货信息', "电话、手机至少选一", Ext.MessageBox.ERROR);
				return ;
			}
			var radiogroup = win.down('deliveryEditForm').down('radiogroup');
			if (radiogroup.items.length == 0) {
				var params = shipEditForm.getValues();
				params.method = 'init';
				params.depotCode = null;
				params.transType = parent.transType;
				console.dir(params);
				Ext.Ajax.request({
					url:  basePath + '/custom/orderStatus/editDeliveryInfo',
					params: params,
					success: function(response){
						var text = response.responseText;
						var results = Ext.JSON.decode(text);
						tabpanel.getActiveTab().setDisabled(false);
						if (results != null && results.length > 0) {
							results.forEach(function (obj, index) {
								if (index == 0) {
									win.down('deliveryEditForm').getForm().findField("depotCode").setValue(obj.depotCode);
								}
								var boxLabel = '名称：'+ obj.shippingCode +' 描述:' + obj.shippingDesc 
									+ ' 配送费：' + obj.shippingFee + ' 免费额度:' + obj.freeMoney + ' 保价费:' + obj.insure;
								var radio = {boxLabel: boxLabel, name: 'shippingIdAndFee', inputValue: obj.shippingId + "," +obj.shippingFee};
								radiogroup.add(radio);
							});
						}
					},
					failure: function(response){
						var text = response.responseText;
						console.dir('failure:' + text);
					}
				});
			}
		} else if (tabIndex == 3) {
			var deliveryEditForm = win.down("deliveryEditForm");
			if (!deliveryEditForm.isValid()) {
				Ext.msgBox.remainMsg('编辑配送方式', "检查数据必填项是否填写", Ext.MessageBox.ERROR);
				return ;
			}
		}*/
		
		tabpanel.setActiveTab(tabIndex + 1);
		tabpanel.getActiveTab().setDisabled(false);
		if (tabIndex + 1 > 0) {
			win.down('#previous').setDisabled( false );
		}
		if (tabIndex + 1 == 5) {
			btn.setDisabled( true );
		}
	},
	editOrderGoodsParams : function (form) {
		var params = form.getValues();
		var orderGoodsGrid = form.down("ordereditlist");
		var orderBonusGrid = form.down("commongoodsbonus");
		params['orderSn'] = parent.orderSn;
		orderGoodsGrid.store.each(function(record, i) {
			var currColorName = record.get("currColorName");
			var currSizeName = record.get("currSizeName");
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
					params['orderGoodsUpdateInfos['+ i +'].sizeChild['+ index +'].sizeCode'] = obj.sizeCode;
					params['orderGoodsUpdateInfos['+ i +'].sizeChild['+ index +'].skuSn'] = obj.skuSn;
				});
			}
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
					params['orderGoodsUpdateInfos['+ i +'].colorChild['+ index +'].sizeCode'] = obj.sizeCode;
					params['orderGoodsUpdateInfos['+ i +'].colorChild['+ index +'].skuSn'] = obj.skuSn;
				});
			}
			params['orderGoodsUpdateInfos['+ i +'].orderSn'] = record.get("orderSn");
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
//			params['orderGoodsUpdateInfos['+ i +'].sizeChild'] = record.get("sizeChild");
//			params['orderGoodsUpdateInfos['+ i +'].colorChild'] = record.get("colorChild");
			params['orderGoodsUpdateInfos['+ i +'].depotCode'] = record.get("depotCode");
			params['orderGoodsUpdateInfos['+ i +'].extensionCode'] = record.get("extensionCode");
			params['orderGoodsUpdateInfos['+ i +'].extensionId'] = record.get("extensionId");
			params['orderGoodsUpdateInfos['+ i +'].useCard'] = record.get("useCard");
			params['orderGoodsUpdateInfos['+ i +'].promotionDesc'] = record.get("promotionDesc");
			params['orderGoodsUpdateInfos['+ i +'].subTotal'] = record.get("subTotal");
			params['orderGoodsUpdateInfos['+ i +'].id'] = record.get("indexId");
			params['orderGoodsUpdateInfos['+ i +'].madeUrl'] = record.get("madeUrl");
			params['orderGoodsUpdateInfos['+ i +'].lackNum'] = record.get("lackNum");
			params['orderGoodsUpdateInfos['+ i +'].initLackNum'] = record.get("initLackNum");
			params['orderGoodsUpdateInfos['+ i +'].integralMoney'] = record.get("integralMoney");
			params['orderGoodsUpdateInfos['+ i +'].supplierCode'] = record.get("supplierCode");
			params['orderGoodsUpdateInfos['+ i +'].c2mItem'] = record.get("c2mItem");
		});
		
		console.dir(params);
//		var selModel = orderBonusGrid.getSelectionModel();
//		if (selModel.hasSelection()) {
//			var records = selModel.getSelection();
//			var orderSns = new Array();
//			records.forEach(function (record, i) {
//				params['cardPackageUpdateInfos['+i+'].cardNo'] = record.get("cardNo");
//				params['cardPackageUpdateInfos['+i+'].cardMoney'] = record.get("cardMoney");
//				params['cardPackageUpdateInfos['+i+'].cardLimitMoney'] = record.get("cardLimitMoney");
//			});
//		}
		orderBonusGrid.store.each(function(record, i) {
			var selected = record.get("selected");
			if (selected) {
				params['cardPackageUpdateInfos['+i+'].cardNo'] = record.get("cardNo");
				params['cardPackageUpdateInfos['+i+'].cardMoney'] = record.get("cardMoney");
				params['cardPackageUpdateInfos['+i+'].cardLimitMoney'] = record.get("cardLimitMoney");
			}
		});
		return params;
	},
	saveOrderGoods : function(btn) {
		if (resource == 0) {
			var me = this;
			var win = btn.up("window");
			form = win.down("form");
			var params = win.editOrderGoodsParams(form);
			var json = {
				params : params,
				timeout : 40000,
				success : function(formPanel, action) {
					if (action.result.success == "false") {
						Ext.msgBox.remainMsg('修改商品', action.result.msg, Ext.MessageBox.ERROR);
					} else {
						Ext.msgBox.msg('修改商品', action.result.msg, Ext.MessageBox.INFO);
						var orderForm = Ext.getCmp('orderShow');
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
		} else if (resource == 1) {
			if(exchangeOrderSn){
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
			}else{
				var win = btn.up("window");
				var grid=win.down('ordereditlist');
				var newGoodStore=[];
				var total=0;
				var bonus=0;//红包
				grid.store.each(function(record,i){
					if(record.get('shareBonus')){
						record.data.shareBonus=record.get('shareBonus');
					}
					if(record.get('goodsThumb')){
						record.data.goodsThumb=record.get('goodsThumb');
					}
					newGoodStore.push(record.data);
					total+=record.get('transactionPrice')*record.get('goodsNumber');
					bonus+=record.get('shareBonus');
				});
				
				Ext.getCmp('exchangeGoodsSetModule').down('goodDetail').store.loadData(newGoodStore);
				//总计
				Ext.getCmp('exchangeGoodsSetModule').getForm().findField('total').setValue(total);
				Ext.getCmp('exchangeCenter').down('exchangePaySetModule').getForm().findField('goodsAmount').setValue(total);
				//红包
				Ext.getCmp('exchangeCenter').down('exchangePaySetModule').getForm().findField('bonus').setValue(bonus);
				win.close();
				Ext.msgBox.msg('结果', '添加商品成功！', Ext.MessageBox.INFO);
			}
		}
	}
});