Ext.define("MB.view.commonComb.RegionProvinceComb", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.regionProvinceComb',
	store : 'MB.store.RegionProvinceCombStore',
	displayField : 'regionName',
	valueField : 'regionId',
	id : 'provinceComb',
	queryMode : 'remote',
	fieldLabel : '<b>省份</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	},
	listeners: {
		change : function(combo, newValue ,oldValue){
			var regionCityComb = combo.up('form').down('#cityComb');
			var regionCityCombStore = regionCityComb.store;
			regionCityCombStore.getProxy().url=basePath+'custom/common/getSystemRegionAreaList.spmvc?parentId='+combo.value;
			regionCityCombStore.load();
		},
		select : function( combo, records, eOpts ){
			var regionCityComb = combo.up('form').down('#cityComb');
			regionCityComb.clearValue();
			var regionDistrictComb = combo.up('form').down('#districtComb');
			regionDistrictComb.clearValue();
			var regionStreetComb = combo.up('form').down('#streetComb');
			regionStreetComb.clearValue();
		}
	}
});