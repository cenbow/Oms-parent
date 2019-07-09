/**
 * 支付方式管理管理：视图面板
 */
Ext.define("MB.view.paymentManagement.PaymentManagementViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.paymentManagementViewport',
	requires: [
	           'MB.view.paymentManagement.PaymentQueryPanlView',
	           'MB.view.paymentManagement.PaymentQueryGridView',
	           'MB.view.shippingManagement.EnabledCombo',
	           'MB.store.EnabledStore',
	           'MB.model.ComboModel',
	           'MB.view.paymentManagement.IsOnlineCombo',
	           'MB.store.IsOnlineStore',
	           'MB.store.PaymentQueryStore',
	           'MB.model.PaymentModel',
	           'MB.view.paymentManagement.PaymentAdd',
	           'MB.view.paymentManagement.IsMobileForAddCombo',
	           'MB.store.IsMobileForAddStore',
	           'MB.view.paymentManagement.IsOnlineForAddCombo',
	           'MB.store.IsOnlineForAddStore',
	           'MB.view.paymentManagement.IsCodForAddCombo',
	           'MB.store.IsCodForAddStore',
	           'MB.view.shippingManagement.EnabledForAddCombo',
	           'MB.store.EnabledForAddStore',
	           'MB.view.paymentManagement.PaymentEdit'],
	items : [ {
		title : '查询条件',
		xtype: 'paymentQueryPanlView',
		region : "north",
		height : document.body.clientHeight * 0.3
	},{
		title : '查询列表',
		xtype: 'paymentQueryGridView',
		region : "south",
		height: document.body.clientHeight * 0.7
	}]
});