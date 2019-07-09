Ext.define("MB.view.common.TransTypeCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.transtypecombo',
	store: 'TransTypeStore',
	name: 'transType',
	displayField: 'n',
	valueField: 'v',
	queryMode: 'local',
	width: 220,
	labelWidth: 70,
	fieldLabel: '交易类型',
	hiddenName: 'transType',
	emptyText: '请选择交易类型',
	editable: false,
	initComponent: function() {
		this.callParent(arguments);
	}
});