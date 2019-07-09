/**
 * 
 * 退款方式
 * **/
Ext.define("MB.store.ReturnPayStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.return_pay
});