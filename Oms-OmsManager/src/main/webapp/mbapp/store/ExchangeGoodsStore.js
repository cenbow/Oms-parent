Ext.define("MB.store.ExchangeGoodsStore", {
	extend: "Ext.data.Store",
	model: "MB.model.GoodDetailModel",
	groupField:'depotCode'
});