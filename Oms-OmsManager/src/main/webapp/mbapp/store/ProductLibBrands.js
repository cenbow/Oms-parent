Ext.define("MB.store.ProductLibBrands", {
	extend : "Ext.data.Store",
	model : "MB.model.ProductLibBrand",
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		url : basePath + 'custom/common/getProductLibBrand',
		reader : {
			type : 'json'
		}
	}
});