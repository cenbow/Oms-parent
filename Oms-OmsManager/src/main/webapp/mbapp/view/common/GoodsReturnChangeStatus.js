Ext.define("MB.view.common.GoodsReturnChangeStatus", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.goodsReturnChangeStatus',
	store: 'GoodsReturnChangeStatusStore',
	name: 'id',
	displayField: 'name',
	valueField: 'id',
	queryMode: 'local',
	width: '220',
	labelWidth: '70',
	fieldLabel: '处理状态',
	hiddenName: 'id',
	editable: false
});