Ext.define("MB.view.exchOrder.ExchangeGoodsList", {
	extend: "Ext.grid.Panel",
	alias: 'widget.exchangeGoodsList',
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
			{ text: '商品名称', width: 75,align: 'center', dataIndex: 'goodsName' ,sortable:false,menuDisabled : true},
			{ text: '商品属性', width: 75,align: 'center', dataIndex: 'extensionCode' ,sortable:false,menuDisabled : true},
			{ text: '货号', width: 65,align: 'center', dataIndex: 'goodsSn' ,sortable:false,menuDisabled : true},
			{ text: '规格', width: 65,align: 'center', dataIndex: 'goodsColorName+goodsSizeName' ,sortable:false,menuDisabled : true},
//			{ text: '系统SKU码', width: 80,align: 'center', dataIndex: 'skuSn' ,sortable:false,menuDisabled : true},
			{ text: '产品条形码', width: 80,align: 'center', dataIndex: 'barcode' ,sortable:false,menuDisabled : true},
			{ text: '企业SKU码', width: 80,align: 'center', dataIndex: 'customCode',sortable:false,menuDisabled : true },
			{ text: '商品价格', width: 75,align: 'center', dataIndex: 'goodsPrice' ,sortable:false,menuDisabled : true},
			{ text: '会员价格', width: 75,align: 'center', dataIndex: 'goodsPrice',sortable:false,menuDisabled : true },
			{ text: '成交价格', width: 75,align: 'center', dataIndex: 'transactionPrice' ,sortable:false,menuDisabled : true},
			{ text: '财务价格', width: 75,align: 'center', dataIndex: 'settlementPrice' ,sortable:false,menuDisabled : true},
			{ text: '分摊金额', width: 75,align: 'center', dataIndex: 'shareBonus',sortable:false,menuDisabled : true },
			{ text: '商品促销', width: 150,align: 'center', dataIndex: 'promotionDesc' ,sortable:false,menuDisabled : true},
			{ text: '打折卷', width: 75,align: 'center', dataIndex: 'cardMoney' ,sortable:false,menuDisabled : true},
			{ text: '折让金额', width: 75,align: 'center', dataIndex: 'discount' ,sortable:false,menuDisabled : true},
			{ text: '数量', width: 65,align: 'center', dataIndex: 'num' ,sortable:false,menuDisabled : true},
			{ text: '可用数量', width: 75,align: 'center', dataIndex: 'allStock' ,sortable:false,menuDisabled : true},
			{ text: '小计', width: 75,align: 'center', dataIndex: 'actionNote' ,sortable:false,menuDisabled : true}
		];
		this.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		this.callParent(arguments);
	}
});
