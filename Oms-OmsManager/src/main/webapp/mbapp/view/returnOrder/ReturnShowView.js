Ext.define("MB.view.returnOrder.ReturnShowView",{
	extend : "Ext.container.Viewport",
	alias : 'widget.returnShowView',
	requires : [ 'MB.model.ReturnActionModel', 'MB.view.returnOrder.ReturnShow' ],
	layout : "border",
	defaults : {
		collapsible : true,
		split : true
	},
	initComponent : function() {
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		this.items = [{//顶部操作按钮
			xtype : 'form',
			region : "north",
			id : 'returnNorth',
			itemId : 'returnNorth',
			align : 'center',
			height : 35,
			collapsible : false,
			dockedItems : [{
				xtype : 'toolbar',
				dock : 'top',
				items : [
				   { text : '<font style="color:white">锁&nbsp;&nbsp;定</font>', tooltip : '锁定', value : 1, action : 'lockReturn',style:'background-color:#1874CD;' }, 
				   { text : '<font style="color:white">保&nbsp;&nbsp;存</font>', tooltip : '保存', value : 1, action : 'saveReturn',style:'background-color:#1874CD;' }, 
				   { text : '<font style="color:white">确&nbsp;&nbsp;认</font>', tooltip : '确认', value : 1, action : 'confirmReturn',style:'background-color:#1874CD;' }, 
				   { text : '<font style="color:white">未确认</font>', tooltip : '未确认', value : 1, action : 'unConfirmReturn',style:'background-color:#1874CD;' },
//				   { text : '<font style="color:white">已收货(整单)</font>', tooltip : '已收货(整单)', value : 1, action : 'receiveReturn',style:'background-color:#1874CD;' }, 
//				   { text : '<font style="color:white">未收货(整单)</font>', tooltip : '未收货(整单)', value : 1, action : 'unReceiveReturn',style:'background-color:#1874CD;' }, 
//				   { text : '<font style="color:white">质检通过(整单)</font>', tooltip : '质检通过(整单)', value : 1, action : 'passReturn',style:'background-color:#1874CD;' }, 
//				   { text : '<font style="color:white">质检不通过(整单)</font>', tooltip : '质检不通过', value : 1, action : 'unPassReturn',style:'background-color:#1874CD;' }, 
//				   { text : '<font style="color:white">入&nbsp;&nbsp;库(整单)</font>', tooltip : '入库(整单)', value : 1, action : 'storageReturn',style:'background-color:#1874CD;' }, 
//				   { text : '<font style="color:white">虚拟入库(整单)</font>', tooltip : '虚拟入库(整单)', value : 1, action : 'virtualStorageReturn',style:'background-color:#1874CD;' }, 
				   /*{ text : '<font style="color:white">入库撤销(整单)</font>', tooltip : '入库撤销(整单)', value : 1, action : 'storageCancle',style:'background-color:#1874CD;' }, */
//				   { text : '<font style="color:white">结&nbsp;&nbsp;算</font>', tooltip : '结算', value : 1, action : 'settleReturn',style:'background-color:#1874CD;' }, 
				   { text : '<font style="color:white">作&nbsp;&nbsp;废</font>', tooltip : '作废', value : 1, action : 'invalidReturn',style:'background-color:#1874CD;' }, 
				   { text : '<font style="color:white">沟&nbsp;&nbsp;通</font>', tooltip : '沟通', value : 1, action : 'communicateReturn',style:'background-color:#1874CD;' }, 
//				   { text : '<font style="color:white">退单转发</font>', tooltip : '退单转发', value : 1, action : 'returnForward',style:'background-color:#1874CD;' },
//				   { text : '<font style="color:white">发送短信</font>', tooltip : '发送短信', value : 1, action : 'messageEdit',style:'background-color:#1874CD;' },
//				   { text : '<font style="color:white">回退客服</font>', tooltip : '回退客服', value : 1, action : 'backToCsReturn',style:'background-color:#1874CD;' } 
				 ]
			}]
		},
		{//中部退货信息
			xtype : 'returnShow',
			region : "center",
			collapsible : false
		},
		{//底部日志面板
			xtype : 'gridpanel',
			region : "south",
			title : '操作日志',
			id : 'returnSouth',
			itemId : 'returnSouth',
			store : Ext.create('Ext.data.Store',{
						model : "MB.model.ReturnActionModel",
						proxy : {
							type : 'ajax',
							actionMethods : {
								read : 'POST'
							},
							url : basePath + 'custom/orderReturn/getOrderReturnActions',
							reader : {
								rootProperty : 'root',
								totalProperty : 'totalProperty'
							},
							simpleSortMode : true
						},
						autoLoad : false
					}),
			autoRender : true,
			columnLines : true,
			width : '100%',
			loadMask : true, // 读取数据时的遮罩和提示功能即加载loding...
			resizable : true,
			forceFit : true,
			collapsible : true,
			titleCollapse : true,
			minHeight : clientHeight * 0.3,
			maxHeight : clientHeight * 0.6,
			collapsed : true,// 初始不展开
			columns : [
					{ text : '操作者', width : clientWidth * 0.08, dataIndex : 'actionUser' },
					{ text : '操作时间', width : clientWidth * 0.14, align : 'center', dataIndex : 'logTime' },
					{ text : '订单状态', width : clientWidth * 0.08, align : 'center', dataIndex : 'returnOrderStatus',
						renderer : function(value) {
							if (value == 0) {
								return "<span style='color:red;'>未确认</span>";
							} else if (value == 1) {
								return "<span style='color:red;'>已确认</span>";
							} else if (value == 4) {
								return "<span style='color:red;'>无效</span>";
							} else if (value == 10) {
								return "<span style='color:red;'>已完成</span>";
							} else {
								return "<span style='color:red;'>错误解析</span>";
							}
						}
					},
					{ text : '付款状态', width : clientWidth * 0.08, align : 'center', dataIndex : 'returnPayStatus',
						renderer : function(value) {
							if (value == 0) {
								return "<span style='color:red;'>未结算</span>";
							} else if (value == 1) {
								return "<span style='color:red;'>已结算</span>";
							} else if (value == 2) {
								return "<span style='color:red;'>待结算</span>";
							} else {
								return "<span style='color:red;'>错误解析</span>";
							}
						}
					},
					{ text : '发货状态', width : clientWidth * 0.08, align : 'center', dataIndex : 'returnShippingStatus',
						renderer : function(value,cellmeta, record,rowIndex) {
							var str = '';
/*							if (record.get("isGoodReceived") == '0' || returnType == '3' || returnType == '4') {
								str = '未收货'
							} else if (record.get("isGoodReceived") == '2') {
								str += "&nbsp;部分收货";
							} else {
								str = "已收货";
							}

							if (record.get("qualityStatus") == '0') {
								str += ",&nbsp;质检不通过";
							} else if (record.get("qualityStatus") == '2') {
								str += ",&nbsp;部分质检通过";
							} else {
								str += ",&nbsp;质检通过";
							}*/
							if (record.get("checkinStatus") == '0') {
								str += "未入库";
							} else if (record.get("checkinStatus") == '1') {
								str += "已入库";
							} else if (record.get("checkinStatus") == '3') {
								str += "部分入库";
							} else {
								str += "待入库";
							}
							return "<span style='color:red;'>"+ str + "</span>";
						}
					},
					{ text : '备注', width : clientWidth * 0.5, dataIndex : 'actionNote',
						renderer : function(value, meta) {
							meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';
							return value;
						}
					}],
			viewConfig : {
				enableTextSelection : true
			}
		// 设置单元格可复制
		} ];
		this.callParent(arguments);
	}

});