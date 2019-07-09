Ext.define('MB.controller.RefundListController', {
	extend : 'Ext.app.Controller',
	stores : ['SystemPaymentStore','RefundGridStore','ChannelInfoStore','ChannelTypeStore', 'ChannelShopStore','ReturnTypeStore','HaveRefundStore','ReturnPayStatusStore'],
	models : ['SystemPaymentModel','RefundGridModel','ChannelInfo', 'ComboModel','ChannelShop','CommonStatusModel'],
	views : ['refund.RefundList','refund.RefundContent','common.SystemPayment','refund.RefundDataForm',
			 'refund.RefundGrid','common.ReturnType','common.ReturnPayStatusCombo'
	],
	init : function() {
		var me = this;
		me.control({
			'refundContent button[action=searchAction]' : {
				click : this.search
			},
			'refundContent button[action=exportOrderRefund]':{
				click:this.exportOrderRefund
			}
		});
	},
	onLaunch : function() {
		//默认需要退款
		Ext.getCmp('refundContent').getForm().findField('haveRefund').setValue('1');
		Ext.getCmp('refundContent').getForm().findField('returnPayStatus').setValue('2');
	},
	search:function(){
		Ext.getCmp('refundContent').search();
	},
	exportOrderRefund:function(){
		Ext.getCmp('refundContent').exportOrderRefund();
	}
});
