/**
 * 异常SKU维护：视图面板
 */
Ext.define("MB.view.abnormalSKUMaintain.AbnormalSKUMaintainViewport", {
	extend : "Ext.container.Viewport",
	alias : 'widget.abnormalSKUMaintainViewport',
	requires: [
	           'MB.view.abnormalSKUMaintain.AbnormalSKUMaintainPanlView',
	           'MB.view.abnormalSKUMaintain.AbnormalSKUMaintainGridView',
	           'MB.model.ComboModel',
	           'MB.store.ChannelStore',
	           'MB.view.abnormalSKUMaintain.ChannelCombo',
	           'MB.store.AbnormalSKUMaintainStore',
	           'MB.model.AbnormalSKUMaintainModel',
	           'MB.view.abnormalSKUMaintain.AbnSKUMaintainEditWin'],
	items : [ {
		title : '异常SKU调整',
		xtype: 'abnormalSKUMaintainPanlView',
		region : "north",
		height : document.body.clientHeight * 0.3
	},{
		xtype: 'abnormalSKUMaintainGridView',
		region : "south",
		height: document.body.clientHeight * 0.7
	}]
});