Ext.define("MB.store.GoodsReturnChangeStatusStore", {
	extend : "Ext.data.Store",
	model : "MB.model.CommonStatusModel",
	data:[['0','已取消'],['1','待沟通'],['2','已完成'],['3','待处理']]
});