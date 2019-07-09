Ext.define("MB.store.SystemShippingStore", {
	extend : "Ext.data.Store",
	model : "MB.model.SystemShippingModel",
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		timeout:90000,
		url : basePath + 'custom/common/selectSystemShippingList',
		reader : {
			type : 'json'
		}
	},
	autoLoad : true
});