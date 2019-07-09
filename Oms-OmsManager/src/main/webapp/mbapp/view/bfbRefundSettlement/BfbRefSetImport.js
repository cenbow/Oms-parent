/**
 * 邦付宝退款批量结算：生成调整单
 */
Ext.define("MB.view.bfbRefundSettlement.BfbRefSetImport", {
	extend: "Ext.window.Window",
	alias: "widget.bfbRefSetImport",
	title: "生成邦付宝退款结算调整单",
	width: 400,
	height: 400,
	layout: "fit",
	frame : true,
	head : true,
	fieldDefaults: {
		labelAlign: 'right'
	},
	initComponent : function() {
		this.items = {
			xtype : "form",
			margin : 5,
			border : false,
			frame : true,
			fieldDefaults : {
				labelAlign : 'right',
				labelWidth : 80
			},
			items : [
			     {xtype : 'textfield',fieldLabel : "渠&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;道 ",value : '邦购',disabled : true,width:300,margin : 5},
			     {xtype : 'textfield',fieldLabel : "业务类型 ",value : '邦付宝退款',disabled : true,width:300,margin : 5},
			     {xtype : 'textfield',fieldLabel : "参数类型",value : '订单号/退单号',disabled : true,width:300,margin : 5},
			     {xtype : 'filefield',fieldLabel : "XLS文件 ",value : '邦购',name : 'myfile',msgTarget : 'under',allowBlank : false,buttonText : '选择文件',width:300,margin : 5},
			     {xtype : "textareafield",name: 'note',fieldLabel : "备注",width : 300,height:120,growMin:40,growMax:100,margin : 5},
			     {
			    	align : "center",
					frame : true,
					xtype:'container',
					layout : "column",
					style: {
						padding: '10px',
						margin: '20px'
					},
					html:'<a id="importTemplateFile" style="color:red" href = "'+ basePath+'custom/downloadTemp.spmvc?path=' +encodeURI( encodeURI( '/pages/excelModel/邦付宝退款结算导入模板.xls'))+'">下载导入模板文件（简体中文）</a>'
				}
			]
		};
		this.buttons = [ {
			text : "导入",
			action : "doImport"
		}, {
			text : "关闭",
			action : "closeImport"
		} ];
		this.callParent(arguments);
	}
});