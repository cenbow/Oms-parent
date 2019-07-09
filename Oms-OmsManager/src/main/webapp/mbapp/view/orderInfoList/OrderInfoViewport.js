Ext.define("MB.view.orderInfoList.OrderInfoViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.orderInfoViewport',
	requires: [
	           'MB.view.orderInfoList.OrderInfoGrid',
	          'MB.view.orderInfoList.OrderInfoPanl',
	          'MB.view.orderInfoList.BatchCancelOrder',
	           'MB.view.orderInfoList.OrderInfoExportTemplateEdit',
	           'MB.view.common.TransTypeCombo',  
	           'MB.view.common.OrderCategoryCombo',
	           'MB.view.common.CountryChannelTypeCombo', 
	           'MB.model.OrderInfoQuery',
	           'MB.model.ComboModel',    
	           'MB.store.DepotStatusStore',      
	           'MB.store.IsAdvanceStore',
	           'MB.store.OrderInfoListStore',
	           'MB.store.OrderStatusStore',
	           'MB.store.OrderTypeStore',
	           'MB.store.OrderViewStore',
	           'MB.store.PayIdStore',
	           'MB.store.RefererStore',
	           'MB.store.ShippingIdStore',
	           'MB.store.TransTypeStore',
	           'MB.store.ShipStatusStore'
	        ],
	items : [ {
		title : '订单列表查询',
		xtype: 'orderInfoPanl',
		region: "north"
	
	
	},{
		title : '订单列表',
		xtype: 'orderInfoGrid',
		region : "south"
	
	}]
});