Ext.define("MB.view.common.ChannelTypeCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.channeltypecombo',
	store: 'ChannelTypeStore',
	name: 'channelType',
	displayField: 'n',
	valueField: 'v',
	queryMode: 'local',
	width: 220,
	labelWidth: 70,
	fieldLabel: '渠道类型',
	hiddenName: 'channelType',
	emptyText: '请选择渠道类型',
	editable: false,
	initComponent: function() {
		this.callParent(arguments);
	}
});