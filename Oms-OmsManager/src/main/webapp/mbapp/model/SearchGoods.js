Ext.define('MB.model.SearchGoods', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'goodsSn',
		type : 'string'
	} , {
		name : 'goodsTitle',
		type : 'string'
	} , {
		name : 'channelCode',
		type : 'string'
	} , {
		name : 'brandCode',
		type : 'string'
	} , {
		name : 'channelPrice'
	} , {
		name : 'platformPrice'
	} , {
		name : 'marketPrice'
	}]
});