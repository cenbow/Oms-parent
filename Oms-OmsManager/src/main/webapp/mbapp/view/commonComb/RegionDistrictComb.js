Ext.define("MB.view.commonComb.RegionDistrictComb", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.regionDistrictComb',
	store : 'MB.store.RegionDistrictCombStore',
	displayField : 'regionName',
	valueField : 'regionId',
	id : 'districtComb',
	queryMode : 'remote',
	fieldLabel : '<b>区县</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
	/*,
	listeners: {
		change : function(combo, newValue ,oldValue){
			var regionStreetComb = combo.up('form').down('#streetComb');
			var regionStreetCombStore = regionStreetComb.store;
			regionStreetCombStore.getProxy().url=basePath+'custom/common/getSystemRegionAreaList.spmvc?parentId='+combo.value;
			regionStreetCombStore.load();
		},
		select : function( combo, records, eOpts ){
			var regionStreetComb = combo.up('form').down('#streetComb');
			regionStreetComb.clearValue();
		}
	}*/
});