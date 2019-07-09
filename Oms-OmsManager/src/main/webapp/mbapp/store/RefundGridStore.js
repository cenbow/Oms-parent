Ext.define("MB.store.RefundGridStore", {
	extend: "Ext.data.Store",
	model: "MB.model.RefundGridModel",
	pageSize:15,
	proxy : {
		type : 'ajax',
		timeout:90000,
		actionMethods : {
			read : 'POST'
		},
		url : basePath + '/custom/orderReturn/orderRefundList',
		reader : {
			rootProperty: 'root',
			totalProperty: 'totalProperty'
		}
	}
});