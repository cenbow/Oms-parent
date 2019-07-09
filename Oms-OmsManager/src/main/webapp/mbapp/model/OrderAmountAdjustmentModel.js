/**
 * 订单金额调整批次模型
 */
Ext.define('MB.model.OrderAmountAdjustmentModel', {
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