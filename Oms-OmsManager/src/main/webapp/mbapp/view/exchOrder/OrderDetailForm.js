Ext.define("MB.view.exchOrder.OrderDetailForm", {
	extend: "Ext.form.Panel",
	alias: 'widget.orderDetailForm',
	id:'orderDetailForm',
	title: "<span style='color:black;font-weight:bold';>原订单信息</span>",
	width: '100%',
	frame: true,
	/*bodyStyle: {
		padding: '10px'
	},*/
	autoWidth:true,
	autoHeight:true,
	autoScroll : false,
	collapsed : true,
	collapsible:true,
	titleCollapse:true,
	buttonAlign : 'center',
	fieldDefaults: {
		labelAlign: 'right'
	},
	bodyBorder:true,
	//store: 'Demos',
	initComponent: function () {
		this.items = [
		{
			xtype:'orderSetModule',
			titleCollapse:true
		} , {
			xtype:'shipSetModule',
			titleCollapse:true
		}, {
			xtype:'otherSetModule',
			title : "其他信息&nbsp;&nbsp;&nbsp;",
			titleCollapse:true
			
		},{
			xtype:'goodSetModule',
			collapsed: true,//初始不展开
			titleCollapse:true
		},{
			xtype:'deliverySetModule',
			collapsed: true,//初始不展开
			titleCollapse:true
		}, {
			xtype:'paySetModule',
			titleCollapse:true,//初始不展开
			collapsed: true//初始不展开
		}
		];
//		//以json形式读取数据
		this.reader = Ext.create('Ext.data.reader.Json', {
					rootProperty : "masterOrderInfo",
					model : 'MB.model.OrderDetailModel'
		});
		this.callParent(arguments);
	},
	initData: function() {
		var orderForm=this;
		var deliveryGrid = orderForm.down("deliveryDetail");
		var payGrid = orderForm.down("payDetail");
		var goodGrid = orderForm.down("goodDetail");
		
		//展开原订单信息，后台发送请求
		Ext.getCmp('orderDetailForm').collapse();
		Ext.getCmp('orderDetailForm').addListener('expand',function(){
			orderForm.load({
				url : basePath + '/custom/exchangeorder/getOrderDetail',
				params : {
					"orderSn" : orderSn,
					"isHistory": isHistory
				},
				timeout:90000,
				success : function(opForm, action) {
					console.dir(opForm);
					// 数据加载成功后操作
					channelCode = opForm.reader.rawData.masterOrderInfo.orderFrom;
//					activityType = opForm.reader.rawData.result.activityType;
//					orderForm.addOrderQuestion(opForm, action);
					deliveryGrid.store.loadData(opForm.reader.rawData.orderDepotShipList);
					payGrid.store.loadData(opForm.reader.rawData.masterOrderPayList);
					goodGrid.store.loadData(opForm.reader.rawData.masterOrderGoodsDetailList);
					goodGrid.recalculateTotal(goodGrid);
					
					if(!exchangeOrderSn){//申请换单页面
						orderForm.up('exchangeCenter').down('exchangeForm').initApplyPage(opForm.reader.rawData.masterOrderInfo);
					}
					
					//详细地址加载
					var address = "";
					var orderInfo = opForm.reader.rawData.masterOrderInfo;
					var masterOrderInfoData = opForm.reader.rawData.masterOrderInfo;
					setTimeout(function(){
						Ext.getCmp('orderSetModule').getForm().findField('ordertotalstatusStr').setValue(getCombineStatus(masterOrderInfoData.orderStatus,masterOrderInfoData.payStatus,masterOrderInfoData.shipStatus));//订单状态
					}, 1);
					if(orderInfo != undefined && orderInfo != null){
						var shipSetModule = orderForm.down("shipSetModule");
						if (orderInfo.country != null && orderInfo.country != '') {
							address += orderInfo.country + " ";
						}
						if (orderInfo.province != null && orderInfo.province != '') {
							address += orderInfo.province + " ";
						}
						if (orderInfo.city != null && orderInfo.city != '') {
							address += orderInfo.city + " ";
						}
						if (orderInfo.district != null && orderInfo.district != '') {
							address += orderInfo.district + " ";
						}
						if (orderInfo.street != null && orderInfo.street != '') {
							address += orderInfo.street + " ";
						}
//						shipSetModule.getForm().findField("detailAddress").setValue('[' + address + '] ' + orderInfo.address);
					}
				},
				failure : function(opForm, action) {
					// 数据加载失败后操作
					alert(2222);
				}
			});
	    });
		
//		grid = Ext.getCmp('deliveryDetail'),
//		payGrid = Ext.widget('payDetail'),
//		goodForm=Ext.getCmp('goodSetModule');
//		var goodGrid=goodForm.down('grid');
		
		
	
	}
});

