Ext.define("MB.view.orderEdit.OrderSnComb", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.orderSnComb',
	store : 'MB.store.OrderSnCombStore',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'remote',
	emptyText : '全部',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});