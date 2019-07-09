Ext.define("MB.view.abnormalSKUMaintain.AbnSKUMaintainEditWin", {
	extend: "Ext.window.Window",
	alias: "widget.abnSKUMaintainEditWin",
	title: "编辑SKU",
	width: 400,
	height: 300,
	layout: "fit",
	initComponent: function () {
		this.items = {
			xtype: "form",
			margin: 5,
			border: false,
			frame: true,
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 150
			},
			reader: new Ext.data.reader.Json({
				rootProperty : "data",
				model : 'MB.model.AbnormalSKUMaintainModel'
			}),
			items: [
				{ xtype: "textfield",fieldLabel: '外部订单号', name: 'orderOutSn',readOnly : true},
				{ xtype: "textfield",fieldLabel: '11位sku码', name: 'skuSn',allowBlank: false,blankText:'sku码不能为空'},
				{ xtype: "textfield",fieldLabel: '外部商品编码', name: 'outSkuSn',readOnly : true},
				{ xtype: "textfield",fieldLabel: '外部商品名称', name: 'outSkuName',allowBlank: false,blankText:'商品名称不能为空'},
				{ xtype: "textfield",fieldLabel: '渠道类型码', name: 'channelType',readOnly : true},
				{ xtype: "numberfield",fieldLabel: '商品数量', name: 'goodsNum',allowDecimals: false,minValue: 0,allowBlank: false,blankText:'商品数量不能为空'}
			]
		};
		this.buttons = [
			{ text: "保存", action: "doSaveEdit" },
			{ text: "关闭", action: "closeEdit" }
		];
		this.callParent(arguments);
	}
});