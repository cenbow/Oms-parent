Ext.define("MB.view.orderDetail.PaymentEditForm", {
	extend : "Ext.form.Panel",
	alias : 'widget.paymentEditForm',
	width : '100%',
	frame : true,
	margin : 1,
	bodyPadding : 2,
	layout : 'column',
	buttonAlign : 'center',// 按钮居中
	url : basePath + '/custom/orderStatus/editOrderOther',
	initComponent : function() {
		var me = this;
		me.items = [{
			xtype : 'hidden',
			name : 'paySn'
		} , {
			xtype: 'radiogroup',
			fieldLabel: '支付方式',
			name : 'payId',
			columns: 1,
			allowBlank : false,
			items: []
		}]
		me.callParent(arguments);
		me.initPage(me);
	},
	initPage : function (form) {
		// 加载支付方式
		console.dir(parent.transType);
		Ext.Ajax.request({
			url:  basePath + '/custom/common/getSystemPayment',
			params: {transType : parent.transType},
			success: function(response){
				var text = response.responseText;
				var results = Ext.JSON.decode(text);
				if (results != null && results.length > 0) {
					results.forEach(function (obj, index) {
						var boxLabel = obj.payName +'  ' + obj.payDesc + '  手续费：' + obj.payFee;
						var radio = {boxLabel: boxLabel, name: 'payId', inputValue: obj.payId};
						form.down('radiogroup').add(radio);
					});
				}
			},
			failure: function(response){
				var text = response.responseText;
				console.dir('failure:' + text);
			}
		});
	}
});