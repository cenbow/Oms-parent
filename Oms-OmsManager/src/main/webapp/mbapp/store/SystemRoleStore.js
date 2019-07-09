Ext.define("MB.store.SystemRoleStore", {
	extend: "Ext.data.Store",
	model: "MB.model.SystemRoleModel",
	autoLoad: true,
	pageSize : 20,// 每页显示条目数量
    proxy: {
    	type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url:  basePath + 'custom/systemResource/getSystemOmsRoleList.spmvc',
		reader: {
	
			rootProperty: 'root',
			type: 'json',
		
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
	   
    }

});





