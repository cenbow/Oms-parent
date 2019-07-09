Ext.define("MB.view.handOrder.HandOrderListWin", {
	extend: "Ext.window.Window",
	alias: "widget.handOrderListWin",
	title: "赠品打单导入",
	width: 1000,
	height: 500,
	layout: "fit",
	maskDisabled : false,
	modal : true,
	initComponent: function () {
		this.items = {
			xtype: "form",
			margin: 5,
			border: false,
			frame: true,
			autoScroll : false,
			fieldDefaults: {
				labelAlign: 'right',
				labelWidth: 150
			},
			items: [{
				xtype: 'grid',
				id:'hardOrderListGrid',
				width: '100%',
				height: 415,
				viewConfig:{
					enableTextSelection : true,
					getRowClass:function(record, index, rowParams, store){
		                 return record.get('flag')=='1'?"child-row":"";
		            }
				},
				listeners:{
					itemdblclick:function(dataview,record, item, index, e){
						var win = Ext.widget("HandOrderEditWin");
						win.down("form").loadRecord(record);
						win.show();
					}
				},
				autoRender:true,
				columnLines: true,
				loadMask: true, //读取数据时的遮罩和提示功能即加载loding...
				resizable: true,
				forceFit: false,
				collapsible:false,
				titleCollapse:false,
				selModel : new Ext.selection.CheckboxModel(),
				plugins:[  
		                 Ext.create('Ext.grid.plugin.CellEditing',{  
		                     clicksToEdit:2 //设置单击单元格双击编辑  
		                 })  
		        ],
				store: Ext.create('Ext.data.Store', {
					fields:[{
						name : 'flag'
					},{
						name : 'errorMsg'
					},{
						name : 'orderId'
					},{
						name : 'masterOrderSn'
					},{
						name : 'userId'
					},{
						name : 'customCode'
					},{
						name : 'goodsNumber'
					},{
						name : 'createTime'
					},{
						name : 'processMessage'
					},{
						name : 'sourceType'
					},{
						name : 'shippingAddress'
					}],
					idProperty : 'id'
				}),
				columns : {
					items:[
					    { text:'序号',xtype: 'rownumberer',width: 50},
						{ text: 'flag',dataIndex: 'flag',width: 150,align: 'center',hidden:true},
						{ text: '临时订单号',dataIndex: 'orderId',width: 150,align: 'center', hidden:true},
						{ text: '用户名',dataIndex: 'userId',width: 110,align: 'center'},
						{ text: '商品码|商品数量（多个,分隔）',dataIndex: 'goodsInfo',width: 250,align: 'center'},
						{ text: '打单类型',dataIndex: 'sourceType',width: 150,align: 'center',
							renderer : function (value, meta, record) {
								if (value == null || value== '' || value == 5) {
									return '一般赠品订单';
								} else if (value == 4) {
									return '首购赠品订单';
								}
							}
						},
//						{ text: '商品码',dataIndex: 'customCode',width: 150,align: 'center'},
//						{ text: '商品数量',dataIndex: 'goodsNumber',width: 100,align: 'center'},
						{ text: '订单号',dataIndex: 'masterOrderSn',width: 150,align: 'center', hidden:true/*,editor:{ allowBlank:true }*/},
						{ text: '订单创建时间',dataIndex: 'createTime',width: 150,align: 'center'},
						{ text: '收货地址',dataIndex: 'shippingAddress',width: 150,align: 'center'},
						{ text: '错误信息',dataIndex: 'errorMsg',width: 150,align: 'center',
							renderer: function(value, meta, record) {//超长自动换行
								meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';   
								return value;   
							}
						},
						{ text: 'processMessage',dataIndex: 'note',width: 150,align: 'center',hidden:true},
						{ text: 'channelCode',dataIndex: 'channelCode',width: 150,align: 'center',hidden:true},
						{ text: 'channelName',dataIndex: 'channelName',width: 150,align: 'center',hidden:true}
					],
					defaults: {
						align: 'center',
						sortable:false,
						menuDisabled : true
					}
				},
				dockedItems : [{
					xtype: 'toolbar',
					dock: 'top',
					items: [
					/*{
						id: 'thirdPartyOrderList_handadd',
						iconCls: 'add',
						text: '手工录入打单',
						tooltip : '手动录入订单数据',
						disabled :false,
						handler : function () {
							var win = Ext.widget("HandOrderAddWin");
							win.show();
						}
				
					},*/
					{
						id : 'thirdPartyOrderList_batchadd',
						text : '批量导入订单',
						tooltip : 'excel批量导入订单数据',
						iconCls : 'add',
						disabled :false,
						handler : function () {
							var win = Ext.widget("handOrder");
							win.show();
						}
					},{
						id : 'thirdPartyOrderList_batchminus',
						text : '批量删除订单',
						tooltip : '勾选批量删除订单数据',
						iconCls : 'delete',
						disabled :false,
						handler : function () {
							var records = Ext.getCmp("hardOrderListGrid").getSelectionModel().getSelection();//获取选中行
							if(records.length==0){
					            Ext.MessageBox.show({   
					               title:"提示",   
					               msg:"请先选择您要操作的行!"
					            });
					           return;
					 		}else{
					      		 Ext.Msg.confirm('确认', '确认删除选中的订单吗?', function (text) {
					        	   if (text == 'yes') {
					        		   	var store = Ext.getCmp("hardOrderListGrid").store;
					        		    Ext.each(records,function(record){
					        			   store.remove(record);
					        		    });
					        		    Ext.getCmp('hardOrderListGrid').getView().refresh();
						        	}
						        });  
					 		}
						}
					}]
				}]
			}]
		};
		this.buttons = [
			{
				text : "确认打单导入",
				action : "submitImport"
			},{ 
				text : "关闭",
				action : "closeImport"
			}
		];
		this.callParent(arguments);
	}
});