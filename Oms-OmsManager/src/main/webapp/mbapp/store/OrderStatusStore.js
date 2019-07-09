/**
 *订单状态
 ***/
Ext.define("MB.store.OrderStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.order_status
});