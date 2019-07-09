Ext.define("MB.view.common.ReturnReason", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commonreturnreason',
	store: 'ReturnReasonStore',
	name: 'returnReason',
	displayField: 'name',
	valueField: 'code',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
	fieldLabel: '退单理由',
	hiddenName: 'code',
	emptyText: '请选择退单理由',
	editable: false
});