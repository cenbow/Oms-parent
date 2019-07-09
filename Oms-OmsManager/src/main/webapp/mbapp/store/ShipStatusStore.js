/**
 *发货状态
 ***/
Ext.define("MB.store.ShipStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.ship_status
});