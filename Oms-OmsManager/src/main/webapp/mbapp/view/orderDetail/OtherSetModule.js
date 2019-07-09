Ext.define("MB.view.orderDetail.OtherSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.otherSetModule',
	id: 'otherSetModule',
	width: '100%',
	title:'其他信息',
	margin:5,
	bodyPadding:5,
	title:'其他信息',
	head:true,
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
//	bodyBorder:false,
//	border:false,
//	style:'border-width:0 0 0 0;',
	initComponent: function () {
		var me = this;
		me.items = [ {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield", name : 'invType', fieldLabel : "发票类型", columnWidth: .33},
				{xtype : "displayfield", name : 'invPayee', fieldLabel : "发票抬头", columnWidth: .33 } ,
				{xtype : "displayfield", name : 'invContent', fieldLabel : "发票内容", columnWidth: .33 }
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield", name : 'postscript', fieldLabel : "客户留言", columnWidth: 1 }
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			labelWidth: 200,
			defaultType: 'displayfield',
			items:  [ {
				xtype : "displayfield",
				name : 'howOos',
				fieldLabel : "缺货处理",
				value: '',
				columnWidth: 1
			} ]
		} ];
		me.tools=[ {
			type: 'gear',
			disabled : true,
			tooltip : '修改其他信息',
			action: 'otherEdit',
			scope: me
		}];
		me.callParent(arguments);
	}
});