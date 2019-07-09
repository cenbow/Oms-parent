/**
 * 查询条件：国家
 */
Ext.define("MB.store.CountryRegionStore", {
	extend: "Ext.data.Store",
	model: "MB.model.QueryRegionModel",
	proxy: {
		type: 'ajax',
		url:basePath+'custom/common/getRegionQueryCondition.spmvc?parentId=0',//, 这里是参数可以顺便写,这个数据源是在第一个下拉框select的时候load的
	},
	listeners:{
		load : function(store, records, options ){
			var data ={ "regionId": "", "regionName": "请选择"};      
            var rs = [new Ext.data.Record(data)];      
            store.insert(0,rs);
		}
	}
});
