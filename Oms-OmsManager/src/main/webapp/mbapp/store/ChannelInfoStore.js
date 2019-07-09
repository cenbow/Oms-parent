Ext.define("MB.store.ChannelInfoStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ChannelInfo",
	proxy: {
		type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url: basePath + 'custom/common/getChannelInfos',
		reader: {
			type:'json'
		}
	},
	autoLoad:true
});