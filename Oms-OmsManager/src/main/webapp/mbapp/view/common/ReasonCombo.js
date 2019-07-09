Ext.define("MB.view.common.ReasonCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.reasoncombo',
	store: 'ReasonStore',
	name: 'reason',
	displayField: 'n',
	valueField: 'v',
	queryMode: 'local',
	width: 220,
	labelWidth: 70,
	fieldLabel: '原因',
	hiddenName: 'reason',
	emptyText: '请选择原因',
	editable: false,
	initComponent: function() {
		this.callParent(arguments);
	}
});