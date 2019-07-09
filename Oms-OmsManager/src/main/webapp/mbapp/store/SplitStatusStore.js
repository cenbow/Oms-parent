/**
 *分仓状态
 ***/
Ext.define("MB.store.SplitStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.split_status
});