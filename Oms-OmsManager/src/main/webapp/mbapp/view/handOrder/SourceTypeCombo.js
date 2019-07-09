/**
 * 打单类型 4:首购赠品订单;5:一般赠品订单
 */
Ext.define("MB.view.handOrder.SourceTypeCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.sourceTypeCombo',
	store : 'MB.store.HandOrderSourTypeStore',
	name : 'sourceType',
	hiddenName : 'sourceType',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>打单类型</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});