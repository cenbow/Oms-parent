<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>
<html>
	<head>
		<title>保证金数据导入</title>
		<style type="text/css">
	</style>
	<script type="text/javascript">
		var basePath = '<%=basePath%>';
	</script>
	
	
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script >
	<script type="text/javascript">

	var questionDataStoreList = null;
	Ext.Loader.setConfig({enabled: true});
	//Ext.Loader.setPath('Ext.ux', 'ext4/ux');
	// Ext.Loader.setPath('Ext.ux', basePath+'/ext4/ux');
	Ext.require(['*']);
	var importUrl = basePath+"/custom/orderSettleBill/exportDeposit.spmvc";

	var getSystemShippingUrl = basePath+"/custom/orderSettleBill/getSystemShipping.spmvc";
	
	Ext.define('orderBillType', {
		extend: 'Ext.data.Model',
		fields: [
			{name: 'v', type: 'string'},
			{name: 'n',  type: 'string'}
		]
	});
	
	Ext.define('orderSystemShipping', {
		extend: 'Ext.data.Model',
		fields: [
			{name: 'shipping_id'},
			{name: 'shipping_code'},
			{name: 'shipping_name'},
			{name: 'shipping_desc'},
			{name: 'insure'}, //'保价费用，单位元，或者是百分数，该值直接输出为报价费用',
			{name: 'support_cod'}, //'是否支持货到付款，1，支持；0，不支持',
			{name: 'enabled'},  // '该配送方式是否被禁用，1，可用；0，禁用',
			{name: 'shipping_print'}, //'打印模板',
			{name: 'shipping_print2'},// '货到付款打印模板',
			{name: 'is_receive_print'}, //'是否是货到付款模板',
			{name: 'model_img'}, //'模板图片',
			{name: 'defalut_delivery'} //'是否是默认配送方式',
		]
	});
	
	var shippingStore = new Ext.data.Store({
		model:"orderSystemShipping",
		proxy:{
			type: 'ajax',
			url: getSystemShippingUrl,
			reader: {
				type:'json'
			}
		}
	});
	
	//配送方式
	var shippingCombo = createComboBoxLocal(shippingStore,'exportTicketPageShippingId', 'shippingName','shippingId', 'remote','配送方式', 230, 80, 'shippingId', '请选择配送方式');

	Ext.getCmp("exportTicketPageShippingId").setDisabled(true);//置灰配送方式	
	
	//问题单类型   0:订单号/退单号; 1:外部交易号。
 	var selectLogisType = [
		{v: '0', n: '订单号/退单号'},
		{v: '1', n: '外部交易号'}
	]; 
	
	//1.订单结算2.订单货到付款结算3.退单退款方式结算',
	var selectBillType = [
    //  {v: '-1', n: '请选择业务类型'},
   		{v: '1', n: '订单结算'},
   		{v: '2', n: '订单货到付款结算'}        
	]; 
	
	var logisTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'orderBillType',
		data: selectLogisType
	});
	
	var billTypeDataStore = Ext.create('Ext.data.Store', {
		model: 'orderBillType',
		data: selectBillType
	});


	
	//订单号，外部交易号，退单号
	var logisTypeCombo = createComboBoxLocal(logisTypeDataStore, "exportTicketPageOrderCodeType", 'n','v', "local", "参数类型", 220, 90, "orderCodeType","请选择参数类型");

	//业务类型
	var billTypeCombo = createComboBoxLocal(billTypeDataStore, "exportTicketPageBillType", 'n','v', "local", "业务类型", 220, 90, "billType","请选择业务类型");
	
	var changeQuestionType = function (combo, record, index) {
		if (combo.value== 1) {
			// 缺货物流问题单
	
			
			var path = encodeURI('/pages/excelModel/订单结算单.xls');
			path = encodeURI(path);
			var downFileUrl = basePath+"custom/downloadTemp.spmvc?path=" + path;
			
			
			//var downFileUrl = basePath+'custom/downloadTemp.spmvc?path=' + '/pages/excelModel/订单结算单.xls';
			$("#importTemplateFile").attr('href', downFileUrl);
			
		//	$("#importTemplateFile").attr('href', "javascript:void(0);");
			//清空物流问题单原因;
			//orderQuestionOptionCombo.clearValue();
	//		questionDataStore.removeAll();
			//更新物流问题单原因;
		//	questionDataStore.loadData(mylogistics_question_type, false);
		//	questionDataStore.hiddenName="code";
		} else {
			
			var path = encodeURI('/pages/excelModel/输入外部交易号导出模板.xls');
			path = encodeURI(path);
			var downFileUrl = basePath+"custom/downloadTemp.spmvc?path=" + path;
			
			// 一般问题单
			//var downFileUrl = basePath+'custom/downloadTemp.spmvc?path=' + '/pages/excelModel/订单结算单.xls';
			$("#importTemplateFile").attr('href', downFileUrl);
			//Ext.getCmp("importLogistic").setDisabled(ButtonDis(batchNormalQuestion));
			//orderQuestionOptionCombo.clearValue();
	//		questionDataStore.removeAll();
			//更新物流问题单原因;
	//		questionDataStore.loadData(questionDataStoreList,false);
		//	questionDataStore.hiddenName="code";
		}
	};
	//logisTypeCombo.addListener('change',changeQuestionType);
	
	var changeQuestionType = function (combo, record, index) {
		if (combo.value== 2) {
			Ext.getCmp("exportTicketPageShippingId").setDisabled(false);	
		} else{
			Ext.getCmp("exportTicketPageShippingId").setDisabled(true);
		}
	}
	
	billTypeCombo.addListener('change',changeQuestionType);

	Ext.onReady(function() {
		
		var billNo = $("#billNo").val() == null ?  "": $("#billNo").val();
		
		var returnSettlementType = parent.returnSettlementTypeOptionCombo.getValue();
	
		Ext.create('Ext.form.Panel', {
			url : importUrl+"?billNo="+billNo+"&returnSettlementType="+returnSettlementType,
			waitMsg : 'Uploading your file...',
			//title : '文件上传',
			width : 500,
			height :  300,
			bodyPadding : 10,
			//frame : true,
			//renderTo : 'impOrderLogisticsQuestionDiv',
			renderTo:Ext.getBody(),
			items : [ {
				//frame : true,
				xtype:'container',
				layout : "column", 
				items : [
					//	shippingCombo,
				     //   logisTypeCombo
				
				]
			} , {
			
				xtype:'container',
				layout : "column", 
				items : [
				
				      	 {
							margin: '4 0 0 0',
							id: 'uploadFileName',
							xtype : 'filefield',
							name : 'myfile',
							fieldLabel : '批量XLS文件',
							labelWidth : 90,
							width : 250,
							msgTarget : 'side',
							allowBlank : false,
							anchor : '100%',
							buttonText : '请选择文件...'
						}//,
					//	billTypeCombo
				]
			},{
				align : "center",
				frame : true,
				xtype:'container',
				style: {
					padding: '10px',
					margin: '20px'
				},
				layout : "column"
		
			} , {
				align : "center",
				frame : true,
				xtype:'container',
				layout : "column",
				style: {
					padding: '10px',
					margin: '20px'
				},
				html:'<a id="importTemplateFile" style="color:red" href = "'+ basePath+'custom/downloadTemp.spmvc?path=' +encodeURI( encodeURI('/pages/excelModel/保证金调整单.xls'))+'">下载导出模板文件（简体中文）</a>'
			} ],
			buttons : [ {
				id : 'inputIsDerivedPageImportOrder',
				text : '导入调整单',
				margin: 10,
		//		disabled : true,
				handler : function() {
			
					var form = this.up('form').getForm();
				
			//		var inputType = Ext.getCmp('exportTicketPageOrderCodeType').getValue(); //参数类型
					
		//			var billType = Ext.getCmp('exportTicketPageBillType').getValue(); //业务类型
					
	//				var shippingId = Ext.getCmp('exportTicketPageShippingId').getValue();  //配送方式
			
					var channelCode = parent.returnSettlementTypeOptionCombo.getValue(); //退单保证金状态
					
					if(null  ==channelCode || "" == channelCode){
						alert("请选择退款类型！");
						return;
					}
					/* 
					if(null  ==billType || "" == billType){
						alert("请选择业务类型！");
						return;
					}
					
					if(null  ==inputType || "" == inputType){
						alert("请选择参数类型！");
						return;
					}
					 */
					 
					//货到付款结算单，配送方式不能为空
				/* 	if(2 == billType){
						if(null  ==shippingId || "" == shippingId){
							alert("请选择配送方式！");
							return;
						}
					} */
					
					if (form.isValid()) {
						var uploadFileName = Ext.getCmp('uploadFileName');
						var fileName = uploadFileName.getValue();
						if (fileName && fileName != '') {
							if (fileName.lastIndexOf(".xls") == -1) {
								errorMsg("结果", "选择文件不是[.xls]格式文件");
								return ;
							}
						}
						
					//	Ext.getCmp("inputIsDerivedPageImportOrder").setDisabled(true);//导入按钮置灰;
						
						form.submit({
							success : function(f, action) {
								if (action.result.success  == "true") {
							
								
									//	parent.Ext.getCmp("orderSettleBillTBarAdd").setDisabled(true);
								
										var billNo = $("#billNo").val() == null ?  "": $("#billNo").val();
					
								//	    parent.addOrderTicketJsonStore.getProxy().url=basePath+"/custom/orderSettleBill/createOrderSettleBill.spmvc?billNo="+billNo;
										parent.addOrderTicketJsonStore.getProxy().url=basePath+"/custom/orderSettleBill/createOrderSettleBill.spmvc?billNo="+billNo;
								
										parent.addOrderTicketJsonStore.load();
										parent.parent.orderDepositJsonStore.getProxy().url= basePath+"/custom/orderSettleBill/orderDepositList.spmvc";
										parent.parent.orderDepositJsonStore.load();
										parent.FormEditWin.close();
					
							
								} else {
									alertMsg("提示",action.result.msg);
								}
								
							//	Ext.getCmp("exportTicketPageInputType").setDisabled(false);
							},
							failure : function(response,options) {							
								alert("验证失败！");		
							//	Ext.getCmp("exportTicketPageInputType").setDisabled(false);
							}
						});
					}
				}
			} , {
				text: '关闭',
				margin: 10,
				handler:function(){
					var form = this.up('form').getForm();
					form.destroy();
					parent.FormEditWin.close();
				}
			}],
			buttonAlign: 'center'
		});
	});

	function doSuccessfun(id, response, opts) {
		if ("questionData" == id) {
			var respText = Ext.JSON.decode(response.responseText);

		} else {
			var respText = Ext.JSON.decode(response.responseText);
	
		}
	}

	function doFailurefun(id, response, opts) {
		errorMsg("结果", respText.msg);
	}

	</script>
	</head>
	<body>
		<input type="hidden" id="billNo" name="billNo" value="${billNo}"/>
	<!-- <div id="impOrderLogisticsQuestionDiv" style="height: 400px;"> -->
	
	</body>
</html>