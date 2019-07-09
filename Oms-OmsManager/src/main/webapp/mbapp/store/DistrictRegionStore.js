/**
 * 查询条件：区县
 */
Ext.define("MB.store.DistrictRegionStore", {
	extend: "Ext.data.Store",
	model: "MB.model.QueryRegionModel",
	autoLoad: false,
	proxy: {
		type: 'ajax',
		url:basePath+'custom/common/getRegionQueryCondition.spmvc?parentId=x',//, 这里是参数可以顺便写,这个数据源是在第一个下拉框select的时候load的
	},
	listeners:{
		load : function(store, records, options ){
			var data ={ "regionId": "", "regionName": "请选择"};      
            var rs = [new Ext.data.Record(data)];      
            store.insert(0,rs);
		}
	}
});