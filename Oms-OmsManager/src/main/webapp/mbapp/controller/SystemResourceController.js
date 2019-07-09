Ext.define('MB.controller.SystemResourceController', {
	extend : 'Ext.app.Controller',
	stores : ['SystemResourceStore','ResourceTypeStore','IsShowStore'],
	models : ['SystemResourceModel','ComboModel'],
	views : [ 'systemResource.SystemResourceGridView',
	          'systemResource.SystemResourcePanlView',
	          'systemResource.ResourceTypeCombo',
	          'systemResource.SystemResourceEdit',
	          'systemResource.IsShowType'
	],
	refs : [{
		ref : 'systemResourceGridView',
		selector : 'systemResourceGridView'
	} ,{
		ref : 'systemResourceEdit',
		selector : 'systemResourceEdit'
	}],
	init : function() {
		var me = this;
		me.control({
			'systemResourcePanlView button[action=reset]':{
				click : this.reset
			},
			'systemResourcePanlView button[action=add]':{
				click : this.add
			},
			'systemResourceEdit button[action=close]' : {
				click : this.closeSystemResource
			},
			'systemResourceEdit button[action=save]' : {
				click : this.saveSystemResource
			}
		});
	},
	initPage: function (win, form) {
			
	},
	onLaunch : function() {
		
	},
	saveSystemResource : function(btn) {
		var win = btn.up("window");
		form = win.down("form");
		var formValues=form.getForm().getValues();
		console.dir(formValues);
		var mod="add";
		if(null != formValues.resourceId && "" != formValues.resourceId){
			mod="update";
		}
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/systemResource/addSystemOmsResource.spmvc?method='+mod,
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				if(respText.isok){
					Ext.msgBox.msg('添加/修改资源', respText.message, Ext.MessageBox.INFO);
					win.close();
					
					var systemResourceGridView = Ext.widget("systemResourceGridView");
					systemResourceGridView.store.reload();
			
				}else{
					Ext.msgBox.remainMsg('添加/修改资源', respText.message, Ext.MessageBox.ERROR);
				}
			},
			failure : function(response) {
				Ext.msgBox.remainMsg('添加/修改资源', "操作异常", Ext.MessageBox.ERROR);
			}
		});
	},
	closeSystemResource : function(btn) {
		var win = btn.up("window");
		win.close();
	},
	reset: function (btn) {
		var form = btn.up('form');
		form.reset();
	},
	add: function (btn) {
		var win = Ext.widget("systemResourceEdit");
		win.show();
		
	}
	
});