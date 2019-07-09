Ext.define('MB.controller.SystemRoleController', {
	extend : 'Ext.app.Controller',
	stores : [
	          'SystemRoleStore'
	],
	models : [
	             'SystemRoleModel'
	],
	views : [
	         
	         'systemRole.SystemRoleDispatchEdit',
	          'systemRole.SystemRoleGridView',
	          'systemRole.SystemRolePanlView',
	           'systemRole.SystemRoleEdit'
	],
	refs : [
        {
			ref : 'systemRolePanlView',
			selector : 'systemRolePanlView'
        } ,{
			ref : 'systemRoleEdit',
			selector : 'systemRoleEdit'
        }
	],
	init : function() {
		var me = this;
		me.control(
			{
				'systemRolePanlView button[action=add]':{
					click : this.addRole
				},
			    'systemRoleEdit button[action=save]':{
					click : this.saveRole
				},
				 'systemRolePanlView button[action=search]':{
						click : this.search
				},
				 'systemRolePanlView button[action=reset]':{
						click : this.reset
				},
				 'systemRoleEdit button[action=close]':{
						click : this.closeRole
				}
				
			}
		
		);
	},
	initPage: function (win, form) {
		
	},
	onLaunch : function() {
		
	},
	saveSystemResource : function(btn) {

		var win = btn.up("window");
		form = win.down("form");
		
		var formValues=form.getForm().getValues();
		
		var  resourceId =  formValues["resourceId"];
		var  parentId =  formValues["parentId"];
		var  parentCode =  formValues["parentCode"];
		var  resourceType =  formValues["resourceType"];
		var  resourceName =  formValues["resourceName"];
		var  resourceCode =  formValues["resourceCode"];
		var  resourceUrl =  formValues["resourceUrl"];
		var  sortOrder =  formValues["sortOrder"];	
		var  rootId =  formValues["rootId"];

		var mod="add";
		if(null != resourceId && "" != resourceId){
			mod="update";
		}
		
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/systemResource/addSystemOmsResource.spmvc?method='+mod,
			method : 'post',	
			timeout:600000,
			params : {		
				  'resourceId': resourceId,  
				  'parentId': parentId,   
				  'parentCode': parentCode,  
				  'resourceType': resourceType, 
				  'resourceName': resourceName,  
				  'resourceCode':resourceCode,   
				  'resourceUrl': resourceUrl,  
				  'sortOrder' : sortOrder, 
				  'rootId' : rootId   
			}, 
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				if(respText.isok){
					alert(respText.message);
					win.close();
			
				}else{
				
					alert(respText.message);
				}				
			},
			failure : function(response) {
                alert("失败");
			}
		});
		

	},
	searchRole:function(btn){
		
		
		var pageSize = 20;

		var form = btn.up('form');//当前按钮form	
		
		var initParams = {start : 0, limit : pageSize };
		var searchParams = getFormParams(form, initParams);

		var roleName = Ext.getCmp("systemRoleRoleName").getValue();
		var roleCode = Ext.getCmp("systemRoleRoleCode").getValue();
		
		searchParams.roleName = roleName;
		searchParams.roleCode = roleCode;
		
		var systemRoleGridOrderList = Ext.widget("systemRoleGridView");
		systemRoleGridOrderList.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		systemRoleGridOrderList.store.pageSize = pageSize;
		systemRoleGridOrderList.store.load({params : searchParams});
		
	},
	
	closeRole : function(btn) {
		var win = btn.up("window");
		win.close();
	},
	
	deleTeRole:function(btn){
		var win = btn.up("window");
		form = win.down("form");
		var formValues=form.getForm().getValues();
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/systemResource/addSystemOmsRole.spmvc?method='+mod,
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				if(respText.isok){
					Ext.msgBox.msg('删除资源', respText.message, Ext.MessageBox.INFO);
					win.close();
		
				}else{
					Ext.msgBox.remainMsg('删除资源', respText.message, Ext.MessageBox.ERROR);
				}
			},
			failure : function(response) {
				Ext.msgBox.remainMsg('删除资源', "操作异常", Ext.MessageBox.ERROR);
			}
		});
	},
	search: function (btn) {
		
		var pageSize = 20;

		var form = btn.up('form');//当前按钮form	
		
		var initParams = {start : 0, limit : pageSize };
		var searchParams = getFormParams(form, initParams);

		var roleName = Ext.getCmp("systemRoleRoleName").getValue();
		var roleCode = Ext.getCmp("systemRoleRoleCode").getValue();
		
		searchParams.roleName = roleName;
		searchParams.roleCode = roleCode;
		
		var systemRoleGridOrderList = Ext.widget("systemRoleGridView");
		systemRoleGridOrderList.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		systemRoleGridOrderList.store.pageSize = pageSize;
		systemRoleGridOrderList.store.load({params : searchParams});
			
	},
	reset: function (btn) {
		var form = btn.up('form');
		form.reset();
	},
	addRole: function (btn) {
	
		var win = Ext.widget("systemRoleEdit");
		win.show();
		
	},
	saveRole: function (btn) {
		
		var win = btn.up("window");
		form = win.down("form");
		var formValues=form.getForm().getValues();
		console.dir(formValues);

		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/systemResource/addSystemOmsRole.spmvc',
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				if(respText.isok){
					Ext.msgBox.msg('添加/修改资源', respText.message, Ext.MessageBox.INFO);
					win.close();
					Ext.widget("systemRoleGridView").store.reload();//刷新查询列表数据
				}else{
					Ext.msgBox.remainMsg('添加/修改资源', respText.message, Ext.MessageBox.ERROR);
				}
			},
			failure : function(response) {
				Ext.msgBox.remainMsg('添加/修改资源', "操作异常", Ext.MessageBox.ERROR);
			}
		});
		
	}
	
});