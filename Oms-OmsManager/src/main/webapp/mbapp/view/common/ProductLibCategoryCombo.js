Ext.define("MB.view.common.ProductLibCategoryCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commonproductlibcategorycombo',
	store: 'ProductLibCategorys',
	name: 'catId',
	displayField: 'catName',
	valueField: 'catId',
	queryMode: 'remote',
	width: '220',
	labelWidth: '70',
	fieldLabel: '商品种类',
	hiddenName: 'catId',
	emptyText: '请选择商品种类',
	editable: false
});