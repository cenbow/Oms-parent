/**
 *批量返回正常单 
 **/
Ext.define("MB.view.orderQuestionList.ReturnNormal", {
	extend : "Ext.window.Window",
	alias : "widget.returnNormal",
	title : "问题单操作-批量返回正常单",
    id:'returnNormalWin',
	width : 500,
	height : 400,
	layout : "fit",
	initComponent : function() {
		var me = this;

		var occDis = true;
		var oqDesc = "";
		if (questionType == 0) {	
			occDis = false;
			oqDesc = "此问题单是OMS问题单。";
		} else {
//			oqDesc = "此问题单是物流问题单。";
		}
		me.items = {
			xtype : "form",
			margin : 5,
			border : false,
			frame : true,
			url: basePath+"/custom/orderStatus/batchNormal.spmvc",
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 70
			},
			items : [ 
				{
					id:'orderInfoPageMessage',
					xtype: 'textarea',
					name : 'message',
					fieldLabel : '备注',
					labelWidth : 40,
					width: 300,
					height : 100
				
				},{
					xtype : "hidden",
					name : 'hasOccupyStock',
					fieldLabel : "是否占用库存",
					value : '0'
				},{
					anchor : '100%',
					html: '<font color="red">' + oqDesc + '</font>'
				} ,{
					xtype: 'hidden',
					fieldLabel: '订单编号',
					name: 'orderSns',
					value: orderSns
				} , {
					xtype:'hidden',
					fieldLabel: '是否历史订单',
					name: 'isHistory',
					value: isHistory
				},{
					xtype:'hidden',
					fieldLabel: '问题单类型',
					name: 'questionType',
					value: questionType
				},{
					xtype:'hidden',
					fieldLabel: '问题单类型',
					name: 'type',
					value: retrunNormalQuestionType
				},{
					xtype:'hidden',
					fieldLabel: '问题单类型',
					name: 'mainChild',
					value: retrunNormalMainChild
				}
			
			]
		};
		me.buttons = [
		              
			{ 
				text: "保存", 
				handler : me.returnNormalAjax 
			},{
				text: "关闭",
				handler : me.closeWindow			
			}
		              
		];
		me.callParent(arguments);
	},
	returnNormalAjax :function (btn){
		
		var win = btn.up("window");
		
		var formPanel = win.down('form');
		
	//	var formPanel = btn.up("form");
		
		if(!formPanel.getForm().isValid()){
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}
		
		var json = {
			success: function(formPanel, action){
				if (action.result.success  == "false") {
					errorMsg("结果", action.result.msg);
				} else{
					Ext.Msg.alert('结果',action.result.msg,function(xx){
						try{
							orderSns = new Array();
				
							// Ext.getCmp("orderQuestionGridId");
			
							 Ext.getCmp("orderQuestionGridId").getStore().load();
						} catch (e) {
							 Ext.getCmp("orderQuestionGridId").getStore().load();
						}
						
					//	parent.FormEditWin.close();
			
						var win = btn.up("window");
						win.close();
					});
				}
			},
			failure: function(formPanel, action){
				errorMsg("结果", action.result.msg);
			},
			waitMsg:'Loading...'
		};
		
		formPanel.getForm().submit(json);
		
	/*	var text =  Ext.getCmp("orderInfoPageMessage").getValue();//备注
		
		var code =  Ext.getCmp("orderInfoOperationReason").getValue(); //取消原因编码
	
		var orderSns = parent.orderSnValue;
	
		if(null == text ||  "" ==text || 0 == text.length ){
			alert("备注不能为空！");
			return;
		}
		
		if(null == code ||  "" ==code || 0 == code.length ){
			alert("取消原因不能为空！");
			return;
		}
	
		createAjaxData("batchCancel",  basePath + "/custom/orderStatus/batchCancel", confirmSuccFun, doFailurefun, {"orderSns":orderSns,"message": text,"code":code}, 100000);
	*/
		
	},
	closeWindow : function(btn){
		
		var win = btn.up("window");
		win.close();
		
	},
	
	initPage : function(form) {
		
	}
});
