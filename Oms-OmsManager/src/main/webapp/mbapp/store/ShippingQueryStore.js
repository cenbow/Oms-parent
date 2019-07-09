Ext.define("MB.store.ShippingQueryStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ShippingQueryModel",
	autoLoad: true,
	pageSize : 20,// 每页显示条目数量
    proxy: {
    	type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url:  basePath + 'custom/shippingManagement/getShippingQueryList.spmvc',
		reader: {
			rootProperty: 'root',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
    }

});