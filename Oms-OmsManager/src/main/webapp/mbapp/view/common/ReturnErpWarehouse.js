Ext.define("MB.view.common.ReturnErpWarehouse", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.commonreturnerpwarehouse',
	store: 'ReturnErpWarehouseStore',
	name: 'warehouseCode',
	displayField: 'warehouseName',
	valueField: 'warehouseCode',
	queryMode: 'local',
	width: '250',
	labelWidth: '70',
	fieldLabel: '退货仓库',
	hiddenName: 'warehouseCode',
	emptyText: '请选择退货仓库',
	editable: false
});