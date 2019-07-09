Ext.define("MB.view.goodsReturnChange.GoodsReturnChangeSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.goodsReturnChangeSetModule',
	id:'goodsReturnChangeSetModule',
	title:'退换货申请信息',
	width: '100%',
//	frame: true,
	head:true,
	margin:5,
	bodyPadding:5,
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
	initComponent: function () {
		var me = this;
		this.items = [ {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{
				xtype: 'fieldcontainer',
				labelStyle: 'font-weight:bold;padding:0;',
				layout: 'column',
				columnWidth: .33,
				defaultType: 'displayfield',
				labelWidth: 200,
				items: [
					{xtype : "goodsReturnChangeStatus",name:"status",columnWidth:.55,labelWidth:100 },
					{xtype : "displayfield", columnWidth: .07} ,
					{xtype : "button", name : 'update', text: "更新",columnWidth: .15,handler : me.updateStatus} ,
					{xtype : "displayfield", columnWidth: .03} ,
					{xtype : "button", name : 'updateAll', text : "全部更新",columnWidth: .2,handler : me.updateStatus} 
				]
				},
				{xtype : "displayfield", name : 'relatingOrderSn', fieldLabel : "关联订单号", columnWidth: .33},
				{xtype : "displayfield", name : 'contactName', fieldLabel : "联系人", columnWidth: .33} 
			]
		} ,
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "displayfield", name : 'contactMobile', fieldLabel : "手机", columnWidth: .33 },
				{xtype : "displayfield", name : 'contactTelephone', fieldLabel : "固定电话", columnWidth: .33} 
			]
		} ,
		{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "textareafield", name : 'actionNote', fieldLabel : "操作备注", columnWidth: .76 },
				{xtype : "displayfield", columnWidth: .01},
				{xtype : "button", name : 'submitNote', text: "提交备注",columnWidth: .06,handler : me.updateStatus}
			]
		}
		];
		me.callParent(arguments);
	},
	updateStatus: function (update) {
		var param={};
		param.id=goodsReturnChangeId;
		param.orderSn=orderSn;
		param.isHistory=isHistory;
		if (update.name=='update') {
			param.type=1;
		}else if(update.name=='updateAll'){
			param.type=0;
		}else if(update.name=='submitNote'){
			param.type=2;
		}
		param.actionNote=this.up('form').getForm().findField('actionNote').getValue();
		param.status=this.up('form').getForm().findField('status').getValue();
		console.dir(param);
		Ext.Ajax.request({
			url:  basePath + 'custom/goodsReturnChange/updateProcessStatus.spmvc',
			params: param,
			success: function(response){
				var text = response.responseText;
//				form.getForm().findField("encodeTel").setValue(text);
				var result = Ext.JSON.decode(text);
				console.dir(result);
			},
			failure: function(response){
				var text = response.responseText;
				console.dir('failure:' + text);
			}
		});
	}
});