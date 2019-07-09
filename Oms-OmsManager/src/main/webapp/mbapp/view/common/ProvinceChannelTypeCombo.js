Ext.define("MB.view.common.ProvinceChannelTypeCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.provinceChannelTypeCombo',
	store: 'ProvinceStore',
	name: 'province',
	displayField: 'regionName',
	valueField: 'regionId',
	queryMode: 'remote',
	width: 220,
	labelWidth: 70,
	fieldLabel: '省份',
	hiddenName: 'province',
	emptyText: '请选择省',
	editable: false,
	initComponent: function() {
		this.callParent(arguments);
	}
});