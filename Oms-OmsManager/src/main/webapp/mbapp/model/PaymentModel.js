/**
 * 支付方式列表模型
 */
Ext.define('MB.model.PaymentModel', {
	extend: 'Ext.data.Model',
	fields: [
		{ name: 'payId', type: 'string' },
		{ name: 'payCode', type: 'string' },
		{ name: 'payName', type: 'string' },
		{ name: 'payFee', type: 'string' },
		{ name: 'payDesc', type: 'string' },
		{ name: 'payOrder', type: 'string' },
		{ name: 'payConfig', type: 'string' },
		{ name: 'enabled', type: 'string' },
		{ name: 'isCod', type: 'string' },
		{ name: 'isOnline', type: 'string' },
		{ name: 'isMobile', type: 'string' }
	]
});