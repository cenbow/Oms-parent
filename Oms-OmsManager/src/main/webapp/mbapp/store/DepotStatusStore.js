/**
 *分仓状态
 ***/
Ext.define("MB.store.DepotStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.question_status
});