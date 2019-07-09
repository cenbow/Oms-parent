/**
 *退款类型 
 ***/
Ext.define("MB.store.ReturnSettlementTypeStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.return_settlement_type
});