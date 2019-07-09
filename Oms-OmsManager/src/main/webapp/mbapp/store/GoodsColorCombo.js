Ext.define("MB.view.common.GoodsColorCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commongoodscolorcombo',
	store: 'GoodsColors',
	name: 'colorCode',
	displayField: 'colorName',
	valueField: 'colorCode',
	queryMode: 'remote',
	width: '220',
	labelWidth: '70',
	fieldLabel: '商品颜色',
	hiddenName: 'colorCode',
	emptyText: '请选择商品颜色',
	editable: false
});