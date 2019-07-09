/**
 * 订单金额调整：生成调整单
 */
Ext.define("MB.view.orderAmountAdjustment.OrderAmountAdjustmentImport", {
	extend: "Ext.window.Window",
	alias: "widget.orderAmountAdjustmentImport",
	title: "订单金额调整生成调整单",
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
			items : [{
				xtype:'container',
				layout : "column", 
				items : [ {
					margin: '4 0 0 0',
					id: 'uploadFileName',
					xtype : 'filefield',
					name : 'myfile',
					fieldLabel : 'XLS文件',
					labelWidth : 90,
					width : 360,
					msgTarget : 'side',
					allowBlank : false,
					anchor : '100%',
					buttonText : '选择文件'
				},{
					xtype : "textareafield",
					name: 'note',
					fieldLabel : "备注",
					width : 360,
					height:80,
					growMin:40,
					growMax:100,
					margin : 5
				}]
			} , {
				align : "center",
				frame : true,
				xtype:'container',
				style: {
					padding: '5px',
					margin: '10px'
				},
				layout : "column", 
				html: '<div><ul><li>1、数据请去重后再导入!!</li>'+
				'<li>2、请保证“单号”是文本长字符串形式，不要是科学计数法的形式！！</li><li>3、请保证每次批量的数量不超过60000！！</li>' +
				'<li>4、Excel版本为Excel 97-2003,请使用模板的文档作为导入数据文档！！</li><li>5、请按照模板的规定的格式来排列数据！！</li><li>6、订单财务金额只能下调！！</li></ul></div>'
			} , {
				align : "center",
				frame : true,
				xtype:'container',
				layout : "column",
				style: {
					padding: '5px',
					margin: '10px'
				},
				html:'<a id="importTemplateFile" style="color:red" href = "'+basePath+'custom/downloadTemp.spmvc?path='+ encodeURI(encodeURI("/pages/excelModel/订单金额修改导入模板.xls"))+'">下载批量处理模板文件（简体中文）</a>'
			}]
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