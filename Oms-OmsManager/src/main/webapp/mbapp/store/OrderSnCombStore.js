Ext.define("MB.store.OrderSnCombStore", {
	extend: "Ext.data.Store",
	model: 'MB.model.ComboModel',
	autoLoad: true,
	proxy: {
		type: 'ajax',
		actionMethods: {
			read: 'POST'
		},
		url: basePath + 'custom/orderGoodsEdit/getOrderSnCombStore?masterOrderSn='+masterOrderSn,
		reader: {
			type:'json'
		}
	},
	listeners:{
		load : function(store, records, options ){
			var data ={ "v": "", "n": "全部"};      
            var rs = [new Ext.data.Record(data)];      
            store.insert(0,rs);
		}
	}
});