<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page.jsp"%>
<%@ include file="/extJs5Script.jsp"%>
<html>
	<head>
		<title>批量修改订单财务金额</title>
		<style type="text/css">
	</style>
	<script type="text/javascript">
		var basePath = '<%=basePath%>';
	</script>
	<script type="text/javascript" src="<%=basePath%>/js/jquery/jquery-1.6.2.min.js"></script >
		<script type="text/javascript" src="<%=basePath%>/js/iss.js"></script>
	<script type="text/javascript">
	Ext.Loader.setConfig({enabled: true});
	//Ext.Loader.setPath('Ext.ux', 'ext4/ux');
	// Ext.Loader.setPath('Ext.ux', basePath+'/ext4/ux');
	Ext.require(['*']);
	var importUrl = basePath+"/custom/orderSettleBill/importOrderMoney.spmvc";

	Ext.onReady(function() {
		Ext.create('Ext.form.Panel', {
			url : importUrl,
			waitMsg : 'Uploading your file...',
			//title : '文件上传',
			width : 688,
			height : 350,
			bodyPadding : 2,
			//frame : true,
			//renderTo : 'impOrderLogisticsQuestionDiv',
			renderTo:Ext.getBody(),
			items : [ {
				//frame : true,
				xtype:'container',
				layout : "column", 
				items : [ {
					margin: '4 0 0 0',
					id: 'uploadFileName',
					xtype : 'filefield',
					name : 'myfile',
					fieldLabel : '批量XLS文件',
					labelWidth : 90,
					width : 483,
					msgTarget : 'side',
					allowBlank : false,
					anchor : '100%',
					buttonText : '请选择文件...'
				}]
			} , {
				align : "center",
				frame : true,
				xtype:'container',
				style: {
					padding: '5px',
					margin: '10px'
				},
				layout : "column", 
				html: '<div><h2>注意</h2> <ul><li>1、数据请去重后再导入!!</li>'+
				'<li>2、请保证“单号”是文本长字符串形式，不要是科学计数法的形式！！</li><li>3、请保证每次批量的数量不超过60000！！</li>' +
				'<li>4、Excel版本为Excel 97-2003,请使用模板的文档作为导入数据文档！！</li><li>5、请按照模板的规定的格式来排列数据！！</li><li>6、订单财务金额只能下调！！</li><li>7、订单号和外部交易号任填即可！！</li></ul></div>'
			} , {
				align : "center",
				frame : true,
				xtype:'container',
				layout : "column",
				style: {
					padding: '5px',
					margin: '10px'
				},
				html:'<a id="importTemplateFile" style="color:red" href = "'+basePath+'custom/downloadTemp.spmvc?path='+ encodeURI(encodeURI("/pages/excelModel/批量修改订单财务金额模板.xls"))+'">下载批量处理模板文件（简体中文）</a>'
			} ],
			buttons : [ {
				id : 'importLogistic',
				text : '导入',
				margin: 8,
				handler : function() {
					var form = this.up('form').getForm();
					if (form.isValid()) {
						var uploadFileName = Ext.getCmp('uploadFileName');
						var fileName = uploadFileName.getValue();
						if (fileName && fileName != '') {
							if (fileName.lastIndexOf(".xls") == -1) {
								errorMsg("结果", "选择文件不是[.xls]格式文件");
								return ;
							}
						}
						form.submit({
							success : function(f, action) {
								if (action.result.success  == "true") {
									Ext.Msg.alert('结果',action.result.msg,function(xx){
										parent.orderQuestionJsonStore.load();
										parent.FormEditWin.close();
									});
								} else {
									alertMsg("提示",action.result.msg);
								}
							},
							failure : function(response,options) {
								alertMsg("验证","失败！");
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

	</script>
	</head>
	<body>
	
	</body>
</html>