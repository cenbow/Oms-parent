Ext.define('MB.controller.OrderGoodsEditController', {
	extend : 'Ext.app.Controller',
	stores : ['MB.store.OrderSnCombStore',
			 'MB.store.GoodsColorCombStore',
			 'MB.store.GoodsSizeCombStore',
			 'MB.store.SearchGoodss'],
	models : ['MB.model.ComboModel',
	          'MB.model.ProductBarcodeListModel'],
	views : ['MB.view.orderEdit.OrderGoodsEditWin',
	         'MB.view.orderEdit.OrderSnComb',
             'MB.view.orderEdit.AddGoodsModule',
             'MB.view.orderEdit.GoodsBonusModule',
             'MB.view.commonComb.GoodsColorComb',
             'MB.view.commonComb.GoodsSizeComb',
             'MB.view.common.SearchGoodsCombo'],
	refs : [{
		ref : 'orderGoodsEditWin',
		selector : 'orderGoodsEditWin'
	}],
	init : function() {
		var me = this;
		me.control({
			'orderGoodsEditWin button[action=searchOrderGoods]' : { //关闭窗口
				click : this.searchOrderGoods
			},
			'orderGoodsEditWin button[action=reset]' : { //关闭窗口
				click : this.reset
			}
		});
	},
	onLaunch : function() {
		var win = Ext.widget('orderGoodsEditWin');
		//给验证字段赋值
		Ext.Ajax.request({
			url:  basePath + '/custom/orderGoodsEdit/getIsVerifyStock',
			success: function(response){
				var result = Ext.JSON.decode(response.responseText);
				parent.isVerifyStock = result.code;
			},
			failure: function(response){
				
			}
		});
		
		if(editGoodsType != 3){
			//获取商品编辑页数据
			var params = {};
			params.masterOrderSn = masterOrderSn;
			params.orderSn = deliveryOrderSn;
			if(deliveryOrderSn!=''){
				Ext.getCmp('orderGoodsEditWinOrderSn').setValue(deliveryOrderSn);
			}
			Ext.Ajax.request({
				url:  basePath + '/custom/orderGoodsEdit/getOrderGoodsEdit',
				params: params,
				success: function(response){
					var result = Ext.JSON.decode(response.responseText);
					if (result.code=='1') {
						win.initPage(win,result);
					} else {
						Ext.msgBox.remainMsg('获取商品信息', result.msg, Ext.MessageBox.ERROR);
					}
				},
				failure: function(response){
					var result = Ext.JSON.decode(response.responseText);
					Ext.msgBox.remainMsg('获取商品信息', result.msg, Ext.MessageBox.ERROR);
				}
			});	
			
		}else{
			var store_t = Ext.data.StoreMgr.lookup('ExchangeGoodsStore');
			var grid=Ext.getCmp('districtGoods');
				grid.store = store_t;  
				grid.bindStore(store_t);
//			Ext.getCmp('orderGoodsEditWinOrderSn').setValue(orderSn);
//			Ext.getCmp('orderGoodsEditWinOrderSn').setReadOnly(true);
//			Ext.getCmp('goodsEditSearchButton').disable();
//			win.down('form').down('fieldcontainer').hidden = true;//隐藏搜索栏
			grid.down('#recalculateButton').hidden = true;//隐藏红包试算按钮
			grid.down('#bonusTotal').hidden = true;//隐藏红包合计
			grid.down('#goodsBonusTotal').hidden = true;//隐藏商品使用红包合计
			grid.down('#orderTotal').hidden = true;//隐藏订单总金额
			grid.columns[6].editor = false;//设置红包金额不可编辑
			win.down('form').down('tabpanel').tabBar.items.items[1].hide();//隐藏红包面板
			Ext.getCmp('editGoodsReset').hidden = true;//隐藏重置按钮
//			Ext.getCmp('editGoodsClose').hidden = true;//隐藏关闭按钮
//			win.closable = false;//隐藏窗口的关闭按钮
		}
		win.show();
	},
	searchOrderGoods : function(btn){
		var win = btn.up('window');
		var orderSn = Ext.getCmp('orderGoodsEditWinOrderSn').getValue();
		var params = {};
		params.masterOrderSn = masterOrderSn;
		params.orderSn = orderSn;
		
		Ext.Ajax.request({
			url:  basePath + '/custom/orderGoodsEdit/getOrderGoodsEdit',
			params: params,
			success: function(response){
				var result = Ext.JSON.decode(response.responseText);
				if (result.code=='1') {
					sonOrderEditSn = orderSn;//保存搜索的交货单号
					win.initPage(win,result);
				} else {
					Ext.msgBox.remainMsg('获取商品信息', result.msg, Ext.MessageBox.ERROR);
				}
			},
			failure: function(response){
				var result = Ext.JSON.decode(response.responseText);
				Ext.msgBox.remainMsg('获取商品信息', result.msg, Ext.MessageBox.ERROR);
			}
		});	
	},
	reset : function(btn){
		if(editGoodsType != 3){
			var win = btn.up('window');
			//重置查询条件
			Ext.getCmp('orderGoodsEditWinOrderSn').setValue(deliveryOrderSn);
			//拼装查询参数
			var params = {};
			params.masterOrderSn = masterOrderSn;
			params.orderSn = deliveryOrderSn;
			Ext.Ajax.request({
				url:  basePath + '/custom/orderGoodsEdit/getOrderGoodsEdit',
				params: params,
				success: function(response){
					var result = Ext.JSON.decode(response.responseText);
					if (result.code=='1') {
						sonOrderEditSn = deliveryOrderSn;//保存重置后的交货单号
						win.initPage(win,result);
					} else {
						Ext.msgBox.remainMsg('获取商品信息', result.msg, Ext.MessageBox.ERROR);
					}
				},
				failure: function(response){
					var result = Ext.JSON.decode(response.responseText);
					Ext.msgBox.remainMsg('获取商品信息', result.msg, Ext.MessageBox.ERROR);
				}
			});	
			
		}
	}
	
});