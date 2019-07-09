Ext.define("MB.view.orderDetail.DeliveryDetail", {
	extend: "Ext.grid.Panel",
	alias: 'widget.deliveryDetail',
	store: Ext.create('Ext.data.Store', {
		model: "MB.model.DeliveryDetailModel"
	}),
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
//	frame: true,
	resizable: true,
	forceFit: true,
	initComponent: function () {
		var me = this;
		me.columns = [
			{ text: '仓库', width: 200,align: 'center', dataIndex: 'depotCode' ,sortable:false,menuDisabled : true},
			{
				text: '承运商',
				width: 200,
				align: 'center',
				dataIndex: 'carriers',
				sortable:false,
				menuDisabled : true,
				renderer: function(value, metaData, record) {
					if (value && value != '') {
						var delivery = record.get("delivery");
						var editDelivery = true;
						if (delivery == 1 && !auth['order_info_shipping']) {
							editDelivery = false;
						}
						var id = Ext.id();
						setTimeout(function() {
							var panel = Ext.create('Ext.panel.Panel', {
								style:'border-width:0 0 0 0;',
								bodyPadding: 0,
								width: 200,
								height: 20,
								layout: 'column',
								columnWidth : 1,
								items : [{
									xtype : 'displayfield',
									columnWidth : .5,
									value : value
								} , {
									columnWidth : .4,
									padding: 0,
									text : '修改',
									margin: '0 0 0 1',
									disabled: editDelivery,
									xtype: 'button',
									buttonConfig: {
										text : '修改'
									},
									handler: function () {
										me.updateDeliveryInfo(record)
									}
								}]
							});
							if (Ext.get(id)) {
								panel.render(Ext.get(id));
							}
						}, 1);
						return '<div id="' + id + '"></div>';
					}
				}
			},
			{ text: '快递单号', width: 200,align: 'center', dataIndex: 'invoiceNo' ,sortable:false,menuDisabled : true},
//			{ text: '商品sku', width: 200,align: 'center', dataIndex: 'custumCode' ,sortable:false,menuDisabled : true},
			{ text: '商品数量', width: 200,align: 'center', dataIndex: 'goodsNumber' ,sortable:false,menuDisabled : true},
			{
				xtype: 'widgetcolumn',
				width: 140,
				sortable:false,
				menuDisabled : true,
				widget: {
					xtype: 'button',
					text: '物流状态查询',
					handler: me.queryExpress
				}
			}
		];
		me.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},
	updateDeliveryInfo : function (record) {
		depotCode = record.get('depotCode');
		var app = Ext.application('MB.orderDeliveryEditApp');
		/*var win = Ext.widget("deliveryEdit");
		var depotCode = record.get('depotCode');
		win.initPage(this.up('orderCenter').down('shipSetModule'), depotCode, win);
		win.show();*/
	},
	queryExpress : function (record) {
		var widgetRecord = record.getWidgetRecord();
		console.dir(widgetRecord);
		depotCode = widgetRecord.get('depotCode');
		invoiceNo = widgetRecord.get('invoiceNo');
		var app = Ext.application('MB.orderExpressApp');
	}
});
