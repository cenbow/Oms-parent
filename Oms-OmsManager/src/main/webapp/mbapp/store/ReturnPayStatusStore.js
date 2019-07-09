Ext.define("MB.store.ReturnPayStatusStore", {
	extend : "Ext.data.Store",
	model : "MB.model.CommonStatusModel",
	data:[['1','已结算'],['2','待结算']]
});