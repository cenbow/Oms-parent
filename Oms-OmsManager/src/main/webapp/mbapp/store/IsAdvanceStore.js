/**
 *订单来源媒体
 ***/
Ext.define("MB.store.IsAdvanceStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.pre_group_order
});