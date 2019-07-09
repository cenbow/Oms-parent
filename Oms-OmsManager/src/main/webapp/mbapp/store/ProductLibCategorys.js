Ext.define("MB.store.ProductLibCategorys", {
	extend : "Ext.data.Store",
	model : "MB.model.ProductLibCategory",
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		url : basePath + 'custom/common/getProductLibCategory',
		reader : {
			type : 'json'
		}
	}
});