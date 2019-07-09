Ext.define("MB.store.OrderGoodsStore", {
	extend: "Ext.data.Store",
	model: "MB.model.OrderGoodsEdit",
//	storeId:'employeeStore',
//	fields:['name', 'seniority', 'department'],
	groupField: 'depotCode'
//	data: {'employees':[
//        { "name": "Michael Scott",  "seniority": 7, "department": "Management" },
//        { "name": "Dwight Schrute", "seniority": 2, "department": "Sales" },
//        { "name": "Jim Halpert",    "seniority": 3, "department": "Sales" },
//        { "name": "Kevin Malone",   "seniority": 4, "department": "Accounting" },
//        { "name": "Angela Martin",  "seniority": 5, "department": "Accounting" }
//    ]},
//    proxy: {
//        type: 'memory',
//        reader: {
//            type: 'json',
//            rootProperty: 'employees'
//        }
//    }
		
		
		

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