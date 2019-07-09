Ext.define("MB.view.exchOrder.ExchangePayList", {
	extend: "Ext.grid.Panel",
	alias: 'widget.exchangePayList',
//	store: "OrderActions",
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	resizable: true,
	forceFit: true,
//	collapsible: true,
	initComponent: function () {
		this.columns = [
			{ text: '付款单编号', width: 100,align: 'center', dataIndex: 'actionUser' ,sortable:false,menuDisabled : true},
			{ text: '支付方式', width: 100,align: 'center', dataIndex: 'logTime' ,sortable:false,menuDisabled : true},
			{ text: '付款备注', width: 100,align: 'center', dataIndex: 'orderStatus' ,sortable:false,menuDisabled : true},
			{ text: '使用红包', width: 100,align: 'center', dataIndex: 'payStatus' ,sortable:false,menuDisabled : true},
			{ text: '余额支付', width: 100,align: 'center', dataIndex: 'payStatus' ,sortable:false,menuDisabled : true},
			{ text: '付款总金额', width: 100,align: 'center', dataIndex: 'payStatus' ,sortable:false,menuDisabled : true},
			{ text: '支付状态', width: 100,align: 'center', dataIndex: 'payStatus' ,sortable:false,menuDisabled : true},
			{ text: '支付时间', width: 100,align: 'center', dataIndex: 'payStatus' ,sortable:false,menuDisabled : true},
			{ text: '客户支付时间', width: 100,align: 'center', dataIndex: 'payStatus' ,sortable:false,menuDisabled : true},
			{ text: '付款最后期限', width: 100,align: 'center', dataIndex: 'payStatus' ,sortable:false,menuDisabled : true},
			{ text: '操作', width: 100,align: 'center', dataIndex: 'payStatus' ,sortable:false,menuDisabled : true}
		];
		this.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		this.callParent(arguments);
	}
});
