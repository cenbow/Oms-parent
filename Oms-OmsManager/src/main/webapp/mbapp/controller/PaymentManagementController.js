Ext.define('MB.controller.PaymentManagementController', {
	extend : 'Ext.app.Controller',
	stores : ['MB.store.EnabledStore',
	          'MB.store.IsOnlineStore',
	          'MB.store.PaymentQueryStore',
		      'MB.store.IsMobileForAddStore',
	          'MB.store.IsOnlineForAddStore',
	          'MB.store.IsCodForAddStore',
	          'MB.store.EnabledForAddStore'
	          ],
	models : ['MB.model.ComboModel',
	          'MB.model.PaymentModel'
	          ],
	views : ['MB.view.paymentManagement.PaymentQueryPanlView',
	         'MB.view.paymentManagement.PaymentQueryGridView',
	         'MB.view.shippingManagement.EnabledCombo',
	         'MB.view.paymentManagement.IsOnlineCombo',
	         'MB.view.paymentManagement.PaymentAdd',
	         'MB.view.paymentManagement.IsMobileForAddCombo',
	         'MB.view.paymentManagement.IsOnlineForAddCombo',
	         'MB.view.paymentManagement.IsCodForAddCombo',
	         'MB.view.shippingManagement.EnabledForAddCombo',
	         'MB.view.paymentManagement.PaymentEdit'
		     ],
	refs : [{
				ref : 'paymentQueryPanlView',
				selector : 'paymentQueryPanlView'
			},{
				ref : 'paymentQueryGridView',
				selector : 'paymentQueryGridView'
			},{
				ref : 'paymentAdd',
				selector : 'paymentAdd'
			},{
				ref : 'paymentEdit',
				selector : 'paymentEdit'
			}],
	init : function() {
		var me = this;
		me.control({
			'paymentQueryPanlView button[action=reset]':{
				click : this.reset
			},
			'paymentQueryPanlView button[action=search]':{
				click : this.search
			},
			'paymentQueryPanlView button[action=openAdd]':{
				click : this.openAdd
			},
			 'paymentAdd button[action=closeAdd]':{
					click : this.closeAdd
			},
			 'paymentAdd button[action=resetAdd]':{
					click : this.resetAdd
			},
			 'paymentAdd button[action=doAdd]':{
					click : this.doAdd
			},
			 'paymentEdit button[action=doSaveEdit]':{
					click : this.doSaveEdit
			},
			 'paymentEdit button[action=closeEdit]':{
					click : this.closeEdit
			}
		});
	},
	reset: function (btn) {
		var form = btn.up('form');
		form.reset();
	},
	search : function(btn){
		var pageSize = 20;
		var form = btn.up('form');//当前按钮form	
//		var initParams = {start : 0, limit : pageSize };这里很奇怪，角色管理将这个参数设置到searchParams中就没问题，资源管理和此处若设置了该参数，点击查询按钮后翻页内容不更新，真是rilegoule
		var searchParams = getFormParams(form, null);
		console.dir(searchParams);
		var paymentQueryGridViewPaymentList = form.up('viewport').down("paymentQueryGridView");
		paymentQueryGridViewPaymentList.store.on('beforeload', function (store, options){
			Ext.apply(store.proxy.extraParams, searchParams);
		});
		paymentQueryGridViewPaymentList.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		paymentQueryGridViewPaymentList.store.pageSize = pageSize;
		paymentQueryGridViewPaymentList.store.load({params : searchParams});
	},
	openAdd : function(){
		var win = Ext.widget("paymentAdd");
		win.show();
	},
	closeAdd : function(btn){
		var win = btn.up("window");
		win.close();
	},
	resetAdd : function(btn){
		var win = btn.up("window");
		win.down('form').reset();
	},
	doAdd : function(btn){
		//获取添加面板上的参数
		var win = btn.up("window");
		form = win.down("form");
		var formValues=form.getForm().getValues();
		//非空判断
		var payCode = form.getForm().findField('payCode').getValue();
		if(payCode==null||payCode==''){
			Ext.Msg.alert('提示', '支付方式编码不能为空');
			form.getForm().findField('payCode').focus();
			return;
		}
		var payName = form.getForm().findField('payName').getValue();
		if(payName==null||payName==''){
			Ext.Msg.alert('提示', '支付方式名称不能为空');
			form.getForm().findField('payName').focus();
			return;
		}
		var payDesc = form.getForm().findField('payDesc').getValue();
		if(payDesc==null||payDesc==''){
			Ext.Msg.alert('提示', '支付方式描述不能为空');
			form.getForm().findField('payDesc').focus();
			return;
		}
		var payFee = form.getForm().findField('payFee').getValue();
		if(payFee==null||payFee==''){
			if(payFee!='0'){
				Ext.Msg.alert('提示', '支付费用不能为空');
				form.getForm().findField('payFee').focus();
				return;
			}
		}
		var payOrder = form.getForm().findField('payOrder').getValue();
		if(payOrder==null||payOrder==''){
			if(payOrder!='0'){
				Ext.Msg.alert('提示', '显示顺序不能为空');
				form.getForm().findField('payOrder').focus();
				return;
			}
		}
		
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/paymentManagement/doAddPayment.spmvc',
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				console.log(respText);
				if(respText.code=='0'){
					Ext.Msg.alert('支付方式添加', respText.msg);
					win.close();
					Ext.widget("paymentQueryGridView").store.reload();//刷新查询列表数据
				}else{
					Ext.Msg.alert('支付方式添加', respText.msg);
				}
			},
			failure : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('支付方式添加添加', respText.msg);
			}
		});
	},
	closeEdit : function(btn){
		var win = btn.up("window");
		win.close();
	},
	doSaveEdit : function(btn){
		var win = btn.up("window");
		form = win.down("form");
		var formValues=form.getForm().getValues();
		console.dir(formValues);
		//非空判断
		var payCode = form.getForm().findField('payCode').getValue();
		if(payCode==null||payCode==''){
			Ext.Msg.alert('提示', '支付方式编码不能为空');
			form.getForm().findField('payCode').focus();
			return;
		}
		var payName = form.getForm().findField('payName').getValue();
		if(payName==null||payName==''){
			Ext.Msg.alert('提示', '支付方式名称不能为空');
			form.getForm().findField('payName').focus();
			return;
		}
		var payDesc = form.getForm().findField('payDesc').getValue();
		if(payDesc==null||payDesc==''){
			Ext.Msg.alert('提示', '支付方式描述不能为空');
			form.getForm().findField('payDesc').focus();
			return;
		}
		var payFee = form.getForm().findField('payFee').getValue();
		if(payFee==null||payFee==''){
			if(payFee!='0'){
				Ext.Msg.alert('提示', '支付费用不能为空');
				form.getForm().findField('payFee').focus();
				return;
			}
		}
		var payOrder = form.getForm().findField('payOrder').getValue();
		if(payOrder==null||payOrder==''){
			if(payOrder!='0'){
				Ext.Msg.alert('提示', '显示顺序不能为空');
				form.getForm().findField('payOrder').focus();
				return;
			}
		}
		Ext.Ajax.request( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/paymentManagement/doSaveEdit.spmvc',
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				console.log(respText);
				if(respText.code=='0'){
					Ext.Msg.alert('编辑支付方式', respText.msg);
					win.close();
					Ext.widget("paymentQueryGridView").store.reload();//刷新查询列表数据
				}else{
					Ext.Msg.alert('编辑支付方式', respText.msg);
				}
			},
			failure : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('编辑支付方式', respText.msg);
			}
		});
	}
	
});