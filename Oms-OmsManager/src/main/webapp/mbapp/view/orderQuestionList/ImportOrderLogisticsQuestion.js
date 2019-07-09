/**
 *批量导入问题单
 **/

var selectQType = new Array();

var questionDataStoreList = null;

var batchNormalQuestion = null;

var mylogistics_question_type = new Array()

//createAjaxDataBySyn("questionData", basePath+"/custom/customDefine/customDefineList.spmvc" , doSuccessfun, doFailurefun, {"type":3}, 100000);

createAjaxDataBySyn("logisType", basePath+"/custom/orderQuestion/gotoImportQuestionPage1.spmvc" , doSuccessfun, doFailurefun, 100000);

Ext.define("MB.view.orderQuestionList.ImportOrderLogisticsQuestion", {
	extend : "Ext.window.Window",
	alias : "widget.importOrderLogisticsQuestion",
	title : "问题单操作-批量导入问题单",
    id:'ImportOrderLogisticsQuestionWin',
	width : 500,
	waitMsg : 'Uploading your file...',
	height : 400,
	layout : "fit",
	initComponent : function() {
		var me = this;

		Ext.define('orderParam', {
			extend: 'Ext.data.Model',
			fields: [
				{name: 'v', type: 'string'},
				{name: 'n',  type: 'string'}
			]
		});
	
	/*	var selectQType = new Array();
		if (batchNormalQuestion == 1) {
			selectQType.push({v: '0', n: '一般问题单'});
		}
		if (batchTaskOutStock == 1 || batchTasklogstcQust == 1){
			selectQType.push({v: '1', n: '物流问题单'});
		}*/
		
	/*	var logisTypeDataStore = Ext.create('Ext.data.Store', {
			model: 'orderParam',
			data: selectQType
		});*/
		
	/*	Ext.define('Question', {
			extend: 'Ext.data.Model',
			fields: [
				{name: 'code',  type: 'string'},
				{name: 'name',  type: 'string'}
			]
		});
		
		//问题原因类型
		var questionDataStore = Ext.create('Ext.data.Store', {
			model: 'Question',
			data: questionDataStoreList
		});
		*/
		
		
/*		var mylogistics_question_type = new Array();
		if (batchTasklogstcQust == 1){
			mylogistics_question_type.push({code:"37" ,name:"短拣"});
		}
		if (batchTaskOutStock == 1) {
			mylogistics_question_type.push({code:"38" ,name:"短配"});
		}*/
		
		me.items = {
				
			xtype : "form",
			margin : 5,
			border : false,
			frame : true,
			url: basePath+"/custom/OrderLogisticsQuestion/importOrderLogisticsQuestionList.spmvc",
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 70
			},
			items : [
			         
			     	{	
						xtype : 'combo',
						store : 'LogisTypeStore',
						id : 'orderLogisticsQuestionSelectLogisType',
						name : 'logType',
						displayField : 'n',
						valueField : 'v',
						queryMode : "local",
						width : 260,
						fieldStyle : 'width:'+ (260 - 80 - 26),
						labelWidth : 80,
						triggerAction : 'all',
						hideMode : 'offsets',
						forceSelection : true,
						fieldLabel : "问题单类型",
						emptyText : "请选择问题单类型",
						editable : false,
						listeners:{
							'change':function(combo, record, index){
								if (combo.value== 1) {
							        
									var orderLogisticsQuestionCode = Ext.getCmp("orderLogisticsQuestionCode");
									
									orderLogisticsQuestionCode.clearValue();//清空物流问题单原因;

									orderLogisticsQuestionCode.getStore().removeAll();
									orderLogisticsQuestionCode.getStore().loadData(mylogistics_question_type, false);//更新物流问题单原因;
									orderLogisticsQuestionCode.getStore().hiddenName="code";
							
									if(orderLogisticsQuestionCode.getValue()=='37'){
								
										var path = encodeURI('/pages/excelModel/短拣物流问题单模板.xls');
										path = encodeURI(path);
										var downFileUrl = basePath+"custom/downloadTemp.spmvc?path=" + path;
										$("#importTemplateFile").attr('href', downFileUrl);
										
									} else{
								
										var path = encodeURI('/pages/excelModel/短配物流问题单模板.xls');
										path = encodeURI(path);
										var downFileUrl = basePath+"custom/downloadTemp.spmvc?path=" + path;
										$("#importTemplateFile").attr('href', downFileUrl);
							
									}
								
								} else {
									 
									var path = encodeURI('/pages/excelModel/问题单模板.xls');// 一般问题单
									path = encodeURI(path);
									var downFileUrl = basePath+"custom/downloadTemp.spmvc?path=" + path;
									$("#importTemplateFile").attr('href', downFileUrl);
									
									Ext.getCmp("orderLogisticsQuestionSelectLogisType").setDisabled(ButtonDis(batchNormalQuestion));
									
									var orderLogisticsQuestionCode =  Ext.getCmp("orderLogisticsQuestionCode");
									
									orderLogisticsQuestionCode.clearValue();				
									orderLogisticsQuestionCode.getStore().removeAll();							
									orderLogisticsQuestionCode.getStore().loadData(questionDataStoreList,false);//更新物流问题单原因;	
									orderLogisticsQuestionCode.getStore().hiddenName="code";
								}
							}
						}
					
					},{	
						xtype : 'combo',
						store : 'QuestionStore',
						id : 'orderLogisticsQuestionCode',
						name : 'code',
						displayField : 'name',
						valueField : 'code',
						queryMode : "local",
						width : 260,
						fieldStyle : 'width:'+ (260 - 80 - 26),
						labelWidth : 80,
						triggerAction : 'all',
						hideMode : 'offsets',
						forceSelection : true,
						fieldLabel : "问题单原因",
						emptyText : "请选择",
						editable : false,
						listeners:{
							'change':function(combo, record, index){
								if (combo.value== "37") {
									var path = encodeURI('/pages/excelModel/短拣物流问题单模板.xls');
									path = encodeURI(path);
									var downFileUrl = basePath+"custom/downloadTemp.spmvc?path=" + path;
									$("#importTemplateFile").attr('href', downFileUrl);
								} else{
									var path = encodeURI('/pages/excelModel/短配物流问题单模板.xls');
									path = encodeURI(path);
									var downFileUrl = basePath+"custom/downloadTemp.spmvc?path=" + path;
									$("#importTemplateFile").attr('href', downFileUrl);
								}
							}
						}
					
						
					},
					{
						xtype : 'radiogroup',
						id : 'orderLogisticsQuestionMainChild',
						width : 240,
						name : 'mainChild',
						items : [ {
							boxLabel : '订单号',
							name : 'mainChild',
							inputValue : 'main',
							checked : true
						}, {
							boxLabel : '交货单',
							name : 'mainChild',
							inputValue : 'child'
						} ]
				
					/*	xtype : 'combo',
						store : 'MainChildStore',
						id : 'orderLogisticsQuestionPageMainChildType',
						name : 'mainChildType',
						displayField : 'n',
						valueField : 'v',
						queryMode : "local",
						width : 230,
						fieldStyle : 'width:'+ (230 - 80 - 26),
						labelWidth : 80,
						triggerAction : 'all',
						hideMode : 'offsets',
						forceSelection : true,
						fieldLabel : "订单类型",
				//		emptyText : "请选择类型",
						editable : false*/
						
						
					}
					,{
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
					}, {
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
						'<li>4、Excel版本为Excel 97-2003,请使用模板的文档作为导入数据文档！！</li><li>5、请按照模板的规定的格式来排列数据！！</li><li>6、物流必须选择交货单！！</li></ul></div>'
					} , {
						align : "center",
						frame : true,
						xtype:'container',
						layout : "column",
						style: {
							padding: '5px',
							margin: '10px'
						},
						html:'<a id="importTemplateFile" style="color:red" href = "javascript:void(0);">下载批量处理模板文件（简体中文）</a>'
					}
					
			         ]
		};
		me.buttons = [
		              
			{ 
				text: "导入问题单", 
				handler : me.importOrderLogisticsQuestionListAjax 
			},{
				text: "关闭",
				handler : me.closeWindow			
			}
		              
		];
		me.callParent(arguments);
	},
	importOrderLogisticsQuestionListAjax : function (btn){
		
		var win = btn.up("window");
		
		var formPanel = win.down('form');

		if("1" == Ext.getCmp("orderLogisticsQuestionSelectLogisType").getValue()){
			var orderLogisticsQuestionMainChild = Ext.getCmp("orderLogisticsQuestionMainChild").getChecked()[0].inputValue;
			
			if("main"== orderLogisticsQuestionMainChild){
				alert("导入物流问题单时，请使用交货单类型!");
				return;
				
			}
			
		}
		
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
						//	orderQuestionJsonStore.load();
							//var orderInfoGridWin = Ext.getCmp('orderQuestionGridId');
							
							Ext.getCmp('orderQuestionGridId').getStore().load();
							console.dir(Ext.getCmp('orderQuestionGridId'));
							
						} catch (e) {
							
							//parent.orderQuestionJsonStore.load();
							Ext.getCmp('orderQuestionGridId').getStore().load();
							
							console.dir(Ext.getCmp('orderQuestionGridId'));
						}
				
					    var win = Ext.getCmp('ImportOrderLogisticsQuestionWin');
				        if (win) { 
				        	win.close();
				        }
						
					});
				}
			},
			failure: function(formPanel, action){
				errorMsg("结果", action.result.msg);
			},
			waitMsg:'Loading...'
		};
		
		formPanel.getForm().submit(json);
		
	},
	closeWindow : function(btn){
		
		var win = btn.up("window");
		win.close();
		
	},
	initPage : function(form) {
		
	}
});

function doSuccessfun(id, response, opts) {
	if ("questionData" == id) {
		var respText = Ext.JSON.decode(response.responseText);
		var data = respText.data;
		var arr = [];
		for ( var i = 0; i < data.length; i++) {
			var o = {};
			o.code = data[i].code;
			o.name = data[i].name;
			arr.push(o);
		}
		questionDataStoreList = arr;
	} else if("logisType"== id){
		
		var respText = Ext.JSON.decode(response.responseText);
		var data = respText.data;
		
		if(respText.isok == true){
	
			if (data.batchNormalQuestion == 1) {
				var batchNormalQuestion = data.batchNormalQuestion;
				selectQType.push({v: '0', n: '一般问题单'});
			}
			if (data.batchTaskOutStock == 1 || data.batchTasklogstcQust == 1){
				selectQType.push({v: '1', n: '物流问题单'});
			}
			
			if (data.batchTasklogstcQust == 1){
				mylogistics_question_type.push({code:"37" ,name:"短拣"});
			}
			if (data.batchTaskOutStock == 1) {
				mylogistics_question_type.push({code:"38" ,name:"短配"});
			}
			
		}
		
		
	} else {
		var respText = Ext.JSON.decode(response.responseText);
		if (respText.success == "true") {
			questionDataStore.load();
		} else {
			errorMsg("结果", respText.msg);
		}
	}
}

function doFailurefun(id, response, opts) {
	
	var respText = Ext.JSON.decode(response.responseText);
	
	errorMsg("结果", respText.msg);
}
