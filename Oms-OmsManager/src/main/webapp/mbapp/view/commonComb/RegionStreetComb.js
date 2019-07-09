Ext.define("MB.view.commonComb.RegionStreetComb", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.regionStreetComb',
	store : 'MB.store.RegionStreetCombStore',
	displayField : 'regionName',
	valueField : 'regionId',
	id : 'streetComb',
	queryMode : 'remote',
	fieldLabel : '<b>街道</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});