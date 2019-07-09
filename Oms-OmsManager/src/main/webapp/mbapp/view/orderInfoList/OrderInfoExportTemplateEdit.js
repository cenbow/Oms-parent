Ext.define("MB.view.orderInfoList.OrderInfoExportTemplateEdit", {
	extend : "Ext.window.Window",
	alias : "widget.orderInfoExportTemplateEdit",
	title : "输入订单号和外部交易号导出",
    id:'winManageFile',
	width : 500,
	height : 300,
	
	layout :{
	
		   type: 'vbox',
		   pack: 'center',
		   align:'center',
		   padding:'5'
		
	},
	
	initComponent : function() {
		
		var me = this;
		
		me.items = {
			xtype : "form",
			margin : 5,
			border : false,
		
			url: basePath + "/custom/orderInfo/inputIsDerivedByOrderSnOrOrderOutSn.spmvc",

			items : [ {
				xtype : 'combo',
				store : 'OrderInfoInputExportTemplateTypeStore',
				id : 'inputIsDerivedPageInputType',
				name : 'inputType',
				displayField : 'n',
				valueField : 'v',
				queryMode : "local",
				width : 400,
			
				fieldStyle : 'width:' + (230 - 80 - 26),
				labelWidth : 100,
				triggerAction : 'all',
				hideMode : 'offsets',
				forceSelection : true,
				fieldLabel : "输入导出类型",
				emptyText : "请输入导出类型",
				editable : false,
				listeners : {
					'change' : me.changeTemplateType
				}

			}, {
				margin : '4 0 0 0',
				id : 'uploadFileName',
				xtype : 'filefield',
				name : 'myfile',
				fieldLabel : '批量XLS文件',
				labelWidth : 100,
		
				width : 400,
				msgTarget : 'side',
				allowBlank : false,
				buttonText : '请选择文件...'

			}, {
			
				frame : true,
				xtype:'container',
				layout : "column",
				style: {
					padding: '10px',
					margin: '20px'
				},
				html:'<a id="importTemplateFile" style="color:red" href = "javascript:void(0);">下载导出模板文件（简体中文）</a>'
			
			}
			]
		};
		me.buttons = [ {
			id : 'inputIsDerivedPageImportOrder',
			text : '查询',
			margin: 10,
			handler : me.queryImportOrderSn
		}, {
			text : "关闭",
			handler : me.closeWindow
			
		} ];
		
		
		me.callParent(arguments);
	},
	
	queryImportOrderSn : function (btn){
		
		var form = btn.up('window').down("form");

		var inputType = Ext.getCmp('inputIsDerivedPageInputType').getValue();
		
		if(null  ==inputType || "" == inputType){
			alert("请选择输入的类型！");
			return;
		}
		
		if (form.isValid()) {
			var uploadFileName = Ext.getCmp('uploadFileName');
			var fileName = uploadFileName.getValue();
			if (fileName && fileName != '') { 
				if (fileName.lastIndexOf(".xls") == -1) {
					errorMsg("结果", "选择文件不是[.xls]格式文件");
					return ;
				}
			}
		}
		
		var json = {
				success : function(formPanel, action) {
			//		console.dir(action);
	
					var r = decodeURIComponent(action.result.msg);
				
					var respText = Ext.JSON.decode(r);
					
			//		console.log("respText =  "+respText);
		
					var data = respText.root;
				
			//		console.log("data.length =  "+data.length);
					
					var orderSnArr ="";
					
					console.dir(data);
					
					for(var i=0; i<data.length; i++){
			
						var masterOrderSn = data[i].masterOrderSn;
						
						orderSnArr +=masterOrderSn+",";
					
						var addTimeStr = data[i].addTime;
						 data[i].addTime =  getFormatDate(addTimeStr);
						
						var confirmTimeStr = data[i].confirmTime;
						 data[i].confirmTime =  getFormatDate(confirmTimeStr);
						
						var deliveryTimeStr = data[i].deliveryTime;
						 data[i].deliveryTime =  getFormatDate(deliveryTimeStr);
						
					}
					console.dir(Ext.getCmp('orderInfoPageOrderSnArr'));
					Ext.getCmp('orderInfoPageOrderSnArr').setValue(orderSnArr);
				
					var orderInfoGridWin = Ext.getCmp('orderInfoGridId');
					
					orderInfoGridWin.getStore().loadData(respText.root);
	
				    var win = Ext.getCmp('winManageFile');
			        if (win) { 
			        	win.close();
			        }
					
				},
				failure : function(formPanel, action) {
					Ext.msgBox.remainMsg('修改商品', "asdf", Ext.MessageBox.ERROR);
				},
				waitMsg : 'Loading...'
			};
			form.submit(json);
	
	},
	closeWindow : function(btn){
		
		var win = btn.up("window");
		win.close();
	},
	
	changeTemplateType: function(combo, newValue, oldValue) {
		if (combo.value== 1) {
			// 缺货物流问题单
			var path = encodeURI('/pages/excelModel/输入外部交易号导出模板.xls');
			path = encodeURI(path);
			var downFileUrl = basePath+"custom/downloadTemp.spmvc?path=" + path;
	
			$("#importTemplateFile").attr('href', downFileUrl);

		} else {
			
			var path = encodeURI('/pages/excelModel/输入订单号导出模板.xls');
			path = encodeURI(path);
			var downFileUrl = basePath+"custom/downloadTemp.spmvc?path=" + path;
			
			// 一般问题单
			
			$("#importTemplateFile").attr('href', downFileUrl);

		}
	},
	
	initPage : function(form) {
		
		// var orderStatus = Ext.getCmp("order.orderStatus").getValue();
		// var orderStatus = form.findField("orderStatus").getValue();
		// form.findField("orderStatus1").setValue(orderStatus);
		
	}
});


function getFormatDate(millisecond) { 
	
	if(null == millisecond || '' == millisecond){
		return "";
	}

	var date = new Date(millisecond); 

	var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
} 
