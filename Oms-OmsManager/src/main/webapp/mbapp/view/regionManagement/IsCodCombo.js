/**
 * 是否支持货到付款
 */
Ext.define("MB.view.regionManagement.IsCodCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isCodCombo',
	store : 'MB.store.IsCodStore',
	name : 'isCod',
	hiddenName : 'isCod',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});