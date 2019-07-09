Ext.define("MB.view.systemRole.SystemRoleViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.systemRoleViewport',
	requires: [
	       'MB.view.systemRole.SystemRoleGridView',
	       'MB.view.systemRole.SystemRolePanlView',
	       'MB.model.SystemRoleModel',
	       'MB.store.SystemRoleStore',
	       'MB.view.systemRole.SystemRoleDispatchEdit',
	       'MB.view.systemRole.SystemRoleEdit'
	 ],

	items : [ {
		title : '角色资源查询',
		xtype: 'systemRolePanlView',
		region : "north",
		height : document.body.clientHeight * 0.3
	},{
		xtype: 'systemRoleGridView',
		region : "south",
		height: document.body.clientHeight * 0.7
	}]
});