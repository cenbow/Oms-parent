Ext.define('MB.store.BatchDecodeListStore', {
    extend: 'Ext.data.Store',
    model: "Ext.data.Model",
	autoLoad: true,
	pageSize : 20,// 每页显示条目数量
    proxy: {
    	type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url:  basePath + 'custom/batchDecode/getBatchDecodeList.spmvc',
		reader: {
			rootProperty: 'root',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
    }
});