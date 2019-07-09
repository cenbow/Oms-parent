/**
 * 邦付宝退款结算批次清单模型
 */
Ext.define('MB.model.BfbRefundSettlementModel', {
	extend: 'Ext.data.Model',
	fields: [
		{ name: 'billNo', type: 'string' },
		{ name: 'channelCode', type: 'string' },
		{ name: 'billType', type: 'string' },
		{ name: 'actionUser', type: 'string' },
		{ name: 'note', type: 'string' },
		{ name: 'isTiming', type: 'string' },
		{ name: 'execTime', type: 'string' },
		{ name: 'isSync', type: 'string' },
		{ name: 'addTime', type: 'string' },
		{ name: 'updateTime', type: 'string' }
	]
});