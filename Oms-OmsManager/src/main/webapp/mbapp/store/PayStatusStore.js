/**
 *分仓状态
 ***/
Ext.define("MB.store.PayStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.pay_status
});