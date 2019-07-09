Ext.define("MB.store.RegionDistrictCombStore", {
	extend: "Ext.data.Store",
	model: "MB.model.RegionCombModel",
	proxy: {
		type: 'ajax',
		url:basePath+'custom/common/getSystemRegionAreaList.spmvc',
	}
});