Ext.define("MB.store.ChannelShopStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ChannelShop",
	proxy: {
		type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url: basePath + 'custom/common/getChannelShops',
		reader: {
			type:'json'
		}
	}
});