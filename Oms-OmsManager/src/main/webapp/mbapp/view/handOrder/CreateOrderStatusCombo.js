/**
 * 创建订单状态 0:未处理; 1:全部成功; 2:部分成功; 3:全部失败
 */
Ext.define("MB.view.handOrder.CreateOrderStatusCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.createOrderStatusCombo',
	store : 'MB.store.CreateOrderStatusStore',
	name : 'createOrderStatus',
	hiddenName : 'createOrderStatus',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>创建订单状态</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});