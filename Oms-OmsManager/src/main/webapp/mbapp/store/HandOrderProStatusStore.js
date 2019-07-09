Ext.define("MB.store.HandOrderProStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.hand_order_process_status
});