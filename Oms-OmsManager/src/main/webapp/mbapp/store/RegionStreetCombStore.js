Ext.define("MB.store.RegionStreetCombStore", {
	extend: "Ext.data.Store",
	model: "MB.model.RegionCombModel",
	proxy: {
		type: 'ajax',
		url:basePath+'custom/common/getSystemRegionAreaList.spmvc',
	}
});