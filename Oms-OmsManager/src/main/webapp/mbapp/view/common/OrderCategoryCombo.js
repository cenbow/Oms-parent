Ext.define("MB.view.common.OrderCategoryCombo", {
	extend: "Ext.form.field.ComboBox",
	alias: 'widget.ordercategorycombo',
	store: 'OrderCategoryStore',
	name: 'orderCategory',
	displayField: 'n',
	valueField: 'v',
	queryMode: 'local',
	width: 220,
	labelWidth: 70,
	fieldLabel: '订单种类',
	hiddenName: 'orderCategory',
	emptyText: '请选择订单种类',
	editable: false,
	initComponent: function() {
		this.callParent(arguments);
	}
});