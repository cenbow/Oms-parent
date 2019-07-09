Ext.define('MB.controller.BatchDecodeController', {
	extend : 'Ext.app.Controller',
	stores : [
	          'MB.store.BatchDecodeListStore'
	          ],
	models : [
	          /*'MB.model.ComboModel',*/
	          ],
	views : ['MB.view.batchDecode.BatchDecodeViewport',
	         'MB.view.batchDecode.BatchImportWin'
		     ],
	refs : [{
				ref : 'batchImportWin',
				selector : 'batchImportWin'
			}],
	init : function() {
		var me = this;
		me.control({
			'batchImportWin button[action=closeImport]':{
				click : this.closeWin
			},
			'batchImportWin button[action=doImport]':{
				click : this.doImport
			}
		});
	},
	closeWin: function (btn) {
		var win = btn.up("window");
		win.close();
	},
	doImport: function (btn) {
		var win = btn.up("window");
		var form = win.down("form");
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
		//获取参数
		var formValues=form.getForm().getValues();
		//放入执行队列
		form.submit( {
			waitMsg : '请稍等.....',
			url :  basePath + 'custom/batchDecode/doImport.spmvc',
			method : 'post',
			timeout:600000,
			params : formValues,
			success : function(form,action) {
				console.dir(action);
				result = action.result;
				var msg = result.msg;
				var success = result.success;
				if(success=='true'){
					Ext.getCmp('batchDecodeListGrid').store.load();
					win.close();
				}else{
					Ext.Msg.alert('执行结果', msg);
				}
			},
			failure : function(form,action) {
				console.dir(action);
				Ext.Msg.alert('执行结果', "导入文件失败！");
			}
		});
	}
	
});