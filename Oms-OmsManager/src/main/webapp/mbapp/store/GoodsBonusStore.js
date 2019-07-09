Ext.define("MB.store.GoodsBonusStore", {
	extend: "Ext.data.Store",
	model: "MB.model.OrderGoodsEdit"
//	proxy: {
//		type: 'ajax',
//		actionMethods: {
//			read: 'POST'
//		},
//		url: 'http://localhost:8088/OmsManager/custom/demo/getOrderGoods',
//		reader: {
//			rootProperty: 'root',
//			totalProperty: 'totalProperty'
//		},
//		simpleSortMode: true
//	},
//	autoLoad : {
//		params : {
//			orderSn : orderSn
//		}
//	}
});