Ext.define("MB.store.GoodsSizeCombStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ProductBarcodeListModel",
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		url : basePath + 'custom/orderGoodsEdit/getProductBarcodeListByGoodsSn',
		reader : {
			type : 'json'
		}
	}
});