Ext.define('MB.controller.OrderAmountAdjustmentController', {
	extend : 'Ext.app.Controller',
	stores : ['MB.store.IsSyncStore',
	          'MB.store.OrderAmountAdjustmentStore'
	          ],
	models : ['MB.model.ComboModel',
	          'MB.model.OrderAmountAdjustmentModel'
	          ],
	views : ['MB.view.orderAmountAdjustment.OrderAmountAdjustmentPanlView',
	         'MB.view.orderAmountAdjustment.OrderAmountAdjustmentGridView',
	         'MB.view.bfbRefundSettlement.IsSyncCombo',
	         'MB.view.orderAmountAdjustment.OrderAmountAdjustmentImport'],
	refs : [{
		ref : 'orderAmountAdjustmentPanlView',
		selector : 'orderAmountAdjustmentPanlView'
	},{
		ref : 'orderAmountAdjustmentGridView',
		selector : 'orderAmountAdjustmentGridView'
	},{
		ref : 'orderAmountAdjustmentImport',
		selector : 'orderAmountAdjustmentImport'
	}],
	init : function() {
		var me = this;
		me.control({
			'orderAmountAdjustmentPanlView button[action=reset]':{
				click : this.reset
			},
			'orderAmountAdjustmentPanlView button[action=search]':{
				click : this.search
			},
			'orderAmountAdjustmentPanlView button[action=delAdjust]':{
				click : this.delAdjust
			},
			'orderAmountAdjustmentPanlView button[action=openImport]':{
				click : this.openImport
			},
			'orderAmountAdjustmentPanlView button[action=doAdjust]':{
				click : this.doAdjust
			},
			'orderAmountAdjustmentImport button[action=closeImport]':{
				click : this.closeImport
			},
			'orderAmountAdjustmentImport button[action=doImport]':{
				click : this.doImport
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
		var searchParams = getFormParams(form, null);
		console.dir(searchParams);
		var orderAmountAdjustmentGridView = form.up('viewport').down("orderAmountAdjustmentGridView");
		orderAmountAdjustmentGridView.store.on('beforeload', function (store, options){
			Ext.apply(store.proxy.extraParams, searchParams);
		});
		orderAmountAdjustmentGridView.store.currentPage = 1;// 翻页后重新查询 页面重置为1
		orderAmountAdjustmentGridView.store.pageSize = pageSize;
		orderAmountAdjustmentGridView.store.load({params : searchParams});
	},
	delAdjust : function(btn){
		 var ids = "";//用于拼装选中行的billNo
 		var records = Ext.getCmp("orderAmountAdjustmentGridView").getSelectionModel().getSelection();//获取选中行
 		if(records.length==0){
            Ext.MessageBox.show({   
               title:"提示",   
               msg:"请先选择您要操作的行!"
            });
           return;
 		}else{
      		 Ext.Msg.confirm('确认', '确认作废选中的调整单吗?', function (text) {
        	   if (text == 'yes') {
        		   for(var i = 0; i < records.length; i++){
                       ids += records[i].get("billNo"); 
                       if(i<records.length-1){ 
                           ids = ids + ",";  
                       }
                   }
        		   Ext.Ajax.request( {
	   					waitMsg : '请稍等.....',
	   					url :  basePath + 'custom/orderAmountAdjustment/delOrderAmountAdjustment.spmvc',
	   					method : 'post',
	   					timeout:600000,
	   					params : {'ids':ids},
	   					success : function(response) {
	   						var respText = Ext.JSON.decode(response.responseText);
	   						if(respText.code=='0'){
	   							Ext.Msg.alert('删除', respText.msg);
	   							Ext.getCmp("orderAmountAdjustmentGridView").store.reload();//刷新查询列表数据
	   						}else{
	   							Ext.Msg.alert('删除', respText.msg);
	   							win.close();
	   						}
	   					},
	   					failure : function(response) {
	   						var respText = Ext.JSON.decode(response.responseText);
	   						Ext.Msg.alert('删除', respText.msg);
	   					}
   					});
	        	}
	        });  
 		}
	},
	openImport:function(btn){
		var win = Ext.widget("orderAmountAdjustmentImport");
		win.show();		
	},
	closeImport: function(btn){
		var win = btn.up("window");
		win.close();
	},
	doImport: function(btn){
		//获取参数
		var win = btn.up("window");
		var form = win.down("form");
		var formValues=form.getForm().getValues();
		//非空判断
		var myfile = form.getForm().findField('myfile').getValue();
		if(myfile==null||myfile==''){
			Ext.Msg.alert('提示', '请选择数据文件！');
			form.getForm().findField('myfile').focus();
			return;
		}else if(myfile.lastIndexOf(".xls") == -1){
			Ext.Msg.alert('提示', '选择文件不是[.xls]格式文件！');
			form.getForm().findField('myfile').focus();
			return;
		}
		//生成调整单
		form.submit( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/orderAmountAdjustment/doImport.spmvc',
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(form,action) {
				if(action.result.code=='0'){
					Ext.Msg.alert('生成调整单', action.result.msg+'调整单号：['+action.result.billNo+']');
					win.close();
					Ext.getCmp("orderAmountAdjustmentGridView").store.reload();//刷新查询列表数据
				}else{
					Ext.Msg.alert('生成调整单', action.result.msg);
				}
			},
			failure : function(form,action) {
				Ext.Msg.alert('生成调整单', action.result.msg);
			}
		});
		
	},
		
	doAdjust: function(btn){
		 var ids = "";//用于拼装选中行的billNo
	 		var records = Ext.getCmp("orderAmountAdjustmentGridView").getSelectionModel().getSelection();//获取选中行
	 		if(records.length==0){
	            Ext.MessageBox.show({   
	               title:"提示",   
	               msg:"请先选择您要操作的行!"
	            });
	           return;
	 		}else{
	 			for(var i = 0; i < records.length; i++){
                    ids += records[i].get("billNo"); 
                    if(i<records.length-1){ 
                        ids = ids + ",";  
                    }
                }
     		   Ext.Ajax.request( {
					waitMsg : '请稍等.....',
					url :  basePath + 'custom/orderAmountAdjustment/doExecute.spmvc',
					method : 'post',
					timeout:600000,
					params : {'ids':ids},
					success : function(response) {
						var respText = Ext.JSON.decode(response.responseText);
						if(respText.code=='0'){
							Ext.Msg.alert('提示', respText.msg);
							Ext.getCmp("orderAmountAdjustmentGridView").store.reload();//刷新查询列表数据
						}else{
							Ext.Msg.alert('提示', respText.msg);
						}
					},
					failure : function(response) {
						var respText = Ext.JSON.decode(response.responseText);
						Ext.Msg.alert('提示', respText.msg);
					}
     		   });
	 		}
		}
	
	
});