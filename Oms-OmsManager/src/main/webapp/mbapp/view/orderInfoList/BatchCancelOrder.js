Ext.define("MB.view.orderInfoList.BatchCancelOrder", {
	extend : "Ext.window.Window",
	alias : "widget.batchCancelOrder",
	title : "批量取消订单",
    id:'batchCancelOrderWin',
	width : 400,
	height : 300,
	layout : "fit",
	initComponent : function() {
		
		var me = this;
		
		me.items = {
			xtype : "form",
			margin : 5,
			border : false,
			frame : true,
			url: basePath + "/custom/orderStatus/batchCancel",
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 70
			},

			items : [ {
				
				id:'orderInfoPageMessage',
				xtype: 'textarea',
				name : 'message',
				fieldLabel : '备注',
				labelWidth : 40,
				width: 300,
				height : 100
				
	
			}, {
				
				xtype : 'combo',
				store : 'OrderInfoOperationReasonStore',
				id : 'orderInfoOperationReason',
				name : 'code',
				displayField : 'name',
				valueField : 'code',
				queryMode : "remote",
				width : 300,
				labelWidth : 80,
				triggerAction : 'all',
				hideMode : 'offsets',
				forceSelection : true,
				fieldLabel : "操作原因",
				emptyText : "请选择",
				editable : false
	
			},{
				
				xtype : 'radiogroup',
				id : 'batchCancelPageType',
				labelWidth : 100,
				width : 260,
				fieldLabel: "是否创建退单",
				name : 'type',
				items : [ {
					boxLabel : '创建',
					name : 'type',
					inputValue : '1',
					checked : true
				}, {
					boxLabel : '不创建',
					name : 'type',
					inputValue : '2'
				} ]
	
			}

			]
		};
		me.buttons = [
		              
			{ 
				text: "保存", 
				handler : me.batchCancelAjax 
			},{
				text: "关闭",
				handler :me.closeWindow
			}
		              
			];
		me.callParent(arguments);
	},
	
	batchCancelAjax :function (btn){
		
		var text =  Ext.getCmp("orderInfoPageMessage").getValue();//备注
		
		var code =  Ext.getCmp("orderInfoOperationReason").getValue(); //取消原因编码
		
		
		var batchCancelPagetype = Ext.getCmp("batchCancelPageType").getChecked()[0].inputValue;
	
		var orderSns = orderSnValue;
	
		if(null == text ||  "" ==text || 0 == text.length ){
			alert("备注不能为空！");
			return;
		}
		
		if(null == code ||  "" ==code || 0 == code.length ){
			alert("取消原因不能为空！");
			return;
		}
	
		createAjaxData("batchCancel",  basePath + "/custom/orderStatus/batchCancel", confirmSuccFun, doFailurefun, {"orderSns":orderSns,"message": text,"code":code,"type":batchCancelPagetype}, 100000);
	
	},
	
	closeWindow : function(btn){
		
		var win = btn.up("window");
		win.close();
	},
	
	initPage : function(form) {
		
	}
});

/*function confirmSuccFun (id, response, opts) {
	
	if("batchCancel"==id){
		
		var respText = Ext.JSON.decode(response.responseText);
		if(respText.success == "true"){

			Ext.Msg.alert("验证", "订单已放入队列！");

			//parent.FormEditWin.close();
			
			var win = btn.up("window");
			win.close();
  
		}else{
			orderInfoJsonStore.load();
			
			errorMsg("结果", respText.msg);
			
			//cannelOrderInfoWin.close();
			
			var win = btn.up("window");
			win.close();
			
		}
		
	} else {
		var respText = Ext.JSON.decode(response.responseText);
		if(respText.success == "true"){
			Ext.Msg.alert("验证", "订单已放入队列！");
			
			orderInfoJsonStore.load();
			
		}else{
			orderInfoJsonStore.load();
			
			errorMsg("结果", respText.msg);
		}
	}
	
}*/

/*function doFailurefun(){
	var respText = Ext.JSON.decode(response.responseText);
	errorMsg("结果", respText.msg);
}*/