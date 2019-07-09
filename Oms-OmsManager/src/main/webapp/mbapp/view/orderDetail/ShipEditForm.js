Ext.define("MB.view.orderDetail.ShipEditForm", {
	extend : "Ext.form.Panel",
	alias : 'widget.shipEditForm',
	requires: ['MB.view.common.UserAddressCombo','MB.view.common.SystemRegionAreaCombo'],
	width : '100%',
	frame : true,
	margin : 1.5,
	bodyPadding : 6,
	layout : 'column',
	buttonAlign : 'center',// 按钮居中
	fieldDefaults : {
		labelAlign : 'right',
		labelWidth : 90,
		margin : 1.5
	},
	url : basePath + '/custom/orderStatus/editOrderConsig',
	initComponent : function() {
		var me = this;
		this.items = [ {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			defaultType : 'displayfield',
			items : [ {
				xtype : "textfield",
				name : "orderSn",
				hidden : true,
				fieldLabel : "订单编号",
				value : parent.orderSn
			}, {
				xtype : "userAddressCombo",
				columnWidth : .95,
				listeners:{
					'change': me.changeUserAddress
				}
			} ]
		}, {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			defaultType : 'displayfield',
			items : [ {
				xtype : "textfield",
				name : "consignee",
				fieldLabel : "收货人",
				columnWidth : .6,
				allowBlank : false
			} ]
		}, {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			defaultType : 'displayfield',
			items : [ {
				xtype : 'combo',
				itemId : 'selectCountry',
//				name : 'country',
				columnWidth : .3,
				fieldLabel: '所在地区',
//				hiddenName: 'selectCountry',
				emptyText: '请选择国家',
				store: Ext.create('Ext.data.Store', {
					model: "MB.model.SystemRegionArea",
					proxy : {
						type : 'ajax',
						actionMethods : {
							read : 'POST'
						},
						url : basePath + 'custom/common/getSystemRegionArea',
						reader : {
							type : 'json'
						}
					},
					autoLoad : false
				}),
				displayField: 'regionName',
				valueField: 'regionId',
				queryMode: 'remote',
				editable: false,
				allowBlank : false,
				listeners:{
					'change': me.changeCountry
				}
			}, {
				xtype : 'combo',
				store: Ext.create('Ext.data.Store', {
					model: "MB.model.SystemRegionArea",
					proxy : {
						type : 'ajax',
						actionMethods : {
							read : 'POST'
						},
						url : basePath + 'custom/common/getSystemRegionArea',
						reader : {
							type : 'json'
						}
					},
					autoLoad : false
				}),
				labelWidth : 40,
				columnWidth : .16,
//				hiddenName: 'selectProvince',
				emptyText: '请选择省',
				xtype : 'combo',
				itemId : 'selectProvince',
				displayField: 'regionName',
				valueField: 'regionId',
				queryMode: 'remote',
				editable: false,
				allowBlank : false,
				listeners:{
					'change': me.changeProvince
				}
			}, {
				xtype : 'combo',
				store: Ext.create('Ext.data.Store', {
					model: "MB.model.SystemRegionArea",
					proxy : {
						type : 'ajax',
						actionMethods : {
							read : 'POST'
						},
						url : basePath + 'custom/common/getSystemRegionArea',
						reader : {
							type : 'json'
						}
					},
					autoLoad : false
				}),
				itemId : 'selectCity',
				labelWidth : 40,
				columnWidth : .16,
//				hiddenName: 'selectCity',
				emptyText: '请选择市',
				displayField: 'regionName',
				valueField: 'regionId',
				queryMode: 'remote',
				editable: false,
				allowBlank : false,
				listeners:{
					'change': me.changeCity
				}
			}, {
				xtype : 'combo',
				store: Ext.create('Ext.data.Store', {
					model: "MB.model.SystemRegionArea",
					proxy : {
						type : 'ajax',
						actionMethods : {
							read : 'POST'
						},
						url : basePath + 'custom/common/getSystemRegionArea',
						reader : {
							type : 'json'
						}
					},
					autoLoad : false
				}),
				itemId : 'selectDistrict',
				labelWidth : 40,
				columnWidth : .16,
//				hiddenName: 'selectDistrict',
				emptyText: '请选择区',
				displayField: 'regionName',
				valueField: 'regionId',
				queryMode: 'remote',
				editable: false,
				allowBlank : false,
				listeners:{
					'change': me.changeDistrict
				}
			}, {
				xtype : 'combo',
				store: Ext.create('Ext.data.Store', {
					model: "MB.model.SystemRegionArea",
					proxy : {
						type : 'ajax',
						actionMethods : {
							read : 'POST'
						},
						url : basePath + 'custom/common/getSystemRegionArea',
						reader : {
							type : 'json'
						}
					},
					autoLoad : false
				}),
				itemId : 'selectStreet',
				labelWidth : 40,
				columnWidth : .16,
//				hiddenName: 'selectStreet',
				emptyText: '请选择街道',
				displayField: 'regionName',
				valueField: 'regionId',
				queryMode: 'remote',
				editable: false
			}, {
				xtype : 'hidden',
				name : 'country'
			}, {
				xtype : 'hidden',
				name : 'province'
			}, {
				xtype : 'hidden',
				name : 'city'
			}, {
				xtype : 'hidden',
				name : 'district'
			}, {
				xtype : 'hidden',
				name : 'street'
			}]
		}, {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			defaultType : 'displayfield',
			items : [ {
				xtype : "textfield",
				name : "address",
				fieldLabel : "地址",
				columnWidth : .75,
				allowBlank : false
			}, {
				xtype : "textfield",
				name : "zipcode",
				fieldLabel : "邮编",
				labelWidth : 40,
				columnWidth : .2
			} ]
		}, {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			defaultType : 'displayfield',
			items : [{
				xtype : "textfield",
				name : "mobile",
				fieldLabel : "手机",
				columnWidth : .4,
				value : '******'
			}, {
				xtype : "displayfield",
				value : '（电话、手机至少选一）',
				columnWidth: .25
			} , {
				xtype : "displayfield",
				name : 'encodeMobile',
				columnWidth: .2
			} , {
				xtype : "hidden",
				name : 'oldMobile'
			} ]
		} , {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			defaultType : 'displayfield',
			items : [{
				xtype : "textfield",
				name : "tel",
				fieldLabel : "电话",
				columnWidth : .4,
				value : '******'
			} , {
				xtype : "displayfield",
				value : '（电话、手机至少选一）',
				columnWidth: .25
			} , {
				xtype : "displayfield",
				name : 'encodeTel',
				columnWidth: .2
			} , {
				xtype : "hidden",
				name : 'oldTel'
			} , {
				xtype : 'button',
				text : '解密',
				columnWidth: .1,
				handler : me.decodeLinkMobile
			}]
		} , {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			defaultType : 'displayfield',
			items : [ {
				xtype : "textfield",
				name : "email",
				fieldLabel : "邮箱",
				columnWidth : .5
			}]
		}, {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			defaultType : 'displayfield',
			items : [ {
				xtype : "textfield",
				name : "signBuilding",
				fieldLabel : "标志性建筑",
				columnWidth : .95
			} ]
		}, {
			xtype : 'fieldcontainer',
			labelStyle : 'font-weight:bold;padding:0;',
			layout : 'column',
			columnWidth : 1,
			defaultType : 'displayfield',
			items : [ {
				xtype : "textfield",
				fieldLabel : "最佳送货时间",
				columnWidth : .5,
				name: 'bestTime'
//				listeners:{
//					render:function(p){
//						p.getEl().on('click',function(){
//							WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
//						});
//					}
//				}
			} ]
		}]
		//以json形式读取数据
		this.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "orderShip",
			model : 'MB.model.OrderShipEdit'
		});
		me.callParent(arguments);
		me.down("userAddressCombo").store.on('beforeload', function (store){
			Ext.apply(store.proxy.extraParams, {userId : parent.userId});
		});
		me.down("userAddressCombo").getStore().load();
		// 循环
	},
	initPage: function (form, address, changeAddres) {
		// 联系方式保存
		if (changeAddres == undefined || changeAddres == null) {
			form.getForm().findField("oldMobile").setValue(address.mobile);
			form.getForm().findField("oldTel").setValue(address.tel);
			form.getForm().findField("mobile").setValue("******");
			form.getForm().findField("tel").setValue("******");
		}
		var countryCombo = form.down("#selectCountry");
		countryCombo.store.on('beforeload', function (store){
			params = {"regionType" : 0, "parentId" : 0};
			Ext.apply(store.proxy.extraParams, params);
		});
		countryCombo.store.load();
		countryCombo.setValue(address.country);
		
		var provinceCombo = form.down("#selectProvince");
		provinceCombo.store.on('beforeload', function (store){
			params = {"regionType" : 1, "parentId" : address.country};
			Ext.apply(store.proxy.extraParams, params);
		});
		provinceCombo.store.load();
		provinceCombo.setValue(address.province);
		var cityCombo = form.down("#selectCity");
		cityCombo.store.on('beforeload', function (store){
			params = {"regionType" : 2, "parentId" : address.province};
			Ext.apply(store.proxy.extraParams, params);
		});
		cityCombo.store.load();
		cityCombo.setValue(address.city);

		var districtCombo = form.down("#selectDistrict");
		districtCombo.store.on('beforeload', function (store){
			params = {"regionType" : 3, "parentId" : address.city};
			Ext.apply(store.proxy.extraParams, params);
		});
		districtCombo.store.load();
		districtCombo.setValue(address.district);
		
		var streetCombo = form.down("#selectStreet");
		streetCombo.store.on('beforeload', function (store){
			params = {"regionType" : 4, "parentId" : address.district};
			Ext.apply(store.proxy.extraParams, params);
		});
		streetCombo.store.load();
		console.dir(address.street);
		if (address.street != null && address.street != '' && address.street != "null") {
			streetCombo.setValue(address.street);
		}
	},
	changeUserAddress : function (combo, newValue ,oldValue) {
		if (newValue && newValue != oldValue) {
			var form = combo.up("form").getForm();
			// Ext Ajax Request
			Ext.Ajax.request({
				url:  basePath + 'custom/common/getUserAddress',
				params: {addressId : newValue},
				success: function(response){
					var text = response.responseText;
					var results = Ext.JSON.decode(text);
					if (results.length > 0) {
						console.dir( form );
						var userAddress = results[0];
						form.findField("consignee").setValue(userAddress.consignee);
						// 地区咱暂不处理
						form.findField("address").setValue(userAddress.address);
						form.findField("zipcode").setValue(userAddress.zipcode);
						form.findField("mobile").setValue(userAddress.mobile);
						form.findField("tel").setValue(userAddress.tel);
						form.findField("email").setValue(userAddress.email);
						form.findField("signBuilding").setValue(userAddress.signBuilding);
						form.findField("bestTime").setValue(userAddress.bestTime);
						combo.up("form").getForm().findField("country").setValue(userAddress.country);
						combo.up("form").getForm().findField("province").setValue(userAddress.province);
						combo.up("form").getForm().findField("city").setValue(userAddress.city);
						combo.up("form").getForm().findField("district").setValue(userAddress.district);
						var address =
							{
								country: userAddress.country,
								province: userAddress.province,
								city: userAddress.city,
								district: userAddress.district
//								street: userAddress.country,
							};
						// 地区信息填充
						combo.up("form").initPage(combo.up("form"), address, 'change');
//						combo.up("form").getForm().findField("country").setValue(null);
//						combo.up("form").getForm().findField("province").setValue(null);
//						combo.up("form").getForm().findField("city").setValue(null);
//						combo.up("form").getForm().findField("district").setValue(null);
						Ext.msgBox.msg('选择收货地址', "选择收货地址成功！", Ext.MessageBox.INFO);
					} else {
						Ext.msgBox.msg('选择收货地址', "选择收货地址成功！", Ext.MessageBox.INFO);
					}
				},
				failure: function(response){
					var text = response.responseText;
					console.dir('failure:' + text);
				}
			});
		}
	},
	changeCountry : function (combo, newValue ,oldValue) {
		if (newValue && newValue != oldValue) {
			console.dir(combo.up("form").getForm().findField("country").getValue());
			var country = combo.up("form").getForm().findField("country").getValue();
			if (newValue != country) {
				var provinceCombo = combo.up("form").down("#selectProvince");
				console.dir(provinceCombo);
				provinceCombo.store.on('beforeload', function (store){
					params = {"regionType" : 1, "parentId" : newValue};
					Ext.apply(store.proxy.extraParams, params);
				});
				provinceCombo.clearValue();
				provinceCombo.store.load();
				
				var cityCombo = combo.up("form").down("#selectCity");
				cityCombo.clearValue();
				cityCombo.store.loadData([], false);

				var districtCombo = combo.up("form").down("#selectDistrict");
				districtCombo.clearValue();
				districtCombo.store.loadData([], false);
				var streetCombo = combo.up("form").down("#selectStreet");
				streetCombo.clearValue();
				streetCombo.store.loadData([], false);
			} else {
				combo.up("form").getForm().findField("country").setValue(null);
//				combo.up("form").getForm().findField("province").setValue(null);
//				combo.up("form").getForm().findField("city").setValue(null);
//				combo.up("form").getForm().findField("district").setValue(null);
			}
		}
	},
	changeProvince : function (combo, newValue ,oldValue) {
		if (newValue && newValue != oldValue) {
			console.dir(combo.up("form").getForm().findField("province").getValue());
			var province = combo.up("form").getForm().findField("province").getValue();
			if (newValue != province) {
				var cityCombo = combo.up("form").down("#selectCity");
				console.dir(cityCombo);
				cityCombo.store.on('beforeload', function (store){
					params = {"regionType" : 2, "parentId" : newValue};
					Ext.apply(store.proxy.extraParams, params);
				});
				cityCombo.clearValue();
				cityCombo.store.load();
				var districtCombo = combo.up("form").down("#selectDistrict");
				districtCombo.clearValue();
				districtCombo.store.loadData([], false);
				var streetCombo = combo.up("form").down("#selectStreet");
				streetCombo.clearValue();
				streetCombo.store.loadData([], false);
			} else {
//				combo.up("form").getForm().findField("country").setValue(null);
				combo.up("form").getForm().findField("province").setValue(null);
//				combo.up("form").getForm().findField("city").setValue(null);
//				combo.up("form").getForm().findField("district").setValue(null);
			}
		}
	},
	changeCity : function (combo, newValue ,oldValue) {
		if (newValue && newValue != oldValue) {
			console.dir(combo.up("form").getForm().findField("city").getValue());
			var city = combo.up("form").getForm().findField("city").getValue();
			if (newValue != city) {
				var districtCombo = combo.up("form").down("#selectDistrict");
				console.dir(districtCombo);
				districtCombo.store.on('beforeload', function (store){
					params = {"regionType" : 3, "parentId" : newValue};
					Ext.apply(store.proxy.extraParams, params);
				});
				districtCombo.clearValue();
				districtCombo.store.load();
				var streetCombo = combo.up("form").down("#selectStreet");
				streetCombo.clearValue();
				streetCombo.store.loadData([], false);
			} else {
//				combo.up("form").getForm().findField("country").setValue(null);
//				combo.up("form").getForm().findField("province").setValue(null);
				combo.up("form").getForm().findField("city").setValue(null);
//				combo.up("form").getForm().findField("district").setValue(null);
			}
		}
	},
	changeDistrict  : function (combo, newValue ,oldValue) {
		if (newValue && newValue != oldValue) {
			console.dir(combo.up("form").getForm().findField("district").getValue());
			var district = combo.up("form").getForm().findField("district").getValue();
			if (newValue != district) {
				var streetCombo = combo.up("form").down("#selectStreet");
				streetCombo.store.on('beforeload', function (store){
					params = {"regionType" : 4, "parentId" : newValue};
					Ext.apply(store.proxy.extraParams, params);
				});
				streetCombo.clearValue();
				streetCombo.store.load();
			} else {
//				combo.up("form").getForm().findField("country").setValue(null);
//				combo.up("form").getForm().findField("province").setValue(null);
//				combo.up("form").getForm().findField("city").setValue(null);
				combo.up("form").getForm().findField("district").setValue(null);
			}
		}
	},
	decodeLinkMobile : function (btn) {
		var form = btn.up('form');
		var oldMobile = form.getForm().findField("oldMobile").getValue();
		var tel = form.getForm().findField("oldTel").getValue();
		Ext.Ajax.request({
			url:  basePath + 'custom/common/decodeLinkMobile',
			params: {tel : tel, mobile : oldMobile, orderSn: parent.orderSn},
			success: function(response){
				var text = response.responseText;
				var result = Ext.JSON.decode(text);
				if (result) {
					form.getForm().findField("encodeMobile").setValue(result.data.mobile);
					form.getForm().findField("encodeTel").setValue(result.data.tel);
				}
			},
			failure: function(response){
				var text = response.responseText;
				console.dir('failure:' + text);
			}
		});
	}
});