/**
 * 区域列表模型
 */
Ext.define('MB.model.RegionModel', {
	extend: 'Ext.data.Model',
	fields: [
		{ name: 'regionId', type: 'string' },
		{ name: 'parentId', type: 'string' },
		{ name: 'regionName', type: 'string' },
		{ name: 'regionType', type: 'string' },
		{ name: 'zipCode', type: 'string' },
		{ name: 'shippingFee', type: 'string' },
		{ name: 'emsFee', type: 'string' },
		{ name: 'codFee', type: 'string' },
		{ name: 'isCod', type: 'string' },
		{ name: 'codPos', type: 'string' },
		{ name: 'isCac', type: 'string' },
		{ name: 'isVerifyTel', type: 'string' }
	]
});