/**
 * 查询面板：是否在线支付
 */
Ext.define("MB.view.paymentManagement.IsOnlineCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isOnlineCombo',
	store : 'MB.store.IsOnlineStore',
	name : 'isOnline',
	hiddenName : 'isOnline',
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