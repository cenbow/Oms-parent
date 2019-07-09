Ext.define('MB.controller.AbnormalSKUMaintainController', {
	extend : 'Ext.app.Controller',
	stores : ['MB.store.ChannelStore',
	          'MB.store.AbnormalSKUMaintainStore'
	          ],
	models : ['MB.model.ComboModel',
	          'MB.model.AbnormalSKUMaintainModel'
	          ],
	views : ['MB.view.abnormalSKUMaintain.AbnormalSKUMaintainPanlView',
	         'MB.view.abnormalSKUMaintain.AbnormalSKUMaintainGridView',
	         'MB.view.abnormalSKUMaintain.ChannelCombo',
	         'MB.view.abnormalSKUMaintain.AbnSKUMaintainEditWin'],
	refs : [{
		ref : 'abnormalSKUMaintainPanlView',
		selector : 'abnormalSKUMaintainPanlView'
	},{
		ref : 'abnormalSKUMaintainGridView',
		selector : 'abnormalSKUMaintainGridView'
	},{
		ref : 'abnSKUMaintainEditWin',
		selector : 'abnSKUMaintainEditWin'
	}],
	init : function() {
		var me = this;
		me.control({
			'abnormalSKUMaintainPanlView button[action=reset]':{
				click : this.reset
			},
			'abnormalSKUMaintainPanlView button[action=search]':{
				click : this.search
			},
			'abnSKUMaintainEditWin button[action=doSaveEdit]':{
				click : this.doSaveEdit
			},
			'abnSKUMaintainEditWin button[action=closeEdit]':{
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
		var searchParams = form.getForm().getValues();
		//非空判断
		var channelType = form.getForm().findField('channelType').getValue();
		if(channelType==null||channelType==''){
			Ext.Msg.alert('提示', '请选择渠道类型');
			form.getForm().findField('channelType').focus();
			return;
		}
		console.dir(searchParams);
		var abnormalSKUMaintainGridView = form.up('viewport').down("abnormalSKUMaintainGridView");
		abnormalSKUMaintainGridView.store.on('beforeload', function (store, options){
			Ext.apply(store.proxy.extraParams, searchParams);
		});
		abnormalSKUMaintainGridView.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		abnormalSKUMaintainGridView.store.pageSize = pageSize;
		abnormalSKUMaintainGridView.store.load({params : searchParams});
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
		var skuSn = form.getForm().findField('skuSn').getValue();
		if(skuSn==null||skuSn==''){
			Ext.Msg.alert('提示', 'sku码不能为空');
			form.getForm().findField('skuSn').focus();
			return;
		}
		var outSkuName = form.getForm().findField('outSkuName').getValue();
		if(outSkuName==null||outSkuName==''){
			Ext.Msg.alert('提示', '商品名称不能为空');
			form.getForm().findField('outSkuName').focus();
			return;
		}
		var goodsNum = form.getForm().findField('goodsNum').getValue();
		if(goodsNum==null||goodsNum==''){
			if(goodsNum!='0'){
				Ext.Msg.alert('提示', '商品数量不能为空');
				form.getForm().findField('goodsNum').focus();
				return;
			}
		}
		//获取渠道码  如果记录从没有渠道码  就从查询条件中获取
		var channelType = form.getForm().findField('channelType').getValue();
		if(channelType==null||channelType==''){
			channelType = ext.getCmp('abnormalSKUMaintainPanlViewChannelCombo').getValue();
		}
		//重写提交参数中的渠道码
		var saveValues =  Ext.apply(formValues,{
			channelType : channelType
		});
		
		/*//验证sku码对应的颜色尺寸是否正确
		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/abnormalSKUMaintain/selectColorAndSizeBySKU.spmvc',
			method : 'post',
			timeout:600000,
			params : {'skuSn':skuSn},
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				console.log(respText);
				if(respText.code=='0'){
					confirmMsg("确认",respText.msg+',请确认是否正确！', function(btn) {
						if (btn == "yes") {
							//提交保存
							Ext.Ajax.request({
								waitMsg : '请稍等.....',
								url :  basePath + 'custom/abnormalSKUMaintain/doSaveEdit.spmvc',
								method : 'post',
								timeout:600000,
								params : formValues,
								success : function(response) {
									var respData = Ext.JSON.decode(response.responseText);
									console.log(respData);
									if(respData.code=='0'){
										Ext.Msg.alert('编辑', respData.msg);
										win.close();
										Ext.widget("abnormalSKUMaintainGridView").store.reload();//刷新查询列表数据
									}else{
										Ext.Msg.alert('编辑', respData.msg);
									}
								},
								failure : function(response) {
									var respData = Ext.JSON.decode(response.responseText);
									Ext.Msg.alert('编辑', respData.msg);
								}
							});
						}else{
							return;
						}
					});
				}else{
					Ext.Msg.alert('提示', respText.msg);
				}
			},
			failure : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('提示', respText.msg);
			}
		});*/
		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/abnormalSKUMaintain/doSaveEdit.spmvc',
			method : 'post',
			timeout:600000,
			params : saveValues,
			success : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				console.log(respText);
				if(respText.code=='0'){
					Ext.Msg.alert('编辑', respText.msg);
					win.close();
					Ext.widget("abnormalSKUMaintainGridView").store.reload();//刷新查询列表数据
				}else{
					Ext.Msg.alert('编辑', respText.msg);
				}
			},
			failure : function(response) {
				var respText = Ext.JSON.decode(response.responseText);
				Ext.Msg.alert('编辑', respText.msg);
			}
		});
	}
	
	
});