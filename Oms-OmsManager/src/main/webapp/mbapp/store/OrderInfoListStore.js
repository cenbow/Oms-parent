var orderInfoListUrl = "";
if("orderInfoSettle" == settleType ){//待订单结算	
	 orderInfoListUrl = basePath+"/custom/orderInfo/orderInfoList.spmvc?type=orderInfoSettle";
} else{
	 orderInfoListUrl = basePath+"/custom/orderInfo/orderInfoList.spmvc";
}

/*if("orderInfoSettle" == settleType ){//待订单结算	
	 orderInfoListUrl = basePath+"/custom/orderInfo/orderInfoListTest.spmvc?type=orderInfoSettle";
} else{
	 orderInfoListUrl = basePath+"/custom/orderInfo/orderInfoListTest.spmvc";
}*/

Ext.define("MB.store.OrderInfoListStore", {
	extend: "Ext.data.Store",
	model: "MB.model.OrderInfoQuery",
	autoLoad: false,
	pageSize : 20,// 每页显示条目数量
    proxy: {

    	type: 'ajax',
    	timeout:3600000,
		actionMethods: {
			read: 'POST'
		},
		url:  orderInfoListUrl,
		reader: {
	
			rootProperty: 'root',
			type: 'json',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
   
    }
});
