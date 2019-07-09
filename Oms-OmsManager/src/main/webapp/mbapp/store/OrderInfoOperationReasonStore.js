Ext.define('MB.store.OrderInfoOperationReasonStore', {
    extend: 'Ext.data.Store',
    requires: ['Ext.data.reader.Json'],
    model: 'MB.model.OrderCustomDefine',
    proxy: {
    	type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		url : basePath + 'custom/common/getOrderCustomDefine?type=8',
		reader : {
			type : 'json'
		}
    }
});
