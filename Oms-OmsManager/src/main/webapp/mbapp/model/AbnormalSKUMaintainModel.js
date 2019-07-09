Ext.define('MB.model.AbnormalSKUMaintainModel', {
	extend: 'Ext.data.Model',
	fields: [
		{ name: 'orderOutSn', type: 'string' },
		{ name: 'skuSn', type: 'string' },
		{ name: 'outSkuSn', type: 'string' },
		{ name: 'outSkuName', type: 'string' },
		{ name: 'channelType', type: 'string' },
		{ name: 'goodsNum', type: 'int' }
	]
});