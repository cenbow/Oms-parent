Ext.define("MB.store.OnlineChannelShopStore", {
	extend: "Ext.data.Store",
	model: "MB.model.OnlineChannelShopModel",
	proxy : {
		type : 'ajax',
		actionMethods : {
			read : 'POST'
		},
		url : basePath + 'custom/handOrder/getOnlineChannel',
		reader : {
			type : 'json'
		}
	},
	listeners:{
		load : function(store, records, options ){
			var data ={ "channelCode": "", "channelName": "请选择"};      
            var rs = [new Ext.data.Record(data)];      
            store.insert(0,rs);
		}
	}
});