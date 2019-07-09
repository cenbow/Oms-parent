Ext.define("MB.view.commonComb.GoodsSizeComb", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.goodsSizeComb',
	store : 'MB.store.GoodsSizeCombStore',
	displayField : 'sizeName',
	valueField : 'sizeCode',
	queryMode : 'remote',
	fieldLabel : '<b>尺码</b>',
	emptyText : '请选择尺码',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});