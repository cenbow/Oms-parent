/***
 *问题列表
**/
Ext.define('MB.controller.OrderQuestionController', {
	extend : 'Ext.app.Controller',
	stores : [ 
	          'OrderQuestionListStore',
	          'TimeOfOrderQuestionStore',
	          'ProcessStatusStore',
	          'PayStatusStore',
	          'ProcessStatusStore',
	          'ShipStatusStore',
	          'RefererStore',
	          'ChannelShopStore',
	          'ShortageQuestionStore',
	          'QueryPageStore',
	          'ChannelInfoStore',
	          'QuestionStore',
	     //     'TimeStore',
	          'LogisTypeStore',
	          'QuestionStore',
	          'ChannelTypeStore',
	          'QuestionStoreByImport',
	        //  'MainChildStore',
	   //       'questionDataStore',
	   //       'logisTypeDataStore',
	          'TransTypeStore'
	         
		//      'OrderReturnListStore',
		/*		'DepotStatusStore',
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
				'ReturnPayStatusStore',
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
				'TimeStore',
				'ShipStatusStore'*/
     
	          ],
	models : [ 
	          
	   //       'ReturnErpWarehouseModel',
	     //     'OrderReturnQuery',
	   	      'ShortageQuestionModel',
	   		  'OrderCustomDefine',
	          'ComboModel'
	          ],
	views : [
	         'orderQuestionList.ReturnNormal',
	         'orderQuestionList.OrderQuestionGrid',
	      // 'common.WarehouseListOptionCombo',
	         'orderQuestionList.OrderQuestionPanl',
	         'orderQuestionList.ShortageQuestionGrid',
	         'orderQuestionList.OrderQuestionView',
	         'orderQuestionList.ImportOrderLogisticsQuestion'

            ],
	refs : [
	        
	/*	        {
			ref : 'orderInfoGridView',
			selector : 'orderInfoGridView'
		} ,{
			ref : 'orderInfoPanlView',
			selector : 'orderInfoPanlView'
		}*/
	
	        ],
	init : function() {
		
	
	},
	onLaunch : function() {
		
		var orderInfoPageQueryPageArray = Ext.getCmp('orderQuestionQueryPageArray');
		orderInfoPageQueryPageArray.setValue('20');
		
		var orderQuestionPageSelectTimeType = Ext.getCmp('orderQuestionPageSelectTimeType');
		orderQuestionPageSelectTimeType.setValue('addTime');
		
		var orderQuestionListPanlId =  Ext.getCmp("orderQuestionListPanlId");
		var orderQuestionGridId =  Ext.getCmp("orderQuestionGridId");
		
		var orderQuestionPageProcessStatus =  Ext.getCmp("orderQuestionPageProcessStatus");
		
		orderQuestionPageProcessStatus.setValue("0"); 
		
		/*var shortageQuestion_gridss_id =  Ext.getCmp("shortageQuestion_gridss_id");
		shortageQuestion_gridss_id.setWidth('20%');
		shortageQuestion_gridss_id.setVisible(false);*/
		
		setResize(orderQuestionListPanlId, orderQuestionGridId, null, 5);
		
	}
});


