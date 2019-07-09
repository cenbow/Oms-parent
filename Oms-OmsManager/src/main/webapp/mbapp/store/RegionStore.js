Ext.define("MB.store.RegionStore", {
	extend: "Ext.data.Store",
	model: "MB.model.RegionModel",
	autoLoad: true,
	pageSize : 20,// 每页显示条目数量
    proxy: {
    	type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url:  basePath + 'custom/regionManagement/getRegionListByParentId.spmvc',
		reader: {
			rootProperty: 'root',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
    }

});