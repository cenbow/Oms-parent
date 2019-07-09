Ext.define("MB.view.common.SearchGoodsCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commonsearchgoodscombo',
	store: 'MB.store.SearchGoodss',
	name: 'searchgoodsSn',
	displayField: 'goodsName',
	valueField: 'goodsSn',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
	fieldLabel: '检索商品',
	hiddenName: 'searchgoodsSn',
	emptyText: '请选择检索商品',
	editable: false
});