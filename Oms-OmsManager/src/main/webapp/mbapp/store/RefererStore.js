/**
 *订单来源媒体
 ***/
Ext.define("MB.store.RefererStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.referer_type
});