/**
 * 承运商列表模型
 */
Ext.define('MB.model.ShippingQueryModel', {
	extend: 'Ext.data.Model',
	fields: [
		{ name: 'shippingId', type: 'string' },
		{ name: 'shippingCode', type: 'string' },
		{ name: 'shippingName', type: 'string' },
		{ name: 'shippingdesc', type: 'string' },
		{ name: 'insure', type: 'string' },
		{ name: 'supportCod', type: 'string' },
		{ name: 'enabled', type: 'string' },
		{ name: 'shippingPrint', type: 'string' },
		{ name: 'shippingPrint2', type: 'string' },
		{ name: 'isReceivePrint', type: 'string' },
		{ name: 'modelImg', type: 'string' },
		{ name: 'defalutDelivery', type: 'string' },
		{ name: 'isCommonUse',type:'string'}
	]
});