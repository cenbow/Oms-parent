Ext.define("MB.store.SystemResourceStore", {
	extend: "Ext.data.Store",
	model: "MB.model.SystemResourceModel",
	autoLoad: true,
	pageSize : 20,// 每页显示条目数量
    proxy: {

    	type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url:  basePath + 'custom/systemResource/getsystemOmsResourceList.spmvc',
		reader: {
	
			rootProperty: 'root',
			type: 'json',
		
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
         
    }

});





