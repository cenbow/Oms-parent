/*******************************************************************************
 * 订单列表
 ******************************************************************************/
var orderSnValue="";
Ext.define('MB.view.orderInfoList.OrderInfoGrid', {
	extend : "Ext.grid.Panel",
	alias : 'widget.orderInfoGrid',
	store: "OrderInfoListStore",
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, // 读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	buttonAlign : 'center',// 按钮居中
	id:'orderInfoGridId',
	initComponent : function() {
		Ext.QuickTips.init();
		var me = this;
		var selModel = Ext.create('Ext.selection.CheckboxModel', {
			listeners: {
				selectionchange: function(sm, selections, aa) {
//					me.down('#confirmButton').setDisabled((selections.length === 0 || batchConfirmAuth)); // 确认按钮;
//					me.down('#unConfirmButton').setDisabled((selections.length === 0 || batchConfirmAuth)); // 未确认按钮;
//					me.down('#cancelButton').setDisabled((selections.length === 0 || batchConfirmAuth)); // 取消按钮;
//					me.down('#issuedButton').setDisabled((selections.length === 0 || batchConfirmAuth)); // 下发分配;
//					me.down('#updateStateButton').setDisabled((selections.length === 0 || batchConfirmAuth)); // 更新发货状态
				}
			}
		});
		
		me.selModel = selModel;
		me.columns = [
		      		{ id : 'masterOrderSn', header : "订单号", align : "center", width : 160, dataIndex:'masterOrderSn',locked:true, renderer: fixMasterOrderSn, sortable:false},
//		      		{ id : 'orderSn', header : "交货单", align : "center", width : 160, dataIndex:'orderSn', locked:true,renderer: fixOrderSn, sortable:false},
					{ id : 'orderOutSn', header : "外部交易号", align : "center", width : 180, dataIndex : 'orderOutSn',locked:true, /*renderer: fixOrderOutSn,*/ sortable:false},
//					{ id : 'relatingOriginalSn', header : "换货单原订单号", align : "center", width : 120, dataIndex : 'relatingOriginalSn',/* renderer: fixRelatingExchangeSn,*/ sortable:false},
//					{ id : 'orderType', header : "订单类型", align : "center", width : 120, dataIndex : 'orderTypeStr',sortable:false},
					{ id : 'channelName', header : "订单店铺", align : "center", width : 170, dataIndex : 'channelName', sortable:false},
//					{ id : 'referer', header : "referer", align : "center", width :170, dataIndex : 'referer', sortable:false},
//					{ id : 'questionStatus', header : "问题单状态", align : "center", width : 80, dataIndex : 'questionStatus', renderer: fixQuestionStatus, sortable:false},
//					{ id : 'lockStatus', header : "锁定状态", align : "center", width : 100, dataIndex : 'lockStatus', renderer: fixLockStatus, sortable:false},
					{ id : 'orderStatus',header : "订单状态", align : "center", sortable : true,  width : 180, renderer: editOrderStatus, sortable:false},
					{ id : 'userId', header : "下单人", align : "center", width : 160, dataIndex : 'userId'},
					{ id : 'addTime', header : "下单时间", align : "center", width : 160, dataIndex : 'addTime'},
//					{ id : 'payName', header : "支付方式", align : "center", width : 160, dataIndex : 'payName'},
//					{ id : 'payNote', header : "支付流水号", align : "center", width : 160, dataIndex : 'payNote'},
//					{ id : 'deliveryTime', header : "发货时间", align : "center", width : 160, dataIndex : 'deliveryTime'},
					{ id : 'goodsCount', header : "商品数量", align : "center", width : 85, dataIndex : 'goodsCount', sortable:false},
					{ id : 'totalFee', header : "订单总金额", align : "center", width : 120, dataIndex : 'totalFee', sortable:false},
					{ id : 'goodsAmount', header : "商品总金额", align : "center", width : 80, dataIndex : 'goodsAmount', sortable:false},		
//					{ id : 'totalPayable', header : "应付款金额", align : "center", width : 120, dataIndex : 'totalPayable', sortable:false},
					{ id : 'moneyPaid', header : "实收金额", align : "center", width : 120, dataIndex : 'moneyPaid', sortable:false},
					{ id : 'discount', header : "优惠金额", align : "center", width : 120, dataIndex : 'discount', sortable:false}
//					{ id : 'settlementPrice', header : "财务价格", align : "center", width : 80, dataIndex : 'settlementPrice', sortable:false},
//					{ id : 'tax', header : "综合税费", align : "center", width : 80, dataIndex : 'tax', sortable:false},
//					{ id : 'bonus', header : "红包金额", align : "center", width : 80, dataIndex : 'bonus', sortable:false},
//					{ id : 'shippingTotalFee', header : "邮费金额", align : "center", width : 80, dataIndex : 'shippingTotalFee', sortable:false}
//					{ id : 'calculateDiscount', header : "折扣率", align : "center", width : 80, dataIndex : 'calculateDiscount', sortable:false},
//					{ id : 'confirmTime', header : "订单确认时间", align : "center", width : 160, dataIndex : 'confirmTime', sortable:false},
//					{ id : 'integralMoney', header : "使用积分金额", align : "left", width : 80, dataIndex : 'integralMoney',sortable:false},
//					{ id : 'bvValue', header : "赠送美力值", align : "left", width : 80, dataIndex : 'bvValue',sortable:false},
//					{ id : 'expectedShipDate', header : "预计发货日", align : "left", width : 150, dataIndex : 'expectedShipDate',sortable:false}//,
		];

		me.dockedItems = [{
			xtype: 'pagingtoolbar',
			store: 'OrderInfoListStore',
			dock: 'bottom',
			displayMsg:"当前显示从{0}条到{1}条，共{2}条",
			displayInfo: true
		}];
		
		/*me.buttons = [
				{
					minWidth: 80,
					text: '确认',
					itemId: 'confirmButton',
					disabled: true,
					handler: me.batchConfirm
				},{
					minWidth: 80,
					text: '未确认',
					itemId: 'unConfirmButton',
					disabled: true,
					handler: me.batchUnConfirm
				},{
					minWidth: 80,
					text: '取消订单',
					itemId: 'cancelButton',
					disabled: true,
					handler:  me.batchCancel
				},{
					minWidth: 80,
					text: '下发分配',
					itemId: 'issuedButton',
					disabled: true,
					handler:  me.batchIssued
				},{
					minWidth: 80,
					text: 'SWDI',
					itemId: 'updateStateButton',
					disabled: true,
					handler: me.batchswdi
				}
		]*/
		me.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},

	batchConfirm : function(btn) {
		var orderInfoGrid =  Ext.getCmp("orderInfoGridId");
		// 批量确认被选择的行订单
		var selModel = orderInfoGrid.getSelectionModel();
		if (selModel.hasSelection()) {
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				var orderSn = records[i].get("masterOrderSn");
				var orderStatus = records[i].get("orderStatus");
				if (orderStatus == 0) {
					if(orderSn && orderSn != ''){
						orderSns.push(orderSn);
					}
				}
			}
			if(orderSns && orderSns.length > 0){
				promptMsg("确认被选择的订单！","备注：", function(btn, text) {
					if (btn == "ok") {
						createAjaxData("batchConfirm", basePath + "/custom/orderStatus/batchConfirm", confirmSuccFun,doFailurefun, {"masterOrderSns":orderSns,"message": text}, 100000);
					}
				});
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有未确认订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量确认的订单!");
		}	
	
	},
	
	batchUnConfirm : function(btn) {
		
		var orderInfoGrid =  Ext.getCmp("orderInfoGridId");
	
		var selModel = orderInfoGrid.getSelectionModel();
		
		if (selModel.hasSelection()) {
			
	
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				
				var orderSn = records[i].get("masterOrderSn");
		
				orderSns.push(orderSn);
		
			}
			if(orderSns && orderSns.length > 0){
		
						createAjaxData("batchUnConfirm",  basePath + "/custom/orderStatus/batchUnConfirm" , confirmSuccFun,doFailurefun, {"masterOrderSns":orderSns}, 100000);
			
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有下发erp订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量下发erp的订单!");
		}
	
	},
	
	batchCancel : function(btn) {
		
		var orderInfoGrid =  Ext.getCmp("orderInfoGridId");
		
		var selModel = orderInfoGrid.getSelectionModel();
		
		if (selModel.hasSelection()) {
			
		
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				
				var orderSn = records[i].get("masterOrderSn");
				
				orderSns.push(orderSn);
			
			}
			if(orderSns && orderSns.length > 0){
				
				orderSnValue = orderSns; // 赋值全局变量
				
				var win = Ext.widget("batchCancelOrder");
				win.show();

				//FormEditWin.showAddDirWin( "popWins", basePath+"/custom/orderInfo/batchCancelPage.spmvc","questionCode","批量取消订单", 550, 330);
				
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有确认订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量取消的订单!");
		}
	
	},
	
	batchIssued : function(btn) { // 下发erp
		
		var orderInfoGrid =  Ext.getCmp("orderInfoGridId");
		

	//	var orderInfoPageMainOrChild =  Ext.getCmp("orderInfoPageMainOrChild");
		
		var orderInfoPageMainOrChild = Ext.getCmp("orderInfoPageMainOrChild").getChecked()[0].inputValue;
		
		var type= "0";
		
		if(orderInfoPageMainOrChild == "child" ){
			type = "1";
		}
		
		var selModel = orderInfoGrid.getSelectionModel();
		
		if (selModel.hasSelection()) {
			
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				
				var orderSn  = "";			
			
				if(orderInfoPageMainOrChild == "child" ){
					 orderSn = records[i].get("orderSn");
				} else {
					 orderSn = records[i].get("masterOrderSn");	
				}
		
				orderSns.push(orderSn);
			
			}
			if(orderSns && orderSns.length > 0){
				createAjaxData("batchToErp", basePath + "/custom/orderStatus/batchToErp", confirmSuccFun,doFailurefun, {"orderSns":orderSns,"type": type}, 100000);	
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有下发erp订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量下发erp的订单!");
		}
	
	},
	
	batchswdi : function(btn) {  // 订单分仓发货
		
		var orderInfoGrid =  Ext.getCmp("orderInfoGridId");
		 
		var selModel = orderInfoGrid.getSelectionModel();
		

		
		var orderInfoPageMainOrChild = Ext.getCmp("orderInfoPageMainOrChild").getChecked()[0].inputValue;


	
		var type= "0";
		
		if(orderInfoPageMainOrChild == "child" ){
			type = "1";
		}
		
		if (selModel.hasSelection()) {
			
			var records = selModel.getSelection();
			var orderSns = new Array();
			for ( var i = 0; i < records.length; i++) {
				
				//var orderSn = records[i].get("orderSn");
		
				var orderSn  = "";			
				
				if(orderInfoPageMainOrChild == "child" ){
					 orderSn = records[i].get("orderSn");
				} else {
					 orderSn = records[i].get("masterOrderSn");	
				}
		
				
				
				orderSns.push(orderSn);
			}
			if(orderSns && orderSns.length > 0){
						createAjaxData("batchswdi", basePath + "/custom/orderStatus/swdiInList" , confirmSuccFun,doFailurefun, {"orderSn":orderSns,"type": type}, 100000);
		
			} else {
				Ext.Msg.alert("错误", "选择的订单中不含有下发erp订单!");
			}
		} else {
			Ext.Msg.alert("错误", "请选择要批量下发erp的订单!");
		}
		
	},

});

function fixMasterOrderSn(value) {
	if("1" == display && value != undefined && value != null ){
		var url = order_info_url + value +'&isHistory=' + isHistory;
		return "<a href=" +url + " target='_blank'  >" + value + "</a>";
	} else {
		return '<font>' + value + '</font>';
	}
}


//var order_info_url = '<%=basePath%>' + '/custom/orderInfo/orderDetail?masterOrderSn=';

function fixOrderSn(value, cellmeta, record) {
	
	var masterOrderSn = record.get("masterOrderSn");
	
	if("1" == display && value != undefined && value != null ){
		var url = order_info_url + masterOrderSn  +'&&orderSn='+      value +'&isHistory=' + isHistory;  
		return "<a href=" +url + " target='_blank'  >" + value + "</a>";
	} else if(value == undefined || value == ''){
		return '<font>  </font>';
	} else {
		return '<font>' + value + '</font>';
	}
}

function fixLockStatus(v, md, r) {
	var str = v;
	if (v == 0) {
		str = '<font style="color:green;">未锁定</font>';
	} else {
		var userName = r.get('lockUserName');
		if (v == 10000 && (userName == undefined || userName == null || userName == '')) {
			str = '<font style="color:red;">其他管理员锁定</font>';
		} else {
			// str = '<font style="color:red;">'+ userName +'</font>';
			str = '<font style="color:red;">已锁定</font>';
		}
	}
	return str;
}

function fixOrderOutSn(value, md, r) {
	if (value != undefined && value != null ) {
		var orderSn =  r.get('orderSn');
		var url = order_info_url + orderSn +"&isHistory=" + isHistory;
		return "<a href=" +url + " target='_blank' >" + value + "</a>";
	}
}

function fixRelatingExchangeSn(value, md, r) {
	if (value != undefined && value != null ) {
		var orderSn =  r.get('relatingExchangeSn');
		var orderType =  r.get('orderType');
		if (orderType && orderType == '2') {
			var url = order_info_url + orderSn + "&isHistory=" + isHistory;
			return "<a href=" +url + " target='_blank' >" + value + "</a>";
		}
	}
}

function fixQuestionStatus(v) {
	var str = "";
	if (v == 0) {
		str = '<font style="color:green;">正常单</font>';
	} else {
		str = '<font style="color:red;">问题单</font>';
	}
	return str;
} 

function editOrderStatus(value, cellmeta, record) {
	var orderStatus = record.get('orderStatus');
	var payStatus = record.get('payStatus');
	var shipStatus = record.get('shipStatus');
	return getCombineStatus(orderStatus, payStatus, shipStatus);
}


function confirmSuccFun (id, response, opts) {
	
	if("batchCancel"==id){
		
		var respText = Ext.JSON.decode(response.responseText);
		if(respText.success == "true"){
			
			var orderInfoGrid =  Ext.getCmp("orderInfoGridId");			
			orderInfoGrid.getStore().load();
	
			alertMsg("结果", "订单已放入队列！");
			
			var batchCancelOrderWin =  Ext.getCmp("batchCancelOrderWin");			
			
			batchCancelOrderWin.close();
		}else{
			
			var orderInfoGrid =  Ext.getCmp("orderInfoGridId");			
			orderInfoGrid.getStore().load();
		
			errorMsg("结果", respText.msg);
			
			
			var batchCancelOrderWin =  Ext.getCmp("batchCancelOrderWin");			
			
			batchCancelOrderWin.close();
		
		}
		
	} else {
		var respText = Ext.JSON.decode(response.responseText);
		if(respText.success == "true"){
			
			alertMsg("结果","订单已放入队列！");
			
			var orderInfoGrid =  Ext.getCmp("orderInfoGridId");			
			orderInfoGrid.getStore().load();
		
		}else{
			
			var orderInfoGrid =  Ext.getCmp("orderInfoGridId");		
			orderInfoGrid.getStore().load();

			errorMsg("结果", respText.msg);
		}
	}
	
}

function doFailurefun(){
	//var respText = Ext.JSON.decode(response.responseText);
	errorMsg("结果", respText.msg);
}