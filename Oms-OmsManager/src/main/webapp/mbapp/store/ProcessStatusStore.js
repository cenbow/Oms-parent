Ext.define("MB.store.ProcessStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.process_status
});