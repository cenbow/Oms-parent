/***
 *订单列表 
 **/
Ext.define('MB.controller.OrderInfoController', {
	extend : 'Ext.app.Controller',
	stores : [ 
			'DepotStatusStore',
			'IsAdvanceStore',
			'OrderInfoListStore',
			'OrderStatusStore',
			'OrderTypeStore',
			'OrderViewStore',
			'PayIdStore',
			'RefererStore',
			'OrderInfoOperationReasonStore',
			'ShippingIdStore',
			'OrderInfoInputExportTemplateTypeStore',
			'CountryStore',
			'SourceDataStore',
			'AreaStore',
			'OrderInfoViewStore',
			'TransTypeStore',
			'QueryPageStore',
			'CityStore',
			'ChannelInfoStore',
		//	'OrderReturnListStore',
			'ChannelTypeStore',
			'ChannelShopStore',
			'OrderCategoryStore',
			'IsGroupStore',
			'ProvinceStore',
		//	'CityStore',
			'PayIdStore',
			'ShippingIdStore',
			'PayStatusStore',
			'DepotStatusStore',
			'TimeStore',
			'ShipStatusStore',
			'AdvanceStatusStore',
			'SplitStatusStore'
	          ],
	models : [
	          'SystemRegionArea',
	          'OrderInfoQuery',
	          'OrderCustomDefine',
	          'ComboModel'
	         
	          ],
	views : [
	         'orderInfoList.OrderInfoGrid',
	          'orderInfoList.OrderInfoPanl',
	          'common.CountryChannelTypeCombo',
	          'orderInfoList.OrderInfoViewport'//,
	        //  'orderInfo.TransType',
	     //     'orderInfo.OrderType'
	          
            ],
	refs : [{
		ref : 'orderInfoGrid',
		selector : 'orderInfoGrid'
	} ,{
		ref : 'orderInfoPanl',
		selector : 'orderInfoPanl'
	}
	        ],
	init : function() {
		
		//var me = this;
		
	},
	onLaunch : function() {
			// 页面加载完成之后执行
		//	var win = Ext.widget("cancelOrder");
		//	win.show();
		
		var orderInfoPageQueryPageArray = Ext.getCmp('orderInfoPageQueryPageArray');
		orderInfoPageQueryPageArray.setValue('20');
		
		var orderInfoPageSelectTimeType = Ext.getCmp('orderInfoPageSelectTimeType');
		orderInfoPageSelectTimeType.setValue('addTime');
		
		var orderInfoListPanl =  Ext.getCmp("orderInfoListPanlId");
		var OrderInfoGrid =  Ext.getCmp("orderInfoGridId");
	
		 //货到付款待收款订单
		if(1==settleType) {
			var initParams = {start : 0, limit : pageSize };
			var searchParams = getFormParams(orderInfoListPanl);
		
			var transType =2;
			var orderInfoPageTransType =  Ext.getCmp("orderInfoPageTransType");
			orderInfoPageTransType.setValue(2);
			
			var orderStatus =1;
			var orderInfoPageOrderStatus =  Ext.getCmp("orderInfoPageOrderStatus");
			orderInfoPageOrderStatus.setValue(1);

			var payStatus = 0;
			var orderInfoPagePayStatus =  Ext.getCmp("orderInfoPagePayStatus");
			orderInfoPagePayStatus.setValue(0);
			
			var shipStatus  = 3;
			var orderInfoPageShipStatus =  Ext.getCmp("orderInfoPageShipStatus");
			orderInfoPageShipStatus.setValue(3);

			var orderView = 0;	
			var orderInfoPageOrderView =  Ext.getCmp("orderInfoPageOrderView");	
			orderInfoPageOrderView.setValue(0);

			searchParams.transType = transType;
			searchParams.orderStatus = orderStatus;
			
			searchParams.payStatus = payStatus;
			searchParams.shipStatus = shipStatus;
			searchParams.orderView = orderView;
			
			orderInfoGrid.store.currentPage = 1;// 翻页后重新查询 页面重置为1
			orderInfoGrid.store.pageSize = pageSize;
		//	orderInfoGrid.store.load({params : searchParams});
			
		}
		var orderInfoPageOrderView =  Ext.getCmp("orderInfoPageOrderView");	
		orderInfoPageOrderView.setValue(0);
		
		//待订单结算
		if("orderInfoSettle"==settleType) {
			
			var initParams = {start : 0, limit : pageSize };
			var searchParams = getFormParams(orderInfoListPanl);

			var orderView = 0;	
			var orderInfoPageOrderView =  Ext.getCmp("orderInfoPageOrderView");
			orderInfoPageOrderView.setValue(0);
			
			var orderStatus = 1;	
			var orderInfoPageOrderStatus =  Ext.getCmp("orderInfoPageOrderStatus");
			orderInfoPageOrderStatus.setValue(1);
			
			var payStatus = 2;
			var orderInfoPagePayStatus =  Ext.getCmp("orderInfoPagePayStatus");
			orderInfoPagePayStatus.setValue(2);
			
			var shipStatus = 3;
			var orderInfoPageShipStatus =  Ext.getCmp("orderInfoPageShipStatus");
			orderInfoPageShipStatus.setValue(3);
			
			searchParams.orderView = orderView;
			searchParams.orderStatus=orderStatus;
			
			searchParams.payStatus=payStatus;
			
			searchParams.shipStatus=shipStatus;
			
			OrderInfoGrid.store.currentPage = 1;// 翻页后重新查询 页面重置为1
			OrderInfoGrid.store.pageSize = pageSize;
	//		OrderInfoGrid.store.load({params : searchParams});
	
			Ext.getCmp("orderInfoExportBtn").setHidden(true);
			Ext.getCmp("orderInfoSellteExportBtn").setHidden(false);
		}

		setResize(orderInfoListPanl,OrderInfoGrid, null, 5);
		
	}
});