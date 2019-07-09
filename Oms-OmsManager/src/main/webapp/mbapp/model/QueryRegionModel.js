/**
 * 查询条件区域列表模型
 */
Ext.define('MB.model.QueryRegionModel', {
	extend: 'Ext.data.Model',
	fields: [
		{ name: 'regionId', type: 'string' },
		{ name: 'regionName', type: 'string' },
	]
});