Ext.define("MB.view.common.WarehouseListOptionCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.warehouseListOptionCombo',
	store: 'WarehouseListSourceStore',
	name: 'warehouseCode',
	displayField: 'warehouseName',
	valueField: 'warehouseCode',
	queryMode: 'remote',
	width: 220,
	labelWidth: 70,
	fieldLabel: '退货入库仓',
	hiddenName: 'warehouseCode',
	emptyText: '请选择仓库',
	editable: false,
	initComponent: function() {
		this.callParent(arguments);
	}
});