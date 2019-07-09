/**
 *退单状态
 ***/
Ext.define("MB.store.ReturnOrderStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.return_order_status
});