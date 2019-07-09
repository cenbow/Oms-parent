Ext.define("MB.store.RegionCountryCombStore", {
	extend: "Ext.data.Store",
	model: "MB.model.RegionCombModel",
	proxy: {
		type: 'ajax',
		url:basePath+'custom/common/getSystemRegionAreaList.spmvc?parentId=0',
	}
});