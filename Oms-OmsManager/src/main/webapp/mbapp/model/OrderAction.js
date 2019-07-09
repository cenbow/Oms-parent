Ext.define('MB.model.OrderAction', {
	extend: 'Ext.data.Model',
	fields: [
		{ name: 'actionUser', type: 'string' },
		{
			name: 'logTime',
			type: 'date',
			convert:function(value){if(value){return Ext.Date.format(new Date(value),"Y-m-d H:i:s");}} 
		},
		{ name: 'orderStatus', type: 'string' },
		{ name: 'payStatus', type: 'string' },
		{ name: 'shippingStatus', type: 'string' },
		{ name: 'actionNote', type: 'string' }
	]
});