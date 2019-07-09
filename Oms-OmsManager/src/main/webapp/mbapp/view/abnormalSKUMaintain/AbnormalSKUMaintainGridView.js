/**
 * 异常SKU调整：查询列表
 */
Ext.define('MB.view.abnormalSKUMaintain.AbnormalSKUMaintainGridView', {
	extend : "Ext.grid.Panel",
	alias : 'widget.abnormalSKUMaintainGridView',
	store: "MB.store.AbnormalSKUMaintainStore",
	id : 'abnormalSKUMaintainGridView',
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
		   	this.edit(record);
		}
	},
	initComponent : function() {
		Ext.QuickTips.init();
		var me = this;
		var clientWidth = document.body.clientWidth;
		me.columns = [
		    { text:'序号', xtype: 'rownumberer', width:clientWidth * 0.05 },
		    { header: '外部订单号', dataIndex: 'orderOutSn',width:clientWidth * 0.13,align: 'center'},
		    { header: '11位sku码', dataIndex: 'skuSn', width:clientWidth * 0.15, align: 'center'},
		    { header: '外部商品编码', dataIndex: 'outSkuSn',width:clientWidth * 0.16,align: 'center'},
			{ header: '外部商品名称', dataIndex: 'outSkuName',width:clientWidth * 0.15,align: 'center'},
			{ header: '渠道类型码', dataIndex: 'channelType',width:clientWidth * 0.15,align: 'center'},
			{ header: '商品数量', dataIndex: 'goodsNum',width:clientWidth * 0.10,align: 'center'},
			{
				 text: '操作',width:clientWidth * 0.10,align: 'center',dataIndex: '' ,sortable:false,menuDisabled : true,
					renderer : function (value, metaData, record, rowIndex) {
						var id = Ext.id();
						setTimeout(function() {
							var panel = Ext.create('Ext.panel.Panel', {
								bodyPadding: 0,
								border:false,
								baseCls: 'my-panel-no-border',
								width: 90,
								height: 20,
								layout: 'column',
								columnWidth : 1,
								items: [{
									columnWidth: 1,
									xtype: 'segmentedbutton',
									allowToggle: false,
									items: [{
										text: '编辑',
										handler: function () {
											me.edit(record);
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
			store: 'MB.store.AbnormalSKUMaintainStore',
			dock: 'bottom',
			displayInfo: true
		}],
		me.viewConfig = {stripeRows:true,enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},
	edit:function(record){
		var win = Ext.widget("abnSKUMaintainEditWin");
		win.down("form").loadRecord(record);
		win.show();
	}
});

