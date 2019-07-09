Ext.define("MB.view.common.ReturnShippingCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.returnShippingCombo',
	store: 'ReturnShippingStore',
	name: 'id',
	displayField: 'name',
	valueField: 'id',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
	hiddenName: 'id',
	emptyText: '请选择',
	editable: true
	
});