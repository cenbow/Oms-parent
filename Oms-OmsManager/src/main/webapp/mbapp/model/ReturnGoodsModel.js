
Ext.define('MB.model.ReturnGoodsModel', {
	extend : 'Ext.data.Model',
	fields : [
		{ name : 'goodsName' },//商品名称
		{ name : 'extensionCode' },//商品属性
		{ name : 'extensionId' },//商品扩展属性id
		{ name : 'goodsSn' },//货号
		{name:'goodsColorName'},//颜色
		{name:'goodsSizeName'},//尺寸
		{ name : 'customCode' },//企业SKU码
		{ name : 'marketPrice' },//商品价格
		{ name : 'goodsPrice' },//成交价格
		{ name : 'settlementPrice' },//财务价格
		{ name : 'shareBonus' },//分摊金额
		{ name : 'goodsBuyNumber' },//购买数量
		{ name : 'discount' },//折扣
		{ name : 'shareSettle' },//财务分摊金额
		{ name : 'osDepotCode' },//所属发货仓
		{ name : 'shopReturnCount' },//门店退货量
		{ name : 'havedReturnCount' },//已退货量
		{ name : 'canReturnCount' },//可退货量
		{ name : 'returnReason' },//退换货原因
		{ name : 'priceDifferNum' },//退差价数量
		{ name : 'priceDifference' },//退差价单价
		{ name : 'returnReason' },
		{ name : 'integralMoney' }
		]
		
});