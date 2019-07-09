Ext.define("MB.store.HandOrderSourTypeStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.hand_order_source_type
});