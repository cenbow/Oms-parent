/**
 *城市 
 ***/
Ext.define("MB.view.common.AreaCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.areaCombo',
	store: 'AreaStore',
	name: 'district',
	displayField: 'regionName',
	valueField: 'regionId',
	queryMode: 'remote',
	width: 230,
	labelWidth: 80,
	fieldLabel: '地区',
	hiddenName: 'district',
	emptyText: '请选择地区',
	editable: false,
	initComponent: function() {
		this.callParent(arguments);
	}
});