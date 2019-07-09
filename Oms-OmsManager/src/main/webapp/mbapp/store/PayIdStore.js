/**
 *订单来源媒体
 ***/
Ext.define("MB.store.PayIdStore", {
	extend: "Ext.data.Store",
	model: "MB.model.ComboModel",
	data: SELECT.pay_id
});