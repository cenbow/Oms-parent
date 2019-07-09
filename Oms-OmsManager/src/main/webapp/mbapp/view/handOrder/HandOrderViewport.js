Ext.define("MB.view.handOrder.HandOrderViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.handOrderViewport',
	requires: [
	           'MB.view.handOrder.HandOrderPanlView',
	           'MB.view.handOrder.HandOrderGridView',
	           'MB.model.HandOrderModel',
	           'MB.store.HandOrderStore',
	           'MB.model.ComboModel',
	           'MB.store.IsSyncStore',
	           'MB.view.bfbRefundSettlement.IsSyncCombo',
	           'MB.view.handOrder.OnlineChannelShopCombo',
	           'MB.store.OnlineChannelShopStore',
	           'MB.model.OnlineChannelShopModel',
	           'MB.view.handOrder.HandOrder',
	           'MB.view.handOrder.HandOrderListWin',
	           'MB.view.handOrder.HandOrderAddWin',
	           'MB.view.handOrder.HandOrderEditWin'],
	items : [ {
		title : '赠品打单管理',
		xtype: 'handOrderPanlView',
		region : "north",
		height : document.body.clientHeight * 0.3
	},{
		xtype: 'handOrderGridView',
		region : "south",
		height: document.body.clientHeight * 0.7
	}]
});