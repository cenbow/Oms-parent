/**
 * 邦付宝退款批量结算：视图面板
 */
Ext.define("MB.view.bfbRefundSettlement.BfbRefundSettlementViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.bfbRefundSettlementViewport',
	requires: [
	           'MB.view.bfbRefundSettlement.BfbRefundSettlementPanlView',
	           'MB.view.bfbRefundSettlement.BfbRefundSettlementGridView',
	           'MB.view.bfbRefundSettlement.IsSyncCombo',
	           'MB.store.IsSyncStore',
	           'MB.model.ComboModel',
	           'MB.store.BfbRefundSettlementStore',
	           'MB.model.BfbRefundSettlementModel',
	           'MB.view.bfbRefundSettlement.BfbRefSetImport'],
	items : [ {
		title : '邦付宝退款结算',
		xtype: 'bfbRefundSettlementPanlView',
		region : "north",
		height : document.body.clientHeight * 0.3
	},{
		xtype: 'bfbRefundSettlementGridView',
		region : "south",
		height: document.body.clientHeight * 0.7
	}]
});