Ext.define("MB.view.common.ChannelInfoCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.channelinfocombo',
	store : 'ChannelInfoStore',
	name : 'chanelCode',
	displayField : 'channelTitle',
	valueField : 'chanelCode',
	queryMode : 'remote',
	width : 220,
	labelWidth : 70,
	fieldLabel : '平台站点',
	hiddenName : 'chanelCode',
	emptyText : '请选择平台站点',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});