/**
 * 预付款/保证金
 */

Ext.define("MB.view.common.ReturnType", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commonreturnType',
	store: 'ReturnTypeStore',
	name: 'id',
	displayField: 'name',
	valueField: 'id',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
	hiddenName: 'id',
	fieldLabel: '退单类型',
	emptyText: '请选择退单类型',
	editable: false
	
});