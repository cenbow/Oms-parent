/**
 * 新增面板入参：是否支持货到付款
 */
Ext.define("MB.view.shippingManagement.SupportCodForAddCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.supportCodForAddCombo',
	store : 'MB.store.SupportCodForAddStore',
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