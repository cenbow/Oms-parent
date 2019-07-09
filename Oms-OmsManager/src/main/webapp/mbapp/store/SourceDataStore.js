/**
 *资源种类
 ***/
Ext.define("MB.store.SourceDataStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.source
});