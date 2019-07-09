var pageSize = 20;
Ext.define("MB.view.systemResource.SystemResourcePanlView", {
	extend : "Ext.form.Panel",
	alias : 'widget.systemResourcePanlView',
	frame : true,
	head : true,
	buttonAlign : 'center',// 按钮居中
	fieldDefaults : {
		labelAlign : 'right'
	},
	collapsible : true,
	initComponent : function() {
		var me = this;
		me.items = [ {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 80,
			margin: '10 0 0 0',
			items: [ {
				xtype : 'textfield',
				id : 'systemResourcePanlViewResourceName',
				name : 'resourceName',
				fieldLabel : "<b>资源名称 </b>",
				width : 200,
				columnWidth : 0.3
			} , {
				xtype : 'textfield',
				id : 'systemResourcePanlViewResourceCode',
				name : 'resourceCode',
				fieldLabel : "<b>资源编码 </b>",
				width : 200,
				columnWidth : 0.3
			} , {
				xtype : 'resourceTypeCombo',
				id : 'systemResourcePanlViewResourceType',
				name : 'resourceType',
				width : 200,
				columnWidth : 0.3
			} ]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 80,
			margin: '10 0 0 0',
			items: [ {
				xtype : 'textfield',
				id : 'systemResourcePanlViewparentName',
				name : 'parentName',
				fieldLabel : "<b>父类资源名称 </b>",
				width : 200,
				columnWidth : 0.3
			} , {
				xtype : 'textfield',
				id : 'systemResourcePanlViewparentCode',
				name : 'parentCode',
				fieldLabel : "<b>父类资源编码 </b>",
				width : 200,
				columnWidth : 0.3
			} , {
				xtype : 'isShowType',
				id : 'systemResourcePanlViewisShowType',
				name : 'isShow',
				width : 200,
				columnWidth : 0.3
			}]
		
		}];
		me.buttons = [ {
			text : '添加系统资源',
			action : 'add'
		}, {
			text : '查询',
			handler : me.search
		}, {
			text : '重置',
			action : 'reset'
		} ];
		me.callParent(arguments);
	},
	search: function (btn) {
		var pageSize = 20;
		var form = btn.up('form');//当前按钮form	
		var searchParams = getFormParams(form, null);
		console.dir(searchParams);
		var systemResourceGridOrderList = form.up('viewport').down("systemResourceGridView");
		systemResourceGridOrderList.store.on('beforeload', function (store, options){
			Ext.apply(store.proxy.extraParams, searchParams);
		});
		systemResourceGridOrderList.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		systemResourceGridOrderList.store.pageSize = pageSize;
		systemResourceGridOrderList.store.load({params : searchParams});
	}
});

