Ext.define("MB.view.orderDetail.OtherEditWin", {
	extend: "Ext.window.Window",
	alias: "widget.otherEditWin",
	title: "编辑其他信息",
	width: 600,
	height: 400,
	layout: "fit",
	maskDisabled : false,
	modal : true,
	initComponent: function () {
		this.items = [{
			xtype: "form",
			margin: 5,
			border: false,
			frame: true,
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 90
			},
			items: [{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10 30 10 0',
				columnWidth : 1,
				items : [{
					xtype : "textfield",
					name : "masterOrderSn",
					hidden : true,
					fieldLabel : "主单编号"
				},{
					xtype : "textfield",
					fieldLabel : "发票类型",
					name : "invType",
					columnWidth : 1
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10 30 10 0',
				columnWidth : 1,
				items : [{
					xtype : 'textfield',
					fieldLabel : '发票抬头',
					name : "invPayee",
					columnWidth : 1
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10 30 10 0',
				columnWidth : 1,
				items : [{
					xtype : "textfield",
					fieldLabel : "发票内容",
					name : "invContent",
					value : '服饰',
					readOnly : true,
					columnWidth : 1
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10 30 10 0',
				columnWidth : 1,
				items : [{
					xtype : "textfield",
					fieldLabel : "缺货处理",
					name : "howOos",
					columnWidth : 1
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10 30 10 0',
				columnWidth : 1,
				items : [{
					xtype : "textareafield",
					fieldLabel : "客户留言",
					name : "postscript",
					columnWidth : 1
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10 30 10 0',
				columnWidth : 1,
				items : [{
					xtype : "textareafield",
					fieldLabel : "商家留言",
					name : "toBuyer",
					columnWidth : 1
				}]
			}]
		}];
		this.buttons = [
			{ text: "保存", action: "doSaveOtherEdit" },
			{ text: "关闭", handler: 
				function (btn) {
					var win = btn.up("window");
					win.close();
				} 
			}
		];
		this.callParent(arguments);
	}
});