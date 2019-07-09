Ext.define('MB.controller.ShippingManagementController', {
	extend : 'Ext.app.Controller',
	stores : ['MB.store.SupportCodStore',
	          'MB.store.EnabledStore',
	          'MB.store.ShippingQueryStore',
	          'MB.store.SupportCodForAddStore',
	          'MB.store.IsReceivePrintForAddStore',
	          'MB.store.DefalutDeliveryForAddStore',
	          'MB.store.EnabledForAddStore'
	          ],
	models : ['MB.model.ComboModel',
	          'MB.model.ShippingQueryModel'
	          ],
	views : ['MB.view.shippingManagement.ShippingQueryPanlView',
	         'MB.view.shippingManagement.ShippingQueryGridView',
	         'MB.view.shippingManagement.SupportCodCombo',
	         'MB.view.shippingManagement.EnabledCombo',
	         'MB.view.shippingManagement.ShippingAdd',
	         'MB.view.shippingManagement.SupportCodForAddCombo',
	         'MB.view.shippingManagement.IsReceivePrintForAddCombo',
	         'MB.view.shippingManagement.DefalutDeliveryForAddCombo',
	         'MB.view.shippingManagement.ShippingEdit',
	         'MB.view.shippingManagement.EnabledForAddCombo',
	         'MB.view.shippingManagement.IsCommonUseCombo'
		     ],
	refs : [{
				ref : 'shippingQueryPanlView',
				selector : 'shippingQueryPanlView'
			},{
				ref : 'shippingQueryGridView',
				selector : 'shippingQueryGridView'
			},{
				ref : 'shippingEdit',
				selector : 'shippingEdit'
			}],
	init : function() {
		var me = this;
		me.control({
			'shippingQueryPanlView button[action=reset]':{
				click : this.reset
			},
			'shippingQueryPanlView button[action=search]':{
				click : this.search
			},
			'shippingQueryPanlView button[action=openAdd]':{
				click : this.openAdd
			},
			 'shippingAdd button[action=closeAdd]':{
					click : this.closeAdd
			},
			 'shippingAdd button[action=resetAdd]':{
					click : this.resetAdd
			},
			 'shippingAdd button[action=doAdd]':{
					click : this.doAdd
			},
			 'shippingEdit button[action=doSaveEdit]':{
					click : this.doSaveEdit
			},
			 'shippingEdit button[action=closeEdit]':{
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
		var shippingQueryGridViewShippingList = form.up('viewport').down("shippingQueryGridView");
		shippingQueryGridViewShippingList.store.on('beforeload', function (store, options){
			Ext.apply(store.proxy.extraParams, searchParams);
		});
		shippingQueryGridViewShippingList.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		shippingQueryGridViewShippingList.store.pageSize = pageSize;
		shippingQueryGridViewShippingList.store.load({params : searchParams});
	},
	openAdd : function(){
		var win = Ext.widget("shippingAdd");
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
		var shippingCode = form.getForm().findField('shippingCode').getValue();
		if(shippingCode==null||shippingCode==''){
			Ext.Msg.alert('提示', '承运商编码不能为空');
			form.getForm().findField('shippingCode').focus();
			return;
		}
		var shippingName = form.getForm().findField('shippingName').getValue();
		if(shippingName==null||shippingName==''){
			Ext.Msg.alert('提示', '承运商名称不能为空');
			form.getForm().findField('shippingName').focus();
			return;
		}
		var shippingDesc = form.getForm().findField('shippingDesc').getValue();
		if(shippingDesc==null||shippingDesc==''){
			Ext.Msg.alert('提示', '承运商描述不能为空');
			form.getForm().findField('shippingDesc').focus();
			return;
		}
		var insure = form.getForm().findField('insure').getValue();
		if(insure==null||insure==''){
			if(insure!='0'){
				Ext.Msg.alert('提示', '保价费用不能为空');
				form.getForm().findField('insure').focus();
				return;
			}
		}
		
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/shippingManagement/doAddShipping.spmvc',
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				console.log(respText);
				if(respText.code=='0'){
					Ext.Msg.alert('承运商添加', respText.msg);
					win.close();
					Ext.widget("shippingQueryGridView").store.reload();//刷新查询列表数据
				}else{
					Ext.Msg.alert('承运商添加', respText.msg);
				}
			},
			failure : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('承运商添加', respText.msg);
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
		//非空判断
		var shippingCode = form.getForm().findField('shippingCode').getValue();
		if(shippingCode==null||shippingCode==''){
			Ext.Msg.alert('提示', '承运商编码不能为空');
			form.getForm().findField('shippingCode').focus();
			return;
		}
		var shippingName = form.getForm().findField('shippingName').getValue();
		if(shippingName==null||shippingName==''){
			Ext.Msg.alert('提示', '承运商名称不能为空');
			form.getForm().findField('shippingName').focus();
			return;
		}
		var shippingDesc = form.getForm().findField('shippingDesc').getValue();
		if(shippingDesc==null||shippingDesc==''){
			Ext.Msg.alert('提示', '承运商描述不能为空');
			form.getForm().findField('shippingDesc').focus();
			return;
		}
		var insure = form.getForm().findField('insure').getValue();
		if(insure==null||insure==''){
			if(insure!='0'){
				Ext.Msg.alert('提示', '保价费用不能为空');
				form.getForm().findField('insure').focus();
				return;
			}
		}
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/shippingManagement/doSaveEdit.spmvc',
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				console.log(respText);
				if(respText.code=='0'){
					Ext.Msg.alert('编辑承运商', respText.msg);
					win.close();
					Ext.widget("shippingQueryGridView").store.reload();//刷新查询列表数据
				}else{
					Ext.Msg.alert('编辑承运商', respText.msg);
				}
			},
			failure : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('编辑承运商', respText.msg);
			}
		});
	}
	
});