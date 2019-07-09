/**
 * 邦付宝退款批量结算：查询列表
 */
Ext.define('MB.view.bfbRefundSettlement.BfbRefundSettlementGridView', {
	extend : "Ext.grid.Panel",
	alias : 'widget.bfbRefundSettlementGridView',
	store: "MB.store.BfbRefundSettlementStore",
	selModel : new Ext.selection.CheckboxModel(),
	id : 'bfbRefundSettlementGridView',
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
		    { header: '批次号', dataIndex: 'billNo',width: 150,align: 'center'},
		    { header: '渠道号', dataIndex: 'channelCode',width: 130,align: 'center'},
		    { header: '业务类型', dataIndex: 'billType', width: 120, align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '订单结算';
					} else if(v == 2){
						return '订单货到付款结算';
					} else if(v == 3){
						return '退单退款方式结算';
					} else if(v == 4){
						return '保证金结算';
					} else if(v == 5){
						return '日志';
					} else if(v == 6){
						return '邦付宝退款';
					}else if(v == 7){
						return '订单金额调整';
					} else{
						return '未定义';
					}
				}
			},
			{ header: '操作人', dataIndex: 'actionUser',width: 100,align: 'center'},
			{ header: '备注', dataIndex: 'note',width: 100,align: 'center'},
			{ header: '是否定时执行', dataIndex: 'isTiming',hidden: true},
			{ header: '执行时间', dataIndex: 'execTime',hidden: true},
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
			{ header: '添加时间', dataIndex: 'addTime',width: 150,align: 'center'},
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
			store: 'MB.store.BfbRefundSettlementStore',
			dock: 'bottom',
			displayInfo: true
		}],
		me.viewConfig = {stripeRows:true,enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},
	downloadRecord:function(record){
		var billNo = record.get("billNo");//调整单号
		if(billNo==''||billNo==null){
			 Ext.Msg.alert('提示', '获取数据失败！');  
	         return;
		}else{
			 window.location.href = basePath + 'custom/bfbRefundSettlement/downloadRecord.spmvc?billNo='+billNo;
		}
	},
	checkDetail:function(record){
		var billNo = record.get('billNo');
		var isSync = record.get("isSync");//同步状态状态
		var flag = false;//未同步/同步中的不提供下载按钮
		if (isSync == 9 || isSync == 0) {
			flag = true;
		}
		//store的请求路径
		var url = basePath +'custom/bfbRefundSettlement/getBfbRefSetDetailList.spmvc?billNo='+billNo;
		//模型
		var model = Ext.create('Ext.data.Model',{
			fields: [
						{ name: 'billNo', type: 'string' },//单据批次号
						{ name: 'orderCode', type: 'string' },//退单号
						{ name: 'returnPay', type: 'string' },//退款方式
						{ name: 'money', type: 'string' },//结算金额
						{ name: 'resultStatus', type: 'string' }//处理结果0.未定义1.结算成功2.结算失败
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
			id : 'bfbRefSetDetailGridPanel',
			autoRender:true,
			columnLines: true,
			width: '100%',
			loadMask: true, // 读取数据时的遮罩和提示功能即加载loding...
			frame: true,
			resizable: true,
			forceFit: false,
			columns : [
					    { text:'序号', xtype: 'rownumberer', width:50 },
					    { header: '调整单号', dataIndex: 'billNo',width: 180,align: 'center'},
					    { header: '退单号', dataIndex: 'orderCode',width: 150,align: 'center'},
					    { header: '退款方式', dataIndex: 'returnPay',width: 90,align: 'center'},
						{ header: '结算金额', dataIndex: 'money',width: 90,align: 'center'},
						{ header: '处理结果', dataIndex: 'resultMsg',width: 215,align: 'center',
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
			title:"结算清单",   
		       width:800,       
		       height:400,         
		       layout : 'fit',
		       modal:true,
		       items:grid,
		       buttons : [{
		    	   text: "下载",
		    	   disabled: flag,
		    	   handler: function (btn) {
		    		   if(billNo==''||billNo==null){
		    				 Ext.Msg.alert('提示', '获取数据失败！');  
		    		         return;
		    			}else{
		    				 window.location.href = basePath + 'custom/bfbRefundSettlement/downloadRecord.spmvc?billNo='+billNo;
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

