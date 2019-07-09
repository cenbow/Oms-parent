Ext.define('MB.controller.RegionQueryController', {
	extend : 'Ext.app.Controller',
	stores : ['MB.store.CountryRegionStore',
	          'MB.store.ProvinceRegionStore',
	          'MB.store.CityRegionStore',
	          'MB.store.DistrictRegionStore',
	          'MB.store.RegionStore',
	          'MB.store.IsCodStore',
	          'MB.store.CodPosStore',
	          'MB.store.IsCacStore',
	          'MB.store.IsVerifyTelStore'
	          ],
	models : ['MB.model.QueryRegionModel',
	          'MB.model.RegionModel',
	          'MB.model.ComboModel'
	          ],
	views : ['MB.view.regionManagement.RegionQueryPanlView',
	         'MB.view.regionManagement.RegionQueryGridView',
	         'MB.view.regionManagement.CountryRegionCombo',
		     'MB.view.regionManagement.ProvinceRegionCombo',
		     'MB.view.regionManagement.CityRegionCombo',
		     'MB.view.regionManagement.DistrictRegionCombo',
		     'MB.view.regionManagement.RegionAdd',
		     'MB.view.regionManagement.IsCodCombo',
		     'MB.view.regionManagement.CodPosCombo',
		     'MB.view.regionManagement.IsCacCombo',
		     'MB.view.regionManagement.IsVerifyTelCombo',
		     'MB.view.regionManagement.RegionEdit'
		     ],
	refs : [{
				ref : 'regionQueryPanlView',
				selector : 'regionQueryPanlView'
			},{
				ref : 'regionQueryGridView',
				selector : 'regionQueryGridView'
			},{
				ref : 'regionAdd',
				selector : 'regionAdd'
			}],
	init : function() {
		var me = this;
		me.control({
			'regionQueryPanlView button[action=reset]':{
				click : this.reset
			},
			'regionQueryPanlView button[action=search]':{
				click : this.search
			},
			'regionQueryPanlView button[action=openAdd]':{
				click : this.openAdd
			},
			 'regionAdd button[action=closeAdd]':{
					click : this.closeAdd
			},
			 'regionAdd button[action=resetAdd]':{
					click : this.resetAdd
			},
			 'regionAdd button[action=doAdd]':{
					click : this.doAdd
			},
			 'regionEdit button[action=doSaveEdit]':{
					click : this.doSaveEdit
			},
			 'regionEdit button[action=closeEdit]':{
					click : this.closeEdit
			}
		});
	},
	
	reset: function (btn) {
		var form = btn.up('form');
		form.reset();
	},
	search : function(btn){
		var pageSize = 20;
		var form = btn.up('form');//当前按钮form	
//		var initParams = {start : 0, limit : pageSize };这里很奇怪，角色管理将这个参数设置到searchParams中就没问题，资源管理和此处若设置了该参数，点击查询按钮后翻页内容不更新，真是rilegoule
		var searchParams = getFormParams(form, null);
		console.dir(searchParams);
		var regionQueryGridViewRegionList = form.up('viewport').down("regionQueryGridView");
		regionQueryGridViewRegionList.store.on('beforeload', function (store, options){
			Ext.apply(store.proxy.extraParams, searchParams);
		});
		regionQueryGridViewRegionList.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		regionQueryGridViewRegionList.store.pageSize = pageSize;
		regionQueryGridViewRegionList.store.load({params : searchParams});
	},
	openAdd : function(){
		var win = Ext.widget("regionAdd");
		win.show();
	},
	closeAdd : function(btn){
		var win = btn.up("window");
		win.close();
	},
	resetAdd : function(btn){
		var win = btn.up("window");
		win.down('form').reset();
	},
	doAdd : function(btn){
		//获取添加面板上的参数
		var win = btn.up("window");
		form = win.down("form");
		var formValues=form.getForm().getValues();
		//非空判断
		var regionId = form.getForm().findField('regionId').getValue();
		if(regionId==null||regionId==''){
			Ext.Msg.alert('提示', '区域ID不能为空');
			form.getForm().findField('regionId').focus();
			return;
		}
		var regionName = form.getForm().findField('regionName').getValue();
		if(regionName==null||regionName==''){
			Ext.Msg.alert('提示', '区域名称不能为空');
			form.getForm().findField('regionName').focus();
			return;
		}
		//获取查询面板form
		var countryRegion = Ext.getCmp('regionQueryPanlViewCountryRegion').value;
		var provinceRegion = Ext.getCmp('regionQueryPanlViewProvinceRegion').value;
		var cityRegion = Ext.getCmp('regionQueryPanlViewCityRegion').value;
		var districtRegion = Ext.getCmp('regionQueryPanlViewDistrictRegion').value;
		//合并参数
		var doAddFormValues = Ext.apply(formValues,{
			countryRegion : countryRegion,
			provinceRegion : provinceRegion,
			cityRegion : cityRegion,
			districtRegion : districtRegion
		});
		console.dir(doAddFormValues);
		
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/regionManagement/doAddRegion.spmvc',
			method : 'post',
			timeout:600000,
			params : doAddFormValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				console.log(respText);
				if(respText.code=='0'){
					Ext.Msg.alert('下级区域添加结果', respText.msg);
					win.close();
					Ext.widget("regionQueryGridView").store.reload();//刷新查询列表数据
					//以下刷新查询面板数据
					Ext.widget("countryRegionCombo").store.reload();//刷新国家数据
					Ext.widget("provinceRegionCombo").store.reload();//刷新省份数据
					Ext.widget("cityRegionCombo").store.reload();//刷新城市数据
					Ext.widget("districtRegionCombo").store.reload();//刷新区县数据
				}else{
					Ext.Msg.alert('下级区域添加结果', respText.msg);
				}
			},
			failure : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('下级区域添加结果', respText.msg);
			}
		});
	},
	closeEdit : function(btn){
		var win = btn.up("window");
		win.close();
	},
	doSaveEdit : function(btn){
		var win = btn.up("window");
		form = win.down("form");
		var formValues=form.getForm().getValues();
		console.dir(formValues);
		//参数flag表示是否同步下级区域的快递费用、EMS费用、货到付款费用、是否支持货到付款、是否支持POS刷卡、是否支持自提、是否支持货到付款验证手机号，0表示不同步，1表示同步
		Ext.MessageBox.show({
			title : '提示',
			buttons : Ext.Msg.YESNOCANCEL,
			msg : '是否同时同步所有下级区域的快递费用、EMS费用、货到付款费用、是否支持货到付款、是否支持POS刷卡、是否支持自提、是否支持货到付款验证手机号信息？点击yes同步，点击no不同步，点击cancel取消保存！',
			fn: function(e){
				if(e=='yes'){
					Ext.Ajax.request( {
						waitMsg : '请稍等.....',
						url :  basePath + 'custom/regionManagement/doSaveEdit.spmvc?flag=1',
						method : 'post',
						timeout:600000,
						params : formValues,
						success : function(response) {
							var respText = Ext.JSON.decode(response.responseText);
							console.log(respText);
							if(respText.code=='0'){
								Ext.Msg.alert('编辑区域保存结果', respText.msg);
								win.close();
								Ext.widget("regionQueryGridView").store.reload();//刷新查询列表数据
								//以下刷新查询面板数据
								Ext.widget("countryRegionCombo").store.reload();//刷新国家数据
								Ext.widget("provinceRegionCombo").store.reload();//刷新省份数据
								Ext.widget("cityRegionCombo").store.reload();//刷新城市数据
								Ext.widget("districtRegionCombo").store.reload();//刷新区县数据
							}else{
								Ext.Msg.alert('编辑区域保存结果结果', respText.msg);
							}
						},
						failure : function(response) {
							var respText = Ext.JSON.decode(response.responseText);
							Ext.Msg.alert('编辑区域保存结果', respText.msg);
						}
					});
				}else if(e=='no'){
					Ext.Ajax.request( {
						waitMsg : '请稍等.....',
						url :  basePath + 'custom/regionManagement/doSaveEdit.spmvc?flag=0',
						method : 'post',
						timeout:600000,
						params : formValues,
						success : function(response) {
							var respText = Ext.JSON.decode(response.responseText);
							console.log(respText);
							if(respText.code=='0'){
								Ext.Msg.alert('编辑区域保存结果', respText.msg);
								win.close();
								Ext.widget("regionQueryGridView").store.reload();//刷新查询列表数据
								//以下刷新查询面板数据
								Ext.widget("countryRegionCombo").store.reload();//刷新国家数据
								Ext.widget("provinceRegionCombo").store.reload();//刷新省份数据
								Ext.widget("cityRegionCombo").store.reload();//刷新城市数据
								Ext.widget("districtRegionCombo").store.reload();//刷新区县数据
							}else{
								Ext.Msg.alert('编辑区域保存结果结果', respText.msg);
							}
						},
						failure : function(response) {
							var respText = Ext.JSON.decode(response.responseText);
							Ext.Msg.alert('编辑区域保存结果', respText.msg);
						}
					});
				}else if(e=='cancel'){
					return;
				}
			},
		});		
	}
	
});