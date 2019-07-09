Ext.define("MB.store.BackToCsDataStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.back_to_cs
});