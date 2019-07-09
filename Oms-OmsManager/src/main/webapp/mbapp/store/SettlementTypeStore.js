
Ext.define("MB.store.SettlementTypeStore", {
	extend : "Ext.data.Store",
	model : "MB.model.CommonStatusModel",
	data:[['0','请选择'],['1','预付款'],['2','保证金']]
});