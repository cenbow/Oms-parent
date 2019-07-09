/**
 * 
 * 退单类型，用于列表
 * **/
Ext.define("MB.store.ReturnTypeForListStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.return_type
});