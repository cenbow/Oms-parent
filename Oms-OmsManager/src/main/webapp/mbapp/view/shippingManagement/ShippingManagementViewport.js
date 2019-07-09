/**
 * 承运商管理：视图面板
 */
Ext.define("MB.view.shippingManagement.ShippingManagementViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.shippingManagementViewport',
	requires: [
	           'MB.view.shippingManagement.ShippingQueryPanlView',
	           'MB.view.shippingManagement.ShippingQueryGridView',
	           'MB.view.shippingManagement.SupportCodCombo',
	           'MB.store.SupportCodStore',
	           'MB.model.ComboModel',
	           'MB.view.shippingManagement.EnabledCombo',
	           'MB.store.EnabledStore',
	           'MB.store.ShippingQueryStore',
	           'MB.model.ShippingQueryModel',
	           'MB.view.shippingManagement.ShippingAdd',
	           'MB.view.shippingManagement.SupportCodForAddCombo',
	           'MB.store.SupportCodForAddStore',
	           'MB.view.shippingManagement.IsReceivePrintForAddCombo',
	           'MB.view.shippingManagement.DefalutDeliveryForAddCombo',
	           'MB.store.DefalutDeliveryForAddStore',
	           'MB.store.IsReceivePrintForAddStore',
	           'MB.view.shippingManagement.ShippingEdit',
	           'MB.view.shippingManagement.EnabledForAddCombo',
	           'MB.store.EnabledForAddStore'],
	items : [ {
		title : '查询条件',
		xtype: 'shippingQueryPanlView',
		region : "north",
		height : document.body.clientHeight * 0.3
	},{
		title : '查询列表',
		xtype: 'shippingQueryGridView',
		region : "south",
		height: document.body.clientHeight * 0.7
	}]
});