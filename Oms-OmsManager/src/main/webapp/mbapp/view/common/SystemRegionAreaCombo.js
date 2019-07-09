Ext.define("MB.view.common.SystemRegionAreaCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.systemRegionAreaCombo',
	store: 'SystemRegionAreaStore',
	name: 'regionId',
	displayField: 'regionName',
	valueField: 'regionId',
	queryMode: 'remote',
	width: '220',
	labelWidth: '70',
	fieldLabel: '省',
	hiddenName: 'regionId',
	emptyText: '请选择省',
	editable: false
});