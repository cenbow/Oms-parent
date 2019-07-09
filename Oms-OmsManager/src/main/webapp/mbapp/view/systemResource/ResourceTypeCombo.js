Ext.define("MB.view.systemResource.ResourceTypeCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.resourceTypeCombo',
	store : 'ResourceTypeStore',
	name : 'resourceType',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	width : 220,
	labelWidth : 70,
	fieldLabel : '<b>资源类型</b>',
	hiddenName : 'resourceType',
	emptyText : '请选择资源类型',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});