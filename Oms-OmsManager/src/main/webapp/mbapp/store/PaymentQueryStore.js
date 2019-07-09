Ext.define("MB.store.PaymentQueryStore", {
	extend: "Ext.data.Store",
	model: "MB.model.PaymentModel",
	autoLoad: true,
	pageSize : 20,// 每页显示条目数量
    proxy: {
    	type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url:  basePath + 'custom/paymentManagement/getPaymentQueryList.spmvc',
		reader: {
			rootProperty: 'root',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
    }

});