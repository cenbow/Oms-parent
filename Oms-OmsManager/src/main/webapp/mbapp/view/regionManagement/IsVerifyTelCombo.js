/**
 * 支持货到付款验证手机号
 */
Ext.define("MB.view.regionManagement.IsVerifyTelCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isVerifyTelCombo',
	store : 'MB.store.IsVerifyTelStore',
	name : 'isVerifyTel',
	hiddenName : 'isVerifyTel',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});