Ext.define("MB.view.orderDetail.OrderDetailSouth", {
	extend : "Ext.form.Panel",
	alias : 'widget.orderDetailSouth',
	id : 'orderDetailSouth',
	collapsible : true,//允许展开/收缩
	autoScroll : true,//自动滚屏
	forceFit: true,
	collapsed: true,//初始不展开
	minHeight: clientHeight * 0.3,
	maxHeight: clientHeight * 0.6,
	items : [{
		xtype : 'tabpanel',
		id : 'logModuleTabPanel',
		deferredRender:false,
		enableTabScroll: true, //选项卡过多时，允许滚动
		defaults: { autoScroll: true },
		listeners : {//title切换时更新公用日志内容面板的url然后加载
			tabchange:function(tabPanel, newCard, oldCard, eOpts){
				var itemId = newCard.itemId;
				var orderLogModuleStore = Ext.getCmp('orderLogModule').getStore();
				if(itemId=='masterOrderLog'){
					orderLogModuleStore.getProxy().url = basePath + 'custom/commonLog/getMasterOrderAction?masterOrderSn='+masterOrderSn+'&isHistory='+isHistory;
				}else{
					orderLogModuleStore.getProxy().url = basePath + 'custom/commonLog/getDistributeOrderAction?orderSn='+itemId+'&isHistory='+isHistory;
				}
				orderLogModuleStore.load();
			}
		},
		items : [{//默认只有一个订单日志面板
			itemId : 'masterOrderLog',
			title : '订单日志'
		}]
	},{//公用的日志内容面板
		xtype : 'gridpanel',
		id : 'orderLogModule',
		loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
		forceFit: false,
		store: Ext.create('Ext.data.Store', {
			fields: [
			 		{ name: 'actionUser', type: 'string' },
			 		{
			 			name: 'logTime',
			 			type: 'date',
			 			convert:function(value){if(value){return Ext.Date.format(new Date(value),"Y-m-d H:i:s");}} 
			 		},
			 		{ name: 'orderStatus', type: 'string' },
			 		{ name: 'payStatus', type: 'string' },
			 		{ name: 'shippingStatus', type: 'string' },
			 		{ name: 'actionNote', type: 'string' }
			 	],
			proxy: {
				type: 'ajax',
				actionMethods: {
					read: 'POST'
				},
				reader: {
					rootProperty: 'root',
					totalProperty: 'totalProperty'
				},
				simpleSortMode: true
			},
			autoLoad : false
		}),
		columns : [
				{ text: '操作者', width: 100, dataIndex: 'actionUser' },
				{ text: '操作时间', width: 150, align: 'center', dataIndex: 'logTime' },
				{ text: '订单状态', width: 100, align: 'center', dataIndex: 'orderStatus',
					renderer: function (v) {
						return getOrderStatus(v);
					}
				},
				{ text: '付款状态', width: 100,align: 'center', dataIndex: 'payStatus',
					renderer: function (v) {
						return getPayStatus(v);
					}
				},
				{ text: '发货状态', width: 100,align: 'center', dataIndex: 'shippingStatus',
					renderer: function (v) {
						return getShipStatus(v);
					}
				},
				{
					text: '备注',
					width: clientWidth-550,
					dataIndex: 'actionNote',
					renderer: function (value,meta) {
						meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';
						return value;
					}
				}
		],
		viewConfig : {enableTextSelection: true}// 设置单元格可复制
	}]
	
});