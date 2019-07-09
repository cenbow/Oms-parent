/**
 * 查询面板：同步状态
 */
Ext.define("MB.view.bfbRefundSettlement.IsSyncCombo", {
	extend : "Ext.form.field.ComboBox",
	alias : 'widget.isSyncCombo',
	store : 'MB.store.IsSyncStore',
	name : 'isSync',
	hiddenName : 'isSync',
	displayField : 'n',
	valueField : 'v',
	queryMode : 'local',
	fieldLabel : '<b>同步状态</b>',
	emptyText : '请选择',
	editable : false,
	initComponent : function() {
		this.callParent(arguments);
	}
});