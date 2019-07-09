Ext.define("MB.view.refund.RefundContent", {
	extend: "Ext.form.Panel",
	alias: 'widget.refundContent',
	requires: ['MB.view.common.ChannelTypeCombo','MB.view.common.ChannelInfoCombo','MB.view.common.ChannelShopCombo','MB.view.common.HaveRefund'],
	id:"refundContent",
	width: '100%',
	frame: true,
	title:'退单结算列表&nbsp;&nbsp;&nbsp;',
	head:true,
	margin:10,
    bodyPadding:10,
    layout: {
			type: 'vbox',
			align: 'stretch'
		},
	buttonAlign : 'right',// 按钮居右
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
	//collapsed: true,
	initComponent: function () {
	
	var me=this;
		this.items = [
			{
			
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [{// line 1
					width : 220, // 该列有整行中所占百分比
					layout : "form", // 从上往下的布局
					xtype : "textfield",
					id:'orderReturnPageReturnSn',
					name: 'relatingReturnSn',
					fieldLabel : "退单号",
					labelWidth: 80
				} , {
					width : 220,
					layout : "form",
					xtype : 'textfield',
					fieldLabel : '关联订单号',
					id:'orderReturnPageRelatingOrderSn',
					name: 'relatingOrderSn',
					labelWidth: 80
				} , {
					width : 220,
					labelWidth: 80,
					layout : "form",
					xtype : 'textfield',
					fieldLabel : '外部交易号',
					id:'orderReturnPageOrderOutSn',
					name: 'orderOutSn'
				}
				, {
					width : 220,
					labelWidth: 80,
					layout : "form",
					name:'returnType',
					xtype : 'commonreturnType'
				}
			]
		
			}
			, { //line 2
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [{
				xtype : 'channeltypecombo',
				name:'orderFromFirst',
				columnWidth : .2,
				labelWidth:80,
				width : 220,
				allowBlank : false,
				listeners:{
					'change': me.changeChannelType
				}
			} , {
				xtype : 'channelinfocombo',
				columnWidth : .2,
				name:'orderFromSec',
				labelWidth:80,
				width : 220,
				allowBlank : false,
				listeners:{
					'change': me.changeChannelCode
				}
			} , {
				xtype : 'channelshopcombo',
				name:'orderFroms',
				columnWidth : .4,
				labelWidth:80,
				width : 440,
				allowBlank : false
			}/*,{xtype:'displayfield',width:40},
			{
				id : 'isHistory',
				width : 220,
				xtype: 'radiogroup',
				name:'isHistory',
				items: [
					{boxLabel: '近三个月数据', name: 'isHistory', inputValue:'0', checked: true},
					{boxLabel: '历史数据', name: 'isHistory', inputValue:'1'}
				]
			}*/
			]
		},
		 { //line 3
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [
			{
					width : 220,
					labelWidth: 80,
					layout : "form",
					name:'returnPay',
					xtype : 'commonsystemPayment'
				},
			{
					width : 220,
					labelWidth: 80,
					layout : "form",
					name:'returnPayStatus',
					xtype : 'commonreturnPayStatus'
				},
			{
					width : 220,
					labelWidth: 80,
					layout : "form",
					fieldLabel : "是否退款",
					name:'haveRefund',
					xtype : 'commonhaverefund'
				},
			{xtype:'displayfield',width:40},
			{
				id : 'isHistory',
				width : 220,
				xtype: 'radiogroup',
				name:'isHistory',
				items: [
					{boxLabel: '近三个月数据', name: 'isHistory', inputValue:'0', checked: true},
					{boxLabel: '历史数据', name: 'isHistory', inputValue:'1'}
				]
			}
			]
			},
			{ //line 4
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},
			items: [
				{
					xtype : "combobox",
					store : Ext.create('Ext.data.Store', {
								model : "MB.model.CommonStatusModel",
								data : [['1', "已退"], ['0', '未退']]
							}),

					name : 'backbalance',
					displayField : 'name',
					valueField : 'id',
					queryMode : 'local',
					// fieldLabel: '支付方式',
					hiddenName : 'id',
					emptyText : '请选择',
					editable : false,
					fieldLabel : "退邦购币",
					width : 220,
					labelWidth: 80
				}
			]
			}
			
		
			];
			this.buttons =[{
			text : '查询',
			columnWidth : .1,
			action : 'searchAction'
		} , {
			text : '重置',
			handler : function (btn,a,b) {
				btn.up('form').form.reset();
			}
		}, {
			id: "exportOrderRefund",
			text : '导出',
			action:'exportOrderRefund'
			//handler : exportRecord
		}]
		var me = this;
        me.callParent(arguments);
	},
	exportOrderRefund:function(){
		var searchParams = this.getForm().getValues();
		var grid=Ext.getCmp('refundGrid');
		var url = basePath+'/custom/orderReturn/orderRefundExportCsv';
	
		Ext.getCmp('exportOrderRefund').setDisabled(true);
		
		var myMask = new Ext.LoadMask({
		    msg    : '请稍等,正在导出...',
		    target : this
		});
		
		myMask.show();
		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : url,
			method : 'post',
			timeout : 7200000,
			 method : 'post',
			params : searchParams,
			success : function(response) {
				if (myMask != undefined){ 
					myMask.hide();
				}
				var obj = Ext.decode(response.responseText);
				var path = obj.data.path;
				var fileName = obj.data.fileName;
				
				if(obj.isok==true){
					window.location.href=basePath+"custom/downloadFtpFile.spmvc?path="+path+"&fileName="+fileName;
				}else{
					Ext.Msg.alert("错误", "导出失败");
				}
				Ext.getCmp('exportOrderRefund').setDisabled(false);
			},
			failure : function(response) {
				myMask.hide();
				Ext.Msg.alert("验证", "失败");
				Ext.getCmp('exportOrderRefund').setDisabled(false);
			}
		});
	},
	search:function(){
		var pageSize=15;
		var searchParams = {start : 0, limit : pageSize };
		var grid=Ext.getCmp('refundGrid');
		grid.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		//查询翻页后带入查询数据
		var beforeParam=getFormParams(Ext.getCmp('refundContent'));
		grid.store.on('beforeload', function (store, options){
			Ext.apply(store.proxy.extraParams, beforeParam);
		});
		var storeParam=getFormParams(Ext.getCmp('refundContent'), searchParams);
		grid.store.load({params : storeParam});
		grid.doLayout();
		console.dir(grid.store);
	},
	changeChannelCode : function (combo, newValue ,oldValue) {
		var channelShopCombo = this.up('panel').down('channelshopcombo');
		channelShopCombo.clearValue();
		channelShopCombo.store.on('beforeload', function (store){
			var params = {"channelCode" : newValue};
			Ext.apply(store.proxy.extraParams, params);
		});
		channelShopCombo.store.load();
	},
	changeChannelType: function (combo, newValue ,oldValue) {
		var channelInfoCombo = this.up('panel').down('channelinfocombo');
		channelInfoCombo.clearValue();
		channelInfoCombo.store.on('beforeload', function (store){
			var params = {"channelType" : newValue};
			Ext.apply(store.proxy.extraParams, params);
		});
		channelInfoCombo.store.load();
	}
	
});