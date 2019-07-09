/**
 *显示类型
 ***/
Ext.define("MB.store.OrderViewStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.order_return_view
});