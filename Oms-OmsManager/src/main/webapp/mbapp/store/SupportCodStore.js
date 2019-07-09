Ext.define("MB.store.SupportCodStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: [{v:'',n:'请选择'},
		{v:'0',n:'否'},
	    {v:'1',n:'是'}]
});