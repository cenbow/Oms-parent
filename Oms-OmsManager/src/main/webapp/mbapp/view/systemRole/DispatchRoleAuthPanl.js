
var pageSize = 20;
Ext.define("MB.view.systemRole.DispatchRoleAuthPanl", {
	extend : "Ext.form.Panel",
	alias : 'widget.dispatchRoleAuthPanl',
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
		
		this.items = [ {
			items : [{
				xtype : 'checkbox',
				id : 'systemRoleRoleName',
				name : 'roleName',
				fieldLabel : "<b>角色名称 </b>",
				width : 300,
				columnWidth : 0.5
			},{
				xtype : 'textfield',
				id : 'systemRoleRoleCode',
				name : 'roleCode',
				fieldLabel : "<b>角色编码 </b>",
				width : 300,
				columnWidth : 0.5
			}],
			buttons : [{
				text : '保存',
				columnWidth : .1,
				action : 'save',
			
			}]
			
		}];

		this.callParent(arguments);
	}
	
});

