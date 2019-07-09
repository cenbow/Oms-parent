Ext.define("MB.store.OrderActions", {
	extend: "Ext.data.Store",
	model: "MB.model.OrderAction",
	proxy: {
		type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url: basePath + 'custom/demo/getOrderActions',
		reader: {
			rootProperty: 'root',
			totalProperty: 'totalProperty'
		},
		params : {
			"orderSn" : orderSn,
			"histroy" : isHistory
		},
		simpleSortMode: true
	},
	autoLoad : false
});