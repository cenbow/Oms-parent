Ext.define('MB.controller.CommunicateController', {
	extend : 'Ext.app.Controller',
	views : [ 'MB.view.orderDetail.CommunicateWin' ],
	onLaunch : function() {
		// 页面加载完成之后执行
		var win = Ext.widget("communicateWin");
		var form = win.down('form');
		var radiogroup = form.down('radiogroup');
		//动态添加交货单选项
		if(sonOrderListData!=null){
			Ext.each(sonOrderListData,function(sonOrderMap){
				var sonOrder = sonOrderMap.sonOrder;//子单信息
				var orderSn = sonOrder.orderSn;//子单号
				var sonOrderGoodsList = sonOrderMap.sonOrderGoodsList;//子单商品列表
				//供应商
				var supplierCode = '无商品';
				if(sonOrderGoodsList.length>0){
					supplierCode = sonOrderGoodsList[0].supplierCode;//供应商
				}
				if(orderSn!=null&&orderSn!=''){
					var r=new Ext.form.field.Radio({ boxLabel: '('+supplierCode+')'+'交货单'+orderSn, name: 'orderSn', inputValue: orderSn });
					radiogroup.add(r); 
				}
			});
		}
		
		win.show();
	}
});