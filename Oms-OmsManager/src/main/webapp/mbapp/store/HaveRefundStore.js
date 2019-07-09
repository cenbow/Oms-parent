//1需要退款  0无须退款
Ext.define("MB.store.HaveRefundStore", {
	extend : "Ext.data.Store",
	model : "MB.model.CommonStatusModel",
	data:[['1',"需要退款"],['0','无须退款']]
});