Ext.define("MB.view.handOrder.HandOrder", {
	extend: "Ext.window.Window",
	alias: "widget.handOrder",
	title: "批量导入页面",
	width: 400,
	height: 400,
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
				{xtype : 'onlineChannelShopCombo',fieldLabel : "选择店铺 ",name : 'channelCode',allowBlank : false,width : 300,margin : 10},
				{xtype : 'sourceTypeCombo',fieldLabel : "打单类型 ",name : 'sourceType',allowBlank : false,width : 300,margin : 10},
				{xtype : 'filefield',fieldLabel : "CSV文件 ",name : 'myfile',msgTarget : 'under',allowBlank : false,buttonText : '选择文件',width:300,margin : 10},
				{xtype : "textareafield",name: 'note',fieldLabel : "备注",width : 300,height:120,growMin:40,growMax:100, margin : 10,hidden:true},
				{
					align : "center",
					frame : true,
					xtype:'container',
					layout : "column",
					style: {
						padding: '10px',
						margin: '20px'
					},
					html:'<a id="importTemplateFile" style="color:red" href = "'+ basePath+'custom/downloadTemp.spmvc?path=' +encodeURI( encodeURI( '/pages/excelModel/赠品打单模板.csv'))+'">下载导入模板文件（简体中文）</a>'
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