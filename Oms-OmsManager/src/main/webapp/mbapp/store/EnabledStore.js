Ext.define("MB.store.EnabledStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: [{v:'',n:'请选择'},
		{v:'0',n:'禁用'},
	    {v:'1',n:'启用'}]
});