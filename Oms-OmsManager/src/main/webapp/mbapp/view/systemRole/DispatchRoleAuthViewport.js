Ext.define("MB.view.systemRole.DispatchRoleAuthViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.dispatchRoleAuthViewport',
	requires: [    
	],
	items : [ {
		title : '分派角色权限',
		xtype: 'systemRolePanlView'
	}]
});