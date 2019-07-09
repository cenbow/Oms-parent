Ext.define("MB.view.common.ChannelShopCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.channelshopcombo',
	store : 'ChannelShopStore',
	name : 'shopCode',
	displayField : 'shopTitle',
	valueField : 'shopCode',
	queryMode : 'remote',
	width : 220,
	labelWidth : 70,
	fieldLabel : '渠道店铺',
	hiddenName : 'shopCode',
	emptyText : '请选择渠道店铺',
	editable : false,

	
	
	initComponent : function() {
		this.callParent(arguments);
	}
});