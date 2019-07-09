Ext.define("MB.view.commonComb.RegionCountryComb", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.regionCountryComb',
	store : 'MB.store.RegionCountryCombStore',
	displayField : 'regionName',
	valueField : 'regionId',
	id : 'countryComb',
	queryMode : 'remote',
	fieldLabel : '<b>国家</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	},
	listeners: {
		change : function(combo, newValue ,oldValue){
			var regionProvinceComb = combo.up('form').down('#provinceComb');
			var regionProvinceCombStore = regionProvinceComb.store;
			regionProvinceCombStore.getProxy().url=basePath+'custom/common/getSystemRegionAreaList.spmvc?parentId='+combo.value;
			regionProvinceCombStore.load();
		},
		select : function( combo, records, eOpts ){
			var regionProvinceComb = combo.up('form').down('#provinceComb');
			regionProvinceComb.clearValue();
			var regionCityComb = combo.up('form').down('#cityComb');
			regionCityComb.clearValue();
			var regionDistrictComb = combo.up('form').down('#districtComb');
			regionDistrictComb.clearValue();
			var regionStreetComb = combo.up('form').down('#streetComb');
			regionStreetComb.clearValue();
		}
	}
});