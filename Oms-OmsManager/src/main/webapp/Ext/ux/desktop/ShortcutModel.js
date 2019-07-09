/*!
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

/**
 * @class Ext.ux.desktop.ShortcutModel
 * @extends Ext.data.Model
 * This model defines the minimal set of fields for desktop shortcuts.
 */
Ext.define('Ext.ux.desktop.ShortcutModel', {
    extend: 'Ext.data.Model',
    fields: [
       { name: 'name' }, // 名称
       { name: 'iconCls' }, // 图标
       { name: 'module' }, // 组件
       { name: 'url' }, // 连接地址
       { name: 'id' }, // 窗口id
       { name: 'flag' } // 窗口打开形式 1url窗口、2树形菜单、3
    ]
});
