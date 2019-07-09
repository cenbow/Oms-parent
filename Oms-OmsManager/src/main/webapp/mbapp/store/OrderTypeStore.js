/**
 *订单种类 
 ***/
Ext.define("MB.store.OrderTypeStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.order_type
});