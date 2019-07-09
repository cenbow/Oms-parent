Ext.define("MB.view.batchDecode.BatchImportWin", {
	extend: "Ext.window.Window",
	alias: "widget.batchImportWin",
	title: "批量导入页面",
	width: 400,
	height: 250,
	layout: "fit",
	frame : true,
	modal:true,
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
			     {xtype : 'filefield',fieldLabel : "XLS文件 ",name : 'myfile',msgTarget : 'under',allowBlank : false,buttonText : '选择文件',width:350,margin : '20 0 60 10'},
			     {
			    	align : "center",
					frame : true,
					xtype:'container',
					layout : "column",
					style: {
						padding: '10px',
						margin: '20px'
					},
					html:'<a id="importTemplateFile" style="color:red" href = "'+ basePath+'custom/downloadTemp.spmvc?path=' +encodeURI( encodeURI( '/pages/excelModel/联系方式批量解密模板.xls'))+'">下载导入模板文件（简体中文）</a>'
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