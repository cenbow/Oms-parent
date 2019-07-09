Ext.define("MB.store.IsCodForAddStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.is_or_not
});