Ext.define('MB.view.systemResource.SystemResourceGridView', {
	extend : "Ext.grid.Panel",
	alias : 'widget.systemResourceGridView',
	store: "SystemResourceStore",
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
		   	this.resourceDetails(index);
		}
	},
	initComponent : function() {
		Ext.QuickTips.init();
		var me = this;
		
		me.columns = [
		    { text:'序号', xtype: 'rownumberer', width:50 },
			{ header: '资源id', width: 100, hidden: true, dataIndex: 'resourceId' },
			{ header: '父类id', width: 100, dataIndex: 'parentId' },
			{ header: '父类编码', width: 100, dataIndex: 'parentCode' },
			{ header: '资源类型', width: 100, dataIndex: 'resourceType' },
			{ header: '渠道类型', width: 100, dataIndex: 'channelType',hidden: true  },
			{ header: 'ischannel', width: 100, dataIndex: 'isChannel',hidden: true },
			{ header: '资源编码', width: 200, dataIndex: 'resourceCode', align: 'center'},
			{ header: '资源名称', width: 200, dataIndex: 'resourceName', align: 'center' },
			{ header: '资源url', width: 260, dataIndex: 'resourceUrl' },
			{ header: '排序', width: 70, dataIndex: 'sortOrder' },
			{ header: '根id', width: 70, dataIndex: 'rootId' },
			{ header: '是否显示', width: 100, dataIndex: 'isShow',
				renderer : function (v, md, r) {
						if (v == 1) {
							return '是';
						} else {
							return '否';
						}
					}
			},
			{ text: '状态',width: 300,align: 'center',dataIndex: '' ,sortable:false,menuDisabled : true,
				renderer : function (value, metaData, record, rowIndex) {
					
					var id = Ext.id();
					var resourceId = record.get('id');
					console.dir(record);
					setTimeout(function() {
						var panel = Ext.create('Ext.panel.Panel', {
							bodyPadding: 0,
							border:false,
							baseCls: 'my-panel-no-border',
							width: 200,
							height: 20,
							layout: 'column',
							columnWidth : 1,
							items: [{
								columnWidth: 1,
								xtype: 'segmentedbutton',
								allowToggle: false,
								items: [{
									text: '删除角色',
									handler: function () {
										me.delResource(rowIndex);
									}
								},{
						
									text: '查询详情',
									handler: function () {
										me.resourceDetails(rowIndex);
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
			store: 'SystemResourceStore',
			dock: 'bottom',
			displayInfo: true
		}],
		me.viewConfig = {stripeRows:true,enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},
	
	delResource: function(rowIndex){ 
		
		var record = this.getStore().getAt(rowIndex);
		var resourceId = record.get("resourceId");

		confirmMsg("确认","需要删除此记录吗?", function(btn) {
			if (btn == "yes") {
				Ext.Ajax.request( {
					waitMsg : '请稍等.....',
					url :  basePath + 'custom/systemResource/deleteSystemResourceAtExt5.spmvc',
					method : 'post',
					timeout:600000,
					params : {'resourceId':resourceId},
					success : function(response) {
						var respText = Ext.JSON.decode(response.responseText);
						if(respText.isok){
							Ext.msgBox.msg('删除资源', respText.message, Ext.MessageBox.INFO);
							Ext.widget("systemResourceGridView").store.reload();//刷新查询列表							
						}else{
							Ext.msgBox.remainMsg('删除资源', respText.message, Ext.MessageBox.ERROR);
						}
					},
					failure : function(response) {
						Ext.msgBox.remainMsg('删除资源', "操作异常", Ext.MessageBox.ERROR);
					}
				});
			}else {
				return ;
			}
		});
	},
	resourceDetails: function(rowIndex){ 
		var record = this.getStore().getAt(rowIndex);
		   var resourceId = record.get("resourceId");
		
		   var win = Ext.widget("systemResourceEdit");
		
			win.down("form").load({
				url:  basePath + 'custom/systemResource/getSystemOmsResourceByResourceId.spmvc',
				params: {"resourceId": resourceId},
				success  : function (form, action) {
					
				},
				failure : function(form, action) {
					// 数据加载失败后操作
				}
			});
			win.show();
	}
	
});

