Ext.define("MB.store.CreateOrderStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.create_order_status
});