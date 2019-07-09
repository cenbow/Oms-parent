/**
 * 查询面板条件：该配送方式是否被禁用，1，可用；0，禁用
 */
Ext.define("MB.view.shippingManagement.EnabledCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.enabledCombo',
	store : 'MB.store.EnabledStore',
	name : 'enabled',
	hiddenName : 'enabled',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>状态</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});