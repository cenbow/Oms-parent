/**
 *城市 
 ***/
Ext.define("MB.view.common.CityCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.cityCombo',
	store: 'CityStore',
	name: 'city',
	displayField: 'regionName',
	valueField: 'regionId',
	queryMode: 'remote',
	width: 220,
	labelWidth: 70,
	fieldLabel: '城市',
	hiddenName: 'city',
	emptyText: '请选择城市',
	editable: false,
	initComponent: function() {
		this.callParent(arguments);
	}
});