/**
 * 新增面板：是否支持在线支付
 */
Ext.define("MB.view.paymentManagement.IsOnlineForAddCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isOnlineForAddCombo',
	store : 'MB.store.IsOnlineForAddStore',
	name : 'enabled',
	hiddenName : 'enabled',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>支持在线支付</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});