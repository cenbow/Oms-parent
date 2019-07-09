Ext.define("MB.view.refund.RefundGrid", {
	extend: "Ext.grid.Panel",
	alias: 'widget.refundGrid',
	id:"refundGrid",
//	requires : ['Ext.grid.plugin.CellEditing'],
	store: "RefundGridStore",  
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	
	viewConfig:{
		forceFit: true, // 注意不要用autoFill:true,那样设置的话当GridPanel的大小变化（比如你resize了它）时不会自动调整column的宽度
		scrollOffset: 0 //不加这个的话，会在grid的最右边有个空白，留作滚动条的位置
	},
	initComponent: function () {
		Ext.QuickTips.init();
		/*var queryUrl = basePath+"/custom/orderReturn/orderReturnList.spmvc";
		var pageSize=20;
		var orderReturnJsonStore = null;
		orderReturnJsonStore = createObjectGridStoreLazy(queryUrl, pageSize, "MB.OrderReturnQuery");
		this.store=orderReturnJsonStore;*/
		this.columns = [
			{ header : "退单号", align : "left", width : 280, dataIndex : 'relatingReturnSn' , renderer: function(value, md, record) {
				if(value){
					var backBalance = record.get("backbalance");
					var returnPay = record.get("returnPay");
					var url=basePath+"custom/orderReturn/orderReturnPage?returnSn="+value+"&returnType="+record.get('returnType');
					if(returnPay == 3){
						if( backBalance == 1){
							value = value +"<span style='color:green;'>(邦购币已退)</span>"
						}else{
							value = value +"<span style='color:red;'>(邦购币未退)</span>"
						}
						
					}
					return "<a href="+url+"  target='_blank' >" + value + "</a>";
				}
			}
		},
		{ header : "关联订单号", align : "center", width : 120, dataIndex : 'relatingOrderSn', renderer: function(value) {
			if (value != undefined && value != null ) {
				var isHistory=Ext.getCmp('refundContent').getForm().getValues().isHistory;
				var url=basePath+"custom/orderInfo/orderDetail?masterOrderSn="+value+"&isHistory="+isHistory;
				return "<a href="+url+"  target='_blank' >" + value + "</a>";
			}
		}
		},
		{ header : "关联外部交易号", align : "center", width : 200, dataIndex : 'orderOutSn', renderer: function(value, md, r) {
			if (value != undefined && value != null ) {
				/*var isHistory=Ext.getCmp('refundContent').getForm().getValues().isHistory;
				var url=basePath+"custom/orderInfo/orderDetail?masterOrderSn="+value+"&isHistory="+isHistory;
				return "<a href="+url+"  target='_blank' >" + value + "</a>";*/
				return value;
			}
		}},
		{ header : "退款金额", align : "center", width : 120, dataIndex : 'returnFee' },
		{ header : "订单来源", align : "center", width : 200, dataIndex : 'channelName' },
		{ header : "referer", align : "center", width : 200, dataIndex : 'referer'},
		{ header : "退单类型", align : "center", width : 90, dataIndex : 'returnTypeName',renderer: function(value, md, r) {
			if (value == '5' ) {
				return "失货退货单";
			} else{
				return value;
			}
			}},
			
		{ header : "退单状态", align : "center", sortable : true,  width : 320, renderer: this.editReturnStatus},
		{ header : "财务状态", align : "center", sortable : true,  width : 180,dataIndex : 'returnPayStatusName'},
		{ header : "退款方式", align : "center", sortable : true,  width : 160, dataIndex : 'payName' }
		
		];
		this.bbar=new Ext.PagingToolbar({  
	        pageSize:15, //每页显示几条数据  
			store: "RefundGridStore",
	        displayInfo:true, //是否显示数据信息  
	        displayMsg:'显示第 {0} 条到 {1} 条记录，一共  {2} 条', //只要当displayInfo为true时才有效，用来显示有数据时的提示信息，{0},{1},{2}会自动被替换成对应的数据  
	        emptyMsg: "没有记录" //没有数据时显示信息  
	    });
		this.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		this.callParent(arguments);
	},
	editOrderStatus:function (value, cellmeta, record) {
		var orderStatus = record.get('orderOrderStatus');
		var payStatus = record.get('orderPayStatus');
		var shipStatus = record.get('orderShipStatus');
		return getCombineStatus(orderStatus, payStatus, shipStatus);
	},
	editReturnStatus:function (value, cellmeta, record) {
		var orderStatus = record.get('returnOrderStatus');
		var payStatus = record.get('payStatus');
		var isGoodReceived = record.get('isGoodReceived');
		var checkinStatus = record.get('checkinStatus');
		var qualityStatus = record.get('qualityStatus');
		return getCombineReturnStatusNew(orderStatus, payStatus, checkinStatus,isGoodReceived,qualityStatus);
	}
	
});
