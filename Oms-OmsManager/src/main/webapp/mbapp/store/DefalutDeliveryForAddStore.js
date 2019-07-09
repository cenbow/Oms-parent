Ext.define("MB.store.DefalutDeliveryForAddStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.is_or_not
});