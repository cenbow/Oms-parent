Ext.define("MB.view.handOrder.OnlineChannelShopCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.onlineChannelShopCombo',
	store : 'MB.store.OnlineChannelShopStore',
	name : 'channelCode',
	hiddenName : 'channelCode',
	displayField : 'channelName',
	valueField : 'channelCode',
	queryMode : 'remote',
	fieldLabel : '<b>店长店铺</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});