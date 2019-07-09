Ext.define("MB.store.BfbRefundSettlementStore", {
	extend: "Ext.data.Store",
	model: "MB.model.BfbRefundSettlementModel",
	autoLoad: true,
	pageSize : 20,// 每页显示条目数量
    proxy: {
    	type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url:  basePath + 'custom/bfbRefundSettlement/getBfbRefundSettlementList.spmvc',
		reader: {
			rootProperty: 'root',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
    }

});