
/**
 *问题单列表 
 ***/
Ext.define('MB.model.OrderQuestionQuery', {
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
		{name: 'masterOrderSn'},
		{name: 'questionReason'},
		{name: 'questionAddTime'}
	]
	//,
	//idProperty: 'id'
});