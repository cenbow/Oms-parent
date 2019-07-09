/**
 * 处理状态 0:未审核; 1:已审核; 2:已打单
 */
Ext.define("MB.view.handOrder.ProcessStatusCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.processStatusCombo',
	store : 'MB.store.HandOrderProStatusStore',
	name : 'processStatus',
	hiddenName : 'processStatus',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>处理状态</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});