/**
 * 拉单工具
 */
Ext.onReady(function(){
	//一级订单来源
	var channelDataStore=new Ext.data.SimpleStore({
		model: Ext.create('Ext.data.Model',{
			fields: [
			            {name: 'shortText'},
			            {name: 'shortName'}
			        ]
		}),
		autoLoad : true,
		proxy: {
			type: 'ajax',
			actionMethods: {
				read: 'POST'
			},
			url: basePath + 'custom/common/loadOrderShopData?dataType=0',//0拉单、1转单、2发货、3退单
			reader: {
				type:'json'
			}
		}
	});
	var channelOptionCombo=new Ext.form.ComboBox({
		store: channelDataStore,
		id: "type",
		name: "type",
		hiddenName: "type",
		displayField: "shortText",
		valueField: "shortName", 
		mode: "remote",
		height:35,
		width:260,
		triggerAction: 'all',
		selectOnFocus:true,
		allowBlank:true,
		forceSelection:true,
		emptyText: "请选择渠道",
		hideLabel: true,
		editable: false// 不可输入
	});
	
	
	
	
    
    var fp =new Ext.FormPanel({
        title: '手动下载各渠道订单',
//        standardSubmit : true,
		url:basePath+'/custom/pullAndTurn/downloadTrades.ac',
		method:'post',
        frame: true,
        fileupload:true,
        fieldDefaults: {
            labelWidth: 200,
            labelStyle: 'color:green;padding-left:4px'
        },
        renderTo:'form-ct',
        bodyStyle: 'margin-left:15%;',
        buttonAlign : 'center',
        layout : "column", // 从左往右的布局
        items: [{
        	
	        columnWidth: .4,
	        height: 600,
	        items: {
	            xtype: 'fieldset',
	            title: '第一步：填入外部交易号',
	            height: 550,
	            items: [{
	                xtype: 'textarea',
	                name: 'dealCodes',
	                hideLabel: true,
	                width:'100%',
	                height:480
	                
	            },{
	            	xtype: 'label',
	            	text:"填入的外部交易号，多个交易号用英文逗号分隔"
	            	
	            }]
	        }
	    },
	    {	
	    	bodyStyle: 'padding-left:50px;',
	    	columnWidth: .35,
	        items: [{
	            xtype: 'fieldset',
	            title: '第一步：或者导入外部交易号文件',
	            height: 150,
	            autoHeight: true,
	            layout: {
				        align: 'middle',
				        pack: 'center',
				        type: 'hbox'
				},
	            items: [
	                    {
	            	width:'90%',
	            	hideLabel: true,
	                xtype: 'fileuploadfield',
	                id: 'form-file',
	                emptyText: '请选择txt格式文档，一行一个外部交易号',
	                name: 'codeFile',
	                buttonText: '选择文件',  
                    buttonCfg: {  
                        iconCls: 'add'  
                    }
	            }
	                    ]
	        },{
	            xtype: 'fieldset',
	            title: '第二步：选择订单渠道',
	            height: 150,
	            autoHeight: true,
	            layout: {
				        align: 'middle',
				        pack: 'center',
				        type: 'hbox'
				},
	            items: [channelOptionCombo]
	        },{
	            xtype: 'fieldset',
	            height: 150,
	            title: '第三步：执行',
	            layout: {
				        align: 'middle',
				        pack: 'center',
				        type: 'hbox'
				},
	            autoHeight: true,
//	            layout : "column", // 从左往右的布局
	            items: [{
//	                    bodyStyle: 'margin-left:15px;',
	                    items:{
		            	height:30,
		            	width:140,
		                xtype: 'button',
		                text: '开始获取渠道订单',
		                handler: function(){
		                	fp.getForm().submit({
		                		waitMsg : '请稍等.....',
		                        success: function(form,action) {
		                        	Ext.Msg.alert("结果",action.result.data);
		                        },  
		                        failure: function(form,action) {
		                        	Ext.Msg.alert("结果",action.result.data);
		                        }
						});  
	                }
	            }},
	            {xtype : "displayfield", columnWidth: .04} ,
	            { 
//	            	bodyStyle: 'margin-left:15px;',
                    items:{
	            	height:30,
	            	width:140,
	                xtype: 'button',
	                text: '重置',
	                handler: function(){
	                    fp.getForm().reset();
	                }
	            }}]
	        }]
	    }
	    ]
    });
    //文件上传使用编码
    $("form:first").attr("enctype","multipart/form-data");
    function updateChannelInfo(){
		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : basePath+ '/manager/updateChannelInfoNew.ac',
			method : 'post',
			success : function(response) {
				var respText = Ext.util.JSON.decode(response.responseText); 
				$("#channelInfo").html("<span style='color : green;'>"+respText+"ms</span>");
				
			},
			failure : function(response,options) {
				Ext.Msg.alert("结果","更新渠道信息失败！");
			}
		});
    }
    
})