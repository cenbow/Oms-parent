/**
 * 支付方式管理：编辑支付方式
 */
Ext.define("MB.view.paymentManagement.PaymentEdit", {
	extend: "Ext.window.Window",
	alias: "widget.paymentEdit",
	title: "编辑支付方式",
	width: 400,
	height: 420,
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
				model : 'MB.model.PaymentModel'
			}),
			items: [
			    { xtype: "textfield", name: "payId", fieldLabel: "支付方式ID", hidden: true },
			    { xtype: "textfield", name: "payCode", fieldLabel: "支付方式编码", allowBlank: false,blankText:'支付方式编码不能为空' },
				{ xtype: "textfield", name: "payName", fieldLabel: "支付方式名称", allowBlank: false,blankText:'支付方式名称不能为空' },
				{ xtype: "numberfield", name: "payFee", fieldLabel: "支付费用",allowDecimals: true,decimalPrecision:2,minValue: 0,
					listeners: {
			    	 	render: function(obj) {
				    	 	var font=document.createElement("font");
							var label=document.createTextNode('%');
							font.appendChild(label);    
							obj.el.dom.appendChild(font);
						}
			    	}
				},
				{ xtype: "textarea", name: "payDesc", fieldLabel: "支付方式描述", allowBlank: false,blankText:'支付方式描述不能为空',
					preventScrollbars: true ,height:100
				},
				{ xtype: "numberfield", name: "payOrder", fieldLabel: "显示顺序",allowDecimals: false,minValue: 0 },
				{ xtype: 'isCodForAddCombo', name : 'isCod', fieldLabel : '支持货到付款' },
				{ xtype: 'isOnlineForAddCombo', name : 'isOnline', fieldLabel : '支持在线支付' },
				{ xtype: 'isMobileForAddCombo', name : 'isMobile', fieldLabel : '支持手机渠道使用' },
				{ xtype: 'enabledForAddCombo', name : 'enabled', fieldLabel : '状态' },
			]
		};
		this.buttons = [
			{ text: "保存", action: "doSaveEdit" },
			{ text: "关闭", action: "closeEdit" }
		];
		this.callParent(arguments);
	}
});