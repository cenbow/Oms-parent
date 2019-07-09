/*
 * This file is generated and updated by Sencha Cmd. You can edit this file as
 * needed for your application, but these edits will have to be merged by
 * Sencha Cmd when upgrading.
 */
Ext.application({
    name: 'Desktop',

    //-------------------------------------------------------------------------
    // Most customizations should be made to Desktop.Application. If you need to
    // customize this file, doing so below this section reduces the likelihood
    // of merge conflicts when upgrading to new versions of Sencha Cmd.
    //-------------------------------------------------------------------------
    appFolder: 'Ext/desktop/app', // Desktop的目录
    requires: [
        'Desktop.App'
    ],
    init: function() {
        var app = new Desktop.App();
        
        Ext.RESOURCE_PATH_URL = "Ext/desktop/app/";
        Ext.context = path;
    }
});
