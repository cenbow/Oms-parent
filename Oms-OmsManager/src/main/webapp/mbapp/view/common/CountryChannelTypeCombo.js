Ext.define("MB.view.common.CountryChannelTypeCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.countryChannelTypeCombo',
	store: 'CountryStore',
	name: 'country',
	displayField: 'regionName',
	valueField: 'regionId',
	queryMode: 'remote',
	width: 220,
	labelWidth: 70,
	fieldLabel: '国家',
	hiddenName: 'country',
	emptyText: '请选择国家',
	editable: false,
	initComponent: function() {
		this.callParent(arguments);
	}
});