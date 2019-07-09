Ext.define("MB.store.OrderAmountAdjustmentStore", {
	extend: "Ext.data.Store",
	model: "MB.model.OrderAmountAdjustmentModel",
	autoLoad: true,
	pageSize : 20,// 每页显示条目数量
    proxy: {
    	type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url:  basePath + 'custom/orderAmountAdjustment/getOrderAmountAdjustmentList.spmvc',
		reader: {
			rootProperty: 'root',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
    }

});