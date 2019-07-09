Ext.define("MB.view.orderReturnList.OrderReturnView", {
	extend : "Ext.container.Viewport",
	alias : 'widget.orderReturnView',
	requires: [
	           'MB.view.orderReturnList.OrderReturnGrid',
	           'MB.view.orderReturnList.OrderReturnPanl',    
	           
	           'MB.store.OrderReturnListStore',
	           'MB.model.OrderReturnQuery',
	           
	           'MB.view.common.TransTypeCombo',  
	        
	           'MB.view.common.OrderCategoryCombo',
	           
	           'MB.view.common.CountryChannelTypeCombo', 
	           
	   
	           'MB.model.ComboModel',    
	           'MB.store.DepotStatusStore',      
	           'MB.store.IsAdvanceStore',
	     
	           'MB.store.OrderStatusStore',
	           'MB.store.OrderTypeStore',
	           'MB.store.OrderViewStore',
	           'MB.store.PayIdStore',
	           'MB.store.RefererStore',
	           'MB.store.ShippingIdStore',
	           'MB.store.TransTypeStore',   
	           'MB.store.ShipStatusStore'
	           
	        ],

	items : [ 
	    {
			title : '退单列表查询',
			xtype: 'orderReturnPanl',
			region: "north"
	    },{
			title : '退单列表',
			xtype: 'orderReturnGrid',
			region : "south"
		}
	]
});