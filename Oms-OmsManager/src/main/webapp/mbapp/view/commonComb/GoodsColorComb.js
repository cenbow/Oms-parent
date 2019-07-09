Ext.define("MB.view.commonComb.GoodsColorComb", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.goodsColorComb',
	store : 'MB.store.GoodsColorCombStore',
	displayField : 'colorName',
	valueField : 'colorCode',
	queryMode : 'remote',
	fieldLabel : '<b>颜色</b>',
	emptyText : '请选择颜色',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});