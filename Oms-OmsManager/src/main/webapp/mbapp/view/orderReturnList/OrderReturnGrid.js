/**
 *订单列表 
 ***/
Ext.define('MB.view.orderReturnList.OrderReturnGrid', {
	extend : "Ext.grid.Panel",
	alias : 'widget.orderReturnGrid',
	store: "OrderReturnListStore",
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, // 读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	id:'orderReturnGridId',
	initComponent : function() {
		Ext.QuickTips.init();
		var me = this;
		
/*		var selModel = Ext.create('Ext.selection.CheckboxModel', {});
		
		me.selModel = selModel;*/
		
		me.columns = [
		  
		      		{ id : 'returnSn', header : "退单号", align : "left", width : 280, dataIndex : 'returnSn' , renderer: function(value, md, r) {
		      			var backToCs =  r.get('backToCs');
		      
		      			var backToCsStr="";
		      			
		      			//是回退客服
		      			if(1==backToCs){
		      				
		      				backToCsStr="<font color='red'><b>(回退)</b></font>";
		      			}
		      			var returnReason =  r.get('returnReason');
		      
		      			var returnReasonStr="";
		      			
		      			//质量问题
		      			if("21"==returnReason){
		      			
		      				returnReasonStr="<font color='red'><b>(质量问题)</b></font>";
		      			}

		      			if("1" == $("#orderReturnDisplay").val()){
		      				if (value != undefined && value != null ) {
		      					var url = order_return_url + value + '&isHistory=0';
		      					return "<a href='"+url+"' target='_blank' >"+value+"</a>"+backToCsStr+returnReasonStr;
		      				}
		      			} else {
		      				return "<font>"+value+"</font>"+backToCsStr+returnReasonStr;
		      			}
		      		}
		      		},
		      		
		      		{ id : 'masterOrderSn', header : "订单号", align : "left", width : 200, dataIndex : 'masterOrderSn',sortable:false, renderer: fixMasterOrderSn},
		      		
		      		
		      		{ id : 'relatingOrderSn', header : "关联交货单号", align : "center", width : 200, dataIndex : 'relatingOrderSn'
		      		/*	, renderer: function(value) {
		      			if (value != undefined && value != null ) {
		      				var url = order_info_url + value +"&isHistory=" + isHistory;
		      				return "<a href=" +url + " target='_blank' >" + value + "</a>";
		      			}
		      			}*/
		      		},
		      		{ id : 'orderOutSn', header : "关联外部交易号", align : "center", width : 200, dataIndex : 'orderOutSn'
		      	/*		, renderer: function(value, md, r) {
			      			if (value != undefined && value != null ) {
			      				var orderSn =  r.get('relatingOrderSn');
			      				var url = order_info_url + orderSn +"&isHistory=" + isHistory;
			      				return "<a href=" +url + " target='_blank' >" + value + "</a>";
			      			}
		      			}*/
		      		},
		      		{ id : 'channelName', header : "订单来源", align : "center", width : 200, dataIndex : 'channelName' },
		      		{ id : 'referer', header : "referer", align : "center", width : 200, dataIndex : 'referer'},
		      		{ id : 'returnTypeStr', header : "退单类型", align : "center", width : 90, dataIndex : 'returnTypeStr' },
		      		{ id : 'returnStatus',header : "退单状态", align : "center", sortable : true,  width : 180, renderer: editReturnStatus},
		      		{ id : 'orderStatus',header : "订单状态", align : "center", sortable : true,  width : 180, renderer: editOrderStatus},
		      		{ id : 'returnPayStr',header : "退款方式", align : "center", sortable : true,  width : 160, dataIndex : 'returnPayStr' },
		      		{ id : 'addTime', header : "退单时间", align : "center", width : 160, dataIndex : 'addTime',sortable:true},
		      		{ id : 'checkinTime', header : "入库时间", align : "center", width : 160, dataIndex : 'checkinTime' ,sortable:true},	
		      		{ id : 'clearTime', header : "结算时间", align : "center", width : 160, dataIndex : 'clearTime' ,sortable:true},
		      
		      		{ id : 'userName', header : "下单人", align : "center", width : 100, dataIndex : 'userName'},
		  
		      		{ id : 'returnGoodsCount', header : "退货数量", align : "center", width : 120, dataIndex : 'returnGoodsCount'},
		      		{ id : 'returnTotalFee', header : "退款金额", align : "center", width : 120, dataIndex : 'returnTotalFee'},
		      		
		      		{ id : 'returnShipping', header : "邮费", align : "center", width : 120, dataIndex : 'returnShipping'},  //order_refund
		      		{ id : 'returnGoodsMoney', header : "退商品金额", align : "center", width : 120, dataIndex : 'returnGoodsMoney'}, //order_refund		
		      		{ id : 'returnInvoiceNo', header : "退货快递单号", align : "center", width : 120, dataIndex : 'returnInvoiceNo'}, //order_return_ship
		      		 
		      		{ id : 'returnReasonStr', header : "退单原因", align : "center", width : 250, dataIndex : 'returnReasonStr'},
		      		
		      		{ id : 'haveRefund', header : "是否退款", align : "center", width : 250, renderer: editHaveRefund},
		      		
		      		{ id : 'consignee', header : "收件人", align : "center", width : 250, dataIndex : 'consignee'},

		      		{ id : 'returnOtherMoney', header : "退其他费用", align : "center", width : 180, dataIndex : 'returnOtherMoney'},
		      		{ id : 'warehouseName', header : "退货入库仓", align : "center", width : 180, dataIndex : 'warehouseName'}
//		      		{ id : 'integralMoney', header : "使用积分金额", align : "left", width : 200, dataIndex : 'integralMoney',sortable:false},
		];
		me.dockedItems = [{
			xtype: 'pagingtoolbar',
			store: 'OrderReturnListStore',
			dock: 'bottom',
			displayInfo: true	
		}],
		me.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	}

});

//退单状态
function editReturnStatus(value, cellmeta, record) {
	var orderStatus = record.get('returnOrderStatus');
	var payStatus = record.get('payStatus');

	var checkinStatus = record.get('checkinStatus'); // '是否入库 （0未入库 1已入库 2待入库）',
	
	var isGoodReceived = record.get('isGoodReceived'); //'是否收到货 （0 否  1 是）',
	
	var qualityStatus = record.get('qualityStatus'); //退单质检状态 （0质检不通过、1质检通过）',
	
	return getCombineReturnStatusNew(orderStatus, payStatus, isGoodReceived, checkinStatus, qualityStatus);
}

function fixMasterOrderSn(value) {
	if("1" == display && value != undefined && value != null ){
		var url = order_info_url + value +'&isHistory=' + isHistory;
		return "<a href=" +url + " target='_blank'  >" + value + "</a>";
	} else {
		return '<font>' + value + '</font>';
	}
}

function editOrderStatus(value, cellmeta, record) {
	var orderStatus = record.get('orderOrderStatus');
	var payStatus = record.get('orderPayStatus');
	var shipStatus = record.get('orderShipStatus');
	return getCombineStatus(orderStatus, payStatus, shipStatus);
}

function editHaveRefund(value, cellmeta, record) {
	var haveRefund = record.get('haveRefund');

	return getHaveRefundStatus(haveRefund);
	       
}
