Ext.define("MB.store.IsVerifyTelStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.is_or_not
});