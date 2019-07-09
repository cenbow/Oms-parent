/**
 * 处理方式
 */

Ext.define("MB.view.common.ProcessType", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commonprocessType',
	store: 'ProcessTypeStore',
	name: 'id',
	displayField: 'name',
	valueField: 'id',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
//	fieldLabel: '支付方式',
	hiddenName: 'id',
	emptyText: '请选择',
	editable: false
	
});