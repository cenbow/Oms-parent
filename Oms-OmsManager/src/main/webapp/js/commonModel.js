// 页面constant下拉框列表数据对象模型
Ext.define('MB.ComboModel', {
	extend: 'Ext.data.Model',
	fields: [
		{name: 'v'},
		{name: 'n', type: 'string'}
	]
});

/**
 * Channel model 
 */
Ext.define('MB.Channel', {
	extend: 'Ext.data.Model',
	fields: [
		{name: 'chanelCode',type: 'string'},
		{name: 'channelTitle',type: 'string'}
	]
});

/**
 * Shop model 
 */
Ext.define('MB.Shop', {
	extend: 'Ext.data.Model',
	fields: [
		{name: 'shopCode', type: 'string'},
		{name: 'shopTitle',  type: 'string'}
	]
});
/**
 * WareHouse model
 */
Ext.define('MB.WareHouse', {
	extend: 'Ext.data.Model',
	fields: [
		{name: 'warehouseCode', type: 'string'},
		{name: 'warehouseName',  type: 'string'}
	]
});
/**
 * Area model
 */
Ext.define('MB.Area', {
	extend: 'Ext.data.Model',
	fields: [
		{name: 'regionId'},
		{name: 'regionName',  type: 'string'}
	]
});

/**
 * QuestionType model
 */
Ext.define('MB.QuestionType', {
	extend: 'Ext.data.Model',
	fields: [
		{name: 'code',  type: 'string'},
		{name: 'name',  type: 'string'}
	]
});

/**
 * OrderReturn Query Result model 
 */
Ext.define('MB.OrderReturnQuery', {
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
		{name: 'returnReasonName'}
		
		
		
	]
	//,
	//idProperty: 'id'
});


/**
 * OrderInfo Query Result model 
 */
Ext.define('MB.OrderInfoQuery', {
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
		{name: 'useLevelStr'},
		{ name : 'calculateDiscount'},
		{ name : 'goodsAmount'}, 
		{name: 'financialPrices'}, //财务价格
		{name: 'deliveryTime'},
		{ name : 'totalFee'}, //成交价格
		{ name : 'bonus'},  // 使用红包价格
		{ name : 'shippingTotalFee'},  //配送总费用
		{ name : 'shippingFree'}, //配送费用
		{ name : 'relatingExchangeSn'}, // 关联换货单原订单号
		{ name : 'orderTypeStr'}, // 订单类型说明
		{ name : 'returnSn'}, // 订单关联退单号
		{ name : 'opTotalfee'}, // 支付单总金额
		{ name : 'settlementPrice'} // 财务价格
		
	]
	//,
	//idProperty: 'id'
});

/**
 * OrderQuestion Query Result model 
 */
Ext.define('MB.OrderQuestionQuery', {
	extend: 'Ext.data.Model',
	fields: [
		{ name : 'orderSn' },
		{ name : 'orderForm' },
		{ name : 'channelName' },
		{ name : 'useLevel' },
		{ name: 'transType'},
		{ name: 'userName'},
		{ name: 'reason'},
		{ name: 'processStatus' },
		{name: 'no'},
		{name: 'orderOutSn'},
		{name: 'addTime'},
		{name: 'orderFrom'},
		{name: 'userName'},
		{name: 'totalFee'},
		{name: 'totalPayable'},
		{name: 'customerNote'},
		{name: 'businessNote'},
		{name: 'lockStatus'},
		{name: 'listDataType'},
		{name: 'transTypeStr'},
		{name: 'processStatusStr'},
		{name: 'useLevelStr'},
		{name: 'lockStatusStr'},
		{name: 'operationStr'},
		{name: 'questionTypeStr'},
		{name: 'referer'},
		{ name: 'listDataTypehis'},
		{name: 'orderStatus'},
		{name: 'payStatus'},
		{name: 'shipStatus'},
		{name: 'lockUserName'},
		{name: 'outStockCode'},
		{name: 'logisticsType'},
		{name: 'questionDesc'},
		{name: 'questionReason'},
		{name: 'questionAddTime'}
	]
	//,
	//idProperty: 'id'
});


/**
 * GoodsReturnChange Query Result model 
 */
Ext.define('MB.GoodsReturnChangeQuery', {
	extend: 'Ext.data.Model',
	fields: [
		{ name : 'id' },
		{ name: 'userId'},
		{ name: 'orderSn'},
		{ name: 'skuSn'},
		{ name: 'returnTypeStr'},
		{ name: 'reasonStr'},
		{ name: 'explain' },
		{ name: 'redemptionStr'},
		{ name: 'tagTypeStr' },
		{ name: 'exteriorTypeStr'},
		{ name: 'giftTypeStr'},
		{ name: 'contactName'},
		{ name: 'mobileStr'},
		{ name: 'remark'},
		{ name: 'statusStr'},
		{ name: 'returnSum'},
		{ name: 'create'},
		{ name: 'returnchangeSn'},
		{ name: 'returnSn'},
		{ name: 'returnPaySn'},
		{ name: 'transTypeStr'},
		{ name: 'channelName'},
		{ name: 'totalFee'},
		{ name: 'questionStatus'},
		{ name: 'goodsName'},
		{ name: 'transactionPrice'},
	]
	//,
	//idProperty: 'id'
});

Ext.define('MB.OrderSettleBill', {
	extend: 'Ext.data.Model',
	fields: [
		{ name : 'id' },
		{ name : 'billNo' },
		{ name: 'orderCode'},
		{ name: 'channelCode'},
		{ name: 'orderType'},
		{ name: 'billType'},
		{ name : 'shippingId' },
		{ name: 'returnPay'},
		{ name: 'money' },
		{ name: 'actionUser'},
		{ name: 'resultStatus'},
		{ name: 'resultMsg'},
		{ name: 'addTime'}, 
		{ name: 'clearTime'},

		{ name: 'updateTime'}
		
	]
	//,
	//idProperty: 'id'
});


Ext.define('MB.OrderCustomDefine', {
	extend: 'Ext.data.Model',
	fields: [
		{ name : 'code' },					// 编码
		{ name : 'name' },					// 名称
		{ name : 'note' },					// 备注
		{ name : 'type'}					// 类型
	]
});