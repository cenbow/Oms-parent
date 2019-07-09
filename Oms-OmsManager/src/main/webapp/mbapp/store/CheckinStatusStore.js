Ext.define("MB.store.CheckinStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.checkin_status
});