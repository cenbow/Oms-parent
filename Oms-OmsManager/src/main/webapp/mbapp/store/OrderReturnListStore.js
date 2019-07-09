var queryUrl = "";
if("orderReturnSettleList" == settleType ){//待订单结算	
	 queryUrl = basePath+"/custom/orderReturn/orderReturnList.spmvc?type=settle";
} else{//退单列表
	 queryUrl = basePath+"/custom/orderReturn/orderReturnList.spmvc";
}

Ext.define("MB.store.OrderReturnListStore", {
	extend: "Ext.data.Store",
	model: "MB.model.OrderReturnQuery",
	autoLoad: false,
	pageSize : 20,// 每页显示条目数量
    proxy: {

    	type: 'ajax',
    	timeout:3600000,
		actionMethods: {
			read: 'POST'
		},
		url: queryUrl,
		reader: {
	
			rootProperty: 'root',
			type: 'json',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
   
    }

});

