Ext.define("MB.store.AbnormalSKUMaintainStore", {
	extend: "Ext.data.Store",
	model: "MB.model.AbnormalSKUMaintainModel",
	autoLoad: false,
	pageSize : 20,// 每页显示条目数量
    proxy: {
    	type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url:  basePath + 'custom/abnormalSKUMaintain/getAbnormalSKUList.spmvc',
		reader: {
			rootProperty: 'root',
			totalProperty: 'totalProperty'
		},
		simpleSortMode: true
    }

});