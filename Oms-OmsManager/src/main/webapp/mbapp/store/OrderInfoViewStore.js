/**
 *显示类型（订单）
 *
 *
 ***/
Ext.define("MB.store.OrderInfoViewStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.order_view
});