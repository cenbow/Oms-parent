Ext.define("MB.store.ReturnErpWarehouseStore", {
	extend : "Ext.data.Store",
	model : "MB.model.ReturnErpWarehouseModel",
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		url : basePath + 'custom/common/getReturnGoodsDepotList',
		reader : {
			type : 'json'
		}
	},
	autoLoad : true
});