/**
 * 支付方式管理：查询列表
 */
Ext.define('MB.view.paymentManagement.PaymentQueryGridView', {
	extend : "Ext.grid.Panel",
	alias : 'widget.paymentQueryGridView',
	store: "MB.store.PaymentQueryStore",
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, // 读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	resizable: true,
	forceFit: true,
	height:500,
	listeners:{
		itemdblclick:function(dataview,record, item, index, e){
		   	this.openEdit(record);
		}
	},
	initComponent : function() {
		Ext.QuickTips.init();
		var me = this;
		me.columns = [
		    { text:'序号', xtype: 'rownumberer', width:50 },
		    { header: '支付方式ID', dataIndex: 'payId',hidden: true},
		    { header: '支付方式编码', dataIndex: 'payCode',width: 150,align: 'center'},
		    { header: '支付方式名称', dataIndex: 'payName',width: 150,align: 'center'},
		    { header: '支付费用', dataIndex: 'payFee',width: 110,align: 'center'},
		    { header: '支付方式描述', dataIndex: 'payDesc',hidden: true},
		    { header: '显示顺序', dataIndex: 'payOrder',width: 110,align: 'center' },
		    { header: '支付方式的配置信息', dataIndex: 'payConfig',hidden: true},
		    { header: '货到付款', dataIndex: 'isCod', width: 110, align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '支持';
					} else {
						return '不支持';
					}
				}
			},
			{ header: '状态', dataIndex: 'enabled', width: 110, align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '启用';
					} else {
						return '禁用';
					}
				}
			},
			{ header: '在线支付', dataIndex: 'isOnline', width: 110, align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '支持';
					} else {
						return '不支持';
					}
				}
			},
			{ header: '手机渠道使用', dataIndex: 'isMobile', width: 110, align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '支持';
					} else {
						return '不支持';
					}
				}
			},
			{
				 text: '操作',width: 160,align: 'center',dataIndex: '' ,sortable:false,menuDisabled : true,
					renderer : function (value, metaData, record, rowIndex) {
						var id = Ext.id();
						var enabled = record.get("enabled");//承运商状态
						var buttonText = '';//按钮显示value值
						//如果承运商是启用状态，按钮就显示禁用；反之显示启用
						if(enabled == 1){
							buttonText = '禁用';
						}else{
							buttonText = '启用';
						}
//						console.dir(record);
						setTimeout(function() {
							var panel = Ext.create('Ext.panel.Panel', {
								bodyPadding: 0,
								border:false,
								baseCls: 'my-panel-no-border',
								width: 130,
								height: 20,
								layout: 'column',
								columnWidth : 1,
								items: [{
									columnWidth: 1,
									xtype: 'segmentedbutton',
									allowToggle: false,
									items: [{
										text: buttonText,
										handler: function () {
											me.changeStatus(record,buttonText);
										}
									},{
										text: '编辑',
										handler: function () {
											me.openEdit(record);
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
			store: 'MB.store.PaymentQueryStore',
			dock: 'bottom',
			displayInfo: true
		}],
		me.viewConfig = {stripeRows:true,enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},
	changeStatus:function(record,buttonText){
		var payId = record.get("payId");//支付方式ID
		var enabled = record.get("enabled");//状态
		confirmMsg("确认","确认"+buttonText+"该支付方式吗？", function(btn) {
			if (btn == "yes") {
				Ext.Ajax.request( {
					waitMsg : '请稍等.....',
					url :  basePath + 'custom/paymentManagement/changeStatus.spmvc',
					method : 'post',
					timeout:600000,
					params : {'payId':payId,
							'enabled':enabled},
					success : function(response) {
						var respText = Ext.JSON.decode(response.responseText);
						console.log(respText);
						if(respText.code=='0'){
							Ext.Msg.alert(buttonText, respText.msg);
							Ext.widget("paymentQueryGridView").store.reload();//刷新查询列表数据
						}else{
							Ext.Msg.alert(buttonText, respText.msg);
							win.close();
						}
					},
					failure : function(response) {
						var respText = Ext.JSON.decode(response.responseText);
						Ext.Msg.alert(buttonText, respText.msg);
					}
				});
			}else {
				return ;
			}
		});
	},
	openEdit:function(record){
		var win = Ext.widget("paymentEdit");
		win.down("form").loadRecord(record);
		win.show();
	}
});

