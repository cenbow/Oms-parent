Ext.define("MB.view.systemResource.SystemResourceViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.systemResourceViewport',
	requires: ['MB.view.systemResource.SystemResourceGridView',
	           'MB.view.systemResource.SystemResourcePanlView',
	           'MB.view.systemResource.ResourceTypeCombo',
	           'MB.view.systemResource.IsShowType',
	           'MB.view.systemResource.SystemResourceEdit',
	           'MB.model.SystemResourceModel',
	           'MB.model.ComboModel',
	           'MB.store.SystemResourceStore'],
	items : [ {
		title : '系统资源查询',
		xtype: 'systemResourcePanlView',
		region : "north",
		height : clientHeight * 0.3
	},{
		xtype: 'systemResourceGridView',
		region : "south",
		height: clientHeight * 0.7
	}]
});