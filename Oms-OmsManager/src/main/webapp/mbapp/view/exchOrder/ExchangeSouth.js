Ext.define("MB.view.exchOrder.ExchangeSouth", {
	extend: "Ext.grid.Panel",
	alias: 'widget.exchangeSouth',
	store: "OrderActions",
	id:'exchangeSouth',
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	resizable: true,
	forceFit: true,
//	collapsible: true,
	initComponent: function () {
		var clientWidth = document.body.clientWidth;
		this.columns = [
			{ text: '操作者', width: 100,align: 'center', dataIndex: 'actionUser' },
			{ text: '操作时间', width: 100,align: 'center', dataIndex: 'logTime' },
			{ text: '订单状态', width: clientWidth * 0.08, align: 'center', dataIndex: 'orderStatus',
				renderer: function (v) {
					return getOrderStatus(v);
				}
			},
			{ text: '付款状态', width: clientWidth * 0.08,align: 'center', dataIndex: 'payStatus',
				renderer: function (v) {
					return getPayStatus(v);
				}
			},
			{ text: '发货状态', width: clientWidth * 0.08,align: 'center', dataIndex: 'shippingStatus',
				renderer: function (v) {
					return getShipStatus(v);
				}
			},
			{ text: '备注', width: 400,align: 'left', dataIndex: 'actionNote' }
		];
		this.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		this.callParent(arguments);
	}
});