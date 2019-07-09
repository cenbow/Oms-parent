
Ext.define("MB.view.goodsReturnChange.GoodsReturnChangeCenter", {
	extend: "Ext.form.Panel",
	alias: 'widget.goodsReturnChangeCenter',
	id:'goodsReturnChangeCenter',
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
				xtype:'goodsReturnChangeSetModule',
				titleCollapse:true
			}, {
				xtype:'goodsReturnChangeGoodsSetModule',
				titleCollapse:true
//				collapsed: true//初始不展开
			},
			{
				xtype:'goodsReturnChangePicSetModule',
				titleCollapse:true
			} ,
			{
			xtype:'orderSetModule',
			title:'原订单信息',
			titleCollapse:true
		} ,{
			xtype:'goodSetModule',
//			collapsed: true,//初始不展开
			titleCollapse:true
		}/*, {
			xtype:'deliverySetModule',
//			collapsed: true,//初始不展开
			titleCollapse:true
		}*/, {
			xtype:'paySetModule',
			titleCollapse:true
//			collapsed: true//初始不展开
		}
		];
//		//以json形式读取数据
		this.reader = Ext.create('Ext.data.reader.Json', {
			rootProperty : "result",
			model : 'MB.model.OrderDetailModel'
		});
		this.callParent(arguments);
	},
	initData : function (orderForm) {
		var me = this;
		var payGrid = me.down("payDetail");
		var goodGrid = me.down("goodDetail");
//		var orderSn = me.down("orderSn");
		
		me.load({
			url : basePath + '/custom/goodsReturnChange/newReturnChangeInfo',
			params : {
				"orderSn" : orderSn,
				"isHistory": isHistory,
				"id":goodsReturnChangeId
			},
			timeout:90000,
			success : function(opForm, action) {
				//获取数据
				var orderInfo = opForm.reader.rawData.result;
				var goodsReturnChange=opForm.reader.rawData.goodsReturnChange;
				//完善数据
				goodsReturnChange.relatingOrderSn=orderInfo.masterOrderSn;
				//获取组件并赋值
				var goodsReturnChangeSetModule=me.down('goodsReturnChangeSetModule');
				goodsReturnChangeSetModule.getForm().setValues(goodsReturnChange);
				var orderSetModule = me.down("orderSetModule");
				var paySetModule = me.down("paySetModule");
				var goodSetModule = me.down("goodSetModule");
				var goodsReturnChangeGoodsList = me.down("goodsReturnChangeGoodsList");
				orderSetModule.getForm().findField("ordertotalstatusStr")
					.setValue(getCombineStatus(orderInfo.orderStatus,orderInfo.payStatus,orderInfo.shipStatus));
				channelCode = orderInfo.orderFrom;
				userId = orderInfo.userId;
				activityType = orderInfo.activityType;
				transType = orderInfo.transType;
				
				payGrid.store.loadData(opForm.reader.rawData.payDetail);
				goodGrid.store.loadData(opForm.reader.rawData.goodDetail);
				var goodsTotal = 0.000000;
				goodGrid.getStore().each(function(record) {
					goodsTotal += record.get("subTotal");
				});
				var total = '<div style= "float: right;color:black;font-weight:bold">总计：' + goodsTotal +' 元</div>';
				goodSetModule.down("#total").update(total);
//				activityType = "c2b";
//				console.dir(orderInfo);
				// bonusName
				// 展示订单费用信息
				var showTotalFee = '<div style= "float: right; color: #666;">'
					+ '商品总金额：  ' + numFixed(orderInfo.goodsAmount, 2)
					+ ' 元 - 折让：  ' + numFixed(orderInfo.discount, 2)
					+ ' 元 + 发票税额：  ' + numFixed(orderInfo.tax, 2)
					+ ' 元 + 配送费用：  ' + numFixed(orderInfo.shippingTotalFee, 2)
					+ ' 元 + 保价费用：  ' + numFixed(orderInfo.insureTotalFee, 2)
					+ ' 元 + 支付费用：  ' + numFixed(orderInfo.payTotalFee, 2)
					+ ' 元</div>';
				paySetModule.down("#showTotalFee").update(showTotalFee);
				var totalFee = '<div style= "float: right; color: #666;">= 订单总金额：   ' + numFixed(orderInfo.totalFee, 2) +'元</div>';
				paySetModule.down("#totalFee").update(totalFee);
				var exchangeReturnFee = "";
				// 换货订单按正常订单展示
//				if (orderInfo.orderType == 2) {
//					exchangeReturnFee = '- 换货时退货转入款：' + orderInfo.exchangeReturnFee + '元';
//				}
				var showTotalPayable = '<div style= "float: right; color: #666;">'
					+ '- 已付款金额：  ' + numFixed(orderInfo.moneyPaid, 2)
					+ exchangeReturnFee
					+ ' 元 - 使用余额：  ' + numFixed(orderInfo.surplus, 2)
					+ ' 元 - 使用红包【'+ orderInfo.bonusName +'】：  ' + numFixed(orderInfo.bonus, 2)
					+ ' 元</div>';
				paySetModule.down("#showTotalPayable").update(showTotalPayable);
				var totalPayable = '<div style= "float: right; color: #666;">= 应付款金额：   ' + numFixed(orderInfo.totalPayable, 2) +'元</div>';
				paySetModule.down("#totalPayable").update(totalPayable);
				console.dir(opForm.reader.rawData);
				if(opForm.reader.rawData.returnFee){
				var returnFee = opForm.reader.rawData.returnFee;
				// 退货单信息
				var returnTransPrice = '<div style= "float: right; color: #666;">所退商品的总金额：   ' + numFixed(returnFee.returnallmoney, 2)
					+ ' 元  - 所退商品的总折让值：' + numFixed(returnFee.returnalldiscount, 2)
					+ ' 元  = 所退商品成交价总金额：' + numFixed(returnFee.returntransallmoney, 2)
					+' 元</div>';
				paySetModule.down("#returnTransPrice").update(returnTransPrice);
				var returnSettPrice = '<div style= "float: right; color: #666;">- 所退商品分摊红包总金额：   ' + numFixed(returnFee.returnbonusallmoney, 2)
				+ '元 = 所退商品财务总金额：' + numFixed(returnFee.returnsettlemoney, 2)
				+' 元</div>';
				paySetModule.down("#returnSettPrice").update(returnSettPrice);
					
				}
				
//				goodSetModule.tools[1].disable();
				
//				paySetModule.tools[1].disable();
				
				//加载退换货商品信息
				goodsReturnChangeGoodsList.store.loadData(opForm.reader.rawData.goodsReturnChangeList);
				
				// 
			/*	var orderActionGrid = me.up('orderDetailView').down('orderSouth');
				orderActionGrid.store.on('beforeload', function (store){
					params = {"orderSn" : orderSn};
					Ext.apply(store.proxy.extraParams, params);
				});
				orderActionGrid.getStore().load();*/
			},
			failure : function(opForm, action) {
				// 数据加载失败后操作
//				alert(2222);
			}
		});
		
	}
});