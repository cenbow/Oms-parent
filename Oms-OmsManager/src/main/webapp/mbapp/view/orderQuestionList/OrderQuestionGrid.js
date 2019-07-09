/**
 *问题单列表 
 ***/

var  retrunNormalQuestionType = "";
var  retrunNormalMainChild = "";

Ext.define('MB.view.orderQuestionList.OrderQuestionGrid', {
	extend : "Ext.grid.Panel",
	alias : 'widget.orderQuestionGrid',
	store: "OrderQuestionListStore",
	autoRender:true,
	columnLines: true,
	width: '100%',
	height:600,
	loadMask: true, // 读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	id:'orderQuestionGridId',

	initComponent : function() {
		Ext.QuickTips.init();
		var me = this;
		
		var selModel = Ext.create('Ext.selection.CheckboxModel', {});
		me.selModel=selModel;
		
		me.columns = [

		      		{ id : 'no', header : "序号", align : "center", width : 120, dataIndex : 'no', hidden:true},
		      		{ id : 'masterOrderSn', header : "订单号", align : "center", width : 180, dataIndex : 'masterOrderSn' ,renderer: function(value) {
		      			if (value != undefined && value != null ) {
		      				var url = order_info_url + value +"&isHistory=" + isHistory;
		      				return "<a href=" +url + " target='_blank'  >"+value+"</a>";
		      			}
		      		}
		   
		      		},
		      		
		      		{ id : 'orderSn', header : "交货单", align : "center", width : 180, dataIndex : 'orderSn'/*,renderer: function(value) {
		      			if (value != undefined && value != null ) {
		      				var url = order_info_url + value +"&isHistory=" + isHistory;
		      				return "<a href=" +url + " target='_blank'  >"+value+"</a>";
		      			}
		      		}
		      		*/
		      		},
		      		{ id : 'orderOutSn', header : "外部交易号", align : "center", width : 220, dataIndex : 'orderOutSn'/*,renderer: function(value, md, r) {
		      			if (value != undefined && value != null ) {
		      				var orderSn =  r.get('orderSn');
		      				var url = order_info_url + orderSn +"&isHistory=" + isHistory;
		      				return "<a href=" +url + " target='_blank' >" + value + "</a>";
		      			}
		      		}*/
		      		
		      		},
		      		{ id : 'lockStatus', header : "锁定状态", align : "center", width : 105, dataIndex : 'lockStatus', 
		      			renderer: function(v, md, r) {
		      				var str = v;
		      				if (v == 0) {
		      					str = '<font style="color:green;">未锁定</font>';
		      				} else {
		      					var userName = r.get('lockUserName');
		      					if (v == 10000 && (userName == undefined || userName == null || userName == '')) {
		      						str = '<font style="color:red;">其他管理员锁定</font>';
		      					} else {
		      						str = '<font style="color:red;">'+ userName +'</font>';
		      					}
		      				}
		      				return str;
		      			}
		      		},
		     
		      		{ id : 'questionTypeStr', header : "问题单类型", align : "center", width : 110, dataIndex : 'questionTypeStr'},
		    	//	{ id : 'masterQuestionDesc', header : "主问题单原因", align : "center", width : 220, dataIndex : 'masterQuestionDesc'},
		      		{ id : 'questionDesc', header : "问题单原因", align : "center", width : 180, dataIndex : 'questionDesc'},
		      		{ id : 'processStatusStr', header : "订单状态", align : "center", width : 180, dataIndex : 'processStatusStr'/*,renderer: editQuestionOrderStatus*/},
		      		{ id : 'addTime', header : "下单时间", align : "center", width : 200, dataIndex : 'addTime'},
		      		{ id : 'questionAddTime', header : "问题单时间", align : "center", width : 200, dataIndex : 'questionAddTime'},
		      		{ id : 'transTypeStr', header : "订单类型", align : "center", width : 80, dataIndex : 'transTypeStr'},
		      		{ id : 'orderFrom', header : "订单来源", align : "center", width : 160, dataIndex : 'orderFrom', hidden: true},
		      		{ id : 'channelName', header : "订单来源", align : "center", width : 160, dataIndex : 'channelName'},
		      		{ id : 'userName',header : "下单人", align : "center", sortable : true, width : 120, dataIndex : 'userName'},
		      		{ id : 'totalFee', header : "总金额", align : "center", width : 80, dataIndex : 'totalFee'},
		      		{ id : 'totalPayable', header : "应付金额", align : "center", width : 80, dataIndex : 'totalPayable'},
		      		{ id : 'referer', header : "referer", align : "center", width : 120, dataIndex : 'referer'},
		      		{ id : 'customerNote', header : "客服备注", align : "center", width : 120, dataIndex : 'customerNote'},
		      		{ id : 'businessNote', header : "商家备注", align : "center", width : 180, dataIndex : 'businessNote'},
		      		{ id : 'useLevelStr', header : "渠道会员等级类型", align : "center", width : 120, dataIndex : 'useLevelStr'}
		    
		];
		
		me.dockedItems = [{
			xtype: 'pagingtoolbar',
			store: 'OrderQuestionListStore',
			dock: 'bottom',
			displayInfo: true	
		},{
			
			xtype: 'toolbar',
			dock: 'top',
			items: [
				 {
					id: 'question_order_tBar-normal',
					iconCls: 'add',
					text: '批量返回正常单',
					tooltip : '批量返回正常单',
				//	disabled: ButtonDis(toNormal),
					disabled :false,
					handler : function () {

						var  orderQuestionGrid = Ext.getCmp("orderQuestionGridId");
						
						var selModel = orderQuestionGrid.getSelectionModel();
						
						orderSns = new Array();
						
						var orderQuestionListPageMainOrChild = Ext.getCmp("orderQuestionListPageMainOrChild").getChecked()[0].inputValue;//订单号或交货单
						
						var orderQuestionPageListDataType = Ext.getCmp("orderQuestionPageListDataType").getChecked()[0].inputValue;//问题单类型   oms问题单  或   缺货问题单
						
						  retrunNormalQuestionType = orderQuestionPageListDataType;
						  retrunNormalMainChild = orderQuestionListPageMainOrChild;
						
						if (selModel.hasSelection()) {
							var records = selModel.getSelection();
							for ( var i = 0; i < records.length; i++) {
								var orderSn = records[i].get("masterOrderSn"); 
								if(orderSn && orderSn != ''){
									orderSns.push(orderSn);
								}
							}
							if(orderSns && orderSns.length > 0){
								var win = Ext.widget("returnNormal");
								win.show();
						
							}
						} else {
							alertMsg("错误", "请选择要批量返回正常单的记录!");
						}
				
					}
			
				},{
					id : 'question_order_tBar-import',
					text : '批量导入问题单',
					tooltip : '批量导入问题单',
					iconCls : 'add',
					disabled :false,
					handler : function () {
						
						var win = Ext.widget("importOrderLogisticsQuestion");
				
					/*	win.down("form").load({
							url: basePath+"/custom/orderQuestion/gotoImportQuestionPage",
							//params: {"resourceId": resourceId},
							success  : function (form, action) {
								
						
							},
							failure : function(form, action) {
								// 数据加载失败后操作
							}
						});*/
						
						win.show();
						
					}
					
				}
			]
			
		}
		
		],

		me.viewConfig = {enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	}/*,
	listeners:{
		
    	'itemclick' :function(dataview, record, item, index, e){

			// 物流问题单
			if(questionType == 1){
				
				var sOrderSn  = record.get("orderSn");
				var no = record.get("no");
				var questionReason = record.get("questionReason");
				
				var processStatus = Ext.getCmp("orderQuestionPageProcessStatus").getValue()
					//查询翻页后带回查询数据
				var param = {"orderSn" : sOrderSn, "reason": questionReason, "processStatus": processStatus};
					
				var shortageQuestion_gridss_id = Ext.getCmp("shortageQuestion_gridss_id");
	
				
				shortageQuestion_gridss_id.getStore().on('beforeload', function (store, options) {
					Ext.apply(store.proxy.extraParams, param);
				});
		
				shortageQuestion_gridss_id.getStore().load(); 
				shortageQuestion_gridss_id.setTitle("问题单："+sOrderSn + "商品列表");
				
				var orderQuestionGridId = Ext.getCmp('orderQuestionGridId');
				orderQuestionGridId.setWidth('78%');
				shortageQuestion_gridss_id.setWidth('30%');
			}
			
		}
	}*/

});

