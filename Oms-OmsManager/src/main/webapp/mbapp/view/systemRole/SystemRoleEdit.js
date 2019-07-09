/***
 * 编辑角色信息：双击列表选项，弹出窗口，
 * 
 * 
 ****/

Ext.define("MB.view.systemRole.SystemRoleEdit", {
	extend: "Ext.window.Window",
	alias: "widget.systemRoleEdit",
	title: "编辑角色信息",
	width: 400,
	height: 400,
	layout: "fit",
	maskDisabled : false,
	modal : true,
	initComponent: function () {
		this.items = {
			xtype: "form",
			margin: 5,
			border: false,
			frame: true,
			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 70
			},	
		    reader: new Ext.data.reader.Json({
				rootProperty : "data",
				model : 'MB.model.SystemRoleModel'
			}),	
			items: [
				{ xtype: "textfield", name: "roleCode", fieldLabel: "角色编码"},
				{ xtype: "textfield", name: "roleName", fieldLabel: "角色名称" },
				{ xtype: "textfield", name: "roleDesc", fieldLabel: "角色备注" }
		
			]
		};
		this.buttons = [
			{ text: "保存", action: "save" },
			{ text: "关闭", action: "close" }
		];
		this.callParent(arguments);
	},
	initPage: function (form) {
		
	}
});