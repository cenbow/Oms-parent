Ext.define("MB.view.common.ProductLibBrandCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commonproductlibbrandcombo',
	store: 'ProductLibBrands',
	name: 'brandCode',
	displayField: 'brandName',
	valueField: 'brandCode',
	queryMode: 'remote',
	width: '220',
	labelWidth: '70',
	fieldLabel: '商品品牌',
	hiddenName: 'brandCode',
	emptyText: '请选择商品品牌',
	editable: false
});