Ext.define("MB.view.goodsReturnChange.GoodsReturnChangePicSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.goodsReturnChangePicSetModule',
	width: '100%',
	frame: true,
	title:'图片信息&nbsp;&nbsp;&nbsp;',
	head:true,
	layout:'fit',
	margin:10,
    bodyPadding:10,
	buttonAlign : 'center',// 按钮居中
	collapsible:true,
	collapsed: true,
	initComponent: function () {
		this.items = [/*{
			xtype: 'fieldcontainer',
			columnWidth: 1,
			layout : 'column',
			width: '100%',
			items:  [ {
					xtype: 'filefield',  
		            emptyText: '请选择一张图片',  
		            fieldLabel: '图片上传',  
		            name: 'returnChangeImage',  
		            buttonText: '浏览图片', 
		            labelWidth:150,
		            width:500,
		            columnWidth:0.4,
		            buttonCfg: {  
		                        iconCls: 'button-browser'  
		                    }  
				} , {
					xtype : 'button',
					text : '上传',
					style:'margin-left:5px;',
				    width:100,
					action: "uploadImage"
				} , {
					xtype : 'button',
					text : '重置',
					style:'margin-left:5px;',
					width:100,
					action: "resetImage"
				}]
		},*/
		{
			xtype:'panel',
			style:'margin-top:10px;padding:10px;',
			name:'imageList',
			html:""
		}];
		var me = this;
		me.callParent(arguments);
	}
});