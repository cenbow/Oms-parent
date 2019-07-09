Ext.define("MB.view.systemResource.SystemResourceEdit", {
	extend: "Ext.window.Window",
	alias: "widget.systemResourceEdit",
	title: "编辑资源信息",
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
		//	url:  basePath + 'custom/systemResource/getsystemOmsResourceByResourceId.spmvc',
			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 70
			},
			reader: new Ext.data.reader.Json({
				rootProperty : "data",
				model : 'MB.model.SystemResourceModel'
			}),
			items: [
				{ xtype: "textfield", name: "resourceId", fieldLabel: "资源id", hidden: true },
				{ xtype: "textfield", name: "parentId", fieldLabel: "父节点id" },
				{ xtype: "textfield", name: "parentCode", fieldLabel: "父节编码" },
				{ xtype: 'resourceTypeCombo', name: 'resourceType' ,fieldLabel: "类型"  },
//				{ xtype: 'textfield', name: 'channelType' ,fieldLabel: "类型"  }, //对应channel_info 的 channel_type 字段
//				{ xtype: 'textfield', name: 'isChannel' ,fieldLabel: "类型"  },
				{ xtype: "textfield", name: "resourceName", fieldLabel: "资源名称" },
				{ xtype: "textfield", name: "resourceCode", fieldLabel: "资源编码" },
				{ xtype: "textfield", name: "resourceUrl", fieldLabel: "资源地址" },
				{ xtype: "textfield", name: "sortOrder", fieldLabel: "排序号" },
				{ xtype: "textfield", name: "rootId", fieldLabel: "根节点" },
				{ xtype: "isShowType", name: "isShow", fieldLabel: "是否显示" }
		
			]
		};
		this.buttons = [
			{ text: "保存", action: "save" },
			{ text: "关闭", action: "close" }
		];
		this.callParent(arguments);
	},
	initPage: function (form) {
//		var orderStatus = Ext.getCmp("order.orderStatus").getValue();
//		var orderStatus = form.findField("orderStatus").getValue();
	//	form.findField("orderStatus1").setValue(orderStatus);
	}
});