/**
 *一级订单来源 
 ***/
Ext.define("MB.store.ChannelTypeStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.channel_type
});