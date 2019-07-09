/**
 * 承运商管理：查询列表
 */
Ext.define('MB.view.shippingManagement.ShippingQueryGridView', {
	extend : "Ext.grid.Panel",
	alias : 'widget.shippingQueryGridView',
	store: "MB.store.ShippingQueryStore",
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
		    { header: '承运商ID', dataIndex: 'shippingId',hidden: true},
		    { header: '承运商编码', dataIndex: 'shippingCode',width: 130,align: 'center'},
		    { header: '承运商名称', dataIndex: 'shippingName',width: 180,align: 'center'},
		    { header: '承运商描述', dataIndex: 'shippingDesc',hidden: true},
		    { header: '保价费用', dataIndex: 'insure',width: 130,align: 'center' },
		    { header: '货到付款', dataIndex: 'supportCod', width: 110, align: 'center',
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
			{ header: '打印模板', dataIndex: 'shippingPrint',hidden: true},
			{ header: '货到付款打印模板', dataIndex: 'shippingPrint2',hidden: true},
			{ header: '货到付款模板', dataIndex: 'isReceivePrint', width: 110, align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '是';
					} else {
						return '否';
					}
				}
			},
			{ header: '模板图片', dataIndex: 'modelImg',hidden: true},
			{ header: '默认配送方式', dataIndex: 'defalutDelivery', width: 110, align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '是';
					} else {
						return '否';
					}
				}
			},
			{ header: '是否常用', dataIndex: 'isCommonUse', width: 110, align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '是';
					} else {
						return '否';
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
			store: 'MB.store.ShippingQueryStore',
			dock: 'bottom',
			displayInfo: true
		}],
		me.viewConfig = {stripeRows:true,enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},
	
	changeStatus:function(record,buttonText){
		var shippingId = record.get("shippingId");//承运商ID
		var enabled = record.get("enabled");//状态
		confirmMsg("确认","确认"+buttonText+"该承运商吗？", function(btn) {
			if (btn == "yes") {
				Ext.Ajax.request( {
					waitMsg : '请稍等.....',
					url :  basePath + 'custom/shippingManagement/changeStatus.spmvc',
					method : 'post',
					timeout:600000,
					params : {'shippingId':shippingId,
							'enabled':enabled},
					success : function(response) {
						var respText = Ext.JSON.decode(response.responseText);
						console.log(respText);
						if(respText.code=='0'){
							Ext.Msg.alert(buttonText, respText.msg);
							Ext.widget("shippingQueryGridView").store.reload();//刷新查询列表数据
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
		var win = Ext.widget("shippingEdit");
		win.down("form").loadRecord(record);
		win.show();
	}
});

