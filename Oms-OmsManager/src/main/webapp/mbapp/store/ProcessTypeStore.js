//0无操作，1退货，2修补，3销毁，4换货
Ext.define("MB.store.ProcessTypeStore", {
	extend : "Ext.data.Store",
	model : "MB.model.CommonStatusModel",
	data:[['0',"无操作"],['1','退货'],['2','换货']]
});