Ext.define("MB.view.common.OrderCustomDefineCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.orderCustomDefineCombo',
	store: 'OrderCustomDefines',
	name: 'code',
	displayField: 'name',
	valueField: 'code',
	queryMode: 'remote',
	width: '400',
	labelWidth: '70',
	fieldLabel: '操作原因',
	hiddenName: 'code',
	emptyText: '请选择操作原因',
	allowBlank:true,
	editable: false
});