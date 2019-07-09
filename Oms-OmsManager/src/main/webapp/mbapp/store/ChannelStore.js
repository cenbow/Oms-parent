Ext.define("MB.store.ChannelStore", {
	extend: "Ext.data.Store",
	model: Ext.create('Ext.data.Model',{
		fields: [
		            {name: 'shortText'},
		            {name: 'channelCode'}
		        ]
	}),
	autoLoad : true,
	proxy: {
		type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url: basePath + 'custom/common/loadOrderShopData?dataType=1',
		reader: {
			type:'json'
		}
	}
});