/**
 *订单来源媒体
 ***/
Ext.define("MB.store.IsGroupStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.group_order
});