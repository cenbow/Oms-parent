/**
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 * @class Ext.ux.desktop.App
 */
Ext.define('Ext.ux.desktop.App', {
    mixins: {
        observable: 'Ext.util.Observable'
    },

    requires: [
        'Ext.container.Viewport',

        'Ext.ux.desktop.Desktop'
    ],

    isReady: false,
    modules: null,
    useQuickTips: true,

    constructor: function (config) {
        var me = this;

        me.mixins.observable.constructor.call(this, config);

        if (Ext.isReady) {
            Ext.Function.defer(me.init, 10, me);
        } else {
            Ext.onReady(me.init, me);
        }
    },

    init: function() {
        var me = this, desktopCfg;

        if (me.useQuickTips) {
            Ext.QuickTips.init();
        }

        me.modules = me.getModules();
        if (me.modules) {
            me.initModules(me.modules);
        }

        desktopCfg = me.getDesktopConfig();
        me.desktop = new Ext.ux.desktop.Desktop(desktopCfg);

        me.viewport = new Ext.container.Viewport({
            layout: 'fit',
            items: [ me.desktop ]
        });

        Ext.getWin().on('beforeunload', me.onUnload, me);

        me.isReady = true;
        me.fireEvent('ready', me);
    },

    /**
     * This method returns the configuration object for the Desktop object. A derived
     * class can override this method, call the base version to build the config and
     * then modify the returned object before returning it.
     */
    getDesktopConfig: function () {
        var me = this, cfg = {
            app: me,
            taskbarConfig: me.getTaskbarConfig()
        };

        Ext.apply(cfg, me.desktopConfig);
        return cfg;
    },

    getModules: Ext.emptyFn,

    /**
     * This method returns the configuration object for the Start Button. A derived
     * class can override this method, call the base version to build the config and
     * then modify the returned object before returning it.
     */
    getStartConfig: function () {
        var me = this,
            cfg = {
                app: me,
                menu: []
            },
            launcher;

        Ext.apply(cfg, me.startConfig);

        Ext.each(me.modules, function (module) {
            launcher = module.launcher;
            if (launcher) {
            	launcher.handler = launcher.handler || Ext.bind(me.createWindow, me, [module]);
                cfg.menu.push(module.launcher);
            }
        });

        return cfg;
    },
    // 创建window
    createWindow: function(module) {
        this.onShortcutItemClick(module);
    },
    // 点击
    onShortcutItemClick: function (record) {
    	var me = this; 
        var flag = record.flag;
        if (flag == 1 || flag == 3) {
        	// url打开
        	win = me.onShowUrl(record);
        } else if (flag == 2) {
        	// treeplane打开
        	record.url = "Ext/desktop/treePanel.jsp?id=" + record.id;
        	win = me.onShowUrl(record);
        } else {
        	module = me.app.getModule(record.data.module);
         	win = module && module.createWindow();
        }
        
        if (win) {
        	win.show();
        }
    },
    // 用iframe打开url
    onShowUrl : function (record) {
    	var me = this; 
    	var id = "win-" + record.id;
        var desktop = me.getDesktop();
        var win = desktop.getWindow(id);
        if(!win){
        	var w = Ext.getBody().getWidth();
        	var h = Ext.getBody().getHeight() - 30;
        	
        	var title = record.text;
        	var url = record.url;
        	if (url.indexOf("http://") == -1) {
        		// 应用内的地址请求
        		url = Ext.context + "/" + url;
        	}
        	
            win = desktop.createWindow({
            	id:id,
           		title:title,
           		width:w,
             	height:h,
             	iconCls: 'tabs',
          		shim:true,
          		modal : true,
          		maximized:true, // 默认最大化
          		maximizable: false, // 最大化按钮
          		minimizable: true, // 最小化按钮
             	animCollapse:false,
             	expandOnShow:true,
             	resizable:false,
              	border:false,
              	closeAction:'close', 
            	constrainHeader:true,
           		layout: 'fit',
              	html:'<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="iframe-'+id+'" src="'+url+'" width="100%" height="100%"></iframe>'
            });
        }
        return win;
    },

    /**
     * This method returns the configuration object for the TaskBar. A derived class
     * can override this method, call the base version to build the config and then
     * modify the returned object before returning it.
     */
    getTaskbarConfig: function () {
        var me = this, cfg = {
            app: me,
            startConfig: me.getStartConfig()
        };

        Ext.apply(cfg, me.taskbarConfig);
        return cfg;
    },
    // 初始化组件
    initModules : function(modules) {
        var me = this;
        Ext.each(modules, function (module) {
            module.app = me;
        });
    },
    // 获取组件
    getModule : function(name) {
    	var ms = this.modules;
        for (var i = 0, len = ms.length; i < len; i++) {
            var m = ms[i];
            if (m.id == name || m.appType == name) {
                return m;
            }
        }
        return null;
    },

    onReady : function(fn, scope) {
        if (this.isReady) {
            fn.call(scope, this);
        } else {
            this.on({
                ready: fn,
                scope: scope,
                single: true
            });
        }
    },

    getDesktop : function() {
        return this.desktop;
    },

    onUnload : function(e) {
        if (this.fireEvent('beforeunload', this) === false) {
            e.stopEvent();
        }
    }
});
