Ext.define('MB.model.OrderGoodsEdit', {
	extend: 'Ext.data.Model',
	fields: [
		{ name : 'depotCode' },					// 发货仓库编码
		{ name : 'orderSn' },					// 订单编码
		{ name : 'goodsName' },					// 商品名称
		{ name : 'customCode'},					// 商品11位码
		{ name : 'goodsNumber'},				// 商品数量
		{ name : 'marketPrice'},				// 商品的市场售价
		{ name : 'settlementPrice'},			// 结算价格
		{ name : 'goodsPrice'},					// 商品的本店售价
		{ name : 'discount'},					// 折让金额
		{ name : 'transactionPrice'},			// 商品的成交价
		{ name : 'shareBonus'},					// 分摊红包金额
		{ name : 'shareSurplus'},				// 分摊积分金额
		{ name : 'extensionCode'},				// 商品的扩展属性
		{ name : 'useCard'}						// 打折券
	]
});