/**
 * //交易类型
 ****/
Ext.define("MB.store.TransTypeStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.trans_type
});