/**
 * 编辑面板条件：该配送方式是否被禁用，1，可用；0，禁用
 */
Ext.define("MB.view.shippingManagement.EnabledForAddCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.enabledForAddCombo',
	store : 'MB.store.EnabledForAddStore',
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