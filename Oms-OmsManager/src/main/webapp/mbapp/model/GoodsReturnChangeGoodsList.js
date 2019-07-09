Ext.define('MB.model.GoodsReturnChangeGoodsList', {
	extend : 'Ext.data.Model',
	fields : [
		{ name : 'id' },//申请流水号
		{ name : 'returnType' },//类型
		{ name : 'goodsName' },//商品名称
		{ name : 'goodsSn' },//货号
		{ name : 'colorName' },//颜色
		{ name : 'sizeName' },//尺寸
		{ name : 'custumCode' },//企业SKU码
		{ name : 'goodsPrice' },//商品价格
		{ name : 'transactionPrice' },//成交价格
		{ name : 'promotionDesc' },//商品促销
		{ name : 'useCard' },//打折券
		{ name : 'discount' },//折让
		{ name : 'goodsNumber' },//订购数量
		{ name : 'returnSum' },//退换数
		{ name : 'reason' },//退换货原因
		{ name : 'explain' },//说明
		{ name : 'tagType' },//吊牌
		{ name : 'exteriorType' },//外观
		{ name : 'giftType' }//赠品
		]
});