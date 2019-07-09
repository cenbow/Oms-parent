//处理方式
Ext.define("MB.store.ProcessTypeForListStore", {
	extend : "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.process_type
});