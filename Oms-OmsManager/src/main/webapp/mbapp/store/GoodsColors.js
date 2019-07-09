Ext.define("MB.store.GoodsColors", {
	extend: "Ext.data.Store",
	model: "MB.model.ProductBarcodeList",
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		url : basePath + 'custom/common/getProductBarcodeListByGoodsSn',
		reader : {
			type : 'json'
		}
	}
});