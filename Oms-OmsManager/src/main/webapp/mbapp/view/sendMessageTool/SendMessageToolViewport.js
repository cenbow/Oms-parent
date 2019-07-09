Ext.define("MB.view.sendMessageTool.SendMessageToolViewport",{
	extend : "Ext.container.Viewport",
	alias : 'widget.sendMessageToolViewport',
	layout : "border",
	defaults : {
		collapsible : false,
		split : true
	},
	initComponent : function() {
		var clientHeight = document.body.clientHeight;
		var clientWidth = document.body.clientWidth;
		this.items = [{
			xtype: 'form',
			width: clientWidth,
			height: clientHeight,
			titleCollapse:false,
			labelWidth: 100,
			labelAlign: 'right',
			defaults: {
				columnWidth: 1
			},
			items : [ {
				xtype: 'fieldcontainer',
				layout: 'column',
				height:clientHeight*0.2
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{xtype : "displayfield", columnWidth: .20},
					{xtype : "textfield", name : 'mobile', fieldLabel : "联系方式", columnWidth: .50,allowBlank : false},
					{xtype : "displayfield", columnWidth: .30}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{xtype : "displayfield", columnWidth: .20},
					{	xtype : 'combo',
						fieldLabel: '短信通道',
						store : new Ext.data.Store({
							fields: ['n', 'v'],
							data:[['邦购','bg'],['美邦/全网','mb'],['有范','yf']]
						}),
						queryMode: 'local',
						displayField: 'n',
						valueField: 'v',
						name : 'sendType',
						hiddenName: 'sendType',
						value : 'bg',
						columnWidth: .50, 
						editable: false
					},
					{xtype : "displayfield", columnWidth: .30}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{xtype : "displayfield", columnWidth: .20},
					{xtype : "textareafield", name : 'message', fieldLabel : "短信内容", columnWidth: .50,grow : true,
						allowBlank : false,
						height : 150},
					{xtype : "displayfield", columnWidth: .30}
				]
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				height:clientHeight*0.05
			},{
				xtype: 'fieldcontainer',
				layout: 'column',
				items: [
					{xtype : "displayfield", columnWidth: .40},
					{xtype : "button", text:'发送',columnWidth: .10,action:'doSend'},
					{xtype : "displayfield", columnWidth: .04},
					{xtype : "button", text:'重置',columnWidth: .10,action:'reset'},
					{xtype : "displayfield", columnWidth: .36}
				]
			}]
		}];
		this.callParent(arguments);
	}

});