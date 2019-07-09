Ext.define("MB.view.orderEdit.SupplierCodeComb", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.supplierCodeComb',
	store : 'MB.store.SupplierCodeCombStore',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'remote',
	emptyText : '全部',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});