Ext.define("MB.store.IsOnlineStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.is_or_not_for_query
});