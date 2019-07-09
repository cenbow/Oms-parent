
Ext.Loader.setConfig({
	enabled: true
});

Ext.require([
	'Ext.grid.*',
	'Ext.data.*',
	'Ext.selection.CheckboxModel',
    'MB.ComboModel',
    'MB.Channel',
    'MB.Shop',
    'MB.OrderInfoQuery',
    'MB.Area',
    'Ext.dom.Element'
]);

var orderInfoForm = null;

var batchIssuedUrl = basePath+"/custom/orderStatus/mandatoryIssued.spmvc";

Ext.onReady(function(){
	Ext.QuickTips.init();
	
	var dItems = [{
		xtype: 'toolbar',
		dock: 'bottom',
		ui: 'footer',
		layout: {
			pack: 'center'
		},
		items: [
		  {
			minWidth: 80,
			text: '强制下发',
			itemId: 'issued',
			handler: mandatoryIssued
		  }
		  ]
	}];

	orderInfoForm = Ext.widget('form', {
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
	//	layout : "column",
		border: false,

		frame: true,
		collapsible: true,
		title: "强制下发",
		fieldDefaults: {
			labelAlign: 'right'
		},
	
		items: [{
	//		columnWidth: .55,
			
	        height: '100%',
		        
		/*	xtype: 'fieldcontainer',
			labelStyle: 'font-weight:bold;padding:0;',
			layout: 'hbox',
			defaultType: 'textfield',
			fieldDefaults: {
				labelAlign: 'right'
			},*/
		        
			items: [{ //line 1
					xtype: 'textarea',
	                name: 'orderSns',
	                id:'mandatoryIssuedOrderSns',
	                hideLabel: true,
	                width:'100%',
	                height:'80%',
				},{
	            	xtype: 'label',
	            	text:"填入的订单号，多个交易号用英文逗号分隔"
					//html: '<div><h2>注意</h2> <ul><li>1、数据量不能超过1000条!</li><li>2、请选择"输入导出类型",再下载模版!</li>'
	            }	
		   ]
		
		}],
		dockedItems : dItems
		
	});
	
	var mainPanel = Ext.create(Ext.panel.Panel, {
		renderTo: Ext.getBody(),
		
		width: '100%',
		//bodyPadding: 2,
		//resizable: false,
		//autoHeight: true,
		items: [orderInfoForm]
	});
	
	function mandatoryIssued(){	
		
		var orderSns =  Ext.getCmp("mandatoryIssuedOrderSns").getValue(); 
		
		if(null == orderSns || "" == orderSns || orderSns.length==0){
			alert("订单号不能为空！");
			return;
		}
		
		createAjaxData("mandatoryIssued", batchIssuedUrl, confirmSuccFun,doFailurefun, {"orderSns":orderSns}, 100000);
	}

	/**
	 *强制下发 
	 ***/
	function confirmSuccFun (id, response, opts) {
		
		if("mandatoryIssued"==id){
			var respText = Ext.JSON.decode(response.responseText);
			if(respText.success == "true"){
			//	orderInfoJsonStore.load();
			
				alertMsg("结果", "下发成功！")
		//		cannelOrderInfoWin.close();
			}else{
			//	orderInfoJsonStore.load();
				errorMsg("结果", respText.msg);
				//cannelOrderInfoWin.close();
			}
			
		} else {
			var respText = Ext.JSON.decode(response.responseText);
			if(respText.success == "true"){
				alertMsg("结果","下发成功！")
		//		orderInfoJsonStore.load();
			}else{
		//		orderInfoJsonStore.load();
				errorMsg("结果", respText.msg);
			}
		}
		
	}

	function doFailurefun(){
		var respText = Ext.JSON.decode(response.responseText);
		errorMsg("结果", respText.msg);
	}
	
});