Ext.define("MB.store.HaveRefundForListStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.haveRefund
});