/*!
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
// 以url打开窗口
Ext.define('Desktop.UrlWindow', {
    extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.data.ArrayStore',
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'url-win-001',

    init : function(){
        this.launcher = {
            text: '测试URL窗口',
            iconCls:'icon-grid'
        };
    },

    createWindow : function(){
        var desktop = this.app.getDesktop();
        var win = desktop.getWindow('url-win-001');
        if(!win){
            win = desktop.createWindow({
            	id:"url-win-001",
           		title:"测试URL窗口",
           		width:740,
             	height:480,
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
              	html:'<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="iframe-url-win-001" src="http://www.baidu.com" width="100%" height="100%"></iframe>'
            });
        }
        return win;
    }
});

