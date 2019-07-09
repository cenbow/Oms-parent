Ext.define("MB.store.ReturnReasonForListStore", {
	extend : "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.return_reason
});