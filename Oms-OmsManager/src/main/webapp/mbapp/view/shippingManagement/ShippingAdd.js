/**
 * 承运商管理：添加承运商
 */
Ext.define("MB.view.shippingManagement.ShippingAdd", {
	extend: "Ext.window.Window",
	alias: "widget.shippingAdd",
	title: "添加承运商",
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
			    { xtype: "textfield", name: "shippingCode", fieldLabel: "承运商编码", allowBlank: false,blankText:'承运商编码不能为空' },
				{ xtype: "textfield", name: "shippingName", fieldLabel: "承运商名称", allowBlank: false,blankText:'承运商名称不能为空' },
				{ xtype: "textarea", name: "shippingDesc", fieldLabel: "承运商描述", allowBlank: false,blankText:'承运商描述不能为空',
					preventScrollbars: true ,height:100
				},
				{ xtype: "numberfield", name: "insure", fieldLabel: "保价费用",allowDecimals: true,decimalPrecision:2,minValue: 0,value:0,
					listeners: {
			    	 	render: function(obj) {
				    	 	var font=document.createElement("font");
							var label=document.createTextNode('%');
							font.appendChild(label);    
							obj.el.dom.appendChild(font);
						}
			    	}
				},
				{ xtype: 'supportCodForAddCombo', name : 'supportCod', fieldLabel : '支持货到付款', value:'0' },
				{ xtype: 'isReceivePrintForAddCombo', name : 'isReceivePrint', fieldLabel : '货到付款模板', value:'0' },
				{ xtype: 'defalutDeliveryForAddCombo', name : 'defalutDelivery', fieldLabel : '默认配送方式', value:'0' },
				{ xtype: 'isCommonUseCombo',name:'isCommonUse', fieldLabel : '是否常用',value:'0'}
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