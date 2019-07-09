/**
 * 承运商管理：编辑承运商
 */
Ext.define("MB.view.shippingManagement.ShippingEdit", {
	extend: "Ext.window.Window",
	alias: "widget.shippingEdit",
	title: "编辑承运商",
	width: 400,
	height: 450,
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
				model : 'MB.model.ShippingQueryModel'
			}),
			items: [
			    { xtype: "textfield", name: "shippingId", fieldLabel: "承运商ID", hidden: true },
			    { xtype: "textfield", name: "shippingCode", fieldLabel: "承运商编码", allowBlank: false,blankText:'承运商编码不能为空' },
				{ xtype: "textfield", name: "shippingName", fieldLabel: "承运商名称", allowBlank: false,blankText:'承运商名称不能为空' },
				{ xtype: "textarea", name: "shippingDesc", fieldLabel: "承运商描述", allowBlank: false,blankText:'承运商描述不能为空',
					preventScrollbars: true ,height:100
				},
				{ xtype: "numberfield", name: "insure", fieldLabel: "保价费用",allowDecimals: true,decimalPrecision:2,minValue: 0,
					listeners: {
			    	 	render: function(obj) {
				    	 	var font=document.createElement("font");
							var label=document.createTextNode('%');
							font.appendChild(label);    
							obj.el.dom.appendChild(font);
						}
			    	}
				},
				{ xtype: 'enabledForAddCombo', name : 'enabled', fieldLabel : '状态' },
				{ xtype: 'supportCodForAddCombo', name : 'supportCod', fieldLabel : '支持货到付款' },
				{ xtype: 'isReceivePrintForAddCombo', name : 'isReceivePrint', fieldLabel : '货到付款模板' },
				{ xtype: 'defalutDeliveryForAddCombo', name : 'defalutDelivery', fieldLabel : '默认配送方式' },
				{ xtype: 'isCommonUseCombo',name:'isCommonUse', fieldLabel : '是否常用'}
			]
		};
		this.buttons = [
			{ text: "保存", action: "doSaveEdit" },
			{ text: "关闭", action: "closeEdit" }
		];
		this.callParent(arguments);
	}
});