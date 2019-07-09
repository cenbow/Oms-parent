
Ext.define('MB.model.GoodDetailModel', {
	extend : 'Ext.data.Model',
	fields : [
		{ name : 'goodsName' },//商品名称
		{ name : 'extensionCode' },//商品属性
		{ name : 'goodsSn'},//货号
		{ name : 'goodsColorName'},//颜色
		{ name : 'goodsSizeName'},//尺寸
		{ name : 'barcode'},//产品条形码
		{ name : 'customCode'},//企业SKU码
		{ name : 'goodsPrice'},//商品价格
		{ name : 'goodsPrice'},//会员价格
		{ name : 'transactionPrice'},//成交价格
		{ name : 'settlementPrice'},//财务价格
		{ name : 'shareBonus'},//分摊金额
		{ name : 'promotionDesc'},//商品促销
		{ name : 'cardMoney'},//打折卷
		{ name : 'discount'},//折让金额
		{ name : 'goodsNumber'},//数量
		{ name : 'allStock'},//可用数量
		{ name : 'subTotal'},//小计
		{ name : 'depotCode'},//发货仓
		{ name : 'invoiceNo'},//快递单号
		{ name : 'shippingName'},//配送方式
		{ name : 'shippingStatus'},//配送状态
		{ name : 'orderSn'}//订单号
		]
});