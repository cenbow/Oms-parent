Ext.define("MB.view.orderEdit.C2xPropertyEdit", {
	extend: "Ext.window.Window",
	alias: "widget.c2xPropertyEdit",
	title: "订单属性编辑",
	width: 1000,
	height: 600,
	layout: "fit",
	maskDisabled : false,
	modal : true,
	closable: false,
	initComponent: function () {
		this.items = {
			xtype: "form",
			margin: 5,
			border: false,
			frame: true,
			autoScroll : true,
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 150
			},
			items: [{
				xtype: 'form',
				id:'c2xBaseProperty',
				itemId : 'c2xBaseProperty',
				title:'基本属性',
				width: '100%',
				collapsible:true,
				titleCollapse:true,
				defaults: {
					labelWidth: 200,
					columnWidth: 1
				},
				items : [{
					xtype: 'fieldcontainer',
					layout: 'column',
					items: [/*{
						xtype : 'textfield',//用来存储图片路径的隐藏域
						id : 'c2xCloseFlag',
						name : 'closeFlag',
						hidden : true
					},*/{
						xtype : 'textfield',
						id : 'c2xBasePropertyBuyer',
						name : 'buyer',
						fieldLabel : '定制人',
						columnWidth : .33,
						hidden:true,
						readOnly:true
					},{
						xtype : 'textfield',
						id : 'c2xBasePropertyName',
						name : 'name',
						fieldLabel : '商品名称',
						columnWidth : .33,
						hidden:true,
						readOnly:true
					},{
						xtype : 'textfield',
						name : 'diyId',
						fieldLabel : '定制ID',
						columnWidth : .33,
						readOnly:true
					},{
						xtype : 'textfield',
						name : 'num',
						fieldLabel : '购买数量',
						columnWidth : .33,
						readOnly:true
					}]
				},{
					xtype: 'fieldcontainer',
					layout: 'column',
					items: [{
						xtype : 'textfield',
						name : 'sn',
						fieldLabel : '商品6位码',
						columnWidth : .33,
						readOnly:true
					},{
						xtype : 'textfield',
						name : 'clothesID',
						fieldLabel : '商品11位码',
						columnWidth : .33,
						readOnly:true
					},{
						xtype : 'textfield',
						id : 'c2xBasePropertyCost',
						name : 'cost',
						fieldLabel : '定制费用',
						columnWidth : .33,
						hidden:true,
						readOnly:true
					},{
						xtype : 'textfield',
						id : 'c2xBasePropertyTotalPrice',
						name : 'totalPrice',
						fieldLabel : '定制费用',
						columnWidth : .33,
						hidden:true,
						readOnly:true
					}]
				},{
					xtype: 'fieldcontainer',
					layout: 'column',
					items: [{
						xtype : 'combobox',
						store : Ext.create('Ext.data.Store',{
							fields: ['id','name']
						}),
						hiddenName : 'color',
						name : 'color',
						id : 'c2xBasePropertyColorCombo',
						displayField: 'name',
						queryMode : 'local',
						valueField: 'id',
						editable : false, 
						fieldLabel : '颜色',
						columnWidth : .33
					},{
						xtype : 'combobox',
						store : Ext.create('Ext.data.Store',{
							fields: ['id','name']
						}),
						hiddenName : 'size',
						name : 'size',
						id : 'c2xBasePropertySizeCombo',
						displayField: 'name',
						queryMode : 'local',
						valueField: 'id',
						editable : false, 
						fieldLabel : '尺寸',
						columnWidth : .33
					},{
						xtype : 'textfield',
						name : 'sellerPrice',
						fieldLabel : '商品价格',
						columnWidth : .33,
						readOnly:true
					},{
						xtype : 'textfield',
						id : 'c2xBasePropertyCustomCode',
						name : 'customCode',
						fieldLabel : 'sku',
						hidden : true,
						readOnly:true
					}]
				},{
					xtype: 'fieldcontainer',
					id : 'c2mBasePropertyPicField',
					hidden : true,
					layout: 'column',
					items: [{
						xtype : 'textfield',//用来存储图片路径的隐藏域
						id : 'c2mBasePropertyPicPath',
						name : 'c2mPicPath',
						hidden : true,
						readOnly:true
					},{
						xtype : 'displayfield',
						fieldLabel : '定制服装图片',
						columnWidth : .11
					},{
						xtype: 'fieldcontainer',
						id : 'c2mBasePropertyPicPath1',
						name : 'c2mPicPath1',
						columnWidth : .09,
						height: 90,
						align:'right',
						html:'' 
					},{/*
						columnWidth : .13,
						padding: 0,
						margin: '0 0 0 1',
						xtype: 'filefield',
						name : 'file',
						buttonConfig: {
							text : '上传图片',
							disabled : false
						},
						buttonOnly: true,
						hideLabel: true,
						listeners: {
							change : function (file, record) {
								var form = file.up('form');
								var customCode = form.getForm().findField('customCode').value;
								//判断是否图片格式
								var rightTail = ".jpeg|.gif|.jpg|.png|.bmp|.pic|";
								var p = record.lastIndexOf(".");
								var tail = record.substring(p,record.length)+"|";
								tail = tail.toLowerCase();
								if(!(rightTail.indexOf(tail)>-1)){
									Ext.msgBox.remainMsg("警告", "仅支持.jpeg/.gif/.jpg/.png/.bmp/.pic格式文件", Ext.MessageBox.INFO);
									return;
								}
								form.submit({
									url: basePath + 'custom/common/upload',
									params: {'sku' :customCode , "type" : 'C2M'},
									waitMsg: 'Uploading your photo...',
									success: function(fp, action) {
										if (action.result.success == "true") {
											Ext.msgBox.msg('上传图片', "上传成功！", Ext.MessageBox.INFO);
											var newUrl = action.result.msg;
											Ext.getCmp('c2mBasePropertyPicPath1').update('<img width="90" height="90"  onclick=\'showBigPic(this)\' src="'+newUrl+'"/>');
											Ext.getCmp('c2mBasePropertyPicPath').setValue(newUrl);
										} else {
											Ext.msgBox.remainMsg('上传图片', action.result.msg, Ext.MessageBox.ERROR);
										}
									},
									failure : function(formPanel, action) {
										Ext.msgBox.remainMsg('上传图片', action.result.msg, Ext.MessageBox.ERROR);
									},
									waitMsg : 'Loading...'
								});
							}
						}
					*/}]
				},{
					xtype: 'fieldcontainer',
					id : 'c2bBasePropertyPicField',
					hidden : true,
					layout: 'column',
					items: [{
						xtype : 'textfield',//用来存储图片路径的隐藏域
						id : 'c2bBasePropertyPicPath1',
						name : 'c2bPicPath1',
						hidden : true,
						readOnly:true
					},{
						xtype : 'displayfield',
						fieldLabel : '定制效果图',
						columnWidth : .11
					},{
						xtype: 'fieldcontainer',
						id : 'c2bBasePropertyPicPath11',
						name : 'c2bPicPath11',
						columnWidth : .09,
						height: 90,
						align:'right',
						html:'' 
					},{
						columnWidth : .13,
						padding: 0,
						margin: '0 0 0 1',
						xtype: 'filefield',
						name : 'file',
						buttonConfig: {
							text : '上传图片',
							disabled : true
						},
						buttonOnly: true,
						hideLabel: true,
						listeners: {
							change : function (file, record) {
								var form = file.up('form');
								var customCode = form.getForm().findField('customCode').value;
								//判断是否图片格式
								var rightTail = ".jpeg|.gif|.jpg|.png|.bmp|.pic|";
								var p = record.lastIndexOf(".");
								var tail = record.substring(p,record.length)+"|";
								tail = tail.toLowerCase();
								if(!(rightTail.indexOf(tail)>-1)){
									Ext.msgBox.remainMsg("警告", "仅支持.jpeg/.gif/.jpg/.png/.bmp/.pic格式文件", Ext.MessageBox.INFO);
									return;
								}
								form.submit({
									url: basePath + 'custom/common/upload',
									params: {'sku' :customCode , "type" : 'C2B'},
									waitMsg: 'Uploading your photo...',
									success: function(fp, action) {
										if (action.result.success == "true") {
											Ext.msgBox.msg('上传图片', "上传成功！", Ext.MessageBox.INFO);
											var newUrl = action.result.msg;
											Ext.getCmp('c2bBasePropertyPicPath11').update('<img width="90" height="90"  onclick=\'showBigPic(this)\' src="'+newUrl+'"/>');
											Ext.getCmp('c2bBasePropertyPicPath1').setValue(newUrl);
										} else {
											Ext.msgBox.remainMsg('上传图片', action.result.msg, Ext.MessageBox.ERROR);
										}
									},
									failure : function(formPanel, action) {
										Ext.msgBox.remainMsg('上传图片', action.result.msg, Ext.MessageBox.ERROR);
									},
									waitMsg : 'Loading...'
								});
							}
						}
					},{
						xtype : 'textfield',//用来存储图片路径的隐藏域
						id : 'c2bBasePropertyPicPath2',
						name : 'c2bPicPath2',
						hidden : true,
						readOnly:true
					},{
						xtype : 'displayfield',
						fieldLabel : '定制组合图',
						columnWidth : .11
					},{
						xtype: 'fieldcontainer',
						id : 'c2bBasePropertyPicPath22',
						name : 'c2bPicPath22',
						columnWidth : .09,
						height: 90,
						align:'right',
						html:'' 
					},{
						columnWidth : .13,
						padding: 0,
						margin: '0 0 0 1',
						xtype: 'filefield',
						name : 'file2',
						buttonConfig: {
							text : '上传图片',
							disabled : true
						},
						buttonOnly: true,
						hideLabel: true,
						listeners: {
							change : function (file, record) {
								var form = file.up('form');
								var customCode = form.getForm().findField('customCode').value;
								//判断是否图片格式
								var rightTail = ".jpeg|.gif|.jpg|.png|.bmp|.pic|";
								var p = record.lastIndexOf(".");
								var tail = record.substring(p,record.length)+"|";
								tail = tail.toLowerCase();
								if(!(rightTail.indexOf(tail)>-1)){
									Ext.msgBox.remainMsg("警告", "仅支持.jpeg/.gif/.jpg/.png/.bmp/.pic格式文件", Ext.MessageBox.INFO);
									return;
								}
								form.submit({
									url: basePath + 'custom/common/upload',
									params: {'sku' :customCode , "type" : 'C2B',"flag":"1"},
									waitMsg: 'Uploading your photo...',
									success: function(fp, action) {
										if (action.result.success == "true") {
											Ext.msgBox.msg('上传图片', "上传成功！", Ext.MessageBox.INFO);
											var newUrl = action.result.msg;
											Ext.getCmp('c2bBasePropertyPicPath22').update('<img width="90" height="90"  onclick=\'showBigPic(this)\' src="'+newUrl+'"/>');
											Ext.getCmp('c2bBasePropertyPicPath2').setValue(newUrl);
										} else {
											Ext.msgBox.remainMsg('上传图片', action.result.msg, Ext.MessageBox.ERROR);
										}
									},
									failure : function(formPanel, action) {
										Ext.msgBox.remainMsg('上传图片', action.result.msg, Ext.MessageBox.ERROR);
									},
									waitMsg : 'Loading...'
								});
							}
						}
					}]
				}]
			},{
				xtype: 'grid',
				id:'c2xMeasure',
				itemId : 'c2xMeasure',
				title: '特殊定制信息',
				width: '100%',
				hidden : true,
				plugins:[  
		                 Ext.create('Ext.grid.plugin.CellEditing',{  
		                     clicksToEdit:2 //设置单击单元格双击编辑  
		                 })  
		        ],
				store: Ext.create('Ext.data.Store', {
					fields:[{
						name : 'value'
					},{
						name : 'id'
					},{
						name : 'name'
					}]
				}),
				viewConfig:{
					enableTextSelection : true
				},
				autoRender:true,
				columnLines: true,
				loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
				resizable: true,
				forceFit: true,
				collapsible:true,
				titleCollapse:true,
				columns : {
					items:[
					    { text: '属性数值',columnWidth : .5, dataIndex: 'value'/*,editor:{ allowBlank:true }*/},
						{ text: '属性id',columnWidth : .5, dataIndex: 'id'},
						{ text: '属性名称', columnWidth : .5, dataIndex: 'name'}
					],
					defaults: {
						align: 'center',
						sortable:false,
						menuDisabled : true
					}
				}
			},{
				xtype: 'grid',
				id:'c2bCoordinage',
				itemId : 'c2bCoordinage',
				title: '坐标信息',
				width: '100%',
				hidden : true,
				plugins:[  
		                 Ext.create('Ext.grid.plugin.CellEditing',{  
		                     clicksToEdit:2 //设置单击单元格双击编辑  
		                 })  
		        ],
				store: Ext.create('Ext.data.Store', {
					fields:[{
						name : 'id'
					},{
						name : 'x'
					},{
						name : 'y'
					},{
						name : 'width'
					},{
						name : 'height'
					}]
				}),
				viewConfig:{
					enableTextSelection : true
				},
				autoRender:true,
				columnLines: true,
				loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
				resizable: true,
				forceFit: true,
				collapsible:true,
				titleCollapse:true,
				columns : {
					items:[
						{ text: '坐标id',columnWidth : .5, dataIndex: 'id'},
					    { text: 'x坐标',columnWidth : .5, dataIndex: 'x'/*,editor:{ allowBlank:true }*/},
					    { text: 'y坐标',columnWidth : .5, dataIndex: 'y'/*,editor:{ allowBlank:true }*/},
					    { text: '宽度',columnWidth : .5, dataIndex: 'width'/*,editor:{ allowBlank:true }*/},
					    { text: '高度',columnWidth : .5, dataIndex: 'height'/*,editor:{ allowBlank:true }*/}
					],
					defaults: {
						align: 'center',
						sortable:false,
						menuDisabled : true
					}
				}
			}]
		};
		this.buttons = [
			{ 
				text: "关闭", 
				handler: function (btn) {
					//非空判断
	    		   var win = btn.up("window");
	    		   var basePropertyForm = Ext.getCmp("c2xBaseProperty").getForm();
	    		   /*var c2xCloseFlag = basePropertyForm.findField('closeFlag').value;*/
	    		   win.close();
	    		   /*if(c2xCloseFlag=='1'){//查看的时候
	    			   win.close();
	    		   }else{//编辑的时候
	    			   var c2xMeasure = Ext.getCmp("c2xMeasure").store;
		    		   var c2bCoordinage = Ext.getCmp("c2bCoordinage").store;
		    		   var flag = '0';
		    		   c2xMeasure.each(function (record) {
		   					if(record.get('value')==''||record.get('value')==null||record.get('value')=='undefined'){
		   						Ext.msgBox.remainMsg('提示', '属性数值不允许为空！', Ext.MessageBox.ERROR);
		   						flag = '1';
		   					}
		   				});
		    		   c2bCoordinage.each(function (record) {
		   					if(record.get('x')==''||record.get('y')==''||record.get('width')==''||record.get('height')==''){
		   						Ext.msgBox.remainMsg('提示', '坐标不允许为空！', Ext.MessageBox.ERROR);
		   						flag = '1';
		   					}
		   				});
		    		   if(flag=='0'){
			    		   win.close(); 
		    		   }  
	    		   }*/
	    	 	}
			}
		];
		this.callParent(arguments);
	}
});