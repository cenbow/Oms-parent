/*!
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('Desktop.App', {
    extend: 'Ext.ux.desktop.App',

    requires: [
        'Ext.window.MessageBox',

        'Ext.ux.desktop.ShortcutModel',
        /*'Desktop.UrlWindow',
        'Desktop.SystemStatus',
        'Desktop.VideoWindow',
        'Desktop.GridWindow',
        'Desktop.TabWindow',
        'Desktop.AccordionWindow',
        'Desktop.Notepad',
        'Desktop.BogusMenuModule',
        'Desktop.BogusModule',*/

//        'Desktop.Blockalanche',
        'Desktop.Settings'
    ],
    
    init: function() {
        // custom logic before getXYZ methods get called...

        this.callParent();

        // now ready...
    },
    // 获取组件 (在开始菜单窗口显示)
    getModules : function(){
    	/*return [
            //new Desktop.UrlWindow(),    
            //new Desktop.VideoWindow(),
            //new Desktop.Blockalanche(),
            //new Desktop.SystemStatus(),
            //new Desktop.GridWindow(),
            //new Desktop.TabWindow(),
            //new Desktop.AccordionWindow(),
            //new Desktop.Notepad(),
            new Desktop.BogusMenuModule(),
            new Desktop.BogusModule()
        ];*/
    	var me = this;
    	return me.createLauncher();
    },
    // 获取桌面菜单
    getDesktopMenu: function() {
    	 var me = this;
         var allmenu = me.getMenuData();
         var desktopMenu = [];
         
         for (var i = 0; i < allmenu.length; i++) {
        	 var tempData = allmenu[i];
        	 if (tempData.menu) {
        		 desktopMenu.push(tempData);
        	 }
         }
         
         return desktopMenu;
    },

    getDesktopConfig: function () {
        var me = this, ret = me.callParent();
        var desktopMenu = me.getDesktopMenu(); // 获取桌面菜单项目
        
        return Ext.apply(ret, {
            //cls: 'ux-desktop-black',

            contextMenuItems: [
                { text: '设置', handler: me.onSettings, scope: me }
            ],
            // 桌面窗口
            shortcuts: Ext.create('Ext.data.Store', {
                model: 'Ext.ux.desktop.ShortcutModel',
                data: desktopMenu
            }),

            wallpaper: Ext.RESOURCE_PATH_URL + 'resources/images/wallpapers/Blue-Sencha.jpg',
            wallpaperStretch: false
        });
    },

    // config for the start menu
    getStartConfig : function() {
        var me = this, ret = me.callParent();
        
        return Ext.apply(ret, {
            title: authUser.userName + ' [' + roles.toString() + ']',
            iconCls: 'user',
            height: 300,
            toolConfig: {
                width: 100,
                items: [
                    {
                        text:'设置',
                        iconCls:'settings',
                        handler: me.onSettings,
                        scope: me
                    },
                    '-',
                    {
                        text:'退出',
                        iconCls:'logout',
                        handler: me.onLogout,
                        scope: me
                    }
                ]
            }
        });
    },

    getTaskbarConfig: function () {
        var ret = this.callParent();

        return Ext.apply(ret, {
            quickStart: [
                //{ name: 'Accordion Window', iconCls: 'accordion', module: 'acc-win' },
                //{ name: 'Grid Window', iconCls: 'icon-grid', module: 'grid-win' }
            ],
            trayItems: [
                { xtype: 'trayclock', flex: 1 }
            ]
        });
    },
    
    // 获取传入id的子菜单
    getTreeMenu : function (datas) {
    	var me = this;
    	var treeAllMenu = me.getMenuData();
    	
    	var children = [];
    	for (var i = 0; i < treeAllMenu.length; i++) {
    		var tempData = treeAllMenu[i];
    		var tempId = tempData.id;
    		
    		if (datas.id != tempId) {
    			
    			var index = tempId.lastIndexOf("-");
    			
    			if (index > -1) {
    				var ids = tempId.substring(0,index);
    				
    				if (datas.id == ids) {
    					var node = me.getLauncher(tempData);
    					children.push(node);
    					
    					if (node.leaf == false) {
    						getTreeMenu(node);
    					}
    				}
    			}
    		}
    	}
    	
    	datas.menu = {items:children};
    	return datas;
    },
    
    getLauncher : function (obj) {
    	var me = this;
    	var launcher = {};
    	launcher.id = obj.id;
		launcher.text = obj.name;
		launcher.iconCls  = "bogus";
		launcher.leaf = obj.leaf;
		launcher.flag = obj.flag;
		launcher.url = obj.url;
		if (!obj.leaf) {
			/*launcher.handler = function() {
				return false;
			};*/
			launcher.handler = me.createWindow;
			launcher.scope = me;
			launcher = me.getTreeMenu(launcher);
		} else {
			// 打开方式
			launcher.handler = me.createWindow;
			launcher.scope = me;
			//launcher.windowId = obj.id;
		}
		
		return launcher;
    },
    
    createLauncher : function () {
    	var me = this;
    	// 获取桌面菜单
		var desktopMenu = me.getDesktopMenu();
    	var items = [];
    	
    	if (desktopMenu && desktopMenu.length > 0) {
    		
    		for(var i = 0, leng = desktopMenu.length; i < leng; i++) {
    			var temp = desktopMenu[i];
    			
    			var model = {};
    			model.launcher = me.getLauncher(temp);
    		
    			items.push(model);
    		}
    	}
    	return items;
    },

    // 退出函数
    onLogout: function () {
        Ext.Msg.confirm('退出', '您确定退出吗?', function (text) {
        	console.dir(text);
        	if (text == 'yes') {
        		window.location.href= basePath+'/custom/common/logout';
        	
        	}
        });
    },

    // 设置函数
    onSettings: function () {
        var dlg = new Desktop.Settings({
            desktop: this.desktop
        });
        dlg.show();
    } ,
    getMenuData : function() {
    	var data = menuData;
    	return data;
    }
});
