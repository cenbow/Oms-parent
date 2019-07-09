/**
 * 新增面板：知否支持手机渠道使用
 */
Ext.define("MB.view.paymentManagement.IsMobileForAddCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isMobileForAddCombo',
	store : 'MB.store.IsMobileForAddStore',
	name : 'enabled',
	hiddenName : 'enabled',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>支持手机渠道使用</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});