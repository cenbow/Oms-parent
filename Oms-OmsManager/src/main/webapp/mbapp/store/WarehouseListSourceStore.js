Ext.define("MB.store.WarehouseListSourceStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ReturnErpWarehouseModel",
	proxy: {	
		type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url:basePath+'custom/orderReturn/getWarehouseList.spmvc',
		reader: {
			type:'json'
		}
	}
});