/**
 * 主单和子单
 ***/
Ext.define("MB.store.MainChildStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.MainChild_type
});