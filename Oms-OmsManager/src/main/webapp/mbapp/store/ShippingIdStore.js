/**
 *快递种类
 ***/
Ext.define("MB.store.ShippingIdStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.shipping_id
});