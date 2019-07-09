/**
 * OrderReturn Query Result model 
 */
Ext.define('MB.model.OrderReturnQuery', {
	extend: 'Ext.data.Model',
	fields: [
	         
		{ name : 'returnSn' },
		
		{ name: 'relatingOrderSn'},
		{ name: 'channelName'},
		{ name: 'referer'},
		{ name: 'orderOutSn'},
		{ name: 'returnTypeStr'},
		{ name: 'returnPayStr' },
		{ name: 'addTime'},
	//	{ name: 'returnShippingStatusStr' },
		{ name: 'returnGoodsCount'},
		{ name: 'returnTotalFee'},
		{ name: 'returnOrderStatus'},
		{ name: 'payStatus'},
		{ name: 'shipStatus'},
		{ name: 'orderOrderStatus'},
		{ name: 'orderPayStatus'},
		{ name: 'orderShipStatus'},
		{name: 'totalFee'}, //成交价格
		{name: 'goodsAmount'}, //财务价格
		{name: 'bonus'},  //	红包金额 
		{name: 'returnOtherMoney'}, //退其他费用
		{name: 'userName'},
		{name: 'checkinTime'}, //入库时间
		{name: 'clearTime'}, //结算时间
		
		{name: 'returnShipping'}, //退运费,
		{name: 'returnGoodsMoney'},//退商品金额,
		{name: 'consignee'},//收货人
		{name: 'returnInvoiceNo'}, //退货快递单号,
		{name: 'financialPrices'}, //财务价格

		{name: 'orderType'},
		{name: 'warehouseName'},
		{name: 'backToCs'},
		{name: 'returnReason'},
		{name: 'transType'},
		{name: 'masterOrderSn'},
		{name: 'returnReasonName'}

	]
	
});