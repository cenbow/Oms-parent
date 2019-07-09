/**
 * 预付款/保证金
 */

Ext.define("MB.view.common.SettlementType", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commonsettlementType',
	store: 'SettlementTypeStore',
	name: 'id',
	displayField: 'name',
	valueField: 'id',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
	hiddenName: 'id',
	emptyText: '请选择',
	editable: false
	
});