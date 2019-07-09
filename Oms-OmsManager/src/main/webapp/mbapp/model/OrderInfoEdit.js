Ext.define('MB.model.OrderInfoEdit', {
	extend: 'Ext.data.Model',
	fields: [
		{ name : 'orderSn' },
		{ name : 'orderStatus' },
		{ name : 'orderGoods' }					// 订单商品列表
	]
});