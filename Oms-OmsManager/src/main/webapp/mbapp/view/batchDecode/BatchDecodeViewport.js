Ext.define("MB.view.batchDecode.BatchDecodeViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.batchDecodeViewport',
	requires: ['MB.view.handOrder.HandOrderPanlView'],
	items : [{
		xtype: 'grid',
		region : "center",
		id:'batchDecodeListGrid',
		width: '100%',
		height: document.body.clientHeight,
		columnLines: true,
		loadMask: true, // 读取数据时的遮罩和提示功能即加载loding...
		frame: true,
		resizable: true,
		forceFit: true,
		viewConfig:{
			enableTextSelection : true,
			stripeRows:true
		},
		store: 'MB.store.BatchDecodeListStore',
		columns : {
			items:[
				    { text:'序号', xtype: 'rownumberer', width:50 },
				    { header: '批次号', dataIndex: 'billNo',width: 150,align: 'center'},
					{ header: '操作人', dataIndex: 'actionUser',width: 100,align: 'center'},
					{ header: '导入时间', dataIndex: 'addTime',width: 150,align: 'center',
						renderer:function(value){ 
							if(value){
					            var createTime = Ext.Date.format(new Date(value),"Y-m-d H:i:s");
					            return createTime;  
							}
				         }
					},
					{ header: '同步状态', dataIndex: 'isSync',width: 100,align: 'center',
						renderer : function (v, md, r) {
							if (v == 0) {
								return '未同步';
							} else if(v == 1){
								return '已同步';
							} else if(v == 2){
								return '作废';
							}else if(v == 3){
								return '部分同步';
							}else if(v == 4){
								return '失败';
							}else if(v == 9){
								return '同步中';
							}
						}
					},
					{
						 text: '操作',width: 150,align: 'center',dataIndex: '' ,sortable:false,menuDisabled : true,
							renderer : function (value, metaData, record, rowIndex) {
								var id = Ext.id();
								var isSync = record.get("isSync");//同步状态状态
								var flag = true;//未同步/同步中的不提供下载按钮
								if (isSync == 1) {
									flag = false;
								}
								setTimeout(function() {
									var panel = Ext.create('Ext.panel.Panel', {
										width: 230,
										layout: 'column',
										items: [{
											columnWidth: 1,
											xtype: 'segmentedbutton',
											allowToggle: false,
											items: [{
												text: '下载解密清单',
												disabled: flag,
												handler: function () {
													var billNo = record.get("billNo");//调整单号
													if(billNo==''||billNo==null){
														 Ext.Msg.alert('提示', '获取数据失败！');  
												         return;
													}else{
														Ext.Ajax.request({
															url:  basePath + '/custom/batchDecode/checkDownloadDecodeList',
															params: {'billNo':billNo},
															success: function(response){
																var respText = Ext.JSON.decode(response.responseText);
																var code = respText.code;
																if (code=='0') {
																	 window.location.href = basePath + 'custom/batchDecode/downloadDecodeList.spmvc?billNo='+billNo;
																} else {
																	Ext.Msg.alert('提示', respText.msg);
																}
															},
															failure: function(response){
																Ext.Msg.alert('提示', '下载失败！');
															}
														});
													}
												}
											},{
												text: '下载单号清单',
												handler: function () {
													var billNo = record.get("billNo");//调整单号
													if(billNo==''||billNo==null){
														 Ext.Msg.alert('提示', '获取数据失败！');  
												         return;
													}else{
														 window.location.href = basePath + 'custom/batchDecode/downloadOrderSnList.spmvc?billNo='+billNo;
													}
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
			items: [{
				text : '批量导入订单号',
				tooltip : '批量导入待解密联系方式的订单号',
				iconCls : 'add',
				disabled :false,
				handler : function () {
					var win = Ext.widget("batchImportWin");
					win.show();
				}
			}]
		},{
			xtype: 'pagingtoolbar',
			store: 'MB.store.BatchDecodeListStore',
			dock: 'bottom',
			displayInfo: true
		}]
	}]
});