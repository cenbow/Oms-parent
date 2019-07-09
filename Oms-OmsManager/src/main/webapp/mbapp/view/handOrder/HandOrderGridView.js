Ext.define('MB.view.handOrder.HandOrderGridView', {
	extend : "Ext.grid.Panel",
	alias : 'widget.handOrderGridView',
	store: "MB.store.HandOrderStore",
//	selModel : new Ext.selection.CheckboxModel(),
	id : 'handOrderGridView',
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, // 读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	resizable: true,
	forceFit: false,
	height:500,
	listeners:{
		itemdblclick:function(dataview,record, item, index, e){
			this.checkDetail(record);
		}
	},
	initComponent : function() {
		Ext.QuickTips.init();
		var me = this;
		me.columns = [
			{ text:'序号', xtype: 'rownumberer', width:50 },
			{ header: '批次号', dataIndex: 'batchNo',width: 150,align: 'center'},
			{ header: '渠道号', dataIndex: 'channelCode',width: 130,align: 'center'},
			{ header: '处理状态', dataIndex: 'processStatus', width: 120, align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '已审核';
					} else if(v == 2){
						return '已打单';
					} else{
						return '未审核';
					}
				}
			},
			{ text: '打单类型',dataIndex: 'sourceType',width: 150,align: 'center',
				renderer : function (value, meta, record) {
					if (value == null || value== '' || value == 5) {
						return '一般赠品订单';
					} else if (value == 4) {
						return '首购赠品订单';
					}
				}
			},
			{ header: '操作人', dataIndex: 'createUser',width: 100,align: 'center'},
			{ header: '处理信息', dataIndex: 'processMessage',width: 100,align: 'center'},
			{ header: '是否定时执行', dataIndex: 'isTiming',hidden: true},
			{ header: '执行时间', dataIndex: 'execTime',hidden: true},
			{ header: '打单结果', dataIndex: 'createOrderStatus',width: 100,align: 'center',
				renderer : function (v, md, r) {
					if (v == 0) {
						return '未处理';
					} else if(v == 1){
						return '全部成功';
					} else if(v == 2){
						return '部分成功';
					}else if(v == 3){
						return '全部失败';
					}
				}
			},
			{ header: '添加时间', dataIndex: 'createTime',width: 150,align: 'center'},
			{ header: '更新时间', dataIndex: 'updateTime',width: 150,align: 'center'},
			{
				 text: '操作',width: 150,align: 'center',dataIndex: '' ,sortable:false,menuDisabled : true,
					renderer : function (value, metaData, record, rowIndex) {
						var id = Ext.id();
						var isSync = record.get("isSync");//同步状态状态
						var flag = false;//未同步/同步中的不提供下载按钮
						if (isSync == 9 || isSync == 0) {
							flag = true;
						}
						setTimeout(function() {
							var panel = Ext.create('Ext.panel.Panel', {
								bodyPadding: 0,
								border:false,
								baseCls: 'my-panel-no-border',
								width: 140,
								height: 20,
								layout: 'column',
								columnWidth : 1,
								items: [{
									columnWidth: 1,
									xtype: 'segmentedbutton',
									allowToggle: false,
									items: [{
										text: '下载清单',
										disabled: flag,
										handler: function () {
											me.downloadRecord(record);
										}
									},{
										text: '查询清单',
										handler: function () {
											me.checkDetail(record);
										}
									}]
								}]
							});
							if (Ext.get(id)) {
								panel.render(Ext.get(id));
							}
						}, 1);
						return '<div id="' + id + '"></div>';
					}
				
			}
		];
		me.dockedItems = [{
			xtype: 'pagingtoolbar',
			store: 'MB.store.HandOrderStore',
			dock: 'bottom',
			displayInfo: true
		}],
		me.viewConfig = {stripeRows:true,enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},
	downloadRecord:function(record){
		var batchNo = record.get("batchNo");//调整单号
		if(batchNo==''||batchNo==null){
			Ext.Msg.alert('提示', '获取数据失败！');  
			return;
		}else{
			var url = basePath + 'custom/handOrder/downloadRecord.spmvc?batchNo='+batchNo;
//			window.location.href = downLoadUrl;
			var myMask = new Ext.LoadMask({
				msg : '请稍等,正在导出...',
				target : this
			});
					myMask.show();
					Ext.Ajax.request({
						waitMsg : '请稍等.....',
						url : url,
						method : 'post',
						timeout : 7200000,
						method : 'post',
						success : function(response) {
							if (myMask != undefined) {
								myMask.hide();
							}
							var obj = Ext.decode(response.responseText);
							var path = obj.data.path;
							var fileName = obj.data.fileName;
							if (obj.isok == true) {
								window.location.href = basePath
										+ "custom/downloadFtpFile.spmvc?path=" + path + "&fileName=" + fileName;
							} else {
								Ext.Msg.alert("错误", "导出失败");
							}
						},
						failure : function(response) {
							myMask.hide();
							Ext.Msg.alert("验证", "失败");
						}
					});
				
		}
	},
	checkDetail:function(record){
		var batchNo = record.get('batchNo');
		console.dir(record);
		var channelCode = record.get('channelCode');
		var processStatus = record.get("processStatus");//同步状态状态
		var flag = false;//未同步/同步中的不提供下载按钮
		if (processStatus == 0 || processStatus == 1) {
			flag = true;
		}

		//store的请求路径
		var url = basePath +'custom/handOrder/getThirdPartyOrderDetailList.spmvc?batchNo='+batchNo;
		//模型
		var model = Ext.create('Ext.data.Model',{
			fields: [
						{ name: 'batchNo', type: 'string' },//单据批次号
						{ name: 'masterOrderSn', type: 'string' },//订单号
						{ name: 'money', type: 'string' },//结算金额
						{ name: 'processMessage', type: 'string' }//处理结果信息
					]
		});
		//数据源
		var store = Ext.create('Ext.data.Store',{
			model: model,
			autoLoad: true,
			pageSize : 10,// 每页显示条目数量
			proxy: {
				type: 'ajax',
				actionMethods: {
					read: 'POST'
				},
				url:  url,
				reader: {
					rootProperty: 'root',
					totalProperty: 'totalProperty'
				},
				simpleSortMode: true
			}
		});
		//清单列表面板
		var grid = Ext.create('Ext.grid.Panel',{
			store: store,
			id : 'thirdPartyOrderDetailGridPanel',
			autoRender:true,
			columnLines: true,
			width: '100%',
			loadMask: true, // 读取数据时的遮罩和提示功能即加载loding...
			frame: true,
			resizable: true,
			forceFit: false,
			columns : [
						{ text:'序号', xtype: 'rownumberer', width:50 },
						{ header: '调整单号', dataIndex: 'batchNo',width: 180,align: 'center'},
						{ header: '订单号', dataIndex: 'masterOrderSn',width: 150,align: 'center',
							renderer: function(value, meta, record) {//超长自动换行
								if(value != undefined && value != null ) {
									var url = order_info_url + value +'&isHistory=' + 0;
									return "<a href=" +url + " target='_blank'  >" + value + "</a>";
								} else {
									return '<font>' + value + '</font>';
								}
							}
						},
						{ header: '用户名', dataIndex: 'userId',width: 150,align: 'center'},
						{ text: '打单类型',dataIndex: 'sourceType',width: 150,align: 'center',
							renderer : function (value, meta, record) {
								if (value == null || value== '' || value == 5) {
									return '一般赠品订单';
								} else if (value == 4) {
									return '首购赠品订单';
								}
							}
						},
						{ header: '是否打单', dataIndex: 'isOk',width: 100, align: 'center',
							renderer: function(value, meta, record) {
								if (value == 0) {
									return "未打单";
								} else if (value == 1) {
									return "打单成功";
								} else if (value == 2) {
									return "打单失败";
								}
								return value;
							}
						},
						{ header: '处理结果', dataIndex: 'processMessage',width: 295,align: 'center',
							renderer: function(value, meta, record) {//超长自动换行
								meta.style = 'overflow:auto;padding: 3px 6px;text-overflow: ellipsis;white-space: nowrap;white-space:normal;line-height:20px;';   
								return value;
							}
						}
					],
			dockedItems : [{
				xtype: 'pagingtoolbar',
				store: store,
				dock: 'bottom',
				displayInfo: true
			}],
			viewConfig : {stripeRows:true,enableTextSelection: true}
		});
		//要展示的窗体
		var win = Ext.create('Ext.Window',{
			title:"导入清单",
			width:900, 
			height:500,
			layout : 'fit',
			modal:true,
			items:grid,
			buttons : [{
				text: "下载",
				disabled: flag,
				handler: function (btn) {
				if(batchNo==''||batchNo==null){
					Ext.Msg.alert('提示', '获取数据失败！');  
					return;
				}else{
//					window.location.href = downLoadUrl;
					var myMask = new Ext.LoadMask({
						msg : '请稍等,正在导出...',
						target : thisPage
					});
							myMask.show();
							Ext.Ajax.request({
								waitMsg : '请稍等.....',
								url : url,
								method : 'post',
								timeout : 7200000,
								method : 'post',
								params : searchParams,
								success : function(response) {
									if (myMask != undefined) {
										myMask.hide();
									}
									var obj = Ext.decode(response.responseText);
									var path = obj.data.path;
									var fileName = obj.data.fileName;
									if (obj.isok == true) {
										window.location.href = basePath
												+ "custom/downloadFtpFile.spmvc?path="
												+ path + "&fileName="
												+ fileName;
									} else {
										Ext.Msg.alert("错误", "导出失败");
									}
									Ext.getCmp('orderInfoExportBtn').setDisabled(false);
								},
								failure : function(response) {
									myMask.hide();
									Ext.Msg.alert("验证", "失败");
									Ext.getCmp('orderInfoExportBtn').setDisabled(false);
								}
							});
						}
					}
				},{
					text: "关闭",
					handler: function (btn) {
					var win = btn.up("window");
					win.close();
				}
			}]
		});
		//展示
		win.show();
	}
});

