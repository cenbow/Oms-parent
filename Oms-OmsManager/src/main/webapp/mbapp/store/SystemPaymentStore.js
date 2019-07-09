Ext.define("MB.store.SystemPaymentStore", {
	extend : "Ext.data.Store",
	model : "MB.model.SystemPaymentModel",
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		timeout:90000,
		url : basePath + 'custom/common/selectSystemPaymentList',
		reader : {
			type : 'json'
		}
	},
	autoLoad : true
});