/***
 * 查询栏 
 ****/
var pageSize = 20;
Ext.define("MB.view.systemRole.SystemRolePanlView", {
	extend : "Ext.form.Panel",
	alias : 'widget.systemRolePanlView',
	frame : true,
	head : true,
	width : '100%',
	layout : 'column',
	buttonAlign : 'center',// 按钮居中
	fieldDefaults : {
		labelAlign : 'right'
	},
	collapsible : true,
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 80,
			margin: '40 20 0 20',
			items : [
			    {
			    	columnWidth : 0.2
			    },
				{
					xtype : 'textfield',
					id : 'systemRoleRoleName',
					name : 'roleName',
					fieldLabel : "<b>角色名称 </b>",
					width : 200,
					columnWidth : 0.3
				},{
					xtype : 'textfield',
					id : 'systemRoleRoleCode',
					name : 'roleCode',
					fieldLabel : "<b>角色编码 </b>",
					width : 200,
					columnWidth : 0.3
				}]
		}];
		me.buttons = [{
			text : '添加角色',
			columnWidth : .1,
			action : 'add',
		
		},{
			text : '查询',
			columnWidth : .1,
			action : 'search'
		
		}, {
			text : '重置',
			action : 'reset'
	
		}];
		me.callParent(arguments);
	}
	
});

