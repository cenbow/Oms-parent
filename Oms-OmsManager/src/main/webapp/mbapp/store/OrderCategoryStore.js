Ext.define("MB.store.OrderCategoryStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.order_category
});