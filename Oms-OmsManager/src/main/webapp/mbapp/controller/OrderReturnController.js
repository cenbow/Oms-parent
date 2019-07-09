/***
 *退单列表
 **/

Ext.define('MB.controller.OrderReturnController', {
	extend : 'Ext.app.Controller',
	stores : [ 
		        'OrderReturnListStore',
		        'TimeOfOrderReturnStore',
				'DepotStatusStore',
				'ReturnPayStatusByListStore',
				'IsAdvanceStore',
				'BackToCsDataStore',
				'OrderStatusStore',
				'ReturnReasonStore',
				'ReturnPayStore',
				'OrderTypeStore',
				'QualityStatusStore',
				'HaveRefundForListStore',
				'OrderViewStore',
				'ProcessTypeStore',
				'PayIdStore',
				'ReturnOrderStatusStore',
				'OrderStatusStore',
				'RefererStore',
				'ReturnTypeForListStore',
				'ReturnReasonForListStore',
				'ProcessTypeForListStore',
				'ShippingIdStore',
				'IsReceivedGoodsStore',
				'CheckinStatusStore',
				'ReturnSettlementTypeStore',
				'PayStatusStore',
				'ReturnTypeStore',
			 	'CountryStore',
				'AreaStore',
				'TransTypeStore',
				'QueryPageStore',
				'CityStore',
				'ChannelInfoStore',
				'ChannelTypeStore',
				'ChannelShopStore',
				'OrderCategoryStore',
				'IsGroupStore',
				'ProvinceStore',
				'CityStore',
				'PayIdStore',
				'WarehouseListSourceStore',
				'ShippingIdStore',
				'PayStatusStore',
				'DepotStatusStore',
			//	'TimeStore',
				'ShipStatusStore'
	          ],
	models : [ 
	          'ReturnErpWarehouseModel',
	          'OrderReturnQuery',
	          'ComboModel'
	          ],
	views : [
	         
	         'orderReturnList.OrderReturnGrid',
	         'common.WarehouseListOptionCombo',
	         'orderReturnList.OrderReturnPanl',
	         'orderReturnList.OrderReturnView'

            ],
	refs : [
	        

	
	        ],
	init : function() {
		
	
	},
	onLaunch : function() {
		
		var orderReturnPageSelectTimeType = Ext.getCmp('orderReturnPageSelectTimeType');
		orderReturnPageSelectTimeType.setValue('addTime');
		
		var pageSize = 20;

		
		var orderView = 0;
		var orderReturnPageOrderView =  Ext.getCmp("orderReturnPageOrderView");
		orderReturnPageOrderView.setValue(0);
		
		var orderReturnListPanl =  Ext.getCmp("orderReturnListPanlId");
		var orderReturnGridId =  Ext.getCmp("orderReturnGridId");
		
		
/*		console.dir("orderInfopanl=  "+orderReturnListPanl111);

		console.dir(orderReturnListPanl111.store);
		
		console.dir(orderReturnListPanl111.getStore());
		
		console.dir("orderInfogrid= "+orderReturnGridId111);*/
		
		
		////待结算退单列表
		if("orderReturnSettleList"==settleType) {
			
			var initParams = {start : 0, limit : pageSize };
			var searchParams = getFormParams(orderReturnListPanl);
		
			//显示状态
	//		var orderView = 0;
	//		var orderReturnPageOrderView =  Ext.getCmp("orderReturnPageOrderView");
	//		orderReturnPageOrderView.setValue(0);
			
			var payStatus = 2
			var orderReturnPageReturnPayStatus =  Ext.getCmp("orderReturnPageReturnPayStatus");
			orderReturnPageReturnPayStatus.setValue(2);
			
			//是否退款:1是0否;
			var haveRefund = 1;
			var orderReturnPageHaveRefund =  Ext.getCmp("orderReturnPageHaveRefund");
			orderReturnPageHaveRefund.setValue(1);
			
			var returnPay = 1;
			var orderReturnPageReturnPay =  Ext.getCmp("orderReturnPageReturnPay");
			orderReturnPageReturnPay.setValue(1);
			
			searchParams.orderView = orderView;
			searchParams.payStatus = payStatus;
			searchParams.haveRefund = haveRefund;
			searchParams.returnPay = returnPay;
			searchParams.isNotQuanQiuTong = true;
							
			orderReturnGridId.store.currentPage = 1;// 翻页后重新查询 页面重置为1
			orderReturnGridId.store.pageSize = pageSize;
	//		orderReturnGridId.store.load({params : searchParams});
			
			Ext.getCmp("orderReturnPageExportBtn").setHidden(true);
			Ext.getCmp("orderReturnPageSellteExportBtn").setHidden(false);
			
		}
		
		//待入库结算单
		if("bePutInStorage"==settleType) {
			
			var initParams = {start : 0, limit : pageSize };
			var searchParams = getFormParams(orderReturnListPanl);
			
	//		var orderView = 0;
	//		var orderReturnPageOrderView =  Ext.getCmp("orderReturnPageOrderView");
	//		orderReturnPageOrderView.setValue(0);
			
			var isGoodReceived = 0;
			var orderReturnPageIsGoodReceived =  Ext.getCmp("orderReturnPageIsGoodReceived");
			orderReturnPageIsGoodReceived.setValue(0);
			
			var checkinStatus = 0
			var orderReturnPageCheckinStatus =  Ext.getCmp("orderReturnPageCheckinStatus");
			orderReturnPageCheckinStatus.setValue(0);
			
			var payStatus = 0
			var orderReturnPageReturnPayStatus =  Ext.getCmp("orderReturnPageReturnPayStatus");
			orderReturnPageReturnPayStatus.setValue(0);
			
			var returnOrderStatus = 1
			var orderReturnPageReturnOrderStatus =  Ext.getCmp("orderReturnPageReturnOrderStatus");
			orderReturnPageReturnOrderStatus.setValue(1);
			
			var returnType = 1
			var orderReturnPageReturnType =  Ext.getCmp("orderReturnPageReturnType");
			orderReturnPageReturnType.setValue(1); //退单类型
			
			searchParams.orderView = orderView;
			searchParams.isGoodReceived = isGoodReceived;
			searchParams.checkinStatus = checkinStatus;
			searchParams.returnOrderStatus = returnOrderStatus;
			searchParams.payStatus = payStatus;
			searchParams.returnType = returnType;
			
			orderReturnGridId.store.currentPage = 1;
			orderReturnGridId.store.pageSize = pageSize;
	//		orderReturnGridId.store.load({params : searchParams});
	
		}
	
		setResize(orderReturnListPanl,orderReturnGridId, null, 5);

	}
});