Ext.define("MB.store.IsSyncStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.is_syn_state
});