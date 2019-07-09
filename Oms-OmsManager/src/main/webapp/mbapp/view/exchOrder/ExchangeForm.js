Ext.define("MB.view.exchOrder.ExchangeForm", {
	extend: "Ext.form.Panel",
	id:'exchangeForm',
	alias: 'widget.exchangeForm',
	title: "<span style='color:black;font-weight:bold';>换货单信息</span>",
	width: '100%',
	frame: true,
	autoWidth:true,
	autoHeight:true,
	autoScroll : false,
	collapsible:true,
	titleCollapse:true,
	bodyStyle: {
		padding: '10px'
	},
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'right'
	},
	bodyBorder:true,
	//store: 'Demos',
	initComponent: function () {
		this.items = [
		//换单
		{
			xtype:'exchangeSetModule'
		}, {
			xtype:'exchangeGoodsSetModule',
			collapsed: false,//初始不展开
			titleCollapse:true
		}, {
			xtype:'exchangePaySetModule'
		}
		];
		//以json形式读取数据
		this.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "result",
			model : 'MB.model.ExchangeDetailModel'
		});
		this.callParent(arguments);
	},
	initData: function() {
		
		/*//给grid添加复选框，同时添加选中事件
		grid.selModel=Ext.getCmp('returnGoodsList').selModel;
		grid.addListener(
		'selectionchange',function(sm,rowIndex,record){
	      	console.dir(sm);
	      	console.dir(rowIndex);
	      }
		);
		grid.doLayout();
		console.dir(grid);*/
		var me = this;
		this.load({
			url : basePath + '/custom/exchangeorder/getExchangeOrderDetail',
			params : {
				"exchangeOrderSn" : exchangeOrderSn,
				"isHistory": isHistory
			},
			timeout:90000,
			success : function(opForm, action) {
				// 数据加载成功后操作
			/*	console.dir(opForm.reader.rawData.result.orderStatus);
				if(opForm.reader.rawData.result.orderStatus){
					me.getForm().findField('orderStatus').setValue(opForm.reader.rawData.result.orderStatus);
				}*/
//				var store_t = Ext.data.StoreMgr.lookup('ExchangeGoodsStore');  
				var grid=Ext.getCmp('exchangeGoodsSetModule').down('goodDetail');
//				grid.store = store_t;  
//				grid.bindStore(store_t);//切换绑定的store，要不然原先订单详情的store数据会自动加载
				var masterOrderInfoData = opForm.reader.rawData.result;
				setTimeout(function(){
					Ext.getCmp('exchangeSetModule').getForm().findField('ordertotalstatusStr').setValue(getCombineStatus(masterOrderInfoData.orderStatus,masterOrderInfoData.payStatus,masterOrderInfoData.shipStatus));//订单状态
				}, 1);
				if(opForm.reader.rawData){
					opForm.owner.up('exchangeCenter').refreshButtons(opForm.reader.rawData);					
				}
				if(opForm.reader.rawData.goodDetail){
					grid.store.loadData(opForm.reader.rawData.goodDetail);
					var goodDetail=opForm.reader.rawData.goodDetail;
					var len = goodDetail.length;
					var total=0;
					for(var i=0;i<len;i++){
						total+=goodDetail[i].transactionPrice;
					}
					//总计
					Ext.getCmp('exchangeGoodsSetModule').getForm().findField('total').setValue(total);
				}
//				opForm.owner.up('exchangeCenter').submitData();
				
				//换单支付方式
				if(exchangeOrderSn){
					if(opForm.reader.rawData.payDetail){
						if(opForm.reader.rawData.payDetail.length>0)
						var payIdValue = opForm.reader.rawData.payDetail[0].payId;
						opForm.owner.up('exchangeCenter').getForm().findField('payMent').setValue(payIdValue);
					}
				}
				me.disablePage();
			},
			failure : function(opForm, action) {
				// 数据加载失败后操作
//				alert(2222);
			}
		});
		
		//换单日志延迟加载
		var exchangeAction = Ext.getCmp('exchangeSouth');
		exchangeAction.collapse();
		exchangeAction.addListener('expand',function(){
			exchangeAction.store.on('beforeload', function (store){
				params = {"orderSn" : exchangeOrderSn};
				Ext.apply(store.proxy.extraParams, params);
			});
			exchangeAction.getStore().load();
		});
	},
	//换单申请-根据原订单加载换单数据
	initApplyPage: function(data) {
		if(data.orderSn){
			this.getForm().findField('relatingOriginalSn').setValue(data.orderSn);
		}
		if(data.referer){
			this.getForm().findField('referer').setValue(data.referer);
		}
		if(data.userName){
			this.getForm().findField('userName').setValue(data.userName);
		}
		//是否代理发货初始化为否
		this.getForm().findField('isAgent').setValue(0);
		
		//申请状态禁用按钮
		this.up('exchangeCenter').refreshButtons();
	},
	//非申请页面禁用组件
	disablePage : function() {
		if(returnSn == ''){
			return;
		}
		var exchangeCenter = this.getForm();
		exchangeCenter.findField("payMent").readOnly = true;
		exchangeCenter.findField("isAgent").readOnly = true;
		exchangeCenter.findField("goodsAmount").setReadOnly(true);
		exchangeCenter.findField("discount").setReadOnly(true);
		exchangeCenter.findField("shippingTotalFee").setReadOnly(true);
		exchangeCenter.findField("bonus").setReadOnly(true);
	},
	builtExchange:function(){//构建换单保存的参数
		var params=this.getValues();
		params.relatingOrderSn=this.getForm().findField('relatingOriginalSn').getValue();
		params['pageOrder.orderSn']=this.getForm().findField('masterOrderSn').getValue();
		params['pageOrder.relatingOriginalSn']=this.getForm().findField('relatingOriginalSn').getValue();
		params['pageOrder.relatingReturnSn']=this.getForm().findField('relatingReturnSn').getValue();
		params['pageOrder.relatingRemoneySn']=this.getForm().findField('relatingRemoneySn').getValue();
		params['pageOrder.isAgent']=params.isAgent;
		params['pageOrder.payId']=params.payMent;
		params['pageOrder.goodsAmount']=params.goodsAmount;
		params['pageOrder.totalFee']=params.totalFee;
		params['pageOrder.moneyPaid']=params.moneyPaid;
		params['pageOrder.totalPayable']=params.totalPayable;
		params['pageOrder.shippingTotalFee']=params.shippingTotalFee;
		params['pageOrder.bonus']=params.bonus;
		params['pageOrder.discount']=params.discount;
		
		var goodDetail=this.down('goodDetail');
		if(this.down('goodDetail').store.data.length < 1){
			Ext.Msg.alert('警告', "换货商品列表为空！", function(xx) {});
			return false;
		}
		goodDetail.store.each(function(record,i){
			params['pageGoods['+i+'].customCode']=record.get('customCode');
			params['pageGoods['+i+'].extensionCode']=record.get('extensionCode');
			params['pageGoods['+i+'].marketPrice']=record.get('goodsPrice');
			params['pageGoods['+i+'].goodsPrice']=record.get('goodsPrice');
			params['pageGoods['+i+'].transactionPrice']=record.get('transactionPrice');
			params['pageGoods['+i+'].goodsNumber']=record.get('goodsNumber');
			params['pageGoods['+i+'].settlementPrice']=record.get('settlementPrice');
			params['pageGoods['+i+'].shareBonus']=record.get('shareBonus');
			params['pageGoods['+i+'].goodsThumb']=record.get('goodsThumb');
		});
//		var len=goodDetail.length;
//		for(var i=0;i<len;i++){
//			params['pageGoods['+i+'].customCode']=goodDetail[i].customCode;
//			params['pageGoods['+i+'].extensionCode']=goodDetail[i].extensionCode;
//			params['pageGoods['+i+'].marketPrice']=goodDetail[i].marketPrice;
//			params['pageGoods['+i+'].goodsPrice']=goodDetail[i].goodsPrice;
//			params['pageGoods['+i+'].transactionPrice']=goodDetail[i].transactionPrice;
//			params['pageGoods['+i+'].goodsNumber']=goodDetail[i].goodsNumber;
//		}
		/*params['pageGoods['+0+'].customCode']='24165390146';
		params['pageGoods['+0+'].extensionCode']='common';
		params['pageGoods['+0+'].marketPrice']=100;
		params['pageGoods['+0+'].goodsPrice']=100;
		params['pageGoods['+0+'].transactionPrice']=100;
		params['pageGoods['+0+'].goodsNumber']=1;
		
		params['pageOrder.goodsAmount']=100;
		params['pageOrder.totalFee']=110;
		params['pageOrder.moneyPaid']=100;
		params['pageOrder.totalPayable']=0;
		params['pageOrder.shippingTotalFee']=10;
		params['pageOrder.bonus']=0;
		params['pageOrder.discount']=0;*/
		
		return params;
	}
});