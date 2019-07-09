/**
 * 支付方式管理：添加支付方式
 */
Ext.define("MB.view.paymentManagement.PaymentAdd", {
	extend: "Ext.window.Window",
	alias: "widget.paymentAdd",
	title: "添加支付方式",
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
			    { xtype: "textfield", name: "payCode", fieldLabel: "支付方式编码", allowBlank: false,blankText:'支付方式编码不能为空' },
				{ xtype: "textfield", name: "payName", fieldLabel: "支付方式名称", allowBlank: false,blankText:'支付方式名称不能为空' },
				{ xtype: "numberfield", name: "payFee", fieldLabel: "支付费用",allowDecimals: true,decimalPrecision:2,minValue: 0,value:0,
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
				{ xtype: "numberfield", name: "payOrder", fieldLabel: "显示顺序",allowDecimals: false,minValue: 0,value:0 },
				{ xtype: 'isCodForAddCombo', name : 'isCod', fieldLabel : '支持货到付款', value:'0' },
				{ xtype: 'isOnlineForAddCombo', name : 'isOnline', fieldLabel : '支持在线支付', value:'0' },
				{ xtype: 'isMobileForAddCombo', name : 'isMobile', fieldLabel : '支持手机渠道使用', value:'0' }
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