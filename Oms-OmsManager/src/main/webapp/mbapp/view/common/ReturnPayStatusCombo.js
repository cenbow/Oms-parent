Ext.define("MB.view.common.ReturnPayStatusCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commonreturnPayStatus',
	store: 'ReturnPayStatusStore',
	name: 'id',
	displayField: 'name',
	valueField: 'id',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
	hiddenName: 'id',
	fieldLabel: '财务状态',
	emptyText: '请选择财务状态',
	editable: false
	
});