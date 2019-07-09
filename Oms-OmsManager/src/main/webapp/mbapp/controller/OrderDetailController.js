Ext.define('MB.controller.OrderDetailController', {
	extend : 'Ext.app.Controller',
	models : [],
	views : ['MB.view.orderDetail.OrderDetailNorth',
	         'MB.view.orderDetail.OrderDetailCenter'
	         ],
	refs : [{
		ref : 'orderDetailNorth',
		selector : 'orderDetailNorth'
	},{
		ref : 'orderDetailCenter',
		selector : 'orderDetailCenter'
	}],
	init : function() {
		var me = this;
		me.control({
			'orderDetailNorth button[action=OrderOperation]' : { //主单按钮操作
				click : this.OrderOperation
			},
			'orderDetailCenter tool[action=shipEdit]' : { //打开收货人信息编辑页面
				click : this.shipEdit
			},
			'orderDetailCenter tool[action=otherEdit]' : { //其他信息编辑页面
				click : this.otherEdit
			},
			'orderDetailCenter tool[action=payEdit]' : { //付款单编辑页面（付款单不是支付单）
				click : this.payEdit
			},
//			'orderDetailCenter tool[action=editMasterOrderGoods]' : { //编辑主单商品
//				click : this.editMasterOrderGoods
//			},
			'orderDetailCenter tool[action=editDistributeOrderGoods]' : { //编辑交货单商品
				click : this.editDistributeOrderGoods
			},
			'orderDetailCenter button[action=sonOrderOperation]' : { //交货单按钮操作
				click : this.sonOrderOperation
			}
		});
	},
	onLaunch : function() {
		var orderInfoForm = Ext.getCmp('orderDetailCenter');
		//初始化页面数据
		orderInfoForm.initData(orderInfoForm);
		var orderDetailSouth = Ext.getCmp('orderDetailSouth');//日志面板的form
		var logModuleTabPanel = orderDetailSouth.down('#logModuleTabPanel');//日志面板的父tabpanel
		var orderLogModule = orderDetailSouth.down('#orderLogModule');//日志显示面板
		//日志面板添加监听  展开激活第一个日志面板
		orderDetailSouth.addListener('expand',function(){
			//获取tabpanel下所有的itemId
			var logIds = '';
			logModuleTabPanel.items.each(function(item){
				itemId = item.itemId;
				if(itemId&&itemId!=null&&itemId!=''){
					logIds = logIds+itemId+',';
				}
            });
			//遍历交货单  添加交货单title
			Ext.each(sonOrderListData,function(sonOrderMap){
				var sonOrder = sonOrderMap.sonOrder;//子单信息
				var orderSn = sonOrder.orderSn;//子单号
				if(!(logIds.indexOf(orderSn+',')>0)){//不含该交货单的日志就新增
					logModuleTabPanel.add({
						itemId : orderSn,
						title : '交货单('+orderSn+')日志'
					});
				}
			});
			//加载主单日志信息
			logModuleTabPanel.setActiveTab(0);
			var store = orderLogModule.getStore();
			store.getProxy().url = basePath + 'custom/commonLog/getMasterOrderAction?masterOrderSn='+masterOrderSn+'&isHistory='+isHistory;
			store.load();
		});
	},
	sonOrderOperation : function(btn){
		var thisPage = Ext.getCmp('orderDetailCenter').up('viewport');
		var action = btn.value;
		var actionName = btn.text;
		var params = {'message':actionName};
		var distribute = btn.up('form').down('grid');
		var orderSn = '';
		//获取交货单号
		distribute.store.each(function(record, i) {
			if(i==0){
				orderSn = record.get('orderSn');
				return;
			}
		});
		if(orderSn!=''&&action!=''&&orderSn!=null&&action!=null&&orderSn&&action){
			params.orderSn = orderSn;
			if(action=='sonCancel'){
				orderSnForOperate = orderSn;
				var app = Ext.application('MB.jsPages.cancelDistributeOrderApp');
				return ;
			}else if(action=='sonQuestion'){
				orderSnForOperate = orderSn;
				var app = Ext.application('MB.jsPages.questionDistributeOrderApp');
				return ;
			}else if(action=='sonNormal'){
				orderSnForOperate = orderSn;
				var app = Ext.application('MB.jsPages.turnNormalDistributeOrderApp');
				return ;
			}else if(action=='sonAddrefuse'){//生成拒收入库单
				window.open(basePath + "custom/orderReturn/orderReturnPage?relatingOrderSn=" + orderSn+"&returnType=2");
				return ;
			}else if(action=='sonAddReturn'){//生成退货退款单
				window.open(basePath + "custom/orderReturn/orderReturnPage?relatingOrderSn=" + orderSn+"&returnType=1");
				return ;
			}else if(action=='sonAddExtra'){//生成额外退款单
				window.open(basePath + "custom/orderReturn/orderReturnPage?relatingOrderSn=" + orderSn+"&returnType=4");
				return ;
			}else if(action=='sonAddLostReturn'){//生成失货退货单
				window.open(basePath + "custom/orderReturn/orderReturnPage?relatingOrderSn=" + orderSn+"&returnType=5");
				return ;
			}else if(action=='sonAddExchang'){//生成换货单
				window.open(basePath + "custom/exchangeorder/exchangeDetailPage?relatingOrderSn=" + orderSn);
				return ;
			}else{
				this.updateOrderStatus(thisPage, params, action, actionName);
			}
		}else{
			Ext.msgBox.remainMsg('提示','获取参数失败！', Ext.MessageBox.ERROR);
		}
	},
	OrderOperation : function(btn){
		var me = this;
		var thisPage = btn.up('form').up('viewport');
		var action = btn.value;
		var actionName = btn.text;
		var params = {'masterOrderSn' : masterOrderSn};
		if(action == 'normal'){
			var app = Ext.application('MB.jsPages.turnNormalOrderApp');
			return ;
		}else if (action == 'addExtra') {//生成额外退款单
			window.open(basePath + "custom/orderReturn/orderReturnPage?relatingOrderSn=" + masterOrderSn+"&returnType=4");
			return ;
		}else if(action=='cancel'){
			var app = Ext.application('MB.jsPages.cancelOrderApp');
			return ;
		}else if(action == 'question'){
			var app = Ext.application('MB.jsPages.questionOrderApp');
			return ;
		}else if(action == 'normal'){
			var app = Ext.application('MB.jsPages.turnNormalOrderApp');
			return ;
		}else if(action == 'communicate'){
			var app = Ext.application('MB.jsPages.communicateApp');
			return ;
		}else if(action == 'getLogs'){
			params.isHistory = isHistory;
			me.updateOrderStatus(thisPage, params, action, actionName);
		}else if(action == 'sendMessage'){
			var app = Ext.application('MB.jsPages.sendMessageApp');
			return ;
		}else{
			params.message = actionName;
			me.updateOrderStatus(thisPage, params, action, actionName);
		}
	},
	updateOrderStatus : function(thisPage, params, action, actionName,win){
		var loadMarsk = new Ext.LoadMask({
			msg : 'Please wait...',
			target : thisPage
		});
		loadMarsk.show();
		Ext.Ajax.request({
			url: basePath + '/custom/orderStatus/' + action,
			params: params,
			timeout : 40000,
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				if (results.code == "1") {
					if (action == 'copyorder') {
						var url = order_info_url + results.msg +'&isHistory=' + isHistory;
						window.open(url);
					} else if (action == 'recent') {
						window.location.href = order_info_url + masterOrderSn +'&isHistory=0';
					}
					Ext.getCmp('orderDetailCenter').initData();
					if (win) {
						win.close();
					}
				} else {
					Ext.msgBox.remainMsg('订单' + actionName, results.msg, Ext.MessageBox.ERROR);
				}
				loadMarsk.hide();  //隐藏    
			},
			failure: function(response, action){
				loadMarsk.hide();  //隐藏
				Ext.msgBox.remainMsg('订单' + actionName, action.response.statusText, Ext.MessageBox.ERROR);
			}
		});
	},
	closeWin : function(btn){
		var win = btn.up("window");
		win.close();
	},
	shipEdit : function(){
		var app = Ext.application('MB.jsPages.masOrderShipEditApp');
		return ;
	},
	otherEdit : function(){
		var app = Ext.application('MB.jsPages.masOrderOtherEditApp');
		return ;
	},
	payEdit : function(){
		var app = Ext.application('MB.jsPages.masOrderPayEditApp');
		return ;
	},
	/*editMasterOrderGoods : function(btn){
		deliveryOrderSn = '';//编辑主单时把全局变量交货单号置空  以便判断当前编辑的是主单商品还是某个交货单商品
		sonOrderEditSn = '';
		Ext.require("MB.jsPages.orderGoodsEditPage", function () {
			var app = Ext.application('MB.jsPages.orderGoodsEditPage');
		}, self);
	},*/
	editDistributeOrderGoods : function(btn){
		var grid = btn.up('form').up('form').down('#distributeOrderGoddsDetail').down('#distributeOrderGoodsGrid');
		grid.store.each(function(record, i) {
			if(i=='0'){
				deliveryOrderSn = record.get('orderSn');//将交货单号赋值给全局变量
				sonOrderEditSn = record.get('orderSn');
			}
		});
		Ext.require("MB.jsPages.orderGoodsEditPage", function () {
			var app = Ext.application('MB.jsPages.orderGoodsEditPage');
		}, self);
	}
	
});