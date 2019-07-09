Ext.define("MB.store.OrderCustomDefines", {
	extend : "Ext.data.Store",
	model : "MB.model.OrderCustomDefine",
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		url : basePath + 'custom/common/getOrderCustomDefine',
		reader : {
			type : 'json'
		}
	}
});