
/***角色列表**/
Ext.define('MB.view.systemRole.SystemRoleGridView', {
	extend : "Ext.grid.Panel",
	alias : 'widget.systemRoleGridView',
	store: "SystemRoleStore",
	autoRender:true,
	columnLines: true,
	width: '100%',
	loadMask: true, // 读取数据时的遮罩和提示功能即加载loding...
	frame: true,
	resizable: true,
	forceFit: true,
	height : '500',
	listeners:{		
		itemdblclick:function(dataview,record, item, index, e){
		   	this.roleDetails(index);
		}
	},
	initComponent : function() {		
		Ext.QuickTips.init();
		var me = this;
		me.columns = [	
		    { text:'序号', xtype: 'rownumberer', width:50 },
    		{ header: '角色编码', width: 80, dataIndex: 'roleCode' },
			{ header: '角色名称', width: 80, dataIndex: 'roleName' },
			{ header: '角色备注', width: 100, dataIndex: 'roleDesc' },	
			{
				text: '操作',
				width: 120,
				height : 20,
				dataIndex: '',
				renderer: function(value, metaData, record, rowIndex) {
					var id = Ext.id();
					var roleCode = record.get('roleCode');
					console.dir(record);
					setTimeout(function() {
						var panel = Ext.create('Ext.panel.Panel', {
							bodyPadding: 0,
							border:false,
							baseCls: 'my-panel-no-border',
							width: 300,
							height: 20,
							layout: 'column',
							columnWidth : 1,
							
							items: [{
								columnWidth: 1,
								xtype: 'segmentedbutton',
								allowToggle: false,
								items: [{
									text: '分派权限',
									handler: function () {
										me.userManagement(rowIndex);
									}
								},{
									text: '查询详情',
									handler: function () {
										me.roleDetails(rowIndex);
									}
								},{
									text: '删除角色',
									handler: function () {
										me.delRole(rowIndex);
									}
								}
	
								]
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
			store: 'SystemRoleStore',
			dock: 'bottom',
			displayInfo: true
		}],
		me.viewConfig = {stripeRows:true,enableTextSelection: true};// 设置单元格可复制
		me.callParent(arguments);
	},
	userManagement: function(rowIndex){ // 用户管理

		var record = this.getStore().getAt(rowIndex);
	
		var roleCode = record.get('roleCode');
		console.dir(roleCode);
		
		
		FormEditWin.showAddDirWin("popWins",basePath+"/custom/systemResource/enterAssigningPermissionsPage.spmvc?roleCode="+roleCode,
				"orderSettlement_winID","角色页面 ",1000,500);
	
	},
	roleDetails: function(rowIndex){ // 查询详情
		
		var record = this.getStore().getAt(rowIndex);
		   var roleCode = record.get("roleCode");
	
	    	var win = Ext.widget("systemRoleEdit");

	    	win.down("form").load({
		    	
	    		url:  basePath + 'custom/systemResource/getSystemOmsRoleByResourceCode.spmvc',
	    		params: {"roleCode": roleCode},
	    		success  : function (form, action) {
	    					// 数据加载成功后操作
	    		},
	    		failure : function(form, action) {
	    			alert("查询角色详情失败！")
	    			// 数据加载失败后操作
	    		}
	    	});
	    	win.show();
	},
	delRole: function(rowIndex){ 
		var record = this.getStore().getAt(rowIndex);
		var roleCode = record.get("roleCode");

		confirmMsg("确认","需要删除此记录吗?", function(btn) {
			if (btn == "yes") {
				Ext.Ajax.request( {
					waitMsg : '请稍等.....',
					url :  basePath + 'custom/systemResource/delSystemRole.spmvc',
					method : 'post',
					timeout:600000,
					params : {'roleCode':roleCode},
					success : function(response) {
						var respText = Ext.JSON.decode(response.responseText);
						if(respText.isok){
							Ext.msgBox.msg('删除资源', respText.message, Ext.MessageBox.INFO);
							Ext.widget("systemRoleGridView").store.reload();//刷新查询列表数据
							win.close();
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
	}
});

