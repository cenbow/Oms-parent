
/**
 *财务状态 
 ****/
Ext.define("MB.store.ReturnPayStatusByListStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.pay_status_2
});