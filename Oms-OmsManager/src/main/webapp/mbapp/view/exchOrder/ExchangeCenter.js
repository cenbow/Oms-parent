Ext.define("MB.view.exchOrder.ExchangeCenter", {
	extend: "Ext.form.Panel",
	id:'exchangeCenter',
	alias: 'widget.exchangeCenter',
	//title: '订单详情',
	width: '100%',
	frame: true,
	bodyStyle: {
		padding: '10px'
	},
	autoWidth:true,
	autoHeight:true,
	autoScroll : true,
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'right'
	},
	bodyBorder:true,
	//store: 'Demos',
	initComponent: function () {
		this.items = [
		{
			xtype:'orderDetailForm',
			collapsed: false,
			titleCollapse:true
		}, 
		{xtype:'returnForm'},
		//换单
		{
			xtype:'exchangeForm'
		}
		];
//		//以json形式读取数据
//		this.reader = Ext.create('Ext.data.reader.Json', {
//			rootProperty : "info",
//			model : 'MB.model.Demo'
//		});
		this.callParent(arguments);
	},
	//刷新按钮权限
	refreshButtons : function(rawData) {
		console.dir("refreshButtons-1");
		var buttonStatus = undefined;
		if(rawData != null && rawData != undefined){
			buttonStatus = rawData.buttonStatus;
		}
		Ext.each(Ext.getCmp("exchangeNorth").down('toolbar').items.items,function(item){
			if(item.action != undefined){
				//var role = auth[rolePrefix + item.action];
				var role = true;
				if(buttonStatus == undefined){
					item.disable();
				}else{
					var status = buttonStatus[item.action];
					if(status != undefined && status == '1' && role){
						item.enable();
					}else{
						item.disable();
					}
				}
				if(exchangeOrderSn == ''){
					if(item.action == 'saveExchange' && role){
						item.enable();
					}	
				}
			}
			if(returnSn && 
					item.action == 'sendMessage'
						&& role){
				item.enable();
			}
		});
		console.dir("refreshButtons-2");
	},
	initPage: function(form, action) {
			this.down('orderDetailForm').initData();
		this.down('orderDetailForm').getForm().findField('masterOrderSn').setValue(orderSn);
		//切换换单商品grid绑定的store，要不然原先订单详情的store数据会自动加载
		var store_t = Ext.data.StoreMgr.lookup('ExchangeGoodsStore');
		var grid=Ext.getCmp('exchangeGoodsSetModule').down('goodDetail');
				grid.store = store_t;  
				grid.bindStore(store_t);
		
		//退单编辑按钮
		Ext.getCmp('returnSetModuleG').setHidden(true);//默认隐藏
		Ext.getCmp('returnPaySetModuleG').setHidden(true);
		this.down('returnForm').initData();
		if(exchangeOrderSn){//换单详情
			Ext.getCmp('returnSetModuleG').setHidden(false);
			this.down('exchangeForm').initData();
		}else{
			//换货单申请，加载原订单号
			console.dir(this.down('exchangeForm').getForm().findField('relatingOriginalSn'));
			console.dir(orderSn);
//			this.down('exchangeForm').getForm().findField('relatingOriginalSn').setValue(orderSn);
			this.refreshButtons();
		}
		
	},
	builtParams:function(){
		var params=this.down('exchangeForm').builtExchange();
		if(params == false){
			return false;
		}
		params=this.down('returnForm').builtReturn(params);
		console.dir(params);
		return params;
	}/*,
	submitData:function(){
		var json = {
			url:basePath + '/custom/exchangeorder/saveExchangeButtonClick',
			params : this.builtParams(),
			timeout:90000,
			success : function(formPanel, action) {
				if (action.result.success == "false") {
					errorMsg("结果", action.result.msg);
				} else {
					win.close();
					window.location.reload();
				}
			},
			failure : function(formPanel, action) {
				Ext.msgBox.remainMsg("结果", action.result.errorMessage, Ext.MessageBox.ERROR);
			},
			waitMsg : 'Loading...'
		};
		this.submit(json);
	}*/
});