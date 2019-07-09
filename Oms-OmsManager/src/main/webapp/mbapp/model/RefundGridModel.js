
Ext.define('MB.model.RefundGridModel', {
	extend : 'Ext.data.Model',
	fields : [

		{ name : 'relatingReturnSn' },//关联退单号
		{ name : 'relatingOrderSn' },//关联订单号
		{ name : 'orderOutSn'},//关联外部交易号
		{ name : 'returnFee'},//退款金额
		{ name : 'channelName'},//订单来源
		{ name : 'referer'},//referer
		{ name : 'returnType'},//退单类型
		{ name : 'returnOrderStatus'},//退单状态
		{ name : 'returnPayStatus'},//财务状态
		{ name : 'returnPay'},//退款方式
		{ name : 'returnPaySn'}//退款单编号(order_refund主键)
		]
});