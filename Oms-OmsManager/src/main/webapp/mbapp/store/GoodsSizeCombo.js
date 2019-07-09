Ext.define("MB.view.common.GoodsSizeCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commongoodssizecombo',
	store: 'GoodsSizes',
	name: 'sizeCode',
	displayField: 'sizeName',
	valueField: 'sizeCode',
	queryMode: 'remote',
	width: '220',
	labelWidth: '70',
	fieldLabel: '商品尺码',
	hiddenName: 'sizeCode',
	emptyText: '请选择商品尺码',
	editable: false,
	triggerAction: 'all',
	hideMode: 'offsets',
	forceSelection:true,
	autoLoad :false
});