Ext.define("MB.view.orderDetail.ShipSetModule", {
	extend: "Ext.form.Panel",
	alias: 'widget.shipSetModule',
	id:'shipSetModule',
	title: '收货人信息',
	width: '100%',
//	frame: true,
	head:true,
	margin:5,
	bodyPadding:5,
	buttonAlign : 'center',// 按钮居中
	fieldDefaults: {
		labelAlign: 'right'
	},
	collapsible:true,
//	collapsed: true,
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
				{xtype : "displayfield", name : 'consignee', fieldLabel : "收货人", columnWidth: .33 },
				{xtype : "displayfield", name : 'mobile', fieldLabel : "手机", columnWidth: .33} ,
//				{xtype : "hidden", name : 'mobile', fieldLabel : "手机"} ,
				{xtype : "displayfield", name : 'tel', fieldLabel : "电话", columnWidth: .33 },
//				{xtype : "hidden", name : 'tel', fieldLabel : "电话" },
				/*{
					itemId : 'decodeLinkMobile',
					xtype : 'button',
					text : '解密',
					columnWidth: .05,
					handler : me.decodeLinkMobile
				}*/
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{xtype : "hidden", name : 'country', fieldLabel : "国家"},
				{xtype : "hidden", name : 'province', fieldLabel : "省" },
				{xtype : "hidden", name : 'city', fieldLabel : "市" },
				{xtype : "hidden", name : 'district', fieldLabel : "区" },
				{xtype : "hidden", name : 'street', fieldLabel : "街道" },
				{xtype : "hidden", name : 'address', fieldLabel : "地址" },
				{xtype : "hidden", name : 'transType', fieldLabel : "交易类型" },
				{xtype : "displayfield", name : 'fullAddress', fieldLabel : "详细地址",columnWidth: .66},
				{xtype : "displayfield", name : 'zipcode', fieldLabel : "邮编", columnWidth: .33 }
			]
		} , {
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{
					xtype : "displayfield",
					name : 'email',
					fieldLabel : "电子邮件",
					columnWidth: .33 
				} , {
					xtype : "displayfield",
					name : 'signBuilding',
					fieldLabel : "标志性建筑",
					columnWidth: .33
				} , {
					xtype : "displayfield",
					name : 'bestTime',
					fieldLabel : "最佳送货时间",
					columnWidth: .33
				} 
			]
		}/*, {
			xtype: 'panel',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			layout: {
		        type: 'hbox',
		        pack: 'center',
		        align: 'right'             //对齐方式 top、middle、bottom：顶对齐、居中、底对齐；stretch：延伸；stretchmax：以最大的元素为标准延伸
		    },
//			labelWidth: 200,
			dockedItems: [{
		        xtype: 'toolbar',
		        dock: 'top',
		        items: [{
				text : '解密',
//				tooltip : '解密',
//				margin: '1 5 1 15',
				//iconCls : 'plugin',
				columnWidth: .5,
				action: 'process'
			},{
				text : '刷新',
//				tooltip : '刷新',
//				margin: '1 5 1 15',
				columnWidth: .5,
				action: 'process'
			} ]
		    }]
			tbar: [
				{ 
				xtype: 'button',
				text: '解密' ,
//				margin: '60 5 1 15',
				allowDepress: true,     //是否允许按钮被按下的状态
    			enableToggle: true     //是否允许按钮在弹起和按下两种状态中切换
				},
		  		{ xtype: 'button', text: '刷新', marginLeft : '10' }//上面dockedItems的缩写形式
			]
		} */
		/*{
			xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'column',
			columnWidth: 1,
			defaultType: 'displayfield',
			labelWidth: 200,
			items: [
				{
				xtype : "tool",
				type:'refresh',
				tooltip : '刷新当前模块',
				align:'right'
			}
			]
		}*/
		];
		me.tools=[ {
				type: 'gear',
				tooltip : '修改收货人信息',
				disabled : true,
				action: 'shipEdit',
				//handler: me.onCloseClick,
				scope: me
			}];
		//以json形式读取数据
		me.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "result",
			model : 'MB.model.OrderDetailModel'
		});
		me.callParent(arguments);
	},
	decodeLinkMobile : function (btn) {
		var form = btn.up('form');
		var tel = form.getForm().findField("tel").getValue();
		Ext.Ajax.request({
			url:  basePath + 'custom/common/decodeLinkMobile',
			params: {mobile : tel, orderSn: orderSn},
			success: function(response){
				var text = response.responseText;
//				form.getForm().findField("encodeTel").setValue(text);
				var result = Ext.JSON.decode(text);
				console.dir(result);
				if (result && result.isok) {
					form.getForm().findField("encodeTel").setValue(result.message);
				} else {
					Ext.msgBox.remainMsg('解密', results.message, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var text = response.responseText;
				console.dir('failure:' + text);
			}
		});
		
		var oldMobile = form.getForm().findField("mobile").getValue();
		Ext.Ajax.request({
			url:  basePath + 'custom/common/decodeLinkMobile',
			params: {mobile : oldMobile, orderSn: orderSn},
			success: function(response){
				var text = response.responseText;
//				form.getForm().findField("encodeMobile").setValue(text);
				var result = Ext.JSON.decode(text);
				if (result && result.isok) {
					form.getForm().findField("encodeMobile").setValue(result.message);
				}
			},
			failure: function(response){
				var text = response.responseText;
				console.dir('failure:' + text);
			}
		});
	}
});