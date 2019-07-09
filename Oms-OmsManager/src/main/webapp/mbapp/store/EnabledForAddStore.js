Ext.define("MB.store.EnabledForAddStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: [{v:'0',n:'禁用'},
	       {v:'1',n:'启用'}]
});