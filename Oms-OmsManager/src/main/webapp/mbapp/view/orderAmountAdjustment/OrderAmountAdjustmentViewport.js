/**
 * 订单金额调整
 */
Ext.define("MB.view.orderAmountAdjustment.OrderAmountAdjustmentViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.orderAmountAdjustmentViewport',
	requires: [
	           'MB.view.orderAmountAdjustment.OrderAmountAdjustmentPanlView',
	           'MB.view.orderAmountAdjustment.OrderAmountAdjustmentGridView',
	           'MB.view.bfbRefundSettlement.IsSyncCombo',
	           'MB.store.IsSyncStore',
	           'MB.model.ComboModel',
	           'MB.store.OrderAmountAdjustmentStore',
	           'MB.model.OrderAmountAdjustmentModel'/*,
	           'MB.view.orderAmountAdjustment.OrderAmountAdjustmentImport'*/],
	items : [ {
		title : '订单金额调整',
		xtype: 'orderAmountAdjustmentPanlView',
		region : "north",
		height : document.body.clientHeight * 0.3
	},{
		xtype: 'orderAmountAdjustmentGridView',
		region : "south",
		height: document.body.clientHeight * 0.7
	}]
});