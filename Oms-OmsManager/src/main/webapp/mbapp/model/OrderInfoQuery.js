/**
 *订单列表查询 
 ***/

Ext.define('MB.model.OrderInfoQuery', {
	extend: 'Ext.data.Model',
	fields: [
		{ name : 'orderSn' },
		{ name : 'orderOutSn' },
		{ name: 'questionStatusStr'},
		{ name: 'addTime'},
		{ name: 'shippingTime'},
		{ name: 'orderCategoryStr'},
		{ name : 'transTypeStr' },
		{ name: 'orderFrom'},
		{ name: 'userName' },
		{ name: 'referer'},
		{ name: 'goodsCount'},
		{ name: 'totalPayable'},
		{ name: 'moneyPaid'}, //已付款金额
		{ name: 'channelName'},
		{ name: 'selectTimeType' },
		{ name : 'startTime'},
		{ name : 'endTime'},
		{ name : 'orderType'},
		{ name : 'questionStatus'},
		{ name : 'timeoutStatus'},
		{ name : 'orderStatus'},
		{ name : 'payStatus'},
		{ name : 'shipStatus'},
		{ name : 'totalFee'},
		{ name : 'surplus'},//余额
		{ name : 'lockStatus'},
		{ name : 'lockUserName'},
		{ name: 'useLevelStr'},
		{ name : 'calculateDiscount'},
		{ name : 'goodsAmount'}, 
		{ name: 'financialPrices'}, //财务价格
		{ name: 'deliveryTime'},
		{ name : 'totalFee'}, //成交价格
		{ name : 'bonus'},  // 使用红包价格
		{ name : 'shippingTotalFee'},  //配送总费用
		{ name : 'shippingFree'}, //配送费用
		{ name : 'relatingExchangeSn'}, // 关联换货单原订单号
		{ name : 'orderTypeStr'}, // 订单类型说明
		{ name : 'returnSn'}, // 订单关联退单号
		{ name : 'opTotalfee'}, // 支付单总金额
		{ name : 'settlementPrice'}, // 财务价格
		{ name : 'masterOrderSn'} ,// 主订单
		{ name : 'relatingOriginalSn'} 

				  
	]
	//,
	//idProperty: 'id'
});
