/**
 * 缺货列表 
 * 
****/
Ext.define("MB.store.ShortageQuestionStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ShortageQuestionModel",
	autoLoad: true,
	pageSize : 20,// 每页显示条目数量
    proxy: {
    	type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url: basePath+"custom/orderQuestion/getShortageQuestionList.spmvc",
		reader: {
			rootProperty: 'root',
			type: 'json',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
   
    }

});