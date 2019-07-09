Ext.define("MB.store.QualityStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.qualityStatus
});