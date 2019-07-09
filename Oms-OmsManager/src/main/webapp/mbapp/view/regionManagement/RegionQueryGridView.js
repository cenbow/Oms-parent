Ext.define('MB.view.regionManagement.RegionQueryGridView', {
	extend : "Ext.grid.Panel",
	alias : 'widget.regionQueryGridView',
	store: "MB.store.RegionStore",
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
		   	this.checkDetail(record);
		}
	},
	initComponent : function() {
		Ext.QuickTips.init();
		var me = this;
		me.columns = [
		    { text:'序号', xtype: 'rownumberer', width:50 },
		    { header: '区域ID', dataIndex: 'regionId',hidden: true},
		    { header: '父级区域ID', dataIndex: 'parentId',hidden: true},
		    { header: '菜单级别', dataIndex: 'regionType',hidden: true},
			{ header: '区域名称', width: 280, dataIndex: 'regionName',align: 'center'},
		    { header: '邮编', width: 100, dataIndex: 'zipCode',align: 'center'},
			{ header: '快递费用', width: 100, dataIndex: 'shippingFee',align: 'center'},
			{ header: 'EMS费用', width: 100, dataIndex: 'emsFee',align: 'center'},
			{ header: '货到付款费用', width: 130, dataIndex: 'codFee',align: 'center'},
			{ header: '货到付款', width: 100, dataIndex: 'isCod',align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '支持';
					} else {
						return '不支持';
					}
				}
			},
			{ header: 'POS刷卡', width: 100, dataIndex: 'codPos',align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '支持';
					} else {
						return '不支持';
					}
				}
			},
			{ header: '自提', width: 100, dataIndex: 'isCac', align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '支持';
					} else {
						return '不支持';
					}
				}
			},
			{ header: '货到付款验证手机号', width: 180, dataIndex: 'isVerifyTel', align: 'center',
				renderer : function (v, md, r) {
					if (v == 1) {
						return '支持';
					} else {
						return '不支持';
					}
				}
			},
			{
				 text: '操作',width: 200,align: 'center',dataIndex: '' ,sortable:false,menuDisabled : true,
					renderer : function (value, metaData, record, rowIndex) {
						var id = Ext.id();
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
										text: '删除',
										handler: function () {
											me.delRegion(record);
										}
									},{
										text: '编辑',
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
			store: 'MB.store.RegionStore',
			dock: 'bottom',
			displayInfo: true
		}],
		me.viewConfig = {stripeRows:true,enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},
	
	delRegion:function(record){
		var regionId = record.get("regionId");
		var regionType = record.get("regionType");
		confirmMsg("确认","删除该区域同时会删除其所有下级区域！确认执行删除操作吗？", function(btn) {
			if (btn == "yes") {
				Ext.Ajax.request( {
					waitMsg : '请稍等.....',
					url :  basePath + 'custom/regionManagement/delRegion.spmvc',
					method : 'post',
					timeout:600000,
					params : {'regionId':regionId,
							'regionType':regionType},
					success : function(response) {
						var respText = Ext.JSON.decode(response.responseText);
						console.log(respText);
						if(respText.code=='0'){
							Ext.Msg.alert('删除', respText.msg);
							Ext.widget("regionQueryGridView").store.reload();//刷新查询列表数据
						}else{
							Ext.Msg.alert('删除', respText.msg);
							win.close();
						}
					},
					failure : function(response) {
						var respText = Ext.JSON.decode(response.responseText);
						Ext.Msg.alert('删除', respText.msg);
					}
				});
			}else {
				return ;
			}
		});
	},
	
	checkDetail:function(record){
		var win = Ext.widget("regionEdit");
		win.down("form").loadRecord(record);
		win.show();
	}
});

