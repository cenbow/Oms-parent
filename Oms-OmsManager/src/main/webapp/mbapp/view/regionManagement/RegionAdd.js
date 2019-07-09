/**
 * 添加下级区域窗口
 */
Ext.define("MB.view.regionManagement.RegionAdd", {
	extend: "Ext.window.Window",
	alias: "widget.regionAdd",
	title: "添加下级区域",
	width: 400,
	height: 400,
	layout: "fit",
	maskDisabled : false,
	modal : true,
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
			items: [
			    { xtype: "textfield", name: "regionId", fieldLabel: "区域ID", allowBlank: false,blankText:'区域ID不能为空' },
				{ xtype: "textfield", name: "regionName", fieldLabel: "区域名称", allowBlank: false,blankText:'区域名称不能为空' },
				{ xtype: "textfield", name: "zipCode", fieldLabel: "邮编" },
				{ xtype: "numberfield", name: "shippingFee", fieldLabel: "快递费用",allowDecimals: true,decimalPrecision:2,minValue: 0,value:0 },
				{ xtype: "numberfield", name: "emsFee", fieldLabel: "EMS费用",allowDecimals: true,decimalPrecision:2,minValue: 0,value:0  },
				{ xtype: "numberfield", name: "codFee", fieldLabel: "货到付款费用",allowDecimals: true,decimalPrecision:2,minValue: 0,value:0  },
				{ xtype: 'isCodCombo', name : 'isCod', fieldLabel : '支持货到付款', value:'0' },
				{ xtype: 'codPosCombo', name: 'codPos' ,fieldLabel: "支持POS刷卡", value:'0' },
				{ xtype: 'isCacCombo', name: 'isCac' ,fieldLabel: "支持自提", value:'0' },
				{ xtype: 'isVerifyTelCombo', name: 'isVerifyTel' ,fieldLabel: "支持货到付款验证手机号", value:'0' }
			]
		};
		this.buttons = [
			{ text: "新增", action: "doAdd" },
			{ text: "重置", action: "resetAdd" },
			{ text: "关闭", action: "closeAdd" }
		];
		this.callParent(arguments);
	}
});