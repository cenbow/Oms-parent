/**
 * 编辑当前区域
 */
Ext.define("MB.view.regionManagement.RegionEdit", {
	extend: "Ext.window.Window",
	alias: "widget.regionEdit",
	title: "编辑区域",
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
			reader: new Ext.data.reader.Json({
				rootProperty : "data",
				model : 'MB.model.RegionModel'
			}),
			items: [
			    { xtype: "textfield", name: "regionId", fieldLabel: "区域ID", readOnly:true},
			    { xtype: "textfield", name: "regionType", fieldLabel: "区域级别", hidden:true},
				{ xtype: "textfield", name: "regionName", fieldLabel: "区域名称", allowBlank: false,blankText:'区域名称不能为空' },
				{ xtype: "textfield", name: "zipCode", fieldLabel: "邮编" },
				{ xtype: "numberfield", name: "shippingFee", fieldLabel: "快递费用",allowDecimals: true,decimalPrecision:2,minValue: 0 },
				{ xtype: "numberfield", name: "emsFee", fieldLabel: "EMS费用",allowDecimals: true,decimalPrecision:2,minValue: 0  },
				{ xtype: "numberfield", name: "codFee", fieldLabel: "货到付款费用",allowDecimals: true,decimalPrecision:2,minValue: 0  },
				{ xtype: 'isCodCombo', name : 'isCod', fieldLabel : '支持货到付款' },
				{ xtype: 'codPosCombo', name: 'codPos' ,fieldLabel: "支持POS刷卡" },
				{ xtype: 'isCacCombo', name: 'isCac' ,fieldLabel: "支持自提" },
				{ xtype: 'isVerifyTelCombo', name: 'isVerifyTel' ,fieldLabel: "支持货到付款验证手机号" }
			]
		};
		this.buttons = [
			{ text: "保存", action: "doSaveEdit" },
			{ text: "关闭", action: "closeEdit" }
		];
		this.callParent(arguments);
	}
});