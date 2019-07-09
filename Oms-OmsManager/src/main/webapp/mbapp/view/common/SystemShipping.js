Ext.define("MB.view.common.SystemShipping", {
	extend: "Ext.form.field.ComboBox",
//	id:'systemPayment',
	alias: 'widget.commonsystemShipping',
	store: 'SystemShippingStore',
	name: 'shippingCode',
	displayField: 'shippingName',
	valueField: 'shippingCode',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
	fieldLabel: '承运商',
	hiddenName: 'payId',
	emptyText: '请选择承运商',
	editable: false
});