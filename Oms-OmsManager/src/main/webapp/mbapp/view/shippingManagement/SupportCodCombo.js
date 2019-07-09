/**
 * 查询面板条件：是否支持货到付款
 */
Ext.define("MB.view.shippingManagement.SupportCodCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.supportCodCombo',
	store : 'MB.store.SupportCodStore',
	name : 'supportCod',
	hiddenName : 'supportCod',
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