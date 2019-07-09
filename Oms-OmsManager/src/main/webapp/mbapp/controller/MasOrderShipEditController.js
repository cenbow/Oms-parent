Ext.define('MB.controller.MasOrderShipEditController', {
	extend : 'Ext.app.Controller',
	models : ['MB.model.RegionCombModel'],
	stores : ['MB.store.RegionCountryCombStore',
	          'MB.store.RegionProvinceCombStore',
	          'MB.store.RegionCityCombStore',
	          'MB.store.RegionDistrictCombStore',
	          'MB.store.RegionStreetCombStore'
	          ],
	views : ['MB.view.orderDetail.ShipEditWin',
	         'MB.view.commonComb.RegionCountryComb',
	         'MB.view.commonComb.RegionProvinceComb',
	         'MB.view.commonComb.RegionCityComb',
	         'MB.view.commonComb.RegionDistrictComb',
	         'MB.view.commonComb.RegionStreetComb'
	         ],
	refs : [{
		ref : 'shipEditWin',
		selector : 'shipEditWin'
	}],
	init : function() {
		var me = this;
		me.control({
			'shipEditWin button[action=doSaveAddrEdit]' : { // 保存收货人信息编辑
				click : this.doSaveAddrEdit
			}
		});
	},
	onLaunch : function() {
		var win = Ext.widget("shipEditWin");
		var form = win.down('form');
		Ext.Ajax.request({
			url:  basePath + 'custom/orderInfo/getMasterOrderAddressInfo',
			timeout:90000,
			params : {
				"masterOrderSn" : masterOrderSn
			},
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				if (respText.code=='1') {
					var addrInfo = respText.addrInfo;
					var countryComb = form.down("#countryComb");
					var provinceComb = form.down("#provinceComb");
					var cityComb = form.down('#cityComb');
					var districtComb = form.down('#districtComb');
//					var streetComb = form.down('#streetComb');

					countryComb.store.load();
					countryComb.setValue(addrInfo.country);
					provinceComb.setValue(addrInfo.province)
					cityComb.setValue(addrInfo.city);
					districtComb.setValue(addrInfo.district);
//					streetComb.setValue(addrInfo.street);
					
					form.getForm().findField("masterOrderSn").setValue(masterOrderSn);
					form.getForm().findField("consignee").setValue(addrInfo.consignee);
					form.getForm().findField("address").setValue(addrInfo.address);
					form.getForm().findField("zipcode").setValue(addrInfo.zipcode);
					form.getForm().findField("email").setValue(addrInfo.email);
					form.getForm().findField("mobile").setValue(addrInfo.mobile);
					form.getForm().findField("tel").setValue(addrInfo.tel);
					form.getForm().findField("signBuilding").setValue(addrInfo.signBuilding);
					form.getForm().findField("bestTime").setValue(addrInfo.bestTime);
				} else {
					Ext.msgBox.remainMsg('获取收货人信息', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('获取收货人信息', respText.msg, Ext.MessageBox.ERROR);
			}
		});
		win.show();
	},
	doSaveAddrEdit : function(btn){
		var win = btn.up("window");
		form = win.down("form");
		var formValues=form.getForm().getValues();
		//验证必填项
		if (!form.isValid()) {
			Ext.msgBox.remainMsg('编辑收货信息', "检查数据必填项是否填写", Ext.MessageBox.ERROR);
			return ;
		}
		//手机、电话二选一
		var tel = form.getForm().findField("tel").getValue();
		var mobile = form.getForm().findField("mobile").getValue();
		if ((tel == null || tel== '' || tel== '--') && (mobile == null || mobile== '')) {
			Ext.msgBox.remainMsg('编辑收货信息', "电话、手机至少选一", Ext.MessageBox.ERROR);
			return ;
		}
		//提交修改
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/orderInfo/doSaveAddrEdit',
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				if(respText.code=='1'){
					Ext.getCmp('orderDetailCenter').initData();
					win.close();
				}else{
					Ext.Msg.alert('保存收货信息', respText.msg);
				}
			},
			failure : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('保存收货信息', respText.msg);
			}
		});
	}
});