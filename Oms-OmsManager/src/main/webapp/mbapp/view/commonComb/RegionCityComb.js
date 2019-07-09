Ext.define("MB.view.commonComb.RegionCityComb", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.regionCityComb',
	store : 'MB.store.RegionCityCombStore',
	displayField : 'regionName',
	valueField : 'regionId',
	id : 'cityComb',
	queryMode : 'remote',
	fieldLabel : '<b>城市</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	},
	listeners: {
		change : function(combo, newValue ,oldValue){
			var regionDistrictComb = combo.up('form').down('#districtComb');
			var regionDistrictCombStore = regionDistrictComb.store;
			regionDistrictCombStore.getProxy().url=basePath+'custom/common/getSystemRegionAreaList.spmvc?parentId='+combo.value;
			regionDistrictCombStore.load();
		},
		select : function( combo, records, eOpts ){
			var regionDistrictComb = combo.up('form').down('#districtComb');
			regionDistrictComb.clearValue();
			var regionStreetComb = combo.up('form').down('#streetComb');
			regionStreetComb.clearValue();
		}
	}
});