Ext.define('MB.model.OrderCustomDefine', {
	extend: 'Ext.data.Model',
	fields: [
		{ name : 'code' },					// 编码
		{ name : 'name' },					// 名称
		{ name : 'note' },					// 备注
		{ name : 'type'}					// 类型
	]
});