
Ext.define("MB.store.ReturnTypeStore", {
	extend : "Ext.data.Store",
	model : "MB.model.CommonStatusModel",
	data:[['1','退货单'],['2','拒收入库单'],['3','普通退款单'],['4','额外退款单']]
});