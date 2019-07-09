
//var queryUrl = basePath+"custom/orderQuestion/orderQuestionList.spmvc?processStatus=0";

//var queryUrl = basePath+"custom/orderQuestion/orderQuestionList.spmvc";

//var queryUrl = basePath+"custom/orderQuestion/orderQuestionListTest.spmvc";

Ext.define("MB.store.OrderQuestionListStore", {
	extend: "Ext.data.Store",
	model: "MB.model.OrderQuestionQuery",
	autoLoad: false,
	pageSize : 20,// 每页显示条目数量
    proxy: {

    	type: 'ajax',
    	timeout:3600000,
		actionMethods: {
			read: 'POST'
		},
		url: basePath+"custom/orderQuestion/orderQuestionList.spmvc",
		reader: {
	
			rootProperty: 'root',
			type: 'json',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
   
    }

});
