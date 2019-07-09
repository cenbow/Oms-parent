/**
 * 渠道类型
 */
Ext.define("MB.view.abnormalSKUMaintain.ChannelCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.channelCombo',
	store : 'MB.store.ChannelStore',
	name : 'channelType',
	hiddenName : 'channelType',
	displayField : 'shortText',
	valueField : 'channelCode',
	value : 'JD_CHANNEL_CODE',
	queryMode : 'remote',
	fieldLabel : '<b>渠道类型</b>',
	editable : false,
	emptyText : '请选择',
	initComponent : function() {
		this.callParent(arguments);
	}
});