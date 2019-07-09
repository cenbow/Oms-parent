/**
 *预售状态
 ***/
Ext.define("MB.store.AdvanceStatusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.pre_group_order
});