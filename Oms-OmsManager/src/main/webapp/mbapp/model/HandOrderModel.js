Ext.define('MB.model.HandOrderModel', {
	extend: 'Ext.data.Model',
	fields: [
		{ name: 'batchNo', type: 'string' },
		{ name: 'channelCode', type: 'string' },
		{ name: 'processStatus', type: 'string' },
		{ name: 'createOrderStatus', type: 'string' },
		{ name: 'createUser', type: 'string' },
		{ name: 'processMessage', type: 'string' },
		{ name: 'createTime', type: 'string' },
		{ name: 'updateTime', type: 'string' }
	]
});