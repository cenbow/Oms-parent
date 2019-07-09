
Ext.define('MB.model.ReturnActionModel', {
	extend : 'Ext.data.Model',
	fields : [
		{ name : 'actionUser' }, 
		{ name : 'logTime',convert:function(value){if(value){return Ext.Date.format(new Date(value),"Y-m-d H:i:s");}} }, 
		{ name : 'returnOrderStatus' }, 
		{ name : 'returnPayStatus' }, 
		{name:'returnShipStatus'}, 
		{name:'actionNote'}
		]
});