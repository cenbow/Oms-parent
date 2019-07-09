/**
 * 新增面板：是否支持货到付款
 */
Ext.define("MB.view.paymentManagement.IsCodForAddCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isCodForAddCombo',
	store : 'MB.store.IsCodForAddStore',
	name : 'enabled',
	hiddenName : 'enabled',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>支持货到付款</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});