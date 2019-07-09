Ext.define("MB.view.orderDetail.ShipEditWin", {
	extend: "Ext.window.Window",
	alias: "widget.shipEditWin",
	title: "编辑收货人信息",
	width: 680,
	height: 380,
	layout: "fit",
	maskDisabled : false,
	modal : true,
	initComponent: function () {
		this.items = [{
			xtype: "form",
			margin: 5,
			border: false,
			frame: true,
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 90
			},
			items: [{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10',
				columnWidth : 1,
				items : [{
					xtype : "textfield",
					name : "masterOrderSn",
					hidden : true,
					fieldLabel : "主单编号"
				},{
					xtype : "textfield",
					fieldLabel : "收货人",
					name : "consignee",
					hiddenName : 'consignee',
					allowBlank: false,
					columnWidth : .6
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10',
				columnWidth : 1,
				items : [{
					xtype : 'displayfield',
					fieldLabel : '所在地区',
					columnWidth : .10
				},{
					xtype : "regionCountryComb",
					hideLabel : true,
					margin : '0 5 0 0',
					name : 'country',
					hiddenName : 'country',
					allowBlank: false,
					columnWidth : .2
				},{
					xtype : "regionProvinceComb",
					hideLabel : true,
					margin : '0 5 0 0',
					name : 'province',
					hiddenName : 'province',
					allowBlank: false,
					columnWidth : .2
				},{
					xtype : "regionCityComb",
					hideLabel : true,
					margin : '0 5 0 0',
					name : 'city',
					hiddenName : 'city',
					allowBlank: false,
					columnWidth : .2
				},{
					xtype : "regionDistrictComb",
					hideLabel : true,
					margin : '0 5 0 0',
					name : 'district',
					hiddenName : 'district',
					allowBlank: false,
					columnWidth : .2
				}
				/*,
				{
					xtype : "regionStreetComb",
					hideLabel : true,
					margin : '0 5 0 0',
					name : 'street',
					hidden : true,
					hiddenName : 'street',
					columnWidth : .16
				}*/
				]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10',
				columnWidth : 1,
				items : [{
					xtype : "textfield",
					fieldLabel : "地址",
					name : "address",
					hiddenName : 'address',
					allowBlank: false,
					columnWidth : 1
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10',
				columnWidth : 1,
				items : [{
					xtype : "textfield",
					fieldLabel : "邮编",
					name : "zipcode",
					hiddenName : 'zipcode',
					columnWidth : .5
				},{
					xtype : "textfield",
					fieldLabel : "邮箱",
					name : "email",
					hiddenName : 'email',
					columnWidth : .5
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10',
				columnWidth : 1,
				items : [{
					xtype : "textfield",
					fieldLabel : "手机",
					name : "mobile",
					hiddenName : 'mobile',
//					value : '******',
					columnWidth : .4
				},{
					xtype : "displayfield",
					value : '（电话、手机至少选一）',
					columnWidth: .25
				},{
					xtype : "displayfield",
					name : 'encodeMobile',
					columnWidth: .2
				},{
					xtype : "hidden",
					name : 'oldMobile'
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10',
				columnWidth : 1,
				items : [{
					xtype : "textfield",
					fieldLabel : "电话",
					name : "tel",
					hiddenName : 'tel',
//					value : '******',
					columnWidth : .4
				},{
					xtype : "displayfield",
					value : '（电话、手机至少选一）',
					columnWidth: .25
				},{
					xtype : "displayfield",
					name : 'encodeTel',
					columnWidth: .2
				}
				/*,
				{
					xtype : "hidden",
					name : 'oldTel'
				}
				,
				{
					xtype : 'button',
					text : '解密',
					columnWidth: .1,
					handler : this.decodeLinkMobile
				}*/
				]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10',
				columnWidth : 1,
				items : [{
					xtype : "textfield",
					fieldLabel : "标志性建筑",
					name : "signBuilding",
					hiddenName : 'signBuilding',
					columnWidth : 1
				}]
			},{
				xtype : 'fieldcontainer',
				layout : 'column',
				margin: '10',	
				columnWidth : 1,
				items : [{
					xtype : "textfield",
					fieldLabel : "最佳送货时间",
					name : "bestTime",
					hiddenName : 'bestTime',
					columnWidth : 1
				}]
			}]
		}];
		this.buttons = [
			{ text: "保存", action: "doSaveAddrEdit" },
			{ text: "关闭", handler: 
				function (btn) {
					var win = btn.up("window");
					win.close();
				} 
			}
		];
		this.callParent(arguments);
	},
	decodeLinkMobile : function(btn){
		var form = btn.up('form');
		var oldMobile = form.getForm().findField("oldMobile").getValue();
		var oldTel = form.getForm().findField("oldTel").getValue();
		Ext.Ajax.request({
			url:  basePath + 'custom/commonLog/decodeLinkMobile',
			params: {tel : oldTel, mobile : oldMobile, masterOrderSn: masterOrderSn},
			success: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				if (respText.code=='1') {
					form.getForm().findField("encodeMobile").setValue(respText.mobile);
					form.getForm().findField("encodeTel").setValue(respText.tel);
				} else {
					Ext.msgBox.remainMsg('解密', respText.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var respText = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('解密', respText.msg, Ext.MessageBox.ERROR);
			}
		});
	}
});